package com.example.foodkit.presentation.view.navigation
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import com.example.foodkit.presentation.viewModel.UserViewModel
import okhttp3.Route
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreenContent(navController: NavController) {
    val userViewModel: UserViewModel = koinViewModel()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    stringResource(id = R.string.profile), color = Color.Black,
                    fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    fontFamily = poppins,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp,end = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
                        data = if (selectedImageUri==null)  R.drawable.profile_image
                            else  selectedImageUri,
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
                        .size(40.dp).clickable {
                            galleryLauncher.launch("image/*")
                        },
                    shape = CircleShape,
                    colorResource(id = R.color.appColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.padding(8.dp),
                        tint = Color.White
                    )
                }
                
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "user name",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                fontFamily = poppins,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.profile_unselect), label = stringResource(id = R.string.profile),route = Routes.PROFILE,navController)
            ProfileOptionItem(icon = Icons.Outlined.ShoppingCart, label = stringResource(id = R.string.orders),route = Routes.ORDERS,navController)
            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.gift), label = stringResource(id = R.string.coupons),route = Routes.COUPONS,navController)
            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.notification_icon), label = stringResource(id = R.string.notifications),route = Routes.NOTIFICATIONS,navController)
            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.setting), label =stringResource(id = R.string.settings),route = Routes.SETTINGS,navController)

            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.logout), label = stringResource(id = R.string.logout),route = Routes.LOGIN,navController)
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, label: String,route: String,navController: NavController) {

    val logoutViewModel: LogoutViewModel = koinViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {
                if (route == Routes.LOGIN) {
                    logoutViewModel.logout() {
                        navController.navigate(Routes.PROFILE)
                    }
                } else {
                    navController.navigate(Routes.PROFILE)                }
            },
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
            text = label, fontSize = 20.sp,
            color = colorResource(id = R.color._black),
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = label,
            tint = colorResource(id = R.color.appColor),
            modifier = Modifier.size(24.dp)
        )
    }
    Divider(
        color =Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.alpha(0.6f),
    )
    Spacer(modifier = Modifier.height(16.dp))
}

