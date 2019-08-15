package com.jggdevelopment.simpleweather.data.db.entity.weather


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class MinutelyData(
    @ColumnInfo(name = "precipIntensity")
    val precipIntensity: Double,
    @ColumnInfo(name = "precipIntensityError")
    val precipIntensityError: Double,
    @ColumnInfo(name = "precipProbability")
    val precipProbability: Double,
    @ColumnInfo(name = "precipType")
    val precipType: String,
    @ColumnInfo(name = "time")
    val time: Int
)