package com.mapuni.android.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mapuni.android.base.R;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: QYNameListAdapter
 * 
 * @author 赵瑞青
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-8-5 下午15:48:46
 */
public class QYNameListAdapter extends BaseAdapter {

	public Context context;
	public ArrayList<HashMap<String, Object>> data;
	
	

	public QYNameListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data ) {
		this.context = context;
		this.data = data;
		
	}
	public QYNameListAdapter(Context context,String rwbh) {
		this.context = context;
		String sql="select TaskEnpriLink.*,T_WRY_QYJBXX.qymc from TaskEnpriLink left join " +
				"T_WRY_QYJBXX WHERE TaskEnpriLink.[TaskID]='"+rwbh+"'";
		this.data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	}

	@Override
	public int getCount() {
		if(data.size()==0){
			return 1;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = View.inflate(context, R.layout.qynamelist, null);
		TextView qymc_text = (TextView) view.findViewById(R.id.qymc_text);
		TextView qyzt = (TextView) view.findViewById(R.id.qyzt);
		Button qydel_btn = (Button) view.findViewById(R.id.qydel);

		qydel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		final HashMap<String, Object> map = data.get(position);
		String qymc = map.get("qymc").toString();
		if (qymc.equals("")) {
			qymc_text.setText("暂无该企业名称信息");
		} else {
			qymc_text.setText(qymc);
		}
		qyzt.setText("待执行");
		view.setTag(map.get("qydm"));

		return view;
	}

}
