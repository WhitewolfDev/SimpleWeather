package com.jggdevelopment.simpleweather.fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.jggdevelopment.simpleweather.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var useCurrentLocationPreference: SwitchPreferenceCompat
    private lateinit var useCelsiusPreference: SwitchPreferenceCompat
    private lateinit var useDarkThemePreference: SwitchPreferenceCompat
    private lateinit var prefs: SharedPreferences

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        prefs = activity!!.getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)

        setupPreferenceViews()
        setupPreferenceListeners()
    }

    private fun turnOnLocation() {
        ActivityCompat.requestPermissions(activity!!, arrayOf(ACCESS_FINE_LOCATION), 200)
    }

    private fun setupPreferenceViews() {
        useCurrentLocationPreference = preferenceScreen.findPreference<Preference>("useLocation") as SwitchPreferenceCompat
        useCelsiusPreference = preferenceScreen.findPreference<Preference>("useCelsius") as SwitchPreferenceCompat
        useDarkThemePreference = preferenceScreen.findPreference<Preference>("useDarkTheme") as SwitchPreferenceCompat

        useCurrentLocationPreference.isChecked = prefs.getBoolean("useCurrentLocation", false)
        useCelsiusPreference.isChecked = prefs.getBoolean("useCelsius", false)
        useDarkThemePreference.isChecked = prefs.getBoolean("useDarkTheme", false)
    }

    private fun setupPreferenceListeners() {
        useCurrentLocationPreference.setOnPreferenceChangeListener { preference, newValue ->
            prefs.edit().putBoolean("useCurrentLocation", newValue as Boolean).commit()
        }

        useCelsiusPreference.setOnPreferenceChangeListener { preference, newValue ->
            prefs.edit().putBoolean("useCelsius", newValue as Boolean).commit()
        }

        useDarkThemePreference.setOnPreferenceChangeListener { preference, newValue ->
            prefs.edit().putBoolean("useDarkTheme", newValue as Boolean).commit()
            activity!!.recreate()
            true
        }
    }



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}