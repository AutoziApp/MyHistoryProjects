package com.jy.environment.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.jy.environment.R;
import com.jy.environment.adapter.WeatherAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.AlarmDao;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.database.model.ModelAlarmHistory;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.Exceedmodel;
import com.jy.environment.model.MainAqiData;
import com.jy.environment.model.ManageCity;
import com.jy.environment.model.Sweather;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.ACache;
import com.jy.environment.util.Blur;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.ExampleUtil;
import com.jy.environment.util.ImageUtils;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.UpdateManagerUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.MyPopWindows;
import com.jy.environment.widget.RainView;
import com.jy.environment.widget.SnowView;
import com.jy.environment.widget.TopCenterImageView;
import com.jy.environment.widget.XListView;
import com.jy.environment.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentCurrentWeatherPullActivity extends ActivityBase
		implements OnClickListener, SynthesizerListener {
	private MessageReceiver mMessageReceiver;
	private LocationClient mLocationClient;
	// private Exceedmodel currentJson;
	private CurrentWeather weatherCurrent;
	private Sweather sweather;
	private Uri uri;
	private long exitTime = 0;
	private EditText msgText;
	private String yyue = "", userId = "";
	private RainView rain = null;
	private FrameLayout flLayout;
	private ImageView zaoshengImageView, chouiv1, chouiv2, chouiv3, chouiv4,
			chouiv5, chouiv6, chouiv7, chouiv8;
	private String dingweiaddress, lottery = "", check = "";
	private static boolean isLocationChange = false;
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String KEY_Login = "action_Login";
	private CityDB mCityDB;
	public static final String LOCATIONCHANGEACTION = "com.mapuni.weibao.locatinonChange";
	public static final String DING_WEI = "com.mapuni.weibao.ui.CurrentTq";
	public static final String DING_WEIService = "com.weiService";
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	private SharedPreferencesUtil mSpUtil2;
	private WeiBaoApplication mApplication;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private ImageView title_location, title_share;
	private LinearLayout title_city_manager_layout;
	private View mHeaderView;
	private WeatherAdapter adapter;
	private ViewPager aPager;
	private TextView title_city_name, qiandao_btn;
	private XListView current_weather_xListView;
	private List<View> views = new ArrayList<View>();
	GuidePageAdapter pageAdapter;
	private LinearLayout group;
	private ImageView imageView;
	private int pos, posposition = 0;
	private ImageView[] imageViews;
	private int width, height, reheight, reheightm;
	private TopCenterImageView bgChange, bgChange_blurred;
	public SharedPreferences sharedPref;// 缓存
	public RefreshHandler mRedrawHandler = new RefreshHandler();
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> initcityResume = new ArrayList<HashMap<String, Object>>();
	private String latitude = "0", longitude = "0", content, dingweicity = "",
			mShare = "今日%s天气：%s，温度：%s；空气质量指数(AQI)：%s，PM2.5 浓度值：%s μg/m3。";
	private DisplayMetrics metrics;
	private int screenHalfWidth;
	private int screenHalfheigh12, screenHalfheighead;
	private int screenHalfheigh8;
	private int screenWidth;
	public static Boolean is_rain = false;
	public static Boolean is_snow = false;
	public static Boolean is_cloud = false;
	public static Boolean is_wumai = false;
	public static Boolean is_qing = false;
	private boolean choujiangFlag = false;
	private ImageView[] chou = new ImageView[8];
	private Bitmap bitmap;
	private FrameLayout qq_xingxiang;
	private ImageView qq_luo, choujiang_cancel;

	private ImageView ivSun;
	private RelativeLayout weather_layout1;
	// 跳转到一个界面
	private String backgroundType;
	private Context context;
	private String backgroundType_ye = "heiye", morenCity = "";

	private PopupWindow popupWindow;
	private int i = 1;
	private CurrentWeather weather;

	private LinearLayout ll_curr_weather;
	private int index = 0, top = 0;
	private int dds;
	private ImageLoader loader;
	private TextView choujiang_title, choujiang_shop;
	// weather_quality,weather_pm25
	private AnimationDrawable anim;
	private MyOnPageChangeListener pageChangeListener = new MyOnPageChangeListener();
	// private SQLiteDALModelAlarmHistory alarmHistoryDB;
	private TextView warningText, baike, choujiangintro;
	private TextView warmTextView, viewpager_jiaobiao;
	private KjhttpUtils http;
	private ACache aCache, eACache;
	private LinearLayout weatherlay;
	private String externalStorage;
	private View qiandaoView, baikeView, choujiangView;
	public static String userName = "", userPwd = "";
	// 内置存储器路径，缓存存储在内置存储中
	private File efFile;
	private float alpha;
	private int baikeCount = 0;
	private SharedPreferences shares;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private long lastUpdateTime;
	private SnowView flakeView;
	private ImageView title_city_manager;
	private ImageView title_three_day_aqi_trend;
	private GetThreeDayAqiTrendModelTask getThreeDayAqiTrendModelTask;
	private String[] cityArrays = new String[] {
//			"郑州", "开封", "洛阳", "平顶山", "安阳",
//			"鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口",
//			"驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县",
//			"鹿邑县", "新蔡县"
			"焦作", "修武县", "博爱县", "武陟县", "温县", "泌阳县", "孟州"
			};
	public static final String ADD_CITYCLICK = "CITY_CLICK";
	private int screenWidthh;
	private static final String BLURRED_IMG_PATH = "c_duoyun.png";
	private static final String[] BLURRED_IMG_PATHS = new String[] { "qin.png",
			"c_duoyun.png", "c_yu.png", "c_xue.png", "c_yin.png",
			"wu_first.png", "mai_first.png", "heiye.png", "heiye_yin.png",
			"polluction_you.png", "polluction_liang.png",
			"polluction_qingdu.png", "polluction_zhong.png",
			"polluction_zhongdu.png", "polluction_yanzhong.png" };
	private Bitmap bitmapDrawable, bitmapxuhuaDrawable;
	private XListView xlistView;
	private int screenHight;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.viewpager);
		flakeView = new SnowView(this);
		flakeView.setNumFlakes(8);
		context = this;
		shares = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		lastUpdateTime = shares.getLong("lastUpdateTime", 0);
		// if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System
		// .currentTimeMillis()) {
		lastUpdateTime = System.currentTimeMillis();
		SharedPreferences.Editor editor = shares.edit();
		if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();
			UpdateManagerUtil.getUpdateManager().checkAppNot(this, false);
			UpdateManagerUtil.getUpdateManager().checkAppUpdate(
					EnvironmentCurrentWeatherPullActivity.this, false);
		}
		// }
		File cacheDir = new File(getFilesDir() + "/weibao/cache");
		externalStorage = getFilesDir() + File.separator + "cache";
		efFile = new File(externalStorage);
		screenHight = ImageUtils.getScreenHeight(this);
		aCache = ACache.get(EnvironmentCurrentWeatherPullActivity.this,
				cacheDir);
		eACache = ACache
				.get(EnvironmentCurrentWeatherPullActivity.this, efFile);
		loader = ImageLoader.getInstance();
		http = new KjhttpUtils(this, cacheDir);
		bgChange = (TopCenterImageView) findViewById(R.id.weather_background_normal);
		ivSun = (ImageView) findViewById(R.id.ivSun);
		viewpager_jiaobiao = (TextView) findViewById(R.id.viewpager_jiaobiao);
		bgChange_blurred = (TopCenterImageView) findViewById(R.id.weather_background_blurred);
		bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
				.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
						R.drawable.polluction_you)));
		bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(ImageUtils
				.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
						R.drawable.filespolluction_you)));
		reheight = getDisplayHeight(this)
				- getResources().getDimensionPixelOffset(
						R.dimen.abs__action_bar_default_height);
		screenWidthh = ImageUtils.getScreenHeight(this);
		sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
		latitude = sharedPref.getString("CurrentCityLatitude", "0");
		longitude = sharedPref.getString("CurrentCityLongitude", "0");
		dingweicity = sharedPref.getString("dingweiCity", "");
		dingweiaddress = sharedPref.getString("xiangxidi", "");
		mApplication = WeiBaoApplication.getInstance();
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHalfWidth = metrics.widthPixels - 20;
		screenHalfheigh12 = metrics.heightPixels / 13;
		screenHalfheigh8 = metrics.heightPixels / 8;
		SpeechUser.getUser().login(EnvironmentCurrentWeatherPullActivity.this,
				null, null, SpeechConstant.APPID + "=52dcaf71", loginListener);
		registerMessageReceiver();
		mLocationClient = WeiBaoApplication.getInstance().getLocationClient();
		mLocationClient.registerLocationListener(mLocationListener);
		mApplication.setXiangxidizhi(dingweiaddress);
		mApplication.setCurrentCityLatitude(latitude);
		mApplication.setCurrentCityLongitude(longitude);
		initView();
		rain = (RainView) findViewById(R.id.rain);
		rain.LoadRainImage();
		// 获取当前屏幕的高和宽
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		rain.SetView(dm.heightPixels, dm.widthPixels);
		flLayout = (FrameLayout) findViewById(R.id.weather_background);
		int[] location = new int[2];
		title_city_name.getLocationOnScreen(location);
	}

	/*
	 * 负责做界面更新工作 ，实现下雨
	 */
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			rain.invalidate();
			sleep(50);
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	public void update() {
		rain.addRandomRain();
		mRedrawHandler.sleep(100);
	}

	private void initView() {
		title_three_day_aqi_trend = (ImageView) findViewById(R.id.title_three_day_aqi_trend);
		title_three_day_aqi_trend.setOnClickListener(this);
		title_city_manager = (ImageView) findViewById(R.id.title_city_manager);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		mSpUtil2 = SharedPreferencesUtil
				.getInstance(EnvironmentCurrentWeatherPullActivity.this);
		morenCity = mSpUtil2.get("morenCity", "焦作");
		mCityDB = mApplication.getCityDB();
		initcitys = mCityDB.queryBySqlReturnArrayListHashMap("select * from addcity");
		initcitys = selectCitys(initcitys);
		title_city_name = (TextView) findViewById(R.id.title_city_name);
		title_city_name.setText(initcitys.get(0).get("name").toString());
		String province = mCityDB.getprovicecity(initcitys.get(0).get("name").toString());
		String city = initcitys.get(0).get("name").toString();
		if (city.contains("自治州")) {
			city = mCityDB.getSuoSuo(city);
		}
		final String yujingurl = UrlComponent.getAlarmHistory(province, city);
		Exceedmodel model = aCache.getAsString(yujingurl);
		if (model == null || !model.isFlag()) {
			http.getString(yujingurl, 0, new DownGet() {

				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if (!arg0.equals("")) {
						JsonUtils.jsonAlarm(
								EnvironmentCurrentWeatherPullActivity.this,
								arg0);
					}
				}
			});
		}
		dds = initcitys.size() == 0 ? 1 : initcitys.size();

		title_location = (ImageView) findViewById(R.id.title_location);
		title_location.setOnClickListener(this);
		title_share = (ImageView) findViewById(R.id.title_share);
		title_city_manager.setOnClickListener(this);
		title_share.setOnClickListener(this);
		title_city_manager_layout = (LinearLayout) findViewById(R.id.title_city_manager_layout);
		title_city_manager_layout.setOnClickListener(this);
		aPager = (ViewPager) findViewById(R.id.enviewpager);
		aPager.setOnPageChangeListener(pageChangeListener);
		aPager.setOffscreenPageLimit(5);
		group = (LinearLayout) findViewById(R.id.mybottomviewgroup1);
		imageViews = new ImageView[dds];
		if (dds != 1) {
			viewpager_jiaobiao.setText(1 + "/" + dds);
		}
		for (int i = 0; i < dds; i++) {
			imageView = new ImageView(
					EnvironmentCurrentWeatherPullActivity.this);
			imageView.setLayoutParams(new LayoutParams(CommonUtil.dip2px(this,
					10), CommonUtil.dip2px(this, 10)));
			imageViews[i] = imageView;
			String imageUri;
			if (i == 0) {
				// 默认选中第一张图片
				imageUri = "drawable://" + R.drawable.page_indicator_focused;
				imageViews[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
			} else {
				imageUri = "drawable://" + R.drawable.page_indicator;
				imageViews[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.page_indicator));
			}
			group.addView(imageViews[i]);
		}
		for (int i = 0; i < dds; i++) {
			initPagerItem(i);
		}
		WeiBaoApplication.selectedCity = initcitys.get(0).get("name").toString();
		String citysuo = initcitys.get(0).get("name").toString();
		if (citysuo.contains("自治州")) {
			citysuo = mCityDB.getSuoSuo(citysuo);
		}
		final String urlcome = UrlComponent.currentWeatherGet(citysuo, "0", "0");
		Exceedmodel arg0come = aCache.getAsString(urlcome);
		if (arg0come.isFlag()) {
			priseData(arg0come.getData());
			weatherCurrent = JsonUtils.parseCurrentWeather(arg0come.getData());
			Message msg = Message.obtain();
			msg.arg1 = 2;
			msg.arg2 = posposition;
			msg.obj = weatherCurrent;
			EnvironmentCurrentWeatherPullActivity.this.handler.sendMessage(msg);
			String initcitysuUrl = "";
			if (!initcitys.get(0).get("name").toString().equals(dingweicity)) {
				initcitysuUrl = UrlComponent.currentWeatherGet(initcitys.get(0)
						.get("name").toString(), "0", "0");
			} else {
				String citysuos = initcitys.get(0).get("name").toString();
				if (citysuos.contains("自治州")) {
					citysuos = mCityDB.getSuoSuo(citysuos);
				}
				initcitysuUrl = UrlComponent.currentWeatherGet(citysuos,
						latitude, longitude);
			}
			http.getString(initcitysuUrl, 600, new DownGet() {
				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if (!arg0.equals("")) {
						priseData(arg0);
					}
				}
			});
		} else {
			String data = arg0come.getData();
			priseData(data);
			if (data != null) {
				priseData(data);
			}
			http.getString(urlcome, 600, new DownGet() {

				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if (!arg0.equals("")) {
						priseData(arg0);
					}
				}
			});
		}
		pageAdapter = new GuidePageAdapter();
		pageAdapter.bindData(views);
		aPager.setAdapter(pageAdapter);
		aPager.setCurrentItem(0);
	}

	class GuidePageAdapter extends PagerAdapter {
		private List<View> pageViews;

		void bindData(List<View> pageViews) {
			this.pageViews = pageViews;
		}

		public GuidePageAdapter(List<View> pageViews) {
			super();
			// TODO Auto-generated constructor stub
			this.pageViews = pageViews;
		}

		public GuidePageAdapter() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void setPrimaryItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			super.setPrimaryItem(container, position, object);
			// getNewsData(position);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			try {
				((ViewPager) arg0).removeView(pageViews.get(arg1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		public Parcelable saveState() {
			return null;
		}
	}

	private View initPagerItem(final int i) {
		View view = getLayoutInflater().inflate(R.layout.main, null);
		mHeaderView = LayoutInflater.from(this).inflate(
				R.layout.environment_current_weather_activity_item1, null);
		weatherlay = (LinearLayout) mHeaderView.findViewById(R.id.weatherlay);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		weatherlay.measure(w, h);
		int height = weatherlay.getMeasuredHeight();
		// qq形象
		int m = 50;
		if (screenWidth == 720) {
			m = 50;
		}
		screenHalfheighead = reheight + height + CommonUtil.dip2px(this, m) + 8;
		mHeaderView.setMinimumHeight(screenHalfheighead);
		// // 噪音
		// zaoshengImageView = (ImageView) mHeaderView
		// .findViewById(R.id.zaoyin_view);
		// zaoshengImageView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MobclickAgent.onEvent(
		// EnvironmentCurrentWeatherPullActivity.this,
		// "HJOpenNoisePanel");
		// Intent intent = new Intent(
		// EnvironmentCurrentWeatherPullActivity.this,
		// EnvironmentDrawDialActivity.class);
		// intent.putExtra("background", backgroundType);
		// startActivity(intent);
		// }
		// });

		// 天气、aqi相关
		// weather_quality = (TextView) mHeaderView
		// .findViewById(R.id.eweather_quality);
		ll_curr_weather = (LinearLayout) mHeaderView
				.findViewById(R.id.ll_curr_weather);
		// weather_pm25 = (TextView)
		// mHeaderView.findViewById(R.id.eweather_pm25);
		weather_layout1 = (RelativeLayout) mHeaderView
				.findViewById(R.id.head_weather_layout1);
		// xList 下拉框
		current_weather_xListView = (XListView) view
				.findViewById(R.id.current_weather_xListView);
		current_weather_xListView.addHeaderView(mHeaderView, null, true);
		current_weather_xListView
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						Message msg = Message.obtain();
						msg.arg1 = 4;
						msg.arg2 = posposition;
						EnvironmentCurrentWeatherPullActivity.this.handler
								.sendMessage(msg);
						String city = title_city_name.getText().toString();
						final String url;
						if (city.equals(dingweicity)) {
							url = UrlComponent.currentWeatherGet(city,latitude, longitude);
						} else {
							if (city.contains("自治州")) {
								city = mCityDB.getSuoSuo(city);
							}
							url = UrlComponent.currentWeatherGet(city, "0", "0");
						}
						http.getString(url, 0, new DownGet() {

							@Override
							public void downget(String arg0) {
								// TODO Auto-generated method stub
								if (arg0.equals("")) {
									((XListView) views
											.get(posposition)
											.findViewById(R.id.current_weather_xListView))
											.stopRefresh();
									ToastUtil.showShort(EnvironmentCurrentWeatherPullActivity.this,"请检查网络");
								} else {
									CurrentWeather weather = JsonUtils.parseCurrentWeather(arg0);
									weatherCurrent = weather;
									((XListView) views
											.get(posposition)
											.findViewById(R.id.current_weather_xListView))
											.stopRefresh();
									if (null == weather
											&& NetUtil
													.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
										((TextView) views
												.get(posposition)
												.findViewById(R.id.weather_time))
												.setText("无网络");
										bgChange.setBackgroundDrawable(new BitmapDrawable(
												ImageUtils
														.readBitmap(
																EnvironmentCurrentWeatherPullActivity.this,
																R.drawable.polluction_you)));
										bgChange_blurred
												.setBackgroundDrawable(new BitmapDrawable(
														ImageUtils
																.readBitmap(
																		EnvironmentCurrentWeatherPullActivity.this,
																		R.drawable.filespolluction_you)));
									} else {
										Message msg = Message.obtain();
										msg.arg1 = 2;
										msg.arg2 = posposition;
										msg.obj = weather;
										ToastUtil.showShort(context, "更新完毕");
										EnvironmentCurrentWeatherPullActivity.this.handler
												.sendMessage(msg);
										if (isContains(
												WeiBaoApplication.selectedCity,
												cityArrays)) {
											priseData(arg0);
										}
									}
								}
							}
						});

					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub

					}
				});
//		for (int j = 0; j < 1; j++) {
//			final int m1 = 1;
//			new Thread(new Runnable() {
//				File blurredImage = new File(getFilesDir()
//						+ BLURRED_IMG_PATHS[m1]);
//
//				@Override
//				public void run() {
//					int id = 0;
//					if (m1 == 0) {
//						id = R.drawable.qin;
//					} else if (m1 == 1) {
//						id = R.drawable.c_duoyun;
//					} else if (m1 == 2) {
//						id = R.drawable.c_yu;
//					} else if (m1 == 3) {
//						id = R.drawable.c_xue;
//					} else if (m1 == 4) {
//						id = R.drawable.c_yin;
//					} else if (m1 == 5) {
//						id = R.drawable.wu_first;
//					} else if (m1 == 6) {
//						id = R.drawable.mai_first;
//					} else if (m1 == 7) {
//						id = R.drawable.heiye;
//					} else if (m1 == 8) {
//						id = R.drawable.heiye_yin;
//					} else if (m1 == 9) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[9]);
//						id = R.drawable.polluction_you;
//					} else if (m1 == 10) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[10]);
//						id = R.drawable.polluction_liang;
//					} else if (m1 == 11) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[11]);
//						id = R.drawable.polluction_qingdu;
//					} else if (m1 == 12) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[12]);
//						id = R.drawable.polluction_zhong;
//					} else if (m1 == 13) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[13]);
//						id = R.drawable.polluction_zhongdu;
//					} else if (m1 == 14) {
//						blurredImage = new File(getFilesDir()
//								+ BLURRED_IMG_PATHS[14]);
//						id = R.drawable.polluction_yanzhong;
//					}
//					BitmapFactory.Options options = new BitmapFactory.Options();
//					options.inJustDecodeBounds = false;
//					options.inSampleSize = 2;
//					try {
//						Bitmap image = BitmapFactory.decodeResource(
//								getResources(), id, options);
//						Bitmap newImg = Blur.fastblur(
//								EnvironmentCurrentWeatherPullActivity.this,
//								image, 22);
//						ImageUtils.storeImage(newImg, blurredImage);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//
//				}
//			}).start();
//		}
		views.add(view);
		return view;
	}

	// 获取屏幕的高度
	public int getDisplayHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int displayHeight = wm.getDefaultDisplay().getHeight();
		return displayHeight;
	}

	BDLocationListener mLocationListener = new BDLocationListener() {

		// @Override
		// public void onReceivePoi(BDLocation arg0) {
		// }

		@Override
		public void onReceiveLocation(BDLocation location) {

			// MyLog.i("onReceiveLocation" + location);
			// if (location == null || TextUtils.isEmpty(location.getCity())) {
			// return;
			// }
			// // 获取当前城市，
			// // 获取当前城市，
			// latitude = location.getLatitude() + "";
			// longitude = location.getLongitude() + "";
			// mApplication.setCurrentCityLatitude(latitude);
			// mApplication.setCurrentCityLongitude(longitude);
			// dingweicity = location.getCity();
			// dingweiaddress = location.getAddrStr();
			// SharedPreferences.Editor editor;
			// // String cityName = location.getCity();
			// String xiangxidi = location.getAddrStr();
			// String province = location.getProvince();
			// String district = location.getDistrict();
			// if (dingweicity.endsWith("自治州")) {
			// dingweicity = district;
			// }
			// if (dingweicity.contains("市")) {
			// dingweicity = dingweicity
			// .substring(0, dingweicity.length() - 1);
			// }
			// if (dingweicity.contains("地区")) {
			// dingweicity = dingweicity
			// .substring(0, dingweicity.length() - 2);
			// }
			// mApplication.setCurrentCityLatitude(latitude + "");
			// mApplication.setCurrentCityLongitude(longitude + "");
			// // 把定位城市存入到缓存里
			// sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
			// editor = sharedPref.edit();
			// editor.putString("dingweiCity", dingweicity);
			// editor.putString("CurrentCityLongitude", longitude + "");
			// editor.putString("CurrentCityLatitude", latitude + "");
			// editor.putString("province", province);
			// editor.putString("xiangxidi", xiangxidi);
			// editor.commit();
			// mApplication.setXiangxidizhi(xiangxidi);
			// mApplication.setProvince(province);
			// if (!"".equals(dingweicity)) {
			// WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(),
			// false, dingweicity);
			// }
			// // 定位成功后发送广播
			// if (xiangxidi != null && !xiangxidi.equals("")) {
			// Intent intent = new Intent();
			// intent.setAction(EnvironmentCurrentWeatherPullActivity.DING_WEIService);
			// intent.putExtra("dingwei", xiangxidi);
			// intent.putExtra("city", dingweicity);
			// sendBroadcast(intent);
			// }
			// mLocationClient.stop();
			// //
			// City curCity = mCityDB.getCity(dingweicity);// 从数据库中找到该城市
			//
			// MyLog.i("curCity :" + curCity);
			// if (curCity != null) {
			// if (mCityDB.isHaveLocation()) {
			// mCityDB.deleteLocationCity();
			// }
			// // if (mCityDB.addCityExist(dingweicity)
			// // && mCityDB.addCityExistAndLocation(dingweicity)) {
			// // return;
			// // }
			// if (mCityDB.addCityExist(dingweicity)) {
			// mCityDB.updateLocation(dingweicity);
			// } else {
			// mCityDB.addXuanZhecity1(dingweicity, "", "", true);
			// }
			//
			// } else {// 如果定位到的城市数据库中没有，也弹出定位失败
			// // Toast.makeText(LoadingActivity.this, "请手动选择城市", 1).show();
			// return;
			// }
		}
	};

	SpeechListener loginListener = new SpeechListener() {
		// 若error 为null，表示登录成功，反之登录失败
		public void onCompleted(SpeechError error) {
		}

		// 登录操作服务端不会返回数据，忽略此接口
		public void onData(byte[] buffer) {
		}

		// 扩展消息，忽略此接口
		public void onEvent(int eventType, Bundle params) {
		}
	};

	private ArrayList<HashMap<String, Object>> selectCitys(ArrayList<HashMap<String, Object>> citys) {
		List<String> cityAll = new ArrayList<String>();
		for (int j = 0; j < citys.size(); j++) {
			cityAll.add(citys.get(j).get("name") + "");
		}
		for (int i = 0; i < cityArrays.length; i++) {
			if (!cityAll.contains(cityArrays[i])) {
				cityAll.add(cityArrays[i]);
				HashMap<String, Object> cityhaHashMap = new HashMap<String, Object>();
				cityhaHashMap.put("name", cityArrays[i]);
				cityhaHashMap.put("province", "河南");
				cityhaHashMap.put("number", "");
				cityhaHashMap.put("pinyin", "");
				cityhaHashMap.put("py", "");
				cityhaHashMap.put("lat", "");
				cityhaHashMap.put("lon", "");
				cityhaHashMap.put("islocation", "0");
				cityhaHashMap.put("temp", "");
				cityhaHashMap.put("climate", "");
				citys.add(cityhaHashMap);
			}

		}
		try {
			morenCity = mSpUtil2.get("morenCity", "焦作");
			if (null != citys && citys.size() > 0) {
				for (int i = 0; i < citys.size(); i++) {
					if ("1".equals(citys.get(i).get("islocation"))) {
						HashMap<String, Object> hashMapFirst = citys.get(0);
						HashMap<String, Object> hashMapLocation = citys.get(i);
						citys.set(0, hashMapLocation);
						citys.set(i, hashMapFirst);
					}
					if (morenCity.equals(citys.get(i).get("name"))) {
						HashMap<String, Object> hashMapFirst = citys.get(0);
						HashMap<String, Object> hashMapLocation = citys.get(i);
						citys.set(0, hashMapLocation);
						citys.set(i, hashMapFirst);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return citys;
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		filter.addAction(EnvironmentCityManagerActivity.ADD_ACTION);
		filter.addAction(EnvironmentCityManagerActivity.ADD_VIEW);
		filter.addAction(EnvironmentCurrentWeatherPullActivity.DING_WEIService);
		filter.addAction(LOCATIONCHANGEACTION);
		filter.addAction(ADD_CITYCLICK);
		filter.addAction(KEY_Login);
		// 网络状况不好的情况下广播
		filter.addAction(EnvironmentCurrentWeatherPullActivity.DING_WEI);
		filter.addAction(EnvironmentCityManagerActivity.LOCATION_CHANGE);
		// 网络状态切换时候的广播
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			} else if (DING_WEI.equals(intent.getAction())) {
				String dingwei = intent.getStringExtra("dingwei");
				String city = intent.getStringExtra("city");
				// if (city.equals(mTitleTextView.getText().toString())) {
				// dingweichengshi[pos].setVisibility(View.VISIBLE);
				// dingweichengshi[pos].setText(dingwei);
				// }
				// updateWeather1(city, "60000");
				if (null != city && city.equals(WeiBaoApplication.selectedCity)) {
					// LoadWeatherTask loadWeatherCity = new LoadWeatherTask();
					// loadWeatherCity.execute(city, "60000");
				}
				Intent UPintent = new Intent("com.mapuni.weibao.UPDATE");
				sendBroadcast(UPintent);
			} else if (EnvironmentCityManagerActivity.ADD_ACTION.equals(intent
					.getAction())) {

				if (intent.getExtras().getBoolean("bian")) {
					initcitys = (ArrayList<HashMap<String, Object>>) intent
							.getSerializableExtra("view");
					initcitys = selectCitys(initcitys);
					int ss = intent.getExtras().getInt("po");
					dongtai(ss, initcitys);
				} else {
					int ss = intent.getExtras().getInt("po");
					aPager.setCurrentItem(ss);
				}
			} else if (EnvironmentCityManagerActivity.ADD_VIEW.equals(intent
					.getAction())) {
				if (intent.getExtras().getBoolean("bian")) {
					initcitys = mCityDB
							.queryBySqlReturnArrayListHashMap("select * from addcity");
					initcitys = selectCitys(initcitys);
					for (int i = 0; i < initcitys.size(); i++) {
						ManageCity mCity = new ManageCity(initcitys.get(i)
								.get("name").toString(), initcitys.get(i)
								.get("climate").toString(), initcitys.get(i)
								.get("temp").toString());
						Log.i("bai", "mCity11111" + mCity.getCityName());
					}
					pos = getIntent().getIntExtra("posposition", 0);
					try {
						WeiBaoApplication.selectedCity = (String) initcitys
								.get(0).get("name");
						dongtai(pos, initcitys);
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
						try {
							initcitys.clear();
							initcitys = mCityDB
									.queryBySqlReturnArrayListHashMap("select * from addcity");
							initcitys = selectCitys(initcitys);
							for (int i = 0; i < initcitys.size(); i++) {
								ManageCity mCity = new ManageCity(initcitys
										.get(i).get("name").toString(),
										initcitys.get(i).get("climate")
												.toString(), initcitys.get(i)
												.get("temp").toString());
								Log.i("bai", "mCity2222" + mCity.getCityName());
							}
							dongtai(0, initcitys);

						} catch (Exception e2) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					int ss = intent.getExtras().getInt("posposition");
					aPager.setCurrentItem(ss);
				}

			} else if (EnvironmentCityManagerActivity.LOCATION_CHANGE
					.equals(intent.getAction())) {
				isLocationChange = true;
			} else if (intent.getAction().equals(KEY_Login)) {
				getNewsData(posposition, weather, null);
			} else if (intent.getAction().equals(ADD_CITYCLICK)) {
				String cityName = intent.getStringExtra("cityName");
				initcitys = mCityDB
						.queryBySqlReturnArrayListHashMap("select * from addcity");
				initcitys = selectCitys(initcitys);
				int position = 0;
				for (int i = 0; i < initcitys.size(); i++) {
					String city = (String) initcitys.get(i).get("name");
					if (city.equals(cityName)) {
						position = i;
						WeiBaoApplication.selectedCity = city;
						break;
					}
				}
				if (position == 0 && initcitys.size() > 0) {
					WeiBaoApplication.selectedCity = (String) initcitys.get(0)
							.get("name");
				}
				dds = initcitys.size();
				imageViews = new ImageView[dds];
				Message msg = Message.obtain();
				msg.arg2 = position;
				msg.arg1 = 1;
				handler.sendMessage(msg);
			} else if (EnvironmentCurrentWeatherPullActivity.LOCATIONCHANGEACTION
					.equals(intent.getAction())) {
				Message msg = Message.obtain();
				msg.arg1 = 1;
				initcitys = mCityDB
						.queryBySqlReturnArrayListHashMap("select * from addcity");
				initcitys = selectCitys(initcitys);
				latitude = sharedPref.getString("CurrentCityLatitude", "");
				longitude = sharedPref.getString("CurrentCityLongitude", "");
				dingweicity = sharedPref.getString("dingweiCity", "");
				dingweiaddress = sharedPref.getString("xiangxidi", "");
				handler.sendMessage(msg);
			} else if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")
					|| intent.getAction().equals(DING_WEI)) {
				State wifiState = null;
				State mobileState = null;
				ConnectivityManager cm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.getState();
				mobileState = cm
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
						.getState();
				if (wifiState != null && mobileState != null
						&& State.CONNECTED != wifiState
						&& State.CONNECTED == mobileState) {
					mLocationClient.requestLocation();
					LocationService
							.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
					// 手机网络连接成功
				} else if (wifiState != null && mobileState != null
						&& State.CONNECTED != wifiState
						&& State.CONNECTED != mobileState) {
					LocationService
							.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
					// 手机没有任何的网络
				} else if (wifiState != null && State.CONNECTED == wifiState) {
					// 无线网络连接成功
					mLocationClient.requestLocation();
					LocationService
							.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
				}
			} else if (intent.getAction().equals(DING_WEIService)) {
				String city = intent.getStringExtra("city");
				if (city.equals(WeiBaoApplication.getInstance()
						.getDingweicity())) {
					mApplication.setDingweicity(dingweicity);
					return;
				} else {
					mApplication.setDingweicity(dingweicity);
					Message msg = Message.obtain();
					msg.arg1 = 1;
					initcitys = mCityDB
							.queryBySqlReturnArrayListHashMap("select * from addcity");
					latitude = sharedPref.getString("CurrentCityLatitude", "");
					longitude = sharedPref
							.getString("CurrentCityLongitude", "");
					dingweicity = sharedPref.getString("dingweiCity", "");
					dingweiaddress = sharedPref.getString("xiangxidi", "");
					initcitys = selectCitys(initcitys);
					handler.sendMessage(msg);
				}
			}
		}
	}

	private void setCostomMsg(String msg) {
		if (null != msgText) {
			msgText.setText(msg);
			msgText.setVisibility(android.view.View.VISIBLE);
		}
	}

	void dongtai(int ds, ArrayList<HashMap<String, Object>> initcity) {
		Message msg = Message.obtain();
		msg.arg1 = 1;
		msg.arg2 = ds;
		initcitys = initcity;
		handler.sendMessage(msg);
	}

	class GetCurrentWeatherByDBTask extends
			AsyncTask<String, Void, CurrentWeather> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			TextView textView = (TextView) (views.get(posposition)
					.findViewById(R.id.weather_time));
			textView.setText("正在更新");
		}

		@Override
		protected CurrentWeather doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			CurrentWeather _Result = null;
			try {
				_Result = search.getCurrentWeatherByDb(params[0], params[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(CurrentWeather result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			((XListView) views.get(posposition).findViewById(
					R.id.current_weather_xListView)).stopRefresh();
			if (null == result
					&& NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
				((TextView) views.get(posposition).findViewById(
						R.id.weather_time)).setText("无网络");
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.polluction_you)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filespolluction_you)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} else if (null == result) {
				return;
			} else {
				Message msg = Message.obtain();
				msg.arg1 = 2;
				msg.arg2 = posposition;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}
	}

	private void getNewsYuCeData(int position,
			List<ThreeDayAqiTrendModel> resultt) {
		View view = views.get(position);
		TextView yuce_key = (TextView) view.findViewById(R.id.yuce_key);
		TextView yuce_value = (TextView) view.findViewById(R.id.yuce_value);
		TextView yuce_valuee = (TextView) view.findViewById(R.id.yuce_valuee);
		TextView aiq_detail = (TextView) view.findViewById(R.id.aiq_detail);
		LinearLayout ell_curr_weather = (LinearLayout) view
				.findViewById(R.id.ell_curr_weather);
		if (resultt == null) {
			ell_curr_weather.setVisibility(View.INVISIBLE);
		} else {
			ell_curr_weather.setVisibility(View.VISIBLE);
		}
		if (resultt != null) {
			yuce_key.setVisibility(View.VISIBLE);
			yuce_value.setVisibility(View.VISIBLE);
			aiq_detail.setText(resultt.get(0).getAqi());
			ell_curr_weather.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(
							EnvironmentCurrentWeatherPullActivity.this,
							KongqizhiliangyubaoActivity.class);
					intent.putExtra("city", WeiBaoApplication.selectedCity);
					startActivity(intent);
				}
			});
			int m = resultt.get(0).getMINAIRLEVEL();
			int n = resultt.get(0).getMAXAIRLEVEL();
			String yuceDengji = "";
			String yuceDengjii = "";
			if (m == 1) {
				yuceDengji = "优";
			} else if (m == 2) {
				yuceDengji = "良";
			} else if (m == 3) {
				yuceDengji = "轻度污染";
			} else if (m == 4) {
				yuceDengji = "中度污染";
			} else if (m == 5) {
				yuceDengji = "重度污染";
			} else if (m == 6) {
				yuceDengji = "严重污染";
			} else {
				yuceDengji = "优";
			}
			if (n == 1) {
				yuceDengjii = "优";
			} else if (n == 2) {
				yuceDengjii = "良";
			} else if (n == 3) {
				yuceDengjii = "轻度污染";
			} else if (n == 4) {
				yuceDengjii = "中度污染";
			} else if (n == 5) {
				yuceDengjii = "重度污染";
			} else if (n == 6) {
				yuceDengjii = "严重污染";
			} else {
				yuceDengjii = "优";
			}
			yuce_value.setText(yuceDengji);
			yuce_value.setBackgroundResource(getYuCeAqiDrawable(m));
			yuce_valuee.setBackgroundResource(getYuCeAqiDrawable(n));
			yuce_valuee.setText(yuceDengjii);
		} else {
			yuce_key.setVisibility(View.GONE);
			yuce_value.setVisibility(View.GONE);
			yuce_valuee.setVisibility(View.GONE);
		}

	}

	private void getNewsYuCeData(int position, MainAqiData resultt) {
		View view = views.get(position);
		RelativeLayout aqi_relayout = (RelativeLayout) view
				.findViewById(R.id.aqi_relayout);
		LinearLayout ell_curr_weather = (LinearLayout) view
				.findViewById(R.id.ell_curr_weather);
		TextView yuce_key = (TextView) view.findViewById(R.id.yuce_key);
		TextView yuce_value = (TextView) view.findViewById(R.id.yuce_value);
		TextView yuce_valuee = (TextView) view.findViewById(R.id.yuce_valuee);
		TextView aiq_detail = (TextView) view.findViewById(R.id.aiq_detail);
		TextView fabutime = (TextView) view.findViewById(R.id.fabutime);
		TextView aiq_detail_value = (TextView) view
				.findViewById(R.id.aiq_detail_value);

		TextView activity_first_polluction = (TextView) view
				.findViewById(R.id.activity_first_polluction);
		ImageView qq_luo = (ImageView) view.findViewById(R.id.qq_luo);
		TextView tishi_key = (TextView) view.findViewById(R.id.tishi_key);
		TextView tishi_value = (TextView) view.findViewById(R.id.tishi_value);
		if (resultt == null) {
			aqi_relayout.setVisibility(View.INVISIBLE);
			ell_curr_weather.setVisibility(View.INVISIBLE);
		} else {
			aqi_relayout.setVisibility(View.VISIBLE);
			ell_curr_weather.setVisibility(View.VISIBLE);
		}
		if (resultt != null) {
			yuce_key.setVisibility(View.VISIBLE);
			yuce_value.setVisibility(View.VISIBLE);
			fabutime.setText(resultt.getUpdatetime() + "");
			aiq_detail.setText(resultt.getAqi() + "");
			String level = CommonUtil.getDengJiByAQII(resultt.getAqi() + "");
			String first_polluction = resultt.getPrimary();
			if (first_polluction != null && !first_polluction.equals("")) {
				activity_first_polluction.setVisibility(View.VISIBLE);
				activity_first_polluction.setText("首要污染物:" + first_polluction);
			} else {
				activity_first_polluction.setVisibility(View.GONE);
			}
			aiq_detail_value.setText(CommonUtil.getDengJiByAQII(resultt
					.getAqi() + ""));
			tishi_value.setVisibility(View.VISIBLE);
			qq_luo.setVisibility(View.VISIBLE);
			if (level.contains("优")) {
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img1));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_you)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
						ImageUtils.readBitmap(
								EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_you)));
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("空气很好，可以外出活动，呼吸新鲜空气。");
			} else if (level.contains("良")) {
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img2));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_liang)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
						ImageUtils.readBitmap(
								EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_liang)));
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("可以正常在户外活动，易敏感人群应减少外出。");
			} else if (level.contains("轻度")) {
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img3));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_qingdu)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
						ImageUtils.readBitmap(
								EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_qingdu)));
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("敏感人群症状易加剧，应避免高强度户外锻炼，外出时做好防护措施。");
			} else if (level.contains("中度")) {
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img4));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_zhong)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
						ImageUtils.readBitmap(
								EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_zhong)));
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("应减少户外活动，外出时佩戴口罩，敏感人群应尽量避免外出。");
			} else if (level.contains("重度")) {
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img5));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_zhongdu)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_zhongdu)));
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("应减少户外活动，外出时佩戴口罩，敏感人群应留在室内。");
			} else {
				tishi_key.setVisibility(View.VISIBLE);
				tishi_key.setText("温馨提示:");
				tishi_value.setText("应避免外出，敏感人群应留在室内，关好门窗。");
				qq_luo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_img6));
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils
						.readBitmap(EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.polluction_yanzhong)));
				bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
						ImageUtils.readBitmap(
								EnvironmentCurrentWeatherPullActivity.this,
								R.drawable.filespolluction_yanzhong)));
			}
			try {
				if (resultt.getAqi() > 0) {
					aiq_detail_value
							.setBackgroundResource(getAqiDrawable(resultt
									.getAqi() + ""));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ell_curr_weather.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(
							EnvironmentCurrentWeatherPullActivity.this,
							KongqizhiliangyubaoActivity.class);
					intent.putExtra("city", WeiBaoApplication.selectedCity);
					startActivity(intent);
				}
			});
			List<ThreeDayAqiTrendModel> trendModels = resultt.getForecast();
			if (trendModels == null || trendModels.size() == 0) {
				yuce_key.setVisibility(View.GONE);
				yuce_value.setVisibility(View.GONE);
				yuce_valuee.setVisibility(View.GONE);
			} else {
				int m = resultt.getForecast().get(0).getMINAIRLEVEL();
				int n = resultt.getForecast().get(0).getMAXAIRLEVEL();
				String yuceDengji = "";
				String yuceDengjii = "";
				if (m == 1) {
					yuceDengji = "优";
				} else if (m == 2) {
					yuceDengji = "良";
				} else if (m == 3) {
					yuceDengji = "轻度污染";
				} else if (m == 4) {
					yuceDengji = "中度污染";
				} else if (m == 5) {
					yuceDengji = "重度污染";
				} else if (m == 6) {
					yuceDengji = "严重污染";
				} else {
					yuceDengji = "优";
				}
				if (n == 1) {
					yuceDengjii = "优";
				} else if (n == 2) {
					yuceDengjii = "良";
				} else if (n == 3) {
					yuceDengjii = "轻度污染";
				} else if (n == 4) {
					yuceDengjii = "中度污染";
				} else if (n == 5) {
					yuceDengjii = "重度污染";
				} else if (n == 6) {
					yuceDengjii = "严重污染";
				} else {
					yuceDengjii = "优";
				}
				yuce_value.setText(yuceDengji);
				yuce_value.setBackgroundResource(getYuCeAqiDrawable(m));
				yuce_valuee.setBackgroundResource(getYuCeAqiDrawable(n));
				yuce_valuee.setText(yuceDengjii);
			}

		} else {
			yuce_key.setVisibility(View.GONE);
			yuce_value.setVisibility(View.GONE);
			yuce_valuee.setVisibility(View.GONE);
		}

	}

	private void getNewsData(int position, CurrentWeather result,
			List<ThreeDayAqiTrendModel> resultt) {
		View view = views.get(position);
		try {
			// TODO Auto-generated method stub
			if (position != aPager.getCurrentItem()) {
				return;
			}
			String pagerCurrentCity = initcitys.get(aPager.getCurrentItem())
					.get("name").toString().replace("县", "").replace("市", "");
			String loadingUrlCity = result.getSweather().getCity()
					.replace("县", "").replace("市", "");
			try {
				if (null == result || false == result.isFlag()) {
					return;
				}
				Sweather wether = result.getSweather();
				String weather = wether.getWeather();
				String firstweather = "";
				firstweather = weather;
				Time t = new Time();
				t.setToNow();
				int hour = t.hour;
				xlistView = (XListView) view
						.findViewById(R.id.current_weather_xListView);
				xlistView.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub
						try {
							View c = xlistView.getChildAt(0);
							alpha = (float) -c.getTop() / (float) screenHight;
							if (alpha > 1 || alpha == 0) {
								MyLog.i(">>>>>alphapha" + true);
								alpha = 1;
								if (bgChange.getVisibility() == View.VISIBLE) {
									return;
								}
								bgChange.setVisibility(View.VISIBLE);
								bgChange_blurred.setVisibility(View.INVISIBLE);
							} else {
								MyLog.i(">>>>>alphapha" + false);
								if (bgChange.getVisibility() == View.INVISIBLE) {
									return;
								}
								bgChange.setVisibility(View.INVISIBLE);
								bgChange_blurred.setVisibility(View.VISIBLE);
							}
							// bgChange_blurred.setAlpha(alpha);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});
				String climate = CommonUtil.getWeatherIconString(firstweather,
						0);
				if (firstweather.length() > 10) {
					firstweather = CommonUtil.getWeatherIconString(
							firstweather, 1);
				}
				final TextView aiq_detail_value = (TextView) view
						.findViewById(R.id.aiq_detail_value);
				final TextView aiq_detail = (TextView) view
						.findViewById(R.id.aiq_detail);
				Paint mp = aiq_detail.getPaint();
				mp.setTypeface(Typeface.MONOSPACE);
				mp.setStrokeWidth(1);
				// aiq_detail.setPaintFlags(Typeface.MONOSPACE);
				// final TextView weather_time1 = (TextView) view
				// .findViewById(R.id.weather_time);
				final LinearLayout aqi_layout = (LinearLayout) view
						.findViewById(R.id.aqi_layout);
				final RelativeLayout aqi_relayout = (RelativeLayout) view
						.findViewById(R.id.aqi_relayout);
				TextView weather_climate_image = (TextView) view
						.findViewById(R.id.weather_climate_image);
				TextView weather_date1 = (TextView) view
						.findViewById(R.id.weather_date);
				TextView weather_today1 = (TextView) view
						.findViewById(R.id.weather_today);
				TextView weather_lunar1 = (TextView) view
						.findViewById(R.id.weather_lunar);
				TextView weather_temperature1 = (TextView) view
						.findViewById(R.id.weather_temperature);
				TextView weather_climate1 = (TextView) view
						.findViewById(R.id.weather_climate);
				TextView tishi_key = (TextView) view
						.findViewById(R.id.tishi_key);
				TextView tishi_value = (TextView) view
						.findViewById(R.id.tishi_value);
				// ImageView tucao = (ImageView) view.findViewById(R.id.tucao);
				final TextView warning_text = (TextView) view
						.findViewById(R.id.warning_text);
				final TextView warning_count = (TextView) view
						.findViewById(R.id.warning_count);
				final RelativeLayout warning_bg = (RelativeLayout) view
						.findViewById(R.id.warning_bg);

				warning_bg.setTag(position);
				warning_count.setTag(position);
				// final TextView eweather_pm251 = (TextView) view
				// .findViewById(R.id.eweather_pm25);
				// final TextView eweather_quality1 = (TextView) view
				// .findViewById(R.id.eweather_quality);
				TextView weather_temp1 = (TextView) view
						.findViewById(R.id.weather_temp);
				// TextView item1_currenttv = (TextView) view
				// .findViewById(R.id.item1_currenttv);
				TextView weather_wind_direction1 = (TextView) view
						.findViewById(R.id.weather_wind_direction);
				TextView fabutime = (TextView) view.findViewById(R.id.fabutime);
				ImageView windDirectionImg = (ImageView) view
						.findViewById(R.id.weather_wind_direction_img);
				TextView weather_sidu1 = (TextView) view
						.findViewById(R.id.weather_sidu);
				// LinearLayout ll_curr_weather1 = (LinearLayout) view
				// .findViewById(R.id.ell_curr_weather);
				ImageView current_weather_imy = (ImageView) view
						.findViewById(R.id.current_weather_imy);
				// ImageView current_weather_imyy = (ImageView) view
				// .findViewById(R.id.current_weather_imyy);
				// 首页qq形象
				qq_xingxiang = (FrameLayout) view
						.findViewById(R.id.qq_xingxiang);
				qq_luo = (ImageView) view.findViewById(R.id.qq_luo);
				// weather_climate1.setText("今日预报:" + firstweather);

				// tucao.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// openActivity(DiscoverBlogListActivity.class);
				// }
				// });
				aqi_relayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LocationService
								.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
						MobclickAgent.onEvent(
								EnvironmentCurrentWeatherPullActivity.this,
								"HJLifeIdxClick");

						// Intent intent = new Intent(
						// EnvironmentCurrentWeatherPullActivity.this,
						// EnvironmentAQIDetailMoreActivity.class);
						String nowcity = title_city_name.getText().toString();
						Intent intent;
						if (isContains(nowcity, cityArrays)) {
							intent = new Intent(
									EnvironmentCurrentWeatherPullActivity.this,
									EnvironmentWeatherRankkActivity.class);
						} else {
							intent = new Intent(
									EnvironmentCurrentWeatherPullActivity.this,
									EnvironmentWeatherRankActivity.class);
						}
						if (dingweicity.equals(nowcity)) {
							latitude = WeiBaoApplication.getInstance()
									.getCurrentCityLatitude();
							longitude = WeiBaoApplication.getInstance()
									.getCurrentCityLongitude();
							dingweiaddress = WeiBaoApplication.getInstance()
									.getXiangxidizhi();

							intent.putExtra("lat", latitude + "");
							intent.putExtra("lng", longitude + "");
							intent.putExtra("dingweistr", dingweiaddress);
						} else {
							intent.putExtra("lat", 0 + "");
							intent.putExtra("lng", 0 + "");
						}
						// intent.putExtra("pm25",
						// aiq_detail.getText().toString());
						// intent.putExtra("level", aiq_detail_value.getText()
						// .toString());
						try {
							intent.putExtra("pm25", aiq_detail.getText() + "");
							intent.putExtra(
									"level",
									CommonUtil.getDengJiByAQII(aiq_detail
											.getText() + ""));
							// intent.putExtra("pm25", weatherCurrent
							// .getSweather().getPM2Dot5Data());
							// intent.putExtra("level", weatherCurrent
							// .getSweather().getLevel());
							intent.putExtra("city", nowcity);
							startActivity(intent);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});
				fabutime.setText(wether.getRealTime());
				weather_climate1.setText("" + firstweather);
				current_weather_imy.setImageDrawable(getResources()
						.getDrawable(getWeatherIcon(climate)));
				// weather_time1.setText(wether.getRealTime());
				weather_date1.setText(wether.getDate());
				weather_today1.setText(wether.getWeekday());
				weather_lunar1.setText(wether.getLunar());
				int length = climate.length();
				if (null == wether.getFeelTemp()
						|| "--".equals(wether.getFeelTemp())) {
					wether.setFeelTemp("--");
					weather_climate_image.setVisibility(View.GONE);
				} else if (null != wether.getFeelTemp()
						&& wether.getFeelTemp().contains("℃")
						&& wether.getFeelTemp().length() >= 2) {
					wether.setFeelTemp(wether.getFeelTemp().substring(0,
							wether.getFeelTemp().length() - 1));
					weather_climate_image.setVisibility(View.VISIBLE);
				}
				weather_temperature1.setText(wether.getFeelTemp());
				int nearPm25Value = -1;
				String qq_aqi = wether.getLevel_near();
				if (qq_aqi == null || qq_aqi.equals("")) {
					qq_aqi = wether.getLevel();
				}
				int Pm25Value = -1;
				try {
					nearPm25Value = Integer.parseInt(wether
							.getPM2Dot5Data_near());
				} catch (Exception e) {
				}
				try {
					Pm25Value = Integer.parseInt(wether.getPM2Dot5Data());
				} catch (Exception e) {
				}
				if (nearPm25Value > 0) {
					aqi_layout.setVisibility(View.VISIBLE);
					// weather_nearlocation
					// .setText(wether.getPosition_name_near());
					aiq_detail.setText(nearPm25Value + "");
					aiq_detail_value.setText(qq_aqi);
					// aiq_detail_value
					// .setBackgroundResource(getAqiDrawable(wether
					// .getPM2Dot5Data_near()));
					// aiq_detail_value
					// .setBackgroundResource(getYuCeAqiDrawable(wether
					// .getPM2Dot5Data_near()));
					try {
						aiq_detail_value
								.setBackgroundResource(getAqiDrawable(wether
										.getPM2Dot5Data()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					// getYuCeAqiDrawable(resultt.getAqi())
				} else if (Pm25Value > 0) {
					aqi_layout.setVisibility(View.VISIBLE);
					// weather_nearlocation.setText("");
					aiq_detail.setText(Pm25Value + "");
					aiq_detail_value.setText(qq_aqi);
					// aiq_detail_value
					// .setBackgroundResource(getAqiDrawable(wether
					// .getPM2Dot5Data()));
					try {
						aiq_detail_value
								.setBackgroundResource(getAqiDrawable(wether
										.getPM2Dot5Data()));
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					aiq_detail.setText("--");
				}
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				java.util.Date currTime = new java.util.Date();
				final String curTime = formatter.format(currTime);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						AlarmDao dao = new AlarmDao(
								EnvironmentCurrentWeatherPullActivity.this);
						final List<ModelAlarmHistory> modelList = dao
								.getContactList(WeiBaoApplication.selectedCity,
										curTime);
						if (modelList.size() == 0) {
							warning_bg.setVisibility(View.INVISIBLE);
						} else {
							warning_bg.setVisibility(View.VISIBLE);
							warning_text.setVisibility(View.VISIBLE);
							warning_count.setText(modelList.size() + "");
							warning_bg
									.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent();
											String selectedCity = WeiBaoApplication.selectedCity;
											intent.setClass(
													EnvironmentCurrentWeatherPullActivity.this,
													EnvironmentWarmHistoryActivity.class);
											intent.putExtra("city",
													selectedCity);
											intent.putParcelableArrayListExtra(
													"modelList",
													(ArrayList<? extends Parcelable>) modelList);
											AlarmDao dao = new AlarmDao(
													EnvironmentCurrentWeatherPullActivity.this);
											dao.update(selectedCity);
											warning_bg
													.setVisibility(View.INVISIBLE);
											startActivity(intent);
										}
									});
						}
					}
				});

				// eweather_quality1.setText(wether.getLevel());
				weather_temp1.setText(wether.getTemp());
				String direction = wether.getWindDirection().contains("0级") ? "无风"
						: wether.getWindDirection();
				weather_wind_direction1.setText(direction);
				// 根据风向旋转图片方向
				if (wether.getWindDirection().contains("东风")) {
					rotateImg(false, windDirectionImg, 180);

				} else if (wether.getWindDirection().contains("东南风")) {
					rotateImg(true, windDirectionImg, 90);

				} else if (wether.getWindDirection().contains("西南风")) {
					rotateImg(true, windDirectionImg, 180);
				} else if (wether.getWindDirection().contains("南风")) {
					rotateImg(false, windDirectionImg, -90);
				} else if (wether.getWindDirection().contains("西风")) {
					rotateImg(false, windDirectionImg, 0);
				} else if (wether.getWindDirection().contains("西北风")) {
					rotateImg(true, windDirectionImg, -90);
				} else if (wether.getWindDirection().contains("东北风")) {
					rotateImg(true, windDirectionImg, 0);
				} else if (wether.getWindDirection().contains("北风")) {
					rotateImg(false, windDirectionImg, 90);
				}
				weather_sidu1.setText("湿度" + wether.getSD());
				// 点击企鹅形象出现签到，抽奖
				i = 1;
				userId = WeiBaoApplication.getUserId();
				String qiandaoIsUrl = UrlComponent.getIsRegister(userId);
				final KjhttpUtils http = new KjhttpUtils(
						EnvironmentCurrentWeatherPullActivity.this, null);
				http.getString(qiandaoIsUrl, 0, new DownGet() {

					@Override
					public void downget(String arg0) {
						// TODO Auto-generated method stub
						if (!arg0.equals("")) {
							JSONObject object;
							try {
								object = new JSONObject(arg0);
								String flag = object.getString("flag");
								if (flag.equals("true")) {
									lottery = object.getString("lottery");
									check = object.getString("check");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								i = 3;
							}
							if (check.equals("0")) {
								final String qiandaoUrl = UrlComponent
										.getRegister(WeiBaoApplication
												.getUserId());
								http.getString(qiandaoUrl, 0, new DownGet() {

									@Override
									public void downget(String arg0) {
										// TODO Auto-generated method stub
										try {
											String flag = new JSONObject(arg0)
													.getString("flag");
											MobclickAgent
													.onEvent(
															EnvironmentCurrentWeatherPullActivity.this,
															"HJHere");
											if (flag.equals("true")) {
												check = "1";
												ToastUtil
														.showShort(
																EnvironmentCurrentWeatherPullActivity.this,
																"签到成功!微保币+10");
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}
							i = check.equals("") ? (lottery.equals("") ? 3
									: (lottery.equals("0") ? 2 : 3)) : (check
									.equals("0") ? 1 : (lottery.equals("") ? 3
									: (lottery.equals("0") ? 2 : 3)));
							qq_xingxiang
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											MobclickAgent
													.onEvent(
															EnvironmentCurrentWeatherPullActivity.this,
															"HJClickXiang");
											if (popupWindow != null
													&& popupWindow.isShowing()) {
												popupWindow.dismiss();
												return;
											}
											isCheck(lottery, check,
													qq_xingxiang);
										}
									});
						} else {
							i = 3;
							qq_xingxiang
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											MobclickAgent
													.onEvent(
															EnvironmentCurrentWeatherPullActivity.this,
															"HJClickXiang");
											if (popupWindow != null
													&& popupWindow.isShowing()) {
												popupWindow.dismiss();
												return;
											} else {
												isCheck(lottery, check,
														qq_xingxiang);
											}
										}

									});
						}
					}
				});

				// ll_curr_weather1.setOnClickListener(new OnClickListener() {

				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// LocationService
				// .sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
				// MobclickAgent.onEvent(
				// EnvironmentCurrentWeatherPullActivity.this,
				// "HJLifeIdxClick");
				// Intent intent = new Intent(
				// EnvironmentCurrentWeatherPullActivity.this,
				// EnvironmentWeatherRankActivity.class);
				//
				// String nowcity = title_city_name.getText().toString();
				// if (dingweicity.equals(nowcity)) {
				//
				// latitude = WeiBaoApplication.getInstance()
				// .getCurrentCityLatitude();
				// longitude = WeiBaoApplication.getInstance()
				// .getCurrentCityLongitude();
				// dingweiaddress = WeiBaoApplication.getInstance()
				// .getXiangxidizhi();
				//
				// intent.putExtra("lat", latitude + "");
				// intent.putExtra("lng", longitude + "");
				// intent.putExtra("dingweistr", dingweiaddress);
				// } else {
				// intent.putExtra("lat", 0 + "");
				// intent.putExtra("lng", 0 + "");
				// }
				// intent.putExtra("pm25", aiq_detail.getText()
				// .toString());
				// intent.putExtra("level", aiq_detail_value.getText()
				// .toString());
				// intent.putExtra("city", nowcity);
				// startActivity(intent);
				// }
				// });
				// 生活圈初始化

				// 温度变化趋势

				// try {
				// int temp_qq = Integer.parseInt(weather_temperature1
				// .getText().toString());
				// if (temp_qq <= 0) {
				// qq_dongfu.setVisibility(View.VISIBLE);
				// qq_qiufu.setVisibility(View.GONE);
				// qq_cunfu.setVisibility(View.GONE);
				// qq_xiafu.setVisibility(View.GONE);
				// } else if (temp_qq > 0 && temp_qq <= 10) {
				// qq_qiufu.setVisibility(View.VISIBLE);
				// qq_dongfu.setVisibility(View.GONE);
				// qq_cunfu.setVisibility(View.GONE);
				// qq_xiafu.setVisibility(View.GONE);
				// } else if (temp_qq > 10 && temp_qq <= 25) {
				// qq_cunfu.setVisibility(View.VISIBLE);
				// qq_dongfu.setVisibility(View.GONE);
				// qq_xiafu.setVisibility(View.GONE);
				// qq_qiufu.setVisibility(View.GONE);
				// } else if (temp_qq > 25) {
				// qq_xiafu.setVisibility(View.VISIBLE);
				// qq_dongfu.setVisibility(View.GONE);
				// qq_cunfu.setVisibility(View.GONE);
				// qq_qiufu.setVisibility(View.GONE);
				// }
				// } catch (Exception e) {
				// }
				qq_aqi = result.getSweather().getLevel();
				qq_luo.setVisibility(View.GONE);
				if (qq_aqi.contains("优")) {
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img1));
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("空气很好，可以外出活动，呼吸新鲜空气。");
				} else if (qq_aqi.contains("良")) {
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img2));
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("可以正常在户外活动，易敏感人群应减少外出。");
				} else if (qq_aqi.contains("轻度")) {
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img3));
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("敏感人群症状易加剧，应避免高强度户外锻炼，外出时做好防护措施。");
				} else if (qq_aqi.contains("中度")) {
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img4));
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("应减少户外活动，外出时佩戴口罩，敏感人群应尽量避免外出。");
				} else if (qq_aqi.contains("重度")) {
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img5));
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("应减少户外活动，外出时佩戴口罩，敏感人群应留在室内。");
				} else {
					tishi_key.setVisibility(View.VISIBLE);
					tishi_key.setText("温馨提示:");
					tishi_value.setText("应避免外出，敏感人群应留在室内，关好门窗。");
					qq_luo.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_img6));
				}
				qq_luo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(
								EnvironmentCurrentWeatherPullActivity.this,
								"HJClickXiang");
						if (popupWindow != null && popupWindow.isShowing()) {
							popupWindow.dismiss();
							return;
						}
						isCheck(lottery, check, qq_luo);
					}
				});
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isCheck(String lotteryinfo, String checkinfo, final View v) {
		if (NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
			return;
		}

		String userId = WeiBaoApplication.getUserId();
		if (userId.equals("")) {
			i = 3;
		}
		final int[] location = new int[2];
		v.getLocationOnScreen(location);
		final int x = location[0];
		final int y = location[1];
		final int n = v.getBottom() - v.getTop();
		final int m = v.getRight() - v.getLeft();
		initmPopupWindowBaikeView();
		if (popupWindow == null)
			return;
		popupWindow.setFocusable(true);
		String city = WeiBaoApplication.selectedCity;
		String baiUrl = UrlComponent.getBaike(city);
		http.getString(baiUrl, 0, new DownGet() {

			@Override
			public void downget(String arg0) {
				// TODO Auto-generated method stub
				if (!arg0.equals("")) {
					try {
						JSONObject object = new JSONObject(arg0);
						String content = "";
						content = object.getString("flag").equals("true") ? object
								.getString("content") : "";
						baike.setText(content);
						if (popupWindow == null)
							return;
						popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x - n
								/ 3, y + m);
						popupWindow.setFocusable(true);
						// if (object.getString("flag").equals("true")) {
						// popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x
						// + m, y - n / 3);
						// }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		// MyLog.i(">>>>>>>>>isCheck" + m + ">>>>>>n" + n);
		// i = 3;
		// if (i == 1 && !userId.equals("")) {
		// i++;
		// if (check.equals("1")) {
		// } else {
		// initmPopupWindowView();
		// if (popupWindow == null)
		// return;
		// popupWindow.setFocusable(true);
		// MyLog.i(">>>>>popwindow1" + popupWindow.getHeight());
		// popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x + m, y - n
		// / 3);
		// final String qiandaoUrl = UrlComponent
		// .getRegister(WeiBaoApplication.getUserId());
		// qiandao_btn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (CommonUtil.isFastDoubleClick()) {
		// Toast.makeText(
		// EnvironmentCurrentWeatherPullActivity.this,
		// "请不要心急，稍等一下吧！", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if (popupWindow != null & popupWindow.isShowing()) {
		// popupWindow.dismiss();
		// }
		// }
		// });
		// return;
		// }
		//
		// }
		// if (i == 2 && !userId.equals("")) {
		//
		// i++;
		// if (lottery.equals("1")) {
		// } else {
		// initChoujiangmPopupWindowView();
		// if (popupWindow == null)
		// return;
		// popupWindow.setFocusable(true);
		// MyLog.i(">>>>>popwindow2" + popupWindow.getHeight());
		// popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x + m, y - n
		// / 3);
		// choujiangFlag = false;
		// choujiangintro.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (!choujiangFlag) {
		// choujiangFlag = true;
		// choujiangintro.setText("点击抽奖");
		// choujiang_title.setVisibility(View.INVISIBLE);
		// choujiang_shop.setVisibility(View.INVISIBLE);
		// chou[0].setBackgroundResource(R.drawable.fra_intro_img12);
		// chou[1].setBackgroundResource(R.drawable.fra_intro_img11);
		// chou[2].setBackgroundResource(R.drawable.fra_intro_img10);
		// chou[3].setBackgroundResource(R.drawable.fra_intro_img9);
		// chou[4].setBackgroundResource(R.drawable.fra_intro_img8);
		// chou[5].setBackgroundResource(R.drawable.fra_intro_img7);
		// chou[6].setBackgroundResource(R.drawable.fra_intro_img6);
		// chou[7].setBackgroundResource(R.drawable.fra_intro_img5);
		// } else {
		// choujiangintro.setText("奖品介绍");
		// choujiang_title.setVisibility(View.VISIBLE);
		// choujiang_shop.setVisibility(View.VISIBLE);
		// for (int i = 0; i < 8; i++) {
		// chou[i].setBackgroundResource(R.drawable.choujiang);
		// }
		// choujiangFlag = false;
		// }
		// }
		// });
		// return;
		// }
		// }
		// if (i == 3 || userId.equals("")) {
		// i = 1;
		// if (baikeCount == 2 && userId.equals("")) {
		// initmPopupWindowView();
		// if (popupWindow == null)
		// return;
		// popupWindow.setFocusable(true);
		// popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x + m, y - n
		// / 3);
		// qiandao_btn.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(
		// EnvironmentCurrentWeatherPullActivity.this,
		// UserLoginActivity.class);
		// intent.putExtra("from",
		// "EnvironmentCurrentWeatherPullActivity");
		// startActivity(intent);
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// if (popupWindow != null
		// && popupWindow.isShowing()) {
		// popupWindow.dismiss();
		// }
		// }
		// }, 2000);
		// }
		// });
		// } else {
		// initmPopupWindowBaikeView();
		// String city = WeiBaoApplication.selectedCity;
		// if (city.contains("自治州")) {
		// city = mCityDB.getSuoSuo(city);
		// }
		// String baiUrl = UrlComponent.getBaike(city);
		// http.getString(baiUrl, 0, new DownGet() {
		//
		// @Override
		// public void downget(String arg0) {
		// // TODO Auto-generated method stub
		// if (!arg0.equals("")) {
		// try {
		// JSONObject object = new JSONObject(arg0);
		// String content = "";
		// content = object.getString("flag").equals(
		// "true") ? object.getString("content")
		// : "";
		// baike.setText(content);
		// if (popupWindow == null)
		// return;
		// popupWindow.setFocusable(true);
		// if (object.getString("flag").equals("true")) {
		// popupWindow.showAtLocation(v,
		// Gravity.NO_GRAVITY, x + m, y - n
		// / 3);
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// });
		// }
		// }
	}

	public void initmPopupWindowView() {
		// 获取自定义布局文件
		baikeCount = 0;
		qiandaoView = getLayoutInflater().inflate(R.layout.environment_qiandao,
				null);
		popupWindow = new MyPopWindows(
				EnvironmentCurrentWeatherPullActivity.this, qiandaoView, 700,
				350, 8);
		popupWindow.setFocusable(true);
		qiandaoView.setFocusableInTouchMode(true);
		popupWindow.setOutsideTouchable(true);
		qiandao_btn = (TextView) qiandaoView.findViewById(R.id.qiandao_btn);
		qiandaoView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		// 重写onKeyListener
		qiandaoView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					return true;
				}
				return false;
			}
		});
	}

	public void initmPopupWindowBaikeView() {
		// 获取自定义布局文件
		baikeView = getLayoutInflater().inflate(R.layout.environment_baike,
				null);
		if (userId.equals("")) {
			baikeCount++;
			if (baikeCount == 3) {
				baikeCount = 0;
			}
		}
		popupWindow = new MyPopWindows(
				EnvironmentCurrentWeatherPullActivity.this, baikeView, 700,
				350, 6);
		popupWindow.setFocusable(true);
		baikeView.setFocusableInTouchMode(true);
		baike = (TextView) baikeView.findViewById(R.id.baike);
		baikeView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		baikeView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {

					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					return true;
				}
				return false;
			}
		});
	}

	public void initChoujiangmPopupWindowView() {
		// 获取自定义布局文件
		choujiangView = getLayoutInflater().inflate(
				R.layout.environment_choujiang, null);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		// popupWindow = new MyPopWindows(
		// EnvironmentCurrentWeatherPullActivity.this, choujiangView, 900,
		// 700, 10);
		popupWindow = new MyPopWindows(
				EnvironmentCurrentWeatherPullActivity.this, choujiangView, 900,
				700);
		try {
			popupWindow.setBackgroundDrawable(new BitmapDrawable());

		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		choujiangView.setFocusableInTouchMode(true);
		popupWindow.showAtLocation(aPager, Gravity.CENTER, 0, 0);
		String imageUri0 = "drawable://" + R.drawable.fra_img0;
		choujiang_cancel = (ImageView) choujiangView
				.findViewById(R.id.choujiang_cancel);
		choujiangintro = (TextView) choujiangView
				.findViewById(R.id.choujiangintro);
		choujiang_title = (TextView) choujiangView
				.findViewById(R.id.choujiang_title);
		choujiang_shop = (TextView) choujiangView
				.findViewById(R.id.choujiang_shop);
		chouiv1 = (ImageView) choujiangView.findViewById(R.id.chouiv1);
		chouiv1.setBackgroundResource(R.drawable.choujiang);
		chou[0] = chouiv1;
		chouiv2 = (ImageView) choujiangView.findViewById(R.id.chouiv2);
		chouiv2.setBackgroundResource(R.drawable.choujiang);
		chou[1] = chouiv2;
		chouiv3 = (ImageView) choujiangView.findViewById(R.id.chouiv3);
		chouiv3.setBackgroundResource(R.drawable.choujiang);
		chou[2] = chouiv3;
		chouiv4 = (ImageView) choujiangView.findViewById(R.id.chouiv4);
		chouiv4.setBackgroundResource(R.drawable.choujiang);
		chou[3] = chouiv4;
		chouiv5 = (ImageView) choujiangView.findViewById(R.id.chouiv5);
		chouiv5.setBackgroundResource(R.drawable.choujiang);
		chou[4] = chouiv5;
		chouiv6 = (ImageView) choujiangView.findViewById(R.id.chouiv6);
		chouiv6.setBackgroundResource(R.drawable.choujiang);
		chou[5] = chouiv6;
		chouiv7 = (ImageView) choujiangView.findViewById(R.id.chouiv7);
		chouiv7.setBackgroundResource(R.drawable.choujiang);
		chou[6] = chouiv7;
		chouiv8 = (ImageView) choujiangView.findViewById(R.id.chouiv8);
		chouiv8.setBackgroundResource(R.drawable.choujiang);
		chou[7] = chouiv8;
		chouiv1.setOnClickListener(this);
		chouiv2.setOnClickListener(this);
		chouiv3.setOnClickListener(this);
		chouiv4.setOnClickListener(this);
		chouiv5.setOnClickListener(this);
		chouiv6.setOnClickListener(this);
		chouiv7.setOnClickListener(this);
		chouiv8.setOnClickListener(this);
		choujiang_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}

			}
		});
		choujiangView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
		choujiangView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					return true;
				}
				return false;
			}
		});
	}

	// viewpager pagechange
	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (dds != 1) {
				viewpager_jiaobiao.setText((arg0 + 1) + "/" + dds);
			}
			for (int i = 0; i < dds; i++) {
				String imageUri;
				if (i == arg0) {
					// 默认选中第一张图片
					imageUri = "drawable://"
							+ R.drawable.page_indicator_focused;
					imageViews[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.page_indicator_focused));
				} else {
					imageUri = "drawable://" + R.drawable.page_indicator;
					imageViews[i].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.page_indicator));
				}
			}
			XListView xlistView = (XListView) views.get(arg0).findViewById(
					R.id.current_weather_xListView);
			index = xlistView.getFirstVisiblePosition();
			View v = xlistView.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
			initcitys = selectCitys(initcitys);
			String city = initcitys.get(arg0).get("name").toString();
			WeiBaoApplication.selectedCity = city;
			posposition = arg0;
			title_city_name.setText(city);
			String province = mCityDB.getprovicecity(city);
			final String yujingurl = UrlComponent.getAlarmHistory(province,city);
			Exceedmodel yujingjson = aCache.getAsString(yujingurl);
			http.getString(yujingurl, 0, new DownGet() {

				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					Log.i("tianfy", "我是请求回来的json数据："+arg0);
					JsonUtils.jsonAlarm(EnvironmentCurrentWeatherPullActivity.this, arg0);
				}
			});
			final String url;
			if (!city.equals(dingweicity)) {
				if (city.contains("自治州")) {
					city = mCityDB.getSuoSuo(city);
				}
				url = UrlComponent.currentWeatherGet(city, "0", "0");
			} else {
				url = UrlComponent.currentWeatherGet(city, latitude, longitude);
			}
			Exceedmodel currentJson = aCache.getAsString(url);
			if (currentJson.isFlag()) {
				priseData(currentJson.getData());
				// CurrentWeather weather = JsonUtils
				// .parseCurrentWeather(currentJson.getData());
				// weatherCurrent = weather;
				// Message msg1 = Message.obtain();
				// msg1.arg1 = 2;
				// msg1.arg2 = posposition;
				// msg1.obj = weather;
				// EnvironmentCurrentWeatherPullActivity.this.handler
				// .sendMessage(msg1);
			} else {
				String data = currentJson.getData();
				if (data != null) {
					priseData(currentJson.getData());
					// CurrentWeather weather = JsonUtils
					// .parseCurrentWeather(data);
					// weatherCurrent = weather;
					// Message msg1 = Message.obtain();
					// msg1.arg1 = 2;
					// msg1.arg2 = posposition;
					// msg1.obj = weather;
					// EnvironmentCurrentWeatherPullActivity.this.handler
					// .sendMessage(msg1);
				}
				http.getString(url, 600, new DownGet() {
					@Override
					public void downget(String arg0) {
						// TODO Auto-generated method stub
						if (!arg0.equals("")) {

							CurrentWeather weather = JsonUtils
									.parseCurrentWeather(arg0);
							weatherCurrent = weather;
							((XListView) views.get(posposition).findViewById(
									R.id.current_weather_xListView))
									.stopRefresh();
							if (null == weather
									&& NetUtil
											.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
								((TextView) views.get(posposition)
										.findViewById(R.id.weather_time))
										.setText("无网络");
								try {
									bgChange.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils
													.readBitmap(
															EnvironmentCurrentWeatherPullActivity.this,
															R.drawable.polluction_you)));
									bgChange_blurred
											.setBackgroundDrawable(new BitmapDrawable(
													ImageUtils
															.readBitmap(
																	EnvironmentCurrentWeatherPullActivity.this,
																	R.drawable.filespolluction_you)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

							} else {
								// Message msg = Message.obtain();
								// msg.arg1 = 2;
								// msg.arg2 = posposition;
								// msg.obj = weather;
								// EnvironmentCurrentWeatherPullActivity.this.handler
								// .sendMessageDelayed(msg, 800);
								priseData(arg0);
							}

							// CurrentWeather weather = JsonUtils
							// .parseCurrentWeather(arg0);
							// weatherCurrent = weather;
							// ((XListView) views.get(posposition).findViewById(
							// R.id.current_weather_xListView))
							// .stopRefresh();
							// if (null == weather
							// && NetUtil
							// .getNetworkState(getApplicationContext()) ==
							// NetUtil.NETWORN_NONE) {
							// ((TextView) views.get(posposition)
							// .findViewById(R.id.weather_time))
							// .setText("无网络");
							// bgChange.setBackgroundDrawable(new
							// BitmapDrawable(
							// ImageUtils
							// .readBitmap(
							// EnvironmentCurrentWeatherPullActivity.this,
							// R.drawable.qin)));
							// } else {
							// Message msg = Message.obtain();
							// msg.arg1 = 2;
							// msg.arg2 = posposition;
							// msg.obj = weather;
							// EnvironmentCurrentWeatherPullActivity.this.handler
							// .sendMessageDelayed(msg, 800);
							// }

						}
					}
				});
			}
		}
	}

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void updateWeather(String climate, int hour) {
		if (climate.contains("晴")) {
			flLayout.removeView(flakeView);
			is_qing = true;
			rain.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				ivSun.setVisibility(View.GONE);
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye";
				backgroundType_ye = "heiye";
			} else {
				ivSun.setVisibility(View.GONE);
				Animation sunAnimation = AnimationUtils.loadAnimation(
						EnvironmentCurrentWeatherPullActivity.this, R.anim.sun);
				// ivSun.startAnimation(sunAnimation);
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.qin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesqin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "c_qing";
				backgroundType_ye = "heiye";
			}
		} else if (climate.contains("雨")) {
			flLayout.removeView(flakeView);
			is_rain = true;
			// 更新当前雨天
			update();
			ivSun.setVisibility(View.GONE);
			rain.setVisibility(View.VISIBLE);
			// 更新当前雨天
			if (hour >= 19 || hour <= 6) {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye_yu";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.c_yu)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesc_yu)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "c_yu";
				backgroundType_ye = "heiye";
			}
			yyue = "yu";

		} else if (climate.contains("雪")) {
			flLayout.removeView(flakeView);
			if (climate.contains("小")) {
				flakeView.setSnownumber(70);
			} else if (climate.contains("中")) {
				flakeView.setSnownumber(90);
			} else if (climate.contains("大")) {
				flakeView.setSnownumber(110);
			} else if (climate.contains("暴")) {
				flakeView.setSnownumber(120);
			} else {
				flakeView.setSnownumber(70);
			}
			flLayout.addView(flakeView);
			rain.setVisibility(View.GONE);
			ivSun.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye_xue";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.c_xue)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesc_xue)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "c_xue";
				backgroundType_ye = "heiye";
			}
		} else if (climate.contains("云")) {
			flLayout.removeView(flakeView);
			is_cloud = true;
			ivSun.setVisibility(View.GONE);
			rain.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.c_duoyun)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesc_duoyun)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "yun";
				backgroundType_ye = "heiye";
			}
		} else if (climate.contains("阴")) {
			flLayout.removeView(flakeView);
			is_cloud = true;
			ivSun.setVisibility(View.GONE);
			rain.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.c_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesc_yin)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "c_yun";
				backgroundType_ye = "heiye";
			}
		} else if (climate.contains("雾")) {
			flLayout.removeView(flakeView);
			is_wumai = true;
			ivSun.setVisibility(View.GONE);
			rain.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				try {

					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.wu_first)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.fileswu_first)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				backgroundType = "wu";
				backgroundType_ye = "heiye";
			}
		} else if (climate.contains("霾")) {
			flLayout.removeView(flakeView);
			is_wumai = true;
			ivSun.setVisibility(View.GONE);
			rain.setVisibility(View.GONE);
			if (hour >= 19 || hour <= 6) {
				try {

					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.heiye_yin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesheiye_yin)));
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "heiye_yin";
				backgroundType_ye = "heiye";
			} else {
				try {
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.mai_first)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesmai_first)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				backgroundType = "mai";
				backgroundType_ye = "heiye";
			}
		}
	}

	private int getWeatherIcon(String climate) {
		int weatherIcon = R.drawable.weather_icon_qingtian;
		String climateString = CommonUtil.getWeatherIconString(climate, 0);
		if (mApplication.getWeatherIconMap().containsKey(climateString)) {
			weatherIcon = mApplication.getWeatherIconMap().get(climateString);
		}
		return weatherIcon;
	}

	private String newLine(String str, int len) {
		char[] chs = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0, sum = 0; i < chs.length; i++) {
			sum += chs[i] < 0xff ? 1 : 2;
			sb.append(chs[i]);
			if (sum >= len) {
				sum = 0;
				sb.append("\n");
			}
		}
		return sb.toString();

	}

	private void rotateImg(Boolean isDiagonal, ImageView img, int degree) {
		Bitmap bmp;
		if (isDiagonal) {
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.environment_direction_diagonal);
		} else {
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.environment_direction_horizontal);
		}
		// Getting width & height of the given image.
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(degree);
		// Rotating Bitmap
		try {
			Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
			BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
			img.setImageDrawable(bmd);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (popupWindow != null) {
			popupWindow = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		userId = WeiBaoApplication.getUserId();
		if (!WeiBaoApplication.getUserId().equals("")) {
			// && WeatherAdapter.userPwd.equals("")
			String url = UrlComponent.getUserInfoById_Get(WeiBaoApplication
					.getUserId());
			http.getString(url, 0, new DownGet() {

				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if (!arg0.equals("")) {
						try {
							JSONObject object = new JSONObject(arg0);
							if (object.getString("isEmailBind").equals("1")) {
								userName = object.getString("username");
								userPwd = WeiBaoApplication.getUserPwd();
								// WeatherAdapter.userName = userName;
								// WeatherAdapter.userPwd = userPwd;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_share:
			LocationService
					.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
			Intent intent = new Intent(
					EnvironmentCurrentWeatherPullActivity.this,
					EnvironmentCityManagerActivity.class);
			intent.putExtra("posposition", posposition);
			MobclickAgent.onEvent(EnvironmentCurrentWeatherPullActivity.this,
					"HJOpenCityPanel");
			startActivity(intent);
			break;
		/*
		 * if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
		 * Toast.makeText(EnvironmentCurrentWeatherPullActivity.this, "请检查您的网络",
		 * 0).show(); return; } Bitmap bitmap = CommonUtil
		 * .GetCurrentScreen(EnvironmentCurrentWeatherPullActivity.this); try {
		 * 
		 * uri = Uri.parse(MediaStore.Images.Media.insertImage(
		 * getContentResolver(), bitmap, null, null)); } catch (OutOfMemoryError
		 * e) { e.printStackTrace();
		 * Toast.makeText(EnvironmentCurrentWeatherPullActivity.this, "截图失败",
		 * 0).show(); return; } catch (Exception e) {
		 * Toast.makeText(EnvironmentCurrentWeatherPullActivity.this,
		 * "未检测到SD卡，分享失败！", 0).show(); return; } if (sweather != null) { content
		 * = getShareContent(sweather); } else { content = "微保环境，共建美好环境"; }
		 * mSocialShare = Frontia.getSocialShare();
		 * mSocialShare.setContext(this);
		 * mSocialShare.setClientId(MediaType.WEIXIN.toString(),
		 * "wx541df6ed6e9babc0");
		 * mSocialShare.setClientId(MediaType.SINAWEIBO.toString(),
		 * "991071488"); mSocialShare
		 * .setClientId(MediaType.QQFRIEND.toString(), "100358052");
		 * mSocialShare.setParentView(getWindow().getDecorView());
		 * mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "微保");
		 * mImageContent.setTitle("微保"); mImageContent.setContent(content);
		 * mImageContent.setLinkUrl("http://www.wbapp.com.cn");
		 * mImageContent.setImageUri(uri);
		 * mImageContent.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
		 * mImageContent.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
		 * mImageContent.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
		 * mImageContent.setImageData(bitmap); //
		 * mSocialShare.share(mImageContent, new //
		 * String[]{MediaType.WEIXIN+"",
		 * MediaType.QQFRIEND+"",MediaType.WEIXIN_FRIEND
		 * +"",MediaType.SINAWEIBO+""}, // new ShareListener(), false);
		 * mSocialShare.show(this.getWindow().getDecorView(), mImageContent,
		 * FrontiaTheme.LIGHT, new ShareListener());
		 * 
		 * break;
		 */
		case R.id.title_location:
			// if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
			// // TextView t1 = (TextView) views.get(posposition).findViewById(
			// // R.id.eweather_quality);
			// TextView t1 = (TextView) views.get(posposition).findViewById(
			// R.id.aiq_detail_value);
			//
			// TextView t2 = (TextView) views.get(posposition).findViewById(
			// R.id.weather_climate);
			// TextView t3 = (TextView) views.get(posposition).findViewById(
			// R.id.weather_temp);
			// TextView t4 = (TextView) views.get(posposition).findViewById(
			// R.id.weather_wind_direction);
			// synthetizeInSilence(mSpUtil2.getYuy(), 40, 100, "微保为您播报"
			// + title_city_name.getText().toString() + "实时空气质量"
			// + t1.getText().toString() + "今天夜间到明天白天 天气状况"
			// + t2.getText().toString() + "温度"
			// + t3.getText().toString() + "风向"
			// + t4.getText().toString());
			// MobclickAgent.onEvent(
			// EnvironmentCurrentWeatherPullActivity.this, "HJSpeech");
			//
			// }
			Intent id = new Intent(context, ActivitySet.class);
			startActivity(id);
			break;
		case R.id.title_city_manager_layout:
		case R.id.title_city_manager:
			LocationService
					.sendGetLocationBroadcast(EnvironmentCurrentWeatherPullActivity.this);
			Intent intent1 = new Intent(
					EnvironmentCurrentWeatherPullActivity.this,
					EnvironmentCityManagerActivity.class);
			intent1.putExtra("posposition", posposition);
			MobclickAgent.onEvent(EnvironmentCurrentWeatherPullActivity.this,
					"HJOpenCityPanel");
			startActivity(intent1);
			break;
		case R.id.chouiv1:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 0) {
					chou[i].setClickable(false);
				}
			}
			choujiang(1);
			break;
		case R.id.chouiv2:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 1) {
					chou[i].setClickable(false);
				}
			}
			choujiang(2);
			break;
		case R.id.chouiv3:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 2) {
					chou[i].setClickable(false);
				}
			}
			choujiang(3);
			break;
		case R.id.chouiv4:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 3) {
					chou[i].setClickable(false);
				}
			}
			choujiang(4);
			break;
		case R.id.chouiv5:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 4) {
					chou[i].setClickable(false);
				}
			}
			choujiang(5);
			break;
		case R.id.chouiv6:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 5) {
					chou[i].setClickable(false);
				}
			}
			choujiang(6);
			break;
		case R.id.chouiv7:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 6) {
					chou[i].setClickable(false);
				}
			}
			choujiang(7);
			break;
		case R.id.chouiv8:
			if (choujiangFlag) {
				return;
			}
			for (int i = 0; i < chou.length; i++) {
				if (i != 7) {
					chou[i].setClickable(false);
				}
			}
			choujiang(8);
			break;

		case R.id.title_three_day_aqi_trend:
			MobclickAgent.onEvent(EnvironmentCurrentWeatherPullActivity.this,
					"kongqizhiliangyubao");
			Intent intent2 = new Intent(
					EnvironmentCurrentWeatherPullActivity.this,
					KongqizhiliangyubaoActivity.class);
			startActivity(intent2);
			break;
		}
	}

	private void choujiang(final int m) {
		// TODO Auto-generated method stub
		final String url = UrlComponent.getlottery(WeiBaoApplication
				.getUserId());
		KjhttpUtils http = new KjhttpUtils(
				EnvironmentCurrentWeatherPullActivity.this, null);
		http.getString(url, 0, new DownGet() {

			@Override
			public void downget(String arg0) {
				// TODO Auto-generated method stub
				if (!arg0.equals("")) {
					try {
						JSONObject object = new JSONObject(arg0);
						String flag = object.getString("flag");
						String msg = object.getString("msg");
						if (flag.equals("true")) {
							final ImageView imageView = chou[m - 1];
							anim = (AnimationDrawable) imageView
									.getBackground();
							anim.start();
							int duration = 0;
							for (int i = 0; i < anim.getNumberOfFrames(); i++) {

								duration += anim.getDuration(i);

							}
							final int huojiang = Integer.parseInt(msg);
							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {

								public void run() {

									// 此处调用第二个动画播放方法
									imageView.setBackgroundResource(0);
									i = 3;
									lottery = "1";
									switch (huojiang) {
									case 1:
										imageView
												.setBackgroundResource(R.drawable.fra_img5);
										break;
									case 2:
										imageView
												.setBackgroundResource(R.drawable.fra_img6);
										break;
									case 3:
										imageView
												.setBackgroundResource(R.drawable.fra_img7);
										break;
									case 4:
										imageView
												.setBackgroundResource(R.drawable.fra_img8);
										break;
									case 5:
										imageView
												.setBackgroundResource(R.drawable.fra_img9);
										break;
									case 6:
										imageView
												.setBackgroundResource(R.drawable.fra_img10);
										break;
									case 7:
										imageView
												.setBackgroundResource(R.drawable.fra_img11);
										break;
									case 8:
										imageView
												.setBackgroundResource(R.drawable.fra_img12);
										break;
									default:
										imageView
												.setBackgroundResource(R.drawable.nothing);
										break;
									}

								}

							}, duration);
						} else {
							lottery = "1";
							ToastUtil.showShort(
									EnvironmentCurrentWeatherPullActivity.this,
									msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private SpeechSynthesizer mSpeechSynthesizer;

	private void synthetizeInSilence(String role, int speed, int volume,
			String source) {
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
		}
		// 设置合成发音人.

		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, role);
		// 获取语速

		// 设置语速
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "" + speed);
		// 获取音量.

		// 设置音量
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "" + volume);
		// 获取合成文本.

		// 进行语音合成.
		mSpeechSynthesizer.startSpeaking(source, this);
		// showTip(String.format(getString(R.string.tts_toast_format),0 ,0));
	}

	private String getShareContent(Sweather weatherInfo) {

		return String.format(mShare,
				new Object[] { title_city_name.getText().toString(),
						weatherInfo.getWeather(), weatherInfo.getTemp(),
						weatherInfo.getLevel(), weatherInfo.getPM2Dot5Data() });

	}

	// 分享成功还是失败
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			Log.d("Test", "share success");
			MobclickAgent.onEvent(EnvironmentCurrentWeatherPullActivity.this,
					"HJShareHj");
			Toast.makeText(EnvironmentCurrentWeatherPullActivity.this, "分享成功",
					2000).show();
			userId = WeiBaoApplication.getUserId();

			if (!userId.equals("")) {
				http.getString(UrlComponent.getShare(userId, "share"), 0,
						new DownGet() {

							@Override
							public void downget(String arg0) {
								// TODO Auto-generated method stub

							}
						});
			}
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
			Toast.makeText(EnvironmentCurrentWeatherPullActivity.this, "分享失败",
					2000).show();
		}

		@Override
		public void onCancel() {
			Log.d("Test", "cancel ");
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				int m = 0;
				switch (msg.arg1) {

				case 1:
					views.clear();
					initcitys = selectCitys(initcitys);
					dds = initcitys.size() == 0 ? 1 : initcitys.size();
					initcitys = selectCitys(initcitys);
					for (int i = 0; i < dds; i++) {
						initPagerItem(i);
					}
					aPager.setAdapter(new GuidePageAdapter(views));
					aPager.setOnPageChangeListener(new MyOnPageChangeListener());
					aPager.setOffscreenPageLimit(5);
					imageViews = new ImageView[dds];
					group.removeAllViews();
					for (int i = 0; i < views.size(); i++) {
						imageView = new ImageView(
								EnvironmentCurrentWeatherPullActivity.this);
						imageView
								.setLayoutParams(new LayoutParams(
										CommonUtil
												.dip2px(EnvironmentCurrentWeatherPullActivity.this,
														10),
										CommonUtil
												.dip2px(EnvironmentCurrentWeatherPullActivity.this,
														10)));
						imageViews[i] = imageView;

						String imageUri;
						if (i == msg.arg2) {
							// 默认选中第一张图片
							imageUri = "drawable://"
									+ R.drawable.page_indicator_focused;
							imageViews[i].setBackgroundDrawable(getResources()
									.getDrawable(
											R.drawable.page_indicator_focused));
						} else {
							imageUri = "drawable://"
									+ R.drawable.page_indicator;
							imageViews[i].setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.page_indicator));
						}
						group.addView(imageViews[i]);
					}
					if (dds != 1) {
						viewpager_jiaobiao.setText((msg.arg2 + 1) + "/" + dds);
					}
					aPager.setCurrentItem(msg.arg2);
					posposition = msg.arg2;
					String city = initcitys.get(msg.arg2).get("name")
							.toString();
					title_city_name.setText(city);
					final String url;
					if (!city.equals(dingweicity)) {
						if (city.contains("自治州")) {
							city = mCityDB.getSuoSuo(city);
						}
						url = UrlComponent.currentWeatherGet(city, "0", "0");
					} else {
						url = UrlComponent.currentWeatherGet(city, latitude,
								longitude);
					}
					Exceedmodel arg0 = aCache.getAsString(url);
					if (arg0.isFlag()) {
						Message msg1 = Message.obtain();
						msg1.arg1 = 2;
						msg1.arg2 = posposition;
						msg1.obj = weather;
						EnvironmentCurrentWeatherPullActivity.this.handler
								.sendMessage(msg1);
						priseData(arg0.getData());
					} else {
						String data = arg0.getData();
						if (data != null) {
							Message msg2 = Message.obtain();
							msg2.arg1 = 2;
							msg2.arg2 = posposition;
							msg2.obj = weather;
							EnvironmentCurrentWeatherPullActivity.this.handler
									.sendMessage(msg2);
							return;
						}
						http.getString(url, 600, new DownGet() {

							@Override
							public void downget(String arg0) {
								// TODO Auto-generated method stub
								weatherCurrent = JsonUtils
										.parseCurrentWeather(arg0);
								((XListView) views.get(posposition)
										.findViewById(
												R.id.current_weather_xListView))
										.stopRefresh();
								if (null == weather
										&& NetUtil
												.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
									((TextView) views.get(posposition)
											.findViewById(R.id.weather_time))
											.setText("无网络");
									try {
										bgChange.setBackgroundDrawable(new BitmapDrawable(
												ImageUtils
														.readBitmap(
																EnvironmentCurrentWeatherPullActivity.this,
																R.drawable.qin)));
										bgChange_blurred
												.setBackgroundDrawable(new BitmapDrawable(
														ImageUtils
																.readBitmap(
																		EnvironmentCurrentWeatherPullActivity.this,
																		R.drawable.filesqin)));
									} catch (OutOfMemoryError e) {
										// TODO: handle exception
										e.printStackTrace();
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}

								} else {
									Message msg = Message.obtain();
									msg.arg1 = 2;
									msg.arg2 = posposition;
									msg.obj = weatherCurrent;
									EnvironmentCurrentWeatherPullActivity.this.handler
											.sendMessage(msg);
									priseData(arg0);
								}
							}
						});
					}
					break;
				case 2:
					m = msg.arg2;
					weather = (CurrentWeather) msg.obj;
					if (weather != null) {
						adapter = new WeatherAdapter(
								EnvironmentCurrentWeatherPullActivity.this,
								weather, screenHalfheigh12, screenHalfheigh8,
								screenWidth);
						getNewsData(m, weather, null);
						((XListView) views.get(m).findViewById(
								R.id.current_weather_xListView))
								.setAdapter(adapter);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	public int getAqiDrawable(String aqiValue) {
		int aqi = 0;
		try {
			aqi = Integer.parseInt(aqiValue);
		} catch (Exception e) {
		}
		if ((aqi > -1) && (aqi < 51)) {
			return R.drawable.aqi_level_1;
		} else if (aqi < 101) {
			return R.drawable.aqi_level_2;
		} else if (aqi < 151) {
			return R.drawable.aqi_level_3;
		} else if (aqi < 201) {
			return R.drawable.aqi_level_4;
		} else if (aqi < 301) {
			return R.drawable.aqi_level_5;
		} else {
			return R.drawable.aqi_level_6;
		}
	}

	public int getYuCeAqiDrawable(int aqi) {
		if (aqi == 1 || aqi == 0) {
			return R.drawable.aqi_level_1;
		} else if (aqi == 2) {
			return R.drawable.aqi_level_2;
		} else if (aqi == 3) {
			return R.drawable.aqi_level_3;
		} else if (aqi == 4) {
			return R.drawable.aqi_level_4;
		} else if (aqi == 5) {
			return R.drawable.aqi_level_5;
		} else {
			return R.drawable.aqi_level_6;
		}
	}

	class GetThreeDayAqiTrendModelTask extends
			AsyncTask<String, Void, MainAqiData> {
		private String arg0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected MainAqiData doInBackground(String... params) {
			String url = UrlComponent.getThreeDayAqiTrend;
			String city = WeiBaoApplication.selectedCity;

			BusinessSearch search = new BusinessSearch();
			arg0 = params[0];
			MainAqiData _Result = new MainAqiData();
			try {
				_Result = search.GetMianAqiData(url, city);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(MainAqiData result) {

			if (arg0 != null) {
				CurrentWeather weather = JsonUtils.parseCurrentWeather(arg0);
				weatherCurrent = weather;
				((XListView) views.get(posposition).findViewById(
						R.id.current_weather_xListView)).stopRefresh();
				if (null == weather
						&& NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
					((TextView) views.get(posposition).findViewById(
							R.id.weather_time)).setText("无网络");
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.qin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesqin)));
				} else {
					if (weather != null) {
						adapter = new WeatherAdapter(
								EnvironmentCurrentWeatherPullActivity.this,
								weather, screenHalfheigh12, screenHalfheigh8,
								screenWidth);
						getNewsData(posposition, weather, null);
						((XListView) views.get(posposition).findViewById(
								R.id.current_weather_xListView))
								.setAdapter(adapter);
					}
				}
			}
			try {
				getNewsYuCeData(posposition, result);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void priseData(String arg0) {
		View viewe = getLayoutInflater().inflate(R.layout.main, null);
		String city = WeiBaoApplication.selectedCity;
		if (!isContains(city, cityArrays)) {
			if (arg0 != null) {
				CurrentWeather weather = JsonUtils.parseCurrentWeather(arg0);
				weatherCurrent = weather;
				((XListView) views.get(posposition).findViewById(
						R.id.current_weather_xListView)).stopRefresh();
				if (null == weather&& NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
					((TextView) views.get(posposition).findViewById(
							R.id.weather_time)).setText("无网络");
					bgChange.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.qin)));
					bgChange_blurred.setBackgroundDrawable(new BitmapDrawable(
							ImageUtils.readBitmap(
									EnvironmentCurrentWeatherPullActivity.this,
									R.drawable.filesqin)));
				} else {
					if (weather != null) {
						adapter = new WeatherAdapter(
								EnvironmentCurrentWeatherPullActivity.this,
								weather, screenHalfheigh12, screenHalfheigh8,
								screenWidth);
						getNewsData(posposition, weather, null);
						((XListView) views.get(posposition).findViewById(
								R.id.current_weather_xListView))
								.setAdapter(adapter);
					}
				}
			}
			View view = views.get(posposition);
			RelativeLayout aqi_relayout = (RelativeLayout) view
					.findViewById(R.id.aqi_relayout);
			LinearLayout ell_curr_weather = (LinearLayout) view
					.findViewById(R.id.ell_curr_weather);
			aqi_relayout.setVisibility(View.VISIBLE);
			ell_curr_weather.setVisibility(View.GONE);
			return;
		}
		GetThreeDayAqiTrendModelTask aqiTrendModelTask = new GetThreeDayAqiTrendModelTask();
		aqiTrendModelTask.execute(arg0);
	}

	public boolean isContains(String a, String[] str) {
		for (String s : str) {
			if (s.contains(a)) {
				return true;
			}

		}
		return false;
	}

	private void getData(String city) {
		title_city_name.setText(city);
		String province = mCityDB.getprovicecity(city);
		final String yujingurl = UrlComponent.getAlarmHistory(province, city);
		Exceedmodel yujingjson = aCache.getAsString(yujingurl);
		final String url;
		if (!city.equals(dingweicity)) {
			if (city.contains("自治州")) {
				city = mCityDB.getSuoSuo(city);
			}
			url = UrlComponent.currentWeatherGet(city, "0", "0");
		} else {
			url = UrlComponent.currentWeatherGet(city, latitude, longitude);
		}
		Exceedmodel currentJson = aCache.getAsString(url);
		if (currentJson.isFlag()) {
			priseData(currentJson.getData());
		} else {
			String data = currentJson.getData();
			if (data != null) {
				priseData(currentJson.getData());
			}
			http.getString(url, 600, new DownGet() {
				@Override
				public void downget(String arg0) {
					// TODO Auto-generated method stub
					if (!arg0.equals("")) {
						CurrentWeather weather = JsonUtils
								.parseCurrentWeather(arg0);
						weatherCurrent = weather;
						((XListView) views.get(posposition).findViewById(
								R.id.current_weather_xListView)).stopRefresh();
						if (null == weather
								&& NetUtil
										.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
							((TextView) views.get(posposition).findViewById(
									R.id.weather_time)).setText("无网络");
							try {
								bgChange.setBackgroundDrawable(new BitmapDrawable(
										ImageUtils
												.readBitmap(
														EnvironmentCurrentWeatherPullActivity.this,
														R.drawable.polluction_you)));
								bgChange_blurred
										.setBackgroundDrawable(new BitmapDrawable(
												ImageUtils
														.readBitmap(
																EnvironmentCurrentWeatherPullActivity.this,
																R.drawable.filespolluction_you)));

							} catch (OutOfMemoryError e) {
								// TODO: handle exception
								e.printStackTrace();
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						} else {
							priseData(arg0);
						}

					}
				}
			});
		}
	}

	private void updateView(final int screenWidth) {
		Bitmap bmpBlurred = BitmapFactory.decodeFile(getFilesDir()
				+ BLURRED_IMG_PATH);
		bmpBlurred = Bitmap
				.createScaledBitmap(
						bmpBlurred,
						screenWidth,
						(int) (bmpBlurred.getHeight() * ((float) screenWidth) / (float) bmpBlurred
								.getWidth()), false);

		bgChange_blurred.setImageBitmap(bmpBlurred);
	}

}