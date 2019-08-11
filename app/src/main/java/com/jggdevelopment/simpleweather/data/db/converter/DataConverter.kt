package com.jggdevelopment.simpleweather.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jggdevelopment.simpleweather.data.db.entity.DailyData
import com.jggdevelopment.simpleweather.data.db.entity.HourlyData
import com.jggdevelopment.simpleweather.data.db.entity.MinutelyData

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
}