package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.model.BiaoGeModel;
import com.jy.environment.model.GradeModel;
import com.jy.environment.util.MyLog;

public class SettingCaifuActivity extends ActivityBase implements
		OnClickListener {
	private TextView caifu_tv1;
	private ListView caifu_lv1;
	private List<BiaoGeModel> bModels;
	private MyAdapter adapter;
	private ImageView caifu_back;
	private GradeModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.biaoge_weibaoi);
		bModels = new ArrayList<BiaoGeModel>();
		model = getIntent().getParcelableExtra("model");
		try {
			init();
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
	}

	private void init() {
		caifu_back = (ImageView) findViewById(R.id.caifu_back);
		caifu_tv1 = (TextView) findViewById(R.id.caifu_tv1);
		caifu_tv1.setText(model.getCoin());
		caifu_lv1 = (ListView) findViewById(R.id.caifu_lv1);
		caifu_back.setOnClickListener(this);
		BiaoGeModel model = new BiaoGeModel();
		model.setGrade("方式");
		model.setMoney("奖励");
		model.setAward("限制");
		bModels.add(model);
		for (int i = 0; i < 10; i++) {
			BiaoGeModel model2 = new BiaoGeModel();
			if (i == 0) {
				model2.setGrade("注册");
				model2.setMoney("10");
				model2.setAward("");
			} else if (i == 1) {
				model2.setGrade("手机验证");
				model2.setMoney("10");
				model2.setAward("首次");
			} else if (i == 2) {
				model2.setGrade("邮箱注册");
				model2.setMoney("10");
				model2.setAward("首次");
			} else if (i == 3) {
				model2.setGrade("分享");
				model2.setMoney("20");
				model2.setAward("每天前五次");
			} else if (i == 4) {
				model2.setGrade("噪声平均检测");
				model2.setMoney("5");
				model2.setAward("成功上传云端,每天前5次");
			} else if (i == 5) {
				model2.setGrade("发表环境说,一键曝光");
				model2.setMoney("10");
				model2.setAward("审核通过,每天前5次");
			} else if (i == 6) {
				model2.setGrade("评论");
				model2.setMoney("1");
				model2.setAward("审核通过,每天前10次");
			} else if (i == 7) {
				model2.setGrade("短信邀请");
				model2.setMoney("5");
				model2.setAward("每天5人次");
			} else if (i == 8) {
				model2.setGrade("意见反馈");
				model2.setMoney("25");
				model2.setAward("审核通过");
			} else {
				model2.setGrade("签到");
				model2.setMoney("10");
				model2.setAward("");
			}
			bModels.add(model2);
		}
		adapter = new MyAdapter();
		adapter.bindData(bModels);
		caifu_lv1.setAdapter(adapter);

	}

	class MyAdapter extends BaseAdapter {
		private List<BiaoGeModel> bModels;

		void bindData(List<BiaoGeModel> bModels) {
			this.bModels = bModels;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bModels.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				MyLog.i("weibao result:" + bModels.get(position));
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater
							.from(SettingCaifuActivity.this).inflate(
									R.layout.biaoge_item, null);
					holder = new ViewHolder();
					holder.item_tv1 = (TextView) convertView
							.findViewById(R.id.item_tv1);
					holder.item_tv2 = (TextView) convertView
							.findViewById(R.id.item_tv2);
					holder.item_tv3 = (TextView) convertView
							.findViewById(R.id.item_tv3);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 120);
				convertView.setLayoutParams(lp);
				BiaoGeModel model = bModels.get(position);
				holder.item_tv1.setText(model.getGrade());
				holder.item_tv2.setText(model.getMoney());
				holder.item_tv3.setText(model.getAward());
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView item_tv1;
		TextView item_tv2;
		TextView item_tv3;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.caifu_back:
			finish();
			break;

		default:
			break;
		}
	}

}
