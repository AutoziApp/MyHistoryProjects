package com.mapuni.android.onlinemonitor;

import java.io.File;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.sparta.Text;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * Description:在线监测
 * 
 * @author Shangb
 * @Version 1.0.0
 * @Copyright 中科宇图天下科技有限公司 Create at: 2014-4-30 17:00:11
 */
public class ZxjcActivity extends BaseActivity {
	ListView listView;
	EditText editText;
	private static String methodName = "GetOnlineMonitorEntList";
	private static String spName = "zxjcupdatetime";
	private LinearLayout topLayout;
	private RelativeLayout title;
	private SharedPreferences preferences;
	/**是否正在查询,若正在查询,按下返回键时,展示所有企业*/
	private boolean isQuerying = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);

		RelativeLayout relayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		SetBaseStyle(relayout, "在线监测");
		sqliteUtil = SqliteUtil.getInstance();
		preferences = getBaseContext().getSharedPreferences(spName, getBaseContext().MODE_WORLD_WRITEABLE);
		setTitleLayoutVisiable(true);
		topLayout = (LinearLayout) findViewById(R.id.topLayout);
		title = (RelativeLayout) topLayout.findViewById(156189220);
		ImageView sycview = (ImageView) title.findViewById(156189147);
		ImageView queryView = (ImageView) title.findViewById(156189146);
		queryView.setVisibility(View.VISIBLE);
		//同步按钮
		sycview.setImageResource(R.drawable.zxjc_syc);
		sycview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ZxjcActivity.this,"show a dialog here", Toast.LENGTH_SHORT).show();
				String updateTime = preferences.getString("recentUpdates","暂无更新记录!");
				new AlertDialog.Builder(ZxjcActivity.this).setCancelable(true).setTitle("更新在线监测企业列表")
				.setMessage("上次更新:"+updateTime).setPositiveButton("更新",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// update table baseInfo
						if (Net.checkNet(ZxjcActivity.this)) {
							updateZXQYList();							
						}else {
							Toast.makeText(ZxjcActivity.this,"网络不通,请稍后重试!",Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					}
				}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// dismiss
						dialog.dismiss();
					}
				}).show();
			}
		});
		queryView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout relativeLayout = new RelativeLayout(ZxjcActivity.this);
				relativeLayout.setBackgroundColor(Color.WHITE);
				relativeLayout.setLayoutParams(params);
				final EditText editText = new EditText(ZxjcActivity.this);
				editText.setHint("关键字");
				editText.setTextSize(22);
				editText.setId(511314520);
				editText.setLayoutParams(params);
				relativeLayout.addView(editText);
				new AlertDialog.Builder(ZxjcActivity.this).setCancelable(true)
				.setTitle("请输入企业名称中的关键字").setView(relativeLayout).setPositiveButton("查询",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String keyString = editText.getText().toString().trim();
						if (TextUtils.isEmpty(keyString)) {
							reload("");
							isQuerying = false;
						}else {
							reload(keyString);
						}
						isQuerying = true;
					}
				}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isQuerying = false;
						dialog.dismiss();
					}
				}).show();
			}
		});
		LayoutInflater inflater = LayoutInflater.from(ZxjcActivity.this);
		View view = inflater.inflate(R.layout.activity_zxjc, null);
		listView = (ListView) view.findViewById(R.id.zxjc_listView);
		editText = (EditText) view.findViewById(R.id.ZXJC_Enterprise_query);
		LinearLayout ll = (LinearLayout) findViewById(R.id.middleLayout);
		ll.addView(view);
		reload("");

		listView.setEmptyView(findViewById(R.id.emptyTipView));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ZxjcActivity.this, ZxjcDetailActivity.class);
				Object object = parent.getAdapter().getItem(position);
				intent.putExtra("qyid", ((HashMap<String, Object>) object).get("pscode").toString());
				intent.putExtra("qymc", ((HashMap<String, Object>) object).get("psname").toString());
				// intent.putExtra("isww", ((HashMap<String, Object>)
				// object).get("isww").toString());
				// intent.putExtra("isgas", ((HashMap<String, Object>)
				// object).get("isgas").toString());

				startActivity(intent);
			}
		});
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loading.setLoadMsg("请求企业列表......","");
				break;
			case 1:
				loading.setLoadMsg("更新数据库......","");
				break;
			case 2:
				loading.setLoadMsg("请求排污口信息......","");
				break;
			case 3:
				loading.setLoadMsg("请求监测因子信息......","");
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
		
	};
	private YutuLoading loading = null;
	private SqliteUtil sqliteUtil;
	/**更新在线监测企业表*/
	private void updateZXQYList() {
		new AsyncTask<Void, Void,Integer>() {

			protected void onPreExecute() {
				loading = new YutuLoading(ZxjcActivity.this);
				loading.setLoadMsg("加载中,请稍后...", "");
				loading.setCancelable(false);
				loading.showDialog();
			};

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					mHandler.sendEmptyMessage(0);
					String result = (String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, null, Global.getGlobalInstance()
							.getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
					if (TextUtils.isEmpty(result)) {
						return 0;
					}
					mHandler.sendEmptyMessage(1);
					JSONArray jsonArray = new JSONArray(result);
					int leng = jsonArray.length();
					if (null == jsonArray || leng<=0) {
						return 0;
					}
					//TODO 事务优化
					sqliteUtil.deleteAllTableDataByTables("BaseInfo");
					JSONObject jsonObject;
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for(int i = 0;i<leng;i++){
						jsonObject = jsonArray.getJSONObject(i);
						String PSCode = jsonObject.getLong("PSCode")+"";
						String PSName = jsonObject.getString("PSName");
						String PSNumber = jsonObject.getString("PSNumber");
						String RegionName = jsonObject.getString("RegionName");
						String RegistTypeName = jsonObject.getString("RegistTypeName");
						String UnitTypeName = jsonObject.getString("UnitTypeName");
						String PSScaleName = jsonObject.getString("PSScaleName");
						String SubjectionRelationName = jsonObject.getString("SubjectionRelationName");
						String IndustryTypeName = jsonObject.getString("IndustryTypeName");
						String ValleyName = jsonObject.getString("ValleyName");
						String AttentionDegreeName = jsonObject.getString("AttentionDegreeName");
						String PSAddress = jsonObject.getString("PSAddress");
						String Longitude = jsonObject.get("Longitude").toString()+"";
						String Latitude = jsonObject.get("Latitude").toString()+"";
						String PSEnvironmentDept = jsonObject.getString("PSEnvironmentDept");
						String EnvironmentPrincipal = jsonObject.getString("EnvironmentPrincipal");
						String EnvironmentMans = jsonObject.getString("EnvironmentMans");
						String CorporationCode = jsonObject.getString("CorporationCode");
						String CorporationName = jsonObject.getString("CorporationName");
						String RunDate = jsonObject.getString("RunDate");
						String OpenAcocuntBank = jsonObject.getString("OpenAcocuntBank");
						String BankAccount = jsonObject.getString("BankAccount");
						String PSWebsite = jsonObject.getString("PSWebsite");
						String OfficePhone = jsonObject.getString("OfficePhone");
						String Fax = jsonObject.getString("Fax");
						String MobilePhone = jsonObject.getString("MobilePhone");
						String Email = jsonObject.getString("Email");
						String PostalCode = jsonObject.getString("PostalCode");
						String CommunicateAddr = jsonObject.getString("CommunicateAddr");
						String AreaName = jsonObject.getString("AreaName");
						String PSClassName = jsonObject.getString("PSClassName");
						String Linkman = jsonObject.getString("Linkman");
						String Comment = jsonObject.getString("Comment");
						String DisposeTypeCode = jsonObject.get("DisposeTypeCode").toString()+"";
						String IfThirtyTenthousandkilowat = jsonObject.getString("IfThirtyTenthousandkilowat");
						String TotalArea = jsonObject.getString("TotalArea");
						String IsMonitor = jsonObject.getString("IsMonitor");
						String StateControlledTypeName = jsonObject.getString("StateControlledTypeName");
						String PsAlias = jsonObject.getString("PsAlias");
						String UpdateTime = jsonObject.getString("UpdateTime");
						//插入数据库
						ContentValues values = new ContentValues();
						values.put("PSCode", PSCode);
						values.put("PSName", PSName);
						values.put("PSNumber", PSNumber);
						values.put("RegionName", RegionName);
						values.put("RegistTypeName", RegistTypeName);
						values.put("UnitTypeName", UnitTypeName);
						values.put("PSScaleName", PSScaleName);
						values.put("SubjectionRelationName", SubjectionRelationName);
						values.put("IndustryTypeName", IndustryTypeName);
						values.put("ValleyName", ValleyName);
						values.put("AttentionDegreeName", AttentionDegreeName);
						values.put("PSAddress", PSAddress);
						values.put("Longitude", Longitude);
						values.put("Latitude", Latitude);
						values.put("PSEnvironmentDept", PSEnvironmentDept);
						values.put("EnvironmentPrincipal", EnvironmentPrincipal);
						values.put("EnvironmentMans", EnvironmentMans);
						values.put("CorporationCode", CorporationCode);
						values.put("CorporationName", CorporationName);
						values.put("RunDate", RunDate);
						values.put("OpenAcocuntBank", OpenAcocuntBank);
						values.put("BankAccount", BankAccount);
						values.put("PSWebsite", PSWebsite);
						values.put("OfficePhone", OfficePhone);
						values.put("Fax", Fax);
						values.put("MobilePhone", MobilePhone);
						values.put("Email", Email);
						values.put("PostalCode", PostalCode);
						values.put("CommunicateAddr", CommunicateAddr);
						values.put("AreaName", AreaName);
						values.put("PSClassName", PSClassName);
						values.put("Linkman", Linkman);
						values.put("Comment", Comment);
						values.put("DisposeTypeCode", DisposeTypeCode);
						values.put("IfThirtyTenthousandkilowat", IfThirtyTenthousandkilowat);
						values.put("TotalArea", TotalArea);
						values.put("IsMonitor", IsMonitor);
						values.put("StateControlledTypeName", StateControlledTypeName);
						values.put("PsAlias", PsAlias);
						values.put("UpdateTime",format.format(new Date()));
						sqliteUtil.insert(values, "BaseInfo");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
				return 1;
			}

			protected void onPostExecute(Integer i) {
				if (loading != null) {
					loading.dismissDialog();
				}
				if (-1 == i) {//错误
					Toast.makeText(ZxjcActivity.this,"同步时发生错误,请稍后重试!",Toast.LENGTH_SHORT).show();
				}else if (0 == i) {//无数据
					Toast.makeText(ZxjcActivity.this,"没有请求到数据!",Toast.LENGTH_SHORT).show();
				}else if (1 == i) {//正常
					Toast.makeText(ZxjcActivity.this,"同步完成!",Toast.LENGTH_SHORT).show();
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("recentUpdates",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					editor.commit();
				}else {
					
				}
				//刷新界面
				reload("");
			};

		}.execute();
		
	}
	public void query(View view) {
		reload(editText.getText().toString());
	}

	private void reload(String key) {

		new AsyncTask<String, Void, ArrayList<HashMap<String, Object>>>() {

			@Override
			protected ArrayList<HashMap<String, Object>> doInBackground(String... params) {
				String sql = "select PSName,PSCode from BaseInfo  where 1=1 and PSName like '%" + params[0] + "%'";
				ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
				return result;
			}

			@Override
			protected void onPostExecute(final ArrayList<HashMap<String, Object>> result) {
				super.onPostExecute(result);
				Comparator<HashMap<String, Object>> com = new Comparator<HashMap<String, Object>>() {

					@Override
					public int compare(HashMap<String, Object> lhs, HashMap<String, Object> rhs) {

						String n1 = (String) lhs.get("psname");
						String n2 = (String) rhs.get("psname");
						String[] arr = new String[] { n1, n2 };
						Collator com0 = Collator.getInstance(Locale.CHINA);
						return com0.compare(n1, n2);
					}
				};
				Collections.sort(result, com);

				StringBuffer sb = new StringBuffer();
				for (HashMap<String, Object> hashMap : result) {
					sb.append("\"" + hashMap.get("psname") + "\",");
				}
				String[] from = new String[] { "psname" };
				int[] to = new int[] { R.id.entName };
				listView.setAdapter(new SimpleAdapter(ZxjcActivity.this, result, R.layout.zxjc_layout, from, to));
			}
		}.execute(key);

	}
	@Override
	public void onBackPressed() {
		if (isQuerying) {
			reload("");
			isQuerying = false;
		}else {
			super.onBackPressed();			
		}
	}
}
