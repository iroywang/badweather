package com.example.badweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by iroy on 2017/9/1.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
