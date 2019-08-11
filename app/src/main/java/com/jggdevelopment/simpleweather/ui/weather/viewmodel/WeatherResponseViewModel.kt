package com.jggdevelopment.simpleweather.ui.weather.viewmodel

import androidx.lifecycle.ViewModel;
import com.jggdevelopment.simpleweather.data.provider.UnitProvider
import com.jggdevelopment.simpleweather.data.repository.ForecastRepository
import com.jggdevelopment.simpleweather.internal.UnitSystem
import com.jggdevelopment.simpleweather.internal.lazyDeferred

class WeatherResponseViewModel (
        private val forecastRepository: ForecastRepository,
        unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getWeather()
    }

}
