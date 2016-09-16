package com.example.ldy.myweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LDY on 2016/9/14.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {
    //province表建表
    public static final String CREATE_PROVINCE = "create table province ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";
    //city表建表
    public static final String CREATE_CITY = "create table city ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id text)";
    //county表建表
    public static final String CREATE_COUNTY = "create table county ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id text)";



    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //数据库中分别建立三张表
        sqLiteDatabase.execSQL(CREATE_PROVINCE);
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
