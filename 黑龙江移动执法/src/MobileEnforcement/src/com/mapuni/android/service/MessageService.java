package com.mapuni.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.GISLocation;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.main.MainTabActivity;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.thirdpart.SpeakUtil;
/**
 * FileName: MessageService.java Description:后台工作者服务，开机启动、登陆启动 <li>
 * 获取当前用户坐标，时间间隔由系统设置 <li>请求WebService，时间间隔由系统设置 <li>
 * @author Liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午09:40:48
 */
public class MessageService extends Service {
	/** 日志记录标签 */
	private  final String TAG = "MessageService";

	/** 广播ACTION常量值 */
	public static  final String ACTION_LOGIN = "ACTION_LOGIN";
	/** 待执行任务数量发生改变 */
	public  static final String ACTION_TASKCHANGE = "ACTION_TASKCHANGE";
	public  static final String ACTION_BOOTSYSTEM = "ACTION_BOOTSERVICE";
	/** 退出当前服务 */
	public  static final String ACTION_DISMISSNOTIFICATION = "ACTION_DISMISSNOTIFICATION";

	/** 全局变量 */
	private String userName = "yutu";
	private String realName = "环保通";
	private String userID = "";
	/** 判断该系统是否成功连接服务器 1或者0：3G成功,wifi成功 -1：失败 */
	private String result = "";

	private int mTime;// 从配置文件读取时间间隔
	private boolean isAutoSyncData = false;

	// 点击查看
	private Intent activityIntent = null;
	private PendingIntent activityPendingIntent = null;// 延时Intent

	// 通知栏消息
	private final int NOTIFICATION_ID = 1000;
	private Notification messageEnNotification = null;
	private NotificationManager messageEnNotificationManager = null;

	// GPS位置信息
	// private LocationManager mLocationManager = null;
	private double latitude = 0.0;
	private double longitude = 0.0;

	// 线程
	private Handler mHandler;
	/** 定时执行上传当前用户经纬度，和同步最新任务的计时器 */
	private Timer seheduleTimer;
	private Timer notificationTimer;// 定时器
	private Timer timingTimer;// 计时器

	/** 当前为3g网络 */
	private final int NET_3G = 0;
	/** 当前为wifi网络 */
	private final int NET_WIFI = 1;
	/** 当前为无网络 */
	private final int NO_NET = 2;
	private final String QX_GPS = "vmob5A7B";// GPS定位权限
	private boolean isGPS = false;
	private RWXX rwxx;


	
	/** 当前用户待执行任务 */
	private GISLocation gisLocation;
	private NetWorkBroadcastReceiver netWorkBroadcastReceiver;
	/** 执行同步任务和上传经纬度的task */
	private TimerTask myTimeTask;

	private Intent globalIntent;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// 获取用户配置信息
		mTime = 60 * 1000 * Integer.parseInt(String.valueOf(DisplayUitl
				.getSettingValue(this, DisplayUitl.AUTOASYNCDURATION, 10)));
		isAutoSyncData = Boolean.parseBoolean(String.valueOf(DisplayUitl
				.getSettingValue(this, DisplayUitl.SYNCDATA, false)));

		Log.i(TAG, "Get User Setting : Time Interval:" + mTime
				+ "\tisAutoSyncData:" + isAutoSyncData);
		
		initNetWorkReceiver();
		getLoginInfo();
		rwxx=new RWXX();
		
		// 3.初始化定时器：访问服务器、同步最新任务
		if (isAutoSyncData) {
			initTimerSchedule();
		}

		Looper looper = Looper.getMainLooper();
		mHandler = new Handler(looper) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case NET_3G:
					if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
						result = "1";
						initTimerSchedule();
						initNotification(result);
					} else {
						result = "-1";
						initNotification(result);
						closeTimerSchedule();
					}

					break;
				case NET_WIFI:
					if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
						initTimerSchedule();
						result = "0";
						initNotification(result);
					} else {
						result = "-1";
						initNotification(result);
						closeTimerSchedule();
					}

					break;
				case NO_NET:
					result = "-1";

					initNotification(result);
					closeTimerSchedule();
					break;

				default:
					break;

				}

				MessageNetPrompt(result);
			}
		};
		gisLocation = GISLocation.getGISLocationInstance();
		gisLocation.locationPrepare(this);
		super.onCreate();
	}

	private void MessageNetPrompt(String result) {
		if (result.equals("1")) {
			Toast.makeText(MessageService.this, "3G连接服务器成功", Toast.LENGTH_LONG)
					.show();
		} else if (result.equals("0")) {
			Toast.makeText(MessageService.this, "wifi连接服务器成功",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MessageService.this, "和服务器断开连接", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "-->onStartCommand");
		globalIntent=intent;
		// 获取用户信息
		getLoginInfo();
		// 初始化提醒框，环保通常驻状态栏
		activitySchedule(intent);
		if (intent != null) {
			String start = intent.getStringExtra("operation");
			if ("speak".equals(start)) {
				final String data = intent.getStringExtra("data");
				new Thread() {
					@Override
					public void run() {
						if (data != null && !"".equals(data))
							SpeakUtil.getInstance().synthetizeInSilence(
									MessageService.this, data);
						super.run();
					}
				}.start();
				
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Description:注册网络状态改变监听广播接收器
	 * 
	 * @author wanglg
	 * @Create at: 2013-5-22 下午4:28:07
	 */
	private void initNetWorkReceiver() {
		netWorkBroadcastReceiver = new NetWorkBroadcastReceiver();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netWorkBroadcastReceiver, filter);
	}

	/**
	 * 网络状态监听广播
	 * 
	 * @author wanglg
	 * 
	 */
	private class NetWorkBroadcastReceiver extends BroadcastReceiver {
		/** 当前网络状态 */
		private int lastType = -1;

		@Override
		public void onReceive(Context context, Intent arg1) {
			
			State wifiState = null;
			State mobileState = null;
			try
			{
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			}
			catch(Exception e)
			{
				
			}

			if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED == mobileState) {
				if (lastType != NET_3G) {
					lastType = NET_3G;
					Log.i("wang", "手机3g网络打开");
					mHandler.sendEmptyMessage(NET_3G);

				}

			} else if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				if (lastType != NO_NET) {
					lastType = NO_NET;
					mHandler.sendEmptyMessage(NO_NET);
					// 手机没有任何的网络
					Log.i("wang", "手机无网络");
					// Toast.makeText(context, "手机无网络",
					// Toast.LENGTH_SHORT).show();
				}

			} else if (wifiState != null && State.CONNECTED == wifiState) {
				// 无线网络连接成功
				if (lastType != NET_WIFI) {
					lastType = NET_WIFI;
					mHandler.sendEmptyMessage(NET_WIFI);
					Log.i("wang", "手机wifi网络打开");
					// Toast.makeText(context, "手机wifi网络打开",
					// Toast.LENGTH_SHORT).show();
				}

			}
			

		}

	}

	/**
	 * Description: 初始化调度Notification的显示
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:45:16
	 */
	private void activitySchedule(Intent intent) {// 全局变量，控制跳转的Activity
		if (intent != null) {
			String action = intent.getAction();
			if (action != null && action.equals(ACTION_TASKCHANGE)) {
				initNotification(result);
			}
		}
	
		if (timingTimer != null) {
			timingTimer.cancel();
		}
		timingTimer = new Timer();
		timingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!checkUserInfo()) {
					getUserInfo();
				}
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					initNotification("-1");
				} else {
					initNotification("");
				}

			}
		}, 5 * 60 * 1000);// 延迟5分后执行该任务，切换跳转页面
	}

	/** 获得待执行的任务条数 */
	/*public int GetTheTaskToBePerformed() {

		int taskSize = new RWXX().getTaskByUserIDandStatus(userID, "003")
				.size();
		return taskSize;

	}*/

	/**
	 * Description: 初始化提醒
	 * 
	 * @param status
	 *            状态
	 * @author Administrator Create at: 2012-12-3 上午10:44:55
	 */
	private void initNotification(String status) {
		String statusText = "";
		// 初始化提醒
		messageEnNotification = new Notification();

		// 根据状态更改显示图片
		if (!status.equals("-1")) {
			Log.i(TAG, "MessageNotification --> onLine");
			messageEnNotification.icon = R.drawable.yutu_online;
			statusText = " ：在线";
		} else {
			Log.i(TAG, "MessageNotification --> offLine");
			messageEnNotification.icon = R.drawable.yutu_offline;
			statusText = " ：离线";
		}
		if(rwxx==null){
			rwxx=new RWXX();
		}
		int taskSize = rwxx.getTaskNumByUserIDandStatus(userID, RWXX.RWZT_WATE_EXECUTION);
		String message = "";
		if (taskSize == 0) {
			message = "无待执行任务";
		} else {
			message = "待执行任务：" + taskSize + " 条";
		}

		messageEnNotification.tickerText = message;// 通知栏显示消息
		messageEnNotification.flags |= Notification.FLAG_ONGOING_EVENT; // 标注为：正在运行的
		messageEnNotification.flags |= Notification.FLAG_NO_CLEAR; // 不允许清除
		messageEnNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// 初始化Intent
		/*activityIntent = new Intent();

		
		activityIntent.setClass(getApplicationContext(), MainTabActivity.class);
		*/
		// activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//activityIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		/*activityPendingIntent = PendingIntent.getActivity(this, 0,
				activityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);*/
		activityPendingIntent = PendingIntent.getActivity(this, 0,
				globalIntent,0);

		messageEnNotification.setLatestEventInfo(MessageService.this, realName
				+ statusText,// 显示标题
				message, // 显示文本
				activityPendingIntent);
		// 修改Notification
		messageEnNotificationManager.notify(NOTIFICATION_ID,
				messageEnNotification);
	}



	/**
	 * Description:开启更新经纬度任务
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:44:23
	 */
	private void initTimerSchedule() {
		LogUtil.i("wang", "开启计时器");
		if (seheduleTimer == null) {
			seheduleTimer = new Timer();
			myTimeTask = new MyTimerTask();
		} else {
			if (myTimeTask != null) {
				myTimeTask.cancel();
			}
			myTimeTask = new MyTimerTask();
		}

		seheduleTimer.schedule(myTimeTask, 5000, mTime);// mTime

	}

	/**
	 * Description:关闭更新经纬度，和同步任务的计时器
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:44:23
	 */
	private void closeTimerSchedule() {
		LogUtil.i("wang", "关闭计时器");
		if (myTimeTask != null) {
			myTimeTask.cancel();
		}
	}

	/**
	 * 执行任务同步和发送GPS坐标的task
	 */

	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {

			if (!checkUserInfo()) {
				getUserInfo();
			}

		
			// 发送WebService请求
			askLocationUpdateWebService();
		

		}

	}

	/**
	 * Description:请求WebService服务 1.每隔十分钟请求一次 2.网络可用\不可用时立即请求一次，用来标示当前状态
	 * 3.GPS打开\关闭时立即请求一次，用来校订当前位置
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:43:05
	 */
	private void askLocationUpdateWebService() {
		new Thread() {
			@Override
			public void run() {
				String token = "";
				try {
					token = DESSecurity.encrypt("AddUserPosition");
				} catch (Exception e1) {
					Log.i(TAG, "DESSecurity AddUserPosition Error");
				}

				Location location = gisLocation
						.getLocation(MessageService.this);
				if (location == null) {
					return;
				}
				longitude = location.getLongitude();
				latitude = location.getLatitude();
				Log.i(TAG, "当前位置：经度:" + longitude + "-->纬度:" + latitude
						+ "用户的ID->" + userID + (isGPS ? "-->GPS定位" : "-->没有权限"));
				String url = Global.getGlobalInstance().getSystemurl();
				if (!Net.checkURL(url)) {
					return;
				}
				List<HashMap<String, Object>> paramsList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("userID", userID);
				param.put("lon", "" + longitude);
				param.put("lat", "" + latitude);
				param.put("token", token);
				paramsList.add(param);
				try {
					result = String.valueOf(WebServiceProvider.callWebService(
							Global.NAMESPACE, "AddUserPosition", paramsList,
							url + Global.WEBSERVICE_URL,
							WebServiceProvider.RETURN_STRING, true));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!(result.equals("0") || result.equals("1"))) {
					//result = "-1";
				}

			};
		}.start();
	}

	/**
	 * 移除位置监听器
	 */
	@Override
	public void onDestroy() {
		Log.i(TAG, "服务停止：onDestroy");
		if (messageEnNotificationManager != null) {
			messageEnNotificationManager.cancelAll();
			messageEnNotificationManager = null;
		}

		// 销毁计时器
		if (seheduleTimer != null) {
			seheduleTimer.cancel();
			seheduleTimer = null;
		}
		if (notificationTimer != null) {
			notificationTimer.cancel();
			notificationTimer = null;
		}
		// 回收
		unregisterReceiver(netWorkBroadcastReceiver);// 暂停服务后取消广播注册

		super.onDestroy();
	}

	/**
	 * Description:获取登录账户信息
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:30:19
	 */
	private void getLoginInfo() {
		userName = Global.getGlobalInstance().getUsername();
		userID = Global.getGlobalInstance().getUserid();
		realName = getUserRealname(userID);
		isGPS = DisplayUitl.getAuthority(QX_GPS);
		
	}

	/**
	 * Description: 根据用户编号获取用户真实姓名
	 * 
	 * @param userID
	 * @return 真实姓名
	 * @author Administrator Create at: 2012-12-3 上午10:26:47
	 */
	public String getUserRealname(String userID) {
		String realName = userName;
		String sql = "select U_RealName as name from PC_Users where userid = '"
				+ userID + "'";
		
			ArrayList<HashMap<String, Object>> data = SQLiteDataProvider
					.getInstance().queryBySqlReturnArrayListHashMap(sql);
			if (data.size() > 0) {
				realName = (String) data.get(0).get("name");
			}
		
		return realName;
	}

	/**
	 * Description:检测用户信息是否被回收 <li>防止意外崩溃
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:25:08
	 */
	private boolean checkUserInfo() {
		if (userID == null || userName == null || userID.equals("")
				|| userName.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Description:获取用户信息 <li>从SharedPreference中获取
	 * 
	 * @author Administrator Create at: 2012-12-3 上午10:25:08
	 */
	private void getUserInfo() {
	/*	SharedPreferences sp = getSharedPreferences("ServiceInfo",
				MODE_WORLD_WRITEABLE);
		userID = sp.getString("userId", "");
		userName = sp.getString("userName", "");
		isGPS = sp.getBoolean("isGPS", false);*/
		
		String loginName=DisplayUitl.readPreferences(MessageService.this, "lastuser", "user");
		String password=DisplayUitl.readPreferences(MessageService.this, "lastuser", "pass");
		Global.getGlobalInstance().initGlobalData(loginName, password);
		userID=Global.getGlobalInstance().getUserid();
		userName=Global.getGlobalInstance().getUsername();
		isGPS=DisplayUitl.getAuthority(QX_GPS);
	}



	



	

}
