package com.mapuni.mobileenvironment.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.AuthenActivity;
import com.mapuni.mobileenvironment.activity.MainActivity;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.model.AuthenModel;
import com.mapuni.mobileenvironment.model.LoginModel;
import com.mapuni.mobileenvironment.unzip.SplashModel;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class SplashActivity extends Activity {
    private final static String VERSION_NAME = "versionName";
    /** 启动登录activity */
    private final int START_LOGIN_ACTIVITY = 111;
    private final int INITVIEW = 110;
    /** 版本号 */
    private TextView tvVersion;
    /** 系统名称 */
    private TextView tvSysName;
    /** 解压进度layout */
    private LinearLayout llProgressBar;
    /** activity对应的model */
    private SplashModel splashModel;
    /** 解压的进度 */
    private ProgressBar pBarUnzip;

    private AuthenModel model;
    /** 消息 */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case START_LOGIN_ACTIVITY:
                    goLoginActivity();
                    break;
                case INITVIEW:
                    DataApplication.translucentStatusBar(SplashActivity.this);
                    initData();
                    initConfig();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        if(DataApplication.requestPermission(this)){
            isPass();
            handler.sendEmptyMessage(INITVIEW);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1024: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handler.sendEmptyMessage(INITVIEW);
                } else {
                    Toast.makeText(this,"请批准权限,否则程序无法运行",Toast.LENGTH_SHORT).show();
                    DataApplication.requestPermission(this);
                }
                return;
            }
        }
    }

    /**初始化界面 */
    private void initView(){
        tvVersion = (TextView) findViewById(R.id.versionName);
     //   tvSysName = (TextView) findViewById(R.id.fullscreen_content);
        llProgressBar = (LinearLayout) findViewById(R.id.fullscreen_content_controls);
        pBarUnzip = (ProgressBar) findViewById(R.id.unzip_indicator);
    }

    /**初始化数据 */
    private void initData(){
        //需要解压时进行解压
        splashModel = new SplashModel();
//        splashModel.getIp(this);
        if(splashModel.isNeedUnzip(this)){
            llProgressBar.setVisibility(View.VISIBLE);
//            /storage/emulated/0/mobilegrid/data/MobileEnforcement.db
            splashModel.unZip(this, pBarUnzip, new SplashModel.UnZipCallBack() {
                @Override
                public void callback(boolean isOk) {
                    llProgressBar.setVisibility(View.INVISIBLE);
                    if (isOk) {//解压成功 修改本地版本号码
                        if(splashModel.updateLocalVersion(SplashActivity.this)){
                            handler.sendEmptyMessageDelayed(START_LOGIN_ACTIVITY, 1500);
                        }else{
                            Toast.makeText(SplashActivity.this,"版本号更新失败,请重新安装！",Toast.LENGTH_SHORT).show();
                        }
                    } else {//解压失败 给出提示
                        Toast.makeText(SplashActivity.this,"解压失败,请重新安装！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            handler.sendEmptyMessageDelayed(START_LOGIN_ACTIVITY, 1000);
        }
    }




    /** 初始化屏幕的尺寸 */
    private void initConfig(){
        DisplayUitl.getMobileResolution(this);
    }
    /**
     * 获取版本号
     * @param context 上下文
     * @return 版本号
     */
    public static String getAppVersion(Context context){
        PackageManager pm = context.getPackageManager();
        String p = context.getPackageName();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            return pm.getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void isPass(){
        //String url = "http://218.246.81.181:8110/WebService/DeviceInfo.asmx/IsTrustDevice";
        String url = "http://218.246.81.181:8110/WebService/DeviceInfo.asmx/IsTrustDevicePro";
        OkHttpUtils
                .post()//
                .url(url)//
                .addParams("deviceId", AuthenActivity.getIMEI(SplashActivity.this))
                .addParams("phoneBrand", AuthenActivity.getSystemModel()+"-"+AuthenActivity.getSystemVersion())
                .addParams("androidVersion",getAppVersion(this))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        model = (AuthenModel) JsonUtil.jsonToBean(response, AuthenModel.class);
                    }
                });
    }



    /** 进入系统 */
    private void goLoginActivity(){
        tvVersion.setText("版本："+getAppVersion(this));
        LoginModel loginModel = new LoginModel(this);

        //test
      //  loginModel.setAuto(true);

        if(loginModel.isAuto()){
            loginModel.initLoginUser();
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }else{
            Intent intent  =  new Intent(this, LoginActivity.class);
            if(model==null){
                intent.putExtra("authen","");
            }else{
                intent.putExtra("authen",model);
            }
            startActivity(intent);
            this.finish();
        }
    }


}