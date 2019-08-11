package com.jggdevelopment.simpleweather.data.db.entity


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("nearest-station")
    @ColumnInfo(name = "nearestStation")
    val nearestStation: Double,
    @ColumnInfo(name = "units")
    val units: String
)