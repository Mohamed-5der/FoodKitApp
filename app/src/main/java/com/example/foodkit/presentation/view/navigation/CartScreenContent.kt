package com.example.foodkit.presentation.view.navigation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodkit.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable

 fun CartScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {

        Column(
            modifier = Modifier
                .padding(bottom = 60.dp)
                .background(colorResource(id = R.color.white))

        ) {
            TopAppBar(
                modifier = Modifier
                    .background(colorResource(id = R.color.white)),
                title = { Text(text = stringResource(id = R.string.myCart)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                val cartItems = 10
                items(cartItems) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                        ) {
//                cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.padding(8.dp),
                                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            ) {
                                AsyncImage(
                                    model = R.drawable.onboarding_photo1,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .padding(8.dp),
                                    contentScale = ContentScale.Fit,
                                    placeholder = painterResource(id = R.drawable.food_photo),
                                    error = painterResource(id = R.drawable.food_photo)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "item.name",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "item.price",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))


                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = Color.Gray,
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
                                text = "1",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            IconButton(
                                onClick = { },
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
                    }
                }
            }
        }



            Button(
                onClick = { /* Handle Checkout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
            ) {
                Text(
                    stringResource(id = R.string.checkout),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
 }
