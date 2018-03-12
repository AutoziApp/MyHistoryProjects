package com.jy.environment.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.Item;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 环境天气趋势界面
 * 
 * @author baiyuchuan
 * 
 */
@SuppressLint("NewApi")
public class EnvironmentWeatherRankPaiMingActivity extends ActivityBase
		implements OnClickListener {
	private ImageView environment_current_air_aqi_itemf, search_empty;
	private TextView environment_time;
	private ListView environment_current_lv;
	private TextView paiming_paixu;
	private String city = "";
	private ListAdapter adapter;
	private GetAqiDetailItemTask itemTask;
	private CityDB mCityDB;
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private List<String> citys = new ArrayList<String>();
	private CityRank resultData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_current_air_aqi_itemf2);
		city = getIntent().getStringExtra("city");
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
		itemTask = new GetAqiDetailItemTask();
		if (NetUtil.getNetworkState(getApplicationContext()) == NetUtil.NETWORN_NONE) {
			environment_current_lv.setVisibility(View.GONE);
			search_empty.setVisibility(View.VISIBLE);
		}
		itemTask.execute();
		paiming_paixu.setOnClickListener(this);
		mCityDB = WeiBaoApplication.getInstance().getCityDB();
		initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
		initcitys = selectCitys(initcitys);
		for (int i = 0; i < initcitys.size(); i++) {
			citys.add((String) initcitys.get(i).get("name"));
		}
	}

	public class GetAqiDetailItemTask extends AsyncTask<String, Void, CityRank> {
		@Override
		protected CityRank doInBackground(String... params) {
			String url = UrlComponent.cityRankingUrl_Post;
			BusinessSearch search = new BusinessSearch();
			CityRank _Result;
			try {
				/**
				 * 主要为读取历史消息,时间为一小时间隔，保证加载速率，如果读取不到，则从网络上获取
				 */
				if (city.contains("自治州")) {
					_Result = search.getAqiDetailItem(url, 3600,
							mCityDB.getSuoSuo(city));
				} else {
					_Result = search.getAqiDetailItem(url, 3600, city);
				}
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(CityRank result) {
			try {
				resultData = result;
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				if (null == result) {
					return;
				} else {
					adapter = new ListAdapter();
					adapter.bindData(result.get_Result());
					environment_current_lv.setAdapter(adapter);
					if (result.getRank() <= 5) {
						environment_current_lv.setSelection(0);
					}
					environment_current_lv.setSelection(result.getRank() - 5);
					if (null != resultData) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy年-MM月dd日-HH时mm分ss秒");
						Date date = new Date(resultData.getTime());
						MyLog.i("resultData.getTime()" + resultData.getTime());
						MyLog.i("resultData.getTime()"
								+ System.currentTimeMillis());
						MyLog.i("resultData.getTime()" + date);
						MyLog.i("resultData.getTime()"
								+ new Date(System.currentTimeMillis()));
						environment_time.setText("更新时间:"
								+ (date.getMonth() + 1) + "月"
								+ (date.getDate()) + "日" + date.getHours()
								+ "时" + date.getMinutes() + "分");
					}
				}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}

	}

	private class ListAdapter extends BaseAdapter {
		private List<Item> items;
		private boolean isShunxu = true;

		public void bindData(List<Item> items) {
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
									R.layout.environment_current_city_ranking_item2,
									null);
					holder.tv_city_lay = (LinearLayout) convertView
							.findViewById(R.id.tv_city_lay);
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
					holder.city_location_img = (ImageView) convertView
							.findViewById(R.id.city_location_img);
					holder.city_img = (ImageView) convertView
							.findViewById(R.id.city_img);
					holder.tv_city_pro = (TextView) convertView
							.findViewById(R.id.tv_city_pro);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.tv_city_aqi_du.setText(EnvironmentWeatherRankActivity
						.getDuValue(Integer.parseInt(items.get(position)
								.getIndex())));
				holder.tv_city_aqi_layout
						.setBackgroundResource(EnvironmentWeatherRankActivity
								.getDuValueRes(Integer.parseInt(items.get(
										position).getIndex())));
				holder.tv_city_number.setText(items.get(position).getRank());
				holder.tv_city_name.setText(items.get(position).getCityName());
				holder.tv_city_aqi.setText(items.get(position).getIndex());
				String provice = mCityDB.getprovicecity(items.get(position)
						.getCityName());
				if (provice.equals("")) {
					holder.tv_city_pro.setVisibility(View.GONE);
				} else {
					holder.tv_city_pro.setVisibility(View.VISIBLE);
					holder.tv_city_pro.setText(provice);
				}
				
				if (citys.contains(items.get(position).getCityName())) {
					holder.city_img.setVisibility(View.VISIBLE);
				} else {
					holder.city_img.setVisibility(View.INVISIBLE);
				}
				if (0 == citys.indexOf(items.get(position).getCityName())) {
					holder.city_location_img.setVisibility(View.VISIBLE);
				} else {
					holder.city_location_img.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		private LinearLayout tv_city_lay;
		private TextView tv_city_number;
		private TextView tv_city_name;
		private TextView tv_city_aqi;
		private TextView tv_city_aqi_du;
		private LinearLayout tv_city_aqi_layout;
		private ImageView city_img;
		private ImageView city_location_img;
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
				List<Item> list = new ArrayList<Item>();
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

}