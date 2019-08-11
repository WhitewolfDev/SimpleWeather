package com.jggdevelopment.simpleweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jggdevelopment.simpleweather.data.db.entity.Currently
import com.jggdevelopment.simpleweather.data.db.entity.WEATHER_RESPONSE_ID
import com.jggdevelopment.simpleweather.data.db.entity.WeatherResponse

@Dao
interface WeatherResponseDao {

    // UPdate or inSERT.  There can only ever be one Currently weather object in the db
    // so if it doesn't exist, insert it, and if it does, update it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry : WeatherResponse)

    // select everything from the table where the ID matches CURRENT_WEATHER_ID
    // This should only ever be one thing, since there's only one Currently in the table
    @Query("select * from weather_response where id = $WEATHER_RESPONSE_ID")
    fun getWeather() : LiveData<WeatherResponse>
}