package com.mapuni.android.base.service;

import com.mapuni.android.base.util.BaseAutoUpdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * FileName: BaseBroadcastReceiver.java
 * Description:Base的基础广播类
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2013-3-6 下午05:29:16
 */
public class BaseBroadcastReceiver extends BroadcastReceiver {
	public static String BASE_UPDATE = "com.mapuni.android.checkUpdate";

	@Override
	public void onReceive(Context context, Intent intent) {

		/**
		 * 版本更新
		 */
		if (BASE_UPDATE.equals(intent.getAction())) {
			BaseAutoUpdate update = new BaseAutoUpdate();
			if (!update.updateCheck(context)) {
				Toast.makeText(context, "暂无更新信息！", 0).show();
			}
			;
		}
	}

}
