package com.example.foodkit.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodkit.model.FavoriteFood
import com.example.foodkit.model.User

@Database(entities = [User::class, FavoriteFood::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteFoodDao(): FavoriteFoodDao
}