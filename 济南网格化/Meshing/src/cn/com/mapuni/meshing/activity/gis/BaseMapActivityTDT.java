package cn.com.mapuni.meshing.activity.gis;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.FrameLayout;

import com.example.meshing.R;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

/**
 * 天地图
 * 
 * @author Administrator
 * 
 */
public class BaseMapActivityTDT extends Activity {
	public MapView mMapView;
	public FrameLayout content_layout;
	public MapController mMapController;
	private ViewCallback callback;
	public LocationService.MyBinder myBinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basemap_layout);
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
		content_layout = (FrameLayout) findViewById(R.id.content_layout);
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setLogoPos(MapView.LOGO_LEFT_TOP);
		mMapView.setBuiltInZoomControls(false);// 内部缩放控件不显示
		mMapView.enableRotate(false);
		mMapView.setMapType(MapView.TMapType.MAP_TYPE_VEC);
		callback.onCreate(content_layout,mMapView);
	}

	interface ViewCallback {
		public void onCreate(FrameLayout view,MapView mapView);
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
