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
		 * ��ȡ��ǰλ�õĵ���λ������ <br>
		 * ע����֤�����Լ���������location ����Ϊ��
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
				Log.i(TAG, "----------��ȡ��ַʧ�� point---------");
				return;
			}
			if (addr == null) {
				Log.i(TAG, "----------��ȡ��ַʧ�� point---------");
				return;
			}
			mapview.getController().setMapBound(mGeoPoint, 11);
			// ��ʾ
//			String str = "point =  " + mGeoPoint.toString() + "\n";
//			str += "�����poi����:" + addr.getPoiName() + "\n";
//			str += "���poi�ķ�λ:" + addr.getPoiDirection() + "\n";
//			str += "���poi�ľ���:" + addr.getPoiDistance() + "\n";
//			str += "��������:" + addr.getCity() + "\n";
//			str += "ȫ��:" + addr.getFullName() + "\n";
//			str += "����ĵ�ַ:" + addr.getAddress() + "\n";
//			str += "�����ַ�ķ�λ:" + addr.getAddrDirection() + "\n";
//			str += "�����ַ�ľ���:" + addr.getAddrDistance() + "\n";
//			str += "����ĵ�·����:" + addr.getRoadName() + "\n";
//			str += "�����·�ľ���:" + addr.getRoadDistance();
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
     * ���ͼ��ȡ��γ����
     * @author Administrator
     *
     */
	class MyOverlay extends MyLocationOverlay {
		public MyOverlay(Context context, MapView mapView) {
			super(context, mapView);
		}

		/*
		 * ������"�ҵ�λ��"�ϵĵ���¼�
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
     * ��ȥ��ǰ��ַ�ص��ӿ�
     * @author Administrator
     *
     */
	public interface CallBackAdrr {
		public void callbackadrr(String adrr);
	}
}
