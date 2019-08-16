package com.jggdevelopment.simpleweather.ui.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse
import com.jggdevelopment.simpleweather.data.provider.UnitProvider
import com.jggdevelopment.simpleweather.data.repository.ForecastRepository
import com.jggdevelopment.simpleweather.internal.UnitSystem
import com.jggdevelopment.simpleweather.model.WeatherLocation
import java.text.SimpleDateFormat
import java.util.*

class WeatherResponseViewModel (
        private val forecastRepository: ForecastRepository,
        unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()
    lateinit var weather: LiveData<out WeatherResponse>
    lateinit var location: LiveData<out WeatherLocation>

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    suspend fun refreshWeatherWithLocation() {
        weather = forecastRepository.getWeatherWithLocation()
    }

    suspend fun refreshWeatherWithCoordinates(latitude: Double, longitude: Double) {
        weather = forecastRepository.getWeatherWithCoordinates(latitude, longitude)
    }

    fun unixTimeToActualTime(time: Int, timezone: String): String {
        val date = Date(time * 1000L)
        // format of the date
        val jdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        jdf.timeZone = TimeZone.getTimeZone(timezone)
        return jdf.format(date)
    }
}
