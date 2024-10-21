package com.example.foodkit.location


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import java.util.Locale

/*
class LocationHelper(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//    private var locationRequest: LocationRequest = LocationRequest.create().apply {
//        interval = 10000 // Set the interval to check the location
//        fastestInterval = 5000
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
    private lateinit var locationCallback: LocationCallback

    // Main function that handles location logic
    fun getLocationFun(onLocationResult: (String) -> Unit) {
        if (checkLocationPermission()) {
            // Permission is granted, request location
            requestLocation(onLocationResult)
        } else {
            // Permission is not granted, request it from the user
            // Implement the permission request logic in your activity
            onLocationResult("Permission not granted")
        }
    }

    // Check if location permissions are granted
//    private fun checkLocationPermission(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }

//    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//        location = "Lat: ${loc?.latitude}, Lng: ${loc?.longitude}"

    /*
    import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestLocationPermission() {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher to request location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
        }
    )

    if (!hasLocationPermission) {
        // Ask for permission when the Composable is displayed
        LaunchedEffect(Unit) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else {
        Text(text = "Location Permission Granted!")
    }
}
     */

    @Composable
    fun RequestLocationPermission(context: Context) {
//        val context = LocalContext.current
        var hasLocationPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        // Launcher to request location permission
        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasLocationPermission = isGranted
            }
        )

        if (!hasLocationPermission) {
            // Ask for permission when the Composable is displayed
            LaunchedEffect(Unit) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestLocation(onLocationResult: (String) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let {
                    // Get the location coordinates (latitude, longitude)
                    val location = "${it.latitude}, ${it.longitude}"
                    // Convert to human-readable address
                    val address = getAddressFromLatLng(it)
                    onLocationResult(address)
                }
            }
        }

        // Request location updates
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
    }

    /*
    import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            address.getAddressLine(0) // Full address
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
     */
    // Convert latitude and longitude to a readable address
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                address.getAddressLine(0) // Full address
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
*/


/*
@Composable
fun LocationApp(fusedLocationClient: FusedLocationProviderClient) {
    var permissionGranted by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("") }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    // Check if permission is already granted ----1
    if (ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        permissionGranted = true
    }

    // UI with two buttons
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Request permission button----2
        if (!permissionGranted) {
            Button(
                onClick = { requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
            ) {
                Text(text = "Request Location Permission")
            }
            // Get location button----3
        } else {
            Button(
                onClick = {
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                        location = "Lat: ${loc?.latitude}, Lng: ${loc?.longitude}"
                    }
                }
            ) {
                Text(text = "Get Location")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = location)
        }
    }
}

 */