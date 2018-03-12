package com.mapuni.android.taskmanager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.base.widget.RefreshableView;
import com.mapuni.android.base.widget.RefreshableView.PullToRefreshListener;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.task.TaskType;
import com.mapuni.android.user.BaseUser;
import com.mapuni.android.user.BoardLeadership;
import com.mapuni.android.user.DeputyLeader;
import com.mapuni.android.user.ExecuteLeader;
import com.mapuni.android.user.ExecuteMan;
import com.mapuni.android.user.OfficeLeader;
import com.mapuni.android.user.OfficeMan;
import com.mapuni.android.user.userInterface.UserCreateInterface;

/*
 * 任务管理主界面
 * */
public class TaskMainActivity extends BaseActivity {
	/** 当前登录用户 */
	private String userID;
	private RWXX rwxx;
	private YutuLoading yutuLoading;
	private CheckBox search_moveZhr_checkbox;
	private ListView taskListView;
	/** 总的任务条数 */
	private ArrayList<HashMap<String, Object>> taskStateData;
	private BaseGeneralTaskAdapter baseGeneralTaskAdapter;
	/** 待审核任务条数，请求webservice获取 */
	private int WAIT_AUDIT_NUM;

	/** 待协助任务条数，请求webservice获取 */
	private int WAIT_ASSIST_NUM;

	/** 待归档任务条数，请求webservice获取 */
	private int WAIT_ARCHIVE_NUM;

	/** 创建任务按钮 */
	private Button btn_addTask;

	Boolean isFirst = false;

	private EditText searchTaskname_et, startTime_et, endTime_et,
			searchTasktype_et;
	private String rwlxCode;
	ArrayList<HashMap<String, Object>> listandNumArr = new ArrayList<HashMap<String, Object>>();
	RefreshableView refreshableView;
	// 当前登录对象
	private BaseUser baseUser = Global.getGlobalInstance().getCurrentUser();
	private boolean isFirstNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("任务管理");
		rwxx = new RWXX();
		userID = baseUser.getUserID();
		initView();
		// OtherTools.showToast(this, "如需获取最新任务,请下拉刷新");
		refuseView();
		// if (yutuLoading != null) {
		// yutuLoading.showDialog();
		// }
		// syncTheLastTasks();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (isFirstNew) {
		// refuseView();
		// isFirstNew = true;
		// }
		taskStateData.clear();
		listandNumArr = getListandNum();
		initTaskNumData();
		baseGeneralTaskAdapter = new BaseGeneralTaskAdapter(
				TaskMainActivity.this, taskStateData);
		taskListView.setAdapter(baseGeneralTaskAdapter);
		// if (yutuLoading != null) {
		// yutuLoading.dismissDialog();
		// }
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	// 查询数据库获取当前的任务的状态和 ----数量
	private String getTaskNum(String RWZT) {

		String taskListSql = "select distinct a.guid,a.[RWMC],a.[BJQX],a.jjcd,a.[FBSJ],c.[U_RealName] as fbrmc from T_YDZF_RWXX a left join "
				+ " T_YDZF_RWXX_USER as b on a.rwbh=b.RWXXBH left join PC_Users as c on a.fbr=c.UserID  ";
		// 得到当前用户所在地区
		String UserAreaCode = Global.getGlobalInstance().getAreaCode();
		String temp = "0";
		if (RWZT.equals(RWXX.RWZT_WAIT_DISPATCH)) {// 待提交
			String sql = taskListSql + " where a.fbr='"
					+ Global.getGlobalInstance().getUserid()
					+ "' and (rwly='011' or rwly='012') and rwzt='"
					+ RWXX.RWZT_WAIT_DISPATCH + "' and a.syncdataregioncode='"
					+ UserAreaCode + "' order by fbsj desc";
			temp = String.valueOf(SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql).size());
		} else if (RWZT.equals(RWXX.RWZT_AUDIT_NOPASSED)) {// 待修改
			String sql = taskListSql + " where a.fbr='"
					+ Global.getGlobalInstance().getUserid() + "' and rwzt='"
					+ RWXX.RWZT_AUDIT_NOPASSED + "' and a.syncdataregioncode='"
					+ UserAreaCode + "' order by fbsj desc";
			temp = String.valueOf(SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql).size());
		} else if (RWZT.equals(RWXX.RWZT_ON_EXECUTION)) {// 执行中
			String sql = taskListSql + " where b.userid='"
					+ Global.getGlobalInstance().getUserid()
					+ "' and (rwly='011' or rwly='012') and rwzt='"
					+ RWXX.RWZT_ON_EXECUTION + "' and a.syncdataregioncode='"
					+ UserAreaCode + "' order by fbsj desc";
			temp = String.valueOf(SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql).size());
		} else if (RWZT.equals(RWXX.RWZT_WATE_EXECUTION)) {// 待执行
			String sql = taskListSql + " where b.userid='"
					+ Global.getGlobalInstance().getUserid()
					+ "' and (rwly='011' or rwly='012') and rwzt='"
					+ RWXX.RWZT_WATE_EXECUTION + "' and a.syncdataregioncode='"
					+ UserAreaCode + "' order by fbsj desc";
			temp = String.valueOf(SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql).size());
		}

		// String UserAreaCode = Global.getGlobalInstance().getAreaCode();
		// String temp = "0";
		// if (RWXX.RWZT_WAIT_DISPATCH.equals(RWZT)
		// || RWXX.RWZT_AUDIT_NOPASSED.equals(RWZT)) {
		// temp = SqliteUtil
		// .getInstance()
		// .queryBySqlReturnArrayListHashMap(
		// "select count(*) from T_YDZF_RWXX where RWZT = '"
		// + RWZT + "' and FBR = '" + userID
		// + "' and RWLY in ('011','012');").get(0)
		// .get("count(*)").toString();
		// } else {
		// if (baseUser instanceof OfficeLeader
		// && RWZT.equals(RWXX.RWZT_ON_EXECUTION)) {//
		// // 当用户职务为3的时候，他可以替下面的执法人员执行局长回退的任务
		// temp = SqliteUtil
		// .getInstance()
		// .queryBySqlReturnArrayListHashMap(
		// "select count(*) from YDZF_RWLC a left join T_YDZF_RWXX b where a.[Tid]=b.[RWBH] and (b.[RWZT]='005' or a.[IsPassed] = '0') and Id in (select max(Id) from YDZF_RWLC Group By Tid) and a.[AuditUserId] = 'user_16156f73-abf6-4aa1-bbac-f8038e297a62' and b.[RWLY] in ('011','012');")
		// .get(0).get("count(*)").toString();
		// } else {
		// temp = SqliteUtil
		// .getInstance()
		// .queryBySqlReturnArrayListHashMap(
		// "select count(*) from YDZF_RWLC a left join T_YDZF_RWXX b where a.[Tid]=b.[RWBH] and b.[RWZT]='"
		// + RWZT
		// +
		// "' and Id in (select max(Id) from YDZF_RWLC Group By Tid) and a.[AuditUserId] = '"
		// + userID
		// + "' and b.[RWLY] in ('011','012');")
		// .get(0).get("count(*)").toString();
		// }
		// }
		return temp;
	}

	// 传入一个信息类型
	// 获取当前任务信息集合
	private class RWZTInfo {
		private HashMap<String, Object> map;

		public RWZTInfo(String rWZT, String tip, int imgId, boolean flag) {
			int size = 0;
			if (flag) {// true 代表从本地获取
				size = Integer.valueOf(getTaskNum(rWZT));
			} else {

			}
			map = new HashMap<String, Object>();
			map.put("statu", tip);
			map.put("img", imgId);
			map.put("statuvalue", "0");
			map.put("number", size + "");
		}

		public HashMap<String, Object> getRWZTInfo() {
			return map;
		}
	}

	/**TODO 执法人员没有待提交标签
	 * 查询各个任务信息 
	 */
	protected void initTaskNumData() {
		taskStateData = new ArrayList<HashMap<String, Object>>();
		if (baseUser instanceof ExecuteMan) {
			// 4没有待提交 0、1、2、3其他有
		} else {
			RWZTInfo rwztWaitDispatch = new RWZTInfo(RWXX.RWZT_WAIT_DISPATCH,
					"待提交", R.drawable.rwgl_task_list_dsc, true);
			taskStateData.add(rwztWaitDispatch.getRWZTInfo());
//			if (baseUser instanceof BoardLeadership || baseUser instanceof DeputyLeader) {
//				for (int i = 0; i < listandNumArr.size(); i++) {
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				String name = String.valueOf(listandNumArr.get(i).get("Name"));
//				
//						
//				 if (name.contains("待审核")) {
//					map.put("statu", name);
//					map.put("statuvalue",String.valueOf(listandNumArr.get(i).get("QueryType")));
//					map.put("img", R.drawable.rwgl_task_list_dxf);
//					map.put("number", String.valueOf(listandNumArr.get(i).get("Value")));
//					taskStateData.add(map);
//				} else if (name.contains("待修改")) {
//					map.put("statu", name);
//					map.put("statuvalue",String.valueOf(listandNumArr.get(i).get("QueryType")));
//					map.put("img", R.drawable.rwgl_task_list_dxg);
//					map.put("number", String.valueOf(listandNumArr.get(i).get("Value")));
//					taskStateData.add(map);
//				}else if (name.contains("已办结")) {// 缺图片4
//					map.put("statu", name);
//					map.put("statuvalue",String.valueOf(listandNumArr.get(i).get("QueryType")));
//					map.put("img", R.drawable.rwgl_ybj);
//					map.put("number", String.valueOf(listandNumArr.get(i).get("Value")));
//					taskStateData.add(map);
//				}
//			
//			}
//			}
			
		}

		for (int i = 0; i < listandNumArr.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String name = String.valueOf(listandNumArr.get(i).get("Name"));
			map.put("statu", name);
			map.put("statuvalue",
					String.valueOf(listandNumArr.get(i).get("QueryType")));
			if (name.contains("待签收")) { // 缺图片1
				map.put("img", R.drawable.rwgl_dqs);
			} else if (name.contains("待审核")) {
				map.put("img", R.drawable.rwgl_task_list_dxf);
			} else if (name.contains("待修改")) {
				map.put("img", R.drawable.rwgl_task_list_dxg);
			} else if (name.contains("待执行")) {
				map.put("img", R.drawable.rwgl_icon_taskwait);
			} else if (name.contains("执行中")) {
				map.put("img", R.drawable.rwgl_task_list_zxz);
			} else if (name.contains("待协办")) {
				map.put("img", R.drawable.rwgl_task_list_dxz);
			} else if (name.contains("待归档")) {
				map.put("img", R.drawable.rwgl_task_list_zwwc);
			} else if (name.contains("已挂起")) {// 缺图片2
				map.put("img", R.drawable.rwgl_ygq);
			} else if (name.contains("待上报")) {// 缺图片3
				map.put("img", R.drawable.rwgl_dsb);
			} else if (name.contains("已办结")) {// 缺图片4
				map.put("img", R.drawable.rwgl_ybj);
			} else {
				map.put("img", R.drawable.rwgl_task_list_dxg);
			}
			map.put("number", String.valueOf(listandNumArr.get(i).get("Value")));
			taskStateData.add(map);
		}
		// RWZTInfo rwztAuditNopassed = new RWZTInfo(RWXX.RWZT_AUDIT_NOPASSED,
		// "待修改", R.drawable.rwgl_task_list_dxg, true);
		// RWZTInfo rwztWateExecution = new RWZTInfo(RWXX.RWZT_WATE_EXECUTION,
		// "待执行", R.drawable.rwgl_icon_taskwait, true);
		// RWZTInfo rwztOnExecution = new RWZTInfo(RWXX.RWZT_ON_EXECUTION,
		// "执行中",
		// R.drawable.rwgl_task_list_zxz, true);
		// if (baseUser instanceof OfficeMan) {// 办公室
		// /** 待提交条数 */
		// taskStateData.add(rwztWaitDispatch.getRWZTInfo());
		//
		// /** 待审核 */
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("statu", "待审核");
		// map.put("img", R.drawable.rwgl_task_list_dxf);
		// map.put("number", WAIT_AUDIT_NUM + "");
		// taskStateData.add(map);
		//
		// /** 待修改 */
		// taskStateData.add(rwztAuditNopassed.getRWZTInfo());
		//
		// /** 待执行条数 */
		// // taskStateData.add(rwztWateExecution.getRWZTInfo());
		//
		// /** 执行中条数 */
		// // taskStateData.add(rwztOnExecution.getRWZTInfo());
		//
		// /** 待协助条数 */
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("statu", "待协助");
		// map3.put("img", R.drawable.rwgl_task_list_dxz);
		// map3.put("number", WAIT_ASSIST_NUM + "");
		// taskStateData.add(map3);
		//
		// /** 待归档条数 */
		// map = new HashMap<String, Object>();
		// map.put("statu", "待归档");
		// map.put("img", R.drawable.rwgl_task_list_zwwc);
		// map.put("number", WAIT_ARCHIVE_NUM + "");
		// taskStateData.add(map);
		//
		// } else if (baseUser instanceof BoardLeadership) {// 局长
		// /** 待提交条数 */
		// taskStateData.add(rwztWaitDispatch.getRWZTInfo());
		// /** 待审核 */
		//
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("statu", "待审核");
		// map.put("img", R.drawable.rwgl_task_list_dxf);
		// map.put("number", WAIT_AUDIT_NUM + "");
		// taskStateData.add(map);
		//
		//
		//
		// /** 待协助条数 */
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("statu", "待协助");
		// map3.put("img", R.drawable.rwgl_task_list_dxz);
		// map3.put("number", WAIT_ASSIST_NUM + "");
		// taskStateData.add(map3);
		//
		// /** 待归档条数 */
		// HashMap<String, Object> m = new HashMap<String, Object>();
		// m.put("statu", "待归档");
		// m.put("img", R.drawable.rwgl_task_list_zwwc);
		// m.put("number", WAIT_ARCHIVE_NUM + "");
		// taskStateData.add(m);
		//
		// } else if (baseUser instanceof DeputyLeader) {// 分局局长
		// /** 待提交条数 */
		// taskStateData.add(rwztWaitDispatch.getRWZTInfo());
		// /** 待审核 */
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("statu", "待审核");
		// map.put("img", R.drawable.rwgl_task_list_dxf);
		// map.put("number", WAIT_AUDIT_NUM + "");
		// taskStateData.add(map);
		//
		//
		//
		// /** 待协助条数 */
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("statu", "待协助");
		// map3.put("img", R.drawable.rwgl_task_list_dxz);
		// map3.put("number", WAIT_ASSIST_NUM + "");
		// taskStateData.add(map3);
		//
		// /** 待归档条数 */
		// HashMap<String, Object> m = new HashMap<String, Object>();
		// m.put("statu", "待归档");
		// m.put("img", R.drawable.rwgl_task_list_zwwc);
		// m.put("number", WAIT_ARCHIVE_NUM + "");
		// taskStateData.add(m);
		//
		// } else if (baseUser instanceof ExecuteLeader) {// 直接负责人
		// /** 待提交条数 */
		// taskStateData.add(rwztWaitDispatch.getRWZTInfo());
		// /** 待审核 */
		//
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("statu", "待审核");
		// map.put("img", R.drawable.rwgl_task_list_dxf);
		// map.put("number", WAIT_AUDIT_NUM + "");
		// taskStateData.add(map);
		//
		//
		//
		// /** 待修改 */
		// taskStateData.add(rwztAuditNopassed.getRWZTInfo());
		//
		// /** 待执行条数 */
		// taskStateData.add(rwztWateExecution.getRWZTInfo());
		//
		// /** 执行中条数 */
		// taskStateData.add(rwztOnExecution.getRWZTInfo());
		//
		// /** 待协助条数 */
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("statu", "待协助");
		// map3.put("img", R.drawable.rwgl_task_list_dxz);
		// map3.put("number", WAIT_ASSIST_NUM + "");
		// taskStateData.add(map3);
		// /** 待归档条数 */
		// HashMap<String, Object> m = new HashMap<String, Object>();
		// m.put("statu", "待归档");
		// m.put("img", R.drawable.rwgl_task_list_zwwc);
		// m.put("number", WAIT_ARCHIVE_NUM + "");
		// taskStateData.add(m);
		//
		// } else if (baseUser instanceof ExecuteMan) {// 具体执行人
		// /** 待执行 */
		// taskStateData.add(rwztWateExecution.getRWZTInfo());
		// /** 执行中条数 */
		// taskStateData.add(rwztOnExecution.getRWZTInfo());
		// /** 待协助条数 */
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("statu", "待协助");
		// map3.put("img", R.drawable.rwgl_task_list_dxz);
		// map3.put("number", WAIT_ASSIST_NUM + "");
		// taskStateData.add(map3);
		// /** 待归档条数 */
		// HashMap<String, Object> m = new HashMap<String, Object>();
		// m.put("statu", "待归档");
		// m.put("img", R.drawable.rwgl_task_list_zwwc);
		// m.put("number", WAIT_ARCHIVE_NUM + "");
		// taskStateData.add(m);
		// }
	}
	
  public void initTaskNumData2(){
	  

  }

	/**
	 * 初始化界面
	 */
	private void initView() {
		if (baseUser instanceof UserCreateInterface) {
			queryImg.setVisibility(View.VISIBLE);

			queryImg.setImageResource(R.drawable.add);
			queryImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent _Intent = new Intent(TaskMainActivity.this,
							TaskRegisterActivity.class);
					startActivity(_Intent);
				}
			});
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		refreshableView = new RefreshableView(this);
		refreshableView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		taskListView = new ListView(this);
		taskListView.setCacheColorHint(Color.TRANSPARENT);
		taskListView.setDivider(null);
		taskListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		// 从后台获取列表及对应数量
		listandNumArr = getListandNum();
		initTaskNumData();
		baseGeneralTaskAdapter = new BaseGeneralTaskAdapter(
				TaskMainActivity.this, taskStateData);
		taskListView.setAdapter(baseGeneralTaskAdapter);
		refreshableView.addView(taskListView);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {// 执行下拉刷新操作
				if (yutuLoading != null) {
					yutuLoading.showDialog();
				}
				if (refreshableView != null) {
					refreshableView.finishRefreshing();
				}
				// syncTheLastTasks();
				taskStateData.clear();
				listandNumArr = getListandNum();
				initTaskNumData();
				baseGeneralTaskAdapter = new BaseGeneralTaskAdapter(
						TaskMainActivity.this, taskStateData);
				taskListView.setAdapter(baseGeneralTaskAdapter);
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
			}
		}, 0);
		taskListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(TaskMainActivity.this,
								TaskFlowSildeActivity.class);
						Bundle bundle = new Bundle();

						bundle.putSerializable("obj",
								((BaseGeneralTaskAdapter) arg0.getAdapter())
										.getData());
						bundle.putInt("Position", arg2);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});

		// 查询条件
		View searchView = inflater.inflate(R.layout.task_search, null);
		// 任务名称
		searchTaskname_et = (EditText) searchView
				.findViewById(R.id.search_Taskname_et2);
		// 是否移交执法人员
		// search_moveZhr_checkbox = (CheckBox) searchView
		// .findViewById(R.id.search_moveZhr_checkbox);
		// 创建时间
		startTime_et = (EditText) searchView.findViewById(R.id.startTime2);
		startTime_et.setOnClickListener(new startTimeListener());
		endTime_et = (EditText) searchView.findViewById(R.id.endTime2);
		endTime_et.setOnClickListener(new endTimeListener());

		searchTasktype_et = (EditText) searchView
				.findViewById(R.id.search_Tasktype_et2);
		searchTasktype_et.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//	 createDialogViewForTaskType();
				// 弹出任务来源对话框
				createDialogViewForTaskType2();
			}
		});
		// 查询按钮
		Button searchButton = (Button) searchView
				.findViewById(R.id.search_Task_btn);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String startTime = startTime_et.getText().toString();
				String endTime = endTime_et.getText().toString();
				if (startTime != null && !startTime.equals("")
						&& endTime != null && !endTime.equals("")) {
					if (!DateCompare(startTime, endTime)) {
						Toast.makeText(TaskMainActivity.this, "结束时间不能小于开始时间",
								Toast.LENGTH_SHORT).show();
						endTime_et.setText("");
						return;
					}
				}
				TaskManagerController contro = TaskManagerController
						.getInstance(TaskMainActivity.this);
				// 1147
				// contro.openArrayListParaView(contro.TASK_SEARCH_RESULT_LIST,
				// getSearchCondition());
				contro.openArrayListParaView(contro.TASK_SEARCH_RESULT_LIST,
						getSearchCondition2());
			}
		});
		middleLayout.addView(searchView);
		middleLayout.addView(refreshableView);
		if (baseUser instanceof UserCreateInterface) {
			// 创建任务按钮
			btn_addTask = new Button(this);
			btn_addTask.setBackgroundResource(R.drawable.create_task);
			btn_addTask.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			btn_addTask.setGravity(Gravity.RIGHT);
			btn_addTask.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent _Intent = new Intent(TaskMainActivity.this,
							TaskRegisterActivity.class);
					startActivity(_Intent);
				}
			});
			middleLayout.addView(btn_addTask);
		}
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在更新任务信息，请稍候", "");
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void refuseView() {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
				//	requestArchiveTaskNum();
					if (!(baseUser instanceof ExecuteMan)) {
					//	requestAuditTaskNum();
					}
				//	requestAssistTaskNum();
				}
				initTaskNumData();
				return null;
			}

			protected void onPostExecute(Void result) {
				baseGeneralTaskAdapter.updateData(taskStateData);
			};
		}.execute();
	}

	private class BaseGeneralTaskAdapter extends BaseAdapter {
		private final LayoutInflater inflater;
		private ArrayList<HashMap<String, Object>> taskData;

		private BaseGeneralTaskAdapter(Context context,
				ArrayList<HashMap<String, Object>> taskData) {
			inflater = LayoutInflater.from(context);
			this.taskData = taskData;
		}

		public void updateData(ArrayList<HashMap<String, Object>> taskData) {
			this.taskData = taskData;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return taskData.size();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return taskData;
		}

		@Override
		public Object getItem(int position) {
			return taskData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.general_task_list_item,
						null);
				holder.task_img = (ImageView) convertView
						.findViewById(R.id.image);
				holder.general_task_tv = (TextView) convertView
						.findViewById(R.id.general_task_tv);
				holder.number_of_tasks_tv = (TextView) convertView
						.findViewById(R.id.number_of_tasks_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (taskData.size() == 0) {
				YutuLoading yutuLoading = new YutuLoading(TaskMainActivity.this);
				yutuLoading.setLoadMsg("", "用户信息缺少，请同步用户信息后重试！");
				yutuLoading.setFailed();
				convertView = yutuLoading;
			} else {
				holder.task_img.setImageResource((Integer) taskData.get(
						position).get("img"));
				holder.general_task_tv.setText(taskData.get(position)
						.get("statu").toString());
				String num = taskData.get(position).get("number").toString();
				holder.number_of_tasks_tv.setText(num);
			}
			return convertView;
		}
	}

	public static class ViewHolder {
		ImageView task_img;
		TextView general_task_tv;
		TextView number_of_tasks_tv;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
			refuseView();
			switch (msg.what) {
			case 0:
				Toast.makeText(TaskMainActivity.this, "网络已断开，无法获取最新任务",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(TaskMainActivity.this, "数据更新已完成",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	/**
	 * 同步最新任务
	 */
	public void syncTheLastTasks() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					handler.sendEmptyMessage(0);
					return;
				}
				// rwxx.getNewTask(TaskMainActivity.this);
				handler.sendEmptyMessage(1);
			}
		}).start();
	}

	/**
	 * 请求待审核的任务条数
	 */
	public void requestAuditTaskNum() {
		String methodName = "GetAwaitTaskCountByUserId";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("userId", userID);
		conditions.put("syncDataRegionCode", Global.getGlobalInstance()
				.getAreaCode());
		String token = "";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		conditions.put("token", token);
		params.add(conditions);
		WAIT_AUDIT_NUM = 0;
		try {
			Integer result = (Integer) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_INT, true);
			if (result != null) {
				WAIT_AUDIT_NUM = result;
			}
		} catch (IOException e) {
			WAIT_AUDIT_NUM = 0;
			e.printStackTrace();
		}
	}

	/**
	 * 请求待归档的任务条数
	 */
//	public void requestArchiveTaskNum() {
//		String methodName = "GetAwaitFileTaskCountByUserId";
//		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
//		HashMap<String, Object> conditions = new HashMap<String, Object>();
//		conditions.put("userId", userID);
//		conditions.put("syncDataRegionCode", Global.getGlobalInstance()
//				.getAreaCode());
//		String token = "";
//		try {
//			token = DESSecurity.encrypt(methodName);
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
//		conditions.put("token", token);
//		params.add(conditions);
//		WAIT_ARCHIVE_NUM = 0;
//		try {
//			Integer result = (Integer) WebServiceProvider.callWebService(
//					Global.NAMESPACE, methodName, params, Global
//							.getGlobalInstance().getSystemurl()
//							+ Global.WEBSERVICE_URL,
//					WebServiceProvider.RETURN_INT, true);
//			if (result != null) {
//				WAIT_ARCHIVE_NUM = result;
//			}
//		} catch (IOException e) {
//			WAIT_ARCHIVE_NUM = 0;
//			e.printStackTrace();
//		}
//	}

	/**
	 * 请求待协助的任务条数
	 */
	public void requestAssistTaskNum() {
		String methodName = "GetAssistTaskCountByUserId";
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("userId", userID);
		conditions.put("syncDataRegionCode", Global.getGlobalInstance()
				.getAreaCode());
		String token = "";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		conditions.put("token", token);
		params.add(conditions);
		WAIT_ASSIST_NUM = 0;
		try {
			Integer result = (Integer) WebServiceProvider.callWebService(
					Global.NAMESPACE, methodName, params, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_INT, true);
			if (result != null) {
				WAIT_ASSIST_NUM = result;
				/*
				 * String[] node={"TaskCount"}; WAIT_AUDIT_NUM=(Integer)
				 * JsonHelper.paseJSON((String) result,
				 * node).get(0).get("TaskCount");
				 */
			}
		} catch (IOException e) {
			WAIT_ASSIST_NUM = 0;
			e.printStackTrace();
		}
	}

	// 任务类型
	public void createDialogViewForTaskType() {
		LayoutInflater inflater = LayoutInflater.from(TaskMainActivity.this);
		View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
		TextView expand_title_tv = (TextView) viewex
				.findViewById(R.id.expand_title_tv);
		expand_title_tv.setText("任务来源");

		ArrayList<HashMap<String, Object>> groupData = rwxx.getTask_type();
		final List<String> groupList = new ArrayList<String>();
		if (groupData != null && groupData.size() > 0) {
			for (HashMap<String, Object> hashMap : groupData) {
				String tnameStr = hashMap.get("name").toString();
				groupList.add(tnameStr);
			}
		}
		final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx
				.getTask_type_child(groupData);

		final ExpandableListView expandableListView = (ExpandableListView) viewex
				.findViewById(R.id.expandablelistview);
		ExpandableBaseAdapter adapter = new ExpandableBaseAdapter(this,
				groupList, childMapData);
		expandableListView.setAdapter(adapter);
		expandableListView.setCacheColorHint(0);// 设置拖动列表的时候防止出现黑色背景
		// 设置 可扩展expandableListView的指示器
		expandableListView.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));

		AlertDialog.Builder builder = new AlertDialog.Builder(
				TaskMainActivity.this);
		builder.setIcon(R.drawable.yutu);
		builder.setTitle("请选择任务类型");
		builder.setView(viewex);

		final AlertDialog dialog = builder.create();
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
				searchTasktype_et.setText(cnames);
				searchTasktype_et.setTag(rwlxCode);

				dialog.cancel();
				return false;
			}
		});
	}

	String[] items = { "现场执法", "一般任务流程", "移交移送执法人员", "计划任务流程" };
	String taskFrom = "";
	
	// 任务来源 byk
	public void createDialogViewForTaskType2() {
		
		List<String> names=new ArrayList<String>();
		final List<String> codes=new ArrayList<String>();
	String sql="select * from T_YDZF_RWLX";
	ArrayList<HashMap<String,Object>> maps = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	
		for (int i = 0; i < maps.size(); i++) {
			HashMap<String,Object> hashMap = maps.get(i);
			String name = hashMap.get("name").toString();
			String code = hashMap.get("code").toString();
			names.add(name);
			codes.add(code);
		}
		
		final String[] array = names.toArray(new String[maps.size()]);
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("选择任务类型")
				.setIcon(R.drawable.yutu)
				.setSingleChoiceItems(array, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								taskFrom = codes.get(which);
								searchTasktype_et.setText(array[which]);
								dialog.dismiss();
							}
						}).create();
		dialog.show();

	}

	/** 查询开始时间 **/
	private class startTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(TaskMainActivity.this,
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
							startTime_et.setText(year + "-" + monthStr + "-"
									+ dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	/** 查询结束时间 **/
	private class endTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(TaskMainActivity.this,
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
							endTime_et.setText(year + "-" + monthStr + "-"
									+ dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	/**
	 * 两个日期比较
	 * 
	 * @throws ParseException
	 */
	private boolean DateCompare(String startTime, String endTime) {
		boolean flag = false;
		if ((java.sql.Date.valueOf(startTime)).before(java.sql.Date
				.valueOf(endTime))) {
			flag = true;
		}
		if ((java.sql.Date.valueOf(startTime)).equals(java.sql.Date
				.valueOf(endTime))) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 转成小写的key
	 * 
	 * @param data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> parseLowerList(
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> parseData = new ArrayList<HashMap<String, Object>>();
		;
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = data.get(i).entrySet().iterator();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(),
						entry.getValue());
			}
			parseData.add(map);
		}
		return parseData;
	}

	// 获取查询任务条件集合 Y
	public ArrayList<HashMap<String, Object>> getSearchCondition() {
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
		String taskSource = "";
		if (!searchTasktype_et.getText().toString().equals("")) {
			taskSource = searchTasktype_et.getTag().toString();
		}
		String rwly = "";
		// if (search_moveZhr_checkbox.isChecked()) {
		// rwly = "012";
		// } else {
		// rwly = "011";
		// }
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		 * 
		 * map.put("pageIndex",1); map.put("pageSize",15);
		 */
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
		String stime = startTime_et.getText().toString();
		String etime = endTime_et.getText().toString();
		String startDate = "";
		ProgressDialog pdDialog = new ProgressDialog(this);
		String endDate = "";
		if (!stime.equals("")) {
			try {
				startDate = from.format(from.parse(stime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!etime.equals("")) {
			try {
				endDate = from.format(from.parse(etime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		map.put("taskName", searchTaskname_et.getText().toString());
		map.put("beginTime", startDate);
		map.put("endTime", endDate);
		map.put("loginUserId", userID);
		map.put("taskSource", taskSource);
		map.put("rwly", rwly);
		searchdata.add(map);
		return searchdata;
	}

	// 获取查询任务条件集合 Y
	public ArrayList<HashMap<String, Object>> getSearchCondition2() {
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
	
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		 * 
		 * map.put("pageIndex",1); map.put("pageSize",15);
		 */
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
		String stime = startTime_et.getText().toString();
		String etime = endTime_et.getText().toString();
		String startDate = "";
		ProgressDialog pdDialog = new ProgressDialog(this);
		String endDate = "";
		if (!stime.equals("")) {
			try {
				startDate = from.format(from.parse(stime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!etime.equals("")) {
			try {
				endDate = from.format(from.parse(etime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (searchTaskname_et.getText().toString() != null
				&& !"".equals(searchTaskname_et.getText().toString())) {
			map.put("LeTaskName", searchTaskname_et.getText().toString());
		}
		if (startDate != null && !"".equals(startDate)) {
			map.put("LeTaskCreatedStartTime", startDate);
		}

		if (endDate != null && !"".equals(endDate)) {
			map.put("LeTaskCreatedEndTime", endDate);
		}

		if (taskFrom != null && !"".equals(taskFrom)) {
			map.put("LeTaskSource", "");
			map.put("LeTaskType", taskFrom);
			
		}

		map.put("RegionCode", Global.getGlobalInstance().getAreaCode());
		searchdata.add(map);
		return searchdata;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data1) {
		if (requestCode == 520 && resultCode == -1) {

			TaskManagerController contro = TaskManagerController
					.getInstance(TaskMainActivity.this);
			contro.openArrayListParaView(contro.TASK_SEARCH_RESULT_LIST,
					getSearchCondition());
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
			return;
		}
	}

	/**
	 * 获取任务管理模块列表及数量
	 * 
	 */
	public ArrayList<HashMap<String, Object>> getListandNum() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		final String methodName = "GetLeTaskProcessStatusCount";
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TransactorCode", userID);
		data_json.add(conditions);
		String TransactorCode = JsonHelper.listToJSONXin(data_json);

		 ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		// param.put("parms", TransactorCode);
		param.put("TransactorCode", TransactorCode);
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
}
