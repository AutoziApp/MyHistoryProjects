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
 * FileName: MessageService.java Description:��̨�����߷��񣬿�����������½���� <li>
 * ��ȡ��ǰ�û����꣬ʱ������ϵͳ���� <li>����WebService��ʱ������ϵͳ���� <li>
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����09:40:48
 */
public class MessageService extends Service {
	/** ��־��¼��ǩ */
	private  final String TAG = "MessageService";

	/** �㲥ACTION����ֵ */
	public static  final String ACTION_LOGIN = "ACTION_LOGIN";
	/** ��ִ���������������ı� */
	public  static final String ACTION_TASKCHANGE = "ACTION_TASKCHANGE";
	public  static final String ACTION_BOOTSYSTEM = "ACTION_BOOTSERVICE";
	/** �˳���ǰ���� */
	public  static final String ACTION_DISMISSNOTIFICATION = "ACTION_DISMISSNOTIFICATION";

	/** ȫ�ֱ��� */
	private String userName = "yutu";
	private String realName = "����ͨ";
	private String userID = "";
	/** �жϸ�ϵͳ�Ƿ�ɹ����ӷ����� 1����0��3G�ɹ�,wifi�ɹ� -1��ʧ�� */
	private String result = "";

	private int mTime;// �������ļ���ȡʱ����
	private boolean isAutoSyncData = false;

	// ����鿴
	private Intent activityIntent = null;
	private PendingIntent activityPendingIntent = null;// ��ʱIntent

	// ֪ͨ����Ϣ
	private final int NOTIFICATION_ID = 1000;
	private Notification messageEnNotification = null;
	private NotificationManager messageEnNotificationManager = null;

	// GPSλ����Ϣ
	// private LocationManager mLocationManager = null;
	private double latitude = 0.0;
	private double longitude = 0.0;

	// �߳�
	private Handler mHandler;
	/** ��ʱִ���ϴ���ǰ�û���γ�ȣ���ͬ����������ļ�ʱ�� */
	private Timer seheduleTimer;
	private Timer notificationTimer;// ��ʱ��
	private Timer timingTimer;// ��ʱ��

	/** ��ǰΪ3g���� */
	private final int NET_3G = 0;
	/** ��ǰΪwifi���� */
	private final int NET_WIFI = 1;
	/** ��ǰΪ������ */
	private final int NO_NET = 2;
	private final String QX_GPS = "vmob5A7B";// GPS��λȨ��
	private boolean isGPS = false;
	private RWXX rwxx;


	
	/** ��ǰ�û���ִ������ */
	private GISLocation gisLocation;
	private NetWorkBroadcastReceiver netWorkBroadcastReceiver;
	/** ִ��ͬ��������ϴ���γ�ȵ�task */
	private TimerTask myTimeTask;

	private Intent globalIntent;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// ��ȡ�û�������Ϣ
		mTime = 60 * 1000 * Integer.parseInt(String.valueOf(DisplayUitl
				.getSettingValue(this, DisplayUitl.AUTOASYNCDURATION, 10)));
		isAutoSyncData = Boolean.parseBoolean(String.valueOf(DisplayUitl
				.getSettingValue(this, DisplayUitl.SYNCDATA, false)));

		Log.i(TAG, "Get User Setting : Time Interval:" + mTime
				+ "\tisAutoSyncData:" + isAutoSyncData);
		
		initNetWorkReceiver();
		getLoginInfo();
		rwxx=new RWXX();
		
		// 3.��ʼ����ʱ�������ʷ�������ͬ����������
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
			Toast.makeText(MessageService.this, "3G���ӷ������ɹ�", Toast.LENGTH_LONG)
					.show();
		} else if (result.equals("0")) {
			Toast.makeText(MessageService.this, "wifi���ӷ������ɹ�",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MessageService.this, "�ͷ������Ͽ�����", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "-->onStartCommand");
		globalIntent=intent;
		// ��ȡ�û���Ϣ
		getLoginInfo();
		// ��ʼ�����ѿ򣬻���ͨ��פ״̬��
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
	 * Description:ע������״̬�ı�����㲥������
	 * 
	 * @author wanglg
	 * @Create at: 2013-5-22 ����4:28:07
	 */
	private void initNetWorkReceiver() {
		netWorkBroadcastReceiver = new NetWorkBroadcastReceiver();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netWorkBroadcastReceiver, filter);
	}

	/**
	 * ����״̬�����㲥
	 * 
	 * @author wanglg
	 * 
	 */
	private class NetWorkBroadcastReceiver extends BroadcastReceiver {
		/** ��ǰ����״̬ */
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
					Log.i("wang", "�ֻ�3g�����");
					mHandler.sendEmptyMessage(NET_3G);

				}

			} else if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				if (lastType != NO_NET) {
					lastType = NO_NET;
					mHandler.sendEmptyMessage(NO_NET);
					// �ֻ�û���κε�����
					Log.i("wang", "�ֻ�������");
					// Toast.makeText(context, "�ֻ�������",
					// Toast.LENGTH_SHORT).show();
				}

			} else if (wifiState != null && State.CONNECTED == wifiState) {
				// �����������ӳɹ�
				if (lastType != NET_WIFI) {
					lastType = NET_WIFI;
					mHandler.sendEmptyMessage(NET_WIFI);
					Log.i("wang", "�ֻ�wifi�����");
					// Toast.makeText(context, "�ֻ�wifi�����",
					// Toast.LENGTH_SHORT).show();
				}

			}
			

		}

	}

	/**
	 * Description: ��ʼ������Notification����ʾ
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:45:16
	 */
	private void activitySchedule(Intent intent) {// ȫ�ֱ�����������ת��Activity
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
		}, 5 * 60 * 1000);// �ӳ�5�ֺ�ִ�и������л���תҳ��
	}

	/** ��ô�ִ�е��������� */
	/*public int GetTheTaskToBePerformed() {

		int taskSize = new RWXX().getTaskByUserIDandStatus(userID, "003")
				.size();
		return taskSize;

	}*/

	/**
	 * Description: ��ʼ������
	 * 
	 * @param status
	 *            ״̬
	 * @author Administrator Create at: 2012-12-3 ����10:44:55
	 */
	private void initNotification(String status) {
		String statusText = "";
		// ��ʼ������
		messageEnNotification = new Notification();

		// ����״̬������ʾͼƬ
		if (!status.equals("-1")) {
			Log.i(TAG, "MessageNotification --> onLine");
			messageEnNotification.icon = R.drawable.yutu_online;
			statusText = " ������";
		} else {
			Log.i(TAG, "MessageNotification --> offLine");
			messageEnNotification.icon = R.drawable.yutu_offline;
			statusText = " ������";
		}
		if(rwxx==null){
			rwxx=new RWXX();
		}
		int taskSize = rwxx.getTaskNumByUserIDandStatus(userID, RWXX.RWZT_WATE_EXECUTION);
		String message = "";
		if (taskSize == 0) {
			message = "�޴�ִ������";
		} else {
			message = "��ִ������" + taskSize + " ��";
		}

		messageEnNotification.tickerText = message;// ֪ͨ����ʾ��Ϣ
		messageEnNotification.flags |= Notification.FLAG_ONGOING_EVENT; // ��עΪ���������е�
		messageEnNotification.flags |= Notification.FLAG_NO_CLEAR; // ���������
		messageEnNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// ��ʼ��Intent
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
				+ statusText,// ��ʾ����
				message, // ��ʾ�ı�
				activityPendingIntent);
		// �޸�Notification
		messageEnNotificationManager.notify(NOTIFICATION_ID,
				messageEnNotification);
	}



	/**
	 * Description:�������¾�γ������
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:44:23
	 */
	private void initTimerSchedule() {
		LogUtil.i("wang", "������ʱ��");
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
	 * Description:�رո��¾�γ�ȣ���ͬ������ļ�ʱ��
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:44:23
	 */
	private void closeTimerSchedule() {
		LogUtil.i("wang", "�رռ�ʱ��");
		if (myTimeTask != null) {
			myTimeTask.cancel();
		}
	}

	/**
	 * ִ������ͬ���ͷ���GPS�����task
	 */

	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {

			if (!checkUserInfo()) {
				getUserInfo();
			}

		
			// ����WebService����
			askLocationUpdateWebService();
		

		}

	}

	/**
	 * Description:����WebService���� 1.ÿ��ʮ��������һ�� 2.�������\������ʱ��������һ�Σ�������ʾ��ǰ״̬
	 * 3.GPS��\�ر�ʱ��������һ�Σ�����У����ǰλ��
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:43:05
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
				Log.i(TAG, "��ǰλ�ã�����:" + longitude + "-->γ��:" + latitude
						+ "�û���ID->" + userID + (isGPS ? "-->GPS��λ" : "-->û��Ȩ��"));
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
	 * �Ƴ�λ�ü�����
	 */
	@Override
	public void onDestroy() {
		Log.i(TAG, "����ֹͣ��onDestroy");
		if (messageEnNotificationManager != null) {
			messageEnNotificationManager.cancelAll();
			messageEnNotificationManager = null;
		}

		// ���ټ�ʱ��
		if (seheduleTimer != null) {
			seheduleTimer.cancel();
			seheduleTimer = null;
		}
		if (notificationTimer != null) {
			notificationTimer.cancel();
			notificationTimer = null;
		}
		// ����
		unregisterReceiver(netWorkBroadcastReceiver);// ��ͣ�����ȡ���㲥ע��

		super.onDestroy();
	}

	/**
	 * Description:��ȡ��¼�˻���Ϣ
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:30:19
	 */
	private void getLoginInfo() {
		userName = Global.getGlobalInstance().getUsername();
		userID = Global.getGlobalInstance().getUserid();
		realName = getUserRealname(userID);
		isGPS = DisplayUitl.getAuthority(QX_GPS);
		
	}

	/**
	 * Description: �����û���Ż�ȡ�û���ʵ����
	 * 
	 * @param userID
	 * @return ��ʵ����
	 * @author Administrator Create at: 2012-12-3 ����10:26:47
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
	 * Description:����û���Ϣ�Ƿ񱻻��� <li>��ֹ�������
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:25:08
	 */
	private boolean checkUserInfo() {
		if (userID == null || userName == null || userID.equals("")
				|| userName.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Description:��ȡ�û���Ϣ <li>��SharedPreference�л�ȡ
	 * 
	 * @author Administrator Create at: 2012-12-3 ����10:25:08
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
