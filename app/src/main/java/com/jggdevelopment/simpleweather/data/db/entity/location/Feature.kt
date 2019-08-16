package com.jggdevelopment.simpleweather.data.db.entity.location

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.jggdevelopment.simpleweather.data.db.converter.DataConverter

class Feature (
        @ColumnInfo
        val text: String,
        @ColumnInfo
        val place_name: String,
        @TypeConverters(DataConverter::class)
        @ColumnInfo
        val coordinates: List<Double>
) {

}