package com.example.foodkit.presentation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.components.FoodCard
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navController : NavController) {
    FoodListScreen(navController)
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

