package com.kazimasum.retro;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
//    @SerializedName("location")
//    private LocationData locationData;

    @SerializedName("current")
    private CurrentWeather currentWeather;

//    public LocationData getLocationData() {
//        return locationData;
//    }
//
//    public void setLocationData(LocationData locationData) {
//        this.locationData = locationData;
//    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }
}
