package cn.com.mapuni.meshing.activity.db_activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.com.mapuni.meshing.activity.LoginActivity;
import cn.com.mapuni.meshing.adapter.DbNewsAdapyer;
import cn.com.mapuni.meshing.adapter.XiaFaTaskAdapter;
import cn.com.mapuni.meshing.model.DbTaskList;
import cn.com.mapuni.meshing.model.TodoTaskModel;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;
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
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.MapView.LayoutParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("Instantiatable")
public class DbMainActivity implements View.OnClickListener, Callback,
		SwipeRefreshLayout.OnRefreshListener, OnCheckedChangeListener {
	SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayout2;// 刷新布局
	OnRefreshListener listener;
	RadioGroup rg;
	RadioButton rbShangbao;
	DbNewsAdapyer adapter;
	ListView listView, listView2;
	TextView xcwtsjTv;
	TextView wtjlsj;
	private Context Mcontent;
	private FrameLayout Mview;
	private MapView mapView;
	TextView showHind;
	LinearLayout news, xiafa_layout, liebiao;
	MapController mMapController;
	private View mainView;
	private OverItemT mOverlay = null;
	public static View mPopView = null;
	private ImageView ivClose;
	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private List<DbTaskList.RowsBean> taskList = new ArrayList<DbTaskList.RowsBean>();
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	List<TodoTaskModel> data = new ArrayList<TodoTaskModel>();
	List<XiaFaTaskModel> xiafaData = new ArrayList<XiaFaTaskModel>();
	XiaFaTaskAdapter xiaFaTaskAdapter;
	private String url;

	public DbMainActivity(Context mcontent, FrameLayout mview, MapView mapView) {
		super();
		mapView.removeAllOverlay();// 清空所有覆盖物
		mapView.removeView(DbMainActivity.mPopView);//清空弹出视图
		mview.removeAllViews();// 清除界面所有视图
		this.Mcontent = mcontent;
		this.Mview = mview;
		this.mapView = mapView;
	}

	public void showView() {
		initRefreshLayout();
		preInitView();
		getDbTaskList();
	}

	private void initRefreshLayout() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(Mcontent);
		//初始化界面
		mainView = inflater.inflate(R.layout.dbmainactivity_layout, null);
		Mview.addView(mainView);
		showHind = (TextView) mainView.findViewById(R.id.showhind);
		liebiao = (LinearLayout) mainView.findViewById(R.id.liebiao);
		news = (LinearLayout) mainView.findViewById(R.id.news);
		xiafa_layout = (LinearLayout) mainView.findViewById(R.id.xiafa_layout);
		showHind.setOnClickListener(DbMainActivity.this);
		rg = (RadioGroup) mainView.findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(this);
		rbShangbao = (RadioButton) mainView.findViewById(R.id.shangbao);
		listView2 = (ListView) mainView.findViewById(R.id.lv_xiafa);
		swipeRefreshLayout = (SwipeRefreshLayout) mainView
				.findViewById(R.id.refresh_layout);
		// 设置刷新时动画的颜色，可以设置4个
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout2 = (SwipeRefreshLayout) mainView
				.findViewById(R.id.refresh_layout_xiafa);
		// 设置刷新时动画的颜色，可以设置4个
		swipeRefreshLayout2.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		listener = new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mPopView.setVisibility(View.GONE);
				getXiafaList();
			}
		};
		swipeRefreshLayout2.setOnRefreshListener(listener);
		mPopView = LayoutInflater.from(Mcontent)
				.inflate(R.layout.popview, null);
		mPopView.setTag("mPopView");
		rbShangbao.setChecked(true);
	}

	public void preInitView() {
		if (null != DbMainActivity.mPopView) {
			DbMainActivity.mPopView.setVisibility(View.GONE);
			// mapView.removeView(mPopView);
		}

		// 閸︽澘娴樼憴鍡楁禈
		mapView.removeAllOverlay();// 濞撳懐鈹栭幍锟介張澶庮洬閻╂牜澧�
		mapView.setBuiltInZoomControls(false);// 閺勵垰鎯侀弰鍓с仛閸︽澘娴樼紓鈺傛杹閹稿鎸�
		// 瀵版鍩宮MapView閻ㄥ嫭甯堕崚鑸垫綀,閸欘垯浜掗悽銊ョ暊閹貉冨煑閸滃矂鈹嶉崝銊ラ挬缁夎鎷扮紓鈺傛杹
		mMapController = mapView.getController();
		// 閻€劎绮扮�规氨娈戠紒蹇曞惈鎼达附鐎柅鐘辩娑撶嫤eoPoint閿涘苯宕熸担宥嗘Ц瀵邦喖瀹� (鎼达拷 * 1E6)
		GeoPoint point = new GeoPoint((int) (36.687105 * 1E6),
				(int) (117.036054 * 1E6));
		// 鐠佸墽鐤嗛崷鏉挎禈娑擃厼绺鹃悙锟�
		mMapController.setCenter(point);
		// 鐠佸墽鐤嗛崷鏉挎禈zoom缁狙冨焼
		mMapController.setZoom(16);

	}

	/**
	 * http://192.168.15.64:8080/JiNanhuanbaoms/task/selDaochuli.do?userid=123
	 */
	private void getDbTaskList() {
		LoginActivity loginActivity = new LoginActivity();
		final YutuLoading yutuLoading = new YutuLoading(Mcontent);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在获取待办任务数据，请稍候", "");
		yutuLoading.setCancelable(false);
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// 添加提交参数
		String sesssionID = DisplayUitl.readPreferences(Mcontent,
				LAST_USER_SP_NAME, "sessionId");
		if (DisplayUitl.readPreferences(Mcontent, LAST_USER_SP_NAME, "user")
				.length() > 11) {
			url = PathManager.SIJI_XUCHAYUAN_URL + "?sessionId=" + sesssionID;
		} else {
			url = PathManager.GETDBTASK_URL_JINAN + "?sessionId=" + sesssionID;
		}
		utils.send(HttpMethod.GET, url, /* params, */
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						if (null != listView) {
							listView.setAdapter(null);
						}
						Toast.makeText(Mcontent, "数据请求失败", 200).show();
						swipeRefreshLayout.setRefreshing(false);

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}

						String resoult = String.valueOf(arg0.result);
						try {
							Gson gson = new Gson();
							JSONObject jsonObject = new JSONObject(resoult);
							String status = jsonObject.getString("status");
							if (status.contains("200")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("rows");

								List<TodoTaskModel> retList = gson.fromJson(
										jsonArray.toString(),
										new TypeToken<List<TodoTaskModel>>() {
										}.getType());
								data = retList;
								initView();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Mcontent, "数据请求失败", 200).show();
							swipeRefreshLayout.setRefreshing(false);
							e.printStackTrace();
						}

					}
				});
	}

	private void initView() {

		listView = (ListView) mainView.findViewById(R.id.lv);

		adapter = new DbNewsAdapyer(Mcontent, data);
		listView.setAdapter(adapter);

		news.setVisibility(View.VISIBLE);
		listView.setVisibility(View.VISIBLE);
		// ///////////////

		List<Overlay> list = mapView.getOverlays();

		MyLocationOverlay myLocation = new MyLocationOverlay(Mcontent, mapView);

		myLocation.enableMyLocation();

		// list.add(myLocation);

		// mapView.removeView(mPopView);
		// mapView.addView(mPopView, new
		// MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT, null,
		// MapView.LayoutParams.TOP_LEFT));
		// mPopView.setVisibility(View.GONE);
		Drawable marker = Mcontent.getResources().getDrawable(
				R.drawable.poi_xml);
		mOverlay = new OverItemT(marker);

		// 打点后默认打开最新点位弹窗
		// if(data.size()>0){
		// mOverlay.setLastPoint(0);
		// }

		// mOverlay.setOnFocusChangeListener( this);

		list.add(mOverlay);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				mOverlay.setLastPoint(i);
			}
		});
		swipeRefreshLayout.setRefreshing(false);
		// Toast.makeText(Mcontent, "刷新成功", 0).show();
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {
		public OverItemT(Drawable marker) {
			super(boundCenterBottom(marker));
			mGeoList.clear();
			for (int i = 0; i < data.size(); i++) {

				double lat = Double.parseDouble(data.get(i).getX());
				double lon = Double.parseDouble(data.get(i).getY());
				OverlayItem item = new OverlayItem(new GeoPoint(
						(int) (lon * 1E6), (int) (lat * 1E6)), "P" + i, "point"
						+ i);
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

		int last = -1;
		int j = 0;

		@Override
		protected boolean onTap(int i) {
			Log.i("bbb", i + "");
			if (i == -1) {
				mPopView.setVisibility(View.GONE);
				return true;
			}
			j = i;
			if (data != null && data.size() > 0) {
				// 污染类型
				// TextView xcwtTv = (TextView)
				// mPopView.findViewById(R.id.xcwt);
				// if (!TextUtils.isEmpty(taskList.get(j).getXcwt())) {
				// xcwtTv.setText(taskList.get(j).getXcwt());
				// }
				// 任务名称
				TextView tv_taskName = (TextView) mPopView
						.findViewById(R.id.tv_taskName);
				if (!TextUtils.isEmpty(data.get(j).getTaskName())) {
					tv_taskName.setText(data.get(j).getTaskName());
				}
				// 巡查人员
				TextView tv_createUserName = (TextView) mPopView
						.findViewById(R.id.tv_createUserName);
				if (!TextUtils.isEmpty(data.get(j).getCreateUserName())) {
					tv_createUserName.setText(data.get(j).getCreateUserName());
				}
				// 创建时间
				TextView tv_createTime = (TextView) mPopView
						.findViewById(R.id.tv_createTime);
				if (!TextUtils.isEmpty(data.get(j).getCreateTime())) {
					tv_createTime.setText(data.get(j).getCreateTime());
				}
				// 网格名称
				TextView tv_createGridName = (TextView) mPopView
						.findViewById(R.id.tv_createGridName);
				if (!TextUtils.isEmpty(data.get(j).getCreateGridName())) {
					tv_createGridName.setText(data.get(j).getCreateGridName());
				}

				double lat = Double.parseDouble(data.get(j).getX());
				double lon = Double.parseDouble(data.get(j).getY());

				GeoPoint point = new GeoPoint((int) (lon * 1E6),
						(int) (lat * 1E6));
				// 鐠佸墽鐤嗛崷鏉挎禈娑擃厼绺鹃悙锟�
				mMapController.setCenter(point);
				// 鐠佸墽鐤嗛崷鏉挎禈zoom缁狙冨焼
				mMapController.setZoom(16);
			}
			TextView upload = (TextView) mPopView.findViewById(R.id.upload);
			upload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
//					if (data.get(j).getStatus().equals("7")) {
//						Intent intent = new Intent(Mcontent,
//								SaveXcrwActivity.class);
//						mPopView.setVisibility(View.GONE);
//						ArrayList<String> imagemm = new ArrayList<String>();
//						List<ProblemImgsModel> problemImgs = data.get(j)
//								.getProblemImgs();
//						List<ProblemsModel>problems=data.get(j).getProblems();
//						String problemName=problems.get(0).getProblemName();
//						String problemCode=problems.get(0).getProblemCode();
//						if (!problemImgs.isEmpty()) {
//							for (int i = 0; i < problemImgs.size(); i++) {
//								imagemm.add(problemImgs.get(i).getImgPath());
//							}
//						}
//                        intent.putExtra("patrolObjectName", data.get(j).getPatrolObjectName());
//						intent.putExtra("recordId", data.get(j).getId());
//						intent.putExtra("address", data.get(j).getAddress());
//						intent.putExtra("problemDesc", data.get(j)
//								.getProblemDesc());
//						intent.putExtra("isHaveProblem", data.get(j)
//								.getIsHaveProblem());
//						intent.putExtra("problemName", problemName);
//						intent.putExtra("problemCode", problemCode);
//						intent.putStringArrayListExtra("imagemm", imagemm);
//						intent.putExtra("patrolId", data.get(j).getPatrolId());
//						Mcontent.startActivity(intent);
//					} else {
						Intent intent = new Intent(Mcontent,
								DbscfkActivity.class);
						mPopView.setVisibility(View.GONE);
						intent.putExtra("id", data.get(j).getId());// post
						intent.putExtra("currenStatus", data.get(j).getStatus());											// submit
						intent.putExtra("patrolId", data.get(j).getPatrolId());
						intent.putExtra("judgeTura", data.get(j).getJudgeTura());
						Mcontent.startActivity(intent);
//					}
				}
			});
			ivClose = (ImageView) mPopView.findViewById(R.id.close);
			ivClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPopView.setVisibility(View.GONE);
				}
			});

			// /
			// if (i == -1) {
			// mPopView.setVisibility(View.GONE);
			// return false;
			// }

			GeoPoint pt = mGeoList.get(i).getPoint();
			mapView.removeView(mPopView);
			mapView.addView(mPopView, new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, null,
					MapView.LayoutParams.TOP_LEFT));
			mPopView.setVisibility(View.GONE);
			mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
					-13, -50, MapView.LayoutParams.BOTTOM_CENTER));
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
			if (liebiao.getVisibility() == View.VISIBLE) {
				liebiao.setVisibility(View.GONE);
			} else {
				liebiao.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		preInitView();
		getDbTaskList();
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.shangbao:
			news.setVisibility(View.VISIBLE);
			xiafa_layout.setVisibility(View.GONE);
			onRefresh();
			break;
		case R.id.xiafa:
			news.setVisibility(View.GONE);
			xiafa_layout.setVisibility(View.VISIBLE);
			// getXiafaList();
			listener.onRefresh();
			break;

		}

	}

	/**
	 * 获取登陆用户的待办任务数据。接口址址：/work/task/list.do
	 */
	private void getXiafaList() {
		final YutuLoading yutuLoading = new YutuLoading(Mcontent);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在获取下发任务数据，请稍候", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// 添加提交参数
		String sesssionID = DisplayUitl.readPreferences(Mcontent,
				LAST_USER_SP_NAME, "sessionId");
		String url = PathManager.GETXFTASK_URL_JINAN + "?sessionId="
				+ sesssionID;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				if (null != listView2) {
					listView2.setAdapter(null);
				}
				Toast.makeText(Mcontent, "数据请求失败", 200).show();
				swipeRefreshLayout2.setRefreshing(false);

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				try {
					Gson gson = new Gson();
					JSONObject jsonObject = new JSONObject(resoult);
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						JSONArray jsonArray = jsonObject.getJSONArray("rows");

						List<XiaFaTaskModel> retList = gson.fromJson(
								jsonArray.toString(),
								new TypeToken<List<XiaFaTaskModel>>() {
								}.getType());
						xiafaData = retList;
						xiaFaTaskAdapter = new XiaFaTaskAdapter(Mcontent,
								xiafaData);
						listView2.setAdapter(xiaFaTaskAdapter);
						listView2
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int i, long arg3) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(Mcontent,
												XiaFaDetailActivity.class);
										intent.putExtra("xiaFaTaskModel",
												xiafaData.get(i));
										Mcontent.startActivity(intent);
									}
								});

						swipeRefreshLayout2.setRefreshing(false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Mcontent, "数据请求失败", 200).show();
					swipeRefreshLayout2.setRefreshing(false);
					e.printStackTrace();
				}

			}
		});
	}

}
