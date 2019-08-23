package com.jggdevelopment.simpleweather.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse

@Dao
interface LocationResponseDao {

    // update or insert existing entry if there is a conflict when adding to db
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(locationResults: LocationSearchResponse)

    @Query("select * from location_search_results WHERE searchQuery = :query")
    fun searchForLocation(query: String): LiveData<LocationSearchResponse>

    @Query("select * from location_search_results WHERE searchQuery = :query")
    fun searchForLocationNonLive(query: String): LocationSearchResponse?

    @Query("delete from location_search_results")
    fun nukeTable()
}