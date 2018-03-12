package com.jy.environment.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jy.environment.webservice.UrlComponent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

public class okUtils {

	protected static final String TAG = "YYF";

	// 必须在子线程中执行
	public static String getJsonByPost(String url, Map<String, String> params) {
		try {
			Response response = OkHttpUtils.post().url(url).params(params)
					.build().execute();
			return response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	// 必须在子线程中执行
	public static String getJsonByGet(String url) {
		try {
			Response response = OkHttpUtils.get().url(url).build().execute();
			return response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	// /**
	// * 获取消息列表
	// * */
	// public static void requestMessageList(String sessionId,String rows,String
	// page, Callback call) {
	// String url = PathManager.getMessageList;
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("sessionId", sessionId);
	// params.put("rows", rows);
	// params.put("page", page);
	// NetManager.post(url, params, call);
	// }

	// public static void requestPollutionOfMap(String currentStatonType,String
	// pollutionType,Callback call ){
	// String url =
	// "http://192.168.4.250:8045/api/v1/PdsApp/getAppMapStationList";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("stationType", currentStatonType);
	// params.put("pollutionFactor", pollutionType);
	// okUtils.post(url, params, call);
	// }

	public static void requestPollutionOfMap(String currentStatonType,
			String pollutionType, Callback call) {
		// String url =
		// "http://192.168.4.250:8045/api/v1/PdsApp/getAppMapStationList";
		String url = UrlComponent.getDataOfMap;
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("stationType", currentStatonType);
		// params.put("pollutionFactor", pollutionType);
		url = url + "?stationType=" + currentStatonType + "&pollutionFactor="
				+ pollutionType;
		okUtils.get(url, call);
	}

	public static void get(String url, Callback call) {
		OkHttpUtils.get().url(url).build().execute(call);
	}

	public static void post(String url, Map<String, String> params,
			Callback call) {
		OkHttpUtils.post().url(url).params(params).build().execute(call);
	}

	public static void downloadFile(View view,String url, Callback call) {
		OkHttpUtils//
				.get()//
				.url(url)//
				.build()//
				.execute(call);
	}

}
