package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse

interface ForecastRepository {
    suspend fun getWeatherWithLocation(): LiveData<out WeatherResponse>
    suspend fun getWeatherWithCoordinates(latitude: Double, longitude: Double): LiveData<out WeatherResponse>
}