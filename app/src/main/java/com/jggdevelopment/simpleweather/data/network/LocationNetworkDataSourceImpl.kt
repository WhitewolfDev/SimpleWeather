package com.jggdevelopment.simpleweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse
import com.jggdevelopment.simpleweather.internal.NoConnectivityException

class LocationNetworkDataSourceImpl (
        private val mapboxApiService: MapboxApiService
) : LocationNetworkDataSource {

    private val _downloadedLocationSearchResults = MutableLiveData<LocationSearchResponse>()
    override val downloadedLocationSearchResults: LiveData<LocationSearchResponse>
        get() = _downloadedLocationSearchResults

    override suspend fun fetchLocationSearchResults(endpoint: String, query: String) {
        try {
            val fetchedLocationSearchResults = mapboxApiService
                    .searchPlaces(endpoint, query)
                    .await()

            _downloadedLocationSearchResults.postValue(fetchedLocationSearchResults)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}