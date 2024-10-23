package com.example.foodkit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodkit.presentation.view.Account.MyOrderScreen
import com.example.foodkit.presentation.view.CategoryDetailScreen
import com.example.foodkit.presentation.view.CategoryListScreen
import com.example.foodkit.presentation.view.Account.ProfileScreen
import com.example.foodkit.presentation.view.CompleteSignUpScreen
import com.example.foodkit.presentation.view.LoginScreen
import com.example.foodkit.presentation.view.MainDetailsAnalysis
import com.example.foodkit.presentation.view.MainScreen
import com.example.foodkit.presentation.view.MasterScreen
import com.example.foodkit.presentation.view.ProductDetailsScreen
import com.example.foodkit.presentation.view.SignUpScreen
import com.example.foodkit.presentation.view.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val startDestination = Routes.SPLASH

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) { LoginScreen(navController) }

        composable(Routes.SIGNUP) { SignUpScreen(navController) }

        composable(Routes.COMPLETE_PROFILE) { CompleteSignUpScreen(navController) }

        composable(Routes.MAIN) { MainScreen(navController) }

        composable(Routes.MASTER) { MasterScreen(navController) }

        composable(Routes.PROFILE) { ProfileScreen(navController) }

        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.ORDERS) { MyOrderScreen(navController, currentUserId) }

//        composable(Routes.ADD_CATEGORY) { AddCategoryScreen(navController) }


        composable(Routes.CATEGORY_LIST) { CategoryListScreen(navController) }

        composable(Routes.CATEGORY_DETAIL) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
            CategoryDetailScreen(navController, categoryId)
        }

        composable(Routes.FOOD_DETAILS) {
            val itemId = it.arguments?.getString("itemId") ?: ""
            ProductDetailsScreen(itemId, navController)
        }

        composable(Routes.DETAILS_ANALYSIS) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId")
            if (itemId != null) {
                MainDetailsAnalysis(navController, itemId, currentUserId)
            }
        }


    }
}
