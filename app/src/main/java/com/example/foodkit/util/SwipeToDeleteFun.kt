package com.khedr.ShopVerse.util

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.repository.CartItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDurationMillis: Int = 500,
    cartItem: CartItem,
    content: @Composable (RowScope.(T) -> Unit)
) {
    var showDialog = remember { mutableStateOf(false) }  // State to show dialog
    var isRemoved by remember { mutableStateOf(false) }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDialog.value = true
                false
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDurationMillis.toLong())
            onDelete(item)
        }
    }

    if (showDialog.value) {
        CreateBottomSheet(showDialog,onCancel = {
            showDialog.value= false
        }, onRemove = {
            isRemoved = true
            showDialog.value= false
        },cartItem=cartItem)

    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDurationMillis),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(animationDurationMillis))
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = { DeleteBackground(state) },
            content = { content(item) },
            enableDismissFromStartToEnd = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(state: SwipeToDismissBoxState) {
    val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red.copy(alpha = 0.4f)
    } else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBottomSheet(isSheetOpen: MutableState<Boolean>, onCancel: () -> Unit, onRemove: () -> Unit, cartItem: CartItem) {
    val numberOfItems = remember { mutableStateOf(1) }
    Box (modifier = Modifier.fillMaxSize()){
        if (isSheetOpen.value) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen.value = false
                },
                modifier = Modifier
                    .height(600.dp)
                    .align(Alignment.BottomCenter),

                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.remove_from_cart),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    androidx.compose.material.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.White),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AsyncImage(
                                model = cartItem.imageUrl,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(id = R.drawable.logo),
                                error = painterResource(id = R.drawable.logo)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                androidx.compose.material.Text(
                                    text = cartItem.foodName,
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                androidx.compose.material.Text(
                                    text = "$" + cartItem.foodPrice.toString(),
                                    color = Color.Gray,
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {

                                    },
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
                                androidx.compose.material.Text(
                                    text = cartItem.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    fontFamily = poppins,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                IconButton(
                                    onClick = {

                                    },
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
                                }                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onCancel()
                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.LightGray),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.cancel), color = colorResource(id = R.color.white),
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp)
                    }
                    Button(
                        onClick = {
                            onRemove()
                        },
                        shape = RoundedCornerShape(50),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(colorResource(id = R.color.appColor)),

                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.remove), color = Color.White,
                            fontFamily = poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp)
                    }
                }

            }
        }
    }


}
