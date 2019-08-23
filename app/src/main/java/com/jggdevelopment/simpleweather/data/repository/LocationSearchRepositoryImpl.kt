package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.LocationResponseDao
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse
import com.jggdevelopment.simpleweather.data.network.LocationNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationSearchRepositoryImpl (
        private val locationResponseDao: LocationResponseDao,
        private val locationNetworkDataSource: LocationNetworkDataSource
): LocationSearchRepository {

    init {
        // locationResponseDao.nukeTable()
        locationNetworkDataSource.downloadedLocationSearchResults.observeForever { locationResults ->
            persistFetchedLocations(locationResults)
        }
    }

    // update search data in db if necessary, then return the data that was searched for.
    override suspend fun searchForLocation(query: String): LiveData<out LocationSearchResponse> {
        return withContext(Dispatchers.IO) {
            initSearch(query)
            return@withContext locationResponseDao.searchForLocation(query)
        }
    }

    // if a fetch is necessary (query has not already been searched), fetch search results
    private suspend fun initSearch(query: String) {
        if (isFetchLocationResultsNeeded(query))
            fetchLocationResults(query)
    }

    private fun isFetchLocationResultsNeeded(query: String) : Boolean {
        // get the cached results.  If it's null, return true because it needs to be updated
        val cachedResults = locationResponseDao.searchForLocationNonLive(query.toLowerCase())

        if (cachedResults == null) return true

        // if the results are empty, it needs to be fetched, else it doesn't
        return cachedResults.features.isEmpty()
    }

    private suspend fun fetchLocationResults(query: String) {
        locationNetworkDataSource.fetchLocationSearchResults("mapbox.places", query)
    }

    private fun persistFetchedLocations(fetchedLocationResults: LocationSearchResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            locationResponseDao.upsert(fetchedLocationResults)
        }
    }
}