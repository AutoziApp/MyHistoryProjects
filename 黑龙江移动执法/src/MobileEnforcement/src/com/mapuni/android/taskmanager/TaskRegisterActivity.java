package com.mapuni.android.taskmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
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
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.TaskFile;
import com.mapuni.android.attachment.UploadFile;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter1;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter12;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapterr;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.AttachmentBaseActivity;
import com.mapuni.android.enforcement.SiteEvidenceActivity;
import com.mapuni.android.fragment.DatePickerFragment;
import com.mapuni.android.fragment.TimePickerFragment;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.teamcircle.SpUtils;
import com.mapuni.android.user.BaseUser;
import com.mapuni.android.user.BoardLeadership;
import com.mapuni.android.user.DeputyLeader;
import com.zhy.tree_view.AddressListActivity;

/**
 * 任务创建界面
 */
public class TaskRegisterActivity extends BaseActivity {
	LayoutInflater _LayoutInflater;
	View taskRegisterView;
	private RWXX rwxx;
	private String rwGuid;
	private String RWBH;
	/** 任务名称 */
	private AutoCompleteTextView etTaskName;
	/** 办结期限 */
	private EditText completeTime;

	private EditText sendTime;
	private EditText etRemark;
	/** 任务紧急程度 */
	private Spinner taskStateSpinner;
	/** 审核领导 */
	// private Spinner leaderSpinner;
	private EditText leaderSpinner;
	/** 任务类型 */
	private EditText taskTypeEditText;

	YutuLoading pd;
	ArrayList<HashMap<String, Object>> leaderData = new ArrayList<HashMap<String, Object>>();
	/** 审核领导适配器 */
	private ArrayAdapter<SpinnerItem> leaderAdapter;
	/** 紧急程度 */
	private ArrayAdapter<SpinnerItem> stateAdapter;

	/** 任务来源 */
	private ArrayAdapter<SpinnerItem> sourceAdapter;
	/** 审核领导适配器数据 */
	List<SpinnerItem> leaderAdapterData = new ArrayList<SpinnerItem>();
	/** 紧急程度数据 */
	List<SpinnerItem> stateAdapterData;
	/** 任务类型数据 */
	// List<SpinnerItem> typeAdapterData;
	/** 任务来源数据 */
	List<SpinnerItem> sourceAdapterData;
	private HashMap<String, Object> rwDetail;
	private String rwlxCode = "";
	ArrayList<HashMap<String, Object>> qynameList = null;
	/** 企业guid字符串，以，分隔 */
	String qyidStr = "";
	String bjqxDate = "";
	public static final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH
			+ "Attach/RWXF/";
	public static String fileName = "";// 附件名称

	public final int SELECT_SDKARD_FILE = 2;
	private ListView task_attach_list;
	private String imageGuid;
	/** 用户所属地区 **/
	private final String UserAreaCode = Global.getGlobalInstance()
			.getAreaCode();

	/** 附件列表适配器 */
	private AttachAdapter attachAdapter;
	private ArrayList<HashMap<String, Object>> attachAdapterData = new ArrayList<HashMap<String, Object>>();
	/** 用户 ID */
	private String userID;

	private BaseUser currentUser = Global.getGlobalInstance().getCurrentUser();
	String myTaskId = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		sModify = getIntent().getStringExtra("modify");

		_LayoutInflater = LayoutInflater.from(this);
		taskRegisterView = _LayoutInflater.inflate(R.layout.taskedit, null);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			rwxx = (RWXX) bundle.getSerializable("BusinessObj");
			rwGuid = rwxx.getCurrentID();
			myRwguid = rwGuid;
		} else {
			rwxx = new RWXX();
		}

		if (sModify != null && sModify.equals("1")) {
			SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),
					"任务修改");

			// new Thread(){
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// super.run();
			//
			// ArrayList<String> tables = new ArrayList<String>();
			// tables.add("T_Attachment");// TaskEnpriLink表中状态发生 改变，需要同步一下
			// //tables.add("T_YDZF_RWXX");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
			// // 同步一下着两张表
			// DataSyncModel dm2 = new DataSyncModel();
			//
			// dm2.syncServiceData(tables, true);
			// }
			// };

			myTaskId = getIntent().getStringExtra("myTaskId");
			rwGuid = getGuidByRWBH(rwxx.getCurrentID());
			myTransactedTaskId = getIntent().getStringExtra(
					"myTransactedTaskId");

		} else {
			SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),
					"任务创建");
			queryImg.setImageDrawable(getResources().getDrawable(
					R.drawable.save_task_new2));
			queryImg.setVisibility(View.VISIBLE);

		}
		// 新加返回按钮 屏蔽同步按钮
		setBackButtonVisiable(true);
		// setSynchronizeButtonVisiable(false);
		// 获得用户的权限
		userID = Global.getGlobalInstance().getUserid();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("UserID", userID);

		initData();
		initView();
	}

	private void initData() {

		pd = new YutuLoading(TaskRegisterActivity.this);
		pd.setLoadMsg("正在加载数据，请稍等...", "");
		pd.showDialog();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (rwGuid != null && !"".equals(rwGuid)) {// 本地有缓存数据
					rwDetail = rwxx.getDetailed(rwGuid);
					attachAdapterData = getAttachAdapterData(T_Attachment.RWXF
							+ "", rwDetail.get("rwbh").toString());
				}
				initSpinnerAdapterData();
				handler.sendEmptyMessage(4);
			}

		}).start();
		if (pd != null) {
			pd.dismissDialog();
		}
	}

	private String getGuidByRWBH(String rwbh) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("RWBH", rwbh);
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.getList("T_YDZF_RWXX", conditions);
		if (list != null && list.size() != 0) {
			return list.get(0).get("guid").toString();
		} else {
			return "";
		}
	}

	/**
	 * 初始化spinner的适配器
	 */
	private void initSpinnerAdapterData() {
		// =======原来==================
		// if (currentUser instanceof OfficeMan) {
		// leaderAdapterData = rwxx.getleaderSpinnerItem("zw = '1' or zw='2'");
		// } else if (currentUser instanceof BoardLeadership) {
		// leaderAdapterData = rwxx
		// .getleaderSpinnerItem("zw = '2' or zw = '3'");
		// } else if (currentUser instanceof DeputyLeader) {
		// leaderAdapterData = rwxx.getleaderSpinnerItem("zw in ('1','3')");
		// } else if (currentUser instanceof ExecuteLeader) {
		// leaderAdapterData = rwxx.getleaderSpinnerItem("zw in ('1','2')");
		// }
		// 接口获取
		leaderData = getLeaderData();
		// SpinnerItem item = null;
		// int i = 0;
		// for (Map<String, Object> map : leaderData) {
		// String code = map.get("Id").toString();
		// String name = map.get("Text").toString();
		// item = new SpinnerItem(code, name, i);
		// leaderAdapterData.add(item);
		// i++;
		// }
		// leaderAdapter = new ArrayAdapter<SpinnerItem>(this,
		// android.R.layout.simple_spinner_item, leaderAdapterData);
		// leaderAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sourceAdapterData = rwxx.gettaskSourceSpinnerItem();
		sourceAdapter = new ArrayAdapter<SpinnerItem>(this,
				android.R.layout.simple_spinner_item, sourceAdapterData);
		sourceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 紧急程度 */
		stateAdapterData = rwxx.gettaskStateSpinnerItem();
		stateAdapter = new ArrayAdapter<SpinnerItem>(this,
				android.R.layout.simple_spinner_item, stateAdapterData);
		stateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	// 根据数据设置listView的高度
	public void setListViewHeightBasedOnChildren(ListView listView) {
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

	private void initView() {

		// _LayoutInflater = LayoutInflater.from(this);
		// taskRegisterView = _LayoutInflater.inflate(R.layout.taskedit, null);
		task_attach_list = (ListView) taskRegisterView
				.findViewById(R.id.taskedit_list1);
		// 任务修改的时候设置不能再次选择办理人.

		if (sModify != null && sModify.equals("1")) {

			LinearLayout ll_selectpeopleLayout = (LinearLayout) taskRegisterView
					.findViewById(R.id.select_nextpeople);

			ll_selectpeopleLayout.setVisibility(View.GONE);

		}

		String[] aryComName = null;
		Cursor data_cursor = null;
		int i = 0;
		try {

			String sql = "select rwmc from t_ydzf_rwxx  order by UpdateTime desc limit 0, 50";
			data_cursor = SqliteUtil.getInstance().queryBySql(sql);
			aryComName = new String[data_cursor.getCount()];
			while (data_cursor.moveToNext()) {
				aryComName[i] = data_cursor.getString(0);
				i++;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					TaskRegisterActivity.this,
					android.R.layout.simple_dropdown_item_1line, aryComName);
			etTaskName = (AutoCompleteTextView) taskRegisterView
					.findViewById(R.id.etTaskName);
			etTaskName.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (data_cursor != null) {
				data_cursor.close();
			}
		}

		Object obj = Global.getGlobalInstance().getCurrentUser();
		sendTime = (EditText) taskRegisterView.findViewById(R.id.sendTime);
		sendTime.setTag(new Date());
		if (obj instanceof BoardLeadership || obj instanceof DeputyLeader) {
			// 暂时屏蔽登录人为局级领导的时候 创建任务选择 发送时间问题
			// taskRegisterView.findViewById(R.id.sendTimeGroup).setVisibility(
			// View.VISIBLE);
			taskRegisterView.findViewById(R.id.sendTimeGroup).setVisibility(
					View.GONE);
			sendTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new DatePickerFragment(new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, final int year,
								final int monthOfYear, final int dayOfMonth) {
							new TimePickerFragment(
									new TimePickerDialog.OnTimeSetListener() {

										@Override
										public void onTimeSet(TimePicker view,
												int hourOfDay, int minute) {
											Date date = new Date(year - 1900,
													monthOfYear, dayOfMonth,
													hourOfDay, minute);

											if (bjqx != null
													&& bjqx.compareTo(date) < 0) {
												OtherTools
														.showToast(
																TaskRegisterActivity.this,
																"发送时间不能晚于办结时间,请重新选择");
												return;
											}

											sendTime.setText(date
													.toLocaleString());
											sendTime.setTag(date);
										}
									}).show(TaskRegisterActivity.this
									.getSupportFragmentManager(), "请选择时间");
						}
					}).show(TaskRegisterActivity.this
							.getSupportFragmentManager(), "请选择日期");
				}
			});
		}

		completeTime = (EditText) taskRegisterView
				.findViewById(R.id.completeTime);// 办结期限
		etRemark = (EditText) taskRegisterView.findViewById(R.id.etRemark);// 备注
		taskStateSpinner = (Spinner) taskRegisterView
				.findViewById(R.id.TaskState);// 紧急程度
		leaderSpinner = (EditText) taskRegisterView.findViewById(R.id.leader);// 审核领导

		taskTypeEditText = (EditText) taskRegisterView
				.findViewById(R.id.TaskType);// EditText任务类型

		attachAdapter = new AttachAdapter(attachAdapterData);
		task_attach_list.setCacheColorHint(Color.TRANSPARENT);
		task_attach_list.setAdapter(attachAdapter);
		task_attach_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String guid = ((TextView) (arg1
								.findViewById(R.id.listitem_text))).getTag()
								.toString();
						FileHelper fileHelper = new FileHelper();
						fileHelper.showFileByGuid(guid,
								TaskRegisterActivity.this);
					}
				});

		task_attach_list
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

						showDialog(guid, fileNam);
						return true;
					}
				});

		loadBottomMenu();
		bindListener();

		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);

	}

	private void bindListener() {
		completeTime.setOnClickListener(new TaskEditTextListener());
		taskTypeEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// createDialogViewForTaskType();
				// createDialogViewForTaskType_new();

				createDiaLogSigleSelect();
			}

		});
		// 审核领导
		leaderSpinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// createDialogViewForTaskType();
				Intent intent = new Intent(TaskRegisterActivity.this,
						AddressListActivity.class);
				intent.putExtra("arr", leaderData);
				startActivityForResult(intent, 0);
			}
		});
		queryImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (insertOneTask(false)) {
					Toast.makeText(TaskRegisterActivity.this, "暂存任务成功",
							Toast.LENGTH_SHORT).show();
					TaskRegisterActivity.this.finish();
				}
			}
		});

	}

	/** 办结期限监听事件,判断所选日期大于当前日期 **/
	private class TaskEditTextListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(TaskRegisterActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {

							int flag;
							if (year > year1) { // 设置年大于当前年，直接设置，不用判断下面的
								completeTime.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								flag = 1;
							} else if (year == year1) {
								// 设置年等于当前年，则向下开始判断月
								if (monthOfYear > month1) {
									// 设置月大于当前月，直接设置，不用判断下面的
									flag = 1;
									completeTime.setText(year + "-"
											+ (monthOfYear + 1) + "-" + day1);
								} else if (monthOfYear == month1) {
									// 设置月等于当前月，则向下开始判断日
									if (dayOfMonth > day1) {
										// 设置日大于当前日，直接设置，不用判断下面的
										flag = 1;
										completeTime.setText(year + "-"
												+ (monthOfYear + 1) + "-"
												+ dayOfMonth);
									} else if (dayOfMonth == day1) {
										// 设置日等于当前日，则向下开始判断时
										flag = 2;
										completeTime.setText(year + "-"
												+ (monthOfYear + 1) + "-"
												+ dayOfMonth);
									} else { // 设置日小于当前日，提示重新设置
										flag = 3;
										completeTime.setText("");
										Toast.makeText(
												TaskRegisterActivity.this,
												"所选择日期不能小于当前日期,请重新设置", 2000)
												.show();
									}
								} else { // 设置月小于当前月，提示重新设置
									flag = 3;
									completeTime.setText("");
									Toast.makeText(TaskRegisterActivity.this,
											"所选择日期不能小于当前日期,请重新设置", 2000).show();
								}
							} else { // 设置年小于当前年，提示重新设置
								flag = 3;
								completeTime.setText("");
								Toast.makeText(TaskRegisterActivity.this,
										"所选择日期不能小于当前日期,请重新设置", 2000).show();
							}
							if (flag != 3) {
								Calendar nowDate = Calendar.getInstance(), newDate = Calendar
										.getInstance();
								nowDate.setTime(new Date());// 设置为当前系统时间
								newDate.set(year, monthOfYear, dayOfMonth);// 设置为1990年（6）月29日
								long timeNow = nowDate.getTimeInMillis();
								long timeNew = newDate.getTimeInMillis();
								long dd = (timeNew - timeNow)
										/ (1000 * 60 * 60 * 24);// 化为天
								bjqxDate = year + "-" + (monthOfYear + 1) + "-"
										+ dayOfMonth;
								completeTime.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth
										+ "  (离办结还有" + dd + "天)");
								bjqx = new Date(year - 1900, monthOfYear,
										dayOfMonth);
							} else {
								completeTime.setText("");
							}
						}

					}, year1, month1, day1).show();

		}
	}

	private Date bjqx;

	/**
	 * 加载底部菜单
	 */
	public void loadBottomMenu() {
		Button tasksend = (Button) taskRegisterView.findViewById(R.id.tasksend);

		if (sModify != null && sModify.equals("1")) {

			tasksend.setText("任务提交");
		}
		// LinearLayout bottom = (LinearLayout) findViewById(R.id.bottomLayout);
		// bottom.setVisibility(View.VISIBLE);
		// final Button tasksend;
		// Button back;
		// tasksend = new Button(this);
		// back = new Button(this);
		// tasksend.setBackgroundResource(R.drawable.btn_shap);
		// tasksend.setText("任务创建");
		// tasksend.setTextColor(android.graphics.Color.WHITE);
		// tasksend.getPaint().setFakeBoldText(true);// 加粗
		// tasksend.setWidth(1);
		// back.setBackgroundResource(R.drawable.btn_shap);
		// back.setText("返   回");
		// back.setTextColor(android.graphics.Color.WHITE);
		// back.getPaint().setFakeBoldText(true);// 加粗
		// back.setWidth(1);
		// // 获取手机分辨率
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// // 算出按钮的高宽
		// int width = (int) (dm.widthPixels / (double) 2);
		//
		// tasksend.setLayoutParams(new LinearLayout.LayoutParams(width,
		// LinearLayout.LayoutParams.MATCH_PARENT, 0));
		// back.setLayoutParams(new LinearLayout.LayoutParams(width,
		// LinearLayout.LayoutParams.MATCH_PARENT, 0));
		// ImageView splite = new ImageView(this);
		// splite.setScaleType(ScaleType.FIT_XY);
		// splite.setLayoutParams(new LinearLayout.LayoutParams(2,
		// LinearLayout.LayoutParams.MATCH_PARENT));
		//
		// splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
		// bottom.addView(tasksend);
		// bottom.addView(splite);
		// bottom.addView(back);

		/**
		 * 任务下发
		 */
		tasksend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String rwmc = etTaskName.getText().toString();
				String bjqx = bjqxDate;
				String leader = leaderSpinner.getText().toString();
				// SpinnerItem selectedItem = (SpinnerItem) (leaderSpinner
				// .getSelectedItem());
				// String shldId = "";
				// if (null != selectedItem) {
				// shldId = selectedItem.getCode();
				// }
				if ("".equals(sModify)) {
					if (rwmc.equals("") || bjqx.equals("") || leader.equals("")
							|| leader.equals("请选择办理人员")) {
						handler.sendEmptyMessage(3);
						return;
					}

				}

				if (RWBH == null) {// 没有选择附件
					handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
					return;
				}

				if (TextUtils.isEmpty(taskTypeEditText.getText())) {
					handler.sendEmptyMessage(3);
					return;
				}

				// 暂时隐藏
				ArrayList<TaskFile> taskFile = getAllUploadFile(
						T_Attachment.RWXF, RWBH);
				if (taskFile != null && taskFile.size() > 0) {
					// UploadFile uploadFile = new UploadFile();
					// uploadFile.upLoadFilesMethod(taskFile, handler,
					// TaskRegisterActivity.this,rwGuid+"");
					// 改为自己的上传 不用依赖库BYK
					upLoadFilesMethod(taskFile, TaskRegisterActivity.this,
							rwGuid);
				} else {
					handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
				}

			}
		});

		// /**
		// * 返回
		// */
		// back.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Builder builder = new Builder(TaskRegisterActivity.this);
		// builder.setTitle("确认退出该页面吗？");
		// builder.setPositiveButton("确认",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// TaskRegisterActivity.this.finish();
		// }
		// });
		// builder.setNegativeButton("取消", null);
		// builder.create().show();
		//
		// }
		// });
	}

	private final Handler handler = new Handler() {// UI线程Handler
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (pd != null)
					pd.dismissDialog();

				if ("".equals(sModify)) {

				} else {
					ContentValues cv2 = new ContentValues();
					cv2.put("rwzt", RWXX.RWZT_WATE_YIXIAFA);
					String[] whereArgs2 = { myRwguid };
					try {
						SqliteUtil.getInstance().update("T_YDZF_RWXX", cv2,
								"guid=?", whereArgs2);
					}

					catch (Exception e) {
					}
				}

				Toast.makeText(TaskRegisterActivity.this, "任务创建成功！",
						Toast.LENGTH_SHORT).show();
				TaskRegisterActivity.this.finish();
				break;

			case 1:
				if (pd != null)
					pd.dismissDialog();

				if (sModify != null && sModify.equals("1")) {
					Toast.makeText(TaskRegisterActivity.this, "任务修改失败,请重新提交!",
							Toast.LENGTH_SHORT).show();

				} else {
					if (msg.obj != null) {
						String reason = msg.obj.toString();
						Toast.makeText(TaskRegisterActivity.this, reason,
								Toast.LENGTH_SHORT).show();
					} else {

						ContentValues cv = new ContentValues();
						cv.put("rwzt", RWXX.RWZT_WAIT_DISPATCH);
						// 修改任务保存本地数据问题
						String[] whereArgs = { myRwguid };
						try {
							SqliteUtil.getInstance().update("T_YDZF_RWXX", cv,
									"guid=?", whereArgs);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						Toast.makeText(TaskRegisterActivity.this,
								"任务创建失败,已保存当前任务等待提交！", Toast.LENGTH_SHORT)
								.show();
					}
				}

				break;
			case 2:
				if (pd != null) {
					pd.dismissDialog();
				}
				Toast.makeText(TaskRegisterActivity.this, "网络不通，请检查网络设置！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (pd != null)
					pd.dismissDialog();
				Toast.makeText(TaskRegisterActivity.this, "任务信息不全，请完善带*号的选项！",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:// 任务数据加载完成，刷新界面
				if (pd != null)
					pd.dismissDialog();
				if (rwDetail != null) {
					bindValue(rwDetail);
					LogUtil.i("bindValue",
							"--------------我走了bindValue方法--------------");
				} else {
					taskStateSpinner.setAdapter(stateAdapter);
					// leaderSpinner.setAdapter(leaderAdapter);
				}

				break;
			case 5:
				if (pd != null)
					pd.dismissDialog();
				Toast.makeText(TaskRegisterActivity.this, "任务创建失败",
						Toast.LENGTH_SHORT).show();

				break;

			case UploadFile.UPLOAD_CALL_BACK:
				pd = new YutuLoading(TaskRegisterActivity.this);
				pd.setLoadMsg("正在提交任务，请稍等...", "");
				pd.setCancelable(true);
				pd.showDialog();
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (!Net.checkURL(Global.getGlobalInstance()
								.getSystemurl())) {
							handler.sendEmptyMessage(2);
							return;
						}

						if (insertOneTask(true)) {
							if (requestRegisterTask(rwGuid)) {
								handler.sendEmptyMessage(0);
							} else {
								handler.sendEmptyMessage(1);
							}
						} else {
						}
					}
				}).start();
				break;

			}
		}

	};

	private void bindValue(HashMap<String, Object> rwDetail) {
		RWBH = rwDetail.get("rwbh").toString();
		etTaskName.setText(rwDetail.get("rwmc").toString());
		try {

			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
					.parse(rwDetail.get("fbsj").toString());
			sendTime.setText(date.toLocaleString());
			sendTime.setTag(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		bjqxDate = rwDetail.get("bjqx").toString();
		if (sModify != null && sModify.equals("1") && bjqxDate != null) {
			String[] split = bjqxDate.split("T");
			completeTime.setText(split[0]);
		} else {
			completeTime.setText(bjqxDate);
		}

		try {
			bjqx = new SimpleDateFormat("yyyy-MM-dd").parse(bjqxDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		etRemark.setText(rwDetail.get("bz").toString());

		rwlxCode = rwDetail.get("rwlx").toString();
		if (rwlxCode != null && !rwlxCode.equals("")) {
			String[] tempString = rwlxCode.split(";");
			String textvalue = "";
			for (int i = 0; i < tempString.length; i++) {
				String sql = "select name from T_YDZF_RWLX where code='"
						+ tempString[i] + "'";
				if (i == 0) {
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else {
					textvalue = textvalue + ";"
							+ SqliteUtil.getInstance().getDepidByDepName(sql);
				}
			}
			taskTypeEditText.setText(textvalue);
		}

		// HashMap<String, Object> conditions = new HashMap<String, Object>();
		// if (rwlxCode != null && !rwlxCode.equals("")) {
		// conditions.put("code", rwlxCode);
		// String lxName = SqliteUtil.getInstance()
		// .getList("name", conditions, "T_YDZF_RWLX").get(0)
		// .get("name").toString();
		// taskTypeEditText.setText(lxName);
		// }

		// 紧急程度
		taskStateSpinner.setAdapter(stateAdapter);
		String jjcd = rwDetail.get("jjcd").toString();
		for (int i = 0; i < stateAdapterData.size(); i++) {
			if (stateAdapterData.get(i).getCode().equals(jjcd)) {
				taskStateSpinner.setSelection(i);
				break;
			}
		}

		// ((AutoCompleteTextView) leaderSpinner).setAdapter(leaderAdapter);
		String shkz = rwDetail.get("shkz").toString();
		// leaderSpinner.setText(shkz);
		// for (int i = 0; i < leaderAdapterData.size(); i++) {
		// if (leaderAdapterData.get(i).getCode().equals(shkz)) {
		// leaderSpinner.setSelection(i);
		// break;
		// }
		// }
		attachAdapterData = getAttachAdapterData(T_Attachment.RWXF + "", RWBH);
		attachAdapter = new AttachAdapter(attachAdapterData);
		task_attach_list.setAdapter(attachAdapter);
		attachAdapter.updateData(attachAdapterData);
		leaderSpinner.setText(rwDetail.get("shkz").toString());
		leaderSpinner.setTag(rwDetail.get("transactedtaskid").toString());
	}

	/**
	 * 调用webservice创建一条任务
	 * 
	 * @param rwGuid
	 *            任务的guid
	 */
	public Boolean requestRegisterTask(String rwGuid) {
		LogUtil.i("rwGuid", "调用webservice创建一条任务传递进来rwGuid:-------->" + rwGuid);
		Boolean resultBoolean = false;
		String json = "";
		String methodName = "MobileEnforcememtTaskLaunch";
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", rwGuid);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("T_YDZF_RWXX", conditions);
		// String taskInfoJson = JsonHelper.listToJSON(data);
		//
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		// param.put("taskInfoJson", taskInfoJson);

		// 添加数据
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("TaskId", String.valueOf(data.get(0).get("guid")) );

		if (sModify != null && sModify.equals("1")) {
			map.put("TaskId", myTaskId);
			map.put("FlowTaskId", myTransactedTaskId);
			map.put("NextTransactorId", "0");
		} else {
			map.put("TaskId", "0");
			map.put("FlowTaskId", "0");
			map.put("NextTransactorId", leaderSpinner.getTag().toString());
		}

		map.put("TaskCode", String.valueOf(data.get(0).get("rwbh")));
		map.put("TaskName", String.valueOf(data.get(0).get("rwmc")));
		map.put("TaskSource", String.valueOf(data.get(0).get("rwly")));
		map.put("FromChannel", "2");
		map.put("RegionCode",
				String.valueOf(data.get(0).get("syncdataregioncode")));
		map.put("TaskType", String.valueOf(data.get(0).get("rwlx")));
		map.put("KeyWord", "");
		map.put("Urgency", String.valueOf(data.get(0).get("jjcd")));
		map.put("PublishedTime", String.valueOf(data.get(0).get("fbsj")));
		map.put("PublisherName", String.valueOf(data.get(0).get("fbr")));
		map.put("TransactedTime", String.valueOf(data.get(0).get("bjqx")));
		map.put("Remark", String.valueOf(data.get(0).get("bz")));

		map.put("Publisher", String.valueOf(data.get(0).get("fbr")));
		map.put("PublisherId", "0"/* Global.getGlobalInstance().getUserid() */);
		// map.put("NextTransactorId", leaderSpinner.getTag().toString());

		// map.put("Tid", RWBH);
		// map.put("LoginUserId", Global.getGlobalInstance().getUserid());
		// map.put("ApprovalResult", "");
		// map.put("AuditUserId",
		// ((SpinnerItem) (leaderSpinner.getSelectedItem())).getCode());
		// map.put("SecondaryAuditUserId", "");
		// map.put("FlowDirection", "1");
		data_json.add(map);

		String lawEnforcementTaskModelJson = JsonHelper
				.listToJSONXin(data_json);
		param.put("lawEnforcementTaskModelJson", lawEnforcementTaskModelJson);
		// param.put("entInfoJson", "");
		// String token = "";
		// try {
		// token = DESSecurity.encrypt(methodName);
		// } catch (Exception e1) {
		//
		// e1.printStackTrace();
		// }
		// param.put("token", token);
		params.add(param);

		LogUtil.i("TAG_YK", params.toString());
		try {
			json = (String) WebServiceProvider.callWebService(Global.NAMESPACE,
					methodName, params, Global.getGlobalInstance()
							.getSystemurl() + Global.WEBSERVICE_URL,
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

		return resultBoolean;
	}

	/**
	 * 获取办理人员 leaderData
	 * 
	 * @param rwGuid
	 *            任务的guid
	 */
	public ArrayList<HashMap<String, Object>> getLeaderData() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		String methodName = "GetNodeTransactor";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		// 添加数据
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("OperationCode", "10");
		map.put("FlowTaskId", Global.getGlobalInstance().getUserid());
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
				result = JsonHelper.paseJSON(json);
			} else {

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 调用webservice创建一条任务流程
	 * 
	 * @param rwGuid
	 *            任务的guid
	 */
	public Boolean registerTaskFlow(String rwbh) {
		Boolean result = false;
		String methodName = "SaveWorkFlowInfo";
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("Tid", rwbh);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("YDZF_RWLC", conditions);
		String workFlowJson = JsonHelper.listToJSON(data);

		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("workFlowJson", workFlowJson);
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
		}

		return result;
	}

	/**
	 * 
	 * @param tag
	 *            true 为直接下发 false 保存本地待提交
	 * @return
	 */
	public Boolean insertOneTask(Boolean tag) {
		Boolean result = false;
		ContentValues cv = new ContentValues();

		String rwmc = etTaskName.getText().toString();
		String bjqx = bjqxDate;
		// String shldId = ((SpinnerItem) (leaderSpinner.getSelectedItem()))
		// .getCode();
		String leader = leaderSpinner.getText().toString();
		if ("".equals(sModify)) {

			if (rwmc.equals("") || bjqx.equals("") || leader.equals("")
					|| leader.equals("请选择办理人员")) {
				handler.sendEmptyMessage(3);
				return false;
			}
		}

		if (RWBH == null || RWBH.equals("")) {
			RWBH = rwxx.returnRWBH();
			LogUtil.i("RWBH", "RWBH:---------->" + RWBH);
		}
		String jjcd = ((SpinnerItem) (taskStateSpinner.getSelectedItem()))
				.getCode();
		String rwms = etRemark.getText().toString();
		cv.put("rwly", RWXX.YBRW_LY);
		cv.put("bz", rwms);
		cv.put("bjqx", bjqx);
		cv.put("rwmc", rwmc);
		cv.put("rwbh", RWBH);
		cv.put("jjcd", jjcd);
		cv.put("rwlx", rwlxCode);
		cv.put("shkz", leaderSpinner.getText().toString());
		if (leaderSpinner.getText().toString() != null
				&& !"".equals(leaderSpinner.getText().toString())) {
			cv.put("transactedtaskid", leaderSpinner.getTag().toString());
		} else {
			cv.put("transactedtaskid", "");
		}

		cv.put("fbr", Global.getGlobalInstance().getUserid());
		// 任务所属地区和登录用户所属地区相同
		cv.put("syncdataregioncode", UserAreaCode);
		cv.put("fbsj", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
				.format((Date) sendTime.getTag()));
		if (tag) {// 直接下发需要和服务器时间同步
			String updateTime = DisplayUitl.getServerTime();
			cv.put("UpdateTime", updateTime);
			cv.put("rwzt", RWXX.RWZT_WAIT_AUDIT);
			if (rwGuid != null && !rwGuid.equals("")) {// 更新操作
			// String[] whereArgs = { rwGuid };
				cv.put("guid", rwGuid);
				LogUtil.i("success", "rwGuid：------------>" + rwGuid);
				try {
					// tfy:将更新替换为插入
					long insert = SqliteUtil.getInstance().insert(cv,
							"T_YDZF_RWXX");
					LogUtil.i("success", "受影响行数：------------>" + insert);
					// delFromTaskEnpriLink();
					// addinfoToTaskEnpriLink();
					result = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				rwGuid = UUID.randomUUID().toString();
				// addinfoToTaskEnpriLink();
				cv.put("guid", rwGuid); // TaskId
				if (SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX") > 0) {
					result = true;
				}

			}
		} else {
			if (rwGuid != null && !rwGuid.equals("")) {// 更新操作
				String[] whereArgs = { rwGuid };
				try {
					SqliteUtil.getInstance().update("T_YDZF_RWXX", cv,
							"guid=?", whereArgs);
					result = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				rwGuid = UUID.randomUUID().toString();
				// addinfoToTaskEnpriLink();
				cv.put("guid", rwGuid);
				cv.put("rwzt", RWXX.RWZT_WAIT_DISPATCH);
				if (SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX") > 0) {
					result = true;
				}

			}
		}
		return result;

	}

	// 最新任务类型单选
	private void createDiaLogSigleSelect() {

		final List<HashMap<String, Object>> taskTypes = rwxx
				.gettaskTypeEditText2();

		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> codes = new ArrayList<String>();

		for (Map<String, Object> map : taskTypes) {
			String code = map.get("code").toString();
			String name = map.get("name").toString();

			names.add(name);
			codes.add(code);

		}
		final String[] names2 = names.toArray(new String[names.size()]);
		final String[] codes2 = codes.toArray(new String[codes.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("任务类型");
		builder.setIcon(R.drawable.yutu);
		builder.setSingleChoiceItems(names2, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String name = names2[which];
						String code = codes2[which];

						rwlxCode = code;
						taskTypeEditText.setText(name);

						dialog.dismiss();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

	// 任务类型（单选）
	public void createDialogViewForTaskType() {
		LayoutInflater inflater = LayoutInflater
				.from(TaskRegisterActivity.this);
		View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
		TextView expand_title_tv = (TextView) viewex
				.findViewById(R.id.expand_title_tv);
		expand_title_tv.setText("任务分类");

		ArrayList<HashMap<String, Object>> groupData = rwxx.getTask_type2();
		final List<String> groupList = new ArrayList<String>();
		if (groupData != null && groupData.size() > 0) {
			for (HashMap<String, Object> hashMap : groupData) {
				String tnameStr = hashMap.get("name").toString();
				groupList.add(tnameStr);
			}
		}
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx
				.getTask_type_child2(groupData);

		final ExpandableListView expandableListView = (ExpandableListView) viewex
				.findViewById(R.id.expandablelistview);
		ExpandableBaseAdapterr adapter = new ExpandableBaseAdapterr(this,
				groupList, childMapData);
		expandableListView.setAdapter(adapter);
		expandableListView.setCacheColorHint(0);// 设置拖动列表的时候防止出现黑色背景
		expandableListView.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));

		AlertDialog.Builder builder = new AlertDialog.Builder(
				TaskRegisterActivity.this);
		builder.setIcon(R.drawable.yutu);
		builder.setTitle("请选择任务类型");
		builder.setView(viewex);

		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

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
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String cnames = childMapData.get(groupPosition)
						.get(childPosition).get("name").toString();
				rwlxCode = childMapData.get(groupPosition).get(childPosition)
						.get("code").toString();
				taskTypeEditText.setText(cnames);

				dialog.cancel();
				return false;
			}
		});
	}

	// 任务类型（多选）
	StringBuffer sbFshr;
	private String sModify = "";
	private String myTransactedTaskId;
	private String myRwguid;

	public void createDialogViewForTaskType_new() {
		LayoutInflater inflater = LayoutInflater
				.from(TaskRegisterActivity.this);
		View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
		TextView expand_title_tv = (TextView) viewex
				.findViewById(R.id.expand_title_tv);
		expand_title_tv.setText("任务分类");

		ArrayList<HashMap<String, Object>> groupData = rwxx.getTask_type2();
		final List<String> groupList = new ArrayList<String>();
		if (groupData != null && groupData.size() > 0) {
			for (HashMap<String, Object> hashMap : groupData) {
				String tnameStr = hashMap.get("name").toString();
				groupList.add(tnameStr);
			}
		}
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx
				.getTask_type_child2(groupData);

		final ExpandableListView expandableListView = (ExpandableListView) viewex
				.findViewById(R.id.expandablelistview);
		/** 用户id集合 */
		final LinkedList<String> linkedList = new LinkedList<String>();
		/** 用户姓名集合 */
		final LinkedList<String> linkedName = new LinkedList<String>();
		ExpandableBaseAdapter12 adapter = new ExpandableBaseAdapter12(this,
				groupList, childMapData, linkedList, linkedName);
		expandableListView.setAdapter(adapter);
		expandableListView.setCacheColorHint(0);// 设置拖动列表的时候防止出现黑色背景
		expandableListView.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));

		AlertDialog.Builder builder = new AlertDialog.Builder(
				TaskRegisterActivity.this);
		builder.setIcon(R.drawable.yutu);
		builder.setTitle("请选择任务类型");
		builder.setView(viewex);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				StringBuffer userName = new StringBuffer();
				sbFshr = new StringBuffer();
				for (int i = 0; i < linkedList.size(); i++) {
					sbFshr.append(linkedList.get(i) + ";");
					userName.append(linkedName.get(i) + ";");
				}
				if (sbFshr.length() > 0) {
					sbFshr.deleteCharAt(sbFshr.length() - 1);
				}
				if (userName.length() > 0) {
					userName.deleteCharAt(userName.length() - 1);
				}
				rwlxCode = sbFshr.toString();
				taskTypeEditText.setText(userName.toString());
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0; i < groupList.size(); i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
							}
						}
					}
				});
	}

	public void upDataAttathListView() {
		if (attachAdapter != null) {
			attachAdapter.updateData(getAttachAdapterData(T_Attachment.RWXF
					+ "", RWBH));
		}
	}

	// 拍照
	public void photograph(View view) {
		takePhoto();
	}

	private void takePhoto() {// 拍照
		// String
		imageGuid = UUID.randomUUID().toString();
		Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 设置存储路径
		File file = new File(TASK_PATH);
		if (!file.exists())// 第一次拍照创建照片文件夹
			file.mkdirs();
		photo_intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(TASK_PATH + imageGuid + "." + "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		startActivityForResult(photo_intent, SiteEvidenceActivity.TAKE_PHOTOS);

	}

	// 选照
	public void takefigure(View view) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
					SELECT_SDKARD_FILE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
	}

	// 安装文件管理器
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && requestCode == 0 && 20 == resultCode) {
			leaderSpinner.setText(String.valueOf(data.getExtras().getString(
					"name")));
			leaderSpinner.setTag(String.valueOf(data.getExtras().getString(
					"code")));
			return;
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
						+ T_Attachment.RWXF
						+ "','" + RWBH + "')";
				SqliteUtil.getInstance().execute(sql);
			}

		}
		if (data != null && requestCode == SELECT_SDKARD_FILE) {
			AttachmentBaseActivity.selectSDcardFile(data, this,
					T_Attachment.RWXF, RWBH);
		}
		attachAdapterData = getAttachAdapterData(T_Attachment.RWXF + "", RWBH);
		attachAdapter.updateData(attachAdapterData);

	}

	/**
	 * 获取附件列表数据
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
			// 在这里随机生成一个billid(即rwGuid)和指定一个biztype byk
			rwGuid = UUID.randomUUID().toString();
			SpUtils.putString(TaskRegisterActivity.this, "billid_byk", rwGuid);
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
				taskFile.setBillid(rwGuid);
				taskFile.setBiztype(biztype);
				_ListFile.add(taskFile);
			}
		}
		return _ListFile;
	}

	protected void showDialog(final String guid, final String fileName) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		String[] selections = { "重命名", "删除" };
		dialog.setItems(selections, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					LinearLayout ly = new LinearLayout(
							TaskRegisterActivity.this);
					ly.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));
					final EditText edtext = new EditText(
							TaskRegisterActivity.this);
					edtext.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					TextView tv = new TextView(TaskRegisterActivity.this);
					tv.setText("名称：");
					ly.addView(tv);
					ly.addView(edtext);
					AlertDialog.Builder ab = new AlertDialog.Builder(
							TaskRegisterActivity.this);
					ab.setTitle("重命名");
					ab.setIcon(R.drawable.icon_rename);
					ab.setView(ly);
					ab.setPositiveButton("确定",
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
									Toast.makeText(TaskRegisterActivity.this,
											"重命名成功！", Toast.LENGTH_LONG).show();
								}
							});
					ab.setNegativeButton("取消", null);
					ab.show();
					break;
				case 1:
					AlertDialog.Builder deleteab = new AlertDialog.Builder(
							TaskRegisterActivity.this);
					deleteab.setTitle("删除");
					deleteab.setMessage("你确定要删除" + fileName + "吗？");
					deleteab.setIcon(R.drawable.icon_delete);
					deleteab.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

									deleteFile(guid);
									String sql = "delete from T_Attachment "
											+ " where guid = '" + guid + "'";
									SqliteUtil.getInstance().execute(sql);
									upDataAttathListView();
									Toast.makeText(TaskRegisterActivity.this,
											"删除" + fileName + "成功！",
											Toast.LENGTH_LONG).show();
								}

							});
					deleteab.setNegativeButton("取消", null);
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
				ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData = CompanyAdapterData;
			setListViewHeightBasedOnChildren(task_attach_list);
			notifyDataSetChanged();
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData,
				ListView listview) {
			this.attachAdapterData = CompanyAdapterData;
			setListViewHeightBasedOnChildren(listview);
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
				convertView = View.inflate(TaskRegisterActivity.this,
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

	public void upLoadFilesMethod(ArrayList<TaskFile> fileList,
			Context context, String rwGuid) {

		new TaskUploadAsync(fileList, context, rwGuid).execute();
	}

	class TaskUploadAsync extends AsyncTask<Object, Integer, Object> {

		ArrayList<TaskFile> listAllFile;
		/** 上传附件进度条 */
		ProgressDialog pdialog;
		private int index;
		/** 附件是否已经上传过 */
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
				Toast.makeText(mcontext, "没有选择附件！", Toast.LENGTH_SHORT).show();
			} else {
				String fileName = listAllFile.get(index).getFileName()
						+ listAllFile.get(index).getExtension();
				pdialog.setMessage(fileName + " 正在上传...");
				pdialog.show();
			}

		}

		protected void onProgressUpdate(Integer... values) {
			String fileName = listAllFile.get(index).getFileName()
					+ listAllFile.get(index).getExtension();
			if (values[0] == 100) {
				if (isUpload) {
					pdialog.setMessage(fileName + " 已经上传");
					isUpload = false;
				} else {
					pdialog.setMessage(fileName + " 上传成功");
				}

			} else {
				pdialog.setMessage(fileName + " 正在上传...");

			}
			pdialog.setProgress(values[0]);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String serverTime = DisplayUitl.getServerTime();// 首先更新附件表中的更新时间
			if (serverTime != null && !serverTime.equals("")) {
				for (TaskFile taskFile : listAllFile) {// 更新updatetime
														// 确保其他人能够同步此数据
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
				return "网络连接异常，请检查网络设置后重试！";
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

				int finishblocks = 0;// 断点包数

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
						return "获取附件断点信息异常";
					}
					if (finishblocks == -1) {
						return "获取附件断点信息失败";
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
						// 获取附件
						String AttachmentJosn = "["
								+ GetFujian(taskFile, i + "", rwGuid)
										.toString() + "]";

						Boolean resultBoolean = httpUploader.upLoadAffixMethod(
								AttachmentJosn, attachmentData, isEnd);
						if (resultBoolean) {
							index = n;
							publishProgress((i + 1) * 100 / totalblocks);
						} else {
							return "上传附件失败,请检查网络是否正常！";
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "上传附件出现异常";
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

	private JSONObject GetFujian(TaskFile taskFile, String i, String rwGuid) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance()
				.getList("T_Attachment", conditions);
		// 得到billid
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

				if (_Key.equals("FK_ID") || _Key.equals("fk_id")) {

					String fk_id = entry.getValue().toString();
					_JsonObject.put("billid", fk_id);
				}

				// _JsonObject.put("billid",
				// String.valueOf(data.get(0).get("rwbh")));
				_JsonObject.put("biztype", taskFile.getBiztype());

			}

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}

}
