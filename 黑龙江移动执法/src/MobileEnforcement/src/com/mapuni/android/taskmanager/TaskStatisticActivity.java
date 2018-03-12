package com.mapuni.android.taskmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.WebServiceProvider;
	
/**  任务统计
 * FileName: TaskStatisticActivity.java 
 * Description:任务统计
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 上午11:57:54
 */
public class TaskStatisticActivity extends BaseActivity {
	// 分别为装载柱状图的布局，选择显示方式的标签布局，装填listview的线性布局
	private LinearLayout lv, rwtj_middlelayout, parent_listview;
	private ProgressDialog pd;
	private ImageView search;
	private Spinner timeSp, categorySp;
	private TextView rwtj_rwlb;
	private Button rwtj_lb, rwtj_tx;
	private ListView listview;

	private ArrayAdapter<String> aa, bb;
	// private Calendar dateAndTime = Calendar.getInstance();
	private String startTime = null, typeid = null;
	private SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
	// private String endTime = null;

	private  final int HAS_EXCEPTION = 0;
	private  final int SUCCESSFUL = 1;
	private  final String NAME_SPACE = "http://tempuri.org/";
	private  final String NODE_FATHER = "item";// 父节点
	
	private  final String TAG = "TaskStatisticActivity";

	/** 筛选选择项 */
	private  final String[] timeStrs = new String[] { 
		"最近三天", "最近一周", "最近一个月", "最近一季度" };
	private  final String[] categoryStrs = new String[] { 
		"任务来源", "任务类型", "所属科室" };
		
	/** 消息处理 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				
				if (msg.obj != null) {
					@SuppressWarnings("unchecked")
					ArrayList<HashMap<String, Object>> mlist = (ArrayList<HashMap<String, Object>>) msg.obj;
					if (mlist.size() == 0) {
						rwtj_middlelayout.setVisibility(View.GONE);
						parent_listview.setVisibility(View.GONE);
						lv.setVisibility(View.GONE);
						Toast.makeText(TaskStatisticActivity.this,
								"该查询条件下无任务数据！", Toast.LENGTH_LONG).show();
					} else {
						init();
						/*
						 * rwtj_middlelayout.setVisibility(View.VISIBLE);
						 * if(listview.getVisibility()==View.GONE
						 * &&lv.getVisibility()==View.GONE){
						 * listview.setVisibility(View.VISIBLE); }
						 */
						listview.setAdapter(new LAdapter(mlist));

               lv.removeAllViews();
					/*	GraphicalChartView chartView = new ProjectStatusChart()
								.getGraphicalCylinderView(TaskStatisticActivity.this,
										mlist, timeSp.getSelectedItem()
												.toString(), categorySp
												.getSelectedItem().toString(),
												R.color.base_seagreen,R.color.base_zxjc_qx_bg,R.color.base_zxjc_qx_bg);
						lv.addView(chartView);*/
					}
					/* lv.setAdapter(new LAdapter(mlist)); */
				} else {
					rwtj_middlelayout.setVisibility(View.GONE);
					listview.setVisibility(View.GONE);
					lv.setVisibility(View.GONE);
					Toast.makeText(TaskStatisticActivity.this, "服务器无返回数据！",
							Toast.LENGTH_LONG).show();
				}
				pd.cancel();
				break;
			}
		}

		/**
		 * Description:初始化视图的显示和隐藏
		 * @author Administrator<br>
		 * Create at: 2012-12-4 下午01:45:52
		 */
		private void init() {
			rwtj_middlelayout.setVisibility(View.VISIBLE);
			parent_listview.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			rwtj_lb.setTextColor(Color.BLUE);
			rwtj_tx.setTextColor(Color.BLACK);
			rwtj_lb.setBackgroundResource(R.drawable.list_active);
			rwtj_tx.setBackgroundResource(R.drawable.chart_normal);
			rwtj_rwlb.setText(categorySp.getSelectedItem().toString());
		}

		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.statistictask);
		RelativeLayout relayout = (RelativeLayout) this
				.findViewById(R.id.rwtj_head);
		SetBaseStyle(relayout, "任务统计");
		lv = (LinearLayout) findViewById(R.id.parent_graphicalView);

		categorySp = (Spinner) findViewById(R.id.tj_choosecategory_spinner);
		timeSp = (Spinner) findViewById(R.id.tj_choosetime_spinner);

		search = (ImageView) findViewById(R.id.search_dp_imageView);
		rwtj_lb = (Button) findViewById(R.id.rwtj_lb);
		rwtj_tx = (Button) findViewById(R.id.rwtj_tx);
		listview = (ListView) findViewById(R.id.rwtj_listview);
		rwtj_middlelayout = (LinearLayout) findViewById(R.id.rwtj_middlelayout);
		parent_listview = (LinearLayout) findViewById(R.id.parent_listview);
		rwtj_rwlb = (TextView) findViewById(R.id.rwtj_rwlb);

		rwtj_lb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rwtj_lb.setBackgroundResource(R.drawable.list_active);
				rwtj_tx.setBackgroundResource(R.drawable.chart_normal);
				rwtj_lb.setTextColor(Color.BLUE);
				rwtj_tx.setTextColor(Color.BLACK);
				lv.setVisibility(View.GONE);
				parent_listview.setVisibility(View.VISIBLE);
			}
		});
		rwtj_tx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rwtj_lb.setBackgroundResource(R.drawable.list_normal);
				rwtj_tx.setBackgroundResource(R.drawable.chart_active);
				rwtj_tx.setTextColor(Color.BLUE);
				rwtj_lb.setTextColor(Color.BLACK);
				parent_listview.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);

			}
		});

		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, timeStrs);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		bb = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categoryStrs);
		bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		categorySp.setAdapter(bb);
		timeSp.setAdapter(aa);
		timeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view;
				tv.setGravity(Gravity.CENTER);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				/*
				 * if(timeSp.getSelectedItem().toString().equals("-请选择日期-")){
				 * return; }else
				 */

				String category = categorySp.getSelectedItem().toString();
				int len = categoryStrs.length;
				int i;
				// 获得任务类别的ID 任务ID为数组脚标加1
				for (i = 0; i < len; i++) {
					if (categoryStrs[i].equals(category)) {
						typeid = String.valueOf(i + 1);
					}
				}
				if (timeSp.getSelectedItem().toString().equals("最近三天")) {
					now.add(Calendar.DAY_OF_YEAR, -3);
					startTime = fmtDate.format(now.getTime());
					downLoad();
				} else if (timeSp.getSelectedItem().toString().equals("最近一周")) {
					now.add(Calendar.DAY_OF_YEAR, -7);
					startTime = fmtDate.format(now.getTime());
					downLoad();
				} else if (timeSp.getSelectedItem().toString().equals("最近一个月")) {
					now.add(Calendar.MONTH, -1);
					startTime = fmtDate.format(now.getTime());
					downLoad();
				} else if (timeSp.getSelectedItem().toString().equals("最近一季度")) {
					now.add(Calendar.MONTH, -3);
					startTime = fmtDate.format(now.getTime());
					downLoad();
				}
			}
		});

	}

	private void downLoad() {
		HashMap<String, Object> timeCondition = new HashMap<String, Object>();
		timeCondition.put("_startTime", startTime);
		HashMap<String, Object> categoryCondition = new HashMap<String, Object>();
		categoryCondition.put("_typeid", typeid);
		final ArrayList<HashMap<String, Object>> conditions = new ArrayList<HashMap<String, Object>>();
		conditions.add(timeCondition);
		conditions.add(categoryCondition);

		pd = new ProgressDialog(TaskStatisticActivity.this);
		pd.setMessage("正在下载数据...");
		pd.setTitle("下载");
		pd.show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<HashMap<String, Object>> result = LoadData(
						conditions,
						NAME_SPACE,
						Global.getGlobalInstance().getSystemurl()
								+ "/MobileEnforcement/WebService/MobileEnforcementWebService.asmx",
						"synchronizeRWStatDataForClient",
						WebServiceProvider.RETURN_STRING, true, NODE_FATHER);
				Message msg = handler.obtainMessage();
				msg.obj = result;
				msg.arg1 = SUCCESSFUL;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * Description:加载数据
	 * @param conditions
	 * @param NameSpace
	 * @param url
	 * @param methordName
	 * @param reurnType
	 * @param isDotNet
	 * @param NodeFather
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午02:16:33
	 */
	synchronized public ArrayList<HashMap<String, Object>> LoadData(
			ArrayList<HashMap<String, Object>> conditions, String NameSpace,
			String url, String methordName, int reurnType, boolean isDotNet,
			String NodeFather) {
		String result;
		try {
			result = (String) WebServiceProvider.callWebService(NameSpace, methordName,
					conditions, url, reurnType, isDotNet);
			if (result == null) {
				return null;
			}
			FileHelper fh = new FileHelper();
			fh.convertBase64StringToLocalFile(result, Global.SDCARD_TEMP_PATH, "service.zip");
		} catch (IOException e1) {
			ExceptionManager.WriteCaughtEXP(e1, TAG);
			e1.printStackTrace();
			Log.v(TAG, "网络超时异常!");
			Message msg = handler.obtainMessage();
			msg.arg1 = HAS_EXCEPTION;
			handler.handleMessage(msg);
			return null;
		}
		ArrayList<File> files = new FileHelper().deZipFiles(
				Global.SDCARD_TEMP_PATH + "service.zip", Global.SDCARD_TEMP_PATH);
		FileInputStream fin;
		ArrayList<HashMap<String, Object>> data = null;
		try {
			fin = new FileInputStream(files.get(0));// 拿到xml文件流
			// 将xml临时文件中数据取出并封装为ArrayList<HashMap<String, Object>>
			data = XmlHelper.getDataFromXmlStream(fin, NodeFather);
			fin.close();
		} catch (FileNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "ZXJKOverRuleActivity");
			e.printStackTrace();
			Log.v(TAG, "找不到临时数据文件文件service.txt");
			return null;
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "ZXJKOverRuleActivity");
			Log.i(TAG, "网络异常，请检查URL设置是否正确！");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * FileName: TaskStatisticActivity.java
	 * Description:列表数据适配器
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 * Create at: 2012-12-4 下午02:16:08
	 */
	private final class LAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data = null;

		public LAdapter(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(TaskStatisticActivity.this);
			TextView departmenttv = new TextView(TaskStatisticActivity.this);
			TextView numtv = new TextView(TaskStatisticActivity.this);
			departmenttv.setTextSize(20);
			numtv.setTextSize(20);
			//departmenttv.setTextSize(12.0f); numtv.setTextSize(12.0f);
			departmenttv.setTextColor(Color.BLACK);
			numtv.setTextColor(Color.BLACK);
			departmenttv.setText(data.get(position).get("Name").toString());
			numtv.setText(data.get(position).get("count").toString());

			departmenttv.setGravity(Gravity.CENTER);
			numtv.setGravity(Gravity.CENTER);

			ll.addView(departmenttv, new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			ll.addView(numtv, new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			return ll;
		}

	}
}
