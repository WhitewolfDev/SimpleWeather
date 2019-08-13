package com.jggdevelopment.simpleweather.data.db.entity.weather

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jggdevelopment.simpleweather.model.WeatherLocation
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val WEATHER_RESPONSE_ID = 0

@Entity(tableName = "weather_response")
data class WeatherResponse(
        @Embedded(prefix = "current_")
        val currently: Currently,
        @Embedded(prefix = "daily_")
        val daily: Daily,
        @Embedded(prefix = "flags_")
        val flags: Flags,
        @Embedded(prefix = "hourly_")
        val hourly: Hourly,
        @ColumnInfo(name = "latitude")
        val latitude: Double,
        @ColumnInfo(name = "longitude")
        val longitude: Double,
        @Embedded(prefix = "minutely_")
        val minutely: Minutely?,
        @ColumnInfo(name = "offset")
        val offset: Double,
        @ColumnInfo(name = "timezone")
        val timezone: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_RESPONSE_ID

    fun getLocation(): WeatherLocation {
            return WeatherLocation(latitude, longitude)
    }

    fun getLastFetchTime(): ZonedDateTime {
            val instant = Instant.ofEpochSecond(currently.time.toLong())
            val zoneId = ZoneId.of(timezone)
            return ZonedDateTime.ofInstant(instant, zoneId)
    }

    fun getTomorrowStartHour(): Int {
        val hoursLeft = (daily.data[1].time - currently.time) / 3600
        return if (hoursLeft % 3600 == 0)
            hoursLeft
        else hoursLeft + 1
    }
}