package com.jy.environment.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//防止后台服务被杀死
public class MyPubServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		context.startService(new Intent(context, PushService.class));
	}

}
