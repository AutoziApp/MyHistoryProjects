package com.mapuni.android.infoQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class JCKHDetailedActivity extends BaseActivity{
	PagingListView jckh_info_list;
	LayoutInflater _LayoutInflater;
	View infoView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "稽查考核详情");
		HashMap<String, Object> hashmap = (HashMap<String, Object>) getIntent().getExtras().get("hashmap" );
		_LayoutInflater = LayoutInflater.from(this);
		infoView = _LayoutInflater.inflate(R.layout.jckh_info_detailed, null);
		
		((LinearLayout)findViewById(R.id.middleLayout)).addView(infoView);
		  jckh_info_list= (PagingListView) infoView.findViewById(R.id.jckh_info_listview);
		jckh_info_list.setCacheColorHint(Color.TRANSPARENT );
		//获取数据
		initData();
	}
	//获取数据
	private void initData() {
		Intent intent = getIntent();
		String TransactorId = intent.getStringExtra("TransactorId");
		ArrayList<HashMap<String,Object>> requestSearchJCKHInfo = requestSearchJCKHInfo(TransactorId);
		CompanyAdapter adapter=new CompanyAdapter(requestSearchJCKHInfo,JCKHDetailedActivity.this);
		jckh_info_list.setAdapter(adapter);
		
	}	
	/**
	 * 传入条件调用webservice获取任务信息
	 */
	public ArrayList<HashMap<String, Object>> requestSearchJCKHInfo(String TransactorId ){
		
		String methodName="QueryList";
		ArrayList<HashMap<String, Object>> searchdata=new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> data_json = new ArrayList<HashMap<String, Object>>();
		HashMap<String,Object> map2=new HashMap<String, Object>();
		//{"TransactorId":1001,"QueryType":2,"PageIndex":1,"PageSize":15}
		map2.put("TransactorId", TransactorId);
		map2.put("QueryType", 2);
		map2.put("PageIndex", 1);
		map2.put("PageSize", 200);
		data_json.add(map2);
		String flowQueryConditionModel = JsonHelper
				.listToJSONXin(data_json);
		param.put("flowQueryConditionModel",flowQueryConditionModel);
		params.add(param);
		try {
			String jsonStr=(String) WebServiceProvider.callWebService(Global.NAMESPACE, methodName,
					params, Global.getGlobalInstance().getSystemurl()+Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
				JSONObject jsonObject=new JSONObject(jsonStr);
				String json = jsonObject.getString("TaskListLine");			 
			 if(jsonStr!=""){
					searchdata=JsonHelper.paseJSON(json);
					if (searchdata==null ) {
						searchdata=new ArrayList<HashMap<String,Object>>();
					}else{
						searchdata=parseLowerList(searchdata);
					}			
				}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchdata;
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
			//创建时间
			TextView username_jckh = (TextView) convertView.findViewById(R.id.username_jckh);
			String startTime = map.get("letaskcreatedtime").toString();
			String[] startTimes = startTime.split(" ");
			username_jckh.setText(startTimes[0]);			
			//办结期限
			TextView jckh_info1 = (TextView) convertView.findViewById(R.id.jckh_info1);
			String endTime = map.get("letasktransactedtime").toString();
			String[] endTimes = endTime.split(" ");
			jckh_info1.setText(endTimes[0]);
			//任务名称
			TextView jckh_info2 = (TextView) convertView.findViewById(R.id.jckh_info2);
			jckh_info2.setText(map.get("letaskname")+"");
			//任务状态
			TextView jckh_info3 = (TextView) convertView.findViewById(R.id.jckh_info3);
			String trim = map.get("letaskprocessstatus").toString().trim();
			if (trim.equals("10")) {
				jckh_info3.setText("草稿");	  
			}else if (trim.equals("11")) {
				jckh_info3.setText("待签收");	
			}else if (trim.equals("12")) {
				jckh_info3.setText("待审批");	
			}else if (trim.equals("13")) {
				jckh_info3.setText("审批中");	
			}else if (trim.equals("14")) {
				jckh_info3.setText("审批驳回");	
			}else if (trim.equals("15")) {
				jckh_info3.setText("待执行");	
			}else if (trim.equals("16")) {
				jckh_info3.setText("执法中");	
			}else if (trim.equals("17")) {
				jckh_info3.setText("待反馈");	
			}else if (trim.equals("18")) {
				jckh_info3.setText("反馈处理中");	
			}else if (trim.equals("19")) {
				jckh_info3.setText("反馈驳回");	
			}else if (trim.equals("20")) {
				jckh_info3.setText("待归档");	
			}else if (trim.equals("21")) {
				jckh_info3.setText("暂时挂起");	
			}else if (trim.equals("22")) {
				jckh_info3.setText("待上报");	
			}else if (trim.equals("23")) {
				jckh_info3.setText("已办结");	
			}
			
			//发布人
			TextView jckh_info4 = (TextView) convertView.findViewById(R.id.jckh_info4);
			jckh_info4.setText(map.get("letaskpublisher")+"");
			
			return convertView;
		}
		
	}
}
