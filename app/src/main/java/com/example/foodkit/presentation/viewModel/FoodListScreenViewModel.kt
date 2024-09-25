package com.example.foodkit.presentation.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository

class FoodListScreenViewModel(repository: FoodRepository) : ViewModel() {
    val foods = mutableStateListOf<Food>()

    init {
        repository.getFoods { foods.addAll(it) }
    }

}