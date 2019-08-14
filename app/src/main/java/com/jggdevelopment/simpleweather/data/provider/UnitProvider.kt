package com.jggdevelopment.simpleweather.data.provider

import com.jggdevelopment.simpleweather.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}