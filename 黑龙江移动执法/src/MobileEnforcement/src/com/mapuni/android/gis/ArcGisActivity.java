package com.mapuni.android.gis;

import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.mapuni.android.MobileEnforcement.R;

/**
 * 地图模块基类，实现基本view视图的显示 基本控件的初始化 具体监听靠子类来实现
 * 
 * @author wanglg
 * 
 */
public abstract class ArcGisActivity extends Activity {

	public String TAG = "ArcGisActivity";
	/** 地图view */
	// @PmCode(code="vmob6A")
	public static MapView map;
	/** 当前所在位置定位 */
	// @ViewId(id=R.id.btn_location)
	public Button btn_location = null;
	/** 放大地图 */
	// @ViewId(id=R.id.btn_zoomin)
	public Button btn_zoomin = null;
	/** 缩小地图 */
	// @ViewId(id=R.id.btn_zoomout)
	public Button btn_zoomout = null;
	/** 重置地图范围按钮 */
	// @ViewId(id=R.id.btn_zoomfull)
	public Button btn_zoomfull = null;

	/** 定位控制器 */
	public LocationManager locationManager = null;
	/** 定位实现者 */
	public Location location = null;
	/** 定位图层 */
	private GraphicsLayer loctionLayer = null;
	/** 标注图层 */
	public Callout callout;
	/** 缩放到此固定比列尺 */
	public final Double scale = 1292765.4170153292;
	/** 覆盖物图层 */
	public GraphicsLayer tLayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "---------------onCreate--------------------");
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(MapActivity.map != null && !MapActivity.map.isRecycled()){
			MapActivity.map.recycle();
		}
		setContentView(R.layout.gis_main_layout);
		// 初始化定位图层
				loctionLayer = new GraphicsLayer();
		// 初始化地图界面
		InitData();
		
		// 初始化覆盖物图层
		tLayer = new GraphicsLayer();
		// 设置地图背景颜色
		map.setBackgroundColor(Color.WHITE);
		map.addLayer(tLayer);
		map.addLayer(loctionLayer);
	}

	/** 初始化地图添加的控件 */
	protected void InitView() {
	}

	/**
	 * 添加地图初始图层
	 * 
	 * @return 默认地图路径配置在gis_config文件中map2 Type下的UrL字段
	 */
	protected abstract void addMapLayer();

	/** 初始化地图界面 */
	protected void InitData() {
		map = (MapView) findViewById(R.id.map);
		btn_location = (Button) findViewById(R.id.btn_location); // 定位
		btn_zoomin = (Button) findViewById(R.id.btn_zoomin);// 放大
		btn_zoomfull = (Button) findViewById(R.id.btn_zoomfull);// 远角视野
		btn_zoomout = (Button) findViewById(R.id.btn_zoomout);
		btn_zoomin.setOnClickListener(new MapControlListener());

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		addMapLayer();
		MapControlListener mapControlListener = new MapControlListener();
		btn_zoomin.setOnClickListener(mapControlListener);
		btn_zoomfull.setOnClickListener(mapControlListener);
		btn_zoomout.setOnClickListener(mapControlListener);
		
		String bestProvider = getBestProvider();
		location = locationManager.getLastKnownLocation(bestProvider);
		locationMySelf(location);
	}

	/** 判断手机是否开启GPS */
	public boolean bGpsEnable() {
		if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			Toast.makeText(ArcGisActivity.this, "GPS未打开", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	/**
	 * GPS开关监听
	 * 
	 * @author Administrator
	 * 
	 */
	class GPSOpenDownListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			Drawable image = null;
			if (bGpsEnable()) {
				image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.gis_tool_locationshare1);
				Toast.makeText(ArcGisActivity.this, "正在关闭位置共享服务，请稍等...", 1000).show();
			} else {
				image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.gis_tool_locationshare);
				Toast.makeText(ArcGisActivity.this, "正在开启位置共享服务，请稍等...", 1000).show();
			}

			Intent gpsIntent = new Intent();
			gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
			gpsIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(ArcGisActivity.this, 0, gpsIntent, 0).send();
			} catch (CanceledException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 地图缩放重置监听
	 * 
	 * @author
	 * 
	 */
	class MapControlListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.equals(btn_zoomin)) {
				map.zoomin();
			} else if (v.equals(btn_zoomout)) {
				map.zoomout();
			} else {
				loadFullMap();
			}
		}
	}

	/** 获取最好的provider 如果GPS和通信全被禁返回null */
	protected String getBestProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
		String provider = locationManager.getBestProvider(criteria, true);
		return provider;
	}

	/**
	 * Description: 初始化地图范围
	 * 
	 * @author Administrator<br>
	 *         Create at: 2013-3-14 下午05:32:32
	 */
	public abstract void loadFullMap();

	/**
	 * 定位按钮监听
	 * 
	 * @author
	 * 
	 */
	class MyLocationListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			String bestProvider = getBestProvider();
			
			if (bestProvider == null) {// 返回provider为null
				locationMySelf(null);
			} else {
				location = locationManager.getLastKnownLocation(bestProvider);
				if (location != null) {
					locationMySelf(location);
				} else {
					Toast.makeText(ArcGisActivity.this, "当前无法确定您所在的位置", 1).show();
				}
			}
		}
	}

	/** 定位到当前所在位置 */
	public void locationMySelf(Location location) {
		if (location != null) {
			SpatialReference sr4326 = SpatialReference.create(4326);
			//TODO  记得恢复原样   模拟位置
		Point point = (Point) GeometryEngine.project(new Point(location.getLongitude(), location.getLatitude()), sr4326, map.getSpatialReference());
	//		Point point = (Point) GeometryEngine.project(new Point(126.528395, 45.80216), sr4326, map.getSpatialReference());
			map.centerAt(point, true);
		//	Drawable image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.icon_my);
			Drawable image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.hbdt_gis_icon_loc);
			PictureMarkerSymbol imageMarkerSymbol = new PictureMarkerSymbol(image);
			Graphic g = new Graphic(point, imageMarkerSymbol, null, null);
			loctionLayer.addGraphic(g);
		} else {
			Toast.makeText(ArcGisActivity.this, "请检查网络或GPS是否开启！", 1).show();
		}
	}

	/**
	 * 添加标注
	 * 
	 * @param pt
	 *            标注的点
	 * @param content
	 *            标注的内容
	 * @param isListener
	 *            是否为TextView设置监听
	 */

	public void callout(final Point pt, String content, View.OnClickListener Listener) {
		callout = map.getCallout();
		callout.setCoordinates(pt);
		TextView tv = new TextView(this);
		tv.setTextSize((float) 18.0);
		tv.setText(content);
		tv.setTextColor(Color.BLACK);
		tv.setMaxWidth(400);
		tv.setMaxLines(10);
		tv.setEllipsize(TruncateAt.END);
		tv.setBackgroundColor(Color.TRANSPARENT);// 设置为透明背景

		if (Listener != null) {
			tv.setOnClickListener(Listener);
		}
		callout.setMaxWidth(400);
		callout.setMaxHeight(1000);
		callout.setContent(tv);
		callout.show();

	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e(TAG, "---------------onStop--------------------");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "---------------onPause--------------------");
	}
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.e(TAG, "---------------onResume--------------------");
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "---------------onDestroy--------------------");
	}
	
	/**
	 * 根据经纬度进行地位 并进行标注
	 * 
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param flag
	 *            标注
	 * @param attribute
	 *            位图需要的集合存放信息
	 * @param isListener
	 *            是否为标注添加监听事件
	 * @param image
	 *            覆盖的图片
	 */
	public void Location(double longitude, double latitude, String flag, Map<String, Object> attribute, View.OnClickListener Listener, Drawable image) {

		SpatialReference sr4326 = SpatialReference.create(4326);
		Point point = (Point) GeometryEngine.project(new Point(longitude, latitude), sr4326, map.getSpatialReference());
		map.centerAt(point, true);

		PictureMarkerSymbol imageMarkerSymbol = new PictureMarkerSymbol(image);
		Graphic g = new Graphic(point, imageMarkerSymbol, attribute, null);
		tLayer.addGraphic(g);
		if (flag != null) {
			callout(point, flag, Listener);
		}
		map.zoomToScale(point, scale);
	}

}
