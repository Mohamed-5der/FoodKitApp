package com.example.foodkit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodkit.Constants
import com.example.foodkit.presentation.view.AddCategoryScreen
import com.example.foodkit.presentation.view.CartScreenForTest
import com.example.foodkit.presentation.view.CategoryDetailScreen
import com.example.foodkit.presentation.view.CategoryListScreen
import com.example.foodkit.presentation.view.DetailsAnalysisForMaster
import com.example.foodkit.presentation.view.FoodDetailScreen
import com.example.foodkit.presentation.view.LoginScreen
import com.example.foodkit.presentation.view.MainScreen
import com.example.foodkit.presentation.view.MasterOrdersScreen
import com.example.foodkit.presentation.view.MasterScreen
import com.example.foodkit.presentation.view.ProductDetailsScreen
import com.example.foodkit.presentation.view.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {

    val curentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        if (FirebaseAuth.getInstance().currentUser?.email == Constants.ADMIN_EMAIL ) {
            Routes.MASTER
        } else {
            Routes.MAIN
        }
    } else {
        Routes.LOGIN
    }
    NavHost(navController = navController, startDestination = startDestination ) {

        composable( Routes.LOGIN) { LoginScreen(navController) }

        composable( Routes.SIGNUP) { SignUpScreen(navController) }

        composable(Routes.MAIN) { MainScreen(navController) }

        composable(Routes.MASTER) { MasterScreen(navController) }

        composable(Routes.PRODUCT_DETAILS) { ProductDetailsScreen() }

        composable(Routes.FOOD_DETAILS) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            if (itemId != null) {
                FoodDetailScreen(navController, itemId , curentUserId)
            }
        }

        composable(Routes.ADD_CATEGORY) { AddCategoryScreen(navController) }


        composable(Routes.CATEGORY_LIST) { CategoryListScreen(navController) }

        composable(Routes.CATEGORY_DETAIL) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
            CategoryDetailScreen(navController, categoryId)
        }

        composable(Routes.CART_FOR_TEST) {
            CartScreenForTest(navController, curentUserId)
        }

        composable(Routes.ORDERS_LIST) {
            MasterOrdersScreen()
        }

        composable(Routes.DETAILS_ANALYSIS) {backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId")
            if (itemId != null) {
                DetailsAnalysisForMaster(navController, itemId , curentUserId)
            }
        }


    }
}
