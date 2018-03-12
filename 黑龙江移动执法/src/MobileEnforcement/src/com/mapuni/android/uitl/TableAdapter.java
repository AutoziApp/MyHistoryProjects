package com.mapuni.android.uitl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;

public class TableAdapter extends BaseAdapter {
	private Context context;
	List<Map<String, Object>> CompanyAdapterData;
	DecimalFormat df2 = new DecimalFormat("###.0");
	int sum = 0;

	public TableAdapter(Context context, List<Map<String, Object>> listData) {
		this.context = context;
		this.CompanyAdapterData = listData;
	}

	@Override
	public int getCount() {
		return CompanyAdapterData.size();
	}

	public void updateData(List<Map<String, Object>> taskData) {
		this.CompanyAdapterData = taskData;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return CompanyAdapterData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.listadapter_main, null);

			holder = new ViewHolder();
			holder.textView0 = (TextView) convertView.findViewById(R.id.text0);
			holder.textView1 = (TextView) convertView.findViewById(R.id.text1);
			holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
			holder.textView3 = (TextView) convertView.findViewById(R.id.text3);
			holder.box = (CheckBox) convertView.findViewById(R.id.chekbox_01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView0.setText("" + (position + 1));
		holder.textView1.setText(CompanyAdapterData.get(position).get("temp7").toString());
//		holder.textView2.setText("" + df2.format(Double.parseDouble(CompanyAdapterData.get(position).get("temp8").toString())));
//		holder.textView3.setText("" + df2.format(Double.parseDouble(CompanyAdapterData.get(position).get("temp9").toString())));
		holder.textView2.setText(CompanyAdapterData.get(position).get("temp8").toString());
		holder.textView3.setText(CompanyAdapterData.get(position).get("temp9").toString());

		holder.textView0.setTextColor(Color.BLACK);
		holder.textView1.setTextColor(Color.BLACK);
		holder.textView2.setTextColor(Color.BLACK);
		holder.textView3.setTextColor(Color.BLACK);
			
		return convertView;
	}

	public class ViewHolder {
		TextView textView0, textView1, textView2, textView3;
		CheckBox box;
	}

}