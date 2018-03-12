package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.EnvironmentAqiModel;
import com.jy.environment.model.EnvironmentMonitorModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.FixGridLayout;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentLineMonitorActivity extends Activity implements
		OnClickListener, OnItemSelectedListener {
	private LineChartView chart;
	Dialog dialog;
	private String city = "";
	private TextView monitor_aqi, monitor_polluction, environment_city,
			monitor_first, monitor_type, monitor_update_time, monitor_pm10,
			line_title,line_motitle;
	private ImageView activity_main_suggest;
	private GetMonitorActivity getMonitorActivity;
	// private LinearLayout monitor_lay, station_layout;
	private List<EnvironmentMonitorModel> monitorModels;
	private Intent intent;
	private LinearLayout monitor_namelay1, monitor_namelay2, monitor_namelay3,
			monitor_lay;
	private List<String> monitorCity = new ArrayList<String>();
	private TextView[] monitorArrays;
	private FixGridLayout all_station;
	private Context context;
	private static List<String> listColors = new ArrayList<String>();
	private List<String> listShowLineNum = new ArrayList<String>();
	private RelativeLayout head_aqi;
	private Spinner air_polluction;
	private List<String> polluctionArrays = new ArrayList<String>();
	private List<String> lineTitleArrays = new ArrayList<String>();
	private ArrayAdapter<String> polluctionAdapter;
	private int selectPosition = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment_line_monitor);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在加载");
		intent = getIntent();
		context = this;
		city = intent.getStringExtra("city");
		initView();
		listColors.add("#12948d");
		listColors.add("#d24d86");
		listColors.add("#976925");
		listColors.add("#bf3838");
		listColors.add("#19962d");
		listColors.add("#f65627");
		listColors.add("#c1d58f");
		listColors.add("#00b7ee");
		listColors.add("#1cd287");
		listColors.add("#00b7ee");
		listColors.add("#16753e");
		listColors.add("#ff1d1d");
		getMonitorActivity = new GetMonitorActivity();
		getMonitorActivity.execute("");
	}

	private void initView() {
		// TODO Auto-generated method stub
		monitor_aqi = (TextView) findViewById(R.id.monitor_aqi);
		line_motitle = (TextView) findViewById(R.id.line_motitle);
		chart = (LineChartView) findViewById(R.id.chart);
		all_station = (FixGridLayout) findViewById(R.id.all_station);
		monitor_polluction = (TextView) findViewById(R.id.monitor_polluction);
		environment_city = (TextView) findViewById(R.id.environment_city);
		monitor_first = (TextView) findViewById(R.id.monitor_first);
		monitor_type = (TextView) findViewById(R.id.monitor_type);
		line_title = (TextView) findViewById(R.id.line_title);
		head_aqi = (RelativeLayout) findViewById(R.id.head_aqi);
		monitor_update_time = (TextView) findViewById(R.id.monitor_update_time);
		monitor_pm10 = (TextView) findViewById(R.id.monitor_pm10);
		monitor_lay = (LinearLayout) findViewById(R.id.monitor_lay);
		air_polluction = (Spinner) findViewById(R.id.air_polluction);
		// station_layout = (LinearLayout) findViewById(R.id.station_layout);
		monitor_namelay1 = (LinearLayout) findViewById(R.id.monitor_namelay1);
		monitor_namelay2 = (LinearLayout) findViewById(R.id.monitor_namelay2);
		monitor_namelay3 = (LinearLayout) findViewById(R.id.monitor_namelay3);
		monitorArrays = new TextView[15];
		environment_city.setText(city);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(this);
		polluctionArrays.add("AQI");
		polluctionArrays.add("PM2.5");
		polluctionArrays.add("PM10");
		polluctionArrays.add("O3");
		polluctionArrays.add("SO2");
		polluctionArrays.add("NO2");
		polluctionArrays.add("CO");
		lineTitleArrays.add(city + "监测站点近30天PM2.5浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天PM10浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天O3浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天SO2浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天NO2浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天CO浓度趋势图");
		lineTitleArrays.add(city + "监测站点近30天AQI趋势图");
		line_motitle.setText(city + "监测站点趋势图");
		polluctionAdapter = new ArrayAdapter<String>(
				EnvironmentLineMonitorActivity.this,
				R.layout.spinner_checked_text2, polluctionArrays) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				// if (position == 0) {
				// View view = LayoutInflater.from(
				// EnvironmentLineMonitorActivity.this).inflate(
				// R.layout.spinner_item_first_layout, null);
				// return view;
				// }
				return super.getView(position, convertView, parent);
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				// TODO Auto-generated method stub
				View view;
				if (position == 0) {
					view = LayoutInflater.from(
							EnvironmentLineMonitorActivity.this).inflate(
							R.layout.spinner_item_first_layout, null);
					View item_first_lay = (LinearLayout) view
							.findViewById(R.id.item_first_lay);
					if (air_polluction.getSelectedItemPosition() == position) {
						item_first_lay.setBackgroundColor(Color
								.parseColor("#e5e5e5"));
					} else {
						item_first_lay.setBackgroundColor(Color
								.parseColor("#ffffff"));
					}
					return view;
				}
				view = LayoutInflater.from(EnvironmentLineMonitorActivity.this)
						.inflate(R.layout.spinner_item_layout, null);
				TextView tv_spinner = (TextView) view
						.findViewById(R.id.tv_spinner);
				tv_spinner.setText(polluctionArrays.get(position));
				if (air_polluction.getSelectedItemPosition() == position) {
					view.setBackgroundColor(Color.parseColor("#e5e5e5"));
				} else {
					view.setBackgroundColor(Color.parseColor("#ffffff"));
				}
				return view;
			}
		};
		air_polluction.setSelection(0, true);
		air_polluction.setAdapter(polluctionAdapter);
		air_polluction.setOnItemSelectedListener(this);
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
			head_aqi.setVisibility(View.VISIBLE);
			// monitor_lay.setVisibility(View.VISIBLE);
			environment_city.setVisibility(View.VISIBLE);
			// station_layout.setVisibility(View.VISIBLE);
			String updatetime = result.getUpdatetime();
			String pollutant = result.getPollutant();
			String pm10 = result.getPm10();
			String pm25 = result.getPm25();
			if (pm10 != null && pm10.length() >= 7) {
				pm10 = pm10.substring(0, 6);
			}
			if (pm25 != null && pm25.length() >= 7) {
				pm25 = pm25.substring(0, 6);
			}
			if (updatetime != null && !updatetime.equals("")) {
				monitor_update_time.setVisibility(View.VISIBLE);
				monitor_update_time.setText("更新时间:" + updatetime);
			}
			if (pollutant != null && !pollutant.equals("")) {
				monitor_first.setVisibility(View.VISIBLE);
				pollutant = tranPolluction(pollutant);
				monitor_first.setText("首要污染物:" + pollutant);
			}
			if (pm10 != null && pm10 != null) {
				monitor_type.setVisibility(View.VISIBLE);
				monitor_type.setText(pm10);
			}
			if (!pm25.equals("") && !pm25.equals("")) {
				monitor_pm10.setVisibility(View.VISIBLE);
				monitor_pm10.setText(pm25);
			}
			monitor_aqi.setText(result.getAqi() + "");
			monitorModels = result.getMonitorModels();
			monitor_polluction.setText(CommonUtil.getDengJiByAQI(result
					.getAqi() + ""));
			monitor_polluction.setBackgroundResource(getDuValueRes(result
					.getAqi()));
			List<EnvironmentMonitorModel> models = result.getMonitorModels();
			for (int i = 0; i < models.size(); i++) {
				MyLog.i("" + models.get(i).getStation());
				monitorCity.add(models.get(i).getStation());
				all_station.setmCellHeight(CommonUtil.dip2px(context, 45));
				all_station.setmCellWidth(CommonUtil.dip2px(context, 120));
				final CheckBox box = new CheckBox(context);
				box.setTextSize(CommonUtil.dip2px(context, 6));
				// View convertView=
				// LayoutInflater.from(context).inflate(R.layout.check, null);
				// CheckBox box = (CheckBox)
				// convertView.findViewById(R.id.check);
				box.setTextColor(Color.parseColor(listColors.get(i)));
				box.setTag(i);
				box.setText(models.get(i).getStation());
				box.setChecked(true);
				box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int tag = (Integer) box.getTag();
						reset();
						if (isChecked) {
							if (!listShowLineNum.contains(tag + "")) {
								listShowLineNum.add(tag + "");
							}
						} else {
							if (listShowLineNum.contains(tag + "")) {
								listShowLineNum.remove(tag + "");
							}
						}
						generateData(selectPosition);
					}
				});
				listShowLineNum.add(i + "");
				all_station.addView(box);
			}
			reset();
			generateData(selectPosition);
			monitor_lay.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

	private void generateData(int type) {
		List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
		List<Line> lines = new ArrayList<Line>();
		line_title.setText(lineTitleArrays.get(type));
		for (int i = 0; i < listShowLineNum.size(); ++i) {
			int location = Integer.parseInt(listShowLineNum.get(i));
			List<PointValue> values = new ArrayList<PointValue>();
			for (int j = 0; j < monitorModels.get(location).getInfoMonths()
					.size(); j++) {
				float x_value = 0.0f;
				if (type == 6) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getAqi();
				} else if (type == 0) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getPm25();
				} else if (type == 1) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getPm10();
				} else if (type == 2) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getO3();
				} else if (type == 3) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getSo2();
				} else if (type == 4) {
					x_value = monitorModels.get(location).getInfoMonths()
							.get(j).getNo2();
				} else if (type == 5) {
					x_value = (float) monitorModels.get(location)
							.getInfoMonths().get(j).getCo();
				}
				values.add(new PointValue(j, x_value));
				// mAxisValues.add(new AxisValue(i, days[i].toCharArray()));
				String time = monitorModels.get(location).getInfoMonths()
						.get(j).getUpdate_time().substring(5, 10);
				if (j % 3 == 0 && i == 0) {
					mAxisValues.add(new AxisValue(j, time.toCharArray()));
					MyLog.i(monitorModels.get(location).getInfoMonths().get(j)
							.getUpdate_time()
							+ "");
				} else if (i == 0) {
					mAxisValues.add(new AxisValue(j, "".toCharArray()));
					MyLog.i(monitorModels.get(location).getInfoMonths().get(j)
							.getUpdate_time().toCharArray()
							+ "22");
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
		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setValueSelectionEnabled(hasLabelForSelected);
		data = new LineChartData(lines);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);

			axisX.setValues(mAxisValues);
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
		chart.setLineChartData(data);

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
		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setValueSelectionEnabled(hasLabelForSelected);
		resetViewport();
	}

	private void resetViewport() {
		final Viewport v = new Viewport(chart.getMaximumViewport());
		v.bottom = 0;
		// v.top = 100;
		v.top = 500;
		chart.setMaximumViewport(v);
//		chart.setCurrentViewport(v, false);
		chart.setCurrentViewport(v);
	}

	private String tranPolluction(String polluction) {
		String firstPolluction = polluction;
		if (polluction.contains("臭氧")) {
			firstPolluction = "O3";
		} else if (polluction.equals("二氧化硫")) {
			firstPolluction = "SO2";
		} else if (polluction.equals("二氧化氮")) {
			firstPolluction = "NO2";
		} else if (polluction.equals("一氧化碳")) {
			firstPolluction = "CO";
		} else if (polluction.equals("PM25")) {
			firstPolluction = "PM2.5";
		}
		return firstPolluction;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// selectPosition为6代表AQI.
		if (arg2 == 0) {
			selectPosition = 6;
		} else {
			selectPosition = arg2 - 1;
		}
		generateData(selectPosition);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EnvironmentLineMonitorActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EnvironmentLineMonitorActivity");
		MobclickAgent.onPause(this);
	}
}
