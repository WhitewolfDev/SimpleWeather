package com.jggdevelopment.simpleweather.data.db.entity


import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("nearest-station")
    val nearestStation: Double,
    val sources: List<String>,
    val units: String
)