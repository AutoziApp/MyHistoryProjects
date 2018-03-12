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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.model.PmDayHourModel;
import com.jy.environment.model.PmModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.FixGridLayout;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentMonitorPmActivity extends Activity implements
		OnClickListener {
	private String city = "";
	private GetPmTask pmTask;
	private LineChartView monitor_chart;
	private FixGridLayout monitor_name;
	private ImageView activity_main_suggest;
	private Context context;
	private static List<String> listColors = new ArrayList<String>();
	private List<String> listShowLineNum = new ArrayList<String>();
	private List<PmModel> dayModels = new ArrayList<PmModel>();
	private List<PmModel> hourModels = new ArrayList<PmModel>();
	private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
	private List<AxisValue> yAxisValues = new ArrayList<AxisValue>();
	private boolean isCheckfull = false;
	private Intent intent;
	Dialog dialog;
	private TextView monitor_aqi, monitor_polluction, monitor_first,
			monitor_type, environment_city, monitor_update_time, monitor_pm25,
			monitor_title, station_title, monitor_nametitle;
	private String aqi_value = "", level = "", from = "", firstPolluction = "",
			stationType = "", stationcode = "", factor_uptime = "",
			stationname = "";
	private RelativeLayout head_aqi;
	// 两个界面进入这个activity,定义一个变量进行区分
	private boolean comFlag = true;
	private LineChartView rank_pm25, rank_pm10, rank_o3, rank_so2, rank_no2,
			rank_co;
	private LinearLayout monitor_lay;
	private String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.environment_monitor_oldpm);
		context = this;
		intent = getIntent();
		from = intent.getStringExtra("from");
		if (from.equals("CopyOfEnvironmentWeatherRankActivity")) {
			comFlag = true;
		} else {
			comFlag = false;
		}
		type = intent.getStringExtra("type");
		city = intent.getStringExtra("city");
		aqi_value = intent.getStringExtra("aqi");
		level = intent.getStringExtra("level");
		factor_uptime = intent.getStringExtra("factor_uptime");
		station_title = (TextView) findViewById(R.id.station_title);
		monitor_nametitle = (TextView) findViewById(R.id.monitor_nametitle);
		monitor_title = (TextView) findViewById(R.id.monitor_title);
		if (!comFlag) {
			firstPolluction = intent.getStringExtra("firstPolluction");
			stationType = intent.getStringExtra("stationType");
			stationcode = intent.getStringExtra("stationcode");
			stationname = intent.getStringExtra("stationname");
			// 设置标题名称，分为24小时和30天 和分钟 TODO
			if (type.equals("0")) {//小时
				monitor_title.setText(stationname + "近24小时污染物浓度趋势图");
				station_title.setText(stationname + "近24小时污染物浓度趋势图");
				monitor_nametitle.setText("近24小时" + stationname + "趋势");
			}else if (type.equals("2")) {//分钟
				monitor_title.setText(stationname + "近12小时分钟数据浓度趋势图");
				station_title.setText(stationname + "近12小时分钟数据浓度趋势图");
				monitor_nametitle.setText("近12小时" + stationname + "分钟数据趋势");
			} else {//天
				monitor_title.setText(stationname + "近30天污染物浓度趋势图");
				station_title.setText(stationname + "近30天污染物浓度趋势图");
				monitor_nametitle.setText("近30天" + stationname + "趋势");
			}
		} else {
			if (type.equals("0")) {
				monitor_title.setText(city + "近24小时污染物浓度趋势图");
				station_title.setText(city + "近24小时污染物浓度趋势图");
				monitor_nametitle.setText("近24小时" + city + "趋势");
			} else {
				monitor_title.setText(city + "近30天污染物浓度趋势图");
				station_title.setText(city + "近30天污染物浓度趋势图");
				monitor_nametitle.setText("近30天" + city + "趋势");
			}
		}
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText("正在加载");
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
		initView();
		pmTask = new GetPmTask();
		pmTask.execute("");
	}

	private void initView() {
		monitor_chart = (LineChartView) findViewById(R.id.monitor_chart);
		monitor_name = (FixGridLayout) findViewById(R.id.monitor_name);
		monitor_update_time = (TextView) findViewById(R.id.monitor_update_time);
		monitor_pm25 = (TextView) findViewById(R.id.monitor_pm25);
		head_aqi = (RelativeLayout) findViewById(R.id.head_aqi);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(this);
		monitor_aqi = (TextView) findViewById(R.id.monitor_aqi);
		monitor_lay = (LinearLayout) findViewById(R.id.monitor_lay);
		monitor_polluction = (TextView) findViewById(R.id.monitor_polluction);
		environment_city = (TextView) findViewById(R.id.environment_city);
		monitor_first = (TextView) findViewById(R.id.monitor_first);
		monitor_type = (TextView) findViewById(R.id.monitor_type);
		environment_city.setText(city);
		monitor_aqi.setText(aqi_value);
		monitor_polluction.setText(CommonUtil.getDengJiByAQI(aqi_value));
		monitor_polluction.setBackgroundResource(getDuValueRes(Integer
				.parseInt(aqi_value)));
		// monitor_update_time.setText("更新时间:" + factor_uptime);
		try {
			if (!comFlag) {
				monitor_first.setVisibility(View.VISIBLE);
				monitor_type.setVisibility(View.VISIBLE);
				monitor_first.setText("首要污染物:" + firstPolluction);
				monitor_type.setText("站点类型:" + stationType);
			}
		} catch (Exception e) {
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

	class GetPmTask extends AsyncTask<String, Void, PmDayHourModel> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected PmDayHourModel doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "";
			if (comFlag) {
//				url = UrlComponent.monitor_hour_day + "?city=" + city;	
				url = UrlComponent.monitor_hour_day;
			} else {
				url = UrlComponent.station_hour_day + "?code=" + stationcode;
			}
			BusinessSearch search = new BusinessSearch();
			PmDayHourModel _Result = new PmDayHourModel();
			try {
				_Result = search.getMonitorDayHour(url, comFlag);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(PmDayHourModel result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (result == null) {
				return;
			}
			fillPmData(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_main_suggest:
			finish();
			break;
		default:
			break;
		}
	}

	public void fillPmData(PmDayHourModel result) {
		if (result == null) {
			return;
		}
		head_aqi.setVisibility(View.VISIBLE);
		if (type.equals("0")) {
			dayModels = result.getHourModels();
		}else if (type.equals("2")) {
			dayModels = result.getMinuteModels();
		} else {
			dayModels = result.getDayModels();
		}
		String updatetime = result.getTime();
		String pollutant = result.getPollutant();
		String pm10 = "", pm25 = "";
		if (type.equals("0")) {
			pm10 = result.getPm10();
			pm25 = result.getPm25();
		} else if (type.equals("2")) {
			pm10 = result.getPm10();
			pm25 = result.getPm25();
		} else {
			pm10 = result.getPmday10();
			pm25 = result.getPmday25();
		}
		try {
			monitor_update_time.setText("更新时间:" + updatetime);
		} catch (Exception e) {
		}
		monitor_update_time.setVisibility(View.VISIBLE);
		if (pollutant != null && !pollutant.equals("")) {
			monitor_first.setVisibility(View.VISIBLE);
			pollutant = tranPolluction(pollutant);
			monitor_first.setText("首要污染物:" + pollutant);
		}
		if (pm10 != null && pm10.length() >= 7) {
			pm10 = pm10.substring(0, 6);
		}
		if (pm25 != null && pm25.length() >= 7) {
			pm25 = pm25.substring(0, 6);
		}
		if (pm10 != null && pm10 != null) {
			monitor_type.setVisibility(View.VISIBLE);
			if (type.equals("0")) {
				monitor_type.setText(pm10);
			} else if (type.equals("2")) {
				monitor_type.setText(pm10);
			} else {
				monitor_type.setText(pm10);
			}
		}
		if (!pm25.equals("") && !pm25.equals("")) {
			monitor_pm25.setVisibility(View.VISIBLE);
			if (type.equals("0")) {
				monitor_pm25.setText(pm25);
			} else if (type.equals("2")) {
				monitor_pm25.setText(pm25);
			} else {
				monitor_pm25.setText(pm25);
			}
		}
		try {
			monitor_lay.setVisibility(View.VISIBLE);
			environment_city.setVisibility(View.VISIBLE);
			// station_layout.setVisibility(View.VISIBLE);
			for (int i = 0; i < 6; i++) {
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
				//对齐 第一个box TextSize与第四个相同
				//第二个与第五个xiangt
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
				}
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
						generateData(type);
					}
				});
				listShowLineNum.add(i + "");
				monitor_name.addView(box);
			}
			reset();
			generateData(type);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void fillData(PmDayHourModel result) {
		try {
			head_aqi.setVisibility(View.VISIBLE);
			monitor_first.setVisibility(View.VISIBLE);
			monitor_type.setVisibility(View.VISIBLE);
			dayModels = result.getDayModels();
			generateData(type);
		} catch (Exception e) {
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
	private Object weeks;

	private void generateData(String type) {
		List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < listShowLineNum.size(); ++i) {
			int location = Integer.parseInt(listShowLineNum.get(i));
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
							.getCO()));
				}
				values.add(value);
				String time = dayModels.get(j).getTIME();
				if (time.endsWith(":00")) {
					time = time.replace(":00", "");
				}
				if (j % 2 == 0 && i == 0) {
					mAxisValues.add(new AxisValue(j, time.toCharArray()));
				} else if (i == 0) {
					mAxisValues.add(new AxisValue(j, "".toCharArray()));
				}
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

		if (hasAxes) {
			Axis axisX = new Axis().setHasLines(true);
			Axis axisY = new Axis().setHasLines(true);

			axisX.setTextSize(10);
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
		monitor_chart.setLineChartData(data);
	}

	/**
	 * X 轴的显示
	 */
	private void getAxisLables(List<PmModel> dayModels) {
		for (int i = 0; i < dayModels.size(); i++) {
			String time = dayModels.get(i).getTIME();
			if (time.startsWith("0")) {
				time = time.substring(1);
			}
			if (i % 2 == 0) {
				mAxisValues.add(new AxisValue(i).setLabel(time.toCharArray()));
			} else {
				mAxisValues.add(new AxisValue(i).setLabel("".toCharArray()));
			}
		}
	}

	// y轴显示
	private void getAxisLables(int m) {
		float max = 200;
		yAxisValues.add(new AxisValue(0).setValue(50));
		yAxisValues.add(new AxisValue(1).setValue(100));
		yAxisValues.add(new AxisValue(2).setValue(150));
		yAxisValues.add(new AxisValue(3).setValue(200));
		yAxisValues.add(new AxisValue(4).setValue(250));
		yAxisValues.add(new AxisValue(5).setValue(300));
		yAxisValues.add(new AxisValue(6).setValue(350));
		yAxisValues.add(new AxisValue(7).setValue(400));
		yAxisValues.add(new AxisValue(8).setValue(450));
		yAxisValues.add(new AxisValue(9).setValue(500));

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
		// resetViewport();
	}

	private void resetViewport(int m) {
		// float max = 200;
		// if (m == 0) {
		// max = Collections.max(pm25List);
		// } else if (m == 1) {
		// max = Collections.max(pm10List);
		// } else if (m == 2) {
		// max = Collections.max(pm03List);
		// } else if (m == 3) {
		// max = Collections.max(pmso2List);
		// } else if (m == 4) {
		// max = Collections.max(pmno2List);
		// } else if (m == 5) {
		// max = Collections.max(pmcoList);
		// }
		final Viewport v = new Viewport(monitor_chart.getMaximumViewport());
		v.bottom = 0;
		// v.top = 100;
		v.top = 500;
		monitor_chart.setMaximumViewport(v);
//		monitor_chart.setCurrentViewport(v, false);
		monitor_chart.setCurrentViewport(v);
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EnvironmentMonitorPmActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EnvironmentMonitorPmActivity");
		MobclickAgent.onPause(this);
	}
}
