package com.jggdevelopment.simpleweather.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flags {

    @SerializedName("darksky-stations")
    @Expose
    var darkskyStations: List<String>? = null
    @SerializedName("isd-stations")
    @Expose
    var isdStations: List<String>? = null
    @SerializedName("lamp-stations")
    @Expose
    var lampStations: List<String>? = null
    @SerializedName("madis-stations")
    @Expose
    var madisStations: List<String>? = null
    @SerializedName("sources")
    @Expose
    var sources: List<String>? = null
    @SerializedName("units")
    @Expose
    var units: String? = null

}