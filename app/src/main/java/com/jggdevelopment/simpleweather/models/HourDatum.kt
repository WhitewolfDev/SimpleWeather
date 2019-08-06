package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HourDatum {

    @SerializedName("apparentTemperature")
    @Expose
    var apparentTemperature: Double? = null
    @SerializedName("cloudCover")
    @Expose
    var cloudCover: Double? = null
    @SerializedName("dewPoint")
    @Expose
    var dewPoint: Double? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Double = 0.0
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("ozone")
    @Expose
    var ozone: Double? = null
    @SerializedName("precipIntensity")
    @Expose
    var precipIntensity: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double = 0.0
    @SerializedName("pressure")
    @Expose
    var pressure: Double? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("temperature")
    @Expose
    var temperature: Double? = null
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
    @SerializedName("precipType")
    @Expose
    var precipType: String? = null

}
