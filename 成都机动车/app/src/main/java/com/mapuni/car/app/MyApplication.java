package com.mapuni.car.app;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;


/**
 * Created by yawei on 2017/4/6.
 */

public class MyApplication extends Application {
    private static int UpdateCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "71d446eb46", false);
//        netControl = new NetControl();
//        netControl.messageCount(SharepreferenceUtil.getUpdateTime(this),call);
    }
//    private StringCallback call = new StringCallback() {
//        @Override
//        public void onError(Call call, Exception e, int id) {
//        }
//
//        @Override
//        public void onResponse(String response, int id) {
//            String s = response;
//            Map map = JsonUtil.jsonToMap(response);
//            double count = (double) map.get("count");
//            UpdateCount = (int) count;
//        }
//    };
//    public static int getUpDateCount(){
//        return UpdateCount;
//    }
}
