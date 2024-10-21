package com.example.foodkit.presentation.view.masterBottomNavagtion

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.poppins
import com.example.foodkit.navigation.Routes
import com.example.foodkit.presentation.view.navigation.ProfileOptionItem
import com.example.foodkit.presentation.viewModel.LogoutViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun StaffScreen(navController: NavController) {
    Column (
        modifier = Modifier.background(Color.White)
    ){
        TopAppBar(
            title = {
                Text(
                    "Staff Master",
                    fontSize = 16.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier
                        .fillMaxWidth().padding(top = 30.dp)
                )
            },
            windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
            backgroundColor = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.height(70.dp)

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp).padding(horizontal = 16.dp)
                .background(Color.White),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
            AddStaff(name = "Mohamed Khedr", phone = "+201203898109", email ="khedrkhedr370@gmail.com" , imageVector =R.drawable.mohamed,"https://www.linkedin.com/in/mohamed-khedr-186861244/" )
            Spacer(modifier = Modifier.height(16.dp))
            AddStaff(name = "Abdelmalek mukhtar", phone = "+201099672981", email ="abdelmalekmokhtar83@gmail.com" , imageVector =R.drawable.abdelmalek,"https://www.linkedin.com/in/abdelmalek-mokhtar-476033287?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app")
            Spacer(modifier = Modifier.height(16.dp))
            AddStaff(name = "Ahmed Emad", phone = "+201029550695", email ="ahmed.emad.23112003@gmail.com" , imageVector =R.drawable.emad ,"https://www.linkedin.com/in/ahmed-emad-%F0%9F%87%B5%F0%9F%87%B8-010a52262/")
            Spacer(modifier = Modifier.height(16.dp))
            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.profile_un), label = "View user",route = Routes.MAIN,navController)
            ProfileOptionItem(icon = ImageVector.vectorResource(id = R.drawable.logout), label = stringResource(id = R.string.logout),route = Routes.LOGIN,navController)
        }
    }

}
@Composable
fun AddStaff(name:String, phone:String, email:String, imageVector: Int, link:String){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageVector,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth().weight(1f)
                .padding(vertical = 12.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = colorResource(id = R.color.appColor),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                androidx.compose.material.Text(
                    text = name,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color._black),
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,

                    )


            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Outlined.Call,
                    contentDescription = null,
                    tint = colorResource(id = R.color.appColor),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                androidx.compose.material.Text(
                    text = phone, fontSize = 14.sp,
                    color = colorResource(id = R.color._black),
                    fontFamily = poppins,
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Medium,

                    )
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(context, intent, null) },
                    modifier = Modifier.padding(end = 16.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.appColor))) {
                    Text(text = "Connect",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.white),
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,)

                }

            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null,
                    tint = colorResource(id = R.color.appColor),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                androidx.compose.material.Text(
                    text = email, fontSize = 12.sp,
                    color = colorResource(id = R.color._black),
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,

                    )

            }


        }
        Spacer(modifier = Modifier.width(8.dp))

    }
    Divider(
        color =Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.alpha(0.6f),
    )
}