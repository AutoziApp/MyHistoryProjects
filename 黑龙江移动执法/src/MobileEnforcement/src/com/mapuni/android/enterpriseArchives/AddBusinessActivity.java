/**
 * 
 */
package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.TaskFile;
import com.mapuni.android.attachment.UploadFile;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.AttachmentBaseActivity;
import com.mapuni.android.infoQuery.JCKHSearchActivity;
import com.mapuni.android.infoQuery.QYPMT;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * @author SS �����ҵ��ҳ��
 */
public class AddBusinessActivity extends BaseActivity {
	/** ���ݿ���������쳣 */
	private final int DATABASE_INSERT_EXCEPTION = 0;
	/** ���ݲ������ݳɹ� */
	private final int DATA_INSERT_SUCCESS = 1;
	/** ���ݿ��ѯ�쳣 */
	private final int DATA_QUERY_EXCEPTION = 2;
	/** ���ݿ������� */
	private final int NO_DATA_DATABASE = 3;
	/** ���ݿ��ѯ�ɹ� */
	private final int DATA_SUCCESSFUL_QUERY = 4;
	/** ���ݸ����쳣 */
	private final int DATA_UPDATE_EXCEPTION = 5;
	/** ���ݸ��³ɹ� */
	private final int DATA_UPDATE_SUCCESS = 6;
	/** ���к��ز�ѯ�ɹ� */
	private final int ENTERPRISE_DATA_UPDATE_SUCCESS = 7;
	/** �е����ƣ���ҵ����,��ҵ���벻��Ϊ��.. */
	private final int CAN_NOT_BE_EMPTY = 8;
	/** ��ҵ���ʧ��.. */
	private final int ADD_BUSINESS_FAILURE = 9;
	/** ����ҵ�Ѿ�����.. */
	private final int THE_COMPANY_ALREADY_EXISTS = 10;
	/** ����������.. */
	private final int NETWORK_NOT_CONNECTED = 11;
	private final int CONDIT_NOT_BE_EMPTY = 12;
	private final int CORPORATE_CODE_DUPLICATION = 13;
	/** �����ҵ��guId */
	private String guid;
	/** �����ҵ����ҵ���룬�༭��ҵ��ʱ����ֵ */
	private String qydm = "";// ��ҵ�༭ҳ���õ���
	// private final String CITY_DIVISIONS = "230300000";// ���������������Ĵ���
	private final String CITY_DIVISIONS = Global.getGlobalInstance()
			.getAreaCode();// ��ǰ��¼�û����������Ĵ���
	/** ��ҵ�༭ҳ���и�����ҵ�����ѯ����ȫ������ */
	private List<HashMap<String, Object>> queryData = new ArrayList<HashMap<String, Object>>();
	/** �����������ݼ��� */
	private List<HashMap<String, Object>> controlData = new ArrayList<HashMap<String, Object>>();
	/** ��ҵ��ģ�����ݼ��� */
	private List<HashMap<String, Object>> qygmData = new ArrayList<HashMap<String, Object>>();

	private ArrayList<HashMap<String, Object>> jjlxData = new ArrayList<HashMap<String, Object>>();
	private ArrayAdapter<String> jjlxAdapter;
	/** ����״̬�����ݼ��� */
	private List<HashMap<String, Object>> scztData = new ArrayList<HashMap<String, Object>>();
	/** ������ȾԴ�����ݼ��� */
	private List<HashMap<String, Object>> gkwryData = new ArrayList<HashMap<String, Object>>();
	/** ��ѯ����ʡ�µ��е����ݼ��� */
	private List<HashMap<String, Object>> cityData = new ArrayList<HashMap<String, Object>>();
	/** ��ѯ����ʡ�µ��ص����ݼ��� */
	private List<HashMap<String, Object>> countyData = new ArrayList<HashMap<String, Object>>();
	/** ��ѯ����ʡ�µ���������ݼ��� */
	private List<HashMap<String, Object>> ssxzData = new ArrayList<HashMap<String, Object>>();
	/** �����������ļ� */
	private RelativeLayout add_business_rlayout;
	/** ѡ�����Spinner */
	private Spinner select_the_city;
	/** ѡ����Spinner */
	private Spinner select_the_county;
	
	/** ѡ������Spinner */
	private Spinner select_the_ssxz;
	/** ѡ���������Spinner */
	// private Spinner control_category;
	/** ѡ���������button */
	private Button control_category_btn;
	/** ѡ����ҵ��ģ��Spinner */
	private Spinner qygm_spinner;

	/** ѡ����ҵ���� */
	private Spinner add_qyjbxx_hylx;
	// ��������
	private Spinner add_qyjbxx_jjlx;
	/** ѡ�������ȾԴ��Spinner */
	private Spinner gkwry_spinner, gkfxy_spinner, gkylb_spinner,
			sswqwdlqy_spinner;
	/** Spinnerѡ����е������� */
	private ArrayAdapter<String> select_the_city_adapter;
	/** Spinnerѡ���ص������� */
	private ArrayAdapter<String> select_the_county_adapter;//
	/** Spinnerѡ������������� */
	private ArrayAdapter<String> select_the_ssxz_adapter;//
	/** Spinnerѡ��������������� */
	// private ArrayAdapter<String> control_adapter;
	/** Spinnerѡ����ҵ��ģ�������� */
	private ArrayAdapter<String> qygm_adapter;
	// ��������
	private ArrayAdapter<String> jjlx_adapter;
	/** Spinnerѡ������״̬�������� */
	private ArrayAdapter<String> sczt_adapter;
	/** Spinnerѡ�������ȾԴ�������� */
	private ArrayAdapter<String> gkwry_adapter, gkylb_adapter;
	private AutoCompleteTextView enterprise_hymc_et;
	private EditText company_name_et, formerly_known_as_et,
			business_address_et, business_qywz_et, enterprise_yzbm_et,
			enterprise_czhm_et, environmental_contacts_et,
			environmental_contacts_phone_et, enterprise_hblxrlxdz_et,
			enterprise_yyzzdm_et, enterprise_zzjgdm_et, enterprise_ztz_et,
			enterprise_hbtz_et, enterprise_kysj_et, enterprise_jcsj_et,
			enterprise_nscsj_et, enterprise_zzdmj_et, enterprise_szgyymc_et,
			enterprise_zxgkjsj_et, enterprise_tysj_et, enterprise_pwxkzbh_et,
			add_qyjbxx_pwsbdm, enterprise_sczt_et, longitude_et, latitude_et,
			legal_representative_et, enterprise_frdbdh_et, enterprise_frdb_et,
			add_qyjbxx_qygs, add_qyjbxx_bz, add_qyjbxx_ssjd;
	private String company_name_et_str, formerly_known_as_et_str,
			enterprise_code_et_str, business_address_et_str,
			business_qywz_et_str, enterprise_yzbm_et_str,
			enterprise_czhm_et_str, legal_representative_et_str,
			environmental_contacts_et_str, environmental_contacts_phone_et_str,
			enterprise_hblxrlxdz_et_str, enterprise_yyzzdm_et_str,
			enterprise_hymc_et_str, enterprise_ztz_et_str,
			enterprise_hbtz_et_str, enterprise_kysj_et_str,
			enterprise_jcsj_et_str, enterprise_nscsj_et_str,
			enterprise_zzdmj_et_str, enterprise_szgyymc_et_str,
			enterprise_zxgkjsj_et_str, enterprise_tysj_et_str,
			enterprise_pwxkzbh_et_str, add_qyjbxx_pwsbdm_str,
			enterprise_sczt_et_str, enterprise_qxsj_et_str,
			enterprise_frdbdh_et_str, longitude_et_str, latitude_et_str,
			belongs_watershed_et_str, enterprise_frdb_et_str,
			add_qyjbxx_qygs_str, add_qyjbxx_bz_str, add_qyjbxx_ssjd_str;
	/** ��ҵ�������� */
	private String xzqh = "";
	private LinearLayout add_qyjbxx_qygs3_father;
	private Button add_qyjbxx_sczt, add_qyjbxx_kzjb;
	private Button add_qyjbxx_qygs2,add_qyjbxx_qygs3;

	private String control_category_str, qygm_spinner_str, add_qyjbxx_hylx_str,
			add_qyjbxx_jjlx_str, gkwry_spinner_str, sczt_spinner_str,
			gkfxy_spinner_str, sswqwdlqy_spinner_str;
	private LocationManager Manager;
	private Location location;
	// private ImageView gps_image;
	private String token;
	private boolean isAdd = true;

	private String imageGuid;
	public static final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH
			+ "Attach/";
	public final int PWXKZFJ_XZ = 3;
	public final int YYZZ_XZ = 1;
	public final int ZZJGDMTP_XZ = 2;
	public final int PWXKZFJ_PZ = 6;
	public final int YYZZ_PZ = 4;
	public final int ZZJGDMTP_PZ = 5;
	public final int QYPMSYT_XZ = 99;
	public final int QYPMSYT_PZ = 98;
	// ������������ͼ
	public final int SCGYLCT_PZ = 96;
	public final int SCGYLCT_XZ = 97;
	/** �����б������� */
	private AttachAdapter PWXKZFJAdapter;
	private ArrayList<HashMap<String, Object>> PWXKZFJAdapterData = new ArrayList<HashMap<String, Object>>();
	private AttachAdapter YYZZAdapter;
	private ArrayList<HashMap<String, Object>> YYZZAdapterData = new ArrayList<HashMap<String, Object>>();
	private AttachAdapter ZZJGDMTPAdapter;
	private ArrayList<HashMap<String, Object>> ZZJGDMTPAdapterData = new ArrayList<HashMap<String, Object>>();
	private AttachAdapter QYZPMSYTAdapter;
	private ArrayList<HashMap<String, Object>> QYZPMSYTAdapterData = new ArrayList<HashMap<String, Object>>();
	// ������������ͼ
	private AttachAdapter SCGYLCTAdapter;
	private ArrayList<HashMap<String, Object>> SCGYLCTAdapterData = new ArrayList<HashMap<String, Object>>();
	private ListView task_PWXKZFJ_list;
	private ListView task_YYZZ_list;
	private ListView task_ZZJGDMTP_list;
	private ListView task_QYZPMSYT_list;
	private ListView task_SCGYLCT_list;

	private ProgressDialog progressDialog = null;

	private ListView areaCheckListView;
	String[] controlNames = new String[] {};
	boolean[] areaState = new boolean[] {};
	/** ��һ�γ�ʼ�����棬��spinner��ִ�м����¼� */
	private Boolean isFirstInit = false;
	private final Handler handlertake = new Handler() {
		public void handleMessage(Message msg) {
			addtakeinformation();
		};
	};

	private ArrayList<HashMap<String, Object>> qylxData;
	private ArrayAdapter<String> qylx_adapter;
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case DATABASE_INSERT_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "�ܱ�Ǹ,�����ҵ�����쳣..",
						Toast.LENGTH_SHORT).show();

				break;
			case DATA_INSERT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (qydm != null) {
					Toast.makeText(AddBusinessActivity.this, "�༭��ҵ�ɹ�..",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AddBusinessActivity.this, "�µ���ҵ��ӳɹ�..",
							Toast.LENGTH_SHORT).show();
				}
				// ������Ϸ���
				if (isAdd) {
					AddBusinessActivity.this.finish();
				} else {
					AddBusinessActivity.this.finish();
					Intent modifyEnterprise = new Intent(
							AddBusinessActivity.this,
							EnterpriseArchivesActivitySlide.class);
					modifyEnterprise.putExtra("qyid", qydm);
					startActivity(modifyEnterprise);

				}
				break;

			case DATA_QUERY_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "���ݲ�ѯ�쳣..",
						Toast.LENGTH_SHORT).show();
				break;
			case NO_DATA_DATABASE:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "��������..",
						Toast.LENGTH_SHORT).show();
				break;
			case DATA_SUCCESSFUL_QUERY:
				if (progressDialog != null) {
					progressDialog.cancel();
				}

				/** ��ѯ�����Ƶ���� */
				controlData = new ArrayList<HashMap<String, Object>>();
				controlData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select name,code from wrylb order by code");
				int size = controlData.size();
				controlNames = new String[size];
				areaState = new boolean[size];
				for (int i = 0; i < controlData.size(); i++) {
					controlNames[i] = controlData.get(i).get("name").toString();
					areaState[i] = false;
				}
				String wrylb = queryData.get(0).get("wrylb").toString();
				String[] wryCode = new String[] {};
				StringBuilder builder = new StringBuilder();
				if (wrylb != null && !wrylb.equals("")) {
					wryCode = wrylb.split(";");
					for (int j = 0; j < wryCode.length; j++) {
						for (HashMap<String, Object> map : controlData) {
							String regionname = map.get("name").toString();
							String code = map.get("code").toString();
							if (code.equals(wryCode[j])) {
								builder.append(regionname + ";");
							}
						}
					}
					String str = builder.toString();
					if (str != null && !str.equals("")) {
						control_category_btn.setText(str.substring(0,
								str.length() - 1));
					} else {
						control_category_btn.setText("");
					}
				} else {
					control_category_btn.setText("");
				}

				/** ��ѯ����ҵ��ģ */
				qygmData = new ArrayList<HashMap<String, Object>>();
				qygmData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select name,code from wrygm order by code");
				/** ��ҵ��ģSpinner�ؼ��������� */
				ArrayList<String> qygmName = new ArrayList<String>();
				ArrayList<String> qygmCode = new ArrayList<String>();// ��ҵ��ģcodeֵ
				for (HashMap<String, Object> map : qygmData) {
					String regionname = map.get("name").toString();
					qygmName.add(regionname);
					qygmCode.add(map.get("code").toString());
				}

				/** Spinner�ϰ���ҵ��ģ�������� */
				qygm_adapter = initSpinnerAdapter(qygmName);

				qygm_spinner.setAdapter(qygm_adapter);
				// ��ҵ����
				qylxData = new ArrayList<HashMap<String, Object>>();
				qylxData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select code,name from Industry order by code");

				ArrayList<String> qylxName = new ArrayList<String>();
				ArrayList<String> qylxCode = new ArrayList<String>();
				for (HashMap<String, Object> map : qylxData) {
					String regionname = map.get("name").toString();
					String codei = map.get("code").toString();
					qylxName.add(codei + "----" + regionname);
					qylxCode.add(codei);
				}

				qylx_adapter = initSpinnerAdapter(qylxName);

				add_qyjbxx_hylx.setAdapter(qylx_adapter);

				/** ����Ĭ��ѡ�� */
				String qygm = queryData.get(0).get("qygm").toString();
				if (qygm != null && !qygm.equals("")) {
					for (int i = 0; i < qygmCode.size(); i++) {
						if (qygm.equals(qygmCode.get(i))) {
							qygm_spinner.setSelection(i);
						}
					}
				} else {
					qygm_spinner.setSelection(qygmCode.size() - 1);// Ĭ��ѡ�����һ��,��ֹ���ݲ��淶
				}

				/** ����Ĭ��ѡ�� */
				String hylb = queryData.get(0).get("hylb").toString();
				if (hylb != null && !hylb.equals("")) {
					for (int i = 0; i < qylxCode.size(); i++) {
						if (hylb.equals(qylxCode.get(i))) {
							add_qyjbxx_hylx.setSelection(i);
						}
					}
				} else {
					add_qyjbxx_hylx.setSelection(qygmCode.size() - 1);// Ĭ��ѡ�����һ��,��ֹ���ݲ��淶
				}

				/** ��ѯ���������� */

				jjlxData = new ArrayList<HashMap<String, Object>>();
				jjlxData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select name,code from jjlx order by code");

				ArrayList<String> jjlxName = new ArrayList<String>();
				ArrayList<String> jjlxCode = new ArrayList<String>();

				for (HashMap<String, Object> map : jjlxData) {
					String regionname = map.get("name").toString();
					jjlxName.add(regionname);
					jjlxCode.add(map.get("code").toString());
				}

				jjlxAdapter = initSpinnerAdapter(jjlxName);

				add_qyjbxx_jjlx.setAdapter(jjlxAdapter);

				/** ����Ĭ��ѡ�� */
				String jjlx = queryData.get(0).get("jjlx").toString();
				if (jjlx != null && !jjlx.equals("")) {
					for (int i = 0; i < jjlxCode.size(); i++) {
						if (jjlx.equals(jjlxCode.get(i))) {
							add_qyjbxx_jjlx.setSelection(i);
						}
					}
				} else {
					add_qyjbxx_jjlx.setSelection(qygmCode.size() - 1);// Ĭ��ѡ�����һ��,��ֹ���ݲ��淶
				}

				/** ��ѯ������״̬ */
				scztData = new ArrayList<HashMap<String, Object>>();
				scztData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select name,code from SCZT order by code");
				/** ��ҵ��ģSpinner�ؼ��������� */
				ArrayList<String> scztName = new ArrayList<String>();
				ArrayList<String> scztCode = new ArrayList<String>();// ��ҵ��ģcodeֵ
				for (HashMap<String, Object> map : scztData) {
					String regionname = map.get("name").toString();
					scztName.add(regionname);
					scztCode.add(map.get("code").toString());
				}

				/** Spinner�ϰ���ҵ��ģ�������� */
				sczt_adapter = initSpinnerAdapter(scztName);

				/** ��ѯ��������ȾԴ */
				// gkwryData = new ArrayList<HashMap<String, Object>>();

				// gkwryData = SqliteUtil.getInstance()
				// .queryBySqlReturnArrayListHashMap(
				// "select attenname,attencode from attention order by attencode");
				/** ������ȾԴ Spinner�ؼ��������� */
				ArrayList<String> gkwryName = new ArrayList<String>();
				gkwryName.add("��");
				gkwryName.add("��");
				ArrayList<String> gkwryCode = new ArrayList<String>();//
				gkwryCode.add("1");
				gkwryCode.add("0");

				/** Spinner�ϰ���ҵ��ģ�������� */
				gkwry_adapter = initSpinnerAdapter(gkwryName);

				gkwry_spinner.setAdapter(gkwry_adapter);
				gkfxy_spinner.setAdapter(gkwry_adapter);

				/** ����Ĭ��ѡ�� */
				String gkwry = queryData.get(0).get("zdwry").toString();
				if (gkwry != null && !gkwry.equals("")) {
					for (int i = 0; i < gkwryCode.size(); i++) {
						if (gkwry.equals(gkwryCode.get(i))) {
							gkwry_spinner.setSelection(i);
						}
					}
				} else {
					gkwry_spinner.setSelection(gkwryCode.size() - 1);// Ĭ��ѡ�����һ��,��ֹ���ݲ��淶
				}

				String ybgyy = queryData.get(0).get("ybgyy").toString();
				if (ybgyy != null && !ybgyy.equals("")) {
					for (int i = 0; i < gkwryCode.size(); i++) {
						if (ybgyy.equals(gkwryCode.get(i))) {
							gkfxy_spinner.setSelection(i);
						}
					}
				} else {
					gkfxy_spinner.setSelection(gkwryCode.size() - 1);// Ĭ��ѡ�����һ��,��ֹ���ݲ��淶
				}

				/** ��ѯ���е����� */
				cityData = getCityData(CITY_DIVISIONS);

				if (CITY_DIVISIONS.endsWith("0000000")) {// ʡ���û�
					cityData = SqliteUtil.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select RegionCode,RegionName from region where ParentCode = '"
											+ CITY_DIVISIONS
											+ "' order by RegionCode");
				} else if (CITY_DIVISIONS.endsWith("00000")) {// �м��û�
					cityData = SqliteUtil.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select RegionCode,RegionName from region where RegionCode = '"
											+ CITY_DIVISIONS
											+ "' order by RegionCode");
				} else {// �ؼ��û�
					cityData = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select RegionCode,RegionName from region where RegionCode = '"
											+ SqliteUtil
													.getInstance()
													.getDepidByDepName(
															"select ParentCode from region where RegionCode = '"
																	+ CITY_DIVISIONS
																	+ "' order by RegionCode")
											+ "' order by RegionCode");
				}

				// ��¼�û����м���ֵ
				String pcode = Global.getGlobalInstance().getAreaCode()
						.substring(0, 4)
						+ "00000";
				int selectItemCity = 0;
				/** Spinner�ϰ󶨳��е����� */

				/** �������� */
				String xzqhStr = queryData.get(0).get("xzqh").toString();

				String xzqhNameCity = SqliteUtil
						.getInstance()
						.getDepidByDepName(
								"select RegionName from region where RegionCode = (select ParentCode from region where RegionCode = '"
										+ xzqhStr + "')");
				/** �������� */
				String ssds = queryData.get(0).get("ssds").toString();
				ArrayList<String> cityNames = new ArrayList<String>();
				cityNames.add("-��ѡ����-");
				for (int i = 0; i < cityData.size(); i++) {
					String cityName = cityData.get(i).get("regionname")
							.toString();
					cityNames.add(cityName);

					if (cityName.equals(xzqhNameCity)) {
						selectItemCity = i + 1;
					} else if (cityName.equals(ssds)) {
						selectItemCity = i + 1;
					}
					HashMap<String, Object> map = cityData.get(i);
					if (map.get("regioncode").equals(pcode)) {
						// ���������б�ֵ�����޸�
						select_the_city.setClickable(false);
					}
				}
				/** Spinner�ϰ󶨳������ݵ������� */
				select_the_city_adapter = initSpinnerAdapter(cityNames);
				select_the_city.setAdapter(select_the_city_adapter);
				// select_the_city.setSelection(parentCodeIndex);
				select_the_city.setSelection(selectItemCity);

				select_the_city.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// if(isFirstInit){
								// isFirstInit=false;
								// return;
								// }
								if (!select_the_city.getSelectedItem()
										.toString().equals("-��ѡ����-")) {
									if (arg2 > 0) {
										xzqh = countyData.get(arg2 - 1)
												.get("regioncode").toString();}

									String cityRegionCode = cityData
											.get(arg2 - 1).get("regioncode")
											.toString();
									xzqh = cityRegionCode;
									/** ��ȡ����ʡѡ���к��²�����س� */
									countyData = getChildCityData(cityRegionCode);
								

									/** Spinner�ϰ��سǵ����� */
									ArrayList<String> countyNames = new ArrayList<String>();
									int countyCodeIndex = 0;

									int selectItemCounty = 0;
									countyNames.add("-����ѡ����-");
									for (int j = 0; j < countyData.size(); j++) {
										String countyName = countyData.get(j)
												.get("regionname").toString();

										countyNames.add(countyName);
										HashMap<String, Object> map = countyData
												.get(j);
										if (map.get("regioncode").equals(
												Global.getGlobalInstance()
														.getAreaCode())) {
											countyCodeIndex = j + 1;
											// ���������б�ֵ�����޸�
											select_the_county
													.setClickable(false);
											//����ѡ������
											select_the_ssxz
											.setClickable(false);
										}
									}
									/** Spinner�ϰ��س����ݵ������� */
									select_the_county_adapter = initSpinnerAdapter(countyNames);
									select_the_county
											.setAdapter(select_the_county_adapter);
									// select_the_county.setSelection(countyCodeIndex);
									select_the_county
											.setSelection(selectItemCounty);

								} else {
									/** Spinner�ϰ��سǵ����� */
									ArrayList<String> countyNames = new ArrayList<String>();
									countyNames.add("-����ѡ����-");
									/** Spinner�ϰ��س����ݵ������� */
									select_the_county_adapter = initSpinnerAdapter(countyNames);

									select_the_county
											.setAdapter(select_the_county_adapter);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {

							}
						});
				//TODO �������ص�ѡ��code
				select_the_ssxz.setOnItemSelectedListener(new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
					 
						HashMap<String,Object> hashMap = ssxzData.get(position+1);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						
					}
					
					
				});
//				select_the_county
//						.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//							@Override
//							public void onItemSelected(AdapterView<?> arg0,
//									View arg1, int arg2, long arg3) {
//								if (!select_the_city.getSelectedItem()
//										.toString().equals("-����ѡ����-")) {
//									if (arg2 > 0) {
//										xzqh = countyData.get(arg2 - 1)
//												.get("regioncode").toString();
//									}
//								}else{
//									/** Spinner�ϰ���������� */
//									ArrayList<String> countyNames = new ArrayList<String>();
//									countyNames.add("-����ѡ������-");
//									/** Spinner�ϰ��س����ݵ������� */
//									select_the_ssxz_adapter = initSpinnerAdapter(countyNames);
//
//									select_the_ssxz
//											.setAdapter(select_the_ssxz_adapter);
//				
//								}
//							
//							}
//
//							@Override
//							public void onNothingSelected(AdapterView<?> arg0) {
//								/** Spinner�ϰ���������� */
//								ArrayList<String> countyNames = new ArrayList<String>();
//								countyNames.add("-����ѡ������-");
//								/** Spinner�ϰ��س����ݵ������� */
//								select_the_ssxz_adapter = initSpinnerAdapter(countyNames);
//
//								select_the_ssxz
//										.setAdapter(select_the_ssxz_adapter);
//							}
//						});
				
				String parentCode = getParentcode(xzqh);
				if (parentCode.equals(CITY_DIVISIONS)) {// ����������һ�����±�
					for (int i = 0; i < cityData.size(); i++) {
						String str = cityData.get(i).get("regioncode")
								.toString();
						if (xzqh.equals(str)) {
							select_the_city.setSelection(i + 1);
							break;
						}
					}
				} else {// ����������һ�����±�
					String myxzqh = xzqh;
					isFirstInit = true;
					for (int i = 0; i < cityData.size(); i++) {
						String str = cityData.get(i).get("regioncode")
								.toString();
						if (parentCode.equals(str)) {
							select_the_city.setSelection(i + 1);
							countyData = getChildCityData(str);
							ArrayList<String> countyNames = new ArrayList<String>();
							countyNames.add("-����ѡ����-");
							for (int j = 0; j < countyData.size(); j++) {
								String countyName = countyData.get(j)
										.get("regionname").toString();
								countyNames.add(countyName);
							}
							/** Spinner�ϰ��س����ݵ������� */
							select_the_county_adapter = initSpinnerAdapter(countyNames);
							select_the_county
									.setAdapter(select_the_county_adapter);

							for (int n = 0; n < countyData.size(); n++) {
								String region = countyData.get(n)
										.get("regioncode").toString();
								if (myxzqh.equals(region)) {
									select_the_county.setSelection(n + 1);
									break;
								}
							}
							break;
						}
					}

				}
				/** ������ */
				company_name_et
						.setText(queryData.get(0).get("qymc").toString());
				/** ��ҵ���� */
				formerly_known_as_et.setText(queryData.get(0).get("cym")
						.toString());
				/** ��ҵ������ */
				// enterprise_code_et.setText(queryData.get(0).get("qydm").toString());/**
				// ��ҵ���� */
				// enterprise_code_et.setText(qydm);/** ��ҵ���� */
				business_address_et.setText(queryData.get(0).get("qydz")
						.toString());// ��ҵ��ַ
				enterprise_yzbm_et.setText(queryData.get(0).get("yzbm")
						.toString());// ��������
				legal_representative_et.setText(queryData.get(0).get("frdb")
						.toString());

				/** ���˴��� */
				enterprise_czhm_et.setText(queryData.get(0).get("czhm")
						.toString());// �������
				environmental_contacts_et.setText(queryData.get(0).get("hblxr")
						.toString());
				/** ������ϵ�� */
				environmental_contacts_phone_et.setText(queryData.get(0)
						.get("hblxrdh").toString());
				/** ������ϵ�˵绰 */
				enterprise_hblxrlxdz_et.setText(queryData.get(0)
						.get("hblxrlxdz").toString());
				/** ���˴���绰 */
				enterprise_frdbdh_et.setText(queryData.get(0).get("frdbdh")
						.toString());
				/** ���˴��� */
				enterprise_frdb_et.setText(queryData.get(0).get("frdm")
						.toString());
				// ��ҵ����
				add_qyjbxx_qygs.setText(queryData.get(0).get("wryjs")
						.toString());
				add_qyjbxx_bz.setText(queryData.get(0).get("bz").toString());

				add_qyjbxx_ssjd
						.setText(queryData.get(0).get("ssjd").toString());

				/** ������ϵ�˵�ַ */
				enterprise_yyzzdm_et.setText(queryData.get(0).get("yyzzdm")
						.toString());// Ӫҵִ�մ���
				// wsc ����ҵ���ƴ���ת���ɺ���
				if (!"".equals(queryData.get(0).get("hylb").toString())) {

					ArrayList<HashMap<String, Object>> industryName = SqliteUtil
							.getInstance().queryBySqlReturnArrayListHashMap(
									"select t.name from Industry t  where t.code='"
											+ queryData.get(0).get("hylb")
													.toString() + "'");
					if (industryName != null && industryName.size() > 0) {
						enterprise_hymc_et.setText(industryName.get(0)
								.get("name").toString());// ��ҵ����,ȡ��ҵ���
					}
				}

				enterprise_ztz_et.setText(queryData.get(0).get("ztz")
						.toString());// ��Ͷ��
				enterprise_hbtz_et.setText(queryData.get(0).get("hbtz")
						.toString());// ����Ͷ��
				// enterprise_qygm_et.setText(queryData.get(0).get("qygm").toString());/**
				// ��ҵ��ģ */
				enterprise_kysj_et.setText(queryData.get(0).get("kysj")
						.toString());// ��ҵʱ��
				enterprise_nscsj_et.setText(queryData.get(0).get("nscsj")
						.toString());// ������ʱ��
				enterprise_zzdmj_et.setText(queryData.get(0).get("zzdmj")
						.toString());// ��ռ�����
				enterprise_szgyymc_et.setText(queryData.get(0).get("szgyymc")
						.toString());// ���ڹ�ҵ԰����
				enterprise_zxgkjsj_et.setText(queryData.get(0).get("zxgkjsj")
						.toString());// ���¸�����ʱ��
				enterprise_tysj_et.setText(queryData.get(0).get("tysj")
						.toString());// ���ͣҵʱ��
				enterprise_pwxkzbh_et.setText(queryData.get(0).get("pwxkzbh")
						.toString());// �������֤���
				add_qyjbxx_pwsbdm.setText(queryData.get(0).get("pwsbdm")
						.toString());// ������ɵǼǺ�

				/** �������� */
				longitude_et.setText(queryData.get(0).get("jd").toString());
				/** ���� */
				latitude_et.setText(queryData.get(0).get("wd").toString());
				/** γ�� */

				if (qydm == null) {
					guid = "";
				} else {
					guid = queryData.get(0).get("guid").toString();
				}
				break;
			case DATA_UPDATE_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "��ҵ�����޸ķ����쳣..",
						Toast.LENGTH_SHORT).show();
				break;
			case DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "��ҵ�����޸ĳɹ�..",
						Toast.LENGTH_SHORT).show();
				break;

			case ENTERPRISE_DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				break;
			case CAN_NOT_BE_EMPTY:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "��(*)�ŵ���Ϣ����!",
						Toast.LENGTH_SHORT).show();
				break;

			case ADD_BUSINESS_FAILURE:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (qydm != null) {
					Toast.makeText(AddBusinessActivity.this, "�༭��ҵʧ�ܣ�",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(AddBusinessActivity.this, "�����ҵʧ�ܣ�",
							Toast.LENGTH_SHORT).show();

				}

				break;
			case THE_COMPANY_ALREADY_EXISTS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this,
						"��ҵ���ʧ��,��ҵ���Ʋ���Ϊ�ջ��߸���ҵ�Ѿ����ڣ�", Toast.LENGTH_SHORT).show();
				break;

			case NETWORK_NOT_CONNECTED:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "���������Ƿ�����",
						Toast.LENGTH_SHORT).show();
				break;
			case CONDIT_NOT_BE_EMPTY:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this,
						"�е�����,��ҵ����,���������˲���Ϊ��..", Toast.LENGTH_SHORT).show();
				break;
			case CORPORATE_CODE_DUPLICATION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(AddBusinessActivity.this, "��ҵ�����Ѵ���",
						Toast.LENGTH_SHORT).show();
				break;

			}

		};
	};
	private RWXX rwxx;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bussiness_layout);
		Intent intent = getIntent();
		rwxx = new RWXX();
		qydm = intent.getStringExtra("qydm");
		if (qydm == null || qydm.equals("")) {
			isAdd = true;
			guid = UUID.randomUUID().toString();
			// guid = "1234568865DFGDF";
		} else
			isAdd = false;

		/** ���������� */
		add_business_rlayout = (RelativeLayout) this
				.findViewById(R.id.add_business_relayout);
		try {
			if (qydm != null && !qydm.equals("")) {
				SetBaseStyle(add_business_rlayout, "�༭��ҵ");
			} else {
				SetBaseStyle(add_business_rlayout, "�����ҵ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		setSynchronizeButtonVisiable(false);
		setTitleLayoutVisiable(true);
		setSearchButtonVisiable(false);
		// ������ʾ�б�
		task_PWXKZFJ_list = (ListView) findViewById(R.id.pwxkzbh_list);
		task_YYZZ_list = (ListView) findViewById(R.id.yyzzdm_list);
		task_ZZJGDMTP_list = (ListView) findViewById(R.id.zzjgdm_list);
		task_QYZPMSYT_list = (ListView) findViewById(R.id.qyzpmsyt_list);
		// ��������ͼ
		task_SCGYLCT_list = (ListView) findViewById(R.id.scgylct_list);

		PWXKZFJAdapter = new AttachAdapter(PWXKZFJAdapterData);
		YYZZAdapter = new AttachAdapter(YYZZAdapterData);
		ZZJGDMTPAdapter = new AttachAdapter(ZZJGDMTPAdapterData);
		// ��ҵ��ƽ��
		QYZPMSYTAdapter = new AttachAdapter(QYZPMSYTAdapterData);
		// ����
		SCGYLCTAdapter = new AttachAdapter(SCGYLCTAdapterData);

		task_PWXKZFJ_list.setAdapter(PWXKZFJAdapter);
		task_YYZZ_list.setAdapter(YYZZAdapter);
		task_ZZJGDMTP_list.setAdapter(ZZJGDMTPAdapter);
		// ��ҵ��ƽ��
		task_QYZPMSYT_list.setAdapter(QYZPMSYTAdapter);
		// ����
		task_SCGYLCT_list.setAdapter(SCGYLCTAdapter);

		task_PWXKZFJ_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String guid = ((TextView) (arg1
						.findViewById(R.id.listitem_text))).getTag().toString();
				FileHelper fileHelper = new FileHelper();
				fileHelper.showFileByGuid(guid, AddBusinessActivity.this);
			}
		});

		task_YYZZ_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String guid = ((TextView) (arg1
						.findViewById(R.id.listitem_text))).getTag().toString();
				FileHelper fileHelper = new FileHelper();
				fileHelper.showFileByGuid(guid, AddBusinessActivity.this);
			}
		});
		task_ZZJGDMTP_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String guid = ((TextView) (arg1
						.findViewById(R.id.listitem_text))).getTag().toString();
				FileHelper fileHelper = new FileHelper();
				fileHelper.showFileByGuid(guid, AddBusinessActivity.this);
			}
		});

		task_QYZPMSYT_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String guid = ((TextView) (view
						.findViewById(R.id.listitem_text))).getTag().toString();
				FileHelper fileHelper = new FileHelper();
				fileHelper.showFileByGuid(guid, AddBusinessActivity.this);

			}
		});
		// ����
		task_SCGYLCT_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String guid = ((TextView) (view
						.findViewById(R.id.listitem_text))).getTag().toString();
				FileHelper fileHelper = new FileHelper();
				fileHelper.showFileByGuid(guid, AddBusinessActivity.this);

			}
		});

		task_PWXKZFJ_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						String guid = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						String fileNam = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getText()
								.toString();

						showDialog(guid, fileNam, PWXKZFJ_PZ);
						return true;
					}
				});
		task_YYZZ_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						String guid = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						String fileNam = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getText()
								.toString();

						showDialog(guid, fileNam, YYZZ_PZ);
						return true;
					}
				});

		task_ZZJGDMTP_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						String guid = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						String fileNam = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getText()
								.toString();

						showDialog(guid, fileNam, ZZJGDMTP_PZ);
						return true;
					}
				});
		// ��ҵ��ƽ��
		task_QYZPMSYT_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						String guid = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						String fileNam = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getText()
								.toString();

						showDialog(guid, fileNam, QYPMSYT_PZ);
						return true;
					}
				});

		// ��������
		task_SCGYLCT_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						String guid = ((TextView) (view
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						String fileNam = ((TextView) (view
								.findViewById(R.id.listitem_text))).getText()
								.toString();

						showDialog(guid, fileNam, SCGYLCT_PZ);

						return true;
					}
				});

		// ���ؽ���
		InitView();

		Manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		Manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				locationListener);

		// �ж���������ҵ�����޸���ҵ
		if (qydm != null && !qydm.equals("")) { // �޸���ҵ
			progressDialog = new ProgressDialog(AddBusinessActivity.this);
			progressDialog.setMessage("��������Ŭ��������,�����Ժ�...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					queryData = SqliteUtil.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select * from t_wry_qyjbxx where qydm = '"
											+ qydm + "'");
					if (queryData == null || queryData.equals("")) {
						Message msg = handler.obtainMessage();
						msg.arg1 = DATA_QUERY_EXCEPTION;
						handler.sendMessage(msg);
					} else if (queryData.size() == 0) {
						Message msg = handler.obtainMessage();
						msg.arg1 = NO_DATA_DATABASE;
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.arg1 = DATA_SUCCESSFUL_QUERY;
						handler.sendMessage(msg);
					}
				}
			}).start();
		} else { // ������ҵ

			/** ��ѯ�����Ƶ���� */
			controlData = new ArrayList<HashMap<String, Object>>();
			controlData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap("select name from wrylb");
			/** ���Ƽ���Spinner�ؼ��������� */
			// ArrayList<String> controlName = new ArrayList<String>();
			int size = controlData.size();
			controlNames = new String[size];
			areaState = new boolean[size];
			for (int i = 0; i < controlData.size(); i++) {
				controlNames[i] = controlData.get(i).get("name").toString();
				areaState[i] = false;
			}

			/** ��ѯ����ҵ��ģ */
			qygmData = new ArrayList<HashMap<String, Object>>();
			qygmData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap("select name from wrygm");
			/** ��ҵ��ģSpinner�ؼ��������� */
			ArrayList<String> qygmName = new ArrayList<String>();
			for (HashMap<String, Object> map : qygmData) {
				String regionname = map.get("name").toString();
				qygmName.add(regionname);
			}
			/** Spinner�ϰ���ҵ��ģ�������� */
			qygm_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, qygmName);
			qygm_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			qygm_spinner.setAdapter(qygm_adapter);

			// TODO ��ҵ����
			qylxData = new ArrayList<HashMap<String, Object>>();
			qylxData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select code,name from Industry order by code");

			ArrayList<String> qylxName = new ArrayList<String>();
			ArrayList<String> qylxCode = new ArrayList<String>();
			for (HashMap<String, Object> map : qylxData) {
				String regionname = map.get("name").toString();
				String codei = map.get("code").toString();
				qylxName.add(codei + "----" + regionname);
				qylxCode.add(codei);
			}

			qylx_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, qylxName);
			qylx_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			add_qyjbxx_hylx.setAdapter(qylx_adapter);

			// ��������
			jjlxData = new ArrayList<HashMap<String, Object>>();
			jjlxData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select name,code from jjlx order by code");

			ArrayList<String> jjlxName = new ArrayList<String>();

			for (HashMap<String, Object> map : jjlxData) {
				String regionname = map.get("name").toString();
				jjlxName.add(regionname);
			}

			jjlxAdapter = initSpinnerAdapter(jjlxName);

			add_qyjbxx_jjlx.setAdapter(jjlxAdapter);

			jjlx_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, jjlxName);
			jjlx_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			add_qyjbxx_jjlx.setAdapter(jjlx_adapter);

			/** ��ѯ������״̬ */
			scztData = new ArrayList<HashMap<String, Object>>();
			scztData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap("select name from SCZT");
			/** ��ҵ��ģSpinner�ؼ��������� */
			ArrayList<String> scztName = new ArrayList<String>();
			for (HashMap<String, Object> map : scztData) {
				String regionname = map.get("name").toString();
				scztName.add(regionname);
			}
			/** Spinner�ϰ���ҵ��ģ�������� */
			sczt_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, scztName);
			sczt_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			/** ��ѯ��������ȾԴ */
			// gkwryData = new ArrayList<HashMap<String, Object>>();
			// gkwryData = SqliteUtil.getInstance()
			// .queryBySqlReturnArrayListHashMap(
			// "select attenname from attention");
			/** ��ҵ��ģSpinner ���Ƿ� �������� */
			ArrayList<String> gkwryName = new ArrayList<String>();
			gkwryName.add("��");
			gkwryName.add("��");
			// for (HashMap<String, Object> map : gkwryData) {
			// String regionname = map.get("attenname").toString();
			// gkwryName.add(regionname);
			// }
			/** Spinner�ϰ���ҵ��ģ�������� */
			gkwry_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, gkwryName);
			gkwry_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			/** ������ȾԴ��������� */
			String[] strs = { "ˮ����", "������", "ˮ������", "��ˮ����" };
			gkylb_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, strs);
			gkylb_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			gkwry_spinner.setAdapter(gkwry_adapter); // �Ƿ�Ϊ����Դ
			gkfxy_spinner.setAdapter(gkwry_adapter); // �Ƿ�Ϊ�յ����Դ
			gkylb_spinner.setAdapter(gkylb_adapter); // ѡ�����Դ���
			sswqwdlqy_spinner.setAdapter(gkwry_adapter); // �Ƿ�Ϊ30��ǧ�ߵ�����ҵ

			/** ��ѯ���е����� */
			cityData = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from region where regioncode = '"
									+ CITY_DIVISIONS + "' order by RegionCode");

			if (CITY_DIVISIONS.endsWith("0000000")) {// ʡ���û�
				cityData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select RegionCode,RegionName from region where ParentCode = '"
										+ CITY_DIVISIONS
										+ "' order by RegionCode");
			} else if (CITY_DIVISIONS.endsWith("00000")) {// �м��û�
				cityData = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select RegionCode,RegionName from region where RegionCode = '"
										+ CITY_DIVISIONS
										+ "' order by RegionCode");
			} else {// �ؼ��û�
				cityData = SqliteUtil
						.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select RegionCode,RegionName from region where RegionCode = '"
										+ SqliteUtil
												.getInstance()
												.getDepidByDepName(
														"select ParentCode from region where RegionCode = '"
																+ CITY_DIVISIONS
																+ "' order by RegionCode")
										+ "' order by RegionCode");
			}

			/** Spinner�ϰ󶨳��е����� */
			// ��¼�û����м���ֵ
			String pcode = Global.getGlobalInstance().getAreaCode()
					.substring(0, 4)
					+ "00000";
			int parentCodeIndex = 0;

			ArrayList<String> cityNames = new ArrayList<String>();
			cityNames.add("-��ѡ����-");

			for (int i = 0; i < cityData.size(); i++) {
				String cityName = cityData.get(i).get("regionname").toString();
				cityNames.add(cityName);

				HashMap<String, Object> map = cityData.get(i);
				if (map.get("regioncode").equals(pcode)) {
					parentCodeIndex = i + 1;
					// ���������б�ֵ�����޸�
					select_the_city.setClickable(false);
				}
			}
			/** Spinner�ϰ󶨳������ݵ������� */
			select_the_city_adapter = new ArrayAdapter<String>(
					AddBusinessActivity.this,
					android.R.layout.simple_spinner_item, cityNames);
			/** Spinner��������ʽ */
			select_the_city_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			select_the_city.setAdapter(select_the_city_adapter);
			select_the_city.setSelection(parentCodeIndex);
			select_the_city
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if (!select_the_city.getSelectedItem().toString()
									.equals("-��ѡ����-")) {
								String cityRegionCode = cityData.get(arg2 - 1)
										.get("regioncode").toString();
								xzqh = cityRegionCode;
								/** ��ȡ����ʡѡ���к��²�����س� */
								countyData = SqliteUtil.getInstance()
										.queryBySqlReturnArrayListHashMap(
												"select * from region where parentcode = '"
														+ cityRegionCode + "'");

								/** Spinner�ϰ��سǵ����� */
								ArrayList<String> countyNames = new ArrayList<String>();
								countyNames.add("-����ѡ����-");
								int countyCodeIndex = 0;
								for (int j = 0; j < countyData.size(); j++) {
									String countyName = countyData.get(j)
											.get("regionname").toString();
									countyNames.add(countyName);

									HashMap<String, Object> map = countyData
											.get(j);
									if (map.get("regioncode").equals(
											Global.getGlobalInstance()
													.getAreaCode())) {
										countyCodeIndex = j + 1;
										// ���������б�ֵ�����޸�
										select_the_county.setClickable(false);
									//	select_the_ssxz.setClickable(false);
									}
								}
								/** Spinner�ϰ��س����ݵ������� */
								select_the_county_adapter = new ArrayAdapter<String>(
										AddBusinessActivity.this,
										android.R.layout.simple_spinner_item,
										countyNames);
								/** Spinner��������ʽ */
								select_the_county_adapter
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								select_the_county
										.setAdapter(select_the_county_adapter);
								select_the_county.setSelection(countyCodeIndex);
							} else {
								/** Spinner�ϰ��سǵ����� */
								ArrayList<String> countyNames = new ArrayList<String>();
								countyNames.add("-����ѡ����-");
								/** Spinner�ϰ��س����ݵ������� */
								select_the_county_adapter = new ArrayAdapter<String>(
										AddBusinessActivity.this,
										android.R.layout.simple_spinner_item,
										countyNames);
								/** Spinner��������ʽ */
								select_the_county_adapter
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								select_the_county
										.setAdapter(select_the_county_adapter);
								
								
								/** Spinner�ϰ��سǵ����� */
								ArrayList<String> countyNames2 = new ArrayList<String>();
								countyNames2.add("-����ѡ�����-");
								/** Spinner�ϰ��س����ݵ������� */
								select_the_ssxz_adapter = new ArrayAdapter<String>(
										AddBusinessActivity.this,
										android.R.layout.simple_spinner_item,
										countyNames2);
								/** Spinner��������ʽ */
								select_the_ssxz_adapter
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								select_the_ssxz
										.setAdapter(select_the_ssxz_adapter);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			select_the_county.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if (!select_the_city.getSelectedItem().toString()
									.equals("-����ѡ����-")) {
								if (arg2 > 0) {
									xzqh = countyData.get(arg2 - 1)
											.get("regioncode").toString();
									
									ArrayList<String> ssxzNames = new ArrayList<String>();
									//TODO  �ж��ǹ���Ҫ+1
									HashMap<String, Object> map = countyData
											.get(arg2-1);
									
									 ssxzData = getChildCityData(map.get("regioncode").toString());
									 ssxzNames.add("-����ѡ�����-");
										for (int j = 0; j < ssxzData.size(); j++) {
											String countyName = ssxzData.get(j)
													.get("regionname").toString();
											ssxzNames.add(countyName);
										}
									
									
										select_the_ssxz_adapter = new ArrayAdapter<String>(
												AddBusinessActivity.this,
												android.R.layout.simple_spinner_item,
												ssxzNames);
										/** Spinner��������ʽ */
										select_the_ssxz_adapter
												.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
										select_the_ssxz
												.setAdapter(select_the_ssxz_adapter);
								}
								
						
								//	select_the_ssxz.setSelection(0);
								 
							}else{
								
								/** Spinner�ϰ��سǵ����� */
								ArrayList<String> countyNames = new ArrayList<String>();
								countyNames.add("-����ѡ�����-");
								/** Spinner�ϰ��س����ݵ������� */
								select_the_ssxz_adapter = new ArrayAdapter<String>(
										AddBusinessActivity.this,
										android.R.layout.simple_spinner_item,
										countyNames);
								/** Spinner��������ʽ */
								select_the_ssxz_adapter
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								select_the_ssxz
										.setAdapter(select_the_ssxz_adapter);
								
							}
						
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		}
	}

	/** ��ʼ��ҳ����ʾ */
	private void InitView() {
		// gps_image = (ImageView) findViewById(R.id.gps_image);
		select_the_city = (Spinner) findViewById(R.id.select_the_city);
		select_the_city.setPrompt("��ѡ����:");
		select_the_county = (Spinner) findViewById(R.id.select_the_county);
		select_the_county.setPrompt("��ѡ����");
		
		select_the_ssxz = (Spinner) findViewById(R.id.select_the_ssxz);
		select_the_ssxz.setPrompt("��ѡ������");
		// control_category = (Spinner) findViewById(R.id.control_category);
		// control_category.setPrompt("��ѡ��������");
		control_category_btn = (Button) findViewById(R.id.control_category);
		control_category_btn.setOnClickListener(new CheckBoxClickListener());
		qygm_spinner = (Spinner) findViewById(R.id.qygm_spinner);
		qygm_spinner.setPrompt("��ѡ����ҵ��ģ");
		add_qyjbxx_hylx = (Spinner) findViewById(R.id.add_qyjbxx_hylx);
		add_qyjbxx_hylx.setPrompt("��ѡ����ҵ����");
		add_qyjbxx_jjlx = (Spinner) findViewById(R.id.add_qyjbxx_jjlx); // ��������
		add_qyjbxx_jjlx.setPrompt("��ѡ�񾭼�����");
		gkwry_spinner = (Spinner) findViewById(R.id.gkwry_spinner);
		gkfxy_spinner = (Spinner) findViewById(R.id.gkfxy_spinner);
		gkylb_spinner = (Spinner) findViewById(R.id.gkylb_spinner);
		sswqwdlqy_spinner = (Spinner) findViewById(R.id.sswqwdlqy_spinner);
		gkwry_spinner.setPrompt("��ѡ���Ƿ�Ϊ������ȾԴ");
		gkfxy_spinner.setPrompt("��ѡ���Ƿ�Ϊ���ط���Դ");
		gkylb_spinner.setPrompt("��ѡ�����Դ���");
		sswqwdlqy_spinner.setPrompt("��ѡ���Ƿ�Ϊ30��ǧ�ߵ糧��ҵ");
		company_name_et = (EditText) findViewById(R.id.company_name_et);// ��ҵ����
		formerly_known_as_et = (EditText) findViewById(R.id.formerly_known_as_et);// ������
		// enterprise_code_et=(EditText)
		// findViewById(R.id.enterprise_code_et);//��ҵ����
		business_address_et = (EditText) findViewById(R.id.business_address_et);// ��ҵ��ַ
		business_qywz_et = (EditText) findViewById(R.id.business_qywz_et);// ��ҵ��ַ
		enterprise_yzbm_et = (EditText) findViewById(R.id.enterprise_yzbm_et);// ��������
		legal_representative_et = (EditText) findViewById(R.id.legal_representative_et);// ����������
		enterprise_czhm_et = (EditText) findViewById(R.id.enterprise_czhm_et);// �������
		environmental_contacts_et = (EditText) findViewById(R.id.environmental_contacts_et);// ������ϵ��
		environmental_contacts_phone_et = (EditText) findViewById(R.id.environmental_contacts_phone_et);// ������ϵ�˵绰
		enterprise_frdbdh_et = (EditText) findViewById(R.id.enterprise_frdbdh_et);// ��ϵ�绰
		enterprise_frdb_et = (EditText) findViewById(R.id.enterprise_frdb_et);// ���˴���
		enterprise_hblxrlxdz_et = (EditText) findViewById(R.id.enterprise_hblxrlxdz_et);// ������ϵ�˵�ַ
		enterprise_yyzzdm_et = (EditText) findViewById(R.id.enterprise_yyzzdm_et);// Ӫҵִ�մ���
		enterprise_zzjgdm_et = (EditText) findViewById(R.id.enterprise_zzjgdm_et);// ��֯��������
		add_qyjbxx_qygs = (EditText) findViewById(R.id.add_qyjbxx_qygs);// ��ҵ����
																		// ----
																		// ��ȾԴ����
		add_qyjbxx_bz = (EditText) findViewById(R.id.add_qyjbxx_bz);// ��ע

		add_qyjbxx_ssjd = (EditText) findViewById(R.id.add_qyjbxx_ssjd);

		add_qyjbxx_sfzdwry2 = (Button) findViewById(R.id.add_qyjbxx_sfzdwry2);
		// wsc
		String[] aryComName = null;
		Cursor data_cursor = null;
		int i = 0;
		try {

			String sql = "select s.[NAME],s.[CODE],s.[PID] from Industry s where s.code not in(select distinct  s1_parent.code"
					+ " from Industry s1_parent,Industry s2 where 1=1 and s1_parent.code = s2.pid)";
			data_cursor = SqliteUtil.getInstance().queryBySql(sql);
			aryComName = new String[data_cursor.getCount()];
			while (data_cursor.moveToNext()) {
				aryComName[i] = data_cursor.getString(0);
				i++;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AddBusinessActivity.this,
					android.R.layout.simple_dropdown_item_1line, aryComName);
			enterprise_hymc_et = (AutoCompleteTextView) findViewById(R.id.enterprise_hymc_et);// ��ҵ����
			enterprise_hymc_et.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (data_cursor != null) {
				data_cursor.close();
			}
		}

		enterprise_ztz_et = (EditText) findViewById(R.id.enterprise_ztz_et);// ��Ͷ��
		enterprise_hbtz_et = (EditText) findViewById(R.id.enterprise_hbtz_et);// ����Ͷ��
		// enterprise_qygm_et = (EditText)
		// findViewById(R.id.enterprise_qygm_et);//��ҵ��ģ
		enterprise_kysj_et = (EditText) findViewById(R.id.enterprise_kysj_et);// ��ҵʱ��
		enterprise_jcsj_et = (EditText) findViewById(R.id.enterprise_jcsj_et);// ����ʱ��
		enterprise_nscsj_et = (EditText) findViewById(R.id.enterprise_nscsj_et);// ������ʱ��
		enterprise_zzdmj_et = (EditText) findViewById(R.id.enterprise_zzdmj_et);// ��ռ�����
		enterprise_szgyymc_et = (EditText) findViewById(R.id.enterprise_szgyymc_et);// ���ڹ�ҵ԰����
		enterprise_zxgkjsj_et = (EditText) findViewById(R.id.enterprise_zxgkjsj_et);// ���¸�����ʱ��
		enterprise_tysj_et = (EditText) findViewById(R.id.enterprise_tysj_et);// ����ͣҵʱ��
		enterprise_pwxkzbh_et = (EditText) findViewById(R.id.enterprise_pwxkzbh_et);// �������֤���
		add_qyjbxx_pwsbdm = (EditText) findViewById(R.id.add_qyjbxx_pwsbdm);
		add_qyjbxx_qygs3_father=(LinearLayout) findViewById(R.id.add_qyjbxx_qygs3_father);
		add_qyjbxx_sczt = (Button) findViewById(R.id.add_qyjbxx_sczt);// �ɲ�״̬
		add_qyjbxx_kzjb = (Button) findViewById(R.id.add_qyjbxx_kzjb);// ���Ƽ���
		add_qyjbxx_qygs2=(Button) findViewById(R.id.add_qyjbxx_qygs2);//�Ƿ���ʡֱ����ҵ
		add_qyjbxx_qygs3=(Button) findViewById(R.id.add_qyjbxx_qygs3);//��ҵ����
		// enterprise_zdwry_et = (EditText)
		// findViewById(R.id.enterprise_zdwry_et);//�����ص���ȾԴ
		// enterprise_sczt_et = (EditText)
		// findViewById(R.id.enterprise_sczt_et);//����״̬
		longitude_et = (EditText) findViewById(R.id.longitude_et);// ����
		latitude_et = (EditText) findViewById(R.id.latitude_et);// γ��

		enterprise_kysj_et.setOnClickListener(new kysjListener());
		enterprise_jcsj_et.setOnClickListener(new JcsjListener());
		enterprise_zxgkjsj_et.setOnClickListener(new kjListener());
		enterprise_tysj_et.setOnClickListener(new TyListener());
		add_qyjbxx_sczt.setOnClickListener(new scztListener());
		add_qyjbxx_kzjb.setOnClickListener(new kzjbListener());
		add_qyjbxx_sfzdwry2.setOnClickListener(new ZdwryListener());
		add_qyjbxx_qygs2.setOnClickListener(new QygsListener());
		add_qyjbxx_qygs3.setOnClickListener(new Qygs3Listener());
	}

	class CheckBoxClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			final StringBuilder sb = new StringBuilder();
			String lbStr = control_category_btn.getText().toString();
			String lbs[] = new String[] {};
			if (lbStr != null && !lbStr.equals("")) {
				lbs = lbStr.split(",");
				for (int i = 0; i < controlNames.length; i++) {
					for (int k = 0; k < lbs.length; k++) {
						if (controlNames[i].equals(lbs[k])) {
							areaState[i] = true;
						}
					}
				}
			}
			AlertDialog ad = new AlertDialog.Builder(AddBusinessActivity.this)
					.setTitle("��ѡ����ȾԴ���")
					.setMultiChoiceItems(controlNames, areaState,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
									// ���ĳ������
								}
							})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String s = "��ѡ����:";
									for (int i = 0; i < controlNames.length; i++) {
										if (areaCheckListView
												.getCheckedItemPositions().get(
														i)) {
											s += i
													+ ":"
													+ areaCheckListView
															.getAdapter()
															.getItem(i) + "  ";
											sb.append(areaCheckListView
													.getAdapter().getItem(i)
													+ ",");
										} else {
											areaCheckListView
													.getCheckedItemPositions()
													.get(i, false);
										}
									}
									if (areaCheckListView
											.getCheckedItemPositions().size() > 0) {
										// Toast.makeText(AddBusinessActivity.this,
										// s,
										// Toast.LENGTH_LONG).show();
									} else {
										// û��ѡ��
									}
									dialog.dismiss();
									String sbStr = sb.toString();
									if (sbStr.equals("")) {
										control_category_btn.setText("");
									} else {
										control_category_btn.setText(sbStr
												.substring(0,
														sbStr.length() - 1));
									}

								}
							}).setNegativeButton("ȡ��", null).create();
			areaCheckListView = ad.getListView();
			ad.show();
		}
	}

	public ArrayList<TaskFile> getAllUploadFile(String fk_id) {
		ArrayList<TaskFile> _ListFile = new ArrayList<TaskFile>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		// condition.put("FK_Unit", fk_unit + "");
		condition.put("fk_id", fk_id);
		ArrayList<HashMap<String, Object>> fileLists = SqliteUtil.getInstance()
				.getList(" * ", condition, "T_Attachment");
		if (fileLists != null || fileLists.size() > 0) {

			for (HashMap<String, Object> map : fileLists) {
				TaskFile taskFile = new TaskFile();
				String absolutePath = Global.SDCARD_RASK_DATA_PATH
						+ "Attach/"
						+ T_Attachment.transitToChinese(Integer.parseInt(map
								.get("fk_unit").toString())) + "/"
						+ map.get("filepath").toString();
				taskFile.setGuid(map.get("guid").toString());
				taskFile.setFileName(map.get("filename").toString());
				taskFile.setFilePath(map.get("filepath").toString());
				taskFile.setAbsolutePath(absolutePath);
				taskFile.setUnitId(map.get("fk_unit").toString());
				taskFile.setExtension(map.get("extension").toString());
				_ListFile.add(taskFile);
			}
		}
		return _ListFile;
	}

	/** ��Ӱ�ť�����¼� */
	public void add_btn(View view) {

		ArrayList<TaskFile> taskFile = getAllUploadFile(guid);
		if (taskFile != null && taskFile.size() > 0) {
			// UploadFile uploadFile = new UploadFile();
			// guid byk
			// uploadFile.upLoadFilesMethod(taskFile, handlertake,
			// AddBusinessActivity.this);

			upLoadFilesMethod(taskFile, AddBusinessActivity.this, "");
		} else {
			handlertake.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
		}

		// addtakeinformation();

	}

	private void addtakeinformation() {
		
		if("".equals(company_name_et_str)||"".equals(business_address_et_str)||"".equals(legal_representative_et_str)
				||"".equals(environmental_contacts_et_str)||"".equals(environmental_contacts_phone_et_str)||"".equals(enterprise_yyzzdm_et_str)||
				"".equals(enterprise_zzjgdm_et.getText().toString())||"".equals(select_the_city.getSelectedItem().toString())||"".equals(select_the_county.getSelectedItem().toString())||
				"".equals("".equals(select_the_ssxz.getSelectedItem().toString()))||"".equals(control_category_str)||"".equals(longitude_et_str)||"".equals(latitude_et_str)||"".equals(add_qyjbxx_sczt_str)
				){
			//���ñ���
			Message msg = handler.obtainMessage();
			msg.arg1 = CAN_NOT_BE_EMPTY;
			handler.sendMessage(msg);
			return;
			
			
		}
		
		
		progressDialog = new ProgressDialog(AddBusinessActivity.this);
		progressDialog.setMessage("�����ύ����,�����Ժ�...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		try {
			new Thread(new Runnable() {

				private String add_qyjbxx_hylx_str1;

				@Override
				public void run() {
					/** ��ÿ������Ĵ��� */

					String lbStr = control_category_btn.getText().toString();
					String lbs[] = new String[] {};
					StringBuilder builder = new StringBuilder();
					if (lbStr != null && !lbStr.equals("")) {
						lbs = lbStr.split(",");
						for (int i = 0; i < lbs.length; i++) {
							builder.append((SqliteUtil.getInstance()
									.queryBySqlReturnArrayListHashMap("select code from WRYLB where name='"
											+ lbs[i] + "'")).get(0).get("code")
									.toString()
									+ ",");
						}
						String str = builder.toString();
						if (str != null && !str.equals("")) {
							control_category_str = str.substring(0,
									str.length() - 1);
						} else {
							control_category_str = "";
						}

					} else {
						control_category_str = "";
					}

					/** �����ҵ��ģ�Ĵ��� */
					qygm_spinner_str = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select code from wrygm where name='"
											+ qygm_spinner.getSelectedItem()
													.toString() + "'").get(0)
							.get("code").toString();
					// ��ҵ����
					String selectHylxName = add_qyjbxx_hylx.getSelectedItem()
							.toString();

					String[] split = selectHylxName.split("----");
					// add_qyjbxx_hylx_str = SqliteUtil.getInstance()
					// .queryBySqlReturnArrayListHashMap("select code from industry where name='"
					// + split[1] + "'").get(0).get("code")
					// .toString();
					add_qyjbxx_hylx_str = split[0];
					add_qyjbxx_hylx_str1 = split[1];
					/** �����ҵ���͵Ĵ��� */
					add_qyjbxx_jjlx_str = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select code from jjlx where name='"
											+ add_qyjbxx_jjlx.getSelectedItem()
													.toString() + "'").get(0)
							.get("code").toString();

					/** ��ù�����ȾԴ */
					if ("��".equals(gkwry_spinner.getSelectedItem().toString()
							.trim())) {
						gkwry_spinner_str = "1";
					} else {
						gkwry_spinner_str = "0";
					}
					/** ��ù�����ȾԴ */
					if ("��".equals(gkfxy_spinner.getSelectedItem().toString()
							.trim())) {
						gkfxy_spinner_str = "1";
					} else {
						gkfxy_spinner_str = "0";
					}

					if ("��".equals(sswqwdlqy_spinner.getSelectedItem()
							.toString().trim())) {
						sswqwdlqy_spinner_str = "1";
					} else {
						sswqwdlqy_spinner_str = "0";
					}

					final ContentValues contentValues = new ContentValues();
					if (qydm == null || qydm.equals("")) {// �����������ҵ ���Զ�����GUID

						enterprise_code_et_str = guid;
					} else {
						// enterprise_code_et_str=enterprise_code_et.getText().toString();//��ҵ����
						enterprise_code_et_str = qydm;// ��ҵ����
					}

					company_name_et_str = company_name_et.getText().toString();// ��ҵ����
					formerly_known_as_et_str = formerly_known_as_et.getText()
							.toString();// ������
					business_address_et_str = business_address_et.getText()
							.toString();// ��ҵ��ַ
					business_qywz_et_str = business_qywz_et.getText()
							.toString();// ��ҵ��ַ
					enterprise_yzbm_et_str = enterprise_yzbm_et.getText()
							.toString();// ��������
					legal_representative_et_str = legal_representative_et
							.getText().toString();// ����������
					enterprise_czhm_et_str = enterprise_czhm_et.getText()
							.toString();// �������
					environmental_contacts_et_str = environmental_contacts_et
							.getText().toString();// ������ϵ��
					environmental_contacts_phone_et_str = environmental_contacts_phone_et
							.getText().toString();// ������ϵ�˵绰
					enterprise_frdbdh_et_str = enterprise_frdbdh_et.getText()
							.toString();// ��ϵ�绰
					enterprise_frdb_et_str = enterprise_frdb_et.getText()
							.toString();// ���˴���
					add_qyjbxx_qygs_str = add_qyjbxx_qygs.getText().toString()
							.trim();// ��ҵ����
					add_qyjbxx_bz_str = add_qyjbxx_bz.getText().toString()
							.trim();// ��ע

					add_qyjbxx_ssjd_str = add_qyjbxx_ssjd.getText().toString()
							.trim();
					enterprise_hblxrlxdz_et_str = enterprise_hblxrlxdz_et
							.getText().toString();// ������ϵ�˵�ַ
					enterprise_yyzzdm_et_str = enterprise_yyzzdm_et.getText()
							.toString();// Ӫҵִ�մ���
					enterprise_hymc_et_str = enterprise_hymc_et.getText()
							.toString();// ��ҵ����,��ҵ���
					enterprise_ztz_et_str = enterprise_ztz_et.getText()
							.toString();// ��Ͷ��
					enterprise_hbtz_et_str = enterprise_hbtz_et.getText()
							.toString();// ����Ͷ��
					// enterprise_qygm_et_str=enterprise_qygm_et.getText().toString();//��ҵ��ģ
					// ��ҵ���ƺʹ���ת��
					if (!"".equals(enterprise_hymc_et_str)) {
						List<HashMap<String, Object>> queryCode = new ArrayList<HashMap<String, Object>>();
						queryCode = SqliteUtil.getInstance()
								.queryBySqlReturnArrayListHashMap(
										"select t.code from Industry t  where t.name='"
												+ enterprise_hymc_et_str + "'");

						if (queryCode != null && queryCode.size() > 0) {
							enterprise_hymc_et_str = queryCode.get(0)
									.get("code").toString();
						}
					}

					enterprise_kysj_et_str = enterprise_kysj_et.getText()
							.toString();// ��ҵʱ��
					enterprise_jcsj_et_str = enterprise_jcsj_et.getText()
							.toString();// ����ʱ��
					enterprise_nscsj_et_str = enterprise_nscsj_et.getText()
							.toString();// ������ʱ��
					enterprise_zzdmj_et_str = enterprise_zzdmj_et.getText()
							.toString();// ��ռ�����
					enterprise_szgyymc_et_str = enterprise_szgyymc_et.getText()
							.toString();// ���ڹ�ҵ԰����
					enterprise_zxgkjsj_et_str = enterprise_zxgkjsj_et.getText()
							.toString();// ���¸�����ʱ��
					enterprise_tysj_et_str = enterprise_tysj_et.getText()
							.toString();// ͣҵʱ��
					enterprise_pwxkzbh_et_str = enterprise_pwxkzbh_et.getText()
							.toString();// �������֤���
					add_qyjbxx_pwsbdm_str = add_qyjbxx_pwsbdm.getText()
							.toString();// ������ɵǼǺ�
					// enterprise_zdwry_et_str=enterprise_zdwry_et.getText().toString();//�����ص���ȾԴ
					// enterprise_sczt_et_str=enterprise_sczt_et.getText().toString();//����״̬
					longitude_et_str = longitude_et.getText().toString();// ����
					latitude_et_str = latitude_et.getText().toString();// γ��

					/** ���������� */
					contentValues.put("wrylb", control_category_str);
					/** �������������Ĵ��� */
					contentValues.put("xzqh", xzqh);
					contentValues.put("qymc", company_name_et_str);// ��ҵ����
					contentValues.put("cym", formerly_known_as_et_str);// ������
					contentValues.put("qydm", enterprise_code_et_str);// ��ҵ����
					guid = enterprise_code_et_str;
					contentValues.put("guid", guid);
					contentValues.put("qydz", business_address_et_str);// ��ҵ��ַ
					contentValues.put("wzh", business_qywz_et_str);// ��ҵ��ַ
					contentValues.put("yzbm", enterprise_yzbm_et_str);// ��������
					contentValues.put("frdb", legal_representative_et_str);// ����������
					contentValues.put("czhm", enterprise_czhm_et_str);// �������
					contentValues.put("hblxr", environmental_contacts_et_str);// ������ϵ��
					contentValues.put("hblxrdh",
							environmental_contacts_phone_et_str);// ������ϵ�˵绰
					contentValues.put("frdm", enterprise_frdb_et_str);// ���˴���
					contentValues.put("hblxrlxdz", enterprise_hblxrlxdz_et_str);// ������ϵ�˵�ַ
					contentValues.put("yyzzdm", enterprise_yyzzdm_et_str);// Ӫҵִ�մ���
					contentValues.put("zzjgdm", enterprise_zzjgdm_et.getText()
							.toString());// ��֯��������
				//	contentValues.put("hylb", enterprise_hymc_et_str);// ��ҵ����,��ҵ���
					contentValues.put("ztz", enterprise_ztz_et_str);// ��Ͷ��
					contentValues.put("hbtz", enterprise_hbtz_et_str);// ����Ͷ��
					contentValues.put("lxdh", enterprise_frdbdh_et_str);// ��ϵ�绰
					contentValues.put("qygm", qygm_spinner_str);// ��ҵ��ģ
					contentValues.put("hylb", add_qyjbxx_hylx_str);// ��ҵ����
					contentValues.put("jjlx", add_qyjbxx_jjlx_str);// ��������

					if ("����".equals(add_qyjbxx_sczt_str.trim())) {
						contentValues.put("sczt", 1);// ����״̬
					} else {
						contentValues.put("sczt", 0);// ����״̬
					}

					contentValues.put("kysj", enterprise_kysj_et_str);// ��ҵʱ��
					contentValues.put("jcsj", enterprise_jcsj_et_str);// ����
					contentValues.put("nscsj", enterprise_nscsj_et_str);// ������ʱ��
					contentValues.put("zzdmj", enterprise_zzdmj_et_str);// ��ռ�����
					contentValues.put("szgyymc", enterprise_szgyymc_et_str);// ���ڹ�ҵ԰����
					contentValues.put("zxgkjsj", enterprise_zxgkjsj_et_str);// ���¸�����ʱ��
					contentValues.put("tysj", enterprise_tysj_et_str);// ͣҵʱ��
					contentValues.put("pwxkzbh", enterprise_pwxkzbh_et_str);// �������֤���

					contentValues.put("pwsbdm", add_qyjbxx_pwsbdm_str);
					// contentValues.put("zdwry", gkwry_spinner_str);// �����ص���ȾԴ
					contentValues.put("ybgyy", gkfxy_spinner_str);
					// contentValues.put("qxsj", enterprise_qxsj_et_str);//
					// ע��ʱ��//
					contentValues.put("jd", longitude_et_str);// ����
					contentValues.put("wd", latitude_et_str);// γ��
					// contentValues.put("ssly", belongs_watershed_et_str);//
					// ��������
					// ��ҵ����
					contentValues.put("wryjs", add_qyjbxx_qygs_str);
					contentValues.put("bz", add_qyjbxx_bz_str);

					contentValues.put("ssjd", add_qyjbxx_ssjd_str);
					contentValues.put("gkylb", selected_kzjb);

					contentValues.put("zdwry", selected_zdwry);
					contentValues.put("tcrq", enterprise_jcsj_et_str);
					// contentValues.put("ismainpollutionsource","");
					// contentValues.put("controulerevel","");
					// ��ȡ��ַ��code ��ȡ�����code
					String sql_ssds = "select RegionCode from region where RegionName = '"
							+ select_the_city.getSelectedItem().toString()
							+ "'";
					String ssdsCode = SqliteUtil.getInstance()
							.getDepidByDepName(sql_ssds);
					contentValues.put("ssds", ssdsCode);// ��������

					String sql_ssqx = "select RegionCode from region where RegionName = '"
							+ select_the_county.getSelectedItem().toString()
							+ "'";
					String ssqxCode = SqliteUtil.getInstance()
							.getDepidByDepName(sql_ssqx);
					contentValues.put("ssqx", ssqxCode);// ��������
					//��������
					String sql_ssxz = "select RegionCode from region where RegionName = '"
							+ select_the_ssxz.getSelectedItem().toString()
							+ "'";
					String ssxzCode = SqliteUtil.getInstance()
							.getDepidByDepName(sql_ssxz);
					contentValues.put("ssxz", ssxzCode);// ��������
					
					
					
					if (add_qyjbxx_qygs2.getText()=="��"||add_qyjbxx_qygs2.getText().equals("��")) {
						contentValues.put("gxgs","230000000");
					}else {
						contentValues.put("gxgs",add_qyjbxx_qygs3.getTag().toString().trim());
					}

					// ��ȡ����Դ���ͺ�30��ǧ�ߵ糧�ֶ�
					// contentValues.put("gkylb",
					// getGKYLBCode(gkylb_spinner.getSelectedItem().toString().trim()));
					// // ����Դ���
					// contentValues.put("sswqwdlqy", sswqwdlqy_spinner_str);//
					// 30��ǧ�ߵ�����ҵ

					List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
					try {
						token = DESSecurity.encrypt("AddNewEnt");
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					HashMap<String, Object> map = getServiceParams();

					ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					list.add(map);
					String jsonStr = JsonHelper.listToJSON(list);
					HashMap<String, Object> param = new HashMap<String, Object>();

					param.put("token", token);
					param.put("entInfoJson", jsonStr);

					params.add(param);

					if (Net.checkNet(AddBusinessActivity.this)) {
						try {

							if (xzqh != ""
									&& !enterprise_code_et_str.equals("")
									&& !company_name_et_str.equals("")) {// �ж��Ƿ���д��ҵ���룬��ҵ���ƺ���������
								// TODO ������ȾԴ�����Ͳ���Ϊ
								if (!"".equals(control_category_btn.getText()
										.toString())
										|| !"".equals(business_address_et
												.getText().toString())
										|| !"".equals(latitude_et.getText()
												.toString())
										|| !"".equals(longitude_et.getText()
												.toString())
										|| !"".equals(legal_representative_et
												.getText().toString())
										|| !"".equals(environmental_contacts_et
												.getText().toString())
										|| !"".equals(environmental_contacts_phone_et
												.getText().toString())
										|| !"".equals(enterprise_zzjgdm_et
												.getText().toString())
										|| !"".equals(enterprise_yyzzdm_et
												.getText().toString())
										|| !"".equals(add_qyjbxx_sczt.getText()
												.toString())) {

									if (qydm == null) {// ������ҵ�жϴ����Ƿ��ظ�
										if (isQydmExist(enterprise_code_et_str)) {// ��ҵ�����ظ�
											Message msg = handler
													.obtainMessage();
											msg.arg1 = CORPORATE_CODE_DUPLICATION;
											handler.sendMessage(msg);
											return;
										}
									}
								}

							} else {// ������û��
								Message msg = handler.obtainMessage();
								msg.arg1 = CAN_NOT_BE_EMPTY;
								handler.sendMessage(msg);
								return;
							}

							// TODO ���������ҵ�ӿ�
							String result = null;
							if (qydm != null && !qydm.equals("")) {
								result = String.valueOf(WebServiceProvider
										.callWebService(
												Global.NAMESPACE,
												"AddEntMobile",
												params,
												Global.getGlobalInstance()
														.getSystemurl()
														+ Global.WEBSERVICE_URL,
												WebServiceProvider.RETURN_STRING,
												true));
							} else {
								result = String.valueOf(WebServiceProvider
										.callWebService(
												Global.NAMESPACE,
												"AddEntMobile",
												params,
												Global.getGlobalInstance()
														.getSystemurl()
														+ Global.WEBSERVICE_URL,
												WebServiceProvider.RETURN_STRING,
												true));
							}
							if (result == null || "null".equals(result)||"".equals(result)) {

								Message msg = handler.obtainMessage();
								msg.arg1 = ADD_BUSINESS_FAILURE;
								handler.sendMessage(msg);
								return;

							}
							if (result.equals("-1")) {
								Message msg = handler.obtainMessage();
								msg.arg1 = THE_COMPANY_ALREADY_EXISTS;
								handler.sendMessage(msg);

							} else if (result.equals("1")) {
								if (isAdd) {

									contentValues.put("id", result);
									// ������ҵ;
									long count = 0;
									String sczt = "0";
									try {
										if (add_qyjbxx_sczt_str.trim().equals(
												"����")) {
											sczt = "1";
										}

								
										count = SqliteUtil.getInstance()
												.insert(contentValues,
														"t_wry_qyjbxx");

									} catch (Exception e) {
										e.printStackTrace();
										Message msg = handler.obtainMessage();
										msg.arg1 = DATABASE_INSERT_EXCEPTION;
										handler.sendMessage(msg);
										return;
									}
									Message msg = handler.obtainMessage();
									msg.arg1 = DATA_INSERT_SUCCESS;
									handler.sendMessage(msg);
									return;

								} else {
									String[] qydmArr = { qydm };
									if (qydm != null) {
										SqliteUtil.getInstance().update(
												"t_wry_qyjbxx", contentValues,
												"qydm=?", qydmArr);
									}
									Message msg = handler.obtainMessage();
									msg.arg1 = DATA_INSERT_SUCCESS;
									handler.sendMessage(msg);
									return;

								}

							} else if (result != null && !"null".equals(result)) {
								/* try{ */
								if (Integer.valueOf(result).intValue() > 1) {
									if (isAdd) {

										contentValues.put("id", result);
										// ������ҵ;
										long count = 0;
										try {
											count = SqliteUtil.getInstance()
													.insert(contentValues,
															"t_wry_qyjbxx");

										} catch (Exception e) {
											e.printStackTrace();
											Message msg = handler
													.obtainMessage();
											msg.arg1 = DATABASE_INSERT_EXCEPTION;
											handler.sendMessage(msg);
											return;
										}
										Message msg = handler.obtainMessage();
										msg.arg1 = DATA_INSERT_SUCCESS;
										handler.sendMessage(msg);
										return;

									}

								} else {

									Message msg = handler.obtainMessage();
									msg.arg1 = ADD_BUSINESS_FAILURE;
									handler.sendMessage(msg);
								}

								/*
								 * }catch(Exception e){ e.printStackTrace(); }
								 */
							}
						} catch (IOException e) {
							e.printStackTrace();
							Message msg = handler.obtainMessage();
							msg.arg1 = ADD_BUSINESS_FAILURE;
							handler.sendMessage(msg);

						}
					} else {
						Message msg = handler.obtainMessage();
						msg.arg1 = NETWORK_NOT_CONNECTED;
						handler.sendMessage(msg);
					}
				}

			}).start();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	private HashMap<String, Object> getServiceParams() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//������ҵ��������
		if (add_qyjbxx_qygs2.getText()=="��"||add_qyjbxx_qygs2.getText().equals("��")) {
			map.put("gxgs","230000000");
		}else {
			map.put("gxgs",add_qyjbxx_qygs3.getTag().toString().trim());
		}
		
		/** ���������� */
		map.put("wrylb", control_category_str);
		/** ������ҵ��ģ */
		map.put("qygm", qygm_spinner_str);
		// ��ҵ����
		map.put("hylb", add_qyjbxx_hylx_str);
		// ��������
		map.put("jjlx", add_qyjbxx_jjlx_str);
		/** ��������״̬ */
		// map.put("sczt", add_qyjbxx_sczt_str);
		if ("����".equals(add_qyjbxx_sczt_str.trim())) {
			map.put("sczt", 1);// ����״̬
		} else {
			map.put("sczt", 0);// ����״̬
		}
		/** �������������Ĵ��� */
		map.put("xzqh", xzqh);
		map.put("qymc", company_name_et_str);// ��ҵ����
		map.put("cym", formerly_known_as_et_str);// ������
		map.put("qydm", enterprise_code_et_str);// ��ҵ����
		guid = enterprise_code_et_str;
		map.put("guid", guid);
		map.put("qydz", business_address_et_str);// ��ҵ��ַ
		map.put("wzh", business_qywz_et_str);// ��ҵ��ַ
		map.put("yzbm", enterprise_yzbm_et_str);// ��������
		map.put("frdb", legal_representative_et_str);// ����������
		map.put("hblxr", environmental_contacts_et_str);// ������ϵ��
		map.put("czhm", enterprise_czhm_et_str);// ������ϵ��
		map.put("hblxrdh", environmental_contacts_phone_et_str);// ������ϵ�˵绰
		map.put("frdm", enterprise_frdb_et_str);
		map.put("lxdh", enterprise_frdbdh_et_str);// ��ϵ�绰
		map.put("hblxrlxdz", enterprise_hblxrlxdz_et_str);// ������ϵ�˵�ַ
		map.put("yyzzdm", enterprise_yyzzdm_et_str);// Ӫҵִ�մ���
		map.put("zzjgdm", enterprise_zzjgdm_et.getText().toString());
		// map.put("hylb", enterprise_hymc_et_str);// ��ҵ����,��ҵ���
		map.put("ztz", enterprise_ztz_et_str);// ��Ͷ��
		map.put("hbtz", enterprise_hbtz_et_str);// ����Ͷ��
		// map.put("qygm", enterprise_qygm_et_str);//��ҵ��ģ
		map.put("kysj", enterprise_kysj_et_str);// ��ҵʱ��
		map.put("nscsj", enterprise_nscsj_et_str);// ������ʱ��
		map.put("zzdmj", enterprise_zzdmj_et_str);// ��ռ�����
		map.put("szgyymc", enterprise_szgyymc_et_str);// ���ڹ�ҵ԰����
		map.put("zxgkjsj", enterprise_zxgkjsj_et_str);// ���¸�����ʱ��
		map.put("tysj", enterprise_tysj_et_str);// ͣҵʱ��
		map.put("pwxkzbh", enterprise_pwxkzbh_et_str);// �������֤���

		map.put("pwsbdm", add_qyjbxx_pwsbdm_str);
		map.put("zdwry", gkwry_spinner_str);// �����ص���ȾԴ
		map.put("ybgyy", gkfxy_spinner_str);//
		// map.put("qxsj", enterprise_qxsj_et_str);// ע��ʱ��

		map.put("tcrq", enterprise_jcsj_et_str); // ����ʱ��

		map.put("jd", longitude_et_str);// ����
		map.put("wd", latitude_et_str);// γ��
		map.put("wryjs", add_qyjbxx_qygs_str);// ��ҵ����
		map.put("bz", add_qyjbxx_bz_str);// ��ע
		map.put("ssjd", add_qyjbxx_ssjd_str);
		map.put("gkylb", selected_kzjb);// ���Ƽ���
		map.put("zdwry", selected_zdwry);// ���Ƽ���
		map.put("sssf", "230000000");
		// map.put("ssly", belongs_watershed_et_str);// ��������
		// �޸Ĵ���
		// ��ȡ��ַ��code ��ȡ�����code
		String sql_ssds = "select RegionCode from region where RegionName = '"
				+ select_the_city.getSelectedItem().toString() + "'";
		String ssdsCode = SqliteUtil.getInstance().getDepidByDepName(sql_ssds);

		String sql_ssqx = "select RegionCode from region where RegionName = '"
				+ select_the_county.getSelectedItem().toString() + "'";
		String ssqxCode = SqliteUtil.getInstance().getDepidByDepName(sql_ssqx);
		
		String sql_ssxz = "select RegionCode from region where RegionName = '"
				+ select_the_ssxz.getSelectedItem().toString() + "'";
		String ssxzCode = SqliteUtil.getInstance().getDepidByDepName(sql_ssxz);

		map.put("ssds", ssdsCode);// ��������
		map.put("ssqx", ssqxCode);// ��������
		map.put("ssxz", ssxzCode);// ��������

		// map.put("gkylb",
		// getGKYLBCode(gkylb_spinner.getSelectedItem().toString().trim())); //
		// ����Դ���
		// map.put("sswqwdlqy", sswqwdlqy_spinner_str);// 30��ǧ�ߵ�����ҵ

		return map;
	}

	/**
	 * ��ȡ����Դ�����
	 */

	private String getGKYLBCode(String name) {
		String code = "1";
		ArrayList<HashMap<String, Object>> CodeDataList = SqliteUtil
				.getInstance().queryBySqlReturnArrayListHashMap(
						"select Code from gkylb where name = '" + name + "'");
		if (CodeDataList != null && CodeDataList.size() > 0) {
			code = CodeDataList.get(0).get("code").toString();
		}
		return code;
	}

	/**
	 * Description:��ȡ����
	 * 
	 * @param context
	 * @return λ��
	 * @author Administrator Create at: 2012-11-30 ����10:14:53
	 */
	public Location getLocation(Context context) {
		if (Manager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = Manager.getBestProvider(criteria, true);
			location = Manager.getLastKnownLocation(provider);
			if (location == null) {
				return location;
			}
		} else {
			Toast.makeText(getApplicationContext(), "GPS�����ã����GPS",
					Toast.LENGTH_SHORT).show();
			return location;
		}
		return location;
	}

	public void updateLocation(Location location) {
		this.location = location;
	}

	/**
	 * λ�ü�����
	 */
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}
	};

	/**
	 * �ж���ҵ�����ǲ����Ѵ��� ��ҵ����
	 * 
	 * @return true �����ظ���ҵ����
	 */
	public Boolean isQydmExist(String qydm) {

		ArrayList<HashMap<String, Object>> qyData = new ArrayList<HashMap<String, Object>>();
		qyData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select qydm from t_wry_qyjbxx where qydm='" + qydm + "'");

		if (qyData != null && qyData.size() > 0) {

			return true;
		}
		return false;

	}

	/**
	 * ��ѯ����������code
	 * 
	 * @param childCode
	 * @return
	 */
	public String getParentcode(String childCode) {
		String ParentCode = "";
		ArrayList<HashMap<String, Object>> ParentCodeDataList = SqliteUtil
				.getInstance().queryBySqlReturnArrayListHashMap(
						"select ParentCode from region where RegionCode = '"
								+ childCode + "'");
		if (ParentCodeDataList != null && ParentCodeDataList.size() > 0) {
			ParentCode = ParentCodeDataList.get(0).get("parentcode").toString();
		}
		return ParentCode;
	}

	/**
	 * ���ݸ���������ID����ѯ�����е��Լ���������ID�����Ƽ���
	 * 
	 * @param parentcode
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getChildCityData(String parentcode) {
		/** ��ѯ���е����� */
		ArrayList<HashMap<String, Object>> cityDataList = SqliteUtil
				.getInstance().queryBySqlReturnArrayListHashMap(
						"select RegionCode,RegionName from region where parentcode = '"
								+ parentcode + "' order by RegionCode");

		return cityDataList;
	}

	/**
	 * �����Լ�����ID����ѯ�����е��Լ���������ID�����Ƽ���
	 * 
	 * @param parentcode
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getCityData(String regioncode) {
		/** ��ѯ���е����� */
		ArrayList<HashMap<String, Object>> cityDataList = SqliteUtil
				.getInstance().queryBySqlReturnArrayListHashMap(
						"select RegionCode,RegionName from region where RegionCode = '"
								+ regioncode + "' order by RegionCode");

		return cityDataList;
	}

	/**
	 * ��ʼ��ArrayAdapter������ʽ
	 * 
	 * @param AdapterData
	 * @return
	 */
	public ArrayAdapter<String> initSpinnerAdapter(ArrayList<String> AdapterData) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AddBusinessActivity.this, android.R.layout.simple_spinner_item,
				AdapterData);
		/** Spinner��������ʽ */
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/** ��ҵʱ������¼� **/
	private class kysjListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(AddBusinessActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							String monthStr = "";
							if (monthOfYear < 9) {
								monthStr = "0" + (monthOfYear + 1);
							} else {
								monthStr = (monthOfYear + 1) + "";
							}
							enterprise_kysj_et.setText(year + "-" + monthStr
									+ "-" + dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	String[] single_list = { "����", "ͣ��" };
	private String add_qyjbxx_sczt_str;

	/** ����״̬�����¼� **/
	private class scztListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					AddBusinessActivity.this);
			builder.setTitle("����״̬:");
			builder.setIcon(R.drawable.yutu);
			builder.setSingleChoiceItems(single_list, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							add_qyjbxx_sczt_str = single_list[which];

							add_qyjbxx_sczt.setText(add_qyjbxx_sczt_str);
							dialog.dismiss();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	String[] single_list2 = { "����", "ʡ��", "�п�" };
	private int selected_kzjb;
	private String add_qyjbxx_kzjb_str;
	private Button add_qyjbxx_sfzdwry;
	private Button add_qyjbxx_sfzdwry2;

	/** ���Ƽ�������¼� **/
	private class kzjbListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					AddBusinessActivity.this);
			builder.setTitle("���Ƽ���:");
			builder.setIcon(R.drawable.yutu);
			builder.setSingleChoiceItems(single_list2, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							add_qyjbxx_kzjb_str = single_list2[which];
							add_qyjbxx_kzjb.setText(add_qyjbxx_kzjb_str);
							selected_kzjb = which + 1;

							dialog.dismiss();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	String[] single_list3 = { "��", "��" };

	private String add_qyjbxx_qygs_s;
	private int selected_qygs;
	/** �Ƿ���ʡֱ����ҵ **/
	private class QygsListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					AddBusinessActivity.this);
			builder.setTitle("�Ƿ�Ϊʡֱ����ҵ:");
			builder.setIcon(R.drawable.yutu);
			builder.setSingleChoiceItems(single_list3, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							add_qyjbxx_qygs_s = single_list3[which];

							add_qyjbxx_qygs2
									.setText(add_qyjbxx_qygs_s);
							if (which == 0) {
								selected_qygs = 1;
								add_qyjbxx_qygs3_father.setVisibility(View.GONE);
							} else {
								selected_qygs = 0;
								add_qyjbxx_qygs3_father.setVisibility(View.VISIBLE);
							}

							dialog.dismiss();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}
	/** �Ƿ���ʡֱ����ҵ **/
	private class Qygs3Listener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			createDialogViewForTaskType();
		}
	}
	// ��������
		public void createDialogViewForTaskType() {
				LayoutInflater inflater = LayoutInflater.from(AddBusinessActivity.this);
				View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
				TextView expand_title_tv = (TextView) viewex.findViewById(R.id.expand_title_tv);
				expand_title_tv.setText("�������");
				ArrayList<HashMap<String, Object>> groupData = rwxx.getTask_type();
				//final ArrayList<HashMap<String, Object>> groupData = getRegionList("1");// ���е�ȫ����Ϣ
				final List<String> groupList = new ArrayList<String>();
				if (groupData != null && groupData.size() > 0) {
					for (int i = 0; i < groupData.size(); i++) {
						Object name = groupData.get(i).get("regionname");
						groupList.add(name+"");
					}
				}
				final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx.getTask_type_child(groupData);
				
				final ExpandableListView expandableListView = (ExpandableListView) viewex.findViewById(R.id.expandablelistview);
				ExpandableBaseAdapter adapter = new ExpandableBaseAdapter(this, groupList, childMapData);
				expandableListView.setAdapter(adapter);
				expandableListView.setCacheColorHint(0);// �����϶��б��ʱ���ֹ���ֺ�ɫ����
				expandableListView.setGroupIndicator(getResources().getDrawable(R.layout.expandablelistviewselector));

				AlertDialog.Builder builder = new AlertDialog.Builder(AddBusinessActivity.this);
				builder.setIcon(R.drawable.yutu);
				builder.setTitle("������ʡ");
				builder.setView(viewex);

				final AlertDialog dialog = builder.create();
				dialog.show();
				expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0; i < groupList.size(); i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
							}
						}
					}
				});
				expandableListView.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
						String cnames = childMapData.get(groupPosition).get(childPosition).get("regionname").toString();
						  String rwlxCode = childMapData.get(groupPosition).get(childPosition).get("regioncode").toString();
//						if (cnames.equals("��Ͻ��")) {
//							 rwlxCode=childMapData.get(groupPosition).get(childPosition).get("parentcode").toString();
//						}
						  if(v.getTag().equals("1")){
								rwlxCode=childMapData.get(groupPosition).get(childPosition).get("parentcode").toString();
								add_qyjbxx_qygs3.setText(groupList.get(groupPosition));
							}else {
								add_qyjbxx_qygs3.setText(cnames);
							}
						add_qyjbxx_qygs3.setTag(rwlxCode);

						dialog.cancel();
						return false;
					}
				});
			}
	private String add_qyjbxx_sfzdwry2_str;
	private int selected_zdwry;

	/** �Ƿ����ص���ȾԴ�����¼� **/
	private class ZdwryListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					AddBusinessActivity.this);
			builder.setTitle("�ص���ȾԴ:");
			builder.setIcon(R.drawable.yutu);
			builder.setSingleChoiceItems(single_list3, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							add_qyjbxx_sfzdwry2_str = single_list3[which];

							add_qyjbxx_sfzdwry2
									.setText(add_qyjbxx_sfzdwry2_str);
							if (which == 0) {
								selected_zdwry = 1;
							} else {
								selected_zdwry = 0;
							}

							dialog.dismiss();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	/** ����ʱ������¼� **/
	private class JcsjListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(AddBusinessActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							String monthStr = "";
							if (monthOfYear < 9) {
								monthStr = "0" + (monthOfYear + 1);
							} else {
								monthStr = (monthOfYear + 1) + "";
							}
							enterprise_jcsj_et.setText(year + "-" + monthStr
									+ "-" + dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	/** ��������ʱ������¼� **/
	private class kjListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(AddBusinessActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							String monthStr = "";
							if (monthOfYear < 9) {
								monthStr = "0" + (monthOfYear + 1);
							} else {
								monthStr = (monthOfYear + 1) + "";
							}
							enterprise_zxgkjsj_et.setText(year + "-" + monthStr
									+ "-" + dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	/** ���ͣҵʱ������¼� **/
	private class TyListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(AddBusinessActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							String monthStr = "";
							if (monthOfYear < 9) {
								monthStr = "0" + (monthOfYear + 1);
							} else {
								monthStr = (monthOfYear + 1) + "";
							}
							enterprise_tysj_et.setText(year + "-" + monthStr
									+ "-" + dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	/** ע��ʱ������¼� **/
	private class qxsjListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(AddBusinessActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							String monthStr = "";
							if (monthOfYear < 9) {
								monthStr = "0" + (monthOfYear + 1);
							} else {
								monthStr = (monthOfYear + 1) + "";
							}
						}

					}, year1, month1, day1).show();
		}
	}

	// ѡ��
	public void takefigure(View view) {
		switch (view.getId()) {
		case R.id.pwxkzbh_xz_btn:
			takefujain(PWXKZFJ_XZ);
			break;
		case R.id.zzjgdm_xz_btn:
			takefujain(ZZJGDMTP_XZ);
			break;
		case R.id.yyzzdm_xz_btn:
			takefujain(YYZZ_XZ);
			break;

		case R.id.qyzpmsyt_xz_btn:
			takefujain(QYPMSYT_XZ);
			break;
		case R.id.scgylct_xz_btn:
			takefujain(SCGYLCT_XZ);
			break;
		default:
			break;
		}

	}

	// ����
	public void photograph(View view) {
		switch (view.getId()) {
		case R.id.pwxkzbh_pz_btn:
			takePhoto("PWXKZFJ", PWXKZFJ_PZ);
			break;
		case R.id.zzjgdm_pz_btn:
			takePhoto("ZZJGDMTP", ZZJGDMTP_PZ);
			break;
		case R.id.yyzzdm_pz_btn:
			takePhoto("YYZZ", YYZZ_PZ);
			break;
		case R.id.qyzpmsyt_pz_btn:
			takePhoto("QYZPMSYT", QYPMSYT_PZ);
			break;
		case R.id.scgylct_pz_btn:
			takePhoto("SCGYLCT", SCGYLCT_PZ);
			break;
		default:
			break;
		}

	}

	private void takePhoto(String unit, int code) {// ����

		imageGuid = UUID.randomUUID().toString();
		Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(TASK_PATH + unit + "/");
		if (!file.exists())// ��һ�����մ�����Ƭ�ļ���
			file.mkdirs();
		photo_intent.putExtra(
				MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(TASK_PATH + unit + "/" + imageGuid + "."
						+ "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		startActivityForResult(photo_intent, code);

	}

	private void takefujain(int code) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "��ѡ��һ��Ҫ�ϴ����ļ�"),
					code);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "�밲װ�ļ�������", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

		// if (arg2==null) {
		// return;
		// }

		switch (arg0) {
		case PWXKZFJ_XZ:
			AttachmentBaseActivity.selectSDcardFile(arg2, this,
					T_Attachment.PWXKZFJ, guid);
			PWXKZFJAdapterData = getAttachAdapterData(
					T_Attachment.PWXKZFJ + "", guid);
			PWXKZFJAdapter.updateData(PWXKZFJAdapterData, task_PWXKZFJ_list,
					PWXKZFJAdapter);
			break;
		case YYZZ_XZ:
			AttachmentBaseActivity.selectSDcardFile(arg2, this,
					T_Attachment.YYZZ, guid);
			YYZZAdapterData = getAttachAdapterData(T_Attachment.YYZZ + "", guid);
			YYZZAdapter
					.updateData(YYZZAdapterData, task_YYZZ_list, YYZZAdapter);
			break;
		case ZZJGDMTP_XZ:
			AttachmentBaseActivity.selectSDcardFile(arg2, this,
					T_Attachment.ZZJGDMTP, guid);
			ZZJGDMTPAdapterData = getAttachAdapterData(T_Attachment.ZZJGDMTP
					+ "", guid);
			ZZJGDMTPAdapter.updateData(ZZJGDMTPAdapterData, task_ZZJGDMTP_list,
					ZZJGDMTPAdapter);
			break;
		// ��ҵ��ƽ��ʾ��ͼ
		case QYPMSYT_XZ:

			AttachmentBaseActivity.selectSDcardFile(arg2, this,
					T_Attachment.QYZPMSYT, guid);
			QYZPMSYTAdapterData = getAttachAdapterData(T_Attachment.QYZPMSYT
					+ "", guid);
			QYZPMSYTAdapter.updateData(QYZPMSYTAdapterData, task_QYZPMSYT_list,
					QYZPMSYTAdapter);

			break;
		// ��������ѡ��
		case SCGYLCT_XZ:

			AttachmentBaseActivity.selectSDcardFile(arg2, this,
					T_Attachment.SCGYLCT, guid);
			SCGYLCTAdapterData = getAttachAdapterData(
					T_Attachment.SCGYLCT + "", guid);
			SCGYLCTAdapter.updateData(SCGYLCTAdapterData, task_SCGYLCT_list,
					SCGYLCTAdapter);

			break;

		case PWXKZFJ_PZ:
			if (arg1 == RESULT_OK) {
				insterTAttachmentpz(T_Attachment.PWXKZFJ, guid);
				PWXKZFJAdapterData = getAttachAdapterData(T_Attachment.PWXKZFJ
						+ "", guid);
				PWXKZFJAdapter.updateData(PWXKZFJAdapterData,
						task_PWXKZFJ_list, PWXKZFJAdapter);

			}
			break;
		case YYZZ_PZ:
			if (arg1 == RESULT_OK) {
				insterTAttachmentpz(T_Attachment.YYZZ, guid);
				YYZZAdapterData = getAttachAdapterData(T_Attachment.YYZZ + "",
						guid);
				YYZZAdapter.updateData(YYZZAdapterData, task_YYZZ_list,
						YYZZAdapter);
			}
			break;
		case ZZJGDMTP_PZ:
			if (arg1 == RESULT_OK) {
				insterTAttachmentpz(T_Attachment.ZZJGDMTP, guid);
				ZZJGDMTPAdapterData = getAttachAdapterData(
						T_Attachment.ZZJGDMTP + "", guid);
				ZZJGDMTPAdapter.updateData(ZZJGDMTPAdapterData,
						task_ZZJGDMTP_list, ZZJGDMTPAdapter);
			}
			break;

		// ��ҵ��ƽ��ʾ��ͼ
		case QYPMSYT_PZ:
			if (arg1 == RESULT_OK) {
				insterTAttachmentpz(T_Attachment.QYZPMSYT, guid);
				QYZPMSYTAdapterData = getAttachAdapterData(
						T_Attachment.QYZPMSYT + "", guid);
				QYZPMSYTAdapter.updateData(QYZPMSYTAdapterData,
						task_QYZPMSYT_list, QYZPMSYTAdapter);
			}      
			break;
		case SCGYLCT_PZ:
			if (arg1 == RESULT_OK) {
				insterTAttachmentpz(T_Attachment.SCGYLCT, guid);
				SCGYLCTAdapterData = getAttachAdapterData(T_Attachment.SCGYLCT
						+ "", guid);
				SCGYLCTAdapter.updateData(SCGYLCTAdapterData,
						task_SCGYLCT_list, SCGYLCTAdapter);
			}
			break;
		default:
			break;
		}
	}

	// }

	private void insterTAttachmentpz(int unit, String fkid) {
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String fileName = dateFormat.format(now);
		// String sql="";
		// if (unit==99) {
		// sql =
		// "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) "
		// + "values ('" + imageGuid + "','" + fileName + "','" + imageGuid
		// + ".jpg','.jpg','" + "ZYZPMSYT" + "','" + fkid + "')";
		// }else{
		String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) "
				+ "values ('"
				+ imageGuid
				+ "','"
				+ fileName
				+ "','"
				+ imageGuid + ".jpg','.jpg','" + unit + "','" + fkid + "')";
		// }

		SqliteUtil.getInstance().execute(sql);
	}

	/**
	 * ��ȡ�����б�����
	 * 
	 * @param fk_unit
	 * @param fk_id
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getAttachAdapterData(
			String fk_unit, String fk_id) {

		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("FK_Unit", fk_unit);
		conditions.put("FK_Id", fk_id);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("Guid,FileName", conditions, "T_Attachment");
		return data;

	}

	protected void showDialog(final String guid, final String fileName,
			final int CODE) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		String[] selections = { "������", "ɾ��" };
		dialog.setItems(selections, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					LinearLayout ly = new LinearLayout(AddBusinessActivity.this);
					ly.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));
					final EditText edtext = new EditText(
							AddBusinessActivity.this);
					edtext.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					TextView tv = new TextView(AddBusinessActivity.this);
					tv.setText("���ƣ�");
					ly.addView(tv);
					ly.addView(edtext);
					AlertDialog.Builder ab = new AlertDialog.Builder(
							AddBusinessActivity.this);
					ab.setTitle("������");
					ab.setIcon(R.drawable.icon_rename);
					ab.setView(ly);
					ab.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String newName = edtext.getText()
											.toString();

									String id = guid;

									String sql = "update T_Attachment set FileName = '"
											+ newName
											+ "' where guid = '"
											+ id
											+ "'";
									SqliteUtil.getInstance().execute(sql);

									upDataAttathListView(CODE);
									Toast.makeText(AddBusinessActivity.this,
											"�������ɹ���", Toast.LENGTH_LONG).show();
								}
							});
					ab.setNegativeButton("ȡ��", null);
					ab.show();
					break;
				case 1:
					AlertDialog.Builder deleteab = new AlertDialog.Builder(
							AddBusinessActivity.this);
					deleteab.setTitle("ɾ��");
					deleteab.setMessage("��ȷ��Ҫɾ��" + fileName + "��");
					deleteab.setIcon(R.drawable.icon_delete);
					deleteab.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

									deleteFile(guid);
									String sql = "delete from T_Attachment "
											+ " where guid = '" + guid + "'";
									SqliteUtil.getInstance().execute(sql);
									upDataAttathListView(CODE);
									Toast.makeText(AddBusinessActivity.this,
											"ɾ��" + fileName + "�ɹ���",
											Toast.LENGTH_LONG).show();
								}

							});
					deleteab.setNegativeButton("ȡ��", null);
					AlertDialog ad = deleteab.create();
					ad.show();
					break;

				}
			}
		}).show();
	}

	public void upDataAttathListView(int CODE) {
		switch (CODE) {
		case PWXKZFJ_PZ:
			if (PWXKZFJAdapter != null) {
				PWXKZFJAdapter.updateData(
						getAttachAdapterData(T_Attachment.PWXKZFJ + "", guid),
						task_PWXKZFJ_list, PWXKZFJAdapter);
			}
			break;
		case YYZZ_PZ:
			if (YYZZAdapter != null) {
				YYZZAdapter.updateData(
						getAttachAdapterData(T_Attachment.YYZZ + "", guid),
						task_YYZZ_list, YYZZAdapter);
			}
			break;
		case ZZJGDMTP_PZ:
			if (ZZJGDMTPAdapter != null) {
				ZZJGDMTPAdapter.updateData(
						getAttachAdapterData(T_Attachment.ZZJGDMTP + "", guid),
						task_ZZJGDMTP_list, ZZJGDMTPAdapter);
			}
			break;
		// ƽ��
		case QYPMSYT_PZ:
			if (QYZPMSYTAdapter != null) {
				QYZPMSYTAdapter.updateData(
						getAttachAdapterData(T_Attachment.QYZPMSYT + "", guid),
						task_QYZPMSYT_list, QYZPMSYTAdapter);
			}
			break;
		// ��������
		case SCGYLCT_PZ:
			if (SCGYLCTAdapter != null) {
				SCGYLCTAdapter.updateData(
						getAttachAdapterData(T_Attachment.SCGYLCT + "", guid),
						task_SCGYLCT_list, SCGYLCTAdapter);
			}
			break;
		default:
			break;
		}

	}

	public boolean deleteFile(String guid) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", guid);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("FilePath,Extension,FK_Unit", conditions,
						"T_Attachment");
		if (data != null && data.size() == 1) {
			HashMap<String, Object> map = data.get(0);
			String fk_unit = map.get("fk_unit").toString();
			String extension = map.get("extension").toString();
			T_Attachment.transitToChinese(Integer.valueOf(fk_unit));
			String filepath = Global.SDCARD_RASK_DATA_PATH + "Attach/"
					+ T_Attachment.transitToChinese(Integer.valueOf(fk_unit))
					+ "/" + guid + extension;
			File file = new File(filepath);
			if (file.exists()) {
				file.delete();
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	public class AttachAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> attachAdapterData;

		public AttachAdapter(
				ArrayList<HashMap<String, Object>> attachAdapterData) {
			this.attachAdapterData = attachAdapterData;

		}

		@Override
		public int getCount() {
			int size = attachAdapterData.size();
			/*
			 * if(size==0){ return 1; }
			 */
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return attachAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void addData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData,
				ListView listview, AttachAdapter attachAdapter) {
			this.attachAdapterData = CompanyAdapterData;
			setListViewHeightBasedOnChildren(listview, attachAdapter);
			notifyDataSetChanged();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return attachAdapterData;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(AddBusinessActivity.this,
						R.layout.listitem, null);

			}
			ImageView rw_icon = (ImageView) convertView
					.findViewById(R.id.listitem_left_image);
			rw_icon.setImageResource(R.drawable.icon_table);
			TextView rwmc_text = (TextView) convertView
					.findViewById(R.id.listitem_text);
			rwmc_text.setText(attachAdapterData.get(position).get("filename")
					.toString());
			rwmc_text.setTextSize(20);
			rwmc_text.setTag(attachAdapterData.get(position).get("guid")
					.toString());

			return convertView;
		}
	}

	// ������������listView�ĸ߶�
	public void setListViewHeightBasedOnChildren(ListView listView,
			AttachAdapter attachAdapter) {
		// ListAdapter listAdapter = listView.getAdapter();
		if (attachAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < attachAdapter.getCount(); i++) {
			View listItem = attachAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (attachAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public void upLoadFilesMethod(ArrayList<TaskFile> fileList,
			Context context, String rwGuid) {

		new TaskUploadAsync(fileList, context, rwGuid).execute();
	}

	class TaskUploadAsync extends AsyncTask<Object, Integer, Object> {

		ArrayList<TaskFile> listAllFile;
		/** �ϴ����������� */
		ProgressDialog pdialog;
		private int index;
		/** �����Ƿ��Ѿ��ϴ��� */
		Boolean isUpload = false;
		private Context mcontext;
		private String rwGuid;

		TaskUploadAsync(ArrayList<TaskFile> listAllFile, Context mcontext,
				String rwGuid) {
			this.listAllFile = listAllFile;
			this.mcontext = mcontext;
			this.rwGuid = rwGuid;
			pdialog = new ProgressDialog(mcontext, 0);
			pdialog.setCancelable(false);
			pdialog.setMax(100);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			pdialog.cancel();
			if (null != result && !"".equals(result))
				Toast.makeText(mcontext, (CharSequence) result,
						Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			if (listAllFile == null || listAllFile.size() == 0) {
				Toast.makeText(mcontext, "û��ѡ�񸽼���", Toast.LENGTH_SHORT).show();
			} else {
				String fileName = listAllFile.get(index).getFileName()
						+ listAllFile.get(index).getExtension();
				pdialog.setMessage(fileName + " �����ϴ�...");
				pdialog.show();
			}

		}

		protected void onProgressUpdate(Integer... values) {
			String fileName = listAllFile.get(index).getFileName()
					+ listAllFile.get(index).getExtension();
			if (values[0] == 100) {
				if (isUpload) {
					pdialog.setMessage(fileName + " �Ѿ��ϴ�");
					isUpload = false;
				} else {
					pdialog.setMessage(fileName + " �ϴ��ɹ�");
				}

			} else {
				pdialog.setMessage(fileName + " �����ϴ�...");

			}
			pdialog.setProgress(values[0]);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String serverTime = DisplayUitl.getServerTime();// ���ȸ��¸������еĸ���ʱ��
			if (serverTime != null && !serverTime.equals("")) {
				for (TaskFile taskFile : listAllFile) {// ����updatetime
														// ȷ���������ܹ�ͬ��������
					String guid = taskFile.getGuid();
					ContentValues updateValues = new ContentValues();
					updateValues.put("UpdateTime", serverTime);
					String[] whereArgs = { guid };
					try {
						SqliteUtil.getInstance().update("T_Attachment",
								updateValues, "guid=?", whereArgs);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else {
				return "���������쳣�������������ú����ԣ�";
			}
			HttpUploader httpUploader = new HttpUploader();

			httpUploader.setWebServiceUrl(Global.getGlobalInstance()
					.getSystemurl() + Global.WEBSERVICE_URL);

			for (int n = 0; n < listAllFile.size(); n++) {

				TaskFile taskFile = listAllFile.get(n);

				ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param0 = new HashMap<String, Object>();
				String path = T_Attachment.transitToChinese(Integer
						.valueOf(taskFile.getUnitId()))
						+ "/"
						+ taskFile.getFilePath();
				param0.put("Path", path);
				param0.put("type", 0);
				params0.add(param0);

				int finishblocks = 0;// �ϵ����

				try {
					Object resultResponseObj0 = WebServiceProvider
							.callWebService(Global.NAMESPACE,
									"GetUploadFileCount", params0, Global
											.getGlobalInstance().getSystemurl()
											+ Global.WEBSERVICE_URL,
									WebServiceProvider.RETURN_INT, true);
					if (null != resultResponseObj0) {
						finishblocks = Integer.parseInt(resultResponseObj0
								.toString());
					} else {
						return "��ȡ�����ϵ���Ϣ�쳣";
					}
					if (finishblocks == -1) {
						return "��ȡ�����ϵ���Ϣʧ��";
					}

					if (finishblocks == 20000) {
						isUpload = true;
						publishProgress(100);
						continue;

					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

				FileInputStream fis = null;

				try {

					File absFile = new File(taskFile.getAbsolutePath());
					fis = new FileInputStream(absFile);

					boolean isEnd = false;

					int totalblocks = (int) absFile.length() / (1024 * 500) + 1;
					for (int i = 0; i < totalblocks; i++) {
						if (i < finishblocks) {
							byte[] buffers = new byte[1024 * 500];
							fis.read(buffers);
							continue;
						}
						String attachmentData = "";
						if (i == totalblocks - 1) {
							isEnd = true;
							byte[] buffers = new byte[(int) absFile.length()
									% (1024 * 500)];

							fis.read(buffers);
							attachmentData = Base64.encodeToString(buffers,
									Base64.DEFAULT);

						} else {
							byte[] buffer = new byte[1024 * 500];

							fis.read(buffer);
							attachmentData = Base64.encodeToString(buffer,
									Base64.DEFAULT);

						}
						// ��ȡ����
						String AttachmentJosn = "["
								+ GetFujian(taskFile, i + "", rwGuid)
										.toString() + "]";

						Boolean resultBoolean = httpUploader
								.upLoadAffixMethod2(AttachmentJosn,
										attachmentData, isEnd);
						if (resultBoolean) {
							index = n;
							publishProgress((i + 1) * 100 / totalblocks);
						} else {
							return "�ϴ�����ʧ��,���������Ƿ�������";
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "�ϴ����������쳣";
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
					}
				}
			}
			if (handlertake != null) {
				handlertake.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
			}
			return null;

		}

	}

	
	private JSONObject GetFujian(TaskFile taskFile, String i, String rwGuid) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance()
				.getList("T_Attachment", conditions);

		try {
			HashMap<String, Object> _HashMapTemp = _TaskList.get(0);
			Set _Iterator = _HashMapTemp.entrySet();

			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();

				String _Key = entry.getKey().toString();
				if (_Key.contains("filepath")) {
					String fileNameWithPath = entry.getValue().toString();
					fileNameWithPath = fileNameWithPath + "." + i;
					_JsonObject.put("fileNameWithPath", fileNameWithPath);
				} else if (_Key.equals("FK_ID") || _Key.equals("fk_id")) {

					String fk_id = entry.getValue().toString();
					_JsonObject.put("billid", fk_id);
					_JsonObject.put("fk_id", fk_id);
				} else if (_Key.equals("fk_unit") || _Key.equals("FK_Unit")) {

					String FK_values = entry.getValue().toString().trim();
					String temp = "";
					if (FK_values.equals(T_Attachment.YYZZ + "")) {
						temp = "YYZZ";
					} else if (FK_values.equals(T_Attachment.ZZJGDMTP + "")) {
						temp = "ZZJGDMTP";
					} else if (FK_values.equals(T_Attachment.QYZPMSYT + "")) {
						temp = "QYZPMSYT";
					} else if (FK_values.equals(T_Attachment.SCGYLCT + "")) {
						// ������������ͼ
						temp = "SCGYLCT";
					}
					_JsonObject.put("fk_unit", temp);
				} else {
					_JsonObject.put(_Key, entry.getValue().toString());
				}

				// _JsonObject.put("billid",
				// String.valueOf(data.get(0).get("rwbh")));
				_JsonObject.put("biztype", "");

			}

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}

}
