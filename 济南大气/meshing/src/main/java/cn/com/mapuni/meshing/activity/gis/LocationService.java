package cn.com.mapuni.meshing.activity.gis;

import java.io.Serializable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.TErrorCode;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.TGeoDecode.OnGeoResultListener;

public class LocationService extends Service {
	private static String TAG = "LocationService";
	public MyBinder mBinder = new MyBinder();
	private Location Mlocation;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "------------onCreate------------");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "------------onStartCommand------------");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "------------onDestroy------------");
		super.onDestroy();
	}

	public class MyBinder extends Binder implements OnGeoResultListener {
		private GeoPoint mGeoPoint;
		private Context context;
		private MapView mapview;
		private TGeoDecode mGeoDecode;
		private CallBackAdrr callback;

		public void start(Context context, MapView mapview) {
			this.context = context;
			this.mapview = mapview;
			new MyOverlay(context, mapview).enableMyLocation();
		}

		public void getLocation() {

		}

		/**
		 * 获取当前位置的地理位置名称 <br>
		 * 注：保证服务以及开启，且location 不能为空
		 * 
		 * @param callback
		 */
		public void getLocationAddr(CallBackAdrr callback) {
			if (Mlocation != null) {
				this.callback = callback;
				GeoPoint geoPoint = new GeoPoint(
						(int) (Mlocation.getLatitude() * 1E6),
						(int) (Mlocation.getLongitude() * 1E6));
				searchGeoDecode(geoPoint);
			}
		}

		@Override
		public void onGeoDecodeResult(TGeoAddress addr, int errCode) {

			if (errCode != TErrorCode.OK) {
				Log.i(TAG, "----------获取地址失败 point---------");
				return;
			}
			if (addr == null) {
				Log.i(TAG, "----------获取地址失败 point---------");
				return;
			}
			mapview.getController().setMapBound(mGeoPoint, 11);
			// 提示
//			String str = "point =  " + mGeoPoint.toString() + "\n";
//			str += "最近的poi名称:" + addr.getPoiName() + "\n";
//			str += "最近poi的方位:" + addr.getPoiDirection() + "\n";
//			str += "最近poi的距离:" + addr.getPoiDistance() + "\n";
//			str += "城市名称:" + addr.getCity() + "\n";
//			str += "全称:" + addr.getFullName() + "\n";
//			str += "最近的地址:" + addr.getAddress() + "\n";
//			str += "最近地址的方位:" + addr.getAddrDirection() + "\n";
//			str += "最近地址的距离:" + addr.getAddrDistance() + "\n";
//			str += "最近的道路名称:" + addr.getRoadName() + "\n";
//			str += "最近道路的距离:" + addr.getRoadDistance();
//			Log.i(TAG, "----------" + str + "---------");
			callback.callbackadrr(addr.getFullName());
		}

		private void searchGeoDecode(GeoPoint geoPoint) {
			mGeoPoint = geoPoint;
			mapview.getController().setMapBound(mGeoPoint, 11);
			if (mGeoDecode == null) {
				mGeoDecode = new TGeoDecode(this);
			}
			mGeoDecode.search(geoPoint);
		}
	}
    /**
     * 天地图获取经纬度类
     * @author Administrator
     *
     */
	class MyOverlay extends MyLocationOverlay {
		public MyOverlay(Context context, MapView mapView) {
			super(context, mapView);
		}

		/*
		 * 处理在"我的位置"上的点击事件
		 */
		protected boolean dispatchTap() {
			Log.i(TAG, "---------dispatchTap-------------");
			return true;
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, "---------onLocationChanged-------------");
			Mlocation = location;
			super.onLocationChanged(location);
		}
	}
     
	 
    /**
     * 回去当前地址回调接口
     * @author Administrator
     *
     */
	public interface CallBackAdrr {
		public void callbackadrr(String adrr);
	}
}
