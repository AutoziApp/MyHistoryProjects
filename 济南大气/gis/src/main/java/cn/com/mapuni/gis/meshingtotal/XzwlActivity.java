package cn.com.mapuni.gis.meshingtotal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.overlay.MarkerOverlay.OnMarkerClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.gis.meshingtotal.model.DbData;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT;


public class XzwlActivity extends BaseMapActivityTDT implements BaseMapActivityTDT.ViewCallback,
		OnClickListener, OnMarkerClickListener, OnItemClickListener {
	private FrameLayout mView;
	private MapView mapView;
	private Context mContext;

	private TextView showHind;
	private LinearLayout news;
	public View mPopView = null;
	private ImageView ivClose;
	private TextView biaoti1;
	private TextView biaoti2;
	private TextView biaoti3;
	private TextView biaoti4;
	private TextView biaoti5;
	private Drawable gsskMarker;
	private List<DbData> datas;
	private ArrayList<Drawable> gsskDrawables;
	private MarkerOverlay[] mMarkers;

	@Override
	public void onCreate(FrameLayout view, MapView mapView) {
		this.mapView = mapView;
		this.mView = view;
		this.mContext = this;
		initView();
	}

	void initView() {

		/**
		 * ������
		 */
		datas = new ArrayList<DbData>();
		datas.add(new DbData(458, "������", 116.98, 36.67, "����", "��"));
		datas.add(new DbData(302, "������", 117.08, 36.67, "����", "��"));
		datas.add(new DbData(456, "������", 117.0, 36.65, "����", "��"));
		datas.add(new DbData(203, "������", 116.93, 36.65, "����", "��"));
		datas.add(new DbData(320, "������", 116.98, 36.68, "����", "��"));
		datas.add(new DbData(151, "������", 117.07, 36.68, "����", "��"));
		datas.add(new DbData(153, "������", 116.73, 36.55, "����", "��"));
		datas.add(new DbData(52, "ƽ����", 116.45, 36.28, "����", "��"));
		datas.add(new DbData(23, "������", 117.22, 36.98, "����", "��"));
		datas.add(new DbData(58, "�̺���", 117.15, 37.32, "����", "��"));

		/**
		 * ��ʼ���ײ���
		 */
		initBottomLayout(datas);

		/**
		 * ��ʼ����ͼ����Ȩ
		 */
		initMapController();
		/**
		 * ����ҵ�λ�ú͸����Ｏ��
		 */
		addMyLocationAndMarders();

		/**
		 * ����������ӵ���ͼ��
		 */
		addMarkerToMapView(datas);

		/**
		 * ����������PopView
		 */
		initPopView();

	}

	/**
	 * ��ʼ���ײ���
	 */
	private void initBottomLayout(List<DbData> datas2) {
		mView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		// ��ѯ����
		View mainView = inflater.inflate(R.layout.dbmainactivity_layout, null);
		ListView listView = (ListView) mainView.findViewById(R.id.lv);
		showHind = (TextView) mainView.findViewById(R.id.showhind);
		news = (LinearLayout) mainView.findViewById(R.id.news);

		biaoti1 = (TextView) mainView.findViewById(R.id.biaoti1);
		biaoti2 = (TextView) mainView.findViewById(R.id.biaoti2);
		biaoti3 = (TextView) mainView.findViewById(R.id.biaoti3);
		biaoti4 = (TextView) mainView.findViewById(R.id.biaoti4);
		biaoti5 = (TextView) mainView.findViewById(R.id.biaoti5);

		mView.addView(mainView);

		// listview���ģ������
		biaoti1.setText("����");
		biaoti2.setText("վ������");
		biaoti3.setText("�����ȼ�");
		biaoti4.setText("����");
		biaoti5.setText("����");
		// biaoti4.setVisibility(View.GONE);
		biaoti5.setVisibility(View.GONE);
		// GsskAdapter gsskAdapter=new GsskAdapter(mContext, datas2);
		GsskAdapter gsskAdapter = new GsskAdapter(mContext, datas2);
		listView.setAdapter(gsskAdapter);

		// �л���ʾ������
		showHind.setOnClickListener(this);
		// ListView��Ŀ����¼�
		listView.setOnItemClickListener(this);
		// ���õײ�������
		news.setVisibility(View.GONE);
	}

	/**
	 * ��ʼ����ͼ����Ȩ
	 */

	private void initMapController() {
		mapView.removeAllOverlay();// ������и�����
		mapView.setBuiltInZoomControls(false);// �Ƿ���ʾ��ͼ���Ű�ť
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		MapController mMapController = mMapView.getController();
		// �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		GeoPoint point = new GeoPoint((int) (36.687105 * 1E6),
				(int) (117.036054 * 1E6));
		// ���õ�ͼ���ĵ�
		mMapController.setCenter(point);
		// ���õ�ͼzoom����
		mMapController.setZoom(8);
	}

	/**
	 * ����ҵ�λ�ú�marker������
	 */
	private void addMyLocationAndMarders() {

		gsskDrawables = new ArrayList<Drawable>();
		// �������ݵĲ�ͬ���첻ͬ��View����
		for (int i = 0; i < datas.size(); i++) {
			DbData dbData = datas.get(i);
			int aqi = dbData.getAqi();
			if (aqi <= 51) {
				// ���ò�ͬ�ı���������
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.you_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.you_right);
				// ����drawable����ӽ�����
				Drawable gsskDrawable1 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable1);
			} else if (aqi <= 101) {
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.liang_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.liang_right);
				Drawable gsskDrawable2 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable2);
			} else if (aqi <= 151) {
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.qing_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.qing_right);
				Drawable gsskDrawable3 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable3);
			} else if (aqi <= 201) {
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.zhong_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.zhong_right);
				Drawable gsskDrawable4 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable4);
			} else if (aqi <= 301) {
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.zong_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.zong_right);
				Drawable gsskDrawable5 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable5);
			} else if (aqi <= 501) {
				View gsskView = View.inflate(this, R.layout.gssk_item_layout,
						null);
				TextView gsskTextLeft = (TextView) gsskView
						.findViewById(R.id.gssk_text_left);
				TextView gsskTextRight = (TextView) gsskView
						.findViewById(R.id.gssk_text_right);

				gsskTextLeft.setText(aqi + "");
				gsskTextLeft.setBackgroundResource(R.drawable.zhong_left);
				gsskTextRight.setText(dbData.getCityName());
				gsskTextRight.setBackgroundResource(R.drawable.zhong_right);
				Drawable gsskDrawable6 = bitmap2Drawable(convertViewToBitmap(gsskView));
				gsskDrawables.add(gsskDrawable6);
			}
		}

	}

	/**
	 * ��marker��������ӵ�mapView��
	 *
	 * @param datas2
	 */
	private void addMarkerToMapView(List<DbData> datas2) {
		float anchor[][] = { { 0.5f, 1.0f }, { 0.5f, (54.0f - 9.0f) / 54.0f },
				{ 0.5f, 0.5f }, { 0.5f, 0.5f }, { 0.5f, 1.0f }, { 0.5f, 1.0f } };
		int size = gsskDrawables.size();

		mMarkers = new MarkerOverlay[size];
		for (int i = 0; i < size; ++i) {
			DbData dbData = datas2.get(i);
			// DrawableOption option = new DrawableOption();
			// option.setAnchor(anchor[i][0], anchor[i][1]);

			mMarkers[i] = new MarkerOverlay();
			// mMarker[i].setOption(option);
			mMarkers[i].setPosition(new GeoPoint(
					(int) (dbData.getLongitude() * 1E6), (int) (dbData
					.getLatitude() * 1E6)));
			mMarkers[i].setIcon(gsskDrawables.get(i));
			mMarkers[i].setTitle(dbData.getCityName());
			mMarkers[i].setClickListener(this);
			// ����������ӵ�MapView��
			mMapView.addOverlay(mMarkers[i]);
		}
	}

	/**
	 * ��ʼ��popView
	 */
	private void initPopView() {
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.popview_xzwl,
				null);
		// �ر�
		ivClose = (ImageView) mPopView.findViewById(R.id.close);

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopView.setVisibility(View.GONE);
			}
		});
		mapView.addView(mPopView, new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null,
				LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
	}

	/**
	 * ������ʾ������
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.showhind) {
			if (news.getVisibility() == View.VISIBLE) {
				news.setVisibility(View.GONE);
			} else {
				news.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * marker����¼�
	 */
	@Override
	public boolean onMarkerClick(MarkerOverlay marker) {

		/**
		 * ��������ﶨλ����ǰλ�ò���������
		 */
		locationAndview(marker);

		return true;
	}

	private void locationAndview(MarkerOverlay marker) {
		mMapView.updateViewLayout(mPopView,
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, marker.getPosition(), -13,
						-50, LayoutParams.BOTTOM_CENTER));
		for (int i = 0; i < datas.size(); i++) {
			DbData dbData = datas.get(i);
			// ͨ��markerЯ���ĳ�����--�Լ��������ݼ����еĳ�����-�Ƿ���ȣ����ж�չʾpopView
			if (marker.getTitle().equals(dbData.getCityName())) {
				TextView cityName = (TextView) mPopView
						.findViewById(R.id.city_name);
				cityName.setText(dbData.getCityName());
			}
		}
		mPopView.setVisibility(View.VISIBLE);
		mMapView.getController().setCenter(marker.getPosition());
	}

	/**
	 * ListView����������
	 *
	 * @author Administrator
	 *
	 */
	class GsskAdapter extends BaseAdapter {
		private Context context;
		private List<DbData> list;

		public GsskAdapter(Context context, List<DbData> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			View view = null;
			if (convertView == null) {
				view = View.inflate(context, R.layout.db_list_item, null);
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
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			DbData dbData = list.get(position);
			viewHolder.tvId.setText(dbData.getCityName());
			viewHolder.tvImportance.setText(dbData
					.getFK_PolluterSuperviseType());
			viewHolder.tvEndDate.setText(dbData.getPollutionRate());
			viewHolder.tvTaskName.setText("");
			viewHolder.tvTaskName.setVisibility(View.GONE);
			viewHolder.tvDetails.setText("����");
			// viewHolder.tvDetails.setVisibility(View.GONE);
			return view;
		}

	}

	class ViewHolder {
		TextView tvId;
		TextView tvImportance;
		TextView tvEndDate;
		TextView tvTaskName;
		TextView tvDetails;
	}

	/**
	 * view-->bitmap
	 *
	 * @param view
	 * @return
	 */

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	/**
	 * Bitmap-->drawable
	 *
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * ListView����Ŀ����¼�
	 *
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// �����Ŀ��λ����ǰλ��&����popView&�رյײ���
		locationAndview(mMarkers[position]);
		news.setVisibility(View.GONE);
	}

}
