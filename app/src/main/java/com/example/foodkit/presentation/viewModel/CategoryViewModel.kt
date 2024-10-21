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
import com.example.foodkit.repository.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    var categoryName by mutableStateOf(TextFieldValue(""))
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var selectedCategory by mutableStateOf<Category?>(null)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _foodsInCategory = MutableStateFlow<List<Food>>(emptyList())
    val foodsInCategory: StateFlow<List<Food>> = _foodsInCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    fun addCategory(context: Context) {
        if (categoryName.text.isBlank() || selectedImageUri == null) {
            Toast.makeText(
                context,
                "Please fill in all fields and select an image",
                Toast.LENGTH_SHORT
            ).show()
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
                Toast.makeText(
                    context,
                    "Failed to add category: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }


    fun loadCategories() {
        _isLoading.value = true
        repository.getCategories(
            onSuccess = { loadedCategories ->
                _categories.value = loadedCategories
                _isLoading.value = false
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error loading categories", exception)
                _isLoading.value = false
            }
        )
    }

    fun loadFoodsByCategory(categoryId: String) {
        _isLoading.value = true
        Log.d("ViewModel", "Loading foods for category with ID: $categoryId")
        repository.getFoodsByCategory(
            categoryId,
            onSuccess = { loadedFoods ->
                Log.d("ViewModel", "Foods loaded successfully: ${loadedFoods.size}")
                _foodsInCategory.value = loadedFoods
                _isLoading.value = false
            },
            onFailure = { exception ->
                Log.e("ViewModel", "Error loading foods for category", exception)
                _isLoading.value = false
            }
        )
    }

    // New function to select a category
    fun selectCategory(category: Category) {
        selectedCategory = category
        loadFoodsByCategory(category.id)
    }
}
