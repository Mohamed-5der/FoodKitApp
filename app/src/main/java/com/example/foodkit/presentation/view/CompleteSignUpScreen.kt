package com.example.foodkit.presentation.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.local.AppPreferences
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun CompleteSignUpScreen(
    navController: NavController,
) {
    val viewModelDb: UserViewModel = koinViewModel()
    var userName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val appPreferences = AppPreferences(context)
    appPreferences.init()
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    var selectedImagePath by remember { mutableStateOf<String?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImagePath = it.toString()
        }

    }

    Surface(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.complete),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.thankYouForChoosingUs),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Image(
                    painter = rememberImagePainter(
                        data = if (selectedImagePath == null) R.drawable.profile_image
                        else selectedImagePath,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Card(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.appColor), CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .size(40.dp)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        },
                    shape = CircleShape,
                    colorResource(id = R.color.appColor)
                ) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.padding(8.dp),
                        tint = Color.White
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = email,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color._black),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Full Name Input
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(stringResource(id = R.string.userName), color = Color.Black) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.name_icon),
                        contentDescription = "Name Icon",
                        tint = Color.Black
                    )
                }
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(stringResource(id = R.string.phoneNumber), color = Color.Black) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Call,
                        contentDescription = "Name Icon",
                        tint = Color.Black
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedImagePath == null) {
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    } else if (userName.isEmpty() || phoneNumber.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModelDb.addUser(
                            name = userName,
                            email = email,
                            phoneNumber = phoneNumber, imageUrl = selectedImagePath!!
                        ) {
                            navController.navigate(Routes.MAIN)
                            appPreferences.putString("email", email)
                            Toast.makeText(context, "Complete Successful", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}