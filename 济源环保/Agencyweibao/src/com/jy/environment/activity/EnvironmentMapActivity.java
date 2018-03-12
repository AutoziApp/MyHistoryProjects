package com.jy.environment.activity;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.PollutionLevelCacUtils;

import okhttp3.Call;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.DatabaseUpgrate;
import com.jy.environment.model.MicroStationBean;
import com.jy.environment.model.PollutionOfMapBean;
import com.jy.environment.model.PollutionOfMapBean.DataBean.StationListBean;
import com.jy.environment.services.CustomLocationService;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.DateUtil;
import com.jy.environment.util.ScreenUtils;
import com.jy.environment.util.okUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class EnvironmentMapActivity extends ActivityBase implements
		OnClickListener {
	ProgressDialog progressDialog;
	private MapView mMapView;
	private LinearLayout change_map_layout,bottomLayout1,bottomLayout2;
	private ImageView change_map;
	private ImageView change_map_normol;
	private ImageView change_map_weixing;
	private TextView update_time;
	private ImageView img_location;
	private TextView tv_aqi;
	private TextView tv_pm25;
	private TextView tv_pm10;
	private TextView tv_so2;
	private TextView tv_no2;
	private TextView tv_o3;
	private TextView tv_co;
	private UiSettings settings;
	private RelativeLayout rl_index_btn;
	private TextView updataTime;
	private CustomLocationService locationService;
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	private TextView tvTemp;
	private TextView tvTemp2;

	// 污染物类型
	public static final String POLLUTION_TYPE_AQI = "aqi";
	public static final String POLLUTION_TYPE_PM2_5 = "pm25";
	public static final String POLLUTION_TYPE_PM10 = "pm10";
	public static final String POLLUTION_TYPE_SO2 = "so2";
	public static final String POLLUTION_TYPE_NO2 = "no2";
	public static final String POLLUTION_TYPE_O3 = "o3";
	public static final String POLLUTION_TYPE_CO = "co";
	
	//站点类型
	public static final String STATAION_TYPE_COUNTRY = "国控";//国控
	public static final String STATAION_TYPE_CITY= "市控";//市控
	public static final String STATAION_TYPE_MICOR = "微站";//微站
	// 当前站点类型
	private String currentStationType = "";
	// 当前污染物类型
	private String pollutionType = "";
	private Context mContext;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.environment_map_activity);
		progressDialog = new ProgressDialog(this);
		this.mContext = this;

		initView();
		initMapView();
		initListener();
		
		tag = getIntent().getStringExtra("TAG");
		if(tag.equals(EnvironmentMainActivity.class.getSimpleName())){
			img_location.setVisibility(View.VISIBLE);
			bottomLayout1.setVisibility(View.VISIBLE);
			rl_index_btn.setVisibility(View.VISIBLE);
			bottomLayout2.setVisibility(View.GONE);
			initData();
		}else if(tag.equals(MicroStationActivity.class.getSimpleName())){
			img_location.setVisibility(View.GONE);
			bottomLayout1.setVisibility(View.GONE);
			rl_index_btn.setVisibility(View.GONE);
			bottomLayout2.setVisibility(View.VISIBLE);
			drawCityBounds();
			MicroStationBean.DataBean.StationListBean stationListBean=
					(com.jy.environment.model.MicroStationBean.DataBean.StationListBean) getIntent().getSerializableExtra("StationListBean");
			PollutionOfMapBean.DataBean.StationListBean bean=new StationListBean();
			bean.setStationName(stationListBean.getStationName());
			bean.setStationCode(stationListBean.getStationCode());
			bean.setLat(Double.parseDouble(stationListBean.getLatitude()));
			bean.setLng(Double.parseDouble(stationListBean.getLongitude()));
			bean.setFactorValue(stationListBean.getFactorValue()+"");
			List<StationListBean> list=new ArrayList<PollutionOfMapBean.DataBean.StationListBean>();
			list.add(bean);
			initOverLay(list,getIntent().getStringExtra("type"));
		}
		
	}

	/**
	 * 
	 * @Description: 查找控件
	 * @param
	 * @author tianfy
	 */
	private void initView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		change_map = (ImageView) findViewById(R.id.change_map);
		change_map_layout = (LinearLayout) findViewById(R.id.change_map_layout);
		bottomLayout1=(LinearLayout) findViewById(R.id.bottom_layout1);
		bottomLayout2=(LinearLayout) findViewById(R.id.bottom_layout2);
		change_map_normol = (ImageView) findViewById(R.id.change_map_normol);
		change_map_weixing = (ImageView) findViewById(R.id.change_map_weixing);
		update_time = (TextView) findViewById(R.id.update_time);
		img_location = (ImageView) findViewById(R.id.map_location_button);
		
		
		tv_CountryControl = (TextView) findViewById(R.id.tv_country_control);
		tv_CityControl = (TextView) findViewById(R.id.tv_city_control);
		tv_MicroStataion = (TextView) findViewById(R.id.tv_micro_station);
		

		rl_index_btn = (RelativeLayout) findViewById(R.id.rl_map_index_btn);
		tv_aqi = (TextView) findViewById(R.id.tv_aqi_map);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25_map);
		tv_pm10 = (TextView) findViewById(R.id.tv_pm10_map);
		tv_so2 = (TextView) findViewById(R.id.tv_so2_map);
		tv_no2 = (TextView) findViewById(R.id.tv_no2_map);
		tv_o3 = (TextView) findViewById(R.id.tv_o3_map);
		tv_co = (TextView) findViewById(R.id.tv_co_map);
		
		updataTime = (TextView) findViewById(R.id.updateTime);

		progressDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 
	 * @Description: 初始化地图
	 * @param
	 * @author tianfy
	 */
	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 地图中心点设置(平顶山市) 初始缩放级别设置为9；
		LatLng center = new LatLng(35.065012, 112.392275);
		MapStatus ms = new MapStatus.Builder().target(center).zoom(10).build();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 普通地图

		mUiSettings = mBaiduMap.getUiSettings();// 获取UI控制器
		mUiSettings.setOverlookingGesturesEnabled(false);// 禁止俯视(3D视角)
		mUiSettings.setRotateGesturesEnabled(false);// 禁止旋转

		mMapView.showZoomControls(false);// 去除缩放按钮
	}

	/**
	 * 
	 * @Description: 初始化定位
	 * @param
	 * @author tianfy
	 */
	private void initLocation() {
		showToastShort("正在定位...");
		locationService = ((WeiBaoApplication) getApplication()).locationService;
		locationService.registerListener(mListener);
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		locationService.start();// 定位SDK
	}

	/**
	 * 
	 * @Description: 初始化监听
	 * @param
	 * @author tianfy
	 */
	private void initListener() {
		change_map.setOnClickListener(this);
		change_map_normol.setOnClickListener(this);
		change_map_weixing.setOnClickListener(this);
		img_location.setOnClickListener(this);
		tv_CountryControl.setOnClickListener(this);
		tv_CityControl.setOnClickListener(this);
		tv_MicroStataion.setOnClickListener(this);
		tv_aqi.setOnClickListener(this);
		tv_pm25.setOnClickListener(this);
		tv_pm10.setOnClickListener(this);
		tv_so2.setOnClickListener(this);
		tv_no2.setOnClickListener(this);
		tv_o3.setOnClickListener(this);
		tv_co.setOnClickListener(this);
		//初始化底部导航按钮
		tv_MicroStataion.setBackgroundColor(getResources().getColor(R.color.yyf_green));
		tv_MicroStataion.setTextColor(getResources().getColor(R.color.yyf_white));
		tvTemp2=tv_MicroStataion;
		tv_aqi.setBackgroundColor(getResources().getColor(R.color.yyf_green));
		tv_aqi.setTextColor(getResources().getColor(R.color.yyf_white));
		tvTemp = tv_aqi;
		mBaiduMap.setOnMarkerClickListener(markerClickLisener);
	}

	/**
	 * 初始化数据
	 * 
	 * @author tianfy
	 */
	private void initData() {
		// 默认当前污染类型为AQI 站点类型为国控
		pollutionType = POLLUTION_TYPE_AQI;
		currentStationType=STATAION_TYPE_MICOR;
		update_time.setText(DateUtil.getMillon(System.currentTimeMillis()));
		requestPDSdata(currentStationType,pollutionType);
	}

	/**
	 * 请求不同污染类型数据
	 * 
	 * @param pollutionType
	 *            污染类型
	 * @author tianfy
	 */
	private void requestPDSdata(final String currentStatonType,final String pollutionType) {
		// 清空地图
		clearMap();
		// 绘制市边界线
		drawCityBounds();
		progressDialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				okUtils.requestPollutionOfMap(currentStatonType, pollutionType, new PollutionDataCallback());
			}
		}).start();
		
//		showToastShort("当前站点类型为："+currentStatonType+"当前污染类型为："+pollutionType);
		// 请求数据
		// String url="";
		// Map<String, String> params=new HashMap<String, String>();
		// OkHttpUtils.post().params(params).url(url).build().execute(new
		// PollutionDataCallback());
	}

	/**
	 * 清空地图
	 * 
	 * @author tianfy
	 */
	private void clearMap() {
		mBaiduMap.clear();
		// progressDialog.show();
	}

	/**
	 * 绘制市边界线
	 * 
	 * @author tianfy
	 */
	private void drawCityBounds() {
		try {
			// 解析资产目录中平顶山城市边界的经纬度
			InputStream is = getAssets().open("py_jiyuan.json");
			int size = is.available();

			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			List<LatLng> pts = new ArrayList<LatLng>();

			String text = new String(buffer, "UTF-8");
			JSONObject jsonObject = new JSONObject(text);
			JSONArray jsonArray = jsonObject.getJSONArray("features")
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONArray("coordinates").getJSONArray(0);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray jsonArray2 = jsonArray.getJSONArray(i);
				double mLon = jsonArray2.getDouble(0);
				double mLat = jsonArray2.getDouble(1);

				LatLng pt = new LatLng(mLat, mLon);
				pts.add(pt);
			}
			// List<LatLng> pts =(List<LatLng>) msg.obj;
			OverlayOptions polygonOption = new PolygonOptions().points(pts)
					.stroke(new Stroke(5, 0xAA393323)).fillColor(0x00FFFFFF);
			// 在地图上添加多边形Option，用于显示
			mBaiduMap.addOverlay(polygonOption);
			// handler2.obtainMessage(2, pts).sendToTarget();

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class PollutionDataCallback extends StringCallback {

		@Override
		public void onError(Call arg0, Exception arg1, int arg2) {
			progressDialog.dismiss();
		}

		@Override
		public void onResponse(String arg0, int arg1) {
			progressDialog.dismiss();			
			PollutionOfMapBean bean=new Gson().fromJson(arg0, PollutionOfMapBean.class);
			if("true".equals(bean.getFlag())){//请求数据成功
				PollutionOfMapBean.DataBean dataBean=bean.getData();
				update_time.setText(dataBean.getUpdateTime());
				List<PollutionOfMapBean.DataBean.StationListBean> list=dataBean.getStationList();
				initOverLay(list,pollutionType);//添加marker
				
			}else{
				showToastShort("请求数据失败");
			}
		}

	}

	private BDLocationListener mListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {

			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				// 定位到当前位置
				showCurrentLocation(location);

				if (location.getLocType() == BDLocation.TypeServerError) {
					// 服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
					showToastShort("服务端网络定位失败!请稍后重试...");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					// 网络不同导致定位失败，请检查网络是否通畅"
					showToastShort("网络错误!请检查网络是否通畅...");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					// 无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
					showToastShort("网络错误!请检查网络是否通畅...");
				}
			} else {
				showToastShort("网络错误!请检查网络是否通畅...");
			}
		}

	};
	private TextView tv_CountryControl;
	private TextView tv_CityControl;
	private TextView tv_MicroStataion;

	/**
	 * 定位到当前位置
	 * 
	 * @param location
	 * @author tianfy
	 */
	private void showCurrentLocation(BDLocation location) {
		//YYF 联网转换坐标
//		convertCoord(location);
		
		MyLocationData myLocation = new MyLocationData.Builder()
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.accuracy(location.getRadius())
				.direction(location.getDirection()).build();
		mBaiduMap.setMyLocationData(myLocation);
		showToastShort("定位成功");
		// 设置缩放级别
		LatLng cenpt = new LatLng(location.getLatitude(),
				location.getLongitude());
		MapStatus ms = new MapStatus.Builder().zoom(9).target(cenpt).build();
		mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
	}

	/**
	 * 联网转换坐标
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @return 含逗号的string串
	 */
	private void convertCoord(final BDLocation location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		final String url = "http://api.map.baidu.com/geoconv/v1/?coords="+longitude+","+latitude +
				"&from=1&to=5&ak=3OtEjqk0yPCshqNv6iLxsUu2WiYt0Yma&mcode=AF:1F:52:76:37:8F:8C:84:43:ED:29:68:CB:A9:3D:35:97:1E:F9:20;com.jy.environment";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				okUtils.get(url, new StringCallback() {
					
					@Override
					public void onResponse(String result, int id) {
						if (id == 0) {
							double lon = 0;
							double lat = 0;
							try {
								JSONObject jsonObject = new JSONObject(result);
								JSONArray al = (JSONArray) jsonObject
										.get("result");
								JSONObject bean = (JSONObject) al.get(0);
								lon = (Double) bean.get("x");
								lat = (Double) bean.get("y");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							MyLocationData myLocation = new MyLocationData.Builder()
									.latitude(lat).longitude(lon)
									.accuracy(location.getRadius())
									.direction(location.getDirection()).build();
							mBaiduMap.setMyLocationData(myLocation);
							showToastShort("定位成功");
							// 设置缩放级别
							LatLng cenpt = new LatLng(lat, lon);
							MapStatus ms = new MapStatus.Builder().zoom(9)
									.target(cenpt).build();
							mMapView.getMap().setMapStatus(
									MapStatusUpdateFactory.newMapStatus(ms));

						}else{
							showToastShort("定位异常,请重试");
						}
					}
					
					@Override
					public void onError(Call call, Exception e, int id) {
						MyLocationData myLocation = new MyLocationData.Builder()
								.latitude(location.getLatitude())
								.longitude(location.getLongitude())
								.accuracy(location.getRadius())
								.direction(location.getDirection()).build();
						mBaiduMap.setMyLocationData(myLocation);
						showToastShort("请求失败,请重试");
						// 设置缩放级别
						LatLng cenpt = new LatLng(location.getLatitude(),
								location.getLongitude());
						MapStatus ms = new MapStatus.Builder().zoom(9)
								.target(cenpt).build();
						mMapView.getMap().setMapStatus(
								MapStatusUpdateFactory.newMapStatus(ms));
					}
				});
			}
		}).start();
	}

	public void initOverLay(List<StationListBean> list,String type) {
		// TODO Auto-generated method stub
		if(list==null||list.size()==0)
			return;
		for(StationListBean bean:list){
			LatLng latlng = new LatLng(bean.getLat(), bean.getLng());
			TextView tv=new TextView(getApplicationContext());
//						tv.setBackgroundColor(CommonUtil.getColorByAQI(bean.getFactorValue()));
						tv.setBackgroundColor(PollutionLevelCacUtils.getHourLevelColor(type,Double.parseDouble(bean.getFactorValue())));
						tv.setTextColor(Color.WHITE);
						if(bean.getFactorValue().contains(".")){
							BigDecimal bigDecimal = new BigDecimal(bean.getFactorValue());
							BigDecimal setScale = bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP);
							tv.setText(setScale+"");
						}else{
							tv.setText(bean.getFactorValue()+"");
						}
			OverlayOptions ooA = new MarkerOptions().position(latlng)
													.icon(BitmapDescriptorFactory.fromView(tv))
													.zIndex(9)
													.draggable(true);
			Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
			Bundle bundle=new Bundle();
			bundle.putSerializable("obj",bean);
			marker.setExtraInfo(bundle);
		
		}
		
	}

	OnMarkerClickListener markerClickLisener=new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			// TODO Auto-generated method stub
			Bundle bundle=marker.getExtraInfo();
			StationListBean bean=(StationListBean) bundle.getSerializable("obj");
//			showToastShort(bean.getStationName());
			Intent intent=new Intent(EnvironmentMapActivity.this,StationDetailActivity.class);
			intent.putExtra("stationCode", bean.getStationCode());
			intent.putExtra("stationName", bean.getStationName());
			startActivity(intent);
			return true;
		}
	};
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.change_map:
			switchChangeMap();
			break;
		case R.id.change_map_normol:
			mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
			change_map_layout.setVisibility(View.GONE);
			change_map_normol.setImageResource(R.drawable.map_img1_checked);
			change_map_weixing.setImageResource(R.drawable.map_img2);
			break;
		case R.id.change_map_weixing:
			mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			change_map_layout.setVisibility(View.GONE);
			change_map_normol.setImageResource(R.drawable.map_img1);
			change_map_weixing.setImageResource(R.drawable.map_img2_checked);
			break;
		case R.id.map_location_button:
			initLocation();
			// zoomLevel = mBaiduMap.getMapStatus().zoom;
			// mapDataType = MAP_DATA_TYPE_POINT;
			// showMarker();
			break;
		case R.id.tv_country_control:
			tv_CountryControl.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_CountryControl.setTextColor(getResources().getColor(R.color.yyf_white));
			if (currentStationType==STATAION_TYPE_COUNTRY)
				return;
			tvTemp2.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp2.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp2=tv_CountryControl;

			currentStationType=STATAION_TYPE_COUNTRY;
			break;
		case R.id.tv_city_control:
			tv_CityControl.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_CityControl.setTextColor(getResources().getColor(R.color.yyf_white));
			if (currentStationType==STATAION_TYPE_CITY)
				return;
			tvTemp2.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp2.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp2=tv_CityControl;
			currentStationType=STATAION_TYPE_CITY;
			break;
		case R.id.tv_micro_station:
			tv_MicroStataion.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_MicroStataion.setTextColor(getResources().getColor(R.color.yyf_white));
			if (currentStationType==STATAION_TYPE_MICOR)
				return;
			tvTemp2.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp2.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp2=tv_MicroStataion;

			currentStationType=STATAION_TYPE_MICOR;
			
			break;
			
		case R.id.tv_aqi_map:
			//改变按钮状态
			tv_aqi.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_aqi.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_AQI)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_aqi;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_AQI;
//			showMarker();
			break;
		case R.id.tv_pm25_map:
			//改变按钮状态
			tv_pm25.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_pm25.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_PM2_5)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_pm25;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_PM2_5;
//			showMarker();
			break;
		case R.id.tv_pm10_map:
			//改变按钮状态
			tv_pm10.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_pm10.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_PM10)
				return;
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_pm10;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_PM10;
//			showMarker();
			break;
		case R.id.tv_so2_map:
			//改变按钮状态
			tv_so2.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_so2.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_SO2)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_so2;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_SO2;
//			showMarker();
			break;
		case R.id.tv_no2_map:
			//改变按钮状态
			tv_no2.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_no2.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_NO2)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_no2;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_NO2;
//			showMarker();
			break;
		case R.id.tv_o3_map:
			//改变按钮状态
			tv_o3.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_o3.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_O3)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_o3;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_O3;
//			showMarker();
			break;
		case R.id.tv_co_map:
			//改变按钮状态
			tv_co.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_co.setTextColor(getResources().getColor(R.color.yyf_white));
			if (pollutionType == POLLUTION_TYPE_CO)
				return;
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_co;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);

			pollutionType = POLLUTION_TYPE_CO;
//			showMarker();
			break;
		default:
			break;
		}
		if(view.getId()==R.id.change_map||view.getId()==R.id.change_map_normol||view.getId()==R.id.change_map_weixing)
			return;
		requestPDSdata(currentStationType,pollutionType);
	}

	private void switchChangeMap() {
		if (change_map_layout.getVisibility() == View.VISIBLE) {
			change_map_layout.setVisibility(View.GONE);
		} else {
			change_map_layout.setVisibility(View.VISIBLE);
		}
	}


	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		if (locationService!=null) {
			locationService.unregisterListener(mListener); // 注销掉监听
			locationService.stop(); // 停止定位服务
		}
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
	}
	
	private long exitTime = 0;
	private String tag;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (tag.equals(MicroStationActivity.class.getSimpleName())) {
			return super.onKeyDown(keyCode, event);
		}
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
}
