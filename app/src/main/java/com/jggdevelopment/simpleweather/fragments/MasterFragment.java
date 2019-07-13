package com.jggdevelopment.simpleweather.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jggdevelopment.simpleweather.BuildConfig;
import com.jggdevelopment.simpleweather.MainActivity;
import com.jggdevelopment.simpleweather.R;
import com.jggdevelopment.simpleweather.WeatherLocation;
import com.jggdevelopment.simpleweather.adapters.CustomPagerAdapter;
import com.jggdevelopment.simpleweather.models.Forecast;
import com.jggdevelopment.simpleweather.services.WeatherAPIUtils;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import in.adityaanand.morphdialog.MorphDialog;
import in.adityaanand.morphdialog.utils.MorphDialogAction;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

/**
 * This fragment shows the user the conditions of their current location by default
 * or their selected location, if they searched for one.
 */
public class MasterFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private View view;
    private LocationManager locationManager;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private ImageView locationButton;
    private TextView temperatureView;
    private TextView highTemp;
    private TextView lowTemp;
    private TextView description;
    private TextView precipitationChance;
    private ImageView precipImage;
    private LottieAnimationView weatherIcon;
    private ImageView windIcon;
    private TextView windSpeed;
    private TextView apparentTemperature;
    private TextView currentTime;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout pullToRefresh;
    private FloatingActionButton alertFab;
    private CheckBox favoriteCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = getActivity().getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);

        Mapbox.getInstance(getContext(), BuildConfig.mapboxAPI_KEY);

        // Only inflate the view and do setup if the view is null, to prevent pop in of data and unnecessary API calls
        if (view == null) {
            this.view = inflater.inflate(R.layout.fragment_master, container, false);

            setupViews();

            // if location permission not allowed, initialize weather on default location, else do it with found location
            if (!prefs.getBoolean("locationPermissionAllowed", false)) {
                initializeWeatherData(Double.parseDouble(prefs.getString("defaultLatitude", "0")), Double.parseDouble(prefs.getString("defaultLongitude", "0")));
            } else {
                initializeWeatherData();
            }

            setupPreferencesListener();
        }

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,  R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        setHasOptionsMenu(true);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!prefs.getBoolean("locationPermissionAllowed", false)) {
                    initializeWeatherData(Double.parseDouble(prefs.getString("defaultLatitude", "0")), Double.parseDouble(prefs.getString("defaultLongitude", "0")));
                } else {
                    initializeWeatherData();
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }



    private void setupViews() {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        // setup ViewPager and its adapter
        ViewPager viewPager = view.findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getContext(), getActivity().getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        // setup TabLayout and connect it to the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.weather_tabs);
        tabLayout.setupWithViewPager(viewPager);

        locationButton = toolbar.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[] {ACCESS_FINE_LOCATION}, 200);
            }
        });

        appBarLayout = view.findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);

        temperatureView = view.findViewById(R.id.temperature);
        highTemp = view.findViewById(R.id.high_temp);
        lowTemp = view.findViewById(R.id.low_temp);
        description = view.findViewById(R.id.weatherDescription);
        precipitationChance = view.findViewById(R.id.precipitation_chance);
        precipImage = view.findViewById(R.id.precipImage);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        windIcon = view.findViewById(R.id.wind_icon);
        windSpeed = view.findViewById(R.id.wind_speed);
        apparentTemperature = view.findViewById(R.id.apparentTemperature);
        currentTime = view.findViewById(R.id.currentTime);
        alertFab = view.findViewById(R.id.weatherAlertFAB);
        favoriteCity = view.findViewById(R.id.locationStar);

        favoriteCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getActivity(), "City added to Favorites (not really)", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initializeWeatherData();
    }

    private void setupPreferencesListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("useCurrentLocation")) {
                    if (prefs.getBoolean("useCurrentLocation", false)) {
                        initializeWeatherData();
                    } else {
                        initializeWeatherData(Double.parseDouble(prefs.getString("defaultLatitude", "0")), Double.parseDouble(prefs.getString("defaultLongitude", "0")));
                    }
                }

            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);
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

            // Start the autocomplete intent.
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken())
                    .placeOptions(PlaceOptions.builder()
                            .language(Locale.getDefault().getLanguage())
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .build(PlaceOptions.MODE_CARDS))
                    .build(getActivity());
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                initializeWeatherData(feature.center().latitude(), feature.center().longitude());
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
        prefs.edit().putString("defaultLatitude", Double.toString(location.getLatitude())).commit();
        prefs.edit().putString("defaultLongitude", Double.toString(location.getLongitude())).commit();

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

    public boolean usingCelsius() {
        return prefs.getBoolean("useCelsius", true);
    }


    /**
     * updates views based on retrieved data
     * @param weatherData data retrieved from API call
     */
    public void updateConditions(Forecast weatherData) {
        prefs.edit().putString("weatherData", new Gson().toJson(weatherData)).commit();

        AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);

        updateToolbar(getActivity(), weatherData.getLatitude(), weatherData.getLongitude());
        temperatureView.setText(getActivity().getString(R.string.formattedTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.getCurrently().getTemperature())));
        highTemp.setText(getActivity().getString(R.string.formattedHighTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.getDaily().getData().get(0).getTemperatureMax())));
        lowTemp.setText(getActivity().getString(R.string.formattedLowTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.getDaily().getData().get(0).getTemperatureMin())));
        description.setText(weatherData.getCurrently().getSummary());
        precipitationChance.setText(getActivity().getString(R.string.formattedPrecipitationChance, String.format(Locale.getDefault(), "%.0f", weatherData.getHourly().getData().get(0).getPrecipProbability() * 100)));
        apparentTemperature.setText(getActivity().getString(R.string.formattedApparentTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.getCurrently().getApparentTemperature())));
        if (usingCelsius()) {
            windSpeed.setText(getActivity().getString(R.string.formattedWindSpeedC, String.format(Locale.getDefault(), "%.0f", weatherData.getCurrently().getWindSpeed())));
        } else {
            windSpeed.setText(getActivity().getString(R.string.formattedWindSpeedF, String.format(Locale.getDefault(), "%.0f", weatherData.getCurrently().getWindSpeed())));
        }
        currentTime.setText(getActivity().getString(R.string.formattedTime, String.format(Locale.getDefault(), "%s", convertUnixTimeToHours(weatherData.getCurrently().getTime(), weatherData.getTimezone()))));
        if (weatherData.getAlerts() != null) {
            alertFab.setVisibility(View.VISIBLE);
            alertFab.startAnimation(in);
            alertFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabMorph(weatherData);
                }
            });
        } else {
            alertFab.setVisibility(View.GONE);
        }


        temperatureView.startAnimation(in);
        highTemp.startAnimation(in);
        lowTemp.startAnimation(in);
        description.startAnimation(in);
        precipitationChance.startAnimation(in);
        precipImage.setVisibility(View.VISIBLE);
        precipImage.startAnimation(in);
        weatherIcon.startAnimation(in);
        windIcon.setVisibility(View.VISIBLE);
        windIcon.startAnimation(in);
        windSpeed.startAnimation(in);
        apparentTemperature.startAnimation(in);
        currentTime.startAnimation(in);

        setIconAnimation(weatherData);
    }

    private void fabMorph(Forecast weatherData) {
        new MorphDialog.Builder(getActivity(), alertFab)
                .title(weatherData.getAlerts().get(0).getTitle())
                .content(weatherData.getAlerts().get(0).getDescription())
                .positiveText("OK")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .onPositive((MorphDialog dialog1, MorphDialogAction which) -> {
                    Toast.makeText(getActivity(), "onPositive", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    public String convertUnixTimeToHours(int time, String timezone) {
        Date date = new Date((int) time * 1000L);
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        jdf.setTimeZone(TimeZone.getTimeZone(timezone));
        String formattedDate = jdf.format(date);

        return formattedDate;
    }

    public void setIconAnimation(Forecast weatherData) {
        switch (weatherData.getCurrently().getIcon()) {
            case "clear-day":
                weatherIcon.setAnimation("sun.json");
                break;

            case "clear-night":
                weatherIcon.setAnimation("clearNight.json");
                break;

            case "rain":
                weatherIcon.setAnimation("drizzle.json");
                break;

            case "cloudy":
                weatherIcon.setAnimation("Overcast.json");
                break;

            case "partly-cloudy-day":
                weatherIcon.setAnimation("partlyCloudy.json");
                break;

            case "partly-cloudy-night":
                weatherIcon.setAnimation("partlyCloudyNight.json");
                break;

            case "snow":
                weatherIcon.setAnimation("snow.json");
                break;

            case "sleet":
                weatherIcon.setAnimation("sleet.json");
                break;

            case "wind":
                weatherIcon.setAnimation("wind.json");
                break;

            case "fog":
                weatherIcon.setAnimation("fog.json");

            default:
                weatherIcon.setAnimation("heavyThunderstorm.json");
                break;
        }

        weatherIcon.playAnimation();
    }

    /**
     * updates the city label in the toolbar
     * @param activity activity containing the toolbar
     * @param lat latitude of city
     * @param lon longitude of city
     */
    public void updateToolbar(Activity activity, Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String title = "Weather";
        if (addresses != null) {
            if (addresses.get(0).getLocality() != null) {
                title = addresses.get(0).getLocality();
            } else if (addresses.get(0).getSubLocality() != null) {
                title = addresses.get(0).getSubLocality();
            } else if (addresses.get(0).getSubAdminArea() != null) {
                title = addresses.get(0).getSubAdminArea();
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_LONG).show();
        }

        ((MainActivity)activity).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            pullToRefresh.setEnabled(true);
        } else {
            pullToRefresh.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
