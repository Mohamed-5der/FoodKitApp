package com.example.foodkit.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navController : NavController) {
    FoodListScreen(navController)
}

@Composable
fun FoodListScreen(navController: NavController ) {
    val viewModel: FoodListScreenViewModel = koinViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.foods.forEach { food ->
            Text(text = "Name: ${food.name}, Description: ${food.description}")
        }

        Button(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("Logout")
        }
    }
}
