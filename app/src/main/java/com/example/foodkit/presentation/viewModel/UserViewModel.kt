package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.UserDao
import com.example.foodkit.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(private val userDao: UserDao) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun addUser(name: String, email: String, phoneNumber: String, imageUrl: String,onAddSuccess: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val newUser = User(name = name, email = email, phoneNumber = phoneNumber, imageUrl = imageUrl)
                userDao.insert(newUser)
                onAddSuccess(newUser.id.toString())
                Log.d("UserAddition", "User added successfully")
            } catch (e: Exception) {
                Log.e("UserAddition", "Error adding user: ${e.message}")
            }
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

    suspend fun checkIfEmailExists(email: String): Boolean {
        return userDao.doesEmailExist(email)
    }

    fun getUserByEmail(email: String){
         viewModelScope.launch {
             try {
                 _user.value = userDao.getUserByEmail(email)
             }catch (e:Exception){
                 Log.e("UserAddition", "Error adding user: ${e.message}")
             }
         }
    }
}