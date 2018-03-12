package com.mapuni.administrator.activity;


import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.fragment.MainFragment;
import com.mapuni.administrator.service.LocationJobService;
import com.mapuni.administrator.service.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends BaseActivity {
    protected String[]  needPermissions={
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private boolean isNeedCheck=true;
    @Override
    public int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        doService();//执行定位服务
        initMainView();
        setToolbarisVisibility(false);
//        setToolbarBack(mSupportActionBar,false);
//        setToolbarTitle("考核评价");
    }

    private void doService() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//            startService(new Intent(this, LocationJobService.class));

            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, LocationJobService.class));  //指定哪个JobService执行操作
            builder.setMinimumLatency(TimeUnit.SECONDS.toMillis(5)); //执行的最小延迟时间
//        builder.setOverrideDeadline(TimeUnit.MILLISECONDS.toMillis(15));  //执行的最长延时时间
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);  //非漫游网络状态
            builder.setBackoffCriteria(TimeUnit.MINUTES.toMillis(1)/6, JobInfo.BACKOFF_POLICY_LINEAR);  //线性重试方案
            builder.setRequiresCharging(false); // 未充电状态

            jobScheduler.schedule(builder.build());

        }else {
            startService(new Intent(this, LocationService.class));
        }
    }

    private void initMainView() {
        MainFragment mainFragment = MainFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, mainFragment,
                        MainFragment.class.getName()).commit();
    }

    @Override
    public void initData() {
       
    }

    private long exitTime = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                exitApp();
            }
            return true;

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> neesRequestPermissionList=findDeniedPermissions(permissions);
        if(null!=neesRequestPermissionList&&neesRequestPermissionList.size()>0){
            ActivityCompat.requestPermissions(this,neesRequestPermissionList.toArray(
                    new String[neesRequestPermissionList.size()] ),100  );
        }
    }

    private List<String> findDeniedPermissions(String[] permissions){
        List<String> needRequestPermissions=new ArrayList<>();
        for (String perm:permissions){
            if(ContextCompat.checkSelfPermission(this,perm)!= PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,perm)){
                needRequestPermissions.add(perm);
            }
        }
        return needRequestPermissions;
    }

    private boolean verifyPermissions(int[] grantResults){
        for (int result:grantResults){
            if(result!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(!verifyPermissions(grantResults)){
                showMissingPermissionDialog();
                isNeedCheck=false;
            }
        }

    }

    private void showMissingPermissionDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("title");
        builder.setMessage("请授权");
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSetting();
            }
        });
    }

    private void startAppSetting(){
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(intent);
    }
}
