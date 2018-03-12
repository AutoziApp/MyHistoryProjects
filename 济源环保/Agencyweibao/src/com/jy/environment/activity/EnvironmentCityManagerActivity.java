package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.City;
import com.jy.environment.model.EnvironmentCityWeatherModel;
import com.jy.environment.model.ManageCity;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 环境首页城市管理界面
 * 
 * @author baiyuchuan
 * 
 */
@SuppressLint("NewApi")
public class EnvironmentCityManagerActivity extends ActivityBase {
	private DeleteCityTask deleteCityTask;
	public static final String LOCATIONCHANGEACTION = "com.mapuni.weibao.locatinonChange";
	public static final String DELETECITY = "DELETECITY";
	private City city;
	private String cityName;
	private ImageView iv_back;
	private RelativeLayout kuang_add;
	private WeiBaoApplication mApplication;
	private GridView lv;
	private int posposition;   
	private Intent intent;
	private boolean showadd = false;
	// ProgressBar add_pb;
	public static final String ADD_CITYCLICK = "CITY_CLICK";
	public static final String ADD_ACTION = "ADD_ACTION";
	public static final String ADD_VIEW = "ADD_VIEW";
	public static final String LOCATION_CHANGE = "LOCATION_CHANGE";
	private CityDB mCityDB;
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private List<ManageCity> citys1 = new ArrayList<ManageCity>();
	private ImageView edit_btn;
	private int count = 0;
	private int count1 = 0;
	boolean xiehuan = false;
	private myAdapter adapter;
	private boolean isr;
	private Dialog dialog;
	private SharedPreferencesUtil mSpUtil2;
	private String morenCity = "";
	private List<String> cityArrayys = new ArrayList<String>() {
		{
//			add("郑州");
//			add("开封");
//			add("洛阳");
//			add("平顶山");
//			add("安阳");
//			add("鹤壁");
//			add("新乡");
//			add("焦作");
//			add("濮阳");
//			add("许昌");
//			add("漯河");
//			add("三门峡");
//			add("南阳");
//			add("商丘");
//			add("信阳");
//			add("周口");
//			add("驻马店");
//			add("济源");
//			add("巩义");
//			add("兰考县");
//			add("汝州");
//			add("滑县");
//			add("长垣县");
//			add("邓州");
//			add("永城");
//			add("固始县");
//			add("鹿邑县");
//			add("新蔡县");
			add("焦作");
			add("修武县");
			add("博爱县");
			add("武陟县");
			add("温县");
			add("泌阳县");
			add("孟州");
		}
	};

	// private String[] cityArrayys1 = new String[] { "郑州", "开封", "洛阳", "平顶山",
	// "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河",
	// "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店", "济源", "巩义", "兰考县", "汝州", "滑县",
	// "长垣县", "邓州", "永城", "固始县", "鹿邑县",
	// "新蔡县" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_city_manager_activity);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_in);
		try {
			mSpUtil2 = SharedPreferencesUtil
					.getInstance(EnvironmentCityManagerActivity.this);
			morenCity = mSpUtil2.get("morenCity", "焦作");
			LocationService
					.sendGetLocationBroadcast(EnvironmentCityManagerActivity.this);
			intent = getIntent();
			deleteCityTask = new DeleteCityTask();
			posposition = intent.getIntExtra("posposition", 0);
			mApplication = WeiBaoApplication.getInstance();
			dialog = CommonUtil.getCustomeDialog(
					EnvironmentCityManagerActivity.this, R.style.load_dialog,
					R.layout.custom_progress_dialog);
			TextView titleTxtv = (TextView) dialog
					.findViewById(R.id.dialogText);
			titleTxtv.setText("正在努力加载…");
			// 进度条是否不明确
			// ImageView iv = (ImageView) findViewById(R.id.city_iv_back);
			edit_btn = (ImageView) findViewById(R.id.edit_btn);
			edit_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					MobclickAgent.onEvent(EnvironmentCityManagerActivity.this,
							"HJEditCity");
					if (xiehuan) {
						xiehuan = false;
						xianshi = false;
						edit_btn.setBackgroundResource(R.drawable.city_mgr_top_bar_edit_icon);
						if (citys1.size() == 0) {
							setAdapter();
						} else {
							adapter.notifyDataSetChanged();
						}
					} else {
						xiehuan = true;
						xianshi = true;
						edit_btn.setBackgroundResource(R.drawable.city_mgr_top_bar_edit_done_icon_pressed);
						if (citys1.size() == 0) {
							setAdapter();
						} else {
							adapter.notifyDataSetChanged();
						}
					}

				}
			});
			lv = (GridView) findViewById(R.id.c_list);
			iv_back = (ImageView) findViewById(R.id.city_iv_back);
			mCityDB = WeiBaoApplication.getInstance().getCityDB();
			getCityDataFromDBAndResetViews();
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (xianshi) {
						return;
					}
					try {

						WeiBaoApplication.selectedCity = citys1.get(arg2)
								.getCityName();
						Intent intent = new Intent();
						intent.setAction(ADD_CITYCLICK);
						if (bian) {
							intent.putExtra("bian", true);
						} else {
							intent.putExtra("bian", false);
						}
						intent.putExtra("cityName", citys1.get(arg2)
								.getCityName());
						intent.putExtra("view", initcitys);
						intent.putExtra("po", arg2);
						getApplicationContext().sendBroadcast(intent);
						finish();
					} catch (Exception e) {
					}
				}

			});
			// lv.setOnChangeListener(new OnChanageListener() {
			//
			// @Override
			// public void onChange(int from, int to) {
			// ManageCity temp = citys1.get(from);
			// // 直接交互item
			// // dataSourceList.set(from, dataSourceList.get(to));
			// // dataSourceList.set(to, temp);
			// MyLog.i("chang>>>>====<<<<<from" + from);
			// MyLog.i("chang>>>>====<<<<<to" + to);
			//
			// // 这里的处理需要注意下
			// if (from < to) {
			// for (int i = from; i < to; i++) {
			// Collections.swap(citys1, i, i + 1);
			// }
			// } else if (from > to) {
			// for (int i = from; i > to; i--) {
			// Collections.swap(citys1, i, i - 1);
			// }
			// }
			//
			// citys1.set(to, temp);
			//
			// // adapter.notifyDataSetChanged();
			// setAdapter();
			//
			// }
			// });
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,
						int position, long id) {
					// TODO Auto-generated method stub\
					xiehuan = true;
					xianshi = true;
					edit_btn.setBackgroundResource(R.drawable.city_mgr_top_bar_edit_done_icon_pressed);
					setAdapter();
					return false;
				}
			});
			iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(EnvironmentCityManagerActivity.this,
							"CityBack");
					for (int i = 0; i < initcitys.size(); i++) {
						ManageCity mCity = new ManageCity(initcitys.get(i)
								.get("name").toString(), initcitys.get(i)
								.get("climate").toString(), initcitys.get(i)
								.get("temp").toString());
					}
					Intent intent = new Intent();
					intent.setAction(ADD_VIEW);
					intent.putExtra("view", initcitys);
					intent.putExtra("bian", bian);
					intent.putExtra("posposition", posposition);
					getApplicationContext().sendBroadcast(intent);
					finish();
					overridePendingTransition(R.anim.push_left_out,
							R.anim.push_left_out);
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			List<String> cityListsCurrent = new ArrayList<String>();
			for (int i = 0; i < initcitys.size(); i++) {
				MyLog.i("initcitys:" + initcitys.get(i).get("name") + "");
				cityListsCurrent.add(initcitys.get(i).get("name") + "");
			}
			for (int i = 0; i < cityArrayys.size(); i++) {
				if (!cityListsCurrent.contains(cityArrayys.get(i))) {
					String citynameadd = cityArrayys.get(i);
					City cityAdd = mCityDB.getCityInfo(citynameadd);
					if (mCityDB.addCityExist(citynameadd)) {
						mCityDB.update(citynameadd, "", "");
					} else {
						mCityDB.addXuanZhecity(cityAdd);
					}
					MyLog.i("mCityDB cityArrays[i]:" + cityArrayys.get(i));
					if (null != dialog && dialog.isShowing()) {
						dialog.show();
					}
					new OnAddCityQueryWeatherAndLoadTask().execute(citynameadd);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setAdapter() {
		if (null == adapter) {
			MyLog.i("null == adapter");
			adapter = new myAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		intent.setAction(ADD_VIEW);
		intent.putExtra("view", initcitys);
		intent.putExtra("bian", bian);
		intent.putExtra("posposition", posposition);
		getApplicationContext().sendBroadcast(intent);
		finish();
		return super.onKeyDown(keyCode, event);

	}

	private void getCityDataFromDBAndResetViews() {
		MyLog.i("getCityDataFromDBAndResetViews");
		initcitys.clear();
		citys1.clear();
		initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
		initcitys = selectCitys(initcitys);
		setAdapter();
		MyLog.i("initcitys.size() :" + initcitys.size());
		for (int i = 0; i < initcitys.size(); i++) {
			ManageCity mCity = new ManageCity(initcitys.get(i).get("name")
					.toString(), initcitys.get(i).get("climate").toString(),
					initcitys.get(i).get("temp").toString());
			if (!"".equals(mCity.getCityName())) {
				WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(),
						false, mCity.getCityName());
			}
			if ("1".equals(initcitys.get(i).get("islocation"))
					&& citys1.size() > 0) {
				ManageCity mCityFirst = citys1.get(0);

				citys1.set(0, mCity);
				citys1.add(mCityFirst);
			} else {
				citys1.add(mCity);
			}
			if (citys1.size() != 0) {
				setAdapter();
			} else {
				isr = true;
				setAdapter();
			}
		}
		for (int i = 0; i < citys1.size(); i++) {
			// if(null == citys1.get(i).getClimate() ||
			// "".equals(citys1.get(i).getClimate())){
			if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) != NetUtil.NETWORN_NONE) {
				// prDialog.show();
				String cityname = citys1.get(i).getCityName().toString();
				new OnAddCityQueryWeatherTask().execute(cityname);
				// }
			}
		}
		// LoadDBDateTask dateTask = new LoadDBDateTask();
		// dateTask.execute();
	}

	boolean xianshi = false;

	class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (citys1.size() < 35 && !xianshi) {
				showadd = true;
				count = citys1.size() + 1;
			} else {
				showadd = false;
				count = citys1.size();
			}
			return count;

		}

		@Override
		public Object getItem(int position) {
			return citys1.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = getLayoutInflater().inflate(
					R.layout.environment_city_manager_gridview_item, null);
			try {
				cityName = (String) citys1.get(position).getCityName();
			} catch (Exception e) {
				// TODO: handle exception
			}
			TextView tv1 = (TextView) view.findViewById(R.id.city_name);
			TextView tv2 = (TextView) view.findViewById(R.id.city_weather);
			final TextView auto_locate = (TextView) view
					.findViewById(R.id.auto_locate);
			ImageView iv1 = (ImageView) view.findViewById(R.id.add_weather_img);
			kuang_add = (RelativeLayout) view.findViewById(R.id.kuang_add);
			LinearLayout city_weatherinfo = (LinearLayout) view
					.findViewById(R.id.city_weatherinfo);
			ImageView imdel = (ImageView) view.findViewById(R.id.delete_btn);
			try {
				if (showadd) {
					if (position == count - 1) {
						kuang_add.setVisibility(View.VISIBLE);
						city_weatherinfo.setVisibility(View.GONE);

						kuang_add.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (initcitys.size() > 35) {
									Toast.makeText(
											EnvironmentCityManagerActivity.this,
											"您添加的城市已抵达上限，请删除后再添加", 1).show();
									return;
								}
								MobclickAgent.onEvent(
										EnvironmentCityManagerActivity.this,
										"HJSelectCity");
								Intent intent = new Intent(
										EnvironmentCityManagerActivity.this,
										EnvironmentSelectCtiyActivity.class);
								intent.putExtra("from", "add");
								intent.putExtra("load", "citymanager");
								startActivityForResult(intent, 0);
							}
						});

					} else if (citys1.size() > 0) {
						city_weatherinfo.setVisibility(View.VISIBLE);
						kuang_add.setVisibility(View.GONE);

						if (!isContains(cityName, cityArrayys)
								&& xianshi
								&& (null == mApplication.getDingweicity()
										|| "".equals(mApplication
												.getDingweicity()) || !mApplication
										.getDingweicity().equals(cityName))) {
							imdel.setVisibility(View.VISIBLE);
						} else {
							imdel.setVisibility(View.GONE);
						}

						imdel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {

								// mCityDB.deleteSelectedCity(citys1.get(position)
								// .getCityName());
								// mApplication.addJPushAliasAndTags(
								// EnvironmentCityManagerActivity.this, true,
								// city.getName());
								// mApplication.getjPushAliasAndTags().remove(
								// citys1.get(position).getCityName());
								if (deleteCity(citys1.get(position)
										.getCityName())
										&& posposition == position) {
									MobclickAgent
											.onEvent(
													EnvironmentCityManagerActivity.this,
													"HJDeleteCity");
									posposition = 0;
									bian = true;
								}
								// if (posposition == position) {
								// posposition = 0;
								// }
								// LoadDBDateTask dateTask = new
								// LoadDBDateTask();
								// dateTask.execute();
							}
						});
						tv1.setText(cityName);
						if (cityName.equals(mApplication.getDingweicity())) {
							auto_locate.setVisibility(View.VISIBLE);
						}
						tv2.setText(citys1.get(position).getTemp().toString());
						String clima = citys1.get(position).getClimate()
								.toString();
						iv1.setImageResource(getWeatherIcon(clima));
					}

				} else {
					city_weatherinfo.setVisibility(View.VISIBLE);
					kuang_add.setVisibility(View.GONE);

					if (!isContains(cityName, cityArrayys)
							&& xianshi
							&& (null == mApplication.getDingweicity()
									|| "".equals(mApplication.getDingweicity()) || !mApplication
									.getDingweicity().equals(cityName))) {
						imdel.setVisibility(View.VISIBLE);
					} else {
						imdel.setVisibility(View.GONE);
					}

					imdel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							// bian = true;
							// mCityDB.deleteSelectedCity(citys1.get(position)
							// .getCityName());
							// mApplication.addJPushAliasAndTags(
							// EnvironmentCityManagerActivity.this, true,
							// citys1.get(position).getCityName());
							// mApplication.getjPushAliasAndTags().remove(
							// citys1.get(position).getCityName());
							// LoadDBDateTask dateTask = new LoadDBDateTask();
							// dateTask.execute();
							if (deleteCity(citys1.get(position).getCityName())) {
								bian = true;
								MobclickAgent.onEvent(
										EnvironmentCityManagerActivity.this,
										"HJDeleteCity");
							}
							// setAdapter();
						}
					});

					tv1.setText(cityName);
					if (cityName.equals(mApplication.getDingweicity())) {
						auto_locate.setVisibility(View.VISIBLE);
					}
					tv2.setText(citys1.get(position).getTemp().toString());
					String clima = citys1.get(position).getClimate().toString();
					iv1.setImageResource(getWeatherIcon(clima));
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
			if (null != cityArrayys && (cityArrayys.contains(cityName) && xianshi)
					|| cityName.equals(morenCity)) {
				auto_locate.setVisibility(View.VISIBLE);
			} else {
				auto_locate.setVisibility(View.INVISIBLE);
			}
			if (cityName.equals(morenCity)) {
				auto_locate.setText("默认");
				auto_locate.setBackground(getResources().getDrawable(
						R.drawable.city_bottom_select));
			} else {
				auto_locate.setBackground(getResources().getDrawable(
						R.drawable.citybg));
				auto_locate.setText("设为默认");
				auto_locate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// ToastUtil.showShort(EnvironmentCityManagerActivity.this,
						// citys1.get(position).getCityName());
						auto_locate.setText("默认");
						bian = true;
						mSpUtil2.put("morenCity", citys1.get(position)
								.getCityName());
						morenCity = mSpUtil2.get("morenCity", "焦作");
						adapter.notifyDataSetChanged();
					}
				});
			}
			return view;
		}

	}

	private int getWeatherIcon(String climate) {
		int weatherIcon = R.drawable.weather_icon_qingtian;
		String climateString = CommonUtil.getWeatherIconString(climate, 0);
		if (mApplication.getWeatherIconMap().containsKey(climateString)) {
			weatherIcon = mApplication.getWeatherIconMap().get(climateString);
		}

		return weatherIcon;
	}

	boolean bian = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			city = (City) data.getSerializableExtra("city");
			if (city == null) {
				// Toast.makeText(AddCityActivity.this, "当前城市为空,请重新添加",
				// 0).show();
				return;
			}
			if (mCityDB.addCityExist(city.getName())) {
				dialog2();
			} else {
				bian = true;
				if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) != NetUtil.NETWORN_NONE) {
					String cityname = city.getName();
					if (mCityDB.addCityExist(cityname)) {
						mCityDB.update(cityname, "", "");
					} else {
						mCityDB.addXuanZhecity(city);
					}
					if (!"".equals(city.getName())) {
						WeiBaoApplication.addJPushAliasAndTags(
								getApplicationContext(), false, city.getName());
					}
					// LoadDBDateTask dateTask = new LoadDBDateTask();
					// dateTask.execute();
					dialog.show();
					new OnAddCityQueryWeatherAndLoadTask().execute(cityname);
					// LoadDBDateTask dateTask2 = new LoadDBDateTask();
					// dateTask2.execute();
				} else {
					Toast.makeText(EnvironmentCityManagerActivity.this,
							"您的网络不给力，无法添加城市", 1).show();
				}
			}
		}

	}

	private boolean deleteCity(String cityname) {
		if (AsyncTask.Status.PENDING == deleteCityTask.getStatus()) {
			deleteCityTask.execute(cityname);
			return true;
		} else if (AsyncTask.Status.RUNNING == deleteCityTask.getStatus()) {
			// Toast.makeText(EnvironmentCityManagerActivity.this,
			// this.getResources().getString(R.string.loading_data), 2000)
			// .show();
			Toast.makeText(EnvironmentCityManagerActivity.this, "正在删除，请稍后",
					2000).show();
			return false;
		} else if (AsyncTask.Status.FINISHED == deleteCityTask.getStatus()) {
			deleteCityTask = new DeleteCityTask();
			deleteCityTask.execute(cityname);
			return true;
		}
		return false;

	}

	protected void dialog2() {
		AlertDialog dialog = new AlertDialog.Builder(
				EnvironmentCityManagerActivity.this).setTitle("提示")
				.setMessage("抱歉，您不能添加同一个城市两次")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}

	// // 长按删除弹出框
	// protected void dialog3(final int name) {
	// AlertDialog dialog = new
	// AlertDialog.Builder(EnvironmentCityManagerActivity.this)
	// .setMessage("是否确认删除?")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// bian = true;
	// mCityDB.deleteSelectedCity(citys1.get(name)
	// .getCityName());
	// qu1();
	// adapter.notifyDataSetChanged();
	// // lv.setAdapter(new myAdapter());
	//
	// dialog.dismiss();
	// }
	// })
	// .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// dialog.dismiss();
	// }
	// }).create();
	// dialog.show();
	// }

	/**
	 * 定位变化通知
	 * 
	 * @author baiyuchuan
	 * 
	 */
	public class LocationChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			MyLog.i("intent.getAction() :" + intent.getAction());
			if (null != intent && null != intent.getAction()
					&& intent.getAction().equals(LOCATIONCHANGEACTION)) {
				try {
					getCityDataFromDBAndResetViews();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	class OnAddCityQueryWeatherTask extends
			AsyncTask<String, Void, EnvironmentCityWeatherModel> {
		String city = "";

		@Override
		protected EnvironmentCityWeatherModel doInBackground(String... params) {
			// String df = mCityDB.getNumber(params[0]);
			city = params[0];
			// if ("".equals(df)) {
			// return null;
			// }
			if (city.contains("自治州")) {
				city = mCityDB.getSuoSuo(city);
			}
			String url = UrlComponent.getWeatherByCity_Get(city);
			MyLog.i(">>>>>>>>>>>>>>>>suosuo" + city);
			BusinessSearch search = new BusinessSearch();
			EnvironmentCityWeatherModel _Result = null;
			try {
				_Result = search.getCityWeather(url);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (null == _Result) {
					return null;
				}
				if (mCityDB.addCityExist(params[0])) {
					mCityDB.update(params[0], _Result.getClimate(),
							_Result.getTemp());
				} else {
					mCityDB.addXuanZhecity1(params[0], _Result.getClimate(),
							_Result.getTemp(), false);
				}
				initcitys.clear();
				initcitys = mCityDB
						.queryBySqlReturnArrayListHashMap("select * from addcity");
				initcitys = selectCitys(initcitys);
				// LoadDBDateTask dateTask = new LoadDBDateTask();
				// dateTask.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(EnvironmentCityWeatherModel result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (isr) {
				isr = false;
				setAdapter();
			} else {
				setAdapter();
			}

		}

	};

	class LoadDBDateTask extends AsyncTask<String, Void, String> {

		private List<ManageCity> listCache = new ArrayList<ManageCity>();

		@Override
		protected String doInBackground(String... params) {
			try {
				initcitys.clear();
				initcitys = mCityDB
						.queryBySqlReturnArrayListHashMap("select * from addcity");
				initcitys = selectCitys(initcitys);
				if (null == initcitys || initcitys.size() == 0) {
					return null;
				}
				MyLog.i("initcitys +" + initcitys.size());
				for (int i = 0; i < initcitys.size(); i++) {
					ManageCity mCity = new ManageCity(initcitys.get(i)
							.get("name").toString(), initcitys.get(i)
							.get("climate").toString(), initcitys.get(i)
							.get("temp").toString());
					if ("1".equals(initcitys.get(i).get("islocation"))
							&& listCache.size() > 0) {
						ManageCity mCityFirst = listCache.get(0);
						listCache.set(0, mCity);
						listCache.add(mCityFirst);
					} else {
						listCache.add(mCity);
					}
				}
				/**
				 * 如果添加城市 那么去重
				 */
				citys1 = listCache;
				// }
				return null;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null != adapter) {
				setAdapter();
			}
		}
	}

	class OnAddCityQueryWeatherAndLoadTask extends
			AsyncTask<String, Void, EnvironmentCityWeatherModel> {
		String city = "";

		@Override
		protected EnvironmentCityWeatherModel doInBackground(String... params) {
			// String df = mCityDB.getNumber(params[0]);
			city = params[0];
			if (city.contains("自治州")) {
				city = mCityDB.getSuoSuo(city);
			}
			String url = UrlComponent.getWeatherByCity_Get(city);
			MyLog.i(">>>>>>>df" + url);
			BusinessSearch search = new BusinessSearch();
			EnvironmentCityWeatherModel _Result = null;
			try {
				_Result = search.getCityWeather(url);
				MyLog.i(">>>>>>>df" + _Result);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (null == _Result) {
					return null;
				}
				if (mCityDB.addCityExist(params[0])) {
					mCityDB.update(params[0], _Result.getClimate(),
							_Result.getTemp());
				} else {
					mCityDB.addXuanZhecity1(params[0], _Result.getClimate(),
							_Result.getTemp(), false);
				}
				LoadDBDateTask dateTask = new LoadDBDateTask();
				dateTask.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(EnvironmentCityWeatherModel result) {
			super.onPostExecute(result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
				;
			}
			if (isr) {
				isr = false;
				MobclickAgent.onEvent(EnvironmentCityManagerActivity.this,
						"HJAddCity");
				setAdapter();
			} else {
				setAdapter();
			}

		}

	};

	class DeleteCityTask extends AsyncTask<String, Void, String> {
		String city = "";

		@Override
		protected String doInBackground(String... params) {
			try {
				city = params[0];
				if ("".equals(city)) {
					return null;
				}
				mCityDB.deleteSelectedCity(city);
				mApplication.addJPushAliasAndTags(
						EnvironmentCityManagerActivity.this, true, city);
				mApplication.getjPushAliasAndTags(
						EnvironmentCityManagerActivity.this).remove(city);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result) {

			} else {
				Toast.makeText(EnvironmentCityManagerActivity.this, "删除成功",
						2000).show();
				LoadDBDateTask dateTask = new LoadDBDateTask();
				dateTask.execute();
			}

		}

	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public boolean isContains(String a, List<String> str) {
		for (int i = 0; i < str.size(); i++) {
			if (str.get(i).contains(a)) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<HashMap<String, Object>> selectCitys(
			ArrayList<HashMap<String, Object>> citys) {
		List<String> cityAll = new ArrayList<String>();
		for (int j = 0; j < citys.size(); j++) {
			cityAll.add(citys.get(j).get("name") + "");
		}

		try {
			morenCity = mSpUtil2.get("morenCity", "焦作");
			if (null != citys && citys.size() > 0) {
				for (int i = 0; i < citys.size(); i++) {
					if ("1".equals(citys.get(i).get("islocation"))) {
						HashMap<String, Object> hashMapFirst = citys.get(0);
						HashMap<String, Object> hashMapLocation = citys.get(i);
						citys.set(0, hashMapLocation);
						citys.set(i, hashMapFirst);
					}
					if (morenCity.equals(citys.get(i).get("name"))) {
						HashMap<String, Object> hashMapFirst = citys.get(0);
						HashMap<String, Object> hashMapLocation = citys.get(i);
						citys.set(0, hashMapLocation);
						citys.set(i, hashMapFirst);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return citys;
	}

}
