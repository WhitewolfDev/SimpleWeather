package com.jggdevelopment.simpleweather

import `in`.adityaanand.morphdialog.MorphDialog
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import androidx.core.view.GravityCompat

import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.jggdevelopment.simpleweather.adapters.CustomPagerAdapter
import com.jggdevelopment.simpleweather.models.Forecast
import com.jggdevelopment.simpleweather.services.WeatherAPIUtils

import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
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
    private lateinit var alertFab: FloatingActionButton
    private lateinit var favoriteCity: CheckBox
    private lateinit var loadingIcon: LottieAnimationView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var locationButton: ImageView

    private var weatherData: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)
        if (prefs.getBoolean("useDarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        super.onCreate(savedInstanceState)
        // Setup the content view
        setContentView(R.layout.activity_main)

        Mapbox.getInstance(this, BuildConfig.mapboxAPI_KEY)

        setupViews()

        // if location permission not allowed, initialize weather on default location, else do it with found location
        if (!prefs.getBoolean("locationPermissionAllowed", false)) {
            initializeWeatherData(java.lang.Double.parseDouble(prefs.getString("defaultLatitude", "0") ?: "0"), java.lang.Double.parseDouble(prefs.getString("defaultLongitude", "0") ?: "0"))
        } else {
            initializeWeatherData()
        }

        setupPreferencesListener()

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {}

        drawerLayout.addDrawerListener(drawerToggle)

        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeButtonEnabled(true)
        drawerToggle.syncState()

        pullToRefresh.setOnRefreshListener {

            if (weatherData != null) {
                initializeWeatherData(weatherData!!.latitude, weatherData!!.longitude)
            }

            pullToRefresh.isRefreshing = false
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)

        drawerLayout = this.findViewById(R.id.drawer_layout)

        // setup ViewPager and its adapter
        val viewPager = findViewById<ViewPager>(R.id.main_viewpager)
        viewPager.adapter = CustomPagerAdapter(this, this.supportFragmentManager)
        viewPager.offscreenPageLimit = (viewPager.adapter as CustomPagerAdapter).count

        // setup TabLayout and connect it to the ViewPager
        val tabLayout = findViewById<View>(R.id.weather_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

        locationButton = toolbar.findViewById(R.id.locationButton)
        locationButton.setOnClickListener { requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200) }

        appBarLayout = findViewById(R.id.appBarLayout)
        appBarLayout.addOnOffsetChangedListener(this)
        pullToRefresh = findViewById(R.id.pullToRefresh)
        pullToRefresh.isEnabled = false

        temperatureView = findViewById(R.id.temperature)
        highTemp = findViewById(R.id.high_temp)
        lowTemp = findViewById(R.id.low_temp)
        description = findViewById(R.id.weatherDescription)
        humidity = findViewById(R.id.humidity)
        humidityImage = findViewById(R.id.humidity_image)
        weatherIcon = findViewById(R.id.weatherIcon)
        windIcon = findViewById(R.id.wind_icon)
        windSpeed = findViewById(R.id.wind_speed)
        apparentTemperature = findViewById(R.id.apparentTemperature)
        currentTime = findViewById(R.id.currentTime)
        alertFab = findViewById(R.id.weatherAlertFAB)
        favoriteCity = findViewById(R.id.locationStar)
        loadingIcon = findViewById(R.id.loading_icon)

        favoriteCity.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                Toast.makeText(this, "City added to Favorites (not really)", Toast.LENGTH_SHORT).show()
            }
        }
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

    /**
     * fetches data based on users current location
     * updateConditions() is called if the response is successful
     */
    private fun initializeWeatherData() {
        // get the user's current location
        val location = WeatherLocation(this)
        prefs.edit().putString("defaultLatitude", java.lang.Double.toString(location.latitude))
                .putString("defaultLongitude", java.lang.Double.toString(location.longitude)).apply()

        // get the current conditions.  If the response is successful, it will call
        // updateConditions() below with the retrieved data

        WeatherAPIUtils.getCurrentWeatherData(this, location.latitude, location.longitude, object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                if (response.isSuccessful) {
                    response.body()?.let { updateConditions(it) }
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {

            }
        })
    }

    /**
     * fetches data based on a given location
     * @param lat latitude of given location
     * @param lon longitude of given location
     */
    fun initializeWeatherData(lat: Double, lon: Double) {
        WeatherAPIUtils.getCurrentWeatherData(this, lat, lon, object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                if (response.isSuccessful) {
                    response.body()?.let { updateConditions(it) }
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {

            }
        })
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
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
        }

        (activity as MainActivity).supportActionBar?.title = title
    }

    private fun fabMorph(weatherData: Forecast) {
        MorphDialog.Builder(this, alertFab)
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

    fun usingCelsius(): Boolean {
        return prefs.getBoolean("useCelsius", true)
    }

    /**
     * updates views based on retrieved data
     * @param weatherData data retrieved from API call
     */
    fun updateConditions(weatherData: Forecast) {
        this.weatherData = weatherData
        pullToRefresh.isEnabled = true

        prefs.edit().putString("weatherData", Gson().toJson(weatherData)).apply()

        loadingIcon.visibility = View.INVISIBLE

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1500

        updateToolbar(this, weatherData.latitude, weatherData.longitude)
        temperatureView.text = this.getString(R.string.formattedTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.currently.temperature))
        highTemp.text = this.getString(R.string.formattedHighTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.daily.data[0].temperatureMax))
        lowTemp.text = this.getString(R.string.formattedLowTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.daily.data[0].temperatureMin))
        description.text = weatherData.currently.summary
        humidity.text = this.getString(R.string.formattedPrecipitationChance, String.format(Locale.getDefault(), "%.0f", weatherData.hourly.data[0].humidity * 100))
        apparentTemperature.text = this.getString(R.string.formattedApparentTemperature, String.format(Locale.getDefault(), "%.0f", weatherData.currently.apparentTemperature))
        if (usingCelsius()) {
            windSpeed.text = this.getString(R.string.formattedWindSpeedC, String.format(Locale.getDefault(), "%.0f", weatherData.currently.windSpeed))
        } else {
            windSpeed.text = this.getString(R.string.formattedWindSpeedF, String.format(Locale.getDefault(), "%.0f", weatherData.currently.windSpeed))
        }
        currentTime.text = this.getString(R.string.formattedTime, String.format(Locale.getDefault(), "%s", convertUnixTimeToHours(weatherData.currently.time, weatherData.timezone)))
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initializeWeatherData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.search_button) {

            val typedValue = TypedValue()
            val theme = this.theme
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
                    .build(this)
            startActivityForResult(intent, 1)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val feature = PlaceAutocomplete.getPlace(data)
                initializeWeatherData(feature.center()!!.latitude(), feature.center()!!.longitude())
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.send_feedback) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/email")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("cameron@jggmetalshop.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Atom Weather Feedback")
            startActivity(Intent.createChooser(intent, "Send Feedback:"))
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        pullToRefresh.isEnabled = verticalOffset == 0
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
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
