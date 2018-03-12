package com.mapuni.administrator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mapuni.administrator.activity.loginAc.LoginActivity;
import com.mapuni.administrator.activity.myNewsAc.DelayApplicationActivity;
import com.mapuni.administrator.activity.myNewsAc.DownTaskDetailActivity;
import com.mapuni.administrator.activity.myNewsAc.UpTaskDetailActivity;
import com.mapuni.administrator.activity.wdAc.MessageDetailActivity;
import com.mapuni.administrator.app.MyApplication;
import com.mapuni.administrator.bean.JPushBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
				checkSession(context,bundle);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	public void checkSession(final Context context, final Bundle bundle){
		String  sessionId = (String) SPUtils.getSp(MyApplication.getContextObject(), "sessionId", "");
		NetManager.checkSession(sessionId, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {//session无效
				Intent intent = new Intent(context, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(intent);
			}

			@Override
			public void onResponse(String response, int id) {
				try {
					JSONObject jsonObject=new JSONObject(response);
					if(0==jsonObject.optInt("status")){//session有效
						String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
						JPushBean bean=new Gson().fromJson(extras,JPushBean.class);
						if(true==bean.isIsTask()&&false==bean.isIsNews()){//是任务推送
							switch (bean.getTaskType()){
								case "0":
									Intent intent=new Intent(context, UpTaskDetailActivity.class);
									intent.putExtra("handlingRecordUuid",bean.getTaskUuid());
									intent.putExtra("taskType",bean.getTaskType()+"");
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
									context.startActivity(intent);
									break;
								case "1":
									Intent intent1=new Intent(context, DownTaskDetailActivity.class);
									intent1.putExtra("handlingRecordUuid",bean.getTaskUuid());
									intent1.putExtra("taskType",bean.getTaskType()+"");
									intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
									context.startActivity(intent1);
									break;
								case "2":
									Intent  intent2=new Intent(context,DelayApplicationActivity.class);
									intent2.putExtra("handlingRecordUuid",bean.getTaskUuid());
									intent2.putExtra("taskType",bean.getTaskType()+"");
									intent2.putExtra("delayApplyTaskType",Integer.parseInt(bean.getDelayApplyTaskType()));
									intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
									context.startActivity(intent2);
									break;
								default:
									break;

							}

						}else if(true==bean.isIsNews()&&false==bean.isIsTask()){//是通知推送
							Intent intent = new Intent(context, MessageDetailActivity.class);
							intent.putExtra("id",bean.getNewsUuid());
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
							context.startActivity(intent);
						}

					}else {//session无效
						Intent intent = new Intent(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
						context.startActivity(intent);

					}



				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
//	}
}
