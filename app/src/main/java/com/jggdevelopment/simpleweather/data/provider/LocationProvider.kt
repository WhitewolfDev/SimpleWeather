package com.jggdevelopment.simpleweather.data.provider

import com.jggdevelopment.simpleweather.model.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getLatitude(): Double
    suspend fun getLongitude(): Double
}