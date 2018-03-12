package com.jy.environment.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.fragment.DayTimeFragment;
import com.jy.environment.fragment.RealTimeFragment;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.util.CommonUtil;

public 	class TimeDataAdapter extends BaseAdapter {

	private List<CitymeanBean> timeLists;
	private Context mContext;
	private int type;
	public TimeDataAdapter(List<CitymeanBean> timeList, Context mContext, int type) {
		this.timeLists = timeList;
		this.mContext=mContext;
		this.type=type;
	}

	@Override
	public int getCount() {
		return timeLists.size();
	}

	@Override
	public Object getItem(int position) {
		return timeLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CitymeanBean model = timeLists.get(position);
		String city = model.getCITYNAME();
		ViewHolder holder = null;
		if (convertView==null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.statistical_item, parent, false);
			holder = new ViewHolder();
			holder.tvCityName = (TextView) convertView.findViewById(R.id.tv_cityName);
			holder.tvPm10 = (TextView) convertView.findViewById(R.id.tv_pm10);
			holder.tvPm25 = (TextView) convertView.findViewById(R.id.tv_pm25);
			holder.tvAqi = (TextView) convertView.findViewById(R.id.tv_aqi);
			holder.tvFirstPolluction = (TextView) convertView.findViewById(R.id.tv_firstPolluction);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			holder.llStatusContainer = (LinearLayout) convertView.findViewById(R.id.ll_status_container);
			holder.item = (LinearLayout) convertView.findViewById(R.id.item);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if ("市均值".equals(city)||"县均值".equals(city)){
				holder.item.setBackgroundColor(Color.parseColor("#27b3ae"));
			}else{
				holder.item.setBackgroundResource(android.R.color.transparent);
			}

			holder.tvCityName.setText(model.getCITYNAME());
			String pm10 = (model.getPM10().equals("0") || model.getPM10()
					.equals("-1")) ? "-" : model.getPM10() + "";
			String pm25 = (model.getPM25().equals("0") || model.getPM25()
					.equals("-1")) ? "-" : model.getPM25() + "";
			String aqi = (model.getAQI().equals("0") || model.getAQI()
					.equals("-1")) ? "-" : model.getAQI() + "";
			holder.tvPm10.setText(pm10);
			holder.tvPm25.setText(pm25);
			holder.tvAqi.setText(aqi);
			
			//判断是否是实时fragment
			if (type==RealTimeFragment.REAL_TIME) {
				holder.llStatusContainer.setVisibility(View.VISIBLE);
			}else{
				holder.llStatusContainer.setVisibility(View.GONE);
				//控制日报界面市均值、县均值下pm10/pm25的背景
				if (!((city.equals("市均值") || city.equals("县均值")))) {
					holder.tvPm10.setBackgroundColor(CommonUtil
							.getColorByPM10(pm10 + ""));
					holder.tvPm25.setBackgroundColor(CommonUtil
							.getColorByPM25(pm25 + ""));
				}else{
					holder.tvPm10.setBackgroundResource(android.R.color.transparent);
					holder.tvPm25.setBackgroundResource(android.R.color.transparent);
				}
			}
			
			try {
				if (!aqi.equals("-")) {
					holder.tvStatus.setBackgroundResource(CommonUtil
							.getRIdByAQI(model.getAQI() + ""));
				}
			} catch (Exception e) {
			}
			String PRIMARYPOLLUTAN = model.getPRIMARYPOLLUTANT();
			if (PRIMARYPOLLUTAN != null) {
				if (PRIMARYPOLLUTAN.contains("臭氧")) {
					PRIMARYPOLLUTAN = "O3";
				} else if (PRIMARYPOLLUTAN.contains("一氧化碳")) {
					PRIMARYPOLLUTAN = "CO";
				} else if (PRIMARYPOLLUTAN.contains("二氧化硫")) {
					PRIMARYPOLLUTAN = "SO2";
				} else if (PRIMARYPOLLUTAN.contains("二氧化氮")) {
					PRIMARYPOLLUTAN = "NO2";
				}
				holder.tvFirstPolluction.setText(PRIMARYPOLLUTAN);
			} else {
				holder.tvFirstPolluction.setText("--");
			}
			String aqilevel = CommonUtil.getDengJiByAQI(aqi);
			try {
				if (aqi.equals("-")) {
					holder.tvStatus.setText("--");
				} else {
					holder.tvStatus.setText(aqilevel);
				}
			} catch (Exception e) {
			}
		} catch (Exception e) {
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tvCityName;
		private TextView tvPm10;
		private TextView tvPm25;
		private TextView tvAqi;
		private TextView tvFirstPolluction;
		private TextView tvStatus;
		private LinearLayout llStatusContainer;
		private LinearLayout item;
	}

}