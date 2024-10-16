package com.example.foodkit.presentation.view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.foodkit.components.SelectImageButton
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import org.koin.androidx.compose.koinViewModel

//master screen
@Composable
fun MasterScreen(navController: NavController) {
    val viewModel: MasterViewModel = koinViewModel()
    val categoryViewModel : CategoryViewModel = koinViewModel()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = viewModel.foodName,
            onValueChange = { viewModel.foodName = it },
            label = { Text("Food Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.foodDescription,
            onValueChange = { viewModel.foodDescription = it },
            label = { Text("Food Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.foodPrice,
            onValueChange = { viewModel.foodPrice = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.availableQuantityNumber,
            onValueChange = { viewModel.availableQuantityNumber = it },
            label = { Text("AvailableQuantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = viewModel.calories,
            onValueChange = { viewModel.availableQuantityNumber = it },
            label = { Text("CaloriesQuantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = viewModel.protein,
            onValueChange = { viewModel.availableQuantityNumber = it },
            label = { Text("ProteinQuantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = viewModel.fats,
            onValueChange = { viewModel.availableQuantityNumber = it },
            label = { Text("FatsQuantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        SelectImageButton { uri ->
            viewModel.selectedImageUri = uri
        }
        viewModel.selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = { expanded = !expanded }) {
            Text("Select Category")
        }
        // عرض التصنيفات في DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryViewModel.categories.value.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category.name) },
                    onClick = {
                        viewModel.selectedCategory = category.name
                        expanded = false
                    }
                )
            }
        }

        Button(onClick = { viewModel.addFoodToCategory(context) }) {
            Text("Add Food")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(Routes.MAIN) }) {
            Text("View Food List")
        }

        Button(onClick = { navController.navigate(Routes.ADD_CATEGORY) }) {
            Text("Add Category")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.CATEGORY_LIST) }) {
            Text("Category List Screen")
        }
    }
}

    @Composable
    fun AddCategoryScreen(
        navController: NavController,
        viewModel: CategoryViewModel = koinViewModel()
    ) {
        val context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.categoryName,
                onValueChange = { viewModel.categoryName = it },
                label = { Text("Category Name") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SelectImageButton { uri ->
                viewModel.selectedImageUri = uri
            }

            // show selected photo
            viewModel.selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(100.dp)
                )
            }

            Button(onClick = {
                viewModel.addCategory(context)
            }) {
                Text(text = "Add Category")
            }

        }
    }