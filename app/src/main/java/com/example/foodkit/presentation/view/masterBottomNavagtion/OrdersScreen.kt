package com.example.foodkit.presentation.view.masterBottomNavagtion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodkit.components.poppins
import com.example.foodkit.presentation.viewModel.MasterViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun OrdersScreen(navController: NavController) {
    val viewModel: MasterViewModel = koinViewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }
    val orders =viewModel.orders.collectAsState().value

    Column (
        modifier = Modifier.background(Color.White)
    ){
        TopAppBar(
            title = {
                Text(
                    "Orders Master",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier
                        .fillMaxWidth().padding(top = 30.dp)
                )
            },
            windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
            backgroundColor = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.height(70.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp).padding(horizontal = 16.dp)
                .background(Color.White),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(orders?: emptyList()) { order ->
                        OrderCard(order = order,context)
                    }
                }
        }


    }
}