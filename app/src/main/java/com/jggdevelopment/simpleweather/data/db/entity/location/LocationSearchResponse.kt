package com.jggdevelopment.simpleweather.data.db.entity.location

import androidx.room.*
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter

@Entity(tableName = "location_search_results")
class LocationSearchResponse(
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
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}