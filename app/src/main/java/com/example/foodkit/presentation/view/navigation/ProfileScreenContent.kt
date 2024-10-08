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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.foodkit.R
import com.example.foodkit.presentation.viewModel.UserViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreenContent() {
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
                title = { Text("Profile", color = Color.Black,
                    fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                backgroundColor = Color.White,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image
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
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {galleryLauncher.launch("image/*") },
                    modifier = Modifier.size(32.dp)
                        .background(color = colorResource(id = R.color.appColor), CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White
                    )
                }
                
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "user name",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileOptionItem(icon = Icons.Default.Person, label = "Your Profile")
            ProfileOptionItem(icon = Icons.Default.ShoppingCart, label = "Your Coupon")
            ProfileOptionItem(icon = Icons.Default.Settings, label = "Setting")
            ProfileOptionItem(icon = Icons.Default.ExitToApp, label = "Log Out")
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black)
    }
}

