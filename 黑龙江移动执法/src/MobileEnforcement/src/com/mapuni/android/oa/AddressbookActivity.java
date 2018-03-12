package com.mapuni.android.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.base.adapter.ISpinner;
import com.mapuni.android.base.adapter.ISpinner.ISpinnerInterface;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.dataprovider.SqliteUtil;


/**
 * FileName: AddressbookActivity.java
 * Description: 通讯录
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-7 上午09:25:31
 */
public class AddressbookActivity extends QueryListActivity implements ISpinnerInterface {
	
	/** 页面TAG内容 */
	private  final String TAG = "AddressbookActivity";
	/** 一级下拉 */
	private  Spinner onespinner = null; 
	/** 二级下拉 */
	private  Spinner twospinner = null;  
	/** 三级下拉 */
	private  Spinner threespinner = null;  
	/** 筛选条件的适配器 */
	ArrayAdapter<SpinnerItem> adapter;  
	/** 存放一级菜单的id */
	List<String> onecode = new ArrayList<String>(); 
	/** 存放二级菜单的id */
	List<String> twocode = new ArrayList<String>(); 
	/** 存放三级菜单的id */
	List<String> threecode = new ArrayList<String>();  
	/** 一级菜单的数据源 */
	List<HashMap<String, Object>> oneapterlist = new ArrayList<HashMap<String, Object>>();
	/** 二级菜单的数据源 */
	List<HashMap<String, Object>> twoapterlist = new ArrayList<HashMap<String, Object>>();
	/** 三级菜单的数据源 */
	List<HashMap<String, Object>> threeadapterlist = new ArrayList<HashMap<String, Object>>();
	/** 当前选中的菜单的值 */
	String twoparentcode = "", threeparentcode = "";
	LinearLayout lin;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获取顶部父容器
		LinearLayout midLayout = (LinearLayout) findViewById(R.id.middleLayout);
		midLayout.setOrientation(LinearLayout.VERTICAL);

		
		SqliteUtil su = SqliteUtil.getInstance();
		//石家庄的数据差异导致以下分支的出现--liusy
		String condition = "dept1A";
	
		//ArrayList<HashMap<String, Object>> one = su.queryBySqlReturnArrayListHashMap("select * from pc_departmentinfo where depid = 'dept1A'");
		ArrayList<HashMap<String, Object>> one = su.queryBySqlReturnArrayListHashMap("select * from pc_departmentinfo where depid = '"+condition+"'");
		
	    ISpinner is = new ISpinner(one, "pc_departmentinfo", this,3,null,this,new String[]{"depid","depname"},new String[]{"attenname"});
	    LinearLayout linWai = new LinearLayout(this);
	    LinearLayout lin = is .returnSpinnerLayout();
	    linWai.addView(lin);
	    midLayout.addView(linWai, 0);
	    
	    LinearLayout searchLayout = new LinearLayout(this);
	    searchLayout.setGravity(Gravity.CENTER_VERTICAL);
	    searchLayout.setPadding(5, 0, 5, 0);
	    searchLayout.setOrientation(LinearLayout.HORIZONTAL);
	    searchLayout.setBackgroundResource(R.drawable.bg_layout2);
	    searchLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 90));
	    
	    final AutoCompleteTextView acTv = new AutoCompleteTextView(this);
	    acTv.setBackgroundResource(R.drawable.sitelaw_edittext_bg);
	    acTv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 60, 1.0f));
	    acTv.setHint("请输入要找的人的名字");
	    searchLayout.addView(acTv);
	    
	    ImageView btnImg = new ImageView(this);
	    btnImg.setBackgroundResource(R.drawable.sitelaw_search);
	    btnImg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,60));
	    searchLayout.addView(btnImg);
	    
	    midLayout.addView(searchLayout,1);
	    
	    btnImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = acTv.getText().toString();
				if("".equals(content.trim())){
					Toast.makeText(AddressbookActivity.this, "请输入内容后再搜索", 0).show();
					return;
				}
				dataList = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select * from V_Users where U_RealName like '%"+content+"%'"
						);
				LoadList(bundle, dataList, style);
			}
		});
//		midLayout.addView(addTableHead(), 0);
	}
	@Override
	public void flushData(HashMap<String, Object> filterMap) {
		for (String key : filterMap.keySet()) {
			if("".equals(filterMap.get(key)) || filterMap.get(key)==null){
				filterMap.remove(key);
			}
		}
		if(filterMap.size()==0){
			return;
		}
		dataList = businessObj.getDataList(filterMap);
		LoadList(bundle, dataList, style);
	}
	/**
	 * 添加表头布局
	 * 
	 * RelativeLayout Spinner(syncAll) Spinner(syncLastest) RelativeLayout
	 * 
	 * @return
	 */
	// 筛选条件
//	private RelativeLayout addTableHead() {
//		RelativeLayout tableHead = new RelativeLayout(this);
//		tableHead.setGravity(Gravity.CENTER_VERTICAL);
//		tableHead.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.bg_title_datasync));
//		LayoutInflater ll = LayoutInflater.from(AddressbookActivity.this);
//		View view = ll.inflate(R.layout.addressbook, null);
//		onespinner = (Spinner) view.findViewById(R.id.onespinner);
//		twospinner = (Spinner) view.findViewById(R.id.twospinner);
//		threespinner = (Spinner) view.findViewById(R.id.threespinner);
//
//		HashMap<String, Object> conditions = new HashMap<String, Object>();
//		conditions.put("depParentid", "0");
//		oneapterlist = BaseClass.DBHelper.getList("PC_DepartmentInfo",
//				conditions);
//		adapter = returnAdapter(oneapterlist, "depname", "depid");
//		onespinner.setAdapter(adapter);
//		onespinner.setSelection(0, true);
//		onespinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				int onepo = onespinner.getSelectedItemPosition();
//				SpinnerItem item = (SpinnerItem) onespinner.getSelectedItem();
//				HashMap<String, Object> conditions = new HashMap<String, Object>();
//				twoparentcode = item.getCode();
//				conditions.put("depParentid", twoparentcode);
//				twoapterlist = BaseClass.DBHelper.getList("PC_DepartmentInfo",
//						conditions);
//				ArrayAdapter<SpinnerItem> adapter = returnAdapter(twoapterlist,
//						"depname", "depid");
//				// twospinner.setPrompt("--选择部门--");
//				twospinner.setAdapter(adapter);
//				// threespinner.setPrompt("--选择部门--");
//				if (!twoparentcode.equals("")) {
//					conditions = new HashMap<String, Object>();
//					conditions.put("depid", twoparentcode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else {
//					dataList = businessObj.getDataList();
//					LoadList(bundle, dataList, style);
//				}
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		});
//		twoparentcode = oneapterlist.get(0).get("depid").toString();
//		conditions = new HashMap<String, Object>();
//		conditions.put("depParentid", twoparentcode);
//		twoapterlist = BaseClass.DBHelper.getList("PC_DepartmentInfo",
//				conditions);
//		adapter = returnAdapter(twoapterlist, "depname", "depid");
//		twospinner.setAdapter(adapter);
//		twospinner.setSelection(1, true);
//		twospinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				int onepo = twospinner.getSelectedItemPosition();
//				SpinnerItem item = (SpinnerItem) twospinner.getSelectedItem();
//				HashMap<String, Object> conditions = new HashMap<String, Object>();
//				threeparentcode = item.getCode();
//				conditions.put("depParentid", threeparentcode);
//				threeadapterlist = BaseClass.DBHelper.getList(
//						"PC_DepartmentInfo", conditions);
//				ArrayAdapter<SpinnerItem> adapter = returnAdapter(
//						threeadapterlist, "depname", "depid");
//				// threespinner.setPrompt("--选择部门--");
//				threespinner.setAdapter(adapter);
//				if (!threeparentcode.equals("")) {
//					conditions = new HashMap<String, Object>();
//					conditions.put("depid", threeparentcode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else if (!twoparentcode.equals("")) {
//					conditions = new HashMap<String, Object>();
//					conditions.put("depid", twoparentcode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else {
//					dataList = businessObj.getDataList();
//					LoadList(bundle, dataList, style);
//				}
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		});
//		ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String, Object>>();
//		threeparentcode = twoapterlist.get(1).get("depid").toString();
//		conditions = new HashMap<String, Object>();
//		conditions.put("depParentid", threeparentcode);
//		threeadapterlist = BaseClass.DBHelper.getList("PC_DepartmentInfo",
//				conditions);
//		if (threeadapterlist.size() == 0) {
//			conditions.remove("depParentid");
//			conditions
//					.put("depid", twoapterlist.get(0).get("depid").toString());
//			datalist = BaseClass.DBHelper.getList("PC_Users", conditions);
//		} else {
//			conditions.remove("depParentid");
//			conditions.put("depid", threeadapterlist.get(0).get("depid")
//					.toString());
//			datalist = BaseClass.DBHelper.getList("PC_Users", conditions);
//		}
//		LoadList(bundle, datalist, style);
//		adapter = returnAdapter(threeadapterlist, "depname", "depid");
//		threespinner.setAdapter(adapter);
//		threespinner.setSelection(0, true);
//		threespinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				int onepo = threespinner.getSelectedItemPosition();
//				SpinnerItem item = (SpinnerItem) threespinner.getSelectedItem();
//				String threecode = item.getCode();
//				HashMap<String, Object> conditions = new HashMap<String, Object>();
//				if (!threecode.equals("")) {
//					conditions.put("depid", threecode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else if (!threeparentcode.equals("")) {
//					conditions.put("depid", threeparentcode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else if (!twoparentcode.equals("")) {
//					conditions.put("depid", twoparentcode);
//					dataList = businessObj.getDataList(conditions);
//					LoadList(bundle, dataList, style);
//				} else {
//					dataList = businessObj.getDataList();
//					LoadList(bundle, dataList, style);
//				}
//
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		});
//		tableHead.addView(view);
//		return tableHead;
//	}
//
//	/**
//	 * 根据数据返回一个adapter
//	 * 
//	 * @param adapterlist
//	 *            数据源
//	 * @param name
//	 *            显示的文字
//	 * @param code
//	 *            文字对应的id
//	 * @param flag
//	 *            判断是哪个spinner
//	 * @return
//	 */
//	public ArrayAdapter<SpinnerItem> returnAdapter(
//			List<HashMap<String, Object>> adapterlist, String name, String code) {
//		List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
//		SpinnerItem item = null;
//		// Item item =new Item("","--选择部门--",0);
//		/*
//		 * Item item = null; if(adapterlist == oneapterlist){ item =new
//		 * Item("","--选择区划--",0); } else if(adapterlist == twoapterlist){ item
//		 * =new Item("","--选择单位--",0); } else if(adapterlist ==
//		 * threeadapterlist) { item =new Item("","--选择部门--",0); }
//		 * 
//		 * mbList.add(item);
//		 */
//		int i = 0;
//		if (adapterlist.size() == 0) {
//			SpinnerItem it = new SpinnerItem("", "暂无下级部门", 0);
//			mbList.add(it);
//			i = 1;
//		}
//		for (Map<String, Object> map : adapterlist) {
//			String autocode = map.get(code).toString();
//			String autoname = map.get(name).toString();
//			item = new SpinnerItem(autocode, autoname, i);
//			mbList.add(item);
//			i++;
//		}
//		ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(this,
//				android.R.layout.simple_spinner_item, mbList);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		return adapter;
//
//	}


}
