package com.mapuni.android.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.dataprovider.SqliteUtil;
/**
 * FileName: TeamManageActivity.java
 * Description:稽查考核--队伍管理
 * @author 王振洋
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-5 上午10:20:10
 */
public class TeamManageActivity extends QueryListActivity{
	
	private  Spinner onespinner=null;//一级下拉
	private  Spinner twospinner=null;//二级下拉
	
	private  AutoCompleteTextView  oneauto=null;//一级下拉
	private  AutoCompleteTextView  twoauto=null;//二级下拉
	private  FrameLayout onefram=null;
	private  FrameLayout twofram = null;
	private  SqliteUtil sqliteUitl = SqliteUtil.getInstance();
	ArrayAdapter<String> adapter ;//筛选条件的适配器
    List<String> onecode = new ArrayList<String>();//存放一级菜单的id
    List<String> twocode = new ArrayList<String>();//存放二级菜单的id
    List<HashMap<String, Object>> oneapterlist = new ArrayList<HashMap<String,Object>>();
	List<HashMap<String, Object>> twoadapterlist = new ArrayList<HashMap<String,Object>>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取顶部父容器
		LinearLayout topLayout = (LinearLayout)this.findViewById(R.id.topLayout);
		//添加表头布局
		topLayout.addView(addTableHead());
		
		super.setSearchButtonVisiable(false);
	}
	
	/**
	 * 添加表头布局
	 *
	 *	RelativeLayout
	 *		Spinner(syncAll)
	 *		Spinner(syncLastest)
	 *	RelativeLayout
	 *
	 * @return
	 */
	//筛选条件
	private RelativeLayout addTableHead() {
		int syncAllId 	   = 224522;
		int chkChoiceAllId = 224523;
		
		RelativeLayout tableHead = new RelativeLayout(this);
		tableHead.setGravity(Gravity.CENTER_VERTICAL);
		tableHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_title_datasync));
		LayoutInflater ll=LayoutInflater.from(TeamManageActivity.this);
		View view=ll.inflate(R.layout.teammanager, null);	
		oneauto = (AutoCompleteTextView)view.findViewById(R.id.oneatuo); 
		
		twoauto = (AutoCompleteTextView)view.findViewById(R.id.twoatuo);
		onespinner = (Spinner)view. findViewById(R.id.onespinner);   
		twospinner = (Spinner)view. findViewById(R.id.twospinner);
		onefram = (FrameLayout)view. findViewById(R.id.one_clicklayout);
		twofram = (FrameLayout)view. findViewById(R.id.two_clicklayout);
		
		initializationSpinner();
		tableHead.addView(view);
		
		onefram.setOnClickListener(new View.OnClickListener() {   
		      @Override  
		     public void onClick(View v) {   
		         // TODO Auto-generated method stub   
		         oneauto.showDropDown();   
		      }   
		      });
		twofram.setOnClickListener(new View.OnClickListener() {   
		      @Override  
		     public void onClick(View v) {   
		         // TODO Auto-generated method stub   
		         twoauto.showDropDown();   
		      }   
		      });
		 
		return tableHead;
	}
	
	/**
	 * 筛选条件点击事件
	 */
	private OnItemSelectedListener oneItemSelectedListener = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int onepo = onespinner.getSelectedItemPosition();
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("regincode", onecode.get(onepo).toString());
			twoadapterlist = BaseClass.DBHelper.getList("TeamBuildingPerson",conditions);
			ArrayAdapter<String> adapter = returnAdapter(twoadapterlist,"workent","depid","two");
//			ArrayAdapter<String> adapter = returnAdapter(twoadapterlist,"depname","depid","two");
			twospinner.setAdapter(adapter);
			twospinner.setSelection(0, true);
			conditions = new HashMap<String, Object>();
			if(!twocode.equals("")){
				conditions.put("workent", twocode.get(onepo).toString());
			}
			dataList = businessObj.getDataList(conditions);
			LoadList(bundle, dataList, style);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	
	};
	/**
	 * twospinner下拉列表点击事件，更新ListView的显示内容
	 */
	private OnItemSelectedListener twoItemSelectedListener = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
			int twopo = twospinner.getSelectedItemPosition();
			HashMap<String, Object> conditions = new HashMap<String, Object>();////////
			conditions.put("workent", twocode.get(twopo).toString());
			String sql="select * from TeamBuildingPerson where workent='"+twocode.get(twopo)+"'";
				dataList =sqliteUitl.queryBySqlReturnArrayListHashMap(sql);
				LoadList(bundle, dataList, style);
				
			} catch (SQLiteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			dataList = businessObj.getDataList(conditions);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	
	};
	
	/**
	 * Description:初始化spinner的值
	 * 
	 * @author 王振洋
	 * Create at: 2012-12-5 上午10:19:40
	 */
	public void initializationSpinner(){
//		onespinner.setOnItemSelectedListener(oneItemSelectedListener);
		adapter = returnAdapter("one");
		oneauto.setAdapter(adapter);    
		oneauto.setOnItemClickListener(new OnItemClickListener() {   
		    @Override  
		     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {   
		    	try{
		    		String Reginname=null;
		    	onespinner.setSelection(position);   
		    	int onepo = onespinner.getSelectedItemPosition();
				HashMap<String, Object> conditions = new HashMap<String, Object>();
				String reginname=oneapterlist.get(onepo).get("regionname").toString().substring(0,oneapterlist.get(onepo).get("regionname").toString().length());
				System.out.println("reginname"+reginname);
				
				twoadapterlist = getObscureList1("TeamBuildingPerson",reginname);
			
				ArrayAdapter<String> adapter = returnAdapter(twoadapterlist,"workent","regincode","two");
				twospinner.setAdapter(adapter);
				twoauto.setAdapter(adapter);
				twospinner.setSelection(0, true);
				conditions = new HashMap<String, Object>();
				conditions.put("workent", twocode.get(0).toString());
				dataList = businessObj.getDataList(conditions);
				LoadList(bundle, dataList, style);
				
		    	}catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }   
		     });
		
		
		twospinner.setOnItemSelectedListener(twoItemSelectedListener);
		adapter = returnAdapter("two");
		twoauto.setAdapter(adapter); 
		twoauto.setOnItemClickListener(new OnItemClickListener() {   
		    @Override  
		     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {   
		         // TODO Auto-generated method stub   
		    	twospinner.setSelection(position);
		    	int twopo = twospinner.getSelectedItemPosition();
				HashMap<String, Object> conditions = new HashMap<String, Object>();
//				conditions.put("regincode", twocode.get(twopo).toString());
				System.out.println(twocode);
				conditions.put("regincode", twocode.get(twopo).toString());
				dataList = businessObj.getDataList(conditions);//
				LoadList(bundle, dataList, style);
		    }   
		     });
		
	}
	
	 
	/**
	 * 得到spinner数据的方法
	 */
	/**
	 * Description:组装并返回对应spinner的适配Adapter
	 * @param str 对应spinner的标示字符串
	 * @return ArrayAdapter
	 * @author 王振洋
	 * Create at: 2012-12-5 上午10:36:27
	 */
	public ArrayAdapter<String> returnAdapter(String str){
		HashMap<String, Object> conditions = null;
		
		if(str.equals("one")){//第一个
			HashMap<String, Object> condition1=new HashMap<String, Object>();
			
			condition1.put("U_LoginName", Global.getGlobalInstance().getUsername());
			ArrayList<HashMap<String, Object>> data=BaseClass.DBHelper.getList("RegionCode", condition1, "PC_Users");//得到当前登陆人的区域code集合
			String code=data.get(0).get("regioncode").toString();//区域code
			
			conditions = new HashMap<String, Object>();
			conditions.put("parentcode", "0");
			String colum="regioncode";
			String ParentCode=BaseClass.DBHelper.getList(colum, conditions, "Region").get(0).get("regioncode").toString();//从数据库中查询省城code
			conditions.clear();
			conditions.put("parentcode", ParentCode);
			oneapterlist = BaseClass.DBHelper.getList("Region",conditions);
			if(oneapterlist==null){
				//Toast.makeText(TeamManageActivity.this, "数据库无数据", Toast.LENGTH_LONG).show();
				return  new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, new String[]{}) ;
			}
			ArrayList<HashMap<String, Object>> citys=new ArrayList<HashMap<String,Object>>();//存放当前登陆人的行政地区code
			for(HashMap<String, Object> map:oneapterlist){
				String citycode=map.get("regioncode").toString();
				if(code.contains(citycode)){//如果执法区域内包含cityscode
					citys.add(map);
				}
			}
			oneapterlist=citys;
			System.out.println("oneapterlist"+oneapterlist);
			adapter = returnAdapter(oneapterlist,"regionname","regioncode",str);
			onespinner.setAdapter(adapter);
			onespinner.setSelection(0, true);
			
		}
		if(str.equals("two")){//第二个
			conditions = new HashMap<String, Object>();
			conditions.put("regincode",oneapterlist.get(0).get("regionname"));
			String reginname=oneapterlist.get(0).get("regionname").toString();
			System.out.println("reginname"+reginname);
//			twoadapterlist = BaseClass.DBHelper.getObscureList("TeamBuildingPerson","regincode",reginname);
			twoadapterlist = getObscureList1("TeamBuildingPerson",reginname);
			System.out.println("twoadapterlist"+twoadapterlist);
			adapter = returnAdapter(twoadapterlist,"workent","regincode",str);
			twospinner.setSelection(0, true);
			twospinner.setAdapter(adapter);

			
		}
		return adapter;	
	
	}
	/**
	 * Description:根据第一个spinner的选择从数据库查询出第二个spinner的显示数据
	 * @param table 
	 * @param reginname 所属区域名称
	 * @return
	 * @author 王振洋
	 * Create at: 2012-12-5 上午10:49:35
	 */
	public ArrayList<HashMap<String, Object>> getObscureList1(String table,
			String reginname) {
		StringBuilder sql=new StringBuilder("select distinct WorkEnt,RegionCode from TeamBuildingPerson a inner "
				+"join region b on a.[ReginCode]=b.RegionCode where b.[RegionName] ='"+reginname+"'");

		try {
			
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return new ArrayList<HashMap<String, Object>>();
	}
	/**
	 * 根据数据返回一个adapter
	 *
	 * @param adapterlist 数据源
	 * @param name  显示的文字
	 * @param code  文字对应的id
	 * @param flag  判断是哪个spinner
	 * @return ArrayAdapter
	 */
	public ArrayAdapter<String> returnAdapter(List<HashMap<String, Object>> adapterlist,String name,String code,String flag){
		List<String> adaptername = new ArrayList<String>();
		List<String> adaptercode = new ArrayList<String>();
		for (HashMap<String, Object> spinnermap : adapterlist) {
			if (spinnermap.get(name).toString().length() <= 0)
				continue;
			adaptername.add(spinnermap.get(name).toString());
			//adaptercode.add(spinnermap.get(code).toString());
		}
	
		if(flag.equals("one")){
			onecode=adaptercode;
		}
		if(flag.equals("two")){
			twocode=adaptername;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, adaptername);// spinner适配器
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		return adapter;	
	
}
}
