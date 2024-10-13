package com.example.foodkit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_food_table")
data class FavoriteFood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Make sure this is unique for each food item
    val userId: Int, // Foreign key referencing the User
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val category: String,
    val rating: Double,
    val isFavorite: Boolean = false, // Default value set to false
    val numberRating: Int = 0 // Default value for ratings
)