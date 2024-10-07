package com.example.foodkit.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.repository.CartItem
import com.example.foodkit.repository.Order
import org.koin.androidx.compose.koinViewModel

@Composable
fun MasterOrdersScreen(
    viewModel: MasterViewModel = koinViewModel()
) {
    val orders = viewModel.orders.collectAsState(initial = emptyList())

    // Load orders when screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Orders List", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        LazyColumn {
            items(orders.value) { order ->
                OrderRow(order = order)
            }
        }
    }
}

@Composable
fun OrderRow(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
            Text("Total Price: $${order.totalPrice}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Items:", style = MaterialTheme.typography.bodyMedium)

            // استخدام LazyColumn لعرض العناصر داخل الطلب
            LazyRow {
                items(order.items) { item ->
                    ItemCard(item = item)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun ItemCard(item: CartItem) {
    val totalPrice = item.foodPrice * item.quantity
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${item.foodName} x${item.quantity}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
