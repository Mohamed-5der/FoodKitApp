package com.example.foodkit.presentation.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.presentation.viewModel.CartForTestViewModel
import com.example.foodkit.repository.CartItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreenForTest(
    navController: NavController,
    userId: String,
    viewModel: CartForTestViewModel = koinViewModel()
) {
    val cartItems = viewModel.cartItems.collectAsState(initial = emptyList())
    val totalPrice by viewModel.totalPriceState.collectAsState(initial = 0.0)
    val context = LocalContext.current

    // تحميل العناصر عند تشغيل الشاشة
    LaunchedEffect(Unit) {
        viewModel.loadCartItems(userId)
    }

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    // عرض نافذة التأكيد إذا كان الـ BottomSheet مرئي
    if (isBottomSheetVisible) {
        AlertDialog(
            onDismissRequest = { isBottomSheetVisible = false },
            title = { Text("Confirm Your Order") },
            text = {
                Column {
                    Text("Total Price: $$totalPrice")
                    Spacer(modifier = Modifier.height(16.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.checkout(userId, onSuccess = {
                            Toast.makeText(context, "Order Confirmed!", Toast.LENGTH_SHORT).show()
                        }, onFailure = { exception ->
                            Log.e("Checkout", "Failed to checkout: ${exception.message}")
                        })
                        viewModel.clearCart(userId)
                        isBottomSheetVisible = false
                    }
                ) {
                    Text("Confirm Order")
                }
            },
            dismissButton = {
                Button(onClick = { isBottomSheetVisible = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Cart", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(cartItems.value) { item ->
                CartItemRow(
                    item = item,
                    onQuantityChange = { increment ->
                        viewModel.updateQuantityInCartScreen(item.foodId, userId, increment)
                    },
                    onRemove = {
                        viewModel.removeFromCart(item.foodId, userId)
                    }
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        Text("Total Price: $$totalPrice", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { isBottomSheetVisible = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Checkout")
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onQuantityChange: (Int) -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(item.foodName, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$${item.foodPrice}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQuantityChange(-1) }  ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Decrease Quantity")
                }
                Text(item.quantity.toString(), Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = { onQuantityChange(1) }  ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}
