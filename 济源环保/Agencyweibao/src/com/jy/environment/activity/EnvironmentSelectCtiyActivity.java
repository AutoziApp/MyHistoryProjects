package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jy.environment.R;
import com.jy.environment.adapter.CityAdapter;
import com.jy.environment.adapter.EnvironmentSearchCityAdapter;
import com.jy.environment.adapter.EnvironmentSelectCtiyGridViewAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.City;
import com.jy.environment.model.HotCityInfo;
import com.jy.environment.receiver.NetBroadcastReceiver;
import com.jy.environment.receiver.NetBroadcastReceiver.EventHandler;
import com.jy.environment.util.MyLog;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 选择城市界面
 * 
 * @author baiyuchuan
 * 
 */
public class EnvironmentSelectCtiyActivity extends ActivityBase implements
		TextWatcher, OnClickListener, EventHandler {
	private EditText mSearchEditText;
	private ImageView mClearSearchBtn;
	private View mCityContainer;
	private View mSearchContainer;

	private ListView mSearchListView;
	private List<City> mCities;
	private EnvironmentSearchCityAdapter mSearchCityAdapter;
	private CityAdapter mCityAdapter;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	private InputMethodManager mInputMethodManager;

	private TextView mTitleTextView;
	private ImageView mBackBtn;
	List<City> citysList;
	SharedPreferences sp;
	WeiBaoApplication mApplication;
	private CityDB mCityDB;
	private LocationClient mLocationClient;
	// LinearLayout ll_select_pb;
	boolean isf;
	public SharedPreferences sharedPref;// 缓存
	SharedPreferences.Editor editor;
	private String load = "";
	private ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	private boolean isDingwei = false;

	protected void onPause() {
		MyLog.i("onPause");
		super.onPause();
		MobclickAgent.onPause(this);
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_select_ctiy_activity);
		MyLog.i("onCreate");
		// ll_select_pb=(LinearLayout) findViewById(R.id.ll_select_pb);
		String is = getIntent().getExtras().getString("from");
		NetBroadcastReceiver.mListeners.add(this);
		mApplication = WeiBaoApplication.getInstance();
		mCityDB = mApplication.getCityDB();
		initUI();
		initView();
		initData();
		if (is.equals("lo")) {
			isf = true;
			// ll_select_pb.setVisibility(View.VISIBLE);
			mApplication = WeiBaoApplication.getInstance();
			mLocationClient = mApplication.getLocationClient();
			try {
				load = getIntent().getStringExtra("load");
			} catch (Exception e) {
				// TODO: handle exception
				load = "";
			}
			mLocationClient.registerLocationListener(mLocationListener);
			mCityDB = mApplication.getCityDB();
			mLocationClient.start();
			mLocationClient.requestLocation();

		}

	}

	private HotCityInfo mHotCityInfo;

	public List<HotCityInfo> initHotCityInfo() {
		List<HotCityInfo> hotCityList = new ArrayList<HotCityInfo>();
		String[] cityStrings = { "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡",
				"焦作", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口",
				"驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县",
				"鹿邑县", "新蔡县"};
		String[] cityId = { 
				"101040100", "101050101", "101060101", "101070101",
				"101080101", "101090101", "101100101", "101110101",
				"101120101", "101130101", "101140101", "101150101",
				"101160101", "101170101", "101180101", "101190101",
				"101200101", "101210101", "101220101", "101230101",
				"101240101", "101250101", "101260101", "101270101",
				"101280101", "101290101", "101300101", "101310101",
				 };

		for (int i = 0; i < cityId.length; i++) {
			mHotCityInfo = new HotCityInfo();
			mHotCityInfo.setHotCityName(cityStrings[i]);
			mHotCityInfo.setHotArea_id(cityId[i]);
			hotCityList.add(mHotCityInfo);
		}
		return hotCityList;
	}

	BDLocationListener mLocationListener = new BDLocationListener() {

		// @Override
		// public void onReceivePoi(BDLocation arg0) {
		// // do nothing
		// }

		@Override
		public void onReceiveLocation(BDLocation location) {
			// try {
			// Thread.sleep(2000);
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			//
			// ll_select_pb.setVisibility(View.GONE);
			initcitys = mCityDB
					.queryBySqlReturnArrayListHashMap("select * from addcity");
			double longitude;
			double latitude;
			if (location == null || TextUtils.isEmpty(location.getCity())) {
				if (!WeiBaoApplication.selectedCity.equals("")) {
					// Toast.makeText(EnvironmentSelectCtiyActivity.this,
					// "请手动选择城市", 1).show();
				}
				return;
			}
			// 获取当前城市，
			String cityName = location.getCity();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			MyLog.i("cityName :" + cityName);
			String district = location.getDistrict();
			if (cityName.endsWith("自治州")) {
				cityName = district;
			}
			if (cityName.contains("市")) {
				cityName = cityName.substring(0, cityName.length() - 1);
			}
			if (cityName.contains("地区")) {
				cityName = cityName.substring(0, cityName.length() - 2);
			}
			if (cityName.equals("黔西南布依族苗族自治州")) {
				cityName = "兴义";
			}
			// 定位成功后发送广播
			if (cityName != null && cityName != "") {
				Intent intent = new Intent();
				intent.setAction(EnvironmentCurrentWeatherPullActivity.DING_WEIService);
				intent.putExtra("dingwei", location.getAddrStr());
				intent.putExtra("city", cityName);
				sendBroadcast(intent);
			}
			Toast.makeText(EnvironmentSelectCtiyActivity.this,
					"定位" + cityName + "成功", 1).show();
			mLocationClient.stop();
			City curCity = mCityDB.getCity(cityName);// 从数据库中找到该城市
			if (curCity != null) {
				if (!"".equals(cityName)) {
					WeiBaoApplication.addJPushAliasAndTags(
							getApplicationContext(), false, cityName);
				}
				if (mCityDB.isHaveLocation()) {
					mCityDB.deleteLocationCity();
				}
				if (mCityDB.addCityExist(cityName)) {
					mCityDB.updateLocation(cityName);
				}
				if (mCityDB.addCityExist(cityName)
						&& mCityDB.addCityExistAndLocation(cityName)) {
					if (load.equals("citymanager")) {
						Intent data = new Intent();
						data.putExtra("xuanze", cityName);
						setResult(20, data);
						overridePendingTransition(R.anim.exity_activity_enter,
								R.anim.exity_activity_enter);
						finish();
					} else {
						if (!isDingwei) {
							Intent intent = new Intent(
									EnvironmentSelectCtiyActivity.this,
									EnvironmentMainActivity.class);
							startActivity(intent);
							finish();
							isDingwei = true;
							return;
						}

					}
					return;
				}
				if (mCityDB.addCityExist(cityName)) {
					mCityDB.updateLocation(cityName);
					if (load.equals("citymanager")) {
						Intent data = new Intent();
						data.putExtra("xuanze", cityName);
						setResult(20, data);
						overridePendingTransition(R.anim.exity_activity_enter,
								R.anim.exity_activity_enter);
						finish();
						mCityDB.updateLocation(cityName);
					} else {
						if (!isDingwei) {
							Intent intent = new Intent(
									EnvironmentSelectCtiyActivity.this,
									EnvironmentMainActivity.class);
							startActivity(intent);
							finish();
							isDingwei = true;
							return;
						}
					}
				} else {
					mCityDB.addXuanZhecity1(cityName, "", "", true);
					if (load.equals("citymanager")) {
						Intent data = new Intent();
						data.putExtra("xuanze", cityName);
						setResult(20, data);
						// mApplication.setDingweicity(cityName);
						mApplication.setCurrentCityLatitude(latitude + "");
						mApplication.setCurrentCityLongitude(longitude + "");
						overridePendingTransition(R.anim.exity_activity_enter,
								R.anim.exity_activity_enter);
						finish();
						mCityDB.addXuanZhecity1(cityName, "", "", true);
					} else {
						if (!isDingwei) {
							Intent intent = new Intent(
									EnvironmentSelectCtiyActivity.this,
									EnvironmentMainActivity.class);
							startActivity(intent);
							finish();
							isDingwei = true;
							return;
						}
					}
				}

			} else {// 如果定位到的城市数据库中没有，也弹出定位失败
				// showLocationFailDialog();
				if (!WeiBaoApplication.selectedCity.equals("")) {
					Toast.makeText(EnvironmentSelectCtiyActivity.this,
							"请手动选择城市", 1).show();
				}
			}
		}

	};

	private TextView mHotCity;
	private GridView mGridView;
	EnvironmentSelectCtiyGridViewAdapter mGridViewAdapter;
	private List<String> preferenceCitys = new ArrayList<String>();

	private void initUI() {
		// TODO Auto-generated method stub

		final List<HotCityInfo> hotCityInfos = initHotCityInfo();
		mHotCity = (TextView) findViewById(R.id.hotcity);
		mGridView = (GridView) findViewById(R.id.city_gridview);
		mGridView.setVisibility(View.VISIBLE);

		mGridViewAdapter = new EnvironmentSelectCtiyGridViewAdapter(this,
				hotCityInfos, preferenceCitys);
		mGridView.setAdapter(mGridViewAdapter);
		mGridViewAdapter.notifyDataSetChanged();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HotCityInfo hotCityInfo = hotCityInfos.get(arg2);

				String cityName = hotCityInfo.getHotCityName();
				if (isf) {
					if (cityName.equals("自动定位")) {
						Toast.makeText(EnvironmentSelectCtiyActivity.this,
								"目前无法定位，请选择其他城市", 0).show();
						return;
					}
					mCityDB.addXuanZhecity1(cityName, "", "", false);
					Intent data = new Intent();
					data.putExtra("xuanze", cityName);
					// 把定位城市存入到缓存里
					// Util.sendLocationCityChangeBoradcast(SelectCtiyActivity.this,cityName);
					// mApplication.setDingweicity(cityName);
					sharedPref = getSharedPreferences("sharedPref",
							MODE_PRIVATE);
					editor = sharedPref.edit();
					editor.putString("dingweiCity", cityName);
					editor.commit();
					setResult(20, data);
					overridePendingTransition(R.anim.exity_activity_enter,
							R.anim.exity_activity_enter);
					finish();
					return;
				}
				if (cityName.equals("自动定位")) {
					if ("".equals(mApplication.getDingweicity())
							|| null == mApplication.getDingweicity()) {
						Toast.makeText(EnvironmentSelectCtiyActivity.this,
								"目前无法定位，请选择其他城市", 0).show();
						return;
					}
					cityName = mApplication.getDingweicity();
				}
				City city = mCityDB.getCityInfo(cityName);
				startActivity(city);
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	private void initView() {
		mTitleTextView = (TextView) findViewById(R.id.select_title_name);
		mBackBtn = (ImageView) findViewById(R.id.title_back);
		mBackBtn.setOnClickListener(this);
		mTitleTextView.setText("添加城市");

		mSearchEditText = (EditText) findViewById(R.id.search_edit);
		mSearchEditText.addTextChangedListener(this);
		mClearSearchBtn = (ImageView) findViewById(R.id.ib_clear_text);
		mClearSearchBtn.setOnClickListener(this);

		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		mSearchListView = (ListView) findViewById(R.id.search_list);
		mSearchListView.setEmptyView(findViewById(R.id.search_empty));
		mSearchListView.getEmptyView().setVisibility(View.GONE);
		mSearchContainer.setVisibility(View.GONE);
		mSearchListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mInputMethodManager.hideSoftInputFromWindow(
						mSearchEditText.getWindowToken(), 0);
				return false;
			}
		});

		mSearchListView
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// L.i(mSearchCityAdapter.getItem(position).toString());

						if (isf) {
							mCityDB.addXuanZhecity1(
									mSearchCityAdapter.getItem(position)
											.getName(), "", "", false);
							Intent data = new Intent();
							data.putExtra("xuanze",
									mSearchCityAdapter.getItem(position)
											.getName());
							setResult(20, data);
							overridePendingTransition(
									R.anim.exity_activity_enter,
									R.anim.exity_activity_enter);
							finish();
							return;
						}
						startActivity(mSearchCityAdapter.getItem(position));

					}
				});
	}

	private void startActivity(City city) {

		Intent i = new Intent();
		i.putExtra("city", city);
		setResult(RESULT_OK, i);
		finish();
	}

	private void initData() {
		mApplication = WeiBaoApplication.getInstance();
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mCities = mApplication.getCityList();
		mSections = mApplication.getSections();
		mMap = mApplication.getMap();
		mPositions = mApplication.getPositions();
		mIndexer = mApplication.getIndexer();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mSearchCityAdapter = new EnvironmentSearchCityAdapter(
				EnvironmentSelectCtiyActivity.this, mCities);
		mSearchListView.setAdapter(mSearchCityAdapter);
		mSearchListView.setTextFilterEnabled(true);
		mSearchListView.getEmptyView().setVisibility(View.GONE);

		if (mCities != null && (mCities.size() < 1 || TextUtils.isEmpty(s))) {
			mCityContainer.setVisibility(View.VISIBLE);
			mSearchContainer.setVisibility(View.INVISIBLE);
			mClearSearchBtn.setVisibility(View.GONE);
		} else {
			mClearSearchBtn.setVisibility(View.VISIBLE);
			mCityContainer.setVisibility(View.INVISIBLE);
			mSearchContainer.setVisibility(View.VISIBLE);
			mSearchCityAdapter.getFilter().filter(s);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_clear_text:
			if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
				mSearchEditText.setText("");
				mInputMethodManager.hideSoftInputFromWindow(
						mSearchEditText.getWindowToken(), 0);
			}
			break;
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetBroadcastReceiver.mListeners.remove(this);
	}

	@Override
	public void onNetChange() {
	}

}
