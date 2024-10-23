package com.example.foodkit.presentation.view.userBottomNavigation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.components.CartFoodCard
import com.example.foodkit.components.FoodCard
import com.example.foodkit.components.LottieAnimationEmpty
import com.example.foodkit.components.poppins
import com.example.foodkit.model.FavoriteFood
import com.example.foodkit.presentation.viewModel.FavoriteFoodViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreenContent(navController: NavController) {
    val favoriteViewModel: FavoriteFoodViewModel = koinViewModel()
    favoriteViewModel.getFavoriteFoods()
    val favoriteFoods = favoriteViewModel.favoriteFoods.collectAsState(initial = emptyList())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.white))

        ) {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(colorResource(id = R.color.white)),
                title = { Text(text = stringResource(id = R.string.wish_list), modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp), textAlign = TextAlign.Center)},
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.white),
                    titleContentColor = colorResource(id = R.color._black),
                    navigationIconContentColor = colorResource(id = R.color._black)
                ),
                scrollBehavior = null,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxHeight()) {
                if (favoriteFoods.value==emptyList<FavoriteFood>()) {
                    Image(
                        painter = painterResource(id = R.drawable.no_faouritee),
                        contentDescription = "Empty Cart",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }else{
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                    ) {
                        items(favoriteFoods.value?: emptyList()) { food ->

                            FoodCardFav(food = food, onClick = {
                                navController.navigate("food_details/${food.idFood}")
                            }
                            )
                        }
                    }
                }

            }

        }
    }

}

@Composable
fun FoodCardFav(food: FavoriteFood, onClick: () -> Unit) {

    val favoriteViewModel: FavoriteFoodViewModel = koinViewModel()
    val favoriteIds = favoriteViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
            .wrapContentHeight()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Card(
                    colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    AsyncImage(
                        model = food.imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.food_photo),
                        error = painterResource(id = R.drawable.food_photo)
                    )
                }

                if (false) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .height(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "10%",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.white),
                            fontFamily = poppins,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (food.price == food.price) {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(id = R.color.black),
                            fontFamily = poppins,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            fontFamily = poppins,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color._black),
                            fontFamily = poppins,
                        )
                    }
                }
                val isFavorite =
                    remember { mutableStateOf(favoriteIds.value.contains(food.id ?: "")) }

                Icon(
                    painterResource(
                        id = if (isFavorite.value) R.drawable.favorite_se else R.drawable.favorite_se
                    ),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clickable {
                            isFavorite.value = !isFavorite.value
                            favoriteViewModel.deleteFavoriteFood(food.idFood) {
                                Toast
                                    .makeText(context, "Removed from favorites", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        },
                    tint = colorResource(id = R.color.appColor)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = food.name ?: "",
                fontSize = 14.sp,
                minLines = 1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color._black),
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = food.description ?: "",
                fontSize = 14.sp,
                minLines = 1,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color._black),
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val brand = "Molo".split(" ")?.getOrNull(0) ?: "Unknown"
                Text(
                    text = "From: $brand",
                    fontSize = 14.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = food.rating.toString(),
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}