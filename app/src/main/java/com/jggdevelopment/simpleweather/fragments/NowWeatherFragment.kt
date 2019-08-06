package com.jggdevelopment.simpleweather.fragments


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle

import androidx.fragment.app.Fragment

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.jggdevelopment.simpleweather.R
import com.jggdevelopment.simpleweather.models.Forecast
import kotlinx.android.synthetic.main.view_weather_now.*

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NowWeatherFragment : Fragment() {

    private lateinit var weatherData: Forecast
    private lateinit var prefs: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var v: View
    private lateinit var tempLineChart: LineChart
    private lateinit var precipLineChart: LineChart
    private lateinit var tempChartLabel: TextView
    private lateinit var precipChartLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.view_weather_now, container, false)

        prefs = activity!!.getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE)
        setupPreferencesListener()

        return v
    }

    fun setupViews() {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1500

        tempLineChart = tempChart
        precipLineChart = precipChart
        tempChartLabel = temp_chart_label
        precipChartLabel = precip_chart_label

        tempLineChart.animation = anim
        tempLineChart.visibility = View.VISIBLE
        precipLineChart.animation = anim
        precipLineChart.visibility = View.VISIBLE
        tempChartLabel.animation = anim
        tempChartLabel.visibility = View.VISIBLE
        precipChartLabel.animation = anim
        precipChartLabel.visibility = View.VISIBLE

        setupTempChart()
        setupPrecipChart()


        tempLineChart.invalidate()
        precipLineChart.invalidate()
    }

    private fun setupTempChart() {
        val theme = activity!!.theme
        val chartColorValue = TypedValue()
        theme.resolveAttribute(R.attr.chartTempColor, chartColorValue, true)
        val chartColor = resources.getColor(chartColorValue.resourceId)

        val hours = weatherData.hourly!!.data
        val data = ArrayList<Entry>()
        for (i in 0..23) {
            data.add(Entry(i.toFloat(), Math.round(hours!![i].temperature!!).toFloat()))
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

                val date = Date(hours!![value.toInt()].time!!.toInt() * 1000L)
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
    }

    private fun setupPrecipChart() {
        val theme = activity!!.theme
        val chartColorValue = TypedValue()
        theme.resolveAttribute(R.attr.chartPrecipColor, chartColorValue, true)
        val chartColor = resources.getColor(chartColorValue.resourceId)

        val hours = weatherData.hourly!!.data
        val data = ArrayList<Entry>()
        for (i in 0..23) {
            data.add(Entry(i.toFloat(), Math.round(hours!![i].precipProbability * 100).toFloat()))
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

                val date = Date(hours!![value.toInt()].time!!.toInt() * 1000L)
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
    }

    private fun setupPreferencesListener() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == "weatherData") {
                weatherData = Gson().fromJson(prefs.getString("weatherData", ""), Forecast::class.java)
                setupViews()
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
