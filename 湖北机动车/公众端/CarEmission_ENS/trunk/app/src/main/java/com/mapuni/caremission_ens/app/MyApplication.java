package com.mapuni.caremission_ens.app;

import android.app.Application;

import com.mapuni.caremission_ens.presenter.NetControl;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.mapuni.caremission_ens.utils.SharepreferenceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by yawei on 2017/4/6.
 */

public class MyApplication extends Application {
    private static int UpdateCount = 0;
    public NetControl netControl;
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        
        
        netControl = new NetControl();
        netControl.messageCount(SharepreferenceUtil.getUpdateTime(this),call);
        
        
    }
    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            String s = response;
            Map map = JsonUtil.jsonToMap(response);
            double count = (double) map.get("count");
            UpdateCount = (int) count;
        }
    };
    public static int getUpDateCount(){
        return UpdateCount;
    }
}
