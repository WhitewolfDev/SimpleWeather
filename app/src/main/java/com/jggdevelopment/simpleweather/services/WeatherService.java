package com.jggdevelopment.simpleweather.services;

import com.jggdevelopment.simpleweather.models.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherService {
    @GET("{latitude},{longitude}")
    Call<Forecast> getWeather(@Path("latitude") Double lat, @Path("longitude") Double lon);
}
