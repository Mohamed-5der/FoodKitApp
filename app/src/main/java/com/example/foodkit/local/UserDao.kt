package com.example.foodkit.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodkit.model.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE user_table SET name = :name, email = :email ,imageUrl = :imageUrl, phoneNumber = :phoneNumber WHERE id = :id")
    suspend fun updateUserById(
        id: Int,
        name: String,
        email: String,
        imageUrl: String,
        phoneNumber: String,
    )

    @Query("DELETE FROM user_table WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE email = :email)")
    suspend fun doesEmailExist(email: String): Boolean

}