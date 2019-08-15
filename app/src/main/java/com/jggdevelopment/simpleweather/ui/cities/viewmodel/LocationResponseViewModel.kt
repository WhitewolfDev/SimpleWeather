package com.jggdevelopment.simpleweather.ui.cities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse
import com.jggdevelopment.simpleweather.data.repository.LocationSearchRepository

class LocationResponseViewModel (
        private val locationSearchRepository: LocationSearchRepository
) : ViewModel() {
    lateinit var locationResponse: LiveData<out LocationSearchResponse>

    suspend fun searchLocation(query: String) {
        locationSearchRepository.searchForLocation(query)
    }
}