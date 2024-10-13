package com.example.foodkit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.FavoriteFoodDao
import com.example.foodkit.model.FavoriteFood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteFoodViewModel(private val favoriteFoodDao: FavoriteFoodDao) : ViewModel() {

    private val _favoriteFoods = MutableStateFlow<List<FavoriteFood>>(emptyList())
    val favoriteFoods: StateFlow<List<FavoriteFood>> = _favoriteFoods

    fun getFavoriteFoods(userId: Int) {
        viewModelScope.launch {
            _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(userId)
        }
    }

    fun addFavoriteFood(
        name: String,
        description: String,
        userId: Int,
        imageUrl: String,
        price: Double,
        category: String,
        rating: Float,
    ) {
        viewModelScope.launch {
            val newFood = FavoriteFood(
                name = name,
                description = description,
                userId = userId,
                imageUrl = imageUrl,
                price = price,
                category = category,
                rating = rating.toDouble(),
                isFavorite = true,
                numberRating = 0
            )
            favoriteFoodDao.insert(newFood)
            _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(userId)
        }
    }

    fun deleteFavoriteFood(food: FavoriteFood) {
        viewModelScope.launch {
            favoriteFoodDao.delete(food)
            _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(food.userId)
        }
    }
}