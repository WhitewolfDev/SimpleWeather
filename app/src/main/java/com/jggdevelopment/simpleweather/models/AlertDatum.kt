package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlertDatum {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("time")
    @Expose
    var time: Int = 0
    @SerializedName("expires")
    @Expose
    var expiryTime: Int = 0
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null
}
