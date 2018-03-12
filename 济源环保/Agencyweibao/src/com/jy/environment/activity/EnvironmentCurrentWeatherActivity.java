package com.jy.environment.activity;
/*package com.mapuni.weibao.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;










//import com.baidu.cloudsdk.BaiduException;
//import com.baidu.cloudsdk.IBaiduListener;
//import com.baidu.cloudsdk.social.core.MediaType;
//import com.baidu.cloudsdk.social.oauth.SocialConfig;
//import com.baidu.cloudsdk.social.share.ShareContent;
//import com.baidu.cloudsdk.social.share.SocialShare;
//import com.baidu.cloudsdk.social.share.SocialShare.UIWidgetStyle;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIMediaObject;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQFlagType;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQReqestType;
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
import com.mapuni.weibao.R;
import com.mapuni.weibao.base.ActivityBase;
import com.mapuni.weibao.business.BusinessSearch;
import com.mapuni.weibao.controls.AsyncTask;
import com.mapuni.weibao.controls.WeiBaoApplication;
import com.mapuni.weibao.database.dal.CityDB;
import com.mapuni.weibao.model.ManageCity;
import com.mapuni.weibao.model.NearestPm;
import com.mapuni.weibao.model.Sweather;
import com.mapuni.weibao.model.WeatherInfo;
import com.mapuni.weibao.receiver.NetBroadcastReceiver;
import com.mapuni.weibao.receiver.NetBroadcastReceiver.EventHandler;
import com.mapuni.weibao.services.LocationService;
import com.mapuni.weibao.util.CommonUtil;
import com.mapuni.weibao.util.ExampleUtil;
import com.mapuni.weibao.util.ImageUtils;
import com.mapuni.weibao.util.MyLog;
import com.mapuni.weibao.util.NetUtil;
import com.mapuni.weibao.util.NotificationUtils;
import com.mapuni.weibao.util.SharePreferenceUtil;
import com.mapuni.weibao.util.SharedPreferencesUtil;
import com.mapuni.weibao.util.ToastUtil;
import com.mapuni.weibao.webservice.UrlComponent;
import com.mapuni.weibao.widget.DynamicWeatherCloudyView;
import com.mapuni.weibao.widget.DynamicWeatherWuMaiyView;
import com.mapuni.weibao.widget.RainView;
import com.mapuni.weibao.widget.XListView;
import com.mapuni.weibao.widget.XListView.IXListViewListener;

public class EnvironmentCurrentWeatherActivity extends ActivityBase implements
	EventHandler, OnClickListener, SynthesizerListener, IXListViewListener {
    *//**
     * 定位城市的附近数据
     *//*
	private static NearestPm MynearestPm;
    *//**
     * 当前定位城市是否发生过变化
     *//*
    private static boolean isLocationChange = false;
    private Map<Integer, Sweather> sweatherMap = new HashMap<Integer, Sweather>();
    private FrontiaSocialShare mSocialShare;
    private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
    private final static int MESSAGE_EXIT = 0x00001;
    public static Boolean is_rain = false;
    public static Boolean is_snow = false;
    public static Boolean is_cloud = false;
    public static Boolean is_wumai = false;
    public static Boolean is_qing = false;
    int width, height, rewidth, reheight, pingwidth, pingheight;
    String cityName;
    // 控制listview 可见
    public static final String TAG = "CurrentTq";
    boolean flag = true;
    public RefreshHandler mRedrawHandler = new RefreshHandler();
    public static final String UPDATE_WIDGET_WEATHER_ACTION = "com.way.action.update_weather";
    public static final String DING_WEI = "com.mapuni.weibao.ui.CurrentTq";
    private static final int LOACTION_OK = 0;
    private static final int UPDATE_EXISTS_CITY = 2;
    public static final int GET_WEATHER_SCUESS = 3;
    public static final int GET_WEATHER_FAIL = 4;
    private LocationClient mLocationClient; 定位 
    private CityDB mCityDB;
    private SharePreferenceUtil mSpUtil;
    private SharedPreferencesUtil mSpUtil2;
    private WeiBaoApplication mApplication;
    private ImageView mLocationBtn, mShareBtn;
    private ImageView mUpdateProgressBar;
    private TextView mTitleTextView;
    SharedPreferences sharedPreference;
    private LinearLayout bgChange;

    LinearLayout today_climate[];
    RelativeLayout today_weather_layout[];
    TextView times[], weather_pm25[], weather_quality[], weather_temperature[],
	    weather_temp[], weather_climate[], weather_today[], weather_date[],
	    weather_wind_direction[], weather_wind_speed[], weather_sidu[],
	    weather_lunar[], dingweichengshi[];
    ImageView weather_img[], forecast_icon[];
    private String content;
    private EditText msgText;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private Sweather sweather;
    public static final String NAME = "";
    private String yyue = "";
    public static boolean isFirst_cloud = true;
    public LoadWeatherTask loadWeather;

    private NotificationManager notificationManager;
    private SharedPreferences.Editor editor;
    private RainView rain = null;
    private FrameLayout flLayout;
    private ImageView ivSun;
    private DynamicWeatherCloudyView cloudView2;
    private DynamicWeatherWuMaiyView wumaiView;
    private String refresh_date, refresh_time;
    private Date now = new Date();
    private SimpleDateFormat myFmt = new SimpleDateFormat("MM/dd");
    private SimpleDateFormat timeNow = new SimpleDateFormat("HH:mm");
    private ArrayList<View> pageViews;
    private ImageView imageView;
    private RelativeLayout rehight[];
    private LinearLayout layLout[];
    private ViewGroup main;
    private ViewGroup group;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private View densityView[];
    private View densityViewHead[];
    private XListView weather_xListView[];
    private int tuichujing = 0;
    private boolean iszero = false;
    private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
    private List<String> allcity = new ArrayList<String>();
    private String mShare = "今日%s天气：%s，温度：%s；空气质量指数(AQI)：%s，PM2.5 浓度值：%s μg/m3。";
    private String mShareSimple = "今日%s天气：%s，温度：%s，湿度：%s，风向：%s。";
    private Handler mHandler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case MESSAGE_EXIT:
		break;

	    case UPDATE_EXISTS_CITY:
		updateWeatherInfo1(null);
		break;
	    case GET_WEATHER_SCUESS:
		WeatherInfo weatherInfo = (mApplication.getAllWeather());
		updateWeatherInfo1(weatherInfo);
		break;
	    case GET_WEATHER_FAIL:
		updateWeatherInfo1(null);
		break;
	    default:
		break;
	    }
	}

    };

    private ImageView[] imageViews;

    private void changeview(int arg0) {

	updateWeather1(initcitys.get(arg0).get("name").toString(), "7200000");
    }

    class GuidePageChangeListener implements OnPageChangeListener {
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	    changeview(arg0);
	    pos = arg0;

	    is_rain = false;
	    is_snow = false;
	    is_cloud = false;
	    is_wumai = false;
	    is_qing = false;

	    if (!"".equals(mApplication.getXiangxidizhi())
		    && mSpUtil2.getisDingwei()) {
		if (WeiBaoApplication.selectedCity.equals(WeiBaoApplication
			.getInstance().getDingweicity())) {
		    dingweichengshi[arg0].setText(mApplication
			    .getXiangxidizhi());
		} else {
		    dingweichengshi[arg0].setVisibility(View.GONE);
		}

	    } else {
		dingweichengshi[arg0].setVisibility(View.GONE);
	    }

	    for (int i = 0; i < imageViews.length; i++) {
		imageViews[arg0]
			.setBackgroundResource(R.drawable.page_indicator_focused);

		if (arg0 != i) {
		    imageViews[i]
			    .setBackgroundResource(R.drawable.page_indicator);
		}
	    }
	}

    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

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
	public void destroyItem(View arg0, int arg1, Object arg2) {
	    try {
		int dd = pageViews.size();
		if (arg1 == dd) {
		    return;
		}
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

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
	    return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	LocationService.sendGetLocationBroadcast(this);
	notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	DisplayMetrics metric = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(metric);
	width = metric.widthPixels; // 屏幕宽度（像素）
	height = metric.heightPixels; // 屏幕高度（像素）
	rewidth = CommonUtil.px2dip(EnvironmentCurrentWeatherActivity.this,
		width) - 200;
	reheight = CommonUtil.px2dip(EnvironmentCurrentWeatherActivity.this,
		height) - 275;
	pingwidth = CommonUtil.dip2px(EnvironmentCurrentWeatherActivity.this,
		rewidth);
	pingheight = CommonUtil.dip2px(EnvironmentCurrentWeatherActivity.this,
		reheight);
	loadWeather = new LoadWeatherTask();
	mApplication = WeiBaoApplication.getInstance();
	mCityDB = mApplication.getInstance().getCityDB();

	SpeechUser.getUser().login(EnvironmentCurrentWeatherActivity.this,
		null, null, SpeechConstant.APPID + "=52dcaf71", loginListener);
	// 登录回调监听器
	mLocationClient = mApplication.getLocationClient();
	mLocationClient.registerLocationListener(mLocationListener);
	mLocationClient.start();
	mLocationClient.requestLocation();
	LocationService.sendGetLocationBroadcast(EnvironmentCurrentWeatherActivity.this);
	mSpUtil2 = SharedPreferencesUtil
		.getInstance(EnvironmentCurrentWeatherActivity.this);

	initcitys = mCityDB
		.queryBySqlReturnArrayListHashMap("select * from addcity");
	initcitys = selectCitys(initcitys);
	inflater = getLayoutInflater();
	pageViews = new ArrayList<View>();

	if (initcitys.size() == 0) {
	    iszero = true;
	}

	int dds = initcitys.size() == 0 ? 1 : initcitys.size();
	MyLog.i("dds initcitys.size()" + initcitys.size());
	densityView = new View[dds];
	densityViewHead = new View[dds];
	weather_xListView = new XListView[dds];
	today_weather_layout = new RelativeLayout[dds];
	layLout = new LinearLayout[dds];
	times = new TextView[dds];
	weather_pm25 = new TextView[dds];
	weather_lunar = new TextView[dds];
	forecast_icon = new ImageView[dds];
	weather_quality = new TextView[dds];
	weather_temperature = new TextView[dds];
	weather_wind_direction = new TextView[dds];
	weather_wind_speed = new TextView[dds];
	weather_sidu = new TextView[dds];
	weather_temp = new TextView[dds];
	weather_climate = new TextView[dds];
	weather_img = new ImageView[dds];
	weather_today = new TextView[dds];
	weather_date = new TextView[dds];
	dingweichengshi = new TextView[dds];
	today_climate = new LinearLayout[dds];
	rehight = new RelativeLayout[dds];
	for (int i = 0; i < dds; i++) {
	    densityView[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_list, null);
	    densityViewHead[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_head, null);
	    weather_xListView[i] = (XListView) densityView[i]
		    .findViewById(R.id.weather_xListView);
	    weather_xListView[i].setXListViewListener(this);
	    rehight[i] = (RelativeLayout) densityView[i]
		    .findViewById(R.id.rehight);

	    layLout[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.weather_layout1);
	    RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
		    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    layoutParam.topMargin = pingheight;
	    layoutParam.rightMargin = 10;
	    layLout[i].setLayoutParams(layoutParam);
	    dingweichengshi[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.dingweichenshi);
	    times[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_time);
	    today_climate[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.ll_curr_weather);
	    today_climate[i].setOnClickListener(this);
	    forecast_icon[i] = (ImageView) densityViewHead[i]
		    .findViewById(R.id.forecast_icon);

	    today_weather_layout[i] = (RelativeLayout) densityViewHead[i]
		    .findViewById(R.id.today_weather_layout);
	    today_weather_layout[i].setOnClickListener(this);
	    weather_pm25[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_pm25);
	    weather_quality[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_quality);
	    weather_temperature[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temperature);
	    weather_wind_direction[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_direction);
	    weather_wind_speed[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_speed);
	    weather_sidu[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_sidu);
	    weather_lunar[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_lunar);

	    weather_temp[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temp);
	    weather_climate[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_climate);
	    weather_today[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_today);
	    weather_date[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_date);
	    weather_xListView[i].addHeaderView(densityViewHead[i], null, true);
	    weather_xListView[i].setAdapter(null);
	    pageViews.add(densityView[i]);
	}

	getIntent().getBooleanExtra("isWidget", false);
	tuichujing = mSpUtil2.getDi();

	imageViews = new ImageView[pageViews.size()];
	main = (ViewGroup) inflater.inflate(
		R.layout.environment_current_weather_activity, null);
	viewPager = (ViewPager) main.findViewById(R.id.viewpager);
	bgChange = (LinearLayout) main.findViewById(R.id.weather_change_view);

	viewPager.setAdapter(new GuidePageAdapter());
	viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	group = (ViewGroup) main.findViewById(R.id.mybottomviewgroup1);
	mShareBtn = (ImageView) main.findViewById(R.id.title_share);
	mLocationBtn = (ImageView) main.findViewById(R.id.title_location);

	mUpdateProgressBar = (ImageView) main
		.findViewById(R.id.title_update_progress);
	mUpdateProgressBar.setOnClickListener(this);
	mShareBtn.setOnClickListener(this);
	mLocationBtn.setOnClickListener(this);
	city = (LinearLayout) main.findViewById(R.id.title_city_manager_layout);
	mTitleTextView = (TextView) main.findViewById(R.id.title_city_name);
	ivSun = (ImageView) main.findViewById(R.id.ivSun);
	city.setOnClickListener(this);

	for (int i = 0; i < pageViews.size(); i++) {
	    imageView = new ImageView(EnvironmentCurrentWeatherActivity.this);
	    imageView
		    .setLayoutParams(new LayoutParams(width / 22, height / 41));
	    imageView.setPadding(20, 0, 20, 0);
	    imageViews[i] = imageView;

	    if (i == 0) {
		// 默认选中第一张图片
		imageViews[i]
			.setBackgroundResource(R.drawable.page_indicator_focused);
	    } else {
		imageViews[i].setBackgroundResource(R.drawable.page_indicator);
	    }

	    group.addView(imageViews[i]);
	}

	if (iszero) {
	    viewPager.setCurrentItem(0);
	} else {
	    if (tuichujing > dds) {
		viewPager.setCurrentItem(0);
	    } else {
		viewPager.setCurrentItem(tuichujing);
	    }
	}
	if (initcitys.size() != 0) {
	    *//**
	     * 1 刚进入界面时，加载历史数据，方式用户等待过长时间
	     *//*
	    if (initcitys.size() - 1 < tuichujing) {
		updateWeather1(initcitys.get(0).get("name").toString(),
			"7200000");
	    } else {
		updateWeather1(
			initcitys.get(tuichujing).get("name").toString(),
			"7200000");
	    }

	}
	setContentView(main);
	registerMessageReceiver();
	// 获得雨视图,并加载雨图片到内存
	rain = (RainView) findViewById(R.id.rain);
	rain.LoadRainImage();
	// 获取当前屏幕的高和宽
	DisplayMetrics dm = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(dm);
	rain.SetView(dm.heightPixels, dm.widthPixels);
	flLayout = (FrameLayout) findViewById(R.id.fllayout);

    }

    
     * 负责做界面更新工作 ，实现下雨
     
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

    *//**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's
     * location.
     *//*
    public void update() {

	rain.addRandomRain();
	mRedrawHandler.sleep(100);
    }

    private void resetView() {
	pageViews.clear();
	int dds = initcitys.size() == 0 ? 1 : initcitys.size();
	densityView = new View[dds];
	densityViewHead = new View[dds];
	times = new TextView[dds];
	weather_pm25 = new TextView[dds];
	weather_lunar = new TextView[dds];
	today_weather_layout = new RelativeLayout[dds];
	today_climate = new LinearLayout[dds];
	weather_img = new ImageView[dds];
	forecast_icon = new ImageView[dds];
	weather_quality = new TextView[dds];
	weather_temperature = new TextView[dds];
	weather_wind_direction = new TextView[dds];
	weather_wind_speed = new TextView[dds];
	weather_sidu = new TextView[dds];
	weather_temp = new TextView[dds];
	weather_climate = new TextView[dds];
	weather_today = new TextView[dds];
	weather_date = new TextView[dds];
	dingweichengshi = new TextView[dds];
	weather_xListView = new XListView[dds];
	layLout = new LinearLayout[dds];
	// 修改后或者删除城市名的时候更新ui
	for (int i = 0; i < dds; i++) {
	    densityView[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_list, null);
	    densityViewHead[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_head, null);
	    layLout[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.weather_layout1);
	    RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
		    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    layoutParam.topMargin = pingheight;
	    layoutParam.rightMargin = 10;
	    layLout[i].setLayoutParams(layoutParam);
	    weather_xListView[i] = (XListView) densityView[i]
		    .findViewById(R.id.weather_xListView);
	    weather_xListView[i].setXListViewListener(this);
	    times[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_time);
	    today_climate[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.ll_curr_weather);
	    today_weather_layout[i] = (RelativeLayout) densityViewHead[i]
		    .findViewById(R.id.today_weather_layout);
	    layLout[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.weather_layout1);
	    forecast_icon[i] = (ImageView) densityViewHead[i]
		    .findViewById(R.id.forecast_icon);
	    weather_pm25[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_pm25);
	    weather_quality[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_quality);
	    weather_temperature[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temperature);
	    weather_wind_direction[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_direction);
	    weather_wind_speed[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_speed);
	    weather_sidu[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_sidu);
	    weather_temp[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temp);
	    weather_climate[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_climate);
	    weather_today[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_today);
	    weather_date[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_date);
	    weather_lunar[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_lunar);
	    dingweichengshi[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.dingweichenshi);
	    weather_xListView[i].addHeaderView(densityViewHead[i]);
	    weather_xListView[i].setAdapter(null);
	    pageViews.add(densityView[i]);
	}

	imageViews = new ImageView[pageViews.size()];

	viewPager.setAdapter(new GuidePageAdapter());
	viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	//
	group.removeAllViews();

	for (int i = 0; i < pageViews.size(); i++) {
	    imageView = new ImageView(EnvironmentCurrentWeatherActivity.this);
	    imageView
		    .setLayoutParams(new LayoutParams(width / 22, height / 41));
	    imageView.setPadding(20, 0, 20, 0);
	    imageViews[i] = imageView;

	    if (i == 0) {
		// 默认选中第一张图片
		imageViews[i]
			.setBackgroundResource(R.drawable.page_indicator_focused);
	    } else {
		imageViews[i].setBackgroundResource(R.drawable.page_indicator);
	    }

	    group.addView(imageViews[i]);
	}
    }

    void dongtai(int ds) {
	pageViews.clear();
	// if (initcitys.size()==0) {
	// iszero=true;
	// }

	int dds = initcitys.size() == 0 ? 1 : initcitys.size();
	densityView = new View[dds];
	densityViewHead = new View[dds];
	times = new TextView[dds];
	weather_pm25 = new TextView[dds];
	weather_lunar = new TextView[dds];
	today_weather_layout = new RelativeLayout[dds];
	today_climate = new LinearLayout[dds];
	weather_img = new ImageView[dds];
	forecast_icon = new ImageView[dds];
	weather_quality = new TextView[dds];
	weather_temperature = new TextView[dds];
	weather_wind_direction = new TextView[dds];
	weather_wind_speed = new TextView[dds];
	weather_sidu = new TextView[dds];
	weather_temp = new TextView[dds];
	weather_climate = new TextView[dds];
	weather_today = new TextView[dds];
	weather_date = new TextView[dds];
	dingweichengshi = new TextView[dds];
	weather_xListView = new XListView[dds];
	layLout = new LinearLayout[dds];
	// 修改后或者删除城市名的时候更新ui
	for (int i = 0; i < dds; i++) {
	    densityView[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_list, null);
	    densityViewHead[i] = inflater.inflate(
		    R.layout.environment_current_weather_activity_head, null);

	    weather_xListView[i] = (XListView) densityView[i]
		    .findViewById(R.id.weather_xListView);
	    weather_xListView[i].setXListViewListener(this);
	    times[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_time);
	    today_climate[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.ll_curr_weather);
	    today_weather_layout[i] = (RelativeLayout) densityViewHead[i]
		    .findViewById(R.id.today_weather_layout);
	    layLout[i] = (LinearLayout) densityViewHead[i]
		    .findViewById(R.id.weather_layout1);
	    RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
		    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    layoutParam.topMargin = pingheight;
	    layoutParam.rightMargin = 10;
	    layLout[i].setLayoutParams(layoutParam);
	    forecast_icon[i] = (ImageView) densityViewHead[i]
		    .findViewById(R.id.forecast_icon);
	    weather_pm25[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_pm25);
	    weather_quality[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_quality);
	    weather_temperature[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temperature);
	    weather_wind_direction[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_direction);
	    weather_wind_speed[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_wind_speed);
	    weather_sidu[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_sidu);
	    weather_temp[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_temp);
	    weather_climate[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_climate);
	    weather_today[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_today);
	    weather_date[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_date);
	    weather_lunar[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.weather_lunar);
	    dingweichengshi[i] = (TextView) densityViewHead[i]
		    .findViewById(R.id.dingweichenshi);
	    weather_xListView[i].addHeaderView(densityViewHead[i]);
	    weather_xListView[i].setAdapter(null);
	    pageViews.add(densityView[i]);
	}
	imageViews = new ImageView[pageViews.size()];
	viewPager.setAdapter(new GuidePageAdapter());
	viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	group.removeAllViews();

	for (int i = 0; i < pageViews.size(); i++) {
	    imageView = new ImageView(EnvironmentCurrentWeatherActivity.this);
	    imageView
		    .setLayoutParams(new LayoutParams(width / 22, height / 41));
	    imageView.setPadding(20, 0, 20, 0);
	    imageViews[i] = imageView;

	    if (i == 0) {
		// 默认选中第一张图片
		imageViews[i]
			.setBackgroundResource(R.drawable.page_indicator_focused);
	    } else {
		imageViews[i].setBackgroundResource(R.drawable.page_indicator);
	    }

	    group.addView(imageViews[i]);
	}
	viewPager.setCurrentItem(ds);
	if (initcitys.size() != 0) {
	    updateWeather1(initcitys.get(ds).get("name").toString(), "7200000");
	}
    }

    public void registerMessageReceiver() {
	mMessageReceiver = new MessageReceiver();
	IntentFilter filter = new IntentFilter();
	filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
	filter.addAction(MESSAGE_RECEIVED_ACTION);
	filter.addAction(EnvironmentCityManagerActivity.ADD_ACTION);
	filter.addAction(EnvironmentCityManagerActivity.ADD_VIEW);
	// 网络状况不好的情况下广播
	filter.addAction(EnvironmentCurrentWeatherActivity.DING_WEI);
	filter.addAction(EnvironmentCityManagerActivity.LOCATION_CHANGE);
	// 网络状态切换时候的广播
	filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

	registerReceiver(mMessageReceiver, filter);
    }

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
	    }
	    if (DING_WEI.equals(intent.getAction())) {
		String dingwei = intent.getStringExtra("dingwei");
		String city = intent.getStringExtra("city");
		// if (city.equals(mTitleTextView.getText().toString())) {
		// dingweichengshi[pos].setVisibility(View.VISIBLE);
		// dingweichengshi[pos].setText(dingwei);
		// }
		// updateWeather1(city, "60000");
		if (null != city && city.equals(WeiBaoApplication.selectedCity)) {
		    LoadWeatherTask loadWeatherCity = new LoadWeatherTask();
		    loadWeatherCity.execute(city, "60000");
		}
		Intent UPintent = new Intent("com.mapuni.weibao.UPDATE");
		sendBroadcast(UPintent);
	    }
	    if (EnvironmentCityManagerActivity.ADD_ACTION.equals(intent
		    .getAction())) {

		if (intent.getExtras().getBoolean("bian")) {
		    initcitys = (ArrayList<HashMap<String, Object>>) intent
			    .getSerializableExtra("view");
		    initcitys = selectCitys(initcitys);
		    int ss = intent.getExtras().getInt("po");
		    dongtai(ss);

		} else {
		    int ss = intent.getExtras().getInt("po");
		    viewPager.setCurrentItem(ss);
		}
	    }
	    if (EnvironmentCityManagerActivity.ADD_VIEW.equals(intent
		    .getAction())) {
		initcitys = (ArrayList<HashMap<String, Object>>) intent
			.getSerializableExtra("view");
		initcitys = selectCitys(initcitys);
		Log.i("bai", "mCity00000"
			+ WeiBaoApplication.getInstance().isUpdate);
		for (int i = 0; i < initcitys.size(); i++) {
		    ManageCity mCity = new ManageCity(initcitys.get(i)
			    .get("name").toString(), initcitys.get(i)
			    .get("climate").toString(), initcitys.get(i)
			    .get("temp").toString());
		    Log.i("bai", "mCity11111" + mCity.getCityName());
		}
		pos = 0;
		try {
		    dongtai(0);
		} catch (IndexOutOfBoundsException e) {
		    e.printStackTrace();
		    try {
			initcitys.clear();
			initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
			initcitys = selectCitys(initcitys);
			for (int i = 0; i < initcitys.size(); i++) {
			    ManageCity mCity = new ManageCity(initcitys.get(i)
				    .get("name").toString(), initcitys.get(i)
				    .get("climate").toString(), initcitys
				    .get(i).get("temp").toString());
			    Log.i("bai", "mCity2222" + mCity.getCityName());
			}
			dongtai(0);

		    } catch (Exception e2) {
			e.printStackTrace();
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		}

	    }
	    if (EnvironmentCityManagerActivity.LOCATION_CHANGE.equals(intent
		    .getAction())) {
		isLocationChange = true;
		MyLog.i("isLocationChange :" + isLocationChange);
	    }
	    State wifiState = null;
	    State mobileState = null;
	    ConnectivityManager cm = (ConnectivityManager) context
		    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		    .getState();
	    mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		    .getState();
	    if (wifiState != null && mobileState != null
		    && State.CONNECTED != wifiState
		    && State.CONNECTED == mobileState) {
		mLocationClient.requestLocation();
		LocationService.sendGetLocationBroadcast(EnvironmentCurrentWeatherActivity.this);
		// 手机网络连接成功
	    } else if (wifiState != null && mobileState != null
		    && State.CONNECTED != wifiState
		    && State.CONNECTED != mobileState) {
		LocationService.sendGetLocationBroadcast(EnvironmentCurrentWeatherActivity.this);
		// 手机没有任何的网络
	    } else if (wifiState != null && State.CONNECTED == wifiState) {
		// 无线网络连接成功
		mLocationClient.requestLocation();
		LocationService.sendGetLocationBroadcast(EnvironmentCurrentWeatherActivity.this);
	    }

	}
    }

    private void setCostomMsg(String msg) {

	if (null != msgText) {
	    msgText.setText(msg);
	    msgText.setVisibility(android.view.View.VISIBLE);
	}
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // SUPPRESS
	// CHECKSTYLE

	return baos.toByteArray();
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (event.getAction() == KeyEvent.ACTION_DOWN
		&& keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
	    // ToQuitTheApp();
	    long currentTime = System.currentTimeMillis();
	    if ((currentTime - touchTime) >= waitTime) {
		Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
		touchTime = currentTime;
	    } else {
		mSpUtil2.SetDi(pos);
		finish();
		Intent intent = new Intent(
			WeiBaoApplication.LOCATION_SERVICEINTENT);
		EnvironmentCurrentWeatherActivity.this.stopService(intent);
		System.exit(0);
		android.os.Process
				.killProcess(android.os.Process.myPid());
	    }
	    return true;
	} else {
	    return super.onKeyDown(keyCode, event);
	}
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
	JPushInterface.init(getApplicationContext());
    }

    @Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
    @Override
    protected void onResume() {
	isForeground = true;
	super.onResume();
	MyLog.i("isLocationChange :" + isLocationChange);
//	if (isLocationChange) {
//	    try {
//		initcitys.clear();
//		initcitys = mCityDB
//			.queryBySqlReturnArrayListHashMap("select * from addcity");
//		initcitys = selectCitys(initcitys);
//		for (int i = 0; i < initcitys.size(); i++) {
//		    ManageCity mCity = new ManageCity(initcitys.get(i)
//			    .get("name").toString(), initcitys.get(i)
//			    .get("climate").toString(), initcitys.get(i)
//			    .get("temp").toString());
//		    Log.i("bai", "mCity2222" + mCity.getCityName());
//		}
//		resetView();
//		if(initcitys.size() > 0){
//		    dongtai(0);
//		}
//		
////		for (int i = 0; i < initcitys.size(); i++) {
////		    MyLog.i("loadCity :" + initcitys.get(i).get("name").toString());
////		    updateWeather1(initcitys.get(i).get("name").toString(),
////			    "7200000");
////		}
//		isLocationChange = false;
//	    } catch (Exception e) {
//		e.printStackTrace();
//	    }
//	}
    }

    @Override
    protected void onDestroy() {
	unregisterReceiver(mMessageReceiver);
	super.onDestroy();
	NetBroadcastReceiver.mListeners.remove(this);
    }

    class xx implements FilenameFilter {
	@Override
	public boolean accept(File dir, String filename) {
	    for (String ci : allcity) {
		if (filename.contains(ci)) {
		    return false;
		}
	    }

	    if (filename.contains("paiming")) {
		return false;
	    }

	    return true;
	}

    }

    public void startActivityForResult() {
	Intent add = new Intent(EnvironmentCurrentWeatherActivity.this,
		EnvironmentCityManagerActivity.class);
	startActivity(add);
    }

    LinearLayout city;

    public boolean bida(String param1, String param2) {

	int x = Integer.parseInt(param1);
	int y = Integer.parseInt(param2);
	String temp;
	if ((x - y) > 0) {
	    return true;
	} else {
	    return false;
	}

    }

    private void updateWeather1(String city, String hao) {
	// 因为在异步任务中有判断网络连接问题，在网络请求前先获取文件中的缓存，所以，此处未处理网络连接问题
	if (city == null) {
	    ToastUtil.showLong(mApplication, "未找到此城市,请重新定位或选择...");
	    onLoad();
	    return;
	}
	mTitleTextView.setText(city);
	reLoadWeather(city, hao);
    }

    private void reLoadWeather(String city, String hao) {
	if (loadWeather.getStatus() == AsyncTask.Status.PENDING) {
	    loadWeather.execute(city, hao);
	    if (!mCityDB.ci(city, Long.parseLong(hao))) {
		times[0].setText("同步中...");
		// T.showShort(this, "正在更新天气...");
	    }
	    mTitleTextView.setText(city);
	    WeiBaoApplication.selectedCity = city;
	} else if (loadWeather.getStatus() == AsyncTask.Status.RUNNING) {
	    Log.i("CurrentTq",
		    this.getResources().getString(R.string.loading_data));
	} else if (loadWeather.getStatus() == AsyncTask.Status.FINISHED) {
	    loadWeather = new LoadWeatherTask();
	    loadWeather.execute(city, hao);
	    mTitleTextView.setText(city);
	    WeiBaoApplication.selectedCity = city;
	}
    }

    class LoadWeatherTask extends AsyncTask<String, Void, Sweather> {

	// boolean isCurrentCity = false;
	// private NearestPm nearestPm = null;

	String city = null;

	@Override
	protected Sweather doInBackground(String... params) {
	    BusinessSearch search = new BusinessSearch();
	    city = params[0];
	    // if (mCityDB.isHaveNearestPm(params[0],
	    // mApplication.getCurrentCityLongitude(),
	    // mApplication.getCurrentCityLatitude(), 3600000)) {
	    // nearestPm = mCityDB.getNearestPm(params[0]);
	    // MyLog.i("nearestPm mCityDB.nearestPm:" + nearestPm);
	    // }
	    // if (null == nearestPm && null != params[0]
	    // && null != mApplication.getDingweicity()
	    // && params[0].equals(mApplication.getDingweicity())
	    // && null != mApplication.getCurrentCityLatitude()
	    // && null != mApplication.getCurrentCityLongitude()) {
	    // isCurrentCity = true;
	    // String getCurrentLocationurl = UrlComponent
	    // .getLocationPmInfoByCity_Get(
	    // mApplication.getCurrentCityLongitude(),
	    // mApplication.getCurrentCityLatitude(),
	    // mApplication.getDingweicity());
	    // MyLog.i("getLocationPmInfoByCity_Get load url:"
	    // + getCurrentLocationurl);
	    // try {
	    // nearestPm =
	    // search.getNearestPm(getCurrentLocationurl,mApplication.getDingweicity(),
	    // mApplication.getCurrentCityLongitude(),mApplication.getCurrentCityLatitude());
	    // } catch (IOException e) {
	    // e.printStackTrace();
	    // }
	    // if(null == nearestPm){
	    // if (mCityDB.isExitCurrentPm(params[0])) {
	    // nearestPm = mCityDB.getNearestPm(params[0]);
	    // }
	    // }else{
	    // mCityDB.insertCurrentPm(nearestPm);
	    // }
	    // } else {
	    // isCurrentCity = false;
	    // }
	    if (mCityDB.ci(params[0], Long.parseLong(params[1]))) {
		sweather = mCityDB.getweather(params[0]);
		MyLog.i("sweather mCityDB.getweather:" + sweather);
		return sweather;
	    }
	    String d = mCityDB.getNumber(params[0]);
	    if ("".equals(d)) {
		return null;
	    }
	    String url = UrlComponent.getWeatherInfoNowByCity_Get(d);
	    MyLog.i("getWeatherInfoNowByCity_Get load url:" + url);
	    try {
		sweather = search.getNowWeather(url, params[0]);
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    try {
		if (null == sweather) {
		    if (mCityDB.ci(params[0])) {
			sweather = mCityDB.getweather(params[0]);
			return sweather;
		    }
		    return null;
		}
		*//**
		 * 如果刷新出来的数据中显示的是暂无，那么就不更新数据。第一次进来的param为7200000
		 *//*
		if ("60000".equals(params[1])
			&& (null == sweather.getPM2Dot5Data()
				|| "".equals(sweather.getPM2Dot5Data()) || "暂无"
				    .equals(sweather.getPM2Dot5Data()))) {
		    return null;
		}
		mCityDB.cha(sweather);
		return sweather;
	    } catch (Exception e) {
		sweather = mCityDB.getweather(params[0]);
		return sweather;
	    }
	}

	@Override
	protected void onPostExecute(final Sweather result) {
		Time t = new Time();
		t.setToNow();
		int hour = t.hour;
	    onLoad();
	    if (result != null) {
		if (null != sweatherMap) {
		    if (sweatherMap.containsKey(pos)) {
			sweatherMap.remove(pos);
			sweatherMap.put(pos, result);
		    } else {
			sweatherMap.put(pos, result);
		    }
		    if (null != result.getCity()
			    && null != mApplication.getDingweicity()
			    && result.getCity().equals(
				    mApplication.getDingweicity())
			    && null != mApplication.getCurrentCityLatitude()
			    && null != mApplication.getCurrentCityLongitude()) {
			LoadWeatherNearestPmTask loadWeatherNearestPmTask = new LoadWeatherNearestPmTask();
			loadWeatherNearestPmTask.execute(city);
		    }
		}
		String tempArray[] = result.getTemp().replace("℃", "")
			.split("~");
		String tianqi = result.getWeather();

		if (mSpUtil2.getnotificationShow()) {

		    if (bida(tempArray[0], tempArray[1])) {
			NotificationUtils.show(
				EnvironmentCurrentWeatherActivity.this,
				getWeatherIcon(tianqi),
				mApplication.selectedCity, result.getWeather(),
				result.getTemp(), result.getLevel(),
				result.getFeelTemp(), result.getRealTime(),
				tempArray[0] + "℃", tempArray[1] + "℃");
		    } else {
			NotificationUtils.show(
				EnvironmentCurrentWeatherActivity.this,
				getWeatherIcon(tianqi),
				mApplication.selectedCity, result.getWeather(),
				result.getTemp(), result.getLevel(),
				result.getFeelTemp(), result.getRealTime(),
				tempArray[1] + "℃", tempArray[0] + "℃");
		    }
		} else {
		    notificationManager
			    .cancel(WeiBaoApplication.NOTIFICATION_ID1);
		}

		rain.setVisibility(View.GONE);
		if (result.getWeather().length() >= 2) {
			String weather = result.getWeather();
			String firstweather = weather.substring(0, 2);
			if (firstweather.contains("晴")) {
				is_qing = true;
				flLayout.removeView(wumaiView);
				flLayout.removeView(cloudView2);
				if(hour >= 19 || hour <= 6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye)));
				}else {
					ivSun.setVisibility(View.VISIBLE);
					Animation sunAnimation = AnimationUtils.loadAnimation(
							EnvironmentCurrentWeatherActivity.this,
							R.anim.sun);
					ivSun.startAnimation(sunAnimation);
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_qing)));
				}
				yyue = "qing";
			} else if (firstweather.contains("雨")) {

				is_rain = true;

				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				// 更新当前雨天
				update();
				rain.setVisibility(View.VISIBLE);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_yu)));
				}
				yyue = "yu";
			} else if (firstweather.contains("雪")) {
				is_snow = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_xue)));
				}
				yyue = "xue";
			} else if (firstweather.contains("云")) {
				is_cloud = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				if(hour >= 19 || hour <= 6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.yun)));
				}
					cloudView2 = new DynamicWeatherCloudyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yjjc_h_a3, 80, height / 5, 10);
				*//**
				 * 防止云的后面出现一群重复的云
				 * 
				 *//*
				if (isFirst_cloud) {
					flLayout.addView(cloudView2);
					isFirst_cloud = false;
				}
				cloudView2.move();
				yyue = "yun";
			}else if ( firstweather.contains("阴")) {
				is_cloud = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_yun)));
				}
						cloudView2 = new DynamicWeatherCloudyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yjjc_h_a3, 80, height / 5, 10);
				*//**
				 * 防止云的后面出现一群重复的云
				 * 
				 *//*
				if (isFirst_cloud) {
					flLayout.addView(cloudView2);
					isFirst_cloud = false;
				}
				cloudView2.move();
				yyue = "yun";
				// mSoundManager.playSound("yun");
			} else if (firstweather.contains("雾")) {
				is_wumai = true;
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.wu)));
				}
				wumaiView = new DynamicWeatherWuMaiyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yun, -150, height / 3, 20);
				flLayout.addView(wumaiView);
				wumaiView.move();
				yyue = "wu";
			} else if (firstweather.contains("霾")) {
				is_wumai = true;
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.mai)));
				}
				wumaiView = new DynamicWeatherWuMaiyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yun, -150, height / 3, 20);
				flLayout.addView(wumaiView);
				wumaiView.move();
				yyue = "wu";
			} else if (result.getWeather().contains("雨")) {

				is_rain = true;

				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				// 更新当前雨天
				update();
				rain.setVisibility(View.VISIBLE);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_yu)));
				}
				yyue = "yu";
			} else if (result.getWeather().contains("雪")) {
				is_snow = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_xue)));
				}
				yyue = "xue";
				// mSoundManager.playSound("");
			} else if (result.getWeather().contains("云")) {
				is_cloud = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.yun)));
				}
						cloudView2 = new DynamicWeatherCloudyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yjjc_h_a3, 80, height / 5, 10);
				*//**
				 * 防止云的后面出现一群重复的云
				 * 
				 *//*
				if (isFirst_cloud) {
					flLayout.addView(cloudView2);
					isFirst_cloud = false;
				}
				cloudView2.move();
				yyue = "yun";
				// mSoundManager.playSound("yun");
			}else if (result.getWeather().contains("阴")) {
					is_cloud = true;
					flLayout.removeView(wumaiView);
					ivSun.setVisibility(View.GONE);
					if(hour>=19 || hour<=6){
						bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
								EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
					}else {
						bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
								EnvironmentCurrentWeatherActivity.this, R.drawable.c_yun)));
					}
							cloudView2 = new DynamicWeatherCloudyView(
							EnvironmentCurrentWeatherActivity.this,
							R.drawable.yjjc_h_a3, 80, height / 5, 10);
					*//**
					 * 防止云的后面出现一群重复的云
					 * 
					 *//*
					if (isFirst_cloud) {
						flLayout.addView(cloudView2);
						isFirst_cloud = false;
					}
					cloudView2.move();
					yyue = "yun";
					// mSoundManager.playSound("yun");
			} else if (result.getWeather().equals("晴")) {
				is_qing = true;
				flLayout.removeView(wumaiView);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye)));
				}else {
					ivSun.setVisibility(View.VISIBLE);
					Animation sunAnimation = AnimationUtils.loadAnimation(
							EnvironmentCurrentWeatherActivity.this,
							R.anim.sun);
					ivSun.startAnimation(sunAnimation);
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_qing)));
				}
				yyue = "qing";

			} else if (result.getWeather().contains("雾")) {
				is_wumai = true;
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.wu)));
				}
				wumaiView = new DynamicWeatherWuMaiyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yun, -150, height / 3, 20);
				flLayout.addView(wumaiView);
				wumaiView.move();
				yyue = "wu";

			}else if (result.getWeather().contains("霾")) {
				is_wumai = true;
				ivSun.setVisibility(View.GONE);
				flLayout.removeView(cloudView2);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.mai)));
				}
				wumaiView = new DynamicWeatherWuMaiyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yun, -150, height / 3, 20);
				flLayout.addView(wumaiView);
				wumaiView.move();
				yyue = "wu";

			}
		} else if (result.getWeather().contains("雨")) {

			is_rain = true;

			flLayout.removeView(wumaiView);
			ivSun.setVisibility(View.GONE);
			flLayout.removeView(cloudView2);
			// 更新当前雨天
			update();
			rain.setVisibility(View.VISIBLE);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
			}else {
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.c_yu)));
			}

			yyue = "yu";
		} else if (result.getWeather().contains("雪")) {
			is_snow = true;
			flLayout.removeView(wumaiView);
			ivSun.setVisibility(View.GONE);
			flLayout.removeView(cloudView2);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
			}else {
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.c_xue)));
			}
			yyue = "xue";
			// mSoundManager.playSound("");
		} else if (result.getWeather().contains("云")) {
			is_cloud = true;
			flLayout.removeView(wumaiView);
			ivSun.setVisibility(View.GONE);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
			}else {
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.yun)));
			}
					cloudView2 = new DynamicWeatherCloudyView(
					EnvironmentCurrentWeatherActivity.this,
					R.drawable.yjjc_h_a3, 80, height / 5, 10);
			*//**
			 * 防止云的后面出现一群重复的云
			 * 
			 *//*
			if (isFirst_cloud) {
				flLayout.addView(cloudView2);
				isFirst_cloud = false;
			}
			cloudView2.move();
			yyue = "yun";
			// mSoundManager.playSound("yun");
		}else if (result.getWeather().contains("阴")) {
				is_cloud = true;
				flLayout.removeView(wumaiView);
				ivSun.setVisibility(View.GONE);
				if(hour>=19 || hour<=6){
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
				}else {
					bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
							EnvironmentCurrentWeatherActivity.this, R.drawable.c_yun)));
				}
						cloudView2 = new DynamicWeatherCloudyView(
						EnvironmentCurrentWeatherActivity.this,
						R.drawable.yjjc_h_a3, 80, height / 5, 10);
				*//**
				 * 防止云的后面出现一群重复的云
				 * 
				 *//*
				if (isFirst_cloud) {
					flLayout.addView(cloudView2);
					isFirst_cloud = false;
				}
				cloudView2.move();
				yyue = "yun";
				// mSoundManager.playSound("yun");
		} else if (result.getWeather().equals("晴")) {
			is_qing = true;
			flLayout.removeView(wumaiView);
			flLayout.removeView(cloudView2);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye)));
			}else {
				ivSun.setVisibility(View.VISIBLE);
				Animation sunAnimation = AnimationUtils.loadAnimation(
						EnvironmentCurrentWeatherActivity.this, R.anim.sun);
				ivSun.startAnimation(sunAnimation);
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.c_qing)));
			}
			yyue = "qing";

		} else if (result.getWeather().contains("雾")) {
			is_wumai = true;
			ivSun.setVisibility(View.GONE);
			flLayout.removeView(cloudView2);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
			}else {
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.wu)));
			}
			wumaiView = new DynamicWeatherWuMaiyView(
					EnvironmentCurrentWeatherActivity.this,
					R.drawable.yun, -150, height / 3, 20);
			flLayout.addView(wumaiView);
			wumaiView.move();
			yyue = "wu";

		} else if (result.getWeather().contains("霾")) {
			is_wumai = true;
			ivSun.setVisibility(View.GONE);
			flLayout.removeView(cloudView2);
			if(hour>=19 || hour<=6){
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.heiye_yin)));
			}else {
				bgChange.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
						EnvironmentCurrentWeatherActivity.this, R.drawable.mai)));
			}
			wumaiView = new DynamicWeatherWuMaiyView(
					EnvironmentCurrentWeatherActivity.this,
					R.drawable.yun, -150, height / 3, 20);
			flLayout.addView(wumaiView);
			wumaiView.move();
			yyue = "wu";

		}
		String xxx = result.getPM2Dot5Data();
		if ("".equals(xxx)) {
		    xxx = "暂无";
		    today_climate[pos].setVisibility(View.GONE);
		    weather_quality[pos].setVisibility(View.GONE);
		    weather_pm25[pos].setVisibility(View.GONE);
		} else {
		    weather_quality[pos].setText(result.getLevel());
		    today_climate[pos].setVisibility(View.VISIBLE);
		    weather_quality[pos].setVisibility(View.VISIBLE);
		    weather_pm25[pos].setVisibility(View.VISIBLE);
		}
		MyLog.i("result.getCity()" + result.getCity());
		MyLog.i("mApplication.getDingweicity()"
			+ mApplication.getDingweicity());
		// if (null != result.getCity()
		// && null != mApplication.getDingweicity()
		// && result.getCity().equals(
		// mApplication.getDingweicity())
		// && null != mApplication.getCurrentCityLatitude()
		// && null != mApplication.getCurrentCityLongitude()
		// && null != nearestPm) {
		// MyLog.i("nearestPm.getPm25() :" + nearestPm.getPm25());
		// weather_pm25[pos].setText(nearestPm.getPm25());
		// if (null != nearestPm) {
		// times[pos].setText(result.getRealTime() + ","
		// + nearestPm.getPosition_name() + "监测点");
		// }
		// } else {
		weather_pm25[pos].setText(xxx);
		times[pos].setText(result.getRealTime() + "");
		// }

		if (null == result.getFeelTemp()
			|| "暂无实况℃".equals(result.getFeelTemp())) {
		    weather_temperature[pos].setText("--");
		} else {
		    weather_temperature[pos].setText((result.getFeelTemp())
			    .substring(0, (result.getFeelTemp()).length() - 1)
			    + "°");
		}
		setDateToTextView(weather_wind_direction[pos],
			result.getWindDirection());
		setDateToTextView(weather_wind_speed[pos],
			result.getWindSpeed());
		setDateToTextView(weather_sidu[pos], "湿度" + result.getSidu());

		setDateToTextView(weather_temp[pos], result.getTemp());
		if (null != result.getWeather()) {
		    String weather = result.getWeather();
		    if (weather.contains(",")) {
			weather = weather.substring(0, weather.indexOf(","));
		    }
		    if (weather.contains("、")) {
			weather = weather.substring(0, weather.indexOf("、"));
		    }
		    setDateToTextView(weather_climate[pos], weather);
		} else {
		    setDateToTextView(weather_climate[pos], result.getWeather());
		}
		setDateToTextView(weather_today[pos], result.getWeekday());
		setDateToTextView(weather_date[pos], result.getDate());
		setDateToTextView(weather_lunar[pos], result.getLunar());
		sharedPreference = getSharedPreferences("sharedPreference",
			MODE_PRIVATE);

		editor = sharedPreference.edit();
		editor.putString(result.getCity() + "weather_temperature",
			result.getFeelTemp());
		editor.putString(result.getCity() + "weather_climate",
			result.getTemp());
		editor.putString(result.getCity() + "weather_temp",
			result.getWeather());
		editor.putString(result.getCity() + "pm", result.getLevel());
		editor.putString(result.getCity(), result.getCity());
		editor.putString(result.getCity() + "weather_date",
			result.getDate());
		editor.putString(result.getCity() + "weather_today",
			result.getWeekday());
		refresh_date = myFmt.format(now);
		refresh_time = timeNow.format(now);
		editor.putString(result.getCity() + "refresh_date",
			refresh_date);
		editor.putString(result.getCity() + "refresh_time",
			refresh_time);
		editor.commit();

		if (!"".equals(mApplication.getXiangxidizhi())
			&& mApplication.getXiangxidizhi() != null
			&& mSpUtil2.getisDingwei()) {

		    if (mApplication.getDingweicity().equals(
			    mTitleTextView.getText().toString())) {
			MyLog.i("dingweichengshi1 View.VISIBLE");
			dingweichengshi[pos].setVisibility(View.VISIBLE);
			dingweichengshi[pos].setText(mApplication
				.getXiangxidizhi());
		    } else {
			dingweichengshi[pos].setVisibility(View.GONE);
			MyLog.i("dingweichengshi1 View.GONE");
		    }

		} else {

		    MyLog.i("dingweichengshi2 View.GONE");
		    dingweichengshi[pos].setVisibility(View.GONE);
		}

		String x = "0";
		if (!"".equals(result.getPM2Dot5Data())) {
		    x = result.getPM2Dot5Data();
		}
		int aqi = Integer.parseInt(x);
		// if (null != nearestPm && null != nearestPm.getPm25()) {
		// aqi = Integer.parseInt(nearestPm.getPm25());
		// if (aqi > 300) {
		// forecast_icon[pos].setImageResource(R.drawable.dead);
		// weather_quality[pos].setText("严重污染");
		// } else if (aqi > 200) {
		// forecast_icon[pos].setImageResource(R.drawable.heavy);
		// weather_quality[pos].setText("重度污染");
		// } else if (aqi > 150) {
		// forecast_icon[pos].setImageResource(R.drawable.medium);
		// weather_quality[pos].setText("中度污染");
		// } else if (aqi > 100) {
		// forecast_icon[pos].setImageResource(R.drawable.mild);
		// weather_quality[pos].setText("轻度污染");
		// } else if (aqi > 50) {
		// forecast_icon[pos].setImageResource(R.drawable.fine);
		// weather_quality[pos].setText("良");
		// } else {
		// forecast_icon[pos]
		// .setImageResource(R.drawable.excellent);
		// weather_quality[pos].setText("优");
		// }
		// } else {
		if (aqi > 300) {
		    forecast_icon[pos].setImageResource(R.drawable.dead);
		} else if (aqi > 200) {
		    forecast_icon[pos].setImageResource(R.drawable.heavy);
		} else if (aqi > 150) {
		    forecast_icon[pos].setImageResource(R.drawable.medium);
		} else if (aqi > 100) {
		    forecast_icon[pos].setImageResource(R.drawable.mild);
		} else if (aqi > 50) {
		    forecast_icon[pos].setImageResource(R.drawable.fine);
		} else {
		    forecast_icon[pos].setImageResource(R.drawable.excellent);
		}
		// }
	    }
	    // TODO Auto-generated method stub
	    today_climate[pos].setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    MyLog.i(weather_pm25[pos].getText().toString());
		    if ("暂无".equals(weather_pm25[pos].getText())) {
			return;
		    }
		    
		    MyLog.i("MynearestPm :" + MynearestPm);
		    MyLog.i("nearestPm boolean :" + (null != mApplication.getDingweicity()
			    && mApplication.getDingweicity().equals(
				    mTitleTextView.getText().toString())));
		    Intent intent = new Intent(
			    EnvironmentCurrentWeatherActivity.this,
			    EnvironmentAQIDetailActivity.class);
		    if (null != mApplication.getDingweicity()
			    && mApplication.getDingweicity().equals(
				    mTitleTextView.getText().toString())
			    && null != MynearestPm) {

			intent.putExtra("from", "dingwei");
			if ("0".equals(MynearestPm.getPm25())) {
			    intent.putExtra("pm", result.getPM2Dot5Data());
			} else {
			    intent.putExtra("pm", MynearestPm.getPm25());
			}
			intent.putExtra("position_name",
				MynearestPm.getPosition_name());
			intent.putExtra("level", weather_quality[pos].getText()
				.toString());
		    } else {
			intent.putExtra("from", "nodingwei");
		    }
		    intent.putExtra("city", mTitleTextView.getText().toString());
		    startActivity(intent);
		}
	    });

	    today_weather_layout[pos].setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub

		    Intent sevendayIntent = new Intent(
			    EnvironmentCurrentWeatherActivity.this,
			    EnvironmentWeatherTrendActivity.class);
		    startActivity(sevendayIntent);
		    // reflv.setVisibility(View.VISIBLE);

		}
	    });

	    super.onPostExecute(result);
	}

    }

    int pos = 0;

    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {

	    switch (msg.what) {
	    case 100:
		if (mSpUtil2.getBeijing()) {
		    // mSoundManager.playSound(yyue);
		}

		break;

	    default:
		break;
	    }

	};
    };

    private void updateWeatherInfo1(WeatherInfo allWeather) {
	if (allWeather != null) {
	    times[0].setText(allWeather.getCity() + allWeather.getFeelTemp());

	}

    }

    String sskqzs = "150";

    *//**
     * 更新aqi界面
     *//*
    WeatherInfo chuandi;

    private int getWeatherIcon(String climate) {
	int weatherIcon = R.drawable.weather_icon_qingtian;
	String climateString = CommonUtil.getWeatherIconString(climate,0);
	if (mApplication.getWeatherIconMap().containsKey(climateString)) {
	    weatherIcon = mApplication.getWeatherIconMap().get(climateString);
	}
	return weatherIcon;
    }

    BDLocationListener mLocationListener = new BDLocationListener() {

	double longitude;
	double latitude;

//	@Override
//	public void onReceivePoi(BDLocation arg0) {
//	    // do nothing
//	}

	@Override
	public void onReceiveLocation(BDLocation location) {
	    try {
	    MyLog.i("onReceiveLocation" + location);
	    if (location == null || TextUtils.isEmpty(location.getCity())) {
		return;
	    }
	    // 获取当前城市
	    cityName = location.getCity();
	    String xiangxidi = location.getAddrStr();

	    String province = location.getProvince();
	    latitude = location.getLatitude();
	    longitude = location.getLongitude();
	    if (!"".equals(cityName)) {
		if (cityName.contains("市")) {
		    cityName = cityName.substring(0, cityName.length() - 1);
		}
		if (cityName.contains("地区")) {
			cityName = cityName.substring(0, cityName.length() - 2);
		    }
//		CommonUtil.sendLocationCityChangeBoradcast(
//			EnvironmentCurrentWeatherActivity.this, cityName);
		mApplication.setDingweicity(cityName);
		mApplication.setCurrentCityLatitude(latitude + "");
		mApplication.setCurrentCityLongitude(longitude + "");
		mApplication.setXiangxidizhi(xiangxidi);
		mApplication.setProvince(province);

		if (!"".equals(cityName)) {
		    WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(), false ,cityName);
		}
		SharedPreferences sharedPref = getSharedPreferences(
			"sharedPref", MODE_PRIVATE);
		editor = sharedPref.edit();
		editor.putString("dingweiCity", cityName);
		editor.putString("CurrentCityLongitude", longitude + "");
		editor.putString("CurrentCityLatitude", latitude + "");
		editor.putString("province", province);
		editor.putString("xiangxidi", xiangxidi);
		editor.commit();
		if (!"".equals(mApplication.getXiangxidizhi())
			&& mSpUtil2.getisDingwei()) {
		    if (iszero) {
			dingweichengshi[0].setVisibility(View.GONE);
		    } else {

			if (tuichujing > initcitys.size()) {
			    dingweichengshi[0].setText(mApplication
				    .getXiangxidizhi());
			} else {
			    dingweichengshi[tuichujing].setText(mApplication
				    .getXiangxidizhi());
			}

		    }

		} else {
		    dingweichengshi[0].setVisibility(View.GONE);
		}

	    }
	    // 定位成功后发送广播
	    if (xiangxidi != null && xiangxidi != "") {
		Intent intent = new Intent();
		intent.setAction(DING_WEI);
		intent.putExtra("dingwei", xiangxidi);
		intent.putExtra("city", cityName);
		sendBroadcast(intent);
	    }
	    mLocationClient.stop();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    };

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

    @Override
    public void onNetChange() {
	if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
	    ToastUtil.showLong(this, R.string.net_err);
	} else {
	    if (!TextUtils.isEmpty(mSpUtil.getCity()))
		mHandler.sendEmptyMessage(UPDATE_EXISTS_CITY);
	}
    }

    private String clientID;
    Uri uri;

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.title_city_manager_layout:
	    startActivityForResult();
	    break;
	case R.id.title_location:
	    if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
		if (sweather != null) {
		    synthetizeInSilence(mSpUtil2.getYuy(), 40, 100,
			    "微保为您播报" + mTitleTextView.getText().toString()
				    + "实时空气质量" + sweather.getLevel()
				    + "今天夜间到明天白天 天气状况" + sweather.getWeather()
				    + "温度" + sweather.getTemp() + "风向"
				    + sweather.getWindDirection());
		}

	    }

	    break;
	case R.id.title_share:
	    if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
		Toast.makeText(EnvironmentCurrentWeatherActivity.this,
			"请检查您的网络", 0).show();
		return;
	    }
	    Bitmap bitmap = CommonUtil
		    .GetCurrentScreen(EnvironmentCurrentWeatherActivity.this);
	    try {
			uri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), bitmap, null, null));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentCurrentWeatherActivity.this, "截图失败", 0).show();
			return;
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentCurrentWeatherActivity.this, "截图失败", 0).show();
			return;
		}
	    if (sweather != null) {
		content = getShareContent(sweather);
	    } else {
		content = "微保环境，共建美好环境";
	    }

	    mSocialShare = Frontia.getSocialShare();
	    mSocialShare.setContext(this);
	    mSocialShare = Frontia.getSocialShare();
	    mSocialShare.setContext(this);
	    mSocialShare.setClientId(MediaType.WEIXIN.toString(),
		    "wx541df6ed6e9babc0");
	    mSocialShare.setClientId(MediaType.SINAWEIBO.toString(),
		    "991071488");
	    mSocialShare
		    .setClientId(MediaType.QQFRIEND.toString(), "100358052");
	    mSocialShare.setParentView(getWindow().getDecorView());
	    mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "微保");
	    mImageContent.setTitle("微保");
	    mImageContent.setContent(content);
	    mImageContent.setLinkUrl("http://www.wbapp.com.cn");
	    mImageContent.setImageUri(uri);
	    mImageContent.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
	    mImageContent.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
	    mImageContent.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
	    mImageContent.setImageData(bitmap);
	    // mSocialShare.show(CurrentTq.this.getWindow().getDecorView(),
	    // mImageContent, FrontiaTheme.LIGHT, new ShareListener());
	    mSocialShare.show(this.getWindow().getDecorView(), mImageContent,
		    FrontiaTheme.LIGHT, new ShareListener());

	    break;
	case R.id.title_update_progress:
	    if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
		updateWeather1(mTitleTextView.getText().toString(), "60000");

	    } else {
		ToastUtil.showShort(this, R.string.net_err);
	    }
	    break;

	default:
	    break;
	}
    }

    private String getShareContent(Sweather weatherInfo) {

	return String.format(
		mShare,
		new Object[] { mTitleTextView.getText().toString(),
			weatherInfo.getWeather(), weatherInfo.getTemp(),
			weatherInfo.getLevel(), weatherInfo.getPM2Dot5Data() });

    }

    *//**
     * 连续按两次返回键就退出
     *//*
    private long firstTime;

    @Override
    public void onBackPressed() {
	if (System.currentTimeMillis() - firstTime < 3000) {
	    finish();
	} else {
	    firstTime = System.currentTimeMillis();
	    ToastUtil.showShort(this, R.string.press_again_exit);

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

    private class ShareListener implements FrontiaSocialShareListener {

	@Override
	public void onSuccess() {
	    Log.d("Test", "share success");
	    Toast.makeText(EnvironmentCurrentWeatherActivity.this, "分享成功", 2000)
		    .show();
	}

	@Override
	public void onFailure(int errCode, String errMsg) {
	    Log.d("Test", "share errCode " + errCode);
	    Toast.makeText(EnvironmentCurrentWeatherActivity.this, "分享失败", 2000)
		    .show();
	}

	@Override
	public void onCancel() {
	    Log.d("Test", "cancel ");
	    // Toast.makeText(CurrentTq.this, "分享取消", 2000).show();
	}
    }

    @Override
    public void onRefresh() {
	LocationService.sendGetLocationBroadcast(EnvironmentCurrentWeatherActivity.this);
	if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
	    updateWeather1(mTitleTextView.getText().toString(), "60000");
	} else {
	    ToastUtil.showShort(this, R.string.net_err);
	    onLoad();
	}
    }

    class MyAdapter extends BaseAdapter {

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return 0;
	}

	@Override
	public Object getItem(int position) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public long getItemId(int position) {
	    // TODO Auto-generated method stub
	    return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    @Override
    public void onLoadMore() {
	// TODO Auto-generated method stub

    }

    private void onLoad() {
	if (null != weather_xListView && weather_xListView.length > pos) {
	    weather_xListView[pos].stopRefresh();
	}

    }

    private void setDateToTextView(TextView view, String data) {
	if (null == data || data.equals("") || data.contains("暂无实况")) {
	    view.setVisibility(View.GONE);
	} else {
	    view.setVisibility(View.VISIBLE);
	    view.setText(data);
	}

    }

    class LoadWeatherNearestPmTask extends AsyncTask<String, Void, NearestPm> {

	boolean isCurrentCity = false;
	private NearestPm nearestPm = null;

	@Override
	protected NearestPm doInBackground(String... params) {
	    BusinessSearch search = new BusinessSearch();
	    if (mCityDB.isHaveNearestPm(params[0],
		    mApplication.getCurrentCityLongitude(),
		    mApplication.getCurrentCityLatitude(), 3600000)) {
		nearestPm = mCityDB.getNearestPm(params[0]);
		MyLog.i("nearestPm mCityDB.nearestPm:" + nearestPm);
	    }
	    if (null == nearestPm && null != params[0]
		    && null != mApplication.getDingweicity()
		    && params[0].equals(mApplication.getDingweicity())
		    && null != mApplication.getCurrentCityLatitude()
		    && null != mApplication.getCurrentCityLongitude()) {
		isCurrentCity = true;
		String getCurrentLocationurl = UrlComponent
			.getLocationPmInfoByCity_Get(
				mApplication.getCurrentCityLongitude(),
				mApplication.getCurrentCityLatitude(),
				mApplication.getDingweicity());
		MyLog.i("getLocationPmInfoByCity_Get load url:"
			+ getCurrentLocationurl);
		try {
		    nearestPm = search.getNearestPm(getCurrentLocationurl,
			    mApplication.getDingweicity(),
			    mApplication.getCurrentCityLongitude(),
			    mApplication.getCurrentCityLatitude());
		} catch (IOException e) {
		    e.printStackTrace();
		}
		if (null == nearestPm) {
		    if (mCityDB.isExitCurrentPm(params[0])) {
			nearestPm = mCityDB.getNearestPm(params[0]);
		    }
		} else {
		    mCityDB.insertCurrentPm(nearestPm);
		}
	    } else {
		isCurrentCity = false;
	    }
	    return nearestPm;
	}

	@Override
	protected void onPostExecute(NearestPm result) {
	    super.onPostExecute(result);
	    MynearestPm = result;
	    if (null != result && null != result.getCity()
		    && null != mApplication.getDingweicity()
		    && result.getCity().equals(mApplication.getDingweicity())
		    && null != mApplication.getCurrentCityLatitude()
		    && null != mApplication.getCurrentCityLongitude()
		    && null != nearestPm) {
		MyLog.i("nearestPm.getPm25() :" + nearestPm.getPm25());
		if (null != nearestPm.getPm25()
			&& "0".equals(nearestPm.getPm25())) {
		    if ("".equals(sweatherMap.get(pos).getPM2Dot5Data())) {
			weather_pm25[pos].setText("暂无");
		    } else {
			weather_pm25[pos].setText(sweatherMap.get(pos)
				.getPM2Dot5Data());
		    }
		} else {
		    weather_pm25[pos].setText(nearestPm.getPm25());
		    // sweatherMap.get(pos).result.getPM2Dot5Data()

		}
		if (null != nearestPm && null != sweatherMap.get(pos)) {
		    times[pos].setText(sweatherMap.get(pos).getRealTime() + ","
			    + nearestPm.getPosition_name() + "监测点");
		}
	    }
	    if (null != nearestPm && null != nearestPm.getPm25()) {
	    	if (nearestPm.getPm25().equals("0")) {
	    		MyLog.i("abc=");
				nearestPm.setPm25("-");
			}
		int aqi = Integer.parseInt(nearestPm.getPm25());
		if (aqi > 300) {
		    forecast_icon[pos].setImageResource(R.drawable.dead);
		    weather_quality[pos].setText("严重污染");
		} else if (aqi > 200) {
		    forecast_icon[pos].setImageResource(R.drawable.heavy);
		    weather_quality[pos].setText("重度污染");
		} else if (aqi > 150) {
		    forecast_icon[pos].setImageResource(R.drawable.medium);
		    weather_quality[pos].setText("中度污染");
		} else if (aqi > 100) {
		    forecast_icon[pos].setImageResource(R.drawable.mild);
		    weather_quality[pos].setText("轻度污染");
		} else if (aqi > 50) {
		    forecast_icon[pos].setImageResource(R.drawable.fine);
		    weather_quality[pos].setText("良");
		}  else if(aqi == 0){
//		    forecast_icon[pos].setImageResource(R.drawable.excellent);
//		    weather_quality[pos].setText("优");
		}else  {
		    forecast_icon[pos].setImageResource(R.drawable.excellent);
		    weather_quality[pos].setText("优");
		}
	    }

	}
	
    }
    
    private ArrayList<HashMap<String, Object>> selectCitys(ArrayList<HashMap<String, Object>> citys){
	    try {
	    if(null != citys && citys.size() > 0){
		    for (int i = 0; i < citys.size(); i++) {
			if("1".equals(citys.get(i).get("islocation"))){
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
}*/