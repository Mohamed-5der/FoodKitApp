package com.example.foodkit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.UserDao
import com.example.foodkit.model.User
import kotlinx.coroutines.launch


class UserViewModel(private val userDao: UserDao) : ViewModel() {

    fun addUser(name: String, email: String, phoneNumber: String, imageUrl: String) {
        viewModelScope.launch {
            val newUser = User(name = name, email = email, phoneNumber = phoneNumber, imageUrl = imageUrl)
            userDao.insert(newUser)
        }
    }

    fun updateUser(id : Int, name: String, email: String, phoneNumber: String, imageUrl: String) {
        viewModelScope.launch {
            userDao.updateUserById(id, name, email, phoneNumber, imageUrl)
            // Update the list of users after update
        }
    }

    fun deleteUser(id : Int) {
        viewModelScope.launch {
            userDao.deleteUserById(id)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}