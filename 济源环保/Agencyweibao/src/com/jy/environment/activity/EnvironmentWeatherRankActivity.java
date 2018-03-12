package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.CityDetails;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.EnvironmentForecastWeekModel;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.ThreeDayForestModel;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7_tian;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.PathView;
import com.jy.environment.widget.PathView_24;
import com.umeng.analytics.MobclickAgent;

/**
 * 环境天气趋势界面
 * 
 * @author baiyuchuan
 * 
 */
@SuppressLint("NewApi")
public class EnvironmentWeatherRankActivity extends ActivityBase implements
		OnClickListener {

	private TextView city_name;
	private TextView aqi_du_value;
	private TextView aqi_value;
	private TextView baifenbi_value;
	private TextView paiming_value;
	private LinearLayout station_layout;
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
			environment_rank_detailstv16, environment_path, environment_path24;
	private ImageView environment_rank_details_back,
			environment_rank_details_share;
	private TableRow textv_nodata, tableRow1, tableRow2;
	int[] aqi24_Lists = new int[24];
	int[] aqi_Lists = new int[7];
	private CityDetails details;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private Uri uri;
	public static String[] days_data = new String[7];
	public static String[] hours = new String[24];
	private List<WeatherInfo7_tian> lists_7tian = new ArrayList<WeatherInfo7_tian>();
	private List<WeatherInfo24> weatherLists = new ArrayList<WeatherInfo24>();
	private PathView_24 pv_24th;
	private PathView pw;
	private Intent intent;
	private String lat, lng, city, pm25, level, position_name, content,
			dingweiString;
	private RelativeLayout environment_rank_pm, relativeLayout4,
			city_paiming_value_layout;
	private LinearLayout layout_dingwei, station_title_layout;
	private GetAqiDetailItemTask itemTask;
	private TableLayout tav;
	Dialog dialog;
	private int cityLength, cityRank;
	private WeiBaoApplication mApplication;
	private CityDB mCityDB;
	private GetThreeDayForestTask getThreeDayForestTask;
	private String type = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_rank_details_old);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在加载");
		mApplication = WeiBaoApplication.getInstance();
		mCityDB = mApplication.getCityDB();
		// LocationService.sendGetLocationBroadcast(EnvironmentWeatherRankActivity.this);
		intent = getIntent();
		lat = intent.getStringExtra("lat");
		lng = intent.getStringExtra("lng");
		city = intent.getStringExtra("city");
		MyLog.i("city===" + city);
		pm25 = intent.getStringExtra("pm25");
		level = intent.getStringExtra("level");
		// dingweiString = intent.getStringExtra("dingweistr");
		MyLog.i("dingweiStr:" + dingweiString);
		SharedPreferencesUtil preferencesUtil = SharedPreferencesUtil
				.getInstance(this);
		initView();
		if (dingweiString != null && preferencesUtil.getisDingwei()) {
			// layout_dingwei.setVisibility(View.VISIBLE);
			text_dingwei.setText(dingweiString);
		}
		registerMessageReceiver();
		rankActivity = new GetWeatherRankActivity();
		MyLog.i(">>>>>>>>>>details"
				+ UrlComponent.currentWeatherDetailsGet(city, lat, lng));
		if (city.contains("自治州")) {
			city = mCityDB.getSuoSuo(city);
		}
		rankActivity.execute(
				UrlComponent.currentWeatherDetailsGet(city, lat, lng), "600");
		itemTask = new GetAqiDetailItemTask();
		itemTask.execute();
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
		unregisterReceiver(locationReceiver);
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
		// TODO Auto-generated method stub
		environment_rank_details_dw = (TextView) findViewById(R.id.environment_rank_details_dw);
		if (lat.equals("0")) {
			environment_rank_details_dw.setText("均值");
			environment_rank_details_dw.setGravity(Gravity.CENTER);
		}
		text_dingwei = (TextView) findViewById(R.id.text_dingwei);
		layout_dingwei = (LinearLayout) findViewById(R.id.layout_dingwei);
		station_title_layout = (LinearLayout) findViewById(R.id.station_title_layout);
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
		station_layout = (LinearLayout) findViewById(R.id.station_layout);
		textv_nodata = (TableRow) findViewById(R.id.textv_nodata);
		tableRow1 = (TableRow) findViewById(R.id.tableRow1);
		tableRow2 = (TableRow) findViewById(R.id.tableRow2);
		environment_rank_detailstv16 = (TextView) findViewById(R.id.environment_rank_detailstv16);
		environment_rank_detailstv16.setText(city);
		environment_rank_details_back = (ImageView) findViewById(R.id.environment_rank_details_back);
		environment_rank_details_share = (ImageView) findViewById(R.id.environment_rank_details_share);
		relativeLayout4 = (RelativeLayout) findViewById(R.id.relativeLayout4);
		tav = (TableLayout) findViewById(R.id.tav);
		environment_path = (TextView) findViewById(R.id.environment_path);
		environment_path24 = (TextView) findViewById(R.id.environment_path24);
		environment_path.setOnClickListener(this);
		environment_path24.setOnClickListener(this);
		environment_rank_details_back.setOnClickListener(this);
		environment_rank_details_share.setOnClickListener(this);
		pv_24th = (PathView_24) findViewById(R.id.rank_details_pv_24th);
		for (int i = 0; i < 24; i++) {
			hours[i] = "10时";
			aqi24_Lists[i] = 10;
		}
		pv_24th.setXCount(500, 5);
		pv_24th.setType(PathView_24.DAY_OLDHOUR);
		pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
				aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
				aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
				aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
				aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
				aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
				aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
				aqi24_Lists[22], aqi24_Lists[23] });
		pw = (PathView) findViewById(R.id.rank_details_pv_pv);
		for (int i = 0; i < 7; i++) {
			days_data[i] = "";
			aqi_Lists[i] = 10;
		}
		pw.setXCount(500, 5);
		pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1], aqi_Lists[2],
				aqi_Lists[3], aqi_Lists[4], aqi_Lists[5], aqi_Lists[6] });
		pw.setType(PathView.DAY_OLDWEEK);

		environment_rank_pm = (RelativeLayout) findViewById(R.id.environment_rank_pm);
		environment_rank_pm.setOnClickListener(this);
		paiming_value.setOnClickListener(this);
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
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
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
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected CityDetails doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			CityDetails _Result = new CityDetails();
			try {
				_Result = search.getWeatherRankActivity(params[0], params[1],
						type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(CityDetails result) {
			// TODO Auto-generated method stub
			try {
				getThreeDayForestTask = new GetThreeDayForestTask();
				getThreeDayForestTask.execute();
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				MyLog.i(">>>>>>>cityDetails" + result);
				dialog.dismiss();
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
				.from(EnvironmentWeatherRankActivity.this);
		aqi_du_value.setText("" + getDuValue(Integer.parseInt(pm25)));
		aqi_value.setText("" + pm25);
		city_name.setText(city);
		if (null != result.getAqiStationModels()
				&& result.getAqiStationModels().size() > 0) {
			// station_layout
			station_title_layout.setVisibility(View.VISIBLE);
			station_layout.setVisibility(View.VISIBLE);
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
		environment_rank_details_ts.setText("健康提示:" + zhishu.getQingkuang());
		// 空气质量预报
		EnvironmentForecastWeekModel weekmodel = result.getWeekModel();

		if (null != weekmodel) {
			environment_rank_details_tv6.setText(weekmodel.getWeek1());
			environment_rank_details_tv7.setText(weekmodel.getWeek2());
			environment_rank_details_tv8.setText(weekmodel.getWeek3());
			environment_rank_details_tv9.setText(weekmodel.getWeek4());
			// environment_rank_details_tv10.setText(weekmodel.getWeek5());
			// environment_rank_details_tv10.setText(weekmodel.getWeek5());

			environment_rank_details_tv11.setText(pm25);
			environment_rank_details_tv12.setText(weekmodel.getAqi_level_2());
			environment_rank_details_tv13.setText(weekmodel.getAqi_level_3());
			environment_rank_details_tv14.setText(weekmodel.getAqi_level_4());
		} else {
			relativeLayout4.setVisibility(View.GONE);
			tav.setVisibility(View.GONE);
		}
		weatherLists = result.getInfo24s();
		for (int i = 0; i < 24; i++) {
			hours[i] = "10时";
			aqi24_Lists[i] = 10;
		}
		for (int i = 0; i < weatherLists.size(); i++) {
			String time = weatherLists.get(weatherLists.size() - 1)
					.getUpdate_time();
			if (!"".equals(time)) {
				time = time.substring(11, 13);
				int time_int = Integer.parseInt(time);
				time_int = time_int - i;
				if (time_int < 0) {
					time_int = time_int + 24;
				}
				time = time_int + "时";
				hours[23 - i] = time;
				aqi24_Lists[i] = weatherLists.get(i).getAqi();
			} else {
				hours[i] = "10时";
				aqi24_Lists[i] = 10;
			}

		}
		pv_24th.setXCount(500, 5);
		pv_24th.setType(PathView_24.DAY_OLDHOUR);
		MyLog.i(">>>>>>>>>>>>>aqi24_list" + aqi24_Lists[0]);
		pv_24th.setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
				aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
				aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
				aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
				aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
				aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
				aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
				aqi24_Lists[22], aqi24_Lists[23] });
		lists_7tian = result.getWeInfo7();
		if (lists_7tian.size() > 0) {
			for (int i = 0; i < lists_7tian.size(); i++) {
				if (i == 7) {
					break;
				}
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
			pw.setXCount(200, 5);
			pw.setDate(new int[] { aqi_Lists[0], aqi_Lists[1], aqi_Lists[2],
					aqi_Lists[3], aqi_Lists[4], aqi_Lists[5], aqi_Lists[6] });
			pw.setType(PathView.DAY_OLDWEEK);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.environment_rank_details_back:
			finish();
			break;
		case R.id.environment_rank_details_share:
			share();
			break;
		case R.id.environment_rank_pm:
		case R.id.paiming_value:
			MobclickAgent.onEvent(EnvironmentWeatherRankActivity.this,
					"HJAirRanking");
			Intent intent = new Intent(getApplicationContext(),
					EnvironmentWeatherRankPaiMingActivity.class);
			intent.putExtra("city", city);
			startActivity(intent);
			break;
		case R.id.environment_path:
			if (null != details) {
				pv_24th.setVisibility(View.GONE);
				pw.setVisibility(View.VISIBLE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path.setBackground(getResources().getDrawable(
						R.drawable.kuang10));

				environment_path24.setBackground(null);
			} else {
				// environment_path.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.environment_path24));
				environment_path.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.kuang10));
				environment_path24.setBackgroundDrawable(null);

			}

			;

			break;
		case R.id.environment_path24:
			MobclickAgent.onEvent(EnvironmentWeatherRankActivity.this,
					"HJAirLastWeek");
			if (null != details) {
				pv_24th.setVisibility(View.VISIBLE);
				pw.setVisibility(View.GONE);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// environment_path24.setBackground(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.kuang10));
				environment_path.setBackground(null);
			} else {
				// environment_path24.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.environment_path24));
				environment_path24.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.kuang10));
				environment_path.setBackgroundDrawable(null);
			}

			break;

		default:
			break;
		}
	}

	// 分享成功还是失败
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			MobclickAgent.onEvent(EnvironmentWeatherRankActivity.this,
					"HJAirShare");
			Log.d("Test", "share success");
			Toast.makeText(EnvironmentWeatherRankActivity.this, "分享成功", 2000)
					.show();
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
			Toast.makeText(EnvironmentWeatherRankActivity.this, "分享失败", 2000)
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
			Toast.makeText(EnvironmentWeatherRankActivity.this, "请检查您的网络", 0)
					.show();
			return;
		}
		Bitmap bitmap = CommonUtil
				.GetCurrentScreen(EnvironmentWeatherRankActivity.this);
		try {
			uri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), bitmap, null, null));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentWeatherRankActivity.this, "截图失败", 0)
					.show();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(EnvironmentWeatherRankActivity.this, "截图失败", 0)
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
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public static String getDuValue(int aqi) {
		String xx = "优";
		if (aqi <= 50) {
			xx = "优";
		} else if (aqi <= 100) {
			xx = "良";
		} else if (aqi <= 150) {
			xx = "轻度";
		} else if (aqi <= 200) {
			xx = "中度";
		} else if (aqi < 301) {
			xx = "重度";
		} else {
			xx = "严重";
		}
		return xx;
	}
	
	public static String getDuValueYY(int level) {
		String xx = "优";
		switch (level) {
		case 1:
			xx = "优";
			break;
		case 2:
			xx = "良";
			break;
		case 3:
			xx = "轻度";
			break;
		case 4:
			xx = "中度";
			break;
		case 5:
			xx = "重度";
			break;
		case 6:
			xx = "严重";
			break;
		}
//		if (aqi <= 50) {
//			xx = "优";
//		} else if (aqi <= 100) {
//			xx = "良";
//		} else if (aqi <= 150) {
//			xx = "轻度";
//		} else if (aqi <= 200) {
//			xx = "中度";
//		} else if (aqi < 301) {
//			xx = "重度";
//		} else {
//			xx = "严重";
//		}
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
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected List<ThreeDayForestModel> doInBackground(String... params) {
			// TODO Auto-generated method stub

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

}