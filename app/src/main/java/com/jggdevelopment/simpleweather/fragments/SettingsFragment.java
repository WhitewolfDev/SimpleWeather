package com.jggdevelopment.simpleweather.fragments;


import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.jggdevelopment.simpleweather.MainActivity;
import com.jggdevelopment.simpleweather.R;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat useCurrentLocationPreference;
    private SwitchPreferenceCompat useCelsiusPreference;
    private SwitchPreferenceCompat useDarkThemePreference;
    private SharedPreferences prefs;
    private PreferenceScreen preferenceScreen;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // To get a preference
        preferenceScreen = getPreferenceScreen();

        prefs = getActivity().getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);

        setupPreferenceViews();
        setUpPreferenceListeners();

    }

    public void turnOnLocation() {
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 200);
    }

    private void setupPreferenceViews() {
        useCurrentLocationPreference = (SwitchPreferenceCompat) preferenceScreen.findPreference("useLocation");
        useCelsiusPreference = (SwitchPreferenceCompat) preferenceScreen.findPreference("useCelsius");
        useDarkThemePreference = (SwitchPreferenceCompat) preferenceScreen.findPreference("useDarkTheme");

        if (prefs.getBoolean("useCurrentLocation", false)) {
            useCurrentLocationPreference.setChecked(true);
        } else {
            useCurrentLocationPreference.setChecked(false);
        }

        if (prefs.getBoolean("useCelsius", false)) {
            useCelsiusPreference.setChecked(true);
        } else {
            useCelsiusPreference.setChecked(false);
        }

        if (prefs.getBoolean("useDarkTheme", false)) {
            useDarkThemePreference.setChecked(true);
        } else {
            useDarkThemePreference.setChecked(false);
        }
    }

    private void setUpPreferenceListeners() {
        useCurrentLocationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue == Boolean.TRUE) {
                    prefs.edit().putBoolean("useCurrentLocation", true).commit();
                } else {
                    prefs.edit().putBoolean("useCurrentLocation", false).commit();
                }
                turnOnLocation();
                return true;
            }
        });

        useCelsiusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue == Boolean.TRUE) {
                    prefs.edit().putBoolean("useCelsius", true).commit();
                } else {
                    prefs.edit().putBoolean("useCelsius", false).commit();
                }
                return true;
            }
        });

        useDarkThemePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue == Boolean.TRUE) {
                    prefs.edit().putBoolean("useDarkTheme", true).commit();
                } else {
                    prefs.edit().putBoolean("useDarkTheme", false).commit();
                }

                getActivity().recreate();

                return true;
            }
        });
    }
}