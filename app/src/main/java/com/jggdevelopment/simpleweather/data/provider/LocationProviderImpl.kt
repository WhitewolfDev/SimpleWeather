package com.jggdevelopment.simpleweather.data.provider

import com.jggdevelopment.simpleweather.model.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return false
    }

    override suspend fun getLatitude(): Double {
        return 36.0
    }

    override suspend fun getLongitude(): Double {
        return -79.0
    }
}