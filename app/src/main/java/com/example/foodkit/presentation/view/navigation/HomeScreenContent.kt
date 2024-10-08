package com.example.foodkit.presentation.view.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.view.HomeTopAppBar
import com.example.foodkit.presentation.view.ProductDetailsScreen
import com.example.foodkit.repository.Food


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent() {
    val context = LocalContext.current
    val errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(colorResource(id = R.color.secondaryColor))

        ) {
            TopAppBar(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize(),
                title = { HomeTopAppBar() }
                )

            SearchBar()

            BannerSection()
            Spacer(modifier = Modifier.height(16.dp))
            CategoriesSection()
            Spacer(modifier = Modifier.height(16.dp))
            ProductSection()

        }

    }


}


@Composable
fun SearchBar() {
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
            onValueChange = { search = it },
            placeholder = { Text("Search", color = Color.Black) },
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
fun BannerSection() {
    val pagerState = com.google.accompanist.pager.rememberPagerState()
    val banners = listOf(
        R.drawable.banner_photo,
        R.drawable.onboarding_photo2,
        R.drawable.onboarding_photo3,
        R.drawable.onboarding_photo4,

        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (banners.isNotEmpty()) {
            com.google.accompanist.pager.HorizontalPager(
                count = banners.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),

                    ) {
                    Image(
                        painter = painterResource(id = banners[page]),
                        contentDescription = "Banner Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DotsIndicator(
                totalDots = banners.size,
                selectedIndex = pagerState.currentPage
            )
        } else {
            Text("No banners available")
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
fun CategoriesSection() {


    data class Category(
        val name: String,
        val image: Int,
    )

    val allCategories = listOf(
        Category("Food", R.drawable.onboarding_photo1),
        Category("Drinks", R.drawable.onboarding_photo2),
        Category("Desserts", R.drawable.onboarding_photo3),
        Category("Meet", R.drawable.onboarding_photo3),
        Category("Choose", R.drawable.onboarding_photo3),
        Category("Boos", R.drawable.onboarding_photo3),
        Category("Done", R.drawable.onboarding_photo3),
        Category("Well", R.drawable.onboarding_photo3),
        Category("Yaa", R.drawable.onboarding_photo3),
        Category("Moon", R.drawable.onboarding_photo3)
    )

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
                modifier = Modifier,
                fontWeight = FontWeight.Bold

            )

            Text(
                text = stringResource(id = R.string.viewAll),
                color = colorResource(id = R.color.appColor),
                modifier = Modifier,
                fontWeight = FontWeight.Medium
            )

        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(allCategories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .size(75.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp)


                    ) {
                        Image(
                            painter = painterResource(id = category.image),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = category.name,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun ProductSection() {
    val navController = rememberNavController()
    

    data class Product(
        val name: String,
        val image: Int,
        var price: Double = 0.0,
        var isFavorite: Boolean = false,
        var inCart: Boolean = false,

        )

    val allProducts = listOf(
        Product("Food Good", R.drawable.food_photo, 49.99, false, false),
        Product("Drinks", R.drawable.onboarding_photo2, 55.90, false, false),
        Product("Desserts", R.drawable.onboarding_photo3, 99.90, false, false),
        Product("Berger", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Rab", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Meet", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Choose", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Boos", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Done", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Well", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Yaa", R.drawable.onboarding_photo3, 20.0, false, false),
        Product("Moon", R.drawable.onboarding_photo3, 20.0, false, false)
    )


    Column(modifier = Modifier.height(1000.dp)) {

        Text(
            text = stringResource(id = R.string.product),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            fontWeight = FontWeight.Bold
        )

        Box(modifier = Modifier.fillMaxHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allProducts) { product ->
                    Card(
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { },
                    ) {

                        Column(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                                Image(
                                    painter = painterResource(id = product.image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(150.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { navController.navigate(Routes.PRODUCT_DETAILS) },
                                    contentScale = ContentScale.Fit


                                )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Price
                                Text(
                                    text = "$${product.price}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 16.sp,
//                                    modifier = Modifier.padding(top = 4.dp)
                                )


                                // Favorite icon
                                IconButton(
                                    onClick = { product.isFavorite = !product.isFavorite },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        painter = if (product.isFavorite) painterResource(id = R.drawable.favorite_se) else
                                            painterResource(id = R.drawable.favorite_un),
                                        tint = Color.Red,
                                        contentDescription = "Favorite"
                                    )
                                }
                            }

                            // Product name
                            Text(
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 14.sp
                            )

                            // Seller and rating
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "5.0",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}

/*
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
