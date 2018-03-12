package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.bean.PollutionMode;

import java.util.List;

public class PollutionInfoAdapter extends BaseAdapter{
	private List<PollutionMode> data;
	private Context context;
	private LayoutInflater mInflater;
	public PollutionInfoAdapter(Context con,List<PollutionMode>data){
		context = con;
		this.data = data;
		mInflater=LayoutInflater.from(con);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}
	private ViewHolder vh 	 =new ViewHolder();
	private static class ViewHolder {
		private TextView value ;
		private TextView level ;
		private TextView date ;
		private LinearLayout view;
		
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		if(convertView==null){
			convertView = mInflater.inflate(R.layout.item_pollution_info, null);
			vh.date=(TextView)convertView.findViewById(R.id.date);
			vh.value=(TextView)convertView.findViewById(R.id.value);
			vh.level=(TextView)convertView.findViewById(R.id.level);
			vh.view = (LinearLayout) convertView;
//			convertView.setTag(vh);
//		}else{
//			vh=(ViewHolder)convertView.getTag();
//		}
		vh.date.setText(data.get(position).getDate());
		vh.value.setText(data.get(position).getValue());
		vh.level.setText(data.get(position).getLevel());
		
		if (position % 2 > 0) {
			vh.view.setBackgroundResource(R.color.white);
		} else {
			vh.view.setBackgroundResource(R.color.item_blue);
		}
		return convertView;
	}
}