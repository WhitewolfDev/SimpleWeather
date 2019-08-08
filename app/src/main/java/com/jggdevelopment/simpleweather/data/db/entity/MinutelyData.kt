package com.jggdevelopment.simpleweather.data.db.entity


import com.google.gson.annotations.SerializedName

data class MinutelyData(
    val precipIntensity: Double,
    val precipIntensityError: Double,
    val precipProbability: Double,
    val precipType: String,
    val time: Int
)