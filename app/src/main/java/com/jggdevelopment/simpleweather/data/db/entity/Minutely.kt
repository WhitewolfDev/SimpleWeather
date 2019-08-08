package com.jggdevelopment.simpleweather.data.db.entity

import androidx.room.Embedded


data class Minutely(
        val `data`: List<MinutelyData>,
        val icon: String,
        val summary: String
)