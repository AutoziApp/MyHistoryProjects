package com.jy.environment.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIMediaObject;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQFlagType;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQReqestType;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph2;
import com.echo.holographlibrary.LineGraph3;
import com.echo.holographlibrary.LinePoint;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.EnvironmentForecastWeeklyModel;
import com.jy.environment.model.ForecastWeekly;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7_tian;
import com.jy.environment.util.ImageUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.PathView;
import com.jy.environment.widget.PathView_24;

/**
 * aqi详细viewPager
 * 
 * @author baiyuchuan
 * 
 */
public class EnvironmentAQIDetailActivity extends ActivityBase implements
		OnClickListener {
	private GetAqiDetailWeatherInfo24HourTask getAqiDetailWeatherInfoTask;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textView1, textView2, textView3, textView4;
	private ArrayList<View> views;
	private int lineoff = 0;// 横线的偏移量
	private int currIndex = 0;// 当前页的编号
	private int lineW;// 图片宽度
	private View view1, view2, view3, view4;
	private ListView listView3, listViewWeekly;
	private TextView cityName;
	private TextView aqi;
	private TextView number;
	private TextView weeks, aqi_level, tv_nodata;
	private ImageView imgForecastWeeklyAqiLevel;
	String city;
	ImageView back2;
	TextView aqi_level_tomorrow;
	RelativeLayout aqi_graphical_layout;
	LineGraph2 li1;
	LineGraph3 li1Weekly;
	private LinearLayout ll_right;

	TextView aqi_number, pollution_level, share_tips, pm25, pm10, no2, so2, co,
			o3, aqi_advice, mingri, dailydetail_jun;
	ImageView notif_level;
	ImageView aqi_img;
	LinearLayout share_tips_layout, aqi_share_btn;
	String aqi1 = "";
	TextView aqi_option_1, aqi_option_2, aqi_option_3, aqi_option_4,
			aqi_option_5, aqi_option_6, aqi_option_7, aqi_option_8,
			aqi_option_9;
	HorizontalScrollView horizontal_scrollview;
	boolean flag = false;
	String[] str = {
			"我选择地铁。坐地铁出行100公里，可省油六分之五。如果全国私家车主都这么做，那么每年可节油2.1亿升，减排二氧化碳46万吨",
			"我选择公交车。坐地铁出行100公里，可省油六分之五。如果全国私家车主都这么做，那么每年可节油2.1亿升，减排二氧化碳46万吨",
			"我选择骑车。每月少开一天，每车每年可节油约44升，相应减排二氧化碳98千克。如果全国私家车主都做到，每年可节油约5.54亿升，减排二氧化碳122万吨。",
			"今天我开车出行，竟然比一般人多使用燃油约3.67升，排放二氧化碳约8.16千克。明天起我也要参与#微保天气-环保使者#活动。绿色出行，拒绝成为空气污染的元凶。",
			"我选择步行。每月少开一天，每车每年可节油约44升，相应减排二氧化碳98千克。如果全国私家车主都做到，每年可节油约5.54亿升，减排二氧化碳122万吨。",
			"我正在参与#微保天气-环保使者#活动。保护树木和草地，绿色植物能够有效吸收大气污染，还能美化环境!",
			"我正在参与#微保天气-环保使者#活动。每月用手洗代替一次机洗，每台洗衣机每年节能约1.4千克标准煤，减排二氧化碳3.6千克。全国1.9亿台洗衣机每月少用一次，每年节能约26万吨标准煤，减排二氧化碳68.4万吨",
			"我正在参与#微保天气-环保使者#活动。在夏季的3个月里平均每月少喝1瓶啤酒，1人1年可节能约0.23千克标准煤，相应减排二氧化碳0.6千克。每年可节能约29.7万吨标准煤，减排二氧化碳78万吨",
			"我正在参与#微保天气-环保使者#活动。吸烟有害健康，香烟生产还消耗能源。1天少抽1支烟，每人每年节能约0.14千克标准煤，减排二氧化碳0.37千克。全国3.5亿烟民每年节能约5万吨标准煤，减排二氧化碳13万吨" };

	LinearLayout ll, ll_weekly;
	View forcastView, forcastView_weekly;
	ScrollView scrollView;
	String currentCity;
	ImageView back7;
	ImageView fengche;
	private List<Item> items;
	private List<ForecastWeekly> ForecastWeeks;
	// 传递过来的intent
	Intent currentIntent;
	private String pm, position_name, level;
	ProgressDialog prDialog;
	private WeiBaoApplication mApplication;

	boolean xians = false;

	List<WeatherInfo24> weatherLists;
	TextView tv_aqi_number;
	TextView[] dates;
	TextView[] WeeksForecast;
	List<WeatherInfo7_tian> lists_7tian = new ArrayList<WeatherInfo7_tian>();
	PathView pw;
	PathView_24 pv_24th;
	int[] aqi24_Lists = new int[24];
	int[] aqi_Lists = new int[7];
	public static String[] days_data = new String[7];
	public static String[] hours = new String[24];
	Date date;
	int dingwei = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				for (int i = 0; i < weatherLists.size(); i++) {
					String time = weatherLists.get(0).getUpdate_time();
					if (!"".equals(time)) {
						time = time.substring(11, 13);
						int time_int = Integer.parseInt(time);
						time_int = time_int + i;
						if (time_int >= 24) {
							time_int = time_int - 24;
						}
						time = time_int + "时";
						hours[i] = time;
						aqi24_Lists[i] = weatherLists.get(i).getAqi();
					} else {
						hours[i] = "1日10时";
						aqi24_Lists[i] = 10;
					}

				}
				pv_24th.setXCount(500, 5);
				pv_24th.setType(PathView_24.DAY_HOUR);
				pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
						aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4],
						aqi24_Lists[5], aqi24_Lists[6], aqi24_Lists[7],
						aqi24_Lists[8], aqi24_Lists[9], aqi24_Lists[10],
						aqi24_Lists[11], aqi24_Lists[12], aqi24_Lists[13],
						aqi24_Lists[14], aqi24_Lists[15], aqi24_Lists[16],
						aqi24_Lists[17], aqi24_Lists[18], aqi24_Lists[19],
						aqi24_Lists[20], aqi24_Lists[21], aqi24_Lists[22],
						aqi24_Lists[23] });
				break;
			case 1:
				ListAdapter adapter2 = new ListAdapter();
				listView3.setAdapter(adapter2);
				listView3.setSelection(dingwei);

				adapter2.notifyDataSetChanged();
				break;

			case 2:
				ListAdapterWeekly adapterWeekly = new ListAdapterWeekly();
				listViewWeekly.setAdapter(adapterWeekly);
				adapterWeekly.notifyDataSetChanged();

				drawBitmapWeekly();
				break;
			case 5:
				tv_nodata.setVisibility(View.VISIBLE);
				scrollView.setVisibility(View.GONE);
				break;
			case 6:
				if (lists_7tian.size() > 0) {
					for (int i = 0; i < lists_7tian.size(); i++) {
						aqi_Lists[i] = lists_7tian.get(i).getAqi();
						String day = lists_7tian.get(i).getUpdate_time();
						if (!"".equals(day)) {
							day = day.substring(5, 10);
							days_data[i] = day;
						} else {
							days_data[i] = "";
							aqi24_Lists[i] = 10;
						}

					}
					pw.setXCount(500, 5);
					pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1],
							aqi_Lists[2], aqi_Lists[3], aqi_Lists[4],
							aqi_Lists[5], aqi_Lists[6] });
					pw.setType(PathView.DAY_WEEK);
				} else {
					return;
				}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_aqidetail_activity);
		mApplication = WeiBaoApplication.getInstance();
		currentCity = WeiBaoApplication.getInstance().selectedCity;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		currentIntent = getIntent();
		if (currentIntent.getStringExtra("from").equals("dingwei")) {
			pm = currentIntent.getStringExtra("pm");
			position_name = currentIntent.getStringExtra("position_name");
			level = currentIntent.getStringExtra("level");
		}
		getAqiDetailWeatherInfoTask = new GetAqiDetailWeatherInfo24HourTask();
		InitImageView();
		InitTextView();
		InitViewPager();
		items = new ArrayList<Item>();
		ForecastWeeks = new ArrayList<ForecastWeekly>();

	}

	private void initWeatherInfo() {
		getAqiDetailWeatherInfoTask.execute("");
	}

	private void initWeatherInfo7_tian() {
		GetAqiDetailWeatherInfo7DayTask aqiDetailWeatherInfo7DayTask = new GetAqiDetailWeatherInfo7DayTask();
		aqiDetailWeatherInfo7DayTask.execute("");
	}

	private void refreshData() {
		GetAqiDetailItemTask aqiDetailItemTask = new GetAqiDetailItemTask();
		aqiDetailItemTask.execute();
	}

	private void forecast_weekly_data() {
		GetAqiDetailForecastWeeklyTask aqiDetailForecastWeeklyTask = new GetAqiDetailForecastWeeklyTask();
		aqiDetailForecastWeeklyTask.execute();
	}

	LinePoint p1, p2;

	public void drawLine(LinePoint p1, LinePoint p2, int[] lists, int i) {

		p1 = new LinePoint();
		p2 = new LinePoint();
		Line line = new Line();

		if (lists[i] <= 50) {
			line.setColor(Color.parseColor("#00FF00"));
		} else if (lists[i] <= 100) {
			line.setColor(Color.parseColor("#FFFF00"));
		} else if (lists[i] <= 150) {
			line.setColor(Color.parseColor("#FF7E00"));
		} else if (lists[i] <= 200) {
			line.setColor(Color.parseColor("#FF0000"));
		} else if (lists[i] <= 251) {
			line.setColor(Color.parseColor("#A0004C"));
		} else {
			line.setColor(Color.parseColor("#7D0125"));
		}

		p1.setY(lists[i]);
		p1.setX(i * 2 + 1);

		p2.setY(lists[i + 1]);
		p2.setX((i + 1) * 2 + 1);

		line.addPoint(p1);
		line.addPoint(p2);
		li1.addLine(line);
	}

	LinePoint p1week, p2week;

	public void drawLineWeekly(LinePoint p1, LinePoint p2, int[] lists, int i) {

		p1 = new LinePoint();
		p2 = new LinePoint();
		Line line = new Line();

		if (lists[i] <= 50) {
			line.setColor(Color.parseColor("#00FF00"));
		} else if (lists[i] <= 100) {
			line.setColor(Color.parseColor("#FFFF00"));
		} else if (lists[i] <= 150) {
			line.setColor(Color.parseColor("#FF7E00"));
		} else if (lists[i] <= 200) {
			line.setColor(Color.parseColor("#FF0000"));
		} else if (lists[i] < 251) {
			line.setColor(Color.parseColor("#A0004C"));
		} else {
			line.setColor(Color.parseColor("#7D0125"));
		}

		p1.setY(lists[i]);
		p1.setX(i * 2 + 1);		

		p2.setY(lists[i + 1]);
		p2.setX((i + 1) * 2 + 1);

		line.addPoint(p1);
		line.addPoint(p2);
		li1Weekly.addLine(line);
	}

	private void drawBitmapWeekly() {

		int aqiNumber;
		int high = 0;
		int low = 0;

		int[] aqiLists = new int[ForecastWeeks.size()];

		if (ForecastWeeks.size() <= 0) {
			return;
		}

		for (int i = 0; i < ForecastWeeks.size(); i++) {

			String weekStr = ForecastWeeks.get(i).getDateString();
			WeeksForecast[i].setText(weekStr);

			String aa = ForecastWeeks.get(i).getAQIlevel();
			if (aa.equals("优")) {
				aqiLists[i] = 45;
			} else if (aa.equals("良")) {
				aqiLists[i] = 75;
			} else if (aa.equals("轻度污染")) {
				aqiLists[i] = 120;
			} else if (aa.equals("中度污染")) {
				aqiLists[i] = 160;
			} else if (aa.equals("重度污染")) {
				aqiLists[i] = 230;
			} else if (aa.equals("严重污染")) {
				aqiLists[i] = 305;
			}

		}

		for (int i = 0; i < aqiLists.length - 1; i++) {

			drawLineWeekly(p1week, p2week, aqiLists, i);

		}

		Arrays.sort(aqiLists);

		li1Weekly.setRangeY(aqiLists[0] - 15,
				aqiLists[ForecastWeeks.size() - 1] + 13);

		li1Weekly
				.setOnPointClickedListener(new com.echo.holographlibrary.LineGraph3.OnPointClickedListener() {
					@Override
					public void onClick(int lineIndex, int pointIndex) {
					}
				});

		ll_weekly.removeAllViews();
		ll_weekly.addView(forcastView_weekly);
	}

	private void InitViewPager() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.vPager);
		views = new ArrayList<View>();

		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.environment_current_dailydetail_item,
				null);
		RelativeLayout yy10 = (RelativeLayout) view1
				.findViewById(R.id.aqi_graphical_layout);
		try {
			yy10.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
					EnvironmentAQIDetailActivity.this, R.drawable.yy10)));
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		ll_right = (LinearLayout) view1.findViewById(R.id.ll_right);
		findViewById();
		initListener();
		city = getIntent().getExtras().getString("city");
		GetKongqizhishuTask getKongqizhishuTask = new GetKongqizhishuTask();
		getKongqizhishuTask.execute(city);
		view2 = inflater.inflate(R.layout.environment_current_air_aqi_item,
				null);
		LinearLayout yy4 = (LinearLayout) view2.findViewById(R.id.yy4);
		try {
			yy4.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
					EnvironmentAQIDetailActivity.this, R.drawable.yy13)));
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		view3 = inflater.inflate(
				R.layout.environment_aqidetail_activity_trends_item, null);
		LinearLayout yy13 = (LinearLayout) view3.findViewById(R.id.yy13);
		try {
			yy13.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
					EnvironmentAQIDetailActivity.this, R.drawable.yy13)));
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		view4 = inflater.inflate(R.layout.environment_forecast_weekly, null);
		LinearLayout yy15 = (LinearLayout) view4.findViewById(R.id.yy15);
		// yy15.setBackgroundDrawable(new
		// BitmapDrawable(ImageUtils.readBitmap(CurrentRank.this,
		// R.drawable.yy15)));
		try {
			yy15.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
					EnvironmentAQIDetailActivity.this, R.drawable.yy13)));
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		tv_nodata = (TextView) view4.findViewById(R.id.tv_nodata);
		scrollView = (ScrollView) view4.findViewById(R.id.scrollView);
		weatherLists = new ArrayList<WeatherInfo24>();
		dates = new TextView[23];
		forcastView = LayoutInflater.from(EnvironmentAQIDetailActivity.this)
				.inflate(R.layout.environment_aqidetail_linegraph, null);
		li1 = (LineGraph2) forcastView.findViewById(R.id.linegraph1);
		dates[0] = (TextView) forcastView.findViewById(R.id.week1Text);
		dates[1] = (TextView) forcastView.findViewById(R.id.week2Text);
		dates[2] = (TextView) forcastView.findViewById(R.id.week3Text);
		dates[3] = (TextView) forcastView.findViewById(R.id.week4Text);
		dates[4] = (TextView) forcastView.findViewById(R.id.week5Text);
		dates[5] = (TextView) forcastView.findViewById(R.id.week6Text);
		dates[6] = (TextView) forcastView.findViewById(R.id.week7Text);
		dates[7] = (TextView) forcastView.findViewById(R.id.week8Text);
		dates[8] = (TextView) forcastView.findViewById(R.id.week9Text);
		dates[9] = (TextView) forcastView.findViewById(R.id.week10Text);
		dates[10] = (TextView) forcastView.findViewById(R.id.week11Text);
		dates[11] = (TextView) forcastView.findViewById(R.id.week12Text);
		dates[12] = (TextView) forcastView.findViewById(R.id.week13Text);
		dates[13] = (TextView) forcastView.findViewById(R.id.week14Text);
		dates[14] = (TextView) forcastView.findViewById(R.id.week15Text);
		dates[15] = (TextView) forcastView.findViewById(R.id.week16Text);
		dates[16] = (TextView) forcastView.findViewById(R.id.week17Text);
		dates[17] = (TextView) forcastView.findViewById(R.id.week18Text);
		dates[18] = (TextView) forcastView.findViewById(R.id.week19Text);
		dates[19] = (TextView) forcastView.findViewById(R.id.week20Text);
		dates[20] = (TextView) forcastView.findViewById(R.id.week21Text);
		dates[21] = (TextView) forcastView.findViewById(R.id.week22Text);
		dates[22] = (TextView) forcastView.findViewById(R.id.week23Text);

		WeeksForecast = new TextView[6];
		ll_weekly = (LinearLayout) view4.findViewById(R.id.linearlayout_weekly);
		forcastView_weekly = LayoutInflater.from(
				EnvironmentAQIDetailActivity.this).inflate(
				R.layout.environment_forecast_weekly_linegraph, null);
		li1Weekly = (LineGraph3) forcastView_weekly
				.findViewById(R.id.linegraph1);

		WeeksForecast[0] = (TextView) forcastView_weekly
				.findViewById(R.id.week0Text);
		WeeksForecast[1] = (TextView) forcastView_weekly
				.findViewById(R.id.week1Text);
		WeeksForecast[2] = (TextView) forcastView_weekly
				.findViewById(R.id.week2Text);
		WeeksForecast[3] = (TextView) forcastView_weekly
				.findViewById(R.id.week3Text);
		WeeksForecast[4] = (TextView) forcastView_weekly
				.findViewById(R.id.week4Text);
		WeeksForecast[5] = (TextView) forcastView_weekly
				.findViewById(R.id.week5Text);

		views.add(view1);
		views.add(view3);
		views.add(view4);
		views.add(view2);

		listView3 = (ListView) view2.findViewById(R.id.listView);
		listViewWeekly = (ListView) view4.findViewById(R.id.listView_weekly);

		pv_24th = (PathView_24) view3.findViewById(R.id.pv_24th);
		for (int i = 0; i < 24; i++) {
			hours[i] = "10时";
			aqi24_Lists[i] = 10;
		}
		pv_24th.setXCount(500, 5);
		pv_24th.setType(PathView_24.DAY_HOUR);
		pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
				aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
				aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
				aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
				aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
				aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
				aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
				aqi24_Lists[22], aqi24_Lists[23] });

		initWeatherInfo();
		pw = (PathView) view3.findViewById(R.id.pv);
		for (int i = 0; i < 7; i++) {
			days_data[i] = "";
			aqi24_Lists[i] = 10;
		}
		pw.setXCount(500, 5);
		pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1], aqi_Lists[2],
				aqi_Lists[3], aqi_Lists[4], aqi_Lists[5], aqi_Lists[6] });
		pw.setType(PathView.DAY_WEEK);

		initWeatherInfo7_tian();
		viewPager.setAdapter(new MyViewPagerAdapter());
		viewPager.setOffscreenPageLimit(3);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private void InitTextView() {

		back2 = (ImageView) findViewById(R.id.goback2);
		back2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		textView1 = (TextView) findViewById(R.id.tv_recent);
		textView2 = (TextView) findViewById(R.id.tv_hour);
		textView3 = (TextView) findViewById(R.id.tv_month);
		textView4 = (TextView) findViewById(R.id.tv_forecast);

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(0);
			}
		});

		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(3);
			}
		});

		textView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(1);
			}
		});

		textView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(2);
			}
		});
	}

	private int lastRawX = 0;
	private int lastRawY = 0;
	private int lastRawLeft = 0;
	private int lastRawBotoom = 0;
	private int xLast = 0;
	private int YLast = 0;
	int width, height;

	private void initListener() {
		// TODO Auto-generated method stub
		aqi_img.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					lastRawX = (int) event.getRawX();
					lastRawY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastRawX;
					int dy = (int) event.getRawY() - lastRawY;

					int left = v.getLeft() + dx;
					int top = v.getTop() + dy;
					int right = v.getRight() + dx;
					int bottom = v.getBottom() + dy;

					if (left < ll_right.getRight()) {
						left = ll_right.getRight();
						right = left + v.getWidth();
					}
					if (0 != share_tips_layout.getBottom()
							&& bottom > share_tips_layout.getBottom()) {
						bottom = share_tips_layout.getBottom();
						top = bottom - v.getHeight();
					} else if (bottom > (ll_right.getBottom() + 10)) {
						bottom = ll_right.getBottom() + 10;
						top = bottom - v.getHeight();
					}
					if (right > width) {
						right = width;
						left = right - v.getWidth();
					}
					if (top < 0) {
						top = 0;
						bottom = top + v.getHeight();
					}
					v.layout(left, top, right, bottom);
					lastRawX = (int) event.getRawX();
					lastRawY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					int[] location2 = new int[2];
					aqi_img.getLocationOnScreen(location2);
					int nowx = location2[0];
					int nowy = location2[1];
					if (Math.abs(nowx - xLast) > 10
							|| Math.abs(nowy - YLast) > 10) {
						int[] location = new int[2];
						aqi_img.getLocationOnScreen(location);
						xLast = location[0];
						YLast = location[1];
						return true;
					} else {
						return false;
					}
				}
				return false;
			}
		});
		aqi_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				if (xians) {
					share_tips_layout.setVisibility(View.GONE);
					xians = !xians;
				}

				if (!flag) {

					horizontal_scrollview.setVisibility(View.VISIBLE);

				} else {
					horizontal_scrollview.setVisibility(View.GONE);
				}
				flag = !flag;
			}
		});

		aqi_option_1.setOnClickListener(this);
		aqi_option_2.setOnClickListener(this);
		aqi_option_3.setOnClickListener(this);
		aqi_option_4.setOnClickListener(this);
		aqi_option_5.setOnClickListener(this);
		aqi_option_6.setOnClickListener(this);
		aqi_option_7.setOnClickListener(this);
		aqi_option_8.setOnClickListener(this);
		aqi_option_9.setOnClickListener(this);
		aqi_share_btn.setOnClickListener(this);
		aqi_graphical_layout.setOnClickListener(this);

	}

	private void startGame() {
		uiHandler.sendEmptyMessageDelayed(1, 100);
	}

	// 更新时间的显示
	private void updateClockUI() {
		aqi_number.setText(getMin());

	}

	public void addTimeUsed() {

		switch (caoz) {

		case 1:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 12 == timeUsedInSec) {
				paused = true;
			}

			break;
		case 2:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 16 == timeUsedInSec) {
				paused = true;
			}

			break;
		case 3:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 24 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 4:
			timeUsedInSec = timeUsedInSec + 1;
			if (shu + 36 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 5:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 38 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 6:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 18 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 7:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 10 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 8:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 10 == timeUsedInSec) {
				paused = true;
			}
			break;
		case 9:
			timeUsedInSec = timeUsedInSec - 1;
			if (shu - 38 == timeUsedInSec) {
				paused = true;
			}
			break;

		default:
			break;
		}

		// timeUsed = this.getMin() + ":" + this.getSec();
	}

	public CharSequence getMin() {
		if (0 >= timeUsedInSec) {
			return "0";
		}
		return String.valueOf(timeUsedInSec);
	}

	private boolean paused = false;
	private int timeUsedInSec = 90;
	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (!paused) {
					addTimeUsed();
					updateClockUI();

					uiHandler.sendEmptyMessageDelayed(1, 70);
				} else {
					xians = true;
					// aqi_level_tomorrow.setVisibility(View.GONE);
					switch (caoz) {

					case 1:
						share_tips_layout.setVisibility(View.VISIBLE);

						share_tips
								.setText("你选择地铁出行，相比开车出行节约了约2.19升燃油，减少二氧化碳排放约4.87千克。");
						break;
					case 2:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("你选择公交车出行，相比开车出行节约了约2.19升燃油，减少二氧化碳排放约4.87千克。");
						break;
					case 3:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("你选择骑车出行，相比开车出行节约了约3.67升燃油，减少二氧化碳排放约8.16千克。");
						break;
					case 4:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("你选择汽车出行，相比一般人多使用燃油约3.67升，排放二氧化碳约8.16千克。");
						break;
					case 5:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("你选择步行出行，相比开车出行节约了约3.67升燃油，减少二氧化碳排放约8.16千克。");
						break;
					case 6:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("你保护了一棵绿色植物，它在未来的一年可能为地球吸收18.3千克的二氧化碳。");
						break;
					case 7:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips.setText("你选择了手洗衣物，大约节约了116.7克煤，减排二氧化碳300克。");
						break;
					case 8:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips.setText("少喝一瓶啤酒，可以节约19.2克煤，减少二氧化碳排放200克。");
						break;
					case 9:
						share_tips_layout.setVisibility(View.VISIBLE);
						share_tips
								.setText("拒绝抽烟，1天少抽1支烟，每人每年节能约0.14千克标准煤，减排二氧化碳0.37千克。");
						break;

					default:
						break;
					}

				}

				break;
			default:
				break;
			}
		}

	};
	int caoz = 1;
	int shu;

	void dianjishie(int shu, int caoz) {
		timeUsedInSec = shu;
		this.caoz = caoz;
		this.shu = shu;
		uiHandler.removeMessages(1);
		startGame();
		paused = false;

	}

	@Override
	public void onClick(View v) {
		if (aqi1.equals("")) {
			return;
		}
		int aqis = Integer.parseInt(aqi1);

		if (xians) {
			share_tips_layout.setVisibility(View.GONE);
			xians = false;
		}
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.aqi_option_1:

			dianjishie(aqis, 1);
			break;
		case R.id.aqi_option_2:
			dianjishie(aqis, 2);
			break;
		case R.id.aqi_option_3:
			dianjishie(aqis, 3);
			break;
		case R.id.aqi_option_4:
			dianjishie(aqis, 4);
			break;
		case R.id.aqi_option_5:
			dianjishie(aqis, 5);
			break;
		case R.id.aqi_option_6:
			dianjishie(aqis, 6);
			break;
		case R.id.aqi_option_7:
			dianjishie(aqis, 7);
			break;
		case R.id.aqi_option_8:
			dianjishie(aqis, 8);
			break;
		case R.id.aqi_option_9:
			dianjishie(aqis, 9);
			break;
		case R.id.aqi_graphical_layout:
			share_tips_layout.setVisibility(View.GONE);
			xians = false;
			break;
		case R.id.aqi_share_btn:
			try {
				if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
					Toast.makeText(EnvironmentAQIDetailActivity.this,
							"请检查您的网络", 0).show();
					return;
				}
				Bitmap bitmap = GetCurrentScreen();
				Uri uri = null;
				try {
					uri = Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(), bitmap, null, null));
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					Toast.makeText(EnvironmentAQIDetailActivity.this, "截图失败", 0)
							.show();
					return;
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(EnvironmentAQIDetailActivity.this, "截图失败", 0)
							.show();
					return;
				}
				String content = "微保环境，共建美好环境";
				mSocialShare = Frontia.getSocialShare();
				mSocialShare.setContext(this);
				mSocialShare.setClientId(MediaType.WEIXIN.toString(),
						"wx541df6ed6e9babc0");
				mSocialShare.setClientId(MediaType.SINAWEIBO.toString(),
						"991071488");
				mSocialShare.setClientId(MediaType.QQFRIEND.toString(),
						"100358052");
				mSocialShare.setParentView(getWindow().getDecorView());
				mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "微保");
				mImageContent.setTitle("微保");
				mImageContent.setContent(content);
				mImageContent.setLinkUrl("http://www.wbapp.com.cn");
				mImageContent.setImageUri(uri);
				mImageContent
						.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
				mImageContent.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
				mImageContent.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
				mImageContent
						.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
				mImageContent.setImageData(bitmap);
				mSocialShare.show(EnvironmentAQIDetailActivity.this.getWindow()
						.getDecorView(), mImageContent, FrontiaTheme.LIGHT,
						new ShareListener());
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(EnvironmentAQIDetailActivity.this, "分享失败", 0)
						.show();
			}

		default:
			break;
		}
	}

	private Location getLocation() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

		String provider = locationManager.getBestProvider(criteria, false); // 获取GPS信息
		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		return location;
	}

	public String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {

			while ((line = reader.readLine()) != null) {

				sb.append(line + "/n");

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}
		return sb.toString();

	}

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void InitImageView() {
		// TODO Auto-generated method stub
		imageView = (ImageView) findViewById(R.id.iv_bottom_line);

		lineW = imageView.getLayoutParams().width;
		// BitmapFactory.decodeResource(getResources(), id);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		lineoff = ((screenW - 40) / 4 - lineW - 8) / 2;

		Matrix matrix = new Matrix();
		matrix.postTranslate(lineoff, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		aqi_level_tomorrow = (TextView) view1
				.findViewById(R.id.aqi_level_tomorrow);
		dailydetail_jun = (TextView) view1.findViewById(R.id.dailydetail_jun);
		aqi_number = (TextView) view1.findViewById(R.id.aqi_number_text);
		pollution_level = (TextView) view1.findViewById(R.id.aqi_level_text);
		aqi_img = (ImageView) view1.findViewById(R.id.aqi_img);

		aqi_graphical_layout = (RelativeLayout) view1
				.findViewById(R.id.aqi_graphical_layout);

		pm25 = (TextView) view1.findViewById(R.id.aqi_detail_pm25);
		pm10 = (TextView) view1.findViewById(R.id.aqi_detail_pm10);
		no2 = (TextView) view1.findViewById(R.id.aqi_detail_no2);
		so2 = (TextView) view1.findViewById(R.id.aqi_detail_so2);
		co = (TextView) view1.findViewById(R.id.aqi_detail_co);
		o3 = (TextView) view1.findViewById(R.id.aqi_detail_o3);
		aqi_advice = (TextView) view1.findViewById(R.id.aqi_advice);
		mingri = (TextView) view1.findViewById(R.id.aqi_level_tomorrow);
		notif_level = (ImageView) view1.findViewById(R.id.notif_level);
		aqi_option_1 = (TextView) view1.findViewById(R.id.aqi_option_1);
		aqi_option_2 = (TextView) view1.findViewById(R.id.aqi_option_2);
		aqi_option_3 = (TextView) view1.findViewById(R.id.aqi_option_3);
		aqi_option_4 = (TextView) view1.findViewById(R.id.aqi_option_4);
		aqi_option_5 = (TextView) view1.findViewById(R.id.aqi_option_5);
		aqi_option_6 = (TextView) view1.findViewById(R.id.aqi_option_6);
		aqi_option_7 = (TextView) view1.findViewById(R.id.aqi_option_7);
		aqi_option_8 = (TextView) view1.findViewById(R.id.aqi_option_8);
		aqi_option_9 = (TextView) view1.findViewById(R.id.aqi_option_9);

		share_tips = (TextView) view1.findViewById(R.id.aqi_share_tips);
		aqi_share_btn = (LinearLayout) view1.findViewById(R.id.aqi_share_btn);
		share_tips_layout = (LinearLayout) view1
				.findViewById(R.id.aqi_share_tips_layout);
		horizontal_scrollview = (HorizontalScrollView) view1
				.findViewById(R.id.aqi_options_layout);
	}

	class MyViewPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (0 == position) {
				View view = views.get(position);
				ImageView aqi_img = (ImageView) view.findViewById(R.id.aqi_img);
				String key = "CurrentRank";
				aqi_img.setTag(aqi_img);
			}
			container.addView(views.get(position));
			return views.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = lineoff * 2 + lineW;// 页卡1-->页卡2 偏移量
		// int two = lineoff * 2;// 页卡2-->页卡3 偏移量

		int one = lineoff * 2 + lineW;// 页卡1-->页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {

			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				GetKongqizhishuTask name = new GetKongqizhishuTask();
				name.execute(city);
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(lineoff, one, 0, 0);

				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(lineoff, two, 0, 0);

				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				forecast_weekly_data();
				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(lineoff, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else {
					animation = new TranslateAnimation(two, three, 0, 0);
				}

				refreshData();
				break;
			}
			currIndex = arg0;

			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

		}

	}

	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(EnvironmentAQIDetailActivity.this)
					.inflate(R.layout.environment_current_city_ranking_item,
							null);

			cityName = (TextView) view.findViewById(R.id.tv_city_name);
			number = (TextView) view.findViewById(R.id.tv_city_number);
			number.setText(items.get(position).getRank());
			aqi = (TextView) view.findViewById(R.id.tv_city_aqi);
			cityName.setText(items.get(position).getCityName());
			if (items.get(position).getCityName().equals(currentCity)) {
				// cityName.setTextColor(color.yellow);
				view.setBackgroundColor(Color.argb(50, 255, 255, 255));
				cityName.setTextSize(20.0f);
				aqi.setTextSize(15.0f);
				number.setTextSize(15.0f);
//				aqi.setTextColor(color.yellow);
//				number.setTextColor(color.yellow);
			}
			aqi.setText(items.get(position).getIndex());
			return view;
		}

	}

	private class ListAdapterWeekly extends BaseAdapter {

		@Override
		public int getCount() {
			return ForecastWeeks.size();
		}

		@Override
		public Object getItem(int position) {
			return ForecastWeeks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(EnvironmentAQIDetailActivity.this)
					.inflate(R.layout.environment_forecast_weekly_list_item,
							null);
			weeks = (TextView) view.findViewById(R.id.tv_weeks);
			tv_aqi_number = (TextView) view.findViewById(R.id.tv_aqi_number);

			aqi_level = (TextView) view.findViewById(R.id.tv_aqi_level);
			imgForecastWeeklyAqiLevel = (ImageView) view
					.findViewById(R.id.img_forecastweekly_aqi_level);

			if ("优".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_0);
			} else if ("良".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_1);
			} else if ("轻度污染".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_2);
			} else if ("中度污染".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_3);
			} else if ("重度污染".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_4);
			} else if ("严重污染".equals(ForecastWeeks.get(position).getAQIlevel())) {
				imgForecastWeeklyAqiLevel
						.setImageResource(R.drawable.forecastweekly_aqi_level_5);
			} else {
			}
			String ss = ForecastWeeks.get(position).getAQIlevel();
			if (ss.indexOf("污染") >= 0) {
				ss = ss.substring(0, 2);
			}
			weeks.setText(ForecastWeeks.get(position).getDateString());
			aqi_level.setText(ss);
			tv_aqi_number.setText(ForecastWeeks.get(position).getAqi() + "");

			return view;
		}

	}

	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			MyLog.i("share success");
			Toast.makeText(EnvironmentAQIDetailActivity.this, "分享成功", 2000)
					.show();
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			MyLog.i("share errCode :" + errCode);
			Toast.makeText(EnvironmentAQIDetailActivity.this, "分享失败", 2000)
					.show();
		}

		@Override
		public void onCancel() {
			MyLog.i("share cancel");
			// Toast.makeText(CurrentRank.this, "分享取消", 2000).show();
		}
	}

	private Bitmap GetCurrentScreen() {
		// 1.构建Bitmap
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕
		View decorview = this.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		bmp = decorview.getDrawingCache();
		return bmp;
	}

	public class GetAqiDetailWeatherInfo24HourTask extends
			AsyncTask<String, Void, List<WeatherInfo24>> {
		@Override
		protected List<WeatherInfo24> doInBackground(String... params) {
			String url = UrlComponent.getAirHistoryInfo_Get(currentCity);
			MyLog.i(">>>>>>>>>>asynctask" + url);
			BusinessSearch search = new BusinessSearch();
			List<WeatherInfo24> _Result;
			try {
				/**
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				_Result = search.getAqiDetailWeatherInfo24Hour(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<WeatherInfo24> result) {
			super.onPostExecute(result);
			if (null == result) {
				return;
			}
			if (null != result && null != weatherLists
					&& weatherLists.size() > 0) {
				weatherLists.clear();
			}
			weatherLists = result;
			Message msg = new Message();
			msg.what = 0;
			mHandler.sendMessage(msg);

		}

	}

	public class GetAqiDetailWeatherInfo7DayTask extends
			AsyncTask<String, Void, List<WeatherInfo7_tian>> {
		@Override
		protected List<WeatherInfo7_tian> doInBackground(String... params) {
			String url = UrlComponent.getAirBycityWeek_Get(currentCity);
			BusinessSearch search = new BusinessSearch();
			List<WeatherInfo7_tian> _Result;
			try {
				/**
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				_Result = search.getAqiDetailWeatherInfo7Day(url, 3600);
				return _Result;
			} catch (Exception e) {
				MyLog.i(e.toString());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<WeatherInfo7_tian> result) {
			super.onPostExecute(result);
			if (null == result) {
				return;
			}
			if (null != result && lists_7tian.size() > 0) {
				lists_7tian.clear();
			}
			lists_7tian = result;
			Message msg = new Message();
			msg.what = 6;
			mHandler.sendMessage(msg);

		}

	}

	public class GetAqiDetailItemTask extends
			AsyncTask<String, Void, List<Item>> {
		@Override
		protected List<Item> doInBackground(String... params) {
			String url = UrlComponent.cityRankingUrl_Post;
			BusinessSearch search = new BusinessSearch();
			List<Item> _Result;
			try {
				/**
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				_Result = search.getAqiDetailItem(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Item> result) {
			super.onPostExecute(result);
			if (null == result) {
				return;
			}
			if (null != result && null != items && items.size() > 0) {
				items.clear();
			}
			for (int i = 0; i < result.size(); i++) {
				items.add(result.get(i));
				if (result.get(i).getCityName().equals(currentCity)) {
					if (i > 7) {
						dingwei = i - 7;
					} else {
						dingwei = i;
					}
				}
			}
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
		}

	}

	public class GetAqiDetailForecastWeeklyTask extends
			AsyncTask<String, Void, EnvironmentForecastWeeklyModel> {
		@Override
		protected EnvironmentForecastWeeklyModel doInBackground(
				String... params) {
			String url = "";
			if ("北京".equals(currentCity)) {
				url = UrlComponent.beijingForecastInfoUrl_Post;
			} else {
				url = UrlComponent.getForecastInfoByCity_Get(currentCity);
			}
			MyLog.i(">>>>>>>>>>>>>>>hgghghghghh" + url);
			BusinessSearch search = new BusinessSearch();
			EnvironmentForecastWeeklyModel _Result;
			try {
				/**
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				_Result = search.getAqiDetailForecastWeekly(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(EnvironmentForecastWeeklyModel result) {
			super.onPostExecute(result);
			if (null == result) {
				return;
			}
			if (null != result && !result.isFlag()) {
				Message msg = new Message();
				msg.what = 5;
				mHandler.sendMessage(msg);
				return;
			}
			if (ForecastWeeks.size() > 0) {
				ForecastWeeks.clear();
			}
			ForecastWeekly aqiWeeks0 = new ForecastWeekly();
			aqiWeeks0.setDateString(result.getWeek1());
			aqiWeeks0.setAQIlevel(result.getAqi_level_1());
			aqiWeeks0.setAQInumber(result.getAqi_1());
			String xx = "0";
			if (!"".equals(result.getAqi_1())) {
				xx = result.getAqi_1();
			}
			aqiWeeks0.setAqi(Integer.parseInt(xx));
			ForecastWeeks.add(aqiWeeks0);
			MyLog.i(aqiWeeks0.toString());

			ForecastWeekly aqiWeeks1 = new ForecastWeekly();
			aqiWeeks1.setDateString(result.getWeek2());
			aqiWeeks1.setAQIlevel(result.getAqi_level_2());
			aqiWeeks1.setAQInumber(result.getAqi_2());
			String xx2 = "0";
			if (!"".equals(result.getAqi_2())) {
				xx2 = result.getAqi_2();
			}
			aqiWeeks1.setAqi(Integer.parseInt(xx2));
			ForecastWeeks.add(aqiWeeks1);
			MyLog.i(aqiWeeks1.toString());

			ForecastWeekly aqiWeeks2 = new ForecastWeekly();
			aqiWeeks2.setDateString(result.getWeek3());
			aqiWeeks2.setAQIlevel(result.getAqi_level_3());
			aqiWeeks2.setAQInumber(result.getAqi_3());
			String xx3 = "0";
			if (!"".equals(result.getAqi_3())) {
				xx3 = result.getAqi_3();
			}
			aqiWeeks2.setAqi(Integer.parseInt(xx3));
			ForecastWeeks.add(aqiWeeks2);
			MyLog.i(aqiWeeks2.toString());

			ForecastWeekly aqiWeeks3 = new ForecastWeekly();
			aqiWeeks3.setDateString(result.getWeek4());
			aqiWeeks3.setAQIlevel(result.getAqi_level_4());
			aqiWeeks3.setAQInumber(result.getAqi_4());
			String xx4 = "0";
			if (!"".equals(result.getAqi_4())) {
				xx4 = result.getAqi_4();
			}
			aqiWeeks3.setAqi(Integer.parseInt(xx4));
			ForecastWeeks.add(aqiWeeks3);
			MyLog.i(aqiWeeks3.toString());

			ForecastWeekly aqiWeeks4 = new ForecastWeekly();
			aqiWeeks4.setDateString(result.getWeek5());
			aqiWeeks4.setAQIlevel(result.getAqi_level_5());
			aqiWeeks4.setAQInumber(result.getAqi_5());
			String xx5 = "0";
			if (!"".equals(result.getAqi_5())) {
				xx5 = result.getAqi_5();
			}
			aqiWeeks4.setAqi(Integer.parseInt(xx5));
			ForecastWeeks.add(aqiWeeks4);
			MyLog.i(aqiWeeks4.toString());

			ForecastWeekly aqiWeeks5 = new ForecastWeekly();
			aqiWeeks5.setDateString(result.getWeek6());
			aqiWeeks5.setAQIlevel(result.getAqi_level_6());
			aqiWeeks5.setAQInumber(result.getAqi_6());
			String xx6 = "0";
			if (!"".equals(result.getAqi_6())) {
				xx6 = result.getAqi_6();
			}
			aqiWeeks5.setAqi(Integer.parseInt(xx6));
			ForecastWeeks.add(aqiWeeks5);
			MyLog.i(aqiWeeks5.toString());

			mHandler.sendEmptyMessage(2);
		}

	}

	class GetKongqizhishuTask extends AsyncTask<String, Void, Kongqizhishu> {
		@Override
		protected Kongqizhishu doInBackground(String... params) {
			String url = UrlComponent.getAirBycity_Get(params[0]);
			MyLog.i(">>>>>>>>>ctiyttg" + url);
			BusinessSearch search = new BusinessSearch();
			Kongqizhishu _Result = null;
			try {
				_Result = search.getKongqizhishu(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(Kongqizhishu result) {

			if (result != null) {
				if (null != pm) {
					dailydetail_jun.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					dailydetail_jun.setText(position_name + "检测点:");
					aqi_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
					aqi_number.setText(pm);
					aqi1 = pm + "";
					pollution_level.setText(level);
				} else {
					aqi1 = result.getAqi();
					aqi_number.setText(result.getAqi());
					// changColor(aqi_number, result.getAqi());
					pollution_level.setText(result.getLevel());
				}

				pm25.setText(result.getPm25());
				changColorPm25(pm25, result.getPm25());
				// changColor(pm25, result.getPm25());
				pm10.setText(result.getPm10());
				// changColor(pm10, result.getPm10());
				changColorPM10(pm10, result.getPm10());
				no2.setText(result.getNo2());
				// changColor(no2, result.getNo2());
				changColorNO2(no2, result.getNo2());
				so2.setText(result.getSo2());
				// changColor(so2, result.getSo2());
				changColorSO2(so2, result.getSo2());
				co.setText(result.getCo());
				changColorCO(co, result.getCo());
				o3.setText(result.getO3());
				changColorO3(o3, result.getO3());

				aqi_advice.setText(result.getQingkuang() + result.getJianyi());
				if ("".equals(result.getMingri())) {
					mingri.setVisibility(View.GONE);
					notif_level.setVisibility(View.GONE);
				}
				if (result.getMingri().equals("优")) {

					notif_level.setBackgroundResource(R.drawable.notif_level1);

				} else if (result.getMingri().equals("良")) {
					notif_level.setBackgroundResource(R.drawable.notif_level2);

				} else if (result.getMingri().equals("轻度污染")) {
					notif_level.setBackgroundResource(R.drawable.notif_level3);

				} else if (result.getMingri().equals("中度污染")) {
					notif_level.setBackgroundResource(R.drawable.notif_level4);

				} else if (result.getMingri().equals("重度污染")) {
					notif_level.setBackgroundResource(R.drawable.notif_level5);

				} else if (result.getMingri().equals("严重污染")) {
					notif_level.setBackgroundResource(R.drawable.notif_level6);
				}
			} else {
				mingri.setVisibility(View.GONE);
				notif_level.setVisibility(View.GONE);

			}
			super.onPostExecute(result);
		}

		private void changColor(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 251) {
						param.setTextColor(Color.rgb(68, 9, 49));
					} else if (zhi > 200) {
						param.setTextColor(Color.rgb(123, 9, 95));
					} else if (zhi > 150) {
						param.setTextColor(Color.rgb(227, 65, 45));
					} else if (zhi > 100) {
						param.setTextColor(Color.rgb(239, 134, 0));
					} else if (zhi > 50) {
						param.setTextColor(Color.rgb(249, 199, 1));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorPm25(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 250) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 150) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 115) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 75) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 35) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorPM10(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 420) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 350) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 250) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 150) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 50) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorNO2(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 2340) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 1200) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 700) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 200) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 100) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorSO2(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 1600) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 800) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 650) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 500) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 150) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorCO(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 90) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 60) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 35) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 10) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 5) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

		private void changColorO3(TextView param, String number) {
			try {
				if (!"".equals(number)) {
					Double zhi = Double.parseDouble(number);
					if (zhi > 800) {
						param.setTextColor(Color.rgb(126, 0, 36));
					} else if (zhi > 400) {
						param.setTextColor(Color.rgb(153, 0, 76));
					} else if (zhi > 300) {
						param.setTextColor(Color.rgb(255, 0, 0));
					} else if (zhi > 200) {
						param.setTextColor(Color.rgb(255, 126, 0));
					} else if (zhi > 160) {
						param.setTextColor(Color.rgb(255, 255, 0));
					} else {
						param.setTextColor(Color.rgb(0, 228, 0));
					}
				}
			} catch (Exception e) {
				param.setTextColor(Color.rgb(0, 228, 0));
			}
		}

	}
}
