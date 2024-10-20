package com.example.foodkit.presentation.view.Account

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.presentation.view.masterBottomNavagtion.OrderCard
import com.example.foodkit.presentation.viewModel.MasterViewModel
import com.example.foodkit.repository.Order
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyOrderScreen(navController: NavController,userId:String){
    val viewModel: MasterViewModel = koinViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }
    val orders =viewModel.orders.collectAsState().value
    val filteredOrders = orders.filter { order -> order.items.any { it.userId ==userId  }}

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(painter = painterResource(id = R.drawable.arrow_left),contentDescription =null,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }.padding(start = 16.dp,top = 30.dp))
                },

                title = { Text(
                    stringResource(id = R.string.orders), color = Color.Black,
                    fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    fontFamily = poppins,
                    modifier = Modifier
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Start
                )
                },
                backgroundColor = Color.White,
                elevation = 1.dp,
                modifier = Modifier.height(70.dp)
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp,bottom = 16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(filteredOrders?: emptyList()) { order ->
                    OrderCardUser(order = order,context)
                }
            }
        }
    }
}

@Composable
fun OrderCardUser(order: Order, context: Context) {
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
                androidx.compose.material3.Text(
                    text = "ID Order : ",
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color._black),
                    fontSize = 14.sp,
                    fontFamily = poppins
                )
                androidx.compose.material3.Text(
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
                androidx.compose.material3.Text(
                    text = "Total Price : $",
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color._black),
                    fontSize = 14.sp,
                    fontFamily = poppins
                )
                androidx.compose.material3.Text(
                    text = order.totalPrice.toString()
                    ,fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color._black),
                    fontSize = 14.sp,
                    fontFamily = poppins
                )
            }

            Column(
            ) {
                androidx.compose.material3.Text(
                    text = "Items :",
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color._black),
                    fontSize = 14.sp,
                    fontFamily = poppins
                )
                LazyRow() {
                    items(order.items){ item->
                        Card (
                            colors = CardDefaults.cardColors(colorResource(id = R.color.appColor)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier.padding(4.dp),
                            shape = RoundedCornerShape(8.dp),

                        ){
                            Row(modifier = Modifier.padding(horizontal = 4.dp)){
                                androidx.compose.material3.Text(text = item.foodName,
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(id = R.color.white),
                                    fontSize = 12.sp,
                                    fontFamily = poppins)
                                Spacer(modifier = Modifier.width(4.dp))
                                androidx.compose.material3.Text(text = item.quantity.toString()+"x",
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(id = R.color.white),
                                    fontSize = 12.sp,
                                    fontFamily = poppins)
                            }

                        }
                    }
                }
            }
        }
    }

}