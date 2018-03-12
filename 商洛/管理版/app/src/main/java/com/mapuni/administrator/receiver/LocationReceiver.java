package com.mapuni.administrator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mapuni.administrator.service.LocationService;

/**
 * Created by yang on 2018/2/5.
 */

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.demo.destroy")){
            Log.i("qqq","重启服务");
            Intent sevice = new Intent(context, LocationService.class);
            context.startService(sevice);
        }
    }
}
