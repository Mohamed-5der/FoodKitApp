package com.example.foodkit.di

import androidx.room.Room
import com.example.foodkit.local.AppPreferences
import com.example.foodkit.local.UserDatabase
import com.example.foodkit.presentation.viewModel.CartForTestViewModel
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.FavoriteFoodViewModel
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.presentation.viewModel.LoginViewModel
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.presentation.viewModel.SignUpViewModel
import com.example.foodkit.presentation.viewModel.UserViewModel
import com.example.foodkit.repository.CartRepository
import com.example.foodkit.repository.CategoryRepository
import com.example.foodkit.repository.FoodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            UserDatabase::class.java,
            "user_database"
        ).fallbackToDestructiveMigration().build()
    }
    // Provide the UserDao
    single { get<UserDatabase>().userDao() }
    // Provide the FavoriteFoodDao
    single { get<UserDatabase>().favoriteFoodDao() }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FoodRepository(get(),get()) }
    single { CategoryRepository(get(),get())}
    single { AppPreferences(get()) }
    single { AppPreferences(get()).init() }
    single { CartRepository(get(),get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { FoodListScreenViewModel(get()) }
    viewModel { FoodDetailViewModel(get(), get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { FavoriteFoodViewModel(get()) }
    viewModel { LogoutViewModel(get()) }
    viewModel { CartForTestViewModel(get()) }

}
