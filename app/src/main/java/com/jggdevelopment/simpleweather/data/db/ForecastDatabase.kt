package com.jggdevelopment.simpleweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter
import com.jggdevelopment.simpleweather.data.db.entity.WeatherResponse


@Database(
        entities = [WeatherResponse::class],
        version = 1
)
@TypeConverters(DataConverter::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): WeatherResponseDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ForecastDatabase::class.java, "forecast.db")
                        .build()
    }
}