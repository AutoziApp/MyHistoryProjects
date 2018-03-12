package com.mapuni.mobileenvironment.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.mapuni.mobileenvironment.bean.LogonUser;
import com.mapuni.mobileenvironment.bean.StreetBean;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.SharepreferenceUtil;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 自定义的application
 * Created by k on 2015/11/26.
 */
public class DataApplication extends Application {
    /** 上下文 */
    public static Context App;
    /** 登录用户 */
    public static LogonUser LogonUser;

    private static Handler mHandler;

    public static StreetBean streetBean;



    @Override
    public void onCreate() {
        super.onCreate();
        PathManager.initBaseIp(this);
        App = this;
        getStreets();

    }

    public static void showToast(String s){
        Toast.makeText(App,s, Toast.LENGTH_SHORT).show();
    }

    public static Handler getHandler(){
        if(mHandler==null){
            mHandler = new Handler(Looper.getMainLooper());
        }
        return  mHandler;
    }

    //网络获取街道
    private void getStreets() {
        String url = PathManager.GetStreets;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
//        jo.put("time", time.getText().toString().replace("-", "/"));

        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
//                        yutuLoading.showDialog();
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String streetInfo = SharepreferenceUtil.getStreetInfo(App);
                        streetBean = (StreetBean) JsonUtil.jsonToBean(streetInfo, StreetBean.class);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                         SharepreferenceUtil.setStreetInfo(App,response);
                         streetBean = (StreetBean) JsonUtil.jsonToBean(response, StreetBean.class);
                    }
                });
    }



    public static void translucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }
    public static boolean requestPermission(Context c){
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};
        int permissionCheck = ContextCompat.checkSelfPermission((Activity)c,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission((Activity)c,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionPhoneState = ContextCompat.checkSelfPermission((Activity)c,
                Manifest.permission.READ_PHONE_STATE);
        int permissionFile = ContextCompat.checkSelfPermission((Activity)c,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED||permissionLocation ==PackageManager.PERMISSION_GRANTED||
                permissionPhoneState==PackageManager.PERMISSION_GRANTED||permissionFile==PackageManager.PERMISSION_GRANTED){
            return true;
        }else if(permissionCheck == PackageManager.PERMISSION_DENIED||permissionLocation==PackageManager.PERMISSION_DENIED
                ||
                permissionPhoneState==PackageManager.PERMISSION_DENIED||permissionFile==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((Activity)c,permission, 1024);
            return false;
        }
        return false;
    }
}
