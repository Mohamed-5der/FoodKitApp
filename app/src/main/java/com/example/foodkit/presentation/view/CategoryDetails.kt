package com.example.foodkit.presentation.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.components.FoodCard
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: String,
    viewModel: CategoryViewModel = koinViewModel()

) {
    val foodsInCategory = viewModel.foodsInCategory.collectAsState(initial = emptyList())

    Log.d("Compose", "Navigated to CategoryDetailScreen with ID: $categoryId")

    LaunchedEffect(categoryId) {
        viewModel.loadFoodsByCategory(categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Category Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 85.dp)
                .padding(16.dp)
        ) {
            items(foodsInCategory.value) { food ->
                FoodCard( food ,navController )
            }
        }
    }
}
