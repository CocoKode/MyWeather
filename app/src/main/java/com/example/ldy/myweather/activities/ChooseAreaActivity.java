package com.example.ldy.myweather.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldy.myweather.R;
import com.example.ldy.myweather.db.WeatherDB;
import com.example.ldy.myweather.modle.City;
import com.example.ldy.myweather.modle.County;
import com.example.ldy.myweather.modle.Province;
import com.example.ldy.myweather.util.HttpCallbackListener;
import com.example.ldy.myweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LDY on 2016/9/15.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog pg;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDB db;
    private List<String> dataList = new ArrayList<String>();

    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County> countyList;

    //选中的省
    private Province selectedProvince;
    //选中的市
    private City selectedCity;

    //当前级别
    private int currLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载布局
        setContentView(R.layout.choose_area);

        titleText = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        db = WeatherDB.getInstance(this);

        //响应点击事件
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);
                    queryCity();
                }
                else if (currLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(i);
                    queryCounty();
                }
            }
        });

        //加载省级列表
        queryProvince();
    }

    private void queryProvince() {
        provinceList = db.loadProvince();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province p : provinceList) {
                dataList.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currLevel = LEVEL_PROVINCE;
        } else {
            Log.d("queryProvince", "未查询到省级列表数据");
            queryFromDatabase("province");
        }
    }

    private void queryCity() {
        cityList = db.loadCity(selectedProvince.getProvinceName());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City c : cityList) {
                dataList.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currLevel = LEVEL_CITY;
        } else {
            Log.d("queryCity", "未查询到市级列表数据");
            queryFromDatabase("city");
        }
    }

    private void queryCounty() {
        countyList = db.loadCounty(selectedCity.getCityName());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County cou : countyList) {
                dataList.add(cou.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currLevel = LEVEL_COUNTY;
        } else {
            Log.d("queryCounty", "未查询到县级列表数据");
            queryFromDatabase("county");
        }
    }

    private void queryFromDatabase(final String type) {
        showProgressDialog();
        Utility.handleAllCities(db, this, new HttpCallbackListener() {
            @Override
            public void onFinish(boolean finish) {
                boolean result = false;
                if (finish) {
                    result = true;
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                                queryProvince();
                            else if ("city".equals(type))
                                queryCity();
                            else if ("county".equals(type))
                                queryCounty();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //关闭进度对话框
    private void closeProgressDialog() {
        if (pg != null) {
            pg.dismiss();
        }
    }

    //显示进度对话框
    private void showProgressDialog() {
        if(pg == null) {
            pg = new ProgressDialog(this);
            pg.setMessage("正在加载...");
            pg.setCanceledOnTouchOutside(false);
        }
        pg.show();
    }

    @Override
    public void onBackPressed() {
        if (currLevel == LEVEL_COUNTY)
            queryCity();
        else if (currLevel == LEVEL_CITY)
            queryProvince();
        else
            finish();
    }
}
