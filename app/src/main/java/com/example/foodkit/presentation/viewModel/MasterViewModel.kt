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
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository


// ViewModel for Admin Screen
class MasterViewModel(private val repository: FoodRepository) : ViewModel() {
    var foodName by mutableStateOf(TextFieldValue(""))
    var foodDescription by mutableStateOf(TextFieldValue(""))
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var foodPrice by mutableStateOf(TextFieldValue(""))
    var selectedCategory by mutableStateOf("")

    fun addFoodToCategory(context: Context) {
        if (foodName.text.isBlank() || foodDescription.text.isBlank() || selectedImageUri == null || foodPrice.text.isBlank() || selectedCategory.isBlank()) {
            Toast.makeText(context, "Please fill in all fields, select an image, and choose a category", Toast.LENGTH_SHORT).show()
            return
        }

        val price = foodPrice.text.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            return
        }
        val food = Food(name = foodName.text, description = foodDescription.text, price = price)

        selectedImageUri?.let { imageUri ->
            repository.addFoodToCategory(food, imageUri, price, selectedCategory, {
                // clear the form fields
                foodName = TextFieldValue("")
                foodDescription = TextFieldValue("")
                selectedImageUri = null
                foodPrice = TextFieldValue("")
                selectedCategory = ""
                Toast.makeText(context, "Food added successfully", Toast.LENGTH_SHORT).show()
            }, { exception ->
                Toast.makeText(context, "Failed to add food: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }
}
