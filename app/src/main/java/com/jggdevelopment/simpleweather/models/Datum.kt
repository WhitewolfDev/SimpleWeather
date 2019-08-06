package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("apparentTemperatureMax")
    @Expose
    var apparentTemperatureMax: Double? = null
    @SerializedName("apparentTemperatureMaxTime")
    @Expose
    var apparentTemperatureMaxTime: Int? = null
    @SerializedName("apparentTemperatureMin")
    @Expose
    var apparentTemperatureMin: Double? = null
    @SerializedName("apparentTemperatureMinTime")
    @Expose
    var apparentTemperatureMinTime: Int? = null
    @SerializedName("cloudCover")
    @Expose
    var cloudCover: Double? = null
    @SerializedName("dewPoint")
    @Expose
    var dewPoint: Double? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Double? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("moonPhase")
    @Expose
    var moonPhase: Double? = null
    @SerializedName("ozone")
    @Expose
    var ozone: Double? = null
    @SerializedName("precipIntensity")
    @Expose
    var precipIntensity: Double? = null
    @SerializedName("precipIntensityMax")
    @Expose
    var precipIntensityMax: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double? = null
    @SerializedName("pressure")
    @Expose
    var pressure: Double? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("sunriseTime")
    @Expose
    var sunriseTime: Int? = null
    @SerializedName("sunsetTime")
    @Expose
    var sunsetTime: Int? = null
    @SerializedName("temperatureMax")
    @Expose
    var temperatureMax: Double? = null
    @SerializedName("temperatureMaxTime")
    @Expose
    var temperatureMaxTime: Int? = null
    @SerializedName("temperatureMin")
    @Expose
    var temperatureMin: Double? = null
    @SerializedName("temperatureMinTime")
    @Expose
    var temperatureMinTime: Int? = null
    @SerializedName("time")
    @Expose
    var time: Int? = null
    @SerializedName("visibility")
    @Expose
    var visibility: Double? = null
    @SerializedName("windBearing")
    @Expose
    var windBearing: Int? = null
    @SerializedName("windSpeed")
    @Expose
    var windSpeed: Double? = null
    @SerializedName("precipIntensityMaxTime")
    @Expose
    var precipIntensityMaxTime: Int? = null
    @SerializedName("precipType")
    @Expose
    var precipType: String? = null

}
