package com.jy.environment.controls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePalApplication;
import org.litepal.util.JsonUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import com.baidu.frontia.Frontia;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentMainActivity;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.database.DatabaseConfig;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.exception.CrashHandler;
import com.jy.environment.invitefriends.SortModel;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.City;
import com.jy.environment.model.LaLngData;
import com.jy.environment.model.ProvinceCity;
import com.jy.environment.model.ProvinceCityData;
import com.jy.environment.model.ProvincePoint;
import com.jy.environment.model.WeatherInfo;
import com.jy.environment.services.CustomLocationService;
import com.jy.environment.util.LogcatHelper;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharePreferenceUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.webservice.UrlComponent;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.MKGeneralListener;
//import com.baidu.mapapi.map.MKEvent;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

/**
 * @author shang
 * 
 */
public class WeiBaoApplication extends LitePalApplication {

	private String TAG = "HbdtApplication";

	private static Set<String> jPushAliasAndTags = new HashSet<String>();

	// /**
	// * 当数据库发生更改是更新数据库操作使用
	// */
	// private final String RELOAD_DB = "RELOAD_DB";
	public static String LOCATION_SERVICEINTENT = "com.mapuni.weibao.services.LocationService";
	public final static String MAP_PICTURE_LIST = "com.mapuni.weibao.map.MapImageView";
	public static final int SHOWMSG = 909;
	/***********************************************************************
	 * 目前用来作为地图版块相关服务的url root,以及相关服务维护列表
	 * 
	 * ******start********
	 */

	// public static final String ServerToken =
	// "pFo6dVeXStFiP2PMUZa3";//---测试库token
	public static final String ServerToken = "GLerDd7KotkGHRHy7T9J";// ---正式库token
	private boolean isDownload;
	// public BMapManager mBMapManager = null;
	// 当前选择的城市
	public static String selectedCity = "焦作";
	

	/******************************************************************************
	 * 
	 * *****end*******
	 * 
	 ******************************************************************************/

	public static final int NOTIFICATION_ID1 = 0x1123;
	// 短信保存历史记录
	public List<SortModel> smsList = new ArrayList<SortModel>();
	// public static String preURL = "http://www.shenbianer.com.cn:8080";

	public static final int CITY_LIST_SCUESS = 0;
	private static final String FORMAT = "^[a-z,A-Z].*$";
	private static WeiBaoApplication mApplication;
	private CityDB mCityDB;
	private HashMap<String, Integer> mWeatherIcon;// 天气图标
	private HashMap<String, Integer> mWidgetWeatherIcon;// 插件天气图标

	private String dingweicity;
	private String currentCityLongitude;
	private String currentCityLatitude;

	private String xiangxidizhi;
	private String province;
	private List<String> baseUrlList;
	private String pinjieCity = "";

	public String getPinjieCity() {
		return pinjieCity;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	private List<City> mCityList;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	public static String usename;
	public static String userId;
	public static String userPwd;
	public static String userPic;
	public static Boolean isUpdate;
	public static String userNc;
	public static String userMail;
	
	/**
	 * 河南城市名称数组，不含“市”字；28个
	 */
	private String[] cityArrays = new String[] {
			
//			"郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河",
//			"三门峡", "南阳", "商丘", "信阳", "周口", "驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县", "鹿邑县",
//			"新蔡县" 
			"焦作", "修武县", "博爱县", "武陟县", "温县", "泌阳县", "孟州"
	};

	public String[] getCityArrays() {
		return cityArrays;
	}

	public static String getUserPwd() {
		return userPwd;
	}

	public static void setUserPwd(String userPwd) {
		WeiBaoApplication.userPwd = userPwd;
	}

	public static String userGener;
	public static String phone;

	private static boolean isnoise_logined = false;
	private static boolean isnoisemuch = false;

	public static String isEmailBind;
	public static String isPhoneBind;
	public static int tag_page;

	private LocationClient mLocationClient = null;
	private SharePreferenceUtil mSpUtil;
	private WeatherInfo mAllWeather;
	public static int mNetWorkState;
	private NotificationManager mNotificationManager;
	// public static final String strKey = "6d2f0528ce0ef23ab85208f1c1b03675";
	public static final String strKey = "C3l41zDMFSp28kXpGvpvAudK";
	public boolean m_bKeyRight = true;

	// 全局handler
	public Handler mhandler;

	SDKReceiver mReceiver;

	public static synchronized WeiBaoApplication getInstance() {
		return mApplication;
	}
	/**
	 * TODO 省内城市坐标集合
	 */
	private HashMap<String,LaLngData> provinceCityLatLngMap = null;

	public HashMap<String, LaLngData> getProvinceCityLatLngMap() {
		Log.i("YYF", "zzzzzzzzzzzzz===="+provinceCityLatLngMap.toString());
		return provinceCityLatLngMap;
	}

	// TODO YYF数据源容器
	// 全国
	private List<AQIPoint> aqipointList = null;
	// 省内city
	private List<ProvinceCity> provincecityList = null;
	// 省内point
	private List<ProvincePoint> provincepointList = null;

	public List<AQIPoint> getAqipointList() {
		return aqipointList;
	}

	public void setAqipointList(ArrayList<AQIPoint> aqipointList) {
		this.aqipointList = aqipointList;
	}

	public List<ProvinceCity> getProvincecityList() {
		return provincecityList;
	}

	public void setProvincecityList(List<ProvinceCity> provincecityList) {
		this.provincecityList = provincecityList;
	}

	public List<ProvincePoint> getProvincepointList() {
		return provincepointList;
	}

	public void setProvincepointList(List<ProvincePoint> provincepointList) {
		this.provincepointList = provincepointList;
	}
	public CustomLocationService locationService;
    public Vibrator mVibrator;
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Class.forName("com.jy.environment.controls.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		baseUrlList = new ArrayList<String>();
		baseUrlList.add(UrlComponent.baseurl_choose2);
		baseUrlList.add(UrlComponent.baseurl_choose1);
		UrlComponent.setBaseUrlList(baseUrlList);
		isDownload = false;
		mApplication = this;
		// CustomCrashHandler mCustomCrashHandler = CustomCrashHandler
		// .getInstance();
		// mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
		DatabaseConfig.getVersion();
		// initEngineManager(this);
		/** baidumap 3.1.1 new key init. **/
        locationService = new CustomLocationService(this.getApplicationContext());
        mVibrator =(Vibrator)this.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		SDKInitializer.initialize(this.getApplicationContext());
		boolean isInit = Frontia.init(this.getApplicationContext(), "LnzKqlgMKdz10Nfi2fm8QueGVNEx6V2T");

		mCityDB = openCityDB();// TODO 这个必须最先复制完,所以我放在单线程中处理,待优化
		CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.setCustomCrashHanler(getApplicationContext());
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		shanchu();
		LogcatHelper.getInstance(this).start();
		SharedPreferences sp = getSharedPreferences("MAP_SHARE_LOGIN_TAG", MODE_PRIVATE);
		String user = sp.getString("MAP_LOGIN_USERNAME", "");
		MyLog.i("user :" + user);
		String userId = sp.getString("MAP_LOGIN_USERID", "");
		MyLog.i("userId :" + userId);
		String userPic = sp.getString("MAP_LOGIN_USERPIC", "");
		MyLog.i("userPic :" + userPic);
		String userNc = sp.getString("MAP_LOGIN_USERNC", "");
		String userMail = sp.getString("MAP_LOGIN_USERMAIL", "");
		String userGender = sp.getString("MAP_LOGIN_USERGENDER", "");
		String phone = sp.getString("MAP_LOGINPHONE", "");
		String isEmailBind = sp.getString("MAP_LOGIN_USERISEMAIL", "");
		String isPhoneBind = sp.getString("MAP_LOGIN_USERISPHONE", "");
		String userPwd = sp.getString("MAP_LOGIN_PASSWORD", "");
		setUsename(user);
		setUserId(userId);
		setUserPic(userPic);
		setUserNc(userNc);
		setUserMail(userMail);
		setUserGener(userGender);
		setPhone(phone);
		setIsEmailBind(isEmailBind);
		setIsPhoneBind(isPhoneBind);
		setUserPwd(userPwd);
		SharedPreferences spMap = getSharedPreferences("sharedPref", MODE_PRIVATE);
		// setCurrentCityLatitude(spMap.getString("CurrentCityLatitude", ""));
		// setCurrentCityLongitude(spMap.getString("CurrentCityLongitude", ""));
		setDingweicity(spMap.getString("dingweiCity", ""));
		// setXiangxidizhi(spMap.getString("xiangxidi", ""));
		setProvince(spMap.getString("province", ""));
		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOWMSG:
					ToastUtil.showShort(getApplicationContext(), msg.obj.toString());
				}
			}
		};
		pinjieCity = "";
		List<AQIPoint> aqipointFromDB = WbMapUtil.getAQICityInMapExtentFromDB(0, 0, 0, 0);
		for (int i = 0; i < aqipointFromDB.size(); i++) {
			String city = aqipointFromDB.get(i).getCity();
			String jiancedian = aqipointFromDB.get(i).getJiancedian();
			if (isContains(jiancedian, cityArrays)) {
				continue;
			}
			pinjieCity += "," + jiancedian;
		}
		MyLog.i(">>>>>>>>pinjieCity" + pinjieCity);

		// TODO YYF asynctask请求网络：post

		requestNationalData();
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		getjPushAliasAndTags(this);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo).showImageOnFail(R.drawable.empty_photo)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
																					// for
																					// release
																					// app
				.discCacheSize(100 * 1024 * 1024)//
				.build();
		ImageLoader.getInstance().init(config);
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);

		// 数据源初始化
		if (aqipointList == null)
			aqipointList = new ArrayList<AQIPoint>();
		if (provincecityList == null)
			provincecityList = new ArrayList<ProvinceCity>();
		if (provincepointList == null)
			provincepointList = new ArrayList<ProvincePoint>();

		// TODO 初始化 OkHttpUtils
		initOkHttpUtils();

		// TODO 请求省内数据：city+point
		// requestProvinceCity();
		// requestProvincePoint();
		
		provinceCityLatLngMap = new HashMap<String, LaLngData>();
		//从数据库中获取省内城市坐标
		getProvinceLaLngDataFromDB();
	}


	/**
	 * 从数据库中获取省内城市坐标
	 */
	private void getProvinceLaLngDataFromDB() {
//		provinceCityLatLngMap = mCityDB.getProvinceCityLatLng(cityArrays);
		provinceCityLatLngMap.clear();
		provinceCityLatLngMap.putAll(mCityDB.getProvinceCityLatLng(cityArrays));
		Log.i("YYF", "provinceCityLatLngMap====="+provinceCityLatLngMap.size()+":"+provinceCityLatLngMap.toString());
	}

	/**
	 * TODO 请求全国污染物数据
	 */
	public void requestNationalData() {
		String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
		UpdateAQIcityTask task = new UpdateAQIcityTask();
		task.execute(url, pinjieCity);
	}

	/**
	 * TODO 请求省内污染物数据：city
	 * 
	 * 仅测试使用
	 */
	public void requestProvinceCity() {
		new Thread(new Runnable() {
			public void run() {
				String url3 = UrlComponent.getHeNanCityValueUrl;
				OkHttpUtils.get().url(url3).build().execute(callback3);
			}
		}).start();
	}

	/**
	 * TODO 请求省内污染物数据：point
	 * 
	 * 仅测试使用
	 */
	public void requestProvincePoint() {
		new Thread(new Runnable() {
			public void run() {
				String url2 = UrlComponent.getHeNanPointValueUrl;
				OkHttpUtils.get().url(url2).build().execute(callback2);
			}
		}).start();
	}

	// TODO 请求省内数据city的回调；仅测试使用
	protected StringCallback callback3 = new StringCallback() {

		@Override
		public void onError(Call call, Exception exception, int arg) {
			exception.printStackTrace();
		}

		@Override
		public void onResponse(String str, int arg) {
			// 解析数据
			resolveCityData(str);
		}

		private void resolveCityData(String str) {
			Gson gson = new Gson();
			ProvinceCityData data = gson.fromJson(str, ProvinceCityData.class);
			List<ProvinceCity> list = data.getData();
			provincecityList.addAll(list);
			Log.i("YYF", "MyApp请求省内数据city的回调：" + provincecityList.toString());
		}

	};

	// TODO 请求省内数据point的回调；仅测试使用
	protected StringCallback callback2 = new StringCallback() {

		@Override
		public void onError(Call call, Exception exception, int arg) {
			exception.printStackTrace();
		}

		@Override
		public void onResponse(String str, int arg) {
			// Log.i("YYF", "请求省内数据point的回调：" + str.toString());
			resolvePointData(str);
		}

		private void resolvePointData(String str) {
			// TODO YYF修改 解析point数据到bean中
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONObject jsonObject2 = jsonObject.getJSONObject("detail");
				JSONObject jsonObject3 = jsonObject2.getJSONObject("洛阳市");
				JSONArray jsonArray = jsonObject3.getJSONArray("STATIONS");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						Object object = jsonArray.get(i);
						String json = JsonUtil.objectToJson(object);
						Gson gson = new Gson();
						ProvincePoint point = gson.fromJson(json, ProvincePoint.class);
						provincepointList.add(point);
					}
					Log.i("YYF", "MyApp请求省内数据point的回调：" + provincepointList.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private void initOkHttpUtils() {
		CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
		HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(30000L, TimeUnit.MILLISECONDS)
				.readTimeout(20000L, TimeUnit.MILLISECONDS)
				// 其他配置
				.cookieJar(cookieJar).addInterceptor(new LoggerInterceptor("TAG"))
				.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build();

		OkHttpUtils.initClient(okHttpClient);
	}

	private void shanchu() {
		String path = Environment.getExternalStorageDirectory().toString() + "/weibao/";
		File file = new File(path + "deviceInfo.log");

		if (file.exists()) {
			file.delete();
		}

	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString() + "/Android/data/com.mapuni.com/huancun";
		}
		return sdDir.toString();

	}

	public String getInternal() {
		File sdDir = null;
		sdDir = getApplicationContext().getFilesDir();
		return sdDir.toString();

	}

	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static final boolean DEVELOPER_MODE = false;

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		WeiBaoApplication.userId = userId;
	}

	public static String getUsename() {
		return usename;
	}

	public static void setUsename(String usename) {
		WeiBaoApplication.usename = usename;
	}

	public static int getTag_page() {
		return tag_page;
	}

	public static void setTag_page(int tag_page) {
		WeiBaoApplication.tag_page = tag_page;
	}

	public static String getUserPic() {
		return userPic;
	}

	public static void setUserPic(String userPic) {
		WeiBaoApplication.userPic = userPic;
	}

	public static String getUserNc() {
		return userNc;
	}

	public static void setUserNc(String userNc) {
		WeiBaoApplication.userNc = userNc;
	}

	public static String getUserMail() {
		return userMail;
	}

	public static void setUserMail(String userMail) {
		WeiBaoApplication.userMail = userMail;
	}

	public static String getUserGener() {
		return userGener;
	}

	public static void setUserGener(String userGener) {
		WeiBaoApplication.userGener = userGener;
	}

	public static boolean getIsnoise_logined() {
		return isnoise_logined;
	}

	public static void setIsnoise_logined(boolean isnoise_logined) {
		WeiBaoApplication.isnoise_logined = isnoise_logined;
	}

	public static Boolean getIsnoisemuch() {
		return isnoisemuch;
	}

	public static void setIsnoisemuch(Boolean isnoisemuch) {
		WeiBaoApplication.isnoisemuch = isnoisemuch;
	}

	public static String getPhone() {
		return phone;
	}

	public static void setPhone(String phone) {
		WeiBaoApplication.phone = phone;
	}

	public static String getIsEmailBind() {
		return isEmailBind;
	}

	public static void setIsEmailBind(String isEmailBind) {
		WeiBaoApplication.isEmailBind = isEmailBind;
		MyLog.i("xu1123:" + isEmailBind);
	}

	public static String getIsPhoneBind() {
		return isPhoneBind;
	}

	public static void setIsPhoneBind(String isPhoneBind) {
		WeiBaoApplication.isPhoneBind = isPhoneBind;
	}

	// public void initEngineManager(Context context) {
	// if (mBMapManager == null) {
	// mBMapManager = new BMapManager(context);
	// }

	// public static class MyGeneralListener implements MKGeneralListener {
	//
	// @Override
	// public void onGetNetworkState(int iError) {
	// if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	// Toast.makeText(
	// WeiBaoApplication.getInstance().getApplicationContext(),
	// "您的网络出错啦！", Toast.LENGTH_LONG).show();
	// } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	// Toast.makeText(
	// WeiBaoApplication.getInstance().getApplicationContext(),
	// "输入正确的检索条件！", Toast.LENGTH_LONG).show();
	// }
	// // ...
	// }
	//
	// @Override
	// public void onGetPermissionState(int iError) {
	// if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
	// // 授权Key错误：
	// // Toast.makeText(HbdtApplication.getInstance().getApplicationContext(),
	// // "请在 DemoApplication.java文件输入正确的授权Key！",
	// // Toast.LENGTH_LONG).show();
	// WeiBaoApplication.getInstance().m_bKeyRight = false;
	// }
	// }
	// }

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				// 网络出错，相应处理
				Toast.makeText(WeiBaoApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			}

		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		try {
			if (mCityDB != null && mCityDB.isOpen())
				mCityDB.close();
			Intent intent = new Intent(WeiBaoApplication.LOCATION_SERVICEINTENT);
			this.stopService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			LogcatHelper.getInstance(this).stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLowMemory() {
		// super.onLowMemory();
		// try {
		// LogcatHelper.getInstance(this).stop();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		try {
			Intent intent = new Intent(WeiBaoApplication.LOCATION_SERVICEINTENT);
			this.stopService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 当程序在后台运行时，释放这部分最占内存的资源
	public void free() {
		mCityList = null;
		mSections = null;
		mMap = null;
		mPositions = null;
		mIndexer = null;
		mWeatherIcon = null;
		mAllWeather = null;
		System.gc();
	}

	private LocationClientOption getLocationClientOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setScanSpan(0);
		option.setNeedDeviceDirect(true);
		return option;
	}

	public void initData(Handler handler) {

		mNetWorkState = NetUtil.getNetworkState(this);
		initCityList(handler);
		mLocationClient = new LocationClient(this, getLocationClientOption());
		initWeatherIconMap();
		initWidgetWeather();
		mSpUtil = new SharePreferenceUtil(this, SharePreferenceUtil.CITY_SHAREPRE_FILE);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	public synchronized CityDB getCityDB() {
		if (mCityDB == null || !mCityDB.isOpen())
			mCityDB = openCityDB();
		return mCityDB;
	}

	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this, SharePreferenceUtil.CITY_SHAREPRE_FILE);
		return mSpUtil;
	}

	public synchronized LocationClient getLocationClient() {
		if (mLocationClient == null)
			mLocationClient = new LocationClient(this, getLocationClientOption());
		return mLocationClient;
	}

	String curVersionName = "";

	private CityDB openCityDB() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			curVersionName = info.versionName;
			MyLog.i("============" + curVersionName);

			if (curVersionName.contains("beta")) {
				curVersionName = curVersionName.substring(0, curVersionName.lastIndexOf("beta"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		String path = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
				+ "com.jy.environment" + File.separator + CityDB.CITY_DB_NAME;

		MyLog.i("path" + path);
		File db = new File(path);
		MyLog.i("db" + db);
		// SharedPreferencesUtil sh =
		// SharedPreferencesUtil.getInstance(mApplication);
		//
		// if (sh.IsFirstUse()&&db.exists()) {
		//
		// db.delete();
		// }
		MyLog.i("curVersionNameggggggg :" + curVersionName);
		// /**
		// * 下个版本更改
		// */
		// if (Double.parseDouble(curVersionName) > 1.2) {
		// db.delete();
		// }

		MyLog.i("!db.exists()" + !db.exists());
		MyLog.i("getSharePreferenceUtil().getVersion()" + getSharePreferenceUtil().getVersion());
		// if (!db.exists() || getSharePreferenceUtil().getVersion() < 0) {
		/**
		 * 如果要修改重新加载数据库，那么就更改版本号，2.23版本进行了一次db更新操作
		 */
		if (!db.exists() || getSharePreferenceUtil().getVersion() < 200) {
			try {
				MyLog.i("CityDB.CITY_DB_NAME" + CityDB.CITY_DB_NAME);
				InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				MyLog.i("Load DB" + CityDB.CITY_DB_NAME);
				try {

					getSharePreferenceUtil().setVersion((int) (Double.parseDouble(curVersionName) * 100));// 用于管理数据库版本，如果数据库有重大更新时使用
					MyLog.i("getSharePreferenceUtil().getVersion() :" + getSharePreferenceUtil().getVersion());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return new CityDB(this, path);
	}

	public List<City> getCityList() {
		return mCityList;
	}

	public List<String> getSections() {
		return mSections;
	}

	public Map<String, List<City>> getMap() {
		return mMap;
	}

	public List<Integer> getPositions() {
		return mPositions;
	}

	public Map<String, Integer> getIndexer() {
		return mIndexer;
	}

	public Map<String, Integer> getWeatherIconMap() {
		if (mWeatherIcon == null || mWeatherIcon.isEmpty())
			mWeatherIcon = initWeatherIconMap();
		return mWeatherIcon;
	}

	public NotificationManager getNotificationManager() {
		return mNotificationManager;
	}

	// public int getWeatherIcon(String climate) {
	// int weatherRes = R.drawable.w0;
	// if (TextUtils.isEmpty(climate))
	// return weatherRes;
	// String[] strs = { "晴", "晴" };
	// if (climate.contains("转")) {// 天气带转字，取前面那部分
	// strs = climate.split("转");
	// climate = strs[0];
	// if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
	// strs = climate.split("到");
	// climate = strs[1];
	// }
	// }
	// if (mWeatherIcon == null || mWeatherIcon.isEmpty())
	// mWeatherIcon = initWeatherIconMap();
	// if (mWeatherIcon.containsKey(climate)) {
	// weatherRes = mWeatherIcon.get(climate);
	// }
	// return weatherRes;
	// }

	// public int getWidgetWeatherIcon(String climate) {
	// int weatherRes = R.drawable.na;
	// if (TextUtils.isEmpty(climate))
	// return weatherRes;
	// String[] strs = { "晴", "晴" };
	// if (climate.contains("转")) {// 天气带转字，取前面那部分
	// strs = climate.split("转");
	// climate = strs[0];
	// if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
	// strs = climate.split("到");
	// climate = strs[1];
	// }
	// }
	// if (mWidgetWeatherIcon == null || mWidgetWeatherIcon.isEmpty())
	// mWidgetWeatherIcon = initWidgetWeather();
	// if (mWidgetWeatherIcon.containsKey(climate)) {
	// weatherRes = mWidgetWeatherIcon.get(climate);
	// }
	// return weatherRes;
	// }

	public WeatherInfo getAllWeather() {
		return mAllWeather;
	}

	public void SetAllWeather(WeatherInfo allWeather) {
		mAllWeather = allWeather;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		WeiBaoApplication.isUpdate = isUpdate;
	}

	public String getXiangxidizhi() {
		return xiangxidizhi;
	}

	public void setXiangxidizhi(String xiangxidizhi) {
		this.xiangxidizhi = xiangxidizhi;
	}

	public String getDingweicity() {
		return dingweicity;
	}

	public void setDingweicity(String dingweicity) {
		this.dingweicity = dingweicity;
	}

	private HashMap<String, Integer> initWeatherIconMap() {
		if (mWeatherIcon != null && !mWeatherIcon.isEmpty())
			return mWeatherIcon;
		mWeatherIcon = new HashMap<String, Integer>();
		mWeatherIcon.put("暴雪", R.drawable.weather_icon_baoxue);
		mWeatherIcon.put("暴雨", R.drawable.weather_icon_baoyu);
		mWeatherIcon.put("大暴雨", R.drawable.weather_icon_dabaoyu);
		mWeatherIcon.put("大雪", R.drawable.weather_icon_daxue);
		mWeatherIcon.put("大雨", R.drawable.weather_icon_dayu);
		mWeatherIcon.put("冻雨", R.drawable.weather_icon_dongyu);
		mWeatherIcon.put("多云", R.drawable.weather_icon_duoyun);
		mWeatherIcon.put("浮尘", R.drawable.weather_icon_fuchen);
		mWeatherIcon.put("降雪", R.drawable.weather_icon_jiangxue);
		mWeatherIcon.put("降雨", R.drawable.weather_icon_jiangyu);
		mWeatherIcon.put("雷阵雨", R.drawable.weather_icon_leizhenyu);
		mWeatherIcon.put("霾", R.drawable.weather_icon_mai);
		mWeatherIcon.put("轻度霾", R.drawable.weather_icon_qingdumai);
		mWeatherIcon.put("轻微霾", R.drawable.weather_icon_qingweimai);
		mWeatherIcon.put("晴", R.drawable.weather_icon_qingtian);
		mWeatherIcon.put("沙尘暴", R.drawable.weather_icon_shachenbao);
		mWeatherIcon.put("特大暴雨", R.drawable.weather_icon_tedabaoyu);
		mWeatherIcon.put("雾", R.drawable.weather_icon_wu);
		mWeatherIcon.put("小雪", R.drawable.weather_icon_xiaoxue);
		mWeatherIcon.put("阵雪", R.drawable.weather_icon_daxue);

		mWeatherIcon.put("小雨", R.drawable.weather_icon_xiaoyu);
		mWeatherIcon.put("阵雨", R.drawable.weather_icon_xiaoyu);
		mWeatherIcon.put("扬沙", R.drawable.weather_icon_yangsha);
		mWeatherIcon.put("阴", R.drawable.weather_icon_yin);
		mWeatherIcon.put("雨", R.drawable.weather_icon_yu);
		mWeatherIcon.put("雨夹雪", R.drawable.weather_icon_yuajiaxue);
		mWeatherIcon.put("中雪", R.drawable.weather_icon_zhongxue);
		mWeatherIcon.put("中雨", R.drawable.weather_icon_zhongyu);
		//
		// mWeatherIcon.put("暴雪", R.drawable.w17);
		// mWeatherIcon.put("暴雨", R.drawable.w10);
		// mWeatherIcon.put("大暴雨", R.drawable.w10);
		// mWeatherIcon.put("大雪", R.drawable.w16);
		// mWeatherIcon.put("大雨", R.drawable.w9);
		// mWeatherIcon.put("中到大雨", R.drawable.w9);
		// mWeatherIcon.put("中到小雨", R.drawable.w7);
		// mWeatherIcon.put("雨", R.drawable.w9);
		//
		// mWeatherIcon.put("多云", R.drawable.w1);
		// mWeatherIcon.put("晴转多云", R.drawable.w1);
		// mWeatherIcon.put("晴间多云", R.drawable.w1);
		// mWeatherIcon.put("雷阵雨", R.drawable.w4);
		// mWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
		// mWeatherIcon.put("晴", R.drawable.w0);
		// mWeatherIcon.put("沙尘暴", R.drawable.w20);
		//
		// mWeatherIcon.put("特大暴雨", R.drawable.w10);
		// mWeatherIcon.put("雾", R.drawable.w18);
		// mWeatherIcon.put("小雪", R.drawable.w14);
		// mWeatherIcon.put("小雨", R.drawable.w7);
		// mWeatherIcon.put("阴", R.drawable.w2);
		//
		// mWeatherIcon.put("雨夹雪", R.drawable.w6);
		// mWeatherIcon.put("阵雪", R.drawable.w13);
		// mWeatherIcon.put("阵雨", R.drawable.w3);
		// mWeatherIcon.put("中雪", R.drawable.w15);
		// mWeatherIcon.put("中雨", R.drawable.w8);
		return mWeatherIcon;
	}

	private HashMap<String, Integer> initWidgetWeather() {
		if (mWidgetWeatherIcon != null && !mWidgetWeatherIcon.isEmpty())
			return mWidgetWeatherIcon;

		mWidgetWeatherIcon = new HashMap<String, Integer>();
		mWidgetWeatherIcon.put("暴雪", R.drawable.weather_icon_baoxue);
		mWidgetWeatherIcon.put("暴雨", R.drawable.weather_icon_baoyu);
		mWidgetWeatherIcon.put("大暴雨", R.drawable.weather_icon_dabaoyu);
		mWidgetWeatherIcon.put("大雪", R.drawable.weather_icon_daxue);
		mWidgetWeatherIcon.put("大雨", R.drawable.weather_icon_dayu);
		mWidgetWeatherIcon.put("冻雨", R.drawable.weather_icon_dongyu);
		mWidgetWeatherIcon.put("多云", R.drawable.weather_icon_duoyun);
		mWidgetWeatherIcon.put("浮尘", R.drawable.weather_icon_fuchen);
		mWidgetWeatherIcon.put("降雪", R.drawable.weather_icon_jiangxue);
		mWidgetWeatherIcon.put("降雨", R.drawable.weather_icon_jiangyu);
		mWidgetWeatherIcon.put("雷阵雨", R.drawable.weather_icon_leizhenyu);
		mWidgetWeatherIcon.put("霾", R.drawable.weather_icon_mai);
		mWidgetWeatherIcon.put("轻度霾", R.drawable.weather_icon_qingdumai);
		mWidgetWeatherIcon.put("轻微霾", R.drawable.weather_icon_qingweimai);
		mWidgetWeatherIcon.put("晴", R.drawable.weather_icon_qingtian);
		mWidgetWeatherIcon.put("沙尘暴", R.drawable.weather_icon_shachenbao);
		mWidgetWeatherIcon.put("特大暴雨", R.drawable.weather_icon_tedabaoyu);
		mWidgetWeatherIcon.put("雾", R.drawable.weather_icon_wu);
		mWidgetWeatherIcon.put("小雪", R.drawable.weather_icon_xiaoxue);
		mWidgetWeatherIcon.put("小雨", R.drawable.weather_icon_xiaoyu);
		mWidgetWeatherIcon.put("扬沙", R.drawable.weather_icon_yangsha);
		mWidgetWeatherIcon.put("阴", R.drawable.weather_icon_yin);
		mWidgetWeatherIcon.put("雨", R.drawable.weather_icon_yu);
		mWidgetWeatherIcon.put("雨夹雪", R.drawable.weather_icon_yuajiaxue);
		mWidgetWeatherIcon.put("中雪", R.drawable.weather_icon_zhongxue);
		mWidgetWeatherIcon.put("中雨", R.drawable.weather_icon_zhongyu);

		// mWidgetWeatherIcon.put("暴雪", R.drawable.w17);
		// mWidgetWeatherIcon.put("暴雨", R.drawable.w10);
		// mWidgetWeatherIcon.put("大暴雨", R.drawable.w10);
		// mWidgetWeatherIcon.put("大雪", R.drawable.w16);
		// mWidgetWeatherIcon.put("大雨", R.drawable.w9);
		// mWidgetWeatherIcon.put("中到大雨", R.drawable.w9);
		// mWidgetWeatherIcon.put("中到小雨", R.drawable.w7);
		//
		// mWidgetWeatherIcon.put("多云", R.drawable.w1);
		// mWidgetWeatherIcon.put("雷阵雨", R.drawable.w4);
		// mWidgetWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
		// mWidgetWeatherIcon.put("晴", R.drawable.w0);
		// mWidgetWeatherIcon.put("晴转多云", R.drawable.w1);
		// mWidgetWeatherIcon.put("晴间多云", R.drawable.w1);
		// mWidgetWeatherIcon.put("沙尘暴", R.drawable.w20);
		//
		// mWidgetWeatherIcon.put("特大暴雨", R.drawable.w10);
		// mWidgetWeatherIcon.put("雾", R.drawable.w18);
		// mWidgetWeatherIcon.put("小雪", R.drawable.w14);
		// mWidgetWeatherIcon.put("小雨", R.drawable.w7);
		// mWidgetWeatherIcon.put("阴", R.drawable.w2);
		//
		// mWidgetWeatherIcon.put("雨夹雪", R.drawable.w6);
		// mWidgetWeatherIcon.put("阵雪", R.drawable.w13);
		// mWidgetWeatherIcon.put("阵雨", R.drawable.w3);
		// mWidgetWeatherIcon.put("中雪", R.drawable.w15);
		// mWidgetWeatherIcon.put("中雨", R.drawable.w8);
		return mWidgetWeatherIcon;
	}

	private void initCityList(final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Looper.prepare();
					prepareCityList();
					handler.sendEmptyMessage(CITY_LIST_SCUESS);
					// Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private boolean prepareCityList() {
		mCityList = new ArrayList<City>();
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<City>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();
		mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
		for (City city : mCityList) {
			String firstName = city.getPy().substring(0, 1).toUpperCase();// 第一个字拼音的第一个字母
			if (firstName.matches(FORMAT)) {
				if (mSections.contains(firstName) && null != mMap && null != mMap.get(firstName)) {
					mMap.get(firstName).add(city);
				} else {
					mSections.add(firstName);
					List<City> list = new ArrayList<City>();
					list.add(city);
					mMap.put(firstName, list);
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(city);
				} else {
					mSections.add("#");
					List<City> list = new ArrayList<City>();
					list.add(city);
					mMap.put("#", list);
				}
			}
		}
		// synchronized (mSections) {
		// Collections.sort(mSections);// 按照字母重新排序
		// }
		// int position = 0;
		// for (int i = 0; i < mSections.size(); i++) {
		// mIndexer.put(mSections.get(i), position);//
		// 存入map中，key为首字母字符串，value为首字母在listview中位置
		// mPositions.add(position);// 首字母在listview中位置，存入list中
		// position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
		// }
		try {
			ResetListTask listTask = new ResetListTask();
			listTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public class ResetListTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				synchronized (mSections) {
					Collections.sort(mSections);// 按照字母重新排序
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			try {
				if (result) {
					int position = 0;
					for (int i = 0; i < mSections.size(); i++) {
						mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
						mPositions.add(position);// 首字母在listview中位置，存入list中
						position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
					}
				}
			} catch (Exception e) {
			}
		}

	}

	public void showNotification() {

		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = mAllWeather.getYujing();
		long when = System.currentTimeMillis();
		Notification mNotification = new Notification(icon, tickerText, when);

		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.contentView = null;

		Intent intent = new Intent(this, EnvironmentMainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 指定内容意图
		mNotification.setLatestEventInfo(mApplication, "宇图天气预警", tickerText, contentIntent);

		mNotificationManager.notify(0x001, mNotification);
	}

	public String getCurrentCityLongitude() {
		return currentCityLongitude;
	}

	public void setCurrentCityLongitude(String currentCityLongitude) {
		this.currentCityLongitude = currentCityLongitude;
	}

	public String getCurrentCityLatitude() {
		return currentCityLatitude;
	}

	public void setCurrentCityLatitude(String currentCityLatitude) {
		MyLog.i("setCurrentCityLatitude :" + currentCityLatitude);
		this.currentCityLatitude = currentCityLatitude;
	}

	/**
	 * 
	 * @param context
	 *            getApplicationContext()
	 * @param city
	 */
	public static void addJPushAliasAndTags(Context context, boolean remove, String city) {
		if (!remove && null != jPushAliasAndTags && !jPushAliasAndTags.contains(city)) {
			jPushAliasAndTags.add(city);
		}
		if (remove && null != jPushAliasAndTags && jPushAliasAndTags.contains(city)) {
			jPushAliasAndTags.remove(city);
		}
		SharedPreferences jPushMap = context.getSharedPreferences("jPushAliasAndTags", MODE_PRIVATE);
		String setData = "";
		Iterator<String> it = jPushAliasAndTags.iterator();
		while (it.hasNext()) {
			String str = it.next();
			setData += str + ",";
		}
		Editor editor = jPushMap.edit();
		editor.putString("setData", setData);
		editor.putString("test", "222");
		editor.commit();

		JPushInterface.setAliasAndTags(context, null, jPushAliasAndTags, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				// MyLog.i("gotResult :" + arg0);
				MyLog.i("gotResult String:" + arg1);
				// MyLog.i("gotResult Set<String>:" + arg2);
			}
		});
	}

	public static Set<String> getjPushAliasAndTags(Context context) {
		SharedPreferences jPushMap = context.getSharedPreferences("jPushAliasAndTags", MODE_PRIVATE);
		String setData = jPushMap.getString("setData", "");
		String ss[] = setData.split(",");
		for (int i = 0; i < ss.length; i++) {
			if (null == ss[i] || "".equals(ss[i])) {
			} else {
				jPushAliasAndTags.add(ss[i]);
			}
		}
		return jPushAliasAndTags;
	}

	public static void setjPushAliasAndTags(Context context, Set<String> jPushAliasAndTags) {
		WeiBaoApplication.jPushAliasAndTags = jPushAliasAndTags;
	}

	/**
	 * TODO post请求全国数据
	 * 
	 * @author 尹亚飞
	 * 
	 */
	class UpdateAQIcityTask extends AsyncTask<Object, Void, List<AQIPoint>> {

		@Override
		protected List<AQIPoint> doInBackground(Object... arg) {
			BusinessSearch search = new BusinessSearch();
			String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
			List<AQIPoint> _Result;
			try {
				MyLog.i("url=="+url);
				MyLog.i("key=="+(String) arg[0]);
				MyLog.i("value=="+pinjieCity);
				_Result = search.getShengHuiAqi((String) arg[0], pinjieCity);
				// YYF获取到了全国数据
				// Log.i("YYF", _Result.toString());
				
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<AQIPoint> result) {
			aqipointList = (ArrayList<AQIPoint>) result;
			if (aqipointList != null)
				Log.i("YYF", aqipointList.toString());
		}

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
