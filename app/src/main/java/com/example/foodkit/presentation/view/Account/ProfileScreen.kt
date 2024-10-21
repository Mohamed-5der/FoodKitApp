package com.example.foodkit.presentation.view.Account


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.local.AppPreferences
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import com.example.foodkit.presentation.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
import java.io.File


@Composable
fun ProfileScreen(navController: NavController) {
    val userViewModel: UserViewModel = koinViewModel()
    val appPreferences = AppPreferences(LocalContext.current)
    appPreferences.init()
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    userViewModel.getUserByEmail(email)
    val user = userViewModel.user.collectAsState().value
    val userImagePath = user?.imageUrl

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .padding(start = 16.dp, top = 30.dp)
                    )
                },

                title = {
                    Text(
                        stringResource(id = R.string.your_profile), color = Color.Black,
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(contentAlignment = Alignment.BottomEnd) {


                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.profile_image,
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
            }

            Spacer(modifier = Modifier.height(24.dp))
            ProfileOptionItem(
                icon = ImageVector.vectorResource(id = R.drawable.profile_unselect),
                label = user?.name.toString() ?: "User Name"
            )
            ProfileOptionItem(
                icon = Icons.Outlined.Email,
                label = user?.email.toString() ?: "Email@gmail.com"
            )
            ProfileOptionItem(
                icon = Icons.Outlined.Phone,
                label = user?.phoneNumber.toString() ?: "01203498410"
            )

        }
    }


}

@Composable
fun ProfileOptionItem(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = colorResource(id = R.color.appColor),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label, fontSize = 18.sp,
            color = colorResource(id = R.color._black),
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .alpha(0.6f)
        )

    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.alpha(0.6f),
    )
    Spacer(modifier = Modifier.height(16.dp))
}
