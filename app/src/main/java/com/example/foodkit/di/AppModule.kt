package com.example.foodkit.di

import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.presentation.viewModel.LoginViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.presentation.viewModel.SignUpViewModel
import com.example.foodkit.repository.FoodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FirebaseAuth.getInstance() }
    single { FoodRepository(FirebaseFirestore.getInstance()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { FoodListScreenViewModel(get()) }
}
