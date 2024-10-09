package com.example.foodkit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkit.local.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpStateFlow = _signUpState.asStateFlow()

    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (id: String) -> Unit,
    ) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                _signUpState.value = SignUpState.Success(result.user)
                onSignUpSuccess(result.user?.uid.toString())
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Error creating account")
            }
        }
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val user: Any?) : SignUpState()
    data class Error(val message: String) : SignUpState()
}
