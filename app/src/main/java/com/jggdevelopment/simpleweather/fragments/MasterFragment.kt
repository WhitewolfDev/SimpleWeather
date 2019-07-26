package com.jggdevelopment.simpleweather.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.jggdevelopment.simpleweather.BuildConfig
import com.jggdevelopment.simpleweather.MainActivity
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.WeatherLocation
import com.jggdevelopment.simpleweather.adapters.CustomPagerAdapter
import com.jggdevelopment.simpleweather.models.Forecast
import com.jggdevelopment.simpleweather.services.WeatherAPIUtils
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import `in`.adityaanand.morphdialog.MorphDialog

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity.RESULT_OK
import android.util.TypedValue

/**
 * This fragment shows the user the conditions of their current location by default
 * or their selected location, if they searched for one.
 */
class MasterFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var v: View
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var prefs: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var locationButton: ImageView
    private lateinit var temperatureView: TextView
    private lateinit var highTemp: TextView
    private lateinit var lowTemp: TextView
    private lateinit var description: TextView
    private lateinit var humidity: TextView
    private lateinit var humidityImage: ImageView
    private lateinit var weatherIcon: LottieAnimationView
    private lateinit var windIcon: ImageView
    private lateinit var windSpeed: TextView
    private lateinit var apparentTemperature: TextView
    private lateinit var currentTime: TextView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var alertFab: FloatingActionButton
    private lateinit var favoriteCity: CheckBox
    private lateinit var loadingIcon: LottieAnimationView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        prefs = activity!!.getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)

        Mapbox.getInstance(context!!, BuildConfig.mapboxAPI_KEY)

        v = inflater.inflate(R.layout.fragment_master, container, false)

        setupViews()

        // if location permission not allowed, initialize weather on default location, else do it with found location
        if (!prefs.getBoolean("locationPermissionAllowed", false)) {
            initializeWeatherData(java.lang.Double.parseDouble(prefs.getString("defaultLatitude", "0") ?: "0"), java.lang.Double.parseDouble(prefs.getString("defaultLongitude", "0") ?: "0"))
        } else {
            initializeWeatherData()
        }

        setupPreferencesListener()


        drawerToggle = object : ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {}

        drawerLayout.addDrawerListener(drawerToggle)

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
        drawerToggle.syncState()

        setHasOptionsMenu(true)

        pullToRefresh.setOnRefreshListener {
            if (!prefs.getBoolean("locationPermissionAllowed", false)) {
                initializeWeatherData(java.lang.Double.parseDouble(prefs.getString("defaultLatitude", "0") ?: "0"), java.lang.Double.parseDouble(prefs.getString("defaultLongitude", "0") ?: "0"))
            } else {
                initializeWeatherData()
            }
            pullToRefresh.isRefreshing = false
        }

        return v
    }


    private fun setupViews() {
        toolbar = v.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        drawerLayout = activity!!.findViewById(R.id.drawer_layout)

        // setup ViewPager and its adapter
        val viewPager = v.findViewById<ViewPager>(R.id.main_viewpager)
        viewPager.adapter = CustomPagerAdapter(context, activity!!.supportFragmentManager)
        viewPager.offscreenPageLimit = (viewPager.adapter as CustomPagerAdapter).count

        // setup TabLayout and connect it to the ViewPager
        val tabLayout = v.findViewById<View>(R.id.weather_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

        locationButton = toolbar.findViewById(R.id.locationButton)
        locationButton.setOnClickListener { requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 200) }

        appBarLayout = v.findViewById(R.id.appBarLayout)
        appBarLayout.addOnOffsetChangedListener(this)
        pullToRefresh = v.findViewById(R.id.pullToRefresh)

        temperatureView = v.findViewById(R.id.temperature)
        highTemp = v.findViewById(R.id.high_temp)
        lowTemp = v.findViewById(R.id.low_temp)
        description = v.findViewById(R.id.weatherDescription)
        humidity = v.findViewById(R.id.humidity)
        humidityImage = v.findViewById(R.id.humidity_image)
        weatherIcon = v.findViewById(R.id.weatherIcon)
        windIcon = v.findViewById(R.id.wind_icon)
        windSpeed = v.findViewById(R.id.wind_speed)
        apparentTemperature = v.findViewById(R.id.apparentTemperature)
        currentTime = v.findViewById(R.id.currentTime)
        alertFab = v.findViewById(R.id.weatherAlertFAB)
        favoriteCity = v.findViewById(R.id.locationStar)
        loadingIcon = v.findViewById(R.id.loading_icon)

        favoriteCity.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                Toast.makeText(activity, "City added to Favorites (not really)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initializeWeatherData()
    }

    private fun setupPreferencesListener() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == "useCurrentLocation") {
                if (prefs.getBoolean("useCurrentLocation", false)) {
                    initializeWeatherData()
                } else {
                    initializeWeatherData(java.lang.Double.parseDouble(prefs.getString("defaultLatitude", "0") ?: "0"), java.lang.Double.parseDouble(prefs.getString("defaultLongitude", "0") ?: "0"))
                }
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.search_button) {

            val typedValue = TypedValue()
            val theme = context!!.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            val color = typedValue.data

            // Start the autocomplete intent.
            val intent = PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken()!!)
                    .placeOptions(PlaceOptions.builder()
                            .language(Locale.getDefault().language)
                            .backgroundColor(color)
                            .statusbarColor(color)
                            .toolbarColor(color)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(activity)
            startActivityForResult(intent, 1)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val feature = PlaceAutocomplete.getPlace(data)
                initializeWeatherData(feature.center()!!.latitude(), feature.center()!!.longitude())
            }
        }
    }

    /**
     * fetches data based on users current location
     * updateConditions() is called if the response is successful
     */
    private fun initializeWeatherData() {
        // get the user's current location
        val location = WeatherLocation(activity)
        prefs.edit().putString("defaultLatitude", java.lang.Double.toString(location.latitude)).commit()
        prefs.edit().putString("defaultLongitude", java.lang.Double.toString(location.longitude)).commit()

        // get the current conditions.  If the response is successful, it will call
        // updateConditions() below with the retrieved data

        WeatherAPIUtils.getCurrentWeatherData(location.latitude, location.longitude, this)

    }

    /**
     * fetches data based on a given location
     * @param lat latitude of given location
     * @param lon longitude of given location
     */
    fun initializeWeatherData(lat: Double, lon: Double) {
        WeatherAPIUtils.getCurrentWeatherData(lat, lon, this)
    }

    fun usingCelsius(): Boolean {
        return prefs.getBoolean("useCelsius", true)
    }


    /**
     * updates views based on retrieved data
     * @param weatherData data retrieved from API call
     */
    fun updateConditions(weatherData: Forecast) {
        prefs.edit().putString("weatherData", Gson().toJson(weatherData)).apply()

        loadingIcon.visibility = View.INVISIBLE

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1500

        updateToolbar(activity!!, weatherData.latitude, weatherData.longitude)
        temperatureView.text = activity!!.getString(R.string.formattedTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.currently.temperature))
        highTemp.text = activity!!.getString(R.string.formattedHighTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.daily.data[0].temperatureMax))
        lowTemp.text = activity!!.getString(R.string.formattedLowTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.daily.data[0].temperatureMin))
        description.text = weatherData.currently.summary
        humidity.text = activity!!.getString(R.string.formattedPrecipitationChance, String.format(Locale.getDefault(), "%.0f", weatherData.hourly.data[0].humidity * 100))
        apparentTemperature.text = activity!!.getString(R.string.formattedApparentTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.currently.apparentTemperature))
        if (usingCelsius()) {
            windSpeed.text = activity!!.getString(R.string.formattedWindSpeedC, String.format(Locale.getDefault(), "%.0f", weatherData.currently.windSpeed))
        } else {
            windSpeed.text = activity!!.getString(R.string.formattedWindSpeedF, String.format(Locale.getDefault(), "%.0f", weatherData.currently.windSpeed))
        }
        currentTime.text = activity!!.getString(R.string.formattedTime, String.format(Locale.getDefault(), "%s", convertUnixTimeToHours(weatherData.currently.time, weatherData.timezone)))
        if (weatherData.alerts != null) {
            alertFab.visibility = View.VISIBLE
            alertFab.startAnimation(anim)
            alertFab.setOnClickListener { fabMorph(weatherData) }
        } else {
            alertFab.visibility = View.GONE
        }


        temperatureView.startAnimation(anim)
        highTemp.startAnimation(anim)
        lowTemp.startAnimation(anim)
        description.startAnimation(anim)
        humidity.startAnimation(anim)
        humidityImage.visibility = View.VISIBLE
        humidityImage.startAnimation(anim)
        weatherIcon.startAnimation(anim)
        windIcon.visibility = View.VISIBLE
        windIcon.startAnimation(anim)
        windSpeed.startAnimation(anim)
        apparentTemperature.startAnimation(anim)
        currentTime.startAnimation(anim)

        setIconAnimation(weatherData)
    }

    private fun fabMorph(weatherData: Forecast) {
        MorphDialog.Builder(activity, alertFab)
                .title(weatherData.alerts[0].title)
                .content(weatherData.alerts[0].description)
                .positiveText("OK")
                .positiveColor(resources.getColor(R.color.colorAccent))
                .show()
    }

    fun convertUnixTimeToHours(time: Int, timezone: String): String {
        val date = Date(time * 1000L)
        // format of the date
        val jdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        jdf.timeZone = TimeZone.getTimeZone(timezone)

        return jdf.format(date)
    }

    fun setIconAnimation(weatherData: Forecast) {
        if (prefs.getBoolean("useDarkTheme", false)) {
            when (weatherData.currently.icon) {
                "clear-day" -> weatherIcon.setAnimation("sun.json")

                "clear-night" -> weatherIcon.setAnimation("clearNight.json")

                "rain" -> weatherIcon.setAnimation("drizzle.json")

                "cloudy" -> weatherIcon.setAnimation("Overcast.json")

                "partly-cloudy-day" -> weatherIcon.setAnimation("partlyCloudy.json")

                "partly-cloudy-night" -> weatherIcon.setAnimation("partlyCloudyNight.json")

                "snow" -> weatherIcon.setAnimation("snow.json")

                "sleet" -> weatherIcon.setAnimation("sleet.json")

                "wind" -> weatherIcon.setAnimation("wind.json")

                "fog" -> weatherIcon.setAnimation("fog.json")

                else -> weatherIcon.setAnimation("heavyThunderstorm.json")
            }
        } else {
            when (weatherData.currently.icon) {
                "clear-day" -> weatherIcon.setAnimation("sun.json")

                "clear-night" -> weatherIcon.setAnimation("clearNightLightTheme.json")

                "rain" -> weatherIcon.setAnimation("drizzleLightTheme.json")

                "cloudy" -> weatherIcon.setAnimation("OvercastLightTheme.json")

                "partly-cloudy-day" -> weatherIcon.setAnimation("partlyCloudyLightTheme.json")

                "partly-cloudy-night" -> weatherIcon.setAnimation("partlyCloudyNightLightTheme.json")

                "snow" -> weatherIcon.setAnimation("snowLightTheme.json")

                "sleet" -> weatherIcon.setAnimation("sleetLightTheme.json")

                "wind" -> weatherIcon.setAnimation("windLightTheme.json")

                "fog" -> weatherIcon.setAnimation("fogLightTheme.json")

                else -> weatherIcon.setAnimation("heavyThunderstormLightTheme.json")
            }
        }

        weatherIcon.playAnimation()
    }

    /**
     * updates the city label in the toolbar
     * @param activity activity containing the toolbar
     * @param lat latitude of city
     * @param lon longitude of city
     */
    fun updateToolbar(activity: Activity?, lat: Double, lon: Double) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        lateinit var addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var title = "Weather"
        if (addresses.size > 0) {
            if (addresses[0].locality != null) {
                title = addresses[0].locality
            } else if (addresses[0].subLocality != null) {
                title = addresses[0].subLocality
            } else if (addresses[0].subAdminArea != null) {
                title = addresses[0].subAdminArea
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_LONG).show()
        }

        (activity as MainActivity).supportActionBar?.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        pullToRefresh.isEnabled = verticalOffset == 0
    }

    override fun onResume() {
        super.onResume()
        appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appBarLayout.removeOnOffsetChangedListener(this)
    }
}
