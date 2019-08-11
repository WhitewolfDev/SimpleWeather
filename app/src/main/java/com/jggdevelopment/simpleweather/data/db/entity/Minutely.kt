package com.jggdevelopment.simpleweather.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter


data class Minutely(
        @TypeConverters(DataConverter::class)
        @ColumnInfo(name = "data")
        val `data`: List<MinutelyData>,
        @ColumnInfo(name = "icon")
        val icon: String,
        @ColumnInfo(name = "summary")
        val summary: String
)