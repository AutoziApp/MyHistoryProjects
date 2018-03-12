package com.mapuni.administrator.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.mapuni.administrator.R;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class LocationService extends Service {
    AMapLocationClient aMapLocationClient;
    AMapLocationClientOption aMapLocationClientOption;
    String sessionId;
    PowerManager.WakeLock mWakeLock;
    private MediaPlayer mMediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        acquireWakeLock(this);
        initGDLocation();

        mMediaPlayer = MediaPlayer.create(this, R.raw.silent);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    startPlayMusic();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try {
                        Thread.sleep(60*1000);
                        Log.i("qqq","定");
                        aMapLocationClient.startLocation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return Service.START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
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
        aMapLocationClientOption.setInterval(5000);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(false);
        aMapLocationClientOption.setOnceLocationLatest(false);
        aMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
//                    Toast.makeText(LoginActivity.this,aMapLocation.getLatitude()+""+
//                            aMapLocation.getLongitude()+aMapLocation.getAddress(),Toast.LENGTH_SHORT).show();
                   Log.i("qqq","定位中"+aMapLocation.getLatitude()+""+aMapLocation.getLongitude()+aMapLocation.getAddress());
                    NetManager.uploadLatLog(sessionId, "" + aMapLocation.getLongitude(), aMapLocation.getLatitude() + "", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
//                            Log.i("bbb",e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("qqq",response);
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



    @Override
    public void onDestroy() {
        aMapLocationClient.onDestroy();
        stopPlayMusic();
        Intent intent = new Intent("com.example.demo.destroy");
        sendBroadcast(intent);
        releaseWakeLock();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    private void startPlayMusic(){
        if(mMediaPlayer != null){
            Log.i("qqq","播放音乐");
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic(){
        if(mMediaPlayer != null){
            Log.i("qqq","停止音乐");
            mMediaPlayer.stop();
        }
    }
}
