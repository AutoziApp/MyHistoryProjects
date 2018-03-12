package com.jy.environment.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.database.model.ModelAlarmHistory;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentWarmHistoryActivity extends ActivityBase implements
		OnClickListener {
	@SuppressWarnings("unused")
	private ListView list;
	private String city = "";
	private ImageView back;
	private TextView warn_history_title;
	private List<ModelAlarmHistory> modelList;
	private Intent intent;
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_warm_history);
		intent = getIntent();
		city = intent.getStringExtra("city");
		modelList = intent.getParcelableArrayListExtra("modelList");
		initView();
	}

	private void initView() {
		// 初始化数据
		// 标题显示
		warn_history_title = (TextView) findViewById(R.id.warn_history_title);
		warn_history_title.setText("预警信息");
		View  view=(View)findViewById(R.id.line);
		view.getBackground().setAlpha(200);
		// 列表
		list = (ListView) findViewById(R.id.environment_warm_list);
		adapter = new ListAdapter();
		adapter.bindData(modelList);
		list.setAdapter(adapter);
		// 返回键
		back = (ImageView) findViewById(R.id.aqi_warn_history_back);
		back.setOnClickListener(this);
	}

	private class ListAdapter extends BaseAdapter {
		List<ModelAlarmHistory> modelList;

		public void bindData(List<ModelAlarmHistory> modelList) {
			this.modelList = modelList;
		}

		@Override
		public int getCount() {
			return modelList.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.environment_warm_history_iterm, null);
				holder.warm_history_iterm = (LinearLayout) convertView
						.findViewById(R.id.warm_history_iterm);
				holder.warm_city_alarm = (TextView) convertView
						.findViewById(R.id.warm_city_alarm);
				holder.warm_city_name = (TextView) convertView
						.findViewById(R.id.warm_city_name);
				holder.warm_city_time = (TextView) convertView
						.findViewById(R.id.warm_city_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ModelAlarmHistory model = modelList.get(position);
			holder.warm_city_name.setText(city+"气象灾害预警");
			holder.warm_city_time.setText(model.getTime());
			holder.warm_city_alarm.setText(model.getAlarm());
			return convertView;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return modelList.get(arg0);
		}
	}

	static class ViewHolder {
		@SuppressWarnings("unused")
		private LinearLayout warm_history_iterm;
		private TextView warm_city_name;
		private TextView warm_city_time;
		private TextView warm_city_alarm;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aqi_warn_history_back:
			finish();
			break;

		default:
			break;
		}
	}
}
