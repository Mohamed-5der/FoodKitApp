package com.example.foodkit.presentation.view.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val errorMessage by remember { mutableStateOf<String?>(null) }


        Column(
            modifier = Modifier
                .fillMaxSize()
//            .height(1000.dp)
                .background(colorResource(id = R.color.secondaryColor))

        ) {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = { Text(text = stringResource(id = R.string.myCart)) },
                navigationIcon = {
                    IconButton(onClick = {} ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        /*    Row {
                IconButton(onClick = { /* Handle back button click */ }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                    }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "My Cart",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(8.dp)
                )

            }

         */

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn (
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .weight(1f)
            ) {
                val cartItems = 10
                items(cartItems) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .padding(8.dp),

                        ) {
//                cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.onboarding_photo1,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("item.name", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("item.price", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
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
                                    painter =  painterResource(id = R.drawable.minus_icon),
                                    tint = Color.White,
                                    contentDescription = "Decrease quantity"

                                )

                            }

                            Text(text = "1", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium , modifier = Modifier.padding(horizontal = 8.dp))

                            IconButton(onClick = { },
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

            Button(
                onClick = { /* Handle Checkout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
