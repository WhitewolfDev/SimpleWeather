package com.jggdevelopment.simpleweather.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.jggdevelopment.simpleweather.BuildConfig;
import com.jggdevelopment.simpleweather.fragments.MasterFragment;
import com.jggdevelopment.simpleweather.models.Forecast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    private static SharedPreferences prefs;

    /**
     * uses retrofit to call the DarkSky API using the API key in the baseURL
     * @return WeatherService
     */
    private static WeatherService getWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }

    public static OkHttpClient getHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);



        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300,TimeUnit.SECONDS).
                        addInterceptor(logging).
                        build();

        return client;
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
        prefs = fragment.getActivity().getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);

        if (prefs.getBoolean("useCelsius", false)) {
            service.getWeatherMetric(lat, lon, "ca").enqueue(new Callback<Forecast>() {
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
        } else {
            service.getWeatherImperial(lat, lon).enqueue(new Callback<Forecast>() {
                @Override
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    if (response.isSuccessful()) {
                        fragment.updateConditions(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Forecast> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
