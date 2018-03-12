package com.mapuni.android.service;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.GridActivity;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.base.util.Apn;

/**
 * FileName: ReturnHome.java Description:返回主页面的广播接收器 <li>接收广播，返回主页面 <li>
 * 接收广播，退出系统时停止服务 <li>退出系统时切换APN <li>接收短信，实现一些安全机制
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午10:49:56
 */
public class ReturnHome extends BroadcastReceiver {

	/** 返回主页Action */
	public final String ACTION_HOME = "com.mapuni.android.workservice";
	/** 退出系统Action */
	public final String ACTION_EXIT = "EXIT";
	/** 接收短信Action */
	public final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
	/** 启动服务 */
	public final String ACTION_SERVICE = "com.mapuni.action.launch_service";

	/** 开启更新服务 */
	public final String ACTION_START = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果接收到的context是九宫格，就得判断是否是主页的，如果是主页的就不能finish掉，否则finish
		if (ACTION_HOME.equals(intent.getAction())) {
			if (context instanceof GridActivity) {
				Intent it = ((GridActivity) context).getIntent();
				Bundle bundle = it.getExtras();
				IGrid businessObj = (IGrid) bundle.getSerializable("BusinessObj");
				if (businessObj.getGridTitle().equals("移动执法")) {
					return;
				}
			}
			if (context instanceof Activity)
				((Activity) context).finish();
			return;
		} else if (ACTION_EXIT.equals(intent.getAction())) {

			/*
			 * boolean isStopService = Boolean.parseBoolean(String
			 * .valueOf(DisplayUitl.getSettingValue(context,
			 * DisplayUitl.STATUS_BAR_TIPS, true)));
			 */
			// 退出应用时，停止服务
			if (true) {
				Intent serviceIntent = new Intent(context, MessageService.class);
				serviceIntent.setAction(MessageService.ACTION_DISMISSNOTIFICATION);
				context.stopService(serviceIntent);

				// 停止定位服务
				Intent loctionIntent = new Intent(context, GetLocationServer.class);
				context.stopService(loctionIntent);
			}

			// 退出时切换APN
			if (Build.VERSION.SDK_INT <= 10 && Global.getGlobalInstance().isUpdataapn()) {
				Apn apn = new Apn(context);
				apn.setReturnAPN();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		} else if (ACTION_SMS.equals(intent.getAction())) {

			String data = "";
			String phoneNumber = "";
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] dataArray = (Object[]) bundle.get("pdus");
				SmsMessage[] message = new SmsMessage[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					message[i] = SmsMessage.createFromPdu((byte[]) dataArray[i]);
				}
				for (SmsMessage sms : message) { // 电话号
					phoneNumber = sms.getDisplayOriginatingAddress(); // 短信信息
					data = sms.getDisplayMessageBody();
				}
			}
			if (data.contains("$**清除**$")) {// 清除数据指令
				File file = new File(Global.SDCARD_DB_LOCAL_PATH);
				file.delete();
				Toast.makeText(context, "数据已清除！", 0).show();
			}

			Toast.makeText(context, data, 0).show();

		} else if (ACTION_SERVICE.equals(intent.getAction())) {
			String operation = intent.getStringExtra("operation");
			Intent it = new Intent(context, MessageService.class);
			if ("start".equals(operation)) {
				context.startService(it);
				// context.startService(startIntent);
			} else if ("stop".equals(operation)) {
				context.stopService(it);
			}
		}
	}

}
