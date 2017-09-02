package com.example.badweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by iroy on 2017/9/1.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int citycode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return citycode;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setCityCode(int cityid) {
        this.citycode= cityid;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
