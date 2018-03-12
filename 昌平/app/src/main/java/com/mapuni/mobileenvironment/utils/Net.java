package com.mapuni.mobileenvironment.utils;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * FileName: Net.java
 * Description:判断网络状态
 *
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 下午3:42:01
 */
public class Net {

    private static final String TAG = "Net";

    private static final boolean enabled = false;
    private static WifiManager WifiMana;
    private static ConnectivityManager conMgre;


    /**
     * 检查网络连接情况
     *
     * @param context
     * @return true 网络连通  false 网络无连接
     */
    public static boolean checkNet(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取本机的MAC地址
     *
     * @param context
     * @return MAC地址
     */
    public static String getLocalIMEIAddress(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 测试url是否连通
     *
     * @param url
     * @return
     */
    public static boolean checkURL(String url) {
        try {
            URL Url = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) Url.openConnection();
            hc.setConnectTimeout(3500);// 设置超时时间3.5秒
            hc.setReadTimeout(3500);
            if (hc.getResponseCode() == 200)
                return true;
        } catch (MalformedURLException e) {
//			e.printStackTrace();
        } catch (SocketTimeoutException e) {
            return false;
//			Toast.makeText(Login.this, "服务器连接超时，请检查网络！", Toast.LENGTH_SHORT)
//					.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    ;


    /**
     * 检查是否有网络，当前是什么网络
     */
    public static boolean isConnect(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            return conManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 检查是否连接WiFi
     *
     * @param context
     * @return
     */
    public static boolean checkWiFi(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 打开或者关闭wifi
     */
    public static void setWIFI(Context context, boolean enabled) {
        try {
            WifiMana = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (WifiMana.isWifiEnabled()) {
                WifiMana.setWifiEnabled(enabled);
            } else {
                WifiMana.setWifiEnabled(enabled);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 打开或者关闭移动网络
     */
    public static void setMobileNet(Context context, boolean enabled) {
        conMgre = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            conMgrClass = Class.forName(conMgre.getClass().getName());
            iConMgrField = conMgrClass.getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            iConMgr = iConMgrField.get(conMgre);
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /*
     * 打开网络设置界面
     */
    public static void OpenWirelessSettings(final Context context) {
        Builder builder = new Builder(context);
        builder.setTitle("网络设置提示")
                .setMessage("网络连接不可用,是否进行设置?")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;

                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName(
                                    "com.android.settings",
                                    "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}