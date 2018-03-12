package com.jy.environment.activity;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.MonitorModel;
import com.jy.environment.model.PM10Info24H;
import com.jy.environment.model.PM25;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.PathView_24;
import com.jy.environment.widget.PathView_24h_pm10;
import com.jy.environment.widget.PathView_24h_pm25;

public class MonitorActivity extends Activity implements OnClickListener {
	private String stationname = "";
	private String stationcode = "";
	private TextView monitor_update, monitor_stationname, monitor_aqi,
			monitor_polluction, monitor_first, monitor_type, monitor_pm25,
			monitor_pm10, monitor_03, monitor_so2, monitor_no2, monitor_co,
			detail_polluction, detail_24polluction;
	private ImageView monitor_back;
	private PathView_24 monitor_details_pv_24th;
	private PathView_24h_pm10 monitor_pathView_24h_pm10;
	private PathView_24h_pm25 monitor_pathView_24h_pm25;
	private GetWeatherTask getWeatherTask;
	private Intent intent;
	Dialog dialog;
	int[] aqi24_Lists = new int[24];
	int[] pm10_24_Lists = new int[24];
	int[] pm25_24_Lists = new int[24];
	public static String[] hours = new String[24];
	public static String[] hours25 = new String[24];
	public static String[] hours10 = new String[24];
	private String aqi = "", level = "", firstPolluction = "",
			stationType = "", factor_uptime = "";
	private TextView tv_aqi_trend, tv_pm25_trend, tv_pm10_trend;
	private TextView[] textViews = new TextView[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor);
		intent = getIntent();
		stationcode = intent.getStringExtra("stationcode");
		stationname = intent.getStringExtra("stationname");
		MyLog.i(">>>>>>>>>>>>>>stationcode" + stationcode);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		initView();
		getWeatherTask = new GetWeatherTask();
		getWeatherTask.execute("");
	}

	private void initView() {
		// TODO Auto-generated method stub
		monitor_update = (TextView) findViewById(R.id.monitor_update);
		monitor_stationname = (TextView) findViewById(R.id.monitor_stationname);
		monitor_stationname.setText(stationname);
		monitor_details_pv_24th = (PathView_24) findViewById(R.id.monitor_details_pv_24th);
		monitor_pathView_24h_pm10 = (PathView_24h_pm10) findViewById(R.id.monitor_details_pv_pm10_24th);
		monitor_pathView_24h_pm25 = (PathView_24h_pm25) findViewById(R.id.monitor_details_pv_pm25_24th);
		monitor_aqi = (TextView) findViewById(R.id.monitor_aqi);
		monitor_polluction = (TextView) findViewById(R.id.monitor_polluction);
		monitor_first = (TextView) findViewById(R.id.monitor_first);
		monitor_type = (TextView) findViewById(R.id.monitor_type);
		monitor_pm25 = (TextView) findViewById(R.id.monitor_pm25);
		monitor_pm10 = (TextView) findViewById(R.id.monitor_pm10);
		monitor_03 = (TextView) findViewById(R.id.monitor_03);
		monitor_so2 = (TextView) findViewById(R.id.monitor_so2);
		monitor_no2 = (TextView) findViewById(R.id.monitor_no2);
		monitor_co = (TextView) findViewById(R.id.monitor_co);
		detail_24polluction = (TextView) findViewById(R.id.detail_24polluction);
		detail_polluction = (TextView) findViewById(R.id.detail_polluction);
		detail_polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		detail_24polluction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		monitor_back = (ImageView) findViewById(R.id.monitor_back);
		monitor_back.setOnClickListener(this);
		detail_polluction.setOnClickListener(this);
		detail_24polluction.setOnClickListener(this);
		tv_aqi_trend = (TextView) findViewById(R.id.tv_aqi_trend);
		tv_pm10_trend = (TextView) findViewById(R.id.tv_pm10_trend);
		tv_pm25_trend = (TextView) findViewById(R.id.tv_pm25_trend);
		textViews[0] = tv_aqi_trend;
		textViews[1] = tv_pm25_trend;
		textViews[2] = tv_pm10_trend;
		tv_aqi_trend.setOnClickListener(this);
		tv_pm10_trend.setOnClickListener(this);
		tv_pm25_trend.setOnClickListener(this);
		for (int i = 0; i < 24; i++) {
			hours[i] = "10时";
			aqi24_Lists[i] = 10;
		}
		monitor_details_pv_24th.setXCount(500, 5);
		monitor_details_pv_24th.setType(PathView_24.MONITOR_HOUR);
		monitor_details_pv_24th.setDate(aqi24_Lists);
		for (int i = 0; i < 24; i++) {
			hours10[i] = "10时";
			pm10_24_Lists[i] = 10;
		}
		monitor_pathView_24h_pm10.setXCount(500, 5);
		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);
		for (int i = 0; i < 24; i++) {
			hours25[i] = "10时";
			pm25_24_Lists[i] = 10;
		}
		monitor_pathView_24h_pm25.setXCount(500, 5);
		monitor_pathView_24h_pm25.setType(PathView_24h_pm25.MONITOR_HOUR);
		monitor_pathView_24h_pm25.setDate(pm25_24_Lists);
		changeBgColor(0);
	}

	class GetWeatherTask extends AsyncTask<String, Void, MonitorModel> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected MonitorModel doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			String url = UrlComponent.getMonitor;
			MonitorModel _Result = null;
			try {
				_Result = search.getWeatherTask(url, stationcode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(MonitorModel result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result == null)
				return;
			fillData(result);
		}

	}

	private void fillData(MonitorModel result) {
		// TODO Auto-generated method stub
		factor_uptime = result.getMONIDATE();
		monitor_update.setText("更新时间:" + result.getMONIDATE());
		monitor_aqi.setText(result.getAQI() + "");
		monitor_polluction.setText(result.getAIRLEVEL());
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
		for (int i = 0; i < weatherInfo24s.size(); i++) {
			WeatherInfo24 info = weatherInfo24s.get(i);
			hours[i] = info.getUpdate_time().substring(11, 13) + "时";
			aqi24_Lists[i] = info.getAqi();
		}
		monitor_details_pv_24th.setXCount(500, 5);
		monitor_details_pv_24th.setType(PathView_24.MONITOR_HOUR);
		monitor_details_pv_24th.setDate(aqi24_Lists);

		List<PM10Info24H> pm10Info24Hs = result.getPm10Info24Hs();
		for (int i = 0; i < pm10Info24Hs.size(); i++) {
			PM10Info24H info = pm10Info24Hs.get(i);
			hours10[i] = info.getTime().substring(11, 13) + "时";
			pm10_24_Lists[i] = info.getPm10();
		}
		monitor_pathView_24h_pm10.setXCount(500, 5);
		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);

		List<PM25> pm25Info24Hs = result.getPm25Info24Hs();
		for (int i = 0; i < pm25Info24Hs.size(); i++) {
			PM25 info = pm25Info24Hs.get(i);
			hours25[i] = info.getTime().substring(11, 13) + "时";
			pm25_24_Lists[i] = info.getPm25();
		}
		monitor_pathView_24h_pm10.setXCount(500, 5);
		monitor_pathView_24h_pm10.setType(PathView_24h_pm10.MONITOR_HOUR);
		monitor_pathView_24h_pm10.setDate(pm10_24_Lists);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.monitor_back:
			finish();
			break;
		case R.id.detail_polluction:
			intent = new Intent(MonitorActivity.this,
					EnvironmentMonitorPmActivity.class);
			intent.putExtra("from", "MonitorActivity");
			intent.putExtra("aqi", aqi);
			intent.putExtra("level", level);
			intent.putExtra("firstPolluction", firstPolluction);
			intent.putExtra("stationType", stationType);
			intent.putExtra("stationcode", stationcode);
			intent.putExtra("type", "1");
			intent.putExtra("factor_uptime", factor_uptime);
			startActivity(intent);
			break;
		case R.id.detail_24polluction:
			intent = new Intent(MonitorActivity.this,
					EnvironmentMonitorPmActivity.class);
			intent.putExtra("from", "MonitorActivity");
			intent.putExtra("aqi", aqi);
			intent.putExtra("level", level);
			intent.putExtra("firstPolluction", firstPolluction);
			intent.putExtra("stationType", stationType);
			intent.putExtra("stationcode", stationcode);
			intent.putExtra("type", "0");
			intent.putExtra("factor_uptime", factor_uptime);
			startActivity(intent);
			break;
		case R.id.tv_aqi_trend:
			monitor_details_pv_24th.setVisibility(View.VISIBLE);
			monitor_pathView_24h_pm10.setVisibility(View.GONE);
			monitor_pathView_24h_pm25.setVisibility(View.GONE);
			changeBgColor(0);
			break;
		case R.id.tv_pm25_trend:
			monitor_details_pv_24th.setVisibility(View.GONE);
			monitor_pathView_24h_pm10.setVisibility(View.GONE);
			monitor_pathView_24h_pm25.setVisibility(View.VISIBLE);
			changeBgColor(1);
			break;
		case R.id.tv_pm10_trend:
			monitor_details_pv_24th.setVisibility(View.GONE);
			monitor_pathView_24h_pm10.setVisibility(View.VISIBLE);
			monitor_pathView_24h_pm25.setVisibility(View.GONE);
			changeBgColor(2);
			break;

		default:
			break;
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
		} else if (aqi <= 300) {
			bg = R.drawable.aqi_level_5;
		} else {
			bg = R.drawable.aqi_level_6;
		}
		return bg;
	}

	@SuppressWarnings("deprecation")
	private void changeBgColor(int position) {
		for (int i = 0; i < 3; i++) {
			if (i == position) {
				textViews[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kuang10));
			} else {
				textViews[i].setBackgroundDrawable(null);
			}

		}
	}
}
