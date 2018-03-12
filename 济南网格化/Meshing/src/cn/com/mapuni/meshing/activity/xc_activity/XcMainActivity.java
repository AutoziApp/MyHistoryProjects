package cn.com.mapuni.meshing.activity.xc_activity;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.load.engine.Resource;
import com.example.meshing.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.renderoption.DrawableOption;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.activity.db_activity.DbMainActivity;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.gis.MapTdtFragment;
import cn.com.mapuni.meshing.adapter.DbNewsAdapyer;
import cn.com.mapuni.meshing.model.AirChildStationModel;
import cn.com.mapuni.meshing.model.PotrlObject;

@SuppressLint("Instantiatable")
public class XcMainActivity implements View.OnClickListener {
	private ImageButton wdgj_bu, sbrw_bu, qd_bu;
	private Context Mcontent;
	private FrameLayout Mview;
	private MapView mapView;
	// private String lat = "", lon = "";
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	public XcMainActivity(Context mcontent, FrameLayout mview, MapView mapView) {
		mapView.removeAllOverlay();// 清空所有覆盖物
		mview.removeAllViews();// 清除界面所有视图
		this.Mcontent = mcontent;
		this.Mview = mview;
		this.mapView = mapView;
	}

	public void showView() {
		initView();
		// setadrr();
		setMyPostionMaker();
		getJgdx();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		if (null != DbMainActivity.mPopView) {
			DbMainActivity.mPopView.setVisibility(View.GONE);
		}
		// 地图视图
		mapView.setBuiltInZoomControls(false);// 是否显示地图缩放按钮
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController = mapView.getController();
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint point = new GeoPoint((int) (36.674678 * 1E6), (int) (116.998955 * 1E6));
		// 设置地图中心点
		mMapController.setCenter(point);
		// 设置地图zoom级别
		mMapController.setZoom(12);
		LayoutInflater inflater = LayoutInflater.from(Mcontent);
		// 查询条件
		View mainView = inflater.inflate(R.layout.xcmainactivity_layout, null);
		sbrw_bu = (ImageButton) mainView.findViewById(R.id.sbrw_bu);
		String haveInspectorRole = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "haveInspectorRole");// 巡检员
		String haveFreeRole = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "haveFreeRole");// 自由巡检员
		String havePatrolRole = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "havePatrolRole");// 中心负责人
		String haveAdminRole = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "haveAdminRole");// 管理员
		String haveLiaisonRole = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "haveLiaisonRole");// 联络员
		String userGridLevel = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "userGridLevel");
		String organization_code = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "organization_code");
		// if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) &&
		// !"1".equals(haveLiaisonRole))
		// || (organization_code.length() == 10)) {
		// sbrw_bu.setVisibility(View.VISIBLE);
		// }

		if (userGridLevel.equals("4") || userGridLevel.equals("3")) {
			sbrw_bu.setVisibility(View.VISIBLE);
		}
		// if("1".equals(haveInspectorRole)||"1".equals(haveFreeRole)){
		// sbrw_bu.setVisibility(View.VISIBLE);
		// }
		qd_bu = (ImageButton) mainView.findViewById(R.id.qd_bu);
		sbrw_bu.setOnClickListener(this);
		qd_bu.setOnClickListener(this);

		Mview.addView(mainView);
	}

	/**
	 * 查看轨迹
	 */
	private void guiji() {

	}

	/**
	 * 上报任务
	 */
	private void sbrw() {
		Intent intent = new Intent(Mcontent, XcrwActivity.class);
		Mcontent.startActivity(intent);
	}

	/**
	 * 签到
	 */
	private void qiandao() {
		Intent intent = new Intent(Mcontent, QiandaoActivity.class);
		Mcontent.startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}
		switch (view.getId()) {
		case R.id.showhind:
			if (news.getVisibility() == View.VISIBLE) {
				news.setVisibility(View.GONE);
			} else {
				news.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.sbrw_bu:
			sbrw();
			break;
		case R.id.qd_bu:
			qiandao();
			break;
		default:
			break;
		}
	}

	/**
	 * 定位
	 */
	// private void setadrr() {
	// MapBinder.getInstance().getBinder().getLocationAddr(new CallBackAdrr() {
	//
	// @Override
	// public void callbackadrr(String r, GeoPoint mGeoPoint) {
	// lon = String.valueOf(mGeoPoint.getLongitudeE6());
	// lat = String.valueOf(mGeoPoint.getLatitudeE6());
	// }
	// });
	// }

	/**
	 * 定位到当前位置
	 */
	private void setMyPostionMaker() {
		MyLocationOverlay myLocation = new MyLocationOverlay(Mcontent, mapView);
		myLocation.enableCompass(); // 显示指南针
		myLocation.enableMyLocation(); // 显示我的位置
		// 定位之前清理覆盖物
		mapView.removeAllOverlay();
		mapView.getController().setZoom(14);
		mapView.getOverlays().add(myLocation);
	}

	/*
	 * private void addMyOverlayItem(GeoPoint point) {
	 * 
	 * MarkerOverlay overlay = new MarkerOverlay(); overlay.setPosition(point);
	 * overlay.setIcon(Mcontent.getResources().getDrawable(R.drawable.gps_point)
	 * ); mapView.addOverlay(overlay); }
	 */

	List<PotrlObject.RowsBean> Objects = new ArrayList<PotrlObject.RowsBean>();// 监管对象全局
	List<AirChildStationModel> listStationModels;
	HttpUtils utils;
	private void getJgdx() {
		final YutuLoading yutuLoading = new YutuLoading(Mcontent);
		yutuLoading.setLoadMsg("正在获取监管对象数据，请稍候", "");
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();

		utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		utils.configRequestRetryCount(0);
		RequestParams params = new RequestParams();// 添加提交参数
		String sessionId = DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "sessionId");
		params.addBodyParameter("sessionId", sessionId);
		String url = PathManager.GETXUNCHAOBJECT_URL_JINAN + "?sessionId=" + sessionId;
		
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(Mcontent, "数据请求失败", 200).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
				String resoult = String.valueOf(arg0.result);
				Gson gson = new Gson();
				PotrlObject potrlObject = gson.fromJson(resoult, PotrlObject.class);
				if ("200".equals(potrlObject.getStatus())) {
					Objects = potrlObject.getRows();
					
				}
				
				utils.send(HttpMethod.GET, PathManager.Air_Child_Station_Url, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						Toast.makeText(Mcontent, "数据请求失败", 200).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						String resoult = String.valueOf(arg0.result);
						Gson gson = new Gson();
						List<AirChildStationModel> list= gson.fromJson(resoult,new TypeToken<List<AirChildStationModel>>() {}.getType());
						listStationModels = new ArrayList<AirChildStationModel>();
						for (int i = 0; i < list.size(); i++) {
							if(!TextUtils.isEmpty(list.get(i).getStationName())){
								listStationModels.add(list.get(i));
							}
						}
						initChildStationList();
						if(listStationModels != null && Objects != null) {
							showJgdx();
						}
					}
				});

			}
		});
	}

	LinearLayout news;
	MapController mMapController;
	private OverItemT mOverlay = null;
	private ImageView ivClose;
	private OverItemT2 mChildStationOverlay = null;
	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
    private TextView biaoti1;
    private TextView biaoti2;
    private TextView biaoti3;
    private TextView biaoti4;
    private TextView biaoti5;
    private Button leftButton, rightButton;
    private DbNewsAdapyer adapter;
    private ListView listView;
    private TextView showHind;
	private void initChildStationList() {
		  View mainView = LayoutInflater.from(Mcontent).inflate(
	                R.layout.airchildlist, null);
		  Mview.addView(mainView);
	        leftButton = (Button) mainView.findViewById(R.id.left_button);
	        rightButton = (Button) mainView.findViewById(R.id.rightbutton);
	        
	        biaoti1 = (TextView) mainView.findViewById(R.id.biaoti1);
	        biaoti2 = (TextView) mainView.findViewById(R.id.biaoti2);
	        biaoti3 = (TextView) mainView.findViewById(R.id.biaoti3);
	        biaoti4 = (TextView) mainView.findViewById(R.id.biaoti4);
	        biaoti5 = (TextView) mainView.findViewById(R.id.biaoti5);
	        biaoti1.setText("空气质量");
	        biaoti2.setText("监测点");
	        biaoti3.setText("详情");
	        biaoti4.setText("");
	        biaoti4.setVisibility(View.GONE);
	        biaoti5.setText("");
	        biaoti5.setVisibility(View.GONE);
	        news = (LinearLayout) mainView.findViewById(R.id.news);
	        showHind = (TextView) mainView.findViewById(R.id.showhind);
	        // listview添加模拟数据
	        listView = (ListView) mainView.findViewById(R.id.lv);
	        showHind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 if (news.getVisibility() == View.VISIBLE) {
			                news.setVisibility(View.GONE);
			            } else {
			                news.setVisibility(View.VISIBLE);
			            }
				}
			});

	        adapter = new DbNewsAdapyer(Mcontent, listStationModels);
	        listView.setAdapter(adapter);
	        listView.setOnItemClickListener(new OnItemClickListener() {

	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                                    int position, long id) {
	                // TODO Auto-generated method stub
	               
	            	try {
						double lon = Double.parseDouble(listStationModels.get(position).getLongitude());// 经度
						double lat = Double.parseDouble(listStationModels.get(position).getLatitude());// 纬度
						if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
							return;
						}
						if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
							return;
						}
						GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
						MapTdtFragment.xcPopView.setVisibility(View.GONE);
						mapView.updateViewLayout(MapTdtFragment.childStationView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT, point, 0, 0, MapView.LayoutParams.BOTTOM_CENTER));
						MapTdtFragment.childStationView.setVisibility(View.VISIBLE);
						mapView.getController().animateTo(point);
						TextView tv_name = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_station_name);
						TextView tv_pm10 = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_pm10);
						TextView tv_fx = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fx);
						TextView tv_fs = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fs);
						TextView tv_sd = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_sd);
						TextView tv_qy = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qy);
						TextView tv_qw = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qw);
						ImageView ivClose = (ImageView) MapTdtFragment.childStationView.findViewById(R.id.close);
						ivClose.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								MapTdtFragment.childStationView.setVisibility(View.GONE);
							}
						});
						int j = position;
						if (listStationModels.get(j).getStationName() != null) {
							tv_name.setText(listStationModels.get(j).getStationName()+"("+listStationModels.get(j).getTime()+")");
						}
						if (listStationModels.get(j).getPm10() != null) {
							String pm10 = "";
							try {
								double pm = Double.parseDouble(listStationModels.get(j).getPm10()) * 1000;
								int pmint = (int) pm;
								pm10 = pmint+"";
							} catch (Exception e) {
								// TODO: handle exception
							}
							
							tv_pm10.setText( pm10);
						}
						if (listStationModels.get(j).getPressure() != null) {
							tv_qy.setText(listStationModels.get(j).getPressure());
						}
						if (listStationModels.get(j).getTemp() != null) {
							tv_qw.setText(listStationModels.get(j).getTemp());
						}
						if (listStationModels.get(j).getHumidity()!= null) {
							tv_sd.setText(listStationModels.get(j).getHumidity());
						}
						if (listStationModels.get(j).getWdir()!= null) {
							tv_fx.setText(listStationModels.get(j).getWdir());
						}
						if (listStationModels.get(j).getWs()!= null) {
							tv_fs.setText(listStationModels.get(j).getWs());
						}
					} catch (Exception e) {
						// TODO: handle exception
						
					}
	            }
	        });
	        
	}
	private List<OverlayItem> mChildStationList = new ArrayList<OverlayItem>();
	private void showJgdx() {
		List<Overlay> list = mapView.getOverlays();

		MyLocationOverlay myLocation = new MyLocationOverlay(Mcontent, mapView);
		myLocation.enableMyLocation();
		// list.add(myLocation);
		Drawable marker = Mcontent.getResources().getDrawable(R.drawable.jgdx_wry);
		Drawable marker2 = Mcontent.getResources().getDrawable(R.drawable.ari1);
		mOverlay = new OverItemT(marker);
		
		
		list.add(mOverlay);
//		mChildStationOverlay = new OverItemT2(marker2);
//		list.add(mChildStationOverlay);
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable {
		List<PotrlObject.RowsBean> Objects_temp = new ArrayList<PotrlObject.RowsBean>();// 监管对象全局

		public OverItemT(Drawable marker) {
			super(boundCenterBottom(marker));
			for (int i = 0; i < Objects.size(); i++) {
				try {
					double lon = Double.parseDouble(Objects.get(i).getX());// 经度
					double lat = Double.parseDouble(Objects.get(i).getY());// 纬度
					if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
						continue;
					}
					if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
						continue;
					}
					OverlayItem item = new OverlayItem(new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6)), "P" + i,
							"point" + i);
					item.setMarker(marker);
					mGeoList.add(item);
					Objects_temp.add(Objects.get(i));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			populate();

		}

		public void setLastPoint(int i) {
			onTap(i);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		int last = -1;
		int j = 0;

		@Override
		protected boolean onTap(int i) {
			MapTdtFragment.childStationView.setVisibility(View.GONE);
			 if (i == -1) {
				 	MapTdtFragment.xcPopView.setVisibility(View.GONE);
				 	
	                return false;
	            }
			j = i;
			if (Objects_temp != null && Objects_temp.size() > 0) {
				// 监管对象名称
				TextView tv_name = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_name);
				if (Objects_temp.get(j).getName() != null) {
					tv_name.setText(Objects_temp.get(j).getName());
				}
				// 地址
				TextView tv_address = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_address);
				if (Objects_temp.get(j).getAddress() != null) {
					tv_address.setText(Objects_temp.get(j).getAddress());
				}
				// 联系电话
				TextView tv_contact = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_contact);
				if (Objects_temp.get(j).getContact() != null) {
					tv_contact.setText(Objects_temp.get(j).getContact());
				}

				
			}
			TextView upload = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.upload);
			upload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					Intent intent = new Intent(Mcontent, XcrwActivity.class);
					intent.putExtra("code", Objects_temp.get(j).getId());
					Mcontent.startActivity(intent);
				}
			});
			ImageView ivClose = (ImageView) MapTdtFragment.xcPopView.findViewById(R.id.close);
			ivClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MapTdtFragment.xcPopView.setVisibility(View.GONE);
				}
			});

			GeoPoint pt = mGeoList.get(i).getPoint();
			mapView.updateViewLayout(MapTdtFragment.xcPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, pt, -13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			MapTdtFragment.xcPopView.setVisibility(View.VISIBLE);

			if (last != i) {
				last = i;
				MapTdtFragment.xcPopView.setVisibility(View.VISIBLE);
				mapView.getController().animateTo(pt);
			} else {
				last = -1;
				MapTdtFragment.xcPopView.setVisibility(View.GONE);
			}
			return true;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView1) {
			return super.onTap(p, mapView);
		}

	}
	class OverItemT3 extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable {
		List<AirChildStationModel> Objects_temp2 = new ArrayList<AirChildStationModel>();// 监管对象全局
		List<PotrlObject.RowsBean> Objects_temp = new ArrayList<PotrlObject.RowsBean>();// 监管对象全局
		public OverItemT3(Drawable marker,Drawable marker2) {
			super(boundCenterBottom(marker));
			for (int i = 0; i < (listStationModels.size()+Objects.size()); i++) {
				try {
					double lon;
					double lat;
					if(i <Objects.size()) {
						lon = Double.parseDouble(Objects.get(i).getX());// 经度
						lat = Double.parseDouble(Objects.get(i).getY());// 纬度
					}else {
						lon = Double.parseDouble(listStationModels.get(i-Objects.size()).getLongitude());// 经度
						lat = Double.parseDouble(listStationModels.get(i-Objects.size()).getLatitude());// 纬度
					}
					
					if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
						continue;
					}
					if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
						continue;
					}
					
					OverlayItem item = new OverlayItem(new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6)), "P" + i,
							"point" + i);
			
					if(i < Objects.size()) {
						Objects_temp.add(Objects.get(i));
						item.setMarker(marker);
					}else {
						Objects_temp2.add(listStationModels.get(i-Objects.size()));
						item.setMarker(marker2);
					}
					mGeoList.add(item);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			populate();

		}

		public void setLastPoint(int i) {
			onTap(i);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		int j = 0;
		int last = -1;
		@Override
		protected boolean onTap(int i) {
			if (i == -1) {
				MapTdtFragment.childStationView.setVisibility(View.GONE);
				MapTdtFragment.xcPopView.setVisibility(View.GONE);
	            return false;
			}
			
			
			
			
			
			GeoPoint pt = mGeoList.get(i).getPoint();
			if(i < Objects_temp.size()) {
				j = i;
				mapView.updateViewLayout(MapTdtFragment.xcPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, pt, -13, -50, MapView.LayoutParams.BOTTOM_CENTER));
				if (Objects_temp != null && Objects_temp.size() > 0) {
					// 监管对象名称
					TextView tv_name = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_name);
					if (Objects_temp.get(j).getName() != null) {
						tv_name.setText(Objects_temp.get(j).getName());
					}
					// 地址
					TextView tv_address = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_address);
					if (Objects_temp.get(j).getAddress() != null) {
						tv_address.setText(Objects_temp.get(j).getAddress());
					}
					// 联系电话
					TextView tv_contact = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.tv_contact);
					if (Objects_temp.get(j).getContact() != null) {
						tv_contact.setText(Objects_temp.get(j).getContact());
					}

					
				}
				TextView upload = (TextView) MapTdtFragment.xcPopView.findViewById(R.id.upload);
				upload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
						Intent intent = new Intent(Mcontent, XcrwActivity.class);
						intent.putExtra("code", Objects_temp.get(j).getId());
						Mcontent.startActivity(intent);
					}
				});
				ImageView ivClose = (ImageView) MapTdtFragment.xcPopView.findViewById(R.id.close);
				ivClose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						MapTdtFragment.xcPopView.setVisibility(View.GONE);
					}
				});
			}else {
				j = i - Objects_temp.size();
				mapView.updateViewLayout(MapTdtFragment.childStationView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, pt, -13, -50, MapView.LayoutParams.BOTTOM_CENTER));
				if (Objects_temp2 != null && Objects_temp2.size() > 0) {
					// 监管对象名称
					TextView tv_name = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_station_name);
					TextView tv_pm10 = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_pm10);
					TextView tv_fx = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fx);
					TextView tv_fs = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fs);
					TextView tv_sd = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_sd);
					TextView tv_qy = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qy);
					TextView tv_qw = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qw);
					ImageView ivClose = (ImageView) MapTdtFragment.childStationView.findViewById(R.id.close);
					ivClose.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MapTdtFragment.childStationView.setVisibility(View.GONE);
						}
					});
					if (Objects_temp2.get(j).getStationName() != null) {
						tv_name.setText(Objects_temp2.get(j).getStationName()+"("+Objects_temp2.get(j).getTime()+")");
					}
					if (Objects_temp2.get(j).getPm10() != null) {
						tv_pm10.setText(Objects_temp2.get(j).getPm10());
					}
					if (Objects_temp2.get(j).getPressure() != null) {
						tv_qy.setText(Objects_temp2.get(j).getPressure());
					}
					if (Objects_temp2.get(j).getTemp() != null) {
						tv_qw.setText(Objects_temp2.get(j).getTemp());
					}
					if (Objects_temp2.get(j).getHumidity()!= null) {
						tv_sd.setText(Objects_temp2.get(j).getHumidity());
					}
					if (Objects_temp2.get(j).getWdir()!= null) {
						tv_fx.setText(Objects_temp2.get(j).getWdir());
					}
					if (Objects_temp2.get(j).getWs()!= null) {
						tv_fs.setText(Objects_temp2.get(j).getWs());
					}
				}
			}
			
			
			
			
			
			if (last != i) {
				last = i;
				if(i < Objects_temp.size()) {
					MapTdtFragment.xcPopView.setVisibility(View.VISIBLE);
					MapTdtFragment.childStationView.setVisibility(View.GONE);
					mapView.getController().animateTo(pt);
				}else {
					MapTdtFragment.childStationView.setVisibility(View.VISIBLE);
					MapTdtFragment.xcPopView.setVisibility(View.GONE);
					mapView.getController().animateTo(pt);
				}
				
			} else {
				last = -1;
				if(i <Objects_temp.size()) {
					MapTdtFragment.xcPopView.setVisibility(View.GONE);
				}else {
					MapTdtFragment.childStationView.setVisibility(View.GONE);
				}
			}
			return true;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView1) {
			return super.onTap(p, mapView);
		}

	}
	class OverItemT2 extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable {
		List<AirChildStationModel> Objects_temp = new ArrayList<AirChildStationModel>();// 监管对象全局

		public OverItemT2(Drawable marker) {
			super(boundCenterBottom(marker));
			for (int i = 0; i < listStationModels.size(); i++) {
				try {
					double lon = Double.parseDouble(listStationModels.get(i).getLongitude());// 经度
					double lat = Double.parseDouble(listStationModels.get(i).getLatitude());// 纬度
					if (lon < 73.33 || lon > 135.05) {// 经度超出中国范围
						continue;
					}
					if (lat < 3.51 || lat > 53.33) {// 纬度超出中国范围
						continue;
					}
					OverlayItem item = new OverlayItem(new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6)), "P" + i,
							"point" + i);
					item.setMarker(marker);
					mChildStationList.add(item);
					Objects_temp.add(listStationModels.get(i));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			populate();

		}

		public void setLastPoint(int i) {
			onTap(i);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mChildStationList.get(i);
		}

		@Override
		public int size() {
			return mChildStationList.size();
		}

		int j = 0;
		int last = -1;
		@Override
		protected boolean onTap(int i) {
			if (i == -1) {
				MapTdtFragment.childStationView.setVisibility(View.GONE);
	            return false;
			}
			j = i;
			
			
			

			GeoPoint pt = mChildStationList.get(i).getPoint();
			mapView.updateViewLayout(MapTdtFragment.childStationView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, pt, -13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			MapTdtFragment.childStationView.setVisibility(View.VISIBLE);
			if (Objects_temp != null && Objects_temp.size() > 0) {
				// 监管对象名称
				TextView tv_name = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_station_name);
				TextView tv_pm10 = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_pm10);
				TextView tv_fx = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fx);
				TextView tv_fs = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_fs);
				TextView tv_sd = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_sd);
				TextView tv_qy = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qy);
				TextView tv_qw = (TextView) MapTdtFragment.childStationView.findViewById(R.id.tv_qw);
				ImageView ivClose = (ImageView) MapTdtFragment.childStationView.findViewById(R.id.close);
				ivClose.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						MapTdtFragment.childStationView.setVisibility(View.GONE);
					}
				});
				if (Objects_temp.get(j).getStationName() != null) {
					tv_name.setText(Objects_temp.get(j).getStationName()+"("+Objects_temp.get(j).getTime()+")");
				}
				if (Objects_temp.get(j).getPm10() != null) {
					tv_pm10.setText(Objects_temp.get(j).getPm10());
				}
				if (Objects_temp.get(j).getPressure() != null) {
					tv_qy.setText(Objects_temp.get(j).getPressure());
				}
				if (Objects_temp.get(j).getTemp() != null) {
					tv_qw.setText(Objects_temp.get(j).getTemp());
				}
				if (Objects_temp.get(j).getHumidity()!= null) {
					tv_sd.setText(Objects_temp.get(j).getHumidity());
				}
				if (Objects_temp.get(j).getWdir()!= null) {
					tv_fx.setText(Objects_temp.get(j).getWdir());
				}
				if (Objects_temp.get(j).getWs()!= null) {
					tv_fs.setText(Objects_temp.get(j).getWs());
				}
			}
			
			
			if (last != i) {
				last = i;
				MapTdtFragment.childStationView.setVisibility(View.VISIBLE);
				mapView.getController().animateTo(pt);
			} else {
				last = -1;
				MapTdtFragment.childStationView.setVisibility(View.GONE);
			}
			return true;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView1) {
			return super.onTap(p, mapView);
		}

	}
	 public class DbNewsAdapyer extends BaseAdapter {

	        private Context context;
	        private List<AirChildStationModel> list;

	        public DbNewsAdapyer(Context context, List<AirChildStationModel> list) {
	            this.context = context;
	            this.list = list;
	        }

	        @Override
	        public int getCount() {
	            // TODO Auto-generated method stub
	            return list.size();
	        }

	        @Override
	        public Object getItem(int arg0) {
	            // TODO Auto-generated method stub
	            return list.get(arg0);
	        }

	        @Override
	        public long getItemId(int arg0) {
	            // TODO Auto-generated method stub
	            return arg0;
	        }

	        @Override
	        public View getView(int i, View view, ViewGroup arg2) {
	            ViewHolder viewHolder = null;
	            if (view == null) {
	                view = View.inflate(context, R.layout.airchildlist_item, null);
	                ((LinearLayout) view).setGravity(Gravity.CENTER);
	                viewHolder = new ViewHolder();
	                viewHolder.grdrecodename = (TextView) view.findViewById(R.id.tv_id);
	                viewHolder.gridpeople = (TextView) view
	                        .findViewById(R.id.entname);
	                viewHolder.details = (TextView) view
	                        .findViewById(R.id.details);
	                view.setTag(viewHolder);

	            } else {
	                viewHolder = (ViewHolder) view.getTag();
	            }
	            AirChildStationModel jzycBean = list.get(i);
	            viewHolder.gridpeople.setText(jzycBean.getStationName());
	            String level = "--";
	            int bg = R.drawable.shape_recround_green1;
	            try {
					level = getLevel(Double.parseDouble(jzycBean.getPm10())*1000);
					bg = getLevelBg(Double.parseDouble(jzycBean.getPm10())*1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
	            viewHolder.grdrecodename.setText(level);
	            
	            viewHolder.grdrecodename.setBackgroundResource(bg);
	            
	            viewHolder.details.setText("详情");;
	            return view;
	        }

	        class ViewHolder {
	            TextView grdrecodename;
	            TextView gridpeople;
	            TextView details;
	        }
	    }
	 
	 private String getLevel(double nd) {
		  if (nd > 420) {
              return "严重污染";
          } else if (nd > 350) {
              return "重度污染";
          } else if (nd > 250) {
              return "中度污染";
          } else if (nd > 150) {
              return "轻度污染 ";
          } else if (nd > 50) {
              return "良 ";
          } else if (nd >= 0) {
              return "优";
          }else {
        	  return "优";		
          }
	 }
	 private int getLevelBg(double nd) {
		  if (nd > 420) {
             return R.drawable.shape_recround_maroon1;
         } else if (nd > 350) {
             return R.drawable.shape_recround_purple1;
         } else if (nd > 250) {
             return R.drawable.shape_recround_red1;
         } else if (nd > 150) {
             return R.drawable.shape_recround_orange1;
         } else if (nd > 50) {
             return R.drawable.shape_recround_yello1;
         } else if (nd >= 0) {
             return R.drawable.shape_recround_green1;
         }else {
        	 return R.drawable.shape_recround_green1;	
         }
	 }
	 	
}
