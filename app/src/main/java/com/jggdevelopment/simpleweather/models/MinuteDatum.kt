package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MinuteDatum {

    @SerializedName("precipIntensity")
    @Expose
    var precipIntensity: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double? = null
    @SerializedName("time")
    @Expose
    var time: Int? = null

}