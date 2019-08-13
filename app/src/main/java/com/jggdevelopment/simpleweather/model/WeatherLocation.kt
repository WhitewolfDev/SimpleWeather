package com.jggdevelopment.simpleweather.model

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

class WeatherLocation (
    val latitude: Double,
    val longitude: Double
) {
    fun getLocationString(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        lateinit var addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var name = "Weather"
        if (addresses.isNotEmpty()) {
            when {
                addresses[0].locality != null -> name = addresses[0].locality
                addresses[0].subLocality != null -> name = addresses[0].subLocality
                addresses[0].subAdminArea != null -> name = addresses[0].subAdminArea
            }
        }

        return name
    }
}