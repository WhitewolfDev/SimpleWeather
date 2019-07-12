package com.jggdevelopment.simpleweather.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertDatum {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("time")
    @Expose
    private int time;
    @SerializedName("expires")
    @Expose
    private int expiryTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(int expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
