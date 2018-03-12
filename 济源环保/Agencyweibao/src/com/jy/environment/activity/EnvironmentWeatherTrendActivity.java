package com.jy.environment.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LineGraph.OnPointClickedListener;
import com.echo.holographlibrary.LinePoint;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.Item;
import com.jy.environment.model.Life;
import com.jy.environment.model.WeatherInfo7;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

/**
 * 环境天气趋势界面
 * 
 * @author baiyuchuan
 * 
 */
@SuppressLint("NewApi")
public class EnvironmentWeatherTrendActivity extends ActivityBase implements
		OnScrollListener {
	LinearLayout ll;
	RelativeLayout forcastView;
	private ListView listView3;
	String currentCity;
	ImageView back7;
	ImageView fengche;
	private TextView cityName;
	private TextView aqi;
	private TextView number;
	private List<Item> items;
	private ListView weather_lv;
	List<WeatherInfo7> weatherLists;
	ProgressDialog prDialog;
	private WeiBaoApplication mApplication;
	TextView week1Text, week2Text, week3Text, week4Text, week5Text, week6Text;
	// 抽屉下的布局
	private SlidingDrawer slidingDrawer;
	private ImageView arrawImageView;
	private ListView life_exlv;
	private MyAdapter adapter = new MyAdapter();
	private boolean flag = false;
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;
	private DisplayMetrics metrics;
	private int screenHalfWidth;
	private int screenHalfheigh12;
	private int screenHalfheigh8;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
				prDialog.cancel();
				weather_lv.setAdapter(new WeatherListAdapter());

				week1Text.setText(weatherLists.get(0).getTodayTime());
				week2Text.setText(weatherLists.get(1).getTodayTime());
				week3Text.setText(weatherLists.get(2).getTodayTime());
				week4Text.setText(weatherLists.get(3).getTodayTime());
				week5Text.setText(weatherLists.get(4).getTodayTime());
				week6Text.setText(weatherLists.get(5).getTodayTime());
				drawBitmap();
				} catch (OutOfMemoryError e) {
					MyLog.e("weibao Exception", e);
				} catch (Exception e) {
					MyLog.e("weibao Exception", e);
				}
				break;
			case 1:
				// ListAdapter adapter2=new ListAdapter();
				// listView3.setAdapter(adapter2);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_weather_trend_activity);
		mApplication = WeiBaoApplication.getInstance();
		currentCity = WeiBaoApplication.getInstance().selectedCity;
		slidingDrawer = (SlidingDrawer) findViewById(R.id.myslidingDrawer);

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHalfWidth = metrics.widthPixels - 20;
		screenHalfheigh12 = metrics.heightPixels / 13;
		screenHalfheigh8 = metrics.heightPixels / 8;
		Log.i("bai", "screenHalfheigh8 :" + screenHalfheigh8);
		Log.i("bai", "screenHalfheigh12 :" + screenHalfheigh12);
		initView();
		life_exlv = (ListView) findViewById(R.id.life_exlv);
		arrawImageView = (ImageView) findViewById(R.id.handlebg);
		slidingDrawer
				.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() { // 打开抽屉

					@Override
					public void onDrawerOpened() {
						arrawImageView
								.setBackgroundResource(R.drawable.trend_down);
					}
				});
		slidingDrawer
				.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() { // 关闭抽屉

					@Override
					public void onDrawerClosed() {
						arrawImageView
								.setBackgroundResource(R.drawable.trend_up);
					}
				});
		getCityWeatherTask cityWeatherTask = new getCityWeatherTask();
		cityWeatherTask.execute();
		initWeatherInfo();
	}

	private int getWeatherIcon(String climate) {
		int weatherIcon = R.drawable.weather_icon_qingtian;
		String climateString = CommonUtil.getWeatherIconString(climate,0);
		if (mApplication.getWeatherIconMap().containsKey(climateString)) {
			weatherIcon = mApplication.getWeatherIconMap().get(climateString);
		}
		return weatherIcon;
	}
	private void drawBitmap() {
		try {
			String tempArray[] = weatherLists.get(0).getTemp().replace("℃", "")
					.split("~");
			int high = 0;
			int low = 0;
			Line l1 = new Line();

			LinePoint p1 = new LinePoint(); // 高温曲线
			LinePoint p2 = new LinePoint(); // 低温曲线
			p1.setX(1);

			p1.setY(high = Math.max(
					Math.max(
							Math.max(Integer.valueOf(tempArray[0]),
									Integer.valueOf(tempArray[1])),
							Integer.valueOf(tempArray[1])),
					Integer.valueOf(tempArray[1])));
			l1.addPoint(p1);
			Line l2 = new Line();
			p2.setX(1);

			p2.setY(low = Math.min(Integer.valueOf(tempArray[1]),
					Integer.valueOf(tempArray[0])));
			l2.addPoint(p2);

			String tempArray2[] = weatherLists.get(1).getTemp()
					.replace("℃", "").split("~");
			p1 = new LinePoint();
			p1.setX(3);

			p1.setY(Math.max(Integer.valueOf(tempArray2[0]),
					Integer.valueOf(tempArray2[1])));
			high = high > Math.max(
					Math.max(Integer.valueOf(tempArray2[0]),
							Integer.valueOf(tempArray2[1])),
					Integer.valueOf(tempArray2[1])) ? high : Math.max(
					Math.max(Integer.valueOf(tempArray2[0]),
							Integer.valueOf(tempArray2[1])),
					Integer.valueOf(tempArray2[1]));
			l1.addPoint(p1);
			p2 = new LinePoint();
			p2.setX(3);

			p2.setY(Math.min(Integer.valueOf(tempArray2[1]),
					Integer.valueOf(tempArray2[0])));
			l2.addPoint(p2);
			low = low < Math.min(Integer.valueOf(tempArray2[1]),
					Integer.valueOf(tempArray2[0])) ? low : Math.min(
					Integer.valueOf(tempArray2[1]),
					Integer.valueOf(tempArray2[0]));

			String tempArray3[] = weatherLists.get(2).getTemp()
					.replace("℃", "").split("~");
			p1 = new LinePoint();
			p1.setX(5);

			p1.setY(Math.max(
					Math.max(Integer.valueOf(tempArray3[0]),
							Integer.valueOf(tempArray3[1])),
					Integer.valueOf(tempArray3[1])));
			l1.addPoint(p1);
			high = high > Math.max(
					Math.max(Integer.valueOf(tempArray3[0]),
							Integer.valueOf(tempArray3[1])),
					Integer.valueOf(tempArray3[1])) ? high : Math.max(
					Math.max(Integer.valueOf(tempArray3[0]),
							Integer.valueOf(tempArray3[1])),
					Integer.valueOf(tempArray3[1]));
			p2 = new LinePoint();
			p2.setX(5);

			p2.setY(Math.min(Integer.valueOf(tempArray3[1]),
					Integer.valueOf(tempArray3[0])));
			l2.addPoint(p2);
			low = low < Math.min(Integer.valueOf(tempArray3[1]),
					Integer.valueOf(tempArray3[0])) ? low : Math.min(
					Integer.valueOf(tempArray3[1]),
					Integer.valueOf(tempArray3[0]));

			String tempArray4[] = weatherLists.get(3).getTemp()
					.replace("℃", "").split("~");
			p1 = new LinePoint();
			p1.setX(7);

			p1.setY(Math.max(
					Math.max(Integer.valueOf(tempArray4[0]),
							Integer.valueOf(tempArray4[1])),
					Integer.valueOf(tempArray4[1])));
			l1.addPoint(p1);
			high = high > Math.max(
					Math.max(Integer.valueOf(tempArray4[0]),
							Integer.valueOf(tempArray4[1])),
					Integer.valueOf(tempArray4[1])) ? high : Math.max(
					Math.max(Integer.valueOf(tempArray4[0]),
							Integer.valueOf(tempArray4[1])),
					Integer.valueOf(tempArray4[1]));
			p2 = new LinePoint();
			p2.setX(7);

			p2.setY(Math.min(Integer.valueOf(tempArray4[1]),
					Integer.valueOf(tempArray4[0])));
			l2.addPoint(p2);
			low = low < Math.min(Integer.valueOf(tempArray4[1]),
					Integer.valueOf(tempArray4[0])) ? low : Math.min(
					Integer.valueOf(tempArray4[1]),
					Integer.valueOf(tempArray4[0]));

			String tempArray5[] = weatherLists.get(4).getTemp()
					.replace("℃", "").split("~");
			p1 = new LinePoint();
			p1.setX(9);

			p1.setY(Math.max(
					Math.max(Integer.valueOf(tempArray5[0]),
							Integer.valueOf(tempArray5[1])),
					Integer.valueOf(tempArray5[1])));
			l1.addPoint(p1);
			high = high > Math.max(
					Math.max(Integer.valueOf(tempArray5[0]),
							Integer.valueOf(tempArray5[1])),
					Integer.valueOf(tempArray5[1])) ? high : Math.max(
					Math.max(Integer.valueOf(tempArray5[0]),
							Integer.valueOf(tempArray5[1])),
					Integer.valueOf(tempArray5[1]));
			p2 = new LinePoint();
			p2.setX(9);

			p2.setY(Math.min(Integer.valueOf(tempArray5[1]),
					Integer.valueOf(tempArray5[0])));
			l2.addPoint(p2);
			low = low < Math.min(Integer.valueOf(tempArray5[1]),
					Integer.valueOf(tempArray5[0])) ? low : Math.min(
					Integer.valueOf(tempArray5[1]),
					Integer.valueOf(tempArray5[0]));

			String tempArray6[] = weatherLists.get(5).getTemp()
					.replace("℃", "").split("~");
			p1 = new LinePoint();
			p1.setX(11);

			p1.setY(Math.max(
					Math.max(Integer.valueOf(tempArray6[0]),
							Integer.valueOf(tempArray6[1])),
					Integer.valueOf(tempArray6[1])));
			l1.addPoint(p1);
			high = high > Math.max(
					Math.max(Integer.valueOf(tempArray6[0]),
							Integer.valueOf(tempArray6[1])),
					Integer.valueOf(tempArray6[1])) ? high : Math.max(
					Math.max(Integer.valueOf(tempArray6[0]),
							Integer.valueOf(tempArray6[1])),
					Integer.valueOf(tempArray6[1]));
			p2 = new LinePoint();
			p2.setX(11);

			p2.setY(Math.min(Integer.valueOf(tempArray6[1]),
					Integer.valueOf(tempArray6[0])));
			l2.addPoint(p2);
			low = low < Math.min(Integer.valueOf(tempArray6[1]),
					Integer.valueOf(tempArray6[0])) ? low : Math.min(
					Integer.valueOf(tempArray6[1]),
					Integer.valueOf(tempArray6[0]));

			l1.setColor(Color.parseColor("#FFFF00"));

			LineGraph li1 = (LineGraph) forcastView
					.findViewById(R.id.linegraph1);
			li1.addLine(l1);
			li1.setRangeY(low - 5, high + 3);

			li1.setOnPointClickedListener(new OnPointClickedListener() {
				@Override
				public void onClick(int lineIndex, int pointIndex) {
				}
			});

			l2.setColor(Color.parseColor("#ffffff"));

			LineGraph li2 = (LineGraph) forcastView
					.findViewById(R.id.linegraph1);
			li2.addLine(l2);
			li2.setRangeY(low - 5, high + 3);

			li2.setOnPointClickedListener(new OnPointClickedListener() {
				@Override
				public void onClick(int lineIndex, int pointIndex) {
				}
			});
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
	}

	class MyAdapter extends BaseAdapter {
		List<Life> list;
		// 各个指数前面的图标
		int[] icon = new int[] { R.drawable.icon_guide_cy,
				R.drawable.icon_guide_cl, R.drawable.icon_guide_uv,
				R.drawable.icon_guide_tr, R.drawable.icon_guide_co,
				R.drawable.icon_guide_zs };

		void bindData(List<Life> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			try {
				MyLog.i("weibao result:" + list.get(position).toString());
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(
						EnvironmentWeatherTrendActivity.this).inflate(
						R.layout.environment_weathertrend_life_item, null);
				// holder = new ViewHolder();
				holder.life_activity_item_iv1 = (ImageView) convertView
						.findViewById(R.id.life_activity_item_iv1);
				holder.life_activity_item_iv2 = (ImageView) convertView
						.findViewById(R.id.life_activity_item_iv2);
				holder.life_activity_item_tv1 = (TextView) convertView
						.findViewById(R.id.life_activity_item_tv1);
				holder.life_activity_item_tv2 = (TextView) convertView
						.findViewById(R.id.life_activity_item_tv2);
				holder.life_activity_item_tv3 = (TextView) convertView
						.findViewById(R.id.life_activity_item_tv3);
				holder.life_ll = (LinearLayout) convertView
						.findViewById(R.id.life_ll);
				holder.height = (ImageView) convertView
						.findViewById(R.id.height);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Life life = list.get(position);
			holder.life_activity_item_tv2.setText(life.getIndex_tv0());
			holder.life_activity_item_tv1.setText(life.getIndex_tv1());
			holder.life_activity_item_tv3.setText(life.getIndex_tv2());
			final LinearLayout re = holder.life_ll;
			final TextView t1 = holder.life_activity_item_tv3;
			final ImageView iv1 = holder.life_activity_item_iv1;
			final ImageView life_activity_item_iv2 = holder.life_activity_item_iv2;
			final TextView life_activity_item_tv1 = holder.life_activity_item_tv1;
			final TextView life_activity_item_tv2 = holder.life_activity_item_tv2;
			final TextView life_activity_item_tv3 = holder.life_activity_item_tv3;
			final ImageView height = holder.height;

			LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) height
					.getLayoutParams();
			height.getLayoutParams();
			lParamsheight.width = 1;
			lParamsheight.height = screenHalfheigh12;
			height.setLayoutParams(lParamsheight);

			LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) iv1
					.getLayoutParams();
			iv1.getLayoutParams();
			lParams.width = screenHalfheigh12;
			lParams.height = screenHalfheigh12;
			t1.setText(life.getIndex_tv2());
			iv1.setLayoutParams(lParams);
			iv1.setImageDrawable(getResources().getDrawable(icon[position]));
			re.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (t1.getVisibility() == View.GONE) {
						t1.setVisibility(View.VISIBLE);
						int height_in_pixels = t1.getLineHeight(); // approx
						// height
						// text
						LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) iv1
								.getLayoutParams();
						iv1.getLayoutParams();

						lParams.width = screenHalfheigh8;
						lParams.height = screenHalfheigh8;

						if (t1.getText().length() > 30) {
							int times = (t1.getText().length() / 15 + 1);
							t1.setHeight(height_in_pixels * times);
						}
						iv1.setLayoutParams(lParams);

						LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) height
								.getLayoutParams();
						height.getLayoutParams();
						lParamsheight.width = 1;
						lParamsheight.height = screenHalfheigh12;
						if (t1.getText().length() > 30) {
							lParamsheight.height = (screenHalfheigh12 + (height_in_pixels * 3));
						}
						height.setLayoutParams(lParamsheight);
						life_activity_item_iv2
								.setBackgroundResource(R.drawable.life_up);
					} else {
						LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) iv1
								.getLayoutParams();
						iv1.getLayoutParams();
						lParams.width = screenHalfheigh12;
						lParams.height = screenHalfheigh12;
						iv1.setLayoutParams(lParams);
						LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) height
								.getLayoutParams();
						height.getLayoutParams();
						lParamsheight.width = 1;
						lParamsheight.height = screenHalfheigh12;
						height.setLayoutParams(lParamsheight);
						life_activity_item_iv2
								.setBackgroundResource(R.drawable.life_down);
						t1.setVisibility(View.GONE);
					}
				}
			});
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return convertView;
		}

		class ViewHolder {
			LinearLayout life_ll;
			ImageView life_activity_item_iv1;
			ImageView life_activity_item_iv2;
			TextView life_activity_item_tv1;
			TextView life_activity_item_tv2;
			TextView life_activity_item_tv3;
			ImageView height;
		}
	}

	private void initView() {
		back7 = (ImageView) findViewById(R.id.goback);
		back7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView3 = (ListView) findViewById(R.id.listView);

		items = new ArrayList<Item>();
		weatherLists = new ArrayList<WeatherInfo7>();
		prDialog = new ProgressDialog(EnvironmentWeatherTrendActivity.this);
		prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prDialog.setMessage("正在努力加载中……");

		// 进度条是否不明确
		prDialog.setIndeterminate(true);
		prDialog.setCancelable(true);

		weather_lv = (ListView) findViewById(R.id.weather_lv);
		forcastView = (RelativeLayout) findViewById(R.id.cell1);

		week1Text = (TextView) forcastView.findViewById(R.id.week1Text);
		week2Text = (TextView) forcastView.findViewById(R.id.week2Text);
		week3Text = (TextView) forcastView.findViewById(R.id.week3Text);
		week4Text = (TextView) forcastView.findViewById(R.id.week4Text);
		week5Text = (TextView) forcastView.findViewById(R.id.week5Text);
		week6Text = (TextView) forcastView.findViewById(R.id.week6Text);

	}

	public class WeatherListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return weatherLists.size();
		}

		@Override
		public Object getItem(int position) {
			return weatherLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(
						EnvironmentWeatherTrendActivity.this).inflate(
						R.layout.environment_weather_trend_forecast_item, null);
				holder = new ViewHolder();
				holder.dayinfo = (TextView) convertView
						.findViewById(R.id.dayinfo);
				holder.tempinfo = (TextView) convertView
						.findViewById(R.id.tempinfo);
				holder.weather = (TextView) convertView
						.findViewById(R.id.tq_info);
				holder.wind = (TextView) convertView.findViewById(R.id.wind);
				holder.dayWeatherIcon = (ImageView) convertView
						.findViewById(R.id.weatherimage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.dayinfo.setText(weatherLists.get(position).getTodayTime());
			holder.tempinfo.setText(weatherLists.get(position).getTemp());

			if (null != weatherLists.get(position).getWeather()) {
				String weather = weatherLists.get(position).getWeather();
				if (weather.contains(",")) {
					weather = weather.substring(0, weather.indexOf(","));
				}
				if (weather.contains("、")) {
					weather = weather.substring(0, weather.indexOf("、"));
				}
				holder.weather.setText(weather);
			} else {
				holder.weather.setText(weatherLists.get(position).getWeather());
			}
			holder.wind.setText(weatherLists.get(position).getWind());
			holder.dayWeatherIcon.setImageResource(getWeatherIcon(weatherLists
					.get(position).getWeather()));
			return convertView;
		}

		class ViewHolder {
			TextView dayinfo, tempinfo, weather, wind;
			ImageView dayWeatherIcon;

		}
	}

	private void initWeatherInfo() {
		prDialog.show();
		GetWeatherInfoTask getWeatherInfoTask = new GetWeatherInfoTask();
		getWeatherInfoTask.execute();

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

	public void onScrollStateChange(NumberPicker view, int scrollState) {
	}

	public class GetWeatherInfoTask extends
			AsyncTask<String, Void, List<WeatherInfo7>> {

		@Override
		protected List<WeatherInfo7> doInBackground(String... params) {
			String url = UrlComponent.getDetailWeatherBycity_Get(currentCity);
			;
			BusinessSearch search = new BusinessSearch();
			List<WeatherInfo7> _Result = null;
			try {
				_Result = search.getWeatherInfo(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(List<WeatherInfo7> result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result:" + result);
			if (null != result) {
				weatherLists = result;
				mHandler.sendEmptyMessage(0);
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	class getCityWeatherTask extends AsyncTask<String, Void, List<Life>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Life> doInBackground(String... params) {
			String url = UrlComponent.getDetailBycity_Get(currentCity);
			BusinessSearch search = new BusinessSearch();
			List<Life> _Result = null;
			try {
				_Result = search.getCityWeather(url, 3600);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(List<Life> result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result:" + result);
			if (null == result) {
				Toast.makeText(EnvironmentWeatherTrendActivity.this,
						"网络出问题,请检查网络设置", 0).show();
				;
			} else {
				adapter.bindData(result);
				life_exlv.setAdapter(adapter);
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}
	}

}
