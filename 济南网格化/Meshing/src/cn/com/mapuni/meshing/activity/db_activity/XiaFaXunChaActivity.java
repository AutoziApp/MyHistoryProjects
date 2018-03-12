package cn.com.mapuni.meshing.activity.db_activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.httpserviceprovider.HttpServiceProvider;
import com.mapuni.android.photograph.BimpHelper;
import com.mapuni.android.photograph.Cameras;
import com.mapuni.android.photograph.CheckPicActivity;
import com.mapuni.android.photograph.DiscoverAlbumSelectCompleteActivity;
import com.mapuni.android.photograph.FileUtils;
import com.mapuni.android.photograph.GridAdapter;
import com.mapuni.android.photograph.PhotoGridView;
import com.mapuni.android.photograph.PhotoPopupWindows;
import com.mapuni.android.photograph.GridAdapter.ViewHolder;
import com.tianditu.android.maps.GeoPoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.activity.gis.LocationService;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.activity.gis.LocationService.MyBinder;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.wd_activity.SetData;
import cn.com.mapuni.meshing.adapter.MyExpandableListViewAdapter;
import cn.com.mapuni.meshing.model.DbTaskList;
import cn.com.mapuni.meshing.model.ProblemType;
import cn.com.mapuni.meshing.model.PotrlObject;
import cn.com.mapuni.meshing.model.ProblemType.RowsBean;
import cn.com.mapuni.meshing.model.ShangBaoBuMen;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;
import cn.com.mapuni.meshing.util.UploadFile;

public class XiaFaXunChaActivity extends BaseActivity implements OnClickListener {
	XiaFaTaskModel xiaFaTaskModel;
	public static String TAG = "XcrwActivity";
	public static String ACTION = "cn.com.mapuni.meshing.activity.gis.LocationService.MyReceiver";
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	LinearLayout middleLayout;
	View mainView;
	public LocationService.MyBinder myBinder;
	public ImageView dingwei_bu; // ��λͼ��
	/** ���Ԥ���б� */
	private PhotoGridView myGridView; // ��Ƭ�ؼ�
	private GridAdapter adapter; // ��Ƭ������
	private PhotoGridView myGridView1; // ��Ƭ�ؼ�
	private GridAdapter adapter1; // ��Ƭ������
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** ����ü� */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	private GeoPoint point; // ��λ��Ϣ
	Cameras cameras; // ���������
	EditText mbwz_eit; // λ���ı���
	RadioGroup rg_sfczhjwt, rg_clyj;
	RadioButton rb_has_problem, rb_no_problem, rb_self_solve, rb_shangbao;// �Ƿ��л������⣬�����ϱ���ʽѡ��ť
	LinearLayout problemLayout;// ���Ⲽ��
	LinearLayout shangbaoLayout;// ѡ���ϱ����ŵĲ���
	TextView tv_xuncha_task;// Ѳ������
	Spinner sp_jianguan_object;// ��ܶ���
	TextView sp_choose_shangbao;// ѡ���ϱ����ŵ�����
	EditText edt_chuli_yijian;// �������
	TextView task_type;// ��������
	EditText edt_problem_description;// Ѳ�����������
	int flag = 0;// �������������Դ

	Button bt_save;
	List<PotrlObject.RowsBean> Objects = new ArrayList<PotrlObject.RowsBean>();// ��ܶ���ȫ��
	List<ProblemType.RowsBean> types = new ArrayList<ProblemType.RowsBean>();// ��������ȫ��
	int lastObjectPosition = 0;// ��¼��ܶ��������
	List<String> lastTypePositions = new ArrayList<String>();// ��¼����������������
	private LinearLayout problem_type_layout;
	String handlerGrid = "";
	private LinearLayout layout_handling_suggestion;// �����������
	String other = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("�·�����Ѳ���ϱ�");
		cameras = new Cameras(XiaFaXunChaActivity.this);
		initView();
		initData();
	}

	void initData() {
		// �Զ���ȡĿ��λ��
		getLocationAdrr();
		getLocationAdrrPoint();

		getShangBaoBuMen();// ���Ȼ�ȡ�ϱ���������
		getObjectData();// ��ʼ����ܶ�������
	}

	// ��ʼ���ؼ�
	private void initView() {
		xiaFaTaskModel = (XiaFaTaskModel) getIntent().getSerializableExtra("xiaFaTaskModel");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.xiafaxuncha_ac, null);
		middleLayout.addView(mainView);
		problem_type_layout = (LinearLayout) mainView
				.findViewById(R.id.problem_type_layout);
		// ���Ͼ�Ϊ��·����
		dingwei_bu = (ImageView) mainView.findViewById(R.id.dingwei_bu);
		dingwei_bu.setOnClickListener(this);
		mbwz_eit = (EditText) mainView.findViewById(R.id.mbwz_eit);

		rg_sfczhjwt = (RadioGroup) mainView.findViewById(R.id.rg_sfczhjwt);
		rb_has_problem = (RadioButton) mainView
				.findViewById(R.id.rb_has_problem);
		rb_no_problem = (RadioButton) mainView.findViewById(R.id.rb_no_problem);

		problemLayout = (LinearLayout) mainView
				.findViewById(R.id.problem_layout);
		layout_handling_suggestion = (LinearLayout) mainView
				.findViewById(R.id.layout_handling_suggestion);

		rg_clyj = (RadioGroup) mainView.findViewById(R.id.rg_clyj);
		rb_self_solve = (RadioButton) mainView.findViewById(R.id.rb_self_solve);
		rb_shangbao = (RadioButton) mainView.findViewById(R.id.rb_shangbao);

		tv_xuncha_task = (TextView) mainView.findViewById(R.id.tv_xuncha_task);
		tv_xuncha_task.setText(xiaFaTaskModel.getTaskName());
		bt_save = (Button) mainView.findViewById(R.id.bt_save);
		bt_save.setOnClickListener(this);
		task_type = (TextView) mainView.findViewById(R.id.myspinner_task_type);
		shangbaoLayout = (LinearLayout) mainView
				.findViewById(R.id.shangbao_layout);
		sp_jianguan_object = (Spinner) mainView
				.findViewById(R.id.sp_jianguan_object);

		edt_chuli_yijian = (EditText) mainView
				.findViewById(R.id.edt_chuli_yijian);
		edt_problem_description = (EditText) mainView
				.findViewById(R.id.edt_problem_description);
		//
		task_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if ("other".equals(other)) {
					selectData2();
				} else {
					// selectData(v);
					selectData3();
				}
			}
		});
		// ���ݼ�ܶ�����ѡ����ȥ��̬�л�������������
		sp_jianguan_object
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						lastObjectPosition = position;
						if (Objects.size() > 0) {
							other = "other";
							other = Objects.get(position).getSuperviseType();
							getProblemType();
							task_type.setText("");
							edt_problem_description.setText("");
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
		sp_choose_shangbao = (TextView) mainView
				.findViewById(R.id.sp_choose_shangbao);
		sp_choose_shangbao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectShangbaoBuMenData();
			}
		});

		rg_sfczhjwt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_has_problem) {
					if (View.GONE == problemLayout.getVisibility()) {
						problemLayout.setVisibility(View.VISIBLE);
						problem_type_layout.setVisibility(View.VISIBLE);
					}
				} else {
					rb_shangbao.setChecked(true);
					if (View.VISIBLE == problemLayout.getVisibility()) {
						problemLayout.setVisibility(View.GONE);
						problem_type_layout.setVisibility(View.GONE);
					}
				}
			}
		});
		rb_has_problem.setChecked(true);

		rg_clyj.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				String haveFreeRole = DisplayUitl.readPreferences(
						XiaFaXunChaActivity.this, LAST_USER_SP_NAME, "haveFreeRole");
				if (checkedId == R.id.rb_shangbao && haveFreeRole != null
						&& haveFreeRole.equals("1")) {
					shangbaoLayout.setVisibility(View.VISIBLE);// ��ʾ�ϱ�����
				} else {
					shangbaoLayout.setVisibility(View.GONE);
				}
				if (checkedId == R.id.rb_shangbao) {
					layout_handling_suggestion.setVisibility(View.GONE);
				} else {
					layout_handling_suggestion.setVisibility(View.VISIBLE);
				}
			}
		});
		rb_shangbao.setChecked(true);

		initPhotoGrideView();
		initPhotoGrideView1();
		LinearLayout objectLayout = (LinearLayout) mainView
				.findViewById(R.id.object_layout);
		LinearLayout cutLine = (LinearLayout) mainView
				.findViewById(R.id.cut_line);
		TextView tvFou = (TextView) mainView.findViewById(R.id.fou);
		String haveFreeRole = DisplayUitl.readPreferences(XiaFaXunChaActivity.this,
				LAST_USER_SP_NAME, "haveFreeRole");
		if ("1".equals(haveFreeRole)) {
			objectLayout.setVisibility(View.GONE);
			cutLine.setVisibility(View.GONE);
			rb_no_problem.setVisibility(View.GONE);
			rb_self_solve.setVisibility(View.GONE);
			tvFou.setVisibility(View.GONE);
			// ��ȡȫ���������
			other = "other";
			getProblemType();
			task_type.setText("");
			edt_problem_description.setText("");
		}
	}

	// һ����ʾ
	String temps_problem_types[] = new String[] { "��" };

	public void selectData(final View view) {
		// temps = getResources().getStringArray(R.array.task_tpye);
		lastTypePositions.clear();
		final List<String> list = new ArrayList<String>();
		final HashSet<String> items = new HashSet<String>();
		final List<String> tempTypes = new ArrayList<String>();// ��ʱ�洢�����ļ���
		AlertDialog.Builder builder = OtherTools.getAlertDialog(
				XiaFaXunChaActivity.this, "��ѡ���������:", null,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						StringBuffer buffer = new StringBuffer();
						for (String string : items) {
							buffer.append(string + ",");
						}
						if (buffer.length() == 0) {
							buffer.append(",");
						}
						((TextView) view).setText(buffer.toString().substring(
								0, buffer.length() - 1));
						edt_problem_description.setText(buffer.toString()
								.substring(0, buffer.length() - 1));

						lastTypePositions.addAll(list);
					}
				}, null, null);
		builder.setMultiChoiceItems(temps_problem_types, null,
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						String temp = temps_problem_types[which];
						if (isChecked) {
							list.add("" + which);
							items.add(temp);
						} else {
							items.remove(temp);
							list.remove("" + which);
						}
					}
				});

		builder.show();
	}

	// ������ʾ
	String all_string = "";
	StringBuffer userName = new StringBuffer();
	StringBuffer code_all = new StringBuffer();
	HashMap<String, String> map_parent = new HashMap<String, String>();

	public void selectData2() {
		Builder builder = new Builder(XiaFaXunChaActivity.this);
		builder.setTitle("��ѡ���������:");
		// builder.setIcon(XcrwActivity.this.getResources().getDrawable(R.drawable.yutu2));
		LayoutInflater inflater = LayoutInflater.from(XiaFaXunChaActivity.this);
		View v = inflater.inflate(R.layout.set_txjcr, null);
		final EditText edit_txjcr_dialog = (EditText) v
				.findViewById(R.id.edit_txjcr_dialog);
		TextView text_chaxun_txjcr = (TextView) v
				.findViewById(R.id.text_chaxun_txjcr);
		final ExpandableListView listView = (ExpandableListView) v
				.findViewById(R.id.expList_txjcr);
		builder.setView(v);

		List<String> groupList = new ArrayList<String>();
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		final LinkedList<String> linkedList = new LinkedList<String>();
		final LinkedList<String> linkedName = new LinkedList<String>();
		final LinkedList<String> linkedZfzh = new LinkedList<String>();

		try {
			JSONObject jsonObject = new JSONObject(all_string);
			if (jsonObject.getString("status").equals("200")) {
				JSONArray jsonArray = jsonObject.getJSONArray("rows");
				if (jsonArray.length() > 0) {
					map_parent = new HashMap<String, String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						// ���游�ڵ�
						map_parent
								.put(jsonArray.getJSONObject(i).getString(
										"itemCode"), jsonArray.getJSONObject(i)
										.getString("itemName"));
						// ���ڵ����ݣ��ӽڵ�����
						groupList.add(jsonArray.getJSONObject(i).getString(
								"itemName"));
						JSONArray jsonArray_childs = jsonArray.getJSONObject(i)
								.getJSONArray("childs");
						if (jsonArray_childs != null
								&& jsonArray_childs.length() > 0) {
							ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
							HashMap<String, Object> map;
							for (int j = 0; j < jsonArray_childs.length(); j++) {
								map = new HashMap<String, Object>();
								map.put("userid", jsonArray_childs
										.getJSONObject(j).getString("itemCode"));
								map.put("u_realname", jsonArray_childs
										.getJSONObject(j).getString("itemName"));
								map.put("zfzh",
										jsonArray_childs.getJSONObject(j)
												.getString("paramCode"));
								list.add(map);
							}
							childMapData.add(list);
						} else {
							ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
							childMapData.add(list);
						}
					}
				} else {
					Toast.makeText(XiaFaXunChaActivity.this, "��������", 400).show();
					return;
				}
			} else {
				Toast.makeText(XiaFaXunChaActivity.this, "�޷���ȡ�������", 400).show();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		SelectAuditorAdapter adapter = new SelectAuditorAdapter(groupList,
				childMapData, linkedList, linkedName, linkedZfzh,
				XiaFaXunChaActivity.this);
		listView.setAdapter(adapter);// �������ݣ���������

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				userName = new StringBuffer();
				code_all = new StringBuffer();

				try {
					Field field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					// ����mShowingֵ����ƭandroidϵͳ
					field.set(dialog, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < linkedList.size(); i++) {
					// code_all.append(linkedList.get(i) + ",");
					userName.append(linkedName.get(i).trim() + ",");
				}
				for (int i = 0; i < linkedZfzh.size(); i++) {
					code_all.append(linkedZfzh.get(i) + ",");
				}
				/* ȥ��, */
				if (code_all.length() > 0) {
					code_all.deleteCharAt(code_all.length() - 1);
				}
				if (userName.length() > 0) {
					userName.deleteCharAt(userName.length() - 1);
				}
				// ��ȡ���ڵ�id
				if (linkedZfzh != null && linkedZfzh.size() > 0) {
					String string = map_parent.get(linkedZfzh.get(0));
					for (int j = 1; j < linkedZfzh.size(); j++) {
						string = string + ","
								+ map_parent.get(linkedZfzh.get(j));
					}
					task_type.setText(string);
				}
				// task_type.setText(userName);
				edt_problem_description.setText(userName);
			}

		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					// ����mShowingֵ����ƭandroidϵͳ
					field.set(dialog, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		builder.create().show();
	}

	public void selectData3() {
		Builder builder = new Builder(XiaFaXunChaActivity.this);
		builder.setTitle("��ѡ���������:");
		// builder.setIcon(XcrwActivity.this.getResources().getDrawable(R.drawable.yutu2));
		LayoutInflater inflater = LayoutInflater.from(XiaFaXunChaActivity.this);
		View v = inflater.inflate(R.layout.set_txjcr, null);
		final EditText edit_txjcr_dialog = (EditText) v
				.findViewById(R.id.edit_txjcr_dialog);
		TextView text_chaxun_txjcr = (TextView) v
				.findViewById(R.id.text_chaxun_txjcr);
		final ExpandableListView listView = (ExpandableListView) v
				.findViewById(R.id.expList_txjcr);
		builder.setView(v);

		List<String> groupList = new ArrayList<String>();
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		final LinkedList<String> linkedList = new LinkedList<String>();
		final LinkedList<String> linkedName = new LinkedList<String>();
		final LinkedList<String> linkedZfzh = new LinkedList<String>();

		try {
			JSONObject jsonObject = new JSONObject(all_string);
			if (jsonObject.getString("status").equals("200")) {
				map_parent = new HashMap<String, String>();
				// ���游�ڵ�
				map_parent
						.put(jsonObject.getJSONObject("content").getString(
								"itemCode"), jsonObject
								.getJSONObject("content").getString("itemName"));
				// ���ڵ����ݣ��ӽڵ�����
				groupList.add(jsonObject.getJSONObject("content").getString(
						"itemName"));
				JSONArray jsonArray_childs = jsonObject.getJSONArray("rows");
				if (jsonArray_childs != null && jsonArray_childs.length() > 0) {
					ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> map;
					for (int j = 0; j < jsonArray_childs.length(); j++) {
						map = new HashMap<String, Object>();
						map.put("userid", jsonArray_childs.getJSONObject(j)
								.getString("itemCode"));
						map.put("u_realname", jsonArray_childs.getJSONObject(j)
								.getString("itemName"));
						map.put("zfzh", jsonArray_childs.getJSONObject(j)
								.getString("paramCode"));
						list.add(map);
					}
					childMapData.add(list);
				} else {
					ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					childMapData.add(list);
				}
			} else {
				Toast.makeText(XiaFaXunChaActivity.this, "�޷���ȡ�������", 400).show();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		SelectAuditorAdapter adapter = new SelectAuditorAdapter(groupList,
				childMapData, linkedList, linkedName, linkedZfzh,
				XiaFaXunChaActivity.this);
		listView.setAdapter(adapter);// �������ݣ���������

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				userName = new StringBuffer();
				code_all = new StringBuffer();

				try {
					Field field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					// ����mShowingֵ����ƭandroidϵͳ
					field.set(dialog, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < linkedList.size(); i++) {
					// code_all.append(linkedList.get(i) + ",");
					userName.append(linkedName.get(i).trim() + ",");
				}
				for (int i = 0; i < linkedZfzh.size(); i++) {
					code_all.append(linkedZfzh.get(i) + ",");
				}
				/* ȥ��, */
				if (code_all.length() > 0) {
					code_all.deleteCharAt(code_all.length() - 1);
				}
				if (userName.length() > 0) {
					userName.deleteCharAt(userName.length() - 1);
				}
				// ��ȡ���ڵ�id
				if (linkedZfzh != null && linkedZfzh.size() > 0) {
					String string = map_parent.get(linkedZfzh.get(0));
					for (int j = 1; j < linkedZfzh.size(); j++) {
						string = string + ","
								+ map_parent.get(linkedZfzh.get(j));
					}
					task_type.setText(string);
				}
				// task_type.setText(userName);
				edt_problem_description.setText(userName);
			}

		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				try {
					Field field = dialog.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					// ����mShowingֵ����ƭandroidϵͳ
					field.set(dialog, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		builder.create().show();
	}

	// spinner��ʼ���򵥷�װ
	private void initSpinner(Spinner spinner, List<String> data) {

		// ����Adapter���Ұ�����Դ
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void initPhotoGrideView() {
		adapter = new GridAdapter(this, 1);
		// adapter.update();
		myGridView = (PhotoGridView) mainView
				.findViewById(R.id.noScrollgridview);
		myGridView.setAdapter(adapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if (BimpHelper.bmp.size() > Cameras.num) {
					Toast.makeText(XiaFaXunChaActivity.this, "���ѡ��" + Cameras.num + "��ͼƬ", 400)
							.show();
					return;
				}
				flag = 1;
				BimpHelper.currentFlag = 1;
				if (arg2 == BimpHelper.drr_temp1.size()) {
					new PhotoPopupWindows(XiaFaXunChaActivity.this, myGridView,
							cameras);
				} else {
					Intent intent = new Intent(XiaFaXunChaActivity.this,
							CheckPicActivity.class);
					intent.putExtra("ID", BimpHelper.drr_temp1.get(arg2));
					startActivity(intent);
				}
			}
		});
	}

	private void initPhotoGrideView1() {
		adapter1 = new GridAdapter(this, 2);
		// adapter1.update();
		myGridView1 = (PhotoGridView) mainView
				.findViewById(R.id.noScrollgridview1);
		myGridView1.setAdapter(adapter1);
		myGridView1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if (BimpHelper.bmp.size() > Cameras.num) {
					Toast.makeText(XiaFaXunChaActivity.this, "���ѡ��" + Cameras.num + "��ͼƬ", 400)
							.show();
					return;
				}
				flag = 2;
				BimpHelper.currentFlag = 2;
				if (arg2 == BimpHelper.drr_temp2.size()) {
					new PhotoPopupWindows(XiaFaXunChaActivity.this, myGridView,
							cameras);
				} else {
					Intent intent = new Intent(XiaFaXunChaActivity.this,
							CheckPicActivity.class);
					intent.putExtra("ID", BimpHelper.drr_temp2.get(arg2));
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCAMERA:
			if (BimpHelper.drr.size() < Cameras.num && resultCode == -1) {
				BimpHelper.drr.add(cameras.protraitPath);
				if (flag == 1) {
					BimpHelper.drr_temp1.add(BimpHelper.drr.size() - 1);
				} else {
					BimpHelper.drr_temp2.add(BimpHelper.drr.size() - 1);
				}
			}
//			cameras.startActionCrop(cameras.photoUri, cameras);// ���պ�ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			cameras.startActionCrop(data.getData(), cameras);// ѡͼ��ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			if (BimpHelper.drr.size() < Cameras.num && resultCode == -1) {
				BimpHelper.drr.add(cameras.protraitPath);
				if (flag == 1) {
					BimpHelper.drr_temp1.add(BimpHelper.drr.size() - 1);
				} else {
					BimpHelper.drr_temp2.add(BimpHelper.drr.size() - 1);
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}
		switch (view.getId()) {
		case R.id.rb_has_problem:
			if (View.GONE == problemLayout.getVisibility()) {
				problemLayout.setVisibility(View.VISIBLE);
				problem_type_layout.setVisibility(View.VISIBLE);

			}
			break;
		case R.id.rb_no_problem:
			if (View.VISIBLE == problemLayout.getVisibility()) {
				problemLayout.setVisibility(View.GONE);
				problem_type_layout.setVisibility(View.GONE);
			}
			break;
		case R.id.dingwei_bu:
			getLocationAdrr();
			getLocationAdrrPoint();
			break;
		case R.id.bt_save:
			// ��װ����
			// ���ͼƬ
			List<String> drr1 = new ArrayList<String>();
			List<String> drr2 = new ArrayList<String>();
			for (int i = 0; i < BimpHelper.drr_temp1.size(); i++) {
				drr1.add(BimpHelper.drr.get(BimpHelper.drr_temp1.get(i)));
			}
			for (int i = 0; i < BimpHelper.drr_temp2.size(); i++) {
				drr2.add(BimpHelper.drr.get(BimpHelper.drr_temp2.get(i)));
			}
			String taskId = "FBF4C61A-B89B-46D3-BE6A-05286E7088ED";
			// String patrolObjectId =
			// "7B9BEC3C-E0E3-4CD2-AE97-45733EBD440D";//��ܶ���code
			String patrolObjectId = "";
			String problemCode = "01";
			if (Objects.size() > 0) {
				if (Objects.get(lastObjectPosition).getId() != null) {
					patrolObjectId = Objects.get(lastObjectPosition).getId();
				}
			}
			if ("other".equals(other)) {
				problemCode = code_all.toString();
			} else {

				problemCode = code_all.toString();
			}

			String isHaveProblem = "1";
			if (rb_has_problem.isChecked())
				isHaveProblem = "1";

			String problemDesc = "";
			if (TextUtils.isEmpty(edt_problem_description.getText().toString())) {
				Toast.makeText(XiaFaXunChaActivity.this, "Ѳ�����ݲ���Ϊ��", 0).show();
				return;
			} else {
				problemDesc = edt_problem_description.getText().toString();
			}
			String status = "1";
			if (rb_self_solve.isChecked())
				status = "1";
			if (rb_shangbao.isChecked())
				status = "2";
			String opinion = "";
			if (!TextUtils.isEmpty(edt_chuli_yijian.getText().toString())) {
				opinion = edt_chuli_yijian.getText().toString();
			}

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
			String str = formatter.format(curDate);
			String createTime = str;

			if (rb_no_problem.isChecked()) {// ����û�л������� �������ֶξ����衰��
				isHaveProblem = "0";
				problemCode = "";
				status = "1";
				opinion = "";
				drr2.clear();
			}
			if (null == point) {
				Toast.makeText(XiaFaXunChaActivity.this, "λ����Ϣ����Ϊ��", 0).show();
				return;
			}

			try {
				uploadFile(uphandler, drr1, drr2, point, taskId,
						patrolObjectId, isHaveProblem, problemDesc, opinion,
						status, handlerGrid, problemCode, createTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	private YutuLoading yutuLoading;
	private Handler uphandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(XiaFaXunChaActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("�����ϴ����ݣ����Ժ�", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(XiaFaXunChaActivity.this, "ִ�гɹ�", 1000).show();
				finish();
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(XiaFaXunChaActivity.this, "ִ��ʧ��", 1000).show();
				break;

			default:
				break;
			}
		}
	};

	private void getLocationAdrr() {
		MapBinder.getInstance().getBinder().getLocationAddr(new CallBackAdrr() {
			@Override
			public void callbackadrr(String adrr, GeoPoint mGeoPoint) {
				mbwz_eit.setText(adrr);
			}
		});
	}

	private void getLocationAdrrPoint() {
		MapBinder.getInstance().getBinder()
				.CallBackAdrrPoint(new CallBackAdrrPoint() {
					@Override
					public void CallBackAdrrPoint(GeoPoint mGeoPoint) {
						point = mGeoPoint;
					}
				});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (flag == 1) {
			adapter.update();
		} else {
			adapter1.update();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		adapter.notifyDataSetChanged();
		adapter1.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BimpHelper.clear();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void uploadFile(final Handler uphandler, List<String> list1,
			List<String> list2, GeoPoint point, String taskId,
			String patrolObjectId, String isHaveProblem, String problemDesc,
			String opinion, String status, String handlerGrid,
			String problemCode, String createTime) throws Exception {
		MediaType MEDIA_TYPE_MARKDOWN = MediaType
				.parse("application/octet-stream");
		OkHttpClient client = new OkHttpClient();
		// String url =
		// "http://119.164.253.236:8184/JiNanhuanbaoms/patrol/save.do";
		String url = PathManager.SHANGBAO_URL_JINAN;

		uphandler.sendEmptyMessage(101);
		double lat = point.getLatitudeE6();
		double lon = point.getLongitudeE6();
		lat = lat / 1000000;
		lon = lon / 1000000;
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("Latitude", lat + "")// ok
				.addFormDataPart("Longitude", lon + "")// ok
				.addFormDataPart("address", mbwz_eit.getText().toString())// Ŀ��λ��
				.addFormDataPart("taskId",xiaFaTaskModel.getTaskId()) // ok
				.addFormDataPart("patrolObjectId", patrolObjectId)// ok
				.addFormDataPart("isHaveProblem", isHaveProblem)// ok
				.addFormDataPart("problemDesc", problemDesc)// ok
				.addFormDataPart("status", status)// ok
				.addFormDataPart("opinion", opinion)// ok
				.addFormDataPart("handlerGrid", handlerGrid)// �ϱ���λ���������
				.addFormDataPart("problemCode", problemCode)
				// ok
				.addFormDataPart(
						"sessionId",
						DisplayUitl.readPreferences(XiaFaXunChaActivity.this,
								LAST_USER_SP_NAME, "sessionId"))// ����
				.addFormDataPart("createTime", createTime);// ok
		for (String path : list1) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(),
					RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}
		for (String path : list2) {
			File file = new File(path);
			builder.addFormDataPart("file1", file.getName(),
					RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody)
				.build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				// Log.i(Tag,"bodyStr==="+bodyStr);
				if (ok) {
					// Log.i(Tag,"ִ�гɹ�:"+bodyStr);

					try {
						JSONObject sywrwJsonObject = new JSONObject(bodyStr);
						String status = sywrwJsonObject.getString("status");
						if (status.contains("200")) {
							uphandler.sendEmptyMessage(102);
						} else {
							uphandler.sendEmptyMessage(103);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					// Log.i(Tag,"ִ��ʧ��1��"+bodyStr);
					uphandler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				// Log.i(Tag,"ִ��ʧ��2��"+e.toString());
				uphandler.sendEmptyMessage(103);
			}
		});

	}

	/**
	 * ��ȡ��ܶ�����������
	 * http://119.164.253.236:8184/JiNanhuanbaoms/patrolObject/list.do?sessionId
	 * =8a8f87ac5abd1242015abd3f10290020
	 */
	private void getObjectData() {
		final YutuLoading yutuLoading = new YutuLoading(XiaFaXunChaActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ��ܶ������ݣ����Ժ�", "");
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// ����ύ����
		String sessionId = DisplayUitl.readPreferences(XiaFaXunChaActivity.this,
				LAST_USER_SP_NAME, "sessionId");
		params.addBodyParameter("sessionId", sessionId);
		Log.i("aaa", sessionId);
		// String url =
		// "http://119.164.253.236:8184/JiNanhuanbaoms/patrolObject/list.do?sessionId="
		// + sessionId;
		String url = PathManager.GETXUNCHAOBJECT_URL_JINAN + "?sessionId="
				+ sessionId+"&types="+xiaFaTaskModel.getTaskType();
		utils.send(HttpMethod.GET, url/* , params */,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						Toast.makeText(XiaFaXunChaActivity.this, "��������ʧ��", 200).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}

						String resoult = String.valueOf(arg0.result);
						Gson gson = new Gson();
						PotrlObject potrlObject = gson.fromJson(resoult,
								PotrlObject.class);
						if ("200".equals(potrlObject.getStatus())) {//
							Objects = potrlObject.getRows();
							List<String> list = new ArrayList<String>();
							for (PotrlObject.RowsBean bean : Objects) {
								list.add(bean.getName());
							}
							initSpinner(sp_jianguan_object, list);
						}

					}
				});
	}

	/**
	 * �������͵�type����
	 * http://119.164.253.236:8184/JiNanhuanbaoms/param/other/items.do?sessionId
	 * =8a8f87ac5abd1242015abd391f44001b
	 */
	private void getProblemType() {
		final YutuLoading yutuLoading = new YutuLoading(XiaFaXunChaActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ����������ݣ����Ժ�", "");
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// ����ύ����
		String sessionId = DisplayUitl.readPreferences(XiaFaXunChaActivity.this,
				LAST_USER_SP_NAME, "sessionId");
		params.addBodyParameter("sessionId", sessionId);
		Log.i("aaa", sessionId);
		String url = "";
		if ("other".equals(other)) {
			url = PathManager.BASE_URL_JINNAN
					+ "/JiNanhuanbaoms/param/supervise_type/all.do?sessionId="
					+ sessionId;
		} else {
			url = PathManager.BASE_URL_JINNAN + "/JiNanhuanbaoms/param/"
					+ other + "/items.do?sessionId=" + sessionId;
		}
		utils.send(HttpMethod.GET, url, /* params, */
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						String resoult = String.valueOf(arg0.result);
						all_string = resoult;
						
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (null != yutuLoading) {
							yutuLoading.dismissDialog();
						}
						Toast.makeText(XiaFaXunChaActivity.this, "��������ʧ��", 200).show();
					}
				});
	}

	public void selectShangbaoBuMenData() {
		Builder builder = new Builder(XiaFaXunChaActivity.this);
		builder.setTitle("��ѡ���ϱ�����:");
		LayoutInflater inflater = LayoutInflater.from(XiaFaXunChaActivity.this);
		View v = inflater.inflate(R.layout.set_txjcr, null);
		final ExpandableListView listView = (ExpandableListView) v
				.findViewById(R.id.expList_txjcr);
		builder.setView(v);
		myAdapter = new MyExpandableListViewAdapter(childrenListMap,
				parentList, XiaFaXunChaActivity.this);
		listView.setAdapter(myAdapter);// �������ݣ���������
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCancelable(true);
		alertDialog.show();
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				handlerGrid = shangBaoBuMen.getRows().get(groupPosition)
						.getChilds().get(childPosition).getCode();
				sp_choose_shangbao.setText(childrenListMap.get(
						parentList.get(groupPosition)).get(childPosition));
				alertDialog.dismiss();
				return true;
			}
		});
	}

	ShangBaoBuMen shangBaoBuMen;
	MyExpandableListViewAdapter myAdapter;
	List<String> parentList = new ArrayList<String>();// ���˵�
	Map<String, List<String>> childrenListMap = new HashMap<String, List<String>>();// �Ӳ˵�

	/**
	 * ��ȡ����Ѳ��Ա�ϱ���λ����
	 * http://192.168.1.151:8080/JiNanhuanbaoms/grid/level/2?sessionId=
	 * 8a8f87ac5ac115e3015ac712158b0099
	 */
	private void getShangBaoBuMen() {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sessionId = DisplayUitl.readPreferences(XiaFaXunChaActivity.this,
				LAST_USER_SP_NAME, "sessionId");
		Log.i("aaa", sessionId);
		String url = PathManager.BASE_URL_JINNAN
				+ "/JiNanhuanbaoms/grid/level/2" + "?sessionId=" + sessionId;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(XiaFaXunChaActivity.this, "��������ʧ��", 200).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String resoult = String.valueOf(arg0.result);
				Gson gson = new Gson();
				shangBaoBuMen = gson.fromJson(resoult, ShangBaoBuMen.class);
				List<ShangBaoBuMen.RowsBean> list = new ArrayList<ShangBaoBuMen.RowsBean>();
				list = shangBaoBuMen.getRows();

				for (ShangBaoBuMen.RowsBean bean : list) {
					parentList.add(bean.getName());
					List<String> children = new ArrayList<String>();
					List<String> childrenCode = new ArrayList<String>();
					for (ShangBaoBuMen.RowsBean.ChildsBean b : bean.getChilds()) {
						children.add(b.getName());
						childrenCode.add(b.getCode());
					}
					childrenListMap.put(bean.getName(), children);
				}
			}
		});
	}

	public class SelectAuditorAdapter extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		private final LinkedList<String> linkedZfzh;
		LayoutInflater layoutInflater;

		public SelectAuditorAdapter(List<String> groupList,
				ArrayList<ArrayList<HashMap<String, Object>>> childMapData,
				LinkedList<String> usersb, LinkedList<String> linkedName,
				LinkedList<String> linkedZfzh, Context context) {
			layoutInflater = LayoutInflater.from(context);
			if (groupList != null) {
				this.groupList = groupList;
			} else {
				this.groupList = new ArrayList<String>();
			}
			if (childMapData != null) {
				this.childMapData = childMapData;
			} else {
				this.childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
			}
			this.usersb = usersb;
			this.linkedName = linkedName;
			this.linkedZfzh = linkedZfzh;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			convertView = layoutInflater.inflate(R.layout.two_class_interface,
					null);

			TextView userTv = (TextView) convertView
					.findViewById(R.id.two_class_interface_name_tv);
			CheckBox isSelect = (CheckBox) convertView
					.findViewById(R.id.two_class_cb);

			final String realName = childMapData.get(groupPosition)
					.get(childPosition).get("u_realname").toString();
			final String zfzh = childMapData.get(groupPosition)
					.get(childPosition).get("zfzh").toString();
			final String userCheckedId = childMapData.get(groupPosition)
					.get(childPosition).get("userid").toString();
			if (usersb.contains(userCheckedId)) {
				isSelect.setChecked(true);
			} else {
				isSelect.setChecked(false);
			}
			isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						if (!usersb.contains(userCheckedId)) {
							usersb.add(userCheckedId);
						}
						if (!linkedName.contains(realName)) {
							linkedName.add(realName);
						}
						if (!linkedZfzh.contains(zfzh)) {
							linkedZfzh.add(zfzh);
						}
					} else {
						if (usersb.contains(userCheckedId)) {
							usersb.remove(userCheckedId);
						}
						if (linkedName.contains(realName)) {
							linkedName.remove(realName);
						}
						if (linkedZfzh.contains(zfzh)) {
							linkedZfzh.remove(zfzh);
						}
					}
				}
			});
			// userTv.setText(realName.trim() + "(" + zfzh.trim() + ")");
			userTv.setText(realName.trim());
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {

			return childMapData.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {

			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.one_class_interface, null);
			}
			TextView one_class_interface_title_tv = (TextView) convertView
					.findViewById(R.id.one_class_interface_title_tv);

			one_class_interface_title_tv.setText(groupList.get(groupPosition)
					.toString());

			return convertView;
		}

		@Override
		public boolean hasStableIds() {

			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
		}

	}

}
