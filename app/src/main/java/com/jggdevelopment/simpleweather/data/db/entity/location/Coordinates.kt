package com.jggdevelopment.simpleweather.data.db.entity.location

import androidx.room.ColumnInfo

data class Coordinates (
        @ColumnInfo(name = "latitude")
        val latitude: Double,
        @ColumnInfo(name = "longitude")
        val longitude: Double
)