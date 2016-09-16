package com.example.ldy.myweather.modle;

/**
 * Created by LDY on 2016/9/14.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private String cityId;

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }


}
