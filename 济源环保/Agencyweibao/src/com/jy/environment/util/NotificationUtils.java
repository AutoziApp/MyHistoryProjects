package com.jy.environment.util;

import java.io.File;
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.RemoteViews;

import com.jy.environment.R;
import com.jy.environment.activity.LoadingActivity;
import com.jy.environment.activity.UpdateNotificationActivity;
import com.jy.environment.controls.WeiBaoApplication;

public class NotificationUtils {
	public static Boolean is_update = false;
	// 下载包保存路径
	private static String savePath = Environment.getExternalStorageDirectory()
			.toString() + "/weibaoupdate/";

	private final static String saveFileName = savePath
			+ "WeiBao_AppUpdate.apk";

	public static void show(Context context, int icon, String notice_city,
			String notice_weather, String notice_temps, String notice_quality,
			String notice_feelTemp, String notice_time, String high_temp,
			String low_temp) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = icon;
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.tickerText = "空气质量实时天气";

		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.notification_weather);
		contentView.setImageViewResource(R.id.weather_icon, icon);
		contentView.setTextViewText(R.id.notice_city, notice_city);
		contentView.setTextViewText(R.id.notice_weather, notice_weather);
		contentView.setTextViewText(R.id.notice_low_temp, "L:" + low_temp);
		contentView.setTextViewText(R.id.notice_high_temp, "H:" + high_temp);
		contentView.setTextViewText(R.id.notice_quality, notice_quality);
		contentView.setTextViewText(R.id.notice_feelTemp, notice_feelTemp);
		contentView.setTextViewText(R.id.notice_time, notice_time);

		notification.contentView = contentView;

		Intent intent = new Intent(context, LoadingActivity.class);

		notification.contentIntent = PendingIntent.getActivity(
				context.getApplicationContext(), 0, intent, 0);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// intent, PendingIntent.FLAG_ONE_SHOT);
		// // 点击状态栏的图标出现的提示信息设置
		// notification.setLatestEventInfo(context, title, content,
		// pendingIntent);
		//
		notificationManager.notify(WeiBaoApplication.NOTIFICATION_ID1,
				notification);

	}

	public static void updateNot(Context context, boolean flag) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.logo31;
		CharSequence tickerText = "河南省空气质量有新版本更新";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.update_notification);
		contentView.setTextViewText(R.id.name2, "版本更新提示");
		is_update = true;
		notification.contentView = contentView;
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutee = calendar.get(Calendar.MINUTE);
		String minute = minutee + "";
		minute = (minutee != 0 && minutee < 10) ? 0 + minute : minute;
		contentView.setTextViewText(R.id.notifi_time, hour + ":" + minute);
		if (flag) {
			File ApkFile = new File(saveFileName);
			// 是否已下载更新文件
			MyLog.i(">>>>>>>>>apkFile" + saveFileName);
			if (ApkFile.exists()) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setDataAndType(Uri.parse("file://" + ApkFile.toString()),
						"application/vnd.android.package-archive");
				notification.contentIntent = PendingIntent.getActivity(
						context.getApplicationContext(), 0, i, 0);
				notificationManager.notify(112, notification);
				return;
			} else {
				Intent it = new Intent(context,
						UpdateNotificationActivity.class);
				notification.contentIntent = PendingIntent.getActivity(
						context.getApplicationContext(), 0, it, 0);
				notificationManager.notify(111, notification);
			}
		}
	}

	private static void installApk(Context context) {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);

	}
}
