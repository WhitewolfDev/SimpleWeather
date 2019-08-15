package com.jggdevelopment.simpleweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse

@Database(
        entities = [LocationSearchResponse::class],
        version = 1
)
@TypeConverters(DataConverter::class)
abstract class LocationSearchDatabase : RoomDatabase() {
    abstract fun locationSearchDao(): LocationResponseDao

    companion object {
        @Volatile private var instance: LocationSearchDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        LocationSearchDatabase::class.java, "locationSearch.db")
                        .build()
    }
}