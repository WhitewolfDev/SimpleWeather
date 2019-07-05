package com.jggdevelopment.simpleweather.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.jggdevelopment.simpleweather.R;
import com.jggdevelopment.simpleweather.adapters.SparkViewAdapter;
import com.jggdevelopment.simpleweather.models.Forecast;
import com.jggdevelopment.simpleweather.models.HourDatum;
import com.jggdevelopment.simpleweather.models.Hourly;
import com.robinhood.spark.SparkView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NowWeatherFragment extends Fragment {

    private Forecast weatherData;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private View v;
    private LineChart lineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.view_weather_now, container, false);

        prefs = getActivity().getSharedPreferences("com.jggdevelopment.simpleweather", Context.MODE_PRIVATE);
        setupPreferencesListener();

        return v;
    }

    public void setupViews() {
        lineChart = v.findViewById(R.id.tempChart);

        List<HourDatum> hours = weatherData.getHourly().getData();
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            data.add(new Entry((float) i, Math.round(hours.get(i).getTemperature().floatValue())));
        }

        LineDataSet lineDataSet = new LineDataSet(data, "");
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.TRANSPARENT);
        lineDataSet.setCircleRadius(15);
        lineDataSet.setCircleHoleColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setValueTextSize(16f);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.setMinimumWidth(3000);
        lineChart.setMinimumHeight(800);
        lineChart.setDragEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(2f);
        xAxis.setLabelCount(data.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("h a", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawLabels(false);

        lineChart.invalidate();
    }

    private void setupPreferencesListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("weatherData")) {
                    weatherData = new Gson().fromJson(prefs.getString("weatherData", ""), Forecast.class);
                    setupViews();
                }

            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);
    }
}
