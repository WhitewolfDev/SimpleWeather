package com.jggdevelopment.simpleweather.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.data.db.entity.location.Feature
import com.jggdevelopment.simpleweather.internal.inflate
import com.jggdevelopment.simpleweather.ui.search.viewmodel.LocationResponseViewModel
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModel
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModelFactory
import kotlinx.android.synthetic.main.location_search_result_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


class LocationSearchResultsAdapter (
        private val features: List<Feature>,
        private val context: Context
) : RecyclerView.Adapter<LocationSearchResultsAdapter.ViewHolder>(), KodeinAware {

    override val kodein by closestKodein(context)
    private lateinit var viewModel: WeatherResponseViewModel
    private val viewModelFactory: WeatherResponseViewModelFactory by instance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.location_search_result_row, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return features.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel = ViewModelProviders.of(context as FragmentActivity, viewModelFactory).get(WeatherResponseViewModel::class.java)
        holder.bindFeature(features[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var feature: Feature

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            refreshWeather()
            itemView.findNavController().navigate(R.id.city_search_result_chosen_action)
        }

        fun bindFeature(feature: Feature) {
            this.feature = feature
            itemView.city_name.text = feature.text
            itemView.place_name.text = feature.place_name
        }

        // when an item in the recycler view is clicked, refresh the weather with the
        // coordinates of that location
        private fun refreshWeather() = CoroutineScope(context as CoroutineContext).launch {
            viewModel.refreshWeatherWithCoordinates(feature.coordinates[0], feature.coordinates[1])
        }
    }
}