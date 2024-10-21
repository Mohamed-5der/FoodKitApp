package com.example.foodkit.presentation.view.masterBottomNavagtion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.repository.Food
import org.koin.androidx.compose.koinViewModel


@Composable
fun FoodsScreen(navController: NavController) {
    val foodViewModel: FoodListScreenViewModel = koinViewModel()
    val foods: List<Food> = emptyList()
    val foodList =
        if (foods.isNotEmpty()) foods else foodViewModel.foods.collectAsState(initial = emptyList()).value
    LaunchedEffect(Unit) {
        if (foods.isEmpty()) {
            foodViewModel.loadAllFoods()
        }
    }
    Column (
        modifier = Modifier.background(Color.White)
    ){
        TopAppBar(
            title = {
                Text(
                    "Foods Master",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier
                        .fillMaxWidth().padding(top = 30.dp)
                )
            },
            windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
            backgroundColor = Color.White,
            modifier = Modifier.height(70.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .background(Color.White),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),

                modifier = Modifier.fillMaxSize()
            ) {
                items(foodList) { food ->
                    FoodCardMaster(food = food, onClick = {
                        navController.navigate( "details_analysis/${food.id}")
                    }, onClickEdit = {

                    })
                }
            }

        }


    }
}