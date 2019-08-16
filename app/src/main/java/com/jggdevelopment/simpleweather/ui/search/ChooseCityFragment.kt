package com.jggdevelopment.simpleweather.ui.search

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.data.db.entity.location.Feature
import com.jggdevelopment.simpleweather.ui.base.ScopedFragment
import com.jggdevelopment.simpleweather.ui.search.viewmodel.LocationResponseViewModel
import com.jggdevelopment.simpleweather.ui.search.viewmodel.LocationResponseViewModelFactory
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModel
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.choose_city_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ChooseCityFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val locationViewModelFactory: LocationResponseViewModelFactory by instance()
    private val weatherResponseViewModelFactory: WeatherResponseViewModelFactory by instance()

    private lateinit var locationViewModel: LocationResponseViewModel
    private lateinit var weatherViewModel: WeatherResponseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setupViews()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_city_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationViewModel = ViewModelProviders.of(this, locationViewModelFactory)
                .get(LocationResponseViewModel::class.java)

        weatherViewModel = ViewModelProviders.of(this, weatherResponseViewModelFactory)
                .get(WeatherResponseViewModel::class.java)

        updateToolbar()
        bindUI()
    }

    fun updateToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Choose Location"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    fun bindUI() = launch(Dispatchers.Main){
        val locationResults = locationViewModel.locationResponse
        val owner = viewLifecycleOwner

        locationResults.observe(owner, Observer {
            if (it == null) return@Observer

            // TODO: set loading icon to GONE

            initRecyclerView(it.features.toLocationSearchResultListItem())
        })
    }

    fun setupViews() = launch {
        search_button.setOnClickListener {
            searchLocations()
            search_results_rv.adapter?.notifyDataSetChanged()
        }
    }

    fun searchLocations() = launch {
        val searchText = search_box.text.toString()

        if (searchText != "")
            locationViewModel.searchLocation(search_box.text.toString())
        else
            Toast.makeText(context?.applicationContext, "Please enter a search term", Toast.LENGTH_SHORT).show()
    }

    private fun List<Feature>.toLocationSearchResultListItem() : List<LocationSearchResultListItem> {
        return this.map {
            LocationSearchResultListItem(it)
        }
    }

    private fun initRecyclerView(items: List<LocationSearchResultListItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        search_results_rv.apply {
            layoutManager = LinearLayoutManager(this@ChooseCityFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            weatherViewModel.refreshWeatherWithCoordinates(item)
        }
    }
}
