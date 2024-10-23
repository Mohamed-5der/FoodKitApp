package com.example.foodkit.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

suspend fun getLocationAndAddress(context: Context): Pair<Location?, Pair<String?, String?>>? {

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return null
    }
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationResult: Location? = fusedLocationClient.lastLocation.await() // Use coroutines with extension function

    locationResult?.let { location ->
        val (city, country) = getCityAndCountryFromLocation(context, location.latitude, location.longitude)
        return Pair(location, Pair(city, country))
    }

    return null
}

private suspend fun getCityAndCountryFromLocation(context: Context, latitude: Double, longitude: Double): Pair<String?, String?> {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale("en", "US"))
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[0]
                val city = address.locality
                val country = address.countryName
                return@withContext Pair(city, country)
            }
        } catch (e: Exception) {
            Log.e("LocationError", "Error getting city and country: ${e.message}")
        }
        return@withContext Pair(null, null)
    }
}
