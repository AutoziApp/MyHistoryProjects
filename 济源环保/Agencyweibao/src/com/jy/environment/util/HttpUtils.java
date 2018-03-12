package com.jy.environment.util;

import java.util.Map;

import com.jy.environment.webservice.UrlComponent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

public class HttpUtils {
	
	
	
	
	
	
	/**
	 * 获取实时数据
	 * @param url
	 * @param callback
	 * @author tianfy
	 */
	public static void getRealTimeData(String url,Callback callback){
		HttpUtils.get(url, callback);
	}
	
	//获取用环比数据
	public static void getTongHuanBi(String timeType,Callback callback){
		HttpUtils.get(UrlComponent.getTongHuanBi+timeType, callback);
	}
	
	public static void post(String url, Map<String, String> params,
			Callback callback) {
		OkHttpUtils.post().url(url).params(params).build().execute(callback);
	}

	public static void get(String url, Callback callback) {
		OkHttpUtils.get().url(url).build().execute(callback);
	}

}
