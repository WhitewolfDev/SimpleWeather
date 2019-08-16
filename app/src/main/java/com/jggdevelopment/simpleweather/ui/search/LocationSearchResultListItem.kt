package com.jggdevelopment.simpleweather.ui.search

import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.data.db.entity.location.Feature
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.location_search_result_row.*

class LocationSearchResultListItem (
        private val feature: Feature
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            city_name.text = feature.text
            place_name.text = feature.place_name
        }
    }

    override fun getLayout() = R.layout.location_search_result_row
}