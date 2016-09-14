package com.example.ldy.myweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    private WeatherDB weatherDB;
    private SQLiteDatabase db;

    //将构造方法私有化
    private WeatherDB (Context  context) {
        //创建一个建表类的实例
        WeatherOpenHelper dbHelper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        //得到数据库实例，准备写入
        db = dbHelper.getWritableDatabase();
    }

    //获取自身实例，且只能创建一个
    public synchronized WeatherDB getInstance(Context context) {
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
}
