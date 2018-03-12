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
 * ArcGis��ͼ
 * 
 * @author
 * 
 * 
 *         *
 */
public class MapActivity extends ArcGisActivity {
	/************************** Ȩ��ֵ���� ************************************/
	private final String QX_QYPLDW = "vmob5A1B";// ��ҵ������λ
	private final String QX_RYDW = "vmob5A2B";// ��Ա��λ
	private final String QX_CLDW = "vmob5A3B";// ������λ
	private final String QX_ZBCXDW = "vmob5A5B";// �ܱ߲�ѯ��λ
	private final String QX_TCKZ = "vmob5A6B";// ͼ�����
	private final String QX_XCZF = "vmob6A";// �ֳ�ִ��Ȩ��
	private final String QX_ZXJC = "vmob3A";// ���߼��Ȩ��
	private final String QX_RWXQ = "vmob2A1B";// ��������Ȩ��
	private final String QX_XJRW = "vmob2A5B";// �½�����Ȩ��
	/*************************************************************************/
	String alet;
	String blet;
	ArrayList<String> strings = new ArrayList<String>();

	// ���в�ѯ
	ArrayList<HashMap<String, Object>> S1 = new ArrayList<HashMap<String, Object>>();
	// ���ز�ѯ
	ArrayList<HashMap<String, Object>> S2 = new ArrayList<HashMap<String, Object>>();
	/** ��¼��ǰ��ı�ʶ */
	private final String TAG = "MapActivity";
	/** ���ص�ͼ�� */
	protected final int LOADMAP = 0;
	/** ��λ�� */
	protected final int DINGWEING = 1;
	/** ˢ�»滭ͼ�� */
	protected final int REFRESHLAYER = 2;
	/** ��ǰ��Χ�������� */
	protected final int NOCONTENT = 3;
	/** ���ݿ������� */
	protected final int DBNOCONTENT = 5;
	/** ���ݲ�ѯ�ɹ� */
	private final int DATA_QUERY_SUCCESS = 6;
	/** ��������ľ�γ�ȶ�λ������ */
	private final int COORDINATE_QUERY_SUCCESS = 7;
	/** �������ſ��� */
	private final int GIS_ZOOM_CONTROL = 9;
	/** ��ѯ������ˮ�ࡢ��������ֽ�ĸ���ҵ��λ */
	private final int INDUSTRY_POSITION_SUCCESS = 10;
	private final SQLiteDataProvider sqLitedataprovider = SQLiteDataProvider
			.getInstance();
	/** ��ʼ��SqLite���� */
	private final SqliteUtil sqliteutil = SqliteUtil.getInstance();
	/** ������ͼ */
	private Intent intent;
	/** ��Ϣ���� */
	private Message msg;
	/** �ܱ߲�ѯ ���� bar */
	private SeekBar seekBar = null;
	/** �Զ���ĶԻ��� */
	private Dialog dialog;
	/** ���������� */
	private ProgressDialog progressDialog;
	/** ��ҵ������λ */
	private Button btn_company = null;
	/** ��ѯͬ�� */
	private Button btn_teammate = null;
	/** ������λ */
	private Button btn_car = null;
	/** ͼ��ѡ�� */
	private Button btn_layers = null;
	/** �ܱ߲�ѯ */
	private Button btn_zbcx = null;
	/** ������Ϣ��ѯ */
	private Button btn_grid_query = null;
	/** ��γ�Ȳ�ѯ */
	private Button btn_grid_coordinate = null;
	/** ��¼�û�����Ͻ����ҵ */
	private Button btn_grid_jur_ent = null;
	/** �жϵ�ͼ�Ƿ����񻯻��ֵı�ʶ�İ�ť */
	private Button btn_bMeshing = null;
	/** ��ҵ����ҳ�洫������ҵ��gUid */
	private String qyid;
	/** ��ҵ����ҳ�洫������ҵ������ */
	private String qymc;
	/** ��õ�¼���û���id */
	private String userId;
	/** ִ��ģ�����ݼ��� */
	private ArrayList<HashMap<String, Object>> zArrayData;
	/** �Ƿ������ֳ�ִ�������¼ */
	private Boolean ISADDTASK = false;
	/** �����������ݼ��� */
	private ArrayList<HashMap<String, Object>> gridListMainData;
	private ArrayList<HashMap<String, Object>> MainData;
	private HashMap<String, Object> gridMap;
	/** �������id���ݼ��� */
	private ArrayList<HashMap<String, Object>> gridData;
	/** ��ȡ�û�����������е��������� */
	public ArrayList<HashMap<String, Object>> gridCodeData;
	/** ��ȾԴ���� */
	public ArrayList<HashMap<String, Object>> entData;
	/** �����������ݼ��ϴ�ž�γ�� */
	private ArrayList<HashMap<String, Object>> mainGrid;
	/** ������ҵ���ƺ��������ز�ѯ��ҵ������Ϣ���� */
	private ArrayList<HashMap<String, Object>> company_data;
	/** �ܱ߲�ѯListView���� */
	private ArrayList<HashMap<String, Object>> listData;
	/** ��������id */
	private String gridId = null;
	/** �Զ���Ի�����ʾ�����ѯ */
	private AlertDialog alertDialog = null;
	/** ����ֵ����� */
	private EditText gis_coordinates_query_jd_edt;
	/** γ��ֵ����� */
	private EditText gis_coordinates_query_wd_edt;
	/** ��ҵ���Ʋ�ѯ����� */
	private EditText gis_company_name_edt;
	/** �������ز�ѯ������ */
	private Spinner gis_company_where_the_county_sp;
	/** Spinnerѡ������� */
	private String where_the_county;
	/** ��־�ܱ߲�ѯʱ������Ƿ�Ϊ��ѯ�ܱ��ص���ȾԴ���ص���ȾԴΪtrue */
	private boolean bPollution = false;
	/** �Ƿ����ܱ߲�ѯ */
	private boolean isClickZbcx = false;
	/** �жϵ�ͼ�Ƿ����񻯻��ֵı�ʶ */
	private boolean bMeshing = true;
	/** ������ӵ�����İ�ť */
	private Button test_geometry_btn;
	/** �����õ��б�����Դ */
	private final String[] geometryTypes = new String[] { "Point", "Polyline",
			"Polygon", "ȡ��������ռ��ѯ" };
	/** �����ô����������Ƶ����� */
	private MyOnTouchListener myOnTouchListener = null;
	/** �������ݳ�����ͼ��ʾ��ͼ���꾭γ�����ż���Χ����Ϣ */
	private TextView map_tv;
	/** �����õ����õ�ǰѡ�����һ�� */
	int selectedGeometryIndex = -1;

	private RWXX rwxx;

	String mainStr = null;
	/** ���ѡ�е������������ */
	String unitStr = null; /* ���񻯲�ѯ���е����� */

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DINGWEING:
				String action = intent.getAction();

				Toast.makeText(MapActivity.this, "���ڽ��ж�λ�����Ե�...", 1).show();
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
				/** ˢ�»滭ͼ�� */
				map.postInvalidate();
				/** ��ʼ����ͼ��Χ */
				loadFullMap();
				break;
			case NOCONTENT:
				if (progressDialog != null)
					progressDialog.cancel();
				Toast.makeText(MapActivity.this, "��ǰ��Χ���޷���Ҫ�������",
						Toast.LENGTH_LONG).show();
				break;
			case DBNOCONTENT:
				if (progressDialog != null)
					progressDialog.cancel();
				Toast.makeText(MapActivity.this, "���ݿ��޷���Ҫ������",
						Toast.LENGTH_LONG).show();
				break;
			case DATA_QUERY_SUCCESS:
				if (progressDialog != null)
					progressDialog.cancel();
				/** �鿴��ǰ��ͼ�е�����Ĺ���Ա������Ϣ */
				GridInfor();
				break;

			case COORDINATE_QUERY_SUCCESS:
				if (progressDialog != null)
					progressDialog.cancel();
				/** ˢ�»滭ͼ�� */
				map.postInvalidate();
				Point point = (Point) msg.obj;
				/** ��ͼ�Ŵ�ָ�������� */
				map.zoomToScale(point, scale);
				break;

			case GIS_ZOOM_CONTROL:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				/** ˢ�»滭ͼ�� */
				map.postInvalidate();
				break;

			case INDUSTRY_POSITION_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				/** ˢ�»滭ͼ�� */
				map.postInvalidate();
				Point pt = (Point) msg.obj;
				/** ��ͼ�Ŵ�ָ�������� */
				map.zoomToScale(pt, scale);
				break;
			}
		}
	};

	/** ��ͼ��λ�������ļ����¼� */
	View.OnClickListener qyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			initialdialog(qymc, qyid);
		}
	};

	/**
	 * �����λ��ĵ������ϵ����ı�ע��Ϣ,��������ҵdialog�Ի���
	 * 
	 * @param qymc
	 *            ��ҵ����
	 * @param qyid
	 *            ��ҵ��guiD
	 */
	private void initialdialog(final String qymc, final String qyid) {

		LayoutInflater li = LayoutInflater.from(MapActivity.this);
		View view = li.inflate(R.layout.map_tablerow, null);
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		/** һ��һ�� */
		TableRow taskwrycxxg_tb = (TableRow) view
				.findViewById(R.id.tbr_wrycxxg);
		/** �鿴���� */
		TableRow taskupload_tb = (TableRow) view.findViewById(R.id.tbr_wryxq);
		/** �ֳ�ִ�� */
		TableRow taskxczf_tb = (TableRow) view.findViewById(R.id.tbr_xczf);
		/** �½����� */
		/* TableRow taskxjrw_tb = (TableRow) view.findViewById(R.id.tbr_xjrw); */
		/** Ȩ���ж� */
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

		/** һ��һ�������¼� */
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

		/** ��ҵ������Ϣ ,�鿴���� */
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

		// /** ���߼�� */
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

		/** �ֳ�ִ�� */
		taskxczf_tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				EnforcementModel enforcementModel = new EnforcementModel();
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				/** ִ����ԱID���ݼ� */
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
					ISADDTASK = true;// ���һ�����񷵻�ˢ��ִ����¼
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
					LogUtil.i(TAG, "��ҵ����Ϊ---" + qyguid + ",������Ϊ--->" + RWBH);
					Global.getGlobalInstance().setMapXczfAddHistory(true);
					startActivity(intent);
				}

			}
		});

	}

	/**
	 * �ֳ�ִ����ť�����¼�
	 * 
	 * @param tid
	 *            ģ��id
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
				/** ������ҵ�����õ�ǰ���������GuId */
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
				Toast.makeText(MapActivity.this, "��������ʧ��!", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ��ȡ��¼�û�����Ͻ�������ϵ */
	@SuppressWarnings("static-access")
	public void GridRelations() {
		/** ��ȡ��¼���û���id */
		userId = Global.getGlobalInstance().getUserid();
		/** ʵ���� */
		gridCodeData = new ArrayList<HashMap<String, Object>>();
		/** ��ȡ�û�����������е��������� */
		gridCodeData = sqliteutil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select gridcode from GridUserMapping where userid = '"
								+ userId + "'");
		if (gridCodeData != null && gridCodeData.size() > 0) {
			/** ���ò�ѯ������ˮ�ࡢ��������ֽ�ĸ���ҵ�ķ��� */
			queryEnt(gridCodeData);
		}
	}

	/** ��ѯ������ˮ�ࡢ��������ֽ�ĸ���ҵ */
	public void queryEnt(final ArrayList<HashMap<String, Object>> gridCodeData) {

		/* �ر����� */
		bMeshing = false;

		/** ��ò�ѯ�����ж��Ƴ������ϵ����� ���� ��Ϊ�������ݵ����� */
		tLayer.removeAll();
		if (callout != null) {
			/** �����ע */
			callout.animatedHide();
		}
		progressDialog = new ProgressDialog(MapActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("���ڲ��ң����Ե�...");
		progressDialog.show();

		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			@Override
			public void run() {

				/** ����û�����Ͻ���������id */
				String gridCodeStr = gridCodeData.get(0).get("gridcode")
						.toString();
				/** ʵ���� */
				entData = new ArrayList<HashMap<String, Object>>();
				/** ��ȡ��ҵ����������е��������� */
				entData = sqliteutil
						.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select * from GridEntMapping INNER JOIN T_WRY_QYJBXX on GridEntMapping.entcode =  T_WRY_QYJBXX.guid where gridcode = '"
										+ gridCodeStr
										+ "' and JD is not null and WD is not null and JD is not '' and WD is not ''");

				if (entData != null && entData.size() > 0) {

					Point ptMap = null;
					try {
						/** ���� */
						Double qy_jd = null;
						/** γ�� */
						Double qy_wd = null;

						/** ������Ⱦ������Ӧģʽ */
						bPollution = true;
						/** ����ͼ�� */
						Drawable steelImage = MapActivity.this.getBaseContext()
								.getResources()
								.getDrawable(R.drawable.hbdt_gis_icon_steel);
						/** ˮ��ͼ�� */
						Drawable cementImage = MapActivity.this
								.getBaseContext().getResources()
								.getDrawable(R.drawable.hbdt_gis_icon_cement);
						/** ����ͼ�� */
						Drawable electricityImage = MapActivity.this
								.getBaseContext()
								.getResources()
								.getDrawable(
										R.drawable.hbdt_gis_icon_electricity);
						/** ��ֽͼ�� */
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
							/** ������ҵ�������ͼ�� */
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

	/** ��ʾ��������������Ϣ */
	public void showMainGrid() {
		/** �����������ݼ��ϴ�ž�γ�� */
		mainGrid = new ArrayList<HashMap<String, Object>>();
		/** �����������ľ�γ������ */
		mainGrid = gridMainData2();
		/** ������ */
		Point point = null;
		/** ���� */
		Double wg_jd = null;
		/** γ�� */
		Double wg_wd = null;
		/** ������Ⱦ������Ӧģʽ */
		bPollution = true;
		/** ����������������� */
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

	/** ���ݵ�ǰ�����߿��Ƶ�ǰ������Ϣ */
	public void CurGridInfor() {
		/** �����������ݼ��ϴ�ž�γ�� */
		mainGrid = new ArrayList<HashMap<String, Object>>();
		double scale = map.getScale();
		
		if (scale >= 4241921.422332997) {
			/** ��ò�ѯ�����ж��Ƴ������ϵ����� ���� ��Ϊ�������ݵ����� */
			tLayer.removeAll();
			if (callout != null) {
				/** �����ע */
				callout.animatedHide();
			}
			/** �����������ľ�γ������ */
			mainGrid = gridMainData2();
			/** ������ */
			Point point = null;
			/** ���� */
			Double wg_jd = null;
			/** γ�� */
			Double wg_wd = null;
			/** ������Ⱦ������Ӧģʽ */
			bPollution = true;
			/** ����������������� */
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
			/** ��ò�ѯ�����ж��Ƴ������ϵ����� ���� ��Ϊ�������ݵ����� */
			tLayer.removeAll();
			if (callout != null) {
				/** �����ע */
				callout.animatedHide();
			}
			/**  �޸�gridElementData()          ��õ�Ԫ����ľ�γ������ */
			mainGrid = gridMainData2();
		//	mainGrid = gridElementData();
			/** ������ */
			Point point = null;
			/** ���� */
			Double wg_jd = null;
			/** γ�� */
			Double wg_wd = null;
			/** ������Ⱦ������Ӧģʽ */
			bPollution = true;
			/** ����������������� */
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

	/** ������Ϣ��ѯ���� */
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
		/** ��ʼ���������� */
		gridData = new ArrayList<HashMap<String, Object>>();

		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setView(view);

		/** ȷ����ť */
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
						/** ���ѡ�е������������ */
						mainStr = select_city_sp.getSelectedItem().toString()
								.trim();
						/** ���ѡ�еĵ�Ԫ�������� */
						unitStr = select_county_sp.getSelectedItem().toString()
								.trim();
						if (!unitStr.equalsIgnoreCase("-��ѡ��Ԫ����-")) {
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
						/** ��������id */
						gridId = gridData.get(0).get("gridcode").toString();
						msg = handler.obtainMessage();
						msg.what = DATA_QUERY_SUCCESS;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
		/** ȡ����ť */
		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	/** �鿴��ǰ��ͼ�е�����Ĺ���Ա������Ϣ */
	public void GridInfor() {
		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View view = inflater.inflate(R.layout.gis_guid_admin_info_dialog, null);

		/** ������Ϣ���� */
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
		/** ��Ҫְ�� */
		EditText main_responsibility_edt = (EditText) view
				.findViewById(R.id.main_responsibility_edt);
		/** ������ */
		EditText grid_code_edt = (EditText) view
				.findViewById(R.id.grid_code_edt);
		/** �����ڼ������ */
		EditText supervise_content_edt = (EditText) view
				.findViewById(R.id.supervise_content_edt);
		/** ��Ҫ������ */
		EditText primary_responsibility_edt = (EditText) view
				.findViewById(R.id.primary_responsibility_edt);
		/** ��Ҫ��������ϵ��ʽ */
		EditText primary_responsibility_phone_edt = (EditText) view
				.findViewById(R.id.primary_responsibility_phone_edt);
		/** ֱ����������ߵ����� */
		TextView direct_reponsibility_text = (TextView) view
				.findViewById(R.id.gis_guid_direct_responsibility_text);
		/** ֱ�������� */
		EditText direct_responsibility_edt = (EditText) view
				.findViewById(R.id.direct_responsibility_edt);
		/** ֱ�Ӹ�������ϵ�绰�Ĳ��� */
		LinearLayout gis_guid_direct_responsibility_telephone_layout = (LinearLayout) view
				.findViewById(R.id.gis_guid_direct_responsibility_telephone_layout);
		/** ֱ����������ϵ��ʽ */
		EditText direct_responsibility_phone_edt = (EditText) view
				.findViewById(R.id.direct_responsibility_phone_edt);

		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		dialog = new Dialog(MapActivity.this, R.style.FullScreenDialog);
		builder.setView(view);

		/** �ܸ��������� */
		String mainNameStr = "";
		/** �ܸ����˵绰 */
		String mainteleStr = "";
		/** ����ܸ�������Ϣ */
		ArrayList<HashMap<String, Object>> mainData = sqliteutil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
								+ gridId + "' and blamerole = '1'");
		if (mainData != null && mainData.size() > 0) {
			mainNameStr = mainData.get(0).get("u_realname").toString();
			mainteleStr = mainData.get(0).get("u_photo").toString();
		}

		/** ��ʼ�������������ݼ��� */
		MainData = new ArrayList<HashMap<String, Object>>();
		MainData = gridMainData2();
		gridMap = new HashMap<String, Object>();
		/**�޸�  gridElementData  */
		company_data = gridMainData2();

		/** ��õ�ǰ������Ϣ */
		String contextStr = "";
		/** ��ǰ������������ */
		String layerStr = "";

		int len = company_data.size();
		int gridList = MainData.size();
		if (unitStr.equalsIgnoreCase("-��ѡ��Ԫ����-")) {
			for (int i = 0; i < gridList; i++) {
				HashMap<String, Object> wgmap = gridMainData2().get(i);
				if (mainStr.equals(wgmap.get("contextStr"))) {
					/** ��õ�ǰ������Ϣ */
					contextStr = wgmap.get("contextStr").toString();
					/** ��ǰ������������ */
					layerStr = wgmap.get("layer").toString();
				}
			}
		} else {
			for (int i = 0; i < len; i++) {
				HashMap<String, Object> wgmap = company_data.get(i);
				if (unitStr.equals(wgmap.get("contextStr"))) {
					/** ��õ�ǰ������Ϣ */
					contextStr = wgmap.get("contextStr").toString();
					/** ��ǰ������������ */
					layerStr = wgmap.get("layer").toString();
				}
			}
		}

		/** ���λ��� */
		String agencyStr = "";
		if (gridId != null && !gridId.equals("")) {
			String sql = "select ResponDepart  from Grid where GridCode = '"
					+ gridId + "'";
			agencyStr = sqliteutil.getInstance().getDepidByUserid(sql);
		}

		if (gridId != null && !gridId.equals("")) {
			/** ��ȡ��Ҫ�����˵����� */
			String resDataStr = "";
			/** ��ȡ��Ҫ�����˵���ϵ��ʽ */
			String resDataPhoStr = "";
			/** ��ȡ�û���������������ݼ��� */
			ArrayList<HashMap<String, Object>> resData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '2'");
			if (resData != null && resData.size() > 0) {
				resDataStr = resData.get(0).get("u_realname").toString();
				resDataPhoStr = resData.get(0).get("u_photo").toString();
			}

			/** ��ȡ��Ҫ�����˵����� */
			String resPonDataStr = "";
			/** ��ȡ��Ҫ�����˵���ϵ��ʽ */
			String resPonDataPhoStr = "";
			/** ��ȡ�û���������������ݼ��� */
			ArrayList<HashMap<String, Object>> resPonData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '3'");
			if (resPonData != null && resPonData.size() > 0) {
				resPonDataStr = resPonData.get(0).get("u_realname").toString();
				resPonDataPhoStr = resPonData.get(0).get("u_photo").toString();
			}

			/** �����ڼ������ */
			String gridCodeContent = "";
			if (gridId != null && gridId != "") {
				String sql = "select SuperContext from Grid  where gridcode = '"
						+ gridId + "'";
				gridCodeContent = sqliteutil.getInstance()
						.getDepidByUserid(sql);
			}

			/** ��Ҫְ�� */
			String mainContext = "";
			if (gridId != null && gridId != "") {
				String sql = "select GridUserMapping.SuperContext from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
						+ gridId + "' and blamerole = '3'";
				mainContext = sqliteutil.getInstance().getDepidByUserid(sql);
			}

			/** ��ȡֱ�������˵����� */
			String directDataStr = "";
			/** ��ȡֱ�������˵���ϵ��ʽ */
			String directDataPhoStr = "";
			/** ��ȡ�û���������������ݼ��� */
			ArrayList<HashMap<String, Object>> directData = sqliteutil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
									+ gridId + "' and blamerole = '4'");
			if (directData != null && directData.size() > 0) {
				directDataStr = directData.get(0).get("u_realname").toString();
				directDataPhoStr = directData.get(0).get("u_photo").toString();
			}

			/** ��ȡ��¼�û��Ĵ�ִ���������� */
			int tasksize = rwxx.getTaskNumByUserIDandStatus(userId, "003");
			if (entData != null && entData.size() > 0) {
				/** �����ȾԴ������ */
				String pollutionStr = String.valueOf(entData.size());
				pollution_sources_edt.setText(pollutionStr);// ��ȾԴ����
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

			main_responsibility_edt.setText(mainContext);// ��Ҫְ��
			supervise_content_edt.setText(gridCodeContent);// �����ڼ������
			Log.e("hello", "131415");
			grid_code_edt.setText(gridId); // ������
			overall_responsibility_edt.setText(mainNameStr);// �ܸ�����
			overall_responsibility_phone_edt.setText(mainteleStr);// �ܸ�������ϵ�绰
			responsible_agencies_edt.setText(agencyStr);// ���λ���
			person_in_charge_edt.setText(resDataStr);// ��Ҫ������
			person_in_charge_photo_edt.setText(resDataPhoStr);// ��Ҫ��������ϵ��ʽ
			// superscalar_pollution_sources_edt.setText("");// ������ȾԴ����
			// the_number_of_tasks_edt.setText(tasksize + "");// ��������
			gis_grid_infor_tv.setText(contextStr);// ��õ�ǰ������Ϣ

			primary_responsibility_edt.setText(resPonDataStr); // ��Ҫ������
			primary_responsibility_phone_edt.setText(resPonDataPhoStr); // ��Ҫ��������ϵ��ʽ
			direct_responsibility_edt.setText(directDataStr); // ֱ��������
			direct_responsibility_phone_edt.setText(directDataPhoStr); // ֱ����������ϵ��ʽ
		}

		dialog = builder.create();
		dialog.show();
	}

	 /** ������������������ */
	 public ArrayList<HashMap<String, Object>> gridMainData() {
	 gridListMainData = new ArrayList<HashMap<String, Object>>();
	 gridMap = new HashMap<String, Object>();
	 /** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.974083");
		gridMap.put("wd", "45.302475");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.970463");
		gridMap.put("wd", "45.765786");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ��ɽ������ */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.917987");
		gridMap.put("wd", "45.216586");
		gridMap.put("contextStr", "��ɽ��");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.706063");
		gridMap.put("wd", "45.098883");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ��ɽ������ */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.487007");
		gridMap.put("wd", "45.218369");
		gridMap.put("contextStr", "��ɽ��");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** �ε������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.852836");
		gridMap.put("wd", "45.355472");
		gridMap.put("contextStr", "�ε���");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ���Ӻ������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.00388");
		gridMap.put("wd", "45.334551");
		gridMap.put("contextStr", "���Ӻ���");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ��ɽ������ */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.86694");
		gridMap.put("wd", "45.540501");
		gridMap.put("contextStr", "��ɽ��");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.13925");
		gridMap.put("wd", "45.248081");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
	 return gridListMainData;
	
	 }
	 
	 /** ������������������ */
	 public ArrayList<HashMap<String, Object>> gridMainData2() {
	 gridListMainData = new ArrayList<HashMap<String, Object>>();
	 gridMap = new HashMap<String, Object>();
	 /** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "126.528395");
		gridMap.put("wd", "45.80216");
		gridMap.put("contextStr", "��������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		/** ���������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "129.6298");
		gridMap.put("wd", "44.5572");
		gridMap.put("contextStr", "ĵ������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		/** ��ɽ������ */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.3138");
		gridMap.put("wd", "47.3726");
		gridMap.put("contextStr", "�׸���");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);

		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.956581");
		gridMap.put("wd", "45.289463");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "125.0974");
		gridMap.put("wd", "46.5866");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "126.9748");
		gridMap.put("wd", "46.6348");
		gridMap.put("contextStr", "�绯��");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.360988");
		gridMap.put("wd", "46.80924");
		gridMap.put("contextStr", "��ľ˹��");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.1514");
		gridMap.put("wd", "46.6392");
		gridMap.put("contextStr", "˫Ѽɽ��");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.921631");
		gridMap.put("wd", "47.362822");
		gridMap.put("contextStr", "���������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "128.833649");
		gridMap.put("wd", "47.725634");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.929306");
		gridMap.put("wd", "45.784343");
		gridMap.put("contextStr", "��̨����");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
		
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "127.4938");
		gridMap.put("wd", "50.2446");
		gridMap.put("contextStr", "�ں���");
		gridMap.put("agency", "");
		gridMap.put("layer", "1");
		gridListMainData.add(gridMap);
		
	
		
		
	 return gridListMainData;
	
	 }
	 
	 

	/** ���õ�Ԫ���������� */
	public ArrayList<HashMap<String, Object>> gridElementData() {
		gridListMainData = new ArrayList<HashMap<String, Object>>();
		gridMap = new HashMap<String, Object>();
		/** ����������1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.957869");
		gridMap.put("wd", "45.300636");
		gridMap.put("contextStr", "����������1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.98313");
		gridMap.put("wd", "45.298391");
		gridMap.put("contextStr", "����������2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.993341");
		gridMap.put("wd", "45.309905");
		gridMap.put("contextStr", "����������3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������4���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.012127");
		gridMap.put("wd", "45.300734");
		gridMap.put("contextStr", "����������4");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������5���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.975907");
		gridMap.put("wd", "45.291828");
		gridMap.put("contextStr", "����������5");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ���������������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.971755");
		gridMap.put("wd", "45.280865");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ���������������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.95697");
		gridMap.put("wd", "45.321984");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** �������������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "133.278163");
		gridMap.put("wd", "45.962104");
		gridMap.put("contextStr", "����������");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "����������1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "����������2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "132.991992");
		gridMap.put("wd", "45.77582");
		gridMap.put("contextStr", "����������3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.975139");
		gridMap.put("wd", "45.295396");
		gridMap.put("contextStr", "��ɽ��1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.018951");
		gridMap.put("wd", "45.243572");
		gridMap.put("contextStr", "��ɽ��2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ������1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.58993");
		gridMap.put("wd", "45.142234");
		gridMap.put("contextStr", "������1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ������2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.801499");
		gridMap.put("wd", "45.147116");
		gridMap.put("contextStr", "������2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ������3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.704338");
		gridMap.put("wd", "45.044903");
		gridMap.put("contextStr", "������3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.441876");
		gridMap.put("wd", "45.228933");
		gridMap.put("contextStr", "��ɽ��1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.557721");
		gridMap.put("wd", "45.223854");
		gridMap.put("contextStr", "��ɽ��2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.557721");
		gridMap.put("wd", "45.223854");
		gridMap.put("contextStr", "��ɽ��3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** �ε���1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.862024");
		gridMap.put("wd", "45.357774");
		gridMap.put("contextStr", "�ε���1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** �ε���2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.807263");
		gridMap.put("wd", "45.366488");
		gridMap.put("contextStr", "�ε���2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** �ε���3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.862024");
		gridMap.put("wd", "45.357774");
		gridMap.put("contextStr", "�ε���3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ���Ӻ���1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.981182");
		gridMap.put("wd", "45.349844");
		gridMap.put("contextStr", "���Ӻ���1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ���Ӻ���2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.052184");
		gridMap.put("wd", "45.340114");
		gridMap.put("contextStr", "���Ӻ���2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ���ϳ������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.879867");
		gridMap.put("wd", "45.532343");
		gridMap.put("contextStr", "�ϳ���");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ�б��������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.872393");
		gridMap.put("wd", "45.559208");
		gridMap.put("contextStr", "������");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ��ɽ��ũ������ */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.816339");
		gridMap.put("wd", "45.497682");
		gridMap.put("contextStr", "ũ��");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** �������������� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.138261");
		gridMap.put("wd", "45.268796");
		gridMap.put("contextStr", "����������");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������1���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.210037");
		gridMap.put("wd", "45.174822");
		gridMap.put("contextStr", "����������1");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������2���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.330476");
		gridMap.put("wd", "45.297571");
		gridMap.put("contextStr", "����������2");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������3���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "130.982052");
		gridMap.put("wd", "45.50553");
		gridMap.put("contextStr", "����������3");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
		/** ����������4���� */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "131.32887");
		gridMap.put("wd", "45.300797");
		gridMap.put("contextStr", "����������4");
		gridMap.put("agency", "");
		gridMap.put("layer", "3");
		gridListMainData.add(gridMap);
	

		return gridListMainData;

	}

	
	
	
	/** ��γ�Ȳ�ѯ���� */
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

		/** ȷ����ť */
		gis_coordinates_query_sure_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						/** ��ȡ������еľ���ֵ */
						String jd = gis_coordinates_query_jd_edt.getText()
								.toString();
						/** ���������γ��ֵ */
						String wd = gis_coordinates_query_wd_edt.getText()
								.toString();

						if (jd != null && !jd.equals("") && wd != null
								&& !wd.equals("")) {
							coord_query(jd, wd);
						} else {
							Toast.makeText(MapActivity.this, "��γ�Ȳ���Ϊ��",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		/** ȡ����ť */
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

	/** ���뾭γ�ȶ�λ��ͼ���� */
	public void coord_query(String jd, String wd) {

		/* �ر����� */
		bMeshing = false;

		/** ��ò�ѯ�����ж��Ƴ������ϵ����� */
		tLayer.removeAll();
		if (callout != null) {
			/** �����ע */
			callout.animatedHide();
		}
		/** ȡ���Ի��� */
		dialog.cancel();
		/** ������ */
		Point point = null;
		/** ���� */
		Double wg_jd = null;
		/** γ�� */
		Double wg_wd = null;
		/** ������Ⱦ������Ӧģʽ */
		bPollution = true;
		/** ����������������� */
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
		coorMap.put("quering", "�Ƿ��ѯ�ܱ���Ϣ");

		Graphic graphic = new Graphic(point, imageMarkerSymbol, coorMap,
				new InfoTemplate("", ""));
		tLayer.addGraphic(graphic);

		msg = handler.obtainMessage();
		msg.what = COORDINATE_QUERY_SUCCESS;
		msg.obj = point;
		handler.sendMessage(msg);
	}

	/** ��ҵ������λ���� */
	private void enterprise_Batch_Locate() {

		final DialogFactory dialogfactory = new DialogFactory(MapActivity.this,
				"��ҵ��λ", "map_JBXX", null);
		AlertDialog.Builder builder = dialogfactory.showResearchDialog();
		builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/* �ر����� */
				bMeshing = false;
				/** ��ò�ѯ�����ж��Ƴ������ϵ����� */
				tLayer.removeAll();
				if (callout != null) {
					/** �����ע */
					callout.animatedHide();
				}
				distoryDialog(dialog);
				progressDialog = new ProgressDialog(MapActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("���ڲ��ң����Ե�...");
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
							
							/** �������� */
							String JiXiCode = "230000000";
							/** ��ѯ�������������ݼ��� */
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
											.toString() + "��";
								}
								String[] regions = regionStrs.split("��");

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
							/** ������Ⱦ������Ӧģʽ */
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

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		((Dialog) alertDialog).setCanceledOnTouchOutside(true);
		alertDialog.show();

	}

	/** ������λ�ķ��� */
	private void car_Location() {
		/* �ر����� */
		bMeshing = false;

		tLayer.removeAll();
		if (callout != null) {
			/** �����ע */
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
						/** ��γ��Ϊ0�޷���λ������ѭ�� */
						if (x == 0 || y == 0) {
							Toast.makeText(MapActivity.this,
									"����" + CarNumber + "�������ݴ����޷���λ��",
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
				Toast.makeText(MapActivity.this, "��ǰ�����޷���λ��",
						Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ͼ����Ʒ��� */
	private void layer_Control() {
		if (callout != null) {
			/** �����ע */
			callout.animatedHide();
		}
		LayoutInflater ll = LayoutInflater.from(MapActivity.this);
		View view = ll.inflate(R.layout.gis_mapandlayer_choose, null);
		final Dialog layerdialog = new Dialog(MapActivity.this,
				R.style.gis_maplayer_dialog);
		/** ͨ������xmL�ļ������Ҫ��ѯ��ͼ������ ���� ͼ�� */
		LinearLayout showcontronl = (LinearLayout) view
				.findViewById(R.id.showcontronl);
		/** ��ȡgis_config */
		ArrayList<HashMap<String, Object>> list = DisplayUitl.getMapLayerData();
		if (list == null || list.size() == 0) {
			Toast.makeText(MapActivity.this, "��ͼ������", 1).show();
			return;
		}
		int checkID = 0;
		final String[] checkCondition = new String[list.size()];
		/** �������Ͷ�ͼƬ��ӳ�� */
		final HashMap<String, String> pictureMap = new HashMap<String, String>();

		for (final HashMap<String, Object> dataMap : list) {
			if (dataMap.get("isonline").toString().equals("0")) {// ���߲�ѯ���ݿ����ͼ��
				pictureMap.put(dataMap.get("type").toString(),
						dataMap.get("icon").toString());
				RelativeLayout Relative = new RelativeLayout(MapActivity.this);
				Relative.setPadding(10, 0, 0, 20);// ������߾�10dp,�ұ߾�20
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				int syncAllId = 224522;
				int syncId = 224523;
				/** ��̬����ͼ�� */
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				layoutParams.setMargins(20, 0, 0, 0);
				ImageView image = new ImageView(MapActivity.this);
				image.setId(syncAllId);
				/** ���ͼƬ��Դid */
				int id = MapActivity.this.getResources().getIdentifier(
						dataMap.get("icon").toString(), "drawable",
						MapActivity.this.getPackageName());
				Drawable img = getResources().getDrawable(id);
				image.setBackgroundDrawable(img);
				Relative.addView(image, layoutParams);

				/** ��̬������Ҫ��ѯ������ */
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

				/** ��̬����checkBox */
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
		confirm.setText("ȷ��");
		Drawable dSyncAll = getResources().getDrawable(
				R.drawable.gis_map_dialog_btn);
		confirm.setBackgroundDrawable(dSyncAll);
		confirm.setTextColor(Color.WHITE);
		confirm.setTextSize(18);
		layoutParam.setMargins(30, 0, 0, 0);
		line.addView(confirm, layoutParam);

		Button cancel = new Button(MapActivity.this);
		cancel.setText("ȡ��");
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

		/** ȷ����ѡ��ͼ���ɾ����ǰͼ�㶨λ���Ҳ�ѯ��ѡ���ͼ����ҵ */
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** ��̬ƴsqL����ѯ��Ҫ��ʾ��ͼ��λ����ҵ */

				/* �ر����� */
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
					/** ��������Ϊ�� */
					return;
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				sb.append(")");

				String strSql = "select * from Environment where type in "
						+ sb.toString();
				Log.i(TAG, "ͼ����Ƶ�sql���Ϊ-->" + strSql);
				ArrayList<HashMap<String, Object>> mylist = sqliteutil
						.queryBySqlReturnArrayListHashMap(strSql);
				if (mylist == null || mylist.size() == 0) {
					Toast.makeText(MapActivity.this, "���ݿ��޴�ͼ������",
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
						Log.i(TAG, "���쳣�ľ�γ��-->" + mmap.get("x").toString()
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
				/** geometryTypesΪ��������������Դ */
				.setItems(geometryTypes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						/** �����õĵ����津�������¼� */
						myOnTouchListener = new MyOnTouchListener(
								MapActivity.this, map);
						/** �����õĵ����津�������¼� */
						map.setOnTouchListener(myOnTouchListener);

						/** �Ƴ������ϵ����� */
						tLayer.removeAll();
						if (callout != null) {
							/** �����ע */
							callout.animatedHide();
						}
						/** Toast��ʾ��Ϣ */
						Toast toast = Toast.makeText(getApplicationContext(),
								"", Toast.LENGTH_LONG);
						/** ��ʾ��Ϣ��ʾ��λ�� */
						toast.setGravity(Gravity.BOTTOM, 0, 0);

						/** ��ȡ�����е��ַ��� */
						String geomType = geometryTypes[which];
						Log.i(TAG, geomType);
						selectedGeometryIndex = which;

						if (geomType.equalsIgnoreCase("Polygon")) {
							myOnTouchListener.setType("POLYGON");
							toast.setText("�϶���ָ����Ļ�ϻ�һ�������.\n�ɿ���ָ,ֹͣ����.");
						} else if (geomType.equalsIgnoreCase("Polyline")) {
							myOnTouchListener.setType("POLYLINE");
							toast.setText("�϶���ָ����Ļ�ϻ�һ������. \n�ɿ���ָ,ֹͣ����.");
						} else if (geomType.equalsIgnoreCase("Point")) {
							myOnTouchListener.setType("POINT");
							toast.setText("���һ������Ļ�ϻ�һ����.");
						} else if (geomType.equalsIgnoreCase("ȡ��������ռ��ѯ")) {
							toast.setText("ȡ��������ռ��ѯ.");
						}

						toast.show();
					}
				}).create();
	}

	class MyOnTouchListener extends MapOnTouchListener {
		/** �˱��������������߻�����Ĺ켣���ݵ� */
		MultiPath poly;
		/** �ж���ִ�е����ֻ�ͼ��ʽ��������Ϊ������ֵ��point��polgyLine��polGyon */
		String type = "";
		/** �滭��ʱ���ڴ����ı��� */
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

		/** �����ǻ��Ƶ�ʱִ�еĴ����� */
		@Override
		public boolean onSingleTap(MotionEvent e) {
			if (type.length() > 1 && type.equalsIgnoreCase("POINT")) {
				/** ���ɵ�ͼ�Σ���������Ӧ����ʽ */
				Graphic graphic = new Graphic(map.toMapPoint(new Point(
						e.getX(), e.getY())), new SimpleMarkerSymbol(Color.RED,
						25, STYLE.CIRCLE));
				/** ����Ҫ����ӵ�ͼ���� */
				tLayer.addGraphic(graphic);
				return true;
			} else if (type.equals("")) {
				/** �ж��Ƿ�Ϊ�ܱ߲�ѯģʽ */
				if (isClickZbcx) {
					initialAroundFind(e.getX(), e.getY());
					isClickZbcx = false;
					return true;
				}

				/** ��ȡҪ�ؼ���Ҫ�ص�������� */
				final int[] graphics = tLayer.getGraphicIDs(e.getX(), e.getY(),
						30);
				if (graphics.length > 0) {
					final Graphic graphic = tLayer.getGraphic(graphics[0]);
					/** ��ȡ����λ�������λͼ */
					if (graphic.getAttributeValue("tag") != null) {
						/** �ܱ߲�ѯ��������λ�����Ӧ */
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

						/** �ܱ߲�ѯ��������λ�����Ӧ */
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
						/** ��ʼ����ͼ���ܱ߲�ѯ */
						initialAroundFind(e.getX(), e.getY());
					}

					/** �����������ĵ���¼� */
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
								/** ������Ϣ���� */
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
									/** �ܸ��������� */
									String mainNameStr = "";
									/** �ܸ����˵绰 */
									String mainteleStr = "";
									/** ����ܸ�������Ϣ */
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

									/** ��ʼ���������ݼ��� */
									gridData = new ArrayList<HashMap<String, Object>>();
									gridData = sqliteutil.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select gridcode from Grid where gridname = '"
															+ contextStr + "'");

									if (gridData != null && gridData.size() > 0) {
										/** ��������id */
										gridId = gridData.get(0)
												.get("gridcode").toString();
									}
									/** ���λ��� */
									String agencyStr = "";
									if (gridId != null && !gridId.equals("")) {
										String sql = "select ResponDepart  from Grid where GridCode = '"
												+ gridId + "'";
										agencyStr = sqliteutil.getInstance()
												.getDepidByUserid(sql);
									}
									if (gridId != null && !gridId.equals("")) {
										/** ��ȡ��Ҫ�����˵����� */
										String resDataStr = "";
										/** ��ȡ��Ҫ�����˵���ϵ��ʽ */
										String resDataPhoStr = "";
										/** ��ȡ�û������������parentCode */
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

										/** ��ȡ��¼�û��Ĵ�ִ���������� */
										int tasksize = rwxx
												.getTaskNumByUserIDandStatus(
														userId, "003");

										overall_responsibility_edt
												.setText(mainNameStr);// �ܸ�����
										overall_responsibility_phone_edt
												.setText(mainteleStr);// �ܸ�������ϵ�绰
										responsible_agencies_edt
												.setText(agencyStr);// ���λ���
										person_in_charge_edt
												.setText(resDataStr);// ��Ҫ������
										person_in_charge_photo_edt
												.setText(resDataPhoStr);// ��Ҫ��������ϵ��ʽ

										if (entData != null
												&& entData.size() > 0) {
											/** �����ȾԴ������ */
											String pollutionStr = String
													.valueOf(entData.size());
											pollution_sources_edt
													.setText(pollutionStr);// ��ȾԴ����
										}
										//
										// superscalar_pollution_sources_edt
										// .setText("");// ������ȾԴ����
										// the_number_of_tasks_edt
										// .setText(tasksize + "");// ��������
										gis_grid_infor_tv.setText(graphic
												.getAttributeValue("cStr")
												.toString());// ��õ�ǰ������Ϣ
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
					/** λͼ�����Ӧ */
					else if (graphic.getAttributeValue("strqymc") != null) {
						String content = (String) graphic
								.getAttributeValue("strqymc");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
					/** ������λ�����Ӧ */
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
					} else if (graphic.getAttributeValue("U_RealName") != null) {// ��Ա��λ
						String content = (String) graphic
								.getAttributeValue("U_RealName");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
				}

				if (graphics.length == 1) {// ���һ����ʱ����Ӧ

					if (tLayer.getGraphic(graphics[0]).getAttributeValue(
							"pname") == null) {// ��λ�ĵ��޵���¼�

						if (tLayer.getGraphic(graphics[0]).getAttributeValue(
								"teamate") != null) {// ��ͬ�´�绰
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

		/** �������߻�����ʱ���õĺ��� */
		@Override
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			if (type.length() > 1
					&& (type.equalsIgnoreCase("POLYLINE") || type
							.equalsIgnoreCase("POLYGON"))) {
				/** �õ��ƶ���ĵ� */
				Point point = map.toMapPoint(to.getX(), to.getY());

				/** �ж�startPoint�Ƿ�Ϊ��,���Ϊ�գ���startPoint��ֵ */
				if (startPoint == null) {
					tLayer.removeAll();
					poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline()
							: new Polygon();
					/** �õ���ʼ�ƶ��ĵ� ��Ҫ�ص���λ����Ϣ */
					startPoint = map.toMapPoint(from.getX(), from.getY());
					/** ����һ�������poLy��,·������ʼλ�� */
					poly.startPath((float) startPoint.getX(),
							(float) startPoint.getY());
					Graphic graphic = new Graphic(startPoint,
							new SimpleLineSymbol(Color.RED, 5));
					tLayer.addGraphic(graphic);
				}
				/** ���ƶ��ĵ����poLy��,·���Ľ���Ϊֹ,�����һ���������һ���ߵ�Ƭ�� */
				poly.lineTo((float) point.getX(), (float) point.getY());

				return true;
			}
			return super.onDragPointerMove(from, to);
		}

		/** ���������߻��棬�뿪��Ļʱ���õĺ��� */
		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if (type.length() > 1
					&& (type.equalsIgnoreCase("POLYLINE") || type
							.equalsIgnoreCase("POLYGON"))) {
				/** �жϵ����Ƶ�����ʱ������ʼ�����뵽poLy���γɱպ� */
				if (type.equalsIgnoreCase("POLYGON")) {
					poly.lineTo((float) startPoint.getX(),
							(float) startPoint.getY());
					tLayer.removeAll();
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.GRAY);
					simpleFillSymbol.setAlpha(70);
					tLayer.addGraphic(new Graphic(poly, simpleFillSymbol));
				}
				/** ���poLyͼ����ӵ�ͼ����ȥ */
				tLayer.addGraphic(new Graphic(poly, new SimpleLineSymbol(
						Color.BLUE, 5)));

				startPoint = null;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}
	}

	/** ��õ�ǰλ�õ����� */
	class CurrentLocationListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// ����GPS����
			openGPSSetting();
		}
	}

	/** ����GPS���� */
	private void openGPSSetting() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS�ѿ���,���ڻ�ȡλ����Ϣ...", Toast.LENGTH_SHORT)
					.show();
			// ��ȡ��ǰλ����Ϣ
			getCurrentLocation();
			return;
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MapActivity.this);
			builder.setTitle("GPS��λ����");
			builder.setMessage("�Ƿ���GPS��λ");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_SECURITY_SETTINGS);
							// ��Ϊ������ɺ󷵻ص���ȡ����
							startActivityForResult(intent, 0);
						}
					});
			builder.setNegativeButton("ȡ��",
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
			// ��ȡ��ǰλ����Ϣ
			getCurrentLocation();
		}
	}

	/** ��ȡ��ǰGPS��λ��λ����Ϣ */
	public void getCurrentLocation() {
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i(TAG, "location-->" + location);
		if (location != null) {
			// ��GPS LocationProvider����ʱ,����λ��
			updateMapView(location);
			// ����λ����Ϣ
			updateToNewLocation(location);
			// ע��״̬��Ϣ�ص�
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
			Log.i(TAG, "provider-->״̬�ı�");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(TAG, "provider-->����");
			if (location != null) {
				// ��GPS LocationProvider����ʱ,����λ��
				updateMapView(location);
				// ����λ����Ϣ
				updateToNewLocation(location);
				// ��������״̬
				locationManager.addGpsStatusListener(statusListener);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(TAG, "provider-->������");
		}

		@Override
		public void onLocationChanged(Location location) {
			// ��GPS��λ��Ϣ�����ı�ʱ,����λ��
			updateMapView(location);
			// ����λ����Ϣ
			updateToNewLocation(location);
			// ��������״̬
			locationManager.addGpsStatusListener(statusListener);
		}
	};

	/** ����Location�����µ�ͼ */
	public void updateMapView(Location location) {
		String jd = String.valueOf(location.getLongitude());
		String wd = String.valueOf(location.getLatitude());
		/** ������ */
		Point point = null;
		/** ���� */
		Double wg_jd = null;
		/** γ�� */
		Double wg_wd = null;
		/** ������Ⱦ������Ӧģʽ */
		bPollution = true;
		/** ����������������� */
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
		coorMap.put("quering", "��������λ��");

		Graphic graphic = new Graphic(point, imageMarkerSymbol, coorMap,
				new InfoTemplate("", ""));
		tLayer.addGraphic(graphic);

		msg = handler.obtainMessage();
		msg.what = COORDINATE_QUERY_SUCCESS;
		msg.obj = point;
		handler.sendMessage(msg);
	}

	/** �����ź� */
	private final List<GpsSatellite> NumSatelliteList = new ArrayList<GpsSatellite>();

	/** ����״̬������ */
	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		@Override
		public void onGpsStatusChanged(int event) { // GPS״̬�仯ʱ�Ļص�����������
			GpsStatus status = locationManager.getGpsStatus(null); // ȡ��ǰ״̬
			updateGpsStatus(event, status);
		}
	};

	/** ��������״̬ */
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

	/** ����λ����Ϣ */
	private void updateToNewLocation(Location location) {
		Time time = new Time();
		time.setToNow(); // ȡ��ϵͳʱ��
		int year = time.year;// �����
		int month = time.month + 1;// �����
		int date = time.monthDay;// �����
		int hour = time.hour; // 24Сʱ��,���Сʱ
		int minute = time.minute;// ��÷���
		int second = time.second;// �����
		TextView map_gps_info = (TextView) this.findViewById(R.id.map_gps_info);
		if (location != null) {
			double latitude = location.getLatitude();// γ��
			double longitude = location.getLongitude();// ����
			double altitude = location.getAltitude(); // ����
			double speed = location.getSpeed();// �ٶ�
			double bear = location.getBearing();// ����
			map_gps_info.setText("�������Ǹ���:" + NumSatelliteList.size() + "/n"
					+ "γ��:" + latitude + "/n" + "����:" + longitude + "/n"
					+ "����:" + altitude + "/n" + "�ٶ�:" + speed + "/n" + "����:"
					+ bear + "/n" + "ʱ��:" + year + "��" + month + "��" + date
					+ "��" + hour + ":" + minute + ":" + second);
		} else {
			map_gps_info.setText("�޷���ȡ������Ϣ");
		}
	}

	/** ��ȡ��ͼ�Ļ�����Ϣ */
	public void BasicInfor() {
		/** �����Ƿ������ͼͨ��pinch��ʽ��ת */
		map.setAllowRotationByPinch(true);
		/** ���õ�ͼ����ת�Ƕ� */
		map.setRotationAngle(15.0);
		Log.i(TAG, "��ȡ��ͼ�����ĵ�--->" + map.getCenter());
		Log.i(TAG, "��ȡ��ͼ��С�������--->" + map.getExtent());
		Log.i(TAG, "��ȡ��ͼ�ı߽�--->" + map.getMapBoundaryExtent());
		Log.i(TAG, "��ȡ��ͼ���ֱ���--->" + map.getMaxResolution());
		Log.i(TAG, "��ȡ��ͼ��С�ֱ���--->" + map.getMinResolution());
		Log.i(TAG, "��ȡ��ǰ��ͼ�ֱ���--->" + map.getResolution());
		Log.i(TAG, "��ȡ��ǰ��ͼ������--->" + map.getScale());
	}

	/** ��ʼ����ͼ��ӵĿؼ� */
	@Override
	protected void InitView() {
		/** ��ȡ��ͼ�Ļ�����Ϣ������ */
		// BasicInfor();
		/** ��ҵ������λ */
		btn_company = (Button) this.findViewById(R.id.btn_company);
		/** ��Ա��λ */
		btn_teammate = (Button) this.findViewById(R.id.btn_teammate);
		/** ������λ */
		btn_car = (Button) this.findViewById(R.id.btn_car);
		/** ��ͼͼ��ѡ�� */
		btn_layers = (Button) this.findViewById(R.id.btn_layerDisplay);
		/** �ܱ߲�ѯ */
		btn_zbcx = (Button) this.findViewById(R.id.around_find);
		/** ��¼�û�����Ͻ����ҵ */
		btn_grid_jur_ent = (Button) this.findViewById(R.id.btn_grid_jur_ent);
		/** ������Ϣ��ѯ */
		btn_grid_query = (Button) this.findViewById(R.id.btn_grid_query);
		/** �жϵ�ͼ�Ƿ����񻯻��ֵı�ʶ�İ�ť */
		btn_bMeshing = (Button) this.findViewById(R.id.btn_bMeshing);

		/** ��γ�Ȳ�ѯ */
		btn_grid_coordinate = (Button) this
				.findViewById(R.id.btn_grid_coordinate);
		/** ������Ϣ */
		map_tv = (TextView) this.findViewById(R.id.map_tv);
		/** ������ӵ�����İ�ť */
		test_geometry_btn = (Button) this.findViewById(R.id.test_geometry_btn);

		/** ���õ�¼��Ա��Ͻ��Χ�ڵ���ҵ��Ϣ */
		GridRelations();

		/** ��ʾ��������������Ϣ */
		showMainGrid();

		ImageView imgDivider = null;
		/** Ȩ���ж� */
		if (!DisplayUitl.getAuthority(QX_QYPLDW)) {
			/** ��ҵ������λ */
			btn_company.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_company_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_RYDW)) {
			/** ��ѯͬ�� */
			btn_teammate.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_teammate_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_CLDW)) {
			/** ������λ */
			btn_car.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.btn_car_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_ZBCXDW)) {
			/** �ܱ߲�ѯ */
			btn_zbcx.setVisibility(View.GONE);
			imgDivider = (ImageView) findViewById(R.id.around_find_divider);
			imgDivider.setVisibility(View.GONE);
		}
		if (!DisplayUitl.getAuthority(QX_TCKZ)) {
			/** ͼ��ѡ�� */
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
			/* �ر����� */
			bMeshing = false;
			Location(myjd, mywd, pname, null, qyListener, image);

			// ʹ��postInvalidate����ֱ�����߳��и��½���
			map.postInvalidate();
		} else if(intent.getAction() != null && intent.getAction().equals("RYDW")) {
			Drawable image = MapActivity.this.getBaseContext().getResources()
					.getDrawable(R.drawable.hbdt_gis_icon_loc);
			
			double myjd = intent.getDoubleExtra("jd", 0.0);
			double mywd = intent.getDoubleExtra("wd", 0.0);
			String name = intent.getStringExtra("name");
			
			/* �ر����� */
			bMeshing = false;
			
			Location(myjd, mywd, name, null, null, image);
			
			// ʹ��postInvalidate����ֱ�����߳��и��½���
			map.postInvalidate();
		} else if(intent.getAction() != null && intent.getAction().equals("GJHF")) {
			ArrayList<HashMap<String, Double>> points = (ArrayList<HashMap<String, Double>>)intent.getSerializableExtra("points");
			String name = intent.getStringExtra("name");
			
			/* �ر����� */
			bMeshing = false;
			
			Point startPoint = new Point(points.get(0).get("jd"),points.get(0).get("wd"));
			
			SpatialReference sr4326 = SpatialReference.create(4326);
			Point point = (Point) GeometryEngine.project(startPoint, sr4326, map.getSpatialReference());
			map.centerAt(point, true);
			
			/** �˱��������������߻�����Ĺ켣���ݵ� */
			MultiPath poly = new Polyline();
			
			/** ����һ�������poLy��,·������ʼλ�� */
			poly.startPath(startPoint);
			/** ���ƶ��ĵ����poLy��,·���Ľ���Ϊֹ,�����һ���������һ���ߵ�Ƭ�� */
			for (int i = 1; i < points.size(); i++) {
				poly.lineTo(points.get(i).get("jd"), points.get(i).get("wd"));
			}
			
			tLayer.addGraphic(new Graphic(poly, new SimpleLineSymbol(
					Color.BLUE, 3)));
			
			map.zoomToScale(point, scale);
			
			// ʹ��postInvalidate����ֱ�����߳��и��½���
			map.postInvalidate();
		} else {
			loadFullMap();
		}
		/** ��ʼ����ͼ��ӵĿؼ� */
		InitView();
		/** ����GPS��λ */
		btn_location.setOnClickListener(new CurrentLocationListener());

		/** ��ҵ������λ�����¼� */
		btn_company.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/** ��ҵ������λ�ķ��� */
				enterprise_Batch_Locate();
			}
		});

		/** ��ѯͬ�µ����¼� */
		btn_teammate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!map.isLoaded()) {
					Toast.makeText(MapActivity.this, "��ͼ��δ�����޷���λ��",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					Toast.makeText(MapActivity.this, "���������쳣��",
							Toast.LENGTH_LONG).show();
					return;
				}
				/** ��ʾͬ��ǰ������滭ͼ�� */
				tLayer.removeAll();
				if (callout != null) {
					/** �����ע */
					callout.animatedHide();
				}
				/** ��������ͬ��λ�õķ��� */
				checkTeamate();
			}
		});

		/** ������λ */
		btn_car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** ������λ�ķ��� */
				car_Location();
			}
		});

		/** �ܱ߲�ѯ�����¼� */
		btn_zbcx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MapActivity.this, "�뵥����ͼ�����ܱ߲�ѯ",
						Toast.LENGTH_SHORT).show();
				isClickZbcx = true;
			}
		});

		/** ͼ����Ƽ����¼� */
		btn_layers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** ͼ����Ʒ��� */
				layer_Control();
			}
		});

		/** ������ӵ�����İ�ť�ļ����¼� */
		test_geometry_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});

		/** �жϵ�ͼ�Ƿ����񻯻��ֵı�ʶ�İ�ť�����¼� */
		btn_bMeshing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bMeshing) {
					btn_bMeshing.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.hbdt_gis_grid_off));
					bMeshing = false;
					Toast.makeText(MapActivity.this, "�ر����ŵ�ͼ�鿴����������ʾ�Ĺ���",
							Toast.LENGTH_LONG).show();
					/** ��ò�ѯ�����ж��Ƴ������ϵ����� */
				 
					tLayer.removeAll();
					if (callout != null) {
						/** �����ע */
						callout.animatedHide();
					}
				} else {
					btn_bMeshing.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.hbdt_gis_grid_open));
					bMeshing = true;
					Toast.makeText(MapActivity.this, "�����Խ������ŵ�ͼ���鿴����������ʾ",
							Toast.LENGTH_LONG).show();
					/** ��ò�ѯ�����ж��Ƴ������ϵ����� */
					tLayer.removeAll();
					if (callout != null) {
						/** �����ע */
						callout.animatedHide();
					}
				}
			}
		});

		/** ��¼�û�����Ͻ����ҵ */
		btn_grid_jur_ent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** ���õ�¼��Ա��Ͻ��Χ�ڵ���ҵ��Ϣ */
				// GridRelations();

				/* �ر����� */
				bMeshing = false;
				tLayer.removeAll();
				if (callout != null) {
					/** �����ע */
					callout.animatedHide();
				}
			}
		});

		/** ��γ�Ȳ�ѯ */
		btn_grid_coordinate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** ��γ�Ȳ�ѯ���� */
				coord_query();
			}
		});

		/** ������Ϣ��ѯ���� */
		btn_grid_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** ������Ϣ��ѯ���� */
				grid_queryinfo();
			}
		});

		/** ��ͼ���������ܱ߻�������ѯ�Ի��� */
		map.setOnLongPressListener(new OnLongPressListener() {

			@Override
			public void onLongPress(final float x, final float y) {
				if (!map.isLoaded()) {
					return;
				}
//				/** ����Ļ����ת���ɵ�ͼ���� */
//				Point point = map.toMapPoint(x, y);
//				map_tv.setText("x-->" + point.getX() + "y-->" + point.getY()
//						+ "��ǰ��ͼ�ֱ���Ϊ��" + map.getResolution() + "��ǰ��ͼ�ı�����"
//						+ map.getScale() + "��ǰ��ͼ�ķ�Χ" + map.getExtent());
//				/** ��ʼ����ͼ���ܱ߲�ѯ */
//				initialAroundFind(x, y);
			}
		});

		/** ���õ�ͼ�����ż��� */
		map.setOnZoomListener(new OnZoomListener() {

			/** ����֮ǰ�Զ����õķ��� */
			@Override
			public void preAction(float arg0, float arg1, double arg2) {

			}

			/** ����֮���Զ����õķ��� */
			@Override
			public void postAction(float arg0, float arg1, double arg2) {

				if (bMeshing) {
					/** ���ݵ�ǰ�����߿��Ƶ�ǰ������Ϣ */
					CurGridInfor();
				}
			}
		});

		/** ��ͼ״̬�ı�ļ��� */
		map.setOnStatusChangedListener(new OnStatusChangedListener() {

			@Override
			public void onStatusChanged(Object arg0, STATUS arg1) {

			}
		});

		/** ��ͼ�����¼� */
		map.setOnSingleTapListener(new OnSingleTapListener() {
			@Override
			public void onSingleTap(float x, float y) {

				/** �ж��Ƿ�Ϊ�ܱ߲�ѯģʽ */
				if (isClickZbcx) {
					initialAroundFind(x, y);
					isClickZbcx = false;
					return;
				}

				/** ��ȡҪ�ؼ���Ҫ�ص�������� */
				final int[] graphics = tLayer.getGraphicIDs(x, y, 30);
				if (graphics.length > 0) {
					final Graphic graphic = tLayer.getGraphic(graphics[0]);
					/** ��ȡ����λ�������λͼ */
					if (graphic.getAttributeValue("tag") != null) {
						/** �ܱ߲�ѯ��������λ�����Ӧ */
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

						/** �ܱ߲�ѯ��������λ�����Ӧ */
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
						/** ��ʼ����ͼ���ܱ߲�ѯ */
						initialAroundFind(x, y);
					}

					/** �����������ĵ���¼� */
					else if (graphic.getAttributeValue("cStr") != null) {
						Log.i("info", "�����������¼�");
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

								/** ������Ϣ���� */
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
								/** ��Ҫְ�� */
								EditText main_responsibility_edt = (EditText) view
										.findViewById(R.id.main_responsibility_edt);
								/** ������ */
								EditText grid_code_edt = (EditText) view
										.findViewById(R.id.grid_code_edt);
								/** �����ڼ������ */
								EditText supervise_content_edt = (EditText) view
										.findViewById(R.id.supervise_content_edt);
								/** ��Ҫ�����˵Ĳ��� */
								LinearLayout primary_responsibility_layout = (LinearLayout) view
										.findViewById(R.id.primary_responsibility_layout);
								/** ��Ҫ������ */
								EditText primary_responsibility_edt = (EditText) view
										.findViewById(R.id.primary_responsibility_edt);
								/** ��Ҫ��������ϵ��ʽ�Ĳ��� */
								LinearLayout primary_responsibility_telephone_layout = (LinearLayout) view
										.findViewById(R.id.primary_responsibility_telephone_layout);
								/** ��Ҫ��������ϵ��ʽ */
								EditText primary_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.primary_responsibility_phone_edt);
								/** ֱ�������˵Ĳ��� */
								LinearLayout gis_guid_direct_responsibility_layout = (LinearLayout) view
										.findViewById(R.id.gis_guid_direct_responsibility_layout);
								/** ֱ�������� */
								EditText direct_responsibility_edt = (EditText) view
										.findViewById(R.id.direct_responsibility_edt);
								/** ֱ�Ӹ�������ϵ�绰�Ĳ��� */
								LinearLayout gis_guid_direct_responsibility_telephone_layout = (LinearLayout) view
										.findViewById(R.id.gis_guid_direct_responsibility_telephone_layout);
								/** ֱ����������ϵ��ʽ */
								EditText direct_responsibility_phone_edt = (EditText) view
										.findViewById(R.id.direct_responsibility_phone_edt);

								Log.i("info", contextStr);
								if (contextStr != null
										&& !contextStr.equals("")) {

									/** �ܸ��������� */
									String mainNameStr = "";
									/** �ܸ����˵绰 */
									String mainteleStr = "";
									Log.i("info",
											"select * from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridname like  '%"
													+ contextStr
													+ "%' and blamerole = '1'");
									/** ����ܸ�������Ϣ */
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
									/** ��ʼ���������� */
									gridData = new ArrayList<HashMap<String, Object>>();
									gridData = sqliteutil
											.getInstance()
											.queryBySqlReturnArrayListHashMap(
													"select gridcode from Grid where gridname like '%"
															+ contextStr + "%'");
									if (gridData != null && gridData.size() > 0) {
										/** ��������id */
										gridId = gridData.get(0)
												.get("gridcode").toString();
									}

									if (gridId != null && !gridId.equals("")) {
										/** ��ȡ��Ҫ�����˵����� */
										String resDataStr = "";
										/** ��ȡ��Ҫ�����˵���ϵ��ʽ */
										String resDataPhoStr = "";
										/** ��ȡ�û������������parentCode */
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

										/** ��ȡ��Ҫ�����˵����� */
										String resPonDataStr = "";
										/** ��ȡ��Ҫ�����˵���ϵ��ʽ */
										String resPonDataPhoStr = "";
										/** ��ȡ�û���������������ݼ��� */
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

										/** ���λ��� */
										String agencyStr = "";
										if (gridId != null
												&& !gridId.equals("")) {
											String sql = "select ResponDepart  from Grid where GridCode = '"
													+ gridId + "'";
											agencyStr = sqliteutil
													.getInstance()
													.getDepidByUserid(sql);
										}
										/** �����ڼ������ */
										String gridCodeContent = "";
										if (gridId != null && gridId != "") {
											String sql = "select SuperContext from Grid  where gridcode = '"
													+ gridId + "'";
											gridCodeContent = sqliteutil
													.getInstance()
													.getDepidByUserid(sql);
										}

										/** ��Ҫְ�� */
										String mainContext = "";
										if (gridId != null && gridId != "") {
											mainContext = sqliteutil
													.getInstance()
													.getDepidByUserid(
															"select GridUserMapping.SuperContext from GridUserMapping INNER JOIN PC_Users on GridUserMapping.UserId = PC_Users.UserId where gridcode = '"
																	+ gridId
																	+ "' and blamerole = '3'");
										}

										/** ��ȡֱ�������˵����� */
										String directDataStr = "";
										/** ��ȡֱ�������˵���ϵ��ʽ */
										String directDataPhoStr = "";
										/** ��ȡ�û���������������ݼ��� */
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

										/** ��ȡ��¼�û��Ĵ�ִ���������� */
										int tasksize = rwxx
												.getTaskNumByUserIDandStatus(
														userId, "003");

										if (entData != null
												&& entData.size() > 0) {
											/** �����ȾԴ������ */
											String pollutionStr = String
													.valueOf(entData.size());
											pollution_sources_edt
													.setText(pollutionStr);// ��ȾԴ����
										}

										if (!graphic.getAttributeValue("layer")
												.toString().equals("")
												&& graphic.getAttributeValue(
														"layer").toString() != null
												&& graphic
														.getAttributeValue(
																"layer")
														.toString().equals("2")) {

											/** ����Ϊʡ��ʱֱ�������˲��ɼ� */
											gis_guid_direct_responsibility_layout
													.setVisibility(View.GONE);
											gis_guid_direct_responsibility_telephone_layout
													.setVisibility(View.GONE);
											/** ����Ϊʡ��ʱ��Ҫ�����˿ɼ� */
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

											/** ����Ϊ�м�ʱֱ�������˿ɼ� */
											gis_guid_direct_responsibility_layout
													.setVisibility(View.VISIBLE);
											gis_guid_direct_responsibility_telephone_layout
													.setVisibility(View.VISIBLE);
											/** ����Ϊ�м�ʱ��Ҫ�����˲��ɼ� */
											primary_responsibility_telephone_layout
													.setVisibility(View.GONE);
											primary_responsibility_layout
													.setVisibility(View.GONE);
										}

										main_responsibility_edt
												.setText(mainContext);// ��Ҫְ��
										supervise_content_edt
												.setText(gridCodeContent);// �����ڼ������
										grid_code_edt.setText(gridId); // ������
										overall_responsibility_edt
												.setText(mainNameStr);// �ܸ�����
										overall_responsibility_phone_edt
												.setText(mainteleStr);// �ܸ�������ϵ�绰
										responsible_agencies_edt
												.setText(agencyStr);// ���λ���
										person_in_charge_edt
												.setText(resDataStr);// ��Ҫ������
										person_in_charge_photo_edt
												.setText(resDataPhoStr);// ��Ҫ��������ϵ��ʽ
										// superscalar_pollution_sources_edt
										// .setText("");// ������ȾԴ����
										// the_number_of_tasks_edt
										// .setText(tasksize + "");// ��������
										gis_grid_infor_tv.setText(graphic
												.getAttributeValue("cStr")
												.toString());// ��õ�ǰ������Ϣ

										primary_responsibility_edt
												.setText(resPonDataStr); // ��Ҫ������
										primary_responsibility_phone_edt
												.setText(resPonDataPhoStr); // ��Ҫ��������ϵ��ʽ
										direct_responsibility_edt
												.setText(directDataStr); // ֱ��������
										direct_responsibility_phone_edt
												.setText(directDataPhoStr); // ֱ����������ϵ��ʽ
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
					/** λͼ�����Ӧ */
					else if (graphic.getAttributeValue("strqymc") != null) {
						String content = (String) graphic
								.getAttributeValue("strqymc");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
					/** ������λ�����Ӧ */
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
					} else if (graphic.getAttributeValue("U_RealName") != null) {// ��Ա��λ
						String content = (String) graphic
								.getAttributeValue("U_RealName");
						Double jd = (Double) graphic.getAttributeValue("jd");
						Double wd = (Double) graphic.getAttributeValue("wd");
						Point pt = new Point(jd, wd);
						callout(pt, content, null);
					}
				}

				if (graphics.length == 1) {// ���һ����ʱ����Ӧ

					if (tLayer.getGraphic(graphics[0]).getAttributeValue(
							"pname") == null) {// ��λ�ĵ��޵���¼�

						if (tLayer.getGraphic(graphics[0]).getAttributeValue(
								"teamate") != null) {// ��ͬ�´�绰
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
			/** �����ע */
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
	 * ����dialog���رգ��Ա㵯����ʾ��Ϣ
	 * 
	 * @param dialog
	 *            Ҫ���õ�dialog
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
	 * �ر�dialog
	 * 
	 * @param dialog
	 *            Ҫ���õ�dialog
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
	 * Description:ListView������
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

	/** ��������ͬ��λ�õķ��� */
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
					Log.i(TAG, "��ǰͬ����-->" + name + ",����->" + x + ",ά��->" + y);
					if (name.equals("") || name == null) {
						continue;
					}
					if (x == 0 || y == 0) {
						Toast.makeText(MapActivity.this,
								"��Ա" + name + "�������ݴ����޷���λ��",
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
				Toast.makeText(MapActivity.this, "��ǰ��ʱ�޷���ȡ��Աλ����Ϣ��",
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
	 * Description:����ͬ�����Ƽ�����Dialog
	 * 
	 * @param tmatename
	 *            ��ϵ������
	 * @return void
	 */
	private void CallTeamate(final String tmatename) {
		AlertDialog.Builder ab = new AlertDialog.Builder(MapActivity.this);
		ab.setTitle("����");
		ab.setMessage("��ȷ��Ҫ����" + tmatename + "?");
		ab.setPositiveButton("����", new DialogInterface.OnClickListener() {
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
					Toast.makeText(MapActivity.this, "���û�����ϵ�绰��",
							Toast.LENGTH_LONG).show();
			}
		});
		AlertDialog ad = ab.create();
		ad.show();
	}

	/**
	 * ��ʼ���ܱ߲�ѯ����
	 * 
	 * @param x
	 * @param y
	 */
	public void initialAroundFind(final float x, final float y) {

		listData = DisplayUitl.getMapListXML("around");

		if (listData == null || listData.size() == 0) {
			Toast.makeText(MapActivity.this, "ȱ���ܱ�������Ϣ�����������ļ�", 1).show();
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
				progress_text.setText(progress + "����");
			}
		});

		ListView layerListview = (ListView) view
				.findViewById(R.id.gis_layers_ListView);
		layerListview.setAdapter(new ListViewAdapter(listData));
		layerListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				/* �ر����� */
				bMeshing = false;
				dialog.cancel();
				tLayer.removeAll();
				/** ���ͼ���� */
				if (callout != null) {
					/** �����ע */
					callout.animatedHide();
				}
				progressDialog = new ProgressDialog(MapActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("���ڲ��ң����Ե�...");
				progressDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Double length = Double.valueOf(progress_text.getText()
								.toString().replace("����", ""));
						/** ����뾶KM */
						Double radius = 6371.004;
						/** Բ������ֵ */
						final Double PI = Math.PI;
						/** ��Ļ����ת���ɵ�ͼ���� */
						final Point pt = map.toMapPoint(new Point(x, y));
						Double JD = pt.getX();
						Double WD = pt.getY();
						/* <length/ ((Math.cos(WD) * radius * PI) / 180>�Ǹ��� */
						/** �������ֵ */
						Double MaxJD = JD - length
								/ ((Math.cos(WD) * radius * PI) / 180);
						/** ������Сֵ */
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
							/** ��ѯ��ȾԴ */
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
							/** ��ȡ�ܱ����� Environment�� */
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
	 * ��ȡ�ܱ����� Environment��
	 * 
	 * @param MinJD
	 *            ��С����
	 * @param MaxJD
	 *            ��󾭶�
	 * @param MinWD
	 *            ��Сά��
	 * @param MaxWD
	 *            ���ά��
	 * @param type
	 *            ��������
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

	/** ��ʼ����ͼ��Χ */
	@Override
	public void loadFullMap() {
		String mapext = null;
		try {
			mapext = DisplayUitl.getMapListXML("map").get(0).get("mapExtent")
					.toString();
		} catch (Exception e) {
			Log.i(TAG, "��ͼ��Χ��ʼ��ʧ��");
			e.printStackTrace();
			return;
		}

		/** ����ѯ���ķ�Χ�ַ������ */
		String[] extents = mapext.split(",");
		Log.i(TAG, "��ͼ���ط�Χ��" + extents[0] + "," + extents[1] + "," + extents[2]
				+ "," + extents[3]);

		/** ���õ�ͼ���뷶Χ */
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
			Toast.makeText(this, "��ȡ��ͼ����ʧ�ܣ�", 1).show();
			e.printStackTrace();
			return;
		}
		if (null != dataMap) {
			if (dataMap.get("layertype").toString().equalsIgnoreCase("dynamic")) {// ���ӷ���ˣ���ȡͼ��
				ArcGISDynamicMapServiceLayer layer = new ArcGISDynamicMapServiceLayer(
						dataMap.get("url").toString());
				map.addLayer(layer);
			} else if (dataMap.get("layertype").toString()
					.equalsIgnoreCase("tiled")) {
				/** ��������ͼ�� */
				ArcGISLocalTiledLayer layer = new ArcGISLocalTiledLayer(dataMap
						.get("url").toString());
				map.addLayer(layer);
			}
		} else {
			Toast.makeText(this, "��ͼȱ�������ļ���", 1).show();
		}
	}

	/**
	 * ����绰
	 * 
	 * @param tal
	 *            �绰����
	 */
	public void tocall(String tal) {
		if (tal.equals("")) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tal));
		/** ��ʼ��ͼ(������绰) */
		startActivity(intent);
	}
}
