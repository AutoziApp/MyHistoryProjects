package com.jy.environment.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.SQLiteDALModelNoiseHistory;
import com.jy.environment.database.model.ModelNoiseHistory;
import com.jy.environment.model.NoiseHistoryModel;
import com.jy.environment.model.NoiseItemModel;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentNoiseHistoryActivity extends Activity implements
		OnClickListener, OnRefreshListener<ListView> {

	private ImageView back;
	private ListView listView_maxmin;
	private PullToRefreshListView listView;
	private SQLiteDALModelNoiseHistory mSqLiteDALModelNoiseHistory;
	private List<ModelNoiseHistory> list;
	private List<ModelNoiseHistory> listmaxmin;
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private ProgressDialog prDialog;
	SimpleAdapter mSimpleAdapter;
	private MyListAdapter mAdapter;
	private MyMaxminAdapter maxminAdapter;
	private Context mContext;
	private ImageView noise_history_much;
	private View moreView;
	private int lastItem;
	private int count;
	private int i = 0;
	private int userid;
	private String useridString;
	private WeiBaoApplication mApplication;
	private int page = 0;
	private ImageView noise_phone, noise_yun;
	private ArrayList<HashMap<String, Object>> listItem;
	private ArrayList<HashMap<String, Object>> listItem_maxmin;
	private String hasmaxmin;
	public static final String ACTION_NAME = "CLAER_DATA";
	private String dataCount = "0";
	private TextView noise_DataTextView;
	private MapView noise_history_mapView;
	private Handler mapHandler;
	private RelativeLayout noise_history_maplayout;
	private List<ModelNoiseHistory> list_map;
	private String tag = "first_no";
	private RelativeLayout layout_nothing, layout_list;
	private ArrayList<HashMap<String, Object>> listItem_maxmin_DB = new ArrayList<HashMap<String, Object>>();
	private int datacount_upload = 0;
	private int j;
	private String tag_other = "other";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noise_history);
		registerBoradcastReceiver();
		initView();
		initListener();
		// 只支持上拉加载
		listView.setMode(Mode.PULL_UP_TO_REFRESH);
		MapStatus ms = new MapStatus.Builder().zoom(12).build();
		noise_history_mapView.getMap().setMapStatus(
				MapStatusUpdateFactory.newMapStatus(ms));
		/**
		 * 设置地图缩放级别
		 */
		noise_history_mapView.getMap().setMapStatus(
				MapStatusUpdateFactory.zoomTo(11));
		/**
		 * 显示内置缩放控件
		 */
		noise_history_mapView.showZoomControls(false);
		mApplication = WeiBaoApplication.getInstance();
		useridString = mApplication.getUserId();
		if (!useridString.equals("")) {
			userid = Integer.parseInt(useridString);
		}
		moreView = getLayoutInflater().inflate(R.layout.noise_history_load,
				null);
		mSqLiteDALModelNoiseHistory = new SQLiteDALModelNoiseHistory(
				ModelNoiseHistory.class);
		SearchHistoryTask searchHistory = new SearchHistoryTask();
		searchHistory.execute();
		mapHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 3:
					 try {
						
					List<ModelNoiseHistory> points = (List<ModelNoiseHistory>) msg.obj;
					MyLog.i("weibao result:" + points);
					// 设定地图中心
					LatLng cenpt = new LatLng(Double.parseDouble(points.get(0)
							.getLatitude()), Double.parseDouble(points.get(0)
							.getLongitude()));
					// 定义地图状态
					MapStatus mMapStatus = new MapStatus.Builder()
							.target(cenpt).zoom(18).build();
					// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
					MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
							.newMapStatus(mMapStatus);
					// 改变地图状态
					if (noise_history_mapView == null) {
						return;
					}
					noise_history_mapView.getMap().setMapStatus(
							mMapStatusUpdate);
					for (int i = 0; i < points.size(); i++) {
						ModelNoiseHistory aqipt = points.get(i);
						Drawable aqimaker = getResources().getDrawable(
								getAQIMarker(aqipt));
						Drawable aqimakertext = null;
						aqimakertext = aqimaker;

						// 定义Maker坐标点
						LatLng point = new LatLng(Double.parseDouble(aqipt
								.getLatitude()), Double.parseDouble(aqipt
								.getLongitude()));
						// 构建Marker图标
						BitmapDescriptor bitmap = BitmapDescriptorFactory
								.fromBitmap(((BitmapDrawable) aqimakertext)
										.getBitmap());
						// 构建MarkerOption，用于在地图上添加Marker
						OverlayOptions option = new MarkerOptions().position(
								point).icon(bitmap);
						// 在地图上添加Marker，并显示
						noise_history_mapView.getMap().addOverlay(option);
					}
					
					 } catch (Exception e) {
							MyLog.e("weibao Exception", e);
						}
					break;

				default:
					break;
				}
			}
		};

	}

	private void initView() {
		back = (ImageView) findViewById(R.id.noise_history_back);
		listView = (PullToRefreshListView) findViewById(R.id.noise_history_list);
		listView_maxmin = (ListView) findViewById(R.id.noise_history_list_maxmin);
		noise_history_much = (ImageView) findViewById(R.id.noise_history_much);
		noise_DataTextView = (TextView) findViewById(R.id.noise_history_datacount);
		noise_history_mapView = (MapView) findViewById(R.id.noise_history_bmapsView);
		noise_history_maplayout = (RelativeLayout) findViewById(R.id.noise_history_maplayout);
		layout_nothing = (RelativeLayout) findViewById(R.id.noise_history_nothing_layout);
		layout_list = (RelativeLayout) findViewById(R.id.noise_history_layout_list);
		ListView mListView = listView.getRefreshableView();
	}

	private void initListener() {
		back.setOnClickListener(this);
		noise_history_much.setOnClickListener(this);
		listView.setOnRefreshListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.noise_history_back:
			finish();
			break;
		case R.id.noise_history_much:
			MobclickAgent.onEvent(EnvironmentNoiseHistoryActivity.this,
					"HJOpenNoiseSetPanel");
			Intent intent = new Intent(EnvironmentNoiseHistoryActivity.this,
					EnvironmentNoiseMuchActivity.class);
			intent.putExtra("dataCount", dataCount);
			startActivity(intent);
			break;
		}

	}

	private class SearchCountTask extends AsyncTask<Void, Void, String> {
		List<ModelNoiseHistory> list_count = new ArrayList<ModelNoiseHistory>();

		@Override
		protected String doInBackground(Void... params) {
			list_count = mSqLiteDALModelNoiseHistory.selectAllHistoryupload();
			if (list != null) {
				dataCount = list.size() + "";
			} else {
				dataCount = "0";
			}
			return dataCount;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				MyLog.i("weibao result:" + result);
			if (!dataCount.equals("0")) {
				noise_DataTextView.setText(dataCount);
				noise_DataTextView.setVisibility(View.VISIBLE);
			} else {
				noise_DataTextView.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	private class SearchHistoryTask extends
			AsyncTask<Void, Void, HashMap<String, Object>> {
		List<ModelNoiseHistory> list_count = new ArrayList<ModelNoiseHistory>();

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(EnvironmentNoiseHistoryActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("噪声历史数据加载中···");
			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(Void... params) {
			try {
			// 获取当前时间
			list_count = mSqLiteDALModelNoiseHistory.selectAllHistoryupload();
			if (list_count != null) {
				MyLog.i(">>>>>>>size" + list_count.size());
				dataCount = list_count.size() + "";
			} else {
				MyLog.i(">>>>>>>size" + "1");
				dataCount = "0";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = sdf.format(new Date());
			// 获取七天前的时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			String seventime = sdf.format(calendar.getTime());
			/**
			 * 定义一个空数据 用于显示地图时做判断
			 */
			ModelNoiseHistory modelNoiseHistory = new ModelNoiseHistory();
			modelNoiseHistory.setIsupload("2");
			modelNoiseHistory.setmResult("null");
			modelNoiseHistory.setLocation("null");
			modelNoiseHistory.setTime("null");
			listmaxmin = mSqLiteDALModelNoiseHistory.selectMaxMin(nowtime,
					seventime);
			list = mSqLiteDALModelNoiseHistory.selectAllHistory();
			// HashSet<ModelNoiseHistory> h = new
			// HashSet<ModelNoiseHistory>(list);
			// list.clear();
			// list.addAll(h);
			if (listmaxmin != null
					&& listmaxmin.size() > 1
					&& !listmaxmin
							.get(0)
							.getmResult()
							.equals(listmaxmin.get(listmaxmin.size() - 1)
									.getmResult())) {
				list.add(0, listmaxmin.get(0));
				// 目前无法实现用sql对结果的去重，只能使用取list里面的第一个和最后一个方法获得最小值和最大值
				list.add(1, listmaxmin.get(listmaxmin.size() - 1));
				list.add(2, modelNoiseHistory);
				map.put("hasmaxmin", "yes");
				map.put("mlist", list);
			} else {
				map.put("hasmaxmin", "no");
				if (list != null) {
					list.add(0, modelNoiseHistory);
				}
				map.put("mlist", list);
			}
			map.put("dataCount", dataCount);
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return map;
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			 try {
					MyLog.i("weibao result:" + result);
			// 定义一个动态数组
			list_map = new ArrayList<ModelNoiseHistory>();
			listItem = new ArrayList<HashMap<String, Object>>();
			listItem_maxmin = new ArrayList<HashMap<String, Object>>();
			// 在数组中存放数据
			hasmaxmin = (String) result.get("hasmaxmin");
			@SuppressWarnings("unchecked")
			List<ModelNoiseHistory> list = (List<ModelNoiseHistory>) result
					.get("mlist");
			// listView.getEmptyView().setVisibility(View.VISIBLE);
			if (list != null) {
				noise_history_maplayout.setVisibility(View.VISIBLE);
				if (hasmaxmin.equals("yes")) {
					for (int i = 0; i < 2; i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("noise_text", list.get(i).getmResult());
						map.put("noise_address", list.get(i).getLocation());
						map.put("noise_time", list.get(i).getTime());
						map.put("isupload", list.get(i).getIsupload());
						listItem_maxmin.add(map);
						listItem_maxmin_DB.add(map);
					}
					for (int i = 3; i < list.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("noise_text", list.get(i).getmResult());
						map.put("noise_address", list.get(i).getLocation());
						map.put("noise_time", list.get(i).getTime());
						map.put("isupload", list.get(i).getIsupload());
						listItem.add(map);
						list_map.add(list.get(i));
					}
				} else if (hasmaxmin.equals("no")) {
					for (int i = 1; i < list.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("noise_text", list.get(i).getmResult());
						map.put("noise_address", list.get(i).getLocation());
						map.put("noise_time", list.get(i).getTime());
						map.put("isupload", list.get(i).getIsupload());
						listItem.add(map);
						list_map.add(list.get(i));
					}
				}
				maxminAdapter = new MyMaxminAdapter();
				maxminAdapter.bindData(listItem_maxmin, hasmaxmin,
						EnvironmentNoiseHistoryActivity.this);
				listView_maxmin.setAdapter(maxminAdapter);
				mAdapter = new MyListAdapter();
				mAdapter.bindData(listItem, hasmaxmin,
						EnvironmentNoiseHistoryActivity.this);
				listView.setAdapter(mAdapter);
				dataCount = (String) result.get("dataCount");
				if (dataCount.equals("0")) {
					noise_DataTextView.setVisibility(View.GONE);
				} else {
					noise_DataTextView.setText(dataCount);
					noise_DataTextView.setVisibility(View.VISIBLE);
				}
				Message msg = Message.obtain();
				msg.what = 3;
				msg.obj = list_map;
				mapHandler.sendMessage(msg);
				prDialog.cancel();
			} else if (!useridString.equals("")) {
				// 没有数据，登陆
				LoadTask load = new LoadTask();
				load.execute(0);
				prDialog.cancel();
			} else {
				layout_nothing.setVisibility(View.VISIBLE);
				layout_list.setVisibility(View.GONE);
				prDialog.cancel();
				noise_history_maplayout.setVisibility(View.GONE);
				return;
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	class MyMaxminAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> listItem_maxmin;
		private Activity activity;
		private String hasmaxmin;

		void bindData(ArrayList<HashMap<String, Object>> listItem,
				String hasmaxmin, Activity activity) {
			this.hasmaxmin = hasmaxmin;
			this.listItem_maxmin = listItem;
			this.activity = activity;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		public Object getItem(int position) {
			return listItem_maxmin.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			 try {
			TextView noise_text = null;
			TextView noise_address = null;
			TextView noise_time = null;
			TextView noise_text_max = null;
			TextView noise_text_min = null;
			LinearLayout noise_layout_max = null;
			LinearLayout noise_layout_min = null;
			ImageView noise_phone_view = null;
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.noise_history_item, null);
			noise_text = (TextView) convertView
					.findViewById(R.id.noise_history_text);
			noise_address = (TextView) convertView
					.findViewById(R.id.noise_history_address);
			noise_time = (TextView) convertView
					.findViewById(R.id.noise_history_time);
			noise_layout_max = (LinearLayout) convertView
					.findViewById(R.id.noise_layout_max);
			noise_layout_min = (LinearLayout) convertView
					.findViewById(R.id.noise_layout_min);
			noise_text_max = (TextView) convertView
					.findViewById(R.id.noise_history_text_max);
			noise_text_min = (TextView) convertView
					.findViewById(R.id.noise_history_text_min);
			noise_phone_view = (ImageView) convertView
					.findViewById(R.id.noise_phone_view);
			noise_phone_view.setVisibility(View.INVISIBLE);
			noise_text.setVisibility(View.VISIBLE);
			noise_layout_max.setVisibility(View.GONE);
			noise_layout_min.setVisibility(View.GONE);
			if (position == 0) {
				noise_text.setVisibility(View.INVISIBLE);
				noise_layout_min.setVisibility(View.VISIBLE);
				noise_text_min.setText(listItem_maxmin.get(position)
						.get("noise_text").toString());
				noise_address.setText(listItem_maxmin.get(position)
						.get("noise_address").toString());
				noise_time.setText(listItem_maxmin.get(position)
						.get("noise_time").toString());
			} else if (position == 1) {
				noise_text.setVisibility(View.INVISIBLE);
				noise_layout_max.setVisibility(View.VISIBLE);
				noise_text_max.setText(listItem_maxmin.get(position)
						.get("noise_text").toString());
				noise_address.setText(listItem_maxmin.get(position)
						.get("noise_address").toString());
				noise_time.setText(listItem_maxmin.get(position)
						.get("noise_time").toString());
			}
			int noise_index = Integer.parseInt(listItem_maxmin.get(position)
					.get("noise_text").toString());
			if (noise_index <= 40) {
				noise_text.setTextColor(Color.parseColor("#50fe00"));
				noise_layout_max.setBackgroundResource(R.drawable.m_1);
				noise_layout_min.setBackgroundResource(R.drawable.m_1);
			} else if (noise_index > 40 && noise_index <= 45) {
				noise_text.setTextColor(Color.parseColor("#80ff00"));
				noise_layout_max.setBackgroundResource(R.drawable.m_2);
				noise_layout_min.setBackgroundResource(R.drawable.m_2);
			} else if (noise_index > 45 && noise_index <= 50) {
				noise_text.setTextColor(Color.parseColor("#Adff00"));
				noise_layout_max.setBackgroundResource(R.drawable.m_3);
				noise_layout_min.setBackgroundResource(R.drawable.m_3);
			} else if (noise_index > 50 && noise_index <= 55) {
				noise_text.setTextColor(Color.parseColor("#Ffff00"));
				noise_layout_max.setBackgroundResource(R.drawable.m_4);
				noise_layout_min.setBackgroundResource(R.drawable.m_4);
			} else if (noise_index > 55 && noise_index <= 60) {
				noise_text.setTextColor(Color.parseColor("#ffd300"));
				noise_layout_max.setBackgroundResource(R.drawable.m_5);
				noise_layout_min.setBackgroundResource(R.drawable.m_5);
			} else if (noise_index > 60 && noise_index <= 65) {
				noise_text.setTextColor(Color.parseColor("#ffa600"));
				noise_layout_max.setBackgroundResource(R.drawable.m_6);
				noise_layout_min.setBackgroundResource(R.drawable.m_6);
			} else if (noise_index > 65 && noise_index <= 70) {
				noise_text.setTextColor(Color.parseColor("#ff7400"));
				noise_layout_max.setBackgroundResource(R.drawable.m_7);
				noise_layout_min.setBackgroundResource(R.drawable.m_7);
			} else if (noise_index > 70 && noise_index <= 75) {
				noise_text.setTextColor(Color.parseColor("#ff4400"));
				noise_layout_max.setBackgroundResource(R.drawable.m_8);
				noise_layout_min.setBackgroundResource(R.drawable.m_8);
			} else if (noise_index > 75 && noise_index <= 80) {
				noise_text.setTextColor(Color.parseColor("#fb1500"));
				noise_layout_max.setBackgroundResource(R.drawable.m_9);
				noise_layout_min.setBackgroundResource(R.drawable.m_9);
			} else if (noise_index >= 80) {
				noise_text.setTextColor(Color.parseColor("#fe0200"));
				noise_layout_max.setBackgroundResource(R.drawable.m_10);
				noise_layout_min.setBackgroundResource(R.drawable.m_10);
			}
			noise_text.setText(listItem_maxmin.get(position).get("noise_text")
					.toString());
			noise_address.setText(listItem_maxmin.get(position)
					.get("noise_address").toString());
			noise_time.setText(listItem_maxmin.get(position).get("noise_time")
					.toString());
			 } catch (Exception e) {
					MyLog.e("weibao Exception", e);
				}
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItem_maxmin.size();
		}

	}

	class MyListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> listItem;
		private Activity activity;
		private String hasmaxmin;

		void bindData(ArrayList<HashMap<String, Object>> listItem,
				String hasmaxmin, Activity activity) {
			this.hasmaxmin = hasmaxmin;
			this.listItem = listItem;
			this.activity = activity;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		public Object getItem(int position) {
			return listItem.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			try {
			TextView noise_text = null;
			TextView noise_address = null;
			TextView noise_time = null;
			TextView noise_text_max = null;
			TextView noise_text_min = null;
			LinearLayout noise_layout_max = null;
			LinearLayout noise_layout_min = null;
			count = listItem.size();
			/* if (hasmaxmin.equals("other")) { */
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.noise_history_item, null);
			noise_text = (TextView) convertView
					.findViewById(R.id.noise_history_text);
			noise_address = (TextView) convertView
					.findViewById(R.id.noise_history_address);
			noise_time = (TextView) convertView
					.findViewById(R.id.noise_history_time);
			noise_phone = (ImageView) convertView
					.findViewById(R.id.noise_phone_view);
			noise_yun = (ImageView) convertView
					.findViewById(R.id.noise_yun_view);
			int noise_index = Integer.parseInt(listItem.get(position)
					.get("noise_text").toString());
			if (noise_index <= 40) {
				noise_text.setTextColor(Color.parseColor("#50fe00"));
			} else if (noise_index > 40 && noise_index <= 45) {
				noise_text.setTextColor(Color.parseColor("#80ff00"));
			} else if (noise_index > 45 && noise_index <= 50) {
				noise_text.setTextColor(Color.parseColor("#Adff00"));
			} else if (noise_index > 50 && noise_index <= 55) {
				noise_text.setTextColor(Color.parseColor("#Ffff00"));
			} else if (noise_index > 55 && noise_index <= 60) {
				noise_text.setTextColor(Color.parseColor("#ffd300"));
			} else if (noise_index > 60 && noise_index <= 65) {
				noise_text.setTextColor(Color.parseColor("#ffa600"));
			} else if (noise_index > 65 && noise_index <= 70) {
				noise_text.setTextColor(Color.parseColor("#ff7400"));
			} else if (noise_index > 70 && noise_index <= 75) {
				noise_text.setTextColor(Color.parseColor("#ff4400"));
			} else if (noise_index > 75 && noise_index <= 80) {
				noise_text.setTextColor(Color.parseColor("#fb1500"));
			} else if (noise_index >= 80) {
				noise_text.setTextColor(Color.parseColor("#fe0200"));
			}
			if (listItem.get(position).get("isupload").toString().equals("1")) {
				noise_phone.setVisibility(View.GONE);
				noise_yun.setVisibility(View.VISIBLE);
			}
			noise_text.setText(listItem.get(position).get("noise_text")
					.toString());
			noise_address.setText(listItem.get(position).get("noise_address")
					.toString());
			noise_time.setText(listItem.get(position).get("noise_time")
					.toString());
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItem.size();
		}
	}

	// 声明Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
				/*
				 * loadMoreData(); //加载更多数据，这里可以使用异步加载
				 * adapter.notifyDataSetChanged();
				 */
				i = i + 1;
				if (mApplication.getTag_page() != 0) {
					page = mApplication.getTag_page() + 1;
				}
				LoadTask load = new LoadTask();
				load.execute(i);
				} catch (Exception e) {
					MyLog.e("weibao Exception", e);
				}
				break;
			case 1:
				moreView.setVisibility(View.GONE);
				break;
			case 2:
				listItem.clear();
				listItem_maxmin.clear();
				layout_nothing.setVisibility(View.VISIBLE);
				layout_list.setVisibility(View.GONE);
				noise_history_maplayout.setVisibility(View.GONE);
				noise_DataTextView.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
				maxminAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	private class LoadTask extends
			AsyncTask<Integer, Void, HashMap<String, Object>> {
		List<ModelNoiseHistory> list = new ArrayList<ModelNoiseHistory>();
		List<ModelNoiseHistory> list2 = new ArrayList<ModelNoiseHistory>();
		List<ModelNoiseHistory> list_maxmin_DB = new ArrayList<ModelNoiseHistory>();

		@Override
		protected HashMap<String, Object> doInBackground(Integer... params) {
			 try {
			map = new HashMap<String, Object>();
			// j ==1 本地有数据，并且登录了
			j = params[0];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = sdf.format(new Date());
			// 获取七天前的时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			String seventime = sdf.format(calendar.getTime());
			useridString = mApplication.getUserId();
			list = mSqLiteDALModelNoiseHistory.selectAllHistoryBypage(j);
			List<ModelNoiseHistory> list_all = mSqLiteDALModelNoiseHistory
					.selectAllHistoryMuch();
			if (list_all != null) {
				datacount_upload = list_all.size();
			} else {
				datacount_upload = 0;
			}
			// 本地无数据，登陆（取数据库中的数据）
			if (list == null && !useridString.equals("")) {
				page = page + 1;
				userid = Integer.parseInt(useridString);
				String url = UrlComponent.getNoiseHistory(userid, page);
				MyLog.i(">>>>>>url" + url);
				BusinessSearch search = new BusinessSearch();
				NoiseHistoryModel _Result = null;
				_Result = search.getNoiseHistory(url);
				if (_Result == null)
					return null;
				if (_Result.getFlag().equals("false")
						|| _Result.getList().size() == 0) {
					// 服务器上也没有数据 使用hasmaxmn == other作为标示
					if (j == 0) {
						map.put("hasmaxmin", "other");
						map.put("mlist", list);
					} else {
						map.put("hasmaxmin", "other_j");
						map.put("mlist", list);
					}
				} else {
					List<NoiseItemModel> itemModels = _Result.getList();
					// 从服务器上取到数据了
					mApplication.setTag_page(page);
					list = new ArrayList<ModelNoiseHistory>();

					for (int i = 0; i < itemModels.size(); i++) {
						ModelNoiseHistory modelNoiseHistory = new ModelNoiseHistory();
						modelNoiseHistory.setIsupload("1");
						modelNoiseHistory.setmResult(itemModels.get(i)
								.getValue());
						modelNoiseHistory.setLocation(itemModels.get(i)
								.getAddress());
						modelNoiseHistory.setTime(itemModels.get(i)
								.getUpdate_time());
						modelNoiseHistory.setLatitude(itemModels.get(i)
								.getLatitude());
						modelNoiseHistory.setLongitude(itemModels.get(i)
								.getLongitude());
						modelNoiseHistory.setUserId(WeiBaoApplication.getUserId());
						list.add(modelNoiseHistory);
					}
					mSqLiteDALModelNoiseHistory.insert(list);
					ModelNoiseHistory modelNoiseHistory = new ModelNoiseHistory();
					modelNoiseHistory.setIsupload("2");
					modelNoiseHistory.setmResult("null");
					modelNoiseHistory.setLocation("null");
					modelNoiseHistory.setTime("null");
					list_maxmin_DB = mSqLiteDALModelNoiseHistory.selectMaxMin(
							nowtime, seventime);
					if (list_maxmin_DB != null
							&& list_maxmin_DB.size() > 1
							&& !list_maxmin_DB
									.get(0)
									.getmResult()
									.equals(list_maxmin_DB.get(
											list_maxmin_DB.size() - 1)
											.getmResult())) {
						list.add(0, list_maxmin_DB.get(0));
						// 目前无法实现用sql对结果的去重，只能使用取list里面的第一个和最后一个方法获得最小值和最大值
						list.add(1,
								list_maxmin_DB.get(list_maxmin_DB.size() - 1));
						list.add(2, modelNoiseHistory);
						map.put("hasmaxmin", "yes");
						map.put("mlist", list);
					} else {
						map.put("hasmaxmin", "no");
						if (list != null) {
							list.add(0, modelNoiseHistory);
						}
						map.put("mlist", list);
					}
					map.put("dataCount", dataCount);
				}
			} else {
				ModelNoiseHistory modelNoiseHistory = new ModelNoiseHistory();
				modelNoiseHistory.setIsupload("2");
				modelNoiseHistory.setmResult("null");
				modelNoiseHistory.setLocation("null");
				modelNoiseHistory.setTime("null");
				// 此时list不为空
				if (j == 0) {
					// 登陆，本地有数据，服务器没数据
					listmaxmin = mSqLiteDALModelNoiseHistory.selectMaxMin(
							nowtime, seventime);
					if (listmaxmin != null
							&& listmaxmin.size() > 1
							&& !listmaxmin
									.get(0)
									.getmResult()
									.equals(listmaxmin.get(
											listmaxmin.size() - 1).getmResult())) {
						list.add(0, listmaxmin.get(0));
						// 目前无法实现用sql对结果的去重，只能使用取list里面的第一个和最后一个方法获得最小值和最大值
						list.add(1, listmaxmin.get(listmaxmin.size() - 1));
						list.add(2, modelNoiseHistory);
						map.put("hasmaxmin", "yes");
						map.put("mlist", list);
					} else {
						list.add(0, modelNoiseHistory);
						map.put("hasmaxmin", "no");
						map.put("mlist", list);
					}
				} else {
					if (list != null) {
						list.add(0, modelNoiseHistory);
					}
					map.put("hasmaxmin", "no");
					map.put("mlist", list);
				}
				map.put("dataCount", dataCount);
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return map;
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			 try {
			listView.onRefreshComplete();
			// 在数组中存放数据
			if (result == null) {
				return;
			}
			hasmaxmin = (String) result.get("hasmaxmin");
			if (hasmaxmin.equals("other")) {
				layout_nothing.setVisibility(View.VISIBLE);
				layout_list.setVisibility(View.GONE);
				prDialog.cancel();
				noise_history_maplayout.setVisibility(View.GONE);
				return;
			} else if (hasmaxmin.equals("other_j")) {
				Toast.makeText(EnvironmentNoiseHistoryActivity.this, "没有更多数据！",
						3000).show();
			} else {
				@SuppressWarnings("unchecked")
				List<ModelNoiseHistory> list = (List<ModelNoiseHistory>) result
						.get("mlist");
				if (list != null) {
					if (hasmaxmin.equals("yes")) {
						listItem_maxmin.clear();
						for (int i = 0; i < 2; i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("noise_text", list.get(i).getmResult());
							map.put("noise_address", list.get(i).getLocation());
							map.put("noise_time", list.get(i).getTime());
							map.put("isupload", list.get(i).getIsupload());
							listItem_maxmin.add(map);
							listItem_maxmin_DB.add(map);
						}
						for (int i = 3; i < list.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("noise_text", list.get(i).getmResult());
							map.put("noise_address", list.get(i).getLocation());
							map.put("noise_time", list.get(i).getTime());
							map.put("isupload", list.get(i).getIsupload());
							listItem.add(map);
							list_map.add(list.get(i));
						}
					} else if (hasmaxmin.equals("no")) {
						for (int i = 1; i < list.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("noise_text", list.get(i).getmResult());
							map.put("noise_address", list.get(i).getLocation());
							map.put("noise_time", list.get(i).getTime());
							map.put("isupload", list.get(i).getIsupload());
							listItem.add(map);
							list_map.add(list.get(i));
						}
					}
					if (j == 0) {
						maxminAdapter = new MyMaxminAdapter();
						maxminAdapter.bindData(listItem_maxmin, hasmaxmin,
								EnvironmentNoiseHistoryActivity.this);
						listView_maxmin.setAdapter(maxminAdapter);
						mAdapter = new MyListAdapter();
						mAdapter.bindData(listItem, hasmaxmin,
								EnvironmentNoiseHistoryActivity.this);
						listView.setAdapter(mAdapter);
						dataCount = (String) result.get("dataCount");
						if (dataCount.equals("0")) {
							noise_DataTextView.setVisibility(View.GONE);
						} else {
							noise_DataTextView.setText(dataCount);
							noise_DataTextView.setVisibility(View.VISIBLE);
						}
						Message msg = Message.obtain();
						msg.what = 3;
						msg.obj = list_map;
						mapHandler.sendMessage(msg);
						prDialog.cancel();
					} else {
						mAdapter.notifyDataSetChanged();
						maxminAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(EnvironmentNoiseHistoryActivity.this,
							"没有更多数据！", 3000).show();
				}
			}
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}

	public class ClearDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				mSqLiteDALModelNoiseHistory
						.execSQL("delete from ModelNoiseHistory where isupload = '1'");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_NAME.equals(intent.getAction())) {
				Message msg = Message.obtain();
				msg.what = 2;
				mHandler.sendMessage(msg);
			}
			context.unregisterReceiver(this);
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(ACTION_NAME);
		registerReceiver(mBroadcastReceiver, mIntentFilter);
	}

	private int getAQIMarker(ModelNoiseHistory point) {

		int marker = R.drawable.rank_1;
		int value = 0;
		value = Integer.parseInt(point.getmResult());
		if (value <= 0) {
			marker = R.drawable.aqi_wrong;
		} else if (value <= 40) {
			marker = R.drawable.rank_1;
		} else if (value <= 45) {
			marker = R.drawable.rank_2;
		} else if (value <= 50) {
			marker = R.drawable.rank_3;
		} else if (value <= 55) {
			marker = R.drawable.rank_4;
		} else if (value <= 60) {
			marker = R.drawable.rank_5;
		} else if (value <= 65) {
			marker = R.drawable.rank_6;
		} else if (value <= 70) {
			marker = R.drawable.rank_7;
		} else if (value <= 75) {
			marker = R.drawable.rank_8;
		} else if (value <= 80) {
			marker = R.drawable.rank_9;
		} else {
			marker = R.drawable.rank_10;
		}
		return marker;
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onDestory()
		 */
		noise_history_mapView.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onPause()
		 */
		noise_history_mapView.onPause();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		noise_history_mapView.onResume();
		if (mApplication.getUserId() != null
				&& !mApplication.getUserId().equals("")
				&& mApplication.getIsnoisemuch()) {
			useridString = mApplication.getUserId();
			userid = Integer.parseInt(useridString);
			i = 0;
			page = 0;
			layout_list.setVisibility(View.VISIBLE);
			layout_nothing.setVisibility(View.GONE);
			noise_history_maplayout.setVisibility(View.VISIBLE);
			listItem_maxmin_DB.clear();
			SearchHistoryTask searchHistory = new SearchHistoryTask();
			searchHistory.execute();
		}
		mApplication.setIsnoisemuch(false);
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if (refreshView.isFooterShown()) {
			mHandler.sendEmptyMessage(0);
		}

	}

}
