package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel for Food List Screen
class FoodListScreenViewModel(private val repository: FoodRepository) : ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    private val _topFiveFoods = MutableStateFlow<List<Food>>(emptyList())
    val topFiveFoods: StateFlow<List<Food>> = _topFiveFoods.asStateFlow()

    fun loadAllFoods() {
        _isLoading.value = true
        repository.getAllFoods(
            onSuccess = { foods ->
                _foods.value = foods
                _isLoading.value = false
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error loading foods", exception)
                _isLoading.value = false
            }
        )
    }

    fun fetchTopFiveFoods() {
        repository.getTopFiveFoods(
            onSuccess = { loadedFoods ->
                _topFiveFoods.value = loadedFoods
                Log.d("TopFoods", "Top foods: $loadedFoods")
            },
            onFailure = { exception ->
                Log.e("TopFoods", "Error fetching top foods: ${exception.message}")
            }
        )
    }
}


