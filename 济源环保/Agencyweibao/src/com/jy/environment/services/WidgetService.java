package com.jy.environment.services;

import com.jy.environment.provider.WeiBaoWidgetProvider;
import com.jy.environment.util.MyLog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetService extends Service {
    Context context;

    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    private static final String TAG = "MyService";

    @Override
    public void onCreate() {
	super.onCreate();
	context = this;
	MyLog.i("onCreate()");
    }

    @Override
    public void onDestroy() {
	MyLog.i("onDestroy()");
	super.onDestroy();
	unregisterReceiver(mTimePickerBroadcast);
    }

    @Override
    public void onRebind(Intent intent) {
	super.onRebind(intent);
	MyLog.i("onRebind()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
	super.onStart(intent, startId);
	MyLog.i("onStart()");
	// 注册系统每分钟提醒广播（注意：这个广播只能在代码中注册）
	IntentFilter updateIntent = new IntentFilter();
	updateIntent.addAction("android.intent.action.TIME_TICK");
	registerReceiver(mTimePickerBroadcast, updateIntent);
	// AppWidgetManager manager = AppWidgetManager.getInstance(this);
	// // 方法二:闹钟模式
	// RemoteViews updateView = WidgetProvider.updateTime(this);
	//
	// if (updateView != null) {
	// manager.updateAppWidget(new ComponentName(this,
	// WidgetProvider.class), updateView);
	// }

	// // 设置下次执行时间,每秒刷新一次
	// long now = System.currentTimeMillis();
	// long updateMilis = 1000;
	//
	// PendingIntent pendingIntent = PendingIntent.getService(this, 0,
	// intent,
	// 0);
	// // Schedule alarm, and force the device awake for this update
	// AlarmManager alarmManager = (AlarmManager)
	// getSystemService(Context.ALARM_SERVICE);
	// alarmManager.set(AlarmManager.RTC_WAKEUP, now + updateMilis,
	// pendingIntent);
	//
	// // 方法一:开辟线程
	// // MyThread thread = new MyThread(this, manager);
	// // thread.start();
	// //
	// stopSelf();
    }

    // 方法一：开辟线程
    // class MyThread extends Thread {
    // private Context context;
    // private RemoteViews views;
    // private AppWidgetManager widgetManager;
    //
    // public MyThread(Context context, AppWidgetManager widgetManager) {
    // this.context = context;
    // this.widgetManager = widgetManager;
    //
    // }
    //
    // @Override
    // public void run() {
    // while (true) {
    // views = MyWidgetProvider.updateTime(context);
    // widgetManager.updateAppWidget(new ComponentName(context,
    // MyWidgetProvider.class), views);
    // try {
    // sleep(1000);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    @Override
    public boolean onUnbind(Intent intent) {
	MyLog.i("onUnbind");
	return super.onUnbind(intent);

    }
    // 广播接收者去接收系统每分钟的提示广播，来更新时间
    private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    RemoteViews updateView = WeiBaoWidgetProvider.updateTimeService(context);
	    if (updateView != null) {
		manager.updateAppWidget(new ComponentName(context,
			WeiBaoWidgetProvider.class), updateView);
	    }

	}
    };

}
