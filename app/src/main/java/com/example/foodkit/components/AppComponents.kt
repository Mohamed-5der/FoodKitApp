package com.example.foodkit.components

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.presentation.viewModel.FavoriteFoodViewModel
import com.example.foodkit.repository.Category
import com.example.foodkit.repository.Food
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun SelectImageButton(onImageSelected: (Uri) -> Unit) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Select Image")
    }
}



@Composable
fun FoodCard(food: Food, onClick: () -> Unit, onClickFavorite : () -> Unit) {

    // Check if the food is already a favorite when the composable is first loaded
//    LaunchedEffect(food) {
//        val favoriteFoods = favoriteViewModel.getFavoriteFoods(userId)}
//        isFavorite.value = favoriteFoods.any { it.name == food.name }
//    }
    val favoriteViewModel: FavoriteFoodViewModel = koinViewModel()
    val getFavoriteFoods = favoriteViewModel.favoriteFoods.collectAsState(initial = emptyList())
    val favoriteIds = favoriteViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current


    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
            .wrapContentHeight()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Card (colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),){
                    AsyncImage(
                        model = food.imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.food_photo),
                        error = painterResource(id = R.drawable.food_photo)
                    )
                }

                if (false) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .height(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "10%",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.white),
//                            fontFamily = poppins,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (food.price == food.price) {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(id = R.color.black),
//                            fontFamily = poppins,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
//                            fontFamily = poppins,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color._black),
//                            fontFamily = poppins,
                        )
                    }
                }
                val isFavorite = remember { mutableStateOf(favoriteIds.value.contains(food.id ?: ""))}

                Icon(
                    painterResource(
                        id = if (isFavorite.value) R.drawable.favorite_se else R.drawable.favorite_un
                    ),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clickable {
                            if (isFavorite.value) {
                                isFavorite.value = !isFavorite.value
                                favoriteViewModel.deleteFavoriteFood(food.id){
                                        Toast
                                            .makeText(context, "Removed from favorites", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                isFavorite.value = !isFavorite.value
                                favoriteViewModel.addFavoriteFood(
                                        name = food.name,
                                        imageUrl = food.imageUrl, price = food.price,
                                        description = food.description,
                                        rating = food.rating.toFloat(),
                                        category = "",
                                        numberRating = food.rating.toDouble(),
                                        idFood = food.id
                                    ) {
                                        Toast
                                            .makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        },
                    tint = colorResource(id = R.color.appColor)
//                    if (isSelected) colorResource(id = R.color.appColor) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = food.name ?: "",
                fontSize = 14.sp,
                minLines = 1,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color._black),
//                fontFamily = poppins,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = food.description ?: "",
                fontSize = 14.sp,
                minLines = 1,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color._black),
//                fontFamily = poppins,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val brand = "Molo".split(" ")?.getOrNull(0) ?: "Unknown"
                Text(
                    text = "From: $brand",
                    fontSize = 14.sp,
//                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = food.rating.toString(),
                        fontSize = 14.sp,
//                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
    }



@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    val id = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Card(
            shape = CircleShape,
            border = if (id.value == category.id) BorderStroke(
                2.dp,
                colorResource(id = R.color.appColor)
            ) else BorderStroke(0.dp, colorResource(id = R.color.black)),
            modifier = Modifier
                .size(80.dp)

        ) {
            Card(
                shape = CircleShape,
                border = if (id.value == category.id) BorderStroke(
                    3.dp,
                    colorResource(id = R.color.black)
                )
                else BorderStroke(3.dp, Color.LightGray),
                modifier = Modifier
                    .size(80.dp)
                    .clickable(onClick = onClick),
                elevation = CardDefaults.cardElevation(16.dp)

            ) {
                AsyncImage(
                    model = category.imageUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp),
            text = category.name,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun CartFoodCard(){
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = R.drawable.onboarding_photo1,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.food_photo),
                    error = painterResource(id = R.drawable.food_photo)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "item.name",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "item.price",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(12.dp))


            IconButton(
                onClick = { },
                modifier = Modifier
                    .padding(4.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.minus_icon),
                    tint = Color.White,
                    contentDescription = "Decrease quantity"

                )

            }

            Text(
                text = "1",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = { },
                modifier = Modifier
                    .padding(4.dp)
                    .background(
                        color = colorResource(id = R.color.appColor),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .size(24.dp)

            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase quantity",
                    tint = Color.White
                )
            }
        }
    }


}

val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.ExtraBold),
)
fun saveImageToFile(context: Context, bitmap: Bitmap): File? {
    val file = File(context.cacheDir, "image.jpg")
    return try {
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        file
    } catch (e: IOException) {
        e.printStackTrace()

        null
    }
}

fun imageRefactored(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val myBitmap = BitmapFactory.decodeStream(inputStream)

        val stream = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        inputStream!!.close()

        saveImageToFile(context, myBitmap)

    } catch (e: IOException) {
        e.printStackTrace()

        null
    }
}