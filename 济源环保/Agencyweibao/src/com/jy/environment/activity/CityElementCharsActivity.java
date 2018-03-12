package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.base.AdapterBase;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;

public class CityElementCharsActivity extends ActivityBase {

	private ColumnChartView chart;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLabels = true;
	private boolean hasLabelForSelected = false;

	private String title;
	private String location;
	private String[] XValue;
	private String[] TIMEValue;
	private int[] Yvalue;
	private int[] AQIvalue;
	private int TYPE; // 1 AQI; 2PM10;3 PM25;4 so2;5 no2

	public static final String XVALUE = "XVALUE";
	public static final String YVALUE = "YVALUE";
	public static final String TITLE = "TITLE";
	public static final String AQIVALUE = "AQIVALUE";
	public static final String TYPEVALUE = "TYPEVALUE";
	public static final String TIMEVALUE = "TIMEVALUE";
	public static final String LOCATION = "LOCATION";
	private ColumnChartData columnData;

	TextView textViewTitle;
	TextView textViewLocation;
	private List<CityElementListModel> cityElementListModels = new ArrayList<CityElementCharsActivity.CityElementListModel>();
	private ListView listView;
	private CityElementAdapter cityElementAdapter;
	private TextView type_title;
	private ImageView environment_rank_details_back;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.city_element_chars_actiity);

		initView();
		Intent intent = getIntent();
		XValue = intent.getStringArrayExtra(XVALUE);
		Yvalue = intent.getIntArrayExtra(YVALUE);
		AQIvalue = intent.getIntArrayExtra(AQIVALUE);
		title = intent.getStringExtra(TITLE);
		location = intent.getStringExtra(LOCATION);
		TIMEValue = intent.getStringArrayExtra(TIMEVALUE);
		TYPE = intent.getIntExtra(TYPEVALUE, 1);
		setListHeaderName();
		MyLog.i("XValue" + XValue);
		MyLog.i("Yvalue" + Yvalue);
		MyLog.i("AQIvalue" + AQIvalue);
		MyLog.i("title" + title);
		MyLog.i("TYPE" + TYPE);
		MyLog.i("TIMEValue" + TIMEValue);
		for (int i = 0; i < Yvalue.length; i++) {
			cityElementListModels.add(new CityElementListModel(Yvalue[i], AQIvalue[i], TYPE, XValue[i], TIMEValue[i]));
		}
		cityElementAdapter = new CityElementAdapter(this, cityElementListModels);
		listView.setAdapter(cityElementAdapter);

		generateColumnData(chart, XValue, Yvalue, TYPE);
		textViewLocation.setText(location);
		textViewTitle.setText(title);
	}

	private void setListHeaderName() {
		type_title = (TextView) findViewById(R.id.type_title);
		switch (TYPE) {
		case 1:
			type_title.setText("AQI");
			break;
		case 2:
			type_title.setText("PM10");
			break;
		case 3:
			type_title.setText("PM2.5");
			break;
		case 4:
			type_title.setText("SO2");
			break;
		case 5:
			type_title.setText("NO2");
			break;

		default:
			break;
		}
	}

	private void initView() {
		chart = (ColumnChartView) findViewById(R.id.chart);
		chart.setOnValueTouchListener(new ValueTouchListener());
		textViewLocation = (TextView) findViewById(R.id.location);
		textViewTitle = (TextView) findViewById(R.id.title);
		listView = (ListView) findViewById(R.id.listview);
		environment_rank_details_back = (ImageView) findViewById(R.id.environment_rank_details_back);
		environment_rank_details_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * To animate values you have to change targets values and then call
	 * {@link Chart#startDataAnimation()} method(don't confuse with
	 * View.animate()).
	 */
	private void prepareDataAnimation() {
		for (Column column : columnData.getColumns()) {
			for (SubcolumnValue value : column.getValues()) {
				value.setTarget((float) Math.random() * 100);
			}
		}
	}

	private class ValueTouchListener implements ColumnChartOnValueSelectListener {

		@Override
		public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
		}

		@Override
		public void onValueDeselected() {
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
//				aqi = (float) (Math.random() * 500);
				if (aqi > maxYValue) {
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
				case 4://so2
					subcolumnValue = new SubcolumnValue(aqi, getSO2ColorByIndex((int) aqi));
					break;
				case 5://no2
					subcolumnValue = new SubcolumnValue(aqi, getNO2ColorByIndex((int) aqi));
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
		}else  if(maxYValue <= 240 ){
			maxYValue = 240;
		}else if(maxYValue <= 340 ){
			maxYValue = 340;
		}else if(maxYValue <= 440 ){
			maxYValue = 440;
		}else if(maxYValue <= 540 ){
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

	private int getLevelColorByIntegerValue(int value) {
		if ((value > 0) && (value <= 50)) {
			return (Color.parseColor("#000000"));
		} else if (value <= 100) {
			return (Color.parseColor("#000000"));
		} else if (value <= 150) {
			return (Color.parseColor("#000000"));
		} else if (value <= 200) {
			return (Color.parseColor("#FFFFFF"));
		} else if (value <= 300) {
			return (Color.parseColor("#FFFFFF"));
		} else {
			return (Color.parseColor("#FFFFFF"));
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


	public class CityElementListModel {
		private int Yvalue;
		private int AQIvalue;
		private int TYPE;
		private String XVALUE;
		private String TIMEVALUE;

		public CityElementListModel(int yvalue, int aQIvalue, int tYPE, String xVALUE, String tIMEVALUE) {
			super();
			Yvalue = yvalue;
			AQIvalue = aQIvalue;
			TYPE = tYPE;
			XVALUE = xVALUE;
			TIMEVALUE = tIMEVALUE;
		}

		public String getTIMEVALUE() {
			return TIMEVALUE;
		}

		public void setTIMEVALUE(String tIMEVALUE) {
			TIMEVALUE = tIMEVALUE;
		}

		public String getXVALUE() {
			return XVALUE;
		}

		public void setXVALUE(String xVALUE) {
			XVALUE = xVALUE;
		}

		public int getYvalue() {
			return Yvalue;
		}

		public void setYvalue(int yvalue) {
			Yvalue = yvalue;
		}

		public int getAQIvalue() {
			return AQIvalue;
		}

		public void setAQIvalue(int aQIvalue) {
			AQIvalue = aQIvalue;
		}

		public int getTYPE() {
			return TYPE;
		}

		public void setTYPE(int tYPE) {
			TYPE = tYPE;
		}

	}

	private class CityElementAdapter extends AdapterBase<CityElementListModel> {
		private LayoutInflater inflater;

		public CityElementAdapter(Context pContext, List<CityElementListModel> pList) {
			super(pContext, pList);
			inflater = LayoutInflater.from(pContext);
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.city_element_list_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.content_time = (TextView) convertView.findViewById(R.id.content_time);
				holder.content_dengji = (TextView) convertView.findViewById(R.id.content_dengji);
				holder.content_value = (TextView) convertView.findViewById(R.id.content_value);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CityElementListModel cityElementListModel = getList().get(arg0);

			holder.content_time.setText(cityElementListModel.getTIMEVALUE() + "");
			holder.content_dengji.setText(CommonUtil.getDengJiByAQII(cityElementListModel.getAQIvalue() + ""));
			holder.content_value.setText(cityElementListModel.getYvalue() + "");
			holder.content_dengji.setTextColor((getLevelColorByIntegerValue(cityElementListModel.getAQIvalue())));
			holder.content_dengji.setBackgroundResource(getDrawablebyValue(cityElementListModel.getAQIvalue()));
			return convertView;
		}

	}

	static class ViewHolder {
		TextView content_time;
		TextView content_dengji;
		TextView content_value;
	}

	public static int getDrawablebyValue(int aqi) {
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
}
