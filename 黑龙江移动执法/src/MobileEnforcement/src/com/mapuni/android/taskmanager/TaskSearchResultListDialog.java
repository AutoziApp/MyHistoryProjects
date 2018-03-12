package com.mapuni.android.taskmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseDialog;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

public class TaskSearchResultListDialog extends BaseDialog implements DialogInterface {
	private Context mContext;
	private YutuLoading yutuLoading;
	private PagingListView listview;
	/** 任务状态 */
	String rwValue = "";
	private CompanyAdapter companyAdapter;
	/** 从查询页面传来过条件集合 */
	ArrayList<HashMap<String, Object>> searchConditionsList;
	/** 分页请求数据的页数 */
	private int pageIndex = 1;
	/** 分页请求一页的数量 */
	private final int pageSize = 30;
	/** 用户所属地区 **/
	private final String UserAreaCode = Global.getGlobalInstance().getAreaCode();

	public TaskSearchResultListDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public TaskSearchResultListDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public void show() {
		super.show();
		yutuLoading = new YutuLoading(mContext);
		yutuLoading.setLoadMsg("正在请求数据，请稍后！", "");
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();
	}

	public void showWithPara(ArrayList<HashMap<String, Object>> getSearchConditionsList) {
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "任务查询");
		initView();
		initData(getSearchConditionsList);
	}

	private void initData(ArrayList<HashMap<String, Object>> getSearchConditionsList) {
		searchConditionsList = getSearchConditionsList;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					handler.sendEmptyMessage(1);
					return;
				}
				//ArrayList<HashMap<String, Object>> data = requestSearchTask(searchConditionsList);
				ArrayList<HashMap<String, Object>> data = requestSearchTask2(searchConditionsList);
				if (data.size() == 0) {
					handler.sendEmptyMessage(3);
					return;
				}
				Message ms = new Message();
				ms.what = 2;
				ms.obj = data;
				handler.sendMessage(ms);

			}
		}).start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			yutuLoading.dismissDialog();
			switch (msg.what) {
			case 0:
				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) msg.obj;
				if (data.size() < pageSize) {
					if (pageIndex > 1) {
						listview.setIsCompleted(true);
						Toast.makeText(mContext, "全部数据加载已完成", Toast.LENGTH_SHORT).show();
					}
				}
				companyAdapter.addData(data);
				break;
			case 1:
				replaceView("由于网络原因，无法查询任务");
				break;
			case 2:
				ArrayList<HashMap<String, Object>> firstData = (ArrayList<HashMap<String, Object>>) msg.obj;
				companyAdapter = new CompanyAdapter(firstData);
				listview.setAdapter(companyAdapter);
				break;
			case 3:
				replaceView("没有查询到符合条件的数据");
				break;
			}

		};
	};

	/**
	 * 没有数据时替换布局
	 */
	public void replaceView(String tip) {
		/** 没有数据，替换布局 */
		YutuLoading loading = new YutuLoading(mContext);
		loading.setLoadMsg("加载中", tip, Color.BLACK);
		loading.setFocusable(false);
		loading.setClickable(false);
		loading.setEnabled(false);
		loading.setFailed();
		loading.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.FILL_PARENT,
				android.widget.AbsListView.LayoutParams.FILL_PARENT));
		listview.setVisibility(View.GONE);

		linearlayout.addView(loading);

	}

	LinearLayout linearlayout;

	private void initView() {
		// 判断如果是查询出来的结果直接列表显示
		linearlayout = new LinearLayout(mContext);
		linearlayout.setOrientation(1);
		linearlayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		listview = new PagingListView(mContext);
		listview.setCacheColorHint(Color.TRANSPARENT);
		listview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		linearlayout.addView(listview);
		((LinearLayout) findViewById(R.id.middleLayout)).addView(linearlayout);
		listview.setOnPageCountChangListener(new PagingListView.PageCountChangListener() {

			@Override
			public void onAddPage(AbsListView view) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
							handler.sendEmptyMessage(1);
							return;
						}
						ArrayList<HashMap<String, Object>> data = requestSearchTask2(searchConditionsList);
						Message ms = new Message();
						ms.what = 0;
						ms.obj = data;
						handler.sendMessage(ms);
					}
				}).start();

			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//String guid = companyAdapter.getData().get(arg2).get("guid").toString();
				
				//修改获取guid  
				String rwbh_byk =  companyAdapter.getData().get(arg2).get("rwbh").toString();
				String guid_byk = getGUID(rwbh_byk);
				ArrayList<HashMap<String, Object>> data = companyAdapter.getData();
				String letaskid = data.get(arg2).get("letaskid").toString();
				RWXX RWXX = new RWXX();
				RWXX.setCurrentID(guid_byk);
				String rwzt = companyAdapter.getData().get(arg2).get("rwzt").toString();
				String rwlx = companyAdapter.getData().get(arg2).get("rwlx").toString();
			
				TaskManagerController contro = TaskManagerController.getInstance(mContext);
				contro.openObjectParaView(contro.TASK_SEARCH_RESULT_DETAIL, RWXX,rwbh_byk,letaskid,rwzt,rwlx);
				
				ArrayList<String> tables = new ArrayList<String>();
				tables.add("TaskEnpriLink");// TaskEnpriLink表中状态发生 改变，需要同步一下
				// 同步一下着两张表
			//	DataSyncModel dm2 = new DataSyncModel();

		//		dm2.syncServiceData(tables, true);
				return;
			}
		});

	}

	/**
	 * 查询出来的任务信息列表
	 * 
	 */
	private class CompanyAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> CompanyAdapterData;

		public CompanyAdapter(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.CompanyAdapterData = CompanyAdapterData;
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

		public void addData(ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.CompanyAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return CompanyAdapterData;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.qyname_list, null);

				viewHolder = new ViewHolder();
				viewHolder.rw_icon = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.rwmc_text = (TextView) convertView.findViewById(R.id.qymc_text);
				viewHolder.zt_btn = (Button) convertView.findViewById(R.id.rwzt);
				convertView.setTag(viewHolder);
			}

			viewHolder = ((ViewHolder) convertView.getTag());
			viewHolder.rwmc_text.setText(CompanyAdapterData.get(position).get("rwmc").toString());
			//修改guid
			String rwbh_byk = CompanyAdapterData.get(position).get("rwbh").toString();
			String guid_byk = getGUID(rwbh_byk);
			viewHolder.rwmc_text.setTag(guid_byk);
			viewHolder.zt_btn.setBackgroundColor(Color.TRANSPARENT);

			String rwzt = CompanyAdapterData.get(position).get("rwzt").toString();
			// 重新整理的任务状态描述和图标的对应关系
			if (rwzt.equals(RWXX.RWZT_WAIT_DISPATCH)) {
				rwValue = "待提交";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dsc);
			} else if (rwzt.equals(RWXX.RWZT_WATE_EXECUTION)) {
				rwValue = "待执行";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_icon_taskwait);
			} else if (rwzt.equals(RWXX.RWZT_ON_EXECUTION)) {
				rwValue = "执行中";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zxz);
			} else if (rwzt.equals(RWXX.RWZT_EXECUTION_FINISH)) {
				rwValue = "执行完成";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zwwc);
			} else if (rwzt.equals(RWXX.RWZT_WAIT_AUDIT)) {
				rwValue = "待审核";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dxf);
			} else if (rwzt.equals(RWXX.RWZT_ON_FINISH)) {
				rwValue = "任务完成";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_yxf);
			} else if (rwzt.equals(RWXX.RWZT_AUDIT_NOPASSED)) {
				rwValue = "审核未通过";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_dxg);
			} else if (rwzt.equals(RWXX.RWZT_ON_RETURN)) {
				rwValue = "退回任务";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_rwht);
			} else if (rwzt.equals(RWXX.RWZT_WAIT_FILE)) {
				rwValue = "待归档";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_task_list_zwwc);
				//tfy:修复已挂起状态
			}else if (rwzt.equals(RWXX.RWZT_YGQ)) {
				rwValue = "已挂起";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_ygq);
			}
			else if (rwzt.equals(RWXX.RWZT_DSB)) {
				rwValue = "待上报";
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_dsb);
			}
			else {
				rwValue = "状态不明确";
				viewHolder.zt_btn.setTextColor(R.color.red);
				viewHolder.rw_icon.setImageResource(R.drawable.rwgl_icon_task_others);
			}
			viewHolder.zt_btn.setText(rwValue);
			return convertView;
		}
	}

	private class ViewHolder {
		ImageView rw_icon;
		TextView rwmc_text;
		Button zt_btn;
	}

	/**
	 * 传入条件调用webservice获取任务信息
	 */
	public ArrayList<HashMap<String, Object>> requestSearchTask(ArrayList<HashMap<String, Object>> searchConditionsList) {

		String methodName = "GetAllTaskByUserId";
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
		//从查询页面传来过条件集合
		HashMap<String, Object> map = searchConditionsList.get(0);
		map.put("pageIndex", pageIndex++);
		map.put("pageSize", pageSize);
		map.put("syncDataRegionCode", UserAreaCode);
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		data.add(map);
		String taskInfoJson = JsonHelper.listToJSON(data);
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("taskInfoJson", taskInfoJson);
		String token = "";
		try {
			token = DESSecurity.encrypt(methodName);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		param.put("token", token);
		params.add(param);
		try {
			String jsonStr = (String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (jsonStr != null&&!"".equals(jsonStr)) {
				// String node[]
				// ={"RowNumber","RWBH","FBSJ","BJQX","RWMC","KeyWord","RWLYMC","RWZT","RWZTMC","FBRMC"};
				searchdata = JsonHelper.paseJSON(jsonStr);
				if (searchdata == null) {
					searchdata = new ArrayList<HashMap<String, Object>>();
				} else {
					searchdata = parseLowerList(searchdata);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchdata;
	}
	
	
	/**
	 * 传入条件调用webservice获取任务信息
	 */
	public ArrayList<HashMap<String, Object>> requestSearchTask2(ArrayList<HashMap<String, Object>> searchConditionsList) {

		String methodName = "QueryList";
		ArrayList<HashMap<String, Object>> searchdata = new ArrayList<HashMap<String, Object>>();
		//从查询页面传来过条件集合
		HashMap<String, Object> map = searchConditionsList.get(0);
//		map.put("pageIndex", pageIndex++);
//		map.put("pageSize", pageSize);
		map.put("pageIndex",pageIndex++ );
		map.put("pageSize", "15");
	
	String hashMapToJson = hashMapToJson(map);
		
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("flowQueryConditionModel", hashMapToJson);
		
		params.add(param);
		try {
			String jsonStr = (String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			if (jsonStr != null&&!"".equals(jsonStr)) {
				// String node[]
				// ={"RowNumber","RWBH","FBSJ","BJQX","RWMC","KeyWord","RWLYMC","RWZT","RWZTMC","FBRMC"};
			
				//searchdata = JsonHelper.paseJSON(jsonStr);
				//TODO 处理字段不同 BYK  
				TaskListModel paseJSON = JsonHelper.paseJSON(jsonStr, TaskListModel.class);
				
				List<TaskListDetail> taskListLine = paseJSON.TaskListLine;
				ArrayList<HashMap<String, Object>> datas=new ArrayList<HashMap<String,Object>>();
				String rwzt_byk="";
				for (int i = 0; i < taskListLine.size(); i++) {
					HashMap<String, Object>  maps=new HashMap<String, Object>();
					
					TaskListDetail taskListDetail = taskListLine.get(i);
					maps.put("fbsj", taskListDetail.LeTaskCreatedTime);
					maps.put("bjqx", taskListDetail.LeTaskTransactedTime);
					//名称
					maps.put("rwmc", taskListDetail.LeTaskName);
					//状态 
					//待下发			
					if (taskListDetail.LeTaskProcessStatus.equals("10")) {
						maps.put("rwzt","000");
						rwzt_byk="000";
					}else if (taskListDetail.LeTaskProcessStatus.equals("15")) {
						//待执行
						maps.put("rwzt","003");
						rwzt_byk="003";
					}else if (taskListDetail.LeTaskProcessStatus.equals("16")) {
						//执法中
						maps.put("rwzt","005");
						rwzt_byk="005";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("23")) {
						//以办结
						maps.put("rwzt","009");
						rwzt_byk="009";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("12")) {
						//待审核
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("13")) {
						//待审核
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("14")) {
						//审核未通过
						maps.put("rwzt","008");
						rwzt_byk="008";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("19")) {
						//退回任务
						maps.put("rwzt","010");
						rwzt_byk="010";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("20")) {
						//待归档
						maps.put("rwzt","011");
						rwzt_byk="011";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("17")) {
						//执行完成*待审核
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("18")) {
						//执行完成*待审核
						maps.put("rwzt","007");
						rwzt_byk="007";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("21")) {
						//已挂起
						maps.put("rwzt","012");
						rwzt_byk="012";
					}
					else if (taskListDetail.LeTaskProcessStatus.equals("22")) {
						//已挂起
						maps.put("rwzt","013");
						rwzt_byk="013";
					}
					else {
						//其他
						maps.put("rwzt","-1");
					}
					//任务类型
					
					maps.put("rwlx",taskListDetail.LeTaskType);
					//  任务来源
					maps.put("rwly",taskListDetail.LeTaskSource);
					//紧急程度
					maps.put("jjcd",taskListDetail.LeTaskUrgency);
					//发布人
					maps.put("fbr",taskListDetail.LeTaskPublisher);
					//主题词
					maps.put("LeTaskSubjectTerm",taskListDetail.LeTaskSubjectTerm);
					
					//流程发起来源(1：单位内部，2：上级单位，3：下级单位)

					maps.put("OriginatingSource",taskListDetail.OriginatingSource);
					
					//当前流程任务Id

					maps.put("CurrentFlowTaskId",taskListDetail.CurrentFlowTaskId);
					
					//上节流程任务Id

					maps.put("PrevFlowTaskId",taskListDetail.PrevFlowTaskId);
					//上节办理人

					maps.put("PrevTransactor",taskListDetail.PrevTransactor);
					
					//上节办理意见

					maps.put("PrevTransactedComment",taskListDetail.PrevTransactedComment);
					//下节办理人

					maps.put("NextTransactor",taskListDetail.NextTransactor);
					//任务code
					maps.put("rwbh", taskListDetail.LeTaskCode);
					
					//任务id
					maps.put("LeTaskId", taskListDetail.LeTaskId);
					
					datas.add(maps);
					
					//插入本地数据库
					ContentValues cv = new ContentValues();
					if ("011".equals(taskListDetail.LeTaskSource)) {
						cv.put("rwly", RWXX.YBRW_LY);
					}else if ("010".equals(taskListDetail.LeTaskSource)) {
						cv.put("rwly", RWXX.XCZF_LY);
					}
					String guid_byk = UUID.randomUUID().toString();
					cv.put("guid", guid_byk);
					cv.put("bz", taskListDetail.PrevTransactedComment);
					cv.put("rwmc", taskListDetail.LeTaskName);
					cv.put("rwbh", taskListDetail.LeTaskCode);
					cv.put("jjcd", taskListDetail.LeTaskUrgency);
		
					
					String sql = "select * from PC_USERS where U_RealName='"+taskListDetail.LeTaskPublisher.toString().trim()+"'";

					HashMap<String, Object> userInfo = SqliteUtil.getInstance().getDataMapBySqlForDetailed(sql);
					if (userInfo.size()!=0) {
						cv.put("fbr",userInfo.get("userid").toString());
					}else{
						cv.put("fbr","");
					}
					
					// 任务所属地区和登录用户所属地区相同
					cv.put("syncdataregioncode", UserAreaCode);
					cv.put("fbsj", taskListDetail.LeTaskCreatedTime);
					cv.put("bjqx", taskListDetail.LeTaskTransactedTime);
					cv.put("rwzt", rwzt_byk);
					cv.put("rwlx",taskListDetail.LeTaskType);
					HashMap<String, Object> conditions = new HashMap<String, Object>();
					conditions.put("rwbh", taskListDetail.LeTaskCode);
					
				ArrayList<HashMap<String, Object>> rwdata = getList2("rwzt", conditions, "T_YDZF_RWXX");
					
					if (rwdata == null || rwdata.size() == 0) {
						
						SqliteUtil.getInstance().insert(cv, "T_YDZF_RWXX");
					}else {
						String[] whereArgs = { taskListDetail.LeTaskCode };
						SqliteUtil.getInstance().update("T_YDZF_RWXX", cv, "guid=?", whereArgs);
					}
				}
				
				
				if (datas == null) {
					searchdata = new ArrayList<HashMap<String, Object>>();
				} else {
					searchdata = parseLowerList(datas);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchdata;
	}
	/**
	 * 转成小写的key
	 * 
	 * @param data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> parseLowerList(ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> parseData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = data.get(i).entrySet().iterator();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(), entry.getValue());
			}
			parseData.add(map);

		}
		return parseData;
	}
	

    /**把数据源HashMap转换成json 
     * @param map  
     */  
    public static String hashMapToJson(HashMap map) {  
        String string = "{";  
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
            Entry e = (Entry) it.next();  
            string +=  e.getKey() + ":";  
            string += "\"" + e.getValue() + "\",";  
        }  
        string = string.substring(0, string.lastIndexOf(","));  
        string += "}";  
        return string;  
    }  
    
	public String getGUID(String rwbh) {
		String statusSql = "select guid from T_YDZF_RWXX where rwbh='" + rwbh
				+ "'";
		ArrayList<HashMap<String, Object>> data = null;
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				statusSql);

		if (data != null && data.size() > 0) {
			return (String) data.get(0).get("guid");
		} else {
			return "";
		}

	}
	
	
	/**
	 * 根据提供的条件，在表中查询单列数据
	 * 
	 * @param colum
	 *            需要查询的列
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList2(String colum,
			HashMap<String, Object> conditions, String table) {
		StringBuilder sql = new StringBuilder("select " + colum + " from "
				+ table + " where ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				sql.append( condition.getKey() + " = '"
						+ condition.getValue() + "'");
			}
		}
		try {
			SqliteUtil instance = SqliteUtil.getInstance();
			ArrayList<HashMap<String, Object>> data = instance.queryBySqlReturnArrayListHashMap(sql
					.toString());
			return data;
		} catch (SQLiteException e) {
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();
	}
	
	
	
}
