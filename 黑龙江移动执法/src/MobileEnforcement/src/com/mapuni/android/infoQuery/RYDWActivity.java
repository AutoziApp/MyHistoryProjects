package com.mapuni.android.infoQuery;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.gis.MapActivity;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.taskmanager.TaskMainActivity;

public class RYDWActivity extends BaseActivity {
	private Button startTime_et, endTime_et;
	private LinearLayout middleLayout;
	private String useridString;

	private static Calendar fromCal = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "轨迹查询");
		init();
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		useridString = getIntent().getExtras().getString("userid");

		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		View searchView = inflater.inflate(R.layout.rydw_search, null);
		// 创建时间
		startTime_et = (Button) searchView.findViewById(R.id.start_time_btn);
		startTime_et.setOnClickListener(new startTimeListener());
		endTime_et = (Button) searchView.findViewById(R.id.end_time_btn);
		endTime_et.setOnClickListener(new endTimeListener());

		// 轨迹回放
		Button gjhfButton = (Button) searchView.findViewById(R.id.select_btn);
		gjhfButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String startTime = startTime_et.getText().toString();
				String endTime = endTime_et.getText().toString();

				if (startTime != null && !startTime.equals("")
						&& endTime != null && !endTime.equals("")) {
					if (!DateCompare(startTime, endTime)) {
						Toast.makeText(RYDWActivity.this, "结束时间不能小于开始时间", 1)
								.show();
						endTime_et.setText("");
						return;
					}
				}

				new CustomGjAsyncTask().execute();
			}
		});

		// 返回
		Button backButton = (Button) searchView.findViewById(R.id.back_btn);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RYDWActivity.this.finish();
			}
		});

		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		middleLayout.addView(searchView);
	}

	private Calendar dateAndTime;
	private SimpleDateFormat datefmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 查询开始时间 **/
	private class startTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			dateAndTime = Calendar.getInstance();
			final int year1 = dateAndTime.get(Calendar.YEAR);
			final int month1 = dateAndTime.get(Calendar.MONTH);
			final int day1 = dateAndTime.get(Calendar.DAY_OF_MONTH);
			final int hour1 = dateAndTime.get(Calendar.HOUR_OF_DAY);
			final int minute1 = dateAndTime.get(Calendar.MINUTE);

			new DatePickerDialog(RYDWActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							dateAndTime.set(Calendar.YEAR, year);
							dateAndTime.set(Calendar.MONTH, monthOfYear);
							dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							new TimePickerDialog(RYDWActivity.this, new TimePickerDialog.OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker arg0, int hour, int minute) {
									// TODO Auto-generated method stub
									
									dateAndTime.set(Calendar.HOUR, hour);
									dateAndTime.set(Calendar.MINUTE, minute);
									dateAndTime.set(Calendar.MILLISECOND, 0);
									
									startTime_et.setText(datefmtDate.format(dateAndTime.getTime()));
								}
							}, hour1, minute1, true).show();
						}

					}, year1, month1, day1).show();
		}
	}

	/** 查询结束时间 **/
	private class endTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			dateAndTime = Calendar.getInstance();
			final int year1 = dateAndTime.get(Calendar.YEAR);
			final int month1 = dateAndTime.get(Calendar.MONTH);
			final int day1 = dateAndTime.get(Calendar.DAY_OF_MONTH);
			final int hour1 = dateAndTime.get(Calendar.HOUR_OF_DAY);
			final int minute1 = dateAndTime.get(Calendar.MINUTE);

			new DatePickerDialog(RYDWActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							
							dateAndTime.set(Calendar.YEAR, year);
							dateAndTime.set(Calendar.MONTH, monthOfYear);
							dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							
							new TimePickerDialog(RYDWActivity.this, new TimePickerDialog.OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker arg0, int hour, int minute) {
									// TODO Auto-generated method stub
									
									dateAndTime.set(Calendar.HOUR, hour);
									dateAndTime.set(Calendar.MINUTE, minute);
									dateAndTime.set(Calendar.MILLISECOND, 0);
									
									endTime_et.setText(datefmtDate.format(dateAndTime.getTime()));
								}
							}, hour1, minute1, true).show();
							
						}

					}, year1, month1, day1).show();

		}
	}

	/**
	 * 两个时间比较
	 * 
	 * @throws ParseException
	 */
	private boolean DateCompare(String startTime, String endTime) {
		boolean flag = false;

		if ((java.sql.Timestamp.valueOf(startTime)).before(java.sql.Timestamp
				.valueOf(endTime))) {
			flag = true;
		}
		if ((java.sql.Timestamp.valueOf(startTime)).equals(java.sql.Timestamp
				.valueOf(endTime))) {
			flag = true;
		}
		return flag;
	}

	private class CustomGjAsyncTask extends AsyncTask<Void, Void, Integer> {
		private YutuLoading loading;
		private ArrayList<HashMap<String, Object>> list;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading = new YutuLoading(RYDWActivity.this);
			loading.setCancelable(false);
			loading.setLoadMsg("正在更新数据，请稍候", "");
			loading.showDialog();
		}

		@Override
		protected Integer doInBackground(Void... p) {
			int flag = -1;
			if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
				flag = 0;
				return flag;
			}

			String result = null;
			String methodName = "RYDWSelect";

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userId", useridString);
			String stime = startTime_et.getText().toString();
			if (!stime.equals("")) {
				param.put("StartDate", stime);
			} else {
				param.put("StartDate", "");
			}
			String etime = endTime_et.getText().toString();
			if (!etime.equals("")) {
				param.put("endDate", etime);
			} else {
				param.put("endDate", "");
			}
			// String token = "";
			// try {
			// token = DESSecurity.encrypt(methodName);
			// } catch (Exception e1) {
			//
			// e1.printStackTrace();
			// }
			// param.put("token", token);
			params.add(param);

			try {
				result = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);

				if (result != null) {
					list = JsonHelper.paseJSON(result);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			flag = 1;
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			switch (result) {
			case 0:
				Toast.makeText(RYDWActivity.this, "网络不给力啊，无法获取数据！",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				if (list != null && list.size() > 1) {
					ArrayList<HashMap<String, Double>> points = new ArrayList<HashMap<String, Double>>();
					HashMap<String, Double> map = null;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, Double>();
						try {
							map.put("jd",
									Double.parseDouble(list.get(i)
											.get("JingDu").toString()));
							map.put("wd",
									Double.parseDouble(list.get(i).get("WeiDu")
											.toString()));
							points.add(map);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					ArrayList<HashMap<String, Object>> data = SqliteUtil
							.getInstance().queryBySqlReturnArrayListHashMap(
									"select u_realname from pc_users where userid='"
											+ useridString + "'");
					Intent intent = new Intent();
					intent.setAction("GJHF");
					intent.putExtra("number", 1);
					intent.putExtra("points", points);
					intent.putExtra("name", data.get(0).get("u_realname")
							.toString());
					intent.setClassName("com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.gis.MapActivity");
					RYDWActivity.this.startActivity(intent);
				} else {
					Toast.makeText(RYDWActivity.this, "暂无数据！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			loading.dismissDialog();
		}

	}
}
