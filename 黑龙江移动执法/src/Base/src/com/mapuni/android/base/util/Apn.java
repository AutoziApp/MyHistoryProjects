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
 * Description: �ֻ�APN����
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-6 ����02:15:29
 */
public class Apn {
	
	/** ��ȡ�ֻ�����apn�б��ַ */
	String allPath = "content://telephony/carriers";
	/** ��ȡ�ֻ���ǰʹ��apn��Ϣ��ַ*/
	String currentPath = "content://telephony/carriers/preferapn";
	/** ����apn��Ϣ����Դ��־������ǰapn��Ϣ����Դ��־�� */
	Uri allUri, currentUri;
	/** ������ѯ���ݵĶ��� */
	ContentResolver cResolver;

	/**
	 * ���췽��
	 * @param context �����Ķ���
	 */
	Context mContext;
	public Apn(Context context) {
		allUri = Uri.parse(allPath);
		currentUri = Uri.parse(currentPath);
		cResolver = context.getContentResolver();
		this.mContext = context;
	}

	/**
	 * Description: ��ȡ����APN
	 * @return ����apn�б���Ϣ
	 * List<ApnInfo>
	 * @author �����
	 * Create at: 2012-12-6 ����02:20:56
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
	 * Description: ��ȡ��ǰѡ��APN
	 * @return ѡ�е�apn��Ϣ
	 * ApnInfo
	 * @author Administrator
	 * Create at: 2012-12-6 ����02:21:27
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
	 * Description: ���APN
	 * @param apnInfo ��apn����
	 * @return ����ӵ�apn��uri
	 * Uri
	 * @author �����
	 * Create at: 2012-12-6 ����02:21:56
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
	 * Description:  * ����APN
	 * @param apnInfo Ĭ��apn
	 * @return 1 �ɹ�
	 * int
	 * @author �����
	 * Create at: 2012-12-6 ����02:23:22
	 */
	public int updateApn(BaseApnInfo apnInfo) {
		/** ������apn����֮ǰ���������ж������������Ƿ���ã����������Ҫ�������رա� */
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
	 * Description: ��ȡָ��apn��id
	 * @param uri ָ��apn��uri
	 * @return apn��ID
	 * String
	 * @author �����
	 * Create at: 2012-12-6 ����02:24:06
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
	 * Description: ɾ��ָ��apn��id
	 * @param uri ָ��apn��uri
	 * @author �����
	 * Create at: 2012-12-6 ����02:24:35
	 */
	public void deleteApn(BaseApnInfo apninfo) {
		cResolver.delete(allUri, "apn in (?)",
				new String[] { apninfo.getApnName() });
	}

	/**
	 * Description: ��ȡ�����ļ�����ָ��apn
	 * void
	 * @author �����
	 * Create at: 2012-12-6 ����02:25:09
	 */
	public void setReturnAPN() {
		HashMap<String, Object> dataMap = DisplayUitl.getDataXML("apn_net");
		if (!"true".equalsIgnoreCase(dataMap.get("isedited").toString())) {
			return;
		}

		/** ����ָ��apn */
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
	 * android 4.0�Ժ�İ汾ȥ����WRITE_APN_SETTINGSȨ�޸����ֶ����� ��apn���ý���
	 */
	public void OpenApnSettings() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("APN������ʾ")
				.setMessage("�Ƿ��л���ר������  ?")
				.setPositiveButton("����", new DialogInterface.OnClickListener() {

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
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}
