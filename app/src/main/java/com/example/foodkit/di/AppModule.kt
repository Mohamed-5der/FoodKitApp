package com.example.foodkit.di

import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.presentation.viewModel.LoginViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.presentation.viewModel.SignUpViewModel
import com.example.foodkit.repository.CategoryRepository
import com.example.foodkit.repository.FoodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FoodRepository(get(),get()) }
    single { CategoryRepository(get(),get())}

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { FoodListScreenViewModel(get()) }
    viewModel { FoodDetailViewModel(get(), get()) }
    viewModel { CategoryViewModel(get()) }
}
