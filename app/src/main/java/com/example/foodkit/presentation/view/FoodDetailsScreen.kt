package com.example.foodkit.presentation.view

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foodkit.presentation.viewModel.CartForTestViewModel
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FoodDetailScreen(navController: NavController, foodId: String, userId: String) {
    val foodDetailViewModel: FoodDetailViewModel =
        koinViewModel(parameters = { parametersOf(userId) })
    val cartViewModel: CartForTestViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val masterViewModel: MasterViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        foodDetailViewModel.loadFood(foodId)
        foodDetailViewModel.loadUserRating(foodId, userId)
    }

    foodDetailViewModel.food?.let { food ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = food.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(food.imageUrl),
                contentDescription = "Food Image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Price: \$${food.price}")
            Text(text = food.description)

            Spacer(modifier = Modifier.height(16.dp))

            val averageRating = if (food.ratingCount > 0) food.rating / food.ratingCount else 0f
            Text(text = "Rating: $averageRating (${food.ratingCount} reviews)")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Total Revenue: \$${food.totalRevenue}")

            Spacer(modifier = Modifier.height(16.dp))

            // State for user's rating
            val ratingState = remember { mutableStateOf(foodDetailViewModel.userRating ?: 0f) }

            if (foodDetailViewModel.userRating != null) {
                Text(text = "Your Rating: ${foodDetailViewModel.userRating}")
            } else {
                Text(text = "Submit Your Rating:")
            }

            AnimatedRatingBar(
                currentRating = ratingState.value,
                onRatingChanged = { rating ->
                    if (foodDetailViewModel.userRating == null) { // Check if the user has already rated
                        ratingState.value = rating
                        foodDetailViewModel.submitRating(food.id, rating, userId)
                        Toast.makeText(context, "Rating Submitted", Toast.LENGTH_SHORT).show()
                    } else {
                        ratingState.value = rating
                        foodDetailViewModel.submitRating(
                            food.id,
                            rating,
                            userId
                        )
                        Toast.makeText(context, "Your rating has been updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            var quantity by remember { mutableStateOf(1) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    if (quantity > 1) quantity--
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Decrease Quantity")
                }

                Text(text = quantity.toString(), style = MaterialTheme.typography.bodyLarge)

                IconButton(onClick = {
                    if (quantity < food.availableQuantity) quantity++
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (quantity <= food.availableQuantity) {
                        cartViewModel.addToCart(food, quantity, userId, {})
                        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Not enough quantity available", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ) {
                Text(text = "Add to Cart")
            }
        }
    }
}
