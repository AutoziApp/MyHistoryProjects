package cn.com.mapuni.gis.meshingtotal.tdt;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.gis.meshingtotal.R;

/**
 * 天地图
 * 
 * @author Administrator
 * 
 */
public class BaseMapActivityTDT extends BaseActivity {
	public MapView mMapView;
	public FrameLayout content_layout;
	public MapController mMapController;
	private ViewCallback callback;
	public LocationService.MyBinder myBinder;
	public static String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		setBACK_ISSHOW(true);
		title = getIntent().getExtras().getString("title");
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), title);
//		setContentView(R.layout.basemap_layout);
		if (this instanceof ViewCallback) {
			callback = (ViewCallback) this;
		}
		initMapView();
		startMapSerview();
	}

	/**
	 * 初始化地图视图
	 */
	private void initMapView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.basemap_layout, null);
		middleLayout.addView(mainView);
		
		content_layout = (FrameLayout) mainView.findViewById(R.id.content_layout);
		mMapView = (MapView) mainView.findViewById(R.id.mapview);
		mMapView.setLogoPos(MapView.LOGO_LEFT_TOP);
		mMapView.setBuiltInZoomControls(false);// 内部缩放控件不显示
		mMapView.enableRotate(false);
		mMapView.setMapType(MapView.TMapType.MAP_TYPE_VEC);
		callback.onCreate(content_layout,mMapView);
	}

	public interface ViewCallback {
		public void onCreate(FrameLayout view, MapView mapView);
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (LocationService.MyBinder) service;
			MapBinder.getInstance().setBinder(myBinder);
			myBinder.start(getApplicationContext(), mMapView);
		}
	};

	private void startMapSerview() {
		Intent bindIntent = new Intent(this.getApplicationContext(),
				LocationService.class);
		this.getApplicationContext().bindService(bindIntent, connection,
				BIND_AUTO_CREATE);
	}

}
