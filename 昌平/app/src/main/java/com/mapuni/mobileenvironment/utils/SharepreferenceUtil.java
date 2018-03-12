package com.mapuni.mobileenvironment.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mai on 2017/2/6.
 */

public class SharepreferenceUtil {

    private static final String Location = "location";
    private static final String Lat = "lat";
    private static final String Log = "log";
    private static final String street = "street";

    /**
     * 设置经纬度
     *
     * @author:yang
     * @Date: 2016/4/21
     * @Time: 15:36
     */
    public static void setLatlog(Context context, double[] latlog) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                Location, 0).edit();
        editor.putString(Lat, String.valueOf(latlog[0]));
        editor.putString(Log, String.valueOf(latlog[1]));
        editor.commit();
    }
    public static void setBaseIp(Context context, String ip) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "BaseIp", 0).edit();
        editor.putString("BaseIp", ip);
        editor.commit();
    }
    public static String getBaseIp(Context context){
        SharedPreferences _Preferences = context.getSharedPreferences(
                "BaseIp", 0);
            String ip =_Preferences.getString("BaseIp","");
            return ip;

    }
    /**
     * 获取经纬度
     *
     * @author:yang
     * @Date: 2016/4/21
     * @Time: 15:36
     */
    public static double[] getLatlog(Context context) {
        double lat = 40.200;//默认经纬度为环保局经纬度
        double log = 116.234;
        SharedPreferences preferences = context.getSharedPreferences(
                Location, 0);
        if (preferences.contains(Lat) && preferences.contains(Log)) {
            lat = Double.parseDouble(preferences.getString(Lat,"40.200"));
            log = Double.parseDouble(preferences.getString(Log,"116.234"));
            return new double[]{lat, log};
        } else {
            return new double[]{lat, log};
        }
    }
    public static void setStreetInfo(Context context, String street) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "street", 0).edit();
        editor.putString("street", street);
        editor.commit();
    }
    public static String getStreetInfo(Context context){
        SharedPreferences _Preferences = context.getSharedPreferences(
                "street", 0);
        String street =_Preferences.getString("street","");
        return street;

    }
}
