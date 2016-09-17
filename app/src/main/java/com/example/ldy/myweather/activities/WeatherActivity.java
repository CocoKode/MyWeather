package com.example.ldy.myweather.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ldy.myweather.R;
import com.example.ldy.myweather.util.HttpCallbackListener;
import com.example.ldy.myweather.util.HttpUtil;
import com.example.ldy.myweather.util.Utility;

/**
 * Created by LDY on 2016/9/16.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    private LinearLayout weatherInfoLayout;
    //分别用于显示城市名，发布时间，天气信息，最低气温，最高气温，当前日期，切换城市按钮和更新天气按钮
    private TextView cityNameText;
    private TextView publishTimeText;
    private TextView weatherText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        //初始化
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishTimeText = (TextView)findViewById(R.id.publish_text);
        weatherText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra("county_code");

        //有县级代号时查询天气，没有直接显示本地天气
        if (!TextUtils.isEmpty(countyCode)) {
            publishTimeText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);

            Log.d("weatherInfoActivity", "有县级代号");
            queryWeatherInfo(countyCode);
        } else {
            showWeather();
        }

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishTimeText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String cityCode = prefs.getString("cityCode", "");
                if (!TextUtils.isEmpty(cityCode))
                    queryWeatherInfo(cityCode);

                break;
            default:
                break;
        }

    }

    //查询天气代号对应的天气
    private void queryWeatherInfo(String cityCode) {
        String address =
                "https://api.heweather.com/x3/weather?cityid="
                + cityCode + "&key=b51c1e24f7e549e3a0661fe4e1ab81d7";
        Log.d("queryWeatherInfo", "查服务器");
        queryFromServer(address, cityCode);
    }

    private void queryFromServer(String address, String cityCode) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(boolean finish) {
                return;
            }

            @Override
            public void onBack(String result) {
                Log.d("onback", "处理返回数据");
                Utility.handleWeatherResponse(WeatherActivity.this, result);

                //更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeText.setText("同步失败");
                    }
                });
            }
        });
    }

    //从sharedpreferences文件中读取天气信息并显示
    private void showWeather() {
        SharedPreferences spfs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(spfs.getString("cityName", ""));
        temp1Text.setText(spfs.getString("temp1", ""));
        temp2Text.setText(spfs.getString("temp2", "") + "℃");
        weatherText.setText(spfs.getString("weather", ""));
        publishTimeText.setText("今天"+spfs.getString("publish_time", "")+"发布");
        currentDateText.setText(spfs.getString("current_time", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
