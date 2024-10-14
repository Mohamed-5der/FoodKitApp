package com.example.foodkit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_food_table")
data class FavoriteFood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique ID for each food item
    val userId: String, // User associated with the favorite food
    val name: String, // Name of the food item
    val description: String, // Description of the food item
    val imageUrl: String, // URL for the food image
    val price: Double, // Price of the food item
    val category: String, // Category of the food item
    val rating: Double, // Rating of the food item
    val isFavorite: Boolean = false, // Default value set to false
    val numberRating: Double = 0.0, // Default value for ratings
    val idFood: String // Unique identifier for the food item
)