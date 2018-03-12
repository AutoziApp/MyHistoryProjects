package cn.com.mapuni.meshing.activity.xxcx_activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.com.mapuni.meshingtotal.R;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.overlay.PolylineOverlay;
import com.tianditu.android.maps.overlay.MarkerOverlay.OnMarkerClickListener;
import com.tianditu.android.maps.renderoption.LineOption;

import cn.com.mapuni.meshing.activity.gis.BaseMapActivityTDT;
import cn.com.mapuni.meshing.activity.gis.BaseMapActivityTDT.ViewCallback;
import cn.com.mapuni.meshing.model.DbNews;
import cn.com.mapuni.meshing.util.DialogUtil;
import cn.com.mapuni.meshing.util.timepicker.DateTimeUtil;

public class ZhaTuCheGPSActivity extends BaseMapActivityTDT implements
		ViewCallback, View.OnClickListener, OnMarkerClickListener {
	private FrameLayout mView;
	private MapView mapView;
	private Context mContext;

	TextView showHind;
	LinearLayout news;
	private OverItemT mOverlay = null;
	public View mPopView = null;
	private ImageView ivClose;
	private TextView upload;
	private TextView biaoti1;
	private TextView biaoti2;
	private TextView biaoti3;
	private TextView biaoti4;
	private TextView biaoti5;

	private List<Integer> drawablelist = new ArrayList<Integer>();

	DateTimeUtil util;

	@Override
	public void onCreate(FrameLayout view, MapView mapView) {
		this.mapView = mapView;
		this.mView = view;
		this.mContext = this;
		util = new DateTimeUtil(ZhaTuCheGPSActivity.this);
		initView();
	}

	double dLon = 114.059291;
	double dLat = 36.402616;
	double dSpan = 0.025062;

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		View mainView = inflater.inflate(R.layout.dbmainactivity_layout, null);
		biaoti1 = (TextView) mainView.findViewById(R.id.biaoti1);
		biaoti2 = (TextView) mainView.findViewById(R.id.biaoti2);
		biaoti3 = (TextView) mainView.findViewById(R.id.biaoti3);
		biaoti4 = (TextView) mainView.findViewById(R.id.biaoti4);
		biaoti5 = (TextView) mainView.findViewById(R.id.biaoti5);

		mView.addView(mainView);
		// listview添加模拟数据
		ListView listView = (ListView) mainView.findViewById(R.id.lv);

		// ///////////////
		showHind = (TextView) mainView.findViewById(R.id.showhind);
		news = (LinearLayout) mainView.findViewById(R.id.news);
		showHind.setOnClickListener(this);

		// 地图视图
		mapView.removeAllOverlay();// 清空所有覆盖物
		mapView.setBuiltInZoomControls(true);// 是否显示地图缩放按钮
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		MapController mMapController = mapView.getController();
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint point = new GeoPoint((int) (36.915 * 1E6),
				(int) (117.404 * 1E6));
		// 设置地图中心点
		mMapController.setCenter(point);
		// 设置地图zoom级别
		mMapController.setZoom(12);
		// 添加我的位置和覆盖物集合

		List<Overlay> list = mapView.getOverlays();
		MyLocationOverlay myLocation = new MyLocationOverlay(this, mapView);
		myLocation.enableMyLocation();
		list.add(myLocation);
		drawablelist.add(R.drawable.tree);

		mapView.setBuiltInZoomControls(true);// 是否显示地图缩放按钮
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		MapController mMapController2 = mapView.getController();
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint point2 = new GeoPoint((int) (36.687105 * 1E6),
				(int) (117.036054 * 1E6));
		// 设置地图中心点
		mMapController.setCenter(point2);
		// 设置地图zoom级别
		mMapController2.setZoom(10);

		Drawable marker = this.getResources().getDrawable(R.drawable.car);

		mPopView = LayoutInflater.from(this).inflate(R.layout.popview_car, null);

		biaoti1.setText("车主姓名");
		biaoti2.setText("车牌号");
		biaoti3.setText("车辆类型");
		biaoti4.setText("经度");
		biaoti5.setText("纬度");

		List<DbNews> data = new ArrayList<DbNews>();
		data.add(new DbNews("张德柱", "鲁A-C3878", "大型翻斗车", "116.895", "36.653"));
		data.add(new DbNews("王东东", "鲁A-C3878", "推土机", "117.092", "36.656"));
		data.add(new DbNews("何晨光", "鲁A-C3878", "推土机", "116.967", "36.584"));
		DbNewsAdapyer adapter = new DbNewsAdapyer(this, data);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				// 清空所有覆盖物
				mapView.removeAllOverlay();

				Drawable marker = ZhaTuCheGPSActivity.this.getResources().getDrawable(R.drawable.car);
				mapView.getOverlays().add(new OverItemT(marker));

				GeoPoint point = null;
				MapController mMapController = null;
				if(position == 0) {
					mMapController = mapView.getController();
					point = new GeoPoint((int) (36.713376 * 1E6),
							(int) (116.935927 * 1E6));

				} else if(position == 1) {
					mMapController = mapView.getController();
					point = new GeoPoint((int) (36.9122 * 1E6),
							(int) (116.9142 * 1E6));
				} else if(position == 2) {
					mMapController = mapView.getController();
					point = new GeoPoint((int) (36.917723 * 1E6),
							(int) (116.8942 * 1E6));
				}

				mMapController.setCenter(point);
				mMapController.setZoom(15);
				mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
						-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
				mPopView.setVisibility(View.VISIBLE);
			}
		});

		mOverlay = new OverItemT(marker);

		// mOverlay.setOnFocusChangeListener( this);

		list.add(mOverlay);
		// 创建弹出框view

		// mPopView = LayoutInflater.from(Mcontent)
		// .inflate(R.layout.popview, null);
		ivClose = (ImageView) mPopView.findViewById(R.id.close);
		upload = (TextView) mPopView.findViewById(R.id.upload);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogUtil dialogUtil = new DialogUtil();
				final Dialog dialog = dialogUtil.getDialog(mContext, true);
				Window window = dialog.getWindow();
				window.setContentView(R.layout.dialog_alert);

				TextView expand_title_tv = (TextView) window.findViewById(R.id.dialog_title);
				expand_title_tv.setText("轨迹查询");

				final Button btn_kssj = (Button) window.findViewById(R.id.btn_kssj);
				util.initCalendar(btn_kssj);
				btn_kssj.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						util.showPopwindow(btn_kssj, dialog);
					}
				});

				final Button btn_jssj = (Button) window.findViewById(R.id.btn_jssj);
				util.initCalendar(btn_jssj);
				btn_jssj.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						util.showPopwindow(btn_jssj, dialog);
					}
				});

				TextView text_cancel = (TextView) window.findViewById(R.id.text_cancel);
				TextView text_verify = (TextView) window.findViewById(R.id.text_verify);
				text_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				text_verify.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 清空所有覆盖物
						mapView.removeAllOverlay();
						//在地图上添加路线
						ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
						points.add(new GeoPoint((int) (36.715768 * 1E6), (int) (116.904996 * 1E6)));
						points.add(new GeoPoint((int) (36.705768 * 1E6), (int) (116.904996 * 1E6)));
						points.add(new GeoPoint((int) (36.705768 * 1E6), (int) (116.913157 * 1E6)));
						points.add(new GeoPoint((int) (36.722664 * 1E6), (int) (116.916157 * 1E6)));

						LineOption option = new LineOption();
						option.setStrokeWidth((int) (3.0f * ZhaTuCheGPSActivity.this.getResources().getDisplayMetrics().density));
//						option.setDottedLine(true);
						option.setStrokeColor(0xAAFF0000);

						PolylineOverlay line = new PolylineOverlay();
						line.setOption(option);
						line.setPoints(points);
						mMapView.addOverlay(line, 0);

						MapController mMapController = mapView.getController();
//						GeoBound mapBound = new GeoBound(points);
//						mMapController.setMapBound(mapBound);
						mMapController.setCenter(points.get(0));
						mMapController.setZoom(13);
						mPopView.setVisibility(View.GONE);

						dialog.dismiss();
					}
				});

			}
		});
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopView.setVisibility(View.GONE);
			}
		});
		mapView.addView(mPopView, new MapView.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {
		GeoPoint point = new GeoPoint((int) (36.915 * 1E6),
				(int) (117.404 * 1E6));
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		double[][] points = new double[][] { { 36.713376, 116.935927 },
				{ 36.9122, 116.9142 }, { 36.917723, 116.8942 },
				{ 36.711826, 116.904157 }, { 36.715768, 116.904996 },
				{ 36.729485, 116.914089 }, { 36.918664, 116.894522 },
				{ 36.780263, 116.704454 }, { 36.811315, 116.914282 } };
		int size = points.length;

		public OverItemT(Drawable marker) {
			super(boundCenterBottom(marker));
			for (int i = 0; i < size; i++) {
				OverlayItem item = new OverlayItem(
						new GeoPoint((int) (points[i][0] * 1E6),
								(int) (points[i][1] * 1E6)), "P" + i, "point"
						+ i);
				item.setMarker(marker);
				mGeoList.add(item);
			}
			populate();

		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		protected boolean onTap(int i) {
			if (i == -1) {
				mPopView.setVisibility(View.GONE);
				return false;
			}
			GeoPoint pt = mGeoList.get(i).getPoint();
			mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
					-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			mPopView.setVisibility(View.VISIBLE);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView1) {
			// mPopView.setVisibility(View.GONE);

			return super.onTap(p, mapView);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
			case R.id.showhind:
				if (news.getVisibility() == View.VISIBLE) {
					news.setVisibility(View.GONE);
				} else {
					news.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
		}
	}

	@Override
	public boolean onMarkerClick(MarkerOverlay arg0) {
		return false;
	}

	public class DbNewsAdapyer extends BaseAdapter {

		private Context context;
		private List<DbNews> list;

		public DbNewsAdapyer(Context context, List<DbNews> list) {
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
				view = View.inflate(context, R.layout.db_list_item, null);
				((LinearLayout) view).setGravity(Gravity.CENTER);
				viewHolder = new ViewHolder();
				viewHolder.tvId = (TextView) view.findViewById(R.id.tv_id);
				viewHolder.tvImportance = (TextView) view
						.findViewById(R.id.importance);
				viewHolder.tvEndDate = (TextView) view
						.findViewById(R.id.enddate);
				viewHolder.tvTaskName = (TextView) view
						.findViewById(R.id.taskname);
				viewHolder.tvDetails = (TextView) view
						.findViewById(R.id.details);
				viewHolder.tvDetails.setTextColor(Color.parseColor("#414141"));
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			DbNews dbNews = list.get(i);
			viewHolder.tvId.setText(dbNews.getId());
			viewHolder.tvImportance.setText(dbNews.getImportance());
			viewHolder.tvEndDate.setText(dbNews.getEndDate());
			viewHolder.tvTaskName.setText(dbNews.getTaskName());
			viewHolder.tvDetails.setText(dbNews.getDetails());
			return view;
		}

		class ViewHolder {
			TextView tvId;
			TextView tvImportance;
			TextView tvEndDate;
			TextView tvTaskName;
			TextView tvDetails;
		}
	}

}
