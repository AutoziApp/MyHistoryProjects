package com.jy.environment.receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jy.environment.R;
import com.jy.environment.activity.LoadingActivity;
import com.jy.environment.activity.WebViewActivity;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";

	private static final String VALUENAME_URL = "url";
	private static final String VALUENAME_CITY = "city";
	private static final String VALUENAME_MODEL = "model";
	private SharedPreferencesUtil mSpUtil;
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private CityDB mCityDB;

	@Override
	public void onReceive(Context context, Intent intent) {
		mSpUtil = SharedPreferencesUtil.getInstance(context);
		Bundle bundle = intent.getExtras();
		MyLog.i("onReceive Action :" + intent.getAction());
		MyLog.i("onReceive bundle :" + bundle);
		if (!mSpUtil.getnotificationShow() || !mSpUtil.getMsgNotify()) {
			return;
		}
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);

			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);

			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {

			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {

			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			MyLog.i("Action :" + intent.getAction());
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			MyLog.i("extras :" + extras);
			String url = null;
			try {
				JSONObject jsonObject = new JSONObject(extras);
				url = jsonObject.getString(VALUENAME_URL);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MyLog.i("url:" + url);
			if (null == url || url.equals("")) {
				Intent i = new Intent(context, LoadingActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			} else {
				// 打开自定义的Activity
				Intent i = new Intent(context, WebViewActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("url", url);
				context.startActivity(i);
			}
			// //打开自定义的Activity
			// Intent i = new Intent(context, ShowPushMsgActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {

			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {

		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to CurrentTq
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (EnvironmentCurrentWeatherActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new
	// Intent(EnvironmentCurrentWeatherActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(EnvironmentCurrentWeatherActivity.KEY_MESSAGE,
	// message);
	// if (!ExampleUtil.isEmpty(extras)) {
	// try {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0) {
	// msgIntent.putExtra(EnvironmentCurrentWeatherActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e) {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }

	/**
	 * 发出通知
	 * 
	 * @param context
	 * @param bundle
	 */
	private void processCustomMessage(Context context, Bundle bundle) {
		// String title = bundle
		// .getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);
		MyLog.i(" title : " + title);
		// String message = bundle.getString(JPushInterface.EXTRA_ALERT);
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		MyLog.i("message : " + message);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		MyLog.i("extras : " + extras);
		String url = null;
		String city = null;
		mCityDB = WeiBaoApplication.getInstance().getCityDB();
		initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
		List<String> citys = new ArrayList<String>();

		try {
			JSONObject jsonObject = new JSONObject(extras);
			city = jsonObject.getString(VALUENAME_CITY);
			url = jsonObject.getString(VALUENAME_URL);
			if (initcitys.size() > 0) {
				for (int i = 0; i < initcitys.size(); i++) {
					String cityshuju = initcitys.get(i).get("name").toString();
					citys.add(cityshuju);
				}
				if (!citys.contains(city))
					return;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyLog.i(" isEmpty---(city) : " + city);
		MyLog.i(" !isEmpty(city) : " + !isEmpty(city));
		MyLog.i(" WeiBaoApplication.getInstance().getjPushAliasAndTagscontainscity : "
				+ WeiBaoApplication.getInstance().getjPushAliasAndTags(context)
						.contains(city));
		Iterator<String> it = WeiBaoApplication.getInstance()
				.getjPushAliasAndTags(context).iterator();
		while (it.hasNext()) {
			String str = it.next();
			MyLog.i(" !getjPushAliasAndTags : " + str);
		}
		if (!isEmpty(city)
				&& WeiBaoApplication.getInstance()
						.getjPushAliasAndTags(context).contains(city)) {
			sendNotification(context, title, message, url);
		}
	}

	private void sendNotification(Context context, String title,
			String content, String url) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.logo_64;// 设置通知的图标
		notification.tickerText = title; // 显示在状态栏中的文字
		notification.when = System.currentTimeMillis(); // 设置来通知时的时间
		// notification.flags = Notification.FLAG_NO_CLEAR; //
		// 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
		// notification.flags = Notification.FLAG_ONGOING_EVENT; //
		// 点击清除按钮不会清除消息通知,可以用来表示在正在运行

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
		notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动

		Intent i = new Intent(context, WebViewActivity.class);
		i.putExtra("url", url);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent contentIntent = PendingIntent.getActivity(context,
		// R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				content.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, title, content, contentIntent);
		nm.notify(content.hashCode(), notification);
		// nm.notify(R.string.app_name, notification);
	}

	private boolean isEmpty(String title) {
		if (null == title || "".equals(title)) {
			return true;
		}
		return false;
	}
}
