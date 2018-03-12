package com.mapuni.android.baidupush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mapuni.android.base.Global;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.netprovider.WebServiceProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Utils {
	public static final String TAG = "MobileEnforcement";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	public static String logStringCache = "";

	// 鑾峰彇ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	// 鐢╯hare preference鏉ュ疄鐜版槸鍚︾粦瀹氱殑寮�叧銆傚湪ionBind涓旀垚鍔熸椂璁剧疆true锛寀nBind涓旀垚鍔熸椂璁剧疆false
	public static boolean hasBind(Context context) {
		String result="";
		try{
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			String flag = sp.getString("bind_flag", "");
			String sbindUserName = sp.getString("loginUserId", "");
			String userId=sp.getString("baiDuId", "");
			String channelId=sp.getString("channelId", "");
			if ("ok".equalsIgnoreCase(flag)) {
				if(!Global.getGlobalInstance().getUserid().equals(sbindUserName)){
					//重新向后台发送接口
					result="false";
					//result=callWebService(userId,channelId);
					//setBind(context, true, Global.getGlobalInstance().getUserid(), userId, channelId);
				}
				if("true".equals(result))
					return true;
				else
					return false;	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static void setBind(Context context, boolean flag,String userId,String baiDuId,String channelId) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		if(!flag){
			editor.putString("loginUserId", userId);
			editor.putString("baiDuId", baiDuId);
			editor.putString("channelId", channelId);
		}
		editor.commit();
	}

	public static List<String> getTagsList(String originalText) {
		if (originalText == null || originalText.equals("")) {
			return null;
		}
		List<String> tags = new ArrayList<String>();
		int indexOfComma = originalText.indexOf(',');
		String tag;
		while (indexOfComma != -1) {
			tag = originalText.substring(0, indexOfComma);
			tags.add(tag);

			originalText = originalText.substring(indexOfComma + 1);
			indexOfComma = originalText.indexOf(',');
		}

		tags.add(originalText);
		return tags;
	}

	public static String getLogText(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString("log_text", "");
	}

	public static void setLogText(Context context, String text) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("log_text", text);
		editor.commit();
	}
	public static  String callWebService(String yunID,String channelID){
		String result="";
		String methodName="UploadUserBaiduPushXX";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("sysUserID", Global.getGlobalInstance().getUserid());
		param.put("yunUserID", yunID);
		param.put("channelID", channelID);

		String token="";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		param.put("token", token);
		params.add(param);


		try {
			result = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName,params, Global.getGlobalInstance().
					getSystemurl()+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_BOOLEAN, true);
			if(result==null){
				result="false";
			}
		} catch (IOException e) {

			e.printStackTrace();
			result="false";
		}
		return result;
	}

}
