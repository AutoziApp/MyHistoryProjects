/**
 * FileName: httpDownloader.java
 * Description:基础类
 * @author 刘海兵
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2013-08-27
 */

package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.util.LogUtil;

public class FloatingWindowService extends Service {

	private static final String TAG = "FloatingWindowService";

	public static final String OPERATION = "operation";
	public static final int OPERATION_SHOW = 100;
	public static final int OPERATION_HIDE = 101;
	public static final int OPERATION_REMOVE = 102;

	private static final int HANDLE_CHECK_WINDOW = 200;
	private static final int HANDLE_HIDE_WINDOW = 201;
	private static final int HANDLE_REMOVE_WINDOW = 202;

	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	private Button btn_floatView;

	private List<String> homeList;
	private ActivityManager mActivityManager;
	/** dialog中listView绑定数据 *//*
	public ArrayList<HashMap<String, Object>> listData;

	*//** 显示Dialog的广播 *//*
	private static final String ACTION = "com.mapuni.android.bro";
	*//** 发送显示Dialog的广播 *//*
	Intent itBroad = new Intent(ACTION);*/

	private HelperController HelperC = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		homeList = getHomes();

		createFloatWindow();

		HelperC = HelperController.getInstance();

	}

	@Override
	public void onDestroy() {
		LogUtil.i(TAG, "onStart !");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		LogUtil.i(TAG, "onStart !");

		int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);

		switch (operation) {
		case OPERATION_SHOW:
			mHandler.sendEmptyMessage(HANDLE_CHECK_WINDOW);
			break;

		case OPERATION_HIDE:
			mHandler.sendEmptyMessage(HANDLE_HIDE_WINDOW);
			break;

		case OPERATION_REMOVE:
			mHandler.sendEmptyMessage(HANDLE_REMOVE_WINDOW);
			break;
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LogUtil.i(TAG, "msg.what"+msg.what);
			switch (msg.what) {		
		
			case HANDLE_CHECK_WINDOW:
				if (isHome()) {
					if (isAdded) {
						wm.removeView(btn_floatView);
						isAdded = false;
					}
				} else {
					if (!isAdded) {
						wm.addView(btn_floatView, params);
						isAdded = true;
					}
				}
				break;

			case HANDLE_HIDE_WINDOW:
				if (isAdded) {
					wm.removeView(btn_floatView);
					isAdded = false;
				}
				break;

			case HANDLE_REMOVE_WINDOW:
				if (isAdded) {
					wm.removeView(btn_floatView);
					isAdded = false;
				}
				onDestroy();
				break;
			}
		}
	};

	/**
	 * 创建悬浮窗
	 */
	private void createFloatWindow() {

		btn_floatView = new Button(getApplicationContext());
		btn_floatView.setBackgroundResource(R.drawable.helper_btn_s);

		wm = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();

		// 设置window 类型
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		// 设置背景透明效果
		params.format = PixelFormat.RGBA_8888;

		// 设置Window 参数
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 设置悬浮窗在屏幕右上角
		params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

		// 设置悬浮窗的长和宽
		params.width = 56;
		params.height = 75;
		// 设置悬浮窗的按键监听事件
		btn_floatView.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;
			int dx = 0;
			int dy = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 按下时设置悬浮窗图标变大
					params.width = 128;
					params.height = 161;
					btn_floatView
							.setBackgroundResource(R.drawable.helper_btn_l);

					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params.x;
					paramY = params.y;
					wm.updateViewLayout(btn_floatView, params);
					break;
				case MotionEvent.ACTION_MOVE:

					dx = (int) event.getRawX() - lastX;
					dy = (int) event.getRawY() - lastY;
					params.x = paramX + dx;
					params.y = paramY + dy;
					// 移动时设置悬浮窗图标变大
					params.width = 128;
					params.height = 161;
					btn_floatView
							.setBackgroundResource(R.drawable.helper_btn_s);
					wm.updateViewLayout(btn_floatView, params);
					break;
				case MotionEvent.ACTION_UP:
					// 离开时设置悬浮窗图标变小
					params.width = 56;
					params.height = 75;
					btn_floatView
							.setBackgroundResource(R.drawable.helper_btn_s);

					wm.updateViewLayout(btn_floatView, params);
					if ((Math.abs(dx) <= 5) && (Math.abs(dy) <= 5)) {
						HelperC.getInstance().OpenMainDialog();
					}

					break;
				}
				return true;
			}
		});

		wm.addView(btn_floatView, params);
		isAdded = true;
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {

		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public boolean isHome() {

		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		}
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return homeList.contains(rti.get(0).topActivity.getPackageName());
	}
}
