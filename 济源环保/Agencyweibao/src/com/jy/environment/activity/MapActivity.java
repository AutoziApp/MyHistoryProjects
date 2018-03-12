package com.jy.environment.activity;
//package com.pds.environment.activity;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
//import com.baidu.mapapi.model.LatLng;
//import com.pds.environment.R;
//import com.pds.environment.base.ActivityBase;
//import com.pds.environment.controls.WeiBaoApplication;
//import com.pds.environment.database.dal.CityDB;
//import com.pds.environment.util.CommonUtil;
//import com.pds.environment.util.WbMapUtil;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.MeasureSpec;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class MapActivity extends ActivityBase implements OnClickListener {
//
//	TextView aqi_show_btn_AQI;
//	TextView aqi_show_btn_PM10;
//	TextView aqi_show_btn_PM25;
//	TextView aqi_show_btn_SO2;
//	TextView aqi_show_btn_NO2;
//	TextView aqi_show_btn_O3;
//	TextView aqi_show_btn_CO;
//	LinearLayout aqi_show_btn_layout;
//	private TextView aqi_show_btn;
//	private TextView update_time;
//	private ImageView change_map;
//	private LinearLayout change_map_layout;
//	private ImageView change_map_normol;
//	private ImageView change_map_weixing;
//	ImageView mapLengend;
//	Dialog dialog;
//	CityDB mCityDB = null;
//	Context context;
//	MapView mMapView = null; // 地图View
//	private BaiduMap mBaiduMap;  
//	
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.map_miann_activity);
//		context = this;
//		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog, R.layout.custom_progress_dialog);
//		dialog.setCanceledOnTouchOutside(false);
//		mCityDB = WeiBaoApplication.getInstance().getCityDB();
//		// initView();// 初始化右侧一排按钮。。。
//		mMapView = (MapView) findViewById(R.id.bmapView);
//		
//		mBaiduMap= mMapView.getMap();
//		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
//			@Override
//			public boolean onMapPoiClick(MapPoi arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			@Override
//			public void onMapClick(LatLng arg0) {
//			}
//		});
//		mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//		update_time = (TextView) findViewById(R.id.update_time);
//		MapStatus ms = new MapStatus.Builder().zoom(12).build();
//		mMapView.getMap().setMapStatus(
//				MapStatusUpdateFactory.newMapStatus(ms));
//
//		mapLengend = (ImageView) findViewById(R.id.map_lengend);
//		city = WeiBaoApplication.selectedCity; // app.selectedCity;
//		/**
//		 * 设置地图缩放级别
//		 */
//		// mMapController.setZoom(11);
//		mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(9));
//		/**
//		 * 显示内置缩放控件
//		 */
//		// mMapView.setBuiltInZoomControls(false);
//		mMapView.showZoomControls(false);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//	
//	
//	
//
//	/**
//	 * 添加标注的覆盖物
//	 * 
//	 * @param layout_id
//	 * @return
//	 */
//	public Drawable LayoutToDrawableMapDengJi(int layout_id, String pointname,
//			String aqi, Drawable pointdrawable) {
//		LayoutInflater inflator = LayoutInflater.from(this);
//		View viewHelp = inflator.inflate(layout_id, null);
//		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
//		TextView pointvalue = (TextView) viewHelp.findViewById(R.id.pointvalue);
//		textView.setText(aqi);
//		textView.setText(aqi);
//		int intAqi = Integer.parseInt(aqi);
//		if (intAqi <= 150) {
//			textView.setTextColor(Color.parseColor("#000033"));
//		}
//		pointvalue.setText(pointname);
//		textView.setBackgroundDrawable(pointdrawable);
//		int size = (int) textView.getText().length();
//		Drawable drawable = null;
//		try {
//			Bitmap snapshot = convertViewToBitmap(viewHelp, size);
//			drawable = (Drawable) new BitmapDrawable(snapshot);
//		} catch (OutOfMemoryError e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		return drawable;
//	}
//
//	/**
//	 * 添加标注的覆盖物
//	 * 
//	 * @param layout_id
//	 * @return
//	 */
//	public Drawable LayoutToDrawable(int layout_id, String pointname,
//			Drawable pointdrawable) {
//		LayoutInflater inflator = LayoutInflater.from(this);
//		View viewHelp = inflator.inflate(layout_id, null);
//		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
//		ImageView imageView = (ImageView) viewHelp.findViewById(R.id.display);
//		textView.setText(pointname);
//		imageView.setBackgroundDrawable(pointdrawable);
//		int size = (int) textView.getText().length();
//		Drawable drawable = null;
//		try {
//			Bitmap snapshot = convertViewToBitmap(viewHelp, size);
//			drawable = (Drawable) new BitmapDrawable(snapshot);
//		} catch (OutOfMemoryError e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		return drawable;
//	}
//
//	/**
//	 * 添加标注的覆盖物
//	 * 
//	 * @param layout_id
//	 * @return
//	 */
//	public Bitmap LayoutToBitmap(int layout_id, String pointname,
//			Drawable pointdrawable) {
//		LayoutInflater inflator = LayoutInflater.from(this);
//		View viewHelp = inflator.inflate(layout_id, null);
//		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
//		ImageView imageView = (ImageView) viewHelp.findViewById(R.id.display);
//		textView.setText(pointname);
//		imageView.setBackgroundDrawable(pointdrawable);
//		int size = (int) textView.getText().length();
//		Bitmap snapshot = convertViewToBitmap(viewHelp, size);
//		return snapshot;
//	}
//
//	/**
//	 * 添加标注的覆盖物
//	 * 
//	 * @param layout_id
//	 * @return
//	 */
//	private Bitmap sharePicureLayoutToDrawable(int layout_id, Drawable img) {
//		LayoutInflater inflator = LayoutInflater.from(this);
//		View viewHelp = inflator.inflate(layout_id, null);
//
//		ImageView imageView = (ImageView) viewHelp
//				.findViewById(R.id.sharepicture);
//
//		imageView.setImageDrawable(img);
//
//		viewHelp.measure(
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		viewHelp.layout(0, 0, WbMapUtil.dip2px(this, 44),
//				WbMapUtil.dip2px(this, 44));
//
//		viewHelp.buildDrawingCache();
//		viewHelp.setDrawingCacheEnabled(true);
//		Bitmap snapshot = viewHelp.getDrawingCache();
//		// viewHelp.setDrawingCacheEnabled(false);
//		// Bitmap snapshot = convertViewToBitmap(viewHelp, size);
//		// Drawable drawable = (Drawable)new BitmapDrawable(snapshot);
//		return snapshot;
//	}
//
//	/**
//	 * 将加标注的覆盖物的View转成BitMap
//	 * 
//	 * @param view
//	 * @param size
//	 * @return
//	 */
//	public static Bitmap convertViewToBitmap(View view, int size) {
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		int width = size * 100;
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight()); // 根据字符串的长度显示view的宽度
//		view.buildDrawingCache();
//		view.setDrawingCacheEnabled(true);
//		Bitmap bitmap = view.getDrawingCache();
//
//		return bitmap;
//	}
//}
