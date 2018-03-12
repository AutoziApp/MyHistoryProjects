package com.mapuni.android.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.infoQuery.LNFLFGExpandListActivity;

public class HelpActivity extends BaseActivity {
	GridView gridView;
	ArrayList<HashMap<String, Object>> Itemmap;
	SimpleAdapter adapter;
	LinearLayout middleLayout;
	RelativeLayout SetBaseStyle;
	LayoutInflater inflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "百事通");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.helper_gridview, null);
		middleLayout.addView(layout);
		gridView = (GridView) layout.findViewById(R.id.gridview_01);
		Itemmap = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemdraw", R.drawable.grid_whp_a01);
		map.put("itemname", "危化品");
		Itemmap.add(map);
		map = new HashMap<String, Object>();
		map.put("itemdraw", R.drawable.grid_zjk_a01);
		map.put("itemname", "专家库");
		Itemmap.add(map);
		map = new HashMap<String, Object>();
		map.put("itemdraw", R.drawable.grid_yuan_a01);
		map.put("itemname", "应急预案");
		Itemmap.add(map);
		map = new HashMap<String, Object>();
		map.put("itemdraw", R.drawable.grid_jywz_a01);
		map.put("itemname", "环保手册");
		Itemmap.add(map);
		adapter = new SimpleAdapter(this, Itemmap, R.layout.helper_gridview_list_view, new String[] { "itemdraw", "itemname" }, new int[] { R.id.helper_main_imageView_01,
				R.id.helper_main_textView_02 });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				HashMap<String, Object> filterMap;
				switch (arg2) {
				case 0:
					Log.i("info", "危化品");
					WHPXX whpxx = null;

					whpxx = new WHPXX();

					bundle.putSerializable("BusinessObj", whpxx);
					bundle.putBoolean("IsShowTitle", true);
					filterMap = new HashMap<String, Object>();
					filterMap.put("cym", "");
					whpxx.setFilter(filterMap);
					intent = new Intent(HelpActivity.this, QueryListActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case 1:
					Log.i("info", "专家");
					ZJKXX zjkxx = null;

					zjkxx = new ZJKXX();

					bundle.putSerializable("BusinessObj", zjkxx);
					bundle.putBoolean("IsShowTitle", true);
					filterMap = new HashMap<String, Object>();
					filterMap.put("cym", "");
					zjkxx.setFilter(filterMap);
					intent = new Intent(HelpActivity.this, QueryListActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case 2:
					Log.i("info", "案例");
					YAJALXX yajalxx = null;

					yajalxx = new YAJALXX();

					bundle.putSerializable("BusinessObj", yajalxx);
					bundle.putBoolean("IsShowTitle", true);
					filterMap = new HashMap<String, Object>();
					filterMap.put("cym", "");
					yajalxx.setFilter(filterMap);
					intent = new Intent(HelpActivity.this, QueryListActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent();
					intent = new Intent(HelpActivity.this, LNFLFGExpandListActivity.class);
					startActivity(intent);
					break;
				}

			}
		});

	}
}
