package com.example.foodkit.presentation.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.components.CategoryCard
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryListScreen(
    navController: NavController,
    viewModel: CategoryViewModel = koinViewModel()
) {
    // تحميل التصنيفات عند بدء تشغيل الشاشة
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(viewModel.categories) { category ->
            CategoryCard(category , onClick = {} )
        }
    }
}
