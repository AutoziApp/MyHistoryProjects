package com.jy.environment.services;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.Exceedmodel;
import com.jy.environment.util.ACache;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;

//向服务器端请求的服务
public class NewsPushService extends Service {
	private KjhttpUtils http;
	private File efFile;
	private String externalStorage;
	private ACache eACache;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		externalStorage = getFilesDir() + File.separator + "cache";
		efFile = new File(externalStorage);
		eACache = ACache.get(NewsPushService.this, efFile);
		http = new KjhttpUtils(NewsPushService.this, efFile);
		String userId = WeiBaoApplication.getUserId();
		if (null == userId || "".equals(userId)) {
			userId = "0";
		}
		final String id = userId;
		final String url = UrlComponent.getNewsPathAaccept_Get(userId);
		MyLog.i(">>>>>>>>urleggh" + url);
		if (NetUtil.getNetworkState(NewsPushService.this) != NetUtil.NETWORN_NONE) {

			Exceedmodel arg0 = eACache.getAsString(url);
			MyLog.i(">>>>>arg0" + arg0);
			if (arg0.isFlag()) {
				JsonUtils.jsonNewsAccept(NewsPushService.this, arg0.getData(), id);
				stopSelf();
			} else {
				http.getString(url, 240, new DownGet() {

					@Override
					public void downget(String arg0) {
						// TODO Auto-generated method stub
						if (!arg0.equals("")) {
							JsonUtils.jsonNewsAccept(NewsPushService.this,
									arg0, id);
							stopSelf();
						}
					}
				});
			}
		} else {
			stopSelf();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		MyLog.i(">>>>>shengngn");
		super.onStart(intent, startId);
	}

}
