package com.jggdevelopment.simpleweather.data.db.entity

import androidx.room.Embedded


data class Daily(
        val `data`: List<DailyData>,
        val icon: String,
        val summary: String
)