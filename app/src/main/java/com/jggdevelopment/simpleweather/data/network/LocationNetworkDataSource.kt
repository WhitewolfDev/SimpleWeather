package com.jggdevelopment.simpleweather.data.network

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse

interface LocationNetworkDataSource {
    val downloadedLocationSearchResults: LiveData<LocationSearchResponse>

    suspend fun fetchLocationSearchResults(
            endpoint: String,
            query: String
    )
}