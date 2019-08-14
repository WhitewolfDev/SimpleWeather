package com.jggdevelopment.simpleweather.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jggdevelopment.simpleweather.BuildConfig
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DarkSkyWeatherApiService {
    @GET("{latitude},{longitude}")
    fun getCurrentWeather(
            @Path("latitude") lat: Double?,
            @Path("longitude") lon: Double?,
            @Query("units") units: String,
            @Query("lang") languageCode: String = "en"
    ): Deferred<WeatherResponse>

    companion object {
        operator fun invoke(): DarkSkyWeatherApiService {

            // add the interceptor to the HTTP client
            val okHttpClient = OkHttpClient.Builder()
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.darksky.net/forecast/" + BuildConfig.darkSkyAPI_KEY + "/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DarkSkyWeatherApiService::class.java)
        }
    }
}