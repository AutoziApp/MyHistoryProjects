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
import java.util.LinkedList;
import java.util.List;
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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.widget.CustomAutoCompleteTextView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.TaskSiteEnforcementActivity.CommonLawPeopleAdapter;
import com.mapuni.android.helper.LawKnowAllActivity;
import com.mapuni.android.ui.Timeuitl;
import com.mapuni.android.uitl.PanduanDayin;

/**
 * @author SS
 * 
 *         Description 现场执法里的执法按钮，点击进入-----现场勘察笔录
 * 
 *         Incoming parameters 企业代码、任务编号、(可传入)任务状态
 */
public class TaskSimplelawEnforcementActivity extends BaseActivity implements OnItemClickListener {

	/** 数据保存异常 */
	private final int DATABASE_INSERT_EXCEPTION = 0;
	/** 数据保存成功 */
	private final int DATA_INSERT_SUCCESS = 1;
	/** 数据查询成功 */
	private final int DATA_QUERY_SUCCESS = 2;
	/** 该用户不存在 */
	private final int THE_USER_OR_ENT_NOT_EXIST = 3;
	/** 数据更新失败 */
	private final int DATA_UPDATE_EXCEPTION = 4;
	/** 数据更新成功 */
	private final int DATA_UPDATE_SUCCESS = 5;
	/** 返回键保存/更新数据 */
	private final int BACK_DATA_INSERT_SUCCESS = 6;
	/** 打印预览保存/更新数据 */
	private final int PREVIEW_SAVE_DATA_SUCCESS = 7;
	/** 清单检查执法模板数据更新成功 */
	private final int CHECK_LIST_DATA_UPDATE_SUCCESS = 8;
	/** 结果上传保存/更新数据 */
	private final int UPLOAD_SAVE_DATA_SUCCESS = 9;
	/** 执法百事通保存/更新数据 */
	private final int PEPSI_SAVE_DATA_SUCCESS = 10;
	/** 现场取证数据 */

	private final int SCENE_UPDATE_DATA_SUCCESS = 11;
	/** 现场环境监察记录点击 */
	private final int GETDATA_SUCCESS = 12;
	/** 标题布局 */
	private RelativeLayout two_list_tool_layout;
	/** 任务ID */
	protected String RWID;
	/** 任务详情 */
	protected HashMap<String, Object> RWDetail;
	/** bundle对象 */
	protected Bundle RWBundle;
	/** 任务信息 */
	protected RWXX rwxx;
	/** 任务信息属性 */
	protected ArrayList<HashMap<String, Object>> rwxxAttachment;
	/** 获取文件路径 */
	protected String path;
	/** 定义一个进度条 */
	private ProgressDialog progressDialog;
	/** 定义一个对话框 */
	private AlertDialog alertDialog;
	/** 开始时间按钮、结束时间按钮 */
	private Button start_time_btn, end_time_btn;
	/** 获取当前文档的名称 */
	private TextView task_name_tv;
	/** 查询全部数据集合 */
	private ArrayList<HashMap<String, Object>> queryData = null;
	/** 查询企业基本信息的数据集合 */
	private ArrayList<HashMap<String, Object>> queryEntData = null;
	/** 查询勘察人的数据集合 */
	private ArrayList<HashMap<String, Object>> queryExeData = null;
	/** 查询问题的数据集合 */
	private ArrayList<HashMap<String, Object>> queryProblemData = null;
	/** 查询任务状态数据集合 */
	private ArrayList<HashMap<String, Object>> queryTaskData = null;
	/** 获得选中的勘察人的工作单位 */
	ArrayList<HashMap<String, Object>> deptData = null;
	/** 绑定spiNner勘察人数据 */
	private List<String> exeList = null;
	/** 清单检查按钮 */
	private Button task_site_check_list_btn;
	/** 执法百事通 */
	private Button task_site_law_enforcement_btn;
	/** 清单检查执法模板Spinner */
	private Spinner task_site_enfor_sp;
	/** 获得所选择的执法模板ID */

	private String task_site_enfor_sp_id;
	/** 所选中的Spinner项 */
	private String task_site_enfor_sp_str = null;
	/** 企业代码 */
	private String qyid;
	/** 任务ID、任务编号 */
	private String rwbh;
	/** 判断任务状态的标志、0代表待执行、1代表已完成、2代表执行中 */
	private String qyzts;
	/** 向数据库中插入 */
	private ContentValues contentValues = null;

	/** 选择语言模板ListView形式 */
	private ListView task_site_enfor_lv;

	/** 询问笔录选择问题的类型 */
	private CustomAutoCompleteTextView autoCompleteTv;
	/** 共同执法人 */
	StringBuffer sbZhr = new StringBuffer();

	/** 选择记录人的按钮 */
	private Button task_surveypeoplename_btn;

	/** 选择共同执法人弹出二级列表 */
	private ExpandableListView task_site_enfor_expandable_lv;

	/** 二级列表绑定子列表的数据集合 */
	private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;

	/** 获得纪录人的数据集合 */
	private ArrayList<String> recordList;

	/** 2013年11月15日 修改 获得新的纪录人的数据集合 */
	private ArrayList<String> recordNewList = new ArrayList<String>();

	/** 记录人数据适配器 */
	private ArrayAdapter<String> recordAdapter;
	private String dengerList[] = { "男", "女" };
	private String record[] = { "男", "女" };
	private ArrayAdapter<String> dengerAdapter;
	private ArrayAdapter<String> natrueDengerAdapter;
	String str, s;
	String[] s1 = new String[1];
	String[] str1 = new String[3];
	/** 数据库工具类 */
	private SqliteUtil sqliteUtil;
	TextView tv2;

	/**勘察笔录填入项*/
	private LinearLayout xianchangkanchabilu;
	/**勘察笔录内容*/
	private EditText beijiancharen,farendaibiao,xianchangfuzeren,shengfenzheng,gongzuodanwei,zhiwu,yubenanguanxi,dizhi,dianhua,qitacanjiaren;
	
	
	
	
	private LinkedList<String> wDlinkedList = new LinkedList<String>();
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case DATABASE_INSERT_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSimplelawEnforcementActivity.this, "数据保存异常", Toast.LENGTH_SHORT).show();
				break;

			case DATA_INSERT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}

				Intent saveIntent = new Intent(TaskSimplelawEnforcementActivity.this, SiteWriteRecordActivity.class);

				saveIntent.putExtra("rwbh", rwbh);

				startActivity(saveIntent);
				break;

			case DATA_QUERY_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (recordList == null || recordList.size() == 0) {
					recordList = new ArrayList<String>();
					recordList.add("-请先选择询问人-");
					ArrayAdapter<String> rAdapter = new ArrayAdapter<String>(TaskSimplelawEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
					rAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					task_recordpeopleid_sp.setAdapter(rAdapter);
				}

				if (queryData != null && queryData.size() > 0) {
					/** 初始化界面绑定数据 */
					bind_EntAndExeData();
					/** 初始化界面绑定数据 */
					bind_data();
				} else {
					/** 初始化界面绑定数据 */
					/** 设置默认时间为当前时间 */
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
					// end_time_btn.setText(currtime);
					// bind_EntAndExeData();
				}

				if (queryProblemData != null && queryProblemData.size() > 0) {

					for (int i = 0; i < queryProblemData.size(); i++) {
						final LinearLayout wdLayout = getWDView();
						final EditText askTv = (EditText) wdLayout.findViewById(R.id.ywyd_ask);
						askTv.setTextSize(15);
						askTv.setText(queryProblemData.get(i).get("wtnr").toString());
						askTv.setTextColor(android.graphics.Color.BLACK);
						// askTv.setFocusable(false);
						final EditText ansTv = (EditText) wdLayout.findViewById(R.id.ywyd_question);
						askTv.setTextSize(15);
						//BYK rwzt
						if ("3".equals(qyzts)) {
//							if ("1".equals(qyzts)) {
							askTv.setFocusable(false);
						}
						if (queryProblemData.get(i).get("result").toString() != null && !queryProblemData.get(i).get("result").toString().equals("")) {
							ansTv.setText(queryProblemData.get(i).get("result").toString());
						}
						task_site_enfor_wd_layout.addView(wdLayout);

						askTv.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
										.setPositiveButton("是", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												//BYK rwzt
												if ("3".equalsIgnoreCase(qyzts)) {
//													if ("1".equalsIgnoreCase(qyzts)) {
													Toast.makeText(TaskSimplelawEnforcementActivity.this, "任务已上传，不能删除记录。", Toast.LENGTH_SHORT).show();
												} else {
													task_site_enfor_wd_layout.removeView(wdLayout);
													Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
												}

											}
										}).setNegativeButton("否", null).create().show();
								return true;
							};
						});
						ansTv.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
										.setPositiveButton("是", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {

												task_site_enfor_wd_layout.removeView(wdLayout);
												Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

											}
										}).setNegativeButton("否", null).create().show();
								return true;
							};
						});
						wdLayout.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {

								new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
										.setPositiveButton("是", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {

												task_site_enfor_wd_layout.removeView(wdLayout);
												Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

											}
										}).setNegativeButton("否", null).create().show();
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
				Toast.makeText(TaskSimplelawEnforcementActivity.this, "该用户或者该企业不存在 ", Toast.LENGTH_SHORT).show();
				break;

			case DATA_UPDATE_EXCEPTION:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSimplelawEnforcementActivity.this, "数据更新失败 ", Toast.LENGTH_SHORT).show();
				break;

			case DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Toast.makeText(TaskSimplelawEnforcementActivity.this, "数据更新成功 ", Toast.LENGTH_SHORT).show();
				Intent updateIntent = new Intent(TaskSimplelawEnforcementActivity.this, SiteWriteRecordActivity.class);

				updateIntent.putExtra("qyid", qyid);
				updateIntent.putExtra("rwbh", rwbh);

				startActivity(updateIntent);
				break;

			case BACK_DATA_INSERT_SUCCESS:

				if (uploadFlag) {
					int bool = new RWXX().uploadSimpleLawXWBL(rwbh);
					Log.i("info", "提交时候任务编号：" + rwbh + "bool:" + bool);

					if (bool == 1) {
						Toast.makeText(TaskSimplelawEnforcementActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
						//BYK rwzt
						String sql = "update TaskEnprilink set isexcute ='3' where TaskID='" + rwbh + "'";

						sqliteUtil.execute(sql);
						//修改上传之后的任务状态 BYK
						String sql2 = "update T_YDZF_RWXX set RWZT='006' where RWBH='" + rwbh + "'";

						sqliteUtil.execute(sql2);
						finish();
					} else if (bool == 2) {
						String sql = "update TaskEnprilink set isexcute ='3' where TaskID='" + rwbh + "'";

						sqliteUtil.execute(sql);
						//修改上传之后的任务状态 BYK
						String sql2 = "update T_YDZF_RWXX set RWZT='006' where RWBH='" + rwbh + "'";

						sqliteUtil.execute(sql2);
						Toast.makeText(TaskSimplelawEnforcementActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
						finish();
					} else if (bool == 0) {
						Toast.makeText(TaskSimplelawEnforcementActivity.this, "上传失败！", Toast.LENGTH_LONG).show();
					}
				} else {
					finish();
				}
				if (progressDialog != null) {
					progressDialog.cancel();
				}

				break;

			case PREVIEW_SAVE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				if (task_name_tv.getText().toString().contains("询问")) {
					PreviewWrit_lnxw_simple plnxw = new PreviewWrit_lnxw_simple(rwbh, qyid, task_type_edt_str, TaskSimplelawEnforcementActivity.this);
					String yon = plnxw.create();
					if (yon.equals("y")) {
						File file1 = new File(plnxw.getPath() + "/第1页.html");
						if (!PanduanDayin.appIsInstalled(TaskSimplelawEnforcementActivity.this, "com.dynamixsoftware.printershare")) {
							PanduanDayin.insatll(TaskSimplelawEnforcementActivity.this);
							return;
						}
						PanduanDayin.startprintshare(TaskSimplelawEnforcementActivity.this, file1.getAbsolutePath());
					} else {
						Toast.makeText(TaskSimplelawEnforcementActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case CHECK_LIST_DATA_UPDATE_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent intent = new Intent(TaskSimplelawEnforcementActivity.this, QDJCActivity.class);
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

					// rwxx.uploadTask(rwbh,	`
					// TaskSimplelawEnforcementActivity.this,
					// qyid);
				}
				break;

			case PEPSI_SAVE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent pepsi_save_intent = new Intent(TaskSimplelawEnforcementActivity.this, LawKnowAllActivity.class);
				startActivity(pepsi_save_intent);
				break;

			case SCENE_UPDATE_DATA_SUCCESS:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				Intent scene_update_intent = new Intent(TaskSimplelawEnforcementActivity.this, SiteEvidenceActivity.class);
				scene_update_intent.putExtra("currentTaskID", rwbh);
				scene_update_intent.putExtra("qyid", qyid);
				startActivity(scene_update_intent);
				break;
			case GETDATA_SUCCESS:
				ItemAdapter itemAdapter = (ItemAdapter) msg.obj;
				zhtree.setAdapter(itemAdapter);
				zhtree.setOnItemClickListener(TaskSimplelawEnforcementActivity.this);
				itemAdapter.notifyDataSetChanged();

				break;
			}
		};
	};

	/** 初始化时间控件 */
	private final Calendar dateAndTime = Calendar.getInstance();
	/** 格式化时间的格式 */
	SimpleDateFormat datefmtDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timefmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 获得开始时间和结束时间的字符串 */
	String startTime = datefmtDate.format(dateAndTime.getTime()) + " 00:00:00";
	String endTime = datefmtDate.format(dateAndTime.getTime()) + " 23:59:59";
	/** 获得系统当前时间 */
	String currtime = timefmtDate.format(new java.util.Date());

	/** 日期监听 */
	OnDateSetListener dateStart = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {// 将Piacker中的年月日赋给calendar对象
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, month);
			dateAndTime.set(Calendar.DAY_OF_MONTH, day);

			TimePickerDialog timedialog = new TimePickerDialog(TaskSimplelawEnforcementActivity.this, timeStart, dateAndTime.get(Calendar.HOUR_OF_DAY),
					dateAndTime.get(Calendar.MINUTE), true);
			timedialog.show();
		}
	};

	OnDateSetListener dateStop = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {// 将Piacker中的年月日赋给calendar对象
			// dateAndTime.set(Calendar.YEAR, year);
			// dateAndTime.set(Calendar.MONTH, month);
			// dateAndTime.set(Calendar.DAY_OF_MONTH, day);
			TimePickerDialog timedialog = new TimePickerDialog(TaskSimplelawEnforcementActivity.this, timeStop, dateAndTime.get(Calendar.HOUR_OF_DAY),
					dateAndTime.get(Calendar.MINUTE), true);
			timedialog.show();
		}
	};

	/** 时间监听 */
	OnTimeSetListener timeStart = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			start_time_btn.setText(timefmtDate.format(dateAndTime.getTime()));
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
		task_type_edt_str = "0";
		/** 任务编号 */
		rwbh = intent.getStringExtra("rwbh");
		if (rwbh == null || "".equals(rwbh)) {
			SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
			/** 生成规则 */
			rwbh = "T" + formatdate.format(new Date());
			createOneEnforcementTask("简易执法_企业名称", Global.getGlobalInstance().getUserid());
		}
		/** 初始化页面 */
		findViewById();
		task_name_tv.setText("调查询问笔录");
		queryTaskData = new ArrayList<HashMap<String, Object>>();
		/** 根据任务编号和企业id查询出任务状态 */
		queryTaskData = sqliteUtil.queryBySqlReturnArrayListHashMap("select isexcute from TaskEnpriLink where taskid = '" + rwbh + "'");
		if (queryTaskData != null && queryTaskData.size() > 0) {
			qyzts = queryTaskData.get(0).get("isexcute").toString();
		}
		if (qyzts != null) {
			String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
			String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
			String userSql = "select fbr FROM T_YDZF_RWXX where rwbh = '" + rwbh + "'";
			String task_userId = SqliteUtil.getInstance().getDepidByUserid(userSql);
			String login_user = Global.getGlobalInstance().getUserid();
			Log.i("info", "状态：" + qyzts);
			if (qyzts.equalsIgnoreCase("3")) {
				//BYK rwzt
//				if (qyzts.equalsIgnoreCase("1")) {
				// if ("1".equals(ZW) || "2".equals(ZW)) {
				// if (!login_user.equals(task_userId)) {
				Toast.makeText(this, "当前企业调查取证已完成，不能对笔录进行编辑", Toast.LENGTH_LONG).show();
				/** 初始化页面不可编辑 */
				not_edit();
				// }

				// }
			}
		}
		/** 选择语言模板ListVIew方式 */
		language_lv_temp();

		task_site_scoll_out.setVisibility(View.VISIBLE);
		task_site_scoll_out_jieshou.setVisibility(View.GONE);
		task_site_scoll_out_jiancha.setVisibility(View.GONE);

		task_surveypeoplename_one_tv.setText("询问人姓名*:");
		LinearLayout ll_qymc = (LinearLayout) findViewById(R.id.ll_qymc);
		ll_qymc.setVisibility(View.VISIBLE);
		task_entcode_tv.setText("企业名称*:");
		// task_dutypeople_tv.setText("被询问人姓名:");
		task_sitecondition_edt.setVisibility(View.GONE);
		task_site_enfor_wd_layout.setVisibility(View.VISIBLE);
		task_site_enfor_wd_btn.setVisibility(View.VISIBLE);
		task_site_enfor_add_btn.setVisibility(View.VISIBLE);

		querySteData();

	}

	/** 选择共同执法人按钮监听事件 */
	public void together_people_click(View view) {
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
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
				Toast.makeText(TaskSimplelawEnforcementActivity.this,"查询人员时出错!请同步人员表!",Toast.LENGTH_SHORT).show();
				return ;
			}
		} catch (Exception e) {
		}
		ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();
		if ("1".equals(depparentid)) {
			/** 根据用户登录用户的id查询 */
			login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from PC_DepartmentInfo where depid = '230000000departA'");	//已经是黑龙江省环境监察总队人员,只能查询下级部门
		}else {
			/** 根据用户登录用户的id查询 */
			login_user_data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from PC_DepartmentInfo where depid = '"+depparentid+"'");			
		}
		/** 获得登录用户父部门的名称 */
		String depParentName = login_user_data.get(0).get("depname").toString();
		/** 获得登录用户父部门的id */
		String depID = login_user_data.get(0).get("depid").toString();
		/** 设置部门名称 */
		title_tv.setText(depParentName);

		ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("DepParentID", depID);
		conditions.put("syncDataRegionCode", Global.getGlobalInstance().getAreaCode());
		SqliteUtil.getInstance().recursiveQueryOrder("PC_DepartmentInfo", depData, conditions, "depid,depname", false, "depid");
		final List<String> groupList = new ArrayList<String>();
		childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
		for (HashMap<String, Object> map : depData) {
			groupList.add(map.get("depname").toString());
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("DepID", map.get("depid").toString());
			condition.put("syncDataRegionCode", Global.getGlobalInstance().getAreaCode());
			ArrayList<HashMap<String, Object>> usersData = SqliteUtil.getInstance().getList("UserID,U_RealName", condition, "PC_Users");
			childMapData.add(usersData);
		}
		StringBuffer stringBuffer = new StringBuffer();
		/** 用户id集合 */
		final LinkedList<String> linkedList = new LinkedList<String>();
		/** 用户姓名集合 */
		final LinkedList<String> linkedName = new LinkedList<String>();
		/** 二级列表设置适配器 */
		task_site_enfor_expandable_lv.setAdapter(new CommonLawPeopleAdapter(groupList, childMapData, linkedList, linkedName, TaskSimplelawEnforcementActivity.this));

		/** 二级列表中子列表的点击事件 */
		task_site_enfor_expandable_lv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CheckBox two_class_cb = (CheckBox) v.findViewById(R.id.two_class_cb);

				String userCheckedId = childMapData.get(groupPosition).get(childPosition).get("userid").toString();
				if (!userCheckedId.equals(Global.getGlobalInstance().getUserid())) {
					two_class_cb.toggle();
				}
				

				ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
				/** 获得选中的检查人的执法证号数据集合 */
				zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '"
						+ childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() + "'");
				if (zfzhData != null && zfzhData.size() > 0) {
					/** 检查人的执法证号 */
					task_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
				}
				if (childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() != null
						&& !childMapData.get(groupPosition).get(childPosition).get("u_realname").toString().equals("")) {
					/** 获得选中的 检查人的部门 */
					ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
					explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
									+ childMapData.get(groupPosition).get(childPosition).get("u_realname").toString() + "'");
					if (explorData != null && explorData.size() > 0) {
						/** 获得选中的检查人的部门ID */
						String deptid = explorData.get(0).get("depparentid").toString();
						/** 获得选中的检查人的工作单位 */
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
						/** 添加执行人 */
						recordList.add(exeStr);
					}
				}

				return false;
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this);
		builder.setTitle("请选择共同执法人");
		builder.setIcon(getResources().getDrawable(R.drawable.yutu));
		builder.setView(v);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (linkedList.size() < 2) {
					task_surveypeoplename_btn.setText("");
					recordList = new ArrayList<String>();
					recordList.add("-请先选择检查人-");
					recordAdapter = new ArrayAdapter<String>(TaskSimplelawEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
					recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					task_recordpeopleid_sp.setAdapter(recordAdapter);
					if (recordList != null && recordList.size() > 1) {
						task_recordpeopleid_sp.setSelection(0, true);
					}
					Toast.makeText(TaskSimplelawEnforcementActivity.this, "除了自己，请至少再选择一位执法人！", Toast.LENGTH_LONG).show();
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
					/** 获得选中的勘察人名称 */
					recordList.add(linkedName.get(i));
				}
				if (sbZhr.length() > 0) {
					sbZhr.deleteCharAt(sbZhr.length() - 1);
				}
				if (userName.length() > 0) {
					userName.deleteCharAt(userName.length() - 1);
				}
				task_surveypeoplename_btn.setText(userName.toString());
				/** 去掉第一个默认的当前用户名 */
				// recordList.remove(0);

				/** 记录人的SpiNner绑定数据 */
				recordAdapter = new ArrayAdapter<String>(TaskSimplelawEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
				recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				task_recordpeopleid_sp.setAdapter(recordAdapter);
				if (recordList != null && recordList.size() > 1) {
					task_recordpeopleid_sp.setSelection(0, true);
				}
				task_recordpeopleid_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						/** 获得选中的记录人的姓名 */
						task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString();
						ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
						/** 获得选中的记录人的执法证号数据集合 */
						zfzhData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
						if (zfzhData != null && zfzhData.size() > 0) {
							/** 记录人的执法证号 */
							task_recordpeopleid_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
						}

						if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
							/** 获得选中的记录人的部门 */
							ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
							explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
									"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
											+ task_recordpeopleid_sp_str + "'");
							if (explorData != null && explorData.size() > 0) {
								/** 获得选中的记录人的部门ID */
								String deptid = explorData.get(0).get("depparentid").toString();
								/** 获得选中的记录人的工作单位 */
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
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog = builder.create();
		alertDialog.show();
	}

	/**
	 * 生成一条现场执法任务
	 * 
	 * @param
	 * 
	 * @param rwmc
	 *            任务名称
	 * @param executorID
	 *            执行人id字符串 用逗号分隔
	 * @return
	 */
	public String createOneEnforcementTask(String rwmc, String executorID) {

		// String rwbh = returnRWBH();
		String rwguid = UUID.randomUUID().toString();

		String updateTime = Global.getGlobalInstance().getDate();
		String fbsj = updateTime;
		String rwzt = RWXX.RWZT_ON_EXECUTION;
		/** 执行中（手机端） */

		ContentValues cv = new ContentValues();
		cv.put("rwbh", rwbh);
		cv.put("guid", rwguid);
		cv.put("rwly", "013");// 简易执法的任务来源为"013"
		cv.put("rwlx", "006");
		/** 现场执法任务类型值：临时性监察 */
		cv.put("updatetime", updateTime);
		cv.put("fbsj", fbsj);
		cv.put("rwzt", rwzt);
		cv.put("rwmc", rwmc);
		cv.put("fbr", Global.getGlobalInstance().getUserid());
		cv.put("ssks", Global.getGlobalInstance().getDepId());
		cv.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
		long bjqx = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);// 默认当前日期延后三天为办结期限
		cv.put("bjqx", Global.getGlobalInstance().getDate(new Date(bjqx), "yyyy-MM-dd"));
		cv.put("bz", "简易执法");

		long result = SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX");
		if (result > 0) {// 向任务信息user表插入数据
			ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
			String[] executorIDArr = executorID.split(",");
			for (int i = 0; i < executorIDArr.length; i++) {
				ContentValues cvRwUsers = new ContentValues();
				cvRwUsers.put("RWXXBH", rwbh);
				cvRwUsers.put("UserID", executorIDArr[i]);
				cvRwUsers.put("UpdateTime", updateTime);

				contentValues.add(cvRwUsers);
			}
			SqliteUtil.getInstance().insert(contentValues, "T_YDZF_RWXX_USER");

			ArrayList<ContentValues> taskEnpriLinkValues = new ArrayList<ContentValues>();// 向任务和企业关联表插入数据

			String taskEnpriLinkGUID = UUID.randomUUID().toString();
			ContentValues qyRwUsers = new ContentValues();
			qyRwUsers.put("TaskID", rwbh);
			qyRwUsers.put("IsExcute", "2");// 执行中
			qyRwUsers.put("UpdateTime", updateTime);
			qyRwUsers.put("guid", taskEnpriLinkGUID);
			qyRwUsers.put("qyid", "");
			taskEnpriLinkValues.add(qyRwUsers);

			SqliteUtil.getInstance().insert(taskEnpriLinkValues, "TaskEnpriLink");

		} else {
			rwbh = "";
		}
		return rwbh;

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

			super(context, mLayout, items);// 上下文环境/布局文件/填充布局文件数据
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

		HashMap<Integer, Integer> wdId = new HashMap<Integer, Integer>();// 存储选择的问题的id

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

	/** 选择问题类型按钮监听事件 */
	MyAdapter mAdapter;

	public void question_type(final ArrayList<HashMap<String, Object>> questryAnsData, final AutoCompleteTextView autoTv) {
		ArrayList<String> questryAnsMap = new ArrayList<String>();
		for (int i = 0; i < questryAnsData.size(); i++) {
			questryAnsMap.add(questryAnsData.get(i).get("wfxwlx").toString().trim());
		}

		// ArrayAdapter<String> questryAnsAdapter = new ArrayAdapter<String>(
		// TaskSiteEnforcementActivity.this,
		// // R.layout.xunwen_moban, questryAnsMap);
		// android.R.layout.simple_dropdown_item_1line, questryAnsMap);

		MyTemplateAdapter myTemplateAdapter = new MyTemplateAdapter(TaskSimplelawEnforcementActivity.this, questryAnsMap);
		autoTv.setAdapter(myTemplateAdapter);

		// autoTv.setAdapter(questryAnsAdapter);
		/** AutoCompleteTextView autoTv 获得焦点时候阻止输入法弹出 */
		// autoTv.setOnTouchListener(new OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// int inType = autoTv.getInputType(); // backup the input type
		// autoTv.setInputType(InputType.TYPE_NULL); // disable soft input
		// autoTv.onTouchEvent(event); // call native handler
		// autoTv.setInputType(inType); // restore input type
		// return true;
		// }
		// });
		autoTv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				autoTv.setFocusable(false);// 点击之后禁止获取焦点
				lanuageData = new ArrayList<HashMap<String, Object>>();
				// lanuageList = new ArrayList<String>();
				/** 查询出语言模板数据集合 */
				lanuageData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_WTZD where pid = '" + questryAnsData.get(arg2).get("id").toString() + "'");
				if (lanuageData != null && lanuageData.size() > 0) {

					mAdapter = new MyAdapter(TaskSimplelawEnforcementActivity.this, lanuageData, arg2);
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

	/** 添加自定义调查笔录 */
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

				new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
						.setPositiveButton("是", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								task_site_enfor_wd_layout.removeView(wdLayout);

								Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("否", null).create().show();
				return true;
			}
		});
		askTv.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {

				new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
						.setPositiveButton("是", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								task_site_enfor_wd_layout.removeView(wdLayout);
								Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("否", null).create().show();
				return true;
			};
		});
		ansTv.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {

				new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
						.setPositiveButton("是", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								task_site_enfor_wd_layout.removeView(wdLayout);
								Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

							}
						}).setNegativeButton("否", null).create().show();
				return true;
			};
		});
		task_site_enfor_wd_layout.addView(wdLayout);
	}

	int qNumber = 0;

	/** 选择问答按钮监听事件 */
	public void add_que_and_answers(View view) {
		LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
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

		AlertDialog.Builder builder = new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this);
		builder.setTitle("选择询问笔录模板");
		builder.setIcon(getResources().getDrawable(R.drawable.yutu));
		builder.setPositiveButton("确定", new OnClickListener() {
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

							new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
									.setPositiveButton("是", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											task_site_enfor_wd_layout.removeView(wdLayout);

											Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("否", null).create().show();
							return true;
						}
					});
					askTv.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {

							new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
									.setPositiveButton("是", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

											task_site_enfor_wd_layout.removeView(wdLayout);
											Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("否", null).create().show();
							return true;
						};
					});
					ansTv.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {

							new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setIcon(R.drawable.yutu).setTitle("提示信息").setMessage("是否删除该条问答记录")
									.setPositiveButton("是", new OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

											task_site_enfor_wd_layout.removeView(wdLayout);
											Toast.makeText(TaskSimplelawEnforcementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

										}
									}).setNegativeButton("否", null).create().show();
							return true;
						};
					});
					task_site_enfor_wd_layout.addView(wdLayout);
				}
				wDlinkedList.clear();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

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

	/** 语言模板数据集合 */
	private ArrayList<String> lanuageList;
	/** 数据库查询出语言模板数据集合 */
	private ArrayList<HashMap<String, Object>> lanuageData;

	/** 选择语言模板ListVIew方式 */
	public void language_lv_temp() {

		/** 现场情况输入框长按事件 */
		task_sitecondition_edt.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {

				LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
				View v = inflater.inflate(R.layout.task_site_joint_law_enforcement, null);
				task_site_enfor_lv = (ListView) v.findViewById(R.id.task_site_enfor_lv);
				/** 根据标题名称查询语言模板 */
				if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {
					lanuageData = new ArrayList<HashMap<String, Object>>();
					lanuageList = new ArrayList<String>();
					/** 查询出语言模板数据集合 */
					lanuageData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from T_ZFWS_XWJLWD");
					if (lanuageData != null && lanuageData.size() > 0) {
						SimpleAdapter lanuageAdapter = new SimpleAdapter(TaskSimplelawEnforcementActivity.this, lanuageData, R.layout.task_site_enforcement_launage_lvitem,
								new String[] { "wtnr", "result" }, new int[] { R.id.task_site_launage_lv_left_tv, R.id.task_site_launage_lv_right_tv });
						task_site_enfor_lv.setAdapter(lanuageAdapter);
					}

					task_site_enfor_lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							/** 获得输入框中的内容 */
							task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();
							/** 语言模板累加 */
							String task_site_enfors = "";
							/** 获得语言模板选择项的内容 */
							task_site_enfors += lanuageData.get(arg2).get("wtnr").toString() + "\n答: " + lanuageData.get(arg2).get("result").toString();
							if (!task_site_enfors.equals("")) {
								if (task_sitecondition_edt_str != null && !task_sitecondition_edt_str.equals("")) {
									/** 设置输入框中的内容 */
									task_sitecondition_edt.setText(task_sitecondition_edt_str + "," + task_site_enfors);
								} else {
									/** 设置输入框中的内容 */
									task_sitecondition_edt.setText(task_site_enfors);
								}
							}

							if (!task_site_enfors.equals("")) {
								alertDialog.cancel();
							}
						}
					});

				}
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this);
				builder.setTitle("请选择语言模板");
				builder.setIcon(getResources().getDrawable(R.drawable.yutu));
				builder.setView(v);
				alertDialog = builder.create();
				alertDialog.show();

				return false;
			}
		});
	}

	/** 选择语言模板Spinner方式 */
	public void language_Spinner_temp() {
		/** 现场情况输入框长按事件 */
		task_sitecondition_edt.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {

				LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
				View v = inflater.inflate(R.layout.task_site_enforcement_template, null);
				task_site_enfor_sp = (Spinner) v.findViewById(R.id.task_site_enfor_sp);
				/** Spinner数据集合 */
				ArrayList<String> lanuageList = new ArrayList<String>();
				lanuageList.add("-请选择语言模板-");
				ArrayList<HashMap<String, Object>> lanuageData = new ArrayList<HashMap<String, Object>>();
				/** 查询出语言模板数据集合 */
				lanuageData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from T_YDZF_BLCreateTemplate");
				if (lanuageData != null && lanuageData.size() > 0) {
					for (int i = 0; i < lanuageData.size(); i++) {
						String lanuageStr = lanuageData.get(i).get("templatecontent").toString();
						lanuageList.add(lanuageStr);
					}
				}
				/** 语言模板适配器 */
				ArrayAdapter<String> lanuageAdapter = null;
				lanuageAdapter = new ArrayAdapter<String>(TaskSimplelawEnforcementActivity.this, android.R.layout.simple_spinner_item, lanuageList);
				lanuageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				task_site_enfor_sp.setAdapter(lanuageAdapter);
				task_site_enfor_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						/** 获得输入框中的内容 */
						task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();
						String task_site_enfors = "";
						/** 获得语言模板选择项的内容 */
						task_site_enfors += task_site_enfor_sp.getSelectedItem().toString();
						if (!task_site_enfors.equals("-请选择语言模板-")) {
							if (task_sitecondition_edt_str != null && !task_sitecondition_edt_str.equals("")) {
								/** 设置输入框中的内容 */
								task_sitecondition_edt.setText(task_sitecondition_edt_str + "," + task_site_enfors);
							} else {
								/** 设置输入框中的内容 */
								task_sitecondition_edt.setText(task_site_enfors);
							}
						}

						if (!task_site_enfors.equals("-请选择语言模板-")) {
							alertDialog.cancel();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

				AlertDialog.Builder builder = new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this);
				builder.setTitle("请选择语言模板");
				builder.setIcon(getResources().getDrawable(R.drawable.yutu));
				builder.setView(v);
				alertDialog = builder.create();
				alertDialog.show();

				return false;
			}
		});
	}

	/** 根据企业代码和勘察笔录类型判断界面的初始化查询 */
	public void querySteData() {

		/** 刷新记录人数据适配器 */
		if (recordList != null && recordList.size() > 0) {
			recordList.clear();
			if (recordAdapter != null) {
				recordAdapter.notifyDataSetChanged();
			}
		}

		progressDialog = new ProgressDialog(TaskSimplelawEnforcementActivity.this);
		progressDialog.setMessage("正在努力加载中...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {

				if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {
					/** 根据任务编号和企业代码查询出询问笔录表中对应的数据集合 */
					String sql = "select * from T_ZFWS_XWBL where taskid = '" + rwbh + "'" + " and type = '" + task_type_edt_str + "'";
					queryData = new ArrayList<HashMap<String, Object>>();
					queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);
				}

				if (qyid != null && !qyid.equals("")) {
					/** 查询企业基本信息的数据集合 */
					queryEntData = new ArrayList<HashMap<String, Object>>();

				}

				if (rwbh != null && !rwbh.equals("")) {
					/** 查询执行人的数据集合 */
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

					/** 查询问题的数据集合 */
					queryProblemData = new ArrayList<HashMap<String, Object>>();
					queryProblemData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_XWJLWD where taskid = '" + rwbh + "'");
				}

				/** 查询出添加问答的条数 */
				ArrayList<HashMap<String, Object>> wdData = new ArrayList<HashMap<String, Object>>();

				if (wdData != null && wdData.size() > 0) {
					qNumber = wdData.size();
				}

				Message msg = handler.obtainMessage();
				msg.arg1 = DATA_QUERY_SUCCESS;
				// Looper.prepare();
				handler.sendMessage(msg);
			}
		}).start();
	}

	/** 数据查询成功绑定数据 */
	public void bind_data() {
		String xwrxmStr = "";
		if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {
			
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
			task_entcode_edt.setText(queryData.get(0).get("dutypeopleentprisename").toString());// 污染源名称(获得企业名称)
			task_dutypeopleoffice_edt.setText(queryData.get(0).get("askedpeopleduty").toString()); // 被询问人职务
			task_surveyaddress_edt.setText(queryData.get(0).get("surveyaddress").toString());// 调查地址

			task_company_address_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());
			task_checkpeople_edt.setText(queryData.get(0).get("dutypeoplename").toString());// 法人代表姓名及职务
			// task_checkpeople_position_edt.setText("");// 检查人职务
			task_checkpeople_phone_edt.setText(queryData.get(0).get("dutypeopletel").toString());// 法人代表电话
			task_checkpeople_zip_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());// 邮政编码
			task_business_license_edt.setText(queryData.get(0).get("yyzzzch").toString());// 营业执照注册号
			task_organization_edt.setText(queryData.get(0).get("zzjgdm").toString());// 组织机构代码
			task_natruepeople_age_edt.setText(queryData.get(0).get("otherpeopleage").toString());// 被询问自然人的年龄
			task_natruepeopleoffice_edt.setText(queryData.get(0).get("otherpeopleentprisename").toString());// 被询问自然人的职务
			task_natruepeopletel_edt.setText(queryData.get(0).get("otherpeopletel").toString());// 被询问自然人的电话
			task_natruepeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// 被询问自然人的住址
			task_otherpeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// 其他参加人员的姓名和地址

			task_dutypeopletel_edt.setText(queryData.get(0).get("askedpeopletel").toString());// 被询问人的电话
			task_dutypeople_age_edt.setText(queryData.get(0).get("askedpeopleage").toString());// 被询问人的年龄
			task_natruepeople_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());// 被询问自然人的姓名

			task_surveypeoplename_btn.setText(xwrxmStr);

			start_time_btn.setText(queryData.get(0).get("surveystartdate").toString());// 开始时间
			end_time_btn.setText(queryData.get(0).get("surveyenddate").toString());// 结束时间
			task_dutypeoplerelation_edt.setText(queryData.get(0).get("otherpeoplerelation").toString());// 与本案关系
			task_type_edt.setText(queryData.get(0).get("type").toString());// 类型
			if("".equals(queryData.get(0).get("recordpeopleunit").toString()) || null == queryData.get(0).get("recordpeopleunit").toString()){
				task_work_units_edt.setText(Global.getGlobalInstance().getUserUnit());
			}else {
				task_work_units_edt.setText(queryData.get(0).get("recordpeopleunit").toString());
			}
			
			task_askedpeoplename_edt.setText(queryData.get(0).get("askedpeoplename").toString());
			task_askedpeopleage_edt.setText(queryData.get(0).get("askedpeopleage").toString());
			task_askedpeopleidnumber_edt.setText(queryData.get(0).get("askedpeopleidnumber").toString());
			task_askedpeopleunit_edt.setText(queryData.get(0).get("surveyentprisename").toString());
			task_askedpeoplepostcode_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());
			String sex = queryData.get(0).get("askedpeoplesex").toString()+"";
			if ("男".equals(sex)) {
				((RadioButton)findViewById(R.id.rb_boy)).setChecked(true);
			}else if ("女".equals(sex)) {
				((RadioButton)findViewById(R.id.rb_gril)).setChecked(true);
			}
		} else if (task_name_tv.getText().toString().trim().equals("现场检查（勘察）笔录")) {
			task_dutypeople_edt.setText(queryData.get(0).get("dutypeople").toString());// 现场负责人
			task_dutypeopleoffice_edt.setText(queryData.get(0).get("dutypeopleoffice").toString());// 现场负责人职务
			task_dutypeopletel_edt.setText(queryData.get(0).get("surveypeoplecradcode").toString());// 现场负责人电话
			task_sitecondition_edt.setText(queryData.get(0).get("sitecondition").toString());// 现场情况

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

			task_surveypeoplename_btn.setText(xwrxmStr);// 获得检查人的姓名
			ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
			/** 获得检查人的执法证号数据集合 */
			zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + queryData.get(0).get("surveypeoplename").toString() + "'");
			if (zfzhData != null && zfzhData.size() > 0) {
				/** 检查人的执法证号 */
				task_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
			}
			if (queryData.get(0).get("surveypeoplename").toString() != null && !queryData.get(0).get("surveypeoplename").toString().equals("")) {
				/** 获得选中的 检查人的部门 */
				ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
				explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
								+ queryData.get(0).get("surveypeoplename").toString() + "'");
				if (explorData != null && explorData.size() > 0) {
					/** 获得选中的检查人的部门ID */
					String deptid = explorData.get(0).get("depparentid").toString();
					/** 获得选中的检查人的工作单位 */
					deptData = sqliteUtil.queryBySqlReturnArrayListHashMap("select depname from PC_DepartmentInfo where depid = '" + deptid + "'");
					if (deptData != null && deptData.size() > 0) {
						task_work_units_edt.setText(deptData.get(0).get("depname").toString());
					}
				}
			}
			
			String enName = "";
			try {
				getIntent().getStringExtra("jyzfqymc");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String dutyName = queryData.get(0).get("recordpeopleunit").toString();
			task_entcode_edt.setText(!"".equals(dutyName) ? dutyName : enName);// 污染源名称(获得企业名称)

			task_company_address_edt.setText(queryData.get(0).get("dutypeopleaddress").toString());// 调查地址

			task_surveyaddress_edt.setText(queryData.get(0).get("surveryaddress").toString());

			task_checkpeople_edt.setText(queryData.get(0).get("checkpeople").toString());// 法人代表姓名及职务
		
			task_checkpeople_phone_edt.setText(queryData.get(0).get("dutypeopletel").toString());// 法人代表电话
			task_checkpeople_zip_edt.setText(queryData.get(0).get("dutypeopleyzbm").toString());// 邮政编码
			task_business_license_edt.setText(queryData.get(0).get("yyzzzch").toString());// 营业执照注册号
			task_organization_edt.setText(queryData.get(0).get("zzjgdm").toString());// 组织机构代码

			start_time_btn.setText(queryData.get(0).get("surveystartdate").toString());// 开始时间
			end_time_btn.setText(queryData.get(0).get("surveyenddate").toString());// 结束时间
			task_dutypeoplerelation_edt.setText(queryData.get(0).get("dutypeoplerelation").toString());// 与本案关系
			task_otherpeopleaddress_edt.setText(queryData.get(0).get("otherpeopleaddress").toString());// 其他参加人员的姓名和地址
			task_type_edt.setText(queryData.get(0).get("type").toString());// 类型
//			task_work_units_edt.setText(queryData.get(0).get("surveypeopleunit").toString());//记录人工作单位
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
			

		} else if (task_name_tv.getText().toString().trim().equals("环境监察通知书")) {
			// 这些数据应该写入一个hashMap中，使用Key来对应值，大大减小冗余代码
			违法行为.setText(queryData.get(0).get("breakbehavior").toString());
			String surveytimestr = queryData.get(0).get("surveytime").toString();
			if (surveytimestr.contains("T")) {
				String values = (String) surveytimestr.subSequence(0, surveytimestr.length() - 6);
				surveytimestr = values.replace("T", " ");
			}
			接受调查时间.setText(surveytimestr);
			接受调查时间.setTag(surveytimestr);
			携带资料.setText(queryData.get(0).get("takematerial").toString());
			现场检查人.setText(queryData.get(0).get("contactperson").toString());
			现场检查人联系电话.setText(queryData.get(0).get("phone").toString());
			现场检查人联系地址.setText(queryData.get(0).get("address").toString());

			通知书编号.setText(queryData.get(0).get("noticeno").toString());
			通知书编号.setTag(queryData.get(0).get("guid").toString());
			被查单位地址.setText(queryData.get(0).get("bjcdz").toString());
			现场接收人.setText(queryData.get(0).get("xcjsr").toString());
			接收人联系电话.setText(queryData.get(0).get("bjclxdh").toString());
			分局.setText(queryData.get(0).get("fj").toString());
			科室.setText(queryData.get(0).get("ks").toString());
			其它资料.setText(queryData.get(0).get("qtzl").toString());
			违反法律.setText(queryData.get(0).get("wffl").toString());
			被查单位.setText(queryData.get(0).get("entname").toString());
			return;
		}

		recordList = new ArrayList<String>();
		String recordpeoplename = queryData.get(0).get("recordpeoplename").toString();
		String[] surveyStr = null;
		if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {
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
				recordList.add("-请先选择检查人-");
			}
		} else {
			recordList.add("-请先选择检查人-");
		}
		for (int i = 0; i < surveyStr.length; i++) {
			if (surveyStr[i] != null && surveyStr[i].trim().length() > 0) {
				recordList.add(surveyStr[i].trim());
				if (surveyStr[i].trim().equals(recordpeoplename)) {
					integer = i;
				}
			}
		}

		recordAdapter = new ArrayAdapter<String>(TaskSimplelawEnforcementActivity.this, android.R.layout.simple_spinner_item, recordList);
		recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		task_recordpeopleid_sp.setAdapter(recordAdapter);
		task_recordpeopleid_sp.setSelection(integer, true);
		task_recordpeopleid_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/** 获得选中的记录人的姓名 */
				task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString();
				ArrayList<HashMap<String, Object>> zfzhData = new ArrayList<HashMap<String, Object>>();
				/** 获得选中的记录人的执法证号数据集合 */
				zfzhData = sqliteUtil.queryBySqlReturnArrayListHashMap("select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
				if (zfzhData != null && zfzhData.size() > 0) {
					/** 记录人的执法证号 */
					task_recordpeopleid_enforcement_certificate_no_edt.setText(zfzhData.get(0).get("zfzh").toString());
				}

				if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
					/** 获得选中的记录人的部门 */
					ArrayList<HashMap<String, Object>> explorData = new ArrayList<HashMap<String, Object>>();
					explorData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select depparentid from PC_Users INNER JOIN PC_DepartmentInfo ON PC_Users.DepID = PC_DepartmentInfo.DepID where u_realname = '"
									+ task_recordpeopleid_sp_str + "'");
					if (explorData != null && explorData.size() > 0) {
						/** 获得选中的记录人的部门ID */
						String deptid = explorData.get(0).get("depparentid").toString();
						/** 获得选中的记录人的工作单位 */
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

	/** 初始化时间、绑定企业名称和执行人的相关数据 */
	public void bind_EntAndExeData() {

		if (queryEntData != null && queryEntData.size() > 0) {
			// task_entcode_edt.setText(queryEntData.get(0).get("dutypeopleentprisename").toString());//
			// 污染源名称(获得企业名称)
//			qymc = queryEntData.get(0).get("qymc").toString();
			try {
				task_entcode_edt.setText(getIntent().getStringExtra("jyzfqymc"));
			} catch (Exception e) {
			}
			
			task_checkpeople_edt.setText(queryEntData.get(0).get("frdb").toString());// 法人代表
			/*
			 * task_checkpeople_phone_edt.setText(queryEntData.get(0)
			 * .get("frdbdh").toString());// 法人代表电话
			 */// task_checkpeople_phone_edt.setTextColor(color.gray);
			task_checkpeople_zip_edt.setText(queryEntData.get(0).get("yzbm").toString());// 邮政编码
			// task_checkpeople_zip_edt.setTextColor(color.gray);
			task_business_license_edt.setText(queryEntData.get(0).get("yyzzdm").toString());// 营业执照注册号
			// task_business_license_edt.setTextColor(color.gray);
			task_organization_edt.setText(queryEntData.get(0).get("zzjgdn").toString());// 组织机构代码
			// task_organization_edt.setTextColor(color.gray);
		}

		/** 确认正文 */
		task_confirm_body_edt.setText("我们是" + Global.getGlobalInstance().getUserUnit()
				+ "的执法人员,这是我们的执法证件,请过目确认。今天我们依法进行检查并了解有关情况,你应当配合调查,如实回答询问和提供材料,不得拒绝、阻碍、隐瞒或者提供虚假情况。如果你认为我们与案有利害关系,可能影响公正办案,可以申请我们回避,并说明理由。清楚了吗?");

		/** 设置默认时间为当前时间 */
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
		接受调查时间.setText(Timeuitl.getsystemtine());
	}

	/** 获取输入的内容 */
	public void getData() {
		task_companyaddress_edt_str = task_company_address_edt.getText().toString();// 调查地址
		// task_checkpeople_position_edt.setText("");//
		// 检查人职务task_surveyaddress_edt

		task_dutypeople_age_edt_str = task_dutypeople_age_edt.getText().toString();// 被询问人的年龄

		task_natruepeople_edt_str = task_natruepeople_edt.getText().toString();// 被询问自然人的姓名
		task_natruepeople_age_edt_str = task_natruepeople_age_edt.getText().toString();// 被询问自然人年龄
		task_natruepeopleoffice_edt_str = task_natruepeopleoffice_edt.getText().toString();// 被询问自然人职务
		task_natruepeopletel_edt_str = task_natruepeopletel_edt.getText().toString();// 被询问自然人电话
		task_natruepeopleaddress_edt_str = task_natruepeopleaddress_edt.getText().toString();// 被询问自然人地址

		task_surveyaddress_edt_str = task_surveyaddress_edt.getText().toString();
		task_checkpeople_phone_edt_str = task_checkpeople_phone_edt.getText().toString();// 法人代表电话
		task_checkpeople_zip_edt_str = task_checkpeople_zip_edt.getText().toString();// 邮政编码
		task_business_license_edt_str = task_business_license_edt.getText().toString();// 营业执照注册号
		task_organization_edt_str = task_organization_edt.getText().toString();// 组织机构代码

		task_entcode_edt_str = task_entcode_edt.getText().toString();// 污染源(企业)名称
		task_enforcement_certificate_no_edt_str = task_enforcement_certificate_no_edt.getText().toString();// 检查人执法证号
//		task_work_units_edt_str = task_work_units_edt.getText().toString();// 检查人的工作单位
		task_work_units_edt_str = Global.getGlobalInstance().getUserUnit();
		task_recordpeopleid_enforcement_certificate_no_edt_str = task_recordpeopleid_enforcement_certificate_no_edt.getText().toString();// 记录人执法证号
		
//		task_recordpeopleid_work_units_edt_str = task_recordpeopleid_work_units_edt.getText().toString();// 记录人的工作单位
		task_recordpeopleid_work_units_edt_str = Global.getGlobalInstance().getUserUnit();
		
		task_checkpeople_edt_str = task_checkpeople_edt.getText().toString();// 法人代表
		task_dutypeople_edt_str = task_dutypeople_edt.getText().toString();// 现场负责人
		task_dutypeopleoffice_edt_str = task_dutypeopleoffice_edt.getText().toString();// 现场负责人职务
		task_dutypeoplerelation_edt_str = task_dutypeoplerelation_edt.getText().toString();// 现场负责人与本案关系
		task_dutypeopletel_edt_str = task_dutypeopletel_edt.getText().toString();// 现场负责人的电话
		task_otherpeopleaddress_edt_str = task_otherpeopleaddress_edt.getText().toString();// 其他参加人员的姓名及工作单位
		task_confirm_body_edt_str = task_confirm_body_edt.getText().toString();// 确认正文
		task_sitecondition_edt_str = task_sitecondition_edt.getText().toString();// 现场情况

		task_surveypeoplename_btn_str = task_surveypeoplename_btn.getText().toString();// 获得检查人名称
		if (recordList != null && recordList.size() > 0) {
			task_recordpeopleid_sp_str = task_recordpeopleid_sp.getSelectedItem().toString(); // 获得记录人的姓名
		} else {
			task_recordpeopleid_sp_str = "";
		}
		if (dengerList != null && dengerList.length > 0) {
			task_dutypeople_gender_spinner_str = button_xunwen.getText().toString();// 获取被询问人的性别
		} else {
			task_dutypeople_gender_spinner_str = "";
		}

		if (record != null && record.length > 0) {
			task_naturepeople_gender_spinner_str = button_ziranren.getText().toString();// 获取被询问自然人性别
		} else {
			task_naturepeople_gender_spinner_str = "";
		}

	}

	private void saveUpdateData(final int stateFlag) {

		progressDialog = new ProgressDialog(TaskSimplelawEnforcementActivity.this);
		progressDialog.setMessage("正在努力加载数据,请您稍后...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				/** 获取数据 */
				getData();

				if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {
					/** 向数据库中字段插入值 (询问表) */
					content_XW_Valput();
				}

				try {
					
					ContentValues contentValue = new ContentValues();
					contentValue.put("rwmc", "简易执法_" + task_entcode_edt_str);
				
					
					sqliteUtil.update("T_YDZF_RWXX", contentValue, "rwbh=?", new String[] { rwbh });
					if (task_name_tv.getText().toString().trim().equals("调查询问笔录")) {

						int wdViewNum = task_site_enfor_wd_layout.getChildCount();

						String deleteSql = "delete from T_ZFWS_XWJLWD where taskid = '" + rwbh + "'";
						sqliteUtil.ExecSQL(deleteSql);// 删除原有数据，重新插入

						for (int index = 0; index < wdViewNum; index++) {
							LinearLayout chindView = (LinearLayout) task_site_enfor_wd_layout.getChildAt(index);
							EditText askEditText = (EditText) chindView.findViewById(R.id.ywyd_ask);
							EditText questionEditText = (EditText) chindView.findViewById(R.id.ywyd_question);

							ContentValues contentValues = new ContentValues();
							contentValues.put("taskid", rwbh);// 任务ID
							contentValues.put("entid", "");// 污染源(企业)ID

							// 更新问题编辑框
							contentValues.put("wtnr", askEditText.getText().toString());
							//
							contentValues.put("result", questionEditText.getText().toString());

							/** 插入 */
							sqliteUtil.insert(contentValues, "T_ZFWS_XWJLWD");

						}

						/** 根据企业代码和任务编号查询勘察笔录的guId */
						if (rwbh != null && !rwbh.equals("")) {
							ArrayList<HashMap<String, Object>> guidData = new ArrayList<HashMap<String, Object>>();
							guidData = sqliteUtil.queryBySqlReturnArrayListHashMap("select * from T_ZFWS_XWBL where taskid = '" + rwbh + "' and type ='" + task_type_edt_str + "'");

							if (guidData != null && guidData.size() > 0) {
								/** 根据企业代码和任务编号查询勘察笔录的guId */
								String guid = guidData.get(0).get("guid").toString();
								/** 更新数据 */
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
								/** 如果是保存数据则新生成一个GuId */
								String guid = UUID.randomUUID().toString();
								contentValues.put("guid", guid);
								/** 保存任务 */
								long count = sqliteUtil.insert(contentValues, "T_ZFWS_XWBL");
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

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/** 保存数据的方法 */
	public void save_data() {
		saveUpdateData(DATA_INSERT_SUCCESS);
	}

	/** 按返回键时保存/更新数据的方法 */
	public void back_save_data() {
		saveUpdateData(BACK_DATA_INSERT_SUCCESS);
	}

	/** 按打印预览时保存/更新数据的方法 */
	public void preview_save_data() {
		saveUpdateData(PREVIEW_SAVE_DATA_SUCCESS);
	}

	/** 结果上传保存/更新数据的方法 */
	public void Upload_Save_Data() {

		saveUpdateData(UPLOAD_SAVE_DATA_SUCCESS);
	}

	/** 执法百事通保存/更新数据的方法 */
	public void Pepsi_Save_Data() {
		saveUpdateData(PEPSI_SAVE_DATA_SUCCESS);
	}

	/** 现场取证保存/更新数据事件 */
	public void Scene_Save_Data() {
		saveUpdateData(SCENE_UPDATE_DATA_SUCCESS);
	}

	/** 向数据库T_ZFWS_XWBL(询问笔录)表中字段插入值 */
	public void content_XW_Valput() {
		contentValues = new ContentValues();
		contentValues.put("taskid", rwbh);// 任务ID
		contentValues.put("surveystartdate", start_time_btn.getText().toString());// 开始时间
		contentValues.put("surveyenddate", end_time_btn.getText().toString());// 结束时间
		contentValues.put("surveyentcode", "");// 污染源(企业)ID
		contentValues.put("surveyaddress", task_surveyaddress_edt_str);// 地址
		
		/** 根据检查人姓名查询检查人ID数据 */
		ArrayList<HashMap<String, Object>> inveData = new ArrayList<HashMap<String, Object>>();
		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);
		
		if (task_surveypeoplename_btn_str != null && !task_surveypeoplename_btn_str.equals("")&&!task_surveypeoplename_btn_str.equals("-请选择检查人-")) {
			
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
			contentValues.put("surveypeoplecradcode", surveypeopleid.toString());//检查人id
			
		} else {
			contentValues.put("surveypeoplecradcode", "");
			contentValues.put("surveypeoplename", "");//检查人
		}
		
		


		/** 根据记录人姓名查询记录人ID数据集合 */
		inveData = new ArrayList<HashMap<String, Object>>();
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
			inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
			if (inveData != null && inveData.size() > 0) {
				String inveID = inveData.get(0).get("userid").toString();
				if (inveID != null && !inveID.equals("")) {
					contentValues.put("recordpeopleid", inveID);// 记录人的ID
				}
			}
		}
		contentValues.put("recordpeoplename", task_recordpeopleid_sp_str);// 记录人的姓名
		contentValues.put("askedpeoplesex", task_dutypeople_gender_spinner_str);// 被询问人性别
		contentValues.put("otherpeoplesex", task_naturepeople_gender_spinner_str);// 被询问自然人的性别
		contentValues.put("dutypeoplename", task_checkpeople_edt_str);// 法人代表
		contentValues.put("dutypeopleentprisename", task_entcode_edt_str);// 负责人所属单位(工作单位)
//		contentValues.put("surveypeoplecradcode", task_surveypeoplename_btn_str);
		contentValues.put("askedpeopleduty", task_dutypeopleoffice_edt_str);// 被询问人姓名和职务
		contentValues.put("askedpeopleage", task_dutypeople_age_edt_str);// 被询问认得年龄
		contentValues.put("dutypeopleaddress", task_natruepeople_edt_str);// 被询问自然人的姓名
		
		contentValues.put("otherpeopleage", task_natruepeople_age_edt_str);// 被询问自然人的年龄
		contentValues.put("otherpeopleaddress", task_natruepeopleaddress_edt_str);// 被询问自然人的地址
		contentValues.put("otherpeopletel", task_natruepeopletel_edt_str);// 被询问自然人的电话
		contentValues.put("otherpeoplerelation", task_dutypeoplerelation_edt_str);// 与本案关系
		contentValues.put("dutypeopletel", task_checkpeople_phone_edt_str);// 法定负责人电话
		contentValues.put("askedpeopletel", task_dutypeopletel_edt_str);// 被询问人电话
		contentValues.put("yyzzzch", task_business_license_edt.getText().toString());// 企业证照注册号
		contentValues.put("zzjgdm", task_organization_edt_str);// 组织机构代码
		contentValues.put("dutypeopleyzbm", task_checkpeople_zip_edt.getText().toString());
		contentValues.put("recordpeopleunit", task_work_units_edt_str);// 检查人的工作单位
		contentValues.put("type", task_type_edt_str);// 类型
		contentValues.put("askedpeoplename",task_askedpeoplename_edt.getText().toString());//被询问人姓名
		contentValues.put("askedpeopleage",task_askedpeopleage_edt.getText().toString());//被询问人年龄
		contentValues.put("askedpeopleidnumber",task_askedpeopleidnumber_edt.getText().toString());//被询问人身份证号
		
		contentValues.put("surveyentprisename",task_askedpeopleunit_edt.getText().toString());//被询问人工作单位
		contentValues.put("dutypeopleyzbm",task_askedpeoplepostcode_edt.getText().toString());//被询问人邮编
		if (-1 != task_askedpeoplesex_edt.getCheckedRadioButtonId()) {
			contentValues.put("askedpeoplesex",((RadioButton)findViewById(task_askedpeoplesex_edt.getCheckedRadioButtonId())).getText().toString());			
		}else {
			contentValues.put("askedpeoplesex","");
		}
		contentValues.put("updatetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

	/** 向数据库T_ZFWS_KCBL(勘查笔录)表中字段插入值 */
	public void content_KC_Valput() {
		contentValues = new ContentValues();
		contentValues.put("entcode", "");// 污染源(企业)ID
		contentValues.put("guid", "");
		
		/** 根据检查人姓名查询检查人ID数据 */
		ArrayList<HashMap<String, Object>> inveData = new ArrayList<HashMap<String, Object>>();
		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);//检查人
		if (task_surveypeoplename_btn_str != null && !task_surveypeoplename_btn_str.equals("")&&!task_surveypeoplename_btn_str.equals("-请选择检查人-")) {
			
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
			contentValues.put("surveypeoplecradcode", surveypeoplecradcode.toString());//检查人执法证号
			contentValues.put("surveypeopleid", surveypeopleid.toString());//检查人id
			
			
		} else {
			contentValues.put("surveypeoplecradcode", "()");
			contentValues.put("surveypeopleid", "");//检查人id
			contentValues.put("surveypeoplename", "");//检查人
		}
		
		/** 根据记录人姓名查询记录人ID集合 */
//		contentValues.put("recordpeoplename", task_recordpeopleid_sp_str);// 记录人的姓名
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")&&!task_recordpeopleid_sp_str.equals("-请先选择检查人-")) {
			inveData = sqliteUtil.queryBySqlReturnArrayListHashMap("select userid,zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "'");
			if (inveData != null && inveData.size() > 0) {
				String inveID = inveData.get(0).get("userid").toString();
				if (inveID != null && !inveID.equals("")) {
					contentValues.put("recordpeopleid", inveID);// 记录人的ID
				}
				String num = inveData.get(0).get("zfzh").toString();
				if (num != null && !num.equals("") ) {
					contentValues.put("recordpeoplename", task_recordpeopleid_sp_str+"("+num.replaceAll("\\s*|\t|\r|\n", "")+")");// 记录人的执法证号
				}else{
					contentValues.put("recordpeoplename", task_recordpeopleid_sp_str+"()");
				}
			}
		}else{
			contentValues.put("recordpeoplename", "");// 记录人的执法证号
		}
		
		
		contentValues.put("surveystartdate", start_time_btn.getText().toString());// 开始时间
		contentValues.put("surveyenddate", end_time_btn.getText().toString());// 结束时间
		contentValues.put("taskid", rwbh);// 任务ID
		contentValues.put("entprisename", task_entcode_edt_str);// 企业名称
		contentValues.put("surveryaddress", task_surveyaddress_edt_str);// 地址

		contentValues.put("surveypeoplename", task_surveypeoplename_btn_str);// 检查人姓名
		
		contentValues.put("surveypeopleunit", task_work_units_edt_str);// 检查人的工作单位
		String num = "";
		if (task_recordpeopleid_sp_str != null && !task_recordpeopleid_sp_str.equals("")) {
			String sql = "select zfzh from PC_Users where u_realname = '" + task_recordpeopleid_sp_str + "' ";
			num = SqliteUtil.getInstance().getDepidByDepName(sql);
		}
		contentValues.put("recordpeopleid", num);// 记录人的执法证号
		contentValues.put("recordpeopleunit", task_recordpeopleid_work_units_edt_str);// 记录人的工作单位

		contentValues.put("sitecondition", task_sitecondition_edt_str);// 现场情况
//		contentValues.put("qrzw",task_entcode_edt.getText().toString() );//记录人工作单位
		contentValues.put("type", task_type_edt_str);// 类型
		contentValues.put("checkpeople", beijiancharen.getText().toString());
//		contentValues.put("checkpeople", farendaibiao.getText().toString());
		contentValues.put("dutypeople", xianchangfuzeren.getText().toString());
		contentValues.put("dutypeoplecode", shengfenzheng.getText().toString());
		
		contentValues.put("dutypeopledepartment", gongzuodanwei.getText().toString());//现场负责人工作单位
		contentValues.put("dutypeopleoffice", zhiwu.getText().toString());
		contentValues.put("dutypeoplerelation", yubenanguanxi.getText().toString());
		contentValues.put("dutypeopleaddress", dizhi.getText().toString());
		
		//BYK  改电话字段
		contentValues.put("dutypeopletel", dianhua.getText().toString());
		contentValues.put("AskedPeopleTel", dianhua.getText().toString());
		contentValues.put("otherpeopleaddress", qitacanjiaren.getText().toString());
		contentValues.put("updatetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		contentValues.put("dutypeopleyzbm", task_checkpeople_zip_edt.getText().toString());
		
	}

	/** 返回键监听事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (qyzts != null) {
				if (qyzts.equalsIgnoreCase("3")) {
					//BYK rwzt
					//if (qyzts.equalsIgnoreCase("1")) {
					finish();
				} else {
					if (rwbh != null && !rwbh.equals("")) {
						uploadFlag = false;
						/** 返回键保存数据 */
						back_save_data();
					} else {
						finish();
					}
				}
			} else {
				finish();
			}
		}
		return false;
	}

	/** 判断文书选择的类型 */
	void clericalVoid() {
		/** 获得文书的数据集合 */
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

	/** 摄录取证按钮监听事件 */
	public void obtain_evid_click(View view) {
		//if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("1")) {
		//BYK rwzt
			if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("3")) {
			Intent scene_update_intent = new Intent(TaskSimplelawEnforcementActivity.this, SiteEvidenceActivity.class);
			scene_update_intent.putExtra("currentTaskID", rwbh);
			scene_update_intent.putExtra("qyid", qyid);
			startActivity(scene_update_intent);
		} else {
			// 摄录取证按钮监听事件
			Scene_Save_Data();
		}
	}

	/** 清单检查事件监听 */
	public void qdjc_click(View view) {
		if (RWBundle != null) {
			LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
			View v = inflater.inflate(R.layout.task_site_enforcement_template, null);
			task_site_enfor_sp = (Spinner) v.findViewById(R.id.task_site_enfor_sp);

			/** 执法模版适配器 */
			ArrayAdapter<SpinnerItem> mbAdapter;
			/** Spinner数据集合 */
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

			AlertDialog.Builder builder = new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this);
			builder.setTitle("-请选择执法模板-");
			builder.setIcon(getResources().getDrawable(R.drawable.yutu));
			builder.setView(v);
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					progressDialog = new ProgressDialog(TaskSimplelawEnforcementActivity.this);
					progressDialog.setMessage("正在提交数据,请您稍后...");
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
									/** 更新数据 */
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

			builder.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					/** 取消对话框 */
					alertDialog.cancel();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();
		}
	}

	boolean uploadFlag = false;// 是上传还是只是存储

	/** 完成并且上传按钮监听事件 */
	public void record_click(View view) {
		if ("3".equalsIgnoreCase(qyzts)) {
			//BYK rwzt
		//	if ("1".equalsIgnoreCase(qyzts)) {
			Toast.makeText(this, "当前任务已经完成，不能重复上传", Toast.LENGTH_LONG).show();
			return;
		}

		int result = compare();
		if (result == 1) {
			return;
		}
		
		if (task_surveypeoplename_btn.getText().toString() != null && !task_surveypeoplename_btn.getText().toString().equals("")) {
			String str = task_entcode_edt.getText().toString();
			if (str != null && !str.equals("")) {
				// 打印预览按钮保存方法
				uploadFlag = true;
				back_save_data();
			} else {
				Toast.makeText(TaskSimplelawEnforcementActivity.this, "企业名称不能为空", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(TaskSimplelawEnforcementActivity.this, "询问人姓名不能为空", Toast.LENGTH_LONG).show();
		}
	}

	/** 执法百事通按钮监听事件 */
	public void law_enfor_click(View view) {
		//BYK rwzt
		if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("3")) {
		//	if (qyzts != null && !qyzts.equals("") && qyzts.equalsIgnoreCase("1")) {
			Intent pepsi_save_intent = new Intent(TaskSimplelawEnforcementActivity.this, LawKnowAllActivity.class);
			startActivity(pepsi_save_intent);
		} else {
			// 执法百事通按钮保存方法
			Pepsi_Save_Data();
		}
	}

	/** 打印预览按钮监听事件 */
	public void preview_click(View view) {
		// if (qyzts != null && !qyzts.equals("") &&
		// qyzts.equalsIgnoreCase("1")) {
		if (task_name_tv.getText().toString().contains("询问")) {

			int result = compare();
			if (result == 1) {
				return;
			}
//			if (task_surveypeoplename_btn.getText().toString() != null && !task_surveypeoplename_btn.getText().toString().equals("")) {
//				String str = task_entcode_edt.getText().toString();
//				if (str != null && !str.equals("")) {
					// 打印预览按钮保存方法
					preview_save_data();
//				} else {
//					Toast.makeText(TaskSimplelawEnforcementActivity.this, "企业名称不能为空", Toast.LENGTH_LONG).show();
//				}

//			} else {
//				Toast.makeText(TaskSimplelawEnforcementActivity.this, "询问人姓名不能为空", Toast.LENGTH_LONG).show();
//			}
		}
		// }
	}

	int hour, minute1;
	static final int TIME_DIALOG_ID = 0;

	/** 弹出时间控件的监听器 */
	public void update(final View view) {
		switch (view.getId()) {
		case R.id.start_time_btn:
			DatePickerDialog dpd = new DatePickerDialog(TaskSimplelawEnforcementActivity.this, dateStart,// 初始化为当前日期
					dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			dpd.show();
			break;

		case R.id.end_time_btn:
			// DatePickerDialog dpd2 = new DatePickerDialog(
			// TaskSiteEnforcementActivity.this,
			// dateStop,// 初始化为当前日期
			// dateAndTime.get(Calendar.YEAR),
			// dateAndTime.get(Calendar.MONTH),
			// dateAndTime.get(Calendar.DAY_OF_MONTH));
			// dpd2.show();
			showDialog(TIME_DIALOG_ID);

			break;

		default:
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

	/** 判断开始时间不能大于结束时间的方法 */
	public int compare() {
		String timeStart = start_time_btn.getText().toString();
		String timeEnd = end_time_btn.getText().toString();
		if (timeStart.compareTo(timeEnd) >= 0) {
			Toast.makeText(TaskSimplelawEnforcementActivity.this, "请确保结束时间大于开始时间", 1).show();
			return 1;
		} else {
			startTime = timeStart;
			endTime = timeEnd;
			return 0;
		}

	}

	/** 判断“调查时间”不能大于“再次调查时间”的方法 */
	public int compareTo() {
		String oneTime = one_time_btn.getText().toString();
		String againTime = again_time_btn.getText().toString();
		if (oneTime.compareTo(againTime) >= 0) {
			Toast.makeText(TaskSimplelawEnforcementActivity.this, "请确保“再次调查事件”大于“调查时间”", 1).show();
			return 1;
		}
		return 0;
	}

	/** 判断当前页面的数据是否需要保存 */
	boolean bNeedSave() {
		String sql2 = "select isexcute from TaskEnpriLink where qyid ='" + qyid + "' and TaskId='" + rwbh + "'";
		ArrayList<HashMap<String, Object>> data = sqliteUtil.queryBySqlReturnArrayListHashMap(sql2);

		for (int i = 0; i < data.size(); i++) {

			String bStr = (String) data.get(i).get("isexcute");
			//if (bStr.equals("1")) {
			//BYK rwzt
				if (bStr.equals("3")) {
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
		Context context = TaskSimplelawEnforcementActivity.this;
		HashMap<String, Object> hashmap = (HashMap<String, Object>) itemAdapter.getItem(position);
		intent.putExtra("entid", qyid);
		intent.putExtra("taskid", rwbh);
		intent.putExtra("code", hashmap.get("code").toString());
		intent.putExtra("name", hashmap.get("name").toString());
		intent.setClass(context, SuperviseItemActivity.class);
		startActivityForResult(intent, 1);
		context = null;

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/** 选择共同执法人适配器 */
	public class CommonLawPeopleAdapter extends BaseExpandableListAdapter {
		/** 获取第一组共同执法人二级列表中父级列表适配的数据集合 */
		private List<String> groupList;
		/** 获取第一组共同执法人二级列表中查询子级列表数据的集合 */
		private ArrayList<ArrayList<HashMap<String, Object>>> childMapData;
		private final LinkedList<String> usersb;
		private final LinkedList<String> linkedName;
		LayoutInflater layoutInflater;
		String currentUserId = Global.getGlobalInstance().getUserid();

		public CommonLawPeopleAdapter(List<String> groupList, ArrayList<ArrayList<HashMap<String, Object>>> childMapData, LinkedList<String> usersb, LinkedList<String> linkedName,
				Context context) {
			// 初始化当前登录用户必须有
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

	/** 初始化页面 */
	private void findViewById() {
		
		xianchangkanchabilu = (LinearLayout) findViewById(R.id.xianchangkanchabilu);
		beijiancharen = (EditText) findViewById(R.id.beijiancharen);
		farendaibiao = (EditText) findViewById(R.id.farendaibiao);
		xianchangfuzeren = (EditText) findViewById(R.id.xianchangfuzeren);
		shengfenzheng = (EditText) findViewById(R.id.shengfenzheng);
		gongzuodanwei = (EditText) findViewById(R.id.gongzuodanwei);
//		gongzuodanwei.setText(rwxx.getQYMC_form_GUID(qyid));
		zhiwu = (EditText) findViewById(R.id.zhiwu);
		yubenanguanxi = (EditText) findViewById(R.id.yubenanguanxi);
		dizhi = (EditText) findViewById(R.id.dizhi);
		dianhua = (EditText) findViewById(R.id.dianhua);
		qitacanjiaren = (EditText) findViewById(R.id.qitacanjiaren);
		
		two_list_tool_layout = (RelativeLayout) this.findViewById(R.id.two_list_tool_layout);
		/** 设置标题内容 */
		SetBaseStyle(two_list_tool_layout, "简易执法");
		/** 清单检查按钮 */
		task_site_check_list_btn = (Button) findViewById(R.id.task_site_check_list_btn);
		task_site_check_list_btn.setVisibility(View.GONE);

		task_site_obtain_evidence_btn = (Button) findViewById(R.id.task_site_obtain_evidence_btn);
		task_site_obtain_evidence_btn.setVisibility(View.GONE);
		/** 执法百事通按钮 */
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
		try {
			task_askedpeopleunit_edt.setText(getIntent().getStringExtra("jyzfqymc"));///----------------被询问人单位
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		task_askedpeoplepostcode_edt = (android.widget.EditText) findViewById(R.id.task_askedpeoplepostcode_edt);
		task_dutypeople_age_edt = (EditText) findViewById(R.id.task_dutypeople_age_edt);// 被询问人的年龄
		task_natruepeople_edt = (EditText) findViewById(R.id.task_natruepeople_edt);// 被询问自然人的名字
		task_natruepeople_age_edt = (EditText) findViewById(R.id.task_natruepeople_age_edt);// 被询问自然人的年龄
		task_natruepeopleoffice_edt = (EditText) findViewById(R.id.task_natruepeopleoffice_edt);// 被询问自然人的职务
		task_natruepeopletel_edt = (EditText) findViewById(R.id.task_natruepeopletel_edt);// 被询问自然人的电话
		task_natruepeopleaddress_edt = (EditText) findViewById(R.id.task_natruepeopleaddress_edt);// 被询问自然人的住址
		task_askedpeoplesex_edt = (RadioGroup) findViewById(R.id.task_askedpeoplesex_edt);
		task_otherpeopleaddress_edt = (EditText) findViewById(R.id.task_otherpeopleaddress_edt);
		task_confirm_body_edt = (EditText) findViewById(R.id.task_confirm_body_edt);
		task_sitecondition_edt = (EditText) findViewById(R.id.task_sitecondition_edt);

		task_type_edt = (EditText) findViewById(R.id.task_type_edt);
		task_surveyaddress_lay = (LinearLayout) findViewById(R.id.task_surveyaddress_lay);
		xwbldybj = (LinearLayout) findViewById(R.id.xwbldybj);
		task_company_address_edt = (EditText) findViewById(R.id.task_surveyaddress_edt);
		task_surveyaddress_edt = (EditText) findViewById(R.id.task_simpleenfo_surveyaddress);
		task_surveypeoplename_btn = (Button) findViewById(R.id.task_surveypeoplename_btn);// 检查人的姓名
		task_work_units_edt = (EditText) findViewById(R.id.task_work_units_edt);// 检查人工作单位
		task_work_units_edt.setText(Global.getGlobalInstance().getUserUnit());
		task_recordpeopleid_sp = (Spinner) findViewById(R.id.task_recordpeopleid_sp);// 记录人
		task_recordpeopleid_work_units_edt = (EditText) findViewById(R.id.task_recordpeopleid_work_units_edt);// 记录人的工作单位
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
		task_site_law_enforcement_btn.setVisibility(View.GONE);// 执法百事通按钮不可见

		task_entcode_tv = (TextView) findViewById(R.id.task_entcode_tv);
		task_dutypeople_tv = (TextView) findViewById(R.id.task_dutypeople_tv);
		task_sitcondtion_lay = (LinearLayout) findViewById(R.id.task_sitcondtion_lay);
		task_site_enfor_wd_layout = (LinearLayout) findViewById(R.id.task_site_enfor_wd_layout);
		task_site_enfor_wd_btn = (Button) findViewById(R.id.task_site_enfor_wd_btn);
		task_site_enfor_add_btn = (Button) findViewById(R.id.task_site_enfor_add_btn);

		task_site_enfor_wd_layout.setVisibility(View.GONE);
		task_site_enfor_wd_btn.setVisibility(View.GONE);
		task_site_enfor_add_btn.setVisibility(View.GONE);

		// 环境监察通知书中的控件
		task_site_scoll_out_jieshou = (LinearLayout) findViewById(R.id.task_site_scoll_out_jieshou);
		通知书编号 = (EditText) findViewById(R.id.task_work_noem_edit_noticeNum);

		被查单位地址 = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseAddress);
		被查单位 = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseName);
		现场接收人 = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseLinkMan);
		接收人联系电话 = (EditText) findViewById(R.id.task_work_noem_edit_enterpriseLinkManTelephone);
		违法行为 = (EditText) findViewById(R.id.task_work_noem_edit_illegalBehavior);
		违反法律 = (EditText) findViewById(R.id.task_work_noem_edit_illegal);

		接受调查时间 = (Button) findViewById(R.id.task_work_noem_dealDate);

		接受调查时间.setTag(Timeuitl.getsystemtine());
		分局 = (EditText) findViewById(R.id.task_work_noem_edit_surveyGOV);
		科室 = (EditText) findViewById(R.id.task_work_noem_edit_surveyAddress);
		携带资料 = (Button) findViewById(R.id.task_work_noem_edit_carryData);
		其它资料 = (EditText) findViewById(R.id.task_work_noem_edit_otherData);

		现场检查人 = (EditText) findViewById(R.id.task_work_noem_edit_surveyMan);
		现场检查人联系地址 = (EditText) findViewById(R.id.task_work_noem_edit_surveyManAddress);
		现场检查人联系电话 = (EditText) findViewById(R.id.task_work_noem_edit_surveyManTelephone);
		task_name_tv_jieshou = (TextView) findViewById(R.id.task_name_tv_jieshou);

		// 现场环境监察记录

		task_site_scoll_out_jiancha = (LinearLayout) findViewById(R.id.task_site_scoll_out_jiancha);
		task_name_tv_jiancha = (TextView) findViewById(R.id.task_name_tv_jiancha);
		task_site_scoll_jiancha = (LinearLayout) findViewById(R.id.task_site_scoll_jiancha);
		task_site_scoll_out_jiancha.setVisibility(View.VISIBLE);
		task_site_scoll_out.setVisibility(View.INVISIBLE);
		task_site_scoll_out_jieshou.setVisibility(View.INVISIBLE);
		task_name_tv.setText(task_name_tv_jiancha.getText().toString());
		button_xunwen = (Button) findViewById(R.id.task_dutypeople_gender_button03);
		button_ziranren = (Button) findViewById(R.id.task_naturepeople_gender_button02);

		button_xunwen.setText("请选择性别");
		button_ziranren.setText("请选择性别");

		button_xunwen.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setTitle("请选择性别").setIcon(

				android.R.drawable.ic_dialog_info).setSingleChoiceItems(

				dengerList, 0,

				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						task_dutypeople_gender_spinner_str = dengerList[which].toString();

						button_xunwen.setText(task_dutypeople_gender_spinner_str);
					}
				}).setNegativeButton("确定", null).show();

			}
		});
		button_ziranren.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(TaskSimplelawEnforcementActivity.this).setTitle("请选择性别").setIcon(

				android.R.drawable.ic_dialog_info).setSingleChoiceItems(

				record, 0,

				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						task_naturepeople_gender_spinner_str = record[which].toString();

						button_ziranren.setText(task_naturepeople_gender_spinner_str);
					}
				}).setNegativeButton("确定", null).show();

			}
		});

	}

 
	/** 初始化界面不可编辑 */
	public void not_edit() {
		button_xunwen.setEnabled(false);
		button_ziranren.setEnabled(false);
		start_time_btn.setEnabled(false);
		end_time_btn.setEnabled(false);
		task_surveypeoplename_btn.setEnabled(false);// 检查人
		task_enforcement_certificate_no_edt.setEnabled(false);// 执法证号
		task_work_units_edt.setEnabled(false);// 工作单位
		task_recordpeopleid_enforcement_certificate_no_edt.setEnabled(false);// 记录人执法证号
		task_recordpeopleid_work_units_edt.setEnabled(false);// 记录人工作单位
		task_entcode_edt.setEnabled(false);// 污染源名称
		task_company_address_edt.setEnabled(false);// 调查地址
		task_surveyaddress_edt.setEnabled(false);
		task_checkpeople_edt.setEnabled(false);// 法人代表
		// task_checkpeople_position_edt.setEnabled(false);// 法人代表职务
		task_checkpeople_phone_edt.setEnabled(false);// 法人代表电话
		task_checkpeople_zip_edt.setEnabled(false);// 法人代表邮编
		task_business_license_edt.setEnabled(false);// 营业执照注册号
		task_organization_edt.setEnabled(false);// 组织机构代码
		task_dutypeople_edt.setEnabled(false);// 现场负责人姓名
		task_dutypeopleoffice_edt.setEnabled(false);// 现场负责人职务
		task_dutypeoplerelation_edt.setEnabled(false);// 与本案关系
		task_dutypeopletel_edt.setEnabled(false);// 现场负责人电话
		task_otherpeopleaddress_edt.setEnabled(false);// 其他参加人姓名及工作单位
		task_confirm_body_edt.setEnabled(false);// 确认正文
		task_sitecondition_edt.setEnabled(false);// 现场情况

		task_dutypeople_age_edt.setEnabled(false);// 被询问人的年龄
		task_natruepeople_edt.setEnabled(false);// 被询问自然人的姓名
		task_natruepeople_age_edt.setEnabled(false);// 被询问自然人的年龄
		task_natruepeopleoffice_edt.setEnabled(false);// 被询问自然人的职务
		task_natruepeopletel_edt.setEnabled(false);// 被询问自然人的电话
		task_natruepeopleaddress_edt.setEnabled(false);// 被询问自然人的住址

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
		
		// 环境监察通知书中的控件
	}

	/**
	 * 获取一问一答view
	 * 
	 * @return
	 */
	public LinearLayout getWDView() {
		LayoutInflater inflater = LayoutInflater.from(TaskSimplelawEnforcementActivity.this);
		return (LinearLayout) inflater.inflate(R.layout.zfws_ywyd, null);
	}

	/** 数据清空 */
	public void clear_data() {
		task_surveypeoplename_btn.setText("");// 检查人
		task_enforcement_certificate_no_edt.setText("");// 执法证号
//		task_work_units_edt.setText("");// 工作单位
		task_recordpeopleid_enforcement_certificate_no_edt.setText("");// 记录人执法证号
//		task_recordpeopleid_work_units_edt.setText("");// 记录人工作单位
		task_entcode_edt.setText("");// 污染源名称
		task_company_address_edt.setText("");// 调查地址
		task_surveyaddress_edt.setText("");
		task_checkpeople_edt.setText("");// 法人代表
		// task_checkpeople_position_edt.setText("");// 法人代表职务
		task_checkpeople_phone_edt.setText("");// 法人代表电话
		task_checkpeople_zip_edt.setText("");// 法人代表邮编
		task_business_license_edt.setText("");// 营业执照注册号
		task_organization_edt.setText("");// 组织机构代码
		task_dutypeople_edt.setText("");// 现场负责人姓名
		task_dutypeopleoffice_edt.setText("");// 现场负责人职务
		task_dutypeoplerelation_edt.setText("");// 与本案关系
		task_dutypeopletel_edt.setText("");// 现场负责人电话
		task_otherpeopleaddress_edt.setText("");// 其他参加人姓名及工作单位
		task_confirm_body_edt.setText("");// 确认正文
		task_sitecondition_edt.setText("");// 现场情况
		task_type_edt.setText("");// 类型

		task_dutypeople_age_edt.setText("");// 被询问人年龄
		task_natruepeople_edt.setText("");// 被询问自然人的姓名
		task_natruepeople_age_edt.setText("");// 被询问自然人的年龄
		task_natruepeopleoffice_edt.setText("");// 被询问自然人的职务
		task_natruepeopletel_edt.setText("");// 被询问自然人的电话
		task_natruepeopleaddress_edt.setText("");// 被询问自然人的住址

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
	/**
	 * 询问笔录-被询问人姓名
	 * */
	private EditText task_askedpeoplename_edt;
	/**
	 * 询问笔录-被询问人性别
	 * */
	private RadioGroup task_askedpeoplesex_edt;
	/**
	 * 询问笔录-被询问人年龄
	 * */
	private EditText task_askedpeopleage_edt;
	/**
	 * 询问笔录-被询问人身份证号
	 * */
	private EditText task_askedpeopleidnumber_edt;
	/** 勘察笔录里面的地址 */
	private LinearLayout task_surveyaddress_lay;
	/** 询问笔录独有的布局 */
	private LinearLayout xwbldybj;
	/**
	 * 询问笔录-被询问人邮编
	 * */
	private EditText task_askedpeoplepostcode_edt;
	/**
	 * 询问笔录-被询问人工作单位
	 * */
	private EditText task_askedpeopleunit_edt;
	private EditText task_entcode_edt,task_company_address_edt, task_enforcement_certificate_no_edt, task_recordpeopleid_enforcement_certificate_no_edt, task_checkpeople_edt, task_dutypeople_edt,
			task_dutypeopleoffice_edt, task_dutypeoplerelation_edt, task_dutypeopletel_edt, task_otherpeopleaddress_edt, task_confirm_body_edt, task_sitecondition_edt,
			task_type_edt, task_companyaddress_edt, task_surveyaddress_edt, task_work_units_edt, task_recordpeopleid_work_units_edt, task_dutypeople_age_edt,
			task_natruepeople_edt, task_natruepeople_age_edt, task_natruepeopleoffice_edt, task_natruepeopletel_edt, task_natruepeopleaddress_edt, task_dutypeople_edt_01;
	private EditText task_checkpeople_phone_edt, task_checkpeople_zip_edt, task_business_license_edt, task_organization_edt;

	private String task_entcode_edt_str,task_checkpeople_phone_edt_str, 	task_checkpeople_zip_edt_str,task_business_license_edt_str,task_organization_edt_str,task_checkpeople_position_edt_str, task_enforcement_certificate_no_edt_str, task_recordpeopleid_enforcement_certificate_no_edt_str,
			task_checkpeople_edt_str, task_dutypeople_edt_str, task_dutypeopleoffice_edt_str, task_dutypeoplerelation_edt_str, task_dutypeopletel_edt_str,
			task_otherpeopleaddress_edt_str, task_confirm_body_edt_str, task_surveyaddress_edt_str, task_sitecondition_edt_str, task_type_edt_str, task_companyaddress_edt_str,
			task_work_units_edt_str, task_recordpeopleid_work_units_edt_str, task_dutypeople_age_edt_str, task_natruepeople_edt_str, task_natruepeople_age_edt_str,
			task_natruepeopleoffice_edt_str, task_natruepeopletel_edt_str, task_natruepeopleaddress_edt_str, task_adress_str, task_dutypeople_edt_01_str, task_all_str;
	private Button button_xunwen, button_ziranren;
	private Spinner task_recordpeopleid_sp;
	private String task_naturepeople_gender_spinner_str, task_dutypeople_gender_spinner_str;
	private String task_surveypeoplename_btn_str, task_recordpeopleid_sp_str, task_work_units_edt_bianhao_str, task_work_units_edt_qiyedanwei_str,
			task_work_units_edt_weifaxingwei_str, task_work_units_edt_xiedaiziliao_str, task_work_units_edt_lianxiren_str, task_work_units_edt_lianxidianhua_str,
			task_work_units_edt_dizhi_str, task_work_units_edt_youbian_str;
	/** 检查（勘察）情况 */
	private LinearLayout task_sitcondtion_lay;
	/** 询问笔录需要改变显示的内容 */
	private TextView task_surveypeoplename_one_tv, task_entcode_tv, task_dutypeople_tv;

	/** 询问笔录问答布局 */
	private LinearLayout task_site_enfor_wd_layout;

	/** 询问笔录问答按钮 */
	private Button task_site_enfor_wd_btn, task_site_enfor_add_btn;
	private EditText 被查单位地址, 通知书编号;
	private EditText 被查单位;
	private Button 接受调查时间;
	private Button 携带资料;
	private EditText 现场接收人;
	private EditText 接收人联系电话;
	private EditText 违反法律;
	private EditText 违法行为;
	private EditText 分局, 科室;
	private EditText 其它资料;
	private EditText 现场检查人, 现场检查人联系地址, 现场检查人联系电话;
	/***/
	private FrameLayout task_site_joint_frlayout;
	private LinearLayout task_site_scoll_out;// 勘察询问笔录布局
	// private ScrollView task_site_scoll;
	// 接受调查通知书中的控件

	private LinearLayout task_site_scoll_out_jieshou;// 接受通知书的布局
	private TextView task_name_tv_jieshou,task_address_tv;
	private TextView task_name_tv_jiancha;
//	private EditText task_work_units_edt_bianhao;// 编号
//	private EditText task_work_units_edt_weifaxingwei; // 违法行为
	private Button one_time_btn; // 调查时间
	private Button again_time_btn;// 再次调查时间
	private EditText task_work_units_edt_xiedaiziliao; // 携带材料
	private EditText task_work_units_edt_lianxiren; // 联系人
	private EditText task_work_units_edt_lianxidianhua; // 联系电话
	private EditText task_work_units_edt_dizhi; // 地址
	private EditText task_work_units_edt_youbian; // 邮编
	private EditText task_work_units_edt_qiyedanwei;// 企业单位
	// 现场环境监察记录的布局
	private LinearLayout task_site_scoll_out_jiancha;// 现场环境监察记录的布局

	private LinearLayout task_site_scoll_jiancha;
	LayoutInflater inflater;
	ListView zhtree;

	private class QdjcAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;
		TextView textView1 = null;
		ArrayList<String> industryIDList;

		public QdjcAdapter(ArrayList<HashMap<String, Object>> list, Context context) {
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

			convertView = inflater.inflate(R.layout.qdjcmb_item, null);

			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			String tname = list.get(position).get("name").toString();
			textView1.setText(tname);
			String code = list.get(position).get("code").toString();

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

			convertView = inflater.inflate(R.layout.listitem, null);

			// Image Left
			ImageView imageleft = (ImageView) convertView.findViewById(R.id.listitem_left_image);
			imageleft.setImageResource(R.drawable.icon_left_not_checked);

			// TextView Center
			TextView tv = (TextView) convertView.findViewById(R.id.listitem_text);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(5, 5, 0, 0);
			tv.setText(list.get(position).get("name").toString());

			// ImageView Right
			ImageView imageRight = (ImageView) convertView.findViewById(R.id.listitem_image);
			imageRight.setPadding(5, 0, 0, 0);

			return convertView;
		}
	}

	YutuLoading yutuLoading;
	ItemAdapter itemAdapter;

	public class WorkAsyncTask extends AsyncTask<String, Integer, String> {
		public WorkAsyncTask(Context context) {
			yutuLoading = new YutuLoading(TaskSimplelawEnforcementActivity.this);
			yutuLoading.setLoadMsg("数据加载中，请稍等...", "");
			yutuLoading.showDialog();
		}

		@Override
		protected String doInBackground(String... params) {

			String code = params[0];
			final ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
			if ("1".equals(code)) {
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("code", "10");
				hashmap.put("name", "生产情况");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "11");
				hashmap.put("name", "建设项目执行环保制度情况");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "12");
				hashmap.put("name", "排污费缴纳情况");
				itemList.add(hashmap);

			} else if ("2".equals(code)) {
				Intent intent = new Intent();
				Context context = TaskSimplelawEnforcementActivity.this;

				intent.putExtra("entid", qyid);
				intent.putExtra("taskid", rwbh);
				intent.putExtra("code", "20");
				intent.putExtra("name", "污染治理设施建设运行情况");
				intent.setClass(context, SuperviseItemActivity.class);
				startActivityForResult(intent, 1);
				context = null;

				return null;
			} else if ("3".equals(code)) {
				HashMap<String, Object> hashmap = new HashMap<String, Object>();
				hashmap.put("code", "30");
				hashmap.put("name", "废水");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "31");
				hashmap.put("name", "废气");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "32");
				hashmap.put("name", "固体废物");
				itemList.add(hashmap);
				hashmap = new HashMap<String, Object>();
				hashmap.put("code", "33");
				hashmap.put("name", "噪声");
				itemList.add(hashmap);
			} else if ("4".equals(code)) {
				Intent intent = new Intent();
				Context context = TaskSimplelawEnforcementActivity.this;

				intent.putExtra("entid", qyid);
				intent.putExtra("taskid", rwbh);
				intent.putExtra("code", "40");
				intent.putExtra("name", "存在环境问题及意见");
				intent.setClass(context, SuperviseItemActivity.class);
				startActivityForResult(intent, 1);
				context = null;

				return null;
			}
			itemAdapter = new ItemAdapter(itemList, TaskSimplelawEnforcementActivity.this);

			Message msg = handler.obtainMessage();
			msg.arg1 = GETDATA_SUCCESS;
			msg.obj = itemAdapter;
			handler.sendMessage(msg);

			// handler.sendEmptyMessage(GETDATA_SUCCESS);
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
