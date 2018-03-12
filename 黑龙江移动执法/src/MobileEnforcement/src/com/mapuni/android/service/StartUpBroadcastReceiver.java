/**
 * 
 */
package com.mapuni.android.service;

import com.mapuni.android.main.MainTabActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author SS
 * 
 *         开机启动的广播
 * 
 */
public class StartUpBroadcastReceiver extends BroadcastReceiver {

	public  final String TAG = "StartUpBroadcastReceiver";
	public  final String ACTION = "android.intent.action.BOOT_COMPLETED";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		/*if (intent.getAction().equals(ACTION)) {
			Intent it = new Intent();
			it.setAction("com.mapuni.android.service.StartUpServices");
			context.startService(it);
		}*/
		
		Intent loctionnewIntent = new Intent(context, RydwServices.class);
		context.startService(loctionnewIntent);
	}
}
