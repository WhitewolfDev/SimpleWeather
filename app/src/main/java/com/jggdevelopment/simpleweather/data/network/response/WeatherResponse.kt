package com.jggdevelopment.simpleweather.data.network.response

import com.jggdevelopment.simpleweather.data.db.entity.*


data class WeatherResponse(
        val currently: Currently,
        val daily: Daily,
        val flags: Flags,
        val hourly: Hourly,
        val latitude: Double,
        val longitude: Double,
        val minutely: Minutely,
        val offset: Double,
        val timezone: String
)