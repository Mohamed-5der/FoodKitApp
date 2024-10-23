package com.example.foodkit.presentation.view.userBottomNavigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.SelectImageButton
import com.example.foodkit.components.poppins
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.presentation.viewModel.UpdateState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditScreen(
    navController: NavController,
    foodId: String,
    userId: String,
) {
    val foodDetailViewModel: FoodDetailViewModel =
        koinViewModel(parameters = { parametersOf(userId) })
    val masterViewModel: MasterViewModel = koinViewModel(parameters = { parametersOf(userId) })

    foodDetailViewModel.food?.let { food ->

        var foodName by remember { mutableStateOf(TextFieldValue(food.name)) }
        var foodDescription by remember { mutableStateOf(TextFieldValue(food.description)) }
        var price by remember { mutableStateOf(TextFieldValue(food.price.toString())) }
        var availableQuantity by remember { mutableStateOf(TextFieldValue(food.availableQuantity.toString())) }
        var calories by remember { mutableStateOf(TextFieldValue(food.calories.toString())) }
        var proteins by remember { mutableStateOf(TextFieldValue(food.protein.toString())) }
        var fats by remember { mutableStateOf(TextFieldValue(food.fats.toString())) }
        var existingImage by mutableStateOf<Uri?>(Uri.parse(food.imageUrl))
        var imageUrl by mutableStateOf<Uri?>(null)

        val updateState by masterViewModel.updateState.collectAsState()
        val context = LocalContext.current
        val scrollState = rememberScrollState()


        Surface(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(listOf(Color(0xFFff0000), Color(0xFFff9a00)))
                ),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.Transparent)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.edit_food_details),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                )

                SelectImageButton(onImageSelected = { Uri ->
                    imageUrl = Uri

                })


                if (imageUrl != null) {
                    imageUrl?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .size(300.dp)
                        )
                    }
                } else {
                    existingImage?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(35.dp))
                                .size(300.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text(stringResource(id = R.string.food_name), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = foodDescription,
                    onValueChange = { foodDescription = it },
                    label = { Text(stringResource(id = R.string.description), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(stringResource(id = R.string.price), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = availableQuantity,
                    onValueChange = { availableQuantity = it },
                    label = { Text(stringResource(id = R.string.available_quantity), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text(stringResource(id = R.string.calories), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = proteins,
                    onValueChange = { proteins = it },
                    label = { Text(stringResource(id = R.string.protein), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                OutlinedTextField(
                    value = fats,
                    onValueChange = { fats = it },
                    label = { Text(stringResource(id = R.string.fats), color = Color.Black,
                        fontFamily = poppins, fontWeight = FontWeight.Medium
                        , fontSize = 12.sp)},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = colorResource(id = R.color.appColor),
                        unfocusedBorderColor = Color.Black,
                        cursorColor = colorResource(id = R.color.appColor),
                        focusedLabelColor = colorResource(id = R.color.appColor),
                        unfocusedPrefixColor = colorResource(id = R.color.appColor),
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                when (updateState) {
                    is UpdateState.Idle -> {}

                    is UpdateState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(
                                Alignment.CenterHorizontally
                            )
                        )
                    }

                    is UpdateState.Error -> {
                        Toast.makeText(
                            context,
                            (updateState as UpdateState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UpdateState.Success -> {
                        Toast.makeText(context, "Food updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Button(
                    onClick = {
                        val updatedFood = food.copy(
                            name = foodName.text,
                            description = foodDescription.text,
                            price = price.text.toDouble(),
                            availableQuantity = availableQuantity.text.toInt(),
                            calories = calories.text.toInt(),
                            protein = proteins.text.toInt(),
                            fats = fats.text.toInt(),
                            imageUrl = imageUrl.toString()
                        )

                        masterViewModel.updateFood(
                            foodId = foodId,
                            updatedFood = updatedFood,
                            name = foodName.text,
                            description = foodDescription.text,
                            imageUri = imageUrl,
                            price = price.text.toDouble(),
                            availableQuantity = availableQuantity.text.toInt(),
                            calories = calories.text.toInt(),
                            protein = proteins.text.toInt(),
                            fats = fats.text.toInt()
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                ) {
                    Text(text = stringResource(id = R.string.save_changes) ,color = Color.White)
                }
                Button(onClick = {
                    masterViewModel.deleteFromFoods(
                        foodId = foodId,
                        imageUrl =food.imageUrl,
                        categoryId = food.category, {
                            Toast.makeText( context, "Food Deleted SuccessFully", Toast.LENGTH_SHORT).show()
                        },{
                            Toast.makeText( context, "Exception in Food Delete ", Toast.LENGTH_SHORT).show()
                        }
                        )
                    navController.popBackStack()
                },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                ) {
                    Text(text = stringResource(id = R.string.delete_the_food) , color = Color.White)
                }
            }
        }
    }
}
