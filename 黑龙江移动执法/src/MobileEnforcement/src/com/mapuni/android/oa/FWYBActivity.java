package com.mapuni.android.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mapuni.android.base.ListActivity;
import com.mapuni.android.base.adapter.ListActivityAdapter;
import com.mapuni.android.base.adapter.ListActivityAdapter.ViewHolder;
import com.mapuni.android.common.DetailedActivity;

public class FWYBActivity extends ListActivity{

	@Override
		public void LoadList(Bundle bundle,
				ArrayList<HashMap<String, Object>> data,
				HashMap<String, Object> style) {
		adapter = new ListActivityAdapter(this, bundle,data, style);
		 
		listView.setAdapter(adapter);


		// 并且在这里实现列表项目的单击响应事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewHolder v = (ViewHolder)view.getTag();
				String idValue = v.id == null ? "" : v.id.getTag().toString();
				String contentValue = v.content == null ? "" : v.content.getText().toString();
				
				//SWYB rwxx=(SWYB) businessObj;
				listItemClick(idValue, contentValue);
			}
		});	
		}
	@Override
	public void listItemClick(String idValue,String contentValue) {
	// 构造 Intent 传送数据, 展开显示详细信息的 Activity
	Intent intent = new Intent();
	Bundle nextBundle=new Bundle();//传递给下个详细页面的Bundle
	businessObj.setCurrentID(idValue);
	nextBundle=bundle;
	intent.putExtras(nextBundle);
	intent.setClass(FWYBActivity.this, DetailedActivity.class);
	startActivity(intent);
	}
	}