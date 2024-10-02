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
import com.example.foodkit.repository.Category
import com.example.foodkit.repository.CategoryRepository

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {
    var categoryName by mutableStateOf(TextFieldValue(""))
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var categories by mutableStateOf<List<Category>>(emptyList())



    fun addCategory(context: Context) {
        if (categoryName.text.isBlank() || selectedImageUri == null) {
            Toast.makeText(context, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }
        // Create a new category object
        val newCategory = Category(name = categoryName.text)

        selectedImageUri?.let { imageUri ->

            //Add category to Firestore and Storage
            repository.addCategory(newCategory, imageUri, {
                // Clear the input fields and selected image
                categoryName = TextFieldValue("")
                selectedImageUri = null
                Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show()
            }, { exception ->
                Toast.makeText(context, "Failed to add category: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }


    fun loadCategories() {
        repository.getCategories(
            onSuccess = { loadedCategories ->
                categories = loadedCategories
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error loading categories", exception)
            }
        )
    }
}
