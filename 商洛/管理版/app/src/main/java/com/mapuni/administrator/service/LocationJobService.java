package com.mapuni.administrator.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;

/**
 * Created by yang on 2018/2/8.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LocationJobService extends JobService {

    AMapLocationClient aMapLocationClient;
    AMapLocationClientOption aMapLocationClientOption;
    String sessionId;
    PowerManager.WakeLock mWakeLock;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        acquireWakeLock(this);
        doJob();
//        doService();

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        doService();
        initGDLocation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                        Log.i("qqq","开始定位");
                        aMapLocationClient.startLocation();
            }
        }).start();
        return true;
    }
    private void doJob() {
//        ToastUtil.showShort(this, "测试");
        Log.i("qqq","服务启动");
    }

    private void initGDLocation() {
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        aMapLocationClient=new AMapLocationClient(this);
        aMapLocationClientOption=new AMapLocationClientOption();
        //设置定位模式为高精度定位
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        aMapLocationClientOption.setGpsFirst(true);//设置gps优先，只在高精度模式下有用
        aMapLocationClientOption.setHttpTimeOut(30000);
        //设置定位间隔,单位毫秒,默认为2000ms
        aMapLocationClientOption.setInterval(10000);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setOnceLocationLatest(false);
        aMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(final AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    Log.i("qqq","定位中"+aMapLocation.getLatitude()+""+aMapLocation.getLongitude()+aMapLocation.getAddress());
                    final String latlon=aMapLocation.getLatitude()+""+aMapLocation.getLongitude()+aMapLocation.getAddress();
                    NetManager.uploadLatLog(sessionId, "" + aMapLocation.getLongitude(), aMapLocation.getLatitude() + "", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("qqq","5.0以上"+response+"sessionId="+sessionId
                            +"LAT:"+latlon);
                        }
                    });
                }else {
                    Log.i("aaa","errCode:"+aMapLocation.getErrorCode()
                            +",errInfo:"+aMapLocation.getErrorInfo());
                }
                aMapLocationClient.stopLocation();
            }
        });




    }


    private void doService() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, LocationJobService.class));  //指定哪个JobService执行操作
        builder.setMinimumLatency(TimeUnit.MINUTES.toMillis(1)); //执行的最小延迟时间
//        builder.setOverrideDeadline(TimeUnit.MILLISECONDS.toMillis(15));  //执行的最长延时时间
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);  //非漫游网络状态
         builder.setBackoffCriteria(TimeUnit.MINUTES.toMillis(1)/6, JobInfo.BACKOFF_POLICY_LINEAR);  //线性重试方案
        builder.setRequiresCharging(false); // 未充电状态

        jobScheduler.schedule(builder.build());

    }



    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        releaseWakeLock();
    }

    //申请设备电源锁
    public  void acquireWakeLock(Context context)
    {
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
            }
        }
    }
    //释放设备电源锁
    public  void releaseWakeLock()
    {
        if (null != mWakeLock)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
