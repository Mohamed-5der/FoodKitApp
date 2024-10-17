package com.example.foodkit.presentation.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.foodkit.repository.CartRepository
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository
import com.example.foodkit.repository.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


// ViewModel for Admin Screen
class MasterViewModel(
    private val foodRepository: FoodRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    var foodName by mutableStateOf(TextFieldValue(""))
    var foodDescription by mutableStateOf(TextFieldValue(""))
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var foodPrice by mutableStateOf(TextFieldValue(""))
    var selectedCategory by mutableStateOf("")
    var categoryName by mutableStateOf("")
    var availableQuantityNumber by mutableStateOf(TextFieldValue(""))
    var calories by mutableStateOf(TextFieldValue(""))
    var protein by mutableStateOf(TextFieldValue(""))
    var fats by mutableStateOf(TextFieldValue(""))

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    var food by mutableStateOf<Food?>(null)
        private set


    fun addFoodToCategory(context: Context, onAddSuccess:()->Unit) {
        if (foodName.text.isBlank() || foodDescription.text.isBlank() || selectedImageUri == null || foodPrice.text.isBlank() || selectedCategory.isBlank()) {
            Toast.makeText(context, "Please fill in all fields, select an image, and choose a category", Toast.LENGTH_SHORT).show()
            return
        }

        val price = foodPrice.text.toDoubleOrNull()
        val availableQuantity = availableQuantityNumber.text.toInt()
        val caloriesQuantity = calories.text.toInt()
        val proteinsQuantity = protein.text.toInt()
        val fatsQuantity = fats.text.toInt()
        if (price == null) {
            Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            return
        }
        val food = Food(name = foodName.text, description = foodDescription.text, price = price)

        selectedImageUri?.let { imageUri ->
            foodRepository.addFoodToCategory(
                food,
                imageUri,
                price,
                selectedCategory,
                categoryName,
                availableQuantity,
                caloriesQuantity,
                proteinsQuantity,
                fatsQuantity, {
                // clear the form fields
                foodName = TextFieldValue("")
                foodDescription = TextFieldValue("")
                selectedImageUri = null
                foodPrice = TextFieldValue("")
                selectedCategory = ""
                availableQuantityNumber = TextFieldValue("")
                    calories = TextFieldValue("")
                    protein = TextFieldValue("")
                    fats = TextFieldValue("")
                Toast.makeText(context, "Food added successfully", Toast.LENGTH_SHORT).show()
            }, { exception ->
                Toast.makeText(context, "Failed to add food: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }

     fun loadOrders() {
        cartRepository.getOrders(
            onSuccess = { ordersList ->
                _orders.value = ordersList
            },
            onFailure = { exception ->
                Log.e("AdminViewModel", "Failed to load orders: ${exception.message}")
            }
        )
    }
}
