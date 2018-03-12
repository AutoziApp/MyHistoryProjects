package com.jy.environment.util;

import java.io.File;

import org.kymjs.aframe.http.HttpConfig;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.StringCallBack;

import com.jy.environment.webservice.UrlComponent;

import android.content.Context;
import android.os.Looper;

public class KjhttpUtils {
	public KJHttp http;
	private ACache aCache;

	public KjhttpUtils(Context context, File file) {
		super();
		// TODO Auto-generated constructor stub
		http = new KJHttp();
		HttpConfig config = new HttpConfig();
		if (file != null) {
			aCache = ACache.get(context, file);
		}
	}

	public void getString(final String url, final int time,final DownGet downGet) {
		
		http.get(UrlComponent.getGoggle(url), new StringCallBack() {
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				if(arg0==null)
				{
					return;
				}
				try {
					String urlString = UrlComponent.getGoggle(url);
					MyLog.i(">>>>>>>>>>ghhghahghg" + urlString);
					MyLog.i(">>>>>>>>>>ghhghahghg" + arg0);
					int m = urlString.indexOf("api/");
					if (m != -1) {
						UrlComponent.baseurl = urlString.substring(0, m + 4);
					}
					if (aCache != null) {
						aCache.put(UrlComponent.getGoggle(url), arg0, time);
					}
					downGet.downget(arg0);
				} catch (Exception e) {
					// TODO: handle exception
					MyLog.e("weibao Exception" + e);
				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				http.get(UrlComponent.getGoggle(url), new StringCallBack() {

					@Override
					public void onSuccess(String arg0) {
						// TODO Auto-generated method stub
						if(arg0==null)
						{
							return;
						}
						try {
							String urlString = UrlComponent.getGoggle(url);
							MyLog.i(">>>>>>>>>>ghhghahghg" + urlString);
							MyLog.i(">>>>>>>>>>ghhghahghg" + arg0);
							int m = urlString.indexOf("api/");
							if (m != -1) {
								UrlComponent.baseurl = urlString.substring(0,
										m + 4);
							}
							if (aCache != null) {
								aCache.put(UrlComponent.getGoggle(url), arg0,
										time);
							}
							downGet.downget(arg0);
						} catch (Exception e) {
							// TODO: handle exception
							MyLog.e("weibao Exception" + e);
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						try {
							downGet.downget("");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
			}
		});

	}

	public interface DownGet {
		public void downget(String arg0);
	}
}
