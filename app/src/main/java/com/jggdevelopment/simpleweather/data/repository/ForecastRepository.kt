package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.Currently

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<out Currently>
}