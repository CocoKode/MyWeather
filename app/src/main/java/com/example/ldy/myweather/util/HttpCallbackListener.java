package com.example.ldy.myweather.util;

/**
 * Created by LDY on 2016/9/14.
 */
public interface HttpCallbackListener {
    //添加onFinish和onError方法
    void onFinish(boolean finish);

    void onError(Exception e);
}
