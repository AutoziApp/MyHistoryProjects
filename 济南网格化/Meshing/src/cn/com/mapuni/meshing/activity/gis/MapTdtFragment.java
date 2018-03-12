package cn.com.mapuni.meshing.activity.gis;

import cn.com.mapuni.meshing.activity.NewMainActivity;
import cn.com.mapuni.meshing.activity.db_activity.NewDbMainActivity;
import cn.com.mapuni.meshing.activity.xc_activity.XcMainActivity;
import com.example.meshing.R;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MapTdtFragment extends Fragment {
	public MapView mMapView;
	public FrameLayout content_layout;
	public MapController mMapController;
	public LocationService.MyBinder myBinder;
	public static View xcPopView;//巡查任务界面弹窗
	public static View dbPopView;//待办任务界面弹窗
	public static View childStationView;//空气子站弹窗
	int position=-1;
	public MapTdtFragment(int position){
		this.position=position;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mapLayout = inflater.inflate(R.layout.basemap_layout,
				container, false);
		initMapView(mapLayout);
		startMapSerview();
		xcPopView = inflater.inflate(R.layout.popview_jgdx, null);
		dbPopView = inflater.inflate(R.layout.popview, null);
		childStationView = inflater.inflate(R.layout.popview_station_child, null);
		mMapView.addView(xcPopView, new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
		mMapView.addView(dbPopView, new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
		mMapView.addView(childStationView, new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
		changeView(position);
		return mapLayout;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(false==isFirst){
			if(1==preId){
				new NewDbMainActivity(getActivity(), content_layout, mMapView).showView();
			}
			
		}
		isFirst=false;
		
		// //////////////////////////////////////不具有巡检功能的角色屏蔽巡检页面
//		String havePatrolRole = DisplayUitl.readPreferences(getActivity(), "lastuser", "havePatrolRole");
//		String haveAdminRole = DisplayUitl.readPreferences(getActivity(), "lastuser", "haveAdminRole");
//		String haveLiaisonRole = DisplayUitl.readPreferences(getActivity(), "lastuser", "haveLiaisonRole");
//		String organization_code = DisplayUitl.readPreferences(getActivity(), "lastuser", "organization_code");
//		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
//				|| (organization_code.length() == 10)) {
//			changeView(0);
//		} else {
//			changeView(1);
//		}
	}
	boolean isFirst=true;
	int preId=-1;
	
	public void changeView(int id) {
		preId=id;
		xcPopView.setVisibility(View.GONE);//默认隐藏
		dbPopView.setVisibility(View.GONE);//默认隐藏
		childStationView.setVisibility(View.GONE);//默认隐藏
		try {
			switch (id) {
			case 0:
				new XcMainActivity(getActivity(), content_layout, mMapView).showView();
				break;
			case 1:
				new NewDbMainActivity(getActivity(), content_layout, mMapView).showView();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("lfwang", "================");
		}
	}
	
	/**
	 * 初始化地图视图
	 */
	private void initMapView(View view) {
		content_layout = (FrameLayout) view.findViewById(R.id.content_layout);
		mMapView = (MapView) view.findViewById(R.id.mapview);
		mMapView.setLogoPos(MapView.LOGO_LEFT_TOP);
		mMapView.setBuiltInZoomControls(false);// 内部缩放控件不显示
		mMapView.enableRotate(false);
		mMapView.setMapType(MapView.TMapType.MAP_TYPE_VEC);
	}

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (LocationService.MyBinder) service;
			MapBinder.getInstance().setBinder(myBinder);
			myBinder.start(getActivity().getApplicationContext(), mMapView);
		}
	};

	private void startMapSerview() {
		Intent bindIntent = new Intent(getActivity().getApplicationContext(),
				LocationService.class);
		getActivity().getApplicationContext().bindService(bindIntent, connection,
				Context.BIND_AUTO_CREATE);
	}
	
}
