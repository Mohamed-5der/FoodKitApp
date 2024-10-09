package com.example.foodkit.presentation.view

import android.annotation.SuppressLint
import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavController
import com.example.foodkit.components.FoodCard
import com.example.foodkit.navigation.Routes
import com.example.foodkit.R
import com.example.foodkit.presentation.view.navigation.CartScreenContent
import com.example.foodkit.presentation.view.navigation.FavoriteScreenContent
import com.example.foodkit.presentation.view.navigation.HomeScreenContent
import com.example.foodkit.presentation.view.navigation.ProfileScreenContent
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navController: NavController) {
//    FoodListScreen(navController)
    Home()
//    ProfileScreenContent()
//    FavoriteScreenContent()
//    CartScreenContent()
//    HomeScreenContent()
//    ProductDetailsScreen()

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home() {

    val context = LocalContext.current
    val errorMessage by remember { mutableStateOf<String?>(null) }
    val selectedIndex = remember { mutableIntStateOf(0) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape( 20.dp)),
                backgroundColor = Color.White
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
                                .size(35.dp)
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

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
//                    .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            when (selectedIndex.intValue) {
                0 -> HomeScreenContent()
                1 -> CartScreenContent()
                2 -> FavoriteScreenContent()
                3 -> ProfileScreenContent()
            }
        }
    }



    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.secondaryColor)
        ),

        title = {

            Column (modifier = Modifier.fillMaxSize()){
                Text(
                    text = "Hi Ahmed Emad",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Row () {
                    IconButton(onClick = { /* Handle notification click */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Location Icon",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Location, Cairo, Egypt",
                        color = Color.Gray,
                        fontSize = 20.sp,
                    )

                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notification click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_icon),
                    contentDescription = "Notification Icon",
                    tint = Color.Black
                )
            }
        },


        /*
        navigationIcon = {
            IconButton(onClick = { /* Handle drawer click */ }) {
                Icon(
//                    tint: Color = LocalContentColor.current,
                    painter = painterResource(id = R.drawable.dropdown_icon),
                    contentDescription = "Menu Icon",
                    tint = Color.Black
                )
            }
        }
        */
    )
}

@Composable
fun FoodListScreen(
    navController: NavController,
    viewModel: FoodListScreenViewModel = koinViewModel()) {

    val foods by viewModel.foods.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadAllFoods() // Load foods when the screen is displayed
    }

    LazyColumn (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(foods) { food ->
            FoodCard(food = food, navController = navController)
        }
    }
}

