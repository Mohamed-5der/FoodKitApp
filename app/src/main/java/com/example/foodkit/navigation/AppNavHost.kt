package com.example.foodkit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodkit.Constants
import com.example.foodkit.local.AppPreferences
import com.example.foodkit.presentation.view.Account.ProfileScreen
import com.example.foodkit.presentation.view.CompleteSignUpScreen
import com.example.foodkit.presentation.view.LoginScreen
import com.example.foodkit.presentation.view.MainScreen
import com.example.foodkit.presentation.view.MasterScreen
import com.example.foodkit.presentation.view.ProductDetailsScreen
import com.example.foodkit.presentation.view.SignUpScreen
import com.example.foodkit.presentation.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val appPreferences = AppPreferences(LocalContext.current)
    appPreferences.init()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        if (FirebaseAuth.getInstance().currentUser?.email == Constants.ADMIN_EMAIL ) {
            Routes.MASTER
        } else {
            val email = appPreferences.getString("email", "")
            if (email == "" ) {
                Routes.COMPLETE_PROFILE
            }else{
                Routes.MAIN
            }
        }
    } else {
        Routes.LOGIN
    }
    NavHost(navController = navController, startDestination = startDestination ) {

        composable( Routes.LOGIN) { LoginScreen(navController) }

        composable( Routes.SIGNUP) { SignUpScreen(navController) }

        composable( Routes.COMPLETE_PROFILE) { CompleteSignUpScreen(navController) }

        composable(Routes.MAIN) { MainScreen(navController) }

        composable(Routes.MASTER) { MasterScreen(navController) }

        composable(Routes.PROFILE) { ProfileScreen(navController) }


        composable(Routes.FOOD_DETAILS) {
            val itemId = it.arguments?.getString("itemId") ?: ""
            val userId = it.arguments?.getString("userId") ?: ""
            ProductDetailsScreen(itemId, userId)
        }
    }
}
