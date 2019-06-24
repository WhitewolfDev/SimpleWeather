package com.jggdevelopment.simpleweather.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.tabs.TabLayout;
import com.jggdevelopment.simpleweather.MainActivity;
import com.jggdevelopment.simpleweather.R;
import com.jggdevelopment.simpleweather.WeatherLocation;
import com.jggdevelopment.simpleweather.adapters.CustomPagerAdapter;
import com.jggdevelopment.simpleweather.models.Forecast;
import com.jggdevelopment.simpleweather.services.WeatherAPIUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * This fragment shows the user the conditions of their current location by default
 * or their selected location, if they searched for one.
 */
public class MasterFragment extends Fragment {

    private View view;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Only inflate the view and do setup if the view is null, to prevent pop in of data and unnecessary API calls
        if (view == null) {
            this.view = inflater.inflate(R.layout.fragment_master, container, false);

            Toolbar toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

            // setup ViewPager and its adapter
            ViewPager viewPager = view.findViewById(R.id.main_viewpager);
            viewPager.setAdapter(new CustomPagerAdapter(getContext(), getActivity().getSupportFragmentManager()));

            // setup TabLayout and connect it to the ViewPager
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.weather_tabs);
            tabLayout.setupWithViewPager(viewPager);

            // Fetch the location data and setup
            initializeWeatherData();
        }

        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.search_button) {
            List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(getActivity());
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                initializeWeatherData(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        }
    }

    /**
     * fetches data based on users current location
     * updateConditions() is called if the response is successful
     */
    private void initializeWeatherData() {
        // get the user's current location
        WeatherLocation location = new WeatherLocation(getActivity());

        // get the current conditions.  If the response is successful, it will call
        // updateConditions() below with the retrieved data
        WeatherAPIUtils.getCurrentWeatherData(location.getLatitude(), location.getLongitude(), this);

    }

    /**
     * fetches data based on a given location
     * @param lat latitude of given location
     * @param lon longitude of given location
     */
    public void initializeWeatherData(double lat, double lon) {
        WeatherAPIUtils.getCurrentWeatherData(lat, lon, this);
    }

    /**
     * updates views based on retrieved data
     * @param weatherData data retrieved from API call
     */
    public void updateConditions(Forecast weatherData) {
        TextView temperatureView = view.findViewById(R.id.temperature);
        TextView highTemp = view.findViewById(R.id.high_temp);
        TextView lowTemp = view.findViewById(R.id.low_temp);
        TextView description = view.findViewById(R.id.weatherDescription);
        TextView precipitationChance = view.findViewById(R.id.precipitation_chance);

        updateToolbarTitle(getActivity(), weatherData.getLatitude(), weatherData.getLongitude());
        temperatureView.setText(getActivity().getString(R.string.formattedTemperature, String.format(Locale.getDefault(), "%.1f", weatherData.getCurrently().getTemperature())));
        highTemp.setText(getActivity().getString(R.string.formattedHighTemperature, String.format(Locale.getDefault(), "%.1f", weatherData.getDaily().getData().get(0).getTemperatureMax())));
        lowTemp.setText(getActivity().getString(R.string.formattedLowTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.getDaily().getData().get(0).getTemperatureMin())));
        description.setText(weatherData.getCurrently().getSummary());
        precipitationChance.setText(getActivity().getString(R.string.formattedPrecipitationChance, String.format(Locale.getDefault(), "%.0f", weatherData.getCurrently().getPrecipProbability() * 100)));
    }

    /**
     * updates the city label in the toolbar
     * @param activity activity containing the toolbar
     * @param lat latitude of city
     * @param lon longitude of city
     */
    public static void updateToolbarTitle(Activity activity, Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
        ((MainActivity)activity).getSupportActionBar().setTitle(cityName);
    }
}
