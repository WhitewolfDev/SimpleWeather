package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Hourly {

    @SerializedName("data")
    @Expose
    var data: List<HourDatum>? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null

}