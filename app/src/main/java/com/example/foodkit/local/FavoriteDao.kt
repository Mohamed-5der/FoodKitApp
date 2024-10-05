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

    @Delete
    suspend fun delete(food: FavoriteFood)


    @Query("SELECT * FROM favorite_food_table WHERE userId = :userId")
    suspend fun getFavoriteFoodsForUser(userId: Int): List<FavoriteFood>
}