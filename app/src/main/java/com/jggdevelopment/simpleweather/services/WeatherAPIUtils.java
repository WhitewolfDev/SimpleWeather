package com.jggdevelopment.simpleweather.services;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.jggdevelopment.simpleweather.BuildConfig;
import com.jggdevelopment.simpleweather.MainActivity;
import com.jggdevelopment.simpleweather.R;
import com.jggdevelopment.simpleweather.fragments.MasterFragment;
import com.jggdevelopment.simpleweather.models.Currently;
import com.jggdevelopment.simpleweather.models.Forecast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPIUtils {

    private static String baseUrl = "https://api.darksky.net/forecast/" + BuildConfig.darkSkyAPI_KEY + "/";
    private static TextView temperatureView;
    private static TextView highTemp;
    private static TextView lowTemp;
    private static TextView description;
    private static TextView precipitationChance;

    /**
     * uses retrofit to call the DarkSky API using the API key in the baseURL
     * @return WeatherService
     */
    private static WeatherService getWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }

    /**
     * Pulls weather data from the DarkSky API using the provided location.
     * On success, it updates the views in MasterFragment
     * @param lat latitude of location
     * @param lon longitude of location
     * @param fragment MasterFragment
     */
    public static void getCurrentWeatherData(Double lat, Double lon, final MasterFragment fragment) {
        WeatherService service = getWeatherService();

        service.getWeather(lat, lon).enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if (response.isSuccessful()) {
                    fragment.updateConditions(response.body());
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });
    }


}
