package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.FoodRepository


class FoodDetailViewModel(private val repository: FoodRepository, private val userId: String) :
    ViewModel() {

    var food by mutableStateOf<Food?>(null)
    var userRating by mutableStateOf<Float?>(null) // لتخزين تقييم المستخدم

    fun loadFood(foodId: String) {
        repository.getFoodById(foodId, { loadedFood ->
            food = loadedFood
        }, { exception ->
            Log.e("Firestore", "Error loading food details", exception)
        })
    }

    fun loadUserRating(foodId: String, userId: String) {
        repository.getUserRating(foodId, userId, { rating ->
            userRating = rating
        }, { exception ->
            Log.e("Firestore", "Error loading user rating", exception)
        })
    }

    fun submitRating(foodId: String, userRating: Float, userId: String) {
        repository.submitRating(foodId, userId, userRating)
        // تحديث القيمة بعد الحفظ
        this.userRating = userRating
    }


}



