package com.example.foodkit.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodkit.model.FavoriteFood


@Dao
interface FavoriteFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FavoriteFood)

    @Query("DELETE FROM favorite_food_table WHERE idFood = :foodId")
    suspend fun delete(foodId : String)

    @Query("SELECT * FROM favorite_food_table WHERE userId = :userId")
    suspend fun getFavoriteFoodsForUser(userId: String): List<FavoriteFood>
}