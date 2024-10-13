package com.example.foodkit.presentation.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun ProductDetailsScreen( foodId: String, userId: String) {
    val viewModel: FoodDetailViewModel = koinViewModel( parameters = { parametersOf(userId) } )
    val context = LocalContext.current

    // Load food details and user rating when the screen is launched
    LaunchedEffect(Unit) {
        viewModel.loadFood(foodId)
        viewModel.loadUserRating(foodId, userId) // Load user-specific rating
    }

    viewModel.food?.let { food ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(colorResource(id = R.color.white))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Food Name, Rating, and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                if (food.price == food.price) {
                    Text(
                        text = "£${food.price}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                }


            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(vertical = 8.dp)
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
                        .weight(1f)
                        .padding(start = 2.dp)
                )

                if (false) {
                    Text(
                        text = "$30.99",
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text(
                text = "Description",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = food.description,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (false) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Price",
                        color = Color.Black,

                        )
                    Text(
                        text = "$20.99",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.appColor)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Add To Cart")
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





