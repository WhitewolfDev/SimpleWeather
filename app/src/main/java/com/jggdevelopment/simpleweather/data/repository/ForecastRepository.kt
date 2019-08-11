package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.Currently
import com.jggdevelopment.simpleweather.data.db.entity.WeatherResponse

interface ForecastRepository {
    suspend fun getWeather(): LiveData<out WeatherResponse>
}