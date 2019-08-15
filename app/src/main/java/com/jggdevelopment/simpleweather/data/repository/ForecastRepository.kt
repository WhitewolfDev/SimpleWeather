package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse

interface ForecastRepository {
    suspend fun getWeather(): LiveData<out WeatherResponse>
}