package com.jggdevelopment.simpleweather.data.network

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.network.response.WeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<WeatherResponse>

    suspend fun fetchCurrentWeather (
            latitude: Double,
            longitude: Double,
            units: String,
            lanugageCode: String
    )
}