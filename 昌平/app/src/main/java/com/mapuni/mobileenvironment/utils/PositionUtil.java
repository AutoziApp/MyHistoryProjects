package com.mapuni.mobileenvironment.utils;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapException;

public class PositionUtil {


    /**
     * 获取定位信息并存储到sharedpreferrence
     */
    public static void setLocation(final Context context) {
        //声明AMapLocationClient类对象
        AMapLocationClient mLocationClient = null;

        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;


        mLocationClient = new AMapLocationClient(context);
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        double jingdu = amapLocation.getLatitude();//获取纬度
                        double weidu = amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        amapLocation.getCountry();//国家信息
                        double[] latlngs = new double[]{0.0, 0.0};
                        latlngs[0] = jingdu;
                        latlngs[1] = weidu;
                        SharepreferenceUtil.setLatlog(context, latlngs);//将当前位置经纬度存入sharepreference中
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.i("app", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        Toast.makeText(context, "定位失败，请查看权限设置", Toast.LENGTH_SHORT);
                    }
                }

            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }


    /**
     * 计算距离
     *
     * @param var0
     * @param var1
     * @return
     */
    public static float calculateLineDistance(double[] var0, double[] var1) {
        if (var0 != null && var1 != null) {
//            double var4 = var0.longitude;
//            double var6 = var0.latitude;
//            double var8 = var1.longitude;
//            double var10 = var1.latitude;
            double var4 = var0[1];
            double var6 = var0[0];
            double var8 = var1[1];
            double var10 = var1[0];
            var4 *= 0.01745329251994329D;
            var6 *= 0.01745329251994329D;
            var8 *= 0.01745329251994329D;
            var10 *= 0.01745329251994329D;
            double var12 = Math.sin(var4);
            double var14 = Math.sin(var6);
            double var16 = Math.cos(var4);
            double var18 = Math.cos(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.sin(var10);
            double var24 = Math.cos(var8);
            double var26 = Math.cos(var10);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var18 * var16;
            var28[1] = var18 * var12;
            var28[2] = var14;
            var29[0] = var26 * var24;
            var29[1] = var26 * var20;
            var29[2] = var22;
            double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
            return (float) ((Math.asin(var30 / 2.0D) * 1.27420015798544E7D) / 1000);

        } else {
            try {
                throw new AMapException("非法坐标值");
            } catch (AMapException var32) {
                var32.printStackTrace();
                return 0.0F;
            }
        }
    }
}
