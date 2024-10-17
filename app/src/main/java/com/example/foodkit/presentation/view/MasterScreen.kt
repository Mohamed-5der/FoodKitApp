package com.example.foodkit.presentation.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.SelectImageButton
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.view.masterBottomNavagtion.FoodsScreen
import com.example.foodkit.presentation.view.masterBottomNavagtion.HomeScreen
import com.example.foodkit.presentation.view.masterBottomNavagtion.OrdersScreen
import com.example.foodkit.presentation.view.masterBottomNavagtion.StaffScreen
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import org.koin.androidx.compose.koinViewModel

//master screen
@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MasterScreen(navController: NavController) {

    val selectedIndex = remember { mutableStateOf(0) }


    Surface(
        color = Color.White,
    ) {
        Scaffold (
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    elevation = 8.dp,
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(35.dp)
                                        .padding(vertical = 4.dp),
                                    painter = painterResource(id = R.drawable.home_un),
                                    contentDescription = "Home Icon",
                                    tint = if (selectedIndex.value == 0) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                            selected = false,
                            onClick = { selectedIndex.value = 0 },
                            label = {
                                Text(
                                    stringResource(id = R.string.home),
                                    fontSize = 10.sp,
                                    color = if (selectedIndex.value == 0) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(35.dp)
                                        .padding(vertical = 4.dp),
                                    painter = painterResource(id = R.drawable.cart_un),
                                    contentDescription = "Cart Icon",
                                    tint = if (selectedIndex.value == 1) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                            selected = false,
                            onClick = { selectedIndex.value = 1 },
                            label = {
                                Text(
                                    stringResource(id = R.string.orders),
                                    fontSize = 10.sp,
                                    color = if (selectedIndex.value == 1) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(32.dp)
                                        .padding(vertical = 4.dp),
                                    painter = painterResource(id = R.drawable.protien),
                                    contentDescription = "Favorite Icon",
                                    tint = if (selectedIndex.value == 2) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                            selected = false,
                            onClick = { selectedIndex.value = 2 },
                            label = {
                                Text(
                                    stringResource(id = R.string.foods),
                                    fontSize = 10.sp,
                                    color = if (selectedIndex.value == 2) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(35.dp)
                                        .padding(vertical = 4.dp),
                                    painter = painterResource(id = R.drawable.profile_un),
                                    contentDescription = "Profile Icon",
                                    tint = if (selectedIndex.value == 3) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                            selected = false,
                            onClick = { selectedIndex.value = 3 },
                            label = {
                                Text(
                                    stringResource(id = R.string.staff),
                                    fontSize = 10.sp,
                                    color = if (selectedIndex.value == 3) colorResource(id = R.color.appColor) else Color.Black
                                )
                            },
                        )
                    }


                }

            }
        ){
            when (selectedIndex.value) {
                0 -> HomeScreen(navController)
                1 -> OrdersScreen(navController)
                2 -> FoodsScreen(navController)
                3 -> StaffScreen(navController)
            }

        }

    }

}
