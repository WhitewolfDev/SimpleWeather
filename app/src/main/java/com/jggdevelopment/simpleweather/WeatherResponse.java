package com.jggdevelopment.simpleweather;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("coord")
    public Coord coord;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public float dt;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}

class Weather {
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
}

class Clouds {
    @SerializedName("all")
    public float all;
}

class Rain {
    @SerializedName("3h")
    public float h3;
}

class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;
}

class Main {
    @SerializedName("temp")
    public float temp;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;
    @SerializedName("temp_min")
    public float temp_min;
    @SerializedName("temp_max")
    public float temp_max;

    public int getTempMin(String format) {
        if (format.equals("F")) {
            return (int) Math.round(temp_min * 9/5 - 459.67);
        } else if (format.equals("C")) {
            return (int) Math.round(temp_min - 273.15);
        } else if (format.equals("K")) {
            return Math.round(temp_min);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getTempMax(String format) {
        if (format.equals("F")) {
            return (int) Math.round(temp_max * 9/5 - 459.67);
        } else if (format.equals("C")) {
            return (int) Math.round(temp_max - 273.15);
        } else if (format.equals("K")) {
            return Math.round(temp_max);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getTemperature(String format) {
        if (format.equals("F")) {
            return (int) Math.round(temp * 9/5 - 459.67);
        } else if (format.equals("C")) {
            return (int) Math.round(temp - 273.15);
        } else if (format.equals("K")) {
            return Math.round(temp);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getHumidity() {
        return Math.round(humidity);
    }

    public int getPressure() {
        return Math.round(pressure);
    }
}

class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}

class Coord {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;
}