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
    val foodDetailViewModel: FoodDetailViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val cartViewModel: CartForTestViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val masterViewModel: MasterViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val context = LocalContext.current

    // Load food details and user rating when the screen is launched
    LaunchedEffect(Unit) {
        foodDetailViewModel.loadFood(foodId)
        foodDetailViewModel.loadUserRating(foodId, userId) // Load user-specific rating
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

            // Display total revenue for the food
            Text(text = "Total Revenue: \$${food.totalRevenue}")

            Spacer(modifier = Modifier.height(16.dp))

            // State for user's rating
            val ratingState = remember { mutableStateOf(foodDetailViewModel.userRating ?: 0f) }

            // عرض التقييم الحالي للمستخدم إذا كان موجودًا
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
                        // بدلًا من ذلك، تحديث التقييم القديم
                        ratingState.value = rating // تحديث القيمة الجديدة
                        foodDetailViewModel.submitRating(food.id, rating, userId) // إرسال التقييم الجديد
                        Toast.makeText(context, "Your rating has been updated", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // التحكم في كمية المنتج
            var quantity by remember { mutableStateOf(1) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    if (quantity > 1) quantity-- // تقليل الكمية
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Decrease Quantity")
                }

                Text(text = quantity.toString(), style = MaterialTheme.typography.bodyLarge)

                IconButton(onClick = {
                    if (quantity < food.availableQuantity) quantity++ // زيادة الكمية بشرط ألا تتجاوز المتاحة
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (quantity <= food.availableQuantity) { // تحقق من أن الكمية المطلوبة غير أكبر من المتاحة
                        cartViewModel.addToCart(food, quantity, userId)
                        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Not enough quantity available", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Add to Cart")
            }
        }
    }
}

@Composable
fun AnimatedRatingBar(
    currentRating: Float,
    onRatingChanged: (Float) -> Unit,
    maxRating: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating) {
            val isSelected = i <= currentRating
            val starColor by animateColorAsState(
                targetValue = if (isSelected) Color.Yellow else Color.Gray,
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
