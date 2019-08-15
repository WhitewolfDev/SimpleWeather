package com.jggdevelopment.simpleweather.ui.weather.current

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.data.db.entity.weather.WeatherResponse
import com.jggdevelopment.simpleweather.ui.base.ScopedFragment
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModel
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModelFactory
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class CurrentWeatherFragment : ScopedFragment(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {

    override val kodein by closestKodein()
    private val viewModelFactory: WeatherResponseViewModelFactory by instance()

    private lateinit var viewModel: WeatherResponseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WeatherResponseViewModel::class.java)

        pullToRefresh.setOnRefreshListener(this)

        bindUI()
    }

    override fun onRefresh() {
        bindUI()
        pullToRefresh.isRefreshing = false
    }

    private fun bindUI() = launch {
        viewModel.refreshWeather()
        val currentWeather = viewModel.weather
        val owner = viewLifecycleOwner

        currentWeather.observe(owner, Observer {
            if (it == null) return@Observer

            loading_icon.visibility = View.GONE

            updateLocation(it.getLocation().getLocationString(context!!.applicationContext))
            updateLocalTime(it.currently.time, it.timezone)
            updateTemperatures(it.currently.temperature.roundToInt(),
                    it.currently.apparentTemperature.roundToInt(),
                    it.daily.data[0].temperatureMin.roundToInt(),
                    it.daily.data[0].temperatureMax.roundToInt())
            updateDescription(it.currently.summary)
            updateEnvironmentals((it.currently.humidity * 100).roundToInt(), it.currently.windSpeed.roundToInt())
            updateWeatherIcon(it.currently.icon)
            updateTempChart(it)
            updatePrecipChart(it)
            updateRefreshTime(it.currently.time, it.timezone)
        })
    }

    private fun chooseUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateLocalTime(time: Int, timezone: String) {
        val localTime = viewModel.unixTimeToActualTime(time, timezone)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Local time: $localTime"
    }

    private fun updateTemperatures(temperature: Int, apparentTemperature: Int, temperatureMin: Int, temperatureMax: Int) {
        temperature_textView.text = "$temperature°"
        apparentTemperature_textView.text = "Feels like $apparentTemperature°"
        high_temp.text = "\u2191 High $temperatureMax"
        low_temp.text = "\u2193 Low $temperatureMin"
    }

    private fun updateDescription(description: String) {
        weatherDescription.text = description
    }

    private fun updateEnvironmentals(humidity: Int, windSpeed: Int) {
        humidity_textView.text = "$humidity%"
        wind_speed.text = "$windSpeed${chooseUnitAbbreviation("kph", "mph")}"
        humidity_image.visibility = View.VISIBLE
        wind_icon.visibility = View.VISIBLE
    }

    private fun updateWeatherIcon(icon: String) {
        when (icon) {
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

        weatherIcon.playAnimation()
    }

    private fun updateTempChart(weatherData: WeatherResponse) {
        val theme = activity!!.theme
        val chartColorValue = TypedValue()
        theme.resolveAttribute(R.attr.chartTempColor, chartColorValue, true)
        val chartColor = resources.getColor(chartColorValue.resourceId)

        val hours = weatherData.hourly.data
        val data = ArrayList<Entry>()
        for (i in 0..23) {
            data.add(Entry(i.toFloat(), hours[i].temperature.roundToInt().toFloat()))
        }

        val lineDataSet = LineDataSet(data, "")
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(Color.TRANSPARENT)
        lineDataSet.circleRadius = 10f
        lineDataSet.circleHoleColor = chartColor
        lineDataSet.color = chartColor
        lineDataSet.valueTextSize = 14f
        //lineDataSet.setDrawFilled(true);
        //lineDataSet.setFillDrawable(chartFillColor);
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        lineDataSet.valueTextColor = chartColor
        lineDataSet.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {

                val formattedValue = value.toInt()
                return Integer.toString(formattedValue)
            }
        }

        val lineData = LineData(lineDataSet)
        val tempLineChart = tempChart
        tempLineChart.data = lineData
        tempLineChart.legend.isEnabled = false
        tempLineChart.setDrawGridBackground(false)
        tempLineChart.description.isEnabled = false
        tempLineChart.setBackgroundColor(Color.TRANSPARENT)
        tempLineChart.minimumWidth = 4000
        tempLineChart.minimumHeight = 800
        tempLineChart.isDragEnabled = false
        tempLineChart.isHighlightPerDragEnabled = false
        tempLineChart.isHighlightPerTapEnabled = false
        tempLineChart.isDoubleTapToZoomEnabled = false
        tempLineChart.setPinchZoom(false)
        tempLineChart.axisRight.setDrawAxisLine(false)
        tempLineChart.axisRight.setDrawGridLines(false)
        tempLineChart.axisRight.setDrawLabels(false)

        val xAxis = tempLineChart.xAxis
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.textColor = chartColor
        xAxis.setCenterAxisLabels(false)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.labelCount = 24
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {

                val date = Date(hours[value.toInt()].time.toInt() * 1000L)
                // format of the date
                val jdf = SimpleDateFormat("h a", Locale.getDefault())
                jdf.timeZone = TimeZone.getTimeZone(weatherData.timezone)

                return jdf.format(date)
            }
        }

        val yAxis = tempLineChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawLabels(false)

        tempLineChart.invalidate()
        tempLineChart.visibility = View.VISIBLE
        temp_chart_label.visibility = View.VISIBLE
    }

    private fun updatePrecipChart(weatherData: WeatherResponse) {
        val theme = activity!!.theme
        val chartColorValue = TypedValue()
        theme.resolveAttribute(R.attr.chartPrecipColor, chartColorValue, true)
        val chartColor = resources.getColor(chartColorValue.resourceId)

        val hours = weatherData.hourly.data
        val data = ArrayList<Entry>()
        for (i in 0..23) {
            data.add(Entry(i.toFloat(), Math.round(hours[i].precipProbability * 100).toFloat()))
        }

        val lineDataSet = LineDataSet(data, "")
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(Color.TRANSPARENT)
        lineDataSet.circleRadius = 10f
        lineDataSet.circleHoleColor = chartColor
        lineDataSet.color = chartColor
        lineDataSet.valueTextSize = 14f
        //lineDataSet.setDrawFilled(true);
        //lineDataSet.setFillDrawable(chartFillColor);
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        lineDataSet.valueTextColor = chartColor
        lineDataSet.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {

                val formattedValue = value.toInt()
                return Integer.toString(formattedValue) + "%"
            }
        }

        val lineData = LineData(lineDataSet)
        val precipLineChart = precipChart
        precipLineChart.data = lineData
        precipLineChart.legend.isEnabled = false
        precipLineChart.setDrawGridBackground(false)
        precipLineChart.description.isEnabled = false
        precipLineChart.setBackgroundColor(Color.TRANSPARENT)
        precipLineChart.minimumWidth = 4000
        precipLineChart.minimumHeight = 800
        precipLineChart.isDragEnabled = false
        precipLineChart.isHighlightPerDragEnabled = false
        precipLineChart.isHighlightPerTapEnabled = false
        precipLineChart.isDoubleTapToZoomEnabled = false
        precipLineChart.setPinchZoom(false)
        precipLineChart.axisRight.setDrawAxisLine(false)
        precipLineChart.axisRight.setDrawGridLines(false)
        precipLineChart.axisRight.setDrawLabels(false)

        val xAxis = precipLineChart.xAxis
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.textColor = chartColor
        xAxis.setCenterAxisLabels(false)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.labelCount = 24
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {

                val date = Date(hours[value.toInt()].time.toInt() * 1000L)
                // format of the date
                val jdf = SimpleDateFormat("h a", Locale.getDefault())
                jdf.timeZone = TimeZone.getTimeZone(weatherData.timezone)

                return jdf.format(date)
            }
        }

        val yAxis = precipLineChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawLabels(false)
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 110f

        precipLineChart.invalidate()
        precipLineChart.visibility = View.VISIBLE
        precip_chart_label.visibility = View.VISIBLE
    }

    fun updateRefreshTime(time: Int, timezone: String) {
        val date = Date(time * 1000L)
        // format of the date
        val jdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        jdf.timeZone = TimeZone.getDefault()

        val formattedDate = jdf.format(date)
        currentTime.text = "Last updated at $formattedDate"
    }
}
