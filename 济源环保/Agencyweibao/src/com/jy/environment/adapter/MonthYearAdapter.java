package com.jy.environment.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;

public class MonthYearAdapter extends BaseAdapter{

	private List<CityAirBean> listAirBeans;
	private Context mContext;
	public MonthYearAdapter(List<CityAirBean> listAirBeans,
			FragmentActivity activity) {
		this.listAirBeans=listAirBeans;
		this.mContext=activity;
	}

	@Override
	public int getCount() {
		return listAirBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return listAirBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.month_year_item, parent, false);
			viewHolder=new ViewHolder();
			viewHolder.tvCityName=(TextView) convertView.findViewById(R.id.tv_cityName);
			viewHolder.tvPm10=(TextView) convertView.findViewById(R.id.tv_pm10);
			viewHolder.tvPm25=(TextView) convertView.findViewById(R.id.tv_pm25);
			viewHolder.tvPm10tb=(TextView) convertView.findViewById(R.id.tv_pm10_tb);
			viewHolder.tvPm25tb=(TextView) convertView.findViewById(R.id.tv_pm25_tb);
			viewHolder.tvDateSum=(TextView) convertView.findViewById(R.id.tv_dateSum);
			viewHolder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CityAirBean cityAirBean = listAirBeans.get(position);
		String cityname = cityAirBean.getCITYNAME();
		if ("市均值".equals(cityname)||"县均值".equals(cityname)) {
			viewHolder.itemLayout.setBackgroundColor(Color.parseColor("#27b3ae"));
		}else{
			viewHolder.itemLayout.setBackgroundResource(android.R.color.transparent);
		}
		viewHolder.tvCityName.setText(cityname);
		viewHolder.tvPm10.setText(cityAirBean.getPM10()+"/");
		viewHolder.tvPm25.setText(cityAirBean.getPM25()+"/");
		viewHolder.tvPm10tb.setText(cityAirBean.getANPM10());
		viewHolder.tvPm25tb.setText(cityAirBean.getANPM25());
		viewHolder.tvDateSum.setText(cityAirBean.getEXCELLENTDAY());
		return convertView;
	}

	class ViewHolder{
		TextView tvCityName;
		TextView tvPm10;
		TextView tvPm25;
		TextView tvPm10tb;
		TextView tvPm25tb;
		TextView tvDateSum;
		LinearLayout itemLayout;
	}
}
