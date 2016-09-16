package com.example.ldy.myweather.util;

import android.content.Context;
import android.util.Log;

import com.example.ldy.myweather.db.WeatherDB;
import com.example.ldy.myweather.modle.City;
import com.example.ldy.myweather.modle.County;
import com.example.ldy.myweather.modle.Province;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LDY on 2016/9/14.
 * 由于中国天气网的API好像不能用了，于是我在多方搜索后选定了和风天气API，但是它不提供省市县联动的查找方式，
 * 所以我的想法是用一个数组保存所有的城市，在遍历相应的省市时，在查重后得到列表，并截取相应的号码作为省市
 * 编号。直接从数据库中获取省市县列表，在查具体城市的天气时再用城市Id。
 */
public class Utility {
    //创建三个方法，分别将从文件中解析的数据存储到数据库中。
    private static final String FILE_NAME = "CityId";

    private static Set<String> provinces = new HashSet<String>();
    private static Set<String> cities = new HashSet<String>();

    public static void handleAllCities(final WeatherDB db, final Context context, final HttpCallbackListener listener) {
        File file = context.getDatabasePath(WeatherDB.DB_NAME);
        if (file.exists()) {
            Log.d("Utility", "数据库已经存在");
            //return;
        }

        //如果数据库未建好，开启一个线程来创建数据库
        Log.d("handleallcities", "线程外");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("run", "线程内");
                copy(db, context, listener);
            }
        }).start();
    }

    //从asset文件夹中的CityId中读取文件存储到“weather_demo”数据库中
    private static void copy(WeatherDB db, Context context, HttpCallbackListener listener) {
        Province pro = new Province();
        City cit = new City();
        County cou = new County();

        try {
            Log.d("copy", "开始复制");
            BufferedReader reader = null;
            /*得到SQLiteDatabase实例，准备存储数据
            WeatherOpenHelper myOpenHelper =
                    new WeatherOpenHelper(context, WeatherDB.DB_NAME, null, WeatherDB.VERSION);
            SQLiteDatabase SQLdb = myOpenHelper.getWritableDatabase();
            */

            //得到asset文件夹里的文件的输出流
            InputStream in = context.getAssets().open(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

            //读取每一行，按相应类别存进数据库
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] cityInfo = line.trim().split(",");
                if (cityInfo != null && cityInfo.length > 0) {
                    String code = cityInfo[0];
                    String countyName = cityInfo[2];
                    String cityName = cityInfo[3];
                    String provinceName = cityInfo[4];

                    //
                    if (!provinces.contains(provinceName)) {
                        pro.setProvinceCode(code);
                        pro.setProvinceName(provinceName);
                        db.saveProvince(pro);
                        provinces.add(provinceName);
                    }

                    if (!cities.contains(cityName)) {
                        cit.setCityCode(code);
                        cit.setCityName(cityName);
                        cit.setProvinceId(pro.getProvinceName());
                        db.saveCity(cit);
                        cities.add(cityName);
                    }

                    cou.setCountyCode(code);
                    cou.setCountyName(countyName);
                    cou.setCityId(cit.getCityName());
                    db.saveCounty(cou);
                }
            }

            if (listener != null) {
                listener.onFinish(true);
            }
        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
        Log.d("Utility", "数据库存储完成");
    }
}
