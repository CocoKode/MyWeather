package com.example.ldy.myweather.modle;

/**
 * Created by LDY on 2016/9/14.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private String provinceId;

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }


}
