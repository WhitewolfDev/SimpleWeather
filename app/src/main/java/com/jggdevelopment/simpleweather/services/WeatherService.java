package com.jggdevelopment.simpleweather.services;

import com.jggdevelopment.simpleweather.models.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("{latitude},{longitude}")
    Call<Forecast> getWeatherImperial(@Path("latitude") Double lat, @Path("longitude") Double lon);

    @GET("{latitude},{longitude}")
    Call<Forecast> getWeatherMetric(@Path("latitude") Double lat, @Path("longitude") Double lon, @Query("units") String units);
}
