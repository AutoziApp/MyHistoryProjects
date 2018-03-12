package com.mapuni.android.gis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.InfoTemplate;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.EnforcementModel;
import com.mapuni.android.enforcement.QdjcnlActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.infoQuery.DialogFactory;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * ArcGis地图
 * 
 * @author
 * 
 * 
 *         *
 */
public class MapActivity extends ArcGisActivity {
	/************************** 权限值控制 ************************************/
	private final String QX_QYPLDW = "vmob5A1B";// 企业批量定位
	private final String QX_RYDW = "vmob5A2B";// 人员定位
	private final String QX_CLDW = "vmob5A3B";// 车辆定位
	private final String QX_ZBCXDW = "vmob5A5B";// 周边查询定位
	private final String QX_TCKZ = "vmob5A6B";// 图层控制
	private final String QX_XCZF = "vmob6A";// 现场执法权限
	private final String QX_ZXJC = "vmob3A";// 在线监测权限
	private final String QX_RWXQ = "vmob2A1B";// 任务详情权限
	private final String QX_XJRW = "vmob2A5B";// 新建任务权限
	/*************************************************************************/
	String alet;
	String blet;
	ArrayList<String> strings = new ArrayList<String>();

	// 城市查询
	ArrayList<HashMap<String, Object>> S1 = new ArrayList<HashMap<String, Object>>();
	// 区县查询
	ArrayList<HashMap<String, Object>> S2 = new ArrayList<HashMap<String, Object>>();
	/** 记录当前类的标识 */
	private final String TAG = "MapActivity";
	/** 加载地图中 */
	protected final int LOADMAP = 0;
	/** 定位中 */
	protected final int DINGWEING = 1;
	/** 刷新绘画图层 */
	protected final int REFRESHLAYER = 2;
	/** 当前范围下无数据 */
	protected final int NOCONTENT = 3;
	/** 数据库无数据 */
	protected final int DBNOCONTENT = 5;
	/** 数据查询成功 */
	private final int DATA_QUERY_SUCCESS = 6;
	/** 根据输入的经纬度定位点坐标 */
	private final int COORDINATE_QUERY_SUCCESS = 7;
	/** 手势缩放控制 */
	private final int GIS_ZOOM_CONTROL = 9;
	/** 查询钢铁、水泥、电力和造纸四个行业定位 */
	private final int INDUSTRY_POSITION_SUCCESS = 10;
	private final SQLiteDataProvider sqLitedataprovider = SQLiteDataProvider
			.getInstance();
	/** 初始化SqLite工具 */
	private final SqliteUtil sqliteutil = SqliteUtil.getInstance();
	/** 接收意图 */
	private Intent intent;
	/** 消息对象 */
	private Message msg;
	/** 周边查询 滑动 bar */
	private SeekBar seekBar = null;
	/** 自定义的对话框 */
	private Dialog dialog;
	/** 进度条加载 */
	private ProgressDialog progressDialog;
	/** 企业批量定位 */
	private Button btn_company = null;
	/** 查询同事 */
	private Button btn_teammate = null;
	/** 车辆定位 */
	private Button btn_car = null;
	/** 图层选项 */
	private Button btn_layers = null;
	/** 周边查询 */
	private Button btn_zbcx = null;
	/** 网格化信息查询 */
	private Button btn_grid_query = null;
	/** 经纬度查询 */
	private Button btn_grid_coordinate = null;
	/** 登录用户所管辖的企业 */
	private Button btn_grid_jur_ent = null;
	/** 判断地图是否网格化划分的标识的按钮 */
	private Button btn_bMeshing = null;
	/** 企业详情页面传来的企业的gUid */
	private String qyid;
	/** 企业详情页面传来的企业的名称 */
	private String qymc;
	/** 获得登录的用户名id */
	private String userId;
	/** 执法模板数据集合 */
	private ArrayList<HashMap<String, Object>> zArrayData;
	/** 是否新增现场执法任务记录 */
	private Boolean ISADDTASK = false;
	/** 设置网格数据集合 */
	private ArrayList<HashMap<String, Object>> gridListMainData;
	private ArrayList<HashMap<String, Object>> MainData;
	private HashMap<String, Object> gridMap;
	/** 获得网格id数据集合 */
	private ArrayList<HashMap<String, Object>> gridData;
	/** 获取用户关联网格表中的所有数据 */
	public ArrayList<HashMap<String, Object>> gridCodeData;
	/** 污染源数量 */
	public ArrayList<HashMap<String, Object>> entData;
	/** 主体网格数据集合存放经纬度 */
	private ArrayList<HashMap<String, Object>> mainGrid;
	/** 根据企业名称和所在区县查询企业基本信息集合 */
	private ArrayList<HashMap<String, Object>> company_data;
	/** 周边查询ListView数据 */
	private ArrayList<HashMap<String, Object>> listData;
	/** 获得网格的id */
	private String gridId = null;
	/** 自定义对话框显示网格查询 */
	private AlertDialog alertDialog = null;
	/** 经度值输入框 */
	private EditText gis_coordinates_query_jd_edt;
	/** 纬度值输入框 */
	private EditText gis_coordinates_query_wd_edt;
	/** 企业名称查询输入框 */
	private EditText gis_company_name_edt;
	/** 所在区县查询下拉框 */
	private Spinner gis_company_where_the_county_sp;
	/** Spinner选择的区县 */
	private String where_the_county;
	/** 标志周边查询时点击的是否为查询周边重点污染源，重点污染源为true */
	private boolean bPollution = false;
	/** 是否点击周边查询 */
	private boolean isClickZbcx = false;
	/** 判断地图是否网格化划分的标识 */
	private boolean bMeshing = true;
	/** 测试添加点线面的按钮 */
	private Button test_geometry_btn;
	/** 测试用的列表数据源 */
	private final String[] geometryTypes = new String[] { "Point", "Polyline",
			"Polygon", "取消点线面空间查询" };
	/** 测试用触摸监听绘制点线面 */
	private MyOnTouchListener myOnTouchListener = null;
	/** 测试数据长按地图显示地图坐标经纬度缩放级别范围等信息 */
	private TextView map_tv;
	/** 测试用的设置当前选择的哪一项 */
	int selectedGeometryIndex = -1;

	private RWXX rwxx;

	String mainStr = null;
	/** 获得选中的主网格的数据 */
	String unitStr = null; /* 网格化查询城市的名字 */

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DINGWEING:
				String action = intent.getAction();

				Toast.makeText(MapActivity.this, "正在进行定位，请稍等...", 1).show();
				if (action != null && action.equals("WRYDY")) {
					Drawable image = MapActivity.this.getBaseContext()
							.getResources()
							.getDrawable(R.drawable.hbdt_gis_icon_loc);
					qyid = intent.getStringExtra("qydm");
					double myjd = intent.getDoubleExtra("jd", 0.0);
					double mywd = intent.getDoubleExtra("wd", 0.0);
					String pname = intent.getStringExtra("pname");
					Location(myjd, mywd, pname, null, qyListener, image);
					map.postInvalidate();
				}
				break;
			case REFRESHLAYER:
				if (progressDialog != null)
					progressDialog.cancel();
				/** 刷新绘画图层 */
				map.postInvalidate();
				/** 初始化地图范围 */
				loadFullMap();
				break;
			case NOCONTENT:
				if (progressDialog != null)
					progressDialog.cancel();
				Toast.makeText(MapActivity.this, "当前范围下无符合要求的数据",
						Toast.LENGTH_LONG).show();
				break;
			case DBNOCONTENT:
				if (progressDialog != null)
					progressDialog.cancel();
				Toast.makeText(MapActivity.this, "数据库无符合要求数据",
						Toast.LENGTH_LONG).show();
				break;
			case DATA_QUERY_SUCCESS:
				if (progressDialog != null)
					progressDialog.cancel();
				/** 查看当前地图中的网格的管理员基本信息 */
				GridInfor();
				break;

			case COORDINATE_QUERY_SUCCESS:
				if (progressDialog != null)
					progressDialog.cancel();
				/** 刷新绘画图层 */
				map.postInvalidate();
				Point point = (Point) msg.obj;
				/** 地图放大到指定比例尺 */
				map.zoomToScale(point, scale);
				break;

			case GIS_ZOOM_CONTROL:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				/** 刷新绘画图层 */
				map.postInvalidate();
				break;

			case INDUSTRY_POSITION_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				/** 刷新绘画图层 */
				map.postInvalidate();
				Point pt = (Point) msg.obj;
				/** 地图放大到指定比例尺 */
				map.zoomToScale(pt, scale);
				break;
			}
		}
	};

	/** 地图定位中坐标点的监听事件 */
	View.OnClickListener qyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			initialdialog(qymc, qyid);
		}
	};

	/**
	 * 点击定位后的点坐标上弹出的标注信息,弹出的企业dialog对话框
	 * 
	 * @param qymc
	 *            企业名称
	 * @param qyid
	 *            企业的guiD
	 */
	private void initialdialog(final String qymc, final String qyid) {

		LayoutInflater li = LayoutInflater.from(MapActivity.this);
		View view = li.inflate(R.layout.map_tablerow, null);
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		/** 一厂一档 */
		TableRow taskwrycxxg_tb = (TableRow) view
				.findViewById(R.id.tbr_wrycxxg);
		/** 查看详情 */
		TableRow taskupload_tb = (TableRow) view.findViewById(R.id.tbr_wryxq);
		/** 现场执法 */
		TableRow taskxczf_tb = (TableRow) view.findViewById(R.id.tbr_xczf);
		/** 新建任务 */
		/* TableRow taskxjrw_tb = (TableRow) view.findViewById(R.id.tbr_xjrw); */
		/** 权限判断 */
		if (!DisplayUitl.getAuthority(QX_XCZF)) {
			taskxczf_tb.setVisibility(View.GONE);
		}
		// if (!DisplayUitl.getAuthority(QX_ZXJC)) {
		// taskassign_tb.setVisibility(View.GONE);
		// }
		if (!DisplayUitl.getAuthority(QX_RWXQ)) {
			taskupload_tb.setVisibility(View.GONE);
		}
		/*
		 * if (!DisplayUitl.getAuthority(QX_XJRW)) {
		 * taskxjrw_tb.setVisibility(View.GONE); }
		 */
		dialog.setContentView(view);
		dialog.show();

		/** 一企一档监听事件 */
		taskwrycxxg_tb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName("com.mapuni.android.MobileEnforcement",
						"com.mapuni.yqydweb.YqydWebActivity");
				intent.putExtra("qyid", qyid);
				intent.putExtra("noedit", "noedit");
				MapActivity.this.startActivity(intent);
//				Intent intent = new Intent();
//				intent.setClassName("com.mapuni.android.MobileEnforcement",
//						"com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide");
//				intent.putExtra("qyid", qyid);
//				intent.putExtra("noedit", "noedit");
//				MapActivity.this.startActivity(intent);
			}
		});

		/** 企业基本信息 ,查看详情 */
		taskupload_tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle nextBundle = new Bundle();
				QYJBXX qyjbxx;
				try {
					qyjbxx = (QYJBXX) BaseObjectFactory
							.createObject(QYJBXX.BusinessClassName);
					qyjbxx.setCurrentID(qyid);
					nextBundle.putSerializable("BusinessObj", qyjbxx);
					nextBundle.putString("DetailedBottomMenuStyleFileName", "");
					dialog.cancel();
					Intent intent = new Intent(MapActivity.this,
							EnterpriseArchivesActivitySlide.class);
					intent.putExtras(nextBundle);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				} catch (InstantiationException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				}
			}
		});

		// /** 在线监测 */
		// taskassign_tb.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Bundle nextBundle = new Bundle();
		// QYJBXX qyjbxx;
		// try {
		// qyjbxx = (QYJBXX) BaseObjectFactory
		// .createObject(QYJBXX.BusinessClassName);
		// qyjbxx.setCurrentID(qyid);
		// nextBundle.putSerializable("BusinessObj", qyjbxx);
		//
		// Intent intent = new Intent(MapActivity.this,
		// ZXJKNRActivity.class);
		// intent.putExtras(nextBundle);
		// startActivity(intent);
		// dialog.cancel();
		// } catch (ClassNotFoundException e) {
		// ExceptionManager.WriteCaughtEXP(e, "MapActivity");
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// ExceptionManager.WriteCaughtEXP(e, "MapActivity");
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// ExceptionManager.WriteCaughtEXP(e, "MapActivity");
		// e.printStackTrace();
		// }
		// }
		// });

		/** 现场执法 */
		taskxczf_tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				EnforcementModel enforcementModel = new EnforcementModel();
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				/** 执法人员ID数据集 */
				StringBuffer sbLawManUsersId = new StringBuffer(Global
						.getGlobalInstance().getUserid());
				ArrayList<HashMap<String, Object>> result = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(
								"select distinct guid from T_WRY_QYJBXX where qymc = '"
										+ qymc + "'");
				String qyguid = result.get(0).get("guid").toString();
				String RWBH = enforcementModel.createOneEnforcementTask(qyguid,
						qymc, sbLawManUsersId.toString());
				if (!RWBH.equals("")) {
					ISADDTASK = true;// 添加一条任务返回刷新执法记录
					Intent intent = new Intent(MapActivity.this,
							QdjcnlActivity.class);

					intent.putExtra("qyid", qyguid);
					intent.putExtra("rwbh", RWBH);
					RWXX rwxx = new RWXX();
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("rwbh", RWBH);
					String guid = SqliteUtil.getInstance()
							.getList("guid", conditions, "T_YDZF_RWXX").get(0)
							.get("guid").toString();
					rwxx.setCurrentID(guid);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(bundle);
					LogUtil.i(TAG, "企业代码为---" + qyguid + ",任务编号为--->" + RWBH);
					Global.getGlobalInstance().setMapXczfAddHistory(true);
					startActivity(intent);
				}

			}
		});

	}

	/**
	 * 现场执法按钮监听事件
	 * 
	 * @param tid
	 *            模板id
	 */
	public void xczfBtnListener(String tid) {

		try {
			RWXX rwxxmanager = new RWXX();
			String guid = UUID.randomUUID().toString();
			int cont = 0;

			String ssks = Global.getGlobalInstance().getDepId();
			String useridStr = Global.getGlobalInstance().getUserid();
			String bjqx = "";
			String rwbh = rwxxmanager
					.insertRWXX("", bjqx, false, guid, cont, "", qyid, qymc,
							ssks, "", useridStr, tid, "xczf", 0, "", "");
			if (rwbh != null && !"".equals(rwbh)) {
				/** 根据企业代码获得当前任务的任务GuId */
				ArrayList<HashMap<String, Object>> listTask = sqliteutil
						.queryBySqlReturnArrayListHashMap("select guid from T_YDZF_RWXX where RWBH = '"
								+ rwbh + "'");
				String rwid = (String) listTask.get(0).get("guid");
				Intent intent = new Intent(MapActivity.this,
						QdjcnlActivity.class);
				Bundle bundle = new Bundle();
				RWXX rwxx = new RWXX();
				rwxx.setCurrentID(rwid);
				bundle.putSerializable("BusinessObj", rwxx);
				ArrayList<HashMap<String, Object>> result = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(
								"select distinct guid from T_WRY_QYJBXX where qymc = '"
										+ qymc + "'");
				intent.putExtra("qyid", result.get(0).get("guid").toString());
				intent.putExtra("rwbh", rwbh);
				intent.putExtra("qy_rwzt", "0");
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(MapActivity.this, "生成任务失败!", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 获取登录用户所管辖的网格关系 */
	@SuppressWarnings("static-access")
	public void GridRelations() {
		/** 获取登录的用户名id */
		userId = Global.getGlobalInstance().getUserid();
		/** 实例化 */
		gridCodeData = new ArrayList<HashMap<String, Object>>();
		/** 获取用户关联网格表中的所有数据 */
		gridCodeData = sqliteutil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select gridcode from GridUserMapping where userid = '"
								+ userId + "'");
		if (gridCodeData != null && gridCodeData.size() > 0) {
			/** 调用查询钢铁、水泥、电力和造纸四个行业的方法 */
			queryEnt(gridCodeData);
		}
	}

	/** 查询钢铁、水泥、电力和造纸四个行业 */
	public void queryEnt(final ArrayList<HashMap<String, Object>> gridCodeData) {

		/* 关闭网格 */
		bMeshing = false;

		/** 获得查询条件判断移除不符合的数据 例如 市为县区内容的数据 */
		tLayer.removeAll();
		if (callout != null) {
			/** 清除标注 */
			callout.animatedHide();
		}
		progressDialog = new ProgressDialog(MapActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在查找，请稍等...");
		progressDialog.show();

		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			@Override
			public void run() {

				/** 获得用户所管辖区域的网格id */
				String gridCodeStr = gridCodeData.get(0).get("gridcode")
						.toString();
				/** 实例化 */
				entData = new ArrayList<HashMap<String, Object>>();
				/** 获取企业关联网格表中的所有数据 */
				entData = sqliteutil
						.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select * from GridEntMapping INNER JOIN T_WRY_QYJBXX on GridEntMapping.entcode =  T_WRY_QYJBXX.guid where gridcode = '"
										+ gridCodeStr
										+ "' and JD is not null and WD is not null and JD is not '' and WD is not ''");

				if (entData != null && entData.size() > 0) {

					Point ptMap = null;
					try {
						/** 经度 */
						Double qy_jd = null;
						/** 纬度 */
						Double qy_wd = null;

						/** 启动污染单击响应模式 */
						bPollution = true;
						/** 钢铁图标 */
						Drawable steelImage = MapActivity.this.getBaseContext()
								.getResources()
								.getDrawable(R.drawable.hbdt_gis_icon_steel);
						/** 水泥图标 */
						Drawable cementImage = MapActivity.this
								.getBaseContext().getResources()
								.getDrawable(R.drawable.hbdt_gis_icon_cement);
						/** 电力图标 */
						Drawable electricityImage = MapActivity.this
								.getBaseContext()
								.getResources()
								.getDrawable(
										R.drawable.hbdt_gis_icon_electricity);
						/** 造纸图标 */
						Drawable PapermakingImage = MapActivity.this
								.getBaseContext()
								.getResources()
								.getDrawable(
										R.drawable.hbdt_gis_icon_papermaking);

						for (HashMap<String, Object> qymap : entData) {
							try {
								qy_jd = Double.parseDouble(qymap.get("jd")
										.toString());
								qy_wd = Double.parseDouble(qymap.get("wd")
										.toString());
							} catch (Exception e) {
								continue;
							}
							ptMap = (Point) GeometryEngine.project(new Point(
									qy_jd, qy_wd), SpatialReference
									.create(4326), map.getSpatialReference());
							PictureMarkerSymbol imageMarkerSymbol = null;
							/** 根据行业类别区分图标 */
							String hylb = qymap.get("hylb").toString();
							if (hylb.equalsIgnoreCase("I671")) {
								imageMarkerSymbol = new PictureMarkerSymbol(
										steelImage);
							} else if (hylb.equalsIgnoreCase("D44")) {
								imageMarkerSymbol = new PictureMarkerSymbol(
										cementImage);
							} else if (hylb.equalsIgnoreCase("9901")) {
								imageMarkerSymbol = new PictureMarkerSymbol(
										PapermakingImage);
							} else if (hylb.equalsIgnoreCase("D4411")) {
								imageMarkerSymbol = new PictureMarkerSymbol(
										electricityImage);
							} else {
								imageMarkerSymbol = new PictureMarkerSymbol(
										cementImage);
							}
							Map<String, Object> cmap = new HashMap<String, Object>();
							cmap.put("tagid", qymap.get("guid").toString());
							cmap.put("tag", qymap.get("qymc").toString());
							cmap.put("jd", qy_jd);
							cmap.put("wd", qy_wd);
							cmap.put("zdwry", hylb);

							Graphic graphic = new Graphic(ptMap,
									imageMarkerSymbol, cmap, new InfoTemplate(
											"", ""));

							tLayer.addGraphic(graphic);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					msg = handler.obtainMessage();
					msg.what = REFRESHLAYER;
					msg.obj = ptMap;
					handler.sendMessage(msg);
				}
				// else
				// {
				// msg = handler.obtainMessage();
				// msg.what = DBNOCONTENT;
				// handler.sendMessage(msg);
				// return;
				// }
			}
		}).start();
	}

	/** 显示主体网格坐标信息 */
	public void showMainGrid() {
		/** 主体网格数据集合存放经纬度 */
		mainGrid = new ArrayList<HashMap<String, Object>>();
		/** 获得主体网格的经纬度坐标 */
		mainGrid = gridMainData2();
		/** 点坐标 */
		Point point = null;
		/** 经度 */
		Double wg_jd = null;
		/** 纬度 */
		Double wg_wd = null;
		/** 启动污染单击响应模式 */
		bPollution = true;
		/** 设置主体网格的坐标 */
		Drawable gridImage = MapActivity.this.getBaseContext().getResources()
				.getDrawable(R.drawable.hbdt_gis_one_grid);
		for (HashMap<String, Object> wgmap : mainGrid) {
			wg_jd = Double.parseDouble(wgmap.get("jd").toString());
			wg_wd = Double.parseDouble(wgmap.get("wd").toString());

			point = (Point) GeometryEngine.project(new Point(wg_jd, wg_wd),
					SpatialReference.create(4326), map.getSpatialReference());
			PictureMarkerSymbol imageMarkerSymbol = null;
			imageMarkerSymbol = new PictureMarkerSymbol(gridImage);

			HashMap<String, Object> gridHashMap = new HashMap<String, Object>();
			gridHashMap.put("jd", wg_jd);
			gridHashMap.put("wd", wg_wd);
			gridHashMap.put("cStr", wgmap.get("contextStr").toString());
			gridHashMap.put("agency", wgmap.get("agency").toString());
			gridHashMap.put("layer", wgmap.get("layer").toString());

			Graphic graphic = new Graphic(point, imageMarkerSymbol,
					gridHashMap, new InfoTemplate("", ""));
			tLayer.addGraphic(graphic);

			msg = handler.obtainMessage();
			msg.what = GIS_ZOOM_CONTROL;
			handler.sendMessage(msg);
		}
	}

	/** 根据当前比例尺控制当前网格信息 */
	public void CurGridInfor() {
		/** 主体网格数据集合存放经纬度 */
		mainGrid = new ArrayList<HashMap<String, Object>>();
		double scale = map.getScale();
		
		if (scale >= 4241921.422332997) {
			/** 获得查询条件判断移除不符合的数据 例如 市为县区内容的数据 */
			tLayer.removeAll();
			if (callout != null) {
				/** 清除标注 */
				callout.animatedHide();
			}
			/** 获得主体网格的经纬度坐标 */
			mainGrid = gridMainData2();
			/** 点坐标 */
			Point point = null;
			/** 经度 */
			Double wg_jd = null;
			/** 纬度 */
			Double wg_wd = null;
			/** 启动污染单击响应模式 */
			bPollution = true;
			/** 设置主体网格的坐标 */
			Drawable gridImage = MapActivity.this.getBaseContext()
					.getResources().getDrawable(R.drawable.hbdt_gis_one_grid);
			for (HashMap<String, Object> wgmap : mainGrid) {
				wg_jd = Double.parseDouble(wgmap.get("jd").toString());
				wg_wd = Double.parseDouble(wgmap.get("wd").toString());

				point = (Point) GeometryEngine.project(new Point(wg_jd, wg_wd),
						SpatialReference.create(4326),
						map.getSpatialReference());
				PictureMarkerSymbol imageMarkerSymbol = null;
				imageMarkerSymbol = new PictureMarkerSymbol(gridImage);

				HashMap<String, Object> gridHashMap = new HashMap<String, Object>();
				gridHashMap.put("jd", wg_jd);
				gridHashMap.put("wd", wg_wd);
				gridHashMap.put("cStr", wgmap.get("contextStr").toString());
				gridHashMap.put("agency", wgmap.get("agency").toString());
				gridHashMap.put("layer", wgmap.get("layer").toString());

				Graphic graphic = new Graphic(point, imageMarkerSymbol,
						gridHashMap, new InfoTemplate("", ""));
				tLayer.addGraphic(graphic);

				msg = handler.obtainMessage();
				msg.what = GIS_ZOOM_CONTROL;
				handler.sendMessage(msg);
							
				
	
			}
		} else if ( scale <= 2068178.8707794072) {
			/** 获得查询条件判断移除不符合的数据 例如 市为县区内容的数据 */
			tLayer.removeAll();
			if (callout != null) {
				/** 清除标注 */
				callout.animatedHide();
			}
			/**  修改gridElementData()          获得单元网格的经纬度坐标 */
			mainGrid = gridMainData2();
		//	mainGrid = gridElementData();
			/** 点坐标 */
			Point point = null;
			/** 经度 */
			Double wg_jd = null;
			/** 纬度 */
			Double wg_wd = null;
			/** 启动污染单击响应模式 */
			bPollution = true;
			/** 设置主体网格的坐标 */
			Drawable gridImage = MapActivity.this.getBaseContext()
					.getResources().getDrawable(R.drawable.hbdt_gis_two_gird);
			for (HashMap<String, Object> wgmap : mainGrid) {
				wg_jd = Double.parseDouble(wgmap.get("jd").toString());
				wg_wd = Double.parseDouble(wgmap.get("wd").toString());

				point = (Point) GeometryEngine.project(new Point(wg_jd, wg_wd),
						SpatialReference.create(4326),
						map.getSpatialReference());
				PictureMarkerSymbol imageMarkerSymbol = null;
				imageMarkerSymbol = new PictureMarkerSymbol(gridImage);

				HashMap<String, Object> gridHashMap = new HashMap<String, Object>();
				gridHashMap.put("jd", wg_jd);
				gridHashMap.put("wd", wg_wd);
				gridHashMap.put("cStr", wgmap.get("contextStr").toString());
				gridHashMap.put("agency", wgmap.get("agency").toString());
				gridHashMap.put("layer", wgmap.get("layer").toString());

				Graphic graphic = new Graphic(point, imageMarkerSymbol,
						gridHashMap, new InfoTemplate("", ""));
				tLayer.addGraphic(graphic);

				msg = handler.obtainMessage();
				msg.what = GIS_ZOOM_CONTROL;
				handler.sendMessage(msg);

			}
		}
	}

	/** 网格化信息查询方法 */
	private void grid_queryinfo() {

		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View view = inflater.inflate(R.layout.gis_query_pollution_dialog, null);
		final Spinner select_city_sp = (Spinner) view
				.findViewById(R.id.select_city_sp);
		final Spinner select_county_sp = (Spinner) view
				.findViewById(R.id.select_county_sp);
		Button sure_btn = (Button) view.findViewById(R.id.sure_btn);
		Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
		String sql = "select GridName from Grid where  Layer ='2'";

		if (S1.size() == 0) {
			S1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			for (int i = 0; i < S1.size(); i++) {
				strings.add(S1.get(i).get("gridname").toString());
			}
		}
		ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
				MapActivity.this, android.R.layout.simple_spinner_item, strings);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_city_sp.setAdapter(cityAdapter);

		select_city_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String item = select_city_sp.getSelectedItem().toString();
				String sql2 = "select GridCode from Grid where GridName ='"
						+ item + "'";
				String S3 = SqliteUtil.getInstance().getDepidByDepName(sql2);

				blet = S3.substring(0, S3.length() - 2);

				S2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select GridName from Grid where Layer='3'and gridCode like'%"
								+ blet + "%'");
				ArrayList<String> stringb = new ArrayList<String>();
				for (int i = 0; i < S2.size(); i++) {
					stringb.add(S2.get(i).get("gridname").toString());
				}
				ArrayAdapter<String> CountAdapter = new ArrayAdapter<String>(
						MapActivity.this, android.R.layout.simple_spinner_item,
						stringb);
				CountAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				select_county_sp.setAdapter(CountAdapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		if (gridData != null && gridData.size() > 0) {
			gridData.clear();
		}
		/** 初始化网格化数据 */
		gridData = new ArrayList<HashMap<String, Object>>();

		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setView(view);

		/** 确定按钮 */
		sure_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				progressDialog = new ProgressDialog(MapActivity.this);
				progressDialog.setCancelable(false);
				progressDialog
						.setMessage(getString(R.string.mapactivity_map_loading));
				progressDialog.show();

				new Thread(new Runnable() {

					@SuppressWarnings("static-access")
					@Override
					public void run() {
						/** 获得选中的主网格的数据 */
						mainStr = select_city_sp.getSelectedItem().toString()
								.trim();
						/** 获得选中的单元网格数据 */
						unitStr = select_county_sp.getSelectedItem().toString()
								.trim();
						if (!unitStr.equalsIgnoreCase("-请选择单元网格-")) {
							gridData = sqliteutil.getInstance()
									.queryBySqlReturnArrayListHashMap(
											"select gridcode from Grid where gridname = '"
													+ unitStr + "'");
							if (gridData == null || gridData.size() == 0) {
								msg = handler.obtainMessage();
								msg.what = DBNOCONTENT;
								handler.sendMessage(msg);
								return;
							}
						} else {
							gridData = sqliteutil.getInstance()
									.queryBySqlReturnArrayListHashMap(
											"select gridcode from Grid where gridname = '"
													+ mainStr + "'");
							if (gridData == null || gridData.size() == 0) {
								msg = handler.obtainMessage();
								msg.what = DBNOCONTENT;
								handler.sendMessage(msg);
								return;
							}
						}
						/** 获得网格的id */
						gridId = gridData.get(0).get("gridcode").toString();
						msg = handler.obtainMessage();
						msg.what = DATA_QUERY_SUCCESS;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
		/** 取消按钮 */
		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	/** 查看当前地图中的网格的管理员基本信息 */
	public void GridInfor() {
		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View view = inflater.inflate(R.layout.gis_guid_admin_info_dialog, null);

		/** 网格化信息标题 */
		// TextView gis_grid_infor_tv = (TextView) view
		// .findViewById(R.id.gis_grid_infor_tv);
		TextView gis_grid_infor_tv = (TextView) view
				.findViewById(R.id.gis_grid_infor_ad);

		EditText overall_responsibility_edt = (EditText) view
				.findViewById(R.id.overall_responsibility_edt);
		EditText overall_responsibility_phone_edt = (EditText) view
				.findViewById(R.id.overall_responsibility_phone_edt);
		EditText responsible_agencies_edt = (EditText) view
				.findViewById(R.id.responsible_agencies_edt);
		EditText person_in_charge_edt = (EditText) view
				.findViewById(R.id.person_in_charge_edt);
		EditText person_in_charge_photo_edt = (EditText) view
				.findViewById(R.id.person_in_charge_photo_edt);
		EditText pollution_sources_edt = (EditText) view
				.findViewById(R.id.pollution_sources_edt);
		// EditText superscalar_pollution_sources_edt = (EditText) view
		// .findViewById(R.id.superscalar_pollution_sources_edt);
		// EditText the_number_of_tasks_edt = (EditText) view
		// .findViewById(R.id.the_number_of_tasks_edt);
		/** 主要职责 */
		EditText main_responsibility_edt = (EditText) view
				.findViewById(R.id.main_responsibility_edt);
		/** 网格编号 */
		EditText grid_code_edt = (EditText) view
				.findViewById(R.id.grid_code_edt);
		/** 网格内监管内容 */
		EditText supervise_content_edt = (EditText) view
				.findViewById(R.id.supervise_content_edt);
		/** 主要责任人 */
		EditText primary_responsibility_edt = (EditText) view
				.findViewById(R.id.primary_responsibility_edt);
		/** 主要责任人联系方式 */
		EditText primary_responsibility_phone_edt = (EditText) view
				.findViewById(R.id.primary_responsibility_phone_edt);
		/** 直接责任人左边的文字 */
		TextView direct_reponsibility_text = (TextView) view
				.findViewById(R.id.gis_guid_direct_responsibility_text);
		/** 直接责任人 */
		EditText direct_responsibility_edt = (EditText) view
				.findViewById(R.id.direct_responsibility_edt);
		/** 直接负责人联系电话的布局 */
		LinearLayout gis_guid_direct_responsibility_telephone_layout = (LinearLayout) view
				.findViewById(R.id.gis_guid_direct_responsibility_telephone_layout);
		/** 直接责任人联系方式 */
		EditText direct_responsibility_phone_edt = (EditText) view
				.findViewById(R.id.direct_responsibility_phone_edt);

		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		builder.setView(view);

		/** 总负责人姓名 */
		String mainNameStr = "";
		/** 总负责人电话 */
		String mainteleStr = "";
		/** 获得总负责人信息 */
		ArrayList<HashMap<String, Object>> mainData = sqliteutil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
								+ gridId + "' and blamerole = '1'");
		if (mainData != null && mainData.size() > 0) {
			mainNameStr = mainData.get(0).get("u_realname").toString();
			mainteleStr = mainData.get(0).get("u_photo").toString();
		}

		/** 初始化设置网格数据集合 */
		MainData = new ArrayList<HashMap<String, Object>>();
		MainData = gridMainData2();
		gridMap = new HashMap<String, Object>();
		/**修改  gridElementData  */
		company_data = gridMainData2();

		/** 获得当前网格信息 */
		String contextStr = "";
		/** 当前网格所处级别 */
		String layerStr = "";

		int len = company_data.size();
		int gridList = MainData.size();
		if (unitStr.equalsIgnoreCase("-请选择单元网格-")) {
			for (int i = 0; i < gridList; i++) {
				HashMap<String, Object> wgmap = gridMainData2().get(i);
				if (mainStr.equals(wgmap.get("contextStr"))) {
					/** 获得当前网格信息 */
					contextStr = wgmap.get("contextStr").toString();
					/** 当前网格所处级别 */
					layerStr = wgmap.get("layer").toString();
				}
			}
		} else {
			for (int i = 0; i < len; i++) {
				HashMap<String, Object> wgmap = company_data.get(i);
				if (unitStr.equals(wgmap.get("contextStr"))) {
					/** 获得当前网格信息 */
					contextStr = wgmap.get("contextStr").toString();
					/** 当前网格所处级别 */
					layerStr = wgmap.get("layer").toString();
				}
			}
		}

		/** 责任机构 */
		String agencyStr = "";
		if (gridId != null && !gridId.equals("")) {
			String sql = "select ResponDepart  from Grid where GridCode = '"
					+ gridId + "'";
			agencyStr = sqliteutil.getInstance().getDepidByUserid(sql);
		}

		if (gridId != null && !gridId.equals("")) {
			/** 获取主要负责人的名称 */
			String resDataStr = "";
			/** 获取主要负责人的联系方式 */
			String resDataPhoStr = "";
			/** 获取用户关联网格表中数据集合 */
			ArrayList<HashMap<String, Object>> resData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '2'");
			if (resData != null && resData.size() > 0) {
				resDataStr = resData.get(0).get("u_realname").toString();
				resDataPhoStr = resData.get(0).get("u_photo").toString();
			}

			/** 获取主要责任人的名称 */
			String resPonDataStr = "";
			/** 获取主要责任人的联系方式 */
			String resPonDataPhoStr = "";
			/** 获取用户关联网格表中数据集合 */
			ArrayList<HashMap<String, Object>> resPonData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '3'");
			if (resPonData != null && resPonData.size() > 0) {
				resPonDataStr = resPonData.get(0).get("u_realname").toString();
				resPonDataPhoStr = resPonData.get(0).get("u_photo").toString();
			}

			/** 网格内监管内容 */
			String gridCodeContent = "";
			if (gridId != null && gridId != "") {
				String sql = "select SuperContext from Grid  where gridcode = '"
						+ gridId + "'";
				gridCodeContent = sqliteutil.getInstance()
						.getDepidByUserid(sql);
			}

			/** 主要职责 */
			String mainContext = "";
			if (gridId != null && gridId != "") {
				String sql = "select GridUserMapping.SuperContext from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
						+ gridId + "' and blamerole = '3'";
				mainContext = sqliteutil.getInstance().getDepidByUserid(sql);
			}

			/** 获取直接责任人的名称 */
			String directDataStr = "";
			/** 获取直接责任人的联系方式 */
			String directDataPhoStr = "";
			/** 获取用户关联网格表中数据集合 */
			ArrayList<HashMap<String, Object>> directData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '4'");
			if (directData != null && directData.size() > 0) {
				directDataStr = directData.get(0).get("u_realname").toString();
				directDataPhoStr = directData.get(0).get("u_photo").toString();
			}

			/** 获取登录用户的待执行任务数量 */
			int tasksize = rwxx.getTaskNumByUserIDandStatus(userId, "003");
			if (entData != null && entData.size() > 0) {
				/** 获得污染源的数量 */
				String pollutionStr = String.valueOf(entData.size());
				pollution_sources_edt.setText(pollutionStr);// 污染源数量
			}

			if (layerStr != null && !layerStr.equals("")
					&& layerStr.equals("2")) {
				direct_reponsibility_text.setVisibility(View.GONE);
				direct_responsibility_edt.setVisibility(View.GONE);
				gis_guid_direct_responsibility_telephone_layout
						.setVisibility(View.GONE);
			} else if (layerStr != null && !layerStr.equals("")
					&& layerStr.equals("3")) {
				direct_reponsibility_text.setVisibility(View.VISIBLE);
				direct_responsibility_edt.setVisibility(View.VISIBLE);
				direct_responsibility_phone_edt.setVisibility(View.VISIBLE);
			}

			main_responsibility_edt.setText(mainContext);// 主要职责
			supervise_content_edt.setText(gridCodeContent);// 网格内监管内容
			Log.e("hello", "131415");
			grid_code_edt.setText(gridId); // 网格编号
			overall_responsibility_edt.setText(mainNameStr);// 总负责人
			overall_responsibility_phone_edt.setText(mainteleStr);// 总负责人联系电话
			responsible_agencies_edt.setText(agencyStr);// 责任机构
			person_in_charge_edt.setText(resDataStr);// 主要负责人
			person_in_charge_photo_edt.setText(resDataPhoStr);// 主要负责人联系方式
			// superscalar_pollution_sources_edt.setText("");// 超标污染源数量
			// the_number_of_tasks_edt.setText(tasksize + "");// 任务数量
			gis_grid_infor_tv.setText(contextStr);// 获得当前网格信息

			primary_responsibility_edt.setText(resPonDataStr); // 主要责任人
			primary_responsibility_phone_edt.setText(resPonDataPhoStr); // 主要责任人联系方式
			direct_responsibility_edt.setText(directDataStr); // 直接责任人
			direct_responsibility_phone_edt.setText(directDataPhoStr); // 直接责任人联系方式
		}

		dialog = builder.create();
		dialog.show();
	}

	 /** 设置主体网格的坐标点 */
	 public ArrayList<HashMap<String, Object>> gridMainData() {
	 gridListMainData = new ArrayList<HashMap<String, Object>>();
	 gridMap = new HashMap<String, Object>();
	 /** 鸡冠区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.974083");
		gridMap.put("wd", "45.302475");
		gridMap.put("contextStr", "鸡冠区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 虎林市网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.970463");
		gridMap.put("wd", "45.765786");
		gridMap.put("contextStr", "虎林市");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 恒山区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.917987");
		gridMap.put("wd", "45.216586");
		gridMap.put("contextStr", "恒山区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 梨树区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.706063");
		gridMap.put("wd", "45.098883");
		gridMap.put("contextStr", "梨树区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 麻山区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.487007");
		gridMap.put("wd", "45.218369");
		gridMap.put("contextStr", "麻山区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 滴道区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.852836");
		gridMap.put("wd", "45.355472");
		gridMap.put("contextStr", "滴道区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 城子河区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.00388");
		gridMap.put("wd", "45.334551");
		gridMap.put("contextStr", "城子河区");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 密山市网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.86694");
		gridMap.put("wd", "45.540501");
		gridMap.put("contextStr", "密山市");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 鸡东县网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.13925");
		gridMap.put("wd", "45.248081");
		gridMap.put("contextStr", "鸡东县");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
	 return gridListMainData;
	
	 }
	 
	 /** 设置主体网格的坐标点 */
	 public ArrayList<HashMap<String, Object>> gridMainData2() {
	 gridListMainData = new ArrayList<HashMap<String, Object>>();
	 gridMap = new HashMap<String, Object>();
	 /** 鸡冠区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "126.528395");
		gridMap.put("wd", "45.80216");
		gridMap.put("contextStr", "哈尔滨市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		/** 虎林市网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "129.6298");
		gridMap.put("wd", "44.5572");
		gridMap.put("contextStr", "牡丹江市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		/** 恒山区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.3138");
		gridMap.put("wd", "47.3726");
		gridMap.put("contextStr", "鹤岗市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);

		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.956581");
		gridMap.put("wd", "45.289463");
		gridMap.put("contextStr", "鸡西市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "125.0974");
		gridMap.put("wd", "46.5866");
		gridMap.put("contextStr", "大庆市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "126.9748");
		gridMap.put("wd", "46.6348");
		gridMap.put("contextStr", "绥化市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.360988");
		gridMap.put("wd", "46.80924");
		gridMap.put("contextStr", "佳木斯市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.1514");
		gridMap.put("wd", "46.6392");
		gridMap.put("contextStr", "双鸭山市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.921631");
		gridMap.put("wd", "47.362822");
		gridMap.put("contextStr", "齐齐哈尔市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "128.833649");
		gridMap.put("wd", "47.725634");
		gridMap.put("contextStr", "伊春市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.929306");
		gridMap.put("wd", "45.784343");
		gridMap.put("contextStr", "七台河市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "127.4938");
		gridMap.put("wd", "50.2446");
		gridMap.put("contextStr", "黑河市");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
	
		
		
	 return gridListMainData;
	
	 }
	 
	 

	/** 设置单元网格的坐标点 */
	public ArrayList<HashMap<String, Object>> gridElementData() {
		gridListMainData = new ArrayList<HashMap<String, Object>>();
		gridMap = new HashMap<String, Object>();
		/** 鸡冠区市区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.957869");
		gridMap.put("wd", "45.300636");
		gridMap.put("contextStr", "鸡冠区市区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区市区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.98313");
		gridMap.put("wd", "45.298391");
		gridMap.put("contextStr", "鸡冠区市区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区市区3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.993341");
		gridMap.put("wd", "45.309905");
		gridMap.put("contextStr", "鸡冠区市区3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区市区4网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.012127");
		gridMap.put("wd", "45.300734");
		gridMap.put("contextStr", "鸡冠区市区4");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区市区5网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.975907");
		gridMap.put("wd", "45.291828");
		gridMap.put("contextStr", "鸡冠区市区5");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区红星乡网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.971755");
		gridMap.put("wd", "45.280865");
		gridMap.put("contextStr", "红星乡");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡冠区西郊乡网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.95697");
		gridMap.put("wd", "45.321984");
		gridMap.put("contextStr", "西郊乡");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 虎林市乡镇网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "133.278163");
		gridMap.put("wd", "45.962104");
		gridMap.put("contextStr", "虎林市乡镇");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 虎林市市区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "虎林市市区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 虎林市市区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "虎林市市区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 虎林市市区3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "虎林市市区3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 恒山区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.975139");
		gridMap.put("wd", "45.295396");
		gridMap.put("contextStr", "恒山区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 恒山区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.018951");
		gridMap.put("wd", "45.243572");
		gridMap.put("contextStr", "恒山区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 梨树区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.58993");
		gridMap.put("wd", "45.142234");
		gridMap.put("contextStr", "梨树区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 梨树区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.801499");
		gridMap.put("wd", "45.147116");
		gridMap.put("contextStr", "梨树区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 梨树区3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.704338");
		gridMap.put("wd", "45.044903");
		gridMap.put("contextStr", "梨树区3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 麻山区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.441876");
		gridMap.put("wd", "45.228933");
		gridMap.put("contextStr", "麻山区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 麻山区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.557721");
		gridMap.put("wd", "45.223854");
		gridMap.put("contextStr", "麻山区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 麻山区3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.557721");
		gridMap.put("wd", "45.223854");
		gridMap.put("contextStr", "麻山区3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 滴道区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.862024");
		gridMap.put("wd", "45.357774");
		gridMap.put("contextStr", "滴道区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 滴道区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.807263");
		gridMap.put("wd", "45.366488");
		gridMap.put("contextStr", "滴道区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 滴道区3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.862024");
		gridMap.put("wd", "45.357774");
		gridMap.put("contextStr", "滴道区3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 城子河区1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.981182");
		gridMap.put("wd", "45.349844");
		gridMap.put("contextStr", "城子河区1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 城子河区2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.052184");
		gridMap.put("wd", "45.340114");
		gridMap.put("contextStr", "城子河区2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 密山市南城区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.879867");
		gridMap.put("wd", "45.532343");
		gridMap.put("contextStr", "南城区");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 密山市北城区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.872393");
		gridMap.put("wd", "45.559208");
		gridMap.put("contextStr", "北城区");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 密山市农村网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.816339");
		gridMap.put("wd", "45.497682");
		gridMap.put("contextStr", "农村");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡东县县区网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.138261");
		gridMap.put("wd", "45.268796");
		gridMap.put("contextStr", "鸡东县县区");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡东县乡下1网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.210037");
		gridMap.put("wd", "45.174822");
		gridMap.put("contextStr", "鸡东县乡下1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡东县乡下2网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.330476");
		gridMap.put("wd", "45.297571");
		gridMap.put("contextStr", "鸡东县乡下2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡东县乡下3网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.982052");
		gridMap.put("wd", "45.50553");
		gridMap.put("contextStr", "鸡东县乡下3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** 鸡东县乡下4网格 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.32887");
		gridMap.put("wd", "45.300797");
		gridMap.put("contextStr", "鸡东县乡下4");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
	

		return gridListMainData;

	}

	
	
	
	/** 经纬度查询方法 */
	public void coord_query() {

		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View view = inflater.inflate(R.layout.gis_coordinates_query, null);

		gis_coordinates_query_jd_edt = (EditText) view
				.findViewById(R.id.gis_coordinates_query_jd_edt);
		gis_coordinates_query_wd_edt = (EditText) view
				.findViewById(R.id.gis_coordinates_query_wd_edt);
		Button gis_coordinates_query_sure_btn = (Button) view
				.findViewById(R.id.gis_coordinates_query_sure_btn);
		Button gis_coordinates_query_cancel_btn = (Button) view
				.findViewById(R.id.gis_coordinates_query_cancel_btn);

		/** 确定按钮 */
		gis_coordinates_query_sure_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						/** 获取输入框中的经度值 */
						String jd = gis_coordinates_query_jd_edt.getText()
								.toString();
						/** 获得输入框的纬度值 */
						String wd = gis_coordinates_query_wd_edt.getText()
								.toString();

						if (jd != null && !jd.equals("") && wd != null
								&& !wd.equals("")) {
							coord_query(jd, wd);
						} else {
							Toast.makeText(MapActivity.this, "经纬度不能为空",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		/** 取消按钮 */
		gis_coordinates_query_cancel_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(view);
		dialog.show();

	}

	/** 输入经纬度定位地图坐标 */
	public void coord_query(String jd, String wd) {

		/* 关闭网格 */
		bMeshing = false;

		/** 获得查询条件判断移除不符合的数据 */
		tLayer.removeAll();
		if (callout != null) {
			/** 清除标注 */
			callout.animatedHide();
		}
		/** 取消对话框 */
		dialog.cancel();
		/** 点坐标 */
		Point point = null;
		/** 经度 */
		Double wg_jd = null;
		/** 纬度 */
		Double wg_wd = null;
		/** 启动污染单击响应模式 */
		bPollution = true;
		/** 设置主体网格的坐标 */
		Drawable gridImage = MapActivity.this.getBaseContext().getResources()
				.getDrawable(R.drawable.hbdt_gis_icon_loc_bak);

		wg_jd = Double.parseDouble(jd);
		wg_wd = Double.parseDouble(wd);

		point = (Point) GeometryEngine.project(new Point(wg_jd, wg_wd),
				SpatialReference.create(4326), map.getSpatialReference());
		PictureMarkerSymbol imageMarkerSymbol = null;
		imageMarkerSymbol = new PictureMarkerSymbol(gridImage);

		HashMap<String, Object> coorMap = new HashMap<String, Object>();
		coorMap.put("jd", wg_jd);
		coorMap.put("wd", wg_wd);
		coorMap.put("quering", "是否查询周边信息");

		Graphic graphic = new Graphic(point, imageMarkerSymbol, coorMap,
				new InfoTemplate("", ""));
		tLayer.addGraphic(graphic);

		msg = handler.obtainMessage();
		msg.what = COORDINATE_QUERY_SUCCESS;
		msg.obj = point;
		handler.sendMessage(msg);
	}

	/** 企业批量定位方法 */
	private void enterprise_Batch_Locate() {

		final DialogFactory dialogfactory = new DialogFactory(MapActivity.this,
				"企业定位", "map_JBXX", null);
		AlertDialog.Builder builder = dialogfactory.showResearchDialog();
		builder.setPositiveButton("查询", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/* 关闭网格 */
				bMeshing = false;
				/** 获得查询条件判断移除不符合的数据 */
				tLayer.removeAll();
				if (callout != null) {
					/** 清除标注 */
					callout.animatedHide();
				}
				distoryDialog(dialog);
				progressDialog = new ProgressDialog(MapActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("正在查找，请稍等...");
				progressDialog.show();

				new Thread(new Runnable() {

					@Override
					public void run() {
						Point ptMap = null;
						try {
							Double qy_jd = null;
							Double qy_wd = null;
							HashMap<String, Object> returnValueMap = dialogfactory
									.BuildQueryValue();
							StringBuffer str = new StringBuffer();
							
							/** 行政区划 */
							String JiXiCode = "230000000";
							/** 查询行政区划的数据集合 */
							ArrayList<HashMap<String, Object>> regionsData = new ArrayList<HashMap<String, Object>>();
							regionsData = SqliteUtil
									.getInstance()
									.queryBySqlReturnArrayListHashMap(
											"select * from Region where parentCode in(select RegionCode from Region where parentcode = '"
													+ JiXiCode
													+ "' ) or parentcode = '"
													+ JiXiCode + "'");
							if (regionsData != null && regionsData.size() > 0) {

								ArrayList<String> regionListStr = new ArrayList<String>();
								for (int i = 0; i < regionsData.size(); i++) {
									String regionStr = regionsData.get(i)
											.get("regioncode").toString();
									regionListStr.add(regionStr);
								}
								String regionStrs = "";
								for (int i = 0; i < regionListStr.size(); i++) {
									regionStrs += regionListStr.get(i)
											.toString() + "、";
								}
								String[] regions = regionStrs.split("、");

								for (String region : regions) {
									str.append("'" + region + "',");
								}
								str.deleteCharAt(str.length() - 1).toString();
							}

							String strs = str.toString();
							String sql = "select * from V_WHY_QYJBXX where XZQH in ("
									+ strs
									+ ") and JD is not null and WD is not null and JD is not '' and WD is not ''";
							ArrayList<HashMap<String, Object>> dataList = BaseClass.DBHelper
									.getOrderList("V_WHY_QYJBXX",
											returnValueMap,
											"kysj asc limit 0,500", sql);
							if (dataList == null || dataList.size() == 0) {
								msg = handler.obtainMessage();
								msg.what = DBNOCONTENT;
								handler.sendMessage(msg);
								return;
							}
							/** 启动污染单击响应模式 */
							bPollution = true;
							Drawable image1 = MapActivity.this.getBaseContext()
									.getResources()
									.getDrawable(R.drawable.hbdt_gis_icon_loc);
							Drawable image2 = MapActivity.this.getBaseContext()
									.getResources()
									.getDrawable(R.drawable.hbdt_gis_icon_polu);

							for (HashMap<String, Object> qymap : dataList) {
								try {
									qy_jd = Double.parseDouble(qymap.get("jd")
											.toString());
									qy_wd = Double.parseDouble(qymap.get("wd")
											.toString());
								} catch (Exception e) {
									continue;
								}
								ptMap = (Point) GeometryEngine.project(
										new Point(qy_jd, qy_wd),
										SpatialReference.create(4326),
										map.getSpatialReference());
								PictureMarkerSymbol imageMarkerSymbol = null;
								String zdwry = qymap.get("zdwry").toString();
								if (zdwry.equals("1")) {
									imageMarkerSymbol = new PictureMarkerSymbol(
											image1);
								} else {
									imageMarkerSymbol = new PictureMarkerSymbol(
											image2);
								}
								Map<String, Object> cmap = new HashMap<String, Object>();
								cmap.put("tagid", qymap.get("guid").toString());
								cmap.put("tag", qymap.get("qymc").toString());
								cmap.put("jd", qy_jd);
								cmap.put("wd", qy_wd);
								cmap.put("zdwry", zdwry);

								Graphic g = new Graphic(ptMap,
										imageMarkerSymbol, cmap,
										new InfoTemplate("", ""));
								tLayer.addGraphic(g);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						msg = handler.obtainMessage();
						msg.what = REFRESHLAYER;
						msg.obj = ptMap;
						handler.sendMessage(msg);
					}
				}).start();

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		((Dialog) alertDialog).setCanceledOnTouchOutside(true);
		alertDialog.show();

	}

	/** 车辆定位的方法 */
	private void car_Location() {
		/* 关闭网格 */
		bMeshing = false;

		tLayer.removeAll();
		if (callout != null) {
			/** 清除标注 */
			callout.animatedHide();
		}
		List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		String addpwd = "";
		try {
			addpwd = DESSecurity.encrypt("GetCar");
			param.put("token", addpwd);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		params.add(param);
		try {
			String strjson = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetCar", params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (strjson != null && !"".equals(strjson) && !"[]".equals(strjson)) {
				org.json.JSONArray arr;
				try {
					arr = new org.json.JSONArray(new org.json.JSONTokener(
							strjson));
					Drawable carDrawable = MapActivity.this.getResources()
							.getDrawable(R.drawable.car);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jsonObject = arr.getJSONObject(i);
						double x = Double.parseDouble(jsonObject
								.getString("longitude"));
						double y = Double.parseDouble(jsonObject
								.getString("latitude"));
						String CarNumber = "";
						if (jsonObject.getString("CarNumber") != null) {
							CarNumber = jsonObject.getString("CarNumber")
									.toString();
						}
						/** 经纬度为0无法定位，继续循环 */
						if (x == 0 || y == 0) {
							Toast.makeText(MapActivity.this,
									"车辆" + CarNumber + "返回数据错误，无法定位！",
									Toast.LENGTH_SHORT).show();
							continue;
						}
						String CarDutyPerson = "", CarDesc = "", CarKind = "", CarDutyPersonTelephone = "";

						if (jsonObject.getString("CarDutyPerson") != null
								&& jsonObject.getString("CarDutyPerson")
										.toString() != "null") {
							CarDutyPerson = jsonObject.getString(
									"CarDutyPerson").toString();
						}
						if (jsonObject.getString("CarDesc") != null
								&& jsonObject.getString("CarDesc").toString() != "null") {
							CarDesc = jsonObject.getString("CarDesc")
									.toString();
						}
						if (jsonObject.getString("CarKind") != null
								&& jsonObject.getString("CarKind").toString() != "null") {
							CarKind = jsonObject.getString("CarKind")
									.toString();
						}
						if (jsonObject.getString("CarDutyPersonTelephone") != null
								&& jsonObject.getString(
										"CarDutyPersonTelephone").toString() != "null") {
							CarDutyPersonTelephone = jsonObject.getString(
									"CarDutyPersonTelephone").toString();
						}

						Point point = new Point(x, y);
						final Map<String, Object> mmap = new HashMap<String, Object>();
						mmap.put("CarNumber", CarNumber);
						mmap.put("jd", x);
						mmap.put("wd", y);
						mmap.put("CarDutyPerson", CarDutyPerson);
						mmap.put("CarDesc", CarDesc);
						mmap.put("CarKind", CarKind);
						mmap.put("CarDutyPersonTelephone",
								CarDutyPersonTelephone);
						Graphic gp = new Graphic(point,
								new PictureMarkerSymbol(carDrawable), mmap,
								null);
						View.OnClickListener mylistener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								LayoutInflater li = LayoutInflater
										.from(MapActivity.this);

								View va = li.inflate(R.layout.map_car_details,
										null);
								EditText et_CarNumber = (EditText) va
										.findViewById(R.id.car_detalis_carname);
								EditText et_CarDutyPerson = (EditText) va
										.findViewById(R.id.car_detalis_car_mastername);
								EditText et_CarDesc = (EditText) va
										.findViewById(R.id.car_detalis_car_describe);
								EditText et_CarKind = (EditText) va
										.findViewById(R.id.car_detalis_car_kind);
								final Button et_CarDutyPersonTelephone = (Button) va
										.findViewById(R.id.car_detalis_car_masterphone);
								et_CarNumber.setText(mmap.get("CarNumber")
										.toString());
								et_CarDutyPerson.setText(mmap.get(
										"CarDutyPerson").toString());
								et_CarDesc.setText(mmap.get("CarDesc")
										.toString());
								et_CarKind.setText(mmap.get("CarKind")
										.toString());
								et_CarDutyPersonTelephone.setText(mmap.get(
										"CarDutyPersonTelephone").toString());

								et_CarDutyPersonTelephone
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												tocall(et_CarDutyPersonTelephone
														.getText().toString());
											}
										});
								dialog = new Dialog(MapActivity.this,
										R.style.FullScreenDialog);
								dialog.setCanceledOnTouchOutside(true);
								dialog.setContentView(va);
								dialog.show();

							}
						};
						callout(point, CarNumber, mylistener);
						tLayer.addGraphic(gp);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(MapActivity.this, "当前车辆无法定位！",
						Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 图层控制方法 */
	private void layer_Control() {
		if (callout != null) {
			/** 清除标注 */
			callout.animatedHide();
		}
		LayoutInflater ll = LayoutInflater.from(MapActivity.this);
		View view = ll.inflate(R.layout.gis_mapandlayer_choose, null);
		final Dialog layerdialog = new Dialog(MapActivity.this,
				R.style.gis_maplayer_dialog);
		/** 通过解析xmL文件获得需要查询的图层名称 类型 图标 */
		LinearLayout showcontronl = (LinearLayout) view
				.findViewById(R.id.showcontronl);
		/** 读取gis_config */
		ArrayList<HashMap<String, Object>> list = DisplayUitl.getMapLayerData();
		if (list == null || list.size() == 0) {
			Toast.makeText(MapActivity.this, "无图层配置", 1).show();
			return;
		}
		int checkID = 0;
		final String[] checkCondition = new String[list.size()];
		/** 建立类型对图片的映射 */
		final HashMap<String, String> pictureMap = new HashMap<String, String>();

		for (final HashMap<String, Object> dataMap : list) {
			if (dataMap.get("isonline").toString().equals("0")) {// 离线查询数据库添加图层
				pictureMap.put(dataMap.get("type").toString(),
						dataMap.get("icon").toString());
				RelativeLayout Relative = new RelativeLayout(MapActivity.this);
				Relative.setPadding(10, 0, 0, 20);// 设置左边距10dp,右边距20
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				int syncAllId = 224522;
				int syncId = 224523;
				/** 动态加载图标 */
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				layoutParams.setMargins(20, 0, 0, 0);
				ImageView image = new ImageView(MapActivity.this);
				image.setId(syncAllId);
				/** 获得图片资源id */
				int id = MapActivity.this.getResources().getIdentifier(
						dataMap.get("icon").toString(), "drawable",
						MapActivity.this.getPackageName());
				Drawable img = getResources().getDrawable(id);
				image.setBackgroundDrawable(img);
				Relative.addView(image, layoutParams);

				/** 动态加载需要查询的名称 */
				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				layoutParams.setMargins(30, 0, 20, 0);
				TextView text = new TextView(MapActivity.this);
				text.setText(dataMap.get("name").toString());
				text.setGravity(Gravity.CENTER);
				text.setId(syncId);
				text.setTextSize(18f);
				layoutParams.addRule(RelativeLayout.RIGHT_OF, syncAllId);
				Relative.addView(text, layoutParams);

				/** 动态加载checkBox */
				layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				layoutParams.setMargins(60, 0, 0, 0);
				final CheckBox checkbox = new CheckBox(MapActivity.this);
				checkbox.setGravity(Gravity.RIGHT);
				checkbox.setId(checkID);
				checkID++;
				checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int id = checkbox.getId();
						if (isChecked) {
							checkCondition[id] = dataMap.get("type").toString();

						} else {
							checkCondition[id] = null;
						}
					}
				});

				layoutParams.addRule(RelativeLayout.RIGHT_OF, syncId);
				Relative.addView(checkbox, layoutParams);
				showcontronl.addView(Relative);
			}
		}

		LinearLayout line = new LinearLayout(MapActivity.this);
		line.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 65);
		Button confirm = new Button(MapActivity.this);
		confirm.setText("确认");
		Drawable dSyncAll = getResources().getDrawable(
				R.drawable.gis_map_dialog_btn);
		confirm.setBackgroundDrawable(dSyncAll);
		confirm.setTextColor(Color.WHITE);
		confirm.setTextSize(18);
		layoutParam.setMargins(30, 0, 0, 0);
		line.addView(confirm, layoutParam);

		Button cancel = new Button(MapActivity.this);
		cancel.setText("取消");
		Drawable dSync = getResources().getDrawable(
				R.drawable.gis_map_dialog_btn);
		cancel.setBackgroundDrawable(dSync);
		cancel.setTextColor(Color.WHITE);
		cancel.setTextSize(18);
		layoutParam.setMargins(0, 0, 0, 0);
		line.addView(cancel, layoutParam);

		showcontronl.addView(line);

		layerdialog.setContentView(view);
		layerdialog.show();

		/** 确定被选择图层后删除以前图层定位并且查询显选择的图层企业 */
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** 动态拼sqL语句查询需要显示地图定位的企业 */

				/* 关闭网格 */
				bMeshing = false;

				tLayer.removeAll();
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (String condition : checkCondition) {
					if (condition != null) {
						sb.append(condition + ",");
					}
				}
				if (sb.toString().equals("(")) {
					/** 条件数据为空 */
					return;
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				sb.append(")");

				String strSql = "select * from Environment where type in "
						+ sb.toString();
				Log.i(TAG, "图层控制的sql语句为-->" + strSql);
				ArrayList<HashMap<String, Object>> mylist = sqliteutil
						.queryBySqlReturnArrayListHashMap(strSql);
				if (mylist == null || mylist.size() == 0) {
					Toast.makeText(MapActivity.this, "数据库无此图层数据",
							Toast.LENGTH_LONG).show();
					return;
				}
				PictureMarkerSymbol symbol = null;
				Point point = null;
				Resources pictureResource = MapActivity.this.getResources();
				String packageName = MapActivity.this.getPackageName();
				for (HashMap<String, Object> mmap : mylist) {
					Map<String, Object> map = new HashMap<String, Object>();
					try {

						Double jd = Double.valueOf((mmap.get("x").toString()));
						Double wd = Double.valueOf((mmap.get("y").toString()));
						point = new Point(jd, wd);
						map.put("jd", jd);
						map.put("wd", wd);
					} catch (Exception e) {
						e.printStackTrace();
						Log.i(TAG, "出异常的经纬度-->" + mmap.get("x").toString()
								+ "," + mmap.get("y").toString());
						continue;
					}

					String pictureName = pictureMap.get(mmap.get("type"))
							.toString();
					symbol = new PictureMarkerSymbol(pictureResource
							.getDrawable(pictureResource.getIdentifier(
									pictureName, "drawable", packageName)));
					String strqymc = (String) mmap.get("name");

					map.put("strqymc", strqymc);
					Graphic gp = new Graphic(point, symbol, map, null);
					tLayer.addGraphic(gp);
				}
				layerdialog.cancel();
				loadFullMap();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layerdialog.cancel();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(MapActivity.this)
				.setTitle("Select Geometry")
				/** geometryTypes为这个弹出框的数据源 */
				.setItems(geometryTypes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						/** 测试用的点线面触摸监听事件 */
						myOnTouchListener = new MyOnTouchListener(
								MapActivity.this, map);
						/** 测试用的点线面触摸监听事件 */
						map.setOnTouchListener(myOnTouchListener);

						/** 移除不符合的数据 */
						tLayer.removeAll();
						if (callout != null) {
							/** 清除标注 */
							callout.animatedHide();
						}
						/** Toast提示消息 */
						Toast toast = Toast.makeText(getApplicationContext(),
								"", Toast.LENGTH_LONG);
						/** 提示信息显示的位置 */
						toast.setGravity(Gravity.BOTTOM, 0, 0);

						/** 获取数组中的字符串 */
						String geomType = geometryTypes[which];
						Log.i(TAG, geomType);
						selectedGeometryIndex = which;

						if (geomType.equalsIgnoreCase("Polygon")) {
							myOnTouchListener.setType("POLYGON");
							toast.setText("拖动手指在屏幕上画一个多边形.\n松开手指,停止绘制.");
						} else if (geomType.equalsIgnoreCase("Polyline")) {
							myOnTouchListener.setType("POLYLINE");
							toast.setText("拖动手指在屏幕上画一个折线. \n松开手指,停止绘制.");
						} else if (geomType.equalsIgnoreCase("Point")) {
							myOnTouchListener.setType("POINT");
							toast.setText("点击一次在屏幕上画一个点.");
						} else if (geomType.equalsIgnoreCase("取消点线面空间查询")) {
							toast.setText("取消点线面空间查询.");
						}

						toast.show();
					}
				}).create();
	}

	class MyOnTouchListener extends MapOnTouchListener {
		/** 此变量是用来保存线或者面的轨迹数据的 */
		MultiPath poly;
		/** 判断是执行的哪种绘图方式，它可以为这三种值：point，polgyLine，polGyon */
		String type = "";
		/** 绘画点时用于储存点的变量 */
		Point startPoint = null;

		public MyOnTouchListener(Context context, MapView view) {
			super(context, view);
		}

		public void setType(String geometryType) {
			this.type = geometryType;
		}

		public String getType() {
			return type;
		}

		/** 当我们绘制点时执行的处理函数 */
		@Override
		public boolean onSingleTap(MotionEvent e) {
			if (type.length() > 1 && type.equalsIgnoreCase("POINT")) {
				/** 生成点图形，并设置相应的样式 */
				Graphic graphic = new Graphic(map.toMapPoint(new Point(
						e.getX(), e.getY())), new SimpleMarkerSymbol(Color.RED,
						25, STYLE.CIRCLE));
				/** 将点要素添加到图层上 */
				tLayer.addGraphic(graphic);
				return true;
			} else if (type.equals("")) {
				/** 判断是否为周边查询模式 */
				if (isClickZbcx) {
					initialAroundFind(e.getX(), e.getY());
					isClickZbcx = false;
					return true;
				}

				/** 获取要素及其要素的相关属性 */
				final int[] graphics = tLayer.getGraphicIDs(e.getX(), e.getY(),
						30);
				if (graphics.length > 0) {
					final Graphic graphic = tLayer.getGraphic(graphics[0]);
					/** 获取离点击位置最近的位图 */
					if (graphic.getAttributeValue("tag") != null) {
						/** 周边查询和批量定位点击响应 */
						Double jd = Double.valueOf(graphic.getAttributeValue(
								"jd").toString());
						Double wd = Double.valueOf(graphic.getAttributeValue(
								"wd").toString());
						final Point pt = new Point(jd, wd);
						qymc = tLayer.getGraphic(graphics[0])
								.getAttributeValue("tag").toString();
						qyid = tLayer.getGraphic(graphics[0])
								.getAttributeValue("tagid").toString();

						if (bPollution) {
							callout(pt, qymc, qyListener);
						} else {
							callout(pt, qymc, null);
						}
					}

					else if (graphic.getAttributeValue("surround_ent") != null) {

						/** 周边查询和批量定位点击响应 */
						Double jd = Double.valueOf(graphic.getAttributeValue(
								"jd").toString());
						Double wd = Double.valueOf(graphic.getAttributeValue(
								"wd").toString());
						final Point pt = new Point(jd, wd);
						qymc = tLayer.getGraphic(graphics[0])
								.getAttributeValue("surround_ent").toString();
						qyid = tLayer.getGraphic(graphics[0])
								.getAttributeValue("surround_ent_id")
								.toString();

						if (bPollution) {
							callout(pt, qymc, qyListener);
						} else {
							callout(pt, qymc, null);
						}

					}

					else if (graphic.getAttributeValue("quering") != null) {
						/** 初始化地图的周边查询 */
						initialAroundFind(e.getX(), e.getY());
					}

					/** 获得网格坐标的点击事件 */
					else if (graphic.getAttributeValue("cStr") != null) {
						final String contextStr = (String) graphic
								.getAttributeValue("cStr");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point point = new Point(jd, wd);
						View.OnClickListener gridListener = new OnClickListener() {

							@SuppressWarnings("static-access")
							@Override
							public void onClick(View v) {

								LayoutInflater inflater = LayoutInflater
										.from(MapActivity.this);
								View view = inflater.inflate(
										R.layout.gis_guid_admin_info_dialog,
										null);
								/** 网格化信息标题 */
								// TextView gis_grid_infor_tv = (TextView) view
								// .findViewById(R.id.gis_grid_infor_tv);
								TextView gis_grid_infor_tv = (TextView) view
										.findViewById(R.id.gis_grid_infor_ad);
								EditText overall_responsibility_edt = (EditText) view
										.findViewById(R.id.overall_responsibility_edt);
								EditText overall_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.overall_responsibility_phone_edt);
								EditText responsible_agencies_edt = (EditText) view
										.findViewById(R.id.responsible_agencies_edt);
								EditText person_in_charge_edt = (EditText) view
										.findViewById(R.id.person_in_charge_edt);
								EditText person_in_charge_photo_edt = (EditText) view
										.findViewById(R.id.person_in_charge_photo_edt);
								EditText pollution_sources_edt = (EditText) view
										.findViewById(R.id.pollution_sources_edt);
								// EditText superscalar_pollution_sources_edt =
								// (EditText) view
								// .findViewById(R.id.superscalar_pollution_sources_edt);
								// EditText the_number_of_tasks_edt = (EditText)
								// view
								// .findViewById(R.id.the_number_of_tasks_edt);

								if (contextStr != null
										&& !contextStr.equals("")) {
									/** 总负责人姓名 */
									String mainNameStr = "";
									/** 总负责人电话 */
									String mainteleStr = "";
									/** 获得总负责人信息 */
									ArrayList<HashMap<String, Object>> mainData = sqliteutil
											.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
															+ gridId
															+ "' and blamerole = '1'");
									if (mainData != null && mainData.size() > 0) {
										mainNameStr = mainData.get(0)
												.get("u_realname").toString();
										mainteleStr = mainData.get(0)
												.get("u_photo").toString();
									}

									/** 初始化网格化数据集合 */
									gridData = new ArrayList<HashMap<String, Object>>();
									gridData = sqliteutil.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select gridcode from Grid where gridname = '"
															+ contextStr + "'");

									if (gridData != null && gridData.size() > 0) {
										/** 获得网格的id */
										gridId = gridData.get(0)
												.get("gridcode").toString();
									}
									/** 责任机构 */
									String agencyStr = "";
									if (gridId != null && !gridId.equals("")) {
										String sql = "select ResponDepart  from Grid where GridCode = '"
												+ gridId + "'";
										agencyStr = sqliteutil.getInstance()
												.getDepidByUserid(sql);
									}
									if (gridId != null && !gridId.equals("")) {
										/** 获取主要负责人的名称 */
										String resDataStr = "";
										/** 获取主要负责人的联系方式 */
										String resDataPhoStr = "";
										/** 获取用户关联网格表中parentCode */
										ArrayList<HashMap<String, Object>> resData = sqliteutil
												.getInstance()
												.queryBySqlReturnArrayListHashMap(
														"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																+ gridId + "'");
										if (resData != null
												&& resData.size() > 0) {
											resDataStr = resData.get(0)
													.get("u_realname")
													.toString();
											resDataPhoStr = resData.get(0)
													.get("u_photo").toString();
										}

										/** 获取登录用户的待执行任务数量 */
										int tasksize = rwxx
												.getTaskNumByUserIDandStatus(
														userId, "003");

										overall_responsibility_edt
												.setText(mainNameStr);// 总负责人
										overall_responsibility_phone_edt
												.setText(mainteleStr);// 总负责人联系电话
										responsible_agencies_edt
												.setText(agencyStr);// 责任机构
										person_in_charge_edt
												.setText(resDataStr);// 主要负责人
										person_in_charge_photo_edt
												.setText(resDataPhoStr);// 主要负责人联系方式

										if (entData != null
												&& entData.size() > 0) {
											/** 获得污染源的数量 */
											String pollutionStr = String
													.valueOf(entData.size());
											pollution_sources_edt
													.setText(pollutionStr);// 污染源数量
										}
										//
										// superscalar_pollution_sources_edt
										// .setText("");// 超标污染源数量
										// the_number_of_tasks_edt
										// .setText(tasksize + "");// 任务数量
										gis_grid_infor_tv.setText(graphic
												.getAttributeValue("cStr")
												.toString());// 获得当前网格信息
									}
								}

								dialog = new Dialog(MapActivity.this,
										R.style.FullScreenDialog);
								dialog.setCanceledOnTouchOutside(true);
								dialog.setContentView(view);
								dialog.show();

							}
						};

						callout(point, contextStr, gridListener);
					}
					/** 位图点击响应 */
					else if (graphic.getAttributeValue("strqymc") != null) {
						String content = (String) graphic
								.getAttributeValue("strqymc");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
					/** 车辆定位点击响应 */
					else if (graphic.getAttributeValue("CarNumber") != null) {
						String content = (String) graphic
								.getAttributeValue("CarNumber");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						View.OnClickListener mylistener = new OnClickListener() {

							@Override
							public void onClick(View v) {

								LayoutInflater li = LayoutInflater
										.from(MapActivity.this);

								View va = li.inflate(R.layout.map_car_details,
										null);
								EditText et_CarNumber = (EditText) va
										.findViewById(R.id.car_detalis_carname);
								EditText et_CarDutyPerson = (EditText) va
										.findViewById(R.id.car_detalis_car_mastername);
								EditText et_CarDesc = (EditText) va
										.findViewById(R.id.car_detalis_car_describe);
								EditText et_CarKind = (EditText) va
										.findViewById(R.id.car_detalis_car_kind);
								final Button et_CarDutyPersonTelephone = (Button) va
										.findViewById(R.id.car_detalis_car_masterphone);
								et_CarNumber.setText((CharSequence) graphic
										.getAttributeValue("CarNumber"));
								et_CarDutyPerson.setText((CharSequence) graphic
										.getAttributeValue("CarDutyPerson"));
								et_CarDesc.setText((CharSequence) graphic
										.getAttributeValue("CarDesc"));
								et_CarKind.setText((CharSequence) graphic
										.getAttributeValue("CarKind"));
								et_CarDutyPersonTelephone
										.setText((CharSequence) graphic
												.getAttributeValue("CarDutyPersonTelephone"));

								et_CarDutyPersonTelephone
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												tocall(et_CarDutyPersonTelephone
														.getText().toString());
											}
										});
								dialog = new Dialog(MapActivity.this,
										R.style.FullScreenDialog);
								dialog.setCanceledOnTouchOutside(true);
								dialog.setContentView(va);
								dialog.show();
							}
						};
						callout(pt, content, mylistener);
					} else if (graphic.getAttributeValue("U_RealName") != null) {// 人员定位
						String content = (String) graphic
								.getAttributeValue("U_RealName");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
				}

				if (graphics.length == 1) {// 点击一个点时予以应

					if (tLayer.getGraphic(graphics[0]).getAttributeValue(
							"pname") == null) {// 定位的点无点击事件

						if (tLayer.getGraphic(graphics[0]).getAttributeValue(
								"teamate") != null) {// 给同事打电话
							Toast.makeText(
									MapActivity.this,
									tLayer.getGraphic(graphics[0])
											.getAttributeValue("teamate")
											.toString(), Toast.LENGTH_LONG)
									.show();
							CallTeamate(tLayer.getGraphic(graphics[0])
									.getAttributeValue("teamate").toString());
						}
					}
				}
			}

			return false;

		}

		/** 当绘制线或者面时调用的函数 */
		@Override
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			if (type.length() > 1
					&& (type.equalsIgnoreCase("POLYLINE") || type
							.equalsIgnoreCase("POLYGON"))) {
				/** 得到移动后的点 */
				Point point = map.toMapPoint(to.getX(), to.getY());

				/** 判断startPoint是否为空,如果为空，给startPoint赋值 */
				if (startPoint == null) {
					tLayer.removeAll();
					poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline()
							: new Polygon();
					/** 得到开始移动的点 的要素地理位置信息 */
					startPoint = map.toMapPoint(from.getX(), from.getY());
					/** 将第一个点存入poLy中,路径的起始位置 */
					poly.startPath((float) startPoint.getX(),
							(float) startPoint.getY());
					Graphic graphic = new Graphic(startPoint,
							new SimpleLineSymbol(Color.RED, 5));
					tLayer.addGraphic(graphic);
				}
				/** 将移动的点放入poLy中,路径的结束为止,在最后一个点上添加一个线的片段 */
				poly.lineTo((float) point.getX(), (float) point.getY());

				return true;
			}
			return super.onDragPointerMove(from, to);
		}

		/** 当绘制完线或面，离开屏幕时调用的函数 */
		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if (type.length() > 1
					&& (type.equalsIgnoreCase("POLYLINE") || type
							.equalsIgnoreCase("POLYGON"))) {
				/** 判断当绘制的是面时，将起始点填入到poLy中形成闭合 */
				if (type.equalsIgnoreCase("POLYGON")) {
					poly.lineTo((float) startPoint.getX(),
							(float) startPoint.getY());
					tLayer.removeAll();
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.GRAY);
					simpleFillSymbol.setAlpha(70);
					tLayer.addGraphic(new Graphic(poly, simpleFillSymbol));
				}
				/** 最后将poLy图形添加到图层中去 */
				tLayer.addGraphic(new Graphic(poly, new SimpleLineSymbol(
						Color.BLUE, 5)));

				startPoint = null;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}
	}

	/** 获得当前位置的坐标 */
	class CurrentLocationListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// 开启GPS功能
			openGPSSetting();
		}
	}

	/** 开启GPS功能 */
	private void openGPSSetting() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS已开启,正在获取位置信息...", Toast.LENGTH_SHORT)
					.show();
			// 获取当前位置信息
			getCurrentLocation();
			return;
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MapActivity.this);
			builder.setTitle("GPS定位功能");
			builder.setMessage("是否开启GPS定位");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_SECURITY_SETTINGS);
							// 此为设置完成后返回到获取界面
							startActivityForResult(intent, 0);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0) {
			// 获取当前位置信息
			getCurrentLocation();
		}
	}

	/** 获取当前GPS定位的位置信息 */
	public void getCurrentLocation() {
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i(TAG, "location-->" + location);
		if (location != null) {
			// 当GPS LocationProvider可用时,更新位置
			updateMapView(location);
			// 更新位置信息
			updateToNewLocation(location);
			// 注册状态信息回调
			locationManager.addGpsStatusListener(statusListener);
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 60 * 1000, 500,
					locationListener);
		}
	};

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(TAG, "provider-->状态改变");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(TAG, "provider-->可用");
			if (location != null) {
				// 当GPS LocationProvider可用时,更新位置
				updateMapView(location);
				// 更新位置信息
				updateToNewLocation(location);
				// 监听卫星状态
				locationManager.addGpsStatusListener(statusListener);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(TAG, "provider-->不可用");
		}

		@Override
		public void onLocationChanged(Location location) {
			// 当GPS定位信息发生改变时,更新位置
			updateMapView(location);
			// 更新位置信息
			updateToNewLocation(location);
			// 监听卫星状态
			locationManager.addGpsStatusListener(statusListener);
		}
	};

	/** 根据Location来更新地图 */
	public void updateMapView(Location location) {
		String jd = String.valueOf(location.getLongitude());
		String wd = String.valueOf(location.getLatitude());
		/** 点坐标 */
		Point point = null;
		/** 经度 */
		Double wg_jd = null;
		/** 纬度 */
		Double wg_wd = null;
		/** 启动污染单击响应模式 */
		bPollution = true;
		/** 设置主体网格的坐标 */
		Drawable gridImage = MapActivity.this.getBaseContext().getResources()
				.getDrawable(R.drawable.hbdt_gis_icon_loc_bak);

		wg_jd = Double.parseDouble(jd);
		wg_wd = Double.parseDouble(wd);

		point = (Point) GeometryEngine.project(new Point(wg_jd, wg_wd),
				SpatialReference.create(4326), map.getSpatialReference());
		PictureMarkerSymbol imageMarkerSymbol = null;
		imageMarkerSymbol = new PictureMarkerSymbol(gridImage);

		HashMap<String, Object> coorMap = new HashMap<String, Object>();
		coorMap.put("jd", wg_jd);
		coorMap.put("wd", wg_wd);
		coorMap.put("quering", "您经过的位置");

		Graphic graphic = new Graphic(point, imageMarkerSymbol, coorMap,
				new InfoTemplate("", ""));
		tLayer.addGraphic(graphic);

		msg = handler.obtainMessage();
		msg.what = COORDINATE_QUERY_SUCCESS;
		msg.obj = point;
		handler.sendMessage(msg);
	}

	/** 卫星信号 */
	private final List<GpsSatellite> NumSatelliteList = new ArrayList<GpsSatellite>();

	/** 卫星状态监听器 */
	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		@Override
		public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
			GpsStatus status = locationManager.getGpsStatus(null); // 取当前状态
			updateGpsStatus(event, status);
		}
	};

	/** 更新卫星状态 */
	private void updateGpsStatus(int event, GpsStatus status) {
		if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			NumSatelliteList.clear();
			int count = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite gpsSta = it.next();
				NumSatelliteList.add(gpsSta);
				count++;
			}
		}
	}

	/** 更新位置信息 */
	private void updateToNewLocation(Location location) {
		Time time = new Time();
		time.setToNow(); // 取得系统时间
		int year = time.year;// 获得年
		int month = time.month + 1;// 获得月
		int date = time.monthDay;// 获得天
		int hour = time.hour; // 24小时制,获得小时
		int minute = time.minute;// 获得分钟
		int second = time.second;// 获得秒
		TextView map_gps_info = (TextView) this.findViewById(R.id.map_gps_info);
		if (location != null) {
			double latitude = location.getLatitude();// 纬度
			double longitude = location.getLongitude();// 经度
			double altitude = location.getAltitude(); // 海拔
			double speed = location.getSpeed();// 速度
			double bear = location.getBearing();// 方向
			map_gps_info.setText("搜索卫星个数:" + NumSatelliteList.size() + "/n"
					+ "纬度:" + latitude + "/n" + "经度:" + longitude + "/n"
					+ "海拔:" + altitude + "/n" + "速度:" + speed + "/n" + "方向:"
					+ bear + "/n" + "时间:" + year + "年" + month + "月" + date
					+ "日" + hour + ":" + minute + ":" + second);
		} else {
			map_gps_info.setText("无法获取地理信息");
		}
	}

	/** 获取地图的基本信息 */
	public void BasicInfor() {
		/** 设置是否允许地图通过pinch方式旋转 */
		map.setAllowRotationByPinch(true);
		/** 设置地图的旋转角度 */
		map.setRotationAngle(15.0);
		Log.i(TAG, "获取地图的中心点--->" + map.getCenter());
		Log.i(TAG, "获取地图最小外包矩形--->" + map.getExtent());
		Log.i(TAG, "获取地图的边界--->" + map.getMapBoundaryExtent());
		Log.i(TAG, "获取地图最大分辨率--->" + map.getMaxResolution());
		Log.i(TAG, "获取地图最小分辨率--->" + map.getMinResolution());
		Log.i(TAG, "获取当前地图分辨率--->" + map.getResolution());
		Log.i(TAG, "获取当前地图比例尺--->" + map.getScale());
	}

	/** 初始化地图添加的控件 */
	@Override
	protected void InitView() {
		/** 获取地图的基本信息测试用 */
		// BasicInfor();
		/** 企业批量定位 */
		btn_company = (Button) this.findViewById(R.id.btn_company);
		/** 人员定位 */
		btn_teammate = (Button) this.findViewById(R.id.btn_teammate);
		/** 车辆定位 */
		btn_car = (Button) this.findViewById(R.id.btn_car);
		/** 地图图层选项 */
		btn_layers = (Button) this.findViewById(R.id.btn_layerDisplay);
		/** 周边查询 */
		btn_zbcx = (Button) this.findViewById(R.id.around_find);
		/** 登录用户所管辖的企业 */
		btn_grid_jur_ent = (Button) this.findViewById(R.id.btn_grid_jur_ent);
		/** 网格信息查询 */
		btn_grid_query = (Button) this.findViewById(R.id.btn_grid_query);
		/** 判断地图是否网格化划分的标识的按钮 */
		btn_bMeshing = (Button) this.findViewById(R.id.btn_bMeshing);

		/** 经纬度查询 */
		btn_grid_coordinate = (Button) this
				.findViewById(R.id.btn_grid_coordinate);
		/** 测试信息 */
		map_tv = (TextView) this.findViewById(R.id.map_tv);
		/** 测试添加点线面的按钮 */
		test_geometry_btn = (Button) this.findViewById(R.id.test_geometry_btn);

		/** 设置登录人员管辖范围内的企业信息 */
		GridRelations();

		/** 显示主体网格坐标信息 */
		showMainGrid();

		ImageView imgDivider = null;
		/** 权限判断 */
		if (!DisplayUitl.getAuthority(QX_QYPLDW)) {
			/** 企业批量定位 */
			btn_company.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_company_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_RYDW)) {
			/** 查询同事 */
			btn_teammate.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_teammate_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_CLDW)) {
			/** 车辆定位 */
			btn_car.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_car_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_ZBCXDW)) {
			/** 周边查询 */
			btn_zbcx.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.around_find_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_TCKZ)) {
			/** 图层选项 */
			btn_layers.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_layerDisplay_divider);
			imgDivider.setVisibility(View.GONE);
		}
	}

	@SuppressWarnings("serial")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		rwxx = new RWXX();
		if (intent.getAction() != null && intent.getAction().equals("WRYDY")) {
			Drawable image = MapActivity.this.getBaseContext().getResources()
					.getDrawable(R.drawable.hbdt_gis_icon_loc);
			qymc = intent.getStringExtra("pname");
			qyid = intent.getStringExtra("qyid");
			double myjd = intent.getDoubleExtra("jd", 0.0);
			double mywd = intent.getDoubleExtra("wd", 0.0);
			String pname = intent.getStringExtra("pname");
			/* 关闭网格 */
			bMeshing = false;
			Location(myjd, mywd, pname, null, qyListener, image);

			// 使用postInvalidate可以直接在线程中更新界面
			map.postInvalidate();
		} else if(intent.getAction() != null && intent.getAction().equals("RYDW")) {
			Drawable image = MapActivity.this.getBaseContext().getResources()
					.getDrawable(R.drawable.hbdt_gis_icon_loc);
			
			double myjd = intent.getDoubleExtra("jd", 0.0);
			double mywd = intent.getDoubleExtra("wd", 0.0);
			String name = intent.getStringExtra("name");
			
			/* 关闭网格 */
			bMeshing = false;
			
			Location(myjd, mywd, name, null, null, image);
			
			// 使用postInvalidate可以直接在线程中更新界面
			map.postInvalidate();
		} else if(intent.getAction() != null && intent.getAction().equals("GJHF")) {
			ArrayList<HashMap<String, Double>> points = (ArrayList<HashMap<String, Double>>)intent.getSerializableExtra("points");
			String name = intent.getStringExtra("name");
			
			/* 关闭网格 */
			bMeshing = false;
			
			Point startPoint = new Point(points.get(0).get("jd"),points.get(0).get("wd"));
			
			SpatialReference sr4326 = SpatialReference.create(4326);
			Point point = (Point) GeometryEngine.project(startPoint, sr4326, map.getSpatialReference());
			map.centerAt(point, true);
			
			/** 此变量是用来保存线或者面的轨迹数据的 */
			MultiPath poly = new Polyline();
			
			/** 将第一个点存入poLy中,路径的起始位置 */
			poly.startPath(startPoint);
			/** 将移动的点放入poLy中,路径的结束为止,在最后一个点上添加一个线的片段 */
			for (int i = 1; i < points.size(); i++) {
				poly.lineTo(points.get(i).get("jd"), points.get(i).get("wd"));
			}
			
			tLayer.addGraphic(new Graphic(poly, new SimpleLineSymbol(
					Color.BLUE, 3)));
			
			map.zoomToScale(point, scale);
			
			// 使用postInvalidate可以直接在线程中更新界面
			map.postInvalidate();
		} else {
			loadFullMap();
		}
		/** 初始化地图添加的控件 */
		InitView();
		/** 开启GPS定位 */
		btn_location.setOnClickListener(new CurrentLocationListener());

		/** 企业批量定位单击事件 */
		btn_company.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/** 企业批量定位的方法 */
				enterprise_Batch_Locate();
			}
		});

		/** 查询同事单击事件 */
		btn_teammate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!map.isLoaded()) {
					Toast.makeText(MapActivity.this, "地图尚未加载无法定位！",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					Toast.makeText(MapActivity.this, "网络连接异常！",
							Toast.LENGTH_LONG).show();
					return;
				}
				/** 显示同事前先清除绘画图层 */
				tLayer.removeAll();
				if (callout != null) {
					/** 清除标注 */
					callout.animatedHide();
				}
				/** 弹出查找同事位置的方法 */
				checkTeamate();
			}
		});

		/** 车辆定位 */
		btn_car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 车辆定位的方法 */
				car_Location();
			}
		});

		/** 周边查询单击事件 */
		btn_zbcx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MapActivity.this, "请单击地图进行周边查询",
						Toast.LENGTH_SHORT).show();
				isClickZbcx = true;
			}
		});

		/** 图层控制监听事件 */
		btn_layers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** 图层控制方法 */
				layer_Control();
			}
		});

		/** 测试添加点线面的按钮的监听事件 */
		test_geometry_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});

		/** 判断地图是否网格化划分的标识的按钮监听事件 */
		btn_bMeshing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bMeshing) {
					btn_bMeshing.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.hbdt_gis_grid_off));
					bMeshing = false;
					Toast.makeText(MapActivity.this, "关闭缩放地图查看网格化区域显示的功能",
							Toast.LENGTH_LONG).show();
					/** 获得查询条件判断移除不符合的数据 */
				 
					tLayer.removeAll();
					if (callout != null) {
						/** 清除标注 */
						callout.animatedHide();
					}
				} else {
					btn_bMeshing.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.hbdt_gis_grid_open));
					bMeshing = true;
					Toast.makeText(MapActivity.this, "您可以进行缩放地图来查看网格化区域显示",
							Toast.LENGTH_LONG).show();
					/** 获得查询条件判断移除不符合的数据 */
					tLayer.removeAll();
					if (callout != null) {
						/** 清除标注 */
						callout.animatedHide();
					}
				}
			}
		});

		/** 登录用户所管辖的企业 */
		btn_grid_jur_ent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 设置登录人员管辖范围内的企业信息 */
				// GridRelations();

				/* 关闭网格 */
				bMeshing = false;
				tLayer.removeAll();
				if (callout != null) {
					/** 清除标注 */
					callout.animatedHide();
				}
			}
		});

		/** 经纬度查询 */
		btn_grid_coordinate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 经纬度查询方法 */
				coord_query();
			}
		});

		/** 网格化信息查询监听 */
		btn_grid_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 网格化信息查询方法 */
				grid_queryinfo();
			}
		});

		/** 地图长按弹出周边缓冲区查询对话框 */
		map.setOnLongPressListener(new OnLongPressListener() {

			@Override
			public void onLongPress(final float x, final float y) {
				if (!map.isLoaded()) {
					return;
				}
//				/** 将屏幕坐标转换成地图坐标 */
//				Point point = map.toMapPoint(x, y);
//				map_tv.setText("x-->" + point.getX() + "y-->" + point.getY()
//						+ "当前地图分辨率为：" + map.getResolution() + "当前地图的比例尺"
//						+ map.getScale() + "当前地图的范围" + map.getExtent());
//				/** 初始化地图的周边查询 */
//				initialAroundFind(x, y);
			}
		});

		/** 设置地图的缩放监听 */
		map.setOnZoomListener(new OnZoomListener() {

			/** 缩放之前自动调用的方法 */
			@Override
			public void preAction(float arg0, float arg1, double arg2) {

			}

			/** 缩放之后自动调用的方法 */
			@Override
			public void postAction(float arg0, float arg1, double arg2) {

				if (bMeshing) {
					/** 根据当前比例尺控制当前网格信息 */
					CurGridInfor();
				}
			}
		});

		/** 地图状态改变的监听 */
		map.setOnStatusChangedListener(new OnStatusChangedListener() {

			@Override
			public void onStatusChanged(Object arg0, STATUS arg1) {

			}
		});

		/** 地图单点事件 */
		map.setOnSingleTapListener(new OnSingleTapListener() {
			@Override
			public void onSingleTap(float x, float y) {

				/** 判断是否为周边查询模式 */
				if (isClickZbcx) {
					initialAroundFind(x, y);
					isClickZbcx = false;
					return;
				}

				/** 获取要素及其要素的相关属性 */
				final int[] graphics = tLayer.getGraphicIDs(x, y, 30);
				if (graphics.length > 0) {
					final Graphic graphic = tLayer.getGraphic(graphics[0]);
					/** 获取离点击位置最近的位图 */
					if (graphic.getAttributeValue("tag") != null) {
						/** 周边查询和批量定位点击响应 */
						Double jd = Double.valueOf(graphic.getAttributeValue(
								"jd").toString());
						Double wd = Double.valueOf(graphic.getAttributeValue(
								"wd").toString());
						final Point pt = new Point(jd, wd);
						qymc = tLayer.getGraphic(graphics[0])
								.getAttributeValue("tag").toString();
						qyid = tLayer.getGraphic(graphics[0])
								.getAttributeValue("tagid").toString();

						if (bPollution) {
							callout(pt, qymc, qyListener);
						} else {
							callout(pt, qymc, null);
						}
					}

					else if (graphic.getAttributeValue("surround_ent") != null) {

						/** 周边查询和批量定位点击响应 */
						Double jd = Double.valueOf(graphic.getAttributeValue(
								"jd").toString());
						Double wd = Double.valueOf(graphic.getAttributeValue(
								"wd").toString());
						final Point pt = new Point(jd, wd);
						qymc = tLayer.getGraphic(graphics[0])
								.getAttributeValue("surround_ent").toString();
						qyid = tLayer.getGraphic(graphics[0])
								.getAttributeValue("surround_ent_id")
								.toString();

						if (bPollution) {
							callout(pt, qymc, qyListener);
						} else {
							callout(pt, qymc, null);
						}

					}

					else if (graphic.getAttributeValue("quering") != null) {
						/** 初始化地图的周边查询 */
						initialAroundFind(x, y);
					}

					/** 获得网格坐标的点击事件 */
					else if (graphic.getAttributeValue("cStr") != null) {
						Log.i("info", "网格坐标点击事件");
						final String contextStr = (String) graphic
								.getAttributeValue("cStr");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point point = new Point(jd, wd);
						View.OnClickListener gridListener = new OnClickListener() {

							@SuppressWarnings("static-access")
							@Override
							public void onClick(View v) {
								Log.i("info", "ongridclick");

								LayoutInflater inflater = LayoutInflater
										.from(MapActivity.this);
								View view = inflater.inflate(
										R.layout.gis_guid_admin_info_dialog,
										null);

								/** 网格化信息标题 */
								// TextView gis_grid_infor_tv = (TextView) view
								// .findViewById(R.id.gis_grid_infor_tv);
								TextView gis_grid_infor_tv = (TextView) view
										.findViewById(R.id.gis_grid_infor_ad);
								EditText overall_responsibility_edt = (EditText) view
										.findViewById(R.id.overall_responsibility_edt);
								EditText overall_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.overall_responsibility_phone_edt);
								EditText responsible_agencies_edt = (EditText) view
										.findViewById(R.id.responsible_agencies_edt);
								EditText person_in_charge_edt = (EditText) view
										.findViewById(R.id.person_in_charge_edt);
								EditText person_in_charge_photo_edt = (EditText) view
										.findViewById(R.id.person_in_charge_photo_edt);
								EditText pollution_sources_edt = (EditText) view
										.findViewById(R.id.pollution_sources_edt);
								// EditText superscalar_pollution_sources_edt =
								// (EditText) view
								// .findViewById(R.id.superscalar_pollution_sources_edt);
								// EditText the_number_of_tasks_edt = (EditText)
								// view
								// .findViewById(R.id.the_number_of_tasks_edt);
								/** 主要职责 */
								EditText main_responsibility_edt = (EditText) view
										.findViewById(R.id.main_responsibility_edt);
								/** 网格编号 */
								EditText grid_code_edt = (EditText) view
										.findViewById(R.id.grid_code_edt);
								/** 网格内监管内容 */
								EditText supervise_content_edt = (EditText) view
										.findViewById(R.id.supervise_content_edt);
								/** 主要责任人的布局 */
								LinearLayout primary_responsibility_layout = (LinearLayout) view
										.findViewById(R.id.primary_responsibility_layout);
								/** 主要责任人 */
								EditText primary_responsibility_edt = (EditText) view
										.findViewById(R.id.primary_responsibility_edt);
								/** 主要责任人联系方式的布局 */
								LinearLayout primary_responsibility_telephone_layout = (LinearLayout) view
										.findViewById(R.id.primary_responsibility_telephone_layout);
								/** 主要责任人联系方式 */
								EditText primary_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.primary_responsibility_phone_edt);
								/** 直接责任人的布局 */
								LinearLayout gis_guid_direct_responsibility_layout = (LinearLayout) view
										.findViewById(R.id.gis_guid_direct_responsibility_layout);
								/** 直接责任人 */
								EditText direct_responsibility_edt = (EditText) view
										.findViewById(R.id.direct_responsibility_edt);
								/** 直接负责人联系电话的布局 */
								LinearLayout gis_guid_direct_responsibility_telephone_layout = (LinearLayout) view
										.findViewById(R.id.gis_guid_direct_responsibility_telephone_layout);
								/** 直接责任人联系方式 */
								EditText direct_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.direct_responsibility_phone_edt);

								Log.i("info", contextStr);
								if (contextStr != null
										&& !contextStr.equals("")) {

									/** 总负责人姓名 */
									String mainNameStr = "";
									/** 总负责人电话 */
									String mainteleStr = "";
									Log.i("info",
											"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridname like  '%"
													+ contextStr
													+ "%' and blamerole = '1'");
									/** 获得总负责人信息 */
									ArrayList<HashMap<String, Object>> mainData = sqliteutil
											.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId "
															+ "= PC_Users.UserId where gridcode=(select gridcode from Grid where GridName"
															+ " like  '%"
															+ contextStr
															+ "%' )and blamerole = '1'");
									if (mainData != null && mainData.size() > 0) {
										mainNameStr = mainData.get(0)
												.get("u_realname").toString();
										mainteleStr = mainData.get(0)
												.get("u_photo").toString();
									}

									if (gridData != null && gridData.size() > 0) {
										gridData.clear();
									}
									/** 初始化网格化数据 */
									gridData = new ArrayList<HashMap<String, Object>>();
									gridData = sqliteutil
											.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select gridcode from Grid where gridname like '%"
															+ contextStr + "%'");
									if (gridData != null && gridData.size() > 0) {
										/** 获得网格的id */
										gridId = gridData.get(0)
												.get("gridcode").toString();
									}

									if (gridId != null && !gridId.equals("")) {
										/** 获取主要负责人的名称 */
										String resDataStr = "";
										/** 获取主要负责人的联系方式 */
										String resDataPhoStr = "";
										/** 获取用户关联网格表中parentCode */
										ArrayList<HashMap<String, Object>> resData = sqliteutil
												.getInstance()
												.queryBySqlReturnArrayListHashMap(
														"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																+ gridId
																+ "' and blamerole = '2'");
										if (resData != null
												&& resData.size() > 0) {
											resDataStr = resData.get(0)
													.get("u_realname")
													.toString();
											resDataPhoStr = resData.get(0)
													.get("u_hometel").toString();
										}

										/** 获取主要责任人的名称 */
										String resPonDataStr = "";
										/** 获取主要责任人的联系方式 */
										String resPonDataPhoStr = "";
										/** 获取用户关联网格表中数据集合 */
										ArrayList<HashMap<String, Object>> resPonData = sqliteutil
												.getInstance()
												.queryBySqlReturnArrayListHashMap(
														"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																+ gridId
																+ "' and blamerole = '3'");
										if (resPonData != null
												&& resPonData.size() > 0) {
											resPonDataStr = resPonData.get(0)
													.get("u_realname")
													.toString();
											resPonDataPhoStr = resPonData
													.get(0).get("u_hometel")
													.toString();
										}

										/** 责任机构 */
										String agencyStr = "";
										if (gridId != null
												&& !gridId.equals("")) {
											String sql = "select ResponDepart  from Grid where GridCode = '"
													+ gridId + "'";
											agencyStr = sqliteutil
													.getInstance()
													.getDepidByUserid(sql);
										}
										/** 网格内监管内容 */
										String gridCodeContent = "";
										if (gridId != null && gridId != "") {
											String sql = "select SuperContext from Grid  where gridcode = '"
													+ gridId + "'";
											gridCodeContent = sqliteutil
													.getInstance()
													.getDepidByUserid(sql);
										}

										/** 主要职责 */
										String mainContext = "";
										if (gridId != null && gridId != "") {
											mainContext = sqliteutil
													.getInstance()
													.getDepidByUserid(
															"select GridUserMapping.SuperContext from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																	+ gridId
																	+ "' and blamerole = '3'");
										}

										/** 获取直接责任人的名称 */
										String directDataStr = "";
										/** 获取直接责任人的联系方式 */
										String directDataPhoStr = "";
										/** 获取用户关联网格表中数据集合 */
										ArrayList<HashMap<String, Object>> directData = sqliteutil
												.getInstance()
												.queryBySqlReturnArrayListHashMap(
														"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																+ gridId
																+ "' and blamerole = '4'");
										if (directData != null
												&& directData.size() > 0) {
											directDataStr = directData.get(0)
													.get("u_realname")
													.toString();
											directDataPhoStr = directData
													.get(0).get("u_hometel")
													.toString();
										}

										/** 获取登录用户的待执行任务数量 */
										int tasksize = rwxx
												.getTaskNumByUserIDandStatus(
														userId, "003");

										if (entData != null
												&& entData.size() > 0) {
											/** 获得污染源的数量 */
											String pollutionStr = String
													.valueOf(entData.size());
											pollution_sources_edt
													.setText(pollutionStr);// 污染源数量
										}

										if (!graphic.getAttributeValue("layer")
												.toString().equals("")
												&& graphic.getAttributeValue(
														"layer").toString() != null
												&& graphic
														.getAttributeValue(
																"layer")
														.toString().equals("2")) {

											/** 网格为省级时直接责任人不可见 */
											gis_guid_direct_responsibility_layout
													.setVisibility(View.GONE);
											gis_guid_direct_responsibility_telephone_layout
													.setVisibility(View.GONE);
											/** 网格为省级时主要责任人可见 */
											primary_responsibility_layout
													.setVisibility(View.VISIBLE);
											primary_responsibility_telephone_layout
													.setVisibility(View.VISIBLE);
										} else if (!graphic
												.getAttributeValue("layer")
												.toString().equals("")
												&& graphic.getAttributeValue(
														"layer").toString() != null
												&& graphic
														.getAttributeValue(
																"layer")
														.toString().equals("3")) {

											/** 网格为市级时直接责任人可见 */
											gis_guid_direct_responsibility_layout
													.setVisibility(View.VISIBLE);
											gis_guid_direct_responsibility_telephone_layout
													.setVisibility(View.VISIBLE);
											/** 网格为市级时主要责任人不可见 */
											primary_responsibility_telephone_layout
													.setVisibility(View.GONE);
											primary_responsibility_layout
													.setVisibility(View.GONE);
										}

										main_responsibility_edt
												.setText(mainContext);// 主要职责
										supervise_content_edt
												.setText(gridCodeContent);// 网格内监管内容
										grid_code_edt.setText(gridId); // 网格编号
										overall_responsibility_edt
												.setText(mainNameStr);// 总负责人
										overall_responsibility_phone_edt
												.setText(mainteleStr);// 总负责人联系电话
										responsible_agencies_edt
												.setText(agencyStr);// 责任机构
										person_in_charge_edt
												.setText(resDataStr);// 主要负责人
										person_in_charge_photo_edt
												.setText(resDataPhoStr);// 主要负责人联系方式
										// superscalar_pollution_sources_edt
										// .setText("");// 超标污染源数量
										// the_number_of_tasks_edt
										// .setText(tasksize + "");// 任务数量
										gis_grid_infor_tv.setText(graphic
												.getAttributeValue("cStr")
												.toString());// 获得当前网格信息

										primary_responsibility_edt
												.setText(resPonDataStr); // 主要责任人
										primary_responsibility_phone_edt
												.setText(resPonDataPhoStr); // 主要责任人联系方式
										direct_responsibility_edt
												.setText(directDataStr); // 直接责任人
										direct_responsibility_phone_edt
												.setText(directDataPhoStr); // 直接责任人联系方式
									}
								}

								dialog = new Dialog(MapActivity.this,
										R.style.FullScreenDialog);
								dialog.setCanceledOnTouchOutside(true);
								dialog.setContentView(view);
								dialog.show();

							}
						};

						callout(point, contextStr, gridListener);
					}
					/** 位图点击响应 */
					else if (graphic.getAttributeValue("strqymc") != null) {
						String content = (String) graphic
								.getAttributeValue("strqymc");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
					/** 车辆定位点击响应 */
					else if (graphic.getAttributeValue("CarNumber") != null) {
						String content = (String) graphic
								.getAttributeValue("CarNumber");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						View.OnClickListener mylistener = new OnClickListener() {

							@Override
							public void onClick(View v) {

								LayoutInflater li = LayoutInflater
										.from(MapActivity.this);

								View va = li.inflate(R.layout.map_car_details,
										null);
								EditText et_CarNumber = (EditText) va
										.findViewById(R.id.car_detalis_carname);
								EditText et_CarDutyPerson = (EditText) va
										.findViewById(R.id.car_detalis_car_mastername);
								EditText et_CarDesc = (EditText) va
										.findViewById(R.id.car_detalis_car_describe);
								EditText et_CarKind = (EditText) va
										.findViewById(R.id.car_detalis_car_kind);
								final Button et_CarDutyPersonTelephone = (Button) va
										.findViewById(R.id.car_detalis_car_masterphone);
								et_CarNumber.setText((CharSequence) graphic
										.getAttributeValue("CarNumber"));
								et_CarDutyPerson.setText((CharSequence) graphic
										.getAttributeValue("CarDutyPerson"));
								et_CarDesc.setText((CharSequence) graphic
										.getAttributeValue("CarDesc"));
								et_CarKind.setText((CharSequence) graphic
										.getAttributeValue("CarKind"));
								et_CarDutyPersonTelephone.setText((CharSequence) graphic
										.getAttributeValue("CarDutyPersonTelephone"));

								et_CarDutyPersonTelephone
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												tocall(et_CarDutyPersonTelephone
														.getText().toString());
											}
										});
								dialog = new Dialog(MapActivity.this,
										R.style.FullScreenDialog);
								dialog.setCanceledOnTouchOutside(true);
								dialog.setContentView(va);
								dialog.show();
							}
						};
						callout(pt, content, mylistener);
					} else if (graphic.getAttributeValue("U_RealName") != null) {// 人员定位
						String content = (String) graphic
								.getAttributeValue("U_RealName");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
				}

				if (graphics.length == 1) {// 点击一个点时予以应

					if (tLayer.getGraphic(graphics[0]).getAttributeValue(
							"pname") == null) {// 定位的点无点击事件

						if (tLayer.getGraphic(graphics[0]).getAttributeValue(
								"teamate") != null) {// 给同事打电话
							Toast.makeText(
									MapActivity.this,
									tLayer.getGraphic(graphics[0])
											.getAttributeValue("teamate")
											.toString(), Toast.LENGTH_LONG)
									.show();
							CallTeamate(tLayer.getGraphic(graphics[0])
									.getAttributeValue("teamate").toString());
						}
					}
				}
			}
		});
		tLayer.removeAll();
		if (callout != null) {
			/** 清除标注 */
			callout.animatedHide();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(map == null || map.isRecycled()){
			onCreate(null);
		}
	}

	/**
	 * 保持dialog不关闭，以便弹出提示信息
	 * 
	 * @param dialog
	 *            要作用的dialog
	 */

	private void keepDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭dialog
	 * 
	 * @param dialog
	 *            要作用的dialog
	 */
	private void distoryDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description:ListView适配器
	 */
	class ListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;

		ListViewAdapter(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView tv = new TextView(MapActivity.this);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setTextSize(28.0f);
			tv.setTextColor(Color.WHITE);
			tv.setText(data.get(position).get("cname").toString());
			return tv;
		}

	}

	/** 弹出查找同事位置的方法 */
	private void checkTeamate() {
		List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userID", Global.getGlobalInstance().getUserid());
		param.put("beforeMinuteCount", "120");
		param.put("deptID", "");
		String addpwd = "";
		try {
			addpwd = DESSecurity.encrypt("GetSubordinateLastPosition");
			param.put("token", addpwd);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		params.add(param);
		try {
			String strjson = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetSubordinateLastPosition", params,
					Global.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (strjson != null && !"".equals(strjson) && !strjson.equals("[]")) {
				org.json.JSONArray arr = new org.json.JSONArray(
						new org.json.JSONTokener(strjson));
				Drawable peopleDrawable = MapActivity.this.getResources()
						.getDrawable(R.drawable.teamatelocation);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject jsonObject = arr.getJSONObject(i);
					double x = Double.parseDouble(jsonObject
							.getString("longitude"));
					double y = Double.parseDouble(jsonObject
							.getString("latitude"));
					String name = jsonObject.getString("U_RealName");
					Log.i(TAG, "当前同事有-->" + name + ",经度->" + x + ",维度->" + y);
					if (name.equals("") || name == null) {
						continue;
					}
					if (x == 0 || y == 0) {
						Toast.makeText(MapActivity.this,
								"人员" + name + "返回数据错误，无法定位！",
								Toast.LENGTH_SHORT).show();
						continue;
					}
					Point point = new Point(x, y);
					Map<String, Object> mmap = new HashMap<String, Object>();

					mmap.put("U_RealName", name);
					mmap.put("jd", x);
					mmap.put("wd", y);
					mmap.put("CurrentTime", jsonObject.getString("CurrentTime"));
					Graphic gp = new Graphic(point, new PictureMarkerSymbol(
							peopleDrawable), mmap, null);
					callout(point, name, null);
					tLayer.addGraphic(gp);
					// map.zoomToScale(point, scale);

				}

			} else {
				Toast.makeText(MapActivity.this, "当前暂时无法获取人员位置信息！",
						Toast.LENGTH_SHORT).show();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[] ConvertDouble(HashMap<String, Object> map) {
		String lon = map.get("lon").toString();
		String lat = map.get("lat").toString();
		double jd = Double.parseDouble(lon);
		double wd = Double.parseDouble(lat);

		double data[] = new double[2];
		data[0] = jd;
		data[1] = wd;
		return data;
	}

	/**
	 * Description:弹出同事名称及拨号Dialog
	 * 
	 * @param tmatename
	 *            联系人姓名
	 * @return void
	 */
	private void CallTeamate(final String tmatename) {
		AlertDialog.Builder ab = new AlertDialog.Builder(MapActivity.this);
		ab.setTitle("呼叫");
		ab.setMessage("你确定要呼叫" + tmatename + "?");
		ab.setPositiveButton("拨号", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HashMap<String, Object> condition = new HashMap<String, Object>();
				condition.put("U_LoginName", tmatename);
				ArrayList<HashMap<String, Object>> data = sqLitedataprovider
						.getList("PC_Users", condition);
				if (data.size() > 0) {
					String phone = data.get(0).get("u_photo").toString();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + phone));
					startActivity(intent);
				} else
					Toast.makeText(MapActivity.this, "此用户无联系电话！",
							Toast.LENGTH_LONG).show();
			}
		});
		AlertDialog ad = ab.create();
		ad.show();
	}

	/**
	 * 初始化周边查询界面
	 * 
	 * @param x
	 * @param y
	 */
	public void initialAroundFind(final float x, final float y) {

		listData = DisplayUitl.getMapListXML("around");

		if (listData == null || listData.size() == 0) {
			Toast.makeText(MapActivity.this, "缺少周边配置信息，请检查配置文件", 1).show();
			return;
		}

		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View view = inflater.inflate(R.layout.gis_around_search, null);

		final TextView progress_text = (TextView) view
				.findViewById(R.id.souround_textView);
		seekBar = (SeekBar) view.findViewById(R.id.distance_seekBar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progress_text.setText(progress + "公里");
			}
		});

		ListView layerListview = (ListView) view
				.findViewById(R.id.gis_layers_ListView);
		layerListview.setAdapter(new ListViewAdapter(listData));
		layerListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				/* 关闭网格 */
				bMeshing = false;
				dialog.cancel();
				tLayer.removeAll();
				/** 清除图层标记 */
				if (callout != null) {
					/** 清除标注 */
					callout.animatedHide();
				}
				progressDialog = new ProgressDialog(MapActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("正在查找，请稍等...");
				progressDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Double length = Double.valueOf(progress_text.getText()
								.toString().replace("公里", ""));
						/** 地球半径KM */
						Double radius = 6371.004;
						/** 圆周率数值 */
						final Double PI = Math.PI;
						/** 屏幕坐标转换成地图坐标 */
						final Point pt = map.toMapPoint(new Point(x, y));
						Double JD = pt.getX();
						Double WD = pt.getY();
						/* <length/ ((Math.cos(WD) * radius * PI) / 180>是负数 */
						/** 经度最大值 */
						Double MaxJD = JD - length
								/ ((Math.cos(WD) * radius * PI) / 180);
						/** 经度最小值 */
						Double MinJD = JD + length
								/ ((Math.cos(WD) * radius * PI) / 180);
						Double MaxWD = WD + length / (radius * PI / 180);
						Double MinWD = WD - length / (radius * PI / 180);
						Log.i(TAG, "JD-->" + JD + "," + "WD-->" + WD + ","
								+ "MaxJD-->" + MaxJD + "," + "MinJD-->" + MinJD
								+ "," + "MaxWD-->" + MaxWD + "," + "MinWD-->"
								+ MinWD);
						ArrayList<HashMap<String, Object>> querydata = null;
						String qymcStr = "";
						String guidStr = "";
						String JdStr = "";
						String wdStr = "";
						String type = listData.get(position).get("type")
								.toString();

						if (type.equals("-1")) {
							/** 查询污染源 */
							bPollution = true;
							qymcStr = "qymc";
							guidStr = "guid";
							JdStr = "jd";
							wdStr = "wd";
							String sql = "select * from T_WRY_QYJBXX  where JD "
									+ "between '"
									+ MinJD
									+ "' and '"
									+ MaxJD
									+ "' and WD between '"
									+ MinWD
									+ "' and '"
									+ MaxWD + "'";
							querydata = sqliteutil
									.queryBySqlReturnArrayListHashMap(sql);
						} else {
							bPollution = false;
							qymcStr = "name";
							guidStr = "id";
							JdStr = "x";
							wdStr = "y";
							/** 获取周边数据 Environment表 */
							querydata = getAroundData(MinJD, MaxJD, MinWD,
									MaxWD, type);
						}
						if (querydata != null && querydata.size() > 0) {
							PictureMarkerSymbol symbol = null;
							Map<String, Object> mapData = new HashMap<String, Object>();
							for (HashMap<String, Object> map : querydata) {
								Double jd = Double.valueOf(map.get(JdStr)
										.toString());
								Double wd = Double.valueOf(map.get(wdStr)
										.toString());
								Point point = new Point(jd, wd);
								symbol = new PictureMarkerSymbol(
										MapActivity.this
												.getResources()
												.getDrawable(
														R.drawable.hbdt_gis_icon_loc));

								mapData.put("surround_ent", map.get(qymcStr)
										.toString());
								mapData.put("surround_ent_id", map.get(guidStr)
										.toString());
								mapData.put("jd", jd);
								mapData.put("wd", wd);

								Graphic graphicPollution = new Graphic(point,
										symbol, mapData, null);

								tLayer.addGraphic(graphicPollution);
							}
							Message msg = handler.obtainMessage();
							msg.what = REFRESHLAYER;
							msg.obj = pt;
							handler.sendMessage(msg);

						} else {
							Message msg = handler.obtainMessage();
							msg.what = NOCONTENT;
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		});
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 获取周边数据 Environment表
	 * 
	 * @param MinJD
	 *            最小经度
	 * @param MaxJD
	 *            最大经度
	 * @param MinWD
	 *            最小维度
	 * @param MaxWD
	 *            最大维度
	 * @param type
	 *            数据类型
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getAroundData(Double MinJD,
			Double MaxJD, Double MinWD, Double MaxWD, String type) {
		String sql = "select * from Environment  where X " + "between '"
				+ MinJD + "' and '" + MaxJD + "' and Y between '" + MinWD
				+ "' and '" + MaxWD + "' and type='" + type + "'";
		ArrayList<HashMap<String, Object>> querydata = sqliteutil
				.queryBySqlReturnArrayListHashMap(sql);
		return querydata;
	}

	/** 初始化地图范围 */
	@Override
	public void loadFullMap() {
		String mapext = null;
		try {
			mapext = DisplayUitl.getMapListXML("map").get(0).get("mapExtent")
					.toString();
		} catch (Exception e) {
			Log.i(TAG, "地图范围初始化失败");
			e.printStackTrace();
			return;
		}

		/** 将查询出的范围字符串拆分 */
		String[] extents = mapext.split(",");
		Log.i(TAG, "地图加载范围是" + extents[0] + "," + extents[1] + "," + extents[2]
				+ "," + extents[3]);

		/** 设置地图载入范围 */
		map.setExtent(new Envelope(Double.parseDouble(extents[0]), Double
				.parseDouble(extents[1]), Double.parseDouble(extents[2]),
				Double.parseDouble(extents[3])));
	}

	@Override
	protected void addMapLayer() {

		HashMap<String, Object> dataMap = null;
		try {
			dataMap = DisplayUitl.getMapListXML("map").get(0);
		} catch (Exception e) {
			Toast.makeText(this, "读取地图配置失败！", 1).show();
			e.printStackTrace();
			return;
		}
		if (null != dataMap) {
			if (dataMap.get("layertype").toString().equalsIgnoreCase("dynamic")) {// 连接服务端，获取图层
				ArcGISDynamicMapServiceLayer layer = new ArcGISDynamicMapServiceLayer(
						dataMap.get("url").toString());
				map.addLayer(layer);
			} else if (dataMap.get("layertype").toString()
					.equalsIgnoreCase("tiled")) {
				/** 本地离线图层 */
				ArcGISLocalTiledLayer layer = new ArcGISLocalTiledLayer(dataMap
						.get("url").toString());
				map.addLayer(layer);
			}
		} else {
			Toast.makeText(this, "地图缺乏配置文件！", 1).show();
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param tal
	 *            电话号码
	 */
	public void tocall(String tal) {
		if (tal.equals("")) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tal));
		/** 开始意图(及拨打电话) */
		startActivity(intent);
	}
}
