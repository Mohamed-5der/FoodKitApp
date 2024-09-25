package com.example.foodkit.presentation.viewModel

import android.content.Context
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

    fun addFood(context: Context) {
        if (foodName.text.isBlank() || foodDescription.text.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val food = Food(name = foodName.text, description = foodDescription.text)

        repository.addFood(food, {
            // Reset fields and show success message
            foodName = TextFieldValue("")
            foodDescription = TextFieldValue("")
            Toast.makeText(context, "Food added successfully", Toast.LENGTH_SHORT).show()
        }, { exception ->
            // Handle failure
            Log.e("MasterViewModel", "Error adding food: ${exception.message}")
            Toast.makeText(context, "Failed to add food: ${exception.message}", Toast.LENGTH_SHORT).show()
        })
    }
}
