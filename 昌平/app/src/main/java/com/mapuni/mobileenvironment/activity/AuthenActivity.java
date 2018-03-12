package com.mapuni.mobileenvironment.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.login.SplashActivity;
import com.mapuni.mobileenvironment.model.AuthenModel;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class AuthenActivity extends AppCompatActivity implements DialogInterface.OnKeyListener{
    ImageView mProgress;
    Button mCommit;
    EditText nameEdit,phoneEdit;
    public LinearLayout waitLayout,commitLayout;
    TextView nameView,telView,numView;
    TextView stationView,adminPhone;
    AuthenModel model;
    YutuLoading yutuLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);
        waitLayout = (LinearLayout) findViewById(R.id.waitLayout);
        commitLayout = (LinearLayout) findViewById(R.id.commitLayout);
        yutuLoading = new YutuLoading(this);
        yutuLoading.setLoadMsg("认证中","认证失败,请确认网络连接");
        Object obj =   getIntent().getExtras().get("authen");
        if(obj instanceof AuthenModel){
            model = (AuthenModel) obj;
            showView();
            return;
        }
        yutuLoading.showDialog();
        isPass();
    }
//    此处实现Dialog按键监听
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            finish();
        }
        return false;
    }

    private void showView(){
        if(model.getResult().equals("审核中")||model.getResult().equals("未通过")){
            initView();
        }else if(model.getResult().equals("未申请")){
            commitView();
        }else if(model.getResult().equals("通过")){
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }



    public void initView(){
        if(commitLayout.getVisibility()==View.VISIBLE){
            commitLayout.setVisibility(View.GONE);
        }
        AuthenModel.InfoBean bean = (AuthenModel.InfoBean) model.getInfo().get(0);
        waitLayout.setVisibility(View.VISIBLE);
        nameView = (TextView) findViewById(R.id.nameView);
        telView = (TextView) findViewById(R.id.telView);
        numView = (TextView) findViewById(R.id.numView);
        stationView = (TextView) findViewById(R.id.stationView);
        adminPhone = (TextView) findViewById(R.id.admin);
        nameView.setText(bean.getUSERNAME());
        telView.setText("联系方式: "+bean.getPHONENUM());
        numView.setText("机器码: "+bean.getDEVICEID());
        adminPhone.setText("如需帮助请联系管理员:"+model.getLinkInfo());
//        mProgress = (ImageView) findViewById(R.id.progressView);
        if(model.getResult().equals("未通过")){
//            mProgress.setImageResource(getResources().getDrawable(R.mipmap.));
//            mProgress.setVisibility(View.GONE);
            stationView.setText("认证未通过，请联系管理员");
            return;
        }
//        ObjectAnimator animator = ObjectAnimator.ofFloat(mProgress, "rotation", 0, 90,180,270,360,720,1000,0);
//        animator.setRepeatCount(Integer.MAX_VALUE);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(animator);
//        animatorSet.setDuration(3000);
//        animatorSet.start();
    }

    public void commitView(){
        if(waitLayout.getVisibility()==View.VISIBLE){
            waitLayout.setVisibility(View.GONE);
        }
        commitLayout.setVisibility(View.VISIBLE);
        mCommit = (Button) findViewById(R.id.commit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                if(!name.equals("")&&!phone.equals("")){
                    authen(name,phone);
                }else{
                    Toast.makeText(AuthenActivity.this,"请填写必填项",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void isPass(){
       // String url = "http://218.246.81.181:8110/WebService/DeviceInfo.asmx/IsTrustDevice";
        String url = "http://218.246.81.181:8110/WebService/DeviceInfo.asmx/IsTrustDevicePro";
        OkHttpUtils
                .post()//
                .url(url)//
                .addParams("deviceId", AuthenActivity.getIMEI(AuthenActivity.this))
                .addParams("phoneBrand", AuthenActivity.getSystemModel()+"-"+AuthenActivity.getSystemVersion())
                .addParams("androidVersion", SplashActivity.getAppVersion(this))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        yutuLoading.dismissDialog();
                        yutuLoading.showFailed();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        yutuLoading.dismissDialog();
                         model = (AuthenModel) JsonUtil.jsonToBean(response, AuthenModel.class);
                        showView();
                    }
                });
    }
    public void authen(String name,String phone){
        AuthenModel.InfoBean bean = new AuthenModel.InfoBean();
        String url = "http://218.246.81.181:8110/WebService/DeviceInfo.asmx/DeviceApply";
        bean.setDEVICEID(getIMEI(this));
        bean.setPHONENUM(phone);
        bean.setUSERNAME(name);
        List list = new ArrayList();
        list.add(bean);
        model.setInfo(list);
        HashMap params = new HashMap();
        params.put("deviceId",getIMEI(this));
        params.put("userName",name);
        params.put("phoneNum",phone);
        params.put("phoneBrand",getPhoneBrand());
        params.put("androidVersion",getAndroidVersionName());
        params.put("other","");
        OkHttpUtils
                .post()//
                .url(url)//
                .params(params)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String s  = e.toString();
                        Log.i("Lybin",s);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String s  = response;
                        initView();
                        Log.i("Lybin",s);
                    }
                });
    }

    /**
     * 获取手机唯一识别码
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
        String id;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (tm.getDeviceId()!=null){
            id = tm.getDeviceId();
        }else {
            id =  Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
    public String getAndroidVersionName(){
        String versionName="";
        try {
            PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    public String getPhoneBrand(){
        return Build.MODEL;
    }
}
