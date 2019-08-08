package com.jggdevelopment.simpleweather.data.db.entity

import androidx.room.Embedded


data class Hourly(
        val `data`: List<HourlyData>,
        val icon: String,
        val summary: String
)