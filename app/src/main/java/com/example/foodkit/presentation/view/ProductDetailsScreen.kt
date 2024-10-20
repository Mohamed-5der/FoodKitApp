package com.example.foodkit.presentation.view

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.presentation.viewModel.CartForTestViewModel
import com.example.foodkit.presentation.viewModel.FavoriteFoodViewModel
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.random.Random


@Composable
fun ProductDetailsScreen( foodId: String,navController: NavController) {
    val userId=FirebaseAuth.getInstance().currentUser?.uid.toString()
    val viewModel: FoodDetailViewModel = koinViewModel( parameters = { parametersOf(userId) } )
    val cartViewModel :CartForTestViewModel =koinViewModel()
    val favoriteViewModel: FavoriteFoodViewModel = koinViewModel()
    favoriteViewModel.getFavoriteFoods()
    val favoriteIds = favoriteViewModel.favoriteIds.collectAsState()

    val context = LocalContext.current
    val numberItem= remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        viewModel.loadFood(foodId)
        viewModel.loadUserRating(foodId, userId)
    }
    val rating = viewModel.userRating

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        viewModel.food?.let { food ->
            Box {
                //Food Image
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
                        .verticalScroll(rememberScrollState()),
                ) {
                    Box(modifier = Modifier.height(450.dp)){
                        AsyncImage(
                                model = food.imageUrl,
                                contentDescription = "Food Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .clip(RoundedCornerShape(0.dp)),
                                contentScale = ContentScale.Crop
                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Card(modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .height(92.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = CardDefaults.cardElevation(2.dp),
                            ){

                                Column(modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = food.name,
                                            fontSize = 14.sp,
                                            fontFamily = poppins,
                                            maxLines = 1,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            text = food.price.toString()+"£",
                                            fontSize = 20.sp,
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                        )

                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Rating",
                                                tint = Color(0xFFFFD700)
                                            )

                                            val averageRating = if (food.ratingCount > 0) food.rating / food.ratingCount else 0f
                                            Text(
                                                text = "$averageRating (${food.ratingCount} reviews)",
                                                color = Color.Black,
                                                modifier = Modifier
                                                    .padding(start = 2.dp)
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Text(
                                                text = "20-30 Min ",
                                                color = Color.Black,
                                                modifier = Modifier
                                                    .padding(start = 2.dp)
                                            )
                                            Icon(
                                                painterResource(id = R.drawable.clock),
                                                contentDescription = "Rating",
                                                tint = Color(0xFFFFD700)
                                            )

                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    NutritionInfoRow()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Section
                    Column (modifier = Modifier.padding(horizontal = 16.dp)){
                        Text(
                            text = stringResource(id = R.string.description),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppins,
                            fontSize = 16.sp
                        )
                        Text(
                            text = food.description,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // State for user's rating
                        val ratingState = remember { mutableStateOf(viewModel.userRating ?: 0f) }

                        // عرض التقييم الحالي للمستخدم إذا كان موجودًا
                        if (viewModel.userRating != null) {
                            Text(text = "Your Rating: ${viewModel.userRating}",
                                color = colorResource(id = R.color._black),
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppins,
                                fontSize = 16.sp)
                        } else {
                            Text(text = "Submit Your Rating:",
                                color = colorResource(id = R.color._black),
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppins,
                                fontSize = 16.sp)
                        }
                        AnimatedRatingBar(
                            currentRating = ratingState.value,
                            onRatingChanged = { rating ->
                                if (viewModel.userRating == null) { // Check if the user has already rated
                                    ratingState.value = rating
                                    viewModel.submitRating(food.id, rating, userId)
                                    Toast.makeText(context, "Rating Submitted", Toast.LENGTH_SHORT).show()
                                } else {
                                    // بدلًا من ذلك، تحديث التقييم القديم
                                    ratingState.value = rating // تحديث القيمة الجديدة
                                    viewModel.submitRating(food.id, rating, userId) // إرسال التقييم الجديد
                                    Toast.makeText(context, "Your rating has been updated", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        )
                    }


                }
                //Back Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 50.dp)
                ){
                    Card (
                        modifier = Modifier
                            .size(34.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ){
                        Icon(
                            painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Back",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .clickable {
                                    navController.popBackStack()
                                },
                            tint = colorResource(id = R.color.appColor)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Card( modifier = Modifier
                        .size(34.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        val isFavorite = remember { mutableStateOf(favoriteIds.value.contains(foodId ?: ""))}
                        Icon(
                            painterResource(
                                id = if (isFavorite.value) R.drawable.favorite_se else R.drawable.favorite_un
                            ),
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .clickable {
                                    if (isFavorite.value) {
                                        isFavorite.value = !isFavorite.value
                                        favoriteViewModel.deleteFavoriteFood(food.id){
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

                                },
                            tint = if (true) colorResource(id = R.color.appColor) else Color.Gray
                        )
                    }
                }
                //add to cart
                Card(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(92.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(Color.White),) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Row {

                            IconButton(
                                onClick = {
                                    if (numberItem.value > 1) {
                                        numberItem.value--
                                    }
                                },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    tint = Color.White,
                                    contentDescription = "Decrease quantity"

                                )

                            }

                            Text(
                                text =numberItem.value.toString(),
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(top = 4.dp)
                            )

                            IconButton(
                                onClick = {
                                    numberItem.value++
                                },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = colorResource(id = R.color.appColor),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .size(24.dp)

                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Increase quantity",
                                    tint = Color.White
                                )
                            }
                        }

                        androidx.compose.material3.Button(
                            onClick = {
                                cartViewModel.addToCart(food=food,numberItem.value,userId){
                                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .height(52.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                colorResource(id = R.color.appColor)
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.addToCart),
                                    textAlign = TextAlign.Center,
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.cart_un),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 8.dp),
                                )
                            }


                        }

                    }

                }
            }

        }

    }
}

@Composable
fun AnimatedRatingBar(
    currentRating: Float,
    onRatingChanged: (Float) -> Unit,
    maxRating: Int = 5,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating) {
            val isSelected = i <= currentRating
            val starColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFFFFD700) else Color.LightGray,
                animationSpec = tween(durationMillis = 300) // Smooth color transition
            )

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating Star",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onRatingChanged(i.toFloat())
                    },
                tint = starColor
            )
        }
    }
}
@Composable
fun NutritionInfoRow() {
    val numberKcal = remember { mutableStateOf(Random.nextInt(80, 150)) }
    val numberFat = remember { mutableStateOf(Random.nextInt(60, 180)) }
    val numberProtein = remember { mutableStateOf(Random.nextInt(20, 40)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NutritionInfoItem(
            icon = ImageVector.vectorResource(id = R.drawable.fire),
            contentDescription = "Calories",
            value = "${numberKcal.value} Kcal"
        )
        Spacer(modifier = Modifier.width(20.dp))
        NutritionInfoItem(
            icon = ImageVector.vectorResource(id =R.drawable.protien),
            contentDescription = "Protein",
            value = "${numberProtein.value}g Protein"
        )
        Spacer(modifier = Modifier.width(20.dp))
        NutritionInfoItem(
            icon = ImageVector.vectorResource(id = R.drawable.fat),
            contentDescription = "Fats",
            value = "${numberFat.value}g Fats"
        )
    }
}

@Composable
fun NutritionInfoItem(
    icon: ImageVector,
    contentDescription: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,

        ) {
        androidx.compose.material3.Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(30.dp),
            border = BorderStroke(1.dp, colorResource(id = R.color.appColor))
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                tint = colorResource(id = R.color.appColor)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            fontFamily = poppins
        )
    }
}






