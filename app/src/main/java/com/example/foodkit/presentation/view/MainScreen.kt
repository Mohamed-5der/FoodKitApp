package com.example.foodkit.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodkit.R
import com.example.foodkit.presentation.view.userBottomNavigation.CartScreenContent
import com.example.foodkit.presentation.view.userBottomNavigation.FavoriteScreenContent
import com.example.foodkit.presentation.view.userBottomNavigation.HomeScreenContent
import com.example.foodkit.presentation.view.userBottomNavigation.ProfileScreenContent

@Composable
fun MainScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Home(navController)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController) {

    val errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedIndex = remember { mutableIntStateOf(0) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                                tint = if (selectedIndex.intValue == 0) colorResource(id = R.color.appColor) else Color.Black
                            )
                        },
                        selected = false,
                        onClick = { selectedIndex.intValue = 0 },
                        label = {
                            Text(
                                stringResource(id = R.string.home),
                                fontSize = 10.sp,
                                color = if (selectedIndex.intValue == 0) colorResource(id = R.color.appColor) else Color.Black
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
                                tint = if (selectedIndex.intValue == 1) colorResource(id = R.color.appColor) else Color.Black
                            )
                        },
                        selected = false,
                        onClick = { selectedIndex.intValue = 1 },
                        label = {
                            Text(
                                stringResource(id = R.string.cart),
                                fontSize = 10.sp,
                                color = if (selectedIndex.intValue == 1) colorResource(id = R.color.appColor) else Color.Black
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
                                painter = painterResource(id = R.drawable.favorite_un),
                                contentDescription = "Favorite Icon",
                                tint = if (selectedIndex.intValue == 2) colorResource(id = R.color.appColor) else Color.Black
                            )
                        },
                        selected = false,
                        onClick = { selectedIndex.intValue = 2 },
                        label = {
                            Text(
                                stringResource(id = R.string.wish_list),
                                fontSize = 10.sp,
                                color = if (selectedIndex.intValue == 2) colorResource(id = R.color.appColor) else Color.Black
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
                                tint = if (selectedIndex.intValue == 3) colorResource(id = R.color.appColor) else Color.Black
                            )
                        },
                        selected = false,
                        onClick = { selectedIndex.intValue = 3 },
                        label = {
                            Text(
                                stringResource(id = R.string.profile),
                                fontSize = 10.sp,
                                color = if (selectedIndex.intValue == 3) colorResource(id = R.color.appColor) else Color.Black
                            )
                        },
                    )
                }


            }
        }

    ) {
        // Screen content
        Column(
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            when (selectedIndex.intValue) {
                0 -> HomeScreenContent(navController)
                1 -> CartScreenContent(navController)
                2 -> FavoriteScreenContent(navController)
                3 -> ProfileScreenContent(navController)
            }
        }
    }



    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }

}



