package com.example.ldy.myweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.ldy.myweather.receiver.autoUpdateReceiver;
import com.example.ldy.myweather.util.HttpCallbackListener;
import com.example.ldy.myweather.util.HttpUtil;
import com.example.ldy.myweather.util.Utility;

/**
 * Created by LDY on 2016/9/18.
 */
public class autoUpdateService extends Service {
    //Utility u = new Utility(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHour = 8 * 1000 * 60 * 60;
        long triggerTime = SystemClock.elapsedRealtime() + eightHour;
        Intent i = new Intent(this, autoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        SharedPreferences spfs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityCode = spfs.getString("cityCode", "");
        String address =
                "https://api.heweather.com/x3/weather?cityid="
                        + cityCode + "&key=b51c1e24f7e549e3a0661fe4e1ab81d7";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(boolean finish) {
                return;
            }

            @Override
            public void onBack(String result) {
                Utility.handleWeatherResponse(autoUpdateService.this, result);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
