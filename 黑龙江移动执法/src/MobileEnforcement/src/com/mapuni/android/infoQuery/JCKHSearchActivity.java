package com.mapuni.android.infoQuery;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.lidroid.xutils.util.LogUtils;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.RWXX.ExpandableBaseAdapter;
import com.mapuni.android.dataprovider.SqliteUtil;
/**
 * FileName:JCKHSearchActivity.java Description:稽查考核查询条件界面
 * 
 * @author 赵瑞青
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2013-12-12 
 */
public class JCKHSearchActivity extends BaseActivity {
	/** 当前登录用户 */
	private String userID;
	private EditText startTime_et, endTime_et, searchTasktype_et;
	private String rwlxCode;
	private ImageButton startDelbutton, endDelbutton;
	private RWXX rwxx;
	private QYJBXX qyjbxx;
	private LinearLayout middleLayout;
	private Spinner qyjbxx_query_city_name_;
	private Spinner qyjbxx_query_city_name_2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "稽查考核");
		rwxx = new RWXX();
		qyjbxx=new QYJBXX();
		init();

	}
	ArrayList<String> cityList = new ArrayList<String>();// 市adapter数据
	/**
	 * 初始化界面
	 */
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		View searchView = inflater.inflate(R.layout.jckh_search, null);
		
//		qyjbxx_query_city_name_ = (Spinner) searchView.findViewById(R.id.qyjbxx_query_city_name_);
//		qyjbxx_query_city_name_2 = (Spinner) searchView.findViewById(R.id.qyjbxx_query_city_name_2);
//		// 设置下拉列表值不可修改
//		qyjbxx_query_city_name_.setClickable(false);	
//		qyjbxx_query_city_name_2.setClickable(false);	
//		qyjbxx_query_city_name_.setPrompt("-请选择-");
//		qyjbxx_query_city_name_2.setPrompt("-请选择-");
//		
//			setcity_adapter_byk();
		// 创建时间
		startTime_et = (EditText)searchView.findViewById(R.id.startTime);
		startTime_et.setOnClickListener(new startTimeListener());
		endTime_et = (EditText) searchView.findViewById(R.id.endTime);
		endTime_et.setOnClickListener(new endTimeListener());

		startDelbutton = (ImageButton) searchView.findViewById(R.id.jckh_start_delete_btn);
		startDelbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startTime_et.setText("");
				}
			});

		endDelbutton = (ImageButton) searchView.findViewById(R.id.jckh_end_delete_btn);
		endDelbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				endTime_et.setText("");
			}
		});

		// 任务类型
		ImageButton imbutton = (ImageButton)searchView.findViewById(R.id.jckh_rwlx_delete_btn);
		imbutton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			rwlxCode = "";
			searchTasktype_et.setText("");
	
			}
		});
		searchTasktype_et = (EditText)searchView.findViewById(R.id.search_Tasktype_et);
		searchTasktype_et.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			createDialogViewForTaskType();
			}
		});
		// 查询按钮
		Button searchButton2 = (Button)searchView.findViewById(R.id.search_Jckh_btn2);
		searchButton2.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String startTime = startTime_et.getText().toString();
			String endTime = endTime_et.getText().toString();
			if (!TextUtils.isEmpty(startTime)  && !TextUtils.isEmpty(endTime) ) {
			if (!DateCompare(startTime, endTime)) {
				Toast.makeText(JCKHSearchActivity.this, "结束时间不能小于开始时间", 1).show();
				// startTime_et.setText("");
				endTime_et.setText("");
				return;
				}
			//跳转到地区人员信息详细
			Intent intent = new Intent(JCKHSearchActivity.this,JCKHInfoActivity.class);
			HashMap<String,Object> searchCondition = getSearchCondition();
			intent.putExtra("startTime",searchCondition.get("startTime")+"");
			intent.putExtra("endTime", searchCondition.get("endTime")+"");
			if (searchTasktype_et.getText().toString().trim().equals("")||searchTasktype_et.getText().toString().trim()==null) {
				intent.putExtra("regionode", "230000000");
			}else {
				intent.putExtra("regionode", rwlxCode);
			}
			
			startActivity(intent);
			}else{
				//跳转到地区人员信息详细
				Intent intent = new Intent(JCKHSearchActivity.this,JCKHInfoActivity.class);
				HashMap<String,Object> searchCondition = getSearchCondition();
				intent.putExtra("startTime","0001/01/01 00:00:00");
				intent.putExtra("endTime","0001/01/01 00:00:00");
				if (searchTasktype_et.getText().toString().trim().equals("")||searchTasktype_et.getText().toString().trim()==null) {
					intent.putExtra("regionode", "230000000");
				}else {
					intent.putExtra("regionode", rwlxCode);
				}
				
				startActivity(intent);
			}
				}
			});
		
			
				
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		middleLayout.addView(searchView);
		
		
	}
	

	private void setcity_adapter_byk() {
			
		ArrayList<String> cityList = new ArrayList<String>();// 市adapter数据

	//	final ArrayList<HashMap<String, Object>> cityAllList = getRegionList(parentCode);// 城市的全部信息
		final ArrayList<HashMap<String, Object>> countyAllList = new ArrayList<HashMap<String, Object>>();// 区县的信息
		final ArrayList<String> countyList = new ArrayList<String>();// 区县adapter数据
		countyList.add("选择县(全部)");
		
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	
	
	/** 查询开始时间 **/
	private class startTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			startTime_et.setHint(null);
			startTime_et.setText("");
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(JCKHSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
					String monthStr = "";
					if (monthOfYear < 9) {
						monthStr = "0" + (monthOfYear + 1);
					} else {
						monthStr = (monthOfYear + 1) + "";
					}
					startTime_et.setText(year + "-" + monthStr + "-" + dayOfMonth);
				}

			}, year1, month1, day1).show();
		}
	}

	/** 查询结束时间 **/
	private class endTimeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			endTime_et.setHint(null);
			endTime_et.setText("");
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(JCKHSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
					String monthStr = "";
					if (monthOfYear < 9) {
						monthStr = "0" + (monthOfYear + 1);
					} else {
						monthStr = (monthOfYear + 1) + "";
					}
					endTime_et.setText(year + "-" + monthStr + "-" + dayOfMonth);
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
		if ((java.sql.Date.valueOf(startTime)).before(java.sql.Date.valueOf(endTime))) {
			flag = true;
		}
		if ((java.sql.Date.valueOf(startTime)).equals(java.sql.Date.valueOf(endTime))) {
			flag = true;
		}
		return flag;
	}
	
	// 任务类型
	public void createDialogViewForTaskType() {
			LayoutInflater inflater = LayoutInflater.from(JCKHSearchActivity.this);
			View viewex = inflater.inflate(R.layout.expand_list_dialog, null);
			TextView expand_title_tv = (TextView) viewex.findViewById(R.id.expand_title_tv);
			expand_title_tv.setText("区域分类");
			ArrayList<HashMap<String, Object>> groupData = rwxx.getTask_type();
			//final ArrayList<HashMap<String, Object>> groupData = getRegionList("1");// 城市的全部信息
			final List<String> groupList = new ArrayList<String>();
			if (groupData != null && groupData.size() > 0) {
				for (int i = 0; i < groupData.size(); i++) {
					Object name = groupData.get(i).get("regionname");
					groupList.add(name+"");
				}
			}
			final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = rwxx.getTask_type_child(groupData);
			
			final ExpandableListView expandableListView = (ExpandableListView) viewex.findViewById(R.id.expandablelistview);
			ExpandableBaseAdapter adapter = new ExpandableBaseAdapter(this, groupList, childMapData);
			expandableListView.setAdapter(adapter);
			expandableListView.setCacheColorHint(0);// 设置拖动列表的时候防止出现黑色背景
			expandableListView.setGroupIndicator(getResources().getDrawable(R.layout.expandablelistviewselector));

			AlertDialog.Builder builder = new AlertDialog.Builder(JCKHSearchActivity.this);
			builder.setIcon(R.drawable.yutu);
			builder.setTitle("黑龙江省");
			builder.setView(viewex);

			final AlertDialog dialog = builder.create();
			dialog.show();
			searchTasktype_et.setHint(null);
			searchTasktype_et.setText("");

			expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

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
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					String cnames = childMapData.get(groupPosition).get(childPosition).get("regionname").toString();
					rwlxCode = childMapData.get(groupPosition).get(childPosition).get("regioncode").toString();
//					if (cnames.equals("市辖区")) {
//						 rwlxCode=childMapData.get(groupPosition).get(childPosition).get("parentcode").toString();
//					}
					if(v.getTag().equals("1")){
						rwlxCode=childMapData.get(groupPosition).get(childPosition).get("parentcode").toString();
						searchTasktype_et.setText(groupList.get(groupPosition));
					}else{
						searchTasktype_et.setText(cnames);
					}					
					searchTasktype_et.setTag(rwlxCode);

					dialog.cancel();
					return false;
				}
			});
		}
	
		// 获取查询条件
		public HashMap<String, Object> getSearchCondition() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			/*
			 * map.put("pageIndex",1); map.put("pageSize",15);
			 */
			SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
			String stime=startTime_et.getText().toString();
			String etime=endTime_et.getText().toString();
			String startDate="";
			String endDate="";
			if (!stime.equals("")){
				try {
					startDate=from.format(from.parse(stime));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!etime.equals("")){
				try {
					endDate=from.format(from.parse(etime));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			map.put("startTime", startDate);
			map.put("endTime", endDate);
			map.put("regioncode", rwlxCode);
			return map;
		}
		
		public ArrayList<HashMap<String, Object>> getRegionList(String parentCode) {
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("parentCode", parentCode);
			ArrayList<HashMap<String, Object>> cityList = SqliteUtil.getInstance().getList("RegionCode,RegionName", conditions, "Region");
			return cityList;

		}
		//序列化MAP集合
		public class SerializableMap implements Serializable {

		    private Map<String,Object> map;

		    public Map<String, Object> getMap() {
		        return map;
		    }

		    public void setMap(Map<String, Object> map) {
		        this.map = map;
		    }
		}
}

