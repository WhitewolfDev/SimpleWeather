package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Minutely {

    @SerializedName("data")
    @Expose
    var data: List<MinuteDatum>? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null

}