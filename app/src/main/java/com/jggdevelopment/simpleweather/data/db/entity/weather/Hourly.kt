package com.jggdevelopment.simpleweather.data.db.entity.weather

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter


data class Hourly(
        @TypeConverters(DataConverter::class)
        @ColumnInfo(name = "data")
        val `data`: List<HourlyData>,
        @ColumnInfo(name = "icon")
        val icon: String,
        @ColumnInfo(name = "summary")
        val summary: String
)