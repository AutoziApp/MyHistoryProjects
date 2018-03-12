package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.litepal.util.PollutionLevelCacUtils;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.MicroStationBean;
import com.jy.environment.model.MicroStationBean.DataBean;
import com.jy.environment.model.MicroStationBean.DataBean.StationListBean;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.okUtils;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 微站排名界面
 * 
 * @author tianfy
 * 
 */
@SuppressLint("NewApi")
public class MicroStationActivity extends ActivityBase implements
		OnClickListener, OnItemSelectedListener, OnItemClickListener {
	private ImageView environment_current_air_aqi_itemf, search_empty;
	private TextView environment_time;
	private ListView environment_current_lv;
	private TextView paiming_paixu;
	private String city = "";
	private ListAdapter adapter;
	private CityDB mCityDB;
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private List<String> citys = new ArrayList<String>();
	private CityRank resultData;
	private Spinner air_polluction;
	private ArrayAdapter<String> polluctionAdapter;

	private Gson gson;
	private List<StationListBean> stationList;
	private String pollutionFactor;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.micro_station);
		gson = new Gson();
//		city = "平顶山";
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		environment_current_air_aqi_itemf = (ImageView) findViewById(R.id.environment_rank_itemf_back);
		environment_current_air_aqi_itemf.setOnClickListener(this);
		environment_current_lv = (ListView) findViewById(R.id.environment_current_lv);
		search_empty = (ImageView) findViewById(R.id.search_empty);
		environment_time = (TextView) findViewById(R.id.time);
		paiming_paixu = (TextView) findViewById(R.id.paiming_paixu);
		// 污染类型
		air_polluction = (Spinner) findViewById(R.id.air_polluction);
		environment_current_lv.setOnItemClickListener(this);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		// 初始化Spinner
		initSpinner();
		if (NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
			environment_current_lv.setVisibility(View.GONE);
			search_empty.setVisibility(View.VISIBLE);
		}
		paiming_paixu.setOnClickListener(this);

		adapter = new ListAdapter();
		stationList = new ArrayList<StationListBean>();
		adapter.bindData(stationList);
		environment_current_lv.setAdapter(adapter);
	}

	private String[] polluctionArrays;

	// 初始化Spinner
	private void initSpinner() {
		polluctionArrays = getResources()
				.getStringArray(R.array.pollutionTypes);
		polluctionAdapter = new ArrayAdapter<String>(MicroStationActivity.this,
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
					view = LayoutInflater.from(MicroStationActivity.this)
							.inflate(R.layout.spinner_item_first_layout, null);
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
				view = LayoutInflater.from(MicroStationActivity.this).inflate(
						R.layout.spinner_item_layout, null);
				TextView tv_spinner = (TextView) view
						.findViewById(R.id.tv_spinner);
				tv_spinner.setText(polluctionArrays[position]);
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

	/**
	 * 请求微站数据列表
	 * 
	 * @author tianfy
	 * @param pollutionFactor
	 *            因子
	 */
	private void requestMicroStationData(String pollutionFactor) {
		dialog.show();
		okUtils.get(UrlComponent.getAppMicroStation + "?pollutionFactor="
				+ pollutionFactor, new MicroStationStringCallBack());

	}

	private class MicroStationStringCallBack extends StringCallback {

		@Override
		public void onError(Call arg0, Exception err, int arg2) {
			// 请求失败
			dialog.dismiss();
			showErrorView(true);
		}

		@Override
		public void onResponse(String response, int arg1) {
			// 解析数据
			dialog.dismiss();
			parseJson(response);
		}
	}

	/**
	 * 解析数据
	 * 
	 * @param response
	 * @author tianfy
	 */
	private void parseJson(String response) {
		MicroStationBean microStationBean = gson.fromJson(response,
				MicroStationBean.class);
		showErrorView(false);
		String flag = microStationBean.getFlag();
		String msg = microStationBean.getMsg();
		if ("true".equals(flag) && "成功".equals(msg)) {
			DataBean data = microStationBean.getData();
			String updateTime = data.getUpdateTime();
			environment_time.setText("更新时间：" + updateTime);
			List<StationListBean> tempStationList = data.getStationList();
			if (tempStationList != null && tempStationList.size() > 0) {
				if (stationList!=null&&stationList.size()>0) {
					stationList.clear();
				}
				stationList.addAll(tempStationList);
				adapter.notifyDataSetChanged();
			} else {
				// 请求数据为空
				showErrorView(true);
			}

		} else {
			// 请求失败
			showErrorView(true);
		}
	}
	/**
	 * 是否显示请求失败的标识
	 * @param b true:显示；false:不显示
	 * @author tianfy
	 */
	public void showErrorView(boolean b) {
		if (b) {
			if (environment_current_lv.getVisibility() == View.VISIBLE
					&& search_empty.getVisibility() == View.GONE) {
				environment_current_lv.setVisibility(View.GONE);
				search_empty.setVisibility(View.VISIBLE);
			}
		} else {
			if (environment_current_lv.getVisibility() == View.GONE
					&& search_empty.getVisibility() == View.VISIBLE) {
				environment_current_lv.setVisibility(View.VISIBLE);
				search_empty.setVisibility(View.GONE);
			}
		}
	}

	private class ListAdapter extends BaseAdapter {
		private List<StationListBean> items;
		private boolean isShunxu = true;

		public void bindData(List<StationListBean> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater
							.from(getApplicationContext())
							.inflate(
									R.layout.environment_current_city_ranking_item3,
									null);
					holder.tv_city_aqi = (TextView) convertView
							.findViewById(R.id.tv_city_aqi);
					holder.tv_city_name = (TextView) convertView
							.findViewById(R.id.tv_city_name);
					holder.tv_city_number = (TextView) convertView
							.findViewById(R.id.tv_city_number);
					holder.tv_city_aqi_layout = (LinearLayout) convertView
							.findViewById(R.id.tv_city_aqi_layout);
					holder.tv_city_aqi_du = (TextView) convertView
							.findViewById(R.id.tv_city_aqi_du);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				
				//YYF 处理等级value
				holder.tv_city_aqi_du.setText(EnvironmentWeatherRankActivity
						.getDuValueYY(Integer.parseInt(items.get(position).getFactorLevel())));
//				holder.tv_city_aqi_du.setText(EnvironmentWeatherRankActivity
//						.getDuValue((int)items.get(position).getFactorValue()));
				
				//YYF 这是啥,先不管
//				holder.tv_city_aqi_layout
//						.setBackgroundResource(EnvironmentWeatherRankActivity
//								.getDuValueRes((int)items.get(
//										position).getFactorValue()));
				
				//YYF 背景色
				holder.tv_city_aqi_layout
				.setBackgroundResource(PollutionLevelCacUtils.getLeveliconYY(Integer.parseInt(items.get(position).getFactorLevel())));
//				holder.tv_city_aqi_layout
//				.setBackgroundResource(PollutionLevelCacUtils.getHourLevelicon(pollutionFactor.toLowerCase(), items.get(position).getFactorValue()));
				holder.tv_city_number.setText(position + 1 + "");
				holder.tv_city_name.setText(items.get(position)
						.getStationName());
				holder.tv_city_aqi
						.setText(items.get(position).getFactorValue()+"");
				// String provice = mCityDB.getprovicecity(items.get(position)
				// .getCityName());
				String provice = "";
				if (provice.equals("")) {
					holder.tv_city_pro.setVisibility(View.GONE);
				} else {
					holder.tv_city_pro.setVisibility(View.VISIBLE);
					holder.tv_city_pro.setText(provice);
				}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		private TextView tv_city_number;
		private TextView tv_city_name;
		private TextView tv_city_aqi;
		private TextView tv_city_aqi_du;
		private LinearLayout tv_city_aqi_layout;
		private TextView tv_city_pro;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.environment_rank_itemf_back:
			finish();
			break;
		case R.id.paiming_paixu:
			try {
				List<StationListBean> list = new ArrayList<StationListBean>();
				if (null != adapter.items) {
					for (int i = adapter.items.size() - 1; i >= 0; i--) {
						list.add(adapter.items.get(i));
					}
				}
				adapter.isShunxu = !adapter.isShunxu;
				if (adapter.isShunxu) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.paiming_show_btn_style);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					paiming_paixu.setCompoundDrawables(drawable, null, null,
							null);
				} else {
					Drawable drawable = getResources().getDrawable(
							R.drawable.paiming_btn_pre_style);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					paiming_paixu.setCompoundDrawables(drawable, null, null,
							null);
				}
				adapter.bindData(list);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	private ArrayList<HashMap<String, Object>> selectCitys(
			ArrayList<HashMap<String, Object>> citys) {
		try {
			if (null != citys && citys.size() > 0) {
				for (int i = 0; i < citys.size(); i++) {
					if ("1".equals(citys.get(i).get("islocation"))) {
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) parent.getAdapter();
		pollutionFactor = arrayAdapter.getItem(position);
		if(pollutionFactor.equals("PM2.5")){
			pollutionFactor="PM25";
		}
		requestMicroStationData(pollutionFactor);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListAdapter listAdapter = (ListAdapter) parent.getAdapter();
		StationListBean stationListBean = (StationListBean) listAdapter.getItem(position);
//		Intent intent=new Intent(MicroStationActivity.this,EnvironmentMapActivity.class);
//		intent.putExtra("StationListBean", stationListBean);
//		intent.putExtra("TAG", MicroStationActivity.class.getSimpleName());
//		intent.putExtra("type", pollutionFactor.toLowerCase());
//		startActivity(intent);
		Intent intent=new Intent(MicroStationActivity.this,StationDetailActivity.class);
		intent.putExtra("stationCode", stationListBean.getStationCode());
		intent.putExtra("stationName", stationListBean.getStationName());
		startActivity(intent);
	}
}