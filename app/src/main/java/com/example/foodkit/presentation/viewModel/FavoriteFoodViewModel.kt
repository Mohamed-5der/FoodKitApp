package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.FavoriteFoodDao
import com.example.foodkit.model.FavoriteFood
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoriteFoodViewModel(private val favoriteFoodDao: FavoriteFoodDao) : ViewModel() {

    private val _favoriteFoods = MutableStateFlow<List<FavoriteFood>>(emptyList())
    val favoriteFoods: StateFlow<List<FavoriteFood>> = _favoriteFoods

    private val _favoriteIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteIds: StateFlow<List<String>> get() = _favoriteIds
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun getFavoriteFoods() {
        viewModelScope.launch {
            try {
                _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(userId)
                val ids = _favoriteFoods.value.map { it.idFood.orEmpty() }
                _favoriteIds.value = ids

            } catch (e: Exception) {
                _favoriteIds.value = emptyList()
                _favoriteFoods.value = emptyList()
                Log.e("FavoriteFoodViewModel", "Error fetching favorite foods", e)
            }
        }
    }

    fun addFavoriteFood(
        name: String,
        description: String,
        imageUrl: String,
        price: Double,
        category: String,
        rating: Float,
        idFood: String,
        numberRating: Double,
        onAdded: () -> Unit,
    ) {
        viewModelScope.launch {
            val newFood = FavoriteFood(
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                category = category,
                rating = rating.toDouble(),
                isFavorite = true,
                numberRating = numberRating,
                idFood = idFood,
                userId = userId
            )
            try {
                favoriteFoodDao.insert(newFood)
                _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(userId)
                getFavoriteFoods()
                onAdded()
            } catch (e: Exception) {
                Log.e("FavoriteFoodViewModel", "Error adding favorite food", e)
            }

        }
    }

    fun deleteFavoriteFood(idFood: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                favoriteFoodDao.delete(idFood)
                _favoriteFoods.value = favoriteFoodDao.getFavoriteFoodsForUser(userId)
                getFavoriteFoods()
                onDeleted()
            } catch (e: Exception) {
                Log.e("FavoriteFoodViewModel", "Error deleting favorite food", e)
            }

        }
    }
}