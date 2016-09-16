package com.example.ldy.myweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ldy.myweather.modle.City;
import com.example.ldy.myweather.modle.County;
import com.example.ldy.myweather.modle.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LDY on 2016/9/14.
 */
public class WeatherDB {
    //数据库名字
    public static final String DB_NAME = "weather_demo";

    //数据库版本
    public static final int VERSION = 1;

    //创建自身实例和一个数据库实例
    private static WeatherDB weatherDB;
    private SQLiteDatabase db;

    //将构造方法私有化
    private WeatherDB (Context  context) {
        //创建一个建表类的实例
        WeatherOpenHelper dbHelper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        //得到数据库实例，准备写入
        db = dbHelper.getWritableDatabase();
    }

    //获取自身实例，且只能创建一个
    public synchronized static WeatherDB getInstance(Context context) {
        if (weatherDB == null)
            weatherDB = new WeatherDB(context);

        return weatherDB;
    }

    //将province实例存储到数据库
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("province", null, values);
        }
    }

    //从数据库读取省份信息
    public List<Province> loadProvince() {
        //使用ArrayList储存一组province对象返回
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);

        //使用游标取得数据库中数据存入province对象中
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));

                list.add(province);
            } while (cursor.moveToNext());
        }

        return list;
    }

    //将city信息存储到数据库
    public void saveCity (City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("city", null, values);
        }
    }

    //从数据库读取city信息
    public List<City> loadCity(String provinceId) {
        //使用ArrayList储存一组city对象返回
        List<City> list = new ArrayList<City>();
        Cursor cursor =
                db.query("city", null, "province_id = ?", new String[] {provinceId}, null, null, null);

        //使用游标取得数据库中数据存入province对象中
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);

                list.add(city);
            } while (cursor.moveToNext());
        }

        return list;
    }

    //将county信息存储到数据库
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("county", null, values);
        }
    }

    //从数据库读取county信息
    public List<County> loadCounty(String cityId) {
        //使用ArrayList储存一组county对象返回
        List<County> list = new ArrayList<County>();
        Cursor cursor =
                db.query("county", null, "city_id = ?", new String[] {cityId}, null, null, null);

        //使用游标取得数据库中数据存入county对象中
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);

                list.add(county);
            } while (cursor.moveToNext());
        }

        return list;
    }
}
