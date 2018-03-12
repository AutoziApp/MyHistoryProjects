package com.mapuni.android.taskmanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ser.JdkSerializers.ClassSerializer;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalchina.gallery.ImageGalleryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.base.BaseDialog;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.LoadDetailLayout;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.QdjcnlActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.enterpriseArchives.SlideOnLoadAdapter;
import com.mapuni.android.enterpriseArchives.SlideView;
import com.mapuni.android.model.LeTaskEntLinkListxx2;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.taskmanager.TaskInformationActivity.QyList;
import com.mapuni.yqydweb.YqydWebActivity;

public class TaskSearchResultDetailDialog extends BaseDialog implements
		DialogInterface {

	private Context mContext;
	private RWXX rwxx;
	/** 任务编号 */
	private String RWBH;
	/** 任务GUID */
	private String GUID;
	LayoutInflater layoutInflater;
	SlideView slideView;
	private YutuLoading yutuLoading;
	private HashMap<String, Object> RWDetail;
	private String position;

	/** 任务详情 */
	private final int TASKDETAIL = 0;
	/** 任务附件 */
	private final int TASK_ATTACHMENT = 1;
	/** 任务流程 */
	private final int TASK_PROCESS = 2;
	/** 添加企业执行 */
	private final int TASK_ADDCOMPANY = 3;

	/** 任务所关联企业的状态 */
	Boolean Task_Enpri_ZT = false;
	private final String TableName = "T_YDZF_RWXX";
	ArrayList<HashMap<String, Object>> qylistdata;

	/** 标题集合 */
	private final ArrayList<String> titles = new ArrayList<String>();
	/** 界面标签集合和 titles 一一对应 */
	private final ArrayList<Integer> tags = new ArrayList<Integer>();
	/** 待审核任务上行下行标志 1下行 0 上行 */
	private String TaskFlowDirection = "-1";

	CompanyAdapter companyAdapter;
	private String letaskid;
	private String myRwzt;
	private String myRwlx;

	public TaskSearchResultDetailDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public TaskSearchResultDetailDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public void show() {
		super.show();
	}

	public void showWithParaObject(Object Rwxx, String rwbh_byk,String letaskid,String rwzt,String rwlx) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "任务信息");
		// rwxx=(RWXX) getIntent().getExtras().get("BusinessObj");
		rwxx = (RWXX) Rwxx;
		GUID = rwxx.getCurrentID();
		RWBH = rwbh_byk;
		this.letaskid=letaskid;
		this.myRwzt=rwzt;
		this.myRwlx=rwlx;
		
		initData();
	}

	/*
	 * protected void onRestart() {
	 * 
	 * super.onRestart(); if(companyAdapter!=null){ String sql=
	 * "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
	 * +
	 * " where TaskEnpriLink.TaskID='"+RWBH+"' order by TaskEnpriLink.UpdateTime"
	 * ;
	 * qylistdata=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql
	 * ); companyAdapter.updateData(qylistdata) ;
	 * 
	 * } }
	 */

	private void initData() {
		titles.add("基本信息");
		tags.add(TASKDETAIL);
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", GUID);
		ArrayList<HashMap<String, Object>> rwdata = SqliteUtil.getInstance()
				.getList("rwzt", conditions, TableName);
		if (rwdata == null || rwdata.size() == 0) {
			//bif (rwdata != null || rwdata.size() != 0) {
			Toast.makeText(mContext, "本地缺少此任务信息，请同步任务全部数据", Toast.LENGTH_SHORT)
					.show();
			// finish();
			cancel();
			return;

		}
		// RWBH = SqliteUtil.getInstance().getList("rwbh", conditions,
		// TableName)
		// .get(0).get("rwbh").toString();
		titles.add("关联企业");
		tags.add(TASK_ADDCOMPANY);

		titles.add("附件信息");
		tags.add(TASK_ATTACHMENT);
		titles.add("执行情况");
		tags.add(TASK_PROCESS);
		initView();
	}

	private void initView() {
		layoutInflater = LayoutInflater.from(mContext);
		slideView = new SlideView(mContext, 4);

		for (int i = 0; i < titles.size(); i++) {
			SlideOnLoadAdapter adapter = null;
			LinearLayout taskDetailView = new LinearLayout(mContext);
			taskDetailView.setBackgroundColor(Color.TRANSPARENT);
			taskDetailView.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			taskDetailView.setTag(tags.get(i));
			adapter = new SlideOnLoadAdapter(taskDetailView) {

				@Override
				public void OnLoad() {
					yutuLoading = new YutuLoading(mContext);
					yutuLoading.setCancelable(true);
					yutuLoading.showDialog();

					new SyncLoadingData().execute(this.view);
				}
			};
			//标题
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
	// private final int DOWNPICTURE_SUCCESS = 5;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NO_NET:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(mContext, "网络不通，请检查网络设置！", Toast.LENGTH_SHORT)
						.show();

				break;
			case XIAFA_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

				Toast.makeText(mContext, "任务提交成功", Toast.LENGTH_SHORT).show();
				// mContext.finish();
				cancel();
				break;
			case XIAFA_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(mContext, "任务提交失败", Toast.LENGTH_SHORT).show();

				break;
			case BANJIE_SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

				Toast.makeText(mContext, "任务提交成功", Toast.LENGTH_SHORT).show();
				// mContext.finish();
				cancel();
				break;
			case BANJIE_FALI:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(mContext, "任务提交失败", Toast.LENGTH_SHORT).show();

				break;

			default:
				break;
			}
		};
	};
	private String qylistjson;

	private class SyncLoadingData extends AsyncTask<View, Void, Void> {
		private View view;
		private ArrayList<Object> obj;
		ArrayList<HashMap<String, Object>> data;

		@Override
		protected Void doInBackground(View... params) {
			view = params[0];
			obj = new ArrayList<Object>();
			obj.add(getDataList((Integer) view.getTag()));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			int tag = (Integer) view.getTag();

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			switch (tag) {
			case TASKDETAIL:
				data = (ArrayList<HashMap<String, Object>>) obj.get(0);
				HashMap<String,Object> tempMap=new HashMap<String, Object>();
				String sql="select U_RealName from PC_Users where userid='"+data.get(0).get("fbr")+"'";
				ArrayList<HashMap<String,Object>> queryBySqlReturnArrayListHashMap = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
				if (queryBySqlReturnArrayListHashMap.size()!=0) {
					tempMap.put("PublisherId",queryBySqlReturnArrayListHashMap.get(0).get("u_realname") );
				}else{
					tempMap.put("PublisherId",data.get(0).get("fbr"));
				}
				
				tempMap.put("PublishedTime", data.get(0).get("fbsj"));
				
				String rwValue;
				if (myRwzt.equals(RWXX.RWZT_WAIT_DISPATCH)) {
					rwValue = "待提交";
				} else if (myRwzt.equals(RWXX.RWZT_WATE_EXECUTION)) {
					rwValue = "待执行";
				} else if (myRwzt.equals(RWXX.RWZT_ON_EXECUTION)) {
					rwValue = "执行中";
				} else if (myRwzt.equals(RWXX.RWZT_EXECUTION_FINISH)) {
					rwValue = "执行完成";
				} else if (myRwzt.equals(RWXX.RWZT_WAIT_AUDIT)) {
					rwValue = "待审核";
				} else if (myRwzt.equals(RWXX.RWZT_ON_FINISH)) {
					rwValue = "任务完成";
				} else if (myRwzt.equals(RWXX.RWZT_AUDIT_NOPASSED)) {
					rwValue = "审核未通过";
				} else if (myRwzt.equals(RWXX.RWZT_ON_RETURN)) {
					rwValue = "退回任务";
				} else if (myRwzt.equals(RWXX.RWZT_WAIT_FILE)) {
					rwValue = "待归档";
				}else if (myRwzt.equals(RWXX.RWZT_YGQ)) {
					rwValue = "已挂起";
				}
				else if (myRwzt.equals(RWXX.RWZT_DSB)) {
					rwValue = "待上报";
				}
				else {
					rwValue = "状态不明确";
				}
				tempMap.put("rwztmc",rwValue);
				tempMap.put("TaskType","建设项目管理");
				tempMap.put("TaskName",data.get(0).get("rwmc"));
				tempMap.put("TransactedTime", data.get(0).get("bjqx"));
				tempMap.put("Urgency",data.get(0).get("cdname"));
				tempMap.put("TaskType", data.get(0).get("rwlx"));
				tempMap.put("Remark", data.get(0).get("bz"));
				LoadDetailLayout loadDetailLayout = new LoadDetailLayout(
						mContext, rwxx, false);
				((LinearLayout) (view)).addView(loadDetailLayout
						.getDetailView(tempMap));
				break;
			case TASK_ATTACHMENT:
				data = (ArrayList<HashMap<String, Object>>) obj.get(0);
				if (data != null && data.size() > 0) {
					ListView listview = new ListView(mContext);
					listview.setCacheColorHint(Color.TRANSPARENT);
					listview.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					AttachmentAdapter attachmentAdapter = new AttachmentAdapter(
							data);
					listview.setAdapter(attachmentAdapter);
					((LinearLayout) (view)).addView(listview, lp);
					// 点击打开附件
					listview.setOnItemClickListener(new OnItemClickListener() {
						// @Override
						// public void onItemClick(AdapterView<?> arg0, View
						// arg1, int arg2, long arg3) {
						// String attguid =
						// data.get(arg2).get("Guid").toString();
						// FileHelper fileHelper = new FileHelper();
						// fileHelper.showFileByGuid(attguid, mContext);
						// }

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								final int arg2, long arg3) {
							final YutuLoading loading = new YutuLoading(
									mContext);
							loading.setLoadMsg("正在加载附件，请稍等...", "");
							loading.setCancelable(false);
							loading.showDialog();
							new Thread(new Runnable() {
								@Override
								public void run() {
									Looper.prepare();
									// TODO Auto-generated method stub
									FileHelper fileHelper = new FileHelper();
									String attguid = data.get(arg2).get("Guid")
											.toString();
									String Extension = data.get(arg2)
											.get("Extension").toString();
									String FK_Unit = data.get(arg2)
											.get("FK_Unit").toString();
									String FilePath = data.get(arg2)
									.get("FilePath").toString();
//									if (Extension != null&& !Extension.equals("")) {
//										if (".jpg".equals(Extension
//												.toLowerCase())) {
//											ArrayList<String> arrayTotal = new ArrayList<String>();
//											int result1 = fileHelper.downFile(
//													attguid, mContext, loading);
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
//																		mContext,
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
//											Intent intent = new Intent(
//													mContext,
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
//											mContext.startActivity(intent);
//											if (loading != null) {
//												loading.dismissDialog();
//											}
//											return;
//										}
//									}

									fileHelper
											.showFileByGuid(attguid, mContext);
									
									
									if (loading != null) {
										loading.dismissDialog();
									}
								}
							}).start();

						}

					});
				} else {
					YutuLoading loading = new YutuLoading(mContext);
					loading.setLoadMsg("", "暂无附件信息！");
					loading.setFailed();
					((LinearLayout) (view)).addView(loading, lp);
				}
				break;
			case TASK_PROCESS:

				data = (ArrayList<HashMap<String, Object>>) obj.get(0);

				if (data != null && data.size() > 0) {
					ListView lv = new ListView(mContext);
					lv.setCacheColorHint(Color.TRANSPARENT);
					lv.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					ProcedureAdapter proceAdapter = new ProcedureAdapter(data,
							mContext);
					lv.setAdapter(proceAdapter);
					((LinearLayout) (view)).addView(lv, lp);
				} else {
					YutuLoading loading = new YutuLoading(mContext);
					loading.setLoadMsg("", "暂无执行情况信息！");
					loading.setFailed();
					((LinearLayout) (view)).addView(loading, lp);
				}
				break;
			case TASK_ADDCOMPANY:
				qylistdata = (ArrayList<HashMap<String, Object>>) obj.get(0);
				LinearLayout linearlayout = new LinearLayout(mContext);
				linearlayout.setOrientation(1);
				linearlayout.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				// 添加污染源按

				// 企业列表
				ListView listview = new ListView(mContext);
				listview.setCacheColorHint(Color.TRANSPARENT);
				listview.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				companyAdapter = new CompanyAdapter(qylistdata);
				listview.setAdapter(companyAdapter);
				// 点击进入一企一档
				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String qyid = qylistdata.get(arg2).get("qyguid")
								.toString();
//						
						Intent intent = new Intent(mContext, YqydWebActivity.class);
						intent.putExtra("qyid", qyid);
//						Intent intent = new Intent(mContext,
//								EnterpriseArchivesActivitySlide.class);
//						intent.putExtra("qyid", qyid);
						mContext.startActivity(intent);
					}
				});

				linearlayout.addView(listview);

				// 任务办结按钮
				// 判断是否为自己的任务，任务状态为待执行或执行中,并且所关联企业状态都为已上传

				((LinearLayout) (view)).addView(linearlayout, lp);

				if (qylistdata == null || qylistdata.size() == 0) {

					Toast.makeText(mContext, "暂无企业信息！", Toast.LENGTH_SHORT)
							.show();
				}
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
			RWDetail = rwxx.getDetailed(GUID);
			RWBH = RWDetail.get("rwbh").toString();// 初始化任务编号
			// 任务编号 紧急程度
			String jjcd = RWDetail.get("jjcd").toString();
			if (jjcd.endsWith("001"))
				RWDetail.put("jjcd", "一般");
			if (jjcd.endsWith("002"))
				RWDetail.put("jjcd", "紧急");
			if (jjcd.endsWith("003"))
				RWDetail.put("jjcd", "非常紧急");
			// 任务类型
			String rwlx = RWDetail.get("rwlx").toString();
			if (!myRwlx.equals("")) {
				String sql = "select name from t_ydzf_rwlx  where code='"
						+ myRwlx + "'";
				ArrayList<HashMap<String, Object>> data_name = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(sql);
				String sql2 = "select u_realname from pc_users  where userid='" 
						+ RWDetail.get("fbr")+ "'";
				ArrayList<HashMap<String, Object>> data_name2 = SqliteUtil
						.getInstance().queryBySqlReturnArrayListHashMap(sql2);
				
				if (data_name.size() > 0&&data_name2.size()>0) {
					String realName=data_name2.get(0).get("u_realname").toString();
					String rwlxName = data_name.get(0).get("name").toString();
					RWDetail.put("rwlx", rwlxName);
					RWDetail.put("fbr", realName);
				}
			}else{
				RWDetail.put("rwlx", RWDetail.get("lyname").toString());
//				ArrayList<String> tables = new ArrayList<String>();
//				tables.add("T_YDZF_RWXX");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//				// 同步一下着两张表
//				DataSyncModel dm2 = new DataSyncModel();
//
//				dm2.syncServiceData(tables, true);
//				
//			String sqlrwlxString=" select * from T_YDZF_RWXX where rwbh='"+RWBH+"'";
//			
//			
//			ArrayList<HashMap<String, Object>> arrayList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlrwlxString);
//			
//			if (arrayList.size()!=0) {
//				String rwlx2 = arrayList.get(0).get("rwlx").toString();
//				
//				String sql = "select name from t_ydzf_rwlx  where code='"
//						+ rwlx2 + "'";
//				ArrayList<HashMap<String, Object>> data_name = SqliteUtil
//						.getInstance().queryBySqlReturnArrayListHashMap(sql);
//				RWDetail.put("rwlx",data_name.get(0).get("name").toString());
//			}
				
			}
			data.add(RWDetail);
			break;
		case TASK_ATTACHMENT:
			data = new ArrayList<HashMap<String, Object>>();
   ///  附件信息 有BUG
//			// 从本地数据库中读取任务相关的附件信息
//			String sql_attach = "select * from T_Attachment where fk_id like'%"
//					+ RWBH + "%'";
//			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
//					sql_attach);
			// 修改读取任务相关的附件信息，调用后台接口，从后台数据库读取，再同步到本地数据库中 zhaorq2014.2.18
//			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
//
//			HashMap<String, Object> param1 = new HashMap<String, Object>();
//			// param1.put("token", "");
//			// 参数
//			param1.put("BizType", "10");
//			param1.put("BizCode", RWBH);
//			//TODO 获取 当前任务id 调用获取附件的参数
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
//					// 根据附件的guid判断本地数据是否存在该在该附件记录，若存在update,若不存在insert
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
			//获取任务所有附件
			// 修改读取任务相关的附件信息，调用后台接口，从后台数据库读取，再同步到本地数据库中 zhaorq2014.2.18
			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> param1 = new HashMap<String, Object>();
			// param1.put("token", "");
			// 参数
			param1.put("BizType", "10");
			param1.put("BizCode", RWBH);
			param1.put("BizId", letaskid);
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
			
			// 获取附件接口调试通
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
					//设置全部图标一样
					hashmap2.put("FilePath", list.get(i).get("FilePath")
							.toString());
					hashmap2.put("FK_Unit", 1);
					data.add(hashmap2);
					// // 根据附件的guid判断本地数据是否存在该在该附件记录，若存在update,若不存在insert
					String rwguid = newGuid[0];
					ContentValues updateValues = new ContentValues();
					updateValues.put("FileName", list.get(i).get("FileName")
							.toString());
					updateValues.put("FilePath", list.get(i).get("FilePath")
							.toString());
					updateValues.put("Extension", list.get(i).get("Extension")
							.toString());
					//修改
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
						// TODO测试下载附件
						updateValues.put("guid", rwguid);
						SqliteUtil.getInstance().insert(updateValues,
								"T_Attachment");
					}
				}
		
			}
			
			
			break;
		case TASK_PROCESS:
			data = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("leTaskId", letaskid);
			param.put("regionCode", Global.getGlobalInstance().getAreaCode());
			params.add(param);
		

			String json = null;
			try {
				json = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, "QueryLeTaskTrackingList", params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
			} catch (IOException e) {

				e.printStackTrace();
			}
			if (json != null && !json.equals("")) {
				  ArrayList<FlowTrackingModel>  list2=new ArrayList<FlowTrackingModel>();
		          Type type = new TypeToken<ArrayList<FlowTrackingModel>>() {}.getType(); 
		          
		          list2=  new Gson().fromJson(json, type);
		          FlowTrackingModel flowTrackingModel = list2.get(0);
		          ArrayList<FlowTaskModel> list = flowTrackingModel.FlowTaskList;
				for (int i = 0; i < list.size(); i++) {
					//String num = list.get(i).get("NodeId").toString();
					// if(num.equals("1") || num.equals("2") || num.equals("3")
					// ){
					FlowTaskModel flowTaskModel = list.get(i);
					HashMap<String, Object> hashmap = new HashMap<String, Object>();
					hashmap.put("SecondaryAuditUserId",flowTaskModel.Assistant);
					hashmap.put("AuditName", flowTaskModel.Transactor.toString());
					hashmap.put("AuditTime", flowTaskModel.TransactedTime.toString());
					hashmap.put("NodeId", flowTaskModel.TransactorPosition.toString());
					if (flowTaskModel.Comment!=null) {
						hashmap.put("AuditResult",flowTaskModel.Comment.toString());
					}else{
						hashmap.put("AuditResult","");
					}
				
					hashmap.put("FlowDirection",1);
					hashmap.put("TaskAction", flowTaskModel.NodeName.toString());
					data.add(hashmap);
					// }
				}

			}
			break;
		case TASK_ADDCOMPANY:
			data = new ArrayList<HashMap<String, Object>>();

			if (RWBH == null) {
				RWBH = rwxx.getRWBH(GUID);
			}
			
			ArrayList<HashMap<String, Object>> params_ = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param_qylist = new HashMap<String, Object>();
			HashMap<String, Object> canshu = new HashMap<String, Object>();
			param_qylist.put("taskId", letaskid);
			param_qylist.put("pageSize", 100);
			param_qylist.put("pageIndex", 1);
			param_qylist.put("SurveyUnit", Global.getGlobalInstance().getAreaCode());
			String toJson = hashMapToJson(param_qylist);
			canshu.put("parms", toJson);
			params_.add(canshu);
			try {
			qylistjson = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, "GetSelectedExecuteEntlist", params_, Global
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
//						ContentValues contentValues=new ContentValues();
//						
//						contentValues.put("TaskId", RWBH);
//						contentValues.put("IsExcute", taskEntList.get(i)
//								.getStatus());
//					
//						contentValues.put("Guid", taskEntList.get(i)
//								.getEntCode());
//						contentValues.put("QYID", taskEntList.get(i)
//								.getEntCode());
//						
//						String updateTime = DisplayUitl.getServerTime();
//						contentValues.put("UpdateTime", updateTime);
//						
//						HashMap<String, Object> map=new HashMap<String, Object>();
//						
//						map.put("guid", taskEntList.get(i).getEntCode());
//						ArrayList<HashMap<String,Object>> list2 = SqliteUtil.getInstance().getList("TaskEnpriLink", map);
//						if (list2.size()>0) {
//							try {
//								SqliteUtil.getInstance().update("TaskEnpriLink", contentValues, "guid=?", new String[]{taskEntList.get(i).getEntCode()});
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else{
//							
//							SqliteUtil.getInstance().insert(contentValues,
//									"TaskEnpriLink");
//						}
						
						
						data.add(glqyArr);
					
					}
					
					
				}
			}
			
			
//			ArrayList<String> tables = new ArrayList<String>();
//			tables.add("TaskEnpriLink");// TaskEnpriLink表中状态发生 改变，需要同步一下
//			tables.add("T_YDZF_RWXX");
//			tables.add("T_Attachment");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("TaskEnpriLink");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("T_ZFWS_KCBL");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("T_ZFWS_XWBL");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("Survey_JSTZS");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("YDZF_SiteEnvironmentMonitorRecord");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			tables.add("ExeLawsTemplet");// T_YDZF_RWXX表中状态发生 改变，需要同步一下
//			// 同步一下着两张表
//			DataSyncModel dm2 = new DataSyncModel();
//
//			dm2.syncServiceData(tables, false);
			
//			String sql = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
//					+ " where TaskEnpriLink.TaskID='"
//					+ RWBH
//					+ "' order by TaskEnpriLink.UpdateTime";
//			ArrayList<HashMap<String, Object>> data_ = SqliteUtil.getInstance()
//					.queryBySqlReturnArrayListHashMap(sql);
//			//data.clear();
//				data.addAll(data_);
			
//			if (data != null && companyAdapter != null) {
//				companyAdapter.updateData(data_);
//			}
			
			
//			String sql1 = "select T_WRY_QYJBXX.guid as qyguid, qymc,isexcute,TaskEnpriLink.guid as link_guid from T_WRY_QYJBXX left join TaskEnpriLink on T_WRY_QYJBXX.guid=TaskEnpriLink.QYID"
//					+ " where TaskEnpriLink.TaskID='"
//					+ RWBH
//					+ "' order by TaskEnpriLink.UpdateTime";
//			data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
//					sql1);
			break;

		default:
			break;
		}
		return data;

	}

	private class ProcedureAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> list;
		LayoutInflater inflater;
		TextView textView1 = null;
		TextView textView2 = null;
		TextView textView3 = null;

		public ProcedureAdapter(ArrayList<HashMap<String, Object>> list,
				Context context) {
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
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.procedure_item, null);
			}
			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			textView2 = (TextView) convertView.findViewById(R.id.textView2);
			textView3 = (TextView) convertView.findViewById(R.id.textView3);

			String noteId = list.get(position).get("NodeId").toString();

			String auditName = list.get(position).get("AuditName").toString();
			String auditTime = list.get(position).get("AuditTime").toString();
			String auditResult = list.get(position).get("AuditResult")
					.toString();
			String flowDirection = list.get(position).get("FlowDirection")
					.toString();
			String taskAction = list.get(position).get("TaskAction").toString();

			if ("任务创建".equals(taskAction)) {
				textView1.setText("任务创建人:" + auditName);
				textView2.setText("任务创建时间:" + auditTime);
				textView3.setText("创建信息:" + auditResult);
			} else if ("任务下派".equals(taskAction)) {
				textView1.setText(noteId	+ ":" + auditName);
				textView2.setText("审批时间:" + auditTime);
				textView3.setText("审批意见:" + auditResult);
			} else if ("任务执行".equals(taskAction) && noteId.equals("4")) {
				textView1.setText("执行人:" + auditName);
				textView2.setText("执行时间:" + auditTime);
				textView3.setText("执行意见:" + auditResult);
			} else if ("任务审核".equals(taskAction)) {
				textView1.setText(noteId
						+ ":" + auditName);
				textView2.setText("审核时间:" + auditTime);
				textView3.setText("审核意见:" + auditResult);
			} else if ("任务反馈".equals(taskAction)) {
				textView1.setText(noteId
						+ ":" + auditName);
				textView2.setText("反馈时间:" + auditTime);
				textView3.setText("反馈意见:" + auditResult);
			}else if ("任务执行".equals(taskAction)) {
				textView1.setText("执行人:" + auditName);
				textView2.setText("执行时间:" + auditTime);
				textView3.setText("执行意见:" + auditResult);
			}
			else {
				textView1.setText(noteId
						+ ":" + auditName);
				textView2.setText("审核时间:" + auditTime);
				textView3.setText("审核意见:" + auditResult);
			}

			if (position > 0) {
				String SecondaryAuditUserId = list.get(position)
						.get("SecondaryAuditUserId").toString();
			//	String SecondaryAuditUserName = getNameByid(SecondaryAuditUserId);
				String SecondaryAuditUserName =SecondaryAuditUserId;
				if (!SecondaryAuditUserName.equals("")) {
				    if ("任务下派".equals(taskAction)) {
				    	String string = textView1.getText().toString() + "(抄送人:"
								+ SecondaryAuditUserName + ")";
						textView1.setText(string);
					}else{
						String string = textView1.getText().toString() + "(协办人:"
								+ SecondaryAuditUserName + ")";
						textView1.setText(string);
					}
				
				}
			}

			if (auditResult == null || auditResult.equals("")) {
				textView3.setVisibility(View.GONE);
			} else {
				textView3.setVisibility(View.VISIBLE);
			}

//			if (noteId.equals("0") && flowDirection.equals("1")
//					&& position != 0) {
//				textView1.setText("任务创建人:" + auditName);
//				textView2.setText("任务修改时间:" + auditTime);
//				textView3.setText("创建信息:" + auditResult);
//			}
//			if ( flowDirection.equals("1")
//					&& position != 0) {
//				textView1.setText("任务创建人:" + auditName);
//				textView2.setText("任务修改时间:" + auditTime);
//				textView3.setText("创建信息:" + auditResult);
//			}

			return convertView;
		}
	}

	public String transitToChinese(int nodeid) {
		position = null;
		switch (nodeid) {
		case 0:
			position = "办公室";
			break;
		case 1:
			position = "单位领导";
			break;
		case 2:
			position = "分管领导";
			break;
		case 3:
			position = "直接负责人";
			break;
		default:
			position = "执行人";
			break;
		}
		return position;
	}

	/**
	 * 附件信息
	 */
	private class AttachmentAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> AttachmentData;

		public AttachmentAdapter(
				ArrayList<HashMap<String, Object>> AttachmentData) {
			this.AttachmentData = AttachmentData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return AttachmentData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return AttachmentData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View
						.inflate(mContext, R.layout.qyname_list, null);
			}
		
			String fext = AttachmentData.get(position).get("Extension")
					.toString();
			String fname = AttachmentData.get(position).get("FileName")
					.toString()+fext;
			String file_form = AttachmentData.get(position).get("FK_Unit")
					.toString();
			TextView filename_text = (TextView) convertView
					.findViewById(R.id.qymc_text);
			ImageView iconImg = (ImageView) convertView.findViewById(R.id.img);
			// 判断该图片是上行时上传的附件还是下行是上传的附件，1任务下发，2任务执行，5任务反馈
			if (file_form.equals("1") || file_form.equals("2")) {
				iconImg.setImageResource(R.drawable.icon_down);
			} else if (file_form.equals("5")) {
				iconImg.setImageResource(R.drawable.icon_up);
			}else{
				iconImg.setImageResource(R.drawable.icon_down);
			}
			filename_text.setText(fname);
			filename_text.setTag(AttachmentData.get(position).get("Guid")
					.toString());
			Button zt_btn = (Button) convertView.findViewById(R.id.rwzt);
			zt_btn.setVisibility(View.GONE);
			return convertView;
		}
	}

	/**
	 * 执行企业列表
	 * 
	 * @author wangliugeng
	 * 
	 */
	private class CompanyAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> CompanyAdapterData;

		public CompanyAdapter(
				ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
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
			
			this.CompanyAdapterData = CompanyAdapterData1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return CompanyAdapterData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return CompanyAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData1) {
			
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
			this.CompanyAdapterData = CompanyAdapterData1;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View
						.inflate(mContext, R.layout.qyname_list, null);
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
			if (TaskFlowDirection.equals("1")) {
				zt_btn.setVisibility(View.GONE);
			}
			String qyzt = CompanyAdapterData.get(position).get("isexcute")
					.toString();
			zt_btn.setTag(qyzt);
			// BYK rwzt
			if (qyzt.equals("1")) {
				zt_btn.setText("待执行");
			} else if (qyzt.equals("3")) {
				zt_btn.setText("已上传");
			} else if (qyzt.equals("2")) {
				zt_btn.setText("执行中");
			} else {
				zt_btn.setText("状态错误");
			}
			//暂时屏蔽查询出来的任务关联的企业  能代替执法问题
			zt_btn.setVisibility(View.GONE);
			zt_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, QdjcnlActivity.class);
					// if("已上传".equals(zt_btn.getText().toString())){
					intent.putExtra("taskInfoFlag", "taskInfoFlag");
					// }

					intent.putExtra("qyid", qyGuid);
					intent.putExtra("rwbh", RWBH);
			//		intent.putExtra("isSearch", true);
					rwxx.setCurrentID(GUID);
					Bundle bundle = new Bundle();
					bundle.putSerializable("BusinessObj", rwxx);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					
//					Intent intent = new Intent(mContext, QdjcnlActivity.class);
//					// if("已上传".equals(zt_btn.getText().toString())){
//					intent.putExtra("taskInfoFlag", "taskInfoFlag");
//					// }
//
//					intent.putExtra("qyid", qyGuid);
//					intent.putExtra("rwbh", RWBH);
//					rwxx.setCurrentID(GUID);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("BusinessObj", rwxx);
//					intent.putExtras(bundle);
//					mContext.startActivity(intent);
							
				}
			});
			return convertView;
		}

	}

	/** 根据用户ID得到用户名称 **/
	private String getNameByid(String idStr) {
		if (!idStr.equals("")) {
			String[] ids = idStr.toString().split(",");
			StringBuffer idsStr = new StringBuffer();
			StringBuffer depnames = new StringBuffer();
			for (String nid : ids) {
				idsStr.append("'");
				idsStr.append(nid);
				idsStr.append("'");
				idsStr.append(",");
			}
			String nid = idsStr.toString().substring(0,
					idsStr.toString().lastIndexOf(","));
			;
			final ArrayList<HashMap<String, Object>> data = SqliteUtil
					.getInstance().queryBySqlReturnArrayListHashMap(
							"select U_RealName from  PC_Users where UserID in("
									+ nid + ")");
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					depnames.append(data.get(i).get("u_realname"));
					depnames.append(",");
				}
			}
			String SName = depnames.toString().substring(0,
					depnames.toString().lastIndexOf(","));
			return SName;
		} else {
			return "";
		}
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

}
