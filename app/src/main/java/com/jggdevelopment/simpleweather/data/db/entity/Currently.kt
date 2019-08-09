package com.jggdevelopment.simpleweather.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// current weather ID is constant so there can only ever be one in the table
const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class Currently(
        @ColumnInfo(name = "apparentTemperature")
        val apparentTemperature: Double,
        @ColumnInfo(name = "cloudCover")
        val cloudCover: Double,
        @ColumnInfo(name = "dewPoint")
        val dewPoint: Double,
        @ColumnInfo(name = "humidity")
        val humidity: Double,
        @ColumnInfo(name = "icon")
        val icon: String,
        @ColumnInfo(name = "nearestStormBearing")
        val nearestStormBearing: Int,
        @ColumnInfo(name = "nearestStormDistance")
        val nearestStormDistance: Int,
        @ColumnInfo(name = "ozone")
        val ozone: Double,
        @ColumnInfo(name = "precipIntensity")
        val precipIntensity: Int,
        @ColumnInfo(name = "precipProbability")
        val precipProbability: Int,
        @ColumnInfo(name = "pressure")
        val pressure: Double,
        @ColumnInfo(name = "summary")
        val summary: String,
        @ColumnInfo(name = "temperature")
        val temperature: Double,
        @ColumnInfo(name = "time")
        val time: Int,
        @ColumnInfo(name = "uvIndex")
        val uvIndex: Int,
        @ColumnInfo(name = "visibility")
        val visibility: Double,
        @ColumnInfo(name = "windBearing")
        val windBearing: Int,
        @ColumnInfo(name = "windGust")
        val windGust: Double,
        @ColumnInfo(name = "windSpeed")
        val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}