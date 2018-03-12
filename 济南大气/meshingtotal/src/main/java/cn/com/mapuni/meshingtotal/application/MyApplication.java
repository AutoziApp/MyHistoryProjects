package cn.com.mapuni.meshingtotal.application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 15225 on 2017/7/10.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
