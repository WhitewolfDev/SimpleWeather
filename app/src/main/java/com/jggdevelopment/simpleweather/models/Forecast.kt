package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Forecast {

    @SerializedName("currently")
    @Expose
    var currently: Currently? = null
    @SerializedName("daily")
    @Expose
    var daily: Daily? = null
    @SerializedName("flags")
    @Expose
    var flags: Flags? = null
    @SerializedName("hourly")
    @Expose
    var hourly: Hourly? = null
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
    @SerializedName("minutely")
    @Expose
    var minutely: Minutely? = null
    @SerializedName("offset")
    @Expose
    var offset: Double? = null
    @SerializedName("timezone")
    @Expose
    var timezone: String? = null
    @SerializedName("alerts")
    @Expose
    var alerts: List<AlertDatum>? = null
}