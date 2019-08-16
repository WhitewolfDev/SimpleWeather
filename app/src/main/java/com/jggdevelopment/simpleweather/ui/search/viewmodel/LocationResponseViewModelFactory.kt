package com.jggdevelopment.simpleweather.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jggdevelopment.simpleweather.data.repository.LocationSearchRepository

class LocationResponseViewModelFactory (
        private val locationSearchRepository: LocationSearchRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationResponseViewModel(locationSearchRepository) as T
    }
}