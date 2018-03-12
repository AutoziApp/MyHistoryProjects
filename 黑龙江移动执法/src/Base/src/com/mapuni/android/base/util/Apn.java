package com.mapuni.android.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseApnInfo;


/**
 * FileName: Apn.java
 * Description: 手机APN对象
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-6 下午02:15:29
 */
public class Apn {
	
	/** 获取手机所有apn列表地址 */
	String allPath = "content://telephony/carriers";
	/** 获取手机当前使用apn信息地址*/
	String currentPath = "content://telephony/carriers/preferapn";
	/** 所有apn信息的资源标志符，当前apn信息的资源标志符 */
	Uri allUri, currentUri;
	/** 用来查询数据的对象 */
	ContentResolver cResolver;

	/**
	 * 构造方法
	 * @param context 上下文对象
	 */
	Context mContext;
	public Apn(Context context) {
		allUri = Uri.parse(allPath);
		currentUri = Uri.parse(currentPath);
		cResolver = context.getContentResolver();
		this.mContext = context;
	}

	/**
	 * Description: 获取所有APN
	 * @return 所有apn列表信息
	 * List<ApnInfo>
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:20:56
	 */
	public List<BaseApnInfo> getAllApn() {
		List<BaseApnInfo> apnList = new ArrayList<BaseApnInfo>();
		Cursor cr = cResolver.query(allUri, null, null, null, null);
		while (cr != null && cr.moveToNext()) {
			BaseApnInfo apnInfo = new BaseApnInfo();
			apnInfo.setApnId(cr.getString(cr.getColumnIndex("_id")));
			apnInfo.setApnName(cr.getString(cr.getColumnIndex("apn")));
			apnInfo.setCurrent(cr.getString(cr.getColumnIndex("current")));
			apnList.add(apnInfo);
		}
		cr.close();
		return apnList;
	}

	/**
	 * Description: 获取当前选中APN
	 * @return 选中的apn信息
	 * ApnInfo
	 * @author Administrator
	 * Create at: 2012-12-6 下午02:21:27
	 */
	public BaseApnInfo getCurrentApn() {
		BaseApnInfo apnInfo = null;
		Cursor cr = cResolver.query(currentUri, null, null, null, null);
		while (cr != null && cr.moveToNext()) {
			apnInfo = new BaseApnInfo();
			apnInfo.setApnId(cr.getString(cr.getColumnIndex("_id")));
			apnInfo.setApnName(cr.getString(cr.getColumnIndex("apn")));
			apnInfo.setName(cr.getString(cr.getColumnIndex("name")));
			apnInfo.setMcc(cr.getString(cr.getColumnIndex("mcc")));
			apnInfo.setMnc(cr.getString(cr.getColumnIndex("mnc")));
			apnInfo.setType(cr.getString(cr.getColumnIndex("type")));
			Log.d("carWifi", "---getCurrentApn-----=====" + apnInfo.getApnId());
		}		
		cr.close();
		return apnInfo;
	}

	/**
	 * Description: 添加APN
	 * @param apnInfo 新apn对象
	 * @return 新添加的apn的uri
	 * Uri
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:21:56
	 */
	public Uri addApn(BaseApnInfo apnInfo) {
		Uri uri = null;
		if (apnInfo != null) {
			ContentValues values = new ContentValues();
			values.put("name", apnInfo.getName());
			values.put("apn", apnInfo.getApnName());
			values.put("proxy", apnInfo.getProxy());
			values.put("port", apnInfo.getPort());
			values.put("mmsproxy", "");
			values.put("mmsport", "");
			values.put("user", "");
			values.put("server", "");
			values.put("password", "");
			values.put("mmsc", "");
			values.put("type", apnInfo.getType());
			values.put("mcc", apnInfo.getMcc());
			values.put("mnc", apnInfo.getMnc());
			values.put("numeric", apnInfo.getNumeric());
			uri = cResolver.insert(allUri, values);
			Log.d("carWifi", "---addApn-uri----=====" + uri);
		}
		return uri;
	}

	/**
	 * Description:  * 更改APN
	 * @param apnInfo 默认apn
	 * @return 1 成功
	 * int
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:23:22
	 */
	public int updateApn(BaseApnInfo apnInfo) {
		/** 在设置apn网络之前，首先是判断蓝牙适配器是否可用，如果可用需要把蓝牙关闭。 */
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled()) {
			adapter.disable();
		}
		int x = -1;
		ContentValues values = new ContentValues();
		// values.put("apn_id", apnInfo.getApnId());
		values.put("Name", apnInfo.getName());
		values.put("apn", apnInfo.getApnName());
		values.put("numeric", apnInfo.getNumeric());
		values.put("mcc", apnInfo.getMcc());
		values.put("mnc", apnInfo.getMnc());
		values.put("type", apnInfo.getType());
		x = cResolver.update(
				Uri.parse("content://telephony/carriers/"
						+ getCurrentApn().getApnId()), values, null, null);
		Log.d("carWifi", apnInfo.getApnId() + "---updateApn-----=====" + x);
		return x;
	}

	/**
	 * Description: 获取指定apn的id
	 * @param uri 指定apn的uri
	 * @return apn的ID
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:24:06
	 */
	public String getApnId(Uri uri) {
		String id = "-1";
		if (uri != null) {
			Cursor cr = cResolver.query(uri, null, null, null, null);
			while (cr != null && cr.moveToNext()) {
				id = cr.getString(cr.getColumnIndex("_id"));
			}			
			cr.close();
		}
		Log.d("carWifi", "---getApnId-----=====" + id);
		return id;
	}

	/**
	 * Description: 删除指定apn的id
	 * @param uri 指定apn的uri
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:24:35
	 */
	public void deleteApn(BaseApnInfo apninfo) {
		cResolver.delete(allUri, "apn in (?)",
				new String[] { apninfo.getApnName() });
	}

	/**
	 * Description: 读取配置文件设置指定apn
	 * void
	 * @author 王红娟
	 * Create at: 2012-12-6 下午02:25:09
	 */
	public void setReturnAPN() {
		HashMap<String, Object> dataMap = DisplayUitl.getDataXML("apn_net");
		if (!"true".equalsIgnoreCase(dataMap.get("isedited").toString())) {
			return;
		}

		/** 设置指定apn */
		BaseApnInfo apnInfo = new BaseApnInfo();
		apnInfo.setName(dataMap.get("name").toString());
		apnInfo.setApnName(dataMap.get("apn").toString());
		apnInfo.setNumeric(dataMap.get("numeric").toString());
		apnInfo.setMcc(dataMap.get("mcc").toString());
		apnInfo.setMnc(dataMap.get("mnc").toString());
		apnInfo.setType(dataMap.get("type").toString());
		apnInfo.setApnId(Global.getGlobalInstance().getCurrentApnId());
		int i = this.updateApn(apnInfo);
	}
	/*
	 * android 4.0以后的版本去掉了WRITE_APN_SETTINGS权限改用手动设置 打开apn设置界面
	 */
	public void OpenApnSettings() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("APN设置提示")
				.setMessage("是否切换到专用网络  ?")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						intent = new Intent();

						ComponentName component = new ComponentName(
								"com.android.settings",
								"com.android.settings.ApnSettings");
						intent.setComponent(component);
						intent.setAction("android.intent.action.VIEW");
						mContext.startActivity(intent);
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
