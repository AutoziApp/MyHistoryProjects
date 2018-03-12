package com.jy.environment.services;

import android.app.IntentService;
import android.content.Intent;

import com.jy.environment.util.ApiClient;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;

public class UniService extends IntentService {
	private String path = UrlComponent.jingpin_path;

	public UniService() {
		super("UniService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {

			if (NetUtil.getNetworkState(UniService.this) != NetUtil.NETWORN_NONE) {
				String json = ApiClient.connServerForResult(path);
				MyLog.i("jingpintuijian>>>>>"+json);
				JsonUtils.jsonService(getApplicationContext(), json);
				stopSelf();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
