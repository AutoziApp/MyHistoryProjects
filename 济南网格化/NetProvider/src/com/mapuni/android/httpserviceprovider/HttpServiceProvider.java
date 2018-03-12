package com.mapuni.android.httpserviceprovider;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpServiceProvider {
	private static HttpServiceProvider httpservice;
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("text/x-markdown; charset=utf-8");

	private HttpServiceProvider() {
		super();
	}

	public static HttpServiceProvider getInstance() {
		return httpservice == null ? httpservice = new HttpServiceProvider()
				: httpservice;
	}
    
	/**
	 * 带有参数的post提交
	 * @param url  请求地址
	 * @param values 封装参数 </ber>(LinkedHashMap 避免有序传参)
	 * @return  json or null string
	 */
	public String callHttpServiceForm(String url,
			LinkedHashMap<String, String> values) {
		Request request = new Request.Builder().url(url)
				.post(getRequestBody(values)).build();
		OkHttpClient client = new OkHttpClient();
		try {
			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				return response.body().toString();
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 从map中获取requestbody
	 * 
	 * @param values
	 * @return
	 */
	private RequestBody getRequestBody(LinkedHashMap<String, String> values) {
		FormBody.Builder builder = new FormBody.Builder();
		Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			builder.add(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}
}
