package com.jy.environment.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
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
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.map.MapPopOfCity;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.LaLngData;
import com.jy.environment.model.ProvinceCity;
import com.jy.environment.model.ProvinceCityData;
import com.jy.environment.model.ProvincePoint;
import com.jy.environment.services.LocationService;
import com.jy.environment.webservice.UrlComponent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;

public class MapMainNewActivity extends ActivityBase implements OnClickListener {

	private String[] hnCityArrays = new String[] {
//			"郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市",
//			"许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市", "济源市", "巩义市", "兰考县", "汝州市", "滑县", "长垣县", "邓州市",
//			"永城市", "固始县", "鹿邑县", "新蔡县" 
			"焦作", "修武县", "博爱县", "武陟县", "温县", "泌阳县", "孟州"
			};
	private MapView mMapView;
	private LinearLayout change_map_layout;
	private ImageView change_map;
	private ImageView change_map_normol;
	private ImageView change_map_weixing;
	private TextView update_time;
	private ImageView img_location;
	private UiSettings settings;
	private RelativeLayout rl_index_btn;
	private TextView tv_aqi;
	private TextView tv_pm25;
	private TextView tv_pm10;
	private TextView tv_so2;
	private TextView tv_no2;
	private TextView tv_o3;
	private TextView tv_co;
	private BaiduMap mBaiduMap;
	private boolean showlocation = false;
	private LocationClient mLocationClient;
	private SDKReceiver mReceiver;
	private boolean islocationing = false;
	private ExecutorService threadPool;
	private InfoWindow pop = null;
	private TextView tvTemp;
	
	private TextView updataTime;
	
	//全国数据
	List<AQIPoint>  quanGuoData;
	//全省数据
	List<AQIPoint>  quanShengData;
	//全省点信息
	List<ProvincePoint> provincePoints;
	
	
	ProgressDialog progressDialog;
	

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
				//此处优化全国数据的Marker加载：间隔加载
				AQIPoint aqiPoint = (AQIPoint) msg.obj;
				double weidu = aqiPoint.getWeidu();
				double jingdu = aqiPoint.getJingdu();
				LatLng latLng = new LatLng(weidu, jingdu);
				LinearLayout linear = getNationTextViewByPollutionType(pollutionType, aqiPoint);
			
				int size = (int)( (TextView)linear.getChildAt(0)).getText().length();
				Bitmap bitmap = convertViewToBitmap(linear, size);
				BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(bitmap);
	
				if (bm != null) {
					OverlayOptions options = new MarkerOptions().position(latLng).icon(bm).title(Nation+"-"+aqiPoint.getCity()+"-"+((TextView)linear.getChildAt(0)).getText());
					
					mBaiduMap.addOverlay(options);
				}
		}

	};

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.map_main_new_activity);
		progressDialog=new ProgressDialog(this);
	
		initView();
		initLocation();
		initSDKReceiver();
		initListener();
		initData();
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		change_map = (ImageView) findViewById(R.id.change_map);
		change_map_layout = (LinearLayout) findViewById(R.id.change_map_layout);
		change_map_normol = (ImageView) findViewById(R.id.change_map_normol);
		change_map_weixing = (ImageView) findViewById(R.id.change_map_weixing);
		update_time = (TextView) findViewById(R.id.update_time);
		img_location = (ImageView) findViewById(R.id.map_location_button);

		rl_index_btn = (RelativeLayout) findViewById(R.id.rl_map_index_btn);
		tv_aqi = (TextView) findViewById(R.id.tv_aqi_map);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25_map);
		tv_pm10 = (TextView) findViewById(R.id.tv_pm10_map);
		tv_so2 = (TextView) findViewById(R.id.tv_so2_map);
		tv_no2 = (TextView) findViewById(R.id.tv_no2_map);
		tv_o3 = (TextView) findViewById(R.id.tv_o3_map);
		tv_co = (TextView) findViewById(R.id.tv_co_map);
		
		updataTime=(TextView) findViewById(R.id.updateTime);
		
		progressDialog.setCanceledOnTouchOutside(false);
		
	}

	private void initLocation() {
		// 开启定位图层
		mMapView.getMap().setMyLocationEnabled(true);
		// 定位初始化
		mLocationClient = new LocationClient(getApplicationContext());
		MyLocationListener myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);//YYF 1000->0
		mLocationClient.setLocOption(option);
	}

	private void initSDKReceiver() {
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	private void initListener() {
		change_map.setOnClickListener(this);
		change_map_normol.setOnClickListener(this);
		change_map_weixing.setOnClickListener(this);
		img_location.setOnClickListener(this);
		tv_aqi.setOnClickListener(this);
		tv_pm25.setOnClickListener(this);
		tv_pm10.setOnClickListener(this);
		tv_so2.setOnClickListener(this);
		tv_no2.setOnClickListener(this);
		tv_o3.setOnClickListener(this);
		tv_co.setOnClickListener(this);
	}

	private void initData() {
		threadPool = Executors.newCachedThreadPool();
		// 获取app对象
		app = (WeiBaoApplication) MapMainNewActivity.this.getApplication();
		// 去除缩放按钮
		mMapView.showZoomControls(false);
		// 获取地图控制器
		mBaiduMap = mMapView.getMap();
		LatLng center = new LatLng(35.2156300000, 113.2420100000);
		// 地图中心点设置 初始缩放级别设置为9；
		MapStatus ms = new MapStatus.Builder().target(center).zoom(11).build();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
		// MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图；MAP_TYPE_NONE 卫星图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// 设置地图状态监听
		OnMapStatusChangeListener linstener = new MyOnMapStatusChangeListener();
		mBaiduMap.setOnMapStatusChangeListener(linstener);
		// 设置地图marker点击监听
		MyOnMarkerClickListener myOnMarkerClickListener=new MyOnMarkerClickListener();
		mBaiduMap.setOnMarkerClickListener(myOnMarkerClickListener);
		// 设置地图初始的缩放级别
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(9));
		// 记录当前缩放级别
		zoomLevel = ms.zoom;
		preLevel=zoomLevel;
		// 设置污染物类型
		pollutionType = POLLUTION_TYPE_AQI;
		// 设置数据类型
		mapDataType = MAP_DATA_TYPE_NATION;
		
		progressDialog.show();
		
		// 进入地图直接请求一次省内city数据；		
		requestProvinceCityData_new();
		
		
		
		
		// 手势设置
		settings = mBaiduMap.getUiSettings();
		//初始化底部导航按钮
		tv_aqi.setBackgroundColor(getResources().getColor(R.color.yyf_green));
		tv_aqi.setTextColor(getResources().getColor(R.color.yyf_white));
		tvTemp = tv_aqi;
	}

	private void requestProvinceCityData_new() {
		// TODO Auto-generated method stub
		threadPool.execute(new Runnable() {
			public void run() {
				String url3 = UrlComponent.getHeNanCityValueUrl;
				OkHttpUtils.get().url(url3).build().execute(requestCityCallback_new);
			}
		});
	}

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
			showyourlocation();
			zoomLevel = mBaiduMap.getMapStatus().zoom;
			mapDataType = MAP_DATA_TYPE_POINT;
			showMarker();
			break;
		case R.id.tv_aqi_map:
			//改变按钮状态
			tv_aqi.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_aqi.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_aqi;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_AQI)
				return;
			pollutionType = POLLUTION_TYPE_AQI;
			showMarker();
			break;
		case R.id.tv_pm25_map:
			//改变按钮状态
			tv_pm25.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_pm25.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_pm25;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_PM2_5)
				return;
			pollutionType = POLLUTION_TYPE_PM2_5;
			showMarker();
			break;
		case R.id.tv_pm10_map:
			//改变按钮状态
			tv_pm10.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_pm10.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_pm10;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_PM10)
				return;
			pollutionType = POLLUTION_TYPE_PM10;
			showMarker();
			break;
		case R.id.tv_so2_map:
			//改变按钮状态
			tv_so2.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_so2.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_so2;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_SO2)
				return;
			pollutionType = POLLUTION_TYPE_SO2;
			showMarker();
			break;
		case R.id.tv_no2_map:
			//改变按钮状态
			tv_no2.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_no2.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_no2;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_NO2)
				return;
			pollutionType = POLLUTION_TYPE_NO2;
			showMarker();
			break;
		case R.id.tv_o3_map:
			//改变按钮状态
			tv_o3.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_o3.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_o3;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_O3)
				return;
			pollutionType = POLLUTION_TYPE_O3;
			showMarker();
			break;
		case R.id.tv_co_map:
			//改变按钮状态
			tv_co.setBackgroundColor(getResources().getColor(R.color.yyf_green));
			tv_co.setTextColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setBackgroundColor(getResources().getColor(R.color.yyf_white));
			tvTemp.setTextColor(getResources().getColor(R.color.yyf_black));
			tvTemp = tv_co;
			//关闭污染物导航按钮
			rl_index_btn.setClickable(false);
			if (pollutionType == POLLUTION_TYPE_CO)
				return;
			pollutionType = POLLUTION_TYPE_CO;
			showMarker();
			break;

		default:
			break;
		}

	}

	private void showyourlocation() {
		if (showlocation) {
			// 准备关闭显示定位
			showlocation = false;
			img_location.setImageResource(R.drawable.map_dingwei);
		} else {
			// 准备打开显示定位
			showlocation = true;
			img_location.setImageResource(R.drawable.map_dingwei);
			requestLocation();
		}
	}

	/**
	 * 手动触发一次定位请求
	 */
	private void requestLocation() {
		mLocationClient.start();
		islocationing = true;
		mLocationClient.requestLocation();
		LocationService.sendGetLocationBroadcast(MapMainNewActivity.this);
		Toast.makeText(MapMainNewActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
	}

	private void switchChangeMap() {
		if (change_map_layout.getVisibility() == View.VISIBLE) {
			change_map_layout.setVisibility(View.GONE);
		} else {
			change_map_layout.setVisibility(View.VISIBLE);
		}
	}

	// TODO YYF 新增变量
	// application对象
	private WeiBaoApplication app;
	// 记录之前缩放级别
	private double preLevel = 0;
	// 当前缩放级别
	private double zoomLevel = 0;
	// 当前正在使用的数据源 临时
	private List<AQIPoint> tempListNation = null;
	private List<ProvinceCity> tempListCity = null;
	private List<ProvincePoint> tempListPoint = null;
	// 污染物类型
	public static final int POLLUTION_TYPE_AQI = 11;
	public static final int POLLUTION_TYPE_PM2_5 = 22;
	public static final int POLLUTION_TYPE_PM10 = 33;
	public static final int POLLUTION_TYPE_SO2 = 44;
	public static final int POLLUTION_TYPE_NO2 = 55;
	public static final int POLLUTION_TYPE_O3 = 66;
	public static final int POLLUTION_TYPE_CO = 77;
	// 当前污染物类型
	private int pollutionType = 0;
	// 数据类型
	public static final int MAP_DATA_TYPE_CITY = 111;
	public static final int MAP_DATA_TYPE_POINT = 222;
	public static final int MAP_DATA_TYPE_NATION = 333;
	// 当前数据类型
	private int mapDataType = 0;
	// dialog数据类型变量
	public static final String Nation = "nation";
	public static final String CITY = "city";
	public static final String POINT = "point";

	// protected boolean isFirstEnterMap = true;

	/**
	 * 地图缩放监听
	 * 
	 * @author 尹亚飞
	 *
	 */
	class MyOnMapStatusChangeListener implements OnMapStatusChangeListener {

		/**
		 * 缩放、拖动 地图加载完成时，地图移动完成时、点击到地图可点标注时、动画结束时和截图成功后
		 * 
		 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
		 * 
		 * @param status
		 *            地图状态改变开始时的地图状态
		 */
		public void onMapStatusChangeStart(MapStatus status) {
			Log.i("YYF", "开始==========" + status.zoom);
			mBaiduMap.hideInfoWindow();
		}

		/**
		 * 地图状态变化中
		 * 
		 * @param status
		 *            当前地图状态
		 */
		public void onMapStatusChange(MapStatus status) {
			Log.i("YYF", "变化中==========" + status.zoom);
		}

		/**
		 * 地图状态改变结束
		 * 
		 * @param status
		 *            地图状态改变结束后的地图状态
		 */
		public void onMapStatusChangeFinish(MapStatus status) {
			Log.i("YYF", "结束==========" + status.zoom);
			zoomLevel = status.zoom;
			// 缩放逻辑
			zoomLevel = mBaiduMap.getMapStatus().zoom;
			if (zoomLevel >= 3 && zoomLevel <= 11) {
				mapDataType = MAP_DATA_TYPE_NATION;
				if (preLevel >= 3 && preLevel <= 11) {
					return;
				}
				if (quanGuoData==null) {
					// 请求全国数据
					requestNationalData();
				}else{
					showMarker();
				}
//				
				preLevel = zoomLevel;
//				Toast.makeText(app, "加载全国数据，请耐心等待", Toast.LENGTH_LONG).show();
//			} else if (zoomLevel >= 9 && zoomLevel <= 11) {
//				mapDataType = MAP_DATA_TYPE_CITY;
//				if (preLevel >= 9 && preLevel <= 11) {
//					return;
//				}
//				if (quanShengData==null) {
//					// 请求省内city数据
//					requestProvinceCityData();
//				}else{
//					showMarker();
//				}
//				
//				preLevel = zoomLevel;
			} else if (zoomLevel > 11) {
				mapDataType = MAP_DATA_TYPE_POINT;
				if (preLevel > 11) {
					return;
				}
				
				if (provincePoints==null) {
					// 请求省内point数据
					requestProvincePointData();
				}else{
					showMarker();
				}
				
				preLevel = zoomLevel;
			}
		}

	}

	/**
	 * 定位监听
	 * 
	 * @author 尹亚飞
	 *
	 */
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			Log.i("YYF", "BDLocationListener");
			// mapView 销毁后不在处理新接收的位置
			if (bdLocation == null || mMapView == null)
				return;

			// 只处理一次请求事件
			if (bdLocation == null || islocationing == false) {
				return;
			}

			// 定位到当前位置
			MyLocationData myLocation = new MyLocationData.Builder().latitude(bdLocation.getLatitude())
					.longitude(bdLocation.getLongitude()).accuracy(bdLocation.getRadius())
					.direction(bdLocation.getDirection()).build();
			modifyLocationOverlayIcon(R.drawable.ic_userlocation);
			mBaiduMap.setMyLocationData(myLocation);

			// 设置缩放级别
			LatLng cenpt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
			MapStatus ms = new MapStatus.Builder().zoom(16).target(cenpt).build();
			mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

			showyourlocation();// 目的是切换一下定位按钮的图片。
			mLocationClient.stop();
			islocationing = false;
		}
	}

	/**
	 * 修改我的位置图标
	 * 
	 * @param drawableid
	 */
	public void modifyLocationOverlayIcon(int drawableid) {

		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(drawableid);
		MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
				true, mCurrentMarker);
		mBaiduMap.setMyLocationConfigeration(config);
	}

	/**
	 * 定义Key验证事件的广播监听者
	 * 
	 * @author 尹亚飞
	 *
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				// key 验证失败，相应处理
			}
		}
	}

	/**
	 * 请求全国污染物数据
	 */
	public void requestNationalData() {
		String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
		GetNationalDataTask task = new GetNationalDataTask();
		task.execute(url, app.getPinjieCity());
//		//关闭手势
//		settings.setAllGesturesEnabled(false); 
	}

	/**
	 * post请求获取全国数据
	 * 
	 * @author 尹亚飞
	 * 
	 */
	class GetNationalDataTask extends AsyncTask<Object, Void, List<AQIPoint>> {

		@Override
		protected List<AQIPoint> doInBackground(Object... arg) {
			
			BusinessSearch search = new BusinessSearch();
			String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
			List<AQIPoint> _Result;
			try {
				_Result = search.getShengHuiAqi((String) arg[0], app.getPinjieCity());
				// 设置到全国数据载体
				app.setAqipointList((ArrayList<AQIPoint>) _Result);
				quanGuoData=_Result;
				List<AQIPoint> nationalData = completionNationalData(app.getAqipointList());
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<AQIPoint> result) {
			Log.i("YYF", "========最新全国数据=========" + app.getAqipointList().toString());
			Log.i("YYF", "全国数据长度==========" + app.getAqipointList().size());
			// TODO 拿到最新全国数据后的逻辑
			showMarker();
			updataTime.setText(result.get(0).getUpdateTime());
			addHenanPolygon();
			//回复手势
//			settings.setAllGesturesEnabled(true); 
		}
	}

	/**
	 * 请求省内city数据
	 */
	public void requestProvinceCityData() {
		threadPool.execute(new Runnable() {
			public void run() {
				String url3 = UrlComponent.getHeNanCityValueUrl;
				OkHttpUtils.get().url(url3).build().execute(requestCityCallback);
			}
		});
	}

	/**
	 * TODO 补全全国数据 将省内city数据更新到全国数据中 数据包含7项污染物
	 * 
	 * @param list
	 */
	public List<AQIPoint> completionNationalData(List<AQIPoint> oldList) {
		List<AQIPoint> newList = new ArrayList<AQIPoint>();
		HashMap<String, LaLngData> map = app.getProvinceCityLatLngMap();
		for (ProvinceCity pc : app.getProvincecityList()) {
			String cityname = pc.getCITY();
			String thisName = deleteShi(cityname);
			AQIPoint aqiPoint = new AQIPoint();
			aqiPoint.setAqi(pc.getAQI());
			aqiPoint.setCity(thisName);// TODO 含有“市”字，此处待优化
			aqiPoint.setCO(pc.getCO());
			aqiPoint.setJiancedian(thisName);
			aqiPoint.setNO2(pc.getNO2());
			aqiPoint.setO3(pc.getO3());
			aqiPoint.setPM10(pc.getPM10());
			aqiPoint.setPm2_5(pc.getPM25());
			aqiPoint.setSO2(pc.getSO2());
			LaLngData laLngData = map.get(thisName);
			if(laLngData!=null){
				aqiPoint.setWeidu(laLngData.getWeidu());
				aqiPoint.setJingdu(laLngData.getJingdu());
				newList.add(aqiPoint);
			}
		}
		newList.addAll(oldList);
		app.setAqipointList((ArrayList<AQIPoint>) newList);
		Log.i("YYF", "yyyyyyyyyy 补全1===="+newList.toString());
		return newList;
	}

	/**
	 * 删除“市”的方法(包含)
	 * 
	 * @return province city name;
	 */
	private String deleteShi(String cityname) {
		// TODO YYF 待添加
		String[] cityArrays = app.getCityArrays();
		for (int i = 0; i < cityArrays.length; i++) {
			if (cityname.contains(cityArrays[i])) {
				cityname = cityArrays[i];
			}
		}
		return cityname;
	}

	/**
	 * 请求省内point污染物数据
	 */
	public void requestProvincePointData() {
		threadPool.execute(new Runnable() {
			public void run() {
				String url2 = UrlComponent.getHeNanPointValueUrl;
				OkHttpUtils.get().url(url2).build().execute(requestPointCallback);
			}
		});
	}

	// 请求省内数据city的回调
	protected StringCallback requestCityCallback = new StringCallback() {

		@Override
		public void onError(Call call, Exception exception, int arg) {
			exception.printStackTrace();
		}

		@Override
		public void onResponse(String str, int arg) {
			// 解析数据,存储数据
			resolveCityData(str);
			// 异步 将省内数据填充至全国数据中
			threadPool.execute(new Runnable() {
				public void run() {
					completionNationalData(app.getAqipointList());
				}
			});
			// 显示marker
			showMarker();
		}

		private void resolveCityData(String str) {
			Gson gson = new Gson();
			ProvinceCityData data = gson.fromJson(str, ProvinceCityData.class);
			List<ProvinceCity> list = data.getData();
			List<ProvinceCity> result = new ArrayList<ProvinceCity>();
			for (int i = 0; i < list.size(); i++) {
				String airlevel = list.get(i).getAIRLEVEL();
				if (airlevel != null)
					result.add(list.get(i));
			}
			app.setProvincecityList((ArrayList<ProvinceCity>) result);
			Log.i("YYF", "请求省内数据city的回调 数据长度：" + app.getProvincecityList().size());
			Log.i("YYF", "请求省内数据city的回调：" + app.getProvincecityList().toString());
		}

	};


	// 请求省内数据city的回调
	protected StringCallback requestCityCallback_new = new StringCallback() {

		@Override
		public void onError(Call call, Exception exception, int arg) {
			exception.printStackTrace();
		}

		@Override
		public void onResponse(String str, int arg) {
			// 解析数据,存储数据
			resolveCityData(str);
			// 异步 将省内数据填充至全国数据中
			threadPool.execute(new Runnable() {
				public void run() {
					completionNationalData(app.getAqipointList());
				}
			});
			// 显示marker
//			showMarker();
			//获取全国数据
			requestNationalData();
		}

		private void resolveCityData(String str) {
			Gson gson = new Gson();
			ProvinceCityData data = gson.fromJson(str, ProvinceCityData.class);
			List<ProvinceCity> list = data.getData();
			List<ProvinceCity> result = new ArrayList<ProvinceCity>();
			for (int i = 0; i < list.size(); i++) {
				String airlevel = list.get(i).getAIRLEVEL();
				if (airlevel != null)
					result.add(list.get(i));
			}
			app.setProvincecityList((ArrayList<ProvinceCity>) result);
			Log.i("YYF", "请求省内数据city的回调 数据长度：" + app.getProvincecityList().size());
			Log.i("YYF", "请求省内数据city的回调：" + app.getProvincecityList().toString());
		}

	};
	
	// 请求省内数据point的回调
	protected StringCallback requestPointCallback = new StringCallback() {

		@Override
		public void onError(Call call, Exception exception, int arg) {
			exception.printStackTrace();
		}

		@Override
		public void onResponse(String str, int arg) {
			// Log.i("YYF", "请求省内数据point的回调：" + str.toString());
			// 解析数据,存储数据
			resolvePointData(str);
			// 显示marker
			showMarker();
		}

		private void resolvePointData(String json) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONObject detail = jsonObject.optJSONObject("detail");
				List<ProvincePoint> list = new ArrayList<ProvincePoint>();
				for (int j = 0; j < hnCityArrays.length; j++) {
					JSONObject cityObj = detail.optJSONObject(hnCityArrays[j]);
					JSONArray stations = cityObj.optJSONArray("STATIONS");
					if (stations != null && stations.length() > 0) {
						for (int i = 0; i < stations.length(); i++) {
							ProvincePoint provincePoint = new ProvincePoint();
							JSONObject object = stations.optJSONObject(i);
							provincePoint.setREGIONNAME(object.optString("REGIONNAME"));
							provincePoint.setSTATIONNAME(object.optString("STATIONNAME"));
							provincePoint.setLONGITUDE(object.optDouble("LONGITUDE"));
							provincePoint.setLATITUDE(object.optDouble("LATITUDE"));
							provincePoint.setAQI(object.optInt("AQI"));
							provincePoint.setSO2(object.optInt("SO2"));
							provincePoint.setNO2(object.optInt("NO2"));
							provincePoint.setCO(object.optDouble("CO"));
							provincePoint.setO3(object.optInt("O3"));
							provincePoint.setPM10(object.optInt("PM10"));
							provincePoint.setPM25(object.optInt("PM25"));
							list.add(provincePoint);
						}
					}
				}
				Log.i("YYF", "请求省内数据point的回调(原生解析)：" + list.toString());
				app.setProvincepointList(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 清理地图，展示marker
	 */
	private void showMarker() {
		mBaiduMap.clear();
		progressDialog.show();
		// TODO Auto-generated method stub
		confrimDataFromType(mapDataType, pollutionType);
		
	}

	/**
	 * TODO 确认数据类型
	 * 
	 * @param mapDataType
	 *            数据类型
	 * @param pollutionType
	 *            污染物类型
	 */
	private void confrimDataFromType(int mapDataType, final int pollutionType) {
	
		
		
		switch (mapDataType) {
		case MAP_DATA_TYPE_CITY:
			tempListCity = app.getProvincecityList();
			update_time.setText(tempListCity.get(0).getMONIDATE());
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					confrimCityPollutionFromType(tempListCity, pollutionType);
				}
			}).start();
			break;
		case MAP_DATA_TYPE_POINT:
			tempListPoint = app.getProvincepointList();
//			update_time.setText(tempListPoint.get(0).getu());
			new Thread(new Runnable() {
				@Override
				public void run() {
					confrimPointPollutionFromType(tempListPoint, pollutionType);
				}
			}).start();
			break;
		case MAP_DATA_TYPE_NATION:
			tempListNation = app.getAqipointList();
			
			update_time.setText("发布时间："+app.getProvincecityList().get(0).getMONIDATE());
			confrimNationPollutionFromType(tempListNation, pollutionType);
			break;
		default:
			break;
		}

		// TODO 最新方法展示不出标识marker？
		// LatLng position = new LatLng(bdLocation.getLatitude(),
		// bdLocation.getLongitude());
		// mBaiduMap.addOverlay(new
		// MarkerOptions().icon(bitmapDescriptor).position(position));

	}

	/**
	 * 选定省内城市数据，根据污染物类型，展示marker
	 * 
	 * @param listCity
	 *            省内城市数据
	 * @param pollutionType
	 *            污染物类型
	 */
	private void confrimCityPollutionFromType(List<ProvinceCity> listCity, int pollutionType) {
		for (int i = 0; i < listCity.size(); i++) {
			
			ProvinceCity provinceCity = listCity.get(i);
			String cityname = deleteShi(provinceCity.getCITY());
			HashMap<String, LaLngData> latLngMap = app.getProvinceCityLatLngMap();
			LaLngData ll = latLngMap.get(cityname);
			if(ll!=null){
				double weidu = ll.getWeidu();
				double jingdu = ll.getJingdu();
				LatLng latLng = new LatLng(weidu, jingdu);
				TextView tvCityText = getCityTextViewByPollutionType(pollutionType, provinceCity);
				// Message.obtain(handler, 1).sendToTarget();
				int size = (int) tvCityText.getText().length();
				Bitmap bitmap = convertViewToBitmap(tvCityText, size);
				BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(bitmap);
				MarkerOptions options = new MarkerOptions().position(latLng).icon(bm).title(CITY+"-"+cityname+"-"+tvCityText.getText());
				Message msg=handler2.obtainMessage();
				msg.what=0;
				msg.obj=options;
				handler2.dispatchMessage(msg);
			}
			
		}
		handler2.sendEmptyMessage(1);
	}
	
	private Handler handler2=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0:
					MarkerOptions options=(MarkerOptions) msg.obj;
					mBaiduMap.addOverlay(options);
					break;
				case 1:
					progressDialog.dismiss();
					break;
				case 2:
					List<LatLng> pts =(List<LatLng>) msg.obj;
					OverlayOptions polygonOption = new PolygonOptions()  
				    .points(pts)  
				    .stroke(new Stroke(5, 0xAA393323))  
				    .fillColor(0x00FFFFFF);  
					//在地图上添加多边形Option，用于显示  
					mBaiduMap.addOverlay(polygonOption);
					break;
			}
		};
	};
	
	
	/**
	 * 保留小数点后一位
	 * @param d
	 * @return
	 */
	public String formatDouble(double d) {
        return String.format("%.1f", d);
    }

	/**
	 * 根据污染物类型和City数据大小获取特定对应颜色的textview控件
	 * 
	 * @param pollutionType
	 * @param provinceCity
	 * @return
	 */
	private TextView getCityTextViewByPollutionType(int pollutionType, ProvinceCity provinceCity) {
		View view = View.inflate(app, R.layout.map_dengji_layout, null);
		final TextView tv = (TextView) view.findViewById(R.id.pointtext);
		double doubleValue = 0;
		int resId = 0;
		switch (pollutionType) {
		case POLLUTION_TYPE_AQI:
			doubleValue = Double.parseDouble(provinceCity.getAQI());
			resId = convertBgColorOfAQI(doubleValue);
			break;
		case POLLUTION_TYPE_PM2_5:
			doubleValue = Double.parseDouble(provinceCity.getPM25());
			resId = convertBgColorOfPM25(doubleValue);
			break;
		case POLLUTION_TYPE_PM10:
			doubleValue = Double.parseDouble(provinceCity.getPM10());
			resId = convertBgColorOfPM10(doubleValue);
			break;
		case POLLUTION_TYPE_SO2:
			doubleValue = Double.parseDouble(provinceCity.getSO2());
			resId = convertBgColorOfSO2(doubleValue);
			break;
		case POLLUTION_TYPE_NO2:
			doubleValue = Double.parseDouble(provinceCity.getNO2());
			resId = convertBgColorOfNO2(doubleValue);
			break;
		case POLLUTION_TYPE_O3:
			doubleValue = Double.parseDouble(provinceCity.getO3());
			resId = convertBgColorOfO3(doubleValue);
			break;
		case POLLUTION_TYPE_CO:
			doubleValue = Double.parseDouble(provinceCity.getCO());
			resId = convertBgColorOfCO(doubleValue);
			break;

		default:

			break;
		}
		markerSetText(pollutionType, tv, doubleValue, resId);
		return tv;
	}

	/**
	 * 选定省内站点数据，根据污染物类型，展示marker
	 * 
	 * @param listCity
	 *            省内城市数据
	 * @param pollutionType
	 *            污染物类型
	 */
	private void confrimPointPollutionFromType(List<ProvincePoint> listPoint, int pollutionType) {
		for (int i = 0; i < listPoint.size(); i++) {
			
			ProvincePoint provincePoint = listPoint.get(i);
			double weidu = provincePoint.getLATITUDE();
			double jingdu = provincePoint.getLONGITUDE();
			LatLng latLng = new LatLng(weidu, jingdu);
			TextView tvPointText = getPointTextViewByPollutionType(pollutionType, provincePoint);
			int size = (int) tvPointText.getText().length();
			Bitmap bitmap = convertViewToBitmap(tvPointText, size);
			BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(bitmap);
			if (bm != null) {
				OverlayOptions options = new MarkerOptions().position(latLng).icon(bm).title(POINT+"-"+provincePoint.getSTATIONNAME()+"-"+tvPointText.getText());
				Message msg=handler2.obtainMessage();
				msg.what=0;
				msg.obj=options;
				handler2.dispatchMessage(msg);
			}
		}
		handler2.sendEmptyMessage(1);
	}

	/**
	 * 根据污染物类型和point数据大小获取特定对应颜色的textview控件
	 * 
	 * @param pollutionType
	 * @param provincePoint
	 * @return
	 */
	private TextView getPointTextViewByPollutionType(int pollutionType, ProvincePoint provincePoint) {
		View view = View.inflate(app, R.layout.map_dengji_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.pointtext);
		double doubleValue = 0;
		int resId = 0;
		switch (pollutionType) {
		case POLLUTION_TYPE_AQI:
			doubleValue = provincePoint.getAQI();
			resId = convertBgColorOfAQI(doubleValue);
			break;
		case POLLUTION_TYPE_PM2_5:
			doubleValue = provincePoint.getPM25();
			resId = convertBgColorOfPM25(doubleValue);
			break;
		case POLLUTION_TYPE_PM10:
			doubleValue = provincePoint.getPM10();
			resId = convertBgColorOfPM10(doubleValue);
			break;
		case POLLUTION_TYPE_SO2:
			doubleValue = provincePoint.getSO2();
			resId = convertBgColorOfSO2(doubleValue);
			break;
		case POLLUTION_TYPE_NO2:
			doubleValue = provincePoint.getNO2();
			resId = convertBgColorOfNO2(doubleValue);
			break;
		case POLLUTION_TYPE_O3:
			doubleValue = provincePoint.getO3();
			resId = convertBgColorOfO3(doubleValue);
			break;
		case POLLUTION_TYPE_CO:
			doubleValue = provincePoint.getCO();
			resId = convertBgColorOfCO(doubleValue);
			break;

		default:

			break;
		}
		markerSetText(pollutionType, tv, doubleValue,resId);
		return tv;
	}

	private void markerSetText(int pollutionType, TextView tv, double doubleValue,int resId) {
		tv.setBackgroundResource(resId);
		int tempValue = (int) doubleValue;
		if (tempValue == doubleValue) {
			if(String.valueOf(doubleValue).contains(".")&&pollutionType==POLLUTION_TYPE_CO){
				//保留小数点
				tv.setText(formatDouble(doubleValue));
			}else {
				//不保留小数点
				tv.setText(String.valueOf(tempValue));
			}
		} else {
			tv.setText(formatDouble(doubleValue));
		}
		tv.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 根据AQI数据大小转换TextView背景颜色1
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfAQI(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 50) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 50 && intValue <= 100) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 100 && intValue <= 150) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 150 && intValue <= 200) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 200 && intValue <= 300) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 300 && intValue <= 500) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据PM25数据大小转换TextView背景颜色2
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfPM25(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 35) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 35 && intValue <= 75) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 75 && intValue <= 115) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 115 && intValue <= 150) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 150 && intValue <= 250) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 250 && intValue <= 500) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据PM10数据大小转换TextView背景颜色3
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfPM10(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 50) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 50 && intValue <= 150) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 150 && intValue <= 250) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 250 && intValue <= 350) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 350 && intValue <= 420) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 420 && intValue <= 600) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据SO2数据大小转换TextView背景颜色4
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfSO2(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 50) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 50 && intValue <= 150) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 150 && intValue <= 475) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 475 && intValue <= 800) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 800 && intValue <= 1600) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 1600 && intValue <= 2620) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据NO2数据大小转换TextView背景颜色5
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfNO2(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 40) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 40 && intValue <= 80) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 80 && intValue <= 180) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 180 && intValue <= 280) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 280 && intValue <= 565) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 565 && intValue <= 940) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据O3数据大小转换TextView背景颜色6
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfO3(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 160) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 160 && intValue <= 200) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 200 && intValue <= 300) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 300 && intValue <= 400) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 400 && intValue <= 800) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 800 && intValue <= 1200) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 根据CO数据大小转换TextView背景颜色7
	 * 
	 * @param intValue
	 * @return
	 */
	private int convertBgColorOfCO(double intValue) {
		int res_Id = 0;
		if (intValue > 0 && intValue <= 2) {
			res_Id = R.drawable.map_1you;
		} else if (intValue > 2 && intValue <= 4) {
			res_Id = R.drawable.map_2liang;
		} else if (intValue > 4 && intValue <= 14) {
			res_Id = R.drawable.map_3qingdu;
		} else if (intValue > 14 && intValue <= 24) {
			res_Id = R.drawable.map_4zhong;
		} else if (intValue > 24 && intValue <= 36) {
			res_Id = R.drawable.map_5zhongdu;
		} else if (intValue > 36 && intValue <= 60) {
			res_Id = R.drawable.map_6yanzhong;
		} else if (intValue == 0) {
			res_Id = R.drawable.map_7hui;
		}
		return res_Id;
	}

	/**
	 * 选定全国数据，根据污染物类型，展示marker
	 * 
	 * @param listCity
	 *            省内城市数据
	 * @param pollutionType
	 *            污染物类型
	 */
	private void confrimNationPollutionFromType(final List<AQIPoint> listNation, int pollutionType) {
		//优化全国数据的marker展示：开启子线程处理
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < listNation.size(); i++) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AQIPoint aqiPoint = listNation.get(i);
					Message msg = new Message();
					msg.obj = aqiPoint;
					handler.sendMessageDelayed(msg, 30);
				}
				//恢复污染物导航按钮
				rl_index_btn.setClickable(true);
				handler2.sendEmptyMessage(1);
			}
			
		});
	}

	/**
	 * 根据污染物类型和全国数据大小获取特定对应颜色的textview控件
	 * 
	 * @param pollutionType
	 * @param aqiPoint
	 * @return
	 */
	private LinearLayout getNationTextViewByPollutionType(int pollutionType, AQIPoint aqiPoint) {
		LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.map_dengji_layout, null,false);
		TextView tv = (TextView) view.findViewById(R.id.pointtext);
		double doubleValue = 0;
		int resId = 0;
		switch (pollutionType) {
		case POLLUTION_TYPE_AQI:
			doubleValue = Double.parseDouble(aqiPoint.getAqi());
			resId = convertBgColorOfAQI(doubleValue);
			break;
		case POLLUTION_TYPE_PM2_5:
			doubleValue = Double.parseDouble(aqiPoint.getPm2_5());
			resId = convertBgColorOfPM25(doubleValue);
			break;
		case POLLUTION_TYPE_PM10:
			doubleValue = Double.parseDouble(aqiPoint.getPM10());
			resId = convertBgColorOfPM10(doubleValue);
			break;
		case POLLUTION_TYPE_SO2:
			doubleValue = Double.parseDouble(aqiPoint.getSO2());
			resId = convertBgColorOfSO2(doubleValue);
			break;
		case POLLUTION_TYPE_NO2:
			doubleValue = Double.parseDouble(aqiPoint.getNO2());
			resId = convertBgColorOfNO2(doubleValue);
			break;
		case POLLUTION_TYPE_O3:
			doubleValue = Double.parseDouble(aqiPoint.getO3());
			resId = convertBgColorOfO3(doubleValue);
			break;
		case POLLUTION_TYPE_CO:
			doubleValue = Double.parseDouble(aqiPoint.getCO());
			resId = convertBgColorOfCO(doubleValue);
			break;

		default:

			break;
		}
		markerSetText(pollutionType, tv, doubleValue,resId);
		return view;
	}

	/**
	 * TODO 将加标注的覆盖物的View转成BitMap TODO 空了研究 YYF
	 * 
	 * @param view
	 * @param size
	 * @return
	 */
	public Bitmap convertViewToBitmap(View view, int size) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int width = size * 100;
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight()); // 根据字符串的长度显示view的宽度
		view.buildDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	/**
	 * marker点击监听：弹出dialog信息
	 * @author 尹亚飞
	 *
	 */
	class MyOnMarkerClickListener implements OnMarkerClickListener{

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.getTitle() == null) {
				Log.v("mappic-markerclick", "gettitle is null");
				return false;
			}
			String[] split = marker.getTitle().split("-");
			String dialogDataType = split[0];
			String cityname = split[1];
			String polluteValue = split[2];
			LatLng position = marker.getPosition();
			if(Nation.equals(dialogDataType)||CITY.equals(dialogDataType)){
				MapPopOfCity mapPopOfCity = new MapPopOfCity(MapMainNewActivity.this, cityname, polluteValue, pollutionType);
				// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
				pop  = new InfoWindow(mapPopOfCity, position, -47);
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(pop);
				return true;
			}
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);// 注销地图事件相关的广播
		threadPool.shutdown();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	public void addHenanPolygon(){
		 try {  
			//Return an AssetManager instance for your application's package  
			InputStream is = getAssets().open("py_jiaozuo.json");  
			int size = is.available();  
			  
			// Read the entire asset into a local byte buffer.  
			byte[] buffer = new byte[size];  
			is.read(buffer);  
			is.close();  
			  
			
			List<LatLng> pts = new ArrayList<LatLng>();  
			// Convert the buffer into a string.  
			String text = new String(buffer, "UTF-8");  
			JSONObject jsonObject=new JSONObject(text);
			JSONArray jsonArray=jsonObject.getJSONArray("features")
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONArray("coordinates").getJSONArray(0);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray jsonArray2 = jsonArray.getJSONArray(i);
				double mLon=jsonArray2.getDouble(0);
				double mLat=jsonArray2.getDouble(1);
				
				LatLng pt = new LatLng(mLat, mLon);  
				pts.add(pt);
			}
			
			handler2.obtainMessage(2, pts).sendToTarget();
			
			} catch (IOException e) {  
			// Should never happen!  
			    throw new RuntimeException(e);  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}  
	

}
