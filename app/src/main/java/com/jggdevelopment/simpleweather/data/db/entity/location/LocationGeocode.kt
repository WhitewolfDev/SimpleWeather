package com.jggdevelopment.simpleweather.data.db.entity.location

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter

@Entity(tableName = "location_search_response")
class LocationGeocode(
        @ColumnInfo(name = "type")
        val type: String,
        @TypeConverters(DataConverter::class)
        @ColumnInfo(name = "query")
        val query: List<String>,
        @TypeConverters(DataConverter::class)
        val features: List<Feature>,
        @ColumnInfo(name = "attribution")
        val attribution: String
) {

}