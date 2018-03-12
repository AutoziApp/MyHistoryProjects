package cn.com.mapuni.meshing.activity.wd_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import cn.com.mapuni.meshing.activity.db_activity.DbscfkActivity;
import cn.com.mapuni.meshing.adapter.WdWgyAdapter;
import cn.com.mapuni.meshing.model.DbTaskList;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.util.DisplayUitl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WdMainActivity extends Activity implements OnClickListener {
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	private TextView tv_user_name;
	private TextView tv_organization_name;
	GridView wgy_grid;
	WdWgyAdapter wdWgyAdapter;
	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	RelativeLayout xcjl, czfk, xtgl;
	private String userName, organizationName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdmainactivity_layout);
		userName = DisplayUitl.readPreferences(WdMainActivity.this, LAST_USER_SP_NAME, "user_Name");
		organizationName = DisplayUitl.readPreferences(WdMainActivity.this, LAST_USER_SP_NAME, "organization_name");
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_name.setText(userName);
		tv_organization_name = (TextView) findViewById(R.id.tv_organization_name);
		tv_organization_name.setText(organizationName);
		if (userName.equals(organizationName)) {
			tv_user_name.setVisibility(View.INVISIBLE);
			
		}
		xcjl = (RelativeLayout) findViewById(R.id.xcjl);
		czfk = (RelativeLayout) findViewById(R.id.czfk);
		czfk.setVisibility(View.GONE);
		xtgl = (RelativeLayout) findViewById(R.id.xtgl);
		xcjl.setOnClickListener(this);
		czfk.setOnClickListener(this);
		xtgl.setOnClickListener(this);

		wgy_grid = (GridView) findViewById(R.id.wgy);
		wgy_grid.setVisibility(View.GONE);
		wgy_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		for (int i = 0; i < 2; i++) {
			HashMap<String, String> arr = new HashMap<String, String>();
			if (i == 0) {
				arr.put("wd_text1", "任务信息");
				arr.put("wd_text2", "展现所有上报的任务");
			} else {
				arr.put("wd_text1", "污染源信息");
				arr.put("wd_text2", "辖区内所有污染源");
			}
			data.add(arr);
		}
		wdWgyAdapter = new WdWgyAdapter(WdMainActivity.this, data);
		wgy_grid.setAdapter(wdWgyAdapter);

		wgy_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				wdWgyAdapter.setSeclection(arg2);
				wdWgyAdapter.notifyDataSetChanged();
				Intent intent;
				if (arg2 == 0) {// 任务信息
					intent = new Intent(WdMainActivity.this, RwxxSlideActivity.class);
				} else {// 污染源信息
					intent = new Intent(WdMainActivity.this, WryxxActivity.class);
				}
				startActivity(intent);
			}

		});
		//新增非四级巡查员权限直接屏蔽
		if (DisplayUitl.readPreferences(
				WdMainActivity.this, LAST_USER_SP_NAME, "haveInspectorRole").equals("1")) {
			xtgl.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.xcjl:
			intent = new Intent(WdMainActivity.this, RwxxXcjlActivity.class);
			intent.putExtra("type", "xcjl");
			break;

		case R.id.czfk:
			intent = new Intent(WdMainActivity.this, RwxxCzfkActivity.class);
			intent.putExtra("type", "czfk");
			break;

		case R.id.xtgl:
//			intent = new Intent(WdMainActivity.this, WryxxActivity.class);
			 intent = new Intent(WdMainActivity.this,XCPlanListActivity.class);
			break;

		default:
			break;
		}
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (wdWgyAdapter != null) {
			wdWgyAdapter.setSeclection(-1);
			wdWgyAdapter.notifyDataSetChanged();
		}
	}
}
