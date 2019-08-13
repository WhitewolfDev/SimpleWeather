package com.jggdevelopment.simpleweather.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.jggdevelopment.simpleweather.internal.UnitSystem

const val USE_METRIC = "USE_METRIC"

class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {


    override fun getUnitSystem(): UnitSystem {
        val useMetric = preferences.getBoolean(USE_METRIC, false)
        if (useMetric) return UnitSystem.METRIC else return UnitSystem.IMPERIAL
    }
}