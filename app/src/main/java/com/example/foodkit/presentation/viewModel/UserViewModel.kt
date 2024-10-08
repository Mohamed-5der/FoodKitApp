package com.example.foodkit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.UserDao
import com.example.foodkit.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(private val userDao: UserDao) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

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

     fun getUserByEmail(email: String){
         viewModelScope.launch {
             _user.value = userDao.getUserByEmail(email)
         }

    }
}