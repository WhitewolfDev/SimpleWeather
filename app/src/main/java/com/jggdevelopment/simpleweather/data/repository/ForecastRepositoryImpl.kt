package com.jggdevelopment.simpleweather.data.repository

import androidx.lifecycle.LiveData
import com.jggdevelopment.simpleweather.data.db.WeatherResponseDao
import com.jggdevelopment.simpleweather.data.network.WeatherNetworkDataSource
import com.jggdevelopment.simpleweather.data.db.entity.WeatherResponse
import com.jggdevelopment.simpleweather.data.provider.UnitProvider
import com.jggdevelopment.simpleweather.internal.UnitSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
        private val weatherResponseDao: WeatherResponseDao,
        private val weatherNetworkDataSource: WeatherNetworkDataSource,
        private val unitProvider: UnitProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedWeatherResponse.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getWeather(): LiveData<out WeatherResponse> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext weatherResponseDao.getWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: WeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            weatherResponseDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        val units = unitProvider.getUnitSystem()
        if (units == UnitSystem.IMPERIAL)
            weatherNetworkDataSource.fetchCurrentWeather(36.0, -79.0, "us", Locale.getDefault().language)
        else
            weatherNetworkDataSource.fetchCurrentWeather(36.0, -79.0, "ca", Locale.getDefault().language)
    }

    // check if data was fetched within the last five minutes
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(fiveMinutesAgo)
    }
}