package com.jggdevelopment.simpleweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse
import com.jggdevelopment.simpleweather.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
        private val darkSkyWeatherApiService: DarkSkyWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<WeatherResponse>()
    override val downloadedWeatherResponse: LiveData<WeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(latitude: Double, longitude: Double, units: String, lanugageCode: String) {
        try {
            val fetchedCurrentWeather = darkSkyWeatherApiService
                    .getCurrentWeather(latitude, longitude, units, lanugageCode)
                    .await()

            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}