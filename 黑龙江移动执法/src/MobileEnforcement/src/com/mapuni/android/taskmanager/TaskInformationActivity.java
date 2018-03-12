package com.mapuni.android.taskmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalchina.gallery.ImageGalleryActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.TaskFile;
import com.mapuni.android.attachment.UploadFile;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.LoadDetailLayout;
import com.mapuni.android.base.adapter.QYListAdapter;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.RWXX.ExpandableIncludeParentAdapter;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.AttachmentBaseActivity;
import com.mapuni.android.enforcement.QdjcnlActivity;
import com.mapuni.android.enforcement.SiteEvidenceActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.enterpriseArchives.SlideOnLoadAdapter;
import com.mapuni.android.enterpriseArchives.SlideView;
import com.mapuni.android.infoQuery.JCKHSearchActivity;
import com.mapuni.android.model.AvailableActionListxx;
import com.mapuni.android.model.FlowStepListxx;
import com.mapuni.android.model.FlowTaskListxx;
import com.mapuni.android.model.FlowTransaction;
import com.mapuni.android.model.Json_list;
import com.mapuni.android.model.LawEnforcementTask;
import com.mapuni.android.model.LeTaskEntLinkListxx;
import com.mapuni.android.model.LeTaskEntLinkListxx2;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.service.FlagEvent;
import com.mapuni.android.service.RydwServices;
import com.mapuni.android.taskmanager.TaskManagerModel.AttachAdapter;
import com.mapuni.android.taskmanager.TaskManagerModel.AttachmentAdapter;
import com.mapuni.android.taskmanager.TaskManagerModel.ProcedureAdapter;
import com.mapuni.android.taskmanager.TaskManagerModel.SelectAuditorAdapter;
import com.mapuni.android.taskmanager.TaskManagerModel.SelectAuditorAdapter1;
import com.mapuni.android.taskmanager.TaskManagerModel.SelectAuditorAdapter2;
import com.mapuni.android.taskmanager.TaskRegisterActivity.TaskUploadAsync;
import com.mapuni.android.teamcircle.SpUtils;
import com.mapuni.android.user.BaseUser;
import com.mapuni.yqydweb.YqydWebActivity;
import com.zhy.tree_view.AddressListActivity;


/**
 * ��ʾ������Ϣ����
 * 
 * @author wangliugeng
 * 
 */
@SuppressLint("ResourceAsColor")
public class TaskInformationActivity extends BaseActivity {

	private RWXX rwxx;
	/** ������ */
	private String RWBH;
	/** ����GUID */
	private String GUID;
	LayoutInflater layoutInflater;
	SlideView slideView;
	private CheckBox checkBox_moveZhr;
	private YutuLoading yutuLoading;
	private HashMap<String, Object> RWDetail;
	private String position;
	private Button btn_taskbj;
	private EditText info_taskbj;
	Spinner info_taskjg;
	private String imageGuid;
	private AutoCompleteTextView task_information_ed;
	public static final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH
			+ "Attach/RWFK/";
	private static final String TAG = "TaskInformationActivity";
	public final int SELECT_SDKARD_FILE = 2;
	/** �������� */
	private final int TASKDETAIL = 0;
	/** ���񸽼� */
	private final int TASK_ATTACHMENT = 1;
	/** �������� */
	private final int TASK_PROCESS = 2;
	/** �����ҵִ�� */
	private final int TASK_ADDCOMPANY = 3;

	/** �����ҵִ�� */
	private final int TASK_ADDCOMPANY1 = 30;

	/** ������ת */
	private final int TASK_AUDIT = 4;
	/** ����鵵 */
	private final int TASK_ARCHIVE = 5;

	/** ����״̬ */
	private String RWZT, transactedFlowInstanceId;
	/** ������Դ */
	private String RWLY="";
	/** ������������ҵ��״̬ */
	Boolean Task_Enpri_ZT = false;
	private final String TableName = "T_YDZF_RWXX";
	ArrayList<HashMap<String, Object>> qylistdata;
	/** �����б������� */
	private AttachAdapter attachAdapter;
	private ArrayList<HashMap<String, Object>> attachAdapterData = new ArrayList<HashMap<String, Object>>();
	private TextView task_addcomp_nothing, task_zhrname_tv;

	/** ���⼯�� */
	private final ArrayList<String> titles = new ArrayList<String>();
	/** �����ǩ���Ϻ� titles һһ��Ӧ */
	private final ArrayList<Integer> tags = new ArrayList<Integer>();
	/** ����������������б�־ 1���� 0 ���� */
	private String TaskFlowDirection = "-1";
	/** ��ǰ�û���ְ�� */
	private String userZw;
	/** ���������ִ���� */
	private String rwzhr_name;
	CompanyAdapter companyAdapter;
	EditText task_information_gd, selectArchivePersonEdit;
	LinearLayout move_zhr;

	StringBuffer sbZshr = new StringBuffer();
	StringBuffer sbFshr = new StringBuffer();
	StringBuffer sbZxr = new StringBuffer();
	StringBuffer sbZhr = new StringBuffer();

	EditText task_information_sp, task_information_ed_fshr;// ������ Э����
	Spinner task_information_sp_shjg;

	private int fileUploadTag = -1;
	private String rwgdTransactionType;// ����鵵��TransactionType

	private final String UserID = Global.getGlobalInstance().getUserid();
	/** �û��������� **/
	private final String UserAreaCode = Global.getGlobalInstance()
			.getAreaCode(), UserdepId = Global.getGlobalInstance().getDepId();
	private TaskManagerModel taskManagerModel = new TaskManagerModel();

	private ArrayList<HashMap<String, Object>> taskProcessData = new ArrayList<HashMap<String, Object>>();

	private final int DELSUCCESS = 1;
	private final int DELFAIL = -1;
	private final int EXCEPTION = 1003;

	private int now_type = 0;

	private BaseUser currentUser = Global.getGlobalInstance().getCurrentUser();
	String[] types = null;
	String[] values = null;
	private Handler delEntHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DELSUCCESS:
				deleteCompanyEnforcedInfomation(del_qyguid);
				// ɾ����֮�����²�ѯ����
				String sql = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
						+ " where TaskEnpriLink.TaskID='"
						+ RWBH
						+ "' order by TaskEnpriLink.UpdateTime";
				ArrayList<HashMap<String, Object>> data = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(sql);
				if (companyAdapter != null) {
					companyAdapter.updateData(data);
				}
				btn_taskbj.setVisibility(View.VISIBLE);
				Toast.makeText(TaskInformationActivity.this, "ɾ���ɹ�",
						Toast.LENGTH_LONG).show();
				break;
			case DELFAIL:
				Toast.makeText(TaskInformationActivity.this, "ɾ��ʧ��,��ͬ��������Ϣ!",
						Toast.LENGTH_LONG).show();
				break;
			case EXCEPTION:
				Toast.makeText(TaskInformationActivity.this, "ɾ�������쳣",
						Toast.LENGTH_LONG).show();
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// setContentView(R.layout.ui_mapuni);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);

		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "������Ϣ");
	//	  EventBus.getDefault().register(this); 
		rwxx = (RWXX) getIntent().getExtras().get("BusinessObj");

		RWZT = getIntent().getStringExtra("rwzt");
		transactedFlowInstanceId = getIntent().getStringExtra(
				"transactedTaskId");
		ykrwbh = getIntent().getStringExtra("ykrwbh");
		daiXieBan = getIntent().getStringExtra("daiXieBan");
		RWBH = rwxx.getCurrentID();
		requestData(transactedFlowInstanceId);
		GUID = transactedFlowInstanceId;
	}


@Override
protected void onDestroy() {
	//  EventBus.getDefault().unregister(this); // ���
	super.onDestroy();
}

@Subscribe(threadMode = ThreadMode.MAIN)
public void onEvent(FlagEvent  flag){
	
//TaskInformationActivity.this.stopService(loctionnewIntent);
	
}

	@Override
	protected void onRestart() {

		super.onRestart();
		if (companyAdapter != null) {
			// String sql =
			// "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
			// + " where TaskEnpriLink.TaskID='"
			// + RWBH
			// + "' order by TaskEnpriLink.UpdateTime";
			// qylistdata = SqliteUtil.getInstance()
			// .queryBySqlReturnArrayListHashMap(sql);
			// companyAdapter.updateData(qylistdata);
			//���Э��������һ��һ�����淵��ʱ��ҵ��ʧ������
			String sql = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
					+ " where TaskEnpriLink.TaskID='"
					+ RWBH
					+ "' order by TaskEnpriLink.UpdateTime";
			qylistdata = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql);
			companyAdapter.updateData(qylistdata);

			if (qylistdata == null || qylistdata.size() == 0) {
				task_addcomp_nothing.setVisibility(View.VISIBLE);
				task_addcomp_nothing.setText("���������޹�����ҵ��");
			} else {
				task_addcomp_nothing.setVisibility(View.GONE);
			}
		}
	}

	private void initData() {

		titles.add("������Ϣ");
		tags.add(TASKDETAIL);

		// HashMap<String, Object> conditions = new HashMap<String, Object>();
		// conditions.put("guid", GUID);
		// ArrayList<HashMap<String, Object>> rwdata =
		// SqliteUtil.getInstance().getList("rwzt", conditions, TableName);
		// if (rwdata == null || rwdata.size() == 0) {
		// Toast.makeText(this, "����ȱ�ٴ�������Ϣ����ͬ������ȫ������",
		// Toast.LENGTH_SHORT).show();
		// finish();
		// return;
		//
		// }

		// RWBH = SqliteUtil.getInstance().getList("rwbh", conditions,
		// TableName).get(0).get("rwbh").toString();
		// RWLY = SqliteUtil.getInstance().getList("rwly", conditions,
		// TableName).get(0).get("rwly").toString();
		//
		// // ��õ�ǰ��¼�û���ְ��
		// HashMap<String, Object> conditions_user = new HashMap<String,
		// Object>();
		// conditions_user = new HashMap<String, Object>();
		// conditions_user.put("UserID",
		// Global.getGlobalInstance().getUserid());
		// userZw = SqliteUtil.getInstance().getList("zw", conditions_user,
		// "PC_Users").get(0).get("zw").toString();

		if (RWZT.equals("�����") /* || RWZT.equals("��ִ��") */) {
			processStatus = getIntent().getStringExtra("ProcessStatus");

			// if (result != null) {
			// TaskFlowDirection = result;
			// if (TaskFlowDirection.equals("0")) {// ����
			// if (userZw.equals("0")) {
			// titles.add("����鵵");
			// tags.add(TASK_ARCHIVE);
			// } else {
			titles.add("������ת");
			tags.add(TASK_AUDIT);
			// }
			// titles.add("ִ�н��");
			// tags.add(TASK_ADDCOMPANY);
			// } else {
			// titles.add("������ת");
			// tags.add(TASK_AUDIT);
			// }
			// } else {
			// HashMap<String, Object> condition = new HashMap<String,
			// Object>();
			// condition.put("Tid", RWBH);
			// // SqliteUtil.getInstance().getLimitDataList("YDZF_RWLC",
			// // "FlowDirection", condition, limit, order);
			// }
		}

		if (RWZT.equals("��ǩ��")) {
			processStatus = "11";
			titles.add("����ǩ��");
			tags.add(TASK_AUDIT);
		}
		// TODO ����������ϱ���ʱ�� ��ѡ�������
		if (RWZT.equals("���ϱ�")) {
			processStatus = "22";
			titles.add("�����ϱ�");
			tags.add(TASK_AUDIT);
		}
		// else if ((RWZT.equals("���鵵"))) {
		// titles.add("����鵵");
		// tags.add(TASK_ARCHIVE);
		// titles.add("ִ�н��");
		// tags.add(TASK_ADDCOMPANY);
		// } else
		if (RWZT.equals("���鵵")) {
			titles.add("����鵵");
			tags.add(TASK_ARCHIVE);
		}
		if (RWZT.equals("��ִ��") || RWZT.equals("ִ����")) {
			// FIXME ��������ﰡ
			titles.add("����ִ��");
			tags.add(TASK_ADDCOMPANY1);
		}
		titles.add("������Ϣ");
		tags.add(TASK_ATTACHMENT);
		titles.add("ִ�����");
		tags.add(TASK_PROCESS);
		// if (RWZT.equals("�����") /*&& TaskFlowDirection.equals("1")*/) {
		titles.add("������ҵ");
		tags.add(TASK_ADDCOMPANY);
		// }
	}

	private void initView() {
		layoutInflater = LayoutInflater.from(this);
		slideView = new SlideView(this, 0);

		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();

		for (int i = 0; i < titles.size(); i++) {
			SlideOnLoadAdapter adapter = null;
			LinearLayout taskDetailView = new LinearLayout(this);
			taskDetailView.setBackgroundColor(Color.TRANSPARENT);
			taskDetailView.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			taskDetailView.setTag(tags.get(i));
			adapter = new SlideOnLoadAdapter(taskDetailView) {

				@Override
				public void OnLoad() {
					new SyncLoadingData().execute(this.view);
				}
			};

			slideView.AddPageView(adapter, titles.get(i));
		}
		slideView.Display();
		((LinearLayout) findViewById(R.id.middleLayout)).addView(slideView);

	}

	private final int NO_NET = 0;
	private final int XIAFA_SUCCESS = 1;
	private final int XIAFA_FALI = 2;
	private final int BANJIE_SUCCESS = 3;
	private final int BANJIE_FALI = 4;
	private final int GUIDANG_SUCCESS = 5;
	private final int GUIDANG_FALI = 6;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				initData();
				initView();
				break;
			case 101:// ������
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Intent intent1 = new Intent(TaskInformationActivity.this,
						AddressListActivity.class);
				intent1.putExtra("arr", zshrData);
				startActivityForResult(intent1, 0);
				break;
			case 102:// Э����
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Intent intent2 = new Intent(TaskInformationActivity.this,
						AddressListActivity.class);
				intent2.putExtra("arr", fshrData);
				intent2.putExtra("isFshr", true);
		//		startActivityForResult(intent2, 1);
				noticeToUser_fblr();
				break;
			case 999:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Intent intent3 = new Intent(TaskInformationActivity.this,
						AddressListActivity.class);
				intent3.putExtra("arr", zshrData);
				startActivityForResult(intent3, 999);
				break;
			case NO_NET:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskInformationActivity.this, "���粻ͨ�������������ã�",
						Toast.LENGTH_SHORT).show();
				break;
			case XIAFA_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

				Toast.makeText(TaskInformationActivity.this, "�����ύ�ɹ�",
						Toast.LENGTH_SHORT).show();

				TaskInformationActivity.this.finish();
				break;
			case XIAFA_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				if (!"".equals(msg.obj.toString())&&msg.obj.toString()!=null) {
					Toast.makeText(TaskInformationActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(TaskInformationActivity.this, "�����ύʧ��",
							Toast.LENGTH_SHORT).show();
				}
				
				break;
			case BANJIE_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

				Toast.makeText(TaskInformationActivity.this, "�����ύ�ɹ�",
						Toast.LENGTH_SHORT).show();
				TaskInformationActivity.this.finish();
				break;
			case BANJIE_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskInformationActivity.this, "�����ύʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case GUIDANG_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskInformationActivity.this, "����鵵�ɹ�",
						Toast.LENGTH_SHORT).show();
				TaskInformationActivity.this.finish();
				break;
			case GUIDANG_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(TaskInformationActivity.this, "����鵵ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case UploadFile.UPLOAD_CALL_BACK:
				if (fileUploadTag == 1) { // ��Աִ�����ýӿ�
					callTaskExecuteFinish();
				} else if (fileUploadTag == 2) {
					callAuditWebService();
				} else if (fileUploadTag == 3) {
					// callArchiveTaskWebService();
					callAuditWebService2();
				}

				break;
			default:
				break;
			}
		};
	};

	private boolean isLoadFinish = false;
	private boolean isRequestFinish = false;

	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private View view;
		private ArrayList<Object> obj;
		ArrayList<HashMap<String, Object>> data;
		private TextView task_information_tx_people;
		private TextView task_information_tx_fshr;
		private LinearLayout task_information_linear_fshr;
		private String publishPeo;

		@Override
		protected Void doInBackground(View... params) {
			// if(!isLoadFinish) {
			// int result = 0;
			// DataSyncModel dataSync = new DataSyncModel();
			// String[] tables = { "T_YDZF_RWXX", "T_YDZF_RWXX_USER",
			// "TaskEnpriLink"};
			// for (int i = 0; i < tables.length; i++) {
			// if (tables[i].equals("T_YDZF_RWXX")) {
			// result = dataSync.synchronizeFetchServerData(true, tables[i]);
			// if (result < 0) {// ͬ��ʧ��
			// break;
			// }
			// } else {
			// dataSync.synchronizeFetchServerData(true, tables[i]);
			// }
			// }
			// isLoadFinish = true;
			// }

			// if(!isRequestFinish) {
			// taskProcessData = getDataList(TASK_PROCESS);
			// isRequestFinish = true;
			// }

			view = params[0];
			obj = new ArrayList<Object>();
			obj.add(getDataList((Integer) view.getTag()));
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Void result) {
			int tag = (Integer) view.getTag();

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			switch (tag) {
			case TASKDETAIL:
				data = (ArrayList<HashMap<String, Object>>) obj.get(0);
				LogUtil.i("������Ϣ", data.toString());
				// �õ������˵�����
				publishPeo = (String) data.get(0).get("PublisherId");
				try {
					LoadDetailLayout loadDetailLayout = new LoadDetailLayout(
							TaskInformationActivity.this, rwxx, false);
					((LinearLayout) (view)).addView(loadDetailLayout
							.getDetailView(data.get(0)));
					LogUtil.i("������Ϣ", data.toString());

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TASK_ATTACHMENT:
				data = (ArrayList<HashMap<String, Object>>) obj.get(0);

				// Collections.sort(data, new Comparator<HashMap<String,
				// Object>>() {
				// @Override
				// public int compare(HashMap<String, Object> lhs,
				// HashMap<String, Object> rhs) {
				// String fileName1 = (String) lhs.get("FileName");
				// String fileName2 = (String) rhs.get("FileName");
				// return fileName1.hashCode() - fileName2.hashCode();
				// }
				//
				// });

				if (data != null && data.size() > 0) {
					ListView listview = new ListView(
							TaskInformationActivity.this);
					listview.setCacheColorHint(Color.TRANSPARENT);
					listview.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					AttachmentAdapter attachmentAdapter = taskManagerModel
							.getattachmentAdapter(data,
									TaskInformationActivity.this);
					listview.setAdapter(attachmentAdapter);
					((LinearLayout) (view)).addView(listview, lp);
					// ����򿪸���
					listview.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								final int arg2, long arg3) {
							final YutuLoading loading = new YutuLoading(
									TaskInformationActivity.this);
							loading.setLoadMsg("���ڼ��ظ��������Ե�...", "");
							loading.setCancelable(false);
							loading.showDialog();
							new Thread(new Runnable() {
								@Override
								public void run() {
									Looper.prepare();
									FileHelper fileHelper = new FileHelper();
									String attguid = data.get(arg2).get("Guid")
											.toString();
									String Extension = data.get(arg2)
											.get("Extension").toString();
									String FK_Unit = data.get(arg2)
											.get("FK_Unit").toString();
									
									String FileName = data.get(arg2)
									.get("FileName").toString();
									if (Extension != null
											&& !Extension.equals("")) {
//										if (".jpg".equals(Extension
//												.toLowerCase())) {
//											ArrayList<String> arrayTotal = new ArrayList<String>();
//											int result1 = fileHelper
//													.downFile(
//															attguid,
//															TaskInformationActivity.this,
//															loading);
//											if (0 == result1) {
//												arrayTotal.add(attguid
//														+ Extension);
//
//												for (HashMap<String, Object> hash : data) {
//													String exten = hash.get(
//															"Extension")
//															.toString();
//													String guid = hash.get(
//															"Guid").toString();
//													String fk = hash.get(
//															"FK_Unit")
//															.toString();
//													if (".jpg".equals(exten
//															.toLowerCase())
//															&& !attguid
//																	.equals(guid)
//															&& FK_Unit
//																	.equals(fk)) {
//														int result2 = fileHelper
//																.downFile(
//																		guid,
//																		TaskInformationActivity.this,
//																		loading);
//														if (0 == result2) {
//															arrayTotal.add(guid
//																	+ exten);
//														}
//													}
//												}
//											} else {
//												if (loading != null) {
//													loading.dismissDialog();
//												}
//												return;
//											}
//
//											Intent intent = new Intent(
//													TaskInformationActivity.this,
//													ImageGalleryActivity.class);
//											Bundle bundle = new Bundle();
//											bundle.putSerializable(
//													"arrayTotal", arrayTotal);
//											bundle.putString(
//													"attch",
//													T_Attachment
//															.transitToChinese(Integer
//																	.parseInt(FK_Unit)));
//											intent.putExtras(bundle);
//											startActivity(intent);
//											if (loading != null) {
//												loading.dismissDialog();
//											}
//											return;
//										}
									}

									fileHelper.showFileByGuid(attguid,
											TaskInformationActivity.this);
									if (loading != null) {
										loading.dismissDialog();
									}
								}
							}).start();

						}
					});
				} else {
					YutuLoading loading = new YutuLoading(
							TaskInformationActivity.this);
					loading.setLoadMsg("", "���޸�����Ϣ��");
					loading.setFailed();
					((LinearLayout) (view)).addView(loading, lp);
				}
				break;
			case TASK_PROCESS:

				data = (ArrayList<HashMap<String, Object>>) obj.get(0);

				if (data != null && data.size() > 0) {
					ListView lv = new ListView(TaskInformationActivity.this);
					lv.setCacheColorHint(Color.TRANSPARENT);
					lv.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					ProcedureAdapter proceAdapter = taskManagerModel
							.getProcedureAdapter(data,
									TaskInformationActivity.this,
									TaskFlowDirection);
					lv.setAdapter(proceAdapter);
					((LinearLayout) (view)).addView(lv, lp);
				} else {
					YutuLoading loading = new YutuLoading(
							TaskInformationActivity.this);
					loading.setLoadMsg("", "����ִ�������Ϣ��");
					loading.setFailed();
					((LinearLayout) (view)).addView(loading, lp);
				}
				break;
			case TASK_ADDCOMPANY:
				qylistdata = (ArrayList<HashMap<String, Object>>) obj.get(0);

				View addcomp_down = layoutInflater.inflate(
						R.layout.task_information_addcompany, null);
				task_addcomp_nothing = (TextView) addcomp_down
						.findViewById(R.id.task_addcomp_nothing);
				if (qylistdata == null || qylistdata.size() == 0) {
					task_addcomp_nothing.setVisibility(View.VISIBLE);
					task_addcomp_nothing.setText("���������޹�����ҵ��");
				}
				// Button task_addcomp_img = (Button)
				// addcomp_down.findViewById(R.id.addqyimg);
				// // �����ȾԴ��
				// if (rwxx.JudgeUserName(RWBH) && (RWZT.equals("��ִ��") ||
				// RWZT.equals("ִ����"))) {// �ж��Ƿ�Ϊ�Լ�������ֵΪ����ʾ�����ȾԴ��Ť
				// if (RWLY.equals("011")) {// һ���������������ȾԴ��ť�ɼ�
				// task_addcomp_img.setVisibility(View.VISIBLE);
				// task_addcomp_img.setOnClickListener(new
				// selectCompanyListener());
				// }
				// }
				// ��ҵ�б�
				ListView task_addcomp_qylist = (ListView) addcomp_down
						.findViewById(R.id.task_addcomp_qylist);
				//companyAdapter = new CompanyAdapter(qylistdata);
				qyListAdapter2 qyListAdapter =new qyListAdapter2(qylistdata);
				
				task_addcomp_qylist.setAdapter(qyListAdapter);
				// �������һ��һ��
				 task_addcomp_qylist.setOnItemClickListener(new
				 OnItemClickListener() {
				 @Override
				 public void onItemClick(AdapterView<?> arg0, View arg1, int
				 arg2, long arg3) {
				 String qyid = qylistdata.get(arg2).get("qyguid").toString();
//				 Intent intent = new Intent(TaskInformationActivity.this,
//				 EnterpriseArchivesActivitySlide.class);
				Intent intent = new Intent(TaskInformationActivity.this, YqydWebActivity.class);
				 intent.putExtra("qyid", qyid);
				 startActivity(intent);
				 }
				 });
				// ����ɾ��
				// task_addcomp_qylist.setOnItemLongClickListener(new
				// OnItemLongClickListener() {
				// @Override
				// public boolean onItemLongClick(AdapterView<?> arg0, View
				// view, int position, long arg3) {
				//
				// showDelDialog(position, view);
				// return true;
				// }
				// });
				((LinearLayout) (view)).addView(addcomp_down, lp);
				break;
			case TASK_ADDCOMPANY1:
				qylistdata = (ArrayList<HashMap<String, Object>>) obj.get(0);
//				 try {
//				 LoadDetailLayout loadDetailLayout = new LoadDetailLayout(
//				 TaskInformationActivity.this, rwxx, false);
//				 ((LinearLayout) (view)).addView(loadDetailLayout
//				 .getDetailView(data.get(0)));
//				 LogUtil.i("������Ϣ", data.toString());
//				
//				 } catch (Exception e) {
//				 // TODO: handle exception
//				 }

				View addcomp_down1 = layoutInflater.inflate(
						R.layout.task_information_addcompany1, null);
				task_addcomp_nothing = (TextView) addcomp_down1
						.findViewById(R.id.task_addcomp_nothing);
				if (qylistdata == null || qylistdata.size() == 0) {
					task_addcomp_nothing.setVisibility(View.VISIBLE);
					task_addcomp_nothing.setText("���������޹�����ҵ��");
				}
				Button task_addcomp_img = (Button) addcomp_down1
						.findViewById(R.id.addqyimg);
				// �����ȾԴ��
				if (RWZT.equals("��ִ��") || RWZT.equals("ִ����")) {
					// ����ж�������Դ BYK
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					// conditions.put("guid", GUID);
					// conditions.put("rwbh", GUID);
					conditions.put("rwbh", RWBH);
					ArrayList<HashMap<String, Object>> hashlist = SqliteUtil
							.getInstance().getList("rwly", conditions,
									TableName);
					if (hashlist.size() > 0) {
						RWLY = hashlist.get(0).get("rwly").toString();
					} else {
						RWLY = "011";
					}

					// �ж��Ƿ�Ϊ�Լ�������ֵΪ����ʾ�����ȾԴ��Ť
					if (RWLY.equals("010")||RWLY.equals("012")) {
						task_addcomp_img.setVisibility(View.GONE);
						task_addcomp_img.setVisibility(View.INVISIBLE);
					
		
					} else if(RWLY.equals("011")){
						// һ���������������ȾԴ��ť�ɼ�
						task_addcomp_img.setVisibility(View.VISIBLE);
						task_addcomp_img
								.setOnClickListener(new selectCompanyListener());
					}
				}
				// ��ҵ�б�
				ListView task_addcomp_qylist1 = (ListView) addcomp_down1
						.findViewById(R.id.task_addcomp_qylist);
				companyAdapter = new CompanyAdapter(qylistdata);
				task_addcomp_qylist1.setAdapter(companyAdapter);
				// // �������һ��һ��
				// task_addcomp_qylist1.setOnItemClickListener(new
				// OnItemClickListener() {
				// @Override
				// public void onItemClick(AdapterView<?> arg0, View arg1, int
				// arg2, long arg3) {
				// String qyid = qylistdata.get(arg2).get("qyguid").toString();
				// Intent intent = new Intent(TaskInformationActivity.this,
				// EnterpriseArchivesActivitySlide.class);
				// intent.putExtra("qyid", qyid);
				// startActivity(intent);
				// }
				// });
				// ����ɾ��
				task_addcomp_qylist1
						.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									View view, int position, long arg3) {
								// ��ʱ���γ���ɾ������
								showDelDialog(position, view);

								return true;
							}
						});

				if (/*
					 * rwxx.JudgeUserName(RWBH) && (
					 */RWZT.equals("��ִ��") || RWZT.equals("ִ����")) {
					LinearLayout add_fkinfo = (LinearLayout) addcomp_down1
							.findViewById(R.id.add_fkinfo);
					add_fkinfo.setVisibility(View.VISIBLE);

					ListView task_fjlist = (ListView) addcomp_down1
							.findViewById(R.id.fjlist);
					// attachAdapterData =
					// getAttachAdapterData(T_Attachment.RWFK
					// + "", RWBH + "_" + UserID);
					// �޸�fk_id
					attachAdapterData = getAttachAdapterData(T_Attachment.RWFK
							+ "", RWBH);
					attachAdapter = taskManagerModel.getattachAdapter(
							attachAdapterData, TaskInformationActivity.this);
					task_fjlist.setCacheColorHint(Color.TRANSPARENT);
					task_fjlist.setAdapter(attachAdapter);
					task_fjlist
							.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									String guid = ((TextView) (arg1
											.findViewById(R.id.listitem_text)))
											.getTag().toString();
									FileHelper fileHelper = new FileHelper();
									fileHelper.showFileByGuid(guid,
											TaskInformationActivity.this);
								}
							});

					task_fjlist
							.setOnItemLongClickListener(new OnItemLongClickListener() {
								@Override
								public boolean onItemLongClick(
										AdapterView<?> parent, View view,
										int position, long id) {
									String guid = ((TextView) (view
											.findViewById(R.id.listitem_text)))
											.getTag().toString();
									String fileNam = ((TextView) (view
											.findViewById(R.id.listitem_text)))
											.getText().toString();

									showDialog(guid, fileNam);
									return true;
								}
							});
					info_taskbj = (EditText) addcomp_down1
							.findViewById(R.id.task_addcomp_ed);// �������
					info_taskjg = (Spinner) addcomp_down1
							.findViewById(R.id.task_information_sp_shjg);// �������

					ArrayList<AvailableActionListxx> availableactionxx = flowtransaction
							.getAvailableActionList();
					int spinnerposition = availableactionxx.size();
					types = new String[spinnerposition];
					values = new String[spinnerposition];
					for (int j = 0; j < spinnerposition; j++) {
						types[j] = availableactionxx.get(j).getCode() + "";
						values[j] = availableactionxx.get(j).getName() + "";
					}
					//

					ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
							TaskInformationActivity.this,
							android.R.layout.simple_spinner_item, values);

					// ���������б�ķ��
					typeAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// ��adapter��ӵ�m_Spinner��
					info_taskjg.setAdapter(typeAdapter);
					info_taskjg.setSelection(0);
					OperationCodeFinish = types[0];
					// ���Spinner�¼�����
					info_taskjg
							.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// ��ȡ��Ӧ��ֵ
									OperationCodeFinish = types[arg2];

									arg0.setVisibility(View.VISIBLE);
								}

								public void onNothingSelected(
										AdapterView<?> arg0) {
									// m_TextView.setText("��ѡ��һ��...");
								}
							});

					btn_taskbj = (Button) addcomp_down1
							.findViewById(R.id.task_addcomp_btn);// ������ɰ�ť
					if (/*
						 * rwxx.JudgeUserName(RWBH) && (
						 */RWZT.equals("��ִ��") || RWZT.equals("ִ����")) {
						btn_taskbj.setVisibility(View.VISIBLE);
					} else {
						btn_taskbj.setVisibility(View.GONE);
					}

					LinearLayout task_add_btn_layout = (LinearLayout) addcomp_down1
							.findViewById(R.id.task_add_btn_layout);
					// task_addcomp_img.setVisibility(View.VISIBLE);
					// if (RWLY.equals("011")) {// һ���������������ȾԴ��ť�ɼ�
					// task_addcomp_img.setVisibility(View.VISIBLE);
					// } else if (RWLY.equals("012")) {
					// task_addcomp_img.setVisibility(View.GONE);
					// }
					task_add_btn_layout.setVisibility(View.VISIBLE);
					btn_taskbj.setOnClickListener(new TaskbjListener());
	
				}
				((LinearLayout) (view)).addView(addcomp_down1, lp);
				break;
			case TASK_AUDIT:
				final View sh_down = layoutInflater.inflate(
						R.layout.task_information_sh_down, null);
				
				task_information_ed = (AutoCompleteTextView) sh_down
							.findViewById(R.id.task_information_ed);// ������
				final LinearLayout shLayout = (LinearLayout) sh_down
						.findViewById(R.id.task_information_sh);
				LinearLayout sh_spinner_Layout = (LinearLayout) sh_down
						.findViewById(R.id.task_information_sh_spinner);
				LinearLayout zxLayout = (LinearLayout) sh_down
						.findViewById(R.id.task_information_zx);
				LinearLayout add_fj = (LinearLayout) sh_down
						.findViewById(R.id.add_fj);
				move_zhr = (LinearLayout) sh_down
						.findViewById(R.id.task_information_moveZhr);

				final TextView task_information_tv = (TextView) sh_down
						.findViewById(R.id.task_information_tv);
				task_zhrname_tv = (TextView) sh_down
						.findViewById(R.id.task_information_zhrname);
				task_information_tx_people = (TextView) sh_down
						.findViewById(R.id.task_information_tx_people);
				task_information_tx_fshr = (TextView) sh_down
						.findViewById(R.id.task_information_tx_fshr);
				checkBox_moveZhr = (CheckBox) sh_down
						.findViewById(R.id.checkBox_moveZhr);
				task_information_tv.setText("���������");
				task_information_sp_shjg = (Spinner) sh_down
						.findViewById(R.id.task_information_sp_shjg);// ��˽��
				Button task_information_button = (Button) sh_down
						.findViewById(R.id.task_information_button);// �ύ

				
				// List<SpinnerItem> shjgList = new ArrayList<SpinnerItem>();
				// ��ȡ�������
				ArrayList<AvailableActionListxx> availableactionxx = flowtransaction
						.getAvailableActionList();

				final String isMustChoose = availableactionxx.get(0)
						.getIsMustChoose();
				
				int spinnerposition = availableactionxx.size();
				types = new String[spinnerposition];
				values = new String[spinnerposition];
				for (int j = 0; j < spinnerposition; j++) {
					types[j] = availableactionxx.get(j).getCode() + "";
					values[j] = availableactionxx.get(j).getName() + "";
				}
			

				ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
						TaskInformationActivity.this,
						android.R.layout.simple_spinner_item, values);
	
				// ���������б�ķ��
				typeAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// ��adapter��ӵ�m_Spinner��
				task_information_sp_shjg.setAdapter(typeAdapter);
				task_information_sp_shjg.setSelection(0);
				OperationCode = types[0];
				// ���Spinner�¼�����
				task_information_sp_shjg
						.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// ��ȡ��Ӧ��ֵ
								OperationCode = types[arg2];
								arg0.setVisibility(View.VISIBLE);

								// ��˲�ͨ��������� ֱ�ӻ�����һ��
								if (arg2 == 1) {
									task_information_tx_people
											.setVisibility(View.INVISIBLE);
									task_information_sp
											.setVisibility(View.INVISIBLE);
									//
									if (processStatus.equals("17")) {

										now_type = 1;
									} else {
										now_type = 2;

									}
									task_information_tx_fshr
											.setVisibility(View.INVISIBLE);
									task_information_ed_fshr
											.setVisibility(View.INVISIBLE);

									if (processStatus.equals("13")) {
										now_type = 3;
										task_information_tx_people
												.setVisibility(View.VISIBLE);
										task_information_sp
												.setVisibility(View.VISIBLE);
										task_information_tx_fshr
												.setVisibility(View.INVISIBLE);
										task_information_ed_fshr
												.setVisibility(View.INVISIBLE);
										task_information_tx_people
												.setText("ѡ�����У�");
										task_information_sp.setHint("��ѡ������");
										task_information_sp.setText("");
										task_information_sp.setTag("");

										// TODO ����ѡ������

										task_information_sp
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														createDialogViewForTaskType();
													}
												});
									}

									// ��ȡ�Ͻڵ������Ա �������� �������� code 32
								} else if (arg2 == 0) {
									
									if ("true".equals(isMustChoose)) {
										task_information_tx_people
										.setVisibility(View.VISIBLE);
								task_information_sp
										.setVisibility(View.VISIBLE);

								if (processStatus.equals("17")) {
									now_type = 0;
									task_information_tx_people
											.setText("�鵵�ˣ�");
									task_information_linear_fshr
											.setVisibility(View.GONE);
									task_information_sp
											.setHint("��ѡ��鵵��");
									task_information_linear_fshr
											.setClickable(false);

								} else if (processStatus.equals("18")) {
									now_type = 0;
									task_information_tx_people.setText("�鵵�ˣ�");
									task_information_linear_fshr.setVisibility(View.GONE);
									task_information_sp.setHint("��ѡ��鵵��");
									task_information_linear_fshr.setClickable(false);

								} else if(processStatus.equals("11")){
									task_information_tv.setText("��д�����");
									
									task_information_tx_people.setText("�����ˣ�");
									task_information_sp.setHint("��ѡ���½װ�����");
									task_information_linear_fshr=(LinearLayout)sh_down.findViewById(R.id.task_information_linear_fshr);
									task_information_linear_fshr.setVisibility(View.GONE);
									
								}else{

									task_information_tx_fshr
											.setVisibility(View.VISIBLE);
									task_information_ed_fshr
											.setVisibility(View.VISIBLE);
									 now_type=0;
									task_information_tx_people
											.setText("�����ˣ�");
									task_information_sp
											.setHint("��ѡ����������");
									SelectAuditorListener listener = new SelectAuditorListener();
									task_information_sp
											.setOnClickListener(listener);

									task_information_ed_fshr
											.setOnClickListener(listener);

								}
								// ��������Ϊ���ϱ� ״̬
								if (processStatus.equals("22")) {
									task_information_tx_people
											.setVisibility(View.INVISIBLE);
									task_information_sp
											.setVisibility(View.INVISIBLE);
									task_information_tx_fshr
											.setVisibility(View.INVISIBLE);
									task_information_ed_fshr
											.setVisibility(View.INVISIBLE);
									task_information_tv
											.setText("�������ʣ�");
								}

								task_information_sp.setText("");
								task_information_sp.setTag("");
								task_information_ed_fshr.setText("");
								task_information_ed_fshr.setTag("");
									}else{
										
											task_information_tx_people
											.setVisibility(View.GONE);
									task_information_sp
											.setVisibility(View.GONE);

									now_type = 0;

									task_information_tx_fshr
											.setVisibility(View.GONE);
									task_information_ed_fshr
											.setVisibility(View.GONE);
											
										
									}

									task_information_sp.setText("");
									task_information_sp.setTag("");
									task_information_ed_fshr.setText("");
									task_information_ed_fshr.setTag("");
								} else if (arg2 == 2) {
									now_type = 3;
									task_information_tx_people
											.setVisibility(View.VISIBLE);
									task_information_sp
											.setVisibility(View.VISIBLE);
									task_information_tx_fshr
											.setVisibility(View.INVISIBLE);
									task_information_ed_fshr
											.setVisibility(View.INVISIBLE);
									task_information_tx_people.setText("ѡ�����У�");
									task_information_sp.setHint("��ѡ������");
									task_information_sp.setText("");
									task_information_sp.setTag("");

									// TODO ����ѡ������

									task_information_sp
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													createDialogViewForTaskType();
												}
											});

								}

							}

							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
	
				task_information_sp = (EditText) sh_down
						.findViewById(R.id.task_information_ed_zshr);// �������
				task_information_ed_fshr = (EditText) sh_down
						.findViewById(R.id.task_information_ed_fshr);// �������
				task_information_linear_fshr = (LinearLayout) sh_down
						.findViewById(R.id.task_information_linear_fshr);

				if (processStatus.equals("17")) {
					task_information_tx_people.setText("�鵵�ˣ�");
					task_information_linear_fshr.setVisibility(View.GONE);
					task_information_sp.setHint("��ѡ��鵵��");
					task_information_linear_fshr.setClickable(false);
				}

				SelectAuditorListener listener = new SelectAuditorListener();
				task_information_sp.setOnClickListener(listener);
				task_information_ed_fshr.setOnClickListener(listener);


				ListView task_fjlist = (ListView) sh_down
						.findViewById(R.id.fjlist);
				// attachAdapterData = getAttachAdapterData(
				// T_Attachment.RWFK + "", RWBH + "_" + UserID);
				// �޸� fkid
				attachAdapterData = getAttachAdapterData(
						T_Attachment.RWFK + "", RWBH);
				attachAdapter = taskManagerModel.getattachAdapter(
						attachAdapterData, TaskInformationActivity.this);
				task_fjlist.setCacheColorHint(Color.TRANSPARENT);
				task_fjlist.setAdapter(attachAdapter);
				task_fjlist
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								String guid = ((TextView) (arg1
										.findViewById(R.id.listitem_text)))
										.getTag().toString();
								FileHelper fileHelper = new FileHelper();
								fileHelper.showFileByGuid(guid,
										TaskInformationActivity.this);
							}
						});

				task_fjlist
						.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								String guid = ((TextView) (view
										.findViewById(R.id.listitem_text)))
										.getTag().toString();
								String fileNam = ((TextView) (view
										.findViewById(R.id.listitem_text)))
										.getText().toString();

								showDialog(guid, fileNam);
								return true;
							}
						});

				task_information_button
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// ������Ϊ������
								if (task_information_ed.getText().toString()
										.equals("")) {
									Toast.makeText(
											TaskInformationActivity.this,
											"����д�����", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								if (task_information_sp.getText().toString()
										.equals("")
										&& now_type == 0
										&& !processStatus.equals("22")) {
									if (!processStatus.equals("17")&&!processStatus.equals("18")) {
										Toast.makeText(
												TaskInformationActivity.this,
												"��ѡ������ˣ�", Toast.LENGTH_SHORT)
												.show();
										return;
									} else {
										task_information_sp.setTag(0);
									}

								}

								if (task_information_sp
										.getText()
										.toString()
										.equals(task_information_ed_fshr
												.getText().toString())&& now_type == 0&& !processStatus.equals("22")) {

									if (!processStatus.equals("17")&&!processStatus.equals("18")) {
										Toast.makeText(
												TaskInformationActivity.this,
												"�����˺�Э���˲���Ϊͬһ����!",
												Toast.LENGTH_SHORT).show();
										return;

									}

								}

								ArrayList<TaskFile> taskFile2 = getAllUploadFile(
										T_Attachment.RWFK, RWBH);

								if (taskFile2 != null && taskFile2.size() > 0) {
							
									upLoadFilesMethod2(taskFile2,
											TaskInformationActivity.this, GUID);
								}

								callAuditWebService1();
							}
						});
				((LinearLayout) (view)).addView(sh_down, lp);
				break;
			// ����鵵
			case TASK_ARCHIVE:
				View gd_down = layoutInflater.inflate(
						R.layout.task_information_sh_down, null);
				LinearLayout gd_spinner_Layout = (LinearLayout) gd_down
						.findViewById(R.id.task_information_sh_spinner);
				LinearLayout gdzxLayout = (LinearLayout) gd_down
						.findViewById(R.id.task_information_zx);

				gd_down.findViewById(R.id.task_information_sh).setVisibility(
						View.GONE);
				// ���õ�����ʱ�鵵ʱ�����ϴ�������Ϣ_byk
				gd_down.findViewById(R.id.addfjtableLayout).setVisibility(
						View.VISIBLE);

				TextView task_gd_tv = (TextView) gd_down
						.findViewById(R.id.task_information_tv);
				task_gd_tv.setText("�������ʣ�");
				task_information_gd = (EditText) gd_down
						.findViewById(R.id.task_information_ed);// �鵵�ؽ���
				Button task_information_gdbutton = (Button) gd_down
						.findViewById(R.id.task_information_button);// �ύ
				LinearLayout gdfj_Layout = (LinearLayout) gd_down
						.findViewById(R.id.add_fj);
				gd_spinner_Layout.setVisibility(View.GONE);
				gdzxLayout.setVisibility(View.GONE);
				gdfj_Layout.setVisibility(View.VISIBLE);
				// ����鵵ҳ�渽���б�
				ListView task_gdfjlist = (ListView) gd_down
						.findViewById(R.id.fjlist);
	
				// �޸�fkid
				attachAdapterData = getAttachAdapterData(
						T_Attachment.RWFK + "", RWBH);
				attachAdapter = taskManagerModel.getattachAdapter(
						attachAdapterData, TaskInformationActivity.this);
				task_gdfjlist.setCacheColorHint(Color.TRANSPARENT);
				task_gdfjlist.setAdapter(attachAdapter);
				task_gdfjlist
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								String guid = ((TextView) (arg1
										.findViewById(R.id.listitem_text)))
										.getTag().toString();
								FileHelper fileHelper = new FileHelper();
								fileHelper.showFileByGuid(guid,
										TaskInformationActivity.this);
							}
						});

				task_gdfjlist
						.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								String guid = ((TextView) (view
										.findViewById(R.id.listitem_text)))
										.getTag().toString();
								String fileNam = ((TextView) (view
										.findViewById(R.id.listitem_text)))
										.getText().toString();

								showDialog(guid, fileNam);
								return true;
							}
						});
				task_information_gd.setText("");
				task_information_gdbutton
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								if (task_information_gd.getText().toString()
										.equals("")) {
									Toast.makeText(
											TaskInformationActivity.this,
											"���������ʣ�", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								showgdDialog();
							}
						});

				gd_down.findViewById(R.id.task_information_ed_zshr)
						.setOnClickListener(new SelectAuditorListener());

				((LinearLayout) (view)).addView(gd_down, lp);
				break;
			default:
				break;
			}
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}

		}
	}
	private ArrayList<HashMap<String, Object>> getDataList(int tagID) {
		ArrayList<HashMap<String, Object>> data = null;
		switch (tagID) {
		case TASKDETAIL:
			data = new ArrayList<HashMap<String, Object>>();
			if (lawenforcementtask != null && !lawenforcementtask.equals("")) {
				HashMap<String, Object> arr = new HashMap<String, Object>();
				arr.put("TaskName", lawenforcementtask.getTaskName());// ��������
				
				if ("010".equals(lawenforcementtask.getTaskSource())) {
					arr.put("TaskType", "��ʱ������");
				}else{
					// ��ȡ��������
					String sql_rwlx = "select Name from T_YDZF_RWLX where Code = '"
							+ lawenforcementtask.getTaskType() + "'";
					String rwlx = SqliteUtil.getInstance().getDepidByDepName(
							sql_rwlx);
					arr.put("TaskType", rwlx);// ��������
				}
			
				// ��ȡ�����̶�
				String sql = "select Name from T_YDZF_JJCD where code = '"
						+ lawenforcementtask.getUrgency().trim() + "'";
				String jjcd = SqliteUtil.getInstance().getDepidByDepName(sql);
				arr.put("Urgency", jjcd);// �����̶�
				arr.put("PublishedTime", lawenforcementtask.getPublishedTime());// ����ʱ��
				arr.put("TransactedTime",
						lawenforcementtask.getTransactedTime());// �������
				// ��ȡ������
				// String sql_fbr =
				// "select U_RealName from PC_Users where UserID = '"
				// + lawenforcementtask.getPublisher() + "'";
				String fbr = lawenforcementtask.getPublisher();
				arr.put("PublisherId", fbr);// ������
				arr.put("Remark", lawenforcementtask.getRemark());// ��������
				LogUtil.i("Remark",
						"���������������ݣ�" + lawenforcementtask.getRemark());
				arr.put("rwztmc", RWZT);// ִ��״̬
				data.add(arr);
			}
			// RWDetail = rwxx.getDetailed(GUID);
			// RWBH = RWDetail.get("rwbh").toString();// ��ʼ��������
			// data.add(RWDetail);
			break;
		case TASK_ATTACHMENT:
			data = new ArrayList<HashMap<String, Object>>();
			ArrayList<LeTaskEntLinkListxx> letaskentlinklistxxx = lawenforcementtask
					.getLeTaskEntLinkList();
			// for (int i = 0; i < letaskentlinklistxxx.size(); i++) {
			// HashMap<String, Object> glqyArr = new HashMap<String, Object>();
			// glqyArr.put("EntName", letaskentlinklistxxx.get(i).getEntName());
			// glqyArr.put("Status", letaskentlinklistxxx.get(i).getStatus());
			// glqyArr.put("EntCode", letaskentlinklistxxx.get(i).getEntCode());
			// data.add(glqyArr);
			// }

			// �ӱ������ݿ��ж�ȡ������صĸ�����Ϣ
			// String sql_attach =
			// "select * from T_Attachment where fk_id like'%"
			// + RWBH + "%'";
			// data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
			// sql_attach);

			// TODO�޸Ĳ���

//			// �޸Ķ�ȡ������صĸ�����Ϣ�����ú�̨�ӿڣ��Ӻ�̨���ݿ��ȡ����ͬ�����������ݿ��� zhaorq2014.2.18
//			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
//
//			HashMap<String, Object> param1 = new HashMap<String, Object>();
//			// param1.put("token", "");
//			// ����
//			param1.put("BizType", "10");
//			param1.put("BizCode", RWBH);
//			param1.put("BizId", lawenforcementtask.getTaskId());
//			params0.add(param1);
//			String listToJSON = JsonHelper.listToJSONXin(params0);
//
//			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
//			HashMap<String, Object> param = new HashMap<String, Object>();
//			// param.put("parms", TransactorCode);
//			param.put("attachmentjson", listToJSON);
//			params.add(param);
//
//			String json0 = null;
//			try {
//				json0 = (String) WebServiceProvider.callWebService(
//						Global.NAMESPACE, "GetAttachmentList", params, Global
//								.getGlobalInstance().getSystemurl()
//								+ Global.WEBSERVICE_URL,
//						WebServiceProvider.RETURN_STRING, true);
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//			// ��ȡ�����ӿڵ���ͨ
//			if (json0 != null && !json0.equals("")) {
//				String[] node = { "Id", "BizType", "BizCode", "BizId",
//						"FileName", "FilePath", "Extension", "FileType",
//						"LinkUrl", "Thumbnail", "Remark", "CreatedTime",
//						"UpdatedTime" };
//				ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(
//						json0, node);
//
//				for (int i = 0; i < list.size(); i++) {
//					HashMap<String, Object> hashmap2 = new HashMap<String, Object>();
//					hashmap2.put("Guid", list.get(i).get("Guid").toString());
//					hashmap2.put("FileName", list.get(i).get("FileName")
//							.toString());
//					hashmap2.put("Extension", list.get(i).get("Extension")
//							.toString());
//					hashmap2.put("FK_Unit", list.get(i).get("FK_Unit")
//							.toString());
//					data.add(hashmap2);
//					// // ���ݸ�����guid�жϱ��������Ƿ���ڸ��ڸø�����¼��������update,��������insert
//					String rwguid = list.get(i).get("Guid").toString();
//					ContentValues updateValues = new ContentValues();
//					updateValues.put("FileName", list.get(i).get("FileName")
//							.toString());
//					updateValues.put("FilePath", list.get(i).get("FilePath")
//							.toString());
//					updateValues.put("Extension", list.get(i).get("Extension")
//							.toString());
//					updateValues.put("FK_Unit", list.get(i).get("FK_Unit")
//							.toString());
//					updateValues.put("FK_Id", list.get(i).get("FK_Id")
//							.toString());
//					updateValues.put("Remark", list.get(i).get("Remark")
//							.toString());
//					updateValues.put("LinkUrl", list.get(i).get("LinkUrl")
//							.toString());
//					updateValues.put("UpdateTime", list.get(i)
//							.get("UpdateTime").toString());
//					updateValues.put("FileType", list.get(i).get("FileType")
//							.toString());
//
//					String guidSelect = SqliteUtil.getInstance()
//							.getDepidByUserid(
//									"select guid from T_Attachment where  guid='"
//											+ rwguid + "'");
//					if (guidSelect != null && !guidSelect.equals("")) {
//						String[] whereArgs = { rwguid };
//						try {
//							SqliteUtil.getInstance().update("T_Attachment",
//									updateValues, "guid=?", whereArgs);
//						} catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} else {
//						updateValues.put("guid", rwguid);
//						SqliteUtil.getInstance().insert(updateValues,
//								"T_Attachment");
//					}
//				}
//
//			}
			
			
			data = new ArrayList<HashMap<String, Object>>();
   ///  ������Ϣ ��BUG
//			// �ӱ������ݿ��ж�ȡ������صĸ�����Ϣ
//			String sql_attach = "select * from T_Attachment where fk_id like'%"
//					+ RWBH + "%'";
//			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
//					sql_attach);
			// �޸Ķ�ȡ������صĸ�����Ϣ�����ú�̨�ӿڣ��Ӻ�̨���ݿ��ȡ����ͬ�����������ݿ��� zhaorq2014.2.18
//			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
//
//			HashMap<String, Object> param1 = new HashMap<String, Object>();
//			// param1.put("token", "");
//			// ����
//			param1.put("BizType", "10");
//			param1.put("BizCode", RWBH);
//			//TODO ��ȡ ��ǰ����id ���û�ȡ�����Ĳ���
//	//		param1.put("BizId", lawenforcementtask.getTaskId());
//			params0.add(param1);
//			String listToJSON = JsonHelper.listToJSONXin(params0);
//
//			ArrayList<HashMap<String, Object>> params3 = new ArrayList<HashMap<String, Object>>();
//			HashMap<String, Object> param4 = new HashMap<String, Object>();
//			// param.put("parms", TransactorCode);
//			param4.put("attachmentjson", listToJSON);
//			params3.add(param4);
//
//			String json0 = null;
//			try {
//				json0 = (String) WebServiceProvider.callWebService(
//						Global.NAMESPACE, "GetAttachmentList", params3, Global
//								.getGlobalInstance().getSystemurl()
//								+ Global.WEBSERVICE_URL,
//						WebServiceProvider.RETURN_STRING, true);
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//			if (json0 != null && !json0.equals("")) {
//				String[] node = { "Guid", "FileName", "FilePath", "Extension",
//						"FK_Unit", "FK_Id", "Remark", "LinkUrl", "UpdateTime",
//						"FileType" };
//				ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(
//						json0, node);
//				for (int i = 0; i < list.size(); i++) {
//					HashMap<String, Object> hashmap = new HashMap<String, Object>();
//					hashmap.put("Guid", list.get(i).get("Guid").toString());
//					hashmap.put("FileName", list.get(i).get("FileName")
//							.toString());
//					hashmap.put("Extension", list.get(i).get("Extension")
//							.toString());
//					hashmap.put("FK_Unit", list.get(i).get("FK_Unit")
//							.toString());
//					data.add(hashmap);
//					// ���ݸ�����guid�жϱ��������Ƿ���ڸ��ڸø�����¼��������update,��������insert
//					String rwguid = list.get(i).get("Guid").toString();
//					ContentValues updateValues = new ContentValues();
//					updateValues.put("FileName", list.get(i).get("FileName")
//							.toString());
//					updateValues.put("FilePath", list.get(i).get("FilePath")
//							.toString());
//					updateValues.put("Extension", list.get(i).get("Extension")
//							.toString());
//					updateValues.put("FK_Unit", list.get(i).get("FK_Unit")
//							.toString());
//					updateValues.put("FK_Id", list.get(i).get("FK_Id")
//							.toString());
//					updateValues.put("Remark", list.get(i).get("Remark")
//							.toString());
//					updateValues.put("LinkUrl", list.get(i).get("LinkUrl")
//							.toString());
//					updateValues.put("UpdateTime", list.get(i)
//							.get("UpdateTime").toString());
//					updateValues.put("FileType", list.get(i).get("FileType")
//							.toString());
//
//					String guidSelect = SqliteUtil.getInstance()
//							.getDepidByUserid(
//									"select guid from T_Attachment where  guid='"
//											+ rwguid + "'");
//					if (guidSelect != null && !guidSelect.equals("")) {
//						String[] whereArgs = { rwguid };
//						try {
//							SqliteUtil.getInstance().update("T_Attachment",
//									updateValues, "guid=?", whereArgs);
//						} catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} else {
//						updateValues.put("guid", rwguid);
//						SqliteUtil.getInstance().insert(updateValues,
//								"T_Attachment");
//					}
//				}
		//	}
			//��ȡ�������и���
			// �޸Ķ�ȡ������صĸ�����Ϣ�����ú�̨�ӿڣ��Ӻ�̨���ݿ��ȡ����ͬ�����������ݿ��� zhaorq2014.2.18
			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> param1 = new HashMap<String, Object>();
			// param1.put("token", "");
			// ����
			param1.put("BizType", "10");
			param1.put("BizCode", RWBH);
			param1.put("BizId", lawenforcementtask.getTaskId());
			params0.add(param1);
			String listToJSON = JsonHelper.listToJSONXin(params0);

			ArrayList<HashMap<String, Object>> params2 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param2 = new HashMap<String, Object>();
			// param.put("parms", TransactorCode);
			param2.put("attachmentjson", listToJSON);
			params2.add(param2);

			String json0 = null;
			try {
				json0 = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, "GetAttachmentList", params2, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			// ��ȡ�����ӿڵ���ͨ
			if (json0 != null && !json0.equals("")) {
				
				String[] node = { "Id", "BizType", "BizCode", "BizId",
						"FileName", "FilePath", "Extension", "FileType",
						"LinkUrl", "Thumbnail", "Remark", "CreatedTime",
						"UpdatedTime" };
				
				
				ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(
						json0, node);
				
				
		for (int i = 0; i < list.size(); i++) {
					
					String msg = list.get(i).get("FilePath").toString();
					
					  String[] sss=msg.split("/");
					  
					  String string2 = sss[sss.length-1];
				     String[]  newGuid=string2.split("\\.");
					HashMap<String, Object> hashmap2 = new HashMap<String, Object>();
					hashmap2.put("Guid", newGuid[0]);
					hashmap2.put("FileName", list.get(i).get("FileName")
							.toString());
					hashmap2.put("Extension", list.get(i).get("Extension")
							.toString());
//					hashmap2.put("FK_Unit", list.get(i).get("FK_Unit")
//							.toString());
					//����ȫ��ͼ��һ��
					hashmap2.put("FK_Unit", 1);
					data.add(hashmap2);
					// // ���ݸ�����guid�жϱ��������Ƿ���ڸ��ڸø�����¼��������update,��������insert
					String rwguid = newGuid[0];
					ContentValues updateValues = new ContentValues();
					updateValues.put("FileName", list.get(i).get("FileName")
							.toString());
					updateValues.put("FilePath", list.get(i).get("FilePath")
							.toString());
					updateValues.put("Extension", list.get(i).get("Extension")
							.toString());
					//�޸�
					updateValues.put("FK_Unit", 1);
					//
					updateValues.put("FK_Id", list.get(i).get("BizCode")
							.toString());
					updateValues.put("Remark", list.get(i).get("Remark")
							.toString());
					updateValues.put("LinkUrl", list.get(i).get("LinkUrl")
							.toString());
					updateValues.put("UpdateTime",
							list.get(i).get("UpdatedTime").toString());
					updateValues.put("FileType", list.get(i).get("FileType")
							.toString());

					String guidSelect = SqliteUtil.getInstance()
							.getDepidByUserid(
									"select guid from T_Attachment where  guid='"
											+ rwguid + "'");
					if (guidSelect != null && !guidSelect.equals("")) {
						String[] whereArgs = { rwguid };
						try {
							SqliteUtil.getInstance().update("T_Attachment",
									updateValues, "guid=?", whereArgs);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						updateValues.put("guid", rwguid);
						SqliteUtil.getInstance().insert(updateValues,
								"T_Attachment");
					}
				}
		
			}
			
			break;
		case TASK_PROCESS:
			data = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < FlowTaskListxxArr.size(); i++) {
				
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("SecondaryAuditUserId", FlowTaskListxxArr.get(i)
						.getTransactorPosition());
				hashmap.put("AuditName", FlowTaskListxxArr.get(i)
						.getTransactor());
				hashmap.put("AuditTime", FlowTaskListxxArr.get(i)
						.getTransactedTime());
				hashmap.put("NodeId", FlowTaskListxxArr.get(i)
						.getTransactorPosition());
				if (FlowTaskListxxArr.get(i)
						.getComment()!=null) {
					hashmap.put("AuditResult", FlowTaskListxxArr.get(i)
							.getComment());
				}else{
					hashmap.put("AuditResult", "");
				}
				
				hashmap.put("FlowDirection", FlowTaskListxxArr.get(i)
						.getFlowTaskId());
				hashmap.put("TaskAction", FlowTaskListxxArr.get(i)
						.getNodeName());
				hashmap.put("Assistant", FlowTaskListxxArr.get(i)
						.getAssistant());
				hashmap.put("AssistantTag", FlowTaskListxxArr.get(i)
						.getAssistantTag());
				data.add(hashmap);
			}
			break;
		case TASK_ADDCOMPANY:
			// ��ȡ������ҵ����
			data = new ArrayList<HashMap<String, Object>>();
//			ArrayList<LeTaskEntLinkListxx> letaskentlinklistxx = lawenforcementtask
//					.getLeTaskEntLinkList();
//			if (letaskentlinklistxx != null) {
//				for (int i = 0; i < letaskentlinklistxx.size(); i++) {
//					HashMap<String, Object> glqyArr = new HashMap<String, Object>();
//					glqyArr.put("qymc", letaskentlinklistxx.get(i).getEntName());
//					glqyArr.put("isexcute", letaskentlinklistxx.get(i)
//							.getStatus());
//					glqyArr.put("qyguid", letaskentlinklistxx.get(i)
//							.getEntCode());
//					data.add(glqyArr);
//				}
//			}
//			// byk
//			if (RWBH == null) {
//				RWBH = rwxx.getRWBH(GUID);
//			}
//
//			String sql1 = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
//					+ " where TaskEnpriLink.TaskID='"
//					+ RWBH
//					+ "' order by TaskEnpriLink.UpdateTime";
//			ArrayList<HashMap<String, Object>> data1 = SqliteUtil.getInstance()
//					.queryBySqlReturnArrayListHashMap(sql1);
//		//	data.clear();
			
			
			String taskId_glqy = lawenforcementtask.getTaskId();
			ArrayList<HashMap<String, Object>> params_glqy = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param_glqy = new HashMap<String, Object>();
			HashMap<String, Object> canshu_glqy = new HashMap<String, Object>();
			param_glqy.put("taskId", taskId_glqy);
			param_glqy.put("pageSize", 100);
			param_glqy.put("pageIndex", 1);
			param_glqy.put("SurveyUnit", Global.getGlobalInstance().getAreaCode());
			String toJson_glqy = hashMapToJson(param_glqy);
			canshu_glqy.put("parms", toJson_glqy);
			params_glqy.add(canshu_glqy);
			try {
				qylistjson = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, "GetSelectedExecuteEntlist", params_glqy, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			if (qylistjson!=null&&!"".equals(qylistjson)) {
				

				QyList paseJSON = JsonHelper.paseJSON(qylistjson,QyList.class);
				List<LeTaskEntLinkListxx2> taskEntList = paseJSON.TaskEntList;
				if (taskEntList != null) {
					for (int i = 0; i < taskEntList.size(); i++) {

						HashMap<String, Object> glqyArr = new HashMap<String, Object>();
						glqyArr.put("qymc", taskEntList.get(i)
								.getEntName());
						glqyArr.put("isexcute", taskEntList.get(i)
								.getStatus());
						glqyArr.put("qyguid", taskEntList.get(i)
								.getEntCode());
						data.add(glqyArr);
					
					}
					
					
				}
			
			}
				break;
			
		case TASK_ADDCOMPANY1:
			// ��ȡ������ҵ����
			data = new ArrayList<HashMap<String, Object>>();
			
			String taskId = lawenforcementtask.getTaskId();
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			HashMap<String, Object> canshu = new HashMap<String, Object>();
			param.put("taskId", taskId);
			param.put("pageSize", 100);
			param.put("pageIndex", 1);
			param.put("SurveyUnit", Global.getGlobalInstance().getAreaCode());
			String toJson = hashMapToJson(param);
			canshu.put("parms", toJson);
			params.add(canshu);
			try {
				qylistjson = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, "GetSelectedExecuteEntlist", params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			if (qylistjson!=null&&!"".equals(qylistjson)) {
				QyList paseJSON = JsonHelper.paseJSON(qylistjson,QyList.class);
				List<LeTaskEntLinkListxx2> taskEntList_rwzx = paseJSON.TaskEntList;
				if (taskEntList_rwzx != null) {
					for (int i = 0; i < taskEntList_rwzx.size(); i++) {

						HashMap<String, Object> glqyArr = new HashMap<String, Object>();
						glqyArr.put("qymc", taskEntList_rwzx.get(i)
								.getEntName());
						glqyArr.put("isexcute", taskEntList_rwzx.get(i)
								.getStatus());
						glqyArr.put("qyguid", taskEntList_rwzx.get(i)
								.getEntCode());
						ContentValues contentValues=new ContentValues();
						
						contentValues.put("TaskId", RWBH);
						contentValues.put("IsExcute", taskEntList_rwzx.get(i)
								.getStatus());
					
						contentValues.put("Guid", taskEntList_rwzx.get(i)
								.getEntCode());
						contentValues.put("QYID", taskEntList_rwzx.get(i)
								.getEntCode());
						
						String updateTime = DisplayUitl.getServerTime();
						contentValues.put("UpdateTime", updateTime);
						
						HashMap<String, Object> map=new HashMap<String, Object>();
						
						map.put("guid", taskEntList_rwzx.get(i).getEntCode());
						ArrayList<HashMap<String,Object>> list2 = SqliteUtil.getInstance().getList("TaskEnpriLink", map);
						if (list2.size()>0) {
							try {
							 SqliteUtil.getInstance().update("TaskEnpriLink", contentValues, "guid=?", new String[]{taskEntList_rwzx.get(i).getEntCode()});
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							
							long insert = SqliteUtil.getInstance().insert(contentValues,
									"TaskEnpriLink");
						}
						
						
						data.add(glqyArr);
					
					}
					
					
				}
			}
//			ArrayList<String> tables = new ArrayList<String>();
//			tables.add("ExeLawsTemplet");// TaskEnpriLink����״̬���� �ı䣬��Ҫͬ��һ��
//			tables.add("T_Attachment");// T_YDZF_RWXX����״̬���� �ı䣬��Ҫͬ��һ��
//			// ͬ��һ�������ű�
//			DataSyncModel dm2 = new DataSyncModel();
//
//			dm2.syncServiceData(tables, true);
			
//			// ��ȡ�ӿ��е�����
//			ArrayList<LeTaskEntLinkListxx> letaskentlinklistxx1 = lawenforcementtask
//					.getLeTaskEntLinkList();
//
//			if (letaskentlinklistxx1 != null) {
//				for (int i = 0; i < letaskentlinklistxx1.size(); i++) {
//					HashMap<String, Object> glqyArr = new HashMap<String, Object>();
//					glqyArr.put("qymc", letaskentlinklistxx1.get(i)
//							.getEntName());
//					glqyArr.put("isexcute", letaskentlinklistxx1.get(i)
//							.getStatus());
//					glqyArr.put("qyguid", letaskentlinklistxx1.get(i)
//							.getEntCode());
//					ContentValues contentValues=new ContentValues();
//					
//					contentValues.put("TaskId", RWBH);
//					contentValues.put("IsExcute", letaskentlinklistxx1.get(i)
//							.getStatus());
//				
//					contentValues.put("Guid", letaskentlinklistxx1.get(i)
//							.getEntCode());
//					contentValues.put("QYID", letaskentlinklistxx1.get(i)
//							.getEntCode());
//					
//					String updateTime = DisplayUitl.getServerTime();
//					contentValues.put("UpdateTime", updateTime);
//					
//					HashMap<String, Object> map=new HashMap<String, Object>();
//					
//					map.put("guid", letaskentlinklistxx1.get(i).getEntCode());
//					ArrayList<HashMap<String,Object>> list2 = SqliteUtil.getInstance().getList("TaskEnpriLink", map);
//					if (list2.size()>0) {
//						try {
//							SqliteUtil.getInstance().update("TaskEnpriLink", contentValues, "guid=?", new String[]{letaskentlinklistxx1.get(i).getEntCode()});
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}else{
//						
//						SqliteUtil.getInstance().insert(contentValues,
//								"TaskEnpriLink");
//					}
//					
//					
//					data.add(glqyArr);
//				}
//			}
			LogUtil.i(TAG, "��ȡ������ҵ���ݣ�----��" + data.toString());
			// ȥ�������ݿ��е�����
			String sql = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
					+ " where TaskEnpriLink.TaskID='"
					+ RWBH
					+ "' order by TaskEnpriLink.UpdateTime";
			ArrayList<HashMap<String, Object>> data_ = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql);
			//data.clear();
				data.addAll(data_);
			
			if (data != null && companyAdapter != null) {
				companyAdapter.updateData(data_);
			}

			break;
		default:
			break;
		}
		return data;

	}

	public String transitToChinese(int nodeid) {
		position = null;
		switch (nodeid) {
		case 0:
			position = "�칫��";
			break;
		case 1:
			position = "�ֳ�";
			break;
		case 2:
			position = "�ֹ��쵼";
			break;
		case 3:
			position = "�Ƴ�";
			break;
		default:
			position = "ִ����";
			break;
		}
		return position;
	}

	ArrayList<HashMap<String, Object>> CompanyAdapterData = new ArrayList<HashMap<String, Object>>();

	/**
	 * ִ����ҵ�б�
	 * 
	 * @author wangliugeng
	 * 
	 */
	private class CompanyAdapter extends BaseAdapter {

		public CompanyAdapter(

		ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
			// //BYK ���������ҵ��ʾ���������
			CompanyAdapterData.clear();

			for (int i = 0; i < CompanyAdapterData1.size(); i++) {
				for (int j = 0; j < CompanyAdapterData1.size(); j++) {
					if (i != j) {
						if (CompanyAdapterData1.get(i).get("qymc")
								.equals(CompanyAdapterData1.get(j).get("qymc"))) {
							CompanyAdapterData1.remove(j);
							j--;
						}
					}
				}
			}

			CompanyAdapterData = CompanyAdapterData1;
		}

		@Override
		public int getCount() {
			return CompanyAdapterData.size();
		}

		@Override
		public Object getItem(int position) {
			return CompanyAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
			// //BYK ���������ҵ��ʵ���������
			CompanyAdapterData.clear();
			// ȥ��

			for (int i = 0; i < CompanyAdapterData1.size(); i++) {
				for (int j = 0; j < CompanyAdapterData1.size(); j++) {
					if (i != j) {
						if (CompanyAdapterData1.get(i).get("qymc")
								.equals(CompanyAdapterData1.get(j).get("qymc"))) {
							CompanyAdapterData1.remove(j);
							j--;
						}
					}
				}
			}

			CompanyAdapterData = CompanyAdapterData1;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(TaskInformationActivity.this,
						R.layout.qyname_list, null);
			}
			final String qyGuid = CompanyAdapterData.get(position)
					.get("qyguid").toString();
			ImageView qy_icon = (ImageView) convertView.findViewById(R.id.img);
			qy_icon.setImageResource(R.drawable.xczf_gcqy);
			TextView qymc_text = (TextView) convertView
					.findViewById(R.id.qymc_text);
			qymc_text.setText(CompanyAdapterData.get(position).get("qymc")
					.toString());
			qymc_text.setTag(CompanyAdapterData.get(position).get("qyguid")
					.toString());
			final Button zt_btn = (Button) convertView.findViewById(R.id.rwzt);
			// if(TaskFlowDirection.equals("1")){
			// zt_btn.setVisibility(View.GONE);
			// }
			String qyzt = CompanyAdapterData.get(position).get("isexcute")
					.toString();
			zt_btn.setTag(qyzt);

			// 1�Ǵ�ִ�С�2ִ���С�3�����
			if (qyzt.equals("1")) {// 0���Ǳ��ص�
				zt_btn.setText("��ִ��");
				zt_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String isUpload = "0";
						if ("���ϴ�" == zt_btn.getText() || "ִ����" == zt_btn.getText()) {
							isUpload = "1";
						}
						String qyguid_byk = CompanyAdapterData.get(position)
								.get("qyguid").toString();
						Intent intent = new Intent(TaskInformationActivity.this,
								QdjcnlActivity.class);

						intent.putExtra("qyid", qyguid_byk);
						intent.putExtra("rwbh", RWBH);
						intent.putExtra("IsUpload", isUpload);
						// intent.putExtra("IsTask", "1");
						rwxx.setCurrentID(GUID);
						Bundle bundle = new Bundle();
						bundle.putSerializable("BusinessObj", rwxx);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});
			} else if (qyzt.equals("3")) {
				zt_btn.setText("����ִ��");
				zt_btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						zt_btn.setText("ִ����");
						String sql = "update TaskEnprilink set isexcute ='2' where TaskID='"
								+ RWBH + "' and QYID='"+CompanyAdapterData.get(position).get("qyguid").toString()+"'";

						SqliteUtil.getInstance().execute(sql);
						
					
					//	companyAdapter.notifyDataSetChanged();
						
						String sql2 = "update T_YDZF_RWXX set RWZT='005' where RWBH='"
								+ RWBH + "'";
						SqliteUtil.getInstance().execute(sql2);
						String isUpload = "0";
						if ("���ϴ�" == zt_btn.getText() || "ִ����" == zt_btn.getText()) {
							isUpload = "1";
						}
						String qyguid_byk = CompanyAdapterData.get(position)
								.get("qyguid").toString();
						Intent intent = new Intent(TaskInformationActivity.this,
								QdjcnlActivity.class);

						intent.putExtra("qyid", qyguid_byk);
						intent.putExtra("rwbh", RWBH);
						intent.putExtra("IsUpload", isUpload);
						// intent.putExtra("IsTask", "1");
						rwxx.setCurrentID(GUID);
						Bundle bundle = new Bundle();
						bundle.putSerializable("BusinessObj", rwxx);
						intent.putExtras(bundle);
						startActivity(intent);
						
					}
				});
			} else if (qyzt.equals("2")) {
				zt_btn.setText("ִ����");
				zt_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String isUpload = "0";
						if ("���ϴ�" == zt_btn.getText() || "ִ����" == zt_btn.getText()) {
							isUpload = "1";
						}
						String qyguid_byk = CompanyAdapterData.get(position)
								.get("qyguid").toString();
						Intent intent = new Intent(TaskInformationActivity.this,
								QdjcnlActivity.class);

						intent.putExtra("qyid", qyguid_byk);
						intent.putExtra("rwbh", RWBH);
						intent.putExtra("IsUpload", isUpload);
						// intent.putExtra("IsTask", "1");
						rwxx.setCurrentID(GUID);
						Bundle bundle = new Bundle();
						bundle.putSerializable("BusinessObj", rwxx);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});
			} else {
				zt_btn.setText("״̬����");
				zt_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String isUpload = "0";
						if ("���ϴ�" == zt_btn.getText() || "ִ����" == zt_btn.getText()) {
							isUpload = "1";
						}
						String qyguid_byk = CompanyAdapterData.get(position)
								.get("qyguid").toString();
						Intent intent = new Intent(TaskInformationActivity.this,
								QdjcnlActivity.class);

						intent.putExtra("qyid", qyguid_byk);
						intent.putExtra("rwbh", RWBH);
						intent.putExtra("IsUpload", isUpload);
						// intent.putExtra("IsTask", "1");
						rwxx.setCurrentID(GUID);
						Bundle bundle = new Bundle();
						bundle.putSerializable("BusinessObj", rwxx);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});
			}
			
			if (daiXieBan!=null&&"��Э��".equals(daiXieBan)) {
				zt_btn.setVisibility(View.GONE);
			}
			// if (qyzt.equals("0")) {
			// zt_btn.setText("��ִ��");
			// } else if (qyzt.equals("1") || qyzt.equals("3") ||
			// qyzt.equals("4") || qyzt.equals("5")) {
			// zt_btn.setText("���ϴ�");
			// } else if (qyzt.equals("2")) {
			// zt_btn.setText("ִ����");
			// } else {
			// zt_btn.setText("״̬����");
			// }
		
			return convertView;
		}

	}
	
	
	class qyListAdapter2   extends BaseAdapter{
		
		


		public qyListAdapter2(

		ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
			// //BYK ���������ҵ��ʾ���������
			CompanyAdapterData.clear();

			for (int i = 0; i < CompanyAdapterData1.size(); i++) {
				for (int j = 0; j < CompanyAdapterData1.size(); j++) {
					if (i != j) {
						if (CompanyAdapterData1.get(i).get("qymc")
								.equals(CompanyAdapterData1.get(j).get("qymc"))) {
							CompanyAdapterData1.remove(j);
							j--;
						}
					}
				}
			}

			CompanyAdapterData = CompanyAdapterData1;
		}

		@Override
		public int getCount() {
			return CompanyAdapterData.size();
		}

		@Override
		public Object getItem(int position) {
			return CompanyAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
			// //BYK ���������ҵ��ʵ���������
			CompanyAdapterData.clear();
			// ȥ��

			for (int i = 0; i < CompanyAdapterData1.size(); i++) {
				for (int j = 0; j < CompanyAdapterData1.size(); j++) {
					if (i != j) {
						if (CompanyAdapterData1.get(i).get("qymc")
								.equals(CompanyAdapterData1.get(j).get("qymc"))) {
							CompanyAdapterData1.remove(j);
							j--;
						}
					}
				}
			}

			CompanyAdapterData = CompanyAdapterData1;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(TaskInformationActivity.this,
						R.layout.qyname_list, null);
			}
			final String qyGuid = CompanyAdapterData.get(position)
					.get("qyguid").toString();
			ImageView qy_icon = (ImageView) convertView.findViewById(R.id.img);
			qy_icon.setImageResource(R.drawable.xczf_gcqy);
			TextView qymc_text = (TextView) convertView
					.findViewById(R.id.qymc_text);
			qymc_text.setText(CompanyAdapterData.get(position).get("qymc")
					.toString());
			qymc_text.setTag(CompanyAdapterData.get(position).get("qyguid")
					.toString());
			final Button zt_btn = (Button) convertView.findViewById(R.id.rwzt);
			// if(TaskFlowDirection.equals("1")){
			// zt_btn.setVisibility(View.GONE);
			// }
			String qyzt = CompanyAdapterData.get(position).get("isexcute")
					.toString();
			zt_btn.setTag(qyzt);

			// 1�Ǵ�ִ�С�2ִ���С�3�����
			if (qyzt.equals("1")) {// 0���Ǳ��ص�
				zt_btn.setText("��ִ��");
			} else if (qyzt.equals("3")) {
				zt_btn.setText("�����");
			} else if (qyzt.equals("2")) {
				zt_btn.setText("ִ����");
			} else {
				zt_btn.setText("״̬����");
			
			}
			zt_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String isUpload = "0";
					if ("���ϴ�" == zt_btn.getText() || "ִ����" == zt_btn.getText()) {
						isUpload = "1";
					}
					String qyguid_byk = CompanyAdapterData.get(position)
							.get("qyguid").toString();
					Intent intent = new Intent(TaskInformationActivity.this,
							QdjcnlActivity.class);

					intent.putExtra("qyid", qyguid_byk);
					intent.putExtra("rwbh", RWBH);
					intent.putExtra("IsUpload", isUpload);
					// intent.putExtra("IsTask", "1");
					rwxx.setCurrentID(GUID);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});
			if (daiXieBan!=null&&"��Э��".equals(daiXieBan)) {
				zt_btn.setVisibility(View.GONE);
			}
			// if (qyzt.equals("0")) {
			// zt_btn.setText("��ִ��");
			// } else if (qyzt.equals("1") || qyzt.equals("3") ||
			// qyzt.equals("4") || qyzt.equals("5")) {
			// zt_btn.setText("���ϴ�");
			// } else if (qyzt.equals("2")) {
			// zt_btn.setText("ִ����");
			// } else {
			// zt_btn.setText("״̬����");
			// }
		
			return convertView;
		}

	
	}
	

	/**
	 * �����ȾԴ��ť����¼�
	 */
	private class selectCompanyListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(TaskInformationActivity.this,
					SelectAddCompanyActivity.class);
			// intent.putExtra("rwbh", transactedFlowInstanceId);
			intent.putExtra("rwbh", GUID);
			intent.putExtra("rwbh2", RWBH);
			intent.putExtra("taskId", lawenforcementtask.getTaskId());
			// ѡ����ҵ
			startActivityForResult(intent, 100);
		}
	}
	private Intent loctionnewIntent;
	/**
	 * ִ����ɰ�ť����¼�
	 */
	private class TaskbjListener implements View.OnClickListener {
	

		@Override
		public void onClick(View v) {
			// ��ʱע�� �ֳ�ִ��û�е���--------------shi---------------------
			if (!searchTaskEnpriState(RWBH, qylistdata)) {
				Toast.makeText(TaskInformationActivity.this, "��ȫ��ִ�������ύ ��", 0)
						.show();
				return;
			}
			// else {
			// �ϴ�����
			// ���޸� rwguid
			fileUploadTag = 1;
			// ArrayList<TaskFile> taskFile =
			// getAllUploadFile(T_Attachment.RWFK,
			// RWBH + "_" + UserID);

			String fkyj = info_taskbj.getText().toString();
			if (TextUtils.isEmpty(fkyj)) {
				Toast.makeText(TaskInformationActivity.this, "�����������Ϊ�� ��", 0)
						.show();
				return;
			}
			//�ϴ�λ��
			DisplayUitl.saveAppInfoDataToPreference(TaskInformationActivity.this,
					"userid", Global.getGlobalInstance().getUserid());
			loctionnewIntent = new Intent(TaskInformationActivity.this,
			RydwServices.class);
			TaskInformationActivity.this.startService(loctionnewIntent);

			// �޸�fkid ����ִ���ϴ�����
			ArrayList<TaskFile> taskFile = getAllUploadFile(T_Attachment.RWFK,
					RWBH);
			if (taskFile != null && taskFile.size() > 0) {
				upLoadFilesMethod2(taskFile, TaskInformationActivity.this, GUID);

			} else {
				handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
			}
			// }
		}
	}

	/**
	 * ���������Ų�ѯ����������ҵ��״̬��ÿ����ҵ״̬ȫ��Ϊ���ϴ���ֵΪ1���ſ��԰������
	 */
	private Boolean searchTaskEnpriState(String rwbh,
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> data_IsExcute = null;
		Task_Enpri_ZT = true;
		// String sql_IsExcute =
		// "select TaskID,IsExcute from TaskEnpriLink where TaskID='"
		// + RWBH + "' and (IsExcute='0' or IsExcute='2')";
		String sql_IsExcute = "select TaskID,IsExcute from TaskEnpriLink where TaskID='"
				+ RWBH + "' and (IsExcute='1' or IsExcute='2')";
		data_IsExcute = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql_IsExcute);
		if (data_IsExcute != null && data_IsExcute.size() > 0) {
			Task_Enpri_ZT = false;
		}
		return Task_Enpri_ZT;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data1) {
		if (data1 != null && requestCode == 0 && 20 == resultCode) {

			task_information_sp.setText(String.valueOf(data1.getExtras()
					.getString("name")));
			task_information_sp.setTag(String.valueOf(data1.getExtras()
					.getString("code")));
			task_information_ed_fshr.setText("");
			task_information_ed_fshr.setTag("");
		//	task_information_ed_fshr.setHint("��ѡ��Э����");
			return;
		}
		// ������Բ�ѡЭ���˵�����
		if (data1 != null && requestCode == 1 && 20 == resultCode) {

			if (data1.getExtras().getString("name") == null
					|| data1.getExtras().getString("name").toString()
							.equals("")) {
				task_information_ed_fshr.setHint("��ѡ��Э����");
				task_information_ed_fshr.setTag("");
			} else {
				task_information_ed_fshr.setText(String.valueOf(data1
						.getExtras().getString("name")));
				task_information_ed_fshr.setTag(String.valueOf(data1
						.getExtras().getString("code")));
			}

			return;
		}

		if (data1 != null && requestCode == 999 && 20 == resultCode) {
			task_information_sp.setText(String.valueOf(data1.getExtras()
					.getString("name")));
			task_information_sp.setTag(String.valueOf(data1.getExtras()
					.getString("code")));
		}
		if (requestCode == 520 && resultCode == -1) {
			String qyid = data1.getStringExtra("qyid");
			Intent intent = new Intent(TaskInformationActivity.this,
					QdjcnlActivity.class);
			intent.putExtra("qyid", qyid);
			intent.putExtra("rwbh", RWBH);
			intent.putExtra("IsUpload", "1");
			intent.putExtra("taskInfoFlag", "taskInfoFlag");
			// intent.putExtra("IsTask", "1");
			rwxx.setCurrentID(GUID);
			Bundle bundler = new Bundle();
			bundler.putSerializable("BusinessObj", rwxx);
			intent.putExtras(bundler);
			startActivity(intent);
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}

			return;
		}
		//if (resultCode == 100) {
			if (companyAdapter != null) {
				String sql = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
						+ " where TaskEnpriLink.TaskID='"
						+ RWBH
						+ "' order by TaskEnpriLink.UpdateTime";
				ArrayList<HashMap<String, Object>> data = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(sql);

				companyAdapter.updateData(data);
				

		//	}
			// if (associatedEnterprises()) {
			// System.out.println("�ɹ�");
			// } else {
			// System.out.println("ʧ��");
			//
			//
			// }
			//
		//	btn_taskbj.setVisibility(View.VISIBLE);
			//return;
		}
		if (RWBH == null || RWBH.equals("")) {
			RWBH = rwxx.returnRWBH();
		}
		if (requestCode == SiteEvidenceActivity.TAKE_PHOTOS) {

			if (resultCode == -1) {
				Date now = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
				String fileName = Global.getGlobalInstance().getRealName()
						+ "_" + dateFormat.format(now);

				String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) "
						+ "values ('"
						+ imageGuid
						+ "','"
						+ fileName
						+ "','"
						+ imageGuid
						+ ".jpg','.jpg','"
						+ T_Attachment.RWFK
						+ "','" + RWBH + "')";
				SqliteUtil.getInstance().execute(sql);
			}

		}
		if (data1 != null && requestCode == SELECT_SDKARD_FILE) {
			AttachmentBaseActivity.selectSDcardFile(data1, this,
					T_Attachment.RWFK, RWBH);
		}
		attachAdapterData = getAttachAdapterData(T_Attachment.RWFK + "", RWBH);
		attachAdapter.updateData(attachAdapterData);

		if (yutuLoading != null) {
			yutuLoading.dismissDialog();

		}
	}

	private String del_qyguid;

	/**
	 * Description:��ʾ�����Ի���
	 * 
	 * @param name
	 * @param pos
	 * @author Administrator Create at: 2012-12-4 ����10:49:27
	 */
	protected void showDelDialog(final int position, final View view) {
		AlertDialog.Builder deleteab = new AlertDialog.Builder(
				TaskInformationActivity.this);
		deleteab.setTitle("ɾ��");
		deleteab.setMessage("��ȷ��Ҫɾ����");
		deleteab.setIcon(R.drawable.icon_delete);
		deleteab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				TextView qymc_text = (TextView) view
						.findViewById(R.id.qymc_text);
				del_qyguid = qymc_text.getTag().toString();
				Button zt_btn = (Button) view.findViewById(R.id.rwzt);

				ArrayList<String> keyValue = new ArrayList<String>();
				// byk rwzt
				if (zt_btn.getTag().toString().equals("3")) {
					// if (zt_btn.getTag().toString().equals("1")) {
					Toast.makeText(TaskInformationActivity.this,
							"����ɾ�����������ϴ�����ҵ", Toast.LENGTH_SHORT).show();
				} else {
					keyValue.add(del_qyguid);
					// delEntHandler.sendEmptyMessage(DELSUCCESS);
					// TODO:�Ӻ�̨ɾ��������ҵ

					new Thread() {
						public void run() {
							List<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
							String token = "";
							HashMap<String, Object> param = new HashMap<String, Object>();
							try {
							} catch (Exception e1) {

								e1.printStackTrace();
							}

							long tempp1 = Long.valueOf(lawenforcementtask
									.getTaskId());
							param.put("taskId", tempp1);
							param.put("enterCode", "[\"" + del_qyguid + "\"]");

							Log.i("info", "token:" + token + ",taskId:" + RWBH
									+ ",entGuid:" + del_qyguid);
							params.add(param);
							Log.i("info", "���÷���");
							try {
								String result = (String) WebServiceProvider
										.callWebService(
												Global.NAMESPACE,
												"DeleteTaskEntLink",
												params,
												Global.getGlobalInstance()
														.getSystemurl()
														+ Global.WEBSERVICE_URL,
												WebServiceProvider.RETURN_STRING,
												true);

								Log.i("info", "�����" + result);
								if (result != null && result.contains("true")) {
									delEntHandler.sendEmptyMessage(DELSUCCESS);
								} else {
									delEntHandler.sendEmptyMessage(DELFAIL);
								}
							} catch (IOException e) {
								e.printStackTrace();
								delEntHandler.sendEmptyMessage(EXCEPTION);
							}
						};

					}.start();

				}
			}

		});
		deleteab.setNegativeButton("ȡ��", null);
		AlertDialog ad = deleteab.create();
		ad.show();
	}

	private void deleteCompanyEnforcedInfomation(String del_qyguid) {
		// ɾ�����������ҵ���м�¼
		String sql0 = "delete from TaskEnpriLink where TaskID='" + RWBH
				+ "' and QYID='" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql0);
		// ɾ����ҵ��ص��嵥���¼
		String sql1 = "delete from YDZF_TaskSpecialItem where TaskID='" + RWBH
				+ "' and EnterID='" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql1);
		// ɾ����ҵ��ؿ����¼���¼
		String sql2 = "delete from T_ZFWS_KCBL where TaskId='" + RWBH
				+ "' and EntCode='" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql2);
		// ɾ ����ҵ��ص�ѯ�ʱ�¼���¼
		String sql3 = "delete from T_ZFWS_XWBL where TaskId='" + RWBH
				+ "' and SurveyEntCode='" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql3);
		// ɾ����ҵ��صĸ�����Ϣ���¼
		String sql4 = "delete from T_Attachment where FK_Unit='2' and FK_Id='"
				+ RWBH + "_" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql4);
		// ɾ����ҵ��ص�һ��һ����¼
		String sql5 = "delete from T_ZFWS_XWJLWD where TaskID='" + RWBH
				+ "' and EntID='" + del_qyguid + "'";
		SqliteUtil.getInstance().execute(sql5);
	}

	ArrayList<HashMap<String, Object>> zshrData = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> fshrData = new ArrayList<HashMap<String, Object>>();

	/**
	 * ѡ������˻�ִ���˼����¼�
	 * 
	 * @author wangliugeng
	 * 
	 */
	public class SelectAuditorListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (DisplayUitl.isFastDoubleClick()) {
				return;
			}
			switch (v.getId()) {
			case R.id.task_information_ed_zshr:
				// ��ֹ��ε��
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				// �ȴ�����
				yutuLoading = new YutuLoading(TaskInformationActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.showDialog();

				FlowTaskId = flowtransaction.getCurrentFlowTaskId();
				// TODO
				getLeaderData(OperationCode, FlowTaskId);
				// ��
				// getSpecifiedData2();

				// showDialog("��ѡ�������", v, 0);
				break;
			case R.id.task_information_ed_fshr:
				// ��ֹ��ε��
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				} else if (TextUtils.isEmpty(task_information_sp.getText()
						.toString())) {

					Toast.makeText(TaskInformationActivity.this, "����ѡ����������!", 0)
							.show();

					return;

				}
				yutuLoading = new YutuLoading(TaskInformationActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.showDialog();
				// ��ȡ����
				getSpecifiedData();
				
			//	noticeToUser_fblr();

				// showDialog("��ѡ��Э����", v, 1);
				break;
			// case R.id.task_information_ed_zxpeople:
			// showDialog("��ѡ��ִ����", v, 2);
			// break;
			// case R.id.task_information_ed_zhpeople:
			// showDialog("��ѡ��Э����", v, 3);
			// break;
			case R.id.selectArchivePersonEdit:
				showDialog("��ѡ��鵵��", v, 4);
				break;
			default:
				break;
			}
		}
	}
	
	
	/**
	 * ��ѡЭ����
	 * 
	 * @param view
	 */
	

	
	private StringBuffer userName;
	public void noticeToUser_fblr() {
		Builder builder = new Builder(TaskInformationActivity.this);
	//	builder.setTitle("��ѡ��Э����");
		TextView tv = new TextView(TaskInformationActivity.this);  
        tv.setText("��ѡ��Э����");    //����  
        tv.setTextSize(22);//�����С  
        tv.setPadding(50, 30, 20, 20);//λ��  
        tv.setTextColor(Color.WHITE);//��ɫ  
        tv.setBackgroundColor(Color.parseColor("#404E8B"));
        builder.setCustomTitle(tv);//����setTitle()  
	//	builder.setIcon(TaskInformationActivity.this.getResources().getDrawable(R.drawable.yutu));
		ExpandableListView listView = new ExpandableListView(TaskInformationActivity.this);
		builder.setView(listView);

		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("1",
				"2' or zw='1' or zw='0' or zw='2' or zw='3' or zw='4");

		ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
		List<String> groupList = new ArrayList<String>();
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		final LinkedList<String> linkedList = new LinkedList<String>();
		final LinkedList<String> linkedName = new LinkedList<String>();
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();

		// FIXME ��ס�ָ�ԭ��
		if (UserdepId.equals("210000000departB")) {
			login_user_data = SqliteUtil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from PC_DepartmentInfo where depid ='210000000departA'");
		} else {
			login_user_data = SqliteUtil
					.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from PC_DepartmentInfo where depid =(select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
									+ Global.getGlobalInstance().getUserid() + "')");
		}
		String depID = login_user_data.get(0).get("depid").toString();

		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("DepParentID", depID);
		conditions.put("syncDataRegionCode", UserAreaCode);

		SqliteUtil.getInstance().recursiveQueryOrder("PC_DepartmentInfo",
				depData, conditions, "depid,depname", false, "depid");
		//TODO ��userid ������ѡ���˵�Userid
		
		String sp_name = task_information_sp.getText().toString().trim();
		
		String sql_name="select * from PC_Users where U_RealName='"+sp_name+"' and RegionCode = '"+Global.getGlobalInstance().getAreaCode()+"'";
		ArrayList<HashMap<String, Object>> sql_names = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql_name);
		
		String sp_id = sql_names.get(0).get("userid").toString();
		
		String sql="select * from PC_Users where userid='"+sp_id+"'";
		ArrayList<HashMap<String, Object>> pc_users = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		String depparentid = pc_users.get(0).get("depid").toString();
		String sql2="select * from PC_DepartmentInfo where DepId='"+depparentid+"'";
		ArrayList<HashMap<String, Object>> PC_DepartmentInfos = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql2);
		String PC_DepartmentInfo = PC_DepartmentInfos.get(0).get("depname").toString();
		
		for (HashMap<String, Object> map : depData) {
			
			String depName = map.get("depname").toString();
			String depid2 = map.get("depid").toString();
			if ("���쵼".equals(PC_DepartmentInfo)) {
				
				if (!"��˲���".equals(depName)) {
					groupList.add(depName);

					condition.put("DepID", map.get("depid").toString());
					condition.put("syncDataRegionCode", UserAreaCode);
					ArrayList<HashMap<String, Object>> usersData = SqliteUtil
							.getInstance().getOrderList("PC_Users",
									"UserID,U_RealName", condition, "zw");
					childMapData.add(usersData);
				}
			}else {
				if (!"��˲���".equals(depName)&&depid2.equals(depparentid)) {
					groupList.add(depName);

					condition.put("DepID", map.get("depid").toString());
					condition.put("syncDataRegionCode", UserAreaCode);
					ArrayList<HashMap<String, Object>> usersData = SqliteUtil
							.getInstance().getOrderList("PC_Users",
									"UserID,U_RealName", condition, "zw");
					childMapData.add(usersData);
				}
			}
		}
		
		listView.setAdapter(new TaskManagerModel().getselectAuditorAdapter(
				groupList, childMapData, linkedList, linkedName, TaskInformationActivity.this));// �������ݣ���������
		int groupCount = listView.getCount();

		for (int k = 0; k <groupCount ; k++) {
			listView.expandGroup(k);
		}
		builder.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {

					

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
						userName = new StringBuffer();
						StringBuffer sbFshr = new StringBuffer();
						
						
						StringBuffer sbFshr2 = new StringBuffer();
						for (int i = 0; i < linkedList.size(); i++) {
							
							
							userName.append(linkedName.get(i) + ",");
//							for (int j = 0; j < fshrData.size(); j++) {
//								
//								HashMap<String, Object> hashMap2 = fshrData.get(j);
//								
//								if (hashMap2.get("Text").toString().trim().equals(linkedName.get(i))) {
//									sbFshr2.append(hashMap2.get("Id").toString()+",");
//								}
//							}
							
							
						
							
						for (int j = 0; j < fshrData.size(); j++) {
							HashMap<String, Object> hashMap = fshrData.get(j);
							Iterator iter = hashMap.entrySet().iterator();
							while (iter.hasNext()) {  
							    Map.Entry entry = (Map.Entry) iter.next();  
							    Object key = entry.getKey();  
							    Object val = entry.getValue();  
							  
							    if ("Text".equals( key.toString())&&linkedName.get(i).equals(val.toString())) {
							    	sbFshr.append(hashMap.get("Id") + ",");
								}
							}  
							
						}
							
						}
						/* ȥ��, */
						if (sbFshr.length() > 0) {
							sbFshr.deleteCharAt(sbFshr.length() - 1);
						}
						/* ȥ��, */
						if (sbFshr2.length() > 0) {
							sbFshr2.deleteCharAt(sbFshr2.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
					
						
						task_information_ed_fshr.setText(userName);
						
						
						
						task_information_ed_fshr.setTag(sbFshr);
					}

				});

		builder.setNeutralButton("ȫѡ",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
						StringBuffer userName = new StringBuffer();
						StringBuffer sbFshr = new StringBuffer();
						for (int i = 0; i < childMapData.size(); i++) {
							for (int j = 0; j < childMapData.get(i).size(); j++) {
								sbFshr.append(childMapData.get(i).get(j)
										.get("userid")
										+ ",");
								userName.append(childMapData.get(i).get(j)
										.get("u_realname")
										+ ",");
							}
						}

						if (sbFshr.length() > 0) {
							sbFshr.deleteCharAt(sbFshr.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
						task_information_ed_fshr.setText(userName);
						task_information_ed_fshr.setTag(sbFshr);
					}
				});
		builder.setNegativeButton("ȡ��", null);

		builder.create().show();
		if (yutuLoading != null) {
			yutuLoading.dismissDialog();
		}

	}

	/**
	 * չʾ����˻�ִ����
	 * 
	 * @param title
	 * @param v
	 * @param userTag
	 *            0 ������� 1������� 2ִ���� 3Э����
	 */
	public void showDialog(String title, final View textView, final int userTag) {

		View dialogView = layoutInflater.inflate(
				R.layout.enforcementmodel_select_commonpeople, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(
				TaskInformationActivity.this);
		ab.setTitle(title);
		ab.setIcon(getResources().getDrawable(R.drawable.yutu));
		ab.setView(dialogView);
		final AlertDialog ad = ab.create();
		final ExpandableListView expandableListView = (ExpandableListView) dialogView
				.findViewById(R.id.enforcementmodel_select_commonpeople_explistview);
		expandableListView.setGroupIndicator(TaskInformationActivity.this
				.getResources()
				.getDrawable(R.layout.expandablelistviewselector));

		TextView tv = (TextView) dialogView
				.findViewById(R.id.enforcementmodel_select_commonpeople_tv);
		/** �����û���¼�û���id��ѯ */
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();
		login_user_data = SqliteUtil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select * from PC_DepartmentInfo where depid =(select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo "
								+ "on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
								+ Global.getGlobalInstance().getUserid() + "')");
		/** ��õ�¼�û������ŵ����� */
		String depParentName = login_user_data.get(0).get("depname").toString();
		/** ��õ�¼�û������ŵ�id */
		String depID = login_user_data.get(0).get("depid").toString();
		tv.setText(depParentName);

		// wsc �ж��Ƿ������쵼
		if ("210000000depart".equals(depID)) {
			tv.setText("�����л���������");
			depID = "210300000departA";
		}
		ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
		// HashMap<String, Object> conditions = new HashMap<String, Object>();
		// conditions.put("DepParentID", depID);
		// conditions.put("syncDataRegionCode", UserAreaCode);
		//
		// SqliteUtil.getInstance().recursiveQueryOrder("PC_DepartmentInfo",
		// depData, conditions, "depid,depname", false, "depid");
		//
		// ��ѯ���û����Ӧ�����в���
		depData = SqliteUtil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select distinct a.DepID,b.DepName from PC_Users as a left join PC_DepartmentInfo as b on a.depid=b.DepID "
								+ "where a.syncDataRegionCode= '"
								+ UserAreaCode
								+ "' and a.depid is not null and a.depid!='' and a.depid!='null' and a.depid!='NULL' and a.depid!='Null'  order by a.depid");

		final List<String> groupList = new ArrayList<String>();
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		// condition.put("zw", "2' or zw='1' or zw='4' or zw='3' or zw='0");

		switch (userTag) {
		case 0:// ��������
			if (userZw.equals("1")) {
				if (UserdepId.equals("360000000departB")) {
					condition.put("1",
							"2' or zw='1' or zw='2' or zw='3' or zw='0");
				} else {// �쵼���ܸ��칫��������
					condition.put("1", "2' or zw='2' or zw='3");
				}
			} else if (userZw.equals("2")) {// �쵼���ܸ��칫��������
				condition.put("1", "2' or zw='3' or zw='1");
			} else {
				condition.put("1",
						"2' or zw='0' or zw='3' or zw='1' or zw='2' or zw='4");
			}

			break;
		case 1:// �������
			if (userZw.equals("1")) {
				if (UserdepId.equals("360000000departB")) {
					condition
							.put("1",
									"2' or zw='1' or zw='2' or zw='3' or zw='0' or zw='4");
				} else {
					condition.put("1",
							"2' or zw='2' or zw='3' or zw='0' or zw='4");
				}
			} else if (userZw.equals("2")) {
				condition.put("1", "2' or zw='0' or zw='3' or zw='4");

			}
			break;
		case 2:// ִ����
			condition.put("1", "2' or zw='0' or zw='3' or zw='4");
			String currentDeptId = Global.getGlobalInstance().getDepId();
			for (int i = 0; i < depData.size(); i++) {
				if (currentDeptId
						.equals(depData.get(i).get("depid").toString())) {
					HashMap<String, Object> hashmap = new HashMap<String, Object>();
					hashmap = depData.get(i);
					depData.clear();
					depData.add(hashmap);
				}
			}
			break;
		case 3:// Э����
			condition.put("1", "2' or zw='0' or zw='3' or zw='4");
			break;
		case 4:// �鵵��
			condition.put("1",
					"2' or zw='0' or zw='3' or zw='1' or zw='2' or zw='4");
			break;
		default:
			condition.put("1", "2' or zw='0' or zw='3' or zw='1' or zw='2");
			break;
		}

		for (HashMap<String, Object> map : depData) {
			String depName = map.get("depname").toString();
			groupList.add(depName);
			condition.put("DepID", map.get("depid").toString());
			condition.put("syncDataRegionCode", UserAreaCode);
			condition.put("3", "2' or u_loginName not like 'admin%");
			ArrayList<HashMap<String, Object>> usersData = SqliteUtil
					.getInstance().getList("UserID,U_RealName", condition,
							"PC_Users");
			childMapData.add(usersData);
		}

		/** �û�id���� */
		final LinkedList<String> linkedList = new LinkedList<String>();
		/** �û��������� */
		final LinkedList<String> linkedName = new LinkedList<String>();
		SelectAuditorAdapter selectAuditorAdapter = taskManagerModel
				.getselectAuditorAdapter(groupList, childMapData, linkedList,
						linkedName, TaskInformationActivity.this);
		SelectAuditorAdapter1 selectAuditorAdapter1 = taskManagerModel
				.getselectAuditorAdapter1(groupList, childMapData, linkedList,
						linkedName, TaskInformationActivity.this);

		if (userTag == 0 || userTag == 4) {// ѡ��������˻�鵵��
			expandableListView.setAdapter(selectAuditorAdapter1);
			expandableListView
					.setOnChildClickListener(new OnChildClickListener() {

						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {

							String userCheckedId = childMapData
									.get(groupPosition).get(childPosition)
									.get("userid").toString();
							sbZshr = new StringBuffer();
							sbZshr.append(userCheckedId);
							HashMap<String, Object> conditions = new HashMap<String, Object>();
							conditions.put("userid", sbZshr.toString());
							ArrayList<HashMap<String, Object>> data = SqliteUtil
									.getInstance().getList("u_realname",
											conditions, "PC_Users");

							((TextView) textView).setText(data.get(0)
									.get("u_realname").toString());
							ad.cancel();

							return false;
						}
					});
			ad.show();
		} else if (userTag == 2) {// ѡ��ִ����
			expandableListView.setAdapter(selectAuditorAdapter1);
			expandableListView
					.setOnChildClickListener(new OnChildClickListener() {

						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {

							String userCheckedId = childMapData
									.get(groupPosition).get(childPosition)
									.get("userid").toString();
							sbZxr = new StringBuffer();
							sbZxr.append(userCheckedId);
							HashMap<String, Object> conditions = new HashMap<String, Object>();
							conditions.put("userid", sbZxr.toString());
							ArrayList<HashMap<String, Object>> data = SqliteUtil
									.getInstance().getList("u_realname",
											conditions, "PC_Users");
							((TextView) textView).setText(data.get(0)
									.get("u_realname").toString());
							ad.cancel();

							return false;
						}
					});
			ad.show();
		} else {
			expandableListView.setAdapter(selectAuditorAdapter);
			expandableListView
					.setOnChildClickListener(new OnChildClickListener() {

						@Override
						public boolean onChildClick(ExpandableListView parent,
								View v, int groupPosition, int childPosition,
								long id) {
							CheckBox two_class_cb = (CheckBox) v
									.findViewById(R.id.two_class_cb);
							two_class_cb.toggle();
							return false;
						}
					});
			ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					StringBuffer userName = new StringBuffer();
					if (userTag == 1) {// ѡ�������
						sbFshr = new StringBuffer();
						for (int i = 0; i < linkedList.size(); i++) {
							sbFshr.append(linkedList.get(i) + ",");
							userName.append(linkedName.get(i) + ",");
						}

						if (sbFshr.length() > 0) {
							sbFshr.deleteCharAt(sbFshr.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
						((TextView) textView).setText(userName.toString());

					} else {// ѡ��֪����
						sbZhr = new StringBuffer();
						for (int i = 0; i < linkedList.size(); i++) {
							sbZhr.append(linkedList.get(i) + ",");
							userName.append(linkedName.get(i) + ",");
						}
						if (sbZhr.length() > 0) {
							sbZhr.deleteCharAt(sbZhr.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
						((TextView) textView).setText(userName.toString());
					}
				}

			});
			if (userTag == 3) {
				ab.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				ab.setNeutralButton("����",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (DisplayUitl.isFastDoubleClick()) {
									return;
								}
								((TextView) textView).setText("");

							}
						});
			}

			ab.create().show();
		}
	}

	/** �����û����Ƶõ��û�ְ�� **/
	private ArrayList<HashMap<String, Object>> getInfoByName(String ZJzxr) {
		ArrayList<HashMap<String, Object>> uData = null;

		if (!ZJzxr.equals("")) {
			String sql = "select userid,zw from  PC_Users where u_realname='"
					+ ZJzxr + "'";
			uData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					sql);
			return uData;
		} else {
			return uData;
		}
	}

	/**
	 * Description:��ʾ����鵵�ĵ�����
	 */
	protected void showgdDialog() {
		AlertDialog.Builder deleteab = new AlertDialog.Builder(
				TaskInformationActivity.this);
		deleteab.setTitle("����鵵");
		deleteab.setMessage("��ȷ��Ҫ��������鵵��");
		deleteab.setIcon(R.drawable.icon_delete);
		deleteab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				fileUploadTag = 3;
				// ���ù鵵ʱ �ĸ����ϴ���Ϣ byk
				// ArrayList<TaskFile> taskFile = getAllUploadFile(
				// T_Attachment.RWFK, RWBH + "_" + UserID);
				// �޸�fkid
				ArrayList<TaskFile> taskFile = getAllUploadFile(
						T_Attachment.RWFK, RWBH);
				// ���ù鵵��������
				// ���޸� rwguid byk
				if (taskFile != null && taskFile.size() > 0) {
					// UploadFile uploadFile = new UploadFile();
					// uploadFile.upLoadFilesMethod(taskFile, handler,
					// TaskInformationActivity.this);
					upLoadFilesMethod2(taskFile, TaskInformationActivity.this,
							GUID);
				} else {
					handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
				}
			}
		});
		deleteab.setNegativeButton("ȡ��", null);
		AlertDialog ad = deleteab.create();
		ad.show();
	}

	public void upDataAttathListView() {
		if (attachAdapter != null) {
			// ���� fk_id
			// attachAdapter.updateData(getAttachAdapterData(T_Attachment.RWFK
			// + "", RWBH + "_" + UserID));
			attachAdapter.updateData(getAttachAdapterData(T_Attachment.RWFK
					+ "", RWBH));

			attachAdapter.notifyDataSetChanged();
		}

	}

	// ����
	public void photograph(View view) {
		takePhoto();
	}

	private void takePhoto() {// ����

		imageGuid = UUID.randomUUID().toString();
		Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(TASK_PATH);
		if (!file.exists())// ��һ�����մ�����Ƭ�ļ���
			file.mkdirs();
		photo_intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(TASK_PATH + imageGuid + "." + "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		startActivityForResult(photo_intent, SiteEvidenceActivity.TAKE_PHOTOS);

	}

	// ѡ��
	public void takefigure(View view) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "��ѡ��һ��Ҫ�ϴ����ļ�"),
					SELECT_SDKARD_FILE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "�밲װ�ļ�������", Toast.LENGTH_SHORT).show();
		}
	}

	// ��װ�ļ�������
	public void InstallAPK(View view) {

		String p = Global.SDCARD_RASK_DATA_PATH + "data/RootExplorer.apk";
		File file = new File(p);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
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

	public ArrayList<TaskFile> getAllUploadFile(int fk_unit, String fk_id) {
		ArrayList<TaskFile> _ListFile = new ArrayList<TaskFile>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("FK_Unit", fk_unit + "");
		condition.put("fk_id", fk_id);
		ArrayList<HashMap<String, Object>> fileLists = SqliteUtil.getInstance()
				.getList(" * ", condition, "T_Attachment");
		if (fileLists != null || fileLists.size() > 0) {
			// String rwGuid = UUID.randomUUID().toString();
			String rwGuid = SpUtils.getString(TaskInformationActivity.this,
					"billid_byk");
			if (rwGuid == null || "".equals(rwGuid)) {
				rwGuid = UUID.randomUUID().toString();
			}
			String biztype = 10 + "";
			for (HashMap<String, Object> map : fileLists) {
				TaskFile taskFile = new TaskFile();
				String absolutePath = Global.SDCARD_RASK_DATA_PATH + "Attach/"
						+ T_Attachment.transitToChinese(fk_unit) + "/"
						+ map.get("filepath").toString();
				taskFile.setGuid(map.get("guid").toString());
				taskFile.setFileName(map.get("filename").toString());
				taskFile.setFilePath(map.get("filepath").toString());
				taskFile.setAbsolutePath(absolutePath);
				taskFile.setUnitId(map.get("fk_unit").toString());
				taskFile.setExtension(map.get("extension").toString());

				// �鵵ʱ����ϴ����� billid
				taskFile.setBillid(rwGuid);
				taskFile.setBiztype(biztype);

				_ListFile.add(taskFile);
			}
		}

		return _ListFile;
	}

	protected void showDialog(final String guid, final String fileName) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		String[] selections = { "������", "ɾ��" };
		dialog.setItems(selections, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					LinearLayout ly = new LinearLayout(
							TaskInformationActivity.this);
					ly.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));
					final EditText edtext = new EditText(
							TaskInformationActivity.this);
					edtext.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					TextView tv = new TextView(TaskInformationActivity.this);
					tv.setText("���ƣ�");
					ly.addView(tv);
					ly.addView(edtext);
					AlertDialog.Builder ab = new AlertDialog.Builder(
							TaskInformationActivity.this);
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

									upDataAttathListView();
									Toast.makeText(
											TaskInformationActivity.this,
											"�������ɹ���", Toast.LENGTH_LONG).show();
								}
							});
					ab.setNegativeButton("ȡ��", null);
					ab.show();
					break;
				case 1:
					AlertDialog.Builder deleteab = new AlertDialog.Builder(
							TaskInformationActivity.this);
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
									upDataAttathListView();
									Toast.makeText(
											TaskInformationActivity.this,
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

	/**
	 * ����Webservice��������ִ�����
	 * */
	public void callTaskExecuteFinish() {
		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setLoadMsg("�����ύ�����Ե�...", "");
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (requestTaskExecuteFinish()) {// ������ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ���ϴ���Ҫ����һ��
					// DataSyncModel dm = new DataSyncModel();
					// dm.synchronizeFetchServerData(true, "YDZF_RWLC");
					// dm.synchronizeFetchServerData(true, "T_YDZF_RWXX");
					handler.sendEmptyMessage(BANJIE_SUCCESS);
					// handler.sendEmptyMessage(BANJIE_FALI);
				} else {
					handler.sendEmptyMessage(BANJIE_FALI);
				}

			}
		}).start();
	}

	/**
	 * ����ִ������ύ
	 */
	/**
	 * ����Webservice����������� �ύ�ӿ�
	 * */
	public Boolean requestTaskExecuteFinish() {
		Boolean resultBoolean = false;
		if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
			String methodName = "MobileTaskExecute";
			// ��ȡ��Ӧ��ֵ
			String fkyj = info_taskbj.getText().toString();
			// String blr = task_information_sp.getTag().toString();
			// String xzr = task_information_ed_fshr.getTag().toString();
			// ��ȡ��ǰʱ��
			SimpleDateFormat timefmtDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String currtime = timefmtDate.format(new java.util.Date());

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();

			// �������
			ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			//
			map.put("CurrentFlowTaskId", flowtransaction.getCurrentFlowTaskId());
			map.put("TransactionType", OperationCodeFinish);
			// map.put("TransactionType", 29+"");
			map.put("Comment", fkyj);
			map.put("NextTransactorId", "0");
			// map.put("CoTransactorArry", xzr);
			map.put("TransactorId", "0");
			map.put("TransactorName", Global.getGlobalInstance().getUserid());
			map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
			map.put("TransactedTime", currtime);
			map.put("FromChannel", "2");
			data_json.add(map);
			String DataJson = JsonHelper.listToJSONXin(data_json);
			param.put("parms", DataJson);
			param.put("coTransactorArry", "");
			params.add(param);
			try {
				String json = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
				if (json != null && !json.equals("")) {
					if (json.contains("true")) {
						resultBoolean = true;
					} else {
						resultBoolean = false;
					}
				} else {
					resultBoolean = false;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			// if (resultBoolean) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
			// handler.sendEmptyMessage(XIAFA_SUCCESS);
			// } else {
			// handler.sendEmptyMessage(XIAFA_FALI);
			// }

		} /*
		 * else { handler.sendEmptyMessage(NO_NET); return; }
		 */
		return resultBoolean;
	}

	/**
	 * ������ҵ�ӿ�
	 * */
	public Boolean associatedEnterprises() {
		Boolean resultBoolean = false;
		if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
			String methodName = "AddTaskEntLink";
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			// String[] enterCodes = null;
			StringBuffer enterCodes = new StringBuffer();
			if (CompanyAdapterData != null) {
				// int size = CompanyAdapterData.size();
				// enterCodes = new String[size];

				for (int i = 0; i < CompanyAdapterData.size(); i++) {
					// if (i == 0) {
					// enterCodes.append("[" + "\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + ",");
					// } else if (i!=0&&i == CompanyAdapterData.size() - 1) {
					// enterCodes.append("\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + "]");
					// }else if (i==0&&CompanyAdapterData.size()!=1) {
					// enterCodes.append("[" + "\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + "]");
					// }else {
					// enterCodes.append("\""
					// + CompanyAdapterData.get(i).get("qyguid")
					// + "\"" + ",");
					// }

					if (i == 0 && CompanyAdapterData.size() == 1) {
						enterCodes.append("[" + "\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + "]");
					} else if (i != 0 && i == CompanyAdapterData.size() - 1) {
						enterCodes.append("\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + "]");
					} else if (i == 0) {
						enterCodes.append("[" + "\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + ",");
					} else {
						enterCodes.append("\""
								+ CompanyAdapterData.get(i).get("qyguid")
								+ "\"" + ",");
					}
					// enterCodes[i] =
					// "\""+CompanyAdapterData.get(i).get("qyguid")+"\"";
				}
			}

			long tempp1 = Long.valueOf(transactedFlowInstanceId);
			long tempp = Long.valueOf(lawenforcementtask.getTaskId());
			param.put("taskId", tempp);
			param.put("enterCode", enterCodes.toString());

			params.add(param);

			try {
				Object json = WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_BOOLEAN, true);
				if (json != null && !json.equals("")) {

					resultBoolean = Boolean.parseBoolean(json.toString());
				} else {
					resultBoolean = false;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			// if (resultBoolean) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
			// handler.sendEmptyMessage(XIAFA_SUCCESS);
			// } else {
			// handler.sendEmptyMessage(XIAFA_FALI);
			// }

		} /*
		 * else { handler.sendEmptyMessage(NO_NET); return; }
		 */
		return resultBoolean;
	}

	public Boolean requestTaskExecuteFinish_yanlai() {
		Boolean result = false;
		String methodName = "TaskExecuteFinish";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("Tid", RWBH);
		hashMap.put("LoginUserId", Global.getGlobalInstance().getUserid());
		hashMap.put("ApprovalResult", info_taskbj.getText().toString());
		hashMap.put("FlowDirection", "0");
		hashMap.put("AuditUserId", "");
		hashMap.put("SecondaryAuditUserId", "");
		list.add(hashMap);
		String workFlowJson = JsonHelper.listToJSON(list);
		param.put("workFlowJson", workFlowJson);
		param.put("taskInfoJson", rwxx.GetTask(RWBH, RWXX.RWZT_WAIT_AUDIT)
				.toString());
		// ArrayList<String> RWzxr=rwxx.getTaskExecutorId(RWBH);
		/*
		 * ArrayList< HashMap<String, Object>> data=new
		 * ArrayList<HashMap<String,Object>>(); for(int i=0;i<RWzxr.size();i++){
		 * String zxrUserid=RWzxr.get(i).toString(); if(!zxrUserid.equals("")){
		 * HashMap<String, Object> map=new HashMap<String, Object>();
		 * map.put("RWXXBH", RWBH); map.put("UserID", zxrUserid); data.add(map);
		 * } }
		 */

		String token = "";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		param.put("token", token);
		params.add(param);

		try {
			result = (Boolean) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_BOOLEAN, true);
			if (result == null) {
				result = false;
			}
		} catch (IOException e) {

			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * wsc ������Ƴ������ִ�������ǣ����������
	 * */

	public void callSelfAuditWebService() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {

					String methodName = "SaveWorkFlowInfo";
					ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> param = new HashMap<String, Object>();
					ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("Tid", RWBH);
					hashMap.put("LoginUserId", Global.getGlobalInstance()
							.getUserid());
					hashMap.put("ApprovalResult", "");
					hashMap.put("FlowDirection", "0");

					hashMap.put("AuditUserId", "");
					hashMap.put("SecondaryAuditUserId", "");

					// ��˽����Ϊ1
					hashMap.put("IsPassed", "1");

					list.add(hashMap);
					String workFlowJson = JsonHelper.listToJSON(list);
					param.put("workFlowJson", workFlowJson);
					param.put("taskInfoJson",
							rwxx.GetTask(RWBH, RWXX.RWZT_WAIT_AUDIT).toString());
					param.put("taskExecuteUserJson", "");
					param.put("entInfoJson", "");
					String token = "";
					try {
						token = DESSecurity.encrypt(methodName);
					} catch (Exception e1) {

						e1.printStackTrace();
					}
					param.put("token", token);
					params.add(param);

					Boolean result = false;
					try {
						result = (Boolean) WebServiceProvider.callWebService(
								Global.NAMESPACE, methodName, params, Global
										.getGlobalInstance().getSystemurl()
										+ Global.WEBSERVICE_URL,
								WebServiceProvider.RETURN_BOOLEAN, true);
						if (result == null) {
							result = false;
						}
					} catch (IOException e) {

						e.printStackTrace();
						result = false;
					}
					if (result) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
						if (userZw.equals("3")) {
							// rwxx.changeTaskState(RWBH,
							// RWXX.RWZT_WATE_EXECUTION);
							DataSyncModel dm = new DataSyncModel();
							dm.synchronizeFetchServerData(true,
									"T_YDZF_RWXX_USER");
							dm.synchronizeFetchServerData(true, "T_YDZF_RWXX");
						}
					} else {
					}

				} else {
					return;
				}

			}
		}).start();
	}

	private boolean onFile = false;// ����鵵��־

	/**
	 * ����Webservice�����������
	 * */
	public void callAuditWebService() {
		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setLoadMsg("�����ύ��Ϣ", "");
		yutuLoading.showDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {

					String methodName = "SaveWorkFlowInfo";
					// ��ȡ��Ӧ��ֵ
					String pfyj = task_information_ed.getText().toString();
					String shjg = OperationCode;
					String blr = task_information_sp.getTag().toString();
					String xzr = task_information_ed_fshr.getTag().toString();

					ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> param = new HashMap<String, Object>();
					ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("Tid", RWBH);
					hashMap.put("LoginUserId", Global.getGlobalInstance()
							.getUserid());
					hashMap.put("ApprovalResult", task_information_ed.getText()
							.toString());
					hashMap.put("FlowDirection", TaskFlowDirection);
					if (TaskFlowDirection.equals("1")) {// ����
						if (userZw.equals("3")) {
							hashMap.put("AuditUserId", sbZxr.toString());
							hashMap.put("SecondaryAuditUserId",
									sbZhr.toString());

						} else {
							hashMap.put("AuditUserId", sbZshr.toString());
							hashMap.put("SecondaryAuditUserId",
									sbFshr.toString());
						}

					} else {// ����
						// hashMap.put("AuditUserId", selectArchivePersonEdit !=
						// null ?
						// getInfoByName(selectArchivePersonEdit.getText().toString()).get(0).get("userid").toString()
						// : "");
						hashMap.put("AuditUserId", onFile ? sbZshr.toString()
								: "");
						hashMap.put("SecondaryAuditUserId", "");
					}
					// ��˽�����������ѡ��Ĭ��ֵΪ1
					String IsPassed = ((SpinnerItem) (task_information_sp_shjg
							.getSelectedItem())).getCode();
					if (IsPassed.equals("")) {
						IsPassed = "1";
					}
					if (checkBox_moveZhr.isChecked()) {
						IsPassed = "2";
					}
					hashMap.put("IsPassed", IsPassed);

					list.add(hashMap);
					String workFlowJson = JsonHelper.listToJSON(list);
					param.put("workFlowJson", workFlowJson);
					param.put("onFile", onFile + "");
					if (userZw.equals("3") && TaskFlowDirection.equals("1")) {
						param.put("taskInfoJson",
								rwxx.GetTask(RWBH, RWXX.RWZT_WATE_EXECUTION)
										.toString());
						ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						String zxrUserid = sbZxr.toString();
						if (!zxrUserid.equals("")) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("RWXXBH", RWBH);
							map.put("UserID", zxrUserid);
							data.add(map);
						}
						param.put("taskExecuteUserJson",
								JsonHelper.listToJSON(data));
						param.put("entInfoJson", "");
					} else {
						if (TaskFlowDirection.equals("1")) {
							if (IsPassed == "1") {
								// ���ͨ����ѡ����һ�ڵ��ǿƳ�������״̬Ϊ�����
								ArrayList<HashMap<String, Object>> userData = getInfoByName(task_information_sp
										.getText().toString());
								if (!userData.get(0).get("zw").equals("0")) {
									param.put(
											"taskInfoJson",
											rwxx.GetTask(RWBH,
													RWXX.RWZT_WAIT_AUDIT)
													.toString());
									param.put("taskExecuteUserJson", "");
									param.put("entInfoJson", "");
									// ���ͨ����ѡ����һ�ڵ���ֱ�Ӱ칫��ִ���ˣ�����״̬Ϊ��ִ��
								} else {
									param.put(
											"taskInfoJson",
											rwxx.GetTask(RWBH,
													RWXX.RWZT_WATE_EXECUTION)
													.toString());
									// ����ִ���˹�����
									ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("RWXXBH", RWBH);
									map.put("UserID",
											userData.get(0).get("userid"));
									data.add(map);
									param.put("taskExecuteUserJson",
											JsonHelper.listToJSON(data));
									param.put("entInfoJson", "");
								}
							} else {// ��˲�ͨ��������״̬Ϊ���޸ġ�������
								param.put(
										"taskInfoJson",
										rwxx.GetTask(RWBH,
												RWXX.RWZT_WATE_EXECUTION)
												.toString());
								param.put("taskExecuteUserJson", "");
								param.put("entInfoJson", "");
							}
						} else if (TaskFlowDirection.equals("0")) {
							if (IsPassed == "1") {// ���У����ͨ��
								param.put(
										"taskInfoJson",
										rwxx.GetTask(RWBH, RWXX.RWZT_WAIT_AUDIT)
												.toString());
								param.put("taskExecuteUserJson", "");
								param.put("entInfoJson", "");
							} else {// ���У���˲�ͨ��
								param.put(
										"taskInfoJson",
										rwxx.GetTask(RWBH,
												RWXX.RWZT_WATE_EXECUTION)
												.toString());
								param.put("taskExecuteUserJson", "");
								param.put("entInfoJson", rwxx
										.GetEntpriLinkAllItems(RWBH).toString());
							}
						}
					}
					String token = "";
					try {
						token = DESSecurity.encrypt(methodName);
					} catch (Exception e1) {

						e1.printStackTrace();
					}
					param.put("token", token);
					params.add(param);

					Boolean result = false;
					try {
						result = (Boolean) WebServiceProvider.callWebService(
								Global.NAMESPACE, methodName, params, Global
										.getGlobalInstance().getSystemurl()
										+ Global.WEBSERVICE_URL,
								WebServiceProvider.RETURN_BOOLEAN, true);
						if (result == null) {
							result = false;
						}
					} catch (IOException e) {

						e.printStackTrace();
						result = false;
					}
					if (result) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
						if (userZw.equals("3")) {
							if (TaskFlowDirection.equals("0")
									&& !(IsPassed == "1")) {
								rwxx.changEnpriLinkAllState(RWBH, "2");// ��˲�ͨ�������������ҵ״̬Ϊ2��ִ����
							}
							DataSyncModel dm = new DataSyncModel();
							dm.synchronizeFetchServerData(true,
									"T_YDZF_RWXX_USER");
							dm.synchronizeFetchServerData(true, "T_YDZF_RWXX");

						}
						handler.sendEmptyMessage(XIAFA_SUCCESS);
					} else {
						handler.sendEmptyMessage(XIAFA_FALI);
					}

				} else {
					handler.sendEmptyMessage(NO_NET);
					return;
				}

			}
		}).start();
	}

	private Json_list json_list;
	ArrayList<FlowStepListxx> FlowStepListxxArr;
	ArrayList<FlowTaskListxx> FlowTaskListxxArr;
	FlowTransaction flowtransaction;
	LawEnforcementTask lawenforcementtask;

	/**
	 * //��ȡ����
	 */
	public ArrayList<HashMap<String, Object>> requestData(
			String transactedFlowInstanceId) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		// String methodName = "QueryDetail";
		String methodName = "GetTaskQueryDetail";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parms", transactedFlowInstanceId);
		LogUtil.i("parms", "����parms��" + transactedFlowInstanceId);
		params.add(param);
		try {
			String json = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (json != null && !json.equals("")) {
				json_list = JsonHelper.paseJSON(json, Json_list.class);
				FlowStepListxxArr = json_list.getFlowStepList();
				FlowTaskListxxArr = json_list.getFlowTaskList();
				flowtransaction = json_list.getFlowTransaction();
				lawenforcementtask = json_list.getLawEnforcementTask();
				LogUtil.i(TAG, "������Ϣ��������ݣ�" + json_list.toString());
			} else {

			}
			handler.sendEmptyMessage(100);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	String OperationCode = "";
	String OperationCodeFinish = "";
	String FlowTaskId = "";
	private String processStatus;
	private String ykrwbh;
	private String daiXieBan;

	/**
	 * ѡ�������
	 * 
	 * @param rwGuid
	 *            �����guid
	 */
	public void getLeaderData(String OperationCode, String FlowTaskId) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String methodName = "GetNodeTransactor";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		// �������
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("OperationCode", OperationCode);
		map.put("FlowTaskId", FlowTaskId);
		data_json.add(map);
		String LeaderDataJson = JsonHelper.listToJSONXin(data_json);
		param.put("transactorQueryModelJson", LeaderDataJson);
		params.add(param);
		try {
			String json = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (json != null && !json.equals("")) {
				zshrData = JsonHelper.paseJSON(json);
			} else {

			}
			handler.sendEmptyMessage(101);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * ѡ��Э����
	 * 
	 * @param
	 */
	public void getSpecifiedData() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String methodName = "GetSpecifiedUser";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		// �������
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TargetType", "10");
		map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
		map.put("ChargeUserId", task_information_sp.getTag());
		data_json.add(map);
		String LeaderDataJson = JsonHelper.listToJSONXin(data_json);
		param.put("operationTargetJson", LeaderDataJson);
		params.add(param);
		try {
			String json = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (json != null && !json.equals("")) {
				fshrData = JsonHelper.paseJSON(json);
			} else {

			}
			//���ε�ѡЭ����
			handler.sendEmptyMessage(102);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void getSpecifiedData2() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String methodName = "GetSpecifiedUser";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		// �������
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("TargetType", "1");
		map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
		data_json.add(map);
		String LeaderDataJson = JsonHelper.listToJSONXin(data_json);
		param.put("operationTargetJson", LeaderDataJson);
		params.add(param);
		try {
			String json = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (json != null && !json.equals("")) {
				zshrData = JsonHelper.paseJSON(json);
			} else {

			}
			handler.sendEmptyMessage(999);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	private String jsonTiJiao;
	private String qylistjson;
	/**
	 * ����Webservice����������� �ύ�ӿ�
	 * */
	public void callAuditWebService1() {
		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setLoadMsg("�����ύ��Ϣ", "");
		yutuLoading.showDialog();

		new Thread(new Runnable() {

		

			@Override
			public void run() {
				if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					Boolean resultBoolean = false;
					String methodName = "MobileTaskExecute";
					// ��ȡ��Ӧ��ֵ
					String pfyj = task_information_ed.getText().toString();
					String blr = task_information_sp.getTag().toString();

					// ��ȡ��ǰʱ��
					SimpleDateFormat timefmtDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String currtime = timefmtDate.format(new java.util.Date());

					ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> param = new HashMap<String, Object>();

					// �������
					ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();
					//
					map.put("CurrentFlowTaskId",
							flowtransaction.getCurrentFlowTaskId());
					// ����codeΪ ��ͨ�� �½ڵ������id wei
					if (now_type == 0) {

						map.put("TransactionType", OperationCode);
						if ("52".equals(OperationCode)) {
							map.put("NextTransactorId", 0);
						} else {
							map.put("NextTransactorId", blr);
						}

					} else if (now_type == 1) {

						map.put("TransactionType", 32);
						map.put("NextTransactorId", 0);
					} else if (now_type == 2) {
						map.put("TransactionType", 21);
						map.put("NextTransactorId", 0);

					} else if (now_type == 3) {
						map.put("TransactionType", 50);
		
						map.put("NextTransactorId",0);
					
					}
					
					
					

					map.put("Comment", pfyj);

					// map.put("CoTransactorArry", xzr);
					map.put("TransactorId", "0");
					map.put("TransactorName", Global.getGlobalInstance()
							.getUserid());
					map.put("RegionCode", Global.getGlobalInstance()
							.getAreaCode());
					map.put("TransactedTime", currtime);
					map.put("FromChannel", "2");
					data_json.add(map);
					String DataJson = JsonHelper.listToJSONXin(data_json);
					param.put("parms", DataJson);
					// �޸Ŀ��Բ�ѡ��Э����
					if (!processStatus.equals("17")
							&& !task_information_ed_fshr.getText().toString()
									.equals("")) {
						String xzr = task_information_ed_fshr.getTag()
								.toString();
						String[] split = xzr.split(",");
						List<String> xzrList = new ArrayList<String>();
						
						for (int i = 0; i < split.length; i++) {
							xzrList.add(split[i]);
						}
						
						
						param.put("coTransactorArry", xzrList.toString());
					} else {
						param.put("coTransactorArry", "");
					}
					if (now_type==3) {
						 String xiafa = task_information_sp
									.getTag().toString();
								 
								 String[] split2 = xiafa.split(",");
									List<String> xfrList = new ArrayList<String>();
									
									
									for (int i = 0; i < split2.length; i++) {
										xfrList.add("\""+split2[i]+"\"");	
									}
									param.put("dispatchUnitListArry",xfrList+"");
								
					}else{
						param.put("dispatchUnitListArry","");
					}
					
					params.add(param);
					
					
				
					try {
						jsonTiJiao = (String) WebServiceProvider
								.callWebService(Global.NAMESPACE, methodName,
										params, Global.getGlobalInstance()
												.getSystemurl()
												+ Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_STRING, true);
						if (jsonTiJiao != null && !jsonTiJiao.equals("")) {
							if (jsonTiJiao.contains("true")) {
								resultBoolean = true;
							} else {
								resultBoolean = false;
							}
						} else {
							resultBoolean = false;
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
					if (resultBoolean) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
						handler.sendEmptyMessage(XIAFA_SUCCESS);
					} else {
						if (!"".equals(jsonTiJiao)) {
							//{"RequestType":50,"Result":false,"Message":"��Ͻ������ɳ����û������Ĭ��ǩ����,�����·�ʧ�ܣ�","BizId":0}
							Message msg=new Message();
							msg.what=XIAFA_FALI;
								Gson gson =new Gson();
					Respones respones = gson.fromJson(jsonTiJiao, Respones.class);

//				
					msg.obj=respones.Message;
							//handler.sendEmptyMessage(XIAFA_FALI);
					handler.sendMessage(msg);
						}else{
							
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									if (yutuLoading!=null) {
										yutuLoading.dismissDialog();
									}
									Toast.makeText(TaskInformationActivity.this, "�����ύʧ��",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
			
						
				//		handler.sendEmptyMessage(XIAFA_FALI);
					}

				} else {
					handler.sendEmptyMessage(NO_NET);
					return;
				}

			}
		}).start();
	}

	/**
	 * ����Webservice����������� �ύ�ӿ�
	 * */
	public void callAuditWebService2() {
		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setLoadMsg("���ڹ鵵", "");
		yutuLoading.showDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					Boolean resultBoolean = false;
					String methodName = "MobileTaskExecute";
					// ��ȡ���
					String ztc = task_information_gd.getText().toString();
					// ��ȡ��ǰʱ��
					SimpleDateFormat timefmtDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String currtime = timefmtDate.format(new java.util.Date());

					ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> param = new HashMap<String, Object>();

					// �������
					ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();

					map.put("TransactedTime", currtime);// ��ǰʱ��
					map.put("FromChannel", "2");// ��Դ���� �̶�ֵ2
					map.put("TransactorId", "0");
					map.put("RegionCode", Global.getGlobalInstance()
							.getAreaCode());

					map.put("CurrentFlowTaskId",
							flowtransaction.getCurrentFlowTaskId());// ��ǰ��������id
					map.put("TransactionType", flowtransaction
							.getAvailableActionList().get(0).getCode());// ��������
					map.put("TransactorName", Global.getGlobalInstance()
							.getUserid());// ����������
					map.put("Comment", ztc);// ���
					data_json.add(map);
					String DataJson = JsonHelper.listToJSONXin(data_json);
					param.put("parms", DataJson);
					params.add(param);
					try {
						String json = (String) WebServiceProvider
								.callWebService(Global.NAMESPACE, methodName,
										params, Global.getGlobalInstance()
												.getSystemurl()
												+ Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_STRING, true);
						if (json != null && !json.equals("")) {
							if (json.contains("true")) {
								resultBoolean = true;
							} else {
								resultBoolean = false;
							}
						} else {
							resultBoolean = false;
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
					if (resultBoolean) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
						handler.sendEmptyMessage(XIAFA_SUCCESS);
					} else {
						handler.sendEmptyMessage(XIAFA_FALI);
					}

				} else {
					handler.sendEmptyMessage(NO_NET);
					return;
				}

			}
		}).start();
	}

	/**
	 * ����Webservice��������鵵
	 * */
	public void callArchiveTaskWebService() {
		yutuLoading = new YutuLoading(TaskInformationActivity.this);
		yutuLoading.setLoadMsg("���ڹ鵵", "");
		yutuLoading.showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {
				String methodName = "MobileTaskExecute";
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param = new HashMap<String, Object>();
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("Tid", RWBH);
				hashMap.put("LoginUserId", Global.getGlobalInstance()
						.getUserid());
				hashMap.put("ApprovalResult", task_information_gd.getText()
						.toString());
				hashMap.put("FlowDirection", "0");
				hashMap.put("AuditUserId", "");
				hashMap.put("SecondaryAuditUserId", "");
				hashMap.put("IsPassed", "1");
				list.add(hashMap);
				String workFlowJson = JsonHelper.listToJSON(list);
				param.put("workFlowJson", workFlowJson);
				// ���taskInfoJson
				String keyWord = task_information_gd.getText().toString();
				String sql = "update T_YDZF_RWXX set KeyWord='" + keyWord
						+ "' where rwbh='" + RWBH + "'";
				SqliteUtil.getInstance().ExecSQL(sql);
				String taskInfoJson = rwxx.GetTask(RWBH, RWXX.RWZT_ON_FINISH)
						.toString();
				param.put("taskInfoJson", taskInfoJson);
				param.put("taskExecuteUserJson", "");
				param.put("entInfoJson", "");

				String token = "";
				try {
					token = DESSecurity.encrypt(methodName);
				} catch (Exception e1) {

					e1.printStackTrace();
				}
				param.put("token", token);

				params.add(param);

				Boolean result = false;
				try {
					result = (Boolean) WebServiceProvider.callWebService(
							Global.NAMESPACE, methodName, params, Global
									.getGlobalInstance().getSystemurl()
									+ Global.WEBSERVICE_URL,
							WebServiceProvider.RETURN_BOOLEAN, true);
					if (result == null) {
						result = false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					result = false;
				}
				if (result) {// ��˳ɹ��������ǰ�û�Ϊֱ�Ӹ����ˣ���ʱ������״̬�Ѿ��ı�Ϊ��ִ����Ҫ����һ��
					DataSyncModel dm = new DataSyncModel();
					dm.synchronizeFetchServerData(true, "T_YDZF_RWXX_USER");
					dm.synchronizeFetchServerData(true, "T_YDZF_RWXX");
					handler.sendEmptyMessage(GUIDANG_SUCCESS);
				} else {
					handler.sendEmptyMessage(GUIDANG_FALI);
				}

			}
		}).start();

	}

	public void upLoadFilesMethod2(ArrayList<TaskFile> fileList,
			Context context, String rwGuid) {

		new TaskUploadAsync2(fileList, context, rwGuid).execute();
	}

	class TaskUploadAsync2 extends AsyncTask<Object, Integer, Object> {

		ArrayList<TaskFile> listAllFile;
		/** �ϴ����������� */
		ProgressDialog pdialog;
		private int index;
		/** �����Ƿ��Ѿ��ϴ��� */
		Boolean isUpload = false;
		private Context mcontext;
		private String rwGuid;

		TaskUploadAsync2(ArrayList<TaskFile> listAllFile, Context mcontext,
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
		//	pdialog.cancel();
//
//			handler.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					if (pdialog!=null) {
//						pdialog.dismiss();
//					}
//				}
//			});
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
								+ GetFujian2(taskFile, i + "", rwGuid)
										.toString() + "]";

						Boolean resultBoolean = httpUploader.upLoadAffixMethod(
								AttachmentJosn, attachmentData, isEnd);
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
			if (handler != null) {
				handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
			}
			return null;

		}

	}

	private JSONObject GetFujian2(TaskFile taskFile, String i, String rwGuid) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance()
				.getList("T_Attachment", conditions);
		// �õ�billid
		HashMap<String, Object> conditions2 = new HashMap<String, Object>();
		conditions.put("guid", rwGuid);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("T_YDZF_RWXX", conditions2);
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
				}
				_JsonObject.put(_Key, entry.getValue().toString());
				// Object _Object = _HashMapTemp.get(_Key);
				// _JsonObject.put("billid", taskFile.getBillid());
				if (_Key.equals("FK_ID") || _Key.equals("fk_id")) {

					String fk_id = entry.getValue().toString();

					// TODO ����id
					_JsonObject.put("billid", lawenforcementtask.getTaskCode());

				}

				// _JsonObject.put("billid",
				// String.valueOf(data.get(0).get("rwbh")));
				_JsonObject.put("biztype", 12 + "");

			}

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}
	//�����·��ĵ���ѡ��
	public void createDialogViewForTaskType() {
		LayoutInflater inflater = LayoutInflater
				.from(TaskInformationActivity.this);
		View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
		TextView expand_title_tv = (TextView) viewex
				.findViewById(R.id.expand_title_tv);
		expand_title_tv.setText("ѡ���·�����");
		
		final LinkedList<String> linkedList = new LinkedList<String>();
		final LinkedList<String> linkedName = new LinkedList<String>();
		//��ȡ���list
		final ArrayList<HashMap<String, Object>> groupData = rwxx
				.getTask_type3();
		// final ArrayList<HashMap<String, Object>> groupData =
		// getRegionList("1");// ���е�ȫ����Ϣ
		final List<String> groupList = new ArrayList<String>();
		if (groupData != null && groupData.size() > 0) {
			for (int i = 0; i < groupData.size(); i++) {
				Object name = groupData.get(i).get("regionname");
				groupList.add(name + "");
			}
		}
		//��list
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx
				.getTask_type_child3(groupData);

		final ExpandableListView expandableListView = (ExpandableListView) viewex
				.findViewById(R.id.expandablelistview);
		ExpandableIncludeParentAdapter adapter = new ExpandableIncludeParentAdapter(
				this, groupList, childMapData);
		
		SelectAuditorAdapter2 expandableListViewAdapter = new TaskManagerModel().getselectAuditorAdapter2(groupList, childMapData, linkedList, linkedName, TaskInformationActivity.this);
		expandableListView.setAdapter(expandableListViewAdapter);// �������ݣ���������
		
	//	expandableListView.setAdapter(adapter);
		expandableListView.setCacheColorHint(0);// �����϶��б��ʱ���ֹ���ֺ�ɫ����
		expandableListView.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));
		//����Ĭ��չ������Ŀ
		if (!"230000000".equals(Global.getGlobalInstance().getAreaCode())) {
			for(int i = 0; i < expandableListViewAdapter.getGroupCount(); i++){  
	            
				   expandableListView.expandGroup(i);  
				                          
				}  
		}
		String builderSql="select RegionName from Region where RegionCode='"+Global.getGlobalInstance().getAreaCode()+"'";
			
		ArrayList<HashMap<String, Object>> builderDate = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(builderSql);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TaskInformationActivity.this);
		builder.setIcon(R.drawable.yutu);
		if (builderDate != null && builderDate.size() > 0) {
			builder.setTitle(builderDate.get(0).get("regionname").toString());
		}else{
			builder.setTitle("������ʡ");
		}
	
		
//		TextView tv = new TextView(TaskInformationActivity.this);  
//        tv.setText("������ʡ");    //����  
//        tv.setTextSize(22);//�����С  
//        tv.setPadding(30, 20, 10, 10);//λ��  
//        tv.setTextColor(Color.WHITE);//��ɫ  
//        tv.setBackgroundColor(Color.parseColor("#404E8B"));
//        builder.setCustomTitle(tv);//����setTitle()  
		
		builder.setView(viewex);
		
		builder.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {

					

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
						userName = new StringBuffer();
						StringBuffer sbFshr = new StringBuffer();
						
						
						for (int i = 0; i < linkedList.size(); i++) {
							
							
							userName.append(linkedName.get(i) + ",");
							sbFshr.append(linkedList.get(i)+",");
						}
					
						/* ȥ��, */
						if (sbFshr.length() > 0) {
							sbFshr.deleteCharAt(sbFshr.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
					
						
						task_information_sp.setText(userName);
						
						
						
						task_information_sp.setTag(sbFshr);
						
					}

				});

		builder.setNeutralButton("ȫѡ",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
						StringBuffer userName = new StringBuffer();
						StringBuffer sbFshr = new StringBuffer();
						for (int i = 0; i < childMapData.size(); i++) {
							for (int j = 0; j < childMapData.get(i).size(); j++) {
								sbFshr.append(childMapData.get(i).get(j)
										.get("regioncode")
										+ ",");
								userName.append(childMapData.get(i).get(j)
										.get("regionname")
										+ ",");
							}
						}

						if (sbFshr.length() > 0) {
							sbFshr.deleteCharAt(sbFshr.length() - 1);
						}
						if (userName.length() > 0) {
							userName.deleteCharAt(userName.length() - 1);
						}
						task_information_sp.setText(userName);
						task_information_sp.setTag(sbFshr);
					}
				});
		builder.setNegativeButton("ȡ��", null);
		

		final AlertDialog dialog = builder.create();
		dialog.show();

//		expandableListView
//				.setOnGroupExpandListener(new OnGroupExpandListener() {
//
//					@Override
//					public void onGroupExpand(int groupPosition) {
//						for (int i = 0; i < groupList.size(); i++) {
//							if (groupPosition != i) {
//							//	expandableListView.collapseGroup(i);
//							}
//						}
//					}
//				});
//		expandableListView.setOnChildClickListener(new OnChildClickListener() {
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				if (childPosition == 0) {
//					String cnames = groupData.get(groupPosition)
//							.get("regionname").toString();
//					String rwxfCode = groupData.get(groupPosition)
//							.get("regioncode").toString();
//					task_information_sp.setText(cnames);
//					task_information_sp.setTag(rwxfCode);
//				} else {
//					String cnames = childMapData.get(groupPosition)
//							.get(childPosition - 1).get("regionname")
//							.toString();
//					String rwxfCode = childMapData.get(groupPosition)
//							.get(childPosition - 1).get("regioncode")
//							.toString();
//					task_information_sp.setText(cnames);
//					task_information_sp.setTag(rwxfCode);
//
//				}
//				//dialog.cancel();
//				return false;
//			}
//		});
	}
	
	class Respones {
		
		//{"RequestType":50,"Result":false,"Message":"��Ͻ������ɳ����û������Ĭ��ǩ����,�����·�ʧ�ܣ�","BizId":0}
	public 	String RequestType;
	public 	String Result;
	public 	String Message;
	public 	String BizId;
		
	}
	
	
	   public  String hashMapToJson(HashMap map) {  
	        String string = "{";  
	        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
	            Entry e = (Entry) it.next();  
	            string += "'" + e.getKey() + "':";  
	            string += "'" + e.getValue() + "',";  
	        }  
	        string = string.substring(0, string.lastIndexOf(","));  
	        string += "}";  
	        return string;  
	    }  
	   
	   class QyList{
		   
		   /*
		    * {
    "TaskEntList": [
        {
            "Id": 855,
            "TaskId": 0,
            "EntCode": "136d5289-80ff-4b78-9f0c-cdb58815e15c",
            "Status": 1,
            "Remark": null,
            "UpdatedTime": "0001-01-01 00:00:00",
            "EntName": "������ʡ�۽���ͨԴ��ҵ�������ι�˾",
            "Address": null,
            "SurveyUnit": "������ʡ",
            "RegionCode": null,
            "PageSize": 0,
            "PageIndex": 0
        }
    ],
    "TotalCount": 1
}*/
		   
		   public List<LeTaskEntLinkListxx2> TaskEntList;
		   
		   public String TotalCount;

		@Override
		public String toString() {
			return "QyList [TaskEntList=" + TaskEntList + ", TotalCount="
					+ TotalCount + "]";
		}
		   
		   
		   
		   
	   }

}
