package com.jggdevelopment.simpleweather.data.db.entity.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

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
        val precipIntensity: Double,
        @ColumnInfo(name = "precipProbability")
        val precipProbability: Double,
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
)