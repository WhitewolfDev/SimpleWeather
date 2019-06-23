package com.jggdevelopment.simpleweather;

public enum ModelObject {
    NOW(R.string.now, R.layout.view_weather_now),
    HOURLY(R.string.hourly, R.layout.view_weather_hourly),
    DAILY(R.string.daily, R.layout.view_weather_daily);


    private int titleResId;
    private int layoutResId;

    ModelObject(int titleResId, int layoutResId) {
        this.titleResId = titleResId;
        this.layoutResId = layoutResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
