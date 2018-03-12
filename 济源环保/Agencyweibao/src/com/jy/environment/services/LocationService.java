package com.jy.environment.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jy.environment.activity.EnvironmentCityManagerActivity;
import com.jy.environment.activity.EnvironmentCurrentWeatherPullActivity;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.City;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;

public class LocationService extends Service {
	private LocationReceiver locationReceiver;
	private int x = 1;
	public static final String LOCATIONRECEIVERACTION = "com.mapuni.weibao.getNewLocation";
	public static final String LOCATION_CHANGE_ACTION = "com.mapuni.weibao.LocationChange";
	private String TAG = "LocationService";
	public SharedPreferences sharedPref;// 缓存
	private SharedPreferences.Editor editor;
	private SharedPreferencesUtil sh;
	private LocationClient mLocationClient;
	private CityDB mCityDB;
	private WeiBaoApplication mApplication;
	private String cityName;
	private String xiangxidi;
	private String province;
	private double longitude;
	private double latitude;
	private int m = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initLocationData();
		registerMessageReceiver();
		Log.i(TAG, "location onCreate");
	}

	/**
	 * 启动一次定位
	 */
	private void initLocationData() {
		try {
			mApplication = WeiBaoApplication.getInstance();
			mCityDB = mApplication.getCityDB();
			sh = SharedPreferencesUtil.getInstance(LocationService.this);
			WeiBaoApplication.getInstance().initData(new Handler());
			mLocationClient = mApplication.getLocationClient();
			addLisenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(locationReceiver);
			mLocationClient.stop();
			MyLog.i("location onDestroy");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addLisenter() {
		MyLog.i(">>>>>>>>>listen" + "1");
		mLocationClient.registerLocationListener(mLocationListener);
		mLocationClient.start();
	}

	BDLocationListener mLocationListener = new BDLocationListener() {
		boolean show = true;

		// @Override
		// public void onReceivePoi(BDLocation arg0) {
		// // do nothing
		// }

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLog.i(">>>>>>>>>>>dingwei" + (m++));
			if (NetUtil.getNetworkState(LocationService.this) == NetUtil.NETWORN_NONE) {
				if (mLocationClient.isStarted()) {
					mLocationClient.stop();
				}

			}
			MyLog.i("onReceiveLocation" + location);
			try {
				sh = SharedPreferencesUtil.getInstance(getApplicationContext());
				if (location == null || TextUtils.isEmpty(location.getCity())) {
					MyLog.i("定位失败");
					return;
				}
				cityName = location.getCity();
				xiangxidi = location.getAddrStr();
				province = location.getProvince();
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				String district = location.getDistrict();
				if (cityName.endsWith("自治州")) {
					cityName = district;
				}
				if (cityName.contains("市")) {
					cityName = cityName.substring(0, cityName.length() - 1);
				}
				if (cityName.contains("地区")) {
					cityName = cityName.substring(0, cityName.length() - 2);
				}

				// MyLog.i("New定位x" + x);
				// MyLog.i("New定位x" + (x%3==0));
				// if (x%3==0) {
				// cityName = "郑州市";
				// xiangxidi = "郑州西三环";
				// province = "河南";
				// latitude = 39.9;
				// longitude = 116.4;
				// show = !show;
				// } else {
				//
				// show = !show;
				// }
				// x++;
				MyLog.i("onReceiveLocation xiangxidi :" + xiangxidi);
				if (null == cityName || null == xiangxidi || null == province) {
					MyLog.i("定位失败");
					return;
				} else {
					MyLog.i("定位成功");
					Intent intent = new Intent();
					intent.setAction(EnvironmentCurrentWeatherPullActivity.DING_WEIService);
					intent.putExtra("dingwei", location.getAddrStr());
					intent.putExtra("city", cityName);
					sendBroadcast(intent);
					if (null != mLocationClient && mLocationClient.isStarted()) {
						mLocationClient.stop();
					}
				}
				if (cityName.contains("市")) {
					cityName = cityName.substring(0, cityName.length() - 1);
				}
				if (cityName.contains("地区")) {
					cityName = cityName.substring(0, cityName.length() - 2);
				}
				if (cityName.equals("黔西南布依族苗族自治州")) {
					cityName = "兴义";
				}
				if (null != mApplication.getXiangxidizhi()
						&& !"".equals(mApplication.getXiangxidizhi())
						&& !mApplication.getXiangxidizhi().equals(xiangxidi)) {
					// 在定位城市无变化的情况下，可能会出现位置改变的情况，此时将获取到的经纬度和详细地址存入缓存，方便及时更新定位点
					mApplication.setCurrentCityLatitude(latitude + "");
					mApplication.setCurrentCityLongitude(longitude + "");
					mApplication.setXiangxidizhi(xiangxidi);
					sharedPref = getSharedPreferences("sharedPref",
							MODE_PRIVATE);
					editor = sharedPref.edit();
					editor.putString("CurrentCityLongitude", longitude + "");
					editor.putString("CurrentCityLatitude", latitude + "");
					editor.putString("xiangxidi", xiangxidi);
					editor.commit();
				}
				// // 定位成功后发送广播
				// if (xiangxidi != null && xiangxidi != "") {
				// Intent intent = new Intent();
				// intent.setAction(LOCATION_CHANGE_ACTION);
				// sendBroadcast(intent);
				// }
				if (null != mApplication.getDingweicity()
						&& !"".equals(mApplication.getDingweicity())
						&& mApplication.getDingweicity().equals(cityName)) {
					MyLog.i("定位城市无变化");
					return;
				}
				// 把定位城市存入到缓存里
				mApplication.setDingweicity(cityName);
				mApplication.setCurrentCityLatitude(latitude + "");
				mApplication.setCurrentCityLongitude(longitude + "");
				sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
				editor = sharedPref.edit();
				editor.putString("dingweiCity", cityName);
				editor.putString("CurrentCityLongitude", longitude + "");
				editor.putString("CurrentCityLatitude", latitude + "");
				editor.putString("province", province);
				editor.putString("xiangxidi", xiangxidi);
				editor.commit();
				mApplication.setXiangxidizhi(xiangxidi);
				mApplication.setProvince(province);
				if (!"".equals(cityName)) {
					WeiBaoApplication.addJPushAliasAndTags(
							getApplicationContext(), false, cityName);
				}
				// mLocationClient.stop();
				//

				LoadLocationTask loadLocationTask = new LoadLocationTask();
				loadLocationTask.execute(cityName);
				// // 定位成功后发送广播
				// if (xiangxidi != null && xiangxidi != "") {
				// Intent intent = new Intent();
				// intent.setAction(EnvironmentCurrentWeatherActivity.DING_WEI);
				// intent.putExtra("dingwei", xiangxidi);
				// intent.putExtra("city", cityName);
				// sendBroadcast(intent);
				// }
				// City curCity = mCityDB.getCity(cityName);// 从数据库中找到该城市
				// MyLog.i("curCity :" + curCity);
				// if (curCity != null) {
				// MyLog.i("1 :"
				// + (mCityDB.addCityExist(cityName) && mCityDB
				// .addCityExistAndLocation(cityName)));
				// if (mCityDB.addCityExist(cityName)
				// && mCityDB.addCityExistAndLocation(cityName)) {
				// return;
				// }
				// MyLog.i("mCityDB.isHaveLocation() :"
				// + (mCityDB.isHaveLocation()));
				// if (mCityDB.isHaveLocation()) {
				// mCityDB.deleteLocationCity();
				// Intent intent = new Intent();
				// intent.setAction(EnvironmentCityManagerActivity.LOCATION_CHANGE);
				// getApplicationContext().sendBroadcast(intent);
				// }
				// if (mCityDB.addCityExist(cityName)) {
				// mCityDB.updateLocation(cityName);
				// MyLog.i("mCityDB.updateLocation() :" + cityName);
				// } else {
				// mCityDB.addXuanZhecity1(cityName, "", "", true);
				// MyLog.i("mCityDB.addXuanZhecity1() :" + cityName);
				// }
				// MyLog.i("dingweicity:" + mApplication.getDingweicity());
				// } else {// 如果定位到的城市数据库中没有，也弹出定位失败
				// // Toast.makeText(LocationService.this, "请手动选择城市",
				// // 1).show();
				// return;
				// }

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	class LoadLocationTask extends AsyncTask<String, Void, String> {
		boolean isLocationChange = false;

		@Override
		protected String doInBackground(String... params) {
			City curCity = mCityDB.getCity(cityName);// 从数据库中找到该城市
			MyLog.i("curCity :" + curCity);
			if (curCity != null) {
				MyLog.i("1 :"
						+ (mCityDB.addCityExist(cityName) && mCityDB
								.addCityExistAndLocation(cityName)));
				if (mCityDB.addCityExist(cityName)
						&& mCityDB.addCityExistAndLocation(cityName)) {
					return null;
				}
				MyLog.i("mCityDB.isHaveLocation() :"
						+ (mCityDB.isHaveLocation()));
				if (mCityDB.isHaveLocation()) {
					mCityDB.deleteLocationCity();
					isLocationChange = true;
				}
				if (mCityDB.addCityExist(cityName)) {
					mCityDB.updateLocation(cityName);
					MyLog.i("mCityDB.updateLocation() :" + cityName);
					return cityName;
				} else {
					mCityDB.addXuanZhecity1(cityName, "", "", true);
					MyLog.i("mCityDB.addXuanZhecity1() :" + cityName);
					return cityName;
				}
			} else {// 如果定位到的城市数据库中没有，也弹出定位失败
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isLocationChange) {
				Intent intent = new Intent();
				intent.setAction(EnvironmentCityManagerActivity.LOCATION_CHANGE);
				getApplicationContext().sendBroadcast(intent);

				CommonUtil.sendLocationCityChangeBoradcast(
						LocationService.this, cityName);
			}
			// 定位成功后发送广播
			if (xiangxidi != null && xiangxidi != "" && null != result) {
				Intent intent = new Intent();
				intent.setAction(EnvironmentCurrentWeatherPullActivity.DING_WEI);
				intent.putExtra("dingwei", xiangxidi);
				intent.putExtra("city", cityName);
				sendBroadcast(intent);
			}
		}
	}

	/**
	 * 定位变化通知
	 * 
	 * @author baiyuchuan
	 * 
	 */
	public class LocationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			MyLog.i("intent.getAction() :" + intent.getAction());
			if (null != intent && null != intent.getAction()
					&& intent.getAction().equals(LOCATIONRECEIVERACTION)) {
				try {
					initLocationData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void registerMessageReceiver() {
		locationReceiver = new LocationReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(LOCATIONRECEIVERACTION);
		registerReceiver(locationReceiver, filter);
	}

	/**
	 * 发送定位广播
	 * 
	 * @param context
	 */
	public static void sendGetLocationBroadcast(Context context) {
		MyLog.i("sendGetLocationBroadcast");
		Intent intent = new Intent();
		intent.setAction(LOCATIONRECEIVERACTION);
		context.sendBroadcast(intent);
	}
}
