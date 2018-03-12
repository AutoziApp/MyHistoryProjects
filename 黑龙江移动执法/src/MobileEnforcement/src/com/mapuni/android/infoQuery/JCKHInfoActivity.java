package com.mapuni.android.infoQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.enforcement.QdjcnlActivity;
import com.mapuni.android.enforcement.QdjcnlActivity.WorkAsyncTask;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.teamcircle.DataUtils;

public class JCKHInfoActivity extends BaseActivity{
	private RWXX rwxx;
	private LinearLayout middleLayout;
	LayoutInflater _LayoutInflater;
	View infoView;
	/**用户所属地区**/
	private final String UserAreaCode=Global.getGlobalInstance().getAreaCode();
	private YutuLoading yutuLoading;
	/**分页请求数据的页数*/
	private int pageIndex=1;
	/**分页请求一页的数量*/
	private final int pageSize=30;
	private CompanyAdapter companyAdapter;
	private PagingListView jckh_info_list;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "稽查考核");
		rwxx = new RWXX();
		initView();
		initData();

	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		_LayoutInflater = LayoutInflater.from(this);
		infoView = _LayoutInflater.inflate(R.layout.jckh_info_second, null);
		jckh_info_list = (PagingListView)infoView.findViewById(R.id.jckh_info_listview2);
		jckh_info_list.setCacheColorHint(Color.TRANSPARENT );
		((LinearLayout)findViewById(R.id.middleLayout)).addView(infoView);
//		jckh_info_list.setOnPageCountChangListener(new PagingListView.PageCountChangListener() {
//			
//			@Override
//			public void onAddPage(AbsListView view) {
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						if(!Net.checkURL(Global.getGlobalInstance().getSystemurl())){
//							handler.sendEmptyMessage(1);
//							return;
//						}
//						ArrayList<HashMap<String, Object>> data=requestSearchJCKHInfo(searchConditionsList);
//						Message ms=new Message();
//						ms.what=0;
//						ms.obj=data;
//						handler.sendMessage(ms);
//					}
//				}).start();
//				
//			}
//		});
		
	}
	
	private void initData() {
		Intent intent = getIntent();
		String currentTime = DataUtils.getCurrentTime();
		final HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("LeTaskBegionTime", intent.getStringExtra("startTime"));
//		map.put("LeTaskEndTime", intent.getStringExtra("endTime")+" "+currentTime);
		map.put("LeTaskEndTime", intent.getStringExtra("endTime"));
		map.put("RegionCode", intent.getStringExtra("regionode"));
		map.put("QueryType", "1");
		yutuLoading=new YutuLoading(JCKHInfoActivity.this);
		yutuLoading.setLoadMsg("正在请求数据，请稍后！", "");
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(!Net.checkURL(Global.getGlobalInstance().getSystemurl())){
					handler.sendEmptyMessage(1);
					return;
				}
				ArrayList<HashMap<String, Object>> data=requestSearchJCKHInfo(map);
				Message ms=new Message();
				ms.what=2;
				ms.obj=data;
				handler.sendMessage(ms);
				
			}
		}).start();
		
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			yutuLoading.dismissDialog();
			switch (msg.what) {
			case 0:
				ArrayList<HashMap<String, Object>> data= (ArrayList<HashMap<String, Object>>) msg.obj;
				if(data.size()<pageSize){
					if(pageIndex>1){
						jckh_info_list.setIsCompleted(true);

						Toast.makeText(JCKHInfoActivity.this, "全部数据加载已完成", Toast.LENGTH_SHORT).show();
					}
				}
				companyAdapter.addData(data);
				jckh_info_list.setFootViewVisibility(false);
				break;
			case 1:
				Toast.makeText(JCKHInfoActivity.this, "网络不通畅，无法查询任务！", 
						Toast.LENGTH_SHORT).show();
			    break;
			case 2:
				ArrayList<HashMap<String, Object>> firstData= (ArrayList<HashMap<String, Object>>) msg.obj;
				companyAdapter=new CompanyAdapter(firstData,JCKHInfoActivity.this);
				jckh_info_list.setAdapter(companyAdapter);
				jckh_info_list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						HashMap<String, Object> hashmap = (HashMap<String, Object>) companyAdapter.getItem(arg2);
						if ((hashmap.get("letasktotalcount").toString().trim()).equals("0")) {
							Toast.makeText(JCKHInfoActivity.this, "暂无相关信息", Toast.LENGTH_SHORT).show();
						}else {
							Intent intent = new Intent(JCKHInfoActivity.this,JCKHDetailedActivity.class);
							intent.putExtra("TransactorId",hashmap.get("personid")+"");
							startActivity(intent);
						}			
					}				
				});
				break;
			default:
				break;
			}
			
		};
	};
	/**
	 * 传入条件调用webservice获取任务信息
	 */
	public ArrayList<HashMap<String, Object>> requestSearchJCKHInfo(HashMap<String, Object> map2 ){
		
		String methodName="GetTaskStatistcsListByTransactorName";
		//String methodName="GetTaskStatisticsList";
		ArrayList<HashMap<String, Object>> searchdata=new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		data_json.add(map2);
		String leTaskStatisticsQueryJson = JsonHelper
				.listToJSONXin(data_json);
		param.put("leTaskStatisticsQueryJson",leTaskStatisticsQueryJson);
		params.add(param);
		try {
			String jsonStr=(String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName,
					params, Global.getGlobalInstance().getSystemurl()+Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
			 if(jsonStr!=""){
					searchdata=JsonHelper.paseJSON(jsonStr);
					if (searchdata==null ) {
						searchdata=new ArrayList<HashMap<String,Object>>();
					}else{
						searchdata=parseLowerList(searchdata);
					}			
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchdata;
	}
	
	
	/**
	 * 查询出来的信息列表
	 *
	 */
	private class CompanyAdapter extends BaseAdapter{
		ArrayList<HashMap<String, Object>> CompanyAdapterData;
		Context context;
//		int textsize=22;
		public CompanyAdapter(ArrayList<HashMap<String, Object>> CompanyAdapterData,Context context){
			this.CompanyAdapterData=CompanyAdapterData;
			this.context=context;
		}
		@Override
		public int getCount() {
			int size= CompanyAdapterData.size();
			if(size==0){
				return 1;
			}
			return size;
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
		
		public void addData(ArrayList<HashMap<String, Object>> CompanyAdapterData){
			this.CompanyAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}
		public ArrayList<HashMap<String, Object>> getData(){
			return CompanyAdapterData;
		}
		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(CompanyAdapterData.size()==0){

				/** 没有数据，替换布局 */
				YutuLoading loading = new YutuLoading(context);
				loading.setLoadMsg("加载中", "无符合条件数据！", Color.BLACK);
				loading.setFocusable(false);
				loading.setClickable(false);
				loading.setEnabled(false);
				loading.setFailed();
				loading.setLayoutParams(new android.widget.AbsListView.LayoutParams(
						android.widget.AbsListView.LayoutParams.FILL_PARENT,
						android.widget.AbsListView.LayoutParams.FILL_PARENT));
				return loading;
			
			}
			convertView = View.inflate(context,R.layout.jckh_info_list_second, null);
			HashMap<String,Object> map = CompanyAdapterData.get(position);
			TextView username = (TextView) convertView.findViewById(R.id.username_jckh);
			username.setText(map.get("transactorname").toString());
			
			//待办任务
			TextView jckh_info1 = (TextView) convertView.findViewById(R.id.jckh_info1);
			jckh_info1.setText(map.get("letaskpendingcount")+"");
			//协办任务
			TextView jckh_info2 = (TextView) convertView.findViewById(R.id.jckh_info2);
			jckh_info2.setText(map.get("letaskcooperatedcount")+"");
			//已办结任务
			TextView jckh_info3 = (TextView) convertView.findViewById(R.id.jckh_info3);
			jckh_info3.setText(map.get("letaskfinishedcount")+"");	
			//任务总数
			TextView jckh_info4 = (TextView) convertView.findViewById(R.id.jckh_info4);
			jckh_info4.setText(map.get("letasktotalcount")+"");
			
			return convertView;
		}
		
	}
	
	
	/**
	 * 转成小写的key
	 * @param data
	 * @return 
	 */
	public ArrayList<HashMap<String, Object>> parseLowerList(ArrayList<HashMap<String, Object>> data){
		ArrayList<HashMap<String, Object>> parseData=new ArrayList<HashMap<String,Object>>();;
		for(int i=0;i<data.size();i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			Iterator<?> iterator=data.get(i).entrySet().iterator();
		
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(), entry.getValue());
			}
			parseData.add(map);
		
		}
		return parseData;
	}

}
