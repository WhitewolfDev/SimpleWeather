package com.jggdevelopment.simpleweather.data.db.converter

import androidx.room.TypeConverter
import androidx.room.util.StringUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jggdevelopment.simpleweather.data.db.entity.location.Feature
import com.jggdevelopment.simpleweather.data.db.entity.weather.DailyData
import com.jggdevelopment.simpleweather.data.db.entity.weather.HourlyData
import com.jggdevelopment.simpleweather.data.db.entity.weather.MinutelyData

class DataConverter {
    @TypeConverter
    fun fromMinutelyDataList(data: List<MinutelyData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<MinutelyData>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toMinutelyDataList(dataString: String): List<MinutelyData> {
        val gson = Gson()
        val type = object : TypeToken<List<MinutelyData>>() {}.type
        return gson.fromJson(dataString, type)
    }

    @TypeConverter
    fun fromHourlyDataList(data: List<HourlyData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<HourlyData>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toHourlyDataList(dataString: String): List<HourlyData> {
        val gson = Gson()
        val type = object : TypeToken<List<HourlyData>>() {}.type
        return gson.fromJson(dataString, type)
    }

    @TypeConverter
    fun fromDailyDataList(data: List<DailyData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<DailyData>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toDailyDataList(dataString: String): List<DailyData> {
        val gson = Gson()
        val type = object : TypeToken<List<DailyData>>() {}.type
        return gson.fromJson(dataString, type)
    }

    @TypeConverter
    fun fromCoordinatesList(data: List<Double>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Double>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toCoordinatesList(dataString: String): List<Double> {
        val gson = Gson()
        val type = object : TypeToken<List<Double>>() {}.type
        return gson.fromJson(dataString, type)
    }

    @TypeConverter
    fun fromFeaturesList(data: List<Feature>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Feature>>() {}.type
        return gson.toJson(data, type)
    }

    @TypeConverter
    fun toFeaturesList(dataString: String): List<Feature> {
        val gson = Gson()
        val type = object : TypeToken<List<Feature>>() {}.type
        return gson.fromJson(dataString, type)
    }

    // converts location search query list into a human readable string for the db
    @TypeConverter
    fun fromQueryList(data: List<String>): String {
        val builder = StringBuilder()
        builder.append(data[0])

        for (i in 1 until data.size) {
            builder.append(" ")
            builder.append(data[i])
        }

        return builder.toString()
    }

    @TypeConverter
    fun toQueryList(dataString: String): List<String> {
        return dataString.split(" ")
    }
}