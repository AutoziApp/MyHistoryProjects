package com.jy.environment.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

import cn.qqtheme.framework.picker.DatePicker;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIMediaObject;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQFlagType;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQReqestType;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.jy.environment.R;
import com.jy.environment.activity.UpdateNotificationActivity.ICallbackResult;
import com.jy.environment.adapter.WeatherAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.AQI;
import com.jy.environment.model.AqiStationModel;
import com.jy.environment.model.CityDetails;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.EnvironmentForecastWeekModel;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.ListHumiDityModel;
import com.jy.environment.model.ListPolluctionModel;
import com.jy.environment.model.NO2;
import com.jy.environment.model.PM10Info24H;
import com.jy.environment.model.PM25;
import com.jy.environment.model.PmDayHourModel;
import com.jy.environment.model.PmModel;
import com.jy.environment.model.SO2;
import com.jy.environment.model.ThreeDayForestModel;
import com.jy.environment.model.Trend;
import com.jy.environment.model.TrendModel;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7_tian;
import com.jy.environment.model.WeatherInfoMonth;
import com.jy.environment.model.WeatherInfoYear;
import com.jy.environment.model.WindModel;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.okUtils;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.util.timepicker.NumericWheelAdapter;
import com.jy.environment.util.timepicker.OnWheelScrollListener;
import com.jy.environment.util.timepicker.WheelView;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.FixGridLayout;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 环境天气趋势界面
 * 
 * @author baiyuchuan
 * 
 */
@SuppressLint("NewApi")
public class EnvironmentWeatherRankkActivity extends ActivityBase implements
		OnClickListener {
	int[] aqiOf24;
	int[] pm10Of24;
	int[] pm25Of24;
	int[] so2Of24;//新增 so2
	int[] no2Of24;//新增 no2
	
	private long exitTime = 0;

	private TextView city_name;
	private TextView aqi_du_value;
	private TextView aqi_value;
	private TextView baifenbi_value;
	private TextView paiming_value;
	private LinearLayout station_layout, station_title_layout,
			environment_wind_shidu;
	private GetWeatherRankActivity rankActivity;
	private TextView environment_rank_details_dw, text_dingwei,
			environment_rank_details_dw_sz, environment_rank_details_wr,
			environment_rank_details_rk, environment_rank_details_pm25,
			environment_rank_details_pm10, environment_rank_details_no2,
			environment_rank_details_so2, environment_rank_details_co,
			environment_rank_details_o3, environment_rank_details_ts,
			environment_rank_details_tv6, environment_rank_details_tv7,
			environment_rank_details_tv8, environment_rank_details_tv9,
			environment_rank_details_tv10, environment_rank_details_tv11,
			environment_rank_details_tv12, environment_rank_details_tv13,
			environment_rank_details_tv14, environment_rank_details_tv15,
			environment_rank_detailstv16, environment_path, environment_path24,
			environment_pathyear, environment_pathmonth, environment_wind,
			detail_polluction, environment_path24_pm25,
			environment_path24_so2,//新
			environment_path24_no2,//新
			environment_path24_pm10;
	private ImageView environment_rank_details_back;
	private TableRow textv_nodata, tableRow1, tableRow2;
	public static int[] aqi24_Lists = new int[24];
	int[] pm10_24_Lists = new int[24];
	int[] pm25_24_Lists = new int[24];
	int[] so2_24_Lists = new int[24];//新
	int[] no2_24_Lists = new int[24];//新
	int[] aqi_Lists = new int[7];
	int[] aqi_years = new int[12];
	int[] aqi_months = new int[31];
	// int[] wind_Lists = new int[24];
	int[] shidu_Lists = new int[24];
	private CityDetails details;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private Uri uri;
	public static String[] days_data = new String[7];
	public static String[] hours = new String[24];
	public static String[] hours25 = new String[24];
	public static String[] hours10 = new String[24];
	public static String[] years = new String[12];
	public static String[] months = new String[31];
	public static String[] wind = new String[24];
	public static String[] shidu = new String[24];
	public static String[] initialHoursAQI = new String[24];
	public static String[] initialHoursPM25 = new String[24];
	public static String[] initialHoursPM10 = new String[24];
	public static String[] initialHoursSO2 = new String[24];//新
	public static String[] initialHoursNO2 = new String[24];//新
	public static String[] initialWeeks = new String[7];
	public static String[] initialMonths = new String[31];
	private List<WeatherInfo7_tian> lists_7tian = new ArrayList<WeatherInfo7_tian>();
	private List<WeatherInfo24> weatherLists = new ArrayList<WeatherInfo24>();
	private List<PM25> pm25List = new ArrayList<PM25>();
	private List<PM10Info24H> pm10List = new ArrayList<PM10Info24H>();
	private List<WeatherInfoYear> weInfoYear = new ArrayList<WeatherInfoYear>();
	private List<WeatherInfoMonth> weInfomInfoMonths = new ArrayList<WeatherInfoMonth>();
	private List<WindModel> windModels = new ArrayList<WindModel>();
	private List<ListHumiDityModel> humiDityModels = new ArrayList<ListHumiDityModel>();
	/**
	 * 24小时AQI
	 */
	private ColumnChartView pv_24th;
	private ColumnChartView pathView_24h_pm10;
	private ColumnChartView pathView_24h_pm25;
	private ColumnChartView pathView_24h_so2;//新
	private ColumnChartView pathView_24h_no2;//新
	private ColumnChartView pw;
	private ColumnChartView path_year;
	private ColumnChartView path_month;
	private Intent intent;
	private String lat, lng, city, pm25, level, position_name, content,
			dingweiString, factor_uptime;
	private RelativeLayout environment_rank_pm, relativeLayout4,
			city_paiming_value_layout;
	private LinearLayout layout_dingwei;
	private GetAqiDetailItemTask itemTask;
	private TableLayout tav;
	Dialog dialog;
	private int cityLength, cityRank;
	private WeiBaoApplication mApplication;
	private CityDB mCityDB;
	private GetThreeDayForestTask getThreeDayForestTask;
	private String type = "2";
	private TextView monitor_pm25, monitor_pm10, monitor_03, monitor_so2,
			monitor_no2, monitor_co, tv_pollution_factor_time,
			tv_monitoring_station_time, tv_pollution_factor, rankk_update_time;
	private LinearLayout monitor_ll1, monitor_ll2, monitor_ll3;
	private RelativeLayout relayouty;
	public static List<AqiStationModel> aqiStationModels;
	private LineChartView monitor_chart;
	private FixGridLayout monitor_name;
	private GetPmTask pmTask;
	private List<PmModel> dayModels = new ArrayList<PmModel>();
	private Context context;
	private static List<String> listColors = new ArrayList<String>();
	private List<String> listShowLineNum = new ArrayList<String>();
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean hasAxes = true;
	private boolean hasAxesNames = false;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private boolean isFilled = false;
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = true;
	private LineChartData data;
	private CityDetails cityDetails;
	private TrendModel trendModel;
	private LinearLayout rl_aqi_24h_trend;
	private LinearLayout rl_aqi_30d_trend;
	private PopupWindow menuWindow;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private String dateMonth = "", date = "";
	private String dateStar = "";
	private TextView tv_time;
	private ImageView iv_time_icon;
	private static final int SEARCH = 0;
	private static final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	// 下载包保存路径
	private String savePath = Environment.getExternalStorageDirectory().toString() + "/weibao_agency_update/";
	private String apkName = "weibao_agency_AppUpdate.apk";
	private final String saveFileName = savePath + "weibao_agency_AppUpdate.apk";
	private String time;
	GetTrendTask getTrendTask;
	private List<AQI> listAQI7day;
	private List<PM10Info24H> pm10Info24Hs;
	private List<AQI> listAQI24h;
	private List<AQI> listAQI30d;
	private List<PM25> listPM25Of24h;
	private List<SO2> listSO2Of24h;//新增 so2
	private List<NO2> listNO2Of24h;//新增 no2
	private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SEARCH:
				date = (String) msg.obj;
				dateStar = (String) msg.obj;
				tv_time.setText(dateStar);
				getTrendTask = new GetTrendTask();
				getTrendTask.execute();
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_rankk_details);
		context = this;
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在加载");
		mApplication = WeiBaoApplication.getInstance();
		mCityDB = mApplication.getCityDB();
		// LocationService.sendGetLocationBroadcast(EnvironmentWeatherRankActivity.this);
		// intent = getIntent();
		listColors.add("#acd598");
		listColors.add("#00a8ff");
		listColors.add("#ffcf01");
		listColors.add("#00ffde");
		listColors.add("#f55521");
		listColors.add("#17b55f");
		listColors.add("#8c97cb");
		listColors.add("#a17e59");
		// lat = intent.getStringExtra("lat");
		// lng = intkent.getStringExtra("lng");
		lat = "0";
		lng = "0";
		city = "济源市";
		pm25 = "0";
		level = "";
		// city = intent.getStringExtra("city");
		// MyLog.i("city===" + city);
		// pm25 = intent.getStringExtra("pm25");
		// level = intent.getStringExtra("level");
		MyLog.i("dingweiStr:" + dingweiString);
		SharedPreferencesUtil preferencesUtil = SharedPreferencesUtil
				.getInstance(this);
		initView();
		if (dingweiString != null && preferencesUtil.getisDingwei()) {
			// layout_dingwei.setVisibility(View.VISIBLE);
			text_dingwei.setText(dingweiString);
		}
		// registerMessageReceiver();
		rankActivity = new GetWeatherRankActivity();
		if (city.contains("自治州")) {
			city = mCityDB.getSuoSuo(city);
		}

		rankActivity.execute(UrlComponent.getMapHiss, city);
		itemTask = new GetAqiDetailItemTask();
		itemTask.execute("");
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_time.setOnClickListener(this);
		iv_time_icon = (ImageView) findViewById(R.id.iv_time_icon);
		iv_time_icon.setOnClickListener(this);
		initCalendar(tv_time);
		getTrendTask = new GetTrendTask();
		getTrendTask.execute();
		//天气预报
		initWeatherView();
		final String urlcome = UrlComponent.currentWeatherGet("平顶山", "0", "0");
		requestSevenDayWeatherData(urlcome);
		
		//YYF sp
		shares = context.getSharedPreferences(SHARE_LOGIN_TAG,
				0);
		//YYF 更新检查
		PackageInfo info;
		String curVersionName="";
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
			curVersionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String gxUrl= "http://125.46.1.154:9090/api/v1/PdsApp/getVersion?version="+curVersionName+"&key=8763apk0998";
		requestUpdateYY(gxUrl);
	}

	/**
	 * YYF 异步请求是否有更新
	 * @param gxUrl 
	 */
	private void requestUpdateYY(final String gxUrl) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				okUtils.get(gxUrl, new StringCallback() {
					
					@Override
					public void onResponse(String result, int id) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							boolean haveNew = (Boolean) jsonObject.get("flag");
							String apkUrl = (String) jsonObject.get("url");
							String msg = (String) jsonObject.get("msg");
							if(haveNew){
								//有更新
								showNoticeDialogYYF(apkUrl);
							}else{
								//已是最新
//								showToastShort("已是最新");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onError(Call call, Exception e, int id) {
						
					}
				});
			}
		}).start();
	}
	
	/**
	 * YYF 显示版本更新通知对话框
	 * 
	 * @param url
	 */
	private void showNoticeDialogYYF(final String url) {
		AlertDialog.Builder builder = new Builder(context);
		
		builder.setTitle("提示");
		builder.setMessage("我们有新版本发布了，请及时更新！");
		WeiBaoApplication app = WeiBaoApplication.getInstance();
		final DecimalFormat decimalFormat =new DecimalFormat("0.00");

		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				File apkf = new File(saveFileName);
				if(shares.getBoolean("down", false)&&apkf.exists()){
					Toast.makeText(context, "正在安装,请稍后", Toast.LENGTH_LONG).show();
					installApk();
				}else{
					showDownloadDialog();
					Toast.makeText(context, "正在下载,请稍后", Toast.LENGTH_LONG).show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							okUtils.downloadFile(mProgress, url, new FileCallBack(
									savePath, apkName) {
								
							@Override
							public void inProgress(float progress, long total,
									int id) {
								mProgress.setProgress((int) (100 * progress));
								String format = decimalFormat.format(progress*total/1024/1024);
								mProgressText.setText(format +"M"+"/"+total/1024/1024+"M");
							}
								
								@Override
								public void onResponse(File file, int id) {
									downloadDialog.dismiss();
									Editor edit = shares.edit();
									edit.putBoolean("down", true).commit();
									Log.e("YYF", "onResponse :" + file.getAbsolutePath());
									Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
									installApk();
								}
								
								@Override
								public void onError(Call call, Exception e, int id) {
									downloadDialog.dismiss();
									Editor edit = shares.edit();
									edit.putBoolean("down", false).commit();
									Log.e("YYF", "onError :" + e.getMessage());
									Toast.makeText(context, "下载异常", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}).start();
				}
			}
		});
		builder.setNeutralButton("重新下载", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				File apkf = new File(saveFileName);
				if(apkf.exists()){
					apkf.delete();
				}
				showDownloadDialog();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						okUtils.downloadFile(mProgress, url, new FileCallBack(
								savePath, apkName) {
							
							@Override
							public void inProgress(float progress, long total,
									int id) {
								mProgress.setProgress((int) (100 * progress));
								String format = decimalFormat.format(progress*total/1024/1024);
								mProgressText.setText(format +"M"+"/"+total/1024/1024+"M");
							}
							
							@Override
							public void onResponse(File file, int id) {
								downloadDialog.dismiss();
								Editor edit = shares.edit();
								edit.putBoolean("down", true).commit();
								Log.e("YYF", "onResponse :" + file.getAbsolutePath());
								Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
								installApk();
							}
							
							@Override
							public void onError(Call call, Exception e, int id) {
								downloadDialog.dismiss();
								Editor edit = shares.edit();
								edit.putBoolean("down", false).commit();
								Log.e("YYF", "onError :" + e.getMessage());
								Toast.makeText(context, "下载异常", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();
			}
		});
		builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog noticeDialog = builder.create();
		if (!isBackground(context)) {
			noticeDialog.show();
		}
	}
	
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setCancelable(false);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.updateyy, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progressbarYY);
		mProgressText = (TextView) v.findViewById(R.id.currentPosYY);

		builder.setView(v);
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
	}
	
	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		callback.OnBackResult("finish");

	}
	
	private ICallbackResult callback = new ICallbackResult() {

		@Override
		public void OnBackResult(Object result) {
			if ("finish".equals(result)) {

				return;
			}
			int i = (Integer) result;

		}

	};

	private void initWeatherView() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		GridView gvWeather = (GridView) findViewById(R.id.gv_weather);
		gvWeather.setSelector(new ColorDrawable(Color.TRANSPARENT));
		weatherData = new ArrayList<Trend>();
		gvWeatherAdapter = new GvWeatherAdapter(EnvironmentWeatherRankkActivity.this,weatherData);
		gvWeather.setAdapter(gvWeatherAdapter);
	}
	
	private class GvWeatherAdapter extends BaseAdapter{
		private Context mContext;
		private List<Trend> weatherData;
		public GvWeatherAdapter(EnvironmentWeatherRankkActivity environmentWeatherRankkActivity, List<Trend> weatherData) {
			this.mContext=environmentWeatherRankkActivity;
			this.weatherData=weatherData;
		}

		@Override
		public int getCount() {
			return weatherData.size();
		}

		@Override
		public Object getItem(int position) {
			return weatherData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null; 
			if (convertView==null) {
				viewHolder=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.weather_item_layout, null);
			    AbsListView.LayoutParams params = new AbsListView.LayoutParams(screenWidth/6,AbsListView.LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(params);
				viewHolder.tvDate=(TextView) convertView.findViewById(R.id.linegraph_tv1);
				viewHolder.tvDateDetail=(TextView) convertView.findViewById(R.id.linegraph_tv6);
				viewHolder.tvTemp=(TextView) convertView.findViewById(R.id.tv_temp);
				viewHolder.tvWeather=(TextView) convertView.findViewById(R.id.tv_weather);
				viewHolder.ivWeatherIcon=(ImageView) convertView.findViewById(R.id.activity_main_iv0);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Trend trend = weatherData.get(position);
			viewHolder.tvDate.setText(trend.getWeek());
			viewHolder.tvDateDetail.setText(trend.getDate());
			viewHolder.tvWeather.setText(trend.getWeather());
			viewHolder.tvTemp.setText(trend.getTemp());
			viewHolder.ivWeatherIcon.setImageDrawable(context.getResources().getDrawable(
					getWeatherIcon(trend.getWeather())));
			return convertView;
		}
		private class ViewHolder{
			TextView tvDate,tvDateDetail,tvTemp,tvWeather;
			ImageView ivWeatherIcon;
		
		}
		
	}

	private void requestSevenDayWeatherData(String urlcome) {
		okUtils.get(urlcome, new SevenDayWeatherCallback());
	}

	private class SevenDayWeatherCallback extends StringCallback {

		@Override
		public void onError(Call arg0, Exception arg1, int arg2) {
		}

		@Override
		public void onResponse(String arg0, int arg1) {
			if (arg0 != null) {
				CurrentWeather weather = JsonUtils.parseCurrentWeather(arg0);
				List<Trend> tempWeatherData = weather.getTrends();
				if (tempWeatherData!=null&&tempWeatherData.size()>0) {
					weatherData.addAll(tempWeatherData);
					gvWeatherAdapter.notifyDataSetChanged();
				}
			}
		}

	}
	private int getWeatherIcon(String climate) {
		int weatherIcon = R.drawable.weather_icon_qingtian;
		String climateString = CommonUtil.getWeatherIconString(climate,0);
		if (mApplication.getWeatherIconMap().containsKey(climateString)) {
			weatherIcon = mApplication.getWeatherIconMap().get(climateString);
		}
		return weatherIcon;
	}
	

	private LocationReceiver locationReceiver;

	public void registerMessageReceiver() {
		locationReceiver = new LocationReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(LocationService.LOCATION_CHANGE_ACTION);
		registerReceiver(locationReceiver, filter);
	}

	protected void onDestroy() {
		// unregisterReceiver(locationReceiver);
		super.onDestroy();
	};

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
			if (null != intent
					&& null != intent.getAction()
					&& intent.getAction().equals(
							LocationService.LOCATION_CHANGE_ACTION)) {
				try {
					String NewCity = WeiBaoApplication.getInstance()
							.getDingweicity();
					String NewLat = WeiBaoApplication.getInstance()
							.getCurrentCityLatitude();
					String NewLng = WeiBaoApplication.getInstance()
							.getCurrentCityLongitude();
					String NewDingweiString = WeiBaoApplication.getInstance()
							.getXiangxidizhi();
					if (dingweiString == null
							|| "".equals(dingweiString)
							|| (null != NewDingweiString
									&& NewDingweiString.equals(dingweiString) && NewCity
										.equals(city))) {
						return;
					}
					if (NewDingweiString != null) {
						// layout_dingwei.setVisibility(View.VISIBLE);
						text_dingwei.setText(NewDingweiString);
						environment_rank_detailstv16.setText(NewCity);
					}
					rankActivity = new GetWeatherRankActivity();
					MyLog.i("UrlComponent details"
							+ UrlComponent.currentWeatherDetailsGet(NewCity,
									NewLat, NewLng));
					if (NewCity.contains("自治州")) {
						NewCity = mCityDB.getSuoSuo(NewCity);
					}
					rankActivity.execute(UrlComponent.currentWeatherDetailsGet(
							NewCity, NewLat, NewLng), "3600");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initView() {
		rl_aqi_24h_trend = (LinearLayout) findViewById(R.id.rl_aqi_24h_trend);
		environment_rank_details_dw = (TextView) findViewById(R.id.environment_rank_details_dw);
		if (lat.equals("0")) {
			environment_rank_details_dw.setText("均值");
			environment_rank_details_dw.setGravity(Gravity.CENTER);
		}
		rl_aqi_30d_trend = (LinearLayout) findViewById(R.id.rl_aqi_30d_trend);
		tv_pollution_factor = (TextView) findViewById(R.id.tv_pollution_factor);
		monitor_chart = (LineChartView) findViewById(R.id.monitor_chart);
		monitor_name = (FixGridLayout) findViewById(R.id.monitor_name);
		tv_pollution_factor.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// details_qushi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		rl_aqi_30d_trend.setOnClickListener(this);
		tv_pollution_factor.setOnClickListener(this);
		relayouty = (RelativeLayout) findViewById(R.id.relayouty);
		text_dingwei = (TextView) findViewById(R.id.text_dingwei);
		monitor_pm25 = (TextView) findViewById(R.id.monitor_pm25);
		monitor_pm10 = (TextView) findViewById(R.id.monitor_pm10);
		rankk_update_time = (TextView) findViewById(R.id.rankk_update_time);
		monitor_03 = (TextView) findViewById(R.id.monitor_03);
		monitor_so2 = (TextView) findViewById(R.id.monitor_so2);
		monitor_no2 = (TextView) findViewById(R.id.monitor_no2);
		monitor_co = (TextView) findViewById(R.id.monitor_co);

		tv_pollution_factor_time = (TextView) findViewById(R.id.tv_pollution_factor_time);
		tv_monitoring_station_time = (TextView) findViewById(R.id.tv_monitoring_station_time);
		detail_polluction = (TextView) findViewById(R.id.detail_polluction);
		detail_polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		environment_wind = (TextView) findViewById(R.id.environment_wind);
		detail_polluction.setOnClickListener(this);
		rl_aqi_24h_trend.setOnClickListener(this);
		monitor_ll1 = (LinearLayout) findViewById(R.id.monitor_ll1);
		monitor_ll2 = (LinearLayout) findViewById(R.id.monitor_ll2);
		monitor_ll3 = (LinearLayout) findViewById(R.id.monitor_ll3);
		environment_wind_shidu = (LinearLayout) findViewById(R.id.environment_wind_shidu);
		station_title_layout = (LinearLayout) findViewById(R.id.station_title_layout);
		relayouty.setVisibility(View.VISIBLE);
		monitor_ll1.setVisibility(View.VISIBLE);
		monitor_ll2.setVisibility(View.VISIBLE);
		monitor_ll3.setVisibility(View.VISIBLE);
		station_title_layout.setOnClickListener(this);
		layout_dingwei = (LinearLayout) findViewById(R.id.layout_dingwei);
		environment_rank_details_dw_sz = (TextView) findViewById(R.id.environment_rank_details_dw_sz);
		environment_rank_details_dw_sz.setText(pm25);
		city_paiming_value_layout = (RelativeLayout) findViewById(R.id.city_paiming_value_layout);
		environment_rank_details_wr = (TextView) findViewById(R.id.environment_rank_details_wr);
		environment_rank_details_wr.setText(level);
		environment_rank_details_rk = (TextView) findViewById(R.id.environment_rank_details_rk);
		environment_rank_details_pm25 = (TextView) findViewById(R.id.environment_rank_details_pm25);
		environment_rank_details_pm10 = (TextView) findViewById(R.id.environment_rank_details_pm10);
		environment_rank_details_no2 = (TextView) findViewById(R.id.environment_rank_details_no2);
		environment_rank_details_so2 = (TextView) findViewById(R.id.environment_rank_details_so2);
		environment_rank_details_co = (TextView) findViewById(R.id.environment_rank_details_co);
		environment_rank_details_o3 = (TextView) findViewById(R.id.environment_rank_details_o3);
		environment_rank_details_ts = (TextView) findViewById(R.id.environment_rank_details_ts);
		environment_rank_details_tv6 = (TextView) findViewById(R.id.environment_rank_details_tv6);
		environment_rank_details_tv7 = (TextView) findViewById(R.id.environment_rank_details_tv7);
		environment_rank_details_tv8 = (TextView) findViewById(R.id.environment_rank_details_tv8);
		environment_rank_details_tv9 = (TextView) findViewById(R.id.environment_rank_details_tv9);
		environment_rank_details_tv10 = (TextView) findViewById(R.id.environment_rank_details_tv10);
		environment_rank_details_tv11 = (TextView) findViewById(R.id.environment_rank_details_tv11);
		environment_rank_details_tv12 = (TextView) findViewById(R.id.environment_rank_details_tv12);
		environment_rank_details_tv13 = (TextView) findViewById(R.id.environment_rank_details_tv13);
		environment_rank_details_tv14 = (TextView) findViewById(R.id.environment_rank_details_tv14);
		environment_rank_details_tv15 = (TextView) findViewById(R.id.environment_rank_details_tv15);
		
		city_name = (TextView) findViewById(R.id.city_name);
		aqi_du_value = (TextView) findViewById(R.id.aqi_du_value);
		aqi_value = (TextView) findViewById(R.id.aqi_value);
		baifenbi_value = (TextView) findViewById(R.id.baifenbi_value);
		paiming_value = (TextView) findViewById(R.id.paiming_value);
		environment_pathyear = (TextView) findViewById(R.id.environment_pathyear);
		environment_pathmonth = (TextView) findViewById(R.id.environment_pathmonth);
		environment_path24_pm25 = (TextView) findViewById(R.id.environment_path24_pm25);
		environment_path24_pm10 = (TextView) findViewById(R.id.environment_path24_pm10);
		environment_path24_so2 = (TextView) findViewById(R.id.environment_path24_so2);
		environment_path24_no2 = (TextView) findViewById(R.id.environment_path24_no2);
		// environment_pathyear.setVisibility(View.VISIBLE);
		environment_pathmonth.setVisibility(View.VISIBLE);
		paiming_value = (TextView) findViewById(R.id.paiming_value);
		station_layout = (LinearLayout) findViewById(R.id.station_layout);
		textv_nodata = (TableRow) findViewById(R.id.textv_nodata);
		tableRow1 = (TableRow) findViewById(R.id.tableRow1);
		tableRow2 = (TableRow) findViewById(R.id.tableRow2);
		environment_rank_detailstv16 = (TextView) findViewById(R.id.environment_rank_detailstv16);
		environment_rank_detailstv16.setText(city);
		environment_rank_details_back = (ImageView) findViewById(R.id.environment_rank_details_back);
		environment_rank_details_back.setVisibility(View.GONE);
		titleLocation = (ImageView) findViewById(R.id.title_location);
		titleLocation.setOnClickListener(this);
		relativeLayout4 = (RelativeLayout) findViewById(R.id.relativeLayout4);
		tav = (TableLayout) findViewById(R.id.tav);
		environment_path = (TextView) findViewById(R.id.environment_path);
		environment_path24 = (TextView) findViewById(R.id.environment_path24);
		environment_path.setOnClickListener(this);
		environment_path24.setOnClickListener(this);
		environment_pathyear.setOnClickListener(this);
		environment_pathmonth.setOnClickListener(this);
		environment_path24_pm10.setOnClickListener(this);
		environment_path24_pm25.setOnClickListener(this);
		environment_path24_so2.setOnClickListener(this);
		environment_path24_no2.setOnClickListener(this);
		environment_rank_details_back.setOnClickListener(this);
		pv_24th = (ColumnChartView) findViewById(R.id.rank_details_pv_24th);
		// 24小时AQI
		pv_24th.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 1);
				intent.putExtra(CityElementCharsActivity.XVALUE, hours);
				intent.putExtra(CityElementCharsActivity.YVALUE, aqiOf24);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialHoursAQI);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, aqiOf24);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "24小时AQI");

				MyLog.i("hours" + hours);
				MyLog.i("aqi24" + aqi24_Lists);
				MyLog.i("city_name" + city_name.getText() + "");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);

			}
		});
		// for (int i = 0; i < 24; i++) {
		// hours[i] = "10";
		// aqi24_Lists[i] = 10;
		// }
		//
		// pv_24th.setXCount(500, 5);
		// pv_24th.setType(PathView_24.DAY_HOUR);
		// pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
		// aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
		// aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
		// aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
		// aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
		// aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
		// aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
		// aqi24_Lists[22], aqi24_Lists[23] });
		pathView_24h_pm10 = (ColumnChartView) findViewById(R.id.rank_details_pv_pm10_24th);
		pathView_24h_pm10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 2);
				intent.putExtra(CityElementCharsActivity.XVALUE, hours);
				intent.putExtra(CityElementCharsActivity.YVALUE, pm10Of24);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialHoursPM10);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, pm10Of24);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "24小时PM10");
				MyLog.i("hours" + hours);
				MyLog.i("aqi24" + aqi24_Lists);
				MyLog.i("city_name" + city_name.getText() + "");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		// for (int i = 0; i < 24; i++) {
		// hours10[i] = "10";
		// pm10_24_Lists[i] = 10;
		// }
		// pathView_24h_pm10.setXCount(500, 5);
		// pathView_24h_pm10.setType(PathView_24h_pm10.DAY_HOUR);
		// pathView_24h_pm10.setDate(new int[] { pm10_24_Lists[0],
		// pm10_24_Lists[1], pm10_24_Lists[2], pm10_24_Lists[3],
		// pm10_24_Lists[4], pm10_24_Lists[5], pm10_24_Lists[6],
		// pm10_24_Lists[7], pm10_24_Lists[8], pm10_24_Lists[9],
		// pm10_24_Lists[10], pm10_24_Lists[11], pm10_24_Lists[12],
		// pm10_24_Lists[13], pm10_24_Lists[14], pm10_24_Lists[15],
		// pm10_24_Lists[16], pm10_24_Lists[17], pm10_24_Lists[18],
		// pm10_24_Lists[19], pm10_24_Lists[20], pm10_24_Lists[21],
		// pm10_24_Lists[22], pm10_24_Lists[23] });
		pathView_24h_so2 = (ColumnChartView) findViewById(R.id.rank_details_pv_so2_24th);//新
		pathView_24h_so2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 4);
				intent.putExtra(CityElementCharsActivity.XVALUE, hours);
				intent.putExtra(CityElementCharsActivity.YVALUE, so2Of24);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialHoursSO2);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, so2Of24);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "24小时SO2");			
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		pathView_24h_no2 = (ColumnChartView) findViewById(R.id.rank_details_pv_no2_24th);//新  代价点击事件
		pathView_24h_no2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 5);
				intent.putExtra(CityElementCharsActivity.XVALUE, hours);
				intent.putExtra(CityElementCharsActivity.YVALUE, no2Of24);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialHoursNO2);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, no2Of24);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "24小时NO2");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		pathView_24h_pm25 = (ColumnChartView) findViewById(R.id.rank_details_pv_pm25_24th);
		pathView_24h_pm25.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 3);
				intent.putExtra(CityElementCharsActivity.XVALUE, hours);
				intent.putExtra(CityElementCharsActivity.YVALUE, pm25Of24);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialHoursPM25);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, pm25Of24);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "24小时PM2.5");
				MyLog.i("hours" + hours);
				MyLog.i("aqi24" + aqi24_Lists);
				MyLog.i("city_name" + city_name.getText() + "");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		// for (int i = 0; i < 24; i++) {
		// hours25[i] = "10";
		// pm25_24_Lists[i] = 10;
		// }
		//
		// pathView_24h_pm25.setXCount(500, 5);
		// pathView_24h_pm25.setType(PathView_24h_pm25.DAY_HOUR);
		// pathView_24h_pm25.setDate(new int[] { pm25_24_Lists[0],
		// pm25_24_Lists[1], pm25_24_Lists[2], pm25_24_Lists[3],
		// pm25_24_Lists[4], pm25_24_Lists[5], pm25_24_Lists[6],
		// pm25_24_Lists[7], pm25_24_Lists[8], pm25_24_Lists[9],
		// pm25_24_Lists[10], pm25_24_Lists[11], pm25_24_Lists[12],
		// pm25_24_Lists[13], pm25_24_Lists[14], pm25_24_Lists[15],
		// pm25_24_Lists[16], pm25_24_Lists[17], pm25_24_Lists[18],
		// pm25_24_Lists[19], pm25_24_Lists[20], pm25_24_Lists[21],
		// pm25_24_Lists[22], pm25_24_Lists[23] });
		pw = (ColumnChartView) findViewById(R.id.rank_details_pv_pv);
		pw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 1);
				intent.putExtra(CityElementCharsActivity.XVALUE, days_data);
				intent.putExtra(CityElementCharsActivity.YVALUE, aqi_Lists);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialWeeks);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, aqi_Lists);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "最近一周空气质量");
				MyLog.i("days_data" + days_data);
				MyLog.i("aqi_Lists" + aqi_Lists);
				MyLog.i("city_name" + city_name.getText() + "");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		// for (int i = 0; i < 7; i++) {
		// days_data[i] = "";
		// aqi_Lists[i] = 10;
		// }
		// pw.setXCount(500, 5);
		// pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1], aqi_Lists[2],
		// aqi_Lists[3], aqi_Lists[4], aqi_Lists[5], aqi_Lists[6] });
		// pw.setType(PathView.DAY_WEEK);
		path_year = (ColumnChartView) findViewById(R.id.rank_pathmonth);
		path_month = (ColumnChartView) findViewById(R.id.rank_pathyear);
		path_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(EnvironmentWeatherRankkActivity.this,
						CityElementCharsActivity.class);
				intent.putExtra(CityElementCharsActivity.TYPEVALUE, 1);
				intent.putExtra(CityElementCharsActivity.XVALUE, months);
				intent.putExtra(CityElementCharsActivity.YVALUE, aqi_months);
				intent.putExtra(CityElementCharsActivity.TIMEVALUE,
						initialMonths);
				intent.putExtra(CityElementCharsActivity.AQIVALUE, aqi_months);
				intent.putExtra(CityElementCharsActivity.LOCATION,
						city_name.getText() + "");
				intent.putExtra(CityElementCharsActivity.TITLE, "最近30天空气质量");
				MyLog.i("days_data" + days_data);
				MyLog.i("aqi_Lists" + aqi_Lists);
				MyLog.i("city_name" + city_name.getText() + "");
				EnvironmentWeatherRankkActivity.this.startActivity(intent);
			}
		});
		// path_year.setXCount(500, 5);
		// path_year.setType(PathView_Year.DAY_YEAR);
		// for (int i = 0; i < 12; i++) {
		// years[i] = "";
		// aqi_years[i] = 10;
		// }
		// path_year.setDate(new int[] { aqi_years[0], aqi_years[1],
		// aqi_years[2],
		// aqi_years[3], aqi_years[4], aqi_years[5], aqi_years[6],
		// aqi_years[7], aqi_years[8], aqi_years[9], aqi_years[10],
		// aqi_years[11] });
		// path_month.setXCount(500, 5);
		// path_month.setType(PathView_Month.DAY_MONTH);
		months = new String[31];
		aqi_months = new int[31];
		for (int i = 0; i < 31; i++) {
			months[i] = "";
			aqi_months[i] = 10;
		}
		// path_month.setDate(aqi_months);
		for (int i = 0; i < 24; i++) {
			shidu[i] = "10";
			shidu_Lists[i] = 10;
		}
		environment_rank_pm = (RelativeLayout) findViewById(R.id.environment_rank_pm);
		environment_rank_pm.setOnClickListener(this);
		paiming_value.setOnClickListener(this);
		environment_path24.setBackground(getResources().getDrawable(
				R.drawable.kuang11));
		environment_path.setBackground(null);
		environment_pathyear.setBackground(null);
		environment_pathmonth.setBackground(null);
		environment_path24_pm10.setBackground(null);
		environment_path24_pm25.setBackground(null);
		environment_path24_so2.setBackground(null);
		environment_path24_no2.setBackground(null);
	}

	// 获取城市排名数据 为保持本页面与城市排名页面数据保持一致
	public class GetAqiDetailItemTask extends AsyncTask<String, Void, CityRank> {
		@Override
		protected CityRank doInBackground(String... params) {
			String url = UrlComponent.cityRankingUrl_Post;
			BusinessSearch search = new BusinessSearch();
			CityRank _Result;
			try {
				/**
				 * 主要为读取历史消息,间为一小间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				if (city.contains("自治州")) {
					city = mCityDB.getSuoSuo(city);
				}
				_Result = search.getAqiDetailItem(url, 600, city);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(CityRank result) {
			super.onPostExecute(result);
			if (result != null) {
				cityLength = result.get_Result().size();
				List<Item> _Result = result.get_Result();
				MyLog.i(">>>>>>>citylength" + city);
				for (int i = 0; i < _Result.size(); i++) {
					Item item = _Result.get(i);
					if (city.equals(item.getCityName())) {
						cityRank = i + 1;
						break;
					}
					if (i == _Result.size() - 1
							&& !city.equals(item.getCityName())) {
						city_paiming_value_layout.setVisibility(View.GONE);
					}
				}
				String m = (((float) cityLength - cityRank) / cityLength) * 100
						+ "";
				if (m.contains("."))
					m = m.substring(0, m.indexOf("."));
				if (m.equals("0"))
					m = "1";
				if (m.equals("100"))
					m = "99";
				baifenbi_value.setText(m + "%");
				paiming_value.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				paiming_value.getPaint().setAntiAlias(true);// 抗锯齿
				try {
					if (cityRank == 0)
						cityRank = 1;
					paiming_value.setText("排名" + cityRank);
				} catch (Exception e) {
				}
			}
		}
	}

	class GetWeatherRankActivity extends AsyncTask<String, Void, CityDetails> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected CityDetails doInBackground(String... params) {
			BusinessSearch search = new BusinessSearch();
			cityDetails = new CityDetails();
			try {
				cityDetails = search.getWeatherRankActivity(params[0],
						params[1], type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cityDetails;
		}

		@Override
		protected void onPostExecute(CityDetails result) {
			try {
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				dialog.dismiss();
				pmTask = new GetPmTask();
				pmTask.execute("");
				if (result == null)
					return;
				if (!result.isFlag()) {
					ToastUtil.showShort(getApplicationContext(), "请检查网络设置");
				} else {
					details = result;
					fillData(result);
				}

			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

	private void fillData(CityDetails result) {
		LayoutInflater layoutInflater = LayoutInflater
				.from(EnvironmentWeatherRankkActivity.this);
		aqi_du_value.setText(CommonUtil.getDengJiByAQIII(result.getAqi() + ""));
		aqi_value.setText(result.getAqi());
		city_name.setText(city);
		ListPolluctionModel polluctionModel = result.getPolluctionModel();
		if (polluctionModel != null) {
			relayouty.setVisibility(View.VISIBLE);
			monitor_ll1.setVisibility(View.VISIBLE);
			monitor_ll2.setVisibility(View.VISIBLE);
			monitor_ll3.setVisibility(View.VISIBLE);
			int pm25 = polluctionModel.getPm25();
			int pm10 = polluctionModel.getPm10();
			int o3 = polluctionModel.getO3();
			int so2 = polluctionModel.getSo2();
			int no2 = polluctionModel.getNo2();
			factor_uptime = polluctionModel.getTime();
			rankk_update_time.setText("更新于" + factor_uptime);
			tv_pollution_factor_time.setText("更新于" + factor_uptime);
			double co = polluctionModel.getCo();
			if (pm25 != 0) {
				monitor_pm25.setText(pm25 + "");
				monitor_pm25.setBackgroundResource(CommonUtil.getDengJiByType(
						pm25, "1", 0.5d));
			}
			if (pm10 != 0) {
				monitor_pm10.setText(pm10 + "");
				monitor_pm10.setBackgroundResource(CommonUtil.getDengJiByType(
						pm10, "0", 0.5d));
			}
			if (o3 != 0) {
				monitor_03.setText(o3 + "");
				monitor_03.setBackgroundResource(CommonUtil.getDengJiByType(o3,
						"2", 0.5d));
			}
			if (so2 != 0) {
				monitor_so2.setText(so2 + "");
				monitor_so2.setBackgroundResource(CommonUtil.getDengJiByType(
						so2, "3", 0.5d));
			}
			if (no2 != 0) {
				monitor_no2.setText(no2 + "");
				monitor_no2.setBackgroundResource(CommonUtil.getDengJiByType(
						no2, "4", 0.5d));
			}
			if (co != 0.0) {
				monitor_co.setText(co + "");
				monitor_co.setBackgroundResource(CommonUtil.getDengJiByType(
						pm10, "5", co));
			}
		} else {
			relayouty.setVisibility(View.GONE);
			monitor_ll1.setVisibility(View.GONE);
			monitor_ll2.setVisibility(View.GONE);
			monitor_ll3.setVisibility(View.GONE);
		}
		if (null != result.getAqiStationModels()
				&& result.getAqiStationModels().size() > 0) {
			// station_layout
			MyLog.i("========>>>>>result" + result);
			station_title_layout.setVisibility(View.VISIBLE);
			station_layout.setVisibility(View.VISIBLE);
			tv_monitoring_station_time.setText("更新于"
					+ result.getAqiStationModels().get(0).getMonidate());
			final int count = result.getAqiStationModels().size();
			aqiStationModels = result.getAqiStationModels();
			for (int i = 0; i < result.getAqiStationModels().size(); i++) {
				if (!result.getAqiStationModels().get(i).getPosition()
						.equals(city)) {
					LinearLayout activity_aqi_detail_sation_item = (LinearLayout) layoutInflater
							.inflate(R.layout.activity_aqi_detail_sation_item,
									null);
					TextView station_name = (TextView) activity_aqi_detail_sation_item
							.findViewById(R.id.station_name);
					TextView station_value_du = (TextView) activity_aqi_detail_sation_item
							.findViewById(R.id.station_value_du);
					TextView station_value = (TextView) activity_aqi_detail_sation_item
							.findViewById(R.id.station_value);
					LinearLayout station_value_layout = (LinearLayout) activity_aqi_detail_sation_item
							.findViewById(R.id.station_value_layout);
					View station_line = (View) activity_aqi_detail_sation_item
							.findViewById(R.id.station_line);
					station_value_layout
							.setBackgroundResource(getDuValueRes(result
									.getAqiStationModels().get(i).getAqi()));
					station_name.setText(result.getAqiStationModels().get(i)
							.getPosition());
					try {
						station_value_du.setText(""
								+ getDuValue(result.getAqiStationModels()
										.get(i).getAqi()));
					} catch (Exception e) {
					}
					station_value.setText(""
							+ result.getAqiStationModels().get(i).getAqi());

					if (i == (result.getAqiStationModels().size() - 1)) {
						station_line.setVisibility(View.GONE);
					}
					final String stationcode = result.getAqiStationModels()
							.get(i).getStationcode();
					final String stationname = result.getAqiStationModels()
							.get(i).getPosition();
					final int position = i;
					activity_aqi_detail_sation_item
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											EnvironmentWeatherRankkActivity.this,
											StationViewPagerActivity.class);
									intent.putExtra("count", count);
									intent.putExtra("position", position);
									intent.putExtra("stationcode", stationcode);
									intent.putExtra("stationname", stationname);
									startActivity(intent);
								}
							});
					LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					// station_layout.addView(activity_aqi_detail_sation_item);
					station_layout
							.addView(activity_aqi_detail_sation_item, lp1);
				}
			}
		}
		if (null == result.getWeekModel()) {
			textv_nodata.setVisibility(View.VISIBLE);
			tableRow1.setVisibility(View.GONE);
			tableRow2.setVisibility(View.GONE);
		} else {
			textv_nodata.setVisibility(View.GONE);
			tableRow1.setVisibility(View.VISIBLE);
			tableRow2.setVisibility(View.VISIBLE);
		}
		position_name = result.getPosition_name();
		if (null != position_name && !position_name.equals("")) {
			environment_rank_details_dw.setText("监测点:" + position_name);
		}
		if ("0".equals(result.getRanking())) {
			environment_rank_pm.setVisibility(View.GONE);
		} else {
			environment_rank_details_rk
					.setText("第" + result.getRanking() + "位");
		}

		Kongqizhishu zhishu = result.getZhishu();
		if (zhishu != null) {
			if (zhishu.getPm25().equals("0")) {
				zhishu.setPm25("-");
			}
			if (zhishu.getPm10().equals("0")) {
				zhishu.setPm10("-");
			}
			if (zhishu.getNo2().equals("0")) {
				zhishu.setNo2("-");
			}
			if (zhishu.getSo2().equals("0")) {
				zhishu.setSo2("-");
			}
			if (zhishu.getCo().equals("0")) {
				zhishu.setCo("-");
			}
			if (zhishu.getO3().equals("0")) {
				zhishu.setO3("-");
			}
			environment_rank_details_pm25.setText(zhishu.getPm25());
			environment_rank_details_pm10.setText(zhishu.getPm10());
			environment_rank_details_no2.setText(zhishu.getNo2());
			environment_rank_details_so2.setText(zhishu.getSo2());
			environment_rank_details_co.setText(zhishu.getCo());
			environment_rank_details_o3.setText(zhishu.getO3());
			environment_rank_details_ts
					.setText("健康提示:" + zhishu.getQingkuang());
		}

		// 空气质量预报
		EnvironmentForecastWeekModel weekmodel = result.getWeekModel();
		if (null != weekmodel) {
			environment_rank_details_tv6.setText(weekmodel.getWeek1());
			environment_rank_details_tv7.setText(weekmodel.getWeek2());
			environment_rank_details_tv8.setText(weekmodel.getWeek3());
			environment_rank_details_tv9.setText(weekmodel.getWeek4());
			environment_rank_details_tv11.setText(pm25);
			environment_rank_details_tv12.setText(weekmodel.getAqi_level_2());
			environment_rank_details_tv13.setText(weekmodel.getAqi_level_3());
			environment_rank_details_tv14.setText(weekmodel.getAqi_level_4());
		} else {
			relativeLayout4.setVisibility(View.GONE);
			tav.setVisibility(View.GONE);
		}
		// weatherLists = result.getInfo24s();
		// pm10List = result.getPm10Info24Hs();
		// pm25List = result.getPm25Info24Hs();

		// pv_24th.setXCount(500, 5);
		// pv_24th.setType(PathView_24.DAY_HOUR);
		// pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
		// aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
		// aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
		// aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
		// aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
		// aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
		// aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
		// aqi24_Lists[22], aqi24_Lists[23] });

		// pathView_24h_pm10.setXCount(500, 5);
		// pathView_24h_pm10.setType(PathView_24h_pm10.DAY_HOUR);
		// MyLog.i(">>>>>>>>>>>>>aqi24_list" + aqi24_Lists[0]);
		// pathView_24h_pm10.setDate(new int[] { pm10_24_Lists[0],
		// pm10_24_Lists[1], pm10_24_Lists[2], pm10_24_Lists[3],
		// pm10_24_Lists[4], pm10_24_Lists[5], pm10_24_Lists[6],
		// pm10_24_Lists[7], pm10_24_Lists[8], pm10_24_Lists[9],
		// pm10_24_Lists[10], pm10_24_Lists[11], pm10_24_Lists[12],
		// pm10_24_Lists[13], pm10_24_Lists[14], pm10_24_Lists[15],
		// pm10_24_Lists[16], pm10_24_Lists[17], pm10_24_Lists[18],
		// pm10_24_Lists[19], pm10_24_Lists[20], pm10_24_Lists[21],
		// pm10_24_Lists[22], pm10_24_Lists[23] });

		for (int i = 0; i < 24; i++) {
			hours25[i] = "10";
			pm25_24_Lists[i] = 10;
		}
		for (int i = 0; i < listPM25Of24h.size(); i++) {
			String time = listPM25Of24h.get(listPM25Of24h.size() - 1).getTime();
			if (!"".equals(time)) {
				time = time.substring(11, 13);
				int time_int = Integer.parseInt(time);
				time_int = time_int - i;
				if (time_int < 0) {
					time_int = time_int + 24;
				}
				if (time.length() == 1) {
					time = "0" + time;
				}
				time = time_int + "";
				hours25[23 - i] = time;
				pm25_24_Lists[i] = listPM25Of24h.get(i).getPm25();
			} else {
				hours25[i] = "10";
				pm25_24_Lists[i] = 10;
			}

		}
		generateColumnData(pathView_24h_pm25, hours, new int[] {
				pm25_24_Lists[0], pm25_24_Lists[1], pm25_24_Lists[2],
				pm25_24_Lists[3], pm25_24_Lists[4], pm25_24_Lists[5],
				pm25_24_Lists[6], pm25_24_Lists[7], pm25_24_Lists[8],
				pm25_24_Lists[9], pm25_24_Lists[10], pm25_24_Lists[11],
				pm25_24_Lists[12], pm25_24_Lists[13], pm25_24_Lists[14],
				pm25_24_Lists[15], pm25_24_Lists[16], pm25_24_Lists[17],
				pm25_24_Lists[18], pm25_24_Lists[19], pm25_24_Lists[20],
				pm25_24_Lists[21], pm25_24_Lists[22], pm25_24_Lists[23] }, 3);
		// pathView_24h_pm25.setXCount(500, 5);
		// pathView_24h_pm25.setType(PathView_24h_pm25.DAY_HOUR);
		// pathView_24h_pm25.setDate(new int[] { pm25_24_Lists[0],
		// pm25_24_Lists[1], pm25_24_Lists[2], pm25_24_Lists[3],
		// pm25_24_Lists[4], pm25_24_Lists[5], pm25_24_Lists[6],
		// pm25_24_Lists[7], pm25_24_Lists[8], pm25_24_Lists[9],
		// pm25_24_Lists[10], pm25_24_Lists[11], pm25_24_Lists[12],
		// pm25_24_Lists[13], pm25_24_Lists[14], pm25_24_Lists[15],
		// pm25_24_Lists[16], pm25_24_Lists[17], pm25_24_Lists[18],
		// pm25_24_Lists[19], pm25_24_Lists[20], pm25_24_Lists[21],
		// pm25_24_Lists[22], pm25_24_Lists[23] });

		weInfoYear = result.getWeInfoYear();
		for (int i = 0; i < weInfoYear.size(); i++) {
			aqi_years[i] = weInfoYear.get(i).getAqi();
			String day = weInfoYear.get(i).getUpdate_time();
			if (!"".equals(day)) {
				day = day.substring(5);
				years[i] = day;
			} else {
				years[i] = "";
				aqi24_Lists[i] = 10;
			}
		}
		// path_year.setXCount(500, 5);
		// path_year.setDate(new int[] { aqi_years[0], aqi_years[1],
		// aqi_years[2],
		// aqi_years[3], aqi_years[4], aqi_years[5], aqi_years[6],
		// aqi_years[7], aqi_years[8], aqi_years[9], aqi_years[10],
		// aqi_years[11] });
		// path_year.setType(PathView_Year.DAY_YEAR);

		windModels = result.getWindModels();
		humiDityModels = result.getHumiDityModels();
		if (windModels == null || humiDityModels == null) {
			environment_wind_shidu.setVisibility(View.GONE);
		}
		if (humiDityModels != null && humiDityModels.size() > 0) {
//			environment_wind_shidu.setVisibility(View.VISIBLE);暂时屏蔽
			shidu_Lists = new int[humiDityModels.size()];
			shidu = new String[humiDityModels.size()];
			for (int i = 0; i < humiDityModels.size(); i++) {
				shidu_Lists[i] = humiDityModels.get(i).getHumidity();
				String day = humiDityModels.get(i).getTimne();
				if (!"".equals(day)) {
					day = day.substring(11, 13) + "";
					if (day.startsWith("0")) {
						day = day.substring(1);
					}
					shidu[i] = day;
				} else {
					shidu[i] = "";
					shidu_Lists[i] = 10;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.environment_rank_details_back:
			finish();
			break;
		case R.id.title_location:
//			share();
			Intent id = new Intent(context, ActivitySet.class);
			startActivity(id);
			break;
		case R.id.detail_polluction: 
			intent = new Intent(getApplicationContext(),
					EnvironmentMonitorPmActivity.class);
			intent.putExtra("city", city);
			// intent.putExtra("aqi", pm25);
			// intent.putExtra("level", level);
			intent.putExtra("aqi", aqi_value.getText().toString());
			intent.putExtra("level", aqi_du_value.getText().toString());
			intent.putExtra("from", "CopyOfEnvironmentWeatherRankActivity");
			intent.putExtra("type", "1");
			intent.putExtra("factor_uptime", factor_uptime);
			startActivity(intent);
			break;

		case R.id.rl_aqi_24h_trend:
			intent = new Intent(getApplicationContext(),
					EnvironmnetAqi24hStationTrendActivity.class);
			intent.putExtra("city", city);
			intent.putExtra("from", "CopyOfEnvironmentWeatherRankActivity");
			startActivity(intent);
			break;
		case R.id.environment_rank_pm:
		case R.id.paiming_value:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirRanking");
			intent = new Intent(getApplicationContext(),
					EnvironmentWeatherRankPaiMingActivity.class);
			intent.putExtra("city", city);
			startActivity(intent);
			break;
		case R.id.rl_aqi_30d_trend:
			// intent = new Intent(CopyOfEnvironmentWeatherRankActivity.this,
			// EnvironmentMonitorActivity.class);
			intent = new Intent(EnvironmentWeatherRankkActivity.this,
					EnvironmentLineMonitorActivity.class);
			intent.putExtra("city", city);
			startActivity(intent);
			break;
		case R.id.tv_pollution_factor:
			intent = new Intent(getApplicationContext(),
					EnvironmentMonitorPmActivity.class);
			intent.putExtra("city", city);
			// intent.putExtra("aqi", pm25);
			// intent.putExtra("level", level);
			intent.putExtra("aqi", aqi_value.getText().toString());
			intent.putExtra("level", aqi_du_value.getText().toString());
			intent.putExtra("from", "CopyOfEnvironmentWeatherRankActivity");
			intent.putExtra("type", "0");
			intent.putExtra("factor_uptime", factor_uptime);
			startActivity(intent);
			break;
		case R.id.environment_path:
			if (null != details) {
				pw.setVisibility(View.VISIBLE);
				pv_24th.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);
				pathView_24h_so2.setVisibility(View.GONE);
				pathView_24h_no2.setVisibility(View.GONE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path24.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_path.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path24.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);

			}
			break;
		case R.id.environment_path24:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirLastWeek");
			// Intent intent2 = new Intent(this,
			// CityElementCharsActivity.class);
			// this.startActivity(intent2);
			if (null != details) {
				pw.setVisibility(View.GONE);
				pv_24th.setVisibility(View.VISIBLE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);
			}

			break;
		case R.id.environment_path24_pm25:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirLastWeek");
			if (null != details) {
				pw.setVisibility(View.GONE);
				pv_24th.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.VISIBLE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_pm25.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_pm25.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			}

			break;
			
		case R.id.environment_path24_so2:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirLastWeek");
			if (null != details) {
				pw.setVisibility(View.GONE);
				pv_24th.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);
				pathView_24h_no2.setVisibility(View.GONE);
				pathView_24h_so2.setVisibility(View.VISIBLE);
			
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			}

			break;
			
		case R.id.environment_path24_no2:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirLastWeek");
			if (null != details) {
				pw.setVisibility(View.GONE);
				pv_24th.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);
				pathView_24h_so2.setVisibility(View.GONE);
				pathView_24h_no2.setVisibility(View.VISIBLE);
			
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
			}

			break;
		case R.id.environment_path24_pm10:
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirLastWeek");
			if (null != details) {
				pw.setVisibility(View.GONE);
				pv_24th.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.VISIBLE);
				pathView_24h_pm25.setVisibility(View.GONE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackground(null);
				environment_path.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(getResources()
						.getDrawable(R.drawable.kuang11));
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
			}

			break;
		case R.id.environment_pathyear:
			if (null != details) {
				pv_24th.setVisibility(View.GONE);
				pw.setVisibility(View.GONE);
				path_year.setVisibility(View.VISIBLE);
				path_month.setVisibility(View.GONE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);

			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_pathyear.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_path24.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_pathyear.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_path24.setBackground(null);
				environment_pathmonth.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_no2.setBackground(null);
				environment_path24_so2.setBackground(null);
			}
			break;
		case R.id.environment_pathmonth:
			if (null != details) {
				pv_24th.setVisibility(View.GONE);
				pw.setVisibility(View.GONE);
				path_year.setVisibility(View.GONE);
				path_month.setVisibility(View.VISIBLE);
				pathView_24h_pm10.setVisibility(View.GONE);
				pathView_24h_pm25.setVisibility(View.GONE);
				pathView_24h_so2.setVisibility(View.GONE);
				pathView_24h_no2.setVisibility(View.GONE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_pathmonth.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_path24.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_pathmonth.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
				environment_path.setBackground(null);
				environment_path24.setBackground(null);
				environment_pathyear.setBackground(null);
				environment_path24_pm10.setBackground(null);
				environment_path24_pm25.setBackground(null);
				environment_path24_so2.setBackground(null);
				environment_path24_no2.setBackground(null);
			}
			break;
		case R.id.environment_wind:
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_wind.setBackground(getResources().getDrawable(
						R.drawable.kuang11));
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_wind.setBackground(getResources().getDrawable(
						R.drawable.kuang11));

			}
			break;
		case R.id.environment_shidu:
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_wind.setBackground(null);
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_wind.setBackground(null);
			}
			break;
		case R.id.tv_time:
		case R.id.iv_time_icon:
//			showPopwindow(getDataPick(SEARCH));
			String  currentTime= tv_time.getText().toString();
			int year = Integer.parseInt(currentTime.substring(0, 4));
			int month = Integer.parseInt(currentTime.substring(5, 7));
			int day = Integer.parseInt(currentTime.substring(8));
			onYearMonthDayPicker(year,month,day);
		default:
			break;
		}
	}
	
	/**
	 * 弹出时间选择器的方法
	 * 
	 * @author tianfy
	 * @param day2
	 * @param month2
	 * @param year2
	 */
	public void onYearMonthDayPicker(int year, int month, int day) {
		final DatePicker picker = new DatePicker(EnvironmentWeatherRankkActivity.this);
		picker.setCanceledOnTouchOutside(true);
		picker.setUseWeight(true);
		picker.setGravity(Gravity.CENTER_VERTICAL);
		picker.setRangeStart(2010, 1, 1);
		picker.setRangeEnd(2027, 1, 1);
		picker.setSelectedItem(year, month, day);
		picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
			@Override
			public void onDatePicked(String year, String month, String day) {
				
				Message msg = new Message();
				msg.what = SEARCH;
				msg.obj = year+"-"+month+"-"+day;
				handler.sendMessage(msg);
			}
		});
		picker.show();
	}

	// 分享成功还是失败
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			MobclickAgent.onEvent(EnvironmentWeatherRankkActivity.this,
					"HJAirShare");
			Log.d("Test", "share success");
			Toast.makeText(EnvironmentWeatherRankkActivity.this, "分享成功", 2000)
					.show();
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
			Toast.makeText(EnvironmentWeatherRankkActivity.this, "分享失败", 2000)
					.show();
		}

		@Override
		public void onCancel() {
			Log.d("Test", "cancel ");
			// Toast.makeText(CurrentTq.this, "分享取消", 2000).show();
		}
	}

	private void share() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(EnvironmentWeatherRankkActivity.this, "请检查您的网络", 0)
					.show();
			return;
		}
		Bitmap bitmap = CommonUtil
				.GetCurrentScreen(EnvironmentWeatherRankkActivity.this);
		try {
			uri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), bitmap, null, null));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentWeatherRankkActivity.this, "截图失败", 0)
					.show();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentWeatherRankkActivity.this, "截图失败", 0)
					.show();
			return;
		}

		content = "空气质量环境，共建美好环境";

		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare.setClientId(MediaType.WEIXIN.toString(),
				"wx541df6ed6e9babc0");
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), "991071488");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		mSocialShare.setParentView(getWindow().getDecorView());
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "空气质量");
		mImageContent.setTitle("空气质量");
		mImageContent.setContent(content);
		mImageContent.setLinkUrl("http://www.wbapp.com.cn");
		mImageContent.setImageUri(uri);
		mImageContent.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
		mImageContent.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
		mImageContent.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
		mImageContent.setImageData(bitmap);
		mSocialShare.show(this.getWindow().getDecorView(), mImageContent,
				FrontiaTheme.LIGHT, new ShareListener());
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EnvironmentWeatherRankActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EnvironmentWeatherRankActivity");
		MobclickAgent.onResume(this);
	}

	public static String getDuValue(int aqi) {
		String xx = "优";
		if (aqi <= 50) {
			xx = "优";
		} else if (aqi <= 100) {
			xx = "良";
		} else if (aqi < 150) {
			xx = "轻度";
		} else if (aqi <= 200) {
			xx = "中度";
		} else if (aqi <= 300) {
			xx = "重度";
		} else {
			xx = "严重";
		}
		return xx;
	}

	public static int getDuValueRes(int aqi) {
		int bg = R.drawable.aqi_level_1;
		if (aqi <= 50) {
			bg = R.drawable.aqi_level_1;
		} else if (aqi <= 100) {
			bg = R.drawable.aqi_level_2;
		} else if (aqi <= 150) {
			bg = R.drawable.aqi_level_3;
		} else if (aqi <= 200) {
			bg = R.drawable.aqi_level_4;
		} else if (aqi <= 300) {
			bg = R.drawable.aqi_level_5;
		} else {
			bg = R.drawable.aqi_level_6;
		}
		return bg;
	}

	class GetThreeDayForestTask extends
			AsyncTask<String, Void, List<ThreeDayForestModel>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected List<ThreeDayForestModel> doInBackground(String... params) {
			// String url = UrlComponent.getYuCe(city);
			String url = UrlComponent.getThreeDayForest;
			MyLog.i("ThreeDay" + url);
			BusinessSearch search = new BusinessSearch();
			List<ThreeDayForestModel> _Result = new ArrayList<ThreeDayForestModel>();
			try {
				_Result = search.getThreeDayForestModel(url, city);
				MyLog.i("ThreeDay details:" + _Result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		// String url = UrlComponent.getYuCe(city);
		// MyLog.i(">>>>>>>>>>urlurl" + url);
		// BusinessSearch search = new BusinessSearch();
		// List<ThreeDayForestModel> _Result = new
		// ArrayList<ThreeDayForestModel>();
		// try {
		// _Result = search.getThreeDayForestModel(url, city);
		// MyLog.i("details:" + _Result);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return _Result;
		// }

		@Override
		protected void onPostExecute(List<ThreeDayForestModel> result) {
			try {
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				dialog.dismiss();
				if (((result.size()) == 0)) {
					environment_rank_details_tv12.setText("--");
					environment_rank_details_tv13.setText("--");
					environment_rank_details_tv14.setText("--");
				} else {
					if ((result.get(0).getAQI().toString()).equals("0")) {
						environment_rank_details_tv12.setText("--");
					} else {
						environment_rank_details_tv12.setText(result.get(0)
								.getAQI().toString());
					}
					if ((result.get(1).getAQI().toString()).equals("0")) {
						environment_rank_details_tv13.setText("--");
					} else {
						environment_rank_details_tv13.setText(result.get(1)
								.getAQI().toString());
					}
					if ((result.get(2).getAQI().toString()).equals("0")) {
						environment_rank_details_tv14.setText("--");
					} else {
						environment_rank_details_tv14.setText(result.get(2)
								.getAQI().toString());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				MyLog.e("weibao Exception", e);
			}
		}
	}

	class GetPmTask extends AsyncTask<String, Void, PmDayHourModel> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected PmDayHourModel doInBackground(String... params) {
			String url = "";
			url = UrlComponent.monitor_hour_day + "?city=" + city;
			BusinessSearch search = new BusinessSearch();
			PmDayHourModel _Result = new PmDayHourModel();
			try {
				_Result = search.getMonitorDayHour(url, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(PmDayHourModel result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			// fillData(result);
			fillPmData(result);
		}
	}

	public void fillPmData(PmDayHourModel result) {
		if (result == null) {
			return;
		}
		dayModels = result.getHourModels();
		try {
			// station_layout.setVisibility(View.VISIBLE);
			monitor_name.removeAllViews();
			for (int i = 0; i < 8; i++) {
				monitor_name.setmCellHeight(CommonUtil.dip2px(context, 45));
				monitor_name.setmCellWidth(CommonUtil.dip2px(context, 120));
				final CheckBox box = new CheckBox(context);
				box.setTextSize(CommonUtil.dip2px(context, 6));
				// View convertView=
				// LayoutInflater.from(context).inflate(R.layout.check, null);
				// CheckBox box = (CheckBox)
				// convertView.findViewById(R.id.check);
				box.setTextColor(Color.parseColor(listColors.get(i)));
				box.setTag(i);
				Resources resources = getResources();
				if (i == 0) {
					box.setText(resources.getString(R.string.text_pm25));
				} else if (i == 1) {
					box.setText(resources.getString(R.string.text_pm10));
				} else if (i == 2) {
					box.setText(resources.getString(R.string.text_o3));
				} else if (i == 3) {
					box.setText(resources.getString(R.string.text_so2));
				} else if (i == 4) {
					box.setText(resources.getString(R.string.text_no2));
				} else if (i == 5) {
					box.setText(resources.getString(R.string.text_co));
				} else if (i == 6) {
					box.setText(resources.getString(R.string.text_wind));
				} else if (i == 7) {
					box.setText(resources.getString(R.string.text_humidity));
				}
				box.setChecked(true);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int tag = (Integer) box.getTag();
						MyLog.i("tag　：" + tag + " isChecked :" + isChecked);
						reset();
						if (isChecked) {
							MyLog.i("tag2　：" + tag
									+ " !listShowLineNum.contains(tag + "
									+ !listShowLineNum.contains(tag + ""));
							if (!listShowLineNum.contains(tag + "")) {
								listShowLineNum.add(tag + "");
							}
						} else {
							MyLog.i("tag3　：" + tag
									+ "listShowLineNum.contains(tag +"
									+ listShowLineNum.contains(tag + ""));
							if (listShowLineNum.contains(tag + "")) {
								listShowLineNum.remove(tag + "");
								new Thread() {
									public void run() {
										try {
											sleep(500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									};
								}.start();
								MyLog.i("tag4　：" + tag
										+ "listShowLineNum.contains(tag +"
										+ listShowLineNum.contains(tag + ""));
							}
						}
						MyLog.i("generateData1");
						generateData("0");
					}
				});
				if (!listShowLineNum.contains(i + "")) {
					listShowLineNum.add(i + "");
				}
				monitor_name.addView(box);

			}
			reset();
			generateData("0");
			MyLog.i("generateData2");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void reset() {
		hasAxes = true;
		hasAxesNames = false;
		hasLines = true;
		hasPoints = true;
		shape = ValueShape.CIRCLE;
		isFilled = false;
		hasLabels = false;
		isCubic = false;
		hasLabelForSelected = true;
		// monitor_chart.setZoomType(ZoomType.HORIZONTAL);
		// monitor_chart.setValueSelectionEnabled(hasLabelForSelected);
	}

	private void generateData(String type) {
		monitor_chart.destroyDrawingCache();
		List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
		List<AxisValue> myxisValues = new ArrayList<AxisValue>();
		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < listShowLineNum.size(); ++i) {
			int location = Integer.parseInt(listShowLineNum.get(i));
			MyLog.i("location  +" + location);
			List<PointValue> values = new ArrayList<PointValue>();
			for (int j = 0; j < dayModels.size(); j++) {
				PointValue value = null;
				if (location == 0) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getPM25()));
				} else if (location == 1) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getPM10()));
				} else if (location == 2) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getO3()));
				} else if (location == 3) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getSO2()));
				} else if (location == 4) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getNO2()));
				} else if (location == 5) {
					value = new PointValue(j, Float.parseFloat(dayModels.get(j)
							.getCO()) * 10);
				} else if (location == 6) {
					value = new PointValue(j, cityDetails.getWindModels()
							.get(j).getWind());
				} else if (location == 7) {
					value = new PointValue(j, cityDetails.getHumiDityModels()
							.get(j).getHumidity());
				}
				values.add(value);
				// mAxisValues.add(new AxisValue(i, days[i].toCharArray()));
				String time = dayModels.get(j).getTIME();
				if (time.endsWith(":00")) {
					time = time.replace(":00", "");
				}
				// MyLog.i("dayModels.size()" + dayModels.size());
				// MyLog.i("dayModels.size() j" + j);
				if (j % 2 == 0 && i == 0) {
					mAxisValues.add(new AxisValue(j, time.toCharArray()));
				} else if (i == 0) {
					mAxisValues.add(new AxisValue(j, "".toCharArray()));
				}
				// else{
				// mAxisValues.add(new AxisValue(j, "".toCharArray()));
				// }
				// axisValue.setLabel(monitorModels.get(location).getInfoMonths().get(j).getUpdate_time()+"");
				// mAxisValues.add(new
				// AxisValue(j).setLabel(monitorModels.get(location).getInfoMonths().get(j).getUpdate_time()));
				// values.add(new
				// PointValue(monitorModels.get(location).getInfoMonths().get(j).get,
				// monitorModels.get(location).getInfoMonths().get(j).getAqi()));
			}
			Line line = new Line(values);
			line.setColor(Color.parseColor(listColors.get(location)));
			line.setShape(shape);
			line.setCubic(isCubic);
			line.setFilled(isFilled);
			line.setHasLabels(hasLabels);
			line.setHasLabelsOnlyForSelected(hasLabelForSelected);
			line.setHasLines(hasLines);
			line.setHasPoints(hasPoints);
			lines.add(line);
		}
		monitor_chart.setZoomType(ZoomType.HORIZONTAL);
		monitor_chart.setValueSelectionEnabled(hasLabelForSelected);
		data = new LineChartData(lines);
		myxisValues.add(new AxisValue(0));
		myxisValues.add(new AxisValue(100));
		myxisValues.add(new AxisValue(200));
		myxisValues.add(new AxisValue(300));
		myxisValues.add(new AxisValue(400));
		myxisValues.add(new AxisValue(500));
		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);

			axisX.setTextSize(10);
			axisX.setValues(mAxisValues);
			// axisY.setValues(myxisValues);
			if (hasAxesNames) {
				axisX.setName("Axis X");
				axisY.setName("Axis Y");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		data.setBaseValue(Float.NEGATIVE_INFINITY);
		monitor_chart.setLineChartData(data);
	}

	/**
	 * 
	 * @param chart
	 * @param xValue
	 * @param YValue
	 * @param type
	 *            1 AQI /2PM10 /3 PM25
	 */
	private void generateColumnData(ColumnChartView chart, String[] xValue,
			int[] YValue, int type) {
		ColumnChartData columnDataVaule;
		int numSubcolumns = 1;
		int numColumns = YValue.length;
		int maxYValue = 130;
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				float aqi = (float) YValue[i];
				// aqi = (float) (Math.random()*500);
				if (aqi > maxYValue) {
					maxYValue = (int) aqi;
				}
				SubcolumnValue subcolumnValue = null;
				switch (type) {
				case 1:
					subcolumnValue = new SubcolumnValue(aqi,
							getAQIColorByIntegerValue((int) aqi));
					break;
				case 2:
					subcolumnValue = new SubcolumnValue(aqi,
							getPM10ColorByIndex((int) aqi));
					break;
				case 3:
					subcolumnValue = new SubcolumnValue(aqi,
							getPM25ColorByIndex((int) aqi));
					break;
				case 4://so2
					subcolumnValue = new SubcolumnValue(aqi,
							getSO2ColorByIndex((int) aqi));
					break;
				case 5://no2
					subcolumnValue = new SubcolumnValue(aqi,
							getNO2ColorByIndex((int) aqi));
					break;
				default:
					subcolumnValue = new SubcolumnValue(aqi,
							getAQIColorByIntegerValue((int) aqi));
					break;
				}

				values.add(subcolumnValue);
			}
			axisValues.add(new AxisValue(i).setLabel(xValue[i]));
			columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
		}

		columnDataVaule = new ColumnChartData(columns);
		columnDataVaule.setAxisXBottom(new Axis(axisValues).setHasLines(true));
		columnDataVaule.setAxisYLeft(new Axis().setHasLines(true));
		chart.setColumnChartData(columnDataVaule);
		// Set value touch listener that will trigger changes for chartTop.
		// chart.setOnValueTouchListener(new ValueTouchListener());
		// Set selection mode to keep selected month column highlighted.
		chart.setValueSelectionEnabled(true);
		chart.setValueTouchEnabled(true);
		chart.setZoomType(ZoomType.HORIZONTAL);

		if (maxYValue <= 130) {
			maxYValue = 130;
		} else if (maxYValue <= 240) {
			maxYValue = 240;
		} else if (maxYValue <= 340) {
			maxYValue = 340;
		} else if (maxYValue <= 440) {
			maxYValue = 440;
		} else if (maxYValue <= 540) {
			maxYValue = 540;
		}
		// Viewport v = new Viewport(-1, 340, YValue.length, 0);
		Viewport v = new Viewport(-1, maxYValue, YValue.length, 0);
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v);
	}

	private int getAQIColorByIntegerValue(int value) {
		if ((value > 0) && (value <= 50)) {
			return (Color.parseColor("#00FF00"));
		} else if (value <= 100) {
			return (Color.parseColor("#efdc31"));
		} else if (value <= 150) {
			return (Color.parseColor("#FF7E00"));
		} else if (value <= 200) {
			return (Color.parseColor("#FF0000"));
		} else if (value <= 300) {
			return (Color.parseColor("#A0004C"));
		} else {
			return (Color.parseColor("#7D0125"));
		}
	}

	public int getPM10ColorByIndex(int defStyle) {
		if ((defStyle > -1) && (defStyle < 51)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle < 151) {
			return Color.parseColor("#efdc31");
		} else if (defStyle < 251) {
			return Color.parseColor("#FF7E00");
		} else if (defStyle < 351) {
			return Color.parseColor("#FF0000");
		} else if (defStyle < 421) {
			return Color.parseColor("#A0004C");
		} else {
			return Color.parseColor("#7D0125");
		}
	}

	public int getPM25ColorByIndex(int defStyle) {
		if ((defStyle > -1) && (defStyle < 36)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle < 76) {
			return Color.parseColor("#efdc31");
		} else if (defStyle < 116) {
			return Color.parseColor("#FF7E00");
		} else if (defStyle < 151) {
			return Color.parseColor("#FF0000");
		} else if (defStyle < 251) {
			return Color.parseColor("#A0004C");
		} else {
			return Color.parseColor("#7D0125");
		}
	}
	
	public int getSO2ColorByIndex(int defStyle) {//新
		if ((defStyle > -1) && (defStyle <= 150)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle <= 500) {
			return Color.parseColor("#efdc31");
		} else if (defStyle <=650) {
			return Color.parseColor("#FF7E00");
		} else if (defStyle <= 800) {
			return Color.parseColor("#FF0000");
		} else if (defStyle > 800) {
			return Color.parseColor("#A0004C");
		} else {
			return Color.parseColor("#A0004C");
		}
	}

	public int getNO2ColorByIndex(int defStyle) {//新
		if ((defStyle > -1) && (defStyle < 100)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle < 200) {
			return Color.parseColor("#efdc31");
		} else if (defStyle < 700) {
			return Color.parseColor("#FF7E00");
		} else if (defStyle < 1200) {
			return Color.parseColor("#FF0000");
		} else if (defStyle < 2340) {
			return Color.parseColor("#A0004C");
		} else {
			return Color.parseColor("#7D0125");
		}
	}

	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});
	}

	private View getDataPick(final int typeCode) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(
				EnvironmentWeatherRankkActivity.this).inflate(
				R.layout.datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, curYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth - 1);
		day.setLabel("日");
		day.setCyclic(true);
		curYear = Integer.parseInt(date.substring(0, 4));
		curMonth = Integer.parseInt(date.substring(5, 7));
		curDate = Integer.parseInt(date.substring(8));
		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "";
				int dayy = day.getCurrentItem() + 1;
				String dayTime = dayy + "";
				if (dayTime.length() == 1) {
					dayTime = "0" + dayTime;
				}
				if ((month.getCurrentItem() + 1) < 10) {
					str = (year.getCurrentItem() + 1950) + "-" + "0"
							+ (month.getCurrentItem() + 1) + "-" + dayTime;
				} else {
					str = (year.getCurrentItem() + 1950) + "-"
							+ (month.getCurrentItem() + 1) + "-" + dayTime;
				}
				// String str = (year.getCurrentItem() + 1950) + "-"
				// + (month.getCurrentItem() + 1) + "-"
				// + (day.getCurrentItem() + 1);
				Message msg = new Message();

				msg.what = typeCode;
				msg.obj = str;
				handler.sendMessage(msg);
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			initDay(n_year, n_month);
		}
	};

	private GvWeatherAdapter gvWeatherAdapter;

	private List<Trend> weatherData;

	private int screenWidth;

	private ImageView titleLocation;
	private SharedPreferences shares;
	private ProgressBar mProgress;
	private TextView mProgressText;
	private AlertDialog downloadDialog;



	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private void initCalendar(TextView startTv) {
		Calendar calbefore = Calendar.getInstance();
		calbefore.setTime(new Date());
		calbefore.add(Calendar.MONTH, -1);

		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 0); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		String defaultStartDate = sdf.format(dBefore); // 格式化前一天
		String defaultEndDate = sdf.format(dNow); // 格式化当前时间

		dateMonth = defaultEndDate.substring(0, 7);
		String month, day, year;
		year = defaultEndDate.substring(0, 4);
		month = defaultEndDate.substring(5, 7);
		day = defaultEndDate.substring(8, 10);
		if (day.equals("01")) {
			if (month.equals("01")) {
				year = (Integer.parseInt(year) - 1) + "";
				month = 12 + "";
			} else {
				month = (Integer.parseInt(month) - 1) + "";
			}
		}
		if (month.length() == 1) {
			month = "0" + month;
		}
		dateMonth = year + "-" + month;
		date = defaultStartDate.substring(0, 10);
		startTv.setText(defaultStartDate.substring(0, 10));

	}

	class GetTrendTask extends AsyncTask<String, Void, TrendModel> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected TrendModel doInBackground(String... params) {
			BusinessSearch search = new BusinessSearch();
			trendModel = new TrendModel();
			String url = "";
			url = UrlComponent.getTrendURL;
			time = tv_time.getText().toString();
			try {
				trendModel = search.getTrendModel(url, city, time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return trendModel;
		}

		@Override
		protected void onPostExecute(TrendModel result) {
			try {
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				dialog.dismiss();
				if (result == null)
					return;
				else {
					trendModel = result;
					listAQI7day = trendModel.getListAQI7day();
					pm10Info24Hs = trendModel.getPm10Info24Hs();
					listAQI24h = trendModel.getListAQI24h();
					listAQI30d = trendModel.getListAQI30d();
					listPM25Of24h = trendModel.getListPM25Of24h();
					listSO2Of24h = trendModel.getListSO2Of24h();//新
					listNO2Of24h = trendModel.getListNO2Of24h();//新
					aqiOf24=new int[listAQI24h.size()];
					pm10Of24=new int[pm10Info24Hs.size()];
					pm25Of24=new int[listPM25Of24h.size()];
					so2Of24=new int[listSO2Of24h.size()];//新
					no2Of24=new int[listNO2Of24h.size()];//新
					// AQI 24小时
					for (int i = 0; i < 24; i++) {
						hours[i] = "10";
						aqi24_Lists[i] = 10;

					}
					for (int i = 0; i < listAQI24h.size(); i++) {
						aqiOf24[i]=listAQI24h.get(i).getAqi();
						String time = listAQI24h.get(i).getTime();
						MyLog.i("time :" + time);
						MyLog.i("AQI" + listAQI24h.get(i).toString());
						initialHoursAQI[i] = listAQI24h.get(i).getTime();
						if (!"".equals(time)) {
							time = time.substring(11, 13);
							hours[i] = time;
							aqi24_Lists[i] = listAQI24h.get(i).getAqi();
						} else {
							hours[i] = "10";
							aqi24_Lists[i] = 10;
						}
					}
					generateColumnData(pv_24th, hours,
							aqiOf24, 1);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					// so2 24小时   新
					for (int i = 0; i < 24; i++) {
						hours25[i] = "10";
						so2_24_Lists[i] = 10;
					}
					for (int i = 0; i < listSO2Of24h.size(); i++) {
						so2Of24[i]=listSO2Of24h.get(i).getSo2();
						String time = listSO2Of24h.get(i).getTime();
						initialHoursSO2[i] = listSO2Of24h.get(i).getTime();
						if (!"".equals(time)) {
							time = time.substring(11, 13);
							hours25[i] = time;
							so2_24_Lists[i] = listSO2Of24h.get(i).getSo2();
						} else {
							hours25[i] = "10";
							so2_24_Lists[i] = 10;
						}				
					}
					generateColumnData(pathView_24h_so2, hours25, so2Of24, 4);
					
					// no2 24小时   新
					for (int i = 0; i < 24; i++) {
						hours25[i] = "10";
						no2_24_Lists[i] = 10;
					}
					for (int i = 0; i < listNO2Of24h.size(); i++) {
						no2Of24[i]=listNO2Of24h.get(i).getNo2();
						String time = listNO2Of24h.get(i).getTime();
						initialHoursNO2[i] = listNO2Of24h.get(i).getTime();
						if (!"".equals(time)) {
							time = time.substring(11, 13);
							hours25[i] = time;
							no2_24_Lists[i] = listNO2Of24h.get(i).getNo2();
						} else {
							hours25[i] = "10";
							no2_24_Lists[i] = 10;
						}				
					}
					generateColumnData(pathView_24h_no2, hours25, no2Of24, 5);
					
					
					// pm25 24小时
					for (int i = 0; i < 24; i++) {
						hours25[i] = "10";
						pm25_24_Lists[i] = 10;
					}
					for (int i = 0; i < listPM25Of24h.size(); i++) {
						pm25Of24[i]=listPM25Of24h.get(i).getPm25();
						String time = listPM25Of24h.get(i).getTime();
						initialHoursPM25[i] = listPM25Of24h.get(i).getTime();
						if (!"".equals(time)) {
							time = time.substring(11, 13);
							hours25[i] = time;
							pm25_24_Lists[i] = listPM25Of24h.get(i).getPm25();
						} else {
							hours25[i] = "10";
							pm25_24_Lists[i] = 10;
						}				
					}
					generateColumnData(pathView_24h_pm25, hours25, pm25Of24, 3);
					// PM10的24小时
					for (int i = 0; i < 24; i++) {
						hours10[i] = "10";
						pm10_24_Lists[i] = 10;
					}
					for (int i = 0; i < pm10Info24Hs.size(); i++) {
						pm10Of24[i]=pm10Info24Hs.get(i).getPm10();
						String time = pm10Info24Hs.get(i).getTime();
						initialHoursPM10[i] = pm10Info24Hs.get(i).getTime();
						if (!"".equals(time)) {
							time = time.substring(11, 13);
							hours10[i] = time;
							pm10_24_Lists[i] = pm10Info24Hs.get(i).getPm10();
						} else {
							hours10[i] = "10";
							pm10_24_Lists[i] = 10;
						}
						// String time = pm10Info24Hs.get(pm10Info24Hs.size() -
						// 1).getTime();
						// if (!"".equals(time)) {
						// time = time.substring(11, 13);
						// int time_int = Integer.parseInt(time);
						// time_int = time_int - i;
						// if (time_int < 0) {
						// time_int = time_int + 24;
						// }
						// if (time.length() == 1) {
						// time = "0" + time;
						// }
						// time = time_int + "";
						// hours10[23 - i] = time;
						// pm10_24_Lists[i] = pm10Info24Hs.get(i).getPm10();
						// } else {
						// hours10[i] = "10";
						// pm10_24_Lists[i] = 10;
						// }
					}

					generateColumnData(pathView_24h_pm10, hours10,pm10Of24, 2);
					// pathView_24h_pm25.setXCount(500, 5);
					// lists_7tian = result.getWeInfo7();
					// 最近一周空气质量
					if (listAQI7day.size() > 0) {
						for (int i = 0; i < listAQI7day.size(); i++) {
							if (i == 7) {
								break;
							}
							aqi_Lists[i] = listAQI7day.get(i).getAqi();
							String day = listAQI7day.get(i).getTime();
							if (!"".equals(day)) {
								day = day.substring(5);
								days_data[i] = day;
								initialWeeks[i] = listAQI7day.get(i).getTime();
							} else {
								days_data[i] = "";
								aqi_years[i] = 10;
							}

						}
						generateColumnData(pw, days_data, new int[] {
								aqi_Lists[0], aqi_Lists[1], aqi_Lists[2],
								aqi_Lists[3], aqi_Lists[4], aqi_Lists[5],
								aqi_Lists[6] }, 1);
						// pw.setXCount(200, 5);
						// pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1],
						// aqi_Lists[2],
						// aqi_Lists[3], aqi_Lists[4], aqi_Lists[5],
						// aqi_Lists[6] });
						// pw.setType(PathView.DAY_WEEK);
					}
					// weInfomInfoMonths = result.getWeInfoMonth();
					if (listAQI30d != null && listAQI30d.size() > 0) {
						aqi_months = new int[listAQI30d.size()];
						months = new String[listAQI30d.size()];
						for (int i = 0; i < listAQI30d.size(); i++) {
							aqi_months[i] = listAQI30d.get(i).getAqi();
							String day = listAQI30d.get(i).getTime();
							initialMonths[i] = listAQI30d.get(i).getTime();
							if (!"".equals(day)) {
								day = day.substring(5, 10);
								months[i] = day;
							} else {
								months[i] = "";
								aqi_months[i] = 10;
							}
						}
						generateColumnData(path_month, months, aqi_months, 1);
						// path_month.setXCount(500, 5);
						// path_month.setDate(aqi_months);
						// path_month.setType(PathView_Month.DAY_MONTH);
					}
					MyLog.i("trendModel>>>>>" + trendModel);
				}
				// pmTask = new GetPmTask();
				// pmTask.execute("");
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
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

}