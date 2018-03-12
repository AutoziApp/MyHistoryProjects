package cn.com.mapuni.meshing.activity.db_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.mapuni.meshing.activity.gis.MapTdtFragment;
import cn.com.mapuni.meshing.adapter.CommonAdapter;
import cn.com.mapuni.meshing.adapter.DbNewsAdapyer;
import cn.com.mapuni.meshing.adapter.XiaFaTaskAdapter;
import cn.com.mapuni.meshing.model.PotrlObject;
import cn.com.mapuni.meshing.model.TodoTaskModel;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.MapView.LayoutParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("Instantiatable")
public class NewDbMainActivity implements View.OnClickListener, Callback,
		SwipeRefreshLayout.OnRefreshListener {
	SwipeRefreshLayout swipeRefreshLayout;// 刷新布局
	CommonAdapter adapter;
	ListView listView;
	private Context Mcontent;
	private FrameLayout Mview;
	private MapView mapView;
	TextView showHind;
	LinearLayout news, liebiao;
	MapController mMapController;
	private View mainView;
	private OverItemT mOverlay = null;
	private ImageView ivClose;
	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	// List<TodoTaskModel> data = new ArrayList<TodoTaskModel>();
	List<HashMap<String, Object>> newData = new ArrayList<HashMap<String, Object>>();
	private String url;

	public NewDbMainActivity(Context mcontent, FrameLayout mview,
			MapView mapView) {
		mapView.removeAllOverlay();// 清空所有覆盖物
		mview.removeAllViews();// 清除界面所有视图
		this.Mcontent = mcontent;
		this.Mview = mview;
		this.mapView = mapView;
		yutuLoading = new YutuLoading(Mcontent);
	}

	public void showView() {
		initRefreshLayout();
		preInitView();
		getDbTaskList();
	}

	private void initRefreshLayout() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(Mcontent);
		// 初始化界面
		mainView = inflater.inflate(R.layout.newdbmainactivity_layout, null);
		Mview.addView(mainView);
		showHind = (TextView) mainView.findViewById(R.id.showhind);
		liebiao = (LinearLayout) mainView.findViewById(R.id.liebiao);
		news = (LinearLayout) mainView.findViewById(R.id.news);
		showHind.setOnClickListener(NewDbMainActivity.this);
		swipeRefreshLayout = (SwipeRefreshLayout) mainView
				.findViewById(R.id.refresh_layout);
		// 设置刷新时动画的颜色，可以设置4个
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);

		swipeRefreshLayout.setOnRefreshListener(this);
	}

	public void preInitView() {
		// 设置地图默认中心点
		mMapController = mapView.getController();
		GeoPoint point = new GeoPoint((int) (36.687105 * 1E6),
				(int) (117.036054 * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(16);
	}

	YutuLoading yutuLoading = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getXiafaList();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * http://192.168.15.64:8080/JiNanhuanbaoms/task/selDaochuli.do?userid=123
	 */
	private void getDbTaskList() {
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在获取待办任务数据，请稍候", "");
		yutuLoading.setCancelable(false);
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sesssionID = DisplayUitl.readPreferences(Mcontent,
				LAST_USER_SP_NAME, "sessionId");
		if (DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "user")
				.length() > 11) {
			url = PathManager.SIJI_XUCHAYUAN_URL + "?sessionId=" + sesssionID;
		} else {
			url = PathManager.GETDBTASK_URL_JINAN + "?sessionId=" + sesssionID;
		}
		utils.send(HttpMethod.GET, url, /* params, */
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						if (null != listView) {
							listView.setAdapter(null);
						}
						Toast.makeText(Mcontent, "数据请求失败", 200).show();
						swipeRefreshLayout.setRefreshing(false);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// if (null != yutuLoading) {
						// yutuLoading.dismissDialog();
						// }
						String resoult = String.valueOf(arg0.result);
						try {
							Gson gson = new Gson();
							JSONObject jsonObject = new JSONObject(resoult);
							String status = jsonObject.getString("status");
							if (status.contains("200")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("rows");

								List<TodoTaskModel> retList = gson.fromJson(
										jsonArray.toString(),
										new TypeToken<List<TodoTaskModel>>() {
										}.getType());
								newData = new ArrayList<HashMap<String, Object>>();
								HashMap<String, Object> map = null;
								for (int i = 0; i < retList.size(); i++) {
									map = new HashMap<String, Object>();
									map.put("name", retList.get(i)
											.getTaskName());
									map.put("rwly", "巡查任务");
									map.put("content", retList.get(i));
									newData.add(map);
								}
								handler.sendEmptyMessage(0);
							}
						} catch (Exception e) {
							if (null != yutuLoading) {
								yutuLoading.dismissDialog();
							}
							// TODO Auto-generated catch block
							Toast.makeText(Mcontent, "数据请求失败", 200).show();
							swipeRefreshLayout.setRefreshing(false);
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 获取登陆用户的待办任务数据。接口址址：/work/task/list.do
	 */
	private void getXiafaList() {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sesssionID = DisplayUitl.readPreferences(Mcontent,
				LAST_USER_SP_NAME, "sessionId");
		String url = PathManager.GETXFTASK_URL_JINAN + "?sessionId="
				+ sesssionID;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				swipeRefreshLayout.setRefreshing(false);
				Toast.makeText(Mcontent, "数据请求失败", 200).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				try {
					Gson gson = new Gson();
					JSONObject jsonObject = new JSONObject(resoult);
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						JSONArray jsonArray = jsonObject.getJSONArray("rows");

						List<XiaFaTaskModel> retList = gson.fromJson(
								jsonArray.toString(),
								new TypeToken<List<XiaFaTaskModel>>() {
								}.getType());

						HashMap<String, Object> map = null;
						for (int i = 0; i < retList.size(); i++) {
							map = new HashMap<String, Object>();
							map.put("name", retList.get(i).getTaskName());
							map.put("rwly", "计划任务");
							map.put("content", retList.get(i));
							newData.add(map);
						}
						initView();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Mcontent, "数据请求失败", 200).show();
					e.printStackTrace();
				}
				swipeRefreshLayout.setRefreshing(false);

			}
		});
	}

	private void initView() {
		listView = (ListView) mainView.findViewById(R.id.lv);

		adapter = new CommonAdapter(Mcontent, newData);
		listView.setAdapter(adapter);

		news.setVisibility(View.VISIBLE);
		listView.setVisibility(View.VISIBLE);

		List<Overlay> list = mapView.getOverlays();

		Drawable marker = Mcontent.getResources().getDrawable(
				R.drawable.poi_xml);
		mOverlay = new OverItemT(marker);

		list.add(mOverlay);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				if (newData.get(i).get("rwly").equals("巡查任务")) {
					// 巡查任务点击事件
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					mOverlay.setLastPoint(i);
				} else {
					// 计划任务点击事件
					Intent intent = new Intent(Mcontent,
							XiaFaDetailActivity.class);
					intent.putExtra("xiaFaTaskModel", (XiaFaTaskModel) newData
							.get(i).get("content"));
					Mcontent.startActivity(intent);
				}
			}
		});
		swipeRefreshLayout.setRefreshing(false);
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {
		List<TodoTaskModel> xuncha_temp = new ArrayList<TodoTaskModel>();// 巡查任务全局

		public OverItemT(Drawable marker) {
			super(boundCenterBottom(marker));
			mGeoList.clear();
			for (int i = 0; i < newData.size(); i++) {
				if (newData.get(i).get("rwly").equals("巡查任务")) {
					TodoTaskModel model = (TodoTaskModel) newData.get(i).get(
							"content");
					double lon = Double.parseDouble(model.getX());// 经度
					double lat = Double.parseDouble(model.getY());// 纬度
					if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
						continue;
					}
					if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
						continue;
					}
					OverlayItem item = new OverlayItem(new GeoPoint((int) (lat * 1E6),
							(int) (lon * 1E6)), "P" + i,
							"point" + i);
					item.setMarker(marker);
					mGeoList.add(item);
					xuncha_temp.add(model);
				}
			}
			populate();

		}

		public void setLastPoint(int i) {// 巡查任务弹出窗口
			final TodoTaskModel model = (TodoTaskModel) newData.get(i).get(
					"content");
			double lon = Double.parseDouble(model.getX());// 经度
			double lat = Double.parseDouble(model.getY());// 纬度
			if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
				Toast.makeText(Mcontent, "经纬度错误,定位失败", 200).show();
				return;
			}
			if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
				Toast.makeText(Mcontent, "经纬度错误,定位失败", 200).show();
				return;
			}
			
			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
			mMapController.setCenter(point);
			mMapController.setZoom(16);
			
			mapView.updateViewLayout(MapTdtFragment.dbPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
					-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			MapTdtFragment.dbPopView.setVisibility(View.VISIBLE);
			
			// 任务名称
			TextView tv_taskName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_taskName);
			if (!TextUtils.isEmpty(model.getTaskName())) {
				tv_taskName.setText(model.getTaskName());
			}
			// 巡查人员
			TextView tv_createUserName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createUserName);
			if (!TextUtils.isEmpty(model.getCreateUserName())) {
				tv_createUserName.setText(model.getCreateUserName());
			}
			// 创建时间
			TextView tv_createTime = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createTime);
			if (!TextUtils.isEmpty(model.getCreateTime())) {
				tv_createTime.setText(model.getCreateTime());
			}
			// 网格名称
			TextView tv_createGridName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createGridName);
			if (!TextUtils.isEmpty(model.getCreateGridName())) {
				tv_createGridName.setText(model.getCreateGridName());
			}

			TextView upload = (TextView) MapTdtFragment.dbPopView.findViewById(R.id.upload);
			upload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					Intent intent = new Intent(Mcontent, DbscfkActivity.class);
					intent.putExtra("id", model.getId());// post
					intent.putExtra("currenStatus", model.getStatus()); // submit
					intent.putExtra("patrolId", model.getPatrolId());
					intent.putExtra("judgeTura", model.getJudgeTura());
					Mcontent.startActivity(intent);
					MapTdtFragment.dbPopView.setVisibility(View.GONE);
				}
			});
			ivClose = (ImageView) MapTdtFragment.dbPopView.findViewById(R.id.close);
			ivClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MapTdtFragment.dbPopView.setVisibility(View.GONE);
				}
			});
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		protected boolean onTap(int i) {
			if (i == -1) {
				return true;
			}
			final TodoTaskModel model = xuncha_temp.get(i);
			// 任务名称
			TextView tv_taskName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_taskName);
			if (!TextUtils.isEmpty(model.getTaskName())) {
				tv_taskName.setText(model.getTaskName());
			}
			// 巡查人员
			TextView tv_createUserName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createUserName);
			if (!TextUtils.isEmpty(model.getCreateUserName())) {
				tv_createUserName.setText(model.getCreateUserName());
			}
			// 创建时间
			TextView tv_createTime = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createTime);
			if (!TextUtils.isEmpty(model.getCreateTime())) {
				tv_createTime.setText(model.getCreateTime());
			}
			// 网格名称
			TextView tv_createGridName = (TextView) MapTdtFragment.dbPopView
					.findViewById(R.id.tv_createGridName);
			if (!TextUtils.isEmpty(model.getCreateGridName())) {
				tv_createGridName.setText(model.getCreateGridName());
			}

			double lat = Double.parseDouble(model.getX());
			double lon = Double.parseDouble(model.getY());

			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
			mMapController.setCenter(point);
			mMapController.setZoom(16);

			TextView upload = (TextView) MapTdtFragment.dbPopView.findViewById(R.id.upload);
			upload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					Intent intent = new Intent(Mcontent, DbscfkActivity.class);
					intent.putExtra("id", model.getId());// post
					intent.putExtra("currenStatus", model.getStatus()); // submit
					intent.putExtra("patrolId", model.getPatrolId());
					intent.putExtra("judgeTura", model.getJudgeTura());
					Mcontent.startActivity(intent);
					MapTdtFragment.dbPopView.setVisibility(View.GONE);
				}
			});
			ivClose = (ImageView) MapTdtFragment.dbPopView.findViewById(R.id.close);
			ivClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MapTdtFragment.dbPopView.setVisibility(View.GONE);
				}
			});

			GeoPoint pt = mGeoList.get(i).getPoint();
			mapView.updateViewLayout(MapTdtFragment.dbPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
					-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			MapTdtFragment.dbPopView.setVisibility(View.VISIBLE);

			return true;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView1) {
			return super.onTap(p, mapView);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.showhind:
			if (liebiao.getVisibility() == View.VISIBLE) {
				liebiao.setVisibility(View.GONE);
			} else {
				liebiao.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		preInitView();
		getDbTaskList();
	}

}
