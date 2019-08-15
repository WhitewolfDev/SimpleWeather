package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse

interface LocationSearchRepository {
    suspend fun searchForLocation(query: String): LiveData<out LocationSearchResponse>
}