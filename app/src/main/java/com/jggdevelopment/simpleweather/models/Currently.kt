package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Currently {

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
    var humidity: Double? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("nearestStormBearing")
    @Expose
    var nearestStormBearing: Int? = null
    @SerializedName("nearestStormDistance")
    @Expose
    var nearestStormDistance: Int? = null
    @SerializedName("ozone")
    @Expose
    var ozone: Double? = null
    @SerializedName("precipIntensity")
    @Expose
    var precipIntensity: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double? = null
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

}
