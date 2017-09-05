package com.example.badweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by iroy on 2017/9/3.
 */

public class Weather {
    public String status;
    public AQI aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
