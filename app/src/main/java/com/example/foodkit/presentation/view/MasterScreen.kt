package com.example.foodkit.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.MasterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MasterScreen(navController: NavController ) {
    val viewModel: MasterViewModel =  koinViewModel()
    val context = LocalContext.current


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

        Button(onClick = { viewModel.addFood(context) }) {
            Text("Add Food")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.MAIN) }) {
            Text("View Food List")
        }
    }
}