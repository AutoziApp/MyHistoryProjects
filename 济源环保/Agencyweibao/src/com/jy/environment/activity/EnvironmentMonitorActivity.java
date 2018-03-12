package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.EnvironmentAqiModel;
import com.jy.environment.model.EnvironmentMonitorModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.PathView_Monthh;

public class EnvironmentMonitorActivity extends Activity implements
		OnClickListener {
	Dialog dialog;
	private String city = "";
	private TextView monitor_aqi, monitor_polluction, environment_city;
	private ImageView activity_main_suggest;
	private GetMonitorActivity getMonitorActivity;
	private PathView_Monthh monthh;
	private LinearLayout monitor_lay, station_layout;
	private List<EnvironmentMonitorModel> monitorModels;
	private Intent intent;
//	private HorizontalScrollView monitor_scrollview;
	private LinearLayout monitor_namelay1, monitor_namelay2, monitor_namelay3;
	private List<String> monitorCity = new ArrayList<String>();
	private LinearLayout[] layoutArrays;
	private TextView[] monitorArrays;
	// 监测站名称textview
	private TextView monitor_name1, monitor_name2, monitor_name3,
			monitor_name4, monitor_name5, monitor_name6, monitor_name7,
			monitor_name8, monitor_name9, monitor_name10, monitor_name11,
			monitor_name12, monitor_name13, monitor_name14, monitor_name15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment_monitor);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在加载");
		intent = getIntent();
		city = intent.getStringExtra("city");
		initView();
		getMonitorActivity = new GetMonitorActivity();
		getMonitorActivity.execute("");
	}

	private void initView() {
		// TODO Auto-generated method stub
		monitor_aqi = (TextView) findViewById(R.id.monitor_aqi);
		monitor_polluction = (TextView) findViewById(R.id.monitor_polluction);
		environment_city = (TextView) findViewById(R.id.environment_city);
		monthh = (PathView_Monthh) findViewById(R.id.rank_pathmonthh);
		monitor_lay = (LinearLayout) findViewById(R.id.monitor_lay);
		station_layout = (LinearLayout) findViewById(R.id.station_layout);
		monitor_namelay1 = (LinearLayout) findViewById(R.id.monitor_namelay1);
		monitor_namelay2 = (LinearLayout) findViewById(R.id.monitor_namelay2);
		monitor_namelay3 = (LinearLayout) findViewById(R.id.monitor_namelay3);
		layoutArrays = new LinearLayout[3];
		monitorArrays = new TextView[15];
		layoutArrays[0] = monitor_namelay1;
		layoutArrays[1] = monitor_namelay2;
		layoutArrays[2] = monitor_namelay3;
		monitor_name1 = (TextView) findViewById(R.id.monitor_name1);
		monitor_name2 = (TextView) findViewById(R.id.monitor_name2);
		monitor_name3 = (TextView) findViewById(R.id.monitor_name3);
		monitor_name4 = (TextView) findViewById(R.id.monitor_name4);
		monitor_name5 = (TextView) findViewById(R.id.monitor_name5);
		monitor_name6 = (TextView) findViewById(R.id.monitor_name6);
		monitor_name7 = (TextView) findViewById(R.id.monitor_name7);
		monitor_name8 = (TextView) findViewById(R.id.monitor_name8);
		monitor_name9 = (TextView) findViewById(R.id.monitor_name9);
		monitor_name10 = (TextView) findViewById(R.id.monitor_name10);
		monitor_name11 = (TextView) findViewById(R.id.monitor_name11);
		monitor_name12 = (TextView) findViewById(R.id.monitor_name12);
		monitor_name13 = (TextView) findViewById(R.id.monitor_name13);
		monitor_name14 = (TextView) findViewById(R.id.monitor_name14);
		monitor_name15 = (TextView) findViewById(R.id.monitor_name15);
		monitorArrays[0] = monitor_name1;
		monitorArrays[1] = monitor_name2;
		monitorArrays[2] = monitor_name3;
		monitorArrays[3] = monitor_name4;
		monitorArrays[4] = monitor_name5;
		monitorArrays[5] = monitor_name6;
		monitorArrays[6] = monitor_name7;
		monitorArrays[7] = monitor_name8;
		monitorArrays[8] = monitor_name9;
		monitorArrays[9] = monitor_name10;
		monitorArrays[10] = monitor_name11;
		monitorArrays[11] = monitor_name12;
		monitorArrays[12] = monitor_name13;
		monitorArrays[13] = monitor_name14;
		monitorArrays[14] = monitor_name15;
		monthh.setXCount(500, 5);
		monthh.setType(PathView_Monthh.DAY_MONTH);
		environment_city.setText(city);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(this);
	}

	class GetMonitorActivity extends
			AsyncTask<String, Void, EnvironmentAqiModel> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected EnvironmentAqiModel doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = UrlComponent.monitor;
			BusinessSearch search = new BusinessSearch();
			EnvironmentAqiModel _Result = new EnvironmentAqiModel();
			try {
				_Result = search.getMonitor(url, city);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(EnvironmentAqiModel result) {
			// TODO Auto-generated method stub
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			fillData(result);
		}
	}

	private void fillData(EnvironmentAqiModel result) {
		// TODO Auto-generated method stub
		if (result == null) {
			return;
		}
		try {
			monitor_lay.setVisibility(View.VISIBLE);
			environment_city.setVisibility(View.VISIBLE);
			station_layout.setVisibility(View.VISIBLE);
			monthh.setVisibility(View.VISIBLE);
			monitor_aqi.setText(result.getAqi() + "");
			monitorModels = result.getMonitorModels();
			monthh.setMonitorModels(monitorModels);
			monitor_polluction.setText(CommonUtil.getDengJiByAQI(result
					.getAqi() + ""));
			monitor_polluction.setBackgroundResource(getDuValueRes(result
					.getAqi()));
			List<EnvironmentMonitorModel> models = result.getMonitorModels();
			for (int i = 0; i < models.size(); i++) {
				monitorCity.add(models.get(i).getStation());
			}
			if (monitorCity.size() <= 5) {
				layoutArrays[0].setVisibility(View.VISIBLE);
				layoutArrays[1].setVisibility(View.GONE);
				layoutArrays[2].setVisibility(View.GONE);
			} else if (monitorCity.size() <= 10) {
				layoutArrays[0].setVisibility(View.VISIBLE);
				layoutArrays[1].setVisibility(View.VISIBLE);
				layoutArrays[2].setVisibility(View.GONE);
			} else {
				layoutArrays[0].setVisibility(View.VISIBLE);
				layoutArrays[1].setVisibility(View.VISIBLE);
				layoutArrays[2].setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < monitorCity.size(); i++) {
				monitorArrays[i].setVisibility(View.VISIBLE);
				monitorArrays[i].setText(monitorCity.get(i));
			}
		} catch (Exception e) {
			// TODO: handle exception
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
}
