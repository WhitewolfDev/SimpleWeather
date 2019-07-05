package com.jggdevelopment.simpleweather.adapters;

import com.robinhood.spark.SparkAdapter;

public class SparkViewAdapter extends SparkAdapter {
    private float[] yData;

    public SparkViewAdapter(float[] yData) {
        this.yData = yData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }
}
