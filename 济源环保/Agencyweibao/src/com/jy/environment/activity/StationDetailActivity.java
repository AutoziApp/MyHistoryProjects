package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.AqiStationModel;
import com.jy.environment.model.MonitorModel;
import com.jy.environment.model.NO2;
import com.jy.environment.model.PM10Info24H;
import com.jy.environment.model.PM25;
import com.jy.environment.model.SO2;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.PathView_24;
import com.jy.environment.widget.PathView_24h_pm10;
import com.jy.environment.widget.PathView_24h_pm25;

public class StationDetailActivity extends Activity implements
		OnPageChangeListener, android.view.View.OnClickListener {

	private ViewPager stationviewpager;
	private ViewPagerAdapter pagerAdapter;
	private List<View> stationviewLists;
//	private int position = 0;
//	private String stationname = "";
//	private String stationcode = "";
	private GetWeatherTask getWeatherTask;
	private Intent intent;
	Dialog dialog;
	int[] aqi24_Lists = new int[24];
	int[] pm10_24_Lists = new int[24];
	int[] pm25_24_Lists = new int[24];
	int[] so2_24_Lists = new int[24];
	int[] no2_24_Lists = new int[24];
	public static String[] hours = new String[24];
	public static String[] hours25 = new String[24];
	public static String[] hours10 = new String[24];
	public static String[] hoursSO2 = new String[24];
	public static String[] hoursNO2 = new String[24];
	private String aqi = "", level = "", firstPolluction = "",
			stationType = "", factor_uptime = "";
//	private int count;
	private TextView title_station_name;
	private ImageView activity_main_suggest;
//	private List<AqiStationModel> aqiStationModels;
	private String stationName;
	private String stationCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_viewpager);
		intent = getIntent();
//		count = intent.getIntExtra("count", 1);
//		position = intent.getIntExtra("position", 0);
		
		stationName=intent.getStringExtra("stationName");
		stationCode=intent.getStringExtra("stationCode");
//		aqiStationModels = EnvironmentWeatherRankkActivity.aqiStationModels;
//		MyLog.i(">>>>>>>>>>>>>oncreate" + aqiStationModels + ">>>>>>position"
//				+ position);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		stationviewpager = (ViewPager) findViewById(R.id.stationviewpager);
		title_station_name = (TextView) findViewById(R.id.title_station_name);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(this);
		stationviewLists = new ArrayList<View>();
		for (int i = 0; i < 1; i++) {
			View view = LayoutInflater.from(StationDetailActivity.this)
					.inflate(R.layout.monitor, null);
			TextView detail_24polluction = (TextView) view
					.findViewById(R.id.detail_24polluction);
			TextView detail_polluction = (TextView) view
					.findViewById(R.id.detail_polluction);
			detail_polluction.setText("近12小时分钟数据");
//			detail_polluction.setVisibility(View.GONE);
			detail_polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			detail_24polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			stationviewLists.add(view);
		}
		pagerAdapter = new ViewPagerAdapter(stationviewLists);
		stationviewpager.setAdapter(pagerAdapter);
		stationviewpager.setOnPageChangeListener(this);
		stationviewpager.setCurrentItem(0);
//		if (position == 0) {
			title_station_name.setText(stationName);
			getWeatherTask = new GetWeatherTask();
			getWeatherTask.execute("");
//		}

	}

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
//		position = arg0;
		title_station_name.setText(stationName);
		getWeatherTask = new GetWeatherTask();
		getWeatherTask.execute("");
	}

	public class ViewPagerAdapter extends PagerAdapter {

		List<View> viewLists;

		public ViewPagerAdapter(List<View> lists) {
			viewLists = lists;
		}

		@Override
		public int getCount() { // 获得size
			// TODO Auto-generated method stub
			return viewLists.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object object) // 销毁Item
		{
			try {
				((ViewPager) view).removeView(viewLists.get(position));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public Object instantiateItem(View view, int position) // 实例化Item
		{
			((ViewPager) view).addView(viewLists.get(position));
			return viewLists.get(position);
		}

	}

	class GetWeatherTask extends AsyncTask<String, Void, MonitorModel> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected MonitorModel doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			String url = UrlComponent.getMonitor;
//			stationcode = aqiStationModels.get(position).getStationcode();
			MonitorModel _Result = null;
			try {
				_Result = search.getWeatherTask(url, stationCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(MonitorModel result) {
			// TODO Auto-generated method stub
			if (result == null)
				return;
			fillData(result);
		}
	}

	private void fillData(MonitorModel result) {
		// TODO Auto-generated method stub
		View view = stationviewLists.get(0);
		initView(view, result);
	}

	private void initView(View view, MonitorModel result) {
		for (int i = 0; i < 24; i++) {
			hours[i] = "10";
			aqi24_Lists[i] = 10;
			hours10[i] = "10";
			pm10_24_Lists[i] = 10;
			hours25[i] = "10";
			pm25_24_Lists[i] = 10;
			hoursSO2[i] = "10";
			so2_24_Lists[i] = 10;
			hoursNO2[i] = "10";
			no2_24_Lists[i] = 10;
		}
		// TODO Auto-generated method stub
		TextView monitor_update, monitor_stationname, monitor_aqi, monitor_polluction, monitor_first, monitor_type, monitor_pm25, monitor_pm10, monitor_03, monitor_so2, monitor_no2, monitor_co, detail_polluction, detail_24polluction;
		final ColumnChartView monitor_details_pv_24th;
		final ColumnChartView monitor_pathView_24h_pm10;
		final ColumnChartView monitor_pathView_24h_pm25;
		final ColumnChartView monitor_pathView_24h_so2;
		final ColumnChartView monitor_pathView_24h_no2;
		ImageView monitor_back;
		TextView tv_aqi_trend, tv_pm25_trend, tv_pm10_trend,tv_so2_trend,tv_no2_trend;
		final TextView[] textViews = new TextView[5];
		monitor_update = (TextView) view.findViewById(R.id.monitor_update);
		monitor_stationname = (TextView) view
				.findViewById(R.id.monitor_stationname);
		monitor_stationname.setText(stationName);
		monitor_details_pv_24th = (ColumnChartView) view
				.findViewById(R.id.monitor_details_pv_24th);
		monitor_pathView_24h_pm10 = (ColumnChartView) view
				.findViewById(R.id.monitor_details_pv_pm10_24th);
		monitor_pathView_24h_pm25 = (ColumnChartView) view
				.findViewById(R.id.monitor_details_pv_pm25_24th);
		monitor_pathView_24h_so2 = (ColumnChartView) view
				.findViewById(R.id.monitor_details_pv_so2_24th);
		monitor_pathView_24h_no2 = (ColumnChartView) view
				.findViewById(R.id.monitor_details_pv_no2_24th);
		monitor_aqi = (TextView) view.findViewById(R.id.monitor_aqi);
		monitor_polluction = (TextView) view
				.findViewById(R.id.monitor_polluction);
		monitor_first = (TextView) view.findViewById(R.id.monitor_first);
		monitor_type = (TextView) view.findViewById(R.id.monitor_type);
		monitor_pm25 = (TextView) view.findViewById(R.id.monitor_pm25);
		monitor_pm10 = (TextView) view.findViewById(R.id.monitor_pm10);
		monitor_03 = (TextView) view.findViewById(R.id.monitor_03);
		monitor_so2 = (TextView) view.findViewById(R.id.monitor_so2);
		monitor_no2 = (TextView) view.findViewById(R.id.monitor_no2);
		monitor_co = (TextView) view.findViewById(R.id.monitor_co);
		detail_24polluction = (TextView) view
				.findViewById(R.id.detail_24polluction);
		detail_polluction = (TextView) view
				.findViewById(R.id.detail_polluction);
		detail_polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		detail_24polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		monitor_back = (ImageView) view.findViewById(R.id.monitor_back);
		monitor_back.setOnClickListener(this);
		detail_polluction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StationDetailActivity.this,
						EnvironmentMonitorPmActivity.class);
				intent.putExtra("from", "MonitorActivity");
				intent.putExtra("aqi", aqi);
				intent.putExtra("level", level);
				intent.putExtra("firstPolluction", firstPolluction);
				intent.putExtra("stationType", stationType);
				intent.putExtra("stationcode", stationCode);
				intent.putExtra("stationname", stationName);
				intent.putExtra("type", "2");//2为分钟,1为30天,0为24小时
				intent.putExtra("factor_uptime", factor_uptime);
				startActivity(intent);
			}
		});
		detail_24polluction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StationDetailActivity.this,
						EnvironmentMonitorPmActivity.class);
				intent.putExtra("from", "MonitorActivity");
				intent.putExtra("aqi", aqi);
				intent.putExtra("level", level);
				intent.putExtra("firstPolluction", firstPolluction);
				intent.putExtra("stationType", stationType);
				intent.putExtra("stationcode", stationCode);
				intent.putExtra("type", "0");
				intent.putExtra("stationname", stationName);
				intent.putExtra("factor_uptime", factor_uptime);
				startActivity(intent);
			}
		});
		tv_aqi_trend = (TextView) view.findViewById(R.id.tv_aqi_trend);
		tv_pm10_trend = (TextView) view.findViewById(R.id.tv_pm10_trend);
		tv_pm25_trend = (TextView) view.findViewById(R.id.tv_pm25_trend);
		tv_so2_trend = (TextView) view.findViewById(R.id.tv_so2_trend);
		tv_no2_trend = (TextView) view.findViewById(R.id.tv_no2_trend);
		textViews[0] = tv_aqi_trend;
		textViews[1] = tv_pm25_trend;
		textViews[2] = tv_pm10_trend;
		textViews[3] = tv_so2_trend;
		textViews[4] = tv_no2_trend;
		tv_aqi_trend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				monitor_details_pv_24th.setVisibility(View.VISIBLE);
				monitor_pathView_24h_pm10.setVisibility(View.GONE);
				monitor_pathView_24h_pm25.setVisibility(View.GONE);
				monitor_pathView_24h_so2.setVisibility(View.GONE);
				monitor_pathView_24h_no2.setVisibility(View.GONE);
				changeBgColor(0, textViews);
			}
		});
		tv_pm10_trend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				monitor_details_pv_24th.setVisibility(View.GONE);
				monitor_pathView_24h_pm10.setVisibility(View.VISIBLE);
				monitor_pathView_24h_pm25.setVisibility(View.GONE);
				monitor_pathView_24h_so2.setVisibility(View.GONE);
				monitor_pathView_24h_no2.setVisibility(View.GONE);
				changeBgColor(2, textViews);
			}
		});
		tv_pm25_trend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				monitor_details_pv_24th.setVisibility(View.GONE);
				monitor_pathView_24h_pm10.setVisibility(View.GONE);
				monitor_pathView_24h_pm25.setVisibility(View.VISIBLE);
				monitor_pathView_24h_so2.setVisibility(View.GONE);
				monitor_pathView_24h_no2.setVisibility(View.GONE);
				changeBgColor(1, textViews);
			}
		});
		tv_so2_trend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				monitor_details_pv_24th.setVisibility(View.GONE);
				monitor_pathView_24h_pm10.setVisibility(View.GONE);
				monitor_pathView_24h_pm25.setVisibility(View.GONE);
				monitor_pathView_24h_so2.setVisibility(View.VISIBLE);
				monitor_pathView_24h_no2.setVisibility(View.GONE);
				changeBgColor(3, textViews);
			}
		});
		tv_no2_trend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				monitor_details_pv_24th.setVisibility(View.GONE);
				monitor_pathView_24h_pm10.setVisibility(View.GONE);
				monitor_pathView_24h_pm25.setVisibility(View.GONE);
				monitor_pathView_24h_so2.setVisibility(View.GONE);
				monitor_pathView_24h_no2.setVisibility(View.VISIBLE);
				changeBgColor(4, textViews);
			}
		});
//		for (int i = 0; i < 24; i++) {
//			hours[i] = "10";
//			aqi24_Lists[i] = 10;
//		}
//		monitor_details_pv_24th.setXCount(500, 5);
//		monitor_details_pv_24th.setType(PathView_24.MONITOR_HOUR);
//		monitor_details_pv_24th.setDate(aqi24_Lists);
		
		
//		for (int i = 0; i < 24; i++) {
//			hours10[i] = "10";
//			pm10_24_Lists[i] = 10;
//		}
//		monitor_pathView_24h_pm10.setXCount(500, 5);
//		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
//		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);
//		for (int i = 0; i < 24; i++) {
//			hours25[i] = "10";
//			pm25_24_Lists[i] = 10;
//		}
//		monitor_pathView_24h_pm25.setXCount(500, 5);
//		monitor_pathView_24h_pm25.setType(PathView_24h_pm25.MONITOR_HOUR);
//		monitor_pathView_24h_pm25.setDate(pm25_24_Lists);
		changeBgColor(0, textViews);
		factor_uptime = result.getMONIDATE();
		monitor_update.setText("更新时间:" + result.getMONIDATE());
		monitor_aqi.setText(result.getAQI() + "");
//		monitor_polluction.setText(result.getAIRLEVEL());
		monitor_polluction.setText(CommonUtil.getDengJiByAQI(result
				.getAQI() + ""));
		monitor_polluction.setBackgroundResource(getDuValueRes(result
				.getAQI()));
		int pm25 = result.getPM25();
		int pm10 = result.getPM10();
		int o3 = result.getO3();
		int so2 = result.getSO2();
		int no2 = result.getNO2();
		double co = result.getCO();
		monitor_polluction
				.setBackgroundResource(getDuValueRes(result.getAQI()));
		monitor_first.setText("首要污染物:" + result.getPRIMARYPOLLUTANT());
		monitor_type.setText("站点类型:" + result.getSTATIONTYPE());
		aqi = result.getAQI() + "";
		level = result.getAIRLEVEL();
		firstPolluction = result.getPRIMARYPOLLUTANT();
		stationType = result.getSTATIONTYPE();
		if (pm25 != 0) {
			monitor_pm25.setText(result.getPM25() + "");
			monitor_pm25.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getPM25(), "1", 0.5d));
		}
		if (pm10 != 0) {
			monitor_pm10.setText(result.getPM10() + "");
			monitor_pm10.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getPM10(), "0", 0.5d));
		}
		if (o3 != 0) {
			monitor_03.setText(result.getO3() + "");
			monitor_03.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getO3(), "2", 0.5d));
		}
		if (so2 != 0) {
			monitor_so2.setText(result.getSO2() + "");
			monitor_so2.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getSO2(), "3", 0.5d));
		}
		if (no2 != 0) {
			monitor_no2.setText(result.getNO2() + "");
			monitor_no2.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getNO2(), "4", 0.5d));
		}
		if (co != 0.0) {
			monitor_co.setText(result.getCO() + "");
			monitor_co.setBackgroundResource(CommonUtil.getDengJiByType(
					result.getAQI(), "5", result.getCO()));
		}
		List<WeatherInfo24> weatherInfo24s = result.getWeatherInfo24s();
//		for (int i = 0; i < weatherInfo24s.size(); i++) {
//			WeatherInfo24 info = weatherInfo24s.get(i);
//			hours[i] = info.getUpdate_time().substring(11, 13) + "";
//			aqi24_Lists[i] = info.getAqi();
//		}
//		monitor_details_pv_24th.setXCount(500, 5);
//		monitor_details_pv_24th.setType(PathView_24.MONITOR_HOUR);
//		monitor_details_pv_24th.setDate(aqi24_Lists);
		
		for (int i = 0; i < 24; i++) {
			hours[i] = "10";
			aqi24_Lists[i] = 10;

		}
		for (int i = 0; i < weatherInfo24s.size(); i++) {
			
			String time = weatherInfo24s.get(i).getUpdate_time();
			MyLog.i("time :" + time);
			MyLog.i("AQI" +weatherInfo24s.get(i).toString());
			if(!"".equals(time)){
				time = time.substring(11, 13);
				hours[i] = time;
				aqi24_Lists[i] = weatherInfo24s.get(i).getAqi();
			}else{
				hours[i] = "10";
				aqi24_Lists[i] = 10;
			}
//			String time = weatherInfo24s.get(weatherInfo24s.size() - 1).getUpdate_time();
//			if (!"".equals(time)) {
//				time = time.substring(11, 13);
//				int time_int = Integer.parseInt(time);
//				time_int = time_int - i;
//				if (time_int < 0) {
//					time_int = time_int + 24;
//				}
//				if (time.length() == 1) {
//					time = "0" + time;
//				}
//				time = time_int + "";
//				hours[23 - i] = time;
//				aqi24_Lists[i] = weatherInfo24s.get(i).getAqi();
//			} else {
//				hours[i] = "10";
//				aqi24_Lists[i] = 10;
//			}

		}
		//
		generateColumnData(monitor_details_pv_24th, hours,
				new int[] { aqi24_Lists[0], aqi24_Lists[1], aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4],
						aqi24_Lists[5], aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
						aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12], aqi24_Lists[13], aqi24_Lists[14],
						aqi24_Lists[15], aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18], aqi24_Lists[19],
						aqi24_Lists[20], aqi24_Lists[21], aqi24_Lists[22], aqi24_Lists[23] },
				1);

		List<PM10Info24H> pm10Info24Hs = result.getPm10Info24Hs();
		
//		for (int i = 0; i < pm10Info24Hs.size(); i++) {
//			PM10Info24H info = pm10Info24Hs.get(i);
//			hours10[i] = info.getTime().substring(11, 13) + "";
//			pm10_24_Lists[i] = info.getPm10();
//		}
//		monitor_pathView_24h_pm10.setXCount(500, 5);
//		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
//		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);
		
		for (int i = 0; i < 24; i++) {
			hours10[i] = "10";
			pm10_24_Lists[i] = 10;
		}
		for (int i = 0; i < pm10Info24Hs.size(); i++) {
			
			String time = pm10Info24Hs.get(i).getTime();
			MyLog.i("time :" + time);
			MyLog.i("pm10Info24Hs" +pm10Info24Hs.get(i).toString());
			if(!"".equals(time)){
				time = time.substring(11, 13);
				hours10[i] = time;
				pm10_24_Lists[i] = pm10Info24Hs.get(i).getPm10();
			}else{
				hours10[i] = "10";
				pm10_24_Lists[i] = 10;
			}
			
//			String time = pm10Info24Hs.get(pm10Info24Hs.size() - 1).getTime();
//			if (!"".equals(time)) {
//				time = time.substring(11, 13);
//				int time_int = Integer.parseInt(time);
//				time_int = time_int - i;
//				if (time_int < 0) {
//					time_int = time_int + 24;
//				}
//				if (time.length() == 1) {
//					time = "0" + time;
//				}
//				time = time_int + "";
//				hours10[23 - i] = time;
//				pm10_24_Lists[i] = pm10Info24Hs.get(i).getPm10();
//			} else {
//				hours10[i] = "10";
//				pm10_24_Lists[i] = 10;
//			}
		}

		generateColumnData(monitor_pathView_24h_pm10, hours10,
				new int[] { pm10_24_Lists[0], pm10_24_Lists[1], pm10_24_Lists[2], pm10_24_Lists[3],
						pm10_24_Lists[4], pm10_24_Lists[5], pm10_24_Lists[6], pm10_24_Lists[7],
						pm10_24_Lists[8], pm10_24_Lists[9], pm10_24_Lists[10], pm10_24_Lists[11],
						pm10_24_Lists[12], pm10_24_Lists[13], pm10_24_Lists[14], pm10_24_Lists[15],
						pm10_24_Lists[16], pm10_24_Lists[17], pm10_24_Lists[18], pm10_24_Lists[19],
						pm10_24_Lists[20], pm10_24_Lists[21], pm10_24_Lists[22], pm10_24_Lists[23] },
				2);
		

		List<PM25> pm25Info24Hs = result.getPm25Info24Hs();
//		for (int i = 0; i < pm25Info24Hs.size(); i++) {
//			PM25 info = pm25Info24Hs.get(i);
//			hours25[i] = info.getTime().substring(11, 13) + "";
//			pm25_24_Lists[i] = info.getPm25();
//		}
//		monitor_pathView_24h_pm10.setXCount(500, 5);
//		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
//		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);
		
		for (int i = 0; i < 24; i++) {
			hours25[i] = "10";
			pm25_24_Lists[i] = 10;
		}
		for (int i = 0; i < pm25Info24Hs.size(); i++) {
			String time = pm10Info24Hs.get(i).getTime();
			MyLog.i("time :" + time);
			MyLog.i("pm10Info24Hs" +pm25Info24Hs.get(i).toString());
			if(!"".equals(time)){
				time = time.substring(11, 13);
				hours25[i] = time;
				pm25_24_Lists[i] = pm25Info24Hs.get(i).getPm25();
			}else{
				hours25[i] = "10";
				pm25_24_Lists[i] = 10;
			}
//			String time = pm25Info24Hs.get(pm25Info24Hs.size() - 1).getTime();
//			if (!"".equals(time)) {
//				time = time.substring(11, 13);
//				int time_int = Integer.parseInt(time);
//				time_int = time_int - i;
//				if (time_int < 0) {
//					time_int = time_int + 24;
//				}
//				if (time.length() == 1) {
//					time = "0" + time;
//				}
//				time = time_int + "";
//				hours25[23 - i] = time;
//				pm25_24_Lists[i] = pm25Info24Hs.get(i).getPm25();
//			} else {
//				hours25[i] = "10";
//				pm25_24_Lists[i] = 10;
//			}

		}
		generateColumnData(monitor_pathView_24h_pm25, hours25,
				new int[] { pm25_24_Lists[0], pm25_24_Lists[1], pm25_24_Lists[2], pm25_24_Lists[3],
						pm25_24_Lists[4], pm25_24_Lists[5], pm25_24_Lists[6], pm25_24_Lists[7],
						pm25_24_Lists[8], pm25_24_Lists[9], pm25_24_Lists[10], pm25_24_Lists[11],
						pm25_24_Lists[12], pm25_24_Lists[13], pm25_24_Lists[14], pm25_24_Lists[15],
						pm25_24_Lists[16], pm25_24_Lists[17], pm25_24_Lists[18], pm25_24_Lists[19],
						pm25_24_Lists[20], pm25_24_Lists[21], pm25_24_Lists[22], pm25_24_Lists[23] },
				3);
		
		List<SO2> so2Info24Hs = result.getSO2Info24Hs();
		
		for (int i = 0; i < 24; i++) {
			hoursSO2[i] = "10";
			so2_24_Lists[i] = 10;
		}
		for (int i = 0; i < so2Info24Hs.size(); i++) {
			String time = so2Info24Hs.get(i).getTime();
			if(!"".equals(time)){
				time = time.substring(11, 13);
				hoursSO2[i] = time;
				so2_24_Lists[i] = so2Info24Hs.get(i).getSo2();
			}else{
				hoursSO2[i] = "10";
				so2_24_Lists[i] = 10;
			}
		}
		
		generateColumnData(monitor_pathView_24h_so2, hoursSO2,
				new int[] { so2_24_Lists[0], so2_24_Lists[1], so2_24_Lists[2], so2_24_Lists[3],
				so2_24_Lists[4], so2_24_Lists[5], so2_24_Lists[6], so2_24_Lists[7],
				so2_24_Lists[8], so2_24_Lists[9], so2_24_Lists[10], so2_24_Lists[11],
				so2_24_Lists[12], so2_24_Lists[13], so2_24_Lists[14], so2_24_Lists[15],
				so2_24_Lists[16], so2_24_Lists[17], so2_24_Lists[18], so2_24_Lists[19],
				so2_24_Lists[20], so2_24_Lists[21], so2_24_Lists[22], so2_24_Lists[23] },
				3);
		
		List<NO2> no2Info24Hs = result.getNO2Info24Hs();
		
		for (int i = 0; i < 24; i++) {
			hoursNO2[i] = "10";
			no2_24_Lists[i] = 10;
		}
		for (int i = 0; i < no2Info24Hs.size(); i++) {
			String time = no2Info24Hs.get(i).getTime();
			if(!"".equals(time)){
				time = time.substring(11, 13);
				hoursNO2[i] = time;
				no2_24_Lists[i] = no2Info24Hs.get(i).getNo2();
			}else{
				hoursNO2[i] = "10";
				no2_24_Lists[i] = 10;
			}
		}
		
		generateColumnData(monitor_pathView_24h_no2, hoursNO2,
				new int[] { no2_24_Lists[0], no2_24_Lists[1], no2_24_Lists[2], no2_24_Lists[3],
				no2_24_Lists[4], no2_24_Lists[5], no2_24_Lists[6], no2_24_Lists[7],
				no2_24_Lists[8], no2_24_Lists[9], no2_24_Lists[10], no2_24_Lists[11],
				no2_24_Lists[12], no2_24_Lists[13], no2_24_Lists[14], no2_24_Lists[15],
				no2_24_Lists[16], no2_24_Lists[17], no2_24_Lists[18], no2_24_Lists[19],
				no2_24_Lists[20], no2_24_Lists[21], no2_24_Lists[22], no2_24_Lists[23] },
				3);
	}

	void changeBgColor(int position, TextView[] teViews) {
		for (int i = 0; i < 5; i++) {
			if (i == position) {
				teViews[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kuang10));
			} else {
				teViews[i].setBackgroundDrawable(null);
			}

		}
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
		} else if (aqi < 300) {
			bg = R.drawable.aqi_level_5;
		} else {
			bg = R.drawable.aqi_level_6;
		}
		return bg;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_main_suggest:
			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * 
	 * @param chart
	 * @param xValue
	 * @param YValue
	 * @param type
	 *            1 AQI /2PM10 /3 PM25
	 */
	private void generateColumnData(ColumnChartView chart, String[] xValue, int[] YValue, int type) {
		ColumnChartData columnDataVaule;
		int numSubcolumns = 1;
		int numColumns = YValue.length;
		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		int maxYValue = 130;
		for (int i = 0; i < numColumns; ++i) {
			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				float aqi = (float) YValue[i];
				if(aqi > maxYValue){
					maxYValue = (int) aqi;
				}
				SubcolumnValue subcolumnValue = null;
				switch (type) {
				case 1:
					subcolumnValue = new SubcolumnValue(aqi, getAQIColorByIntegerValue((int) aqi));
					break;
				case 2:
					subcolumnValue = new SubcolumnValue(aqi, getPM10ColorByIndex((int) aqi));
					break;
				case 3:
					subcolumnValue = new SubcolumnValue(aqi, getPM25ColorByIndex((int) aqi));
					break;
				default:
					subcolumnValue = new SubcolumnValue(aqi, getAQIColorByIntegerValue((int) aqi));
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
		
		if(maxYValue <= 130 ){
			maxYValue = 130;
		}else if(maxYValue <= 230 ){
			maxYValue = 230;
		}else if(maxYValue <= 340 ){
			maxYValue = 340;
		}else if(maxYValue <= 440 ){
			maxYValue = 440;
		}else if(maxYValue <= 540 ){
			maxYValue = 540;
		}
//		Viewport v = new Viewport(-1, 340, YValue.length, 0);
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
}
