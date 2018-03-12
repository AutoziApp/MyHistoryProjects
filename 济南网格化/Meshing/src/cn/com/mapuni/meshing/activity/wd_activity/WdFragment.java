package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.mapuni.meshing.adapter.WdWgyAdapter;

import com.example.meshing.R;
import com.mapuni.android.base.util.DisplayUitl;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WdFragment extends Fragment implements OnClickListener{
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	private TextView tv_user_name;
	private TextView tv_organization_name;
	GridView wgy_grid;
	WdWgyAdapter wdWgyAdapter;
	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	RelativeLayout layout_record,layout_feadback,layout_plan,layout_search;
	private String userName, organizationName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View wdLayout = inflater.inflate(R.layout.wdmainactivity_layout,
				container, false);
		userName = DisplayUitl.readPreferences(getActivity(), LAST_USER_SP_NAME, "user_Name");
		organizationName = DisplayUitl.readPreferences(getActivity(), LAST_USER_SP_NAME, "organization_name");
		tv_user_name = (TextView) wdLayout.findViewById(R.id.tv_user_name);
		tv_user_name.setText(userName);
		tv_organization_name = (TextView) wdLayout.findViewById(R.id.tv_organization_name);
		tv_organization_name.setText(organizationName);
		if (userName.equals(organizationName)) {
			tv_user_name.setVisibility(View.INVISIBLE);
			
		}
		layout_record = (RelativeLayout) wdLayout.findViewById(R.id.layout_record);
		layout_search = (RelativeLayout) wdLayout.findViewById(R.id.layout_search);
		layout_feadback = (RelativeLayout) wdLayout.findViewById(R.id.layout_feadback);
		layout_feadback.setVisibility(View.GONE);
		layout_plan = (RelativeLayout) wdLayout.findViewById(R.id.layout_plan);
		layout_record.setOnClickListener(this);
		layout_feadback.setOnClickListener(this);
		layout_plan.setOnClickListener(this);
		layout_search.setOnClickListener(this);

		wgy_grid = (GridView) wdLayout.findViewById(R.id.wgy);
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
		wdWgyAdapter = new WdWgyAdapter(getActivity(), data);
		wgy_grid.setAdapter(wdWgyAdapter);

		wgy_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				wdWgyAdapter.setSeclection(arg2);
				wdWgyAdapter.notifyDataSetChanged();
				Intent intent;
				if (arg2 == 0) {// 任务信息
					intent = new Intent(getActivity(), RwxxSlideActivity.class);
				} else {// 污染源信息
					intent = new Intent(getActivity(), WryxxActivity.class);
				}
				startActivity(intent);
			}

		});
		//新增非四级巡查员权限直接屏蔽
		if (DisplayUitl.readPreferences(
				getActivity(), LAST_USER_SP_NAME, "haveInspectorRole").equals("1")) {
			layout_plan.setVisibility(View.VISIBLE);
		}
		return wdLayout;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (wdWgyAdapter != null) {
			wdWgyAdapter.setSeclection(-1);
			wdWgyAdapter.notifyDataSetChanged();
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
		case R.id.layout_record:
			intent = new Intent(getActivity(), RwxxXcjlActivity.class);
			intent.putExtra("type", "xcjl");
			break;

		case R.id.layout_feadback:
			intent = new Intent(getActivity(), RwxxCzfkActivity.class);
			intent.putExtra("type", "czfk");
			break;

		case R.id.layout_plan:
			 intent = new Intent(getActivity(),XCPlanListActivity.class);
			break;
		case R.id.layout_search:
			 intent = new Intent(getActivity(),DocumentSearchActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

}
