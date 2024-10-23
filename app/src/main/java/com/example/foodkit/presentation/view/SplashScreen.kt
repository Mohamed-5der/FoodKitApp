package com.example.foodkit.presentation.view

import android.widget.Toast
import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodkit.Constants
import com.example.foodkit.R
import com.example.foodkit.navigation.Routes
import com.example.foodkit.utils.getLocationAndAddress
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController){
    val context = LocalContext.current
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)){
        Image(painter = painterResource(id = R.drawable.logo) , contentDescription =null ,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth())
    }

    LaunchedEffect(Unit) {
        val result = getLocationAndAddress(context)
        kotlinx.coroutines.delay(2000)
        if (FirebaseAuth.getInstance().currentUser != null) {
            if (FirebaseAuth.getInstance().currentUser?.email == Constants.ADMIN_EMAIL ||
                FirebaseAuth.getInstance().currentUser?.email == Constants.ADMIN_EMAIL2 ||
                FirebaseAuth.getInstance().currentUser?.email == Constants.ADMIN_EMAIL3
            ) {
               navController.navigate(Routes.MASTER)
            } else {
                    navController.navigate(Routes.MAIN)
            }
        } else {
            navController.navigate(Routes.LOGIN)
        }
    }

}