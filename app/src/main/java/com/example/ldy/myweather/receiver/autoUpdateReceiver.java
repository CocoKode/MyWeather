package com.example.ldy.myweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ldy.myweather.service.autoUpdateService;

/**
 * Created by LDY on 2016/9/18.
 */
public class autoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, autoUpdateService.class);
        context.startActivity(i);
    }
}
