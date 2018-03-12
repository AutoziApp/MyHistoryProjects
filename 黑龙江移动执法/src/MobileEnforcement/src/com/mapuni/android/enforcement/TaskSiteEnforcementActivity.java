/**
 * 
 */
package com.mapuni.android.enforcement;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.MobileEnforcement.R.id;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.base.widget.CustomAutoCompleteTextView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.helper.LawKnowAllActivity;
import com.mapuni.android.ui.Timeuitl;
import com.mapuni.android.uitl.PanduanDayin;

/**
 * @author SS
 * 
 *         Description �ֳ�ִ�����ִ����ť���������-----�ֳ������¼
 * 
 *         Incoming parameters ��ҵ���롢�����š�(�ɴ���)����״̬
 */
public class TaskSiteEnforcementActivity extends BaseActivity implements OnItemClickListener {
	private String guid = "";
	/** ���ݱ����쳣 */
	private final int DATABASE_INSERT_EXCEPTION = 0;
	/** ���ݱ���ɹ� */
	private final int DATA_INSERT_SUCCESS = 1;
	/** ���ݲ�ѯ�ɹ� */
	private final int DATA_QUERY_SUCCESS = 2;
	/** ���û������� */
	private final int THE_USER_OR_ENT_NOT_EXIST = 3;
	/** ���ݸ���ʧ�� */
	private final int DATA_UPDATE_EXCEPTION = 4;
	/** ���ݸ��³ɹ� */
	private final int DATA_UPDATE_SUCCESS = 5;
	/** ���ؼ�����/�������� */
	private final int BACK_DATA_INSERT_SUCCESS = 6;
	/** ��ӡԤ������/�������� */
	private final int PREVIEW_SAVE_DATA_SUCCESS = 7;
	/** �嵥���ִ��ģ�����ݸ��³ɹ� */
	private final int CHECK_LIST_DATA_UPDATE_SUCCESS = 8;
	/** ����ϴ�����/�������� */
	private final int UPLOAD_SAVE_DATA_SUCCESS = 9;
	/** ִ������ͨ����/�������� */
	private final int PEPSI_SAVE_DATA_SUCCESS = 10;
	/** �ֳ�ȡ֤���� */
	private final int SCENE_UPDATE_DATA_SUCCESS = 11;
	/** �ֳ���������¼��� */
	private final int GETDATA_SUCCESS = 12;
	/** ���Ⲽ�� */
	private RelativeLayout two_list_tool_layout;
	/** ����ID */
	protected String RWID;
	/** �������� */
	protected HashMap<String, Object> RWDetail;
	/** bundle���� */
	protected Bundle RWBundle;
	/** ������Ϣ */
	protected RWXX rwxx;
	/** ������Ϣ���� */
	protected ArrayList<HashMap<String, Object>> rwxxAttachment;
	/** ��ȡ�ļ�·�� */
	protected String path;
	/** ����һ�������� */
	private ProgressDialog progressDialog;
	/** ����һ���Ի��� */
	private AlertDialog alertDialog;
	/** ��ʼʱ�䰴ť������ʱ�䰴ť */
	private Button start_time_btn, end_time_btn;
	/** ��ȡ��ǰ�ĵ������� */
	private TextView task_name_tv;
	/** ��ѯȫ�����ݼ��� */
	private ArrayList<HashMap<String, Object>> queryData = null;
	/** ��ѯ��ҵ������Ϣ�����ݼ��� */
	private ArrayList<HashMap<String, Object>> queryEntData = null;
	/** ��ѯ�����˵����ݼ��� */
	private ArrayList<HashMap<String, Object>> queryExeData = null;
	/** ��ѯ��������ݼ��� */
	private ArrayList<HashMap<String, Object>> queryProblemData = null;
	/** ��ѯ����״̬���ݼ��� */
	private ArrayList<HashMap<String, Object>> queryTaskData = null;
	/** ���ѡ�еĿ����˵Ĺ�����λ */
	ArrayList<HashMap<String, Object>> deptData = null;
	/** ��spiNner���������� */
	private List<String> exeList = null;
	/** �嵥��鰴ť */
	private Button task_site_check_list_btn;
	/** ִ������ͨ */
	private Button task_site_law_enforcement_btn;
	/** �嵥���ִ��ģ��Spinner */
	private Spinner task_site_enfor_sp;
	/** �����ѡ���ִ��ģ��ID */

	private String task_site_enfor_sp_id;
	/** ��ѡ�е�Spinner�� */
	private String task_site_enfor_sp_str = null;
	/** ��ҵ���� */
	private String qyid;
	/** ����ID�������� */
	private String rwbh;
	/** �ж�����״̬�ı�־��0�����ִ�С�1��������ɡ�2����ִ���� */
	private String qyzts;
	/** �����ݿ��в��� */
	private ContentValues contentValues = null;

	/** ѡ������ģ��ListView��ʽ */
	private ListView task_site_enfor_lv;

	/** ѯ�ʱ�¼ѡ����������� */
	private CustomAutoCompleteTextView autoCompleteTv;
	/** ��ִͬ���� */
	StringBuffer sbZhr = new StringBuffer();

	/** ѡ���¼�˵İ�ť */
	private Button task_surveypeoplename_btn;

	/** ѡ��ִͬ���˵��������б� */
	private ExpandableListView task_site_enfor_expandable_lv;

	/** �����б�����б�����ݼ��� */
	private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

	/** ��ü�¼�˵����ݼ��� */
	private ArrayList<String> recordList;

	/** 2013��11��15�� �޸� ����µļ�¼�˵����ݼ��� */
	private ArrayList<String> recordNewList = new ArrayList<String>();

	/** ��¼������������ */
	private ArrayAdapter<String> recordAdapter;
	ArrayList<HashMap<String, Object>> itemList;
	ArrayList<HashMap<String, Object>> qdjcList;

	private String dengerList[] = { "��", "Ů" };
	private String record[] = { "��", "Ů" };
	Boolean isshow = false;
	/** ���ݿ⹤���� */
	private SqliteUtil sqliteUtil;
	private TextView tv2;
	private QdjcAdapter qdjcAdapter;

	private LinkedList<String> wDlinkedList = new LinkedList<String>();
	protected ItemAdapter itemAdapter0;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case DATABASE_INSERT_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSiteEnforcementActivity.this, "���ݱ����쳣", Toast.LENGTH_SHORT).show();
				break;

			case DATA_INSERT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}

				Intent saveIntent = new Intent(TaskSiteEnforcementActivity.this, SiteWriteRecordActivity.class);

				saveIntent.putExtra("rwbh", rwbh);

				startActivity(saveIntent);
				break;

			case DATA_QUERY_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (task_name_tv.getText().toString().trim().equals("�ֳ�����¼��")) {

					qdjcList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> hashmap = new HashMap<String, Object>();
					hashmap.put("code", "1");
					hashmap.put("name", "�������");
					qdjcList.add(hashmap);
					hashmap = new HashMap<String, Object>();
					hashmap.put("code", "2");
					hashmap.put("name", "��Ⱦ������ʩ�����������");
					qdjcList.add(hashmap);
					hashmap = new HashMap<String, Object>();
					hashmap.put("code", "3");
					hashmap.put("name", "��Ⱦ���ŷ����");
					qdjcList.add(hashmap);
					hashmap = new HashMap<String, Object>();
					hashmap.put("code", "4");
					hashmap.put("name", "���ڻ������⼰���");
					qdjcList.add(hashmap);

					inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
					View mainview = inflater.inflate(R.layout.node_list, null);
					TextView tv1 = (TextView) mainview.findViewById(R.id.node_list_tv1);
					tv1.setText("����¼");
					tv1.setTextColor(Color.GRAY);
					tv2 = (TextView) mainview.findViewById(R.id.node_list_tv2);
					ImageView iv = (ImageView) mainview.findViewById(R.id.node_iv);
					iv.setVisibility(View.INVISIBLE);
					qdjcAdapter = new QdjcAdapter(qdjcList, TaskSiteEnforcementActivity.this);
					task_site_scoll_jiancha.addView(mainview);
					zhtree = (ListView) mainview.findViewById(R.id.zhzd_tree);
					zhtree.setDivider(getResources().getDrawable(R.drawable.list_divider));
					ImageView dividerImageView = (ImageView) mainview.findViewById(R.id.top_list_divider);
					dividerImageView.setVisibility(View.VISIBLE);
					zhtree.setAdapter(qdjcAdapter);
					zhtree.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							task_site_enfor_sp_id = qdjcList.get(arg2).get("code").toString();
							tv2.setText(qdjcList.get(arg2).get("name").toString());
							tv2.setTextColor(Color.GRAY);
							WorkAsyncTask task = new WorkAsyncTask(TaskSiteEnforcementActivity.this);
							task.execute(task_site_enfor_sp_id);
						}

					});
					return;
				}
				if (recordList == null || recordList.size() == 0) {
					recordList = new ArrayList<String>();
					recordList.add("-����ѡ������-");
					ArrayAdapter<String> rAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
					rAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					task_recordpeopleid_sp.setAdapter(rAdapter);
				}

				if (queryData != null && queryData.size() > 0) {
					/** ��ʼ����������� */
					bind_EntAndExeData();
					/** ��ʼ����������� */
					bind_data();
				} else {
					/** ��ʼ����������� */
					bind_EntAndExeData();
				}

				if (queryProblemData != null && queryProblemData.size() > 0) {

					for (int i = 0; i < queryProblemData.size(); i++) {
						final LinearLayout wdLayout = getWDView();
						wdLayout.setTag(queryProblemData.get(i).get("id").toString());
						final EditText askTv = (EditText) wdLayout.findViewById(R.id.ywyd_ask);
						askTv.setTextSize(15);
						askTv.setText(queryProblemData.get(i).get("wtnr").toString());
						askTv.setTextColor(android.graphics.Color.BLACK);
						// askTv.setFocusable(false);
						final EditText ansTv = (EditText) wdLayout.findViewById(R.id.ywyd_question);
						askTv.setTextSize(15);
						//BYK rwzt
						if (qyzts.equals("3")) {
//							if (qyzts.equals("1")) {
							askTv.setFocusable(false);
							ansTv.setFocusable(false);
						}
						if (queryProblemData.get(i).get("result").toString() != null && !queryProblemData.get(i).get("result").toString().equals("")) {
							ansTv.setText(queryProblemData.get(i).get("result").toString());
						}
						task_site_enfor_wd_layout.addView(wdLayout);

						askTv.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
										.setPositiveButton("��", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												if (qyzts.equals("3")) {
													//BYK RWZT
//													if (qyzts.equals("1")) {
													Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
													return;
												}

												task_site_enfor_wd_layout.removeView(wdLayout);
												Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

											}
										}).setNegativeButton("��", null).create().show();
								return true;
							};
						});
						ansTv.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
										.setPositiveButton("��", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
															//BYK RWZT
												if (qyzts.equals("1")) {
//													if (qyzts.equals("1")) {
													Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
													return;
												}

												task_site_enfor_wd_layout.removeView(wdLayout);
												Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

											}
										}).setNegativeButton("��", null).create().show();
								return true;
							};
						});
						wdLayout.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
										.setPositiveButton("��", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {

												if (qyzts.equals("3")) {
													//BYK rwzt
												//	if (qyzts.equals("1")) {
													Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
													return;
												}

												task_site_enfor_wd_layout.removeView(wdLayout);
												Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

											}
										}).setNegativeButton("��", null).create().show();
								return true;
							}
						});

					}
				}

				break;

			case THE_USER_OR_ENT_NOT_EXIST:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSiteEnforcementActivity.this, "���û����߸���ҵ������ ", Toast.LENGTH_SHORT).show();
				break;

			case DATA_UPDATE_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSiteEnforcementActivity.this, "���ݸ���ʧ�� ", Toast.LENGTH_SHORT).show();
				break;

			case DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSiteEnforcementActivity.this, "���ݸ��³ɹ� ", Toast.LENGTH_SHORT).show();
				Intent updateIntent = new Intent(TaskSiteEnforcementActivity.this, SiteWriteRecordActivity.class);

				updateIntent.putExtra("qyid", qyid);
				updateIntent.putExtra("rwbh", rwbh);

				startActivity(updateIntent);
				break;

			case BACK_DATA_INSERT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				finish();
				break;

			case PREVIEW_SAVE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (task_name_tv.getText().toString().contains("����")) {

					PreviewWrit_lnkc plnkc = new PreviewWrit_lnkc(rwbh, qyid,guid, task_type_edt_str, TaskSiteEnforcementActivity.this);
					String yon = plnkc.create();
					if (yon.equals("y")) {
						File file1 = new File(plnkc.getPath() + "/��1ҳ.html");
						if (!PanduanDayin.appIsInstalled(TaskSiteEnforcementActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(TaskSiteEnforcementActivity.this);
							return;
						}
						PanduanDayin.startprintshare(TaskSiteEnforcementActivity.this, file1.getAbsolutePath());
					} else {
						Toast.makeText(TaskSiteEnforcementActivity.this, "��������", Toast.LENGTH_SHORT).show();
					}
//					File file = new File(Environment.getExternalStorageDirectory().toString()+"/test.txt");
//					boolean b = file.exists();
//					PanduanDayin.startprintshare(TaskSiteEnforcementActivity.this,file.getAbsolutePath());
					

				} else if (task_name_tv.getText().toString().contains("ѯ��")) {
					PreviewWrit_lnxw plnxw = new PreviewWrit_lnxw(rwbh, qyid,guid ,task_type_edt_str, TaskSiteEnforcementActivity.this);
					String yon = plnxw.create();
					if (yon.equals("y")) {
						File file1 = new File(plnxw.getPath() + "/��1ҳ.html");
						if (!PanduanDayin.appIsInstalled(TaskSiteEnforcementActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(TaskSiteEnforcementActivity.this);
							return;
						}
						PanduanDayin.startprintshare(TaskSiteEnforcementActivity.this, file1.getAbsolutePath());
					} else {
						Toast.makeText(TaskSiteEnforcementActivity.this, "��������", Toast.LENGTH_SHORT).show();
					}
				} else if (task_name_tv.getText().toString().equals("�������֪ͨ��")) {
					PreviewWrit_lnjstzs plnjstzs = new PreviewWrit_lnjstzs(rwbh, qyid, TaskSiteEnforcementActivity.this);
					String yon = plnjstzs.create();
					if (yon.equals("y")) {
						File file1 = new File(plnjstzs.getPath() + "/��1ҳ.html");
						if (!PanduanDayin.appIsInstalled(TaskSiteEnforcementActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(TaskSiteEnforcementActivity.this);
							return;
						}
						PanduanDayin.startprintshare(TaskSiteEnforcementActivity.this, file1.getAbsolutePath());
					} else {
						Toast.makeText(TaskSiteEnforcementActivity.this, "��������", Toast.LENGTH_SHORT).show();
					}
				} else if (task_name_tv.getText().toString().equals("�ֳ�����¼��")) {
					// ��ӡԤ����ť���淽��
					PreviewWrit_lnhjjc plnhjjc = new PreviewWrit_lnhjjc(rwbh, qyid, TaskSiteEnforcementActivity.this);
					String yon = plnhjjc.create();
					if (yon.equals("y")) {
						File file1 = new File(plnhjjc.getPath() + "/��1ҳ.html");
						if (!PanduanDayin.appIsInstalled(TaskSiteEnforcementActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(TaskSiteEnforcementActivity.this);
							return;
						}
						PanduanDayin.startprintshare(TaskSiteEnforcementActivity.this, file1.getAbsolutePath());
					} else {
						Toast.makeText(TaskSiteEnforcementActivity.this, "��������", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case CHECK_LIST_DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent intent = new Intent(TaskSiteEnforcementActivity.this, QDJCActivity.class);
				intent.putExtras(RWBundle);
				intent.putExtra("rwbh", rwbh);
				intent.putExtra("qyid", qyid);
				intent.putExtra("stid", task_site_enfor_sp_id);
				startActivity(intent);
				break;

			case UPLOAD_SAVE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (rwxx != null && rwbh != null) {

					// rwxx.uploadTask(rwbh, TaskSiteEnforcementActivity.this,
					// qyid);
				}
				break;

			case PEPSI_SAVE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent pepsi_save_intent = new Intent(TaskSiteEnforcementActivity.this, LawKnowAllActivity.class);
				startActivity(pepsi_save_intent);
				break;

			case SCENE_UPDATE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent scene_update_intent = new Intent(TaskSiteEnforcementActivity.this, SiteEvidenceActivity.class);
				scene_update_intent.putExtra("currentTaskID", rwbh);
				scene_update_intent.putExtra("qyid", qyid);
				startActivity(scene_update_intent);
				break;
			case GETDATA_SUCCESS:
				itemAdapter0 = (ItemAdapter) msg.obj;
				zhtree.setAdapter(itemAdapter0);
				zhtree.setOnItemClickListener(TaskSiteEnforcementActivity.this);
				itemAdapter0.notifyDataSetChanged();

				break;
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && !(resultCode == 20 || resultCode == 40)) {
			if (data != null && data.hasExtra("isClick")) {
				boolean isClick = data.getExtras().getBoolean("isClick");
				if (isClick == true) {
					WorkAsyncTask task = new WorkAsyncTask(TaskSiteEnforcementActivity.this);
					task.execute(task_site_enfor_sp_id);
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/** ��ʼ��ʱ��ؼ� */
	private final Calendar dateAndTime = Calendar.getInstance();
	/** ��ʽ��ʱ��ĸ�ʽ */
	SimpleDateFormat datefmtDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timefmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** ��ÿ�ʼʱ��ͽ���ʱ����ַ��� */
	String startTime = datefmtDate.format(dateAndTime.getTime()) + " 00:00:00";
	String endTime = datefmtDate.format(dateAndTime.getTime()) + " 23:59:59";
	/** ���ϵͳ��ǰʱ�� */
	String currtime = timefmtDate.format(new java.util.Date());

	/** ���ڼ��� */
	OnDateSetListener dateStart = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {// ��Piacker�е������ո���calendar����
			if (isshow) {
				dateAndTime.set(Calendar.YEAR, year);
				dateAndTime.set(Calendar.MONTH, month);
				dateAndTime.set(Calendar.DAY_OF_MONTH, day);

				TimePickerDialog timedialog = new TimePickerDialog(TaskSiteEnforcementActivity.this, timeStart, dateAndTime.get(Calendar.HOUR_OF_DAY),
						dateAndTime.get(Calendar.MINUTE), true);
				timedialog.show();
				isshow = false;
			}
		}
	};

	OnDateSetListener dateStop = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {// ��Piacker�е������ո���calendar����
			// dateAndTime.set(Calendar.YEAR, year);
			// dateAndTime.set(Calendar.MONTH, month);
			// dateAndTime.set(Calendar.DAY_OF_MONTH, day);
			TimePickerDialog timedialog = new TimePickerDialog(TaskSiteEnforcementActivity.this, timeStop, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE),
					true);
			timedialog.show();
		}
	};

	OnDateSetListener again_date = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {// ��Piacker�е������ո���calendar����
			if (isshow) {
				dateAndTime.clear();
				dateAndTime.set(Calendar.YEAR, year);
				dateAndTime.set(Calendar.MONTH, month);
				dateAndTime.set(Calendar.DAY_OF_MONTH, day);
				isshow = true;
				TimePickerDialog timedialog = new TimePickerDialog(TaskSiteEnforcementActivity.this, agin_time, dateAndTime.get(Calendar.HOUR_OF_DAY),
						dateAndTime.get(Calendar.MINUTE), true);

				timedialog.show();
				isshow = false;
			}
		}
	};
	OnTimeSetListener agin_time = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			���ܵ���ʱ��.setText(java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.DEFAULT, java.text.DateFormat.MEDIUM, Locale.CHINESE).format(dateAndTime.getTime()));
			���ܵ���ʱ��.setTag(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateAndTime.getTime()));// 2014-05-30
			// 02:00:35
		}
	};

	/** ʱ����� */
	OnTimeSetListener timeStart = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			start_time_btn.setText(timefmtDate.format(dateAndTime.getTime()));
		}
	};

	/** ʱ����� */
	OnTimeSetListener timeStart1 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			end_time_btn.setText(timefmtDate.format(dateAndTime.getTime()));
		}
	};

	OnTimeSetListener timeStop = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			end_time_btn.setText(timefmtDate.format(dateAndTime.getTime()));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_site_enforcement);
		Intent intent = getIntent();
		sqliteUtil = SqliteUtil.getInstance();
		/** ��ҵ���� */
		qyid = intent.getStringExtra("qyid");
		/** ������ */
		rwbh = intent.getStringExtra("rwbh");
		guid = intent.getStringExtra("guid");
		isSearch = intent.getBooleanExtra("isSearch", false);
//		if (TextUtils.isEmpty(guid)) {
//			guid = UUID.randomUUID().toString();
//		}
		id = intent.getStringExtra("id");
		/***/
		RWBundle = intent.getExtras();
		/***/
		rwxx = (RWXX) RWBundle.get("BusinessObj");
		if (rwxx == null) {
			rwxx = new RWXX();
		}
		// RWID = rwxx.getCurrentID();

		/** ��ʼ��ҳ�� */
		findViewById();
		
		if (isSearch) {
			Toast.makeText(this, "��ǰ��ҵֻ�ܲ鿴!", Toast.LENGTH_LONG).show();
			/** ��ʼ��ҳ�治�ɱ༭ */
			not_edit();
			no_edit();
		}
		
		queryTaskData = new ArrayList<HashMap<String, Object>>();
		/** ���������ź���ҵid��ѯ������״̬ */
		queryTaskData = sqliteUtil.queryBySqlReturnArrayListHashMap("select isexcute from TaskEnpriLink where taskid = '" + rwbh + "' and qyid = '" + qyid + "'");
		if (queryTaskData != null && queryTaskData.size() > 0) {
			qyzts = queryTaskData.get(0).get("isexcute").toString();
		}
		if (qyzts != null) {
		//	if (qyzts.equalsIgnoreCase("1")) {
			//BYK rwzt
				if (qyzts.equalsIgnoreCase("3")) {
				String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
				String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
				//if ("1".equals(qyzts)) {
				//BYK rwzt
				
		
				
					if ("3".equals(qyzts)) {
//					if (!rwxx.JudgeUserName(rwbh)) {
						Toast.makeText(this, "��ǰ��ҵ����ȡ֤����ɣ����ܶԱ�¼���б༭", Toast.LENGTH_LONG).show();
						/** ��ʼ��ҳ�治�ɱ༭ */
						not_edit();
						no_edit();
//					}
				}
				if ("009".equals(new RWXX().queryTaskStatus(rwbh))) {
					Toast.makeText(this, "��ǰ��ҵ�ѹ鵵�����ܶԱ�¼���б༭", Toast.LENGTH_LONG).show();
					/** ��ʼ��ҳ�治�ɱ༭ */
					not_edit();
					no_edit();
				}
			}
		}
		/** ѡ������ģ��ListVIew��ʽ */
		language_lv_temp();

		Bundle bundle = intent.getExtras();
		String fileNameStr = bundle.getString("fn");
		codeStr = bundle.getString("code");
		if (!fileNameStr.equals("") && fileNameStr != null) {
			/** ���ñ�¼�ı��� */
			task_name_tv.setText(fileNameStr);
			/** ����Ϊ1���ֳ����(����)��¼ */
			if (codeStr.equals("1")) {
				task_site_scoll_out_jieshou.setVisibility(View.GONE);
				task_site_scoll_out.setVisibility(View.VISIBLE);
				task_site_scoll_out_jiancha.setVisibility(View.GONE);

				xwbldybj.setVisibility(View.GONE);
//				task_surveyaddress_lay.setVisibility(View.VISIBLE);
				task_sitcondtion_lay.setVisibility(View.VISIBLE);
				task_surveypeoplename_one_tv.setText("���������:");
//				task_dutypeople_tv.setText("�ֳ�����������:");
				task_sitecondition_edt.setVisibility(View.VISIBLE);
				task_site_enfor_wd_layout.setVisibility(View.GONE);
				task_site_enfor_wd_btn.setVisibility(View.GONE);
				task_site_enfor_add_btn.setVisibility(View.GONE);
				xianchangkanchabilu.setVisibility(View.VISIBLE);
			}
			/** ����Ϊ0�ǵ���ѯ�ʱ�¼ */
			else if (codeStr.equals("0")) {
				task_site_scoll_out.setVisibility(View.VISIBLE);
				task_site_scoll_out_jieshou.setVisibility(View.GONE);
				task_site_scoll_out_jiancha.setVisibility(View.GONE);
				task_sitcondtion_lay.setVisibility(View.GONE);
				task_surveypeoplename_one_tv.setText("ѯ��������:");
				// task_entcode_tv.setText("������λ:");
				// task_dutypeople_tv.setText("��ѯ��������:");
				// task_sitecondition_edt.setVisibility(View.GONE);
				task_site_enfor_wd_layout.setVisibility(View.VISIBLE);
				task_site_enfor_wd_btn.setVisibility(View.VISIBLE);
				task_site_enfor_add_btn.setVisibility(View.VISIBLE);
				xianchangkanchabilu.setVisibility(View.GONE);
			}
			/** ����Ϊ2�ǵ���ѯ�ʱ�¼ */
			else if (codeStr.equals("2")) {
				task_site_scoll_out_jieshou.setVisibility(View.VISIBLE);
				task_site_scoll_out.setVisibility(View.INVISIBLE);
				task_site_scoll_out_jiancha.setVisibility(View.INVISIBLE);
				task_sitcondtion_lay.setVisibility(View.GONE);
				xwbldybj.setVisibility(View.VISIBLE);
				
			}
			/** ����Ϊ3���ֳ���������¼ */
			else if (codeStr.equals("3")) {
				task_site_scoll_out_jiancha.setVisibility(View.VISIBLE);

				task_site_scoll_out.setVisibility(View.INVISIBLE);
				task_site_scoll_out_jieshou.setVisibility(View.INVISIBLE);
				task_name_tv.setText(task_name_tv_jiancha.getText().toString());
			}
			/** ������� */
			clear_data();
			/** ������ҵ����������жϽ���ĳ�ʼ����ѯ */
			if (qyid != null && !qyid.equals("") && rwbh != null && !rwbh.equals("")) {
				/** ��������״̬����ҵ����Ϳ����¼�����жϽ���ĳ�ʼ����ѯ */
				querySteData();
			}

		}

	}

	/** ѡ��ִͬ���˰�ť�����¼� */
	public void together_people_click(View view) {
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
		View v = inflater.inflate(R.layout.enforcementmodel_select_commonpeople, null);
		task_site_enfor_expandable_lv = (ExpandableListView) v.findViewById(R.id.enforcementmodel_select_commonpeople_explistview);
		task_site_enfor_expandable_lv.setGroupIndicator(getResources().getDrawable(R.layout.expandablelistviewselector));
		TextView title_tv = (TextView) v.findViewById(R.id.enforcementmodel_select_commonpeople_tv);
		ArrayList<HashMap<String, Object>> depparentidList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
				+ Global.getGlobalInstance().getUserid() + "'");
		String depparentid = "";
		try {
			depparentid	= depparentidList.get(0).get("depparentid").toString();
			if(TextUtils.isEmpty(depparentid)) {
				Toast.makeText(TaskSiteEnforcementActivity.this,"��ѯ��Աʱ����!��ͬ����Ա��!",Toast.LENGTH_SHORT).show();
				return ;
			}
		} catch (Exception e) {
		}
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();
		if ("1".equals(depparentid)) {
			/** �����û���¼�û���id��ѯ */
			login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from PC_DepartmentInfo where depid = '230000000departA'");	//�Ѿ��Ǻ�����ʡ��������ܶ���Ա,ֻ�ܲ�ѯ�¼�����
		}else {
			/** �����û���¼�û���id��ѯ */
			login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from PC_DepartmentInfo where depid = '"+depparentid+"'");			
		}
		/** ��õ�¼�û������ŵ����� */
		String depParentName = login_user_data.get(0).get("depname").toString();
		/** ��õ�¼�û������ŵ�id */
		String depID = login_user_data.get(0).get("depid").toString();
		/** ���ò������� */
		title_tv.setText(depParentName);
		//task_work_units_edt.setText(depParentName);

		ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("DepParentID", depID);
		conditions.put("syncDataRegionCode", Global.getGlobalInstance().getAreaCode());
		SqliteUtil.getInstance().recursiveQueryOrder("PC_DepartmentInfo", depData, conditions, "depid,depname", false, "depid");
		int index=-1;
		for(HashMap<String, Object> map:depData){
			if(map.get("depname").equals("��˲���"))
				index=depData.indexOf(map);
		}
		if(index!=-1)
			depData.remove(index);
		final List<String> groupList = new ArrayList<String>();
		childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		boolean hasChildData=true;
		for (HashMap<String, Object> map : depData) {
			groupList.add(map.get("depname").toString());
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("DepID", map.get("depid").toString());
			condition.put("syncDataRegionCode", Global.getGlobalInstance().getAreaCode());
			ArrayList<HashMap<String, Object>> usersData = SqliteUtil.getInstance().getList("UserID,U_RealName", condition, "PC_Users");
			if(usersData.isEmpty())
				hasChildData=false;
			childMapData.add(usersData);
		}
		if(!hasChildData){
			Toast.makeText(TaskSiteEnforcementActivity.this,"��Ա��Ϣ������!��ͬ����Ա��!",Toast.LENGTH_SHORT).show();
		}
		StringBuffer stringBuffer = new StringBuffer();
		/** �û�id���� */
		final LinkedList<String> linkedList = new LinkedList<String>();
		/** �û��������� */
		final LinkedList<String> linkedName = new LinkedList<String>();
		/** �����б����������� */
		task_site_enfor_expandable_lv.setAdapter(new CommonLawPeopleAdapter(groupList, childMapData, linkedList, linkedName, TaskSiteEnforcementActivity.this));

		/** �����б������б�ĵ���¼� */
		task_site_enfor_expandable_lv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CheckBox two_class_cb = (CheckBox) v.findViewById(R.id.two_class_cb);

				String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
				if (!userCheckedId.equals(Global.getGlobalInstance().getUserid())) {
					two_class_cb.toggle();
				}
				

				ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
				/** ���ѡ�еļ���˵�ִ��֤�����ݼ��� */
				zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '"
						+ childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() + "'");
				if (zfzhData != null && zfzhData.size() > 0) {
					/** ����˵�ִ��֤�� */
					task_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
				}
				if (childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() != null
						&& !childMapData.get(groupPosition).get(childPosition).get("u_realname").toString().equals("")) {
					/** ���ѡ�е� ����˵Ĳ��� */
					ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
					explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
									+ childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() + "'");
					if (explorData != null && explorData.size() > 0) {
						/** ���ѡ�еļ���˵Ĳ���ID */
						String deptid = explorData.get(0).get("depparentid").toString();
						/** ���ѡ�еļ���˵Ĺ�����λ */
						deptData = sqliteUtil.queryBySqlReturnArrayListHashMap("select depname from PC_DepartmentInfo where depid = '" + deptid + "'");
						if (deptData != null && deptData.size() > 0) {
							task_work_units_edt.setText(deptData.get(0).get("depname").toString());
						}
					}
				}

				recordList = new ArrayList<String>();
				if (exeList != null && exeList.size() > 0) {
					for (int i = 0; i < exeList.size(); i++) {
						String exeStr = exeList.get(i).toString();
						/** ���ִ���� */
						recordList.add(exeStr);
					}
				}

				return false;
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(TaskSiteEnforcementActivity.this);
		builder.setTitle("��ѡ��ִͬ����");
		builder.setIcon(getResources().getDrawable(R.drawable.yutu));
		builder.setView(v);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (linkedList.size() < 2) {
					task_surveypeoplename_btn.setText("");
					recordList = new ArrayList<String>();
					recordList.add("-����ѡ������-");
					recordAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
					recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					task_recordpeopleid_sp.setAdapter(recordAdapter);
					if (recordList != null && recordList.size() > 1) {
						task_recordpeopleid_sp.setSelection(0, true);
					}
					Toast.makeText(TaskSiteEnforcementActivity.this, "�����Լ�����������ѡ��һλִ���ˣ�", Toast.LENGTH_LONG).show();
					return;
				}
				sbZhr = new StringBuffer();
				StringBuffer userName = new StringBuffer();
				recordList = new ArrayList<String>();
				ArrayList<HashMap<String, Object>> zfzhDa = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < linkedList.size(); i++) {
					zfzhDa = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + linkedName.get(i).toString() + "'");
//					String zfzhStr = "";
//					if (zfzhDa != null && zfzhDa.size() > 0 && zfzhDa.get(0) != null && zfzhDa.get(0).get("zfzh") != null) {
//						zfzhStr = zfzhDa.get(0).get("zfzh").toString();
//					}
//					userName.append(linkedName.get(i) + "(" + zfzhStr + ")" + ",");
					userName.append(linkedName.get(i) + ",");
					sbZhr.append(linkedList.get(i) + ",");
					/** ���ѡ�еĿ��������� */
					recordList.add(linkedName.get(i));
				}
				if (sbZhr.length() > 0) {
					sbZhr.deleteCharAt(sbZhr.length() - 1);
				}
				if (userName.length() > 0) {
					userName.deleteCharAt(userName.length() - 1);
				}
				task_surveypeoplename_btn.setText(userName.toString());
				/** ȥ����һ��Ĭ�ϵĵ�ǰ�û��� */
				// recordList.remove(0);

				/** ��¼�˵�SpiNner������ */
				recordAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
				recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				task_recordpeopleid_sp.setAdapter(recordAdapter);
				if (recordList != null && recordList.size() > 1) {
					task_recordpeopleid_sp.setSelection(0, true);
				}
				task_recordpeopleid_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						/** ���ѡ�еļ�¼�˵����� */
						task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString();
						ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
						/** ���ѡ�еļ�¼�˵�ִ��֤�����ݼ��� */
						zfzhData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
						if (zfzhData != null && zfzhData.size() > 0) {
							/** ��¼�˵�ִ��֤�� */
							task_recordpeopleid_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
						}

						if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
							/** ���ѡ�еļ�¼�˵Ĳ��� */
							ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
							explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
									"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
											+ task_recordpeopleid_sp_str + "'");
							if (explorData != null && explorData.size() > 0) {
								/** ���ѡ�еļ�¼�˵Ĳ���ID */
								String deptid = explorData.get(0).get("depparentid").toString();
								/** ���ѡ�еļ�¼�˵Ĺ�����λ */
								deptData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select depname from PC_DepartmentInfo where depid = '" + deptid + "'");
								if (deptData != null && deptData.size() > 0) {
									task_recordpeopleid_work_units_edt.setText(deptData.get(0).get("depname").toString());
								}
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog = builder.create();
		alertDialog.show();
	}
	
	public void onclick_byk_showdialog(View v){
		
		HashMap<String, Object> condition = new HashMap<String, Object>();

		condition.put("RegionCode", Global.getGlobalInstance().getAreaCode());
		final ArrayList<HashMap<String, Object>> selectUsers = SqliteUtil
				.getInstance().getOrderList("PC_Users",
						"UserID,U_RealName,ZFZH", condition, "zw");
		List<String> nameList = new ArrayList<String>();
		List<String> usersidList = new ArrayList<String>();
		List<String> zfzhList = new ArrayList<String>();

		for (int i = 0; i < selectUsers.size(); i++) {
			String name = selectUsers.get(i).get("u_realname").toString();
			String userid = selectUsers.get(i).get("userid").toString();
			
			String zfzh = selectUsers.get(i).get("zfzh").toString();
			nameList.add(name);
			usersidList.add(userid);
			zfzhList.add(zfzh);
		}
		// ������ת������
		final String[] hobbies = nameList.toArray(new String[nameList
				.size()]);
		final String[] hobbies2 = usersidList
				.toArray(new String[usersidList.size()]);
		
		final String[] hobbies3 = zfzhList.toArray(new String[zfzhList.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				TaskSiteEnforcementActivity.this);
		builder.setIcon(R.drawable.yutu);
		builder.setTitle("��ѡ��ִͬ����");



		
		final List<String> selectname=new ArrayList<String>();
		final List<String> selectid=new ArrayList<String>();
		final List<String> selectzh=new ArrayList<String>();
		
		builder.setMultiChoiceItems(hobbies, null,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (isChecked) {
//							sb.append(hobbies[which] + ",");
//							sb2.append(hobbies2[which] + ",");
							
							selectname.add(hobbies[which]);
							selectid.add(hobbies2[which]);
							selectzh.add(hobbies3[which]);
						}else{
							selectname.remove(which);
							selectid.remove(which);
							selectzh.remove(which);
						}
					}
				});
		
		
		final StringBuffer sb = new StringBuffer();
		final StringBuffer sb2 = new StringBuffer();
		final StringBuffer sb3 = new StringBuffer();
		builder.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for (int i = 0; i < selectname.size(); i++) {
							String name = selectname.get(i);
							sb.append(name + ",");
						}
						for (int i = 0; i < selectid.size(); i++) {
							String id = selectid.get(i);
							sb2.append(id + ",");
						}
						for (int i = 0; i < selectzh.size(); i++) {
							String zh = selectzh.get(i);
							sb3.append(zh + ",");
						}
						
						if (sb.length()==0) {
							Toast.makeText(TaskSiteEnforcementActivity.this,
									"������ѡ��һλ�����!", 0).show();
							return;
						}
						sb.deleteCharAt(sb.length() - 1).toString();

					}
				});
		builder.setNegativeButton("ȡ��",
				null);
		builder.show();
		
		
	}
	

	private class MyAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> list;
		private LayoutInflater inflater = null;

		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list, int arg2) {
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.task_site_enforcement_launage_lvitem, null);
				holder.tv = (TextView) convertView.findViewById(R.id.task_site_launage_lv_left_tv);
				holder.tv1 = (TextView) convertView.findViewById(R.id.task_site_launage_lv_right_tv);
				holder.cb = (CheckBox) convertView.findViewById(R.id.checkbox_ischecked_ornot);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String string = (String) list.get(position).get("wtnr");
			holder.tv.setText(string);

			holder.tv.setTag((String) list.get(position).get("id"));
			holder.tv1.setText((String) list.get(position).get("tipinfo"));
			String id = (String) list.get(position).get("id");
			if (wDlinkedList.contains(id)) {
				holder.cb.setChecked(true);
			} else {
				holder.cb.setChecked(false);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv, tv1;
		CheckBox cb;
	}

	private class MyTemplateAdapter extends ArrayAdapter<String> {

		private List<String> list;
		private LayoutInflater inflater = null;

		// private static final int mLayout =
		// android.R.layout.simple_dropdown_item_1line;
		private static final int mLayout = R.layout.xunwen_moban1;

		public MyTemplateAdapter(Context context, List<String> items) {

			super(context, mLayout, items);// �����Ļ���/�����ļ�/��䲼���ļ�����
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(mLayout, null);
			}
			TextView textView1 = (TextView) convertView.findViewById(R.id.ttttext);
			textView1.setSelected(true);
			String item = getItem(position);
			textView1.setText(item);
			return convertView;
		}
	}

	class WDItemClickListener implements OnItemClickListener {

		HashMap<Integer, Integer> wdId = new HashMap<Integer, Integer>();// �洢ѡ��������id

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			ViewHolder holder = (ViewHolder) arg1.getTag();
			holder.cb.toggle();
			String id = holder.tv.getTag().toString();
			if (holder.cb.isChecked() == true) {

				wDlinkedList.add(id);

			} else {
				wDlinkedList.remove(id);

			}
		}
	}

	/** ѡ���������Ͱ�ť�����¼� */
	MyAdapter mAdapter;

	public void question_type(final ArrayList<HashMap<String, Object>> questryAnsData, final AutoCompleteTextView autoTv) {
		ArrayList<String> questryAnsMap = new ArrayList<String>();
		for (int i = 0; i < questryAnsData.size(); i++) {
			questryAnsMap.add(questryAnsData.get(i).get("wfxwlx").toString().trim());
		}

		MyTemplateAdapter myTemplateAdapter = new MyTemplateAdapter(TaskSiteEnforcementActivity.this, questryAnsMap);
		autoTv.setAdapter(myTemplateAdapter);

		// autoTv.setAdapter(questryAnsAdapter);
		/** AutoCompleteTextView autoTv ��ý���ʱ����ֹ���뷨���� */
		autoTv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int inType = autoTv.getInputType(); // backup the input type
				autoTv.setInputType(InputType.TYPE_NULL); // disable soft input
				autoTv.onTouchEvent(event); // call native handler
				autoTv.setInputType(inType); // restore input type
				return true;
			}
		});
		autoTv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				autoTv.setFocusable(false);// ���֮���ֹ��ȡ����
				lanuageData = new ArrayList<HashMap<String, Object>>();
				// lanuageList = new ArrayList<String>();
				/** ��ѯ������ģ�����ݼ��� */
				lanuageData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_WTZD where pid = '" + questryAnsData.get(arg2).get("id").toString() + "'");
				if (lanuageData != null && lanuageData.size() > 0) {

					mAdapter = new MyAdapter(TaskSiteEnforcementActivity.this, lanuageData, arg2);
					task_site_enfor_lv.setAdapter(mAdapter);
				}
			}
		});

		autoTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				autoTv.setText("");
				autoTv.showDropDown();
			}
		});
	}

	/** ����Զ�������¼ */
	public void new_que_and_answers(View view) {

		final LinearLayout wdLayout = getWDView();
		final EditText askTv = (EditText) wdLayout.findViewById(R.id.ywyd_ask);
		askTv.setTextSize(15);
		askTv.setTextColor(android.graphics.Color.BLACK);

		final EditText ansTv = (EditText) wdLayout.findViewById(R.id.ywyd_question);
		ansTv.setTextSize(15);
		ansTv.setTextColor(android.graphics.Color.BLACK);

		task_site_enfor_wd_layout.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
						.setPositiveButton("��", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//if (qyzts.equals("1")) {
								// BYK rwzt
									if (qyzts.equals("3")) {
									Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
									return;
								}

								task_site_enfor_wd_layout.removeView(wdLayout);

								Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("��", null).create().show();
				return true;
			}
		});
		askTv.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {

				new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
						.setPositiveButton("��", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							//	if (qyzts.equals("1")) {
								//BYK rwzt
									if (qyzts.equals("3")) {
									Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
									return;
								}

								task_site_enfor_wd_layout.removeView(wdLayout);
								Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("��", null).create().show();
				return true;
			};
		});
		ansTv.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {

				new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
						.setPositiveButton("��", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
//                            BYK rwzt
								if (qyzts.equals("3")) {
//									if (qyzts.equals("1")) {
									Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
									return;
								}

								task_site_enfor_wd_layout.removeView(wdLayout);
								Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("��", null).create().show();
				return true;
			};
		});
		task_site_enfor_wd_layout.addView(wdLayout);
	}

	/** ѡ���ʴ�ť�����¼� */
	public void add_que_and_answers(View view) {
		LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
		View v = inflater.inflate(R.layout.task_site_joint_law_enforcement, null);
		task_site_enfor_lv = (ListView) v.findViewById(R.id.task_site_enfor_lv);
		autoCompleteTv = (CustomAutoCompleteTextView) v.findViewById(R.id.task_site_joint_autotv);

		// autoSp = (Spinner) v.findViewById(R.id.task_site_joint_autosp);
		task_site_joint_frlayout = (FrameLayout) v.findViewById(R.id.task_site_joint_frlayout);
		task_site_joint_frlayout.setVisibility(View.VISIBLE);

		ArrayList<HashMap<String, Object>> questryAnsData = new ArrayList<HashMap<String, Object>>();
		questryAnsData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_WFXW order by DisplayOrder");
		if (questryAnsData != null && questryAnsData.size() > 0) {

			question_type(questryAnsData, autoCompleteTv);

		}

		task_site_enfor_lv.setOnItemClickListener(new WDItemClickListener());

		AlertDialog.Builder builder = new AlertDialog.Builder(TaskSiteEnforcementActivity.this);
		builder.setTitle("ѡ��ѯ�ʱ�¼ģ��");
		builder.setIcon(getResources().getDrawable(R.drawable.yutu));
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (final String id : wDlinkedList) {
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("id", id);
					HashMap<String, Object> map = SqliteUtil.getInstance().getList("T_ZFWS_WTZD", conditions).get(0);
					final LinearLayout wdLayout = getWDView();
					final EditText askTv = (EditText) wdLayout.findViewById(R.id.ywyd_ask);
					askTv.setTextSize(15);
					askTv.setText(map.get("wtnr").toString());
					// askTv.setFocusable(false);
					askTv.setTextColor(android.graphics.Color.BLACK);

					final EditText ansTv = (EditText) wdLayout.findViewById(R.id.ywyd_question);
					ansTv.setTextSize(15);
					ansTv.setTextColor(android.graphics.Color.BLACK);
					ansTv.setHint(map.get("tipinfo").toString());

					task_site_enfor_wd_layout.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {

							new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
									.setPositiveButton("��", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											//BYK rwzt
											if (qyzts.equals("3")) {
												//if (qyzts.equals("1")) {
												Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
												return;
											}

											task_site_enfor_wd_layout.removeView(wdLayout);

											Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("��", null).create().show();
							return true;
						}
					});
					askTv.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {

							new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
									.setPositiveButton("��", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											//if (qyzts.equals("1")) {
											//BYK rwzt	
											if (qyzts.equals("1")) {
												Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
												return;
											}

											task_site_enfor_wd_layout.removeView(wdLayout);
											Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("��", null).create().show();
							return true;
						};
					});
					ansTv.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {

							new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("��ʾ��Ϣ").setMessage("�Ƿ�ɾ�������ʴ��¼")
									.setPositiveButton("��", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
										//	if (qyzts.equals("1")) {
											// BYK  rwzt
											if (qyzts.equals("3")) {
												Toast.makeText(TaskSiteEnforcementActivity.this, "��ȡ֤��ҵ����ɾ����¼", Toast.LENGTH_SHORT).show();
												return;
											}

											task_site_enfor_wd_layout.removeView(wdLayout);
											Toast.makeText(TaskSiteEnforcementActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("��", null).create().show();
							return true;
						};
					});
					task_site_enfor_wd_layout.addView(wdLayout);
				}
				wDlinkedList.clear();

			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.cancel();
			}
		});
		builder.setView(v);

		alertDialog = builder.create();

		alertDialog.show();
		alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	/** ����ģ�����ݼ��� */
	private ArrayList<String> lanuageList;
	/** ���ݿ��ѯ������ģ�����ݼ��� */
	private ArrayList<HashMap<String, Object>> lanuageData;

	/** ѡ������ģ��ListVIew��ʽ */
	public void language_lv_temp() {

		/** �ֳ��������򳤰��¼� */
		task_sitecondition_edt.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {

				LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
				View v = inflater.inflate(R.layout.task_site_joint_law_enforcement, null);
				task_site_enfor_lv = (ListView) v.findViewById(R.id.task_site_enfor_lv);
				/** ���ݱ������Ʋ�ѯ����ģ�� */
				if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {
					lanuageData = new ArrayList<HashMap<String, Object>>();
					lanuageList = new ArrayList<String>();
					/** ��ѯ������ģ�����ݼ��� */
					lanuageData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from T_ZFWS_XWJLWD");
					if (lanuageData != null && lanuageData.size() > 0) {
						SimpleAdapter lanuageAdapter = new SimpleAdapter(TaskSiteEnforcementActivity.this, lanuageData, R.layout.task_site_enforcement_launage_lvitem,
								new String[] { "wtnr", "result" }, new int[] { R.id.task_site_launage_lv_left_tv, R.id.task_site_launage_lv_right_tv });
						task_site_enfor_lv.setAdapter(lanuageAdapter);
					}

					task_site_enfor_lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							/** ���������е����� */
							task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();
							/** ����ģ���ۼ� */
							String task_site_enfors = "";
							/** �������ģ��ѡ��������� */
							task_site_enfors += lanuageData.get(arg2).get("wtnr").toString() + "\n��: " + lanuageData.get(arg2).get("result").toString();
							if (!task_site_enfors.equals("")) {
								if (task_sitecondition_edt_str != null && !task_sitecondition_edt_str.equals("")) {
									/** ����������е����� */
									task_sitecondition_edt.setText(task_sitecondition_edt_str + "," + task_site_enfors);
								} else {
									/** ����������е����� */
									task_sitecondition_edt.setText(task_site_enfors);
								}
							}

							if (!task_site_enfors.equals("")) {
								alertDialog.cancel();
							}
						}
					});
					
					AlertDialog.Builder builder = new AlertDialog.Builder(TaskSiteEnforcementActivity.this);
					builder.setTitle("��ѡ������ģ��");
					builder.setIcon(getResources().getDrawable(R.drawable.yutu));
					builder.setView(v);
					alertDialog = builder.create();
					alertDialog.show();

				} else if (task_name_tv.getText().toString().trim().equals("�ֳ���飨���죩��¼")) {/*
					lanuageData = new ArrayList<HashMap<String, Object>>();
					lanuageList = new ArrayList<String>();
					*//** ��ѯ������ģ�����ݼ��� *//*
					lanuageData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from T_YDZF_BLCreateTemplate where templatetype = 'XCQK'");
					if (lanuageData != null && lanuageData.size() > 0) {
						for (int i = 0; i < lanuageData.size(); i++) {
							String lanuageStr = lanuageData.get(i).get("templatecontent").toString();
							lanuageList.add(lanuageStr);
						}
					}
					*//** ����ģ�������� *//*
					ArrayAdapter<String> lanuageAdapter = null;
					lanuageAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_list_item_checked, lanuageList);

					task_site_enfor_lv.setAdapter(lanuageAdapter);

					task_site_enfor_lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							*//** ���������е����� *//*
							task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();
							*//** ����ģ���ۼ� *//*
							String task_site_enfors = "";
							*//** �������ģ��ѡ��������� *//*
							task_site_enfors += lanuageList.get(arg2).toString();
							if (!task_site_enfors.equals("")) {
								if (task_sitecondition_edt_str != null && !task_sitecondition_edt_str.equals("")) {
									*//** ����������е����� *//*
									task_sitecondition_edt.setText(task_sitecondition_edt_str + "," + task_site_enfors);
								} else {
									*//** ����������е����� *//*
									task_sitecondition_edt.setText(task_site_enfors);
								}
							}

							if (!task_site_enfors.equals("")) {
								alertDialog.cancel();
							}
						}
					});
				*/}

		

				return false;
			}
		});
	}

	/** ѡ������ģ��Spinner��ʽ */
	public void language_Spinner_temp() {
		/** �ֳ��������򳤰��¼� */
		task_sitecondition_edt.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {

				LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
				View v = inflater.inflate(R.layout.task_site_enforcement_template, null);
				task_site_enfor_sp = (Spinner) v.findViewById(R.id.task_site_enfor_sp);
				/** Spinner���ݼ��� */
				ArrayList<String> lanuageList = new ArrayList<String>();
				lanuageList.add("-��ѡ������ģ��-");
				ArrayList<HashMap<String, Object>> lanuageData = new ArrayList<HashMap<String, Object>>();
				/** ��ѯ������ģ�����ݼ��� */
				lanuageData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from T_YDZF_BLCreateTemplate");
				if (lanuageData != null && lanuageData.size() > 0) {
					for (int i = 0; i < lanuageData.size(); i++) {
						String lanuageStr = lanuageData.get(i).get("templatecontent").toString();
						lanuageList.add(lanuageStr);
					}
				}
				/** ����ģ�������� */
				ArrayAdapter<String> lanuageAdapter = null;
				lanuageAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_spinner_item, lanuageList);
				lanuageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				task_site_enfor_sp.setAdapter(lanuageAdapter);
				task_site_enfor_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						/** ���������е����� */
						task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();
						String task_site_enfors = "";
						/** �������ģ��ѡ��������� */
						task_site_enfors += task_site_enfor_sp.getSelectedItem().toString();
						if (!task_site_enfors.equals("-��ѡ������ģ��-")) {
							if (task_sitecondition_edt_str != null && !task_sitecondition_edt_str.equals("")) {
								/** ����������е����� */
								task_sitecondition_edt.setText(task_sitecondition_edt_str + "," + task_site_enfors);
							} else {
								/** ����������е����� */
								task_sitecondition_edt.setText(task_site_enfors);
							}
						}

						if (!task_site_enfors.equals("-��ѡ������ģ��-")) {
							alertDialog.cancel();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

				AlertDialog.Builder builder = new AlertDialog.Builder(TaskSiteEnforcementActivity.this);
				builder.setTitle("��ѡ������ģ��");
				builder.setIcon(getResources().getDrawable(R.drawable.yutu));
				builder.setView(v);
				alertDialog = builder.create();
				alertDialog.show();

				return false;
			}
		});
	}

	/** ������ҵ����Ϳ����¼�����жϽ���ĳ�ʼ����ѯ */
	public void querySteData() {

		/** ˢ�¼�¼������������ */
		if (recordList != null && recordList.size() > 0) {
			recordList.clear();
			if (recordAdapter != null) {
				recordAdapter.notifyDataSetChanged();
			}
		}

		progressDialog = new ProgressDialog(TaskSiteEnforcementActivity.this);
		progressDialog.setMessage("����Ŭ��������...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!task_name_tv.getText().toString().trim().equals("�ֳ�����¼��")) {
					/** �ж�����ѡ������� */
					clericalVoid();
				}
				if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {
					/** ���������ź���ҵ�����ѯ��ѯ�ʱ�¼���ж�Ӧ�����ݼ��� */
					String sql = "select * from T_ZFWS_XWBL where taskid = '" + rwbh + "' and surveyentcode = '" + qyid + "' and type = '" + task_type_edt_str + "' and guid = '"+guid+"'";
					queryData = new ArrayList<HashMap<String, Object>>();
					queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);
				} else if (task_name_tv.getText().toString().trim().equals("�ֳ���飨���죩��¼")) {
					/** ���������ź���ҵ�����ѯ�������¼���ж�Ӧ�����ݼ��� */
					String sql = "select * from T_ZFWS_KCBL where taskid = '" + rwbh + "' and entcode = '" + qyid + "' and type = '" + task_type_edt_str + "'";
					queryData = new ArrayList<HashMap<String, Object>>();
					queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);
				} else if (task_name_tv.getText().toString().trim().equals("�������֪ͨ��")) {
					String sql = "select * from Survey_JSTZS where taskid = '" + rwbh + "' and entid = '" + qyid + "'";
					queryData = new ArrayList<HashMap<String, Object>>();
					queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);

					if (qyid != null && !qyid.equals("")) {
						/** ��ѯ��ҵ������Ϣ�����ݼ��� */
						queryEntData = new ArrayList<HashMap<String, Object>>();
						queryEntData = sqliteUtil
								.queryBySqlReturnArrayListHashMap("select qydz,qymc,frdb,yyzzdm,zzjgdn,frdbdh,yzbm  from T_WRY_QYJBXX where guid = '" + qyid + "'");
					}

					Message msg = handler.obtainMessage();
					msg.arg1 = DATA_QUERY_SUCCESS;
					handler.sendMessage(msg);
					return;
				} else if (task_name_tv.getText().toString().trim().equals("�ֳ�����¼��")) {
					String sql = "select Guid,TaskId,EntId,SCQK_SCZK,SCQK_YLYL,SCQK_CPCL,JFQK_FS,JFQK_FQ,JFQK_ZS,JFQK_FZ,HBZD_WSPJS,HBZD_WAQYS"
							+ ",WRZL_FS,WRZL_FQ,WRZL_WRW,WRZL_GTFW,WRZL_ZS,WRZL_XWS,WRW_FS_PFQX,WRW_FS_YZND,WRW_FS_PFBZ,WRW_FS_CBYY,WRW_FS_ZXJC"
							+ ",WRW_FS_CBYY1,WRW_FS_JDJC,WRW_FS_ZXJCCB,WRW_FQ_KHSD,WRW_FQ_YZND,WRW_FQ_PFBZ,WRW_FQ_CBYY,WRW_FQ_ZXJC,WRW_FQ_CBYY1"
							+ ",WRW_FQ_JDJC,WRW_FQ_ZXJCCB,WRW_GTFW_FWCZ,WRW_GTFW_FWZC,WRW_GTFW_FWZY,WRW_ZS_GNQY,WRW_ZS_PFBZ,WRW_ZS_JCCB,WRW_ZS_CBYY"
							+ ",CZHJWT,XCJCJG,JCRZFZH,BDCDWQZ,strftime('%Y-%m-%d %H:%M:%S',QZRQ) as QZRQ,strftime('%Y-%m-%d %H:%M:%S',Updatetime) as Updatetime from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
							+ rwbh + "' and entid = '" + qyid + "'";
					queryData = new ArrayList<HashMap<String, Object>>();
					queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);

					if (qyid != null && !qyid.equals("")) {
						/** ��ѯ��ҵ������Ϣ�����ݼ��� */
						queryEntData = new ArrayList<HashMap<String, Object>>();
						queryEntData = sqliteUtil
								.queryBySqlReturnArrayListHashMap("select qydz,qymc,frdb,yyzzdm,zzjgdn,frdbdh,yzbm,hylb,frdb,hblxr,hblxrdh,zdwry,wrylb  from T_WRY_QYJBXX where guid = '"
										+ qyid + "'");
					}

					Message msg = handler.obtainMessage();
					msg.arg1 = DATA_QUERY_SUCCESS;
					handler.sendMessage(msg);
					return;
				}

				if (qyid != null && !qyid.equals("")) {
					/** ��ѯ��ҵ������Ϣ�����ݼ��� */
					queryEntData = new ArrayList<HashMap<String, Object>>();
					queryEntData = sqliteUtil.queryBySqlReturnArrayListHashMap("select qydz,qymc,frdb,yyzzdm,zzjgdn,frdbdh,yzbm from T_WRY_QYJBXX where guid = '" + qyid + "'");
				}

				if (rwbh != null && !rwbh.equals("")) {
					/** ��ѯִ���˵����ݼ��� */
					queryExeData = new ArrayList<HashMap<String, Object>>();
					queryExeData = sqliteUtil
							.queryBySqlReturnArrayListHashMap("select u_realname from T_YDZF_RWXX_USER LEFT JOIN PC_Users ON T_YDZF_RWXX_USER.userid = PC_Users.userid  where rwxxbh = '"
									+ rwbh + "'");

					if (queryExeData != null && queryExeData.size() > 0) {
						exeList = new ArrayList<String>();
						for (HashMap<String, Object> map : queryExeData) {
							String exeName = map.get("u_realname").toString();
							exeList.add(exeName);
						}
					}

					/** ��ѯ��������ݼ��� */
					queryProblemData = new ArrayList<HashMap<String, Object>>();
					queryProblemData = sqliteUtil.queryBySqlReturnArrayListHashMap("select id,taskid,entid,result,wtnr from T_ZFWS_XWJLWD where taskid = '" + rwbh
							+ "' and entid = '" + qyid + "'and billid = '"+guid+"'");
				}

				Message msg = handler.obtainMessage();
				msg.arg1 = DATA_QUERY_SUCCESS;
				handler.sendMessage(msg);
			}
		}).start();
	}
	/**
	 * ѯ�ʱ�¼-��ѯ��������
	 * */
	private EditText task_askedpeoplename_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ�����Ա�
	 * */
	private RadioGroup task_askedpeoplesex_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ��������
	 * */
	private EditText task_askedpeopleage_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ�������֤��
	 * */
	private EditText task_askedpeopleidnumber_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ���˹�����λ
	 * */
	private EditText task_askedpeopleunit_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ����ְ��
	 * */
//	private EditText task_dutypeopleoffice_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ���˵绰
	 * */
//	private EditText task_dutypeopletel_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ���˵�ַ
	 * */
//	private EditText task_natruepeople_edt;
	/**
	 * ѯ�ʱ�¼-��ѯ�����ʱ�
	 * */
	private EditText task_askedpeoplepostcode_edt;
	/** ���ݲ�ѯ�ɹ������� */
	public void bind_data() {
		String xwrxmStr = "";
		if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {
			
			String tostring = queryData.get(0).get("surveypeoplecradcode").toString();
			if (tostring != null) {
				String[] surveyStr2 = tostring.split(",");
				StringBuilder xwrxmStrB = new StringBuilder();
				for (String str : surveyStr2) {
					if (str != null && str.trim().length() > 0) {
						String[] s = str.toString().split("\\(");
						xwrxmStrB.append(s[0]);
						xwrxmStrB.append(",");
					}
				}
				String xwrxmStrBStr = xwrxmStrB.toString();
				// String xwrxmStr = "";
				if (xwrxmStrBStr != null && !xwrxmStrBStr.equals("")) {
					xwrxmStr = xwrxmStrBStr.substring(0, xwrxmStrBStr.length() - 1);
				}
			}
			
			
			button_xunwen.setText(queryData.get(0).get("askedpeoplesex").toString());
			button_ziranren.setText(queryData.get(0).get("otherpeoplesex").toString());
			String enName = queryData.get(0).get("surveyentprisename").toString();
			// String dutyName =
			// queryData.get(0).get("dutypeopleentprisename").toString();
			task_entcode_edt.setText(queryData.get(0).get("dutypeopleentprisename").toString());// ��ȾԴ����(�����ҵ����)
			task_dutypeopleoffice_edt.setText(queryData.get(0).get("askedpeopleduty").toString()); // ��ѯ����ְ��
			task_surveyaddress_edt.setText(queryData.get(0).get("surveyaddress").toString());// �����ַ

			task_company_address_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());
			task_checkpeople_edt.setText(queryData.get(0).get("dutypeoplename").toString());// ���˴���������ְ��
			// task_checkpeople_position_edt.setText("");// �����ְ��
			task_checkpeople_phone_edt.setText(queryData.get(0).get("dutypeopletel").toString());// ���˴���绰
			task_checkpeople_zip_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());// ��������
			task_business_license_edt.setText(queryData.get(0).get("yyzzzch").toString());// Ӫҵִ��ע���
			task_organization_edt.setText(queryData.get(0).get("zzjgdm").toString());// ��֯��������
			task_natruepeople_age_edt.setText(queryData.get(0).get("otherpeopleage").toString());// ��ѯ����Ȼ�˵�����
			task_natruepeopleoffice_edt.setText(queryData.get(0).get("otherpeopleentprisename").toString());// ��ѯ����Ȼ�˵�ְ��
			task_natruepeopletel_edt.setText(queryData.get(0).get("otherpeopletel").toString());// ��ѯ����Ȼ�˵ĵ绰
			task_natruepeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// ��ѯ����Ȼ�˵�סַ
			task_otherpeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// �����μ���Ա�������͵�ַ

			task_dutypeopletel_edt.setText(queryData.get(0).get("askedpeopletel").toString());// ��ѯ���˵ĵ绰
			task_dutypeople_age_edt.setText(queryData.get(0).get("askedpeopleage").toString());// ��ѯ���˵�����
			task_natruepeople_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());// ��ѯ����Ȼ�˵�����

			task_surveypeoplename_btn.setText(xwrxmStr);

			start_time_btn.setText(queryData.get(0).get("surveystartdate").toString());// ��ʼʱ��
			end_time_btn.setText(queryData.get(0).get("surveyenddate").toString());// ����ʱ��
			task_dutypeoplerelation_edt.setText(queryData.get(0).get("otherpeoplerelation").toString());// �뱾����ϵ
			task_type_edt.setText(queryData.get(0).get("type").toString());// ����
			if("".equals(queryData.get(0).get("recordpeopleunit").toString()) || null == queryData.get(0).get("recordpeopleunit").toString()){
				task_work_units_edt.setText(Global.getGlobalInstance().getUserUnit());
				if("NULL".equals(Global.getGlobalInstance().getUserUnit())){
					task_work_units_edt.setText("");
				}
			}else {
				task_work_units_edt.setText(queryData.get(0).get("recordpeopleunit").toString());
			}
			
			task_askedpeoplename_edt.setText(queryData.get(0).get("askedpeoplename").toString());
			task_askedpeopleage_edt.setText(queryData.get(0).get("askedpeopleage").toString());
			task_askedpeopleidnumber_edt.setText(queryData.get(0).get("askedpeopleidnumber").toString());
			task_askedpeopleunit_edt.setText(queryData.get(0).get("surveyentprisename").toString());
			task_askedpeoplepostcode_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());
			String sex = queryData.get(0).get("askedpeoplesex").toString()+"";
			if ("��".equals(sex)) {
				((RadioButton)findViewById(R.id.rb_boy)).setChecked(true);
			}else if ("Ů".equals(sex)) {
				((RadioButton)findViewById(R.id.rb_gril)).setChecked(true);
			}
		} else if (task_name_tv.getText().toString().trim().equals("�ֳ���飨���죩��¼")) {
			task_dutypeople_edt.setText(queryData.get(0).get("dutypeople").toString());// �ֳ�������
			task_dutypeopleoffice_edt.setText(queryData.get(0).get("dutypeopleoffice").toString());// �ֳ�������ְ��
			task_dutypeopletel_edt.setText(queryData.get(0).get("surveypeoplecradcode").toString());// �ֳ������˵绰
			task_sitecondition_edt.setText(queryData.get(0).get("sitecondition").toString());// �ֳ����

			String surveypeoplenameStr = queryData.get(0).get("surveypeoplename").toString();
			
			String[] surveyStr = surveypeoplenameStr.split(",");
			StringBuilder xwrxmStrB = new StringBuilder();
			for (String str : surveyStr) {
				if (str != null && str.trim().length() > 0) {
					xwrxmStrB.append(str);
					xwrxmStrB.append(",");
				}

			}
			String xwrxmStrBStr = xwrxmStrB.toString();
			
			if (xwrxmStrBStr != null && !xwrxmStrBStr.equals("")) {
				xwrxmStr = xwrxmStrBStr.substring(0, xwrxmStrBStr.length() - 1);
			}

			task_surveypeoplename_btn.setText(xwrxmStr);// ��ü���˵�����
			ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
			/** ��ü���˵�ִ��֤�����ݼ��� */
			zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + queryData.get(0).get("surveypeoplename").toString() + "'");
			if (zfzhData != null && zfzhData.size() > 0) {
				/** ����˵�ִ��֤�� */
				task_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
			}
			if (queryData.get(0).get("surveypeoplename").toString() != null && !queryData.get(0).get("surveypeoplename").toString().equals("")) {
				/** ���ѡ�е� ����˵Ĳ��� */
				ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
				explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
								+ queryData.get(0).get("surveypeoplename").toString() + "'");
				if (explorData != null && explorData.size() > 0) {
					/** ���ѡ�еļ���˵Ĳ���ID */
					String deptid = explorData.get(0).get("depparentid").toString();
					/** ���ѡ�еļ���˵Ĺ�����λ */
					deptData = sqliteUtil.queryBySqlReturnArrayListHashMap("select depname from PC_DepartmentInfo where depid = '" + deptid + "'");
					if (deptData != null && deptData.size() > 0) {
						task_work_units_edt.setText(deptData.get(0).get("depname").toString());
					}
				}
			}
			
			String enName = rwxx.getQYMC_form_GUID(qyid);
			String dutyName = queryData.get(0).get("recordpeopleunit").toString();
			task_entcode_edt.setText(!"".equals(dutyName) ? dutyName : enName);// ��ȾԴ����(�����ҵ����)

			task_company_address_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());// �����ַ

			task_surveyaddress_edt.setText(queryData.get(0).get("surveryaddress").toString());

			task_checkpeople_edt.setText(queryData.get(0).get("checkpeople").toString());// ���˴���������ְ��
		
			task_checkpeople_phone_edt.setText(queryData.get(0).get("dutypeopletel").toString());// ���˴���绰
			task_checkpeople_zip_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());// ��������
			task_business_license_edt.setText(queryData.get(0).get("yyzzzch").toString());// Ӫҵִ��ע���
			task_organization_edt.setText(queryData.get(0).get("zzjgdm").toString());// ��֯��������

			start_time_btn.setText(queryData.get(0).get("surveystartdate").toString());// ��ʼʱ��
			end_time_btn.setText(queryData.get(0).get("surveyenddate").toString());// ����ʱ��
			task_dutypeoplerelation_edt.setText(queryData.get(0).get("dutypeoplerelation").toString());// �뱾����ϵ
			task_otherpeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// �����μ���Ա�������͵�ַ
			task_type_edt.setText(queryData.get(0).get("type").toString());// ����
//			task_work_units_edt.setText(queryData.get(0).get("surveypeopleunit").toString());//��¼�˹�����λ
			try {
				beijiancharen.setText(queryData.get(0).get("checkpeople").toString());
			} catch (Exception e) {
				beijiancharen.setText("");
			}
//			farendaibiao.setText(queryData.get(0).get("checkpeople").toString());
 			xianchangfuzeren.setText(queryData.get(0).get("dutypeople").toString());
			
			shengfenzheng.setText(queryData.get(0).get("dutypeoplecode").toString());
			gongzuodanwei.setText(queryData.get(0).get("dutypeopledepartment").toString());
			zhiwu.setText(queryData.get(0).get("dutypeopleoffice").toString());
			yubenanguanxi.setText(queryData.get(0).get("dutypeoplerelation").toString());
			dizhi.setText(queryData.get(0).get("dutypeopleaddress").toString());
			dianhua.setText(queryData.get(0).get("dutypeopletel").toString());
			qitacanjiaren.setText(queryData.get(0).get("otherpeopleaddress").toString());
			

		} else if (task_name_tv.getText().toString().trim().equals("�������֪ͨ��")) {
			// ��Щ����Ӧ��д��һ��hashMap�У�ʹ��Key����Ӧֵ������С�������
			Υ����Ϊ.setText(queryData.get(0).get("breakbehavior").toString());
			String surveytimestr = queryData.get(0).get("surveytime").toString();
			if (surveytimestr.contains("T")) {
				String values = (String) surveytimestr.subSequence(0, surveytimestr.length() - 6);
				surveytimestr = values.replace("T", " ");
			}
			���ܵ���ʱ��.setText(surveytimestr);
			���ܵ���ʱ��.setTag(surveytimestr);
			Я������.setText(queryData.get(0).get("takematerial").toString());
			�ֳ������.setText(queryData.get(0).get("contactperson").toString());
			�ֳ��������ϵ�绰.setText(queryData.get(0).get("phone").toString());
			�ֳ��������ϵ��ַ.setText(queryData.get(0).get("address").toString());

			֪ͨ����.setText(queryData.get(0).get("noticeno").toString());
			֪ͨ����.setTag(queryData.get(0).get("guid").toString());
			���鵥λ��ַ.setText(queryData.get(0).get("bjcdz").toString());
			�ֳ�������.setText(queryData.get(0).get("xcjsr").toString());
			��������ϵ�绰.setText(queryData.get(0).get("bjclxdh").toString());
			�־�.setText(queryData.get(0).get("fj").toString());
			����.setText(queryData.get(0).get("ks").toString());
			��������.setText(queryData.get(0).get("qtzl").toString());
			Υ������.setText(queryData.get(0).get("wffl").toString());
			���鵥λ.setText(queryData.get(0).get("entname").toString());
			return;
		}

		recordList = new ArrayList<String>();
		String recordpeoplename = queryData.get(0).get("recordpeoplename").toString();
		String[] surveyStr = null;
		if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {
//			surveyStr = queryData.get(0).get("surveypeoplename").toString().split(",");
			surveyStr = xwrxmStr.split(",");
		} else {
			surveyStr = queryData.get(0).get("surveypeoplename").toString().split(",");
		}

		int integer = 0;
		if (surveyStr != null && surveyStr.length > 0) {
//			surveyStr = xwrxmStr.split(",");
			if (surveyStr[0] != null && surveyStr[0].trim().length() > 0) {

			} else {
				recordList.add("-����ѡ������-");
			}
		} else {
			recordList.add("-����ѡ������-");
		}
		for (int i = 0; i < surveyStr.length; i++) {
			if (surveyStr[i] != null && surveyStr[i].trim().length() > 0) {
				recordList.add(surveyStr[i].trim());
				if (surveyStr[i].trim().equals(recordpeoplename)) {
					integer = i;
				}
			}
		}

		recordAdapter = new ArrayAdapter<String>(TaskSiteEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
		recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		task_recordpeopleid_sp.setAdapter(recordAdapter);
		task_recordpeopleid_sp.setSelection(integer, true);
		task_recordpeopleid_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/** ���ѡ�еļ�¼�˵����� */
				task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString();
				ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
				/** ���ѡ�еļ�¼�˵�ִ��֤�����ݼ��� */
				zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
				if (zfzhData != null && zfzhData.size() > 0) {
					/** ��¼�˵�ִ��֤�� */
					task_recordpeopleid_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
				}

				if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
					/** ���ѡ�еļ�¼�˵Ĳ��� */
					ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
					explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
									+ task_recordpeopleid_sp_str + "'");
					if (explorData != null && explorData.size() > 0) {
						/** ���ѡ�еļ�¼�˵Ĳ���ID */
						String deptid = explorData.get(0).get("depparentid").toString();
						/** ���ѡ�еļ�¼�˵Ĺ�����λ */
						deptData = sqliteUtil.queryBySqlReturnArrayListHashMap("select depname from PC_DepartmentInfo where depid = '" + deptid + "'");
						if (deptData != null && deptData.size() > 0) {
							task_recordpeopleid_work_units_edt.setText(deptData.get(0).get("depname").toString());
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}
    private String qymc = "";
	/** ��ʼ��ʱ�䡢����ҵ���ƺ�ִ���˵�������� */
	public void bind_EntAndExeData() {

		if (queryEntData != null && queryEntData.size() > 0) {
			// task_entcode_edt.setText(queryEntData.get(0).get("dutypeopleentprisename").toString());//
			// ��ȾԴ����(�����ҵ����)
			qymc = queryEntData.get(0).get("qymc").toString();
			task_entcode_edt.setText(qymc);
			task_checkpeople_edt.setText(queryEntData.get(0).get("frdb").toString());// ���˴���
			/*
			 * task_checkpeople_phone_edt.setText(queryEntData.get(0)
			 * .get("frdbdh").toString());// ���˴���绰
			 */// task_checkpeople_phone_edt.setTextColor(color.gray);
			task_checkpeople_zip_edt.setText(queryEntData.get(0).get("yzbm").toString());// ��������
			// task_checkpeople_zip_edt.setTextColor(color.gray);
			task_business_license_edt.setText(queryEntData.get(0).get("yyzzdm").toString());// Ӫҵִ��ע���
			// task_business_license_edt.setTextColor(color.gray);
			task_organization_edt.setText(queryEntData.get(0).get("zzjgdn").toString());// ��֯��������
			// task_organization_edt.setTextColor(color.gray);
		}

		/** ȷ������ */
		task_confirm_body_edt.setText("������" + Global.getGlobalInstance().getUserUnit()
				+ "��ִ����Ա,�������ǵ�ִ��֤��,���Ŀȷ�ϡ����������������м�鲢�˽��й����,��Ӧ����ϵ���,��ʵ�ش�ѯ�ʺ��ṩ����,���þܾ����谭�����������ṩ���������������Ϊ�����밸��������ϵ,����Ӱ�칫���참,�����������ǻر�,��˵�����ɡ��������?");

		/** ����Ĭ��ʱ��Ϊ��ǰʱ�� */
		start_time_btn.setText(currtime);
		Calendar calendar = Calendar.getInstance();
		int mHour = calendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = calendar.get(Calendar.MINUTE);
		if (mMinute + 30 >= 60) {
			mMinute = mMinute + 30 - 60;
			mHour = mHour + 1;
		} else {
			mMinute = mMinute + 30;
		}
		end_time_btn.setText(datefmtDate.format(dateAndTime.getTime()) + " " + mHour + ":" + mMinute + ":00");
		// Calendar curr = Calendar.getInstance();
		// curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) +
		// 10);
		���ܵ���ʱ��.setText(Timeuitl.getsystemtine());
	}

	/** ��ȡ��������� */
	public void getData() {
		task_companyaddress_edt_str = task_company_address_edt.getText().toString().trim();// �����ַ
		// task_checkpeople_position_edt.setText("");//
		// �����ְ��task_surveyaddress_edt

		task_dutypeople_age_edt_str = task_dutypeople_age_edt.getText().toString().trim();// ��ѯ���˵�����

		task_natruepeople_edt_str = task_natruepeople_edt.getText().toString().trim();// ��ѯ����Ȼ�˵�����
		task_natruepeople_age_edt_str = task_natruepeople_age_edt.getText().toString().trim();// ��ѯ����Ȼ������
		task_natruepeopleoffice_edt_str = task_natruepeopleoffice_edt.getText().toString().trim();// ��ѯ����Ȼ��ְ��
		task_natruepeopletel_edt_str = task_natruepeopletel_edt.getText().toString().trim();// ��ѯ����Ȼ�˵绰
		task_natruepeopleaddress_edt_str = task_natruepeopleaddress_edt.getText().toString().trim();// ��ѯ����Ȼ�˵�ַ

		task_surveyaddress_edt_str = task_surveyaddress_edt.getText().toString().trim();
		task_checkpeople_phone_edt_str = task_checkpeople_phone_edt.getText().toString().trim();// ���˴���绰
		task_checkpeople_zip_edt_str = task_checkpeople_zip_edt.getText().toString().trim();// ��������
		task_business_license_edt_str = task_business_license_edt.getText().toString().trim();// Ӫҵִ��ע���
		task_organization_edt_str = task_organization_edt.getText().toString().trim();// ��֯��������

		task_entcode_edt_str = task_entcode_edt.getText().toString().trim();// ��ȾԴ(��ҵ)����
		task_enforcement_certificate_no_edt_str = task_enforcement_certificate_no_edt.getText().toString().trim();// �����ִ��֤��
		task_work_units_edt_str = task_work_units_edt.getText().toString().trim();// ����˵Ĺ�����λ
		//�޸ĵ�λ byk   Global.getGlobalInstance().getUserUnit()  ΪNULL
	//	task_work_units_edt_str = Global.getGlobalInstance().getUserUnit();
		task_recordpeopleid_enforcement_certificate_no_edt_str = task_recordpeopleid_enforcement_certificate_no_edt.getText().toString().trim();// ��¼��ִ��֤��
		
//		task_recordpeopleid_work_units_edt_str = task_recordpeopleid_work_units_edt.getText().toString();// ��¼�˵Ĺ�����λ
		//�޸�ѯ�ʱ�¼��ͷ ***
		task_recordpeopleid_work_units_edt_str = task_work_units_edt.getText().toString().trim();
	//	task_recordpeopleid_work_units_edt_str = Global.getGlobalInstance().getUserUnit();
		
		task_checkpeople_edt_str = task_checkpeople_edt.getText().toString().trim();// ���˴���
		task_dutypeople_edt_str = task_dutypeople_edt.getText().toString().trim();// �ֳ�������
		task_dutypeopleoffice_edt_str = task_dutypeopleoffice_edt.getText().toString().trim();// �ֳ�������ְ��
		task_dutypeoplerelation_edt_str = task_dutypeoplerelation_edt.getText().toString().trim();// �ֳ��������뱾����ϵ
		task_dutypeopletel_edt_str = task_dutypeopletel_edt.getText().toString().trim();// �ֳ������˵ĵ绰
		task_otherpeopleaddress_edt_str = task_otherpeopleaddress_edt.getText().toString().trim();// �����μ���Ա��������������λ
		task_confirm_body_edt_str = task_confirm_body_edt.getText().toString().trim();// ȷ������
		task_sitecondition_edt_str = task_sitecondition_edt.getText().toString().trim();// �ֳ����

		task_surveypeoplename_btn_str = task_surveypeoplename_btn.getText().toString().trim();// ��ü��������
		if (recordList != null && recordList.size() > 0) {
			task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString().trim(); // ��ü�¼�˵�����
		} else {
			task_recordpeopleid_sp_str = "";
		}
		if (dengerList != null && dengerList.length > 0) {
			task_dutypeople_gender_spinner_str = button_xunwen.getText().toString().trim();// ��ȡ��ѯ���˵��Ա�
		} else {
			task_dutypeople_gender_spinner_str = "";
		}

		if (record != null && record.length > 0) {
			task_naturepeople_gender_spinner_str = button_ziranren.getText().toString().trim();// ��ȡ��ѯ����Ȼ���Ա�
		} else {
			task_naturepeople_gender_spinner_str = "";
		}

	}

	private void saveUpdateData(final int stateFlag) {
		if (!bNeedSave()) {
			if (stateFlag == SCENE_UPDATE_DATA_SUCCESS) {
				Toast.makeText(TaskSiteEnforcementActivity.this, "�������ϴ��ɹ�", 0).show();
				return;
			}
			Message msg = handler.obtainMessage();
			msg.arg1 = stateFlag;
			handler.sendMessage(msg);
		}
		progressDialog = new ProgressDialog(TaskSiteEnforcementActivity.this);
		progressDialog.setMessage("����Ŭ����������,�����Ժ�...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				/** ��ȡ���� */
				getData();
				if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {
					/** �����ݿ����ֶβ���ֵ (ѯ�ʱ�) */
					content_XW_Valput();

				} else if (task_name_tv.getText().toString().trim().equals("�ֳ���飨���죩��¼")) {
					/** �����ݿ����ֶβ���ֵ (�����) */

					content_KC_Valput();
				} else if (task_name_tv.getText().toString().trim().equals("�������֪ͨ��")) {
					/** �����ݿ����ֶβ���ֵ (����֪ͨ��) */
					content_JSTZS_Valput();
				}
				try {

					if (task_name_tv.getText().toString().trim().equals("����ѯ�ʱ�¼")) {

						int wdViewNum = task_site_enfor_wd_layout.getChildCount();

						for (int index = 0; index < wdViewNum; index++) {
							LinearLayout chindView = (LinearLayout) task_site_enfor_wd_layout.getChildAt(index);
							EditText askEditText = (EditText) chindView.findViewById(R.id.ywyd_ask);
							EditText questionEditText = (EditText) chindView.findViewById(R.id.ywyd_question);

							ContentValues contentValues = new ContentValues();
							contentValues.put("taskid", rwbh);// ����ID
							contentValues.put("entid", qyid);// ��ȾԴ(��ҵ)ID
							contentValues.put("billid",guid);
							// ��������༭��
							contentValues.put("wtnr", askEditText.getText().toString());
							//
							contentValues.put("result", questionEditText.getText().toString());
							contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

							/** ���� */
							Object id = chindView.getTag();
							if (id != null && !"".equals(id)) {
								sqliteUtil.update("T_ZFWS_XWJLWD", contentValues, "ID = ?", new String[] { (String) id });
							} else {
								SqliteUtil.getInstance().insert(contentValues, "T_ZFWS_XWJLWD");
							}
						}

						/** ������ҵ����������Ų�ѯ�����¼��guId */
						if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
//							ArrayList<HashMap<String, Object>> guidData = new ArrayList<HashMap<String, Object>>();
//							guidData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_XWBL where taskid = '" + rwbh + "' and surveyentcode = '" + qyid
//									+ "' and type ='" + task_type_edt_str + "'");
							
							if (!TextUtils.isEmpty(guid)) {
								/** ������ҵ����������Ų�ѯ�����¼��guId */
								/** �������� */
								int count = sqliteUtil.update("T_ZFWS_XWBL", contentValues, "guid=?", new String[] { guid });
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATA_UPDATE_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1 || count > 1) {
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							} else {
								/** ����Ǳ���������������һ��GuId */
								String guid = UUID.randomUUID().toString();
								contentValues.put("guid", guid);
								/** �������� */
								long count = sqliteUtil.insert(contentValues, "T_ZFWS_XWBL");
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATABASE_INSERT_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1) {
									try {
										updateIntoExelawsTemplet(guid);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							}
						} else {
							Message msg = handler.obtainMessage();
							msg.arg1 = DATABASE_INSERT_EXCEPTION;
							handler.sendMessage(msg);
						}

					} else if (task_name_tv.getText().toString().trim().equals("�ֳ���飨���죩��¼")) {
						/** ������ҵ����������Ų�ѯ�����¼��guId */
						if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
//							ArrayList<HashMap<String, Object>> guidData = new ArrayList<HashMap<String, Object>>();
//							guidData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_KCBL where taskid = '" + rwbh + "' and entcode = '" + qyid
//									+ "' and type ='" + task_type_edt_str + "'");

							if (!TextUtils.isEmpty(guid)) {
								/** �������� */
								int count = sqliteUtil.update("T_ZFWS_KCBL", contentValues, "guid=?", new String[] { guid });
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATA_UPDATE_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1 || count > 1) {
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							} else {
								/** ����Ǳ���������������һ��GuId */
								String guid = UUID.randomUUID().toString();
								contentValues.put("guid", guid);
								/** �������� */
								long count = sqliteUtil.insert(contentValues, "T_ZFWS_KCBL");
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATABASE_INSERT_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1) {
									try {
										updateIntoExelawsTemplet(guid);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							}
						} else {
							Message msg = handler.obtainMessage();
							msg.arg1 = DATABASE_INSERT_EXCEPTION;
							handler.sendMessage(msg);
						}
					} else if (task_name_tv.getText().toString().trim().equals("�������֪ͨ��")) {
						if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
							ArrayList<HashMap<String, Object>> guidData = new ArrayList<HashMap<String, Object>>();
							guidData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from Survey_JSTZS where taskid = '" + rwbh + "' and entid = '" + qyid + "'");
							if (guidData != null && guidData.size() > 0) {
								/** ������ҵ����������Ų�ѯ�����¼��guId */
								String guid = guidData.get(0).get("guid").toString();
								/** �������� */
								int count = sqliteUtil.update("Survey_JSTZS", contentValues, "guid=?", new String[] { guid });
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATA_UPDATE_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1 || count > 1) {
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							} else {
								/** ����Ǳ���������������һ��GuId */
								String guid = UUID.randomUUID().toString();
								contentValues.put("guid", guid);
								/** �������� */
								long count = sqliteUtil.insert(contentValues, "Survey_JSTZS");
								if (count == 0) {
									Message msg = handler.obtainMessage();
									msg.arg1 = DATABASE_INSERT_EXCEPTION;
									handler.sendMessage(msg);
								} else if (count == 1) {
									Message msg = handler.obtainMessage();
									msg.arg1 = stateFlag;
									handler.sendMessage(msg);
								}
							}

						} else {
							Message msg = handler.obtainMessage();
							msg.arg1 = DATABASE_INSERT_EXCEPTION;
							handler.sendMessage(msg);
						}
					} else if (task_name_tv.getText().toString().trim().equals("�ֳ�����¼��")) {
						Message msg = handler.obtainMessage();
						msg.arg1 = stateFlag;
						handler.sendMessage(msg);
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private String id = "";
	/**����ǰ��¼Guid�쳣Ϊ��ʱ,���ô˷������µ�ExelawsTemplet����*/
	private void updateIntoExelawsTemplet(String guid) throws Exception{
		if (TextUtils.isEmpty(id)) {
			return;
		}
		ContentValues contentValues = new ContentValues();
//		contentValues.put("id", id);
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("enterid", qyid);// ��ȾԴ(��ҵ)ID
		contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues.put("billid", guid);
		if (codeStr.equals("0")) {
			contentValues.put("templetname","����ѯ�ʱ�¼"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			contentValues.put("templettype", "XWBL");
		} else if (codeStr.equals("1")) {
			contentValues.put("templetname","�ֳ���飨���죩��¼"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
			contentValues.put("templettype", "KCBL");
		} 
		sqliteUtil.update("ExeLawsTemplet",contentValues,"id = ?",new String[]{id});
	}
	/** �������ݵķ��� */
	public void save_data() {
		saveUpdateData(DATA_INSERT_SUCCESS);
	}

	/** �����ؼ�ʱ����/�������ݵķ��� */
	public void back_save_data() {
		saveUpdateData(BACK_DATA_INSERT_SUCCESS);
	}

	/** ����ӡԤ��ʱ����/�������ݵķ��� */
	public void preview_save_data() {
		saveUpdateData(PREVIEW_SAVE_DATA_SUCCESS);
	}

	/** ����ϴ�����/�������ݵķ��� */
	public void Upload_Save_Data() {
		saveUpdateData(UPLOAD_SAVE_DATA_SUCCESS);
	}

	/** ִ������ͨ����/�������ݵķ��� */
	public void Pepsi_Save_Data() {
		saveUpdateData(PEPSI_SAVE_DATA_SUCCESS);
	}

	/** �ֳ�ȡ֤����/���������¼� */
	public void Scene_Save_Data() {
		saveUpdateData(SCENE_UPDATE_DATA_SUCCESS);
	}

	/** �����ݿ�T_ZFWS_XWBL(ѯ�ʱ�¼)�����ֶβ���ֵ */
	public void content_XW_Valput() {
		contentValues = new ContentValues();
		contentValues.put("guid", guid);
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("surveystartdate", start_time_btn.getText().toString());// ��ʼʱ��
		contentValues.put("surveyenddate", end_time_btn.getText().toString());// ����ʱ��
		contentValues.put("surveyentcode", qyid);// ��ȾԴ(��ҵ)ID
		contentValues.put("surveyaddress", task_surveyaddress_edt_str);// ��ַ
		
		/** ���ݼ����������ѯ�����ID���� */
		ArrayList<HashMap<String, Object>> inveData = new ArrayList<HashMap<String, Object>>();
		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);
		
		if (task_surveypeoplename_btn_str != null && !task_surveypeoplename_btn_str.equals("")&&!task_surveypeoplename_btn_str.equals("-��ѡ������-")) {
			
			String[] arrayStr = task_surveypeoplename_btn_str.split(",");
			StringBuilder surveypeopleidsb = new StringBuilder();
			for (int i = 0; i < arrayStr.length; i++) {
				if (arrayStr[i] != null && arrayStr[i].trim().length() > 0) {
					inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid,zfzh from PC_Users where u_realname = '" + arrayStr[i] + "'");
				
					if (inveData.get(0).get("zfzh") == null) {
						surveypeopleidsb.append(arrayStr[i]+"()");
					}else {
						surveypeopleidsb.append(arrayStr[i]+"("+inveData.get(0).get("zfzh").toString()+")");
					}	
					surveypeopleidsb.append(",");
				}
			}
			String surveypeopleid = surveypeopleidsb.substring(0, surveypeopleidsb.length()-1);
			contentValues.put("surveypeoplecradcode", surveypeopleid.toString());//�����id
			
		} else {
			contentValues.put("surveypeoplecradcode", "");
			contentValues.put("surveypeoplename", "");//�����
		}
		
		


		/** ���ݼ�¼��������ѯ��¼��ID���ݼ��� */
		inveData = new ArrayList<HashMap<String, Object>>();
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
			inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
			if (inveData != null && inveData.size() > 0) {
				String inveID = inveData.get(0).get("userid").toString();
				if (inveID != null && !inveID.equals("")) {
					contentValues.put("recordpeopleid", inveID);// ��¼�˵�ID
				}
			}
		}
		contentValues.put("recordpeoplename", task_recordpeopleid_sp_str);// ��¼�˵�����
		contentValues.put("askedpeoplesex", task_dutypeople_gender_spinner_str);// ��ѯ�����Ա�
		contentValues.put("otherpeoplesex", task_naturepeople_gender_spinner_str);// ��ѯ����Ȼ�˵��Ա�
		contentValues.put("dutypeoplename", task_checkpeople_edt_str);// ���˴���
		contentValues.put("dutypeopleentprisename", task_entcode_edt_str);// ������������λ(������λ)
//		contentValues.put("surveypeoplecradcode", task_surveypeoplename_btn_str);
		contentValues.put("askedpeopleduty", task_dutypeopleoffice_edt_str);// ��ѯ����������ְ��
		contentValues.put("askedpeopleage", task_dutypeople_age_edt_str);// ��ѯ���ϵ�����
		contentValues.put("dutypeopleaddress", task_natruepeople_edt_str);// ��ѯ����Ȼ�˵�����
		
		contentValues.put("otherpeopleage", task_natruepeople_age_edt_str);// ��ѯ����Ȼ�˵�����
		contentValues.put("otherpeopleaddress", task_natruepeopleaddress_edt_str);// ��ѯ����Ȼ�˵ĵ�ַ
		contentValues.put("otherpeopletel", task_natruepeopletel_edt_str);// ��ѯ����Ȼ�˵ĵ绰
		contentValues.put("otherpeoplerelation", task_dutypeoplerelation_edt_str);// �뱾����ϵ
		contentValues.put("dutypeopletel", task_checkpeople_phone_edt_str);// ���������˵绰
		contentValues.put("askedpeopletel", task_dutypeopletel_edt_str);// ��ѯ���˵绰
		contentValues.put("yyzzzch", task_business_license_edt.getText().toString());// ��ҵ֤��ע���
		contentValues.put("zzjgdm", task_organization_edt_str);// ��֯��������
		contentValues.put("dutypeopleyzbm", task_checkpeople_zip_edt.getText().toString());
		contentValues.put("recordpeopleunit", task_work_units_edt_str);// ����˵Ĺ�����λ
		contentValues.put("type", task_type_edt_str);// ����
		contentValues.put("askedpeoplename",task_askedpeoplename_edt.getText().toString());//��ѯ��������
		contentValues.put("askedpeopleage",task_askedpeopleage_edt.getText().toString());//��ѯ��������
		contentValues.put("askedpeopleidnumber",task_askedpeopleidnumber_edt.getText().toString());//��ѯ�������֤��
		
		contentValues.put("surveyentprisename",task_askedpeopleunit_edt.getText().toString());//��ѯ���˹�����λ
		contentValues.put("dutypeopleyzbm",task_askedpeoplepostcode_edt.getText().toString());//��ѯ�����ʱ�
		if (-1 != task_askedpeoplesex_edt.getCheckedRadioButtonId()) {
			contentValues.put("askedpeoplesex",((RadioButton)findViewById(task_askedpeoplesex_edt.getCheckedRadioButtonId())).getText().toString());			
		}else {
			contentValues.put("askedpeoplesex","");
		}
		contentValues.put("updatetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	/** �����ݿ�Survey_JSTZS(����֪ͨ��)�����ֶβ���ֵ */
	public void content_JSTZS_Valput() {
		contentValues = new ContentValues();
		String guid = (String) ֪ͨ����.getTag();

		contentValues.put("guid", guid != null && !guid.equals("") ? guid : UUID.randomUUID().toString());
		contentValues.put("entid", qyid);
		contentValues.put("taskid", rwbh);

		contentValues.put("noticeno", ֪ͨ����.getText().toString());
		contentValues.put("lawtime", currtime);
		contentValues.put("entname", ���鵥λ.getText().toString());
		contentValues.put("surveytime", ���ܵ���ʱ��.getTag().toString());
		contentValues.put("breakbehavior", Υ����Ϊ.getText().toString());
		contentValues.put("takematerial", Я������.getText().toString());
		contentValues.put("contactperson", �ֳ������.getText().toString());
		contentValues.put("address", �ֳ��������ϵ��ַ.getText().toString());
		contentValues.put("phone", �ֳ��������ϵ�绰.getText().toString());
		contentValues.put("updatetime", currtime);
		contentValues.put("signtime", currtime);

		contentValues.put("bjcdz", ���鵥λ��ַ.getText().toString());
		contentValues.put("xcjsr", �ֳ�������.getText().toString());
		contentValues.put("bjclxdh", ��������ϵ�绰.getText().toString());
		contentValues.put("fj", �־�.getText().toString());
		contentValues.put("ks", ����.getText().toString());
		contentValues.put("qtzl", ��������.getText().toString());
		contentValues.put("wffl", Υ������.getText().toString());
	}

	/** �����ݿ�T_ZFWS_KCBL(�����¼)�����ֶβ���ֵ */
	public void content_KC_Valput() {
		contentValues = new ContentValues();
		contentValues.put("entcode", qyid);// ��ȾԴ(��ҵ)ID
		contentValues.put("guid", guid);
		
		
		
		/** ���ݼ����������ѯ�����ID���� */
		ArrayList<HashMap<String, Object>> inveData = new ArrayList<HashMap<String, Object>>();
		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);//�����
		if (task_surveypeoplename_btn_str != null && !task_surveypeoplename_btn_str.equals("")&&!task_surveypeoplename_btn_str.equals("-��ѡ������-")) {
			
			String[] arrayStr = task_surveypeoplename_btn_str.split(",");
			StringBuilder surveypeoplecradcodesb = new StringBuilder();
			StringBuilder surveypeopleidsb = new StringBuilder();
			for (int i = 0; i < arrayStr.length; i++) {
				if (arrayStr[i] != null && arrayStr[i].trim().length() > 0) {
					inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid,zfzh from PC_Users where u_realname = '" + arrayStr[i] + "'");
					if (inveData.get(0).get("zfzh") == null) {
						surveypeoplecradcodesb.append("()");
					}else {
						surveypeoplecradcodesb.append("("+inveData.get(0).get("zfzh").toString().replaceAll("\\s*|\t|\r|\n", "")+")");
					}
					surveypeoplecradcodesb.append(",");
					if (inveData.get(0).get("zfzh") == null) {
						surveypeopleidsb.append(arrayStr[i]+"()");
					}else {
						surveypeopleidsb.append(arrayStr[i]+"("+inveData.get(0).get("zfzh").toString()+")");
					}	
					surveypeopleidsb.append(",");
				}
			}
			String surveypeoplecradcode = surveypeoplecradcodesb.substring(0, surveypeoplecradcodesb.length()-1);
			String surveypeopleid = surveypeopleidsb.substring(0, surveypeopleidsb.length()-1);
			contentValues.put("surveypeoplecradcode", surveypeoplecradcode.toString());//�����ִ��֤��
			contentValues.put("surveypeopleid", surveypeopleid.toString());//�����id
			
			
		} else {
			contentValues.put("surveypeoplecradcode", "()");
			contentValues.put("surveypeopleid", "");//�����id
			contentValues.put("surveypeoplename", "");//�����
		}
		
		/** ���ݼ�¼��������ѯ��¼��ID���� */
//		contentValues.put("recordpeoplename", task_recordpeopleid_sp_str);// ��¼�˵�����
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")&&!task_recordpeopleid_sp_str.equals("-����ѡ������-")) {
			inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid,zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
			if (inveData != null && inveData.size() > 0) {
				String inveID = inveData.get(0).get("userid").toString();
				if (inveID != null && !inveID.equals("")) {
					contentValues.put("recordpeopleid", inveID);// ��¼�˵�ID
				}
				String num = inveData.get(0).get("zfzh").toString();
				if (num != null && !num.equals("") ) {
					contentValues.put("recordpeoplename", task_recordpeopleid_sp_str+"("+num.replaceAll("\\s*|\t|\r|\n", "")+")");// ��¼�˵�ִ��֤��
				}else{
					contentValues.put("recordpeoplename", task_recordpeopleid_sp_str+"()");
				}
			}
		}else{
			contentValues.put("recordpeoplename", "");// ��¼�˵�ִ��֤��
		}
		
		
		
		
		
		contentValues.put("surveystartdate", start_time_btn.getText().toString());// ��ʼʱ��
		contentValues.put("surveyenddate", end_time_btn.getText().toString());// ����ʱ��
		contentValues.put("taskid", rwbh);// ����ID
		contentValues.put("entprisename", task_entcode_edt_str);// ��ҵ����
		contentValues.put("surveryaddress", task_surveyaddress_edt_str);// ��ַ

		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);// ���������
		
		contentValues.put("surveypeopleunit", task_work_units_edt_str);// ����˵Ĺ�����λ
		String num = "";
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
			String sql = "select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "' ";
			num = SqliteUtil.getInstance().getDepidByDepName(sql);
		}
		contentValues.put("recordpeopleid", num);// ��¼�˵�ִ��֤��
		contentValues.put("recordpeopleunit", task_recordpeopleid_work_units_edt_str);// ��¼�˵Ĺ�����λ

		contentValues.put("sitecondition", task_sitecondition_edt_str);// �ֳ����
//		contentValues.put("qrzw",task_entcode_edt.getText().toString() );//��¼�˹�����λ
		contentValues.put("type", task_type_edt_str);// ����
		contentValues.put("checkpeople", beijiancharen.getText().toString());
//		contentValues.put("checkpeople", farendaibiao.getText().toString());
		contentValues.put("dutypeople", xianchangfuzeren.getText().toString());
		contentValues.put("dutypeoplecode", shengfenzheng.getText().toString());
		
		contentValues.put("dutypeopledepartment", gongzuodanwei.getText().toString());//�ֳ������˹�����λ
		contentValues.put("dutypeopleoffice", zhiwu.getText().toString());
		contentValues.put("dutypeoplerelation", yubenanguanxi.getText().toString());
		contentValues.put("dutypeopleaddress", dizhi.getText().toString());
		//BYK �ı�������ݿ� �绰���ֶ�
		contentValues.put("dutypeopletel", dianhua.getText().toString());
		contentValues.put("AskedPeopleTel", dianhua.getText().toString());
		contentValues.put("otherpeopleaddress", qitacanjiaren.getText().toString());
		contentValues.put("updatetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		contentValues.put("dutypeopleyzbm", task_checkpeople_zip_edt.getText().toString());
		
	}

	/** �ж�����ѡ������� */
	void clericalVoid() {
		/** �����������ݼ��� */
		ArrayList<HashMap<String, Object>> clericalData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select code,name from T_YDZF_PublicCode");
		for (int i = 0; i < clericalData.size(); i++) {
			String title = clericalData.get(i).get("name").toString();

			if (task_name_tv.getText().toString().equals(title)) {
				task_type_edt_str = clericalData.get(i).get("code").toString();
				break;
			} else if (task_name_tv_jieshou.getText().toString().equals(title)) {
				task_name_tv.setText(task_name_tv_jieshou.getText().toString());
				task_type_edt_str = clericalData.get(i).get("code").toString();
				break;
			}
		}
	}

	/** ��¼ȡ֤��ť�����¼� */
	public void obtain_evid_click(View view) {
		//if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("1")) {
		//  BYK rwzt
			if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("3")) {
			Intent scene_update_intent = new Intent(TaskSiteEnforcementActivity.this, SiteEvidenceActivity.class);
			scene_update_intent.putExtra("currentTaskID", rwbh);
			scene_update_intent.putExtra("qyid", qyid);
			startActivity(scene_update_intent);
		} else {
			// ��¼ȡ֤��ť�����¼�
			Scene_Save_Data();
		}
	}

	/** �嵥����¼����� */
	public void qdjc_click(View view) {
		if (RWBundle != null) {
			LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
			View v = inflater.inflate(R.layout.task_site_enforcement_template, null);
			task_site_enfor_sp = (Spinner) v.findViewById(R.id.task_site_enfor_sp);

			/** ִ��ģ�������� */
			ArrayAdapter<SpinnerItem> mbAdapter;
			/** Spinner���ݼ��� */
			List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
			if (rwxx != null) {
				mbList = rwxx.getSpinnerItem();
			}

			mbAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, mbList);
			mbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			task_site_enfor_sp.setAdapter(mbAdapter);

			task_site_enfor_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					task_site_enfor_sp_str = task_site_enfor_sp.getSelectedItem().toString();
					ArrayList<HashMap<String, Object>> task_site_enfor_sp_map = new ArrayList<HashMap<String, Object>>();
					task_site_enfor_sp_map = sqliteUtil.queryBySqlReturnArrayListHashMap("select tid from YDZF_SpecialTemplate where tname = '" + task_site_enfor_sp_str + "'");
					if (task_site_enfor_sp_map != null && task_site_enfor_sp_map.size() > 0) {
						task_site_enfor_sp_id = task_site_enfor_sp_map.get(0).get("tid").toString();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder builder = new AlertDialog.Builder(TaskSiteEnforcementActivity.this);
			builder.setTitle("-��ѡ��ִ��ģ��-");
			builder.setIcon(getResources().getDrawable(R.drawable.yutu));
			builder.setView(v);
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					progressDialog = new ProgressDialog(TaskSiteEnforcementActivity.this);
					progressDialog.setMessage("�����ύ����,�����Ժ�...");
					progressDialog.setCancelable(false);
					progressDialog.show();

					new Thread(new Runnable() {

						@Override
						public void run() {
							ContentValues contentValues = new ContentValues();
							contentValues.put("specialtemplateid", task_site_enfor_sp_id);

							if (rwbh != null && !rwbh.equals("")) {
								try {
									int count;
									/** �������� */
									count = sqliteUtil.update("T_YDZF_RWXX", contentValues, "rwbh=?", new String[] { rwbh });
									if (count == 0) {
										Message msg = handler.obtainMessage();
										msg.arg1 = DATA_UPDATE_EXCEPTION;
										handler.sendMessage(msg);
									} else if (count == 1 || count > 1) {
										Message msg = handler.obtainMessage();
										msg.arg1 = CHECK_LIST_DATA_UPDATE_SUCCESS;
										handler.sendMessage(msg);
									}
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					/** ȡ���Ի��� */
					alertDialog.cancel();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();
		}
	}

	/** ����ѡ��ť�����¼� */
	public void record_click(View view) {
		back_save_data();
	}

	/** ����ϴ���ť�����¼� */
	// public void upload_click(View view) {
	// if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("1")) {
	// if (rwxx != null && rwbh != null) {
	//
	// rwxx.uploadTask(rwbh, TaskSiteEnforcementActivity.this, qyid);
	// }
	// } else {
	// // ����ϴ���ť���淽��
	// Upload_Save_Data();
	// }
	//
	// }

	/** ִ������ͨ��ť�����¼� */
	public void law_enfor_click(View view) {
		//BYK rwzt
		if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("3")) {
		//	if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("1")) {
			Intent pepsi_save_intent = new Intent(TaskSiteEnforcementActivity.this, LawKnowAllActivity.class);
			startActivity(pepsi_save_intent);
		} else {
			// ִ������ͨ��ť���淽��
			Pepsi_Save_Data();
		}
	}

	/** ��ӡԤ����ť�����¼� */
	public void preview_click(View view) {
		String taskName = task_name_tv.getText().toString();
		if (taskName.contains("����") || taskName.contains("ѯ��") || taskName.contains("�ֳ�����¼��") || taskName.equals("�������֪ͨ��")) {
			preview_save_data();
		} else {
			int result = compare();
			if (result == 1) {
				return;
			}
			if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("") && task_surveypeoplename_btn.getText().toString() != null
					&& !task_surveypeoplename_btn.getText().toString().equals("")) {
				preview_save_data();
			} else {
				Toast.makeText(TaskSiteEnforcementActivity.this, "ִ�������Ʋ���Ϊ��", Toast.LENGTH_LONG).show();
			}
		}
	}

	int hour, minute1;
	static final int TIME_DIALOG_ID = 0;

	/**
	 * Я������ѡ��
	 * 
	 * @param view
	 */
	public void selectData(View view) {
		final String temps[] = getResources().getStringArray(R.array.noem_carryData);
		final HashSet<String> items = new HashSet<String>();

		AlertDialog.Builder builder = OtherTools.getAlertDialog(this, "��ѡ����ҪЯ��������:", null, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuffer buffer = new StringBuffer();
				for (String string : items) {
					buffer.append(string + ",");
				}
				if (buffer.length() == 0) {
					buffer.append(",");
				}
				Я������.setText(buffer.toString().substring(0, buffer.length() - 1));
			}
		}, null, null);
		builder.setMultiChoiceItems(temps, null, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				String temp = temps[which];
				if (isChecked) {
					items.add(temp);
				} else {
					items.remove(temp);
				}
			}
		});
		builder.show();
	}

	/** ����ʱ��ؼ��ļ����� */
	public void update(final View view) {
		switch (view.getId()) {
		case R.id.start_time_btn:
			DatePickerDialog dpd = new DatePickerDialog(TaskSiteEnforcementActivity.this, dateStart,// ��ʼ��Ϊ��ǰ����
					dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			isshow = true;
			dpd.show();
			break;

		case R.id.end_time_btn:
//			showDialog(TIME_DIALOG_ID);
			TimePickerDialog timedialog = new TimePickerDialog(TaskSiteEnforcementActivity.this, timeStart1, dateAndTime.get(Calendar.HOUR_OF_DAY),
					dateAndTime.get(Calendar.MINUTE), true);
			timedialog.show();

			break;
		case R.id.task_work_noem_dealDate:
		case R.id.again_time_btn:
		case R.id.one_time_btn:
			DatePickerDialog dpd4 = new DatePickerDialog(TaskSiteEnforcementActivity.this, again_date,// ��ʼ��Ϊ��ǰ����
					dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			isshow = true;
			dpd4.show();
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		// return super.onCreateDialog(id);
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, hour, minute1, false);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String hourStr = "";
			String minute1Str = "";
			if (String.valueOf(hourOfDay).length() > 1) {
				hourStr = String.valueOf(hourOfDay);
			} else {
				hourStr = "0" + hourOfDay;
			}
			if (String.valueOf(minute).length() > 1) {
				minute1Str = String.valueOf(minute);
			} else {
				minute1Str = "0" + minute;
			}

			// Toast.makeText(getBaseContext(),
			// "You have selected : " + hourStr + ":" + minute1Str,
			// Toast.LENGTH_SHORT).show();
			end_time_btn.setText(datefmtDate.format(dateAndTime.getTime()) + " " + hourStr + ":" + minute1Str + ":00");
		}
	};

	/** �жϿ�ʼʱ�䲻�ܴ��ڽ���ʱ��ķ��� */
	public int compare() {
		String timeStart = start_time_btn.getText().toString();
		String timeEnd = end_time_btn.getText().toString();
		if (timeStart.compareTo(timeEnd) >= 0) {
			Toast.makeText(TaskSiteEnforcementActivity.this, "��ȷ������ʱ����ڿ�ʼʱ��", 1).show();
			return 1;
		} else {
			startTime = timeStart;
			endTime = timeEnd;
			return 0;
		}

	}

	/** �жϵ�ǰҳ��������Ƿ���Ҫ���� */
	boolean bNeedSave() {
		String sql2 = "select isexcute from TaskEnpriLink where qyid ='" + qyid + "' and TaskId='" + rwbh + "'";
		ArrayList<HashMap<String, Object>> data = sqliteUtil.queryBySqlReturnArrayListHashMap(sql2);

		for (int i = 0; i < data.size(); i++) {

			String bStr = (String) data.get(i).get("isexcute");
			if (bStr.equals("3")) {
				//BYK rwzt
//				if (bStr.equals("1")) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ItemAdapter itemAdapter = (ItemAdapter) parent.getAdapter();
		Intent intent = new Intent();
		Context context = TaskSiteEnforcementActivity.this;
		HashMap<String, Object> hashmap = (HashMap<String, Object>) itemAdapter.getItem(position);
		intent.putExtra("entid", qyid);
		intent.putExtra("taskid", rwbh);
		intent.putExtra("code", hashmap.get("code").toString());
		intent.putExtra("name", hashmap.get("name").toString());
		intent.putExtra("isSearch", isSearch);
		intent.setClass(context, SuperviseItemActivity.class);
		startActivityForResult(intent, 1);
		context = null;

	}

	/** ���ؼ������¼� */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("�ֳ�����¼��".equals(task_name_tv.getText().toString()) && !"".equals(tv2.getText().toString())) {
				tv2.setText("");
				final ArrayList<HashMap<String, Object>> qdjcList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("code", "1");
				hashmap.put("name", "�������");
				qdjcList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "2");
				hashmap.put("name", "��Ⱦ������ʩ�����������");
				qdjcList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "3");
				hashmap.put("name", "��Ⱦ���ŷ����");
				qdjcList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "4");
				hashmap.put("name", "���ڻ������⼰���");
				qdjcList.add(hashmap);

				QdjcAdapter qdjcAdapter = new QdjcAdapter(qdjcList, TaskSiteEnforcementActivity.this);
				zhtree.setAdapter(qdjcAdapter);
				zhtree.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						task_site_enfor_sp_id = qdjcList.get(arg2).get("code").toString();
						tv2.setText("    " + qdjcList.get(arg2).get("name").toString());
						tv2.setTextColor(Color.GRAY);
						WorkAsyncTask task = new WorkAsyncTask(TaskSiteEnforcementActivity.this);
						task.execute(task_site_enfor_sp_id);
					}
				});
			} else {
				if (qyzts != null) {
					if (qyzts.equalsIgnoreCase("3")) {
						//BYK rwzt
//						if (qyzts.equalsIgnoreCase("1")) {
						finish();
					} else {
						if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
							/** ���ؼ��������� */
							back_save_data();
						} else {
							finish();
						}
					}
				} else {
					finish();
				}
			}
		}

		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/** ѡ��ִͬ���������� */
	public class CommonLawPeopleAdapter extends BaseExpandableListAdapter {
		/** ��ȡ��һ�鹲ִͬ���˶����б��и����б���������ݼ��� */
		private List<String> groupList;
		/** ��ȡ��һ�鹲ִͬ���˶����б��в�ѯ�Ӽ��б����ݵļ��� */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		LayoutInflater layoutInflater;
		String currentUserId = Global.getGlobalInstance().getUserid();

		public CommonLawPeopleAdapter(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb, LinkedList<String> linkedName,
				Context context) {
			// ��ʼ����ǰ��¼�û�������
			String sql = "select u_realname from PC_Users where userid = '" + currentUserId + "'";
			String currentUserName = SqliteUtil.getInstance().getDepidByDepName(sql);
			linkedName.add(currentUserName);
			usersb.add(currentUserId);

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
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			convertView = layoutInflater.inflate(R.layout.two_class_interface, null);

			TextView userTv = (TextView) convertView.findViewById(R.id.two_class_interface_name_tv);
			CheckBox isSelect = (CheckBox) convertView.findViewById(R.id.two_class_cb);
			final String realName = childMapData.get(groupPosition).get(childPosition).get("u_realname").toString();
			final String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
			if (usersb.contains(userCheckedId)) {
				isSelect.setChecked(true);
				if (userCheckedId.equals(currentUserId)) {
					userTv.setTextColor(Color.GRAY);
					isSelect.setClickable(false);
					isSelect.setEnabled(false);
				}
			} else {
				isSelect.setChecked(false);
			}
			isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (userCheckedId.equals(currentUserId)) {
						return;
					}

					if (isChecked) {
						if (!usersb.contains(userCheckedId)) {
							usersb.add(userCheckedId);
						}
						if (!linkedName.contains(realName)) {
							linkedName.add(realName);
						}

					} else {

						if (usersb.contains(userCheckedId)) {
							usersb.remove(userCheckedId);
						}
						if (linkedName.contains(realName)) {
							linkedName.remove(realName);
						}

					}
				}
			});
			userTv.setText(realName);
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
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.one_class_interface, null);
			}
			TextView one_class_interface_title_tv = (TextView) convertView.findViewById(R.id.one_class_interface_title_tv);

			one_class_interface_title_tv.setText(groupList.get(groupPosition).toString());

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

	private Button task_site_obtain_evidence_btn;

	/** ��ʼ��ҳ�� */
	private void findViewById() {
		
		xianchangkanchabilu = (LinearLayout) findViewById(R.id.xianchangkanchabilu);
		beijiancharen = (EditText) findViewById(R.id.beijiancharen);
		farendaibiao = (EditText) findViewById(R.id.farendaibiao);
		xianchangfuzeren = (EditText) findViewById(R.id.xianchangfuzeren);
		shengfenzheng = (EditText) findViewById(R.id.shengfenzheng);
		gongzuodanwei = (EditText) findViewById(R.id.gongzuodanwei);
		gongzuodanwei.setText(rwxx.getQYMC_form_GUID(qyid));
		zhiwu = (EditText) findViewById(R.id.zhiwu);
		yubenanguanxi = (EditText) findViewById(R.id.yubenanguanxi);
		dizhi = (EditText) findViewById(R.id.dizhi);
		dianhua = (EditText) findViewById(R.id.dianhua);
		qitacanjiaren = (EditText) findViewById(R.id.qitacanjiaren);
		
		two_list_tool_layout = (RelativeLayout) this.findViewById(R.id.two_list_tool_layout);
		/** ���ñ������� */
		SetBaseStyle(two_list_tool_layout, "�ֳ�ִ��");
		/** �嵥��鰴ť */
		task_site_check_list_btn = (Button) findViewById(R.id.task_site_check_list_btn);
		task_site_check_list_btn.setVisibility(View.GONE);

		task_site_obtain_evidence_btn = (Button) findViewById(R.id.task_site_obtain_evidence_btn);
		task_site_obtain_evidence_btn.setVisibility(View.GONE);
		/** ִ������ͨ��ť */
		task_site_law_enforcement_btn = (Button) findViewById(R.id.task_site_law_enforcement_btn);

		task_surveypeoplename_one_tv = (TextView) findViewById(R.id.task_surveypeoplename_tv);
		start_time_btn = (Button) findViewById(R.id.start_time_btn);
		end_time_btn = (Button) findViewById(R.id.end_time_btn);
		task_name_tv = (TextView) findViewById(R.id.task_name_tv);
		task_entcode_edt = (EditText) findViewById(R.id.task_entcode_edt_01);
		task_enforcement_certificate_no_edt = (EditText) findViewById(R.id.task_enforcement_certificate_no_edt);
		task_recordpeopleid_enforcement_certificate_no_edt = (EditText) findViewById(R.id.task_recordpeopleid_enforcement_certificate_no_edt);
		task_checkpeople_edt = (EditText) findViewById(R.id.task_checkpeople_edt);
		task_dutypeople_edt = (EditText) findViewById(R.id.task_dutypeople_edt);
		task_dutypeopleoffice_edt = (EditText) findViewById(R.id.task_dutypeopleoffice_edt);
		task_dutypeoplerelation_edt = (EditText) findViewById(R.id.task_dutypeoplerelation_edt);
		task_dutypeopletel_edt = (EditText) findViewById(R.id.task_dutypeopletel_edt);

		task_askedpeoplesex_edt = (RadioGroup) findViewById(R.id.task_askedpeoplesex_edt);
		task_askedpeoplename_edt = (android.widget.EditText) findViewById(R.id.task_askedpeoplename_edt);
		task_askedpeopleage_edt = (android.widget.EditText) findViewById(R.id.task_askedpeopleage_edt);
		task_askedpeopleidnumber_edt = (android.widget.EditText) findViewById(R.id.task_askedpeopleidnumber_edt);
		
		task_askedpeopleunit_edt = (android.widget.EditText) findViewById(R.id.task_askedpeopleunit_edt);
		task_askedpeopleunit_edt.setText(rwxx.getQYMC_form_GUID(qyid));///----------------��ѯ���˵�λ
		
		task_askedpeoplepostcode_edt = (android.widget.EditText) findViewById(R.id.task_askedpeoplepostcode_edt);
		task_dutypeople_age_edt = (EditText) findViewById(R.id.task_dutypeople_age_edt);// ��ѯ���˵�����
		task_natruepeople_edt = (EditText) findViewById(R.id.task_natruepeople_edt);// ��ѯ����Ȼ�˵�����
		task_natruepeople_age_edt = (EditText) findViewById(R.id.task_natruepeople_age_edt);// ��ѯ����Ȼ�˵�����
		task_natruepeopleoffice_edt = (EditText) findViewById(R.id.task_natruepeopleoffice_edt);// ��ѯ����Ȼ�˵�ְ��
		task_natruepeopletel_edt = (EditText) findViewById(R.id.task_natruepeopletel_edt);// ��ѯ����Ȼ�˵ĵ绰
		task_natruepeopleaddress_edt = (EditText) findViewById(R.id.task_natruepeopleaddress_edt);// ��ѯ����Ȼ�˵�סַ
		task_askedpeoplesex_edt = (RadioGroup) findViewById(R.id.task_askedpeoplesex_edt);
		task_otherpeopleaddress_edt = (EditText) findViewById(R.id.task_otherpeopleaddress_edt);
		task_confirm_body_edt = (EditText) findViewById(R.id.task_confirm_body_edt);
		task_sitecondition_edt = (EditText) findViewById(R.id.task_sitecondition_edt);

		task_type_edt = (EditText) findViewById(R.id.task_type_edt);
		task_surveyaddress_lay = (LinearLayout) findViewById(R.id.task_surveyaddress_lay);
		xwbldybj = (LinearLayout) findViewById(R.id.xwbldybj);
		task_company_address_edt = (EditText) findViewById(R.id.task_surveyaddress_edt);
		task_surveyaddress_edt = (EditText) findViewById(R.id.task_simpleenfo_surveyaddress);
		task_surveypeoplename_btn = (Button) findViewById(R.id.task_surveypeoplename_btn);// ����˵�����
		task_work_units_edt = (EditText) findViewById(R.id.task_work_units_edt);// ����˹�����λ
		task_work_units_edt.setText(Global.getGlobalInstance().getUserUnit());
		if("NULL".equals(Global.getGlobalInstance().getUserUnit())){
			task_work_units_edt.setText("");
		}
		task_recordpeopleid_sp = (Spinner) findViewById(R.id.task_recordpeopleid_sp);// ��¼��
		task_recordpeopleid_work_units_edt = (EditText) findViewById(R.id.task_recordpeopleid_work_units_edt);// ��¼�˵Ĺ�����λ
		task_recordpeopleid_work_units_edt.setText(Global.getGlobalInstance().getUserUnit());
		task_address_tv = (TextView) findViewById(R.id.task_address_tv);

		// task_checkpeople_position_edt = (EditText)
		// findViewById(R.id.task_checkpeople_position_edt);
		task_checkpeople_phone_edt = (EditText) findViewById(R.id.task_checkpeople_phone_edt);
		task_checkpeople_zip_edt = (EditText) findViewById(R.id.task_checkpeople_zip_edt);
		task_business_license_edt = (EditText) findViewById(R.id.task_business_license_edt);
		task_organization_edt = (EditText) findViewById(R.id.task_organization_edt);
		task_site_scoll_out = (LinearLayout) findViewById(R.id.task_site_scoll_out);
		// task_site_scoll = (ScrollView) findViewById(R.id.task_site_scoll);
		task_site_law_enforcement_btn.setVisibility(View.GONE);// ִ������ͨ��ť���ɼ�

		task_entcode_tv = (TextView) findViewById(R.id.task_entcode_tv);
		task_dutypeople_tv = (TextView) findViewById(R.id.task_dutypeople_tv);
		task_sitcondtion_lay = (LinearLayout) findViewById(R.id.task_sitcondtion_lay);
		task_site_enfor_wd_layout = (LinearLayout) findViewById(R.id.task_site_enfor_wd_layout);
		task_site_enfor_wd_btn = (Button) findViewById(R.id.task_site_enfor_wd_btn);
		task_site_enfor_add_btn = (Button) findViewById(R.id.task_site_enfor_add_btn);

		task_site_enfor_wd_layout.setVisibility(View.GONE);
		task_site_enfor_wd_btn.setVisibility(View.GONE);
		task_site_enfor_add_btn.setVisibility(View.GONE);

		// �������֪ͨ���еĿؼ�
		task_site_scoll_out_jieshou = (LinearLayout) findViewById(R.id.task_site_scoll_out_jieshou);
		֪ͨ���� = (EditText) findViewById(R.id.task_work_noem_edit_noticeNum);

		���鵥λ��ַ = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseAddress);
		���鵥λ = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseName);
		�ֳ������� = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseLinkMan);
		��������ϵ�绰 = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseLinkManTelephone);
		Υ����Ϊ = (EditText) findViewById(R.id.task_work_noem_edit_illegalBehavior);
		Υ������ = (EditText) findViewById(R.id.task_work_noem_edit_illegal);

		���ܵ���ʱ�� = (Button) findViewById(R.id.task_work_noem_dealDate);

		���ܵ���ʱ��.setTag(Timeuitl.getsystemtine());
		�־� = (EditText) findViewById(R.id.task_work_noem_edit_surveyGOV);
		���� = (EditText) findViewById(R.id.task_work_noem_edit_surveyAddress);
		Я������ = (Button) findViewById(R.id.task_work_noem_edit_carryData);
		�������� = (EditText) findViewById(R.id.task_work_noem_edit_otherData);

		�ֳ������ = (EditText) findViewById(R.id.task_work_noem_edit_surveyMan);
		�ֳ��������ϵ��ַ = (EditText) findViewById(R.id.task_work_noem_edit_surveyManAddress);
		�ֳ��������ϵ�绰 = (EditText) findViewById(R.id.task_work_noem_edit_surveyManTelephone);
		task_name_tv_jieshou = (TextView) findViewById(R.id.task_name_tv_jieshou);

		// �ֳ���������¼

		task_site_scoll_out_jiancha = (LinearLayout) findViewById(R.id.task_site_scoll_out_jiancha);
		task_name_tv_jiancha = (TextView) findViewById(R.id.task_name_tv_jiancha);
		task_site_scoll_jiancha = (LinearLayout) findViewById(R.id.task_site_scoll_jiancha);
		task_site_scoll_out_jiancha.setVisibility(View.VISIBLE);
		task_site_scoll_out.setVisibility(View.INVISIBLE);
		task_site_scoll_out_jieshou.setVisibility(View.INVISIBLE);
		task_name_tv.setText(task_name_tv_jiancha.getText().toString());
		button_xunwen = (Button) findViewById(R.id.task_dutypeople_gender_button03);
		button_ziranren = (Button) findViewById(R.id.task_naturepeople_gender_button02);

		button_xunwen.setText("��ѡ���Ա�");
		button_ziranren.setText("��ѡ���Ա�");

		button_xunwen.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setTitle("��ѡ���Ա�").setIcon(

				android.R.drawable.ic_dialog_info).setSingleChoiceItems(

				dengerList, 0,

				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						task_dutypeople_gender_spinner_str = dengerList[which].toString();

						button_xunwen.setText(task_dutypeople_gender_spinner_str);
					}
				}).setNegativeButton("ȷ��", null).show();

			}
		});
		button_ziranren.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(TaskSiteEnforcementActivity.this).setTitle("��ѡ���Ա�").setIcon(

				android.R.drawable.ic_dialog_info).setSingleChoiceItems(

				record, 0,

				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						task_naturepeople_gender_spinner_str = record[which].toString();

						button_ziranren.setText(task_naturepeople_gender_spinner_str);
					}
				}).setNegativeButton("ȷ��", null).show();

			}
		});

	}

	/** ��ʼ�����治�ɱ༭ */
	public void not_edit() {
		task_askedpeoplename_edt.setEnabled(false);//��ѯ����
		task_askedpeopleidnumber_edt.setEnabled(false);//��ѯ�������֤��
		task_askedpeopleunit_edt.setEnabled(false);//��ѯ���˹�����λ
		//task_askedpeoplesex_edt.setClickable(false);//�Ա�
		
		   for (int i = 0; i < task_askedpeoplesex_edt.getChildCount(); i++) {
			   task_askedpeoplesex_edt.getChildAt(i).setEnabled(false);
	        }
		
		task_askedpeopleage_edt.setEnabled(false);//����
		
		
		
		task_askedpeoplepostcode_edt.setEnabled(false);//��ѯ�����ʱ�
		button_xunwen.setEnabled(false);
		button_ziranren.setEnabled(false);
		start_time_btn.setEnabled(false);
		end_time_btn.setEnabled(false);
		task_surveypeoplename_btn.setEnabled(false);// �����
		task_enforcement_certificate_no_edt.setEnabled(false);// ִ��֤��
		task_work_units_edt.setEnabled(false);// ������λ
		task_recordpeopleid_enforcement_certificate_no_edt.setEnabled(false);// ��¼��ִ��֤��
	
		task_recordpeopleid_work_units_edt.setEnabled(false);// ��¼�˹�����λ
		task_entcode_edt.setEnabled(false);// ��ȾԴ����
		task_company_address_edt.setEnabled(false);// �����ַ
		task_surveyaddress_edt.setEnabled(false);
		task_checkpeople_edt.setEnabled(false);// ���˴���
		// task_checkpeople_position_edt.setEnabled(false);// ���˴���ְ��
		task_checkpeople_phone_edt.setEnabled(false);// ���˴���绰
		task_checkpeople_zip_edt.setEnabled(false);// ���˴����ʱ�
		task_business_license_edt.setEnabled(false);// Ӫҵִ��ע���
		task_organization_edt.setEnabled(false);// ��֯��������
		task_dutypeople_edt.setEnabled(false);// �ֳ�����������
		task_dutypeopleoffice_edt.setEnabled(false);// �ֳ�������ְ��
		task_dutypeoplerelation_edt.setEnabled(false);// �뱾����ϵ
		task_dutypeopletel_edt.setEnabled(false);// �ֳ������˵绰
		task_otherpeopleaddress_edt.setEnabled(false);// �����μ���������������λ
		task_confirm_body_edt.setEnabled(false);// ȷ������
		task_sitecondition_edt.setEnabled(false);// �ֳ����

		task_dutypeople_age_edt.setEnabled(false);// ��ѯ���˵�����
		task_natruepeople_edt.setEnabled(false);// ��ѯ����Ȼ�˵�����
		task_natruepeople_age_edt.setEnabled(false);// ��ѯ����Ȼ�˵�����
		task_natruepeopleoffice_edt.setEnabled(false);// ��ѯ����Ȼ�˵�ְ��
		task_natruepeopletel_edt.setEnabled(false);// ��ѯ����Ȼ�˵ĵ绰
		task_natruepeopleaddress_edt.setEnabled(false);// ��ѯ����Ȼ�˵�סַ

		task_surveypeoplename_btn.setEnabled(false);
		task_recordpeopleid_sp.setEnabled(false);
		task_site_enfor_add_btn.setEnabled(false);
		task_site_enfor_wd_btn.setEnabled(false);
		
		beijiancharen.setEnabled(false);
		farendaibiao.setEnabled(false);
		xianchangfuzeren.setEnabled(false);
		shengfenzheng.setEnabled(false);
		gongzuodanwei.setEnabled(false);
		zhiwu.setEnabled(false);
		yubenanguanxi.setEnabled(false);
		dizhi.setEnabled(false);
		dianhua.setEnabled(false);
		qitacanjiaren.setEnabled(false);
		
		// �������֪ͨ���еĿؼ�
	}

	public void no_edit() {
		���ܵ���ʱ��.setEnabled(false);
		Я������.setEnabled(false);
		���鵥λ��ַ.setEnabled(false);
		֪ͨ����.setEnabled(false);
		�ֳ�������.setEnabled(false);
		��������ϵ�绰.setEnabled(false);
		Υ������.setEnabled(false);
		Υ����Ϊ.setEnabled(false);
		�־�.setEnabled(false);
		����.setEnabled(false);
		��������.setEnabled(false);
		�ֳ������.setEnabled(false);
		�ֳ��������ϵ��ַ.setEnabled(false);
		�ֳ��������ϵ�绰.setEnabled(false);

	}

	/**
	 * ��ȡһ��һ��view
	 * 
	 * @return
	 */
	public LinearLayout getWDView() {
		LayoutInflater inflater = LayoutInflater.from(TaskSiteEnforcementActivity.this);
		return (LinearLayout) inflater.inflate(R.layout.zfws_ywyd, null);
	}

	/** ������� */
	public void clear_data() {
		task_surveypeoplename_btn.setText("");// �����
		task_enforcement_certificate_no_edt.setText("");// ִ��֤��
//		task_work_units_edt.setText("");// ������λ
		task_recordpeopleid_enforcement_certificate_no_edt.setText("");// ��¼��ִ��֤��
//		task_recordpeopleid_work_units_edt.setText("");// ��¼�˹�����λ
		task_entcode_edt.setText("");// ��ȾԴ����
		task_company_address_edt.setText("");// �����ַ
		task_surveyaddress_edt.setText("");
		task_checkpeople_edt.setText("");// ���˴���
		// task_checkpeople_position_edt.setText("");// ���˴���ְ��
		task_checkpeople_phone_edt.setText("");// ���˴���绰
		task_checkpeople_zip_edt.setText("");// ���˴����ʱ�
		task_business_license_edt.setText("");// Ӫҵִ��ע���
		task_organization_edt.setText("");// ��֯��������
		task_dutypeople_edt.setText("");// �ֳ�����������
		task_dutypeopleoffice_edt.setText("");// �ֳ�������ְ��
		task_dutypeoplerelation_edt.setText("");// �뱾����ϵ
		task_dutypeopletel_edt.setText("");// �ֳ������˵绰
		task_otherpeopleaddress_edt.setText("");// �����μ���������������λ
		task_confirm_body_edt.setText("");// ȷ������
		task_sitecondition_edt.setText("");// �ֳ����
		task_type_edt.setText("");// ����

		task_dutypeople_age_edt.setText("");// ��ѯ��������
		task_natruepeople_edt.setText("");// ��ѯ����Ȼ�˵�����
		task_natruepeople_age_edt.setText("");// ��ѯ����Ȼ�˵�����
		task_natruepeopleoffice_edt.setText("");// ��ѯ����Ȼ�˵�ְ��
		task_natruepeopletel_edt.setText("");// ��ѯ����Ȼ�˵ĵ绰
		task_natruepeopleaddress_edt.setText("");// ��ѯ����Ȼ�˵�סַ

		beijiancharen.setText("");
		farendaibiao.setText("");
		xianchangfuzeren.setText("");
		shengfenzheng.setText("");
//		gongzuodanwei.setText("");
		zhiwu.setText("");
		yubenanguanxi.setText("");
		dizhi.setText("");
		dianhua.setText("");
		qitacanjiaren.setText("");
		
		task_site_enfor_wd_layout.removeAllViews();

	}

	private EditText task_entcode_edt, task_enforcement_certificate_no_edt, task_recordpeopleid_enforcement_certificate_no_edt, task_checkpeople_edt, task_dutypeople_edt,
			task_dutypeopleoffice_edt, task_dutypeoplerelation_edt, task_dutypeopletel_edt, task_otherpeopleaddress_edt, task_confirm_body_edt, task_sitecondition_edt,
			task_type_edt, task_company_address_edt, task_surveyaddress_edt, task_work_units_edt, task_recordpeopleid_work_units_edt, task_dutypeople_age_edt,
			task_natruepeople_edt, task_natruepeople_age_edt, task_natruepeopleoffice_edt, task_natruepeopletel_edt, task_natruepeopleaddress_edt;
	private EditText task_checkpeople_phone_edt, task_checkpeople_zip_edt, task_business_license_edt, task_organization_edt;
	private Button button_xunwen, button_ziranren;
	private String task_entcode_edt_str, task_checkpeople_phone_edt_str, task_checkpeople_zip_edt_str, task_enforcement_certificate_no_edt_str, task_business_license_edt_str,
			task_organization_edt_str, task_recordpeopleid_enforcement_certificate_no_edt_str, task_checkpeople_edt_str, task_dutypeople_edt_str, task_dutypeopleoffice_edt_str,
			task_dutypeoplerelation_edt_str, task_dutypeopletel_edt_str, task_otherpeopleaddress_edt_str, task_confirm_body_edt_str, task_sitecondition_edt_str, task_type_edt_str,
			task_companyaddress_edt_str, task_surveyaddress_edt_str, task_work_units_edt_str, task_recordpeopleid_work_units_edt_str, task_dutypeople_age_edt_str,
			task_natruepeople_edt_str, task_natruepeople_age_edt_str, task_natruepeopleoffice_edt_str, task_natruepeopletel_edt_str, task_natruepeopleaddress_edt_str;;

	private Spinner task_recordpeopleid_sp;

	private String task_surveypeoplename_btn_str, task_recordpeopleid_sp_str;
	private String task_naturepeople_gender_spinner_str, task_dutypeople_gender_spinner_str;

	/** ѯ�ʱ�¼��Ҫ�ı���ʾ������ */
	private TextView task_surveypeoplename_one_tv, task_entcode_tv, task_dutypeople_tv;

	/**�����¼������*/
	private LinearLayout xianchangkanchabilu;
	/**�����¼����*/
	private EditText beijiancharen,farendaibiao,xianchangfuzeren,shengfenzheng,gongzuodanwei,zhiwu,yubenanguanxi,dizhi,dianhua,qitacanjiaren;
	
	
	/** ѯ�ʱ�¼�ʴ𲼾� */
	private LinearLayout task_site_enfor_wd_layout;
	/** �����¼����ĵ�ַ */
	private LinearLayout task_surveyaddress_lay;
	/** ѯ�ʱ�¼���еĲ��� */
	private LinearLayout xwbldybj;
	/** ��飨���죩��� */
	private LinearLayout task_sitcondtion_lay;
	/** ѯ�ʱ�¼�ʴ�ť */
	private Button task_site_enfor_wd_btn, task_site_enfor_add_btn;

	/***/
	private FrameLayout task_site_joint_frlayout;
	private LinearLayout task_site_scoll_out;// ����ѯ�ʱ�¼����
	// private ScrollView task_site_scoll;
	// �������֪ͨ���еĿؼ�

	private LinearLayout task_site_scoll_out_jieshou;// ����֪ͨ��Ĳ���
	private TextView task_name_tv_jieshou, task_address_tv;
	private TextView task_name_tv_jiancha;

	private EditText ���鵥λ��ַ, ֪ͨ����;
	private EditText ���鵥λ;
	private Button ���ܵ���ʱ��;
	private Button Я������;
	private EditText �ֳ�������;
	private EditText ��������ϵ�绰;
	private EditText Υ������;
	private EditText Υ����Ϊ;
	private EditText �־�, ����;
	private EditText ��������;
	private EditText �ֳ������, �ֳ��������ϵ��ַ, �ֳ��������ϵ�绰;

	// �ֳ���������¼�Ĳ���
	private LinearLayout task_site_scoll_out_jiancha;// �ֳ���������¼�Ĳ���

	private LinearLayout task_site_scoll_jiancha;
	LayoutInflater inflater;
	ListView zhtree;

	private class QdjcAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;
		TextView textView1 = null;

		public QdjcAdapter(ArrayList<HashMap<String, Object>> list, Context context) {
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = inflater.inflate(R.layout.qdjcmb_item, null);

			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			String tname = list.get(position).get("name").toString();
			textView1.setText(tname);
			String code = list.get(position).get("code").toString();
			if (tname.equals("���ڻ������⼰���")) {
				String sqlStr = "select CZHJWT from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid + "'";
				String question = SqliteUtil.getInstance().getDepidByDepName(sqlStr);
				if (question != null && !question.equals("")) {
					textView1.setTextColor(Color.RED);
				}
			} else if (tname.equals("�������")) {
				String sqlString = "select SCQK_SCZK,SCQK_YLYL,SCQK_CPCL,HBZD_WAQYS,HBZD_WSPJS,JFQK_FS,JFQK_FQ,JFQK_FZ,JFQK_ZS  from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
						+ rwbh
						+ "' and entid = '"
						+ qyid
						+ "' and (SCQK_SCZK <> '' or SCQK_YLYL <> '' or SCQK_CPCL <> '' or HBZD_WAQYS  <> '' or HBZD_WSPJS <> '' or JFQK_FS <> '' or JFQK_FQ <> '' or JFQK_FZ <> '' or JFQK_ZS <> '')";
				ArrayList<HashMap<String, Object>> jbqk1 = new ArrayList<HashMap<String, Object>>();
				jbqk1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlString);
				if (jbqk1.size() > 0) {
					textView1.setTextColor(Color.RED);
				}
			} else if (tname.equals("��Ⱦ������ʩ�����������")) {
				String sqlString = "select  WRZL_FS,WRZL_FQ,WRZL_WRW,WRZL_GTFW,WRZL_ZS,WRZL_XWS from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '"
						+ qyid + "' and (WRZL_FS <> '' or WRZL_FQ <> '' or WRZL_WRW <> '' or WRZL_GTFW <> '' or WRZL_ZS <> '' or WRZL_XWS <> '')";
				ArrayList<HashMap<String, Object>> jbqk3 = new ArrayList<HashMap<String, Object>>();
				jbqk3 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlString);
				if (jbqk3.size() > 0) {
					textView1.setTextColor(Color.RED);
				}
			} else if (tname.equals("��Ⱦ���ŷ����")) {
				String sqlString = "select  WRW_FS_PFQX,WRW_FS_YZND,WRW_FS_PFBZ,WRW_FS_CBYY,WRW_FS_ZXJC,WRW_FS_CBYY1,WRW_FS_JDJC,WRW_FS_ZXJCCB,WRW_FQ_KHSD,WRW_FQ_YZND,WRW_FQ_PFBZ,WRW_FQ_CBYY,WRW_FQ_ZXJC,WRW_FQ_CBYY1,WRW_FQ_JDJC,WRW_FQ_ZXJCCB,WRW_GTFW_FWCZ,WRW_GTFW_FWZC,WRW_GTFW_FWZY,WRW_ZS_GNQY,WRW_ZS_PFBZ,WRW_ZS_JCCB,WRW_ZS_CBYY from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
						+ rwbh
						+ "' and entid = '"
						+ qyid
						+ "' and (WRW_FS_PFQX <> '' or WRW_FS_YZND <> '' or WRW_FS_PFBZ <> '' or WRW_FS_CBYY <> '' or WRW_FS_ZXJC <> '' or WRW_FS_CBYY1 <> '' or WRW_FS_JDJC <> '' or WRW_FS_ZXJCCB <> '' or WRW_FQ_KHSD <> '' or WRW_FQ_YZND <> '' or WRW_FQ_PFBZ <> '' or WRW_FQ_CBYY <> '' or WRW_FQ_ZXJC <> '' or WRW_FQ_CBYY1 <> '' or WRW_FQ_JDJC <> '' or WRW_FQ_ZXJCCB <> '' or WRW_GTFW_FWCZ <> '' or WRW_GTFW_FWZC <> '' or WRW_GTFW_FWZY <> '' or WRW_ZS_GNQY <> '' or WRW_ZS_PFBZ <> '' or WRW_ZS_JCCB <> '' or WRW_ZS_CBYY <> '')";
				ArrayList<HashMap<String, Object>> jbqk2 = new ArrayList<HashMap<String, Object>>();
				jbqk2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlString);
				if (jbqk2.size() > 0) {
					textView1.setTextColor(Color.RED);
				}
			}
			return convertView;
		}
	}

	private class ItemAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;

		public ItemAdapter(ArrayList<HashMap<String, Object>> list, Context context) {
			this.list = list;
			inflater = LayoutInflater.from(context);

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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listitem, null);
			} else {
				convertView = new View(TaskSiteEnforcementActivity.this);
			}
			// Image Left
			ImageView imageleft = (ImageView) convertView.findViewById(R.id.listitem_left_image);
			imageleft.setImageResource(R.drawable.icon_left_not_checked);

			// TextView Center
			TextView tv = (TextView) convertView.findViewById(R.id.listitem_text);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(5, 5, 0, 0);
			tv.setText(list.get(position).get("name").toString());
			String nameString = itemList.get(position).get("name").toString();
			if (nameString.equals("�������")) {
				String sqlStr = "select SCQK_SCZK,SCQK_YLYL,SCQK_CPCL from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid
						+ "' and (SCQK_SCZK <> '' or SCQK_YLYL <> '' or SCQK_CPCL <> '')";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("������Ŀִ�л����ƶ����")) {
				String sqlStr = "select HBZD_WAQYS,HBZD_WSPJS from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid
						+ "' and (HBZD_WAQYS <> '' or HBZD_WSPJS <> '' )";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("���۷ѽ������")) {
				String sqlStr = "select JFQK_FS,JFQK_FQ,JFQK_FZ,JFQK_ZS from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid
						+ "' and (JFQK_FS not in ('0','') or JFQK_FQ not in ('0','') or JFQK_FZ not in ('0','') or JFQK_ZS not in ('0',''))";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("����")) {
				String sqlStr = "select WRW_FQ_KHSD,WRW_FQ_YZND,WRW_FQ_PFBZ,WRW_FQ_CBYY,WRW_FQ_ZXJC,WRW_FQ_CBYY1,WRW_FQ_JDJC,WRW_FQ_ZXJCCB from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
						+ rwbh
						+ "' and entid = '"
						+ qyid
						+ "' and (WRW_FQ_KHSD <> '' or WRW_FQ_YZND <> '' or WRW_FQ_PFBZ <> '' or WRW_FQ_CBYY <> '' or WRW_FQ_ZXJC <> '' or WRW_FQ_CBYY1 <> '' or WRW_FQ_JDJC <> '' or WRW_FQ_ZXJCCB <> '')";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("��ˮ")) {
				String sqlStr = "select WRW_FS_PFQX,WRW_FS_YZND,WRW_FS_PFBZ,WRW_FS_CBYY,WRW_FS_ZXJC,WRW_FS_CBYY1,WRW_FS_JDJC,WRW_FS_ZXJCCB from YDZF_SiteEnvironmentMonitorRecord where taskid = '"
						+ rwbh
						+ "' and entid = '"
						+ qyid
						+ "' and (WRW_FS_PFQX <> '' or WRW_FS_YZND <> '' or WRW_FS_PFBZ <> '' or WRW_FS_CBYY <> '' or WRW_FS_ZXJC <> '' or WRW_FS_CBYY1 <> '' or WRW_FS_JDJC <> '' or WRW_FS_ZXJCCB <> '')";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("�������")) {

				String sqlStr = "select WRW_GTFW_FWCZ,WRW_GTFW_FWZC,WRW_GTFW_FWZY from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid
						+ "' and (WRW_GTFW_FWCZ <> '' or WRW_GTFW_FWZC <> '' or WRW_GTFW_FWZY <> '')";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			} else if (nameString.equals("����")) {

				String sqlStr = "select WRW_ZS_GNQY,WRW_ZS_PFBZ,WRW_ZS_JCCB,WRW_ZS_CBYY from YDZF_SiteEnvironmentMonitorRecord where taskid = '" + rwbh + "' and entid = '" + qyid
						+ "' and (WRW_ZS_GNQY <> ''  or WRW_ZS_PFBZ <> '' or WRW_ZS_JCCB <> '' or WRW_ZS_CBYY <> '' )";
				ArrayList<HashMap<String, Object>> sczk = new ArrayList<HashMap<String, Object>>();
				sczk = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlStr);
				if (sczk.size() > 0) {
					imageleft.setImageResource(R.drawable.icon_left_checked);
				}
			}
			// ImageView Right
			ImageView imageRight = (ImageView) convertView.findViewById(R.id.listitem_image);
			imageRight.setPadding(5, 0, 0, 0);

			return convertView;
		}
	}

	YutuLoading yutuLoading;
	ItemAdapter itemAdapter;
	private String codeStr;
	private boolean isSearch;

	public class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(TaskSiteEnforcementActivity.this);
			yutuLoading.setLoadMsg("���ݼ����У����Ե�...", "");
			yutuLoading.showDialog();
		}

		@Override
		protected String doInBackground(String... params) {

			String code = params[0];
			itemList = new ArrayList<HashMap<String, Object>>();
			if ("1".equals(code)) {
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("code", "10");
				hashmap.put("name", "�������");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "11");
				hashmap.put("name", "������Ŀִ�л����ƶ����");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "12");
				hashmap.put("name", "���۷ѽ������");
				itemList.add(hashmap);

			} else if ("2".equals(code)) {
				Intent intent = new Intent();
				Context context = TaskSiteEnforcementActivity.this;

				intent.putExtra("entid", qyid);
				intent.putExtra("taskid", rwbh);
				intent.putExtra("code", "20");
				intent.putExtra("name", "��Ⱦ������ʩ�����������");
				intent.setClass(context, SuperviseItemActivity.class);
				startActivityForResult(intent, 1);
				context = null;

				return null;
			} else if ("3".equals(code)) {
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("code", "30");
				hashmap.put("name", "��ˮ");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "31");
				hashmap.put("name", "����");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "32");
				hashmap.put("name", "�������");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "33");
				hashmap.put("name", "����");
				itemList.add(hashmap);
			} else if ("4".equals(code)) {
				Intent intent = new Intent();
				Context context = TaskSiteEnforcementActivity.this;

				intent.putExtra("entid", qyid);
				intent.putExtra("taskid", rwbh);
				intent.putExtra("code", "40");
				intent.putExtra("name", "���ڻ������⼰���");
				intent.setClass(context, SuperviseItemActivity.class);
				startActivityForResult(intent, 1);
				context = null;

				return null;
			}
			itemAdapter = new ItemAdapter(itemList, TaskSiteEnforcementActivity.this);

			Message msg = handler.obtainMessage();
			msg.arg1 = GETDATA_SUCCESS;
			msg.obj = itemAdapter;
			handler.sendMessage(msg);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
				yutuLoading = null;
			}
			super.onPostExecute(result);
		}
	}

}
