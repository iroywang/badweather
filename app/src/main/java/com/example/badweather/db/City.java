package com.example.badweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by iroy on 2017/9/1.
 */

public class City extends DataSupport {
    int id;
    String cityName;
    int cityCode;
    int provincecode;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public int getProvinceCode() {
        return provincecode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provincecode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
