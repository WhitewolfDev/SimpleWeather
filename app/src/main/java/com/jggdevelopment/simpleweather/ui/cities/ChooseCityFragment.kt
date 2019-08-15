package com.jggdevelopment.simpleweather.ui.cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jggdevelopment.simpleweather.BuildConfig
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.ui.base.ScopedFragment
import com.jggdevelopment.simpleweather.ui.cities.viewmodel.LocationResponseViewModel
import com.jggdevelopment.simpleweather.ui.cities.viewmodel.LocationResponseViewModelFactory
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.choose_city_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ChooseCityFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: LocationResponseViewModelFactory by instance()

    private lateinit var viewModel: LocationResponseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setupViews()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_city_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationResponseViewModel::class.java)

        updateToolbar()
    }

    fun updateToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Choose Location"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    fun setupViews() = launch {
        search_button.setOnClickListener {
            searchLocations()
        }
    }

    fun searchLocations() = launch {
        viewModel.searchLocation(search_box.text.toString())
    }
}
