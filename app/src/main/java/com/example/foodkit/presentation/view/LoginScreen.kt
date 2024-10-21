package com.example.foodkit.presentation.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.foodkit.R
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.LoginState
import com.example.foodkit.presentation.viewModel.LoginViewModel
import com.example.foodkit.presentation.viewModel.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val authState by viewModel.loginStateFlow.collectAsState()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.welcomeBack),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.putYourDataHere),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))


            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email), color = Color.Black) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email_icon),
                        contentDescription = "Email Icon",
                        tint = Color.Black
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password), color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock_icon),
                        contentDescription = "Password Icon",
                        tint = Color.Black
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                trailingIcon = {
                    val image =
                        if (passwordVisible) {
                            painterResource(id = R.drawable.show_password)
                        } else {
                            painterResource(id = R.drawable.hide_password)
                        }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Black

                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = stringResource(id = R.string.forgotPassword_),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { },
                color = Color.Black
            )




            Spacer(modifier = Modifier.height(32.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red)

            }
            when (authState) {
                is LoginState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                is LoginState.Error -> {
                    Column {
                        Text(
                            text = (authState as LoginState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = { viewModel.login(email, password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                        ) {
                            Text(
                                text = stringResource(id = R.string.signIn),
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                is LoginState.AdminSuccess -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Routes.MASTER)
                    }
                }

                is LoginState.UserSuccess -> {
                    LaunchedEffect(Unit) {
                         val emailExists = userViewModel.checkIfEmailExists(email)
                            if (emailExists) {
                                navController.navigate(Routes.MAIN)
                            } else {
                                navController.navigate(Routes.COMPLETE_PROFILE)

                        userViewModel.getUserByEmail(email)
                        val user = userViewModel.user.value
                            }
                    }
                }

                else -> {
                    Button(
                        onClick = {
                            viewModel.login(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                    ) {
                        Text(
                            text = stringResource(id = R.string.signIn),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Text
            TextButton(onClick = { navController.navigate(Routes.SIGNUP) }) {
                Row {
                    Text(
                        text = stringResource(id = R.string.dontHaveAnAccount),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.signUp),
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.appColor)
                    )
                }
            }


        }
    }

}
