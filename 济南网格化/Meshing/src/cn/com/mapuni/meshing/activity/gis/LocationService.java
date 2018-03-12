package cn.com.mapuni.meshing.activity.gis;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
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
	//private CallBackAdrrLonLat callBackAdrrLonLat;

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
		private CallBackAdrrPoint callBackAdrrPoint;
		//private CallBackAdrrLonLat callBackAdrrLonLat;
		private MyOverlay myOverlay;

		public void start(Context context, MapView mapview) {
			this.context = context;
			this.mapview = mapview;
			myOverlay = new MyOverlay(context, mapview);
			myOverlay.enableMyLocation();
		}
		
		public MyOverlay getOverlay() {
			if(myOverlay == null) {
				myOverlay = new MyOverlay(context, mapview);
				myOverlay.enableMyLocation();
			}
			return myOverlay;
		}

		/**
		 * 获取当前位置的地理位置名称 <br>
		 * 注：保证服务以及开启，且location 不能为空
		 * 
		 * @param callback
		 */
		public void CallBackAdrrPoint(CallBackAdrrPoint callback) {
			if (Mlocation != null) {
				this.callBackAdrrPoint = callback;
				GeoPoint geoPoint = new GeoPoint(
						(int) (Mlocation.getLatitude() * 1E6),
						(int) (Mlocation.getLongitude() * 1E6));
				searchGeoDecode(geoPoint);
			}
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
		/**
		 * 获取当前位置的地理位置名称 <br>
		 * 注：保证服务以及开启，且location 不能为空
		 * 
		 * @param callback
		 *//*
		public void getLocationLonLat(CallBackAdrrLonLat callBackAdrrLonLat) {
			if (Mlocation != null) {
				this.callBackAdrrLonLat = callBackAdrrLonLat;
				callBackAdrrLonLat.CallBackAdrrLonLat(Mlocation.getLongitude(),Mlocation.getLatitude());

			}
		}*/

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
//			mapview.getController().setMapBound(mGeoPoint, 11);//暂时注掉根据当前经纬度自动跳转到当前位置，防止地图来回切换
			// // 提示
			// String str = "point =  " + mGeoPoint.toString() + "\n";
			// str += "最近的poi名称:" + addr.getPoiName() + "\n";
			// str += "最近poi的方位:" + addr.getPoiDirection() + "\n";
			// str += "最近poi的距离:" + addr.getPoiDistance() + "\n";
			// str += "城市名称:" + addr.getCity() + "\n";
			// str += "全称:" + addr.getFullName() + "\n";
			// str += "最近的地址:" + addr.getAddress() + "\n";
			// str += "最近地址的方位:" + addr.getAddrDirection() + "\n";
			// str += "最近地址的距离:" + addr.getAddrDistance() + "\n";
			// str += "最近的道路名称:" + addr.getRoadName() + "\n";
			// str += "最近道路的距离:" + addr.getRoadDistance();
			// Log.i(TAG, "----------" + str + "---------");
			callBackAdrrPoint.CallBackAdrrPoint(mGeoPoint);
			callback.callbackadrr(addr.getFullName(),mGeoPoint);
		}

		private void searchGeoDecode(GeoPoint geoPoint) {
			mGeoPoint = geoPoint;
//			mapview.getController().setMapBound(mGeoPoint, 11);//暂时注掉根据当前经纬度自动跳转到当前位置，防止地图来回切换
			if (mGeoDecode == null) {
				mGeoDecode = new TGeoDecode(this);
			}
			mGeoDecode.search(geoPoint);
		}
	}
    
	
	
	
	
	/**
	 * 天地图获取经纬度类
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyOverlay extends MyLocationOverlay {
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
	 * 获取当前地址回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface CallBackAdrr {
		public void callbackadrr(String adrr,GeoPoint mGeoPoint);
	}

	/**
	 * 获取当前地址回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface CallBackAdrrPoint {
		public void CallBackAdrrPoint(GeoPoint mGeoPoint);
	}
/*	*//**
	 * 获取当前地址回调接口
	 * 
	 * @author Administrator
	 * 
	 *//*
	public interface CallBackAdrrLonLat {
		public void CallBackAdrrLonLat(Double lon,Double lat);
	}*/

}
