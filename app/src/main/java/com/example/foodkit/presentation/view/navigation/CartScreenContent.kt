package com.example.foodkit.presentation.view.navigation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodkit.R
import com.example.foodkit.components.CartFoodCard
import com.example.foodkit.components.poppins
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

 fun CartScreenContent() {
    var showDialog = remember { mutableStateOf(false) }  // State to show dialog

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {

        Column(
            modifier = Modifier
                .padding(bottom = 60.dp)
                .background(colorResource(id = R.color.white))
        ) {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(colorResource(id = R.color.white)),
                title = { Text(text = stringResource(id = R.string.cart), modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp), textAlign = TextAlign.Center)},
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.white),
                    titleContentColor = colorResource(id = R.color._black),
                    navigationIconContentColor = colorResource(id = R.color._black)
                ),
                scrollBehavior = null,

                )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    val cartItems = 4
                    items(cartItems) {
                        CartFoodCard()
                    }
                    item {
                        Button(
                            onClick = { showDialog.value = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
                        ) {
                            Text(
                                stringResource(id = R.string.checkout),
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }

                }
            }
            CheckoutBottomSheet(showDialog)

        }
    }
 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(isSheetOpen: MutableState<Boolean>) {
    val sheetState = androidx.compose.material3.rememberModalBottomSheetState()
      if (isSheetOpen.value) {
          Box {
              ModalBottomSheet(
                  sheetState = sheetState,
                  onDismissRequest = {
                      isSheetOpen.value = false
                  },
                  modifier = Modifier
                      .heightIn(600.dp)
                      .align(Alignment.BottomCenter),
                  containerColor = Color.White,
                  shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),

              ) {
                  CheckoutCardContent()
              }
          }

      }

}

@Composable
fun CheckoutCardContent() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutRow(label = "Subtotal", amount = "$950.00")
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutRow(label = "Delivery charge", amount = "$50.00")
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutRow(label = "Discount", amount = "-$50.00")
        Spacer(modifier = Modifier.height(12.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.5.dp
        )
        Spacer(modifier = Modifier.height(12.dp))
        CheckoutRow(label = "Total", amount = "$950.00")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Checkout Successful", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.appColor))
        ) {
            Text(
                text = "Proceed To Checkout",
                color = Color.White,
            )
        }
    }
}

@Composable
fun CheckoutRow(label: String, amount: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label,
            color = colorResource(id = R.color.gray),
            fontSize = 16.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium
        )
        Text(text = amount,
            color =Color.Black,
            fontSize = 16.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium)
    }
}

