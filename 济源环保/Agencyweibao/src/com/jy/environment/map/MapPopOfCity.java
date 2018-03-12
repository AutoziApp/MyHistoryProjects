package com.jy.environment.map;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.activity.MapMainNewActivity;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.webservice.UrlComponent;

public class MapPopOfCity extends LinearLayout {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		// Log.v("mappop_city_a", "pingbi");
	}

	private int pollutionType;
	private String cityname;
	ImageView titleimg = null;
	TextView titletxt = null;
	TextView realvaltxt = null;
	LinearLayout main = null;
	LinearLayout historylayout = null;
	LinearLayout aqichart = null;
	LinearLayout pm10chart = null;
	LinearLayout pm25chart = null;
	LinearLayout no2chart = null;
	LinearLayout so2chart = null;
	LinearLayout cochart = null;
	LinearLayout o3chart = null;
	LinearLayout cont = null;
	Context _context = null;

	private double[] _aqi, _pm25, _pm10, _no2, _so2, _o3, _co;
	private String _uptdate[];
	private String _weekday[];
	private String _level[];

	public MapPopOfCity(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 3333333333
	 * TODO YYF
	 * @param context
	 * @param cityname
	 * @param polluteValue
	 * @param pollutionType
	 */
	public MapPopOfCity(Context context, String name, String polluteValue,
			int type) {
		super(context);
		// TODO Auto-generated constructor stub
		cityname = name;
		pollutionType = type;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.map_popup_city_a, this);

		titleimg = (ImageView) findViewById(R.id.map_pop_ac_titleimg);
		titletxt = (TextView) findViewById(R.id.map_pop_ac_title);
		realvaltxt = (TextView) findViewById(R.id.map_pop_ac_realval);
		main = (LinearLayout) findViewById(R.id.map_pop_ac_main);
		historylayout = (LinearLayout) findViewById(R.id.map_pop_ac_historyval);
		aqichart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_aqi);
		pm10chart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_pm10);
		pm25chart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_pm25);
		no2chart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_no2);
		so2chart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_so2);
		cochart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_co);
		o3chart = (LinearLayout) findViewById(R.id.map_pop_ac_chart_o3);
		cont = (LinearLayout) findViewById(R.id.map_pop_ac_cont);
		_context = context;

		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		String ccode = citydb.getCityCode(cityname);
		String url = UrlComponent.aqihistoryURL + ccode + "/one"
				+ UrlComponent.token;
		// String [] parm = {url,aqi};
		HistoryTask task = new HistoryTask();
		task.execute(url, cityname, polluteValue);
	}

	/**
	 * 从时间文本中抽取hour时刻
	 * 
	 * @param time
	 *            “2014-01-23 09:00:00.0”
	 * @return
	 */
	private int gethourFromTimestr(String time) {

		String str1 = time.split(" ")[1];

		String str2 = str1.split(":")[0];
		return Integer.parseInt(str2);
	}

	/**
	 * @param ll
	 *            父窗口
	 * @param val
	 *            值
	 * @param maxval
	 *            上限值
	 */
	private void addrectview(LinearLayout ll, double val, double maxval,
			String type) {

		View view = new View(_context);
		view.setBackgroundColor(getChartItemColor(val, maxval, type));
		if ((val / maxval) < 0.1f) {
			val = maxval * 0.1f;
		}
		// view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// WbMapUtil.dip2px(_context, (float) (20f * (val / maxval))), 1.0f));
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				WbMapUtil.dip2px(_context, (float) (30f * (val / maxval))),
				1.0f));
		ll.addView(view);
		View vline = new View(_context);
		vline.setLayoutParams(new LayoutParams(1, LayoutParams.FILL_PARENT));
		ll.addView(vline);
	}

	/**
	 * @param val
	 *            当前值
	 * @param maxval
	 *            上限值
	 * @return int color
	 */
	private int getChartItemColor(double val, double maxval, String type) {
		// ---------------------------------------
		// 参考标准 0---50---100---150---200---300------500
		// 绿 黄 橙 红 紫 褐红
		//
		// ---------------------------------------
		int drawable = getResources().getColor(R.color.air1);
		if (type.equals("0")) {
			if (val <= 50) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 150) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 250) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 350) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 420) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("1")) {
			if (val <= 35) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 75) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 115) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 150) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 250) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("2")) {
			if (val <= 160) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 200) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 300) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 400) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 800) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("3")) {
			if (val <= 150) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 500) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 650) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 800) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 1600) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("4")) {
			if (val <= 100) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 200) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 700) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 1200) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 2340) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("5")) {
			if (val <= 5) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 10) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 35) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 60) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 90) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		} else if (type.equals("6")) {
			if (val <= 50) {
				return getResources().getColor(R.color.air1);
			} else if (val <= 100) {
				return getResources().getColor(R.color.air2);
			} else if (val <= 150) {
				return getResources().getColor(R.color.air3);
			} else if (val <= 200) {
				return getResources().getColor(R.color.air4);
			} else if (val <= 300) {
				return getResources().getColor(R.color.air5);
			} else {
				return getResources().getColor(R.color.air6);
			}
		}
		return drawable;
		// int color = Color.GREEN;
		// double stval = val * 500 / maxval;
		// if (stval > 301) {
		// color = Color.parseColor("#8e236b");
		// } else if (stval > 200) {
		// color = Color.parseColor("#800080");
		// } else if (stval > 150) {
		// color = Color.RED;
		// } else if (stval > 100) {
		// color = Color.parseColor("#ffa500");
		// } else if (stval > 50) {
		// color = Color.YELLOW;
		// } else if (stval >= 0) {
		// color = Color.GREEN;
		// }
		// return color;
	}

	class HistoryTask extends AsyncTask<String, Void, Void> {
		int aqi;

		@Override
		protected Void doInBackground(String... params) {
//			url, cityname, polluteValue
			String url = params[0];
			String cityname = params[1];
			//之前是枚举
			String polluteValue = params[2];
			
			
			// params[0] url
			String urlNew = UrlComponent.getMapHis;
			HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
			_BodyParamMap.put("city", params[1]);
			String ret1 = null;
			try {
				ret1 = ApiClient.getDataFromServerByNewPost(urlNew,
						_BodyParamMap);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			MyLog.i(">>>>>>>>>>lengng1" + ret1);
			MyLog.i("1" + ret1);
			if (ret1 != null && ret1.length() > 20) {
			} else {
				// String ret = ApiClient.connServerForResult(params[0]);
				ret1 = ApiClient.connServerForResult(params[0]);
				MyLog.i(params[0]);
				MyLog.i("3" + ret1);
			}
			MyLog.i(">>>>>>>>>>lengng2" + ret1);
			MyLog.i("4" + ret1);
			try {
				aqi = Integer.parseInt(params[2].replace(".0", ""));
				MyLog.i(aqi + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				JSONArray array = new JSONArray(ret1);
				_aqi = new double[array.length()];
				_pm10 = new double[array.length()];
				_pm25 = new double[array.length()];
				_so2 = new double[array.length()];
				_no2 = new double[array.length()];
				_co = new double[array.length()];
				_o3 = new double[array.length()];
				_uptdate = new String[array.length()];
				_weekday = new String[array.length()];
				_level = new String[array.length()];

				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					_uptdate[i] = jsonObject.getString("update_time");
					_weekday[i] = jsonObject.getString("weekDay");
					_level[i] = jsonObject.getString("level");
					// Log.v("jsonObject.getString",
					// jsonObject.getString("level"));
					// Log.v("jsonObject.getString",
					// jsonObject.getString("level").toString());
					// Log.v("_level[i] ", _level[i].toString() );
					_aqi[i] = jsonObject.getDouble("aqi");
					_pm10[i] = jsonObject.getDouble("pm10");
					_pm25[i] = jsonObject.getDouble("pm25");
					_so2[i] = jsonObject.getDouble("so2");
					_no2[i] = jsonObject.getDouble("no2");
					_co[i] = jsonObject.getDouble("co");
					_o3[i] = jsonObject.getDouble("o3");
				}

				// Log.v("OKOOO", "OKKKKKK");

			} catch (Exception e) {

				Log.v("POPAIR", e.toString());
				return null;
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void result) {
			//
			super.onPostExecute(result);
			if (_level == null)
				return;
			// 调整窗口页面数据
			if (_level.length <= 0) {
				main.setAlpha((float) 1.0);
				cont.setVisibility(LinearLayout.VISIBLE);
				titletxt.setText("暂未提供数据。");
				return;
			}
			main.setAlpha((float) 1.0);
			cont.setVisibility(LinearLayout.VISIBLE);
			for (int i = 0; i < _level.length -1; i++) {
				MyLog.i(_level[i]);
			}
			String nowlevel = _level[_level.length - 1].toString();
			MyLog.i("nowlevel :" + nowlevel);
			int aqi_img = R.drawable.excellent;
			String aqiText = "无数据";
			// if (nowlevel.equals("严重污染")) {
			// aqi_img = R.drawable.dead;
			// aqiText = "严重污染";
			// } else if (nowlevel.equals("重度污染")) {
			// aqi_img = R.drawable.heavy;
			// aqiText = "重度污染";
			// } else if (nowlevel.equals("中度污染")) {
			// aqi_img = R.drawable.medium;
			// aqiText = "中度污染";
			// } else if (nowlevel.equals("轻度污染")) {
			// aqi_img = R.drawable.mild;
			// aqiText = "轻度污染";
			// } else if (nowlevel.equals("良")) {
			// aqi_img = R.drawable.fine;
			// aqiText = "良";
			// } else if(nowlevel.equals("优")){
			// aqi_img = R.drawable.excellent;
			// aqiText = "优";
			// }
			if (nowlevel.equals("严重污染")) {
				aqi_img = R.drawable.bg_img6;
				aqiText = "严重污染";
			} else if (nowlevel.equals("重度污染")) {
				aqi_img = R.drawable.bg_img5;
				aqiText = "重度污染";
			} else if (nowlevel.equals("中度污染")) {
				aqi_img = R.drawable.bg_img4;
				aqiText = "中度污染";
			} else if (nowlevel.equals("轻度污染")) {
				aqi_img = R.drawable.bg_img3;
				aqiText = "轻度污染";
			} else if (nowlevel.equals("良")) {
				aqi_img = R.drawable.bg_img2;
				aqiText = "良";
			} else if (nowlevel.equals("优")) {
				aqi_img = R.drawable.bg_img1;
				aqiText = "优";
			}
			if (aqi > 300) {
				aqi_img = R.drawable.bg_img6;
				aqiText = "严重污染";
			} else if (aqi > 200) {
				aqi_img = R.drawable.bg_img5;
				aqiText = "重度污染";
			} else if (aqi > 150) {
				aqi_img = R.drawable.bg_img4;
				aqiText = "中度污染";
			} else if (aqi > 100) {
				aqi_img = R.drawable.bg_img3;
				aqiText = "轻度污染";
			} else if (aqi > 50) {
				aqi_img = R.drawable.bg_img2;
				aqiText = "良";
			} else if (aqi > 0) {
				aqi_img = R.drawable.bg_img1;
				aqiText = "优";
			}
			//

			Drawable drawable = getResources().getDrawable(aqi_img);
			titleimg.setBackground(drawable);
			titletxt.setText(cityname);
			// Log.v("_city", _city);
			String typestr = "";
			double[] historyval = null;
			String historytype = "";
			if ( pollutionType==MapMainNewActivity.POLLUTION_TYPE_AQI) {
				typestr = aqiText + "  " + "AQI: " + _aqi[_aqi.length - 1];
				historyval = _aqi;
				historytype = "AQI";

				typestr = aqiText + "  " + "AQI: " + aqi;
				historyval = _aqi;
				historytype = "AQI";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_PM2_5) {
				typestr = aqiText + "  " + "PM2.5: " + _pm25[_pm25.length - 1];
				historyval = _pm25;
				historytype = "PM2.5";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_PM10) {
				typestr = aqiText + "  " + "PM10: " + _pm10[_pm10.length - 1];
				historyval = _pm10;
				historytype = "PM10";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_SO2) {
				typestr = aqiText + "  " + "SO2: " + _so2[_so2.length - 1];
				historyval = _so2;
				historytype = "SO2";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_NO2) {
				typestr = aqiText + "  " + "NO2: " + _no2[_no2.length - 1];
				historyval = _no2;
				historytype = "NO2";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_CO) {
				typestr = aqiText + "  " + "CO: " + _co[_co.length - 1];
				historyval = _co;
				historytype = "CO";
			} else if (pollutionType==MapMainNewActivity.POLLUTION_TYPE_O3) {
				typestr = aqiText + "  " + "O3: " + _o3[_o3.length - 1];
				historyval = _o3;
				historytype = "O3";
			}
			/*if (_type == EnvTypeOnMapEnum.AQI) {
				typestr = aqiText + "  " + "AQI: " + _aqi[_aqi.length - 1];
				historyval = _aqi;
				historytype = "AQI";

				typestr = aqiText + "  " + "AQI: " + aqi;
				historyval = _aqi;
				historytype = "AQI";
			} else if (_type == EnvTypeOnMapEnum.PM25) {
				typestr = aqiText + "  " + "PM2.5: " + _pm25[_pm25.length - 1];
				historyval = _pm25;
				historytype = "PM2.5";
			} else if (_type == EnvTypeOnMapEnum.PM10) {
				typestr = aqiText + "  " + "PM10: " + _pm10[_pm10.length - 1];
				historyval = _pm10;
				historytype = "PM10";
			} else if (_type == EnvTypeOnMapEnum.SO2) {
				typestr = aqiText + "  " + "SO2: " + _so2[_so2.length - 1];
				historyval = _so2;
				historytype = "SO2";
			} else if (_type == EnvTypeOnMapEnum.NO2) {
				typestr = aqiText + "  " + "NO2: " + _no2[_no2.length - 1];
				historyval = _no2;
				historytype = "NO2";
			} else if (_type == EnvTypeOnMapEnum.CO) {
				typestr = aqiText + "  " + "CO: " + _co[_co.length - 1];
				historyval = _co;
				historytype = "CO";
			} else if (_type == EnvTypeOnMapEnum.O3) {
				typestr = aqiText + "  " + "O3: " + _o3[_o3.length - 1];
				historyval = _o3;
				historytype = "O3";
			}*/
			realvaltxt.setText(typestr);

			// 显示历史数据
			LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

			//TODO 此处报空 historyval==null
			for (int i = 0; i < historyval.length; i++) {
				int hh = gethourFromTimestr(_uptdate[i]);
				TextView tv = new TextView(_context);

				tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
				tv.setText(" " + _level[i] + " \n " + (int) historyval[i] + " \n " + hh + "h ");
				tv.setGravity(Gravity.CENTER);

				if (hh < 18 && hh > 6)// 白天
				{
					tv.setBackgroundColor(Color.parseColor("#D3D9FE"));
					tv.setTextColor(Color.parseColor("#7C7C7D"));
				} else {
					tv.setBackgroundColor(Color.parseColor("#B1B1B3"));
					tv.setTextColor(Color.parseColor("#ffffff"));
				}
				tv.setPadding(2, 0, 2, 0);
				tv.setAlpha((float) 1.0);
				historylayout.addView(tv, lp);
				View vv = new View(_context);
				vv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
				historylayout.addView(vv, lp);
				historylayout.refreshDrawableState();
				addrectview(aqichart, _aqi[i], 500,"6");
				addrectview(pm10chart, _pm10[i], 600,"0");
				addrectview(pm25chart, _pm25[i], 500,"1");
				addrectview(no2chart, _no2[i], 3840,"4");
				addrectview(so2chart, _so2[i], 800,"3");
				addrectview(cochart, _co[i], 150,"5");
				addrectview(o3chart, _o3[i], 800,"2");
			}
		}
	}

}
