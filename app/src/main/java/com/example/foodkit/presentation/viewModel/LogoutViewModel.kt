package com.example.foodkit.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LogoutViewModel(private val firebaseAuth: FirebaseAuth): ViewModel() {
    fun logout(onLogoutSuccess: () -> Unit) {
        try {
            firebaseAuth.signOut()
            onLogoutSuccess()
        }catch (e: Exception){
            Log.e("LogoutViewModel", "Error logging out", e)
        }
    }
}