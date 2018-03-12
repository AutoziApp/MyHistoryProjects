package cn.com.mapuni.meshing.activity.xxcx_activity;

import java.util.ArrayList;
import java.util.List;
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
import com.tianditu.android.maps.overlay.MarkerOverlay.OnMarkerClickListener;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.mapuni.meshing.activity.gis.BaseMapActivityTDT;
import cn.com.mapuni.meshing.activity.gis.BaseMapActivityTDT.ViewCallback;
import cn.com.mapuni.meshing.model.DbNews;

public class WuRanYuanActivity extends BaseMapActivityTDT implements ViewCallback,
		View.OnClickListener, OnMarkerClickListener {
	private FrameLayout mView;
	private MapView mapView;
	private Context mContext;

	private TextView showHind;
	private LinearLayout news;
	private OverItemT mOverlay = null;
	public View mPopView = null;
	private ImageView ivClose;
	private TextView biaoti1;
	private TextView biaoti2;
	private TextView biaoti3;
	private TextView biaoti4;
	private TextView biaoti5;

	@Override
	public void onCreate(FrameLayout view, MapView mapView) {
		this.mapView = mapView;
		this.mView = view;
		this.mContext = this;
		initView();
	}

	void initView() {
		mView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(mContext);
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
		MyLocationOverlay myLocation = new MyLocationOverlay(mContext, mapView);
		myLocation.enableMyLocation();
		list.add(myLocation);

		Drawable marker = mContext.getResources().getDrawable(
				R.drawable.lltd_icon);

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

		marker = mContext.getResources().getDrawable(R.drawable.wuranyuan1);
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.popview_wry,
				null);
		biaoti1.setText("企业名称");
		biaoti2.setText("法人代表");
		biaoti3.setText("当前状态");
		biaoti3.setVisibility(View.GONE);
		biaoti4.setText("当前状态");
		biaoti4.setVisibility(View.GONE);
		biaoti5.setText("操作");

		List<DbNews> data = new ArrayList<DbNews>();
		data.add(new DbNews("光大水务（济南）有限公司二厂", "帅道红", "", "", "详情"));
		data.add(new DbNews("山东章丘市大星造纸机械有限公司", "胡清华", "", "", "详情"));
		data.add(new DbNews("济南玉泉生物发电有限公司", "吕帮勇", "", "", "详情"));
		DbNewsAdapyer adapter = new DbNewsAdapyer(mContext, data);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
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

		// mOverlay.setOnFocusChangeListener(this);

		list.add(mOverlay);
		// 创建弹出框view

		ivClose = (ImageView) mPopView.findViewById(R.id.close);
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
				((LinearLayout)view).setGravity(Gravity.CENTER);
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
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			DbNews dbNews = list.get(i);
			viewHolder.tvId.setText(dbNews.getId());
			viewHolder.tvImportance.setText(dbNews.getImportance());
			viewHolder.tvEndDate.setText(dbNews.getEndDate());
			viewHolder.tvEndDate.setVisibility(View.GONE);
			viewHolder.tvTaskName.setText(dbNews.getTaskName());
			viewHolder.tvTaskName.setVisibility(View.GONE);
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
