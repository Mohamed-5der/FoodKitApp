package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel for Food List Screen
class FoodListScreenViewModel(private val repository: FoodRepository) : ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    fun loadAllFoods() {
        repository.getAllFoods(
            onSuccess = { foods ->
                _foods.value = foods // update StateFlow with loaded foods
            },
            onFailure = { exception ->
                Log.e("Firestore", "Error loading foods", exception)
            }
        )
    }
}


