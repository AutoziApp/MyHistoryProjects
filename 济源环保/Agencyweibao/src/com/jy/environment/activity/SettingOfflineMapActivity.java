package com.jy.environment.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;
import com.umeng.analytics.MobclickAgent;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.map.MKOLSearchRecord;
//import com.baidu.mapapi.map.MKOLUpdateElement;
//import com.baidu.mapapi.map.MKOfflineMap;
//import com.baidu.mapapi.map.MKOfflineMapListener;
//import com.baidu.mapapi.map.MapController;

/**
 * 离线地图下载
 * 
 * @author baiyuchuan
 * 
 */
public class SettingOfflineMapActivity extends ActivityBase implements
		MKOfflineMapListener, OnClickListener {

	// private MapView mMapView = null;
	private MKOfflineMap mOffline = null;
	private TextView cidView;
	private TextView stateView;
	private EditText cityNameView;
	// private Button clbtn,localbtn;
	private ImageView clbtn, localbtn;
	// private MapController mMapController = null;
	private ImageView back;
	/**
	 * 已下载的离线地图信息列表
	 */
	private ArrayList<MKOLUpdateElement> localMapList = null;
	private ArrayList<MKOLSearchRecord> hotcityMapList = null;
	private ArrayList<MKOLSearchRecord> allcityMapList = null;
	private LocalMapAdapter lAdapter = null;

	ListView localMapListView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// WeiBaoApplication app = (WeiBaoApplication)this.getApplication();
		// if (app.mBMapManager == null) {
		// app.mBMapManager = new BMapManager(this);
		// app.mBMapManager.init(WeiBaoApplication.strKey,new
		// WeiBaoApplication.MyGeneralListener());
		// }
		setContentView(R.layout.setting_offline_activity);
		// mMapView = new MapView(this);
		// mMapController = mMapView.getController();

		mOffline = new MKOfflineMap();
		/**
		 * 初始化离线地图模块,MapControler可从MapView.getController()获取
		 */
		mOffline.init(this);
		initView();

		for (int i = 0; i < localMapList.size(); i++) {
			MKOLUpdateElement e = localMapList.get(i);

			if (e.ratio < 100) {
				mOffline.remove(e.cityID);
			}
		}

	}

	private void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);

	}

	private void initView() {

		cidView = (TextView) findViewById(R.id.cityid);
		cityNameView = (EditText) findViewById(R.id.city);
		stateView = (TextView) findViewById(R.id.state);
		clbtn = (ImageView) findViewById(R.id.clButton);
		localbtn = (ImageView) findViewById(R.id.localButton);
		back = (ImageView) findViewById(R.id.login_return_iv);
		back.setOnClickListener(this);

		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}

		hotcityMapList = mOffline.getHotCityList();
		if (hotcityMapList == null) {
			hotcityMapList = new ArrayList<MKOLSearchRecord>();
		}

		allcityMapList = mOffline.getOfflineCityList();
		if (allcityMapList == null) {
			allcityMapList = new ArrayList<MKOLSearchRecord>();
		}

		ListView hotCityList = (ListView) findViewById(R.id.hotcitylist);

		HotCityMapAdapter hcAdapter = new HotCityMapAdapter();
		hotCityList.setAdapter(hcAdapter);
		setListViewHeight(hotCityList);

		ListView allCityList = (ListView) findViewById(R.id.allcitylist);
		AllCityMapAdapter acAdapter = new AllCityMapAdapter();
		allCityList.setAdapter(acAdapter);
		setListViewHeight(allCityList);

		LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
		LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
		lm.setVisibility(View.GONE);
		cl.setVisibility(View.VISIBLE);

		localMapListView = (ListView) findViewById(R.id.localmaplist);
		lAdapter = new LocalMapAdapter();
		localMapListView.setAdapter(lAdapter);

	}

	/**
	 * 切换至城市列表
	 * 
	 * @param view
	 */
	public void clickCityListButton(View view) {
		LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
		LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
		lm.setVisibility(View.GONE);
		cl.setVisibility(View.VISIBLE);
		Drawable background = getResources()
				.getDrawable(R.drawable.city_normal);
		clbtn.setBackgroundDrawable(background);
		background = getResources().getDrawable(R.drawable.down_press);
		localbtn.setBackgroundDrawable(background);
	}

	/**
	 * 切换至下载管理列表
	 * 
	 * @param view
	 */
	public void clickLocalMapListButton(View view) {
		LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
		LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
		lm.setVisibility(View.VISIBLE);
		cl.setVisibility(View.GONE);
		Drawable background = getResources().getDrawable(R.drawable.city_press);
		clbtn.setBackgroundDrawable(background);
		background = getResources().getDrawable(R.drawable.down_normal);
		localbtn.setBackgroundDrawable(background);
	}

	boolean goOnWithNoWIFI = false;
	int cityidWithNoWIFI = 0;

	/**
	 * 检测当前的网络状态，如果是未联网，提示；如果联网但未开启wifi提示是否继续，
	 * 
	 * @param context
	 * @return
	 */
	private boolean checkNet(Context context, int cityid) {

		if (WbMapUtil.isOpenNetwork(context)) {
			if (WbMapUtil.isWifiConn(context)) {
				return true;
			} else {
				// wifi没有打开，弹出提示框
				cityidWithNoWIFI = cityid;
				new AlertDialog.Builder(context)
						.setTitle("流量提醒")
						.setMessage("WIFI尚未开启，下载过程可能产生流量费用，请确认是否继续？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										goOnWithNoWIFI = true;
										// 执行下载任务
										mOffline.start(cityidWithNoWIFI);
										clickLocalMapListButton(null);
										Toast.makeText(
												SettingOfflineMapActivity.this,
												"开始下载离线地图. 城市: "
														+ cityidWithNoWIFI,
												Toast.LENGTH_SHORT).show();
										updateView();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										goOnWithNoWIFI = false;
										MyLog.i("offlinemap-checknet" + "2");
									}
								}).show();
				MyLog.i("offlinemap-checknet" + "3");
				return false;
			}
		} else {
			//
			Toast.makeText(context, "请检查网络后重试！", 1000).show();
			return false;
		}
	}

	/**
	 * 搜索离线需市
	 * 
	 * @param view
	 */
	public void search(View view) {
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityNameView
				.getText().toString());
		if (records == null || records.size() != 1)
			return;
		cidView.setText(String.valueOf(records.get(0).cityID));
	}

	/**
	 * 开始下载
	 * 
	 * @param view
	 */
	public void start(View view) {
		int cityid = Integer.parseInt(cidView.getText().toString());
		mOffline.start(cityid);
		clickLocalMapListButton(null);
		Toast.makeText(this, "开始下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 暂停下载
	 * 
	 * @param view
	 */
	public void stop(View view) {
		int cityid = Integer.parseInt(cidView.getText().toString());
		mOffline.pause(cityid);
		Toast.makeText(this, "暂停下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 删除离线地图
	 * 
	 * @param view
	 */
	public void remove(View view) {
		int cityid = Integer.parseInt(cidView.getText().toString());
		mOffline.remove(cityid);
		Toast.makeText(this, "删除离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 从SD卡导入离线地图安装包
	 * 
	 * @param view
	 */
	public void importFromSDCard(View view) {
		int num = mOffline.importOfflineData();
		String msg = "";
		if (num == 0) {
			msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
		} else {
			msg = String.format("成功导入 %d 个离线包，可以在下载管理查看", num);
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 更新状态显示
	 */
	public void updateView() {
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		int cityid = Integer.parseInt(cidView.getText().toString());
		// mOffline.pause(cityid);
		// mMapView.onPause();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// mMapView.onResume();
		for (int i = 0; i < localMapList.size(); i++) {
			MKOLUpdateElement e = localMapList.get(i);

		}
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	@Override
	protected void onDestroy() {
		for (int i = 0; i < localMapList.size(); i++) {
			MKOLUpdateElement e = localMapList.get(i);
			if (e.ratio < 100) {
				mOffline.remove(e.cityID);
			}
		}
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		// mMapView.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {
				stateView.setText(String.format("%s : %d%%", update.cityName,
						update.ratio));
				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装

			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		}

	}

	/**
	 * 离线地图管理列表适配器
	 */
	public class LocalMapAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return localMapList.size();
		}

		@Override
		public Object getItem(int index) {
			return localMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			try {
				MyLog.i("weibao result:" + (MKOLUpdateElement) getItem(index));
				MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
				view = View.inflate(SettingOfflineMapActivity.this,
						R.layout.setting_offline_localmap_list, null);
				initViewItem(view, e);
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return view;
		}

		void initViewItem(View view, final MKOLUpdateElement e) {
			Button display = (Button) view.findViewById(R.id.display);
			// Button remove = (Button)view.findViewById(R.id.remove);
			ImageView remove = (ImageView) view.findViewById(R.id.remove);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView update = (TextView) view.findViewById(R.id.update);
			TextView ratio = (TextView) view.findViewById(R.id.ratio);
			ratio.setText(e.ratio + "%");
			title.setText(e.cityName);
			if (e.update) {
				update.setText("可更新");
			} else {
				update.setText("最新");
			}
			if (e.ratio != 100) {
				display.setEnabled(false);
			} else {
				display.setEnabled(true);
			}
			remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mOffline.remove(e.cityID);
					updateView();
				}
			});
			display.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		}

	}

	/**
	 * 热门城市列表适配器
	 */
	public class HotCityMapAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return hotcityMapList.size();
		}

		@Override
		public Object getItem(int index) {
			return hotcityMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			try {
				MyLog.i("weibao result:" + (MKOLSearchRecord) getItem(index));
				MKOLSearchRecord e = (MKOLSearchRecord) getItem(index);
				view = View.inflate(SettingOfflineMapActivity.this,
						R.layout.setting_offline_hotcity_list, null);
				initViewItem(view, e);
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return view;
		}

		void initViewItem(View view, final MKOLSearchRecord e) {
			// Button controlTxt = (Button)view.findViewById(R.id.control);
			ImageView controlTxt = (ImageView) view.findViewById(R.id.control);
			TextView cityTxt = (TextView) view.findViewById(R.id.city);
			TextView sizeTxt = (TextView) view.findViewById(R.id.size);
			cityTxt.setText(e.cityName);
			sizeTxt.setText(formatDataSize(e.size));

			controlTxt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//
					if (checkNet(SettingOfflineMapActivity.this, e.cityID) == false) {
						return;
					}
					// 执行下载任务
					mOffline.start(e.cityID);
					clickLocalMapListButton(null);
					Toast.makeText(SettingOfflineMapActivity.this,
							"开始下载离线地图. 城市: " + e.cityName, Toast.LENGTH_SHORT)
							.show();
					updateView();
				}
			});

		}

	}

	/**
	 * 所有城市列表适配器
	 */
	public class AllCityMapAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return allcityMapList.size();

		}

		@Override
		public Object getItem(int index) {
			return allcityMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			try {
				MyLog.i("weibao result:" + (MKOLSearchRecord) getItem(index));
				MKOLSearchRecord e = (MKOLSearchRecord) getItem(index);
				view = View.inflate(SettingOfflineMapActivity.this,
						R.layout.setting_offline_hotcity_list, null);
				initViewItem(view, e);
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return view;
		}

		void initViewItem(View view, final MKOLSearchRecord e) {
			// Button controlTxt = (Button)view.findViewById(R.id.control);
			ImageView controlTxt = (ImageView) view.findViewById(R.id.control);
			TextView cityTxt = (TextView) view.findViewById(R.id.city);
			TextView sizeTxt = (TextView) view.findViewById(R.id.size);
			cityTxt.setText(e.cityName);
			sizeTxt.setText(formatDataSize(e.size));

			controlTxt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 执行下载任务
					mOffline.start(e.cityID);
					clickLocalMapListButton(null);
					Toast.makeText(SettingOfflineMapActivity.this,
							"开始下载离线地图. 城市: " + e.cityName, Toast.LENGTH_SHORT)
							.show();
					updateView();
				}
			});

		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login_return_iv:
			finish();
		}

	}

}