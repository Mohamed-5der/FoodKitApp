package com.example.foodkit.presentation.view.userBottomNavigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.foundation.layout.width
import com.example.foodkit.R
import com.example.foodkit.components.BannerCard
import com.example.foodkit.components.CategoryCard
import com.example.foodkit.components.FoodCard
import com.example.foodkit.components.poppins
import com.example.foodkit.components.LottieAnimationLoading
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.FavoriteFoodViewModel
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.presentation.viewModel.UserViewModel
import com.example.foodkit.repository.Category
import com.example.foodkit.repository.Food
import com.example.foodkit.utils.getLocationAndAddress
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    navController: NavController,
    foodViewModel: FoodListScreenViewModel = koinViewModel(),
    categoryViewModel: CategoryViewModel = koinViewModel(),
) {
    val userViewModel: UserViewModel = koinViewModel()
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    userViewModel.getUserByEmail(email)
    val user = userViewModel.user.collectAsState().value
    var isSearchMode by remember { mutableStateOf(false) }
    var filteredFoods by remember { mutableStateOf<List<Food>>(emptyList()) }
    var filteredCategories by remember { mutableStateOf<List<Category>>(emptyList()) }
    val foods = categoryViewModel.foodsInCategory.collectAsState().value
    val isLoadingFood = foodViewModel.isLoading.collectAsState().value
    val isLoadingCategory = categoryViewModel.isLoading.collectAsState().value
    var city by remember { mutableStateOf<String?>(null) }
    var country by remember { mutableStateOf<String?>(null) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box (){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                HomeTopAppBar(user?.name?:"User Name")

            SearchBar(onSearch = { query ->
                isSearchMode = query.isNotEmpty()
                filteredFoods = if (query.isNotEmpty()) {
                    foodViewModel.foods.value.filter { it.name.contains(query, ignoreCase = true) }
                } else {
                    emptyList()
                }
                filteredCategories = if (query.isNotEmpty()) {
                    categoryViewModel.categories.value.filter {
                        it.name.contains(
                            query,
                            ignoreCase = true
                        )
                    }
                } else {
                    emptyList()
                }
            })
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)

                ) {
                    if (!isSearchMode) {
                        BannerSection(navController)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSearchMode) {
                        // Show filtered categories
                        CategoriesSection(categories = filteredCategories)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Show filtered products
                        ProductSection(navController, foods = filteredFoods)
                    } else {
                        // Show all categories and products
                        CategoriesSection()
                        Spacer(modifier = Modifier.height(16.dp))
                        ProductSection(navController, foods = foods)
                    }

                }
            }
            if (isLoadingFood || isLoadingCategory) {
                LottieAnimationLoading()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(name: String) {
    val context = LocalContext.current
    val city = remember { mutableStateOf<String?>(null) }
    val country = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        val result = getLocationAndAddress(context)
        result?.let { (_, address) ->
            city.value = address.first
            country.value = address.second
            Log.d("LocationViewModel", "City: ${city.value}, Country: ${country.value}")
        } ?: Log.e("LocationViewModel", "No result from getLocationAndAddress")
    }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.white),
        ),
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.hi) + " " + name,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {},
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "Location Icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text =   "${city.value}, ${country.value}",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable {

                        }
                    )

                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_icon),
                    contentDescription = "Notification Icon",
                    tint = Color.Black
                )
            }
        },
    )
}


@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var search by remember { mutableStateOf("") }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), shape = RoundedCornerShape(8.dp), shadowElevation = 8.dp
    ) {


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White)
                .background(Color.White),
            value = search,
            onValueChange = {
                search = it
                onSearch(search)
            },
            placeholder = {
                Text(
                    stringResource(id = R.string.search), color = Color.Black, fontFamily = poppins,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_un),
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter_icon),
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            shape = RectangleShape, // Removes any border rounding
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent, // No border when unfocused
                focusedBorderColor = Color.Transparent    // No border when focused
            )

        )
    }


}


@Composable
fun BannerSection(
    navController: NavController,
    viewModel: FoodListScreenViewModel = koinViewModel(),
) {
    val topFiveFoods by viewModel.topFiveFoods.collectAsState(initial = emptyList())

    val pagerState = rememberPagerState()

    LaunchedEffect(Unit) {
        viewModel.fetchTopFiveFoods()
    }


    Column(
        modifier = Modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.top_foods),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppins,
            modifier = Modifier
                .align(Alignment.Start),
            )


        if (topFiveFoods.isNotEmpty()) {
            HorizontalPager(
                count = topFiveFoods.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                val food = topFiveFoods[page]
                BannerCard(
                    food = food,
                    onClick = {
                        navController.navigate("food_details/${food.id}")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            DotsIndicator(
                totalDots = topFiveFoods.size,
                selectedIndex = pagerState.currentPage
            )
        } else {
            Text("Loading top foods...")
        }
    }
}


@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalDots) { index ->
            val color =
                if (index == selectedIndex) colorResource(id = R.color.appColor) else Color.Black

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(16.dp)
            )
        }
    }
}


@Composable
fun CategoriesSection(
    categories: List<Category> = emptyList(),
    viewModel: CategoryViewModel = koinViewModel(),
) {

    // Load categories only if categories list is empty
    if (categories.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadCategories()
        }
    }
    val categoriesState = viewModel.categories.collectAsState().value

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.findByCategory),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppins,

                )

            Text(
                text = stringResource(id = R.string.viewAll),
                color = colorResource(id = R.color.appColor),
                modifier = Modifier,
                fontWeight = FontWeight.Medium,
                fontFamily = poppins,

                )

        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(categoriesState) { category ->
                CategoryCard(category, onClick = {
                    viewModel.loadFoodsByCategory(categoryId =category.name)
                })
            }
        }
    }
}


@Composable
fun ProductSection(
    navController: NavController,
    foods: List<Food> = emptyList(),
    foodViewModel: FoodListScreenViewModel = koinViewModel(),
) {


    val foodList =
        if (foods.isNotEmpty()) foods else foodViewModel.foods.collectAsState(initial = emptyList()).value
    LaunchedEffect(Unit) {
        if (foods.isEmpty()) {
            foodViewModel.loadAllFoods()
        }
    }
    val favoriteViewModel: FavoriteFoodViewModel = koinViewModel()
    favoriteViewModel.getFavoriteFoods()
    val favoriteIds = favoriteViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current



    Column(modifier = Modifier.height(1000.dp)) {

        Text(
            text = stringResource(id = R.string.foods),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppins,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
        )

        Box(modifier = Modifier.fillMaxHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),

                modifier = Modifier.fillMaxSize()
            ) {
                items(foodList) { food ->
                    val isFavorite =
                        remember { mutableStateOf(favoriteIds.value.contains(food.id ?: "")) }
                    FoodCard(food, isFavorite = isFavorite, onClick = {
                        navController.navigate("food_details/${food.id}")
                    }, onClickFavorite = {
                        if (isFavorite.value) {
                            isFavorite.value = !isFavorite.value
                            favoriteViewModel.deleteFavoriteFood(food.id) {
                                Toast
                                    .makeText(context, "Removed from favorites", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            isFavorite.value = !isFavorite.value
                            favoriteViewModel.addFavoriteFood(
                                name = food.name,
                                imageUrl = food.imageUrl, price = food.price,
                                description = food.description,
                                rating = food.rating.toFloat(),
                                category = "",
                                numberRating = food.rating.toDouble(),
                                idFood = food.id
                            ) {
                                Toast
                                    .makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    )
                }
            }
        }
    }
}


/*
//Khedr Categories
@Composable
fun GetCategories(onCategorySelected: (String) -> Unit) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.findByCategory),
                modifier = Modifier.align(Alignment.TopStart),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,

                )
            )
            Text(
                text = stringResource(id = R.string.viewAll),
                modifier = Modifier.align(Alignment.TopEnd),
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(modifier = Modifier) {
            items(categories?.data?.categories ?: emptyList()) {
                CategoryCard(it, onCategorySelected)
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, onCategorySelected: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Card(
            shape = CircleShape,
            border = if (id.value == category.id) BorderStroke(
                1.dp,
                colorResource(id = R.color.appColor)
            ) else BorderStroke(0.dp, colorResource(id = R.color.black)),
            modifier = Modifier
                .size(80.dp)

        ) {
            Card(
                shape = CircleShape,
                border = if (id.value == category.id) BorderStroke(
                    3.dp,
                    colorResource(id = R.color.black)
                )
                else BorderStroke(3.dp, Color.LightGray),
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        onCategorySelected(category.id.toString())
                    }
            ) {
                AsyncImage(
                    model = category.image,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Text(
            text = category.name ?: "",
            modifier = Modifier.padding(top = 8.dp)
        )
    }

}


 */
