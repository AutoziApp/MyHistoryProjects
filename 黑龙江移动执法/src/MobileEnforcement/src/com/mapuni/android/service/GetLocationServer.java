package com.mapuni.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mapuni.android.base.Global;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GetLocationServer extends Service{
	private LocationManager locationManager;
	private double latitude=0.0;  
	private double longitude =0.0;  
	private Location location=null;
	Timer timer ;
	//用户id
	private String userId=Global.getGlobalInstance().getUserid();
	//后台连接地址
	private String url = Global.getGlobalInstance().getSystemurl();


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public void onCreate(){
		super.onCreate();
		timer=new Timer();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); 


	}
	/** service被启动时回调的方法 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		/*try{
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);   
			else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);   
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		SynchronousData();// 每个10分钟获取用户位置信息并保存
		return super.onStartCommand(intent, flags, startId);
	}
	LocationListener locationListener = new LocationListener()   
	{   

		@Override  
		public void onLocationChanged(Location location)   
		{   
			if(location!=null){
				latitude=location.getLatitude();
				longitude=location.getLongitude();
			}else{
				latitude=0.0;
				longitude=0.0;
				//return;
			}
			//得到经纬度才发送
			//	if(latitude!=0.0 &&longitude!=0.0){
			// 发送WebService请求
			//askLocationUpdateWebService(latitude,longitude);
			String token = "";
			String result="";
			try {
				token = DESSecurity.encrypt("AddUserPosition");
			} catch (Exception e1) {
				e1.printStackTrace();
			}



			List<HashMap<String, Object>> paramsList = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userID", userId);
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
		}   

		@Override  
		public void onProviderDisabled(String provider)   
		{   

		}   

		@Override  
		public void onProviderEnabled(String provider)   
		{   

		}   

		@Override  
		public void onStatusChanged(String provider, int status, Bundle extras)   
		{   

		}


	};   



	/** 每个10分钟同步数据的方法 */
	public void SynchronousData() {
		if(timer==null){
			timer=new Timer(true);
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (!Net.checkURL(url)) {
							return;
						}
						for(int i=0;i<100;i++){
							if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
								location  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
							}else if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)){
								location  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
							}
							if(location!=null)
								return;
						}
						if(location!=null){
							latitude=location.getLatitude();
							longitude=location.getLongitude();
						}else{
							latitude=0.0;
							longitude=0.0;
							return;
						}
						//得到经纬度才发送
						//	if(latitude!=0.0 &&longitude!=0.0){
						// 发送WebService请求
						//askLocationUpdateWebService(latitude,longitude);
						String token = "";
						String result="";
						try {
							token = DESSecurity.encrypt("AddUserPosition");
						} catch (Exception e1) {
							e1.printStackTrace();
						}



						List<HashMap<String, Object>> paramsList = new ArrayList<HashMap<String, Object>>();
						HashMap<String, Object> param = new HashMap<String, Object>();
						param.put("userID", userId);
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

						//}

					}
				}).start();

			}
		}, 1 * 60 * 1000, 10 * 60 * 1000);

	}
	private void askLocationUpdateWebService(Double lat,Double lon) {
		String token = "";
		String result="";
		try {
			token = DESSecurity.encrypt("AddUserPosition");
		} catch (Exception e1) {
			e1.printStackTrace();
		}



		List<HashMap<String, Object>> paramsList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userID", userId);
		param.put("lon", "" + lon);
		param.put("lat", "" + lat);
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
		/*if (!(result.equals("0") || result.equals("1"))) {
					//result = "-1";
				}*/

	}
	/**
	 * 移除位置监听器
	 */
	@Override
	public void onDestroy() {

		super.onDestroy();
	}




}
