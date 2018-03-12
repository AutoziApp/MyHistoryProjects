package cn.com.mapuni.gis.meshingtotal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.gis.meshingtotal.model.JCQYCModel;
import cn.com.mapuni.gis.meshingtotal.tdt.BaseMapActivityTDT;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;


public class JianChengQuYangChenActivity extends BaseMapActivityTDT implements
		BaseMapActivityTDT.ViewCallback, OnClickListener {
	private FrameLayout mView;
	private MapView myMapView;
	private Context mContext;
	MapController mMapController;
	private View mainView;
	TextView showHind;
	LinearLayout news;
	private OverItemT mOverlay = null;
	public View mPopView = null;
	private ImageView ivClose;
	private TextView biaoti1;
	private TextView biaoti2;
	private TextView biaoti3;
	private TextView biaoti4;
	private TextView biaoti5;
	private ListView listView;
	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private double maxNum;
	private Button leftButton, rightButton;
	@Override
	public void onCreate(FrameLayout view, MapView mapView) {
		this.myMapView = mapView;
		this.mView = view;
		this.mContext = this;
		preInitView();
		getData();
	}

	private void preInitView(){
		mView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.dbmainactivity_layout, null);
		mView.addView(mainView);
		leftButton = (Button) mainView.findViewById(R.id.left_button);
		rightButton = (Button) mainView.findViewById(R.id.rightbutton);
		leftButton.setVisibility(View.GONE);
		rightButton.setVisibility(View.GONE);
		biaoti1 = (TextView) mainView.findViewById(R.id.biaoti1);
		biaoti2 = (TextView) mainView.findViewById(R.id.biaoti2);
		biaoti3 = (TextView) mainView.findViewById(R.id.biaoti3);
		biaoti4 = (TextView) mainView.findViewById(R.id.biaoti4);
		biaoti5 = (TextView) mainView.findViewById(R.id.biaoti5);
		biaoti1.setText("���");
		biaoti2.setText("����");
		biaoti3.setText("����");
		biaoti4.setText("");
		biaoti4.setVisibility(View.GONE);
		biaoti5.setText("");
		biaoti5.setVisibility(View.GONE);
		showHind = (TextView) mainView.findViewById(R.id.showhind);
		news = (LinearLayout) mainView.findViewById(R.id.news);
		showHind.setOnClickListener(this);
		// ��ͼ��ͼ
		myMapView.removeAllOverlay();// ������и�����
		myMapView.setBuiltInZoomControls(false);// �Ƿ���ʾ��ͼ���Ű�ť

		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		 mMapController = myMapView.getController();
		// �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		GeoPoint point = new GeoPoint((int) (36.915 * 1E6),
				(int) (117.404 * 1E6));
		// ���õ�ͼ���ĵ�
		mMapController.setCenter(point);
		// ���õ�ͼzoom����
		mMapController.setZoom(12);
		// ����ҵ�λ�ú͸����Ｏ��
		listView = (ListView) mainView.findViewById(R.id.lv);
		mPopView = LayoutInflater.from(this).inflate(R.layout.popview_jcqyc, null);
	}
	private void initView() {
		DbNewsAdapyer adapter = new DbNewsAdapyer(mContext, listData);
		listView.setAdapter(adapter);
		List<Overlay> list = myMapView.getOverlays();
		MyLocationOverlay myLocation = new MyLocationOverlay(mContext, myMapView);

		myLocation.enableMyLocation();

		list.add(myLocation);
		Drawable markers[]=new Drawable[]{this.getResources().getDrawable(R.drawable.shizheng1),
				this.getResources().getDrawable(R.drawable.shizheng2),
				this.getResources().getDrawable(R.drawable.shizheng3),
				this.getResources().getDrawable(R.drawable.shizheng4),
				this.getResources().getDrawable(R.drawable.shizheng5),
				this.getResources().getDrawable(R.drawable.shizheng6),
				this.getResources().getDrawable(R.drawable.shizheng7)};
		Drawable marker = this.getResources().getDrawable(R.drawable.yangchen);
		mOverlay = new OverItemT(marker,markers);
		list.add(mOverlay);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				mOverlay.setLastPoint(position);
			}
		});

	}

	class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {

		public OverItemT(Drawable marker,Drawable markers[]) {
			super(boundCenterBottom(marker));
			for (int i = 0; i < listData.size(); i++) {
				double lat=listData.get(i).getLatitude();
				double lon=listData.get(i).getLongitude();
				OverlayItem item = new OverlayItem(new GeoPoint(
						(int) (lat * 1E6), (int) (lon * 1E6)), "P" + i, "point"
						+ i);
                 if(listData.get(i).getPM10()>listData.get(i).getPM2P5()){
					 maxNum=listData.get(i).getPM10();
				 }else {
					 maxNum=listData.get(i).getPM2P5();
				 }

				if (maxNum > 0.0 && maxNum < 50.0) {
					marker = markers[0];
				} else if (maxNum >= 50.0 && maxNum < 100.0) {
					marker = markers[2];
				} else if (maxNum >= 100.0 && maxNum < 200.0) {
					marker = markers[3];
				} else if (maxNum >= 200.0 && maxNum < 300.0) {
					marker = markers[4];
				} else if (maxNum >= 300.0) {
					marker = markers[5];
				}else {
					marker = markers[6];
				}

				item.setMarker(marker);
				mGeoList.add(item);
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

		int last=-1;
		@Override
		protected boolean onTap(int i) {
			if (i == -1) {
				mPopView.setVisibility(View.GONE);
				return true;
			}
			if(listData!=null&&listData.size()>0){
				TextView tvTitle=(TextView) mPopView.findViewById(R.id.tv_title);
				TextView tvPM25=(TextView) mPopView.findViewById(R.id.tv_pm25);
				TextView tvPM10=(TextView) mPopView.findViewById(R.id.tv_pm10);
				tvTitle.setText(listData.get(i).getEntName());
				tvPM25.setText(listData.get(i).getPM2P5()+"");
				tvPM10.setText(listData.get(i).getPM10()+"");
			}
			ivClose = (ImageView) mPopView.findViewById(R.id.close);
			ivClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPopView.setVisibility(View.GONE);
				}
			});
			GeoPoint pt = mGeoList.get(i).getPoint();
			myMapView.removeView(mPopView);
			myMapView.addView(mPopView, new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, null,
					MapView.LayoutParams.CENTER));
			myMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
					MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, pt,
					-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
			mMapController.setCenter(pt);
			mMapController.animateTo(pt);
			mPopView.setVisibility(View.VISIBLE);

			if (last != i) {
				last = i;
				mPopView.setVisibility(View.VISIBLE);
			} else {
				last = -1;
				mPopView.setVisibility(View.GONE);
			}
			return true;
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.showhind) {
			if (news.getVisibility() == View.VISIBLE) {
				news.setVisibility(View.GONE);
			} else {
				news.setVisibility(View.VISIBLE);
			}
		}
	}


	public class DbNewsAdapyer extends BaseAdapter {

		private Context context;
		private List<JCQYCModel> list;

		public DbNewsAdapyer(Context context, List<JCQYCModel> list) {
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
				view = View.inflate(context, R.layout.jcqyc_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvId = (TextView) view.findViewById(R.id.tv_id);

				viewHolder.tvEntName = (TextView) view
						.findViewById(R.id.entname);
				viewHolder.tvDetails = (TextView) view
						.findViewById(R.id.details);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			JCQYCModel jcqycModel = list.get(i);
			viewHolder.tvId.setText(i+1+"");
			viewHolder.tvEntName.setText(jcqycModel.getEntName());
			viewHolder.tvDetails.setText("����");
			return view;
		}

		class ViewHolder {
			TextView tvId;
			TextView tvEntName;
			TextView tvDetails;
		}
	}

	/**
	 * �������ƶ�ȡ����Դ
	 * @param name
	 */
	private  YutuLoading yutuLoading;
	private List<JCQYCModel> listData=new ArrayList<>();
	private void getData() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ��Ϣ�����Ժ�", "");
		yutuLoading.showDialog();
		//�ӿڵ��÷���һ
		String url = PathManager.JINAN_URL + "/getCRoadData";
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 60);
		utils.configTimeout(60 * 1000);//
		utils.configSoTimeout(60 * 1000);//
		utils.send(HttpRequest.HttpMethod.GET, url,  new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JianChengQuYangChenActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				if (null != listView) {
					listView.setAdapter(null);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = String.valueOf(arg0.result);

				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
					SAXReader reader = new SAXReader();
					Document document = reader.read(inputStream);
					Element root = document.getRootElement();
					JSONArray jsonArray = new JSONArray( root.getText());
					if (jsonArray != null && jsonArray.length() > 0) {
						JCQYCModel jcqycModel;
						for (int i = 0; i < jsonArray.length(); i ++) {
							String PolSorCode=jsonArray.getJSONObject(i).optString("PolSorCode","");
							String EntName=jsonArray.getJSONObject(i).optString("EntName","");
							double Longitude=jsonArray.getJSONObject(i).optDouble("Longitude",0);
							double Latitude=jsonArray.getJSONObject(i).optDouble("Latitude",0);
							double PM2P5=jsonArray.getJSONObject(i).optDouble("PM2P5",0);
							double PM10=jsonArray.getJSONObject(i).optDouble("PM10",0);
							jcqycModel=new JCQYCModel(PolSorCode,EntName,Longitude,Latitude,PM2P5,PM10);
							listData.add(jcqycModel);
						}
					}
					Toast.makeText(JianChengQuYangChenActivity.this, "��������ɹ�", Toast.LENGTH_SHORT).show();
					initView();
				} catch (Exception e) {
					Toast.makeText(mContext, "��������ʧ��", Toast.LENGTH_SHORT).show();
				}

				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
//				updateList();
			}
		});


	}
}
