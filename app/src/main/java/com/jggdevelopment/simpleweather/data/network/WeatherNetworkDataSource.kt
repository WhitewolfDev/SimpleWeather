package com.jggdevelopment.simpleweather.data.network

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.WeatherResponse

interface WeatherNetworkDataSource {
    val downloadedWeatherResponse: LiveData<WeatherResponse>

    suspend fun fetchCurrentWeather (
            latitude: Double,
            longitude: Double,
            units: String,
            lanugageCode: String
    )
}