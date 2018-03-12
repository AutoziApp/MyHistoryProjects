package com.jy.environment.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.services.NewsPushService;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 启动页
 * 
 * @author baiyuchuan
 * 
 */
public class LoadingActivity extends ActivityBase {
	private static final int SHOW_TIME_MIN = 3000;// 最小显示时间
	private long mStartTime;// 开始时间
	private WeiBaoApplication mApplication;
	private LocationClient mLocationClient;
	SharedPreferencesUtil sh;
	public SharedPreferences sharedPref;// 缓存
	SharedPreferences.Editor editor;
	private RelativeLayout loading;
	private CityDB mCityDB;
	private String userID;
	boolean dingwei = false;
	ImageView load_car, load_jiantou, load_sun, load_logo;
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private int textsize;
	private boolean isbreak = false;
	private Intent intent;
	private String[] cityArrays = new String[] {
			// "郑州", "开封", "洛阳", "平顶山", "安阳",
			// "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳",
			// "周口",
			// "驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县",
			// "鹿邑县", "新蔡县"
			"焦作", "修武县", "博爱县", "武陟县", "温县", "泌阳县", "孟州" };
	private KjhttpUtils http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.loadingnew);
		// setContentView(R.layout.loading_360);
		setContentView(R.layout.loadingnew);
		userID = WeiBaoApplication.getUserId();
		if (null == userID || userID.equals("")) {
			userID = "0";
		}
		File cacheDir = new File(getFilesDir() + "/weibao/cache");
		http = new KjhttpUtils(this, cacheDir);
		// for (int i = 0; i < 28; i++) {
		for (int i = 0; i < 7; i++) {
			String initcitysuUrl;
			initcitysuUrl = UrlComponent.currentWeatherGet(cityArrays[i], "0",
					"0");
			http.getString(initcitysuUrl, 600, new DownGet() {

				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
				}
			});
		}
		mApplication = WeiBaoApplication.getInstance();
		mCityDB = mApplication.getCityDB();
		mStartTime = System.currentTimeMillis();// 记录开始时间，
		sh = SharedPreferencesUtil.getInstance(LoadingActivity.this);
		WeiBaoApplication.getInstance().initData(mHandler);
		mLocationClient = mApplication.getLocationClient();
		mLocationClient.registerLocationListener(mLocationListener);
		initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
		if (initcitys.size() == 0) {
			HashMap<String, Object> cityhaHashMap = new HashMap<String, Object>();
			cityhaHashMap.put("name", "焦作");
			cityhaHashMap.put("province", "河南");
			cityhaHashMap.put("number", "101181101");
			cityhaHashMap.put("pinyin", "jiaozuo");
			cityhaHashMap.put("py", "jzs");
			cityhaHashMap.put("lat", "35.22");
			cityhaHashMap.put("lon", "113.25");
			cityhaHashMap.put("islocation", "1");
			initcitys.add(cityhaHashMap);
		}
		MyLog.i(">>>>>>>>initcitys" + initcitys.size());
		// LocationService.sendGetLocationBroadcast(LoadingActivity.this);
		Timer timer = new Timer();
		TimerTask task1 = new TimerTask() {

			@Override
			public void run() {
				if (sh.IsFirstUse()) {
					intent = new Intent(LoadingActivity.this,
							WelcomePagerActivity.class);
					intent.putExtra("bangzhu", "diyici");
					sh.setIsFirstUse(false);
					startActivity(intent);
					finish();
				}
				// TODO Auto-generated method stub
				else if (initcitys.size() == 0) {
					intent = new Intent(LoadingActivity.this,
							EnvironmentSelectCtiyActivity.class);
					intent.putExtra("from", "lo");
					intent.putExtra("load", "loading");
					startActivityForResult(intent, 100);
					overridePendingTransition(R.anim.addcity_activity_enter,
							R.anim.addcity_activity_enter);
				} else {
					intent = new Intent(LoadingActivity.this,
							EnvironmentMainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
				}
			}
		};
		timer.schedule(task1, 2000);

	}

	BDLocationListener mLocationListener = new BDLocationListener() {

		// @Override
		// public void onReceivePoi(BDLocation arg0) {
		// }

		@Override
		public void onReceiveLocation(BDLocation location) {
			// double longitude;
			// double latitude;
			// Intent intent;
			// MyLog.i("onReceiveLocation1" + location);
			// if (location == null || TextUtils.isEmpty(location.getCity())) {
			// MyLog.i("onReceiveLocation2" + location);
			// // if (initcitys.size() == 0) {
			// // Toast.makeText(LoadingActivity.this, "请手动选择城市", 1).show();
			// // }
			// mLocationClient.unRegisterLocationListener(mLocationListener);
			//
			// // int VERSION =
			// // Integer.parseInt(android.os.Build.VERSION.SDK);
			// // if (VERSION >= 5) {
			// // LoadingActivity.this.overridePendingTransition(
			// // R.anim.alpha_in, R.anim.alpha_out);
			// // }
			//
			// return;
			// }
			// MyLog.i("onReceiveLocation1" + location.getAddrStr());
			// // 获取当前城市，
			// // 获取当前城市，
			// String cityName = location.getCity();
			// String xiangxidi = location.getAddrStr();
			// String province = location.getProvince();
			// latitude = location.getLatitude();
			// longitude = location.getLongitude();
			// MyLog.i("cityName :" + cityName);
			// MyLog.i("xiangxidi :" + xiangxidi);
			// String district = location.getDistrict();
			// if (cityName.endsWith("自治州")) {
			// cityName = district;
			// }
			// if (cityName.contains("市")) {
			// cityName = cityName.substring(0, cityName.length() - 1);
			// }
			// if (cityName.contains("地区")) {
			// cityName = cityName.substring(0, cityName.length() - 2);
			// }
			// mApplication.setDingweicity(cityName);
			// mApplication.setCurrentCityLatitude(latitude + "");
			// mApplication.setCurrentCityLongitude(longitude + "");
			// // 把定位城市存入到缓存里
			// sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
			// editor = sharedPref.edit();
			// editor.putString("dingweiCity", cityName);
			// editor.putString("CurrentCityLongitude", longitude + "");
			// editor.putString("CurrentCityLatitude", latitude + "");
			// editor.putString("province", province);
			// editor.putString("xiangxidi", xiangxidi);
			// editor.commit();
			// mApplication.setXiangxidizhi(xiangxidi);
			// mApplication.setProvince(province);
			// if (!"".equals(cityName)) {
			// WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(),
			// false, cityName);
			// }
			// // 定位成功后发送广播
			// if (xiangxidi != null && xiangxidi != "") {
			// intent = new Intent();
			// intent.setAction(EnvironmentCurrentWeatherPullActivity.DING_WEIService);
			// intent.putExtra("dingwei", xiangxidi);
			// intent.putExtra("city", cityName);
			// sendBroadcast(intent);
			// }
			// mLocationClient.stop();
			// //
			// City curCity = mCityDB.getCity(cityName);// 从数据库中找到该城市
			// dingwei = true;
			//
			// // if (sh.IsFirstUse()) {
			// // Toast.makeText(LoadingActivity.this, "定位" + cityName + "成功",
			// 1)
			// // .show();
			// // intent = new Intent(LoadingActivity.this,
			// // WelcomePagerActivity.class);
			// // intent.putExtra("bangzhu", "diyici");
			// // sh.setIsFirstUse(false);
			// // startActivity(intent);
			// // finish();
			// // } else {
			// // intent = new Intent(LoadingActivity.this,
			// // EnvironmentMainActivity.class);
			// // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			// // startActivity(intent);
			// // finish();
			// // }
			//
			// // int VERSION =
			// // Integer.parseInt(android.os.Build.VERSION.SDK);
			// // if (VERSION >= 5) {
			// // LoadingActivity.this.overridePendingTransition(
			// // R.anim.alpha_in, R.anim.alpha_out);
			// // }
			//
			// MyLog.i("curCity :" + curCity);
			// if (curCity != null) {
			// if (mCityDB.addCityExist(cityName)
			// && mCityDB.addCityExistAndLocation(cityName)) {
			// return;
			// }
			// if (mCityDB.isHaveLocation()) {
			// mCityDB.deleteLocationCity();
			// }
			// if (mCityDB.addCityExist(cityName)) {
			// mCityDB.updateLocation(cityName);
			// } else {
			// mCityDB.addXuanZhecity1(cityName, "", "", true);
			// }
			//
			// } else {// 如果定位到的城市数据库中没有，也弹出定位失败
			// // Toast.makeText(LoadingActivity.this, "请手动选择城市", 1).show();
			// return;
			// }
		}
	};

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// case WeiBaoApplication.CITY_LIST_SCUESS:// 如果城市列表加载完毕，就发送此消息
			case WeiBaoApplication.CITY_LIST_SCUESS:
				long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
				// if (loadingTime < 3000) {
				// MyThread myThread = new MyThread(3000 - loadingTime);
				// myThread.start();
				// } else {

				// }
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 20) {
			// boolean xuanze=data.getBooleanExtra("xuanze", false);
			final String city = data.getExtras().getString("xuanze");
			Intent intent;
			if (!"".equals(city)) {
				WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(),
						false, city);
			}
			if (sh.IsFirstUse()) {
				intent = new Intent(LoadingActivity.this,
						WelcomePagerActivity.class);
				intent.putExtra("bangzhu", "diyici");
				sh.setIsFirstUse(false);
				startActivity(intent);
				finish();
			} else {
				intent = new Intent(LoadingActivity.this,
						EnvironmentMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					String city1 = city;
					if (city1.contains("自治州")) {
						city1 = mCityDB.getSuoSuo(city);
					}
					String url = UrlComponent.getWeatherByCity_Get(city1);
					try {
						String netResult = ApiClient.connServerForResult(url);

						if (netResult.equals("")) {
							return null;
						}

						JSONObject jsonObject = new JSONObject(netResult);
						String cllimate = jsonObject.getString("climate");
						String temp = jsonObject.getString("temp");
						if (mCityDB.addCityExist(city)) {
							mCityDB.update(city, cllimate, temp);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					if (sh.IsFirstUse()) {
					}
					super.onPostExecute(result);
				}

			}.execute();

		}

	}

	Runnable goToMainActivity = new Runnable() {
		@Override
		public void run() {
			Intent intent;
			intent = new Intent(LoadingActivity.this,
					EnvironmentMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}
	};

	class MyThread extends Thread {
		long time;

		public MyThread(long l) {
			time = l;
		}

		@Override
		public void run() {
			super.run();
			try {
				sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent;
			if (sh.IsFirstUse()) {
				intent = new Intent(LoadingActivity.this,
						EnvironmentSelectCtiyActivity.class);
				intent.putExtra("from", "lo");
				startActivityForResult(intent, 100);
				overridePendingTransition(R.anim.addcity_activity_enter,
						R.anim.addcity_activity_enter);
			} else {
				intent = new Intent(LoadingActivity.this,
						EnvironmentMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}

		}
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoadingActivity");
		MobclickAgent.onPause(this);
		// clearAnime();
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("LoadingActivity");
		Intent intent = new Intent(LoadingActivity.this, NewsPushService.class);
		startService(intent);
	}

	private void clearAnime() {
		load_car.clearAnimation();
		load_jiantou.clearAnimation();
		load_sun.clearAnimation();
		load_logo.clearAnimation();
	}

	public boolean isContains(String a, String[] str) {
		MyLog.i(">>>>>>>>>maintain" + a);
		for (String s : str) {
			if (s.contains(a)) {
				return true;
			}

		}
		return false;
	}

}
