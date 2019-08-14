package com.jggdevelopment.simpleweather.ui.cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jggdevelopment.simpleweather.BuildConfig
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.ui.base.ScopedFragment
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChooseCityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChooseCityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseCityFragment : ScopedFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_city_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateToolbar()
    }

    fun updateToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Choose Location"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }
}
