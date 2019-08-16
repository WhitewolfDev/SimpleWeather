package com.jggdevelopment.simpleweather.data.network

import android.util.JsonToken
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jggdevelopment.simpleweather.BuildConfig
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

interface MapboxApiService {
    @GET("{endpoint}/{search_text}.json")
    fun searchPlaces(
            @Path("endpoint") endpoint: String?,
            @Path("search_text") searchText: String?,
            @Query("access_token") accessToken: String
    ): Deferred<LocationSearchResponse>

    companion object {
        operator fun invoke(): MapboxApiService {
            val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {message ->
                Timber.d(message)
            }).apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.mapbox.com/geocoding/v5/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MapboxApiService::class.java)
        }
    }
}