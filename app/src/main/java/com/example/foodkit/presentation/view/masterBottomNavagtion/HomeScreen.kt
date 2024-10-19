package com.example.foodkit.presentation.view.masterBottomNavagtion

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.CategoryCard
import com.example.foodkit.components.SelectImageButton
import com.example.foodkit.components.poppins
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.CategoryViewModel
import com.example.foodkit.presentation.viewModel.FoodListScreenViewModel
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.repository.Food
import com.example.foodkit.repository.Order
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController,onClickAllOrder: () -> Unit) {
    val viewModel: MasterViewModel = koinViewModel()
    val categoryViewModel : CategoryViewModel = koinViewModel()
    val logoutViewModel: LogoutViewModel = koinViewModel()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val foodViewModel: FoodListScreenViewModel = koinViewModel()
    val foods: List<Food> = emptyList()
    val showDialogAddFood = remember { mutableStateOf(false) }
    val showDialogAddCategory = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }
    val orders =viewModel.orders.collectAsState().value
    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
    }
    val categories =categoryViewModel.categories.collectAsState().value

    val foodList =
        if (foods.isNotEmpty()) foods else foodViewModel.foods.collectAsState(initial = emptyList()).value
    LaunchedEffect(Unit) {
        if (foods.isEmpty()) {
            foodViewModel.loadAllFoods()
        }
    }

    Column (modifier = Modifier.background(Color.White)){
        TopAppBar(title = { Text("Foodie Master",
            fontSize =16.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)) }, windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
            backgroundColor = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.height(70.dp),
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Categories
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.all_category),
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )

                Text(
                    text = stringResource(id = R.string.add_category),
                    color = colorResource(id = R.color.appColor),
                    modifier = Modifier.clickable {
                        showDialogAddCategory.value = true
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )

            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                items(categories) { category ->
                    CategoryCard(category = category){

                    }
                }
            }
            //Foods
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.all_food),
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )
                Text(
                    text = stringResource(id = R.string.add_food),
                    color = colorResource(id = R.color.appColor),
                    modifier = Modifier.clickable {
                        showDialogAddFood.value = true
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )

            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                items(foodList?: emptyList()) { food ->
                    FoodCardMaster(food = food, onClick = {
                        navController.navigate( "details_analysis/${food.id}")
                    }, onClickEdit = {

                    })
                }
            }

            //Orders
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.orders),
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )
                Text(
                    text = stringResource(id = R.string.all_order),
                    color = colorResource(id = R.color.appColor),
                    modifier = Modifier.clickable {
                        onClickAllOrder()
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = poppins
                )

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
            ) {
                items(orders?: emptyList()) { order ->
                    OrderCard(order = order,context)
                }
            }

            // add food
            if (showDialogAddFood.value) {
                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp),
                    onDismissRequest = { showDialogAddFood.value = false },
                    confirmButton = {
                        Row() {
                            Button(onClick = { showDialogAddFood.value=false }
                                , modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(Color.Gray))
                            {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { viewModel.addFoodToCategory(context){
                                showDialogAddFood.value=false
                            } }
                                , modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor)))
                            {
                                Text("Add ")
                            }
                        }
                    },
                    title = { Text("Add Food",fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color._black),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center) },
                    text = {
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()))
                        {
                            SelectImageButton ({
                                    viewModel.selectedImageUri = it
                            },uri = viewModel.selectedImageUri)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                OutlinedTextField(
                                    value =  viewModel.foodName,
                                    onValueChange = {viewModel.foodName = it },
                                    label = { Text("Food Name", color = Color.Black,
                                        fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = viewModel.foodPrice,
                                    onValueChange = { viewModel.foodPrice = it },
                                    label = { Text("Price", color = Color.Black
                                           , fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp)  },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                OutlinedTextField(
                                    value = viewModel.availableQuantityNumber,
                                    onValueChange = { viewModel.availableQuantityNumber = it },
                                    label = { Text("Available", color = Color.Black,
                                        fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = viewModel.calories,
                                    onValueChange = { viewModel.calories = it },
                                    label = { Text("Calories", color = Color.Black
                                        , fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp)  },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                OutlinedTextField(
                                    value = viewModel.protein,
                                    onValueChange = { viewModel.protein = it },
                                    label = { Text("Protein", color = Color.Black,
                                        fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = viewModel.fats,
                                    onValueChange = { viewModel.fats = it },
                                    label = { Text("Fats", color = Color.Black
                                        , fontFamily = poppins, fontWeight = FontWeight.Medium
                                        , fontSize = 12.sp)  },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.foodDescription,
                                onValueChange = { viewModel.foodDescription = it },
                                label = { Text("Food Description", color = Color.Black,
                                    fontFamily = poppins, fontWeight = FontWeight.Medium
                                    , fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )
                            val  selectedItem = viewModel.selectedCategory?:""
                            var selectedCategory by remember { mutableStateOf(selectedItem) }
                            if (selectedCategory==""){
                                selectedCategory = categories[0].name
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = { },
                                label = { Text("Select Category", color = Color.Black,
                                    fontFamily = poppins, fontWeight = FontWeight.Medium
                                    , fontSize = 12.sp) },
                                trailingIcon = {
                                    Icons.Filled.ArrowDropDown.let {
                                        androidx.compose.material.Icon(
                                            imageVector = it,
                                            contentDescription = "Dropdown arrow",
                                            modifier = Modifier.clickable {
                                                expanded = !expanded
                                            },
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(text = category.name) },
                                        onClick = {
                                            viewModel.selectedCategory = category.name
                                            expanded = false
                                        }
                                    )
                                }
                            }


                        } },
                    containerColor = Color.White

                )
            }
            if (showDialogAddCategory.value) {
                AlertDialog(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = { showDialogAddCategory.value = false },
                    confirmButton = {
                        Row() {
                            Button(onClick = { showDialogAddCategory.value=false }
                                , modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(Color.Gray))
                            {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { categoryViewModel.addCategory(context) }
                                , modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor)))
                            {
                                Text("Add ")
                            }
                        }
                    },
                    title = { Text("Add Category",fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color._black),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)  },
                    text = {
                        Column(modifier = Modifier
                            .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            SelectImageButton ({
                                categoryViewModel.selectedImageUri = it
                            },uri = categoryViewModel.selectedImageUri)

                            OutlinedTextField(
                                value = categoryViewModel.categoryName,
                                onValueChange = { categoryViewModel.categoryName = it },
                                label = { Text("Category Name",color = Color.Black,
                                    fontFamily = poppins, fontWeight = FontWeight.Medium
                                    , fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        },
                    containerColor = Color.White
                )
            }

            Spacer(modifier = Modifier.height(70.dp))


        }
    }

}


@Composable
fun FoodCardMaster(food: Food, onClick: () -> Unit, onClickEdit : () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp)
            .wrapContentHeight()
            .padding(end = 4.dp, start = 4.dp)
            .clickable(onClick = { onClick() }),
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
                            fontFamily = poppins,
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (food.price == food.price) {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(id = R.color.black),
                            fontFamily = poppins,
                            modifier = Modifier.weight(1f)
                        )
                    } else {

                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            fontFamily = poppins,

                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "£${food.price}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color._black),
                            fontFamily = poppins,
                        )
                    }
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        text = food.rating.toString(),
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )

                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = food.name ?: "",
                fontSize = 14.sp,
                minLines = 1,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color._black),
                fontFamily = poppins,
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
                fontFamily = poppins,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

        }
    }
}

@Composable
fun OrderCard(order: Order, context: Context) {
        Card(
            elevation= CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ID Order : ",
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color._black),
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    Text(
                        text = order.id,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color._black),
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Price : $",
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color._black),
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    Text(
                        text = order.totalPrice.toString()
                        ,fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color._black),
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                }

                Column(
                ) {
                    Text(
                        text = "Items :",
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color._black),
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    LazyRow() {
                        items(order.items){ item->
                            Card (
                                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier.padding(4.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, colorResource(id = R.color.gray))

                            ){
                                Row(modifier = Modifier.padding(horizontal = 4.dp)){
                                    Text(text = item.foodName,
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color._black),
                                        fontSize = 12.sp,
                                        fontFamily = poppins)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = item.quantity.toString()+"x",
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color._black),
                                        fontSize = 12.sp,
                                        fontFamily = poppins)
                                }

                            }
                        }
                    }
                }
                Row(modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        Toast.makeText(context,"Order Cancelled",Toast.LENGTH_SHORT).show()
                    }
                        , modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(Color.Gray),
                        shape = RoundedCornerShape(12.dp))
                    {
                        Text("Cancel Order",
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.white),
                            fontSize = 12.sp,
                            fontFamily = poppins,
                            modifier = Modifier.fillMaxHeight())
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        Toast.makeText(context,"Order Confirmed",Toast.LENGTH_SHORT).show()
                    }, modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                        ,shape = RoundedCornerShape(12.dp))
                    {
                        Text("Confirm Order ")
                    }
                }

            }
        }

}