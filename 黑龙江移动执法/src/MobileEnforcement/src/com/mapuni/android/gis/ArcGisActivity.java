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
 * ��ͼģ����࣬ʵ�ֻ���view��ͼ����ʾ �����ؼ��ĳ�ʼ�� ���������������ʵ��
 * 
 * @author wanglg
 * 
 */
public abstract class ArcGisActivity extends Activity {

	public String TAG = "ArcGisActivity";
	/** ��ͼview */
	// @PmCode(code="vmob6A")
	public static MapView map;
	/** ��ǰ����λ�ö�λ */
	// @ViewId(id=R.id.btn_location)
	public Button btn_location = null;
	/** �Ŵ��ͼ */
	// @ViewId(id=R.id.btn_zoomin)
	public Button btn_zoomin = null;
	/** ��С��ͼ */
	// @ViewId(id=R.id.btn_zoomout)
	public Button btn_zoomout = null;
	/** ���õ�ͼ��Χ��ť */
	// @ViewId(id=R.id.btn_zoomfull)
	public Button btn_zoomfull = null;

	/** ��λ������ */
	public LocationManager locationManager = null;
	/** ��λʵ���� */
	public Location location = null;
	/** ��λͼ�� */
	private GraphicsLayer loctionLayer = null;
	/** ��עͼ�� */
	public Callout callout;
	/** ���ŵ��˹̶����г� */
	public final Double scale = 1292765.4170153292;
	/** ������ͼ�� */
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
		// ��ʼ����λͼ��
				loctionLayer = new GraphicsLayer();
		// ��ʼ����ͼ����
		InitData();
		
		// ��ʼ��������ͼ��
		tLayer = new GraphicsLayer();
		// ���õ�ͼ������ɫ
		map.setBackgroundColor(Color.WHITE);
		map.addLayer(tLayer);
		map.addLayer(loctionLayer);
	}

	/** ��ʼ����ͼ��ӵĿؼ� */
	protected void InitView() {
	}

	/**
	 * ��ӵ�ͼ��ʼͼ��
	 * 
	 * @return Ĭ�ϵ�ͼ·��������gis_config�ļ���map2 Type�µ�UrL�ֶ�
	 */
	protected abstract void addMapLayer();

	/** ��ʼ����ͼ���� */
	protected void InitData() {
		map = (MapView) findViewById(R.id.map);
		btn_location = (Button) findViewById(R.id.btn_location); // ��λ
		btn_zoomin = (Button) findViewById(R.id.btn_zoomin);// �Ŵ�
		btn_zoomfull = (Button) findViewById(R.id.btn_zoomfull);// Զ����Ұ
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

	/** �ж��ֻ��Ƿ���GPS */
	public boolean bGpsEnable() {
		if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			Toast.makeText(ArcGisActivity.this, "GPSδ��", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	/**
	 * GPS���ؼ���
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
				Toast.makeText(ArcGisActivity.this, "���ڹر�λ�ù���������Ե�...", 1000).show();
			} else {
				image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.gis_tool_locationshare);
				Toast.makeText(ArcGisActivity.this, "���ڿ���λ�ù���������Ե�...", 1000).show();
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
	 * ��ͼ�������ü���
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

	/** ��ȡ��õ�provider ���GPS��ͨ��ȫ��������null */
	protected String getBestProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���
		String provider = locationManager.getBestProvider(criteria, true);
		return provider;
	}

	/**
	 * Description: ��ʼ����ͼ��Χ
	 * 
	 * @author Administrator<br>
	 *         Create at: 2013-3-14 ����05:32:32
	 */
	public abstract void loadFullMap();

	/**
	 * ��λ��ť����
	 * 
	 * @author
	 * 
	 */
	class MyLocationListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			String bestProvider = getBestProvider();
			
			if (bestProvider == null) {// ����providerΪnull
				locationMySelf(null);
			} else {
				location = locationManager.getLastKnownLocation(bestProvider);
				if (location != null) {
					locationMySelf(location);
				} else {
					Toast.makeText(ArcGisActivity.this, "��ǰ�޷�ȷ�������ڵ�λ��", 1).show();
				}
			}
		}
	}

	/** ��λ����ǰ����λ�� */
	public void locationMySelf(Location location) {
		if (location != null) {
			SpatialReference sr4326 = SpatialReference.create(4326);
			//TODO  �ǵûָ�ԭ��   ģ��λ��
		Point point = (Point) GeometryEngine.project(new Point(location.getLongitude(), location.getLatitude()), sr4326, map.getSpatialReference());
	//		Point point = (Point) GeometryEngine.project(new Point(126.528395, 45.80216), sr4326, map.getSpatialReference());
			map.centerAt(point, true);
		//	Drawable image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.icon_my);
			Drawable image = ArcGisActivity.this.getBaseContext().getResources().getDrawable(R.drawable.hbdt_gis_icon_loc);
			PictureMarkerSymbol imageMarkerSymbol = new PictureMarkerSymbol(image);
			Graphic g = new Graphic(point, imageMarkerSymbol, null, null);
			loctionLayer.addGraphic(g);
		} else {
			Toast.makeText(ArcGisActivity.this, "���������GPS�Ƿ�����", 1).show();
		}
	}

	/**
	 * ��ӱ�ע
	 * 
	 * @param pt
	 *            ��ע�ĵ�
	 * @param content
	 *            ��ע������
	 * @param isListener
	 *            �Ƿ�ΪTextView���ü���
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
		tv.setBackgroundColor(Color.TRANSPARENT);// ����Ϊ͸������

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
	 * ���ݾ�γ�Ƚ��е�λ �����б�ע
	 * 
	 * @param longitude
	 *            ����
	 * @param latitude
	 *            γ��
	 * @param flag
	 *            ��ע
	 * @param attribute
	 *            λͼ��Ҫ�ļ��ϴ����Ϣ
	 * @param isListener
	 *            �Ƿ�Ϊ��ע��Ӽ����¼�
	 * @param image
	 *            ���ǵ�ͼƬ
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
