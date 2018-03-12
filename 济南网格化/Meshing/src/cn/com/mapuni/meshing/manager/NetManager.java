package cn.com.mapuni.meshing.manager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

public class NetManager {
	public static void get(String url, Callback call) {
		OkHttpUtils.get().url(url).build().execute(call);
	}

	public static void downLoadWord(String url, Callback call) {
		NetManager.get(url, call);
	}
}
