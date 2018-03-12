package com.mapuni.android.netprovider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * FileName: Net.java Description:�ж�����״̬
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����3:42:01
 */
public class Net {

	private static final String TAG = "Net";

	private static final boolean enabled = false;
	private static WifiManager WifiMana;
	private static ConnectivityManager conMgre;

	/**
	 * ��������������
	 * 
	 * @param context
	 * @return true ������ͨ false ����������
	 */
	public static boolean checkNet(Context context) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// �жϵ�ǰ�����Ƿ��Ѿ�����
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
	 * ��ȡ������MAC��ַ
	 * 
	 * @param context
	 * @return MAC��ַ
	 */
	public static String getLocalIMEIAddress(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * ����url�Ƿ���ͨ
	 * 
	 * @param url
	 * @return
	 */
	public static boolean checkURL(String url) {
		try {
			URL Url = new URL(url);
			HttpURLConnection hc = (HttpURLConnection) Url.openConnection();
			hc.setConnectTimeout(3500);// ���ó�ʱʱ��3.5��
			hc.setReadTimeout(3500);
			if (hc.getResponseCode() == 200)
				return true;
		} catch (MalformedURLException e) {
			// Log.e(TAG, e.getMessage());
		} catch (SocketTimeoutException e) {
			// Toast.makeText(Login.this, "���������ӳ�ʱ���������磡", Toast.LENGTH_SHORT)
			// .show();
		} catch (IOException e) {
			// Log.e(TAG, e.getMessage());
		}
		return false;
	};

	/**
	 * 
	 * ����Ƿ������磬��ǰ��ʲô����
	 */
	public static boolean isConnect(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			return conManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * ����Ƿ�����WiFi
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
	 * �򿪻��߹ر�wifi
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
	 * �򿪻��߹ر��ƶ�����
	 */
	public static void setMobileNet(Context context, boolean enabled) {
		conMgre = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class<?> conMgrClass = null; // ConnectivityManager��
		Field iConMgrField = null; // ConnectivityManager���е��ֶ�
		Object iConMgr = null; // IConnectivityManager�������
		Class<?> iConMgrClass = null; // IConnectivityManager��
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled����

		try {
			conMgrClass = Class.forName(conMgre.getClass().getName());
			iConMgrField = conMgrClass.getDeclaredField("mService");
			iConMgrField.setAccessible(true);
			iConMgr = iConMgrField.get(conMgre);
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
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
	 * ���������ý���
	 */
	public static void OpenWirelessSettings(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("����������ʾ").setMessage("�������Ӳ�����,�Ƿ��������?").setPositiveButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;

				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

}
