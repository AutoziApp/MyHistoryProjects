package com.mapuni.android.enforcement;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;
import com.mapuni.android.uitl.PanduanDayin;

public class SuperviseCardActivity extends BaseActivity {
	
	
	private EditText supervisec_bh_et,supervisec_sjdr_et,
					 supervisec_tkrq_et,supervisec_lxfs_et,supervisec_txdz_et,
					 supervisec_sjbm_et,supervisec_yzbm_et,supervisec_jdtsdh_et;
	
	private String supervisec_bh_et_str,supervisec_sjdr_et_str,supervisec_tkrq_et_str,
	supervisec_lxfs_et_str,supervisec_txdz_et_str,supervisec_sjbm_et_str,supervisec_yzbm_et_str,supervisec_jdtsdh_et_str;

	private SqliteUtil sqliteUtil;

	private RelativeLayout two_list_tool_layout;

	private LayoutInflater _LayoutInflater;

	private View taskRegisterView;

	private String qyid;

	private String rwbh;

	private String guid;

	private String id;

	private Bundle rWBundle;

	private RWXX rwxx;

	private ArrayList<HashMap<String, Object>> queryTaskData;

	private String qyzts;

	private String codeStr;

	private ProgressDialog progressDialog;

	private ContentValues contentValues;
	
	
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
			//	Toast.makeText(SuperviseCardActivity.this, "数据保存异常", Toast.LENGTH_SHORT).show();
					if (isSearch) {
						finish();
					}
				break;

			case 1:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
			//	Toast.makeText(SuperviseCardActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();

				finish();
				break;
				
			case  2:
				if (progressDialog != null) {
					progressDialog.cancel();
				}
				
				if (queryData != null && queryData.size() > 0) {
					/** 初始化界面绑定数据 */
					bind_data();
				}
				break;
			}
		}
	};
	
	private void bind_data() {
		supervisec_bh_et.setText(queryData.get(0).get("supervisenumber").toString());
		
		supervisec_sjdr_et.setText(queryData.get(0).get("supervisedpeople").toString());
		String makeouttime = queryData.get(0).get("makeouttime").toString();
		String[] split = makeouttime.split("T");
		supervisec_tkrq_et.setText(split[0]);
		supervisec_lxfs_et.setText(queryData.get(0).get("phone").toString());
		
		supervisec_txdz_et.setText(queryData.get(0).get("address").toString());
		
		supervisec_sjbm_et.setText(queryData.get(0).get("receivingdepartment").toString());
		
		supervisec_yzbm_et.setText(queryData.get(0).get("postcode").toString());
		
		supervisec_jdtsdh_et.setText(queryData.get(0).get("complainttel").toString());
	}

	private String billid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//	setContentView(R.layout.activity_supervisecard);
		setContentView(R.layout.ui_mapuni);
	
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "廉政监督 ");
		_LayoutInflater = LayoutInflater.from(this);

		taskRegisterView = _LayoutInflater.inflate(R.layout.activity_supervisecard,
				null);
		
			// 新加返回按钮 屏蔽同步按钮
	//	setBackButtonVisiable(true);
				// 设置出现同步抽屉
		setSynchronizeButtonVisiable(true);
		
		initView();
		initData();
		
	
		
	}


	private void initView() {
		
		isSearch = getIntent().getBooleanExtra("isSearch", false);
		sqliteUtil = SqliteUtil.getInstance();
		
	
		supervisec_bh_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_bh_et);
		supervisec_sjdr_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_sjdr_et);
		supervisec_tkrq_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_tkrq_et);
	//	supervisec_tkrq_et.setEnabled(false);
		supervisec_lxfs_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_lxfs_et);
		supervisec_txdz_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_txdz_et);
		supervisec_sjbm_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_sjbm_et);
		supervisec_yzbm_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_yzbm_et);
		supervisec_jdtsdh_et=(EditText) taskRegisterView.findViewById(R.id.supervisec_jdtsdh_et);
		// 展示出来控件
		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);
//	
		supervisec_tkrq_et.setOnClickListener(new TkrqListener());
	
	}
	
	
	private void getData() {
		
		supervisec_bh_et_str=supervisec_bh_et.getText().toString().trim();
		supervisec_sjdr_et_str=supervisec_sjdr_et.getText().toString().trim();
		supervisec_tkrq_et_str=supervisec_tkrq_et.getText().toString().trim();
		supervisec_lxfs_et_str=supervisec_lxfs_et.getText().toString().trim();
		supervisec_txdz_et_str=supervisec_txdz_et.getText().toString().trim();
		supervisec_sjbm_et_str=supervisec_sjbm_et.getText().toString().trim();
		supervisec_yzbm_et_str=supervisec_yzbm_et.getText().toString().trim();
		supervisec_jdtsdh_et_str=supervisec_jdtsdh_et.getText().toString().trim();

		
	}
	

	private void initData() {
		Intent intent = getIntent();
		qyid = intent.getStringExtra("qyid");
		rwbh = intent.getStringExtra("rwbh");
		
		guid = intent.getStringExtra("guid");
		id = intent.getStringExtra("id");
	//	billid = intent.getStringExtra("billid");
		
		 billid = GetExeLawsTempletItems2(rwbh,qyid);
		
		
		rWBundle = intent.getExtras();
		
		rwxx = (RWXX) rWBundle.get("BusinessObj");
		if (rwxx == null) {
			rwxx = new RWXX();
		}
		
		
		queryTaskData = new ArrayList<HashMap<String, Object>>();
		
		/** 根据任务编号和企业id查询出任务状态 */
		queryTaskData = sqliteUtil.queryBySqlReturnArrayListHashMap("select isexcute from TaskEnpriLink where taskid = '" + rwbh + "' and qyid = '" + qyid + "'");
	
		if (queryTaskData != null && queryTaskData.size() > 0) {
			qyzts = queryTaskData.get(0).get("isexcute").toString();
		}
		if (isSearch) {
			Toast.makeText(this, "当前企业只能查看!", Toast.LENGTH_LONG).show();
			/** 初始化页面不可编辑 */
			not_edit();
		}
		
		if (qyzts != null) {
					if (qyzts.equalsIgnoreCase("3")) {
					//String selectZWsql = "select zw from PC_Users where UserID = '" + Global.getGlobalInstance().getUserid() + "'";
					//String ZW = SqliteUtil.getInstance().getDepidByDepName(selectZWsql);
						if ("3".equals(qyzts)) {
							Toast.makeText(this, "当前企业调查取证已完成，不能对此项进行编辑", Toast.LENGTH_LONG).show();
							/** 初始化页面不可编辑 */
							not_edit();
					}
					if ("009".equals(new RWXX().queryTaskStatus(rwbh))) {
						Toast.makeText(this, "当前企业已归档，不能对此项进行编辑", Toast.LENGTH_LONG).show();
						/** 初始化页面不可编辑 */
						not_edit();
					}
				}
			}
		
		/** 根据企业代码和类型判断界面的初始化查询 */
		if (qyid != null && !qyid.equals("") && rwbh != null && !rwbh.equals("")) {
			/** 根据任务状态和企业代码和勘察笔录类型判断界面的初始化查询 */
			querySteData();
		}


	}
	
//TODO  展示数据查询出来数据
	private ArrayList<HashMap<String, Object>> queryData  = new ArrayList<HashMap<String, Object>>();

	private String task_type_edt_str;

	private boolean isSearch;;
	private void querySteData() {
		progressDialog = new ProgressDialog(SuperviseCardActivity.this);
		progressDialog.setMessage("正在努力加载中...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		new Thread(){
			

			public void run() {
				String sql = "select * from Supervision where billcode = '" + billid +"'";
					//查询获取 数据
				queryData = sqliteUtil.queryBySqlReturnArrayListHashMap(sql);
				
				Message msg = handler.obtainMessage();
				msg.arg1 = 2;
				handler.sendMessage(msg);
			};
		}.start();
	
		
	}


	//设置页面不可编辑
	private void not_edit() {
		
		supervisec_bh_et.setEnabled(false);
		supervisec_sjdr_et.setEnabled(false);
		supervisec_tkrq_et.setEnabled(false);
		supervisec_lxfs_et.setEnabled(false);
		supervisec_txdz_et.setEnabled(false);
		supervisec_sjbm_et.setEnabled(false);
		supervisec_yzbm_et.setEnabled(false);
		supervisec_jdtsdh_et.setEnabled(false);
	}
	
	//完成
	public void complay(View v){
			if (!bNeedSave()) {
	
				}
		progressDialog = new ProgressDialog(SuperviseCardActivity.this);
		progressDialog.setMessage("正在努力加载数据,请您稍后...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		new Thread(){
//			
			public void run() {
				//获取数据
				getData();
				//插入数据库 数据
				content_LZ_Valput();
				if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
				if (!TextUtils.isEmpty(guid)) {
					
					try {
						 int count = sqliteUtil.update("Supervision", contentValues, "BillCode=?", new String[] { guid });
//						String sql="UPDATE Supervision SET SupervisedNumber = "+supervisec_bh_et_str+", SupervisedPeople = "+supervisec_sjdr_et_str+", MakeOutTime = "+supervisec_lxfs_et_str+"" +
//								", Adress="+supervisec_txdz_et_str+", ReceivingDepartment="+supervisec_sjbm_et_str+",PostCode="+
//								supervisec_yzbm_et_str+",ComplaintTel="+supervisec_jdtsdh_et_str+",	CreateBy="+
//								0+",CreateTime="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+",UpdateBy="+
//								0+",UpdateTime="+
//								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" WHERE BillCode='"+guid+"'";
//					
					//	sqliteUtil.execute(sql);
						if (count == 0) {
							Message msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
						}else if (count==1|| count > 1) {
							updateIntoExelawsTemplet(guid);
							
							Message msg = handler.obtainMessage();
							msg.arg1 = 1;
							handler.sendMessage(msg);
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					/** 如果是保存数据则新生成一个GuId */
					String guid = UUID.randomUUID().toString();
					contentValues.put("guid", guid);
					
					long count = sqliteUtil.insert(contentValues, "Supervision");
					
					if (count == 0) {
						Message msg = handler.obtainMessage();
						msg.arg1 = 0;
						handler.sendMessage(msg);
					}else if (count==1|| count > 1) {
						try {
							updateIntoExelawsTemplet(guid);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Message msg = handler.obtainMessage();
						msg.arg1 = 1;
						handler.sendMessage(msg);
						
					}
				}
				
				}else {
					Message msg = handler.obtainMessage();
					msg.arg1 = 0;
					handler.sendMessage(msg);
					
				}
				
				
			
				
			};
		}.start();
		
   
		
		
	}
	
	/**
	 * 打印预览
	 * */
	public void preview_click_supervisec(View view){
		
		task_type_edt_str = getType_edt_str();

		getData();
		
		content_LZ_Valput();
		
		
		new Thread(){
			
			public void run() {
				
				if (rwbh != null && !rwbh.equals("") && qyid != null && !qyid.equals("")) {
					if (!TextUtils.isEmpty(guid)) {
						
						try {
							 int count = sqliteUtil.update("Supervision", contentValues, "BillCode=?", new String[] { guid });
//							String sql="UPDATE Supervision SET SupervisedNumber = "+supervisec_bh_et_str+", SupervisedPeople = "+supervisec_sjdr_et_str+", MakeOutTime = "+supervisec_lxfs_et_str+"" +
//									", Adress="+supervisec_txdz_et_str+", ReceivingDepartment="+supervisec_sjbm_et_str+",PostCode="+
//									supervisec_yzbm_et_str+",ComplaintTel="+supervisec_jdtsdh_et_str+",	CreateBy="+
//									0+",CreateTime="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+",UpdateBy="+
//									0+",UpdateTime="+
//									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" WHERE BillCode='"+guid+"'";
//						
						//	sqliteUtil.execute(sql);
							if (count == 0) {
								Message msg = handler.obtainMessage();
								msg.arg1 = 0;
								handler.sendMessage(msg);
							}else if (count==1|| count > 1) {
								updateIntoExelawsTemplet(guid);
								
								Message msg = handler.obtainMessage();
								msg.arg1 = 1;
								handler.sendMessage(msg);
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						/** 如果是保存数据则新生成一个GuId */
						String guid = UUID.randomUUID().toString();
						contentValues.put("guid", guid);
						
						long count = sqliteUtil.insert(contentValues, "Supervision");
						
						if (count == 0) {
							Message msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
						}else if (count==1|| count > 1) {
							try {
								updateIntoExelawsTemplet(guid);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							Message msg = handler.obtainMessage();
							msg.arg1 = 1;
							handler.sendMessage(msg);
							
						}
					}
					
					}
				
			};
		}.start();
		
		//	  String supervisec_bh_et_str,supervisec_sjdr_et_str,supervisec_tkrq_et_str,
	//	 supervisec_lxfs_et_str,supervisec_txdz_et_str,supervisec_sjbm_et_str,supervisec_yzbm_et_str,supervisec_jdtsdh_et_str;

		PreviewWrit_lzjd plnkc = new PreviewWrit_lzjd(rwbh, qyid,billid, task_type_edt_str, SuperviseCardActivity.this);
		plnkc.setVaues(supervisec_bh_et_str, supervisec_sjdr_et_str, supervisec_tkrq_et_str, supervisec_lxfs_et_str, supervisec_txdz_et_str, supervisec_sjbm_et_str, supervisec_yzbm_et_str, supervisec_jdtsdh_et_str);
		String yon = plnkc.create();
		if (yon.equals("y")) {
			File file1 = new File(plnkc.getPath() + "/第1页.html");
			if (!PanduanDayin.appIsInstalled(SuperviseCardActivity.this, "com.dynamixsoftware.printershare")) {
				PanduanDayin.insatll(SuperviseCardActivity.this);
				return;
			}
			PanduanDayin.startprintshare(SuperviseCardActivity.this, file1.getAbsolutePath());
		} else {
			Toast.makeText(SuperviseCardActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	private String getType_edt_str() {
		
		/** 获得文书的数据集合 */
		ArrayList<HashMap<String, Object>> clericalData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select code,name from T_YDZF_PublicCode");
		for (int i = 0; i < clericalData.size(); i++) {
			String title = clericalData.get(i).get("name").toString();

			if ("廉政监督卡".equals(title)) {
				task_type_edt_str = clericalData.get(i).get("code").toString();
				break;
			} 
		}
		
		return task_type_edt_str;
	}


	private void updateIntoExelawsTemplet(String guid) throws Exception {
		
		
		if (TextUtils.isEmpty(id)) {
			return;
		}
		
		ContentValues contentValues2 = new ContentValues();
		
		contentValues2.put("taskid", rwbh);// 任务ID
		contentValues2.put("enterid", qyid);// 污染源(企业)ID
		
		contentValues2.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
	
		contentValues2.put("billid", guid);
		
		contentValues2.put("templetname","廉政监督卡"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues2.put("templettype", "LZJDK");
		
		sqliteUtil.update("ExeLawsTemplet",contentValues2,"id = ?",new String[]{id});
	
	}
	//向廉政表里插入数据
	private void content_LZ_Valput() {
		
		contentValues = new ContentValues();
		//编号
		contentValues.put("SuperviseNumber", supervisec_bh_et_str);
		//受监督人
		contentValues.put("SupervisedPeople", supervisec_sjdr_et_str);
		//填卡日期
		contentValues.put("MakeOutTime", supervisec_tkrq_et_str);
		//联系方式
		contentValues.put("Phone", supervisec_lxfs_et_str);
		//通信地址
		contentValues.put("Address", supervisec_txdz_et_str);
		//收件部门
		contentValues.put("ReceivingDepartment", supervisec_sjbm_et_str);
		//邮政编码
		contentValues.put("PostCode", supervisec_yzbm_et_str);
		//监督部门电话
		contentValues.put("ComplaintTel", supervisec_jdtsdh_et_str);
		contentValues.put("CreatedBy", 0);
		contentValues.put("CreatedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues.put("UpdatedBy", 0);
		contentValues.put("UpdatedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		
	}


	/** 判断当前页面的数据是否需要保存 */
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
		
	/** 开业时间监听事件 **/
	private class TkrqListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(SuperviseCardActivity.this,
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
							supervisec_tkrq_et.setText(year + "-" + monthStr
									+ "-" + dayOfMonth);
						}

					}, year1, month1, day1).show();
		}
	}

	private String GetExeLawsTempletItems2(String Taskid, String qyid) {
		ArrayList<HashMap<String, Object>> _ArrayList;

		_ArrayList = SqliteUtil.getInstance().getList(
				"ExeLawsTemplet where TaskID='" + Taskid + "'and EnterId='"
						+ qyid + /**/"'and templettype = 'LZJDK'");
		JSONArray _JsonArray = new JSONArray();
		String billid = "";
		for (int i = 0; i < _ArrayList.size(); i++) {
			HashMap<String, Object> _HashMapTemp = _ArrayList.get(i);
			Set _Iterator = _HashMapTemp.entrySet();

			JSONObject _JsonObjectData = new JSONObject();
			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();
				String _Key = entry.getKey().toString();
				Object _Object = _HashMapTemp.get(_Key);
				if (_Key.equals("billid") || _Key.equals("BillId")
						|| _Key.equals("Billid") || _Key.equals("billId")) {
					billid = entry.getValue().toString();
				}
			}
		}
		return billid;
	}
}
