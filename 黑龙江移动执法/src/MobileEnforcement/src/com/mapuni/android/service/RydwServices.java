package com.mapuni.android.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.greenrobot.eventbus.EventBus;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.Gps;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.base.util.PositionUtil;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class RydwServices extends Service {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	private BDLocation location;
	
	private rydwThread rThread;
	private String url;

	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	
	private  Handler   mHandler =new Handler();
	
	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				url = DisplayUitl.getAppInfoDataToPreference(RydwServices.this, "url", "");
				
			//	if (panduanDate()) {
					getCurrentLocation();
					
					rThread.getHandler().sendEmptyMessage(0);
			//	}
			}
		};

		if (mTimer != null && mTimerTask != null) {
			try {
				mTimer.schedule(mTimerTask, 0, 60 *60* 10000);
			} catch (Exception e) {
				LogUtil.v("lfwang", "mTimerTask=====================e");
			}
		}

	}
	
	public boolean panduanDate() {// 8:30--12:00 14:30---18:00
		SimpleDateFormat formatters = new SimpleDateFormat("HH:mm");
		Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
		String strs = formatters.format(curDates);

		String[] dds = strs.split(":");
		int h = Integer.parseInt(dds[0]);
		int m = Integer.parseInt(dds[1]);

		boolean boo = false;

//		if (h >= 8 && h < 12) {
//			if (h >= 9 || m >= 30) {
//				boo = true;
//			}
//		} else if (h >= 14 && h < 18) {
//			if (h >= 15 || m >= 30) {
//				boo = true;
//			}
//		}
		if (h >= 8 && h <= 22) {
			boo = true;
		}
		return true;
	}

	private void stopTimer() {

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(RydwServices.this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		rThread = new rydwThread();
		rThread.start();
		startTimer();
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopTimer();
	
		super.onDestroy();
	}
	
	private class rydwThread extends Thread {
		public Handler handler;
		private final CountDownLatch handlerInitLatch;
		
		public rydwThread() {
			super();
			handlerInitLatch = new CountDownLatch(1);
		}
		
		Handler getHandler() {
			try {
				handlerInitLatch.await();
			} catch (InterruptedException ie) {
				// continue?
			}
			return handler;
		}

		@Override
		public void run() {
			super.run();
			Looper.prepare();
			handler = new Handler(){
				@Override
				public void handleMessage(android.os.Message msg) {
					super.handleMessage(msg);
					String useridString = DisplayUitl.getAppInfoDataToPreference(RydwServices.this, "userid", "");
					//String regioncode = DisplayUitl.getAppInfoDataToPreference(RydwServices.this, "regioncode", "");
					if (location != null && location.getLongitude() != 1 && location.getLatitude() != 1 && useridString != null
							&& !useridString.equals("")) {
						Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
						double jdString = gps.getWgLon();
						double wdString = gps.getWgLat();
						
//						double jdString = location.getLongitude();
//						double wdString = location.getLatitude();
						SimpleDateFormat timefmtDate = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String dwsjString = timefmtDate.format(curDate);
						Log.v("lfwang", useridString + "--------" + jdString + "========" + wdString);
						//缓存到数据库
						String sql = "insert into rydw(userid,jingdu,weidu,dwdate) values('"
								+ useridString + "','"
								+ jdString + "','"
								+ wdString + "','"
								+ dwsjString + "')";
						SQLiteDataProvider.getInstance().ExecSQL(sql);
						
						if (!Net.checkURL(url)) {
							return;
						}
						
						String result = "0";
						String methodName = "MobileInsertPositionTrajectory";

						ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> param = new HashMap<String, Object>();

						//取出缓存数据
						int max = 0;
						ArrayList<HashMap<String, Object>> list = SQLiteDataProvider
								.getInstance().queryBySqlReturnArrayListHashMap("select * from rydw limit 0,20");
						if (list.size() > 0) {
							max = Integer.valueOf(list.get(list.size() - 1).get("id").toString());
						}
						
						ArrayList<HashMap<String, Object>> code = SQLiteDataProvider
								.getInstance().queryBySqlReturnArrayListHashMap("select * from PC_Users where  userid='"+useridString+"'");
						
						param.put("UserCode",useridString);
						param.put("Longitude", jdString);
						param.put("Latitude", wdString);
						param.put("GridCode", code.get(0).get("regioncode"));
						param.put("Address", "");
						param.put("Remark", "");
							
						String hashMapToJson = hashMapToJson(param);
						ArrayList<HashMap<String, Object>> params2 = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> param2 = new HashMap<String, Object>();
						param2.put("positionTrajectoryJson", hashMapToJson);
						params2.add(param2);
						try {
							result = (String) WebServiceProvider
									.callWebService(Global.NAMESPACE,
											methodName, params2, url
													+ Global.WEBSERVICE_URL,
											WebServiceProvider.RETURN_STRING,
											true);
							if (result == null||"".equals(result)) {
								result = "0";
							} else if("1".equals(result)) {//提交成功 删除缓存数据
								boolean execSQL = SQLiteDataProvider.getInstance().ExecSQL(
										"delete from rydw where id >" + max);
							}
							
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									RydwServices.this.stopSelf();
								}
							});
						
							
						//	EventBus.getDefault().post(new FlagEvent(1));
						} catch (IOException e) {
							e.printStackTrace();
							RydwServices.this.stopSelf();
						//	EventBus.getDefault().post(new FlagEvent(1));
						}

					}else{
						
						OtherTools.showToast(RydwServices.this, "获取经纬度失败");
					}
					
				}
			};
//			handlerInitLatch.countDown();
//			Looper.loop();
		}
	}
	
	
    public  String hashMapToJson(HashMap map) {  
        String string = "{";  
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
            Entry e = (Entry) it.next();  
            string += "'" + e.getKey() + "':";  
            string += "'" + e.getValue() + "',";  
        }  
        string = string.substring(0, string.lastIndexOf(","));  
        string += "}";  
        return string;  
    }  

	public void getCurrentLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(false);//通过空间坐标请求街道地址
		mLocationClient.setLocOption(option);
		
		this.location = null;
		mLocationClient.start();
		for (int i = 0; i < 5; i++) {
			if(location != null) {
				break;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mLocationClient.stop();
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			int result = location.getLocType();
			if(result == 61 || result == 65 || result == 66 || result == 67 || result == 68 || result == 161) {
				RydwServices.this.location = location;
			}
			
			Log.v("lfwang", "onReceiveLocation=========" + result);
		}
	}
	
}
