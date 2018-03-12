package com.mapuni.mobileenvironment.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created at 陈 on 2016/9/14.
 * 定位
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class GPSLocation {
    private final String TAG = "GPSLocation";
    private Context mContext;
    private LocationManager locationManager;
    private List<LocationChangeListener> listeners;
    private static GPSLocation gpsLocation;

    /*定位参数*/
    /*默认使用gps定位*/
    private boolean isGPS = true;
    private long minTime = 10000;
    private long minDistance = 0;

    public static GPSLocation getInstance(Context mContext) {
        if (gpsLocation == null)
            gpsLocation = new GPSLocation(mContext);
        return gpsLocation;
    }

    public interface LocationChangeListener {
        public void onChange(Location location);
    }

    public GPSLocation(Context mContext) {
        gpsLocation = this;
        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        listeners = new ArrayList<>();
    }


    public Location getLocation(String provider) {
        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG,"请开启GPS导航...");
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return null;
        }

        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i(TAG,"请开启网络定位...");
            Toast.makeText(mContext, "请开启网络定位...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return null;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        return locationManager.getLastKnownLocation(provider);
    }

    public Location getLocation() {
        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return null;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        return locationManager.getLastKnownLocation(bestProvider);
    }

    public void startListener() {
        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG,"缺少访问地理位置的权限");
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i(TAG,"请开启网络定位...");
            Toast.makeText(mContext, "请开启网络定位...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

//        //为获取地理位置信息时设置查询条件
//        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
//        //获取位置信息
//        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG,"缺少访问地理位置的权限");
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return;
        }
//        Location location_mark = locationManager.getLastKnownLocation(bestProvider);
        //监听状态
        locationManager.addGpsStatusListener(gpsListtener);
        //绑定监听，有4个参数
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        //参数4，监听
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
        locationManager.removeUpdates(locationListener);
        locationManager.requestLocationUpdates(isGPS ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER
                , minTime, minDistance, locationListener);
    }

    /*停止监听*/
    public void stopListener() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG,"缺少访问地理位置的权限");
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private GpsStatus.Listener gpsListtener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int i) {

        }
    };


    private LocationListener locationListener = new LocationListener() {
        /**
         * 位置信息变化时触发
         */
        @Override
        public void onLocationChanged(Location location) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.i(TAG, "时间：" + simpleDateFormat.format(new Date(location.getTime())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
            Log.i(TAG, "速度：" + location.getSpeed());
            for (LocationChangeListener listener : listeners)
                listener.onChange(location);
        }

        /**
         * GPS状态变化时触发
         */
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            switch (i) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        @Override
        public void onProviderEnabled(String provider) {
//            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Location location_mark = locationManager.getLastKnownLocation(provider);
        }

        /**
         * GPS禁用时触发
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(true);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(true);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /*添加监听*/
    public boolean addChangeListener(LocationChangeListener listener) {
        return listeners.add(listener);
    }

    public boolean removeChangeListener(LocationChangeListener listener) {
        return listeners.remove(listener);
    }

    public void removeAllChangeListener() {
        listeners.clear();
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(long minDistance) {
        this.minDistance = minDistance;
    }

    /*使用gps定位*/
    public void useGPS() {
        isGPS = true;
    }

    /*使用网络定位*/
    public void useNetWork() {
        isGPS = false;
    }
}
