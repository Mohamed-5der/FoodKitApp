package com.example.foodkit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_food_table")
data class FavoriteFood(
    @PrimaryKey val id: String = "",
    val name: String,
    val description: String,
    val userId: Int,
    val imageUrl: String,
    val price: Double,
    val category: String,
    val rating: Double,
    val isFavorite: Boolean,
    val numberRating: Int
)