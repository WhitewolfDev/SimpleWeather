package com.jggdevelopment.simpleweather.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jggdevelopment.simpleweather.data.db.entity.location.LocationSearchResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MapboxApiService {
    @GET("{search_text}")
    fun searchPlaces(
            @Path("endpoint") endpoint: String?,
            @Path("search_text") searchText: String?
    ): Deferred<LocationSearchResponse>

    companion object {
        operator fun invoke(): MapboxApiService {
            val okHttpClient = OkHttpClient.Builder()
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.darksky.net/geocoding/v5/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MapboxApiService::class.java)
        }
    }
}