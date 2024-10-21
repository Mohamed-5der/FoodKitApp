package com.example.foodkit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginStateFlow = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                if (result.user?.email == "admin@gmail.com" || result.user?.email == "khedrkhedr370@gmail.com") {
                    // إذا كان المستخدم هو admin
                    _loginState.value = LoginState.AdminSuccess(result.user)
                } else {
                    // مستخدم عادي
                    _loginState.value = LoginState.UserSuccess(result.user)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error logging in")
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class AdminSuccess(val user: Any?) : LoginState()
    data class UserSuccess(val user: Any?) : LoginState()
    data class Error(val message: String) : LoginState()
}



