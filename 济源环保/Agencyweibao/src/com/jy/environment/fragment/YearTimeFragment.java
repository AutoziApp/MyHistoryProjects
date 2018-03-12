package com.jy.environment.fragment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.qqtheme.framework.picker.DatePicker;

import com.jy.environment.R;
import com.jy.environment.adapter.MonthYearAdapter;
import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;
import com.jy.environment.mvpview.IMonthYearView;
import com.jy.environment.presenter.MonthYearPresenter;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.webservice.UrlComponent;

public class YearTimeFragment extends RealTimeDayBaseFragment implements OnClickListener,IMonthYearView {
	Dialog dialog;
	private TextView tvSearch;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private ListView lvStatistical;
	private ImageView imgNodata;
	private MonthYearPresenter monthYearPresenter;
	private List<CityAirBean> listAirBeans=new ArrayList<CityAirBean>();
	private MonthYearAdapter monthYearAdapter;
	public static YearTimeFragment newInstance() {
	    Bundle args = new Bundle();
//	    args.putString(cityName,"cityName");
	    YearTimeFragment fragment = new YearTimeFragment();
	    fragment.setArguments(args);
	    return fragment;
	}
	@Override
	public int getLayoutResources() {
		return R.layout.fragment_month_year;
	}


	@Override
	public void initView(View view) {
		dialog = CommonUtil.getCustomeDialog(getActivity(),
				R.style.load_dialog, R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		view.findViewById(R.id.ll_timeSelect_container).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ll_endTimeSelect_container).setVisibility(View.VISIBLE);
		tvSearch = (TextView) view.findViewById(R.id.tv_search);
		tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
		tvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
		tvStartTime.setOnClickListener(this);
		tvEndTime.setOnClickListener(this);
		tvSearch.setVisibility(View.VISIBLE);
		tvSearch.setOnClickListener(this);
		lvStatistical = (ListView) view.findViewById(R.id.lv_statistical);
		imgNodata = (ImageView) view.findViewById(R.id.img_nodata);
		monthYearAdapter = new MonthYearAdapter(listAirBeans,getActivity());
		lvStatistical.setAdapter(monthYearAdapter);
	}


	@Override
	public void initData() {
		monthYearPresenter = new MonthYearPresenter(this);

		initDate();
	}
	private void initDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		String yeasterdayStr = sdf.format(getYesterday(date)); // 格式化前一天	
		String year = yeasterdayStr.substring(0, 4);
		String month = yeasterdayStr.substring(5, 7);
		String day = yeasterdayStr.substring(8);
		tvStartTime.setText(year+"-01"+"-01");
		tvEndTime.setText(yeasterdayStr);
		requestData();
		
	}
	
	// 获取当前时间的前一天
	private Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		date = calendar.getTime(); // 得到前一天的时间
		return date;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_startTime:
			String startTime = tvStartTime.getText().toString();
			int year = Integer.parseInt(startTime.substring(0, 4));
			int month = Integer.parseInt(startTime.substring(5, 7));
			int day = Integer.parseInt(startTime.substring(8));
			onYearMonthDayPicker(year, month, day,v);
			break;
		case R.id.tv_endTime:
			String endTime = tvEndTime.getText().toString();
			int yearEnd = Integer.parseInt(endTime.substring(0, 4));
			int monthEnd = Integer.parseInt(endTime.substring(5, 7));
			int dayEnd = Integer.parseInt(endTime.substring(8));
			onYearMonthDayPicker(yearEnd, monthEnd, dayEnd,v);
			break;
		case R.id.tv_search:
			requestData();
			
			break;

		default:
			break;
		}
		
	}
	
	private void requestData() {
		String startTimeStr = tvStartTime.getText().toString();
		String endTimeStr = tvEndTime.getText().toString();
		
		String yearSta = startTimeStr.substring(0, 4);
		String monthSta = startTimeStr.substring(5, 7);
		String daySta = startTimeStr.substring(8);
		
		String begintimeS=yearSta+monthSta+daySta;
		
		String yearEndS= endTimeStr.substring(0, 4);
		String monthEndS = endTimeStr.substring(5, 7);
		String dayEndS = endTimeStr.substring(8);
		
		String endTimeS=yearEndS+monthEndS+dayEndS;
		String url=UrlComponent.getYearTimeData;
		monthYearPresenter.requestTimeData(url+"begintime="+begintimeS+"&endTime="+endTimeS);
	}
	
	/**
	 * 弹出时间选择器的方法
	 * 
	 * @author tianfy
	 * @param v 
	 * @param day2
	 * @param month2
	 * @param year2
	 */
	public void onYearMonthDayPicker(int year, int month, int day, final View v) {
		final DatePicker picker = new DatePicker(getActivity());
		picker.setCanceledOnTouchOutside(true);
		picker.setUseWeight(true);
		picker.setGravity(Gravity.CENTER_VERTICAL);
		picker.setRangeStart(2010, 1, 1);
		picker.setRangeEnd(2027, 1, 1);
		picker.setSelectedItem(year, month, day);
		picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
			@Override
			public void onDatePicked(String year, String month, String day) {
				((TextView)v).setText(year + "-" + month + "-" + day);
			}
		});
		picker.show();
	}
	@Override
	public void showLoadDialog() {
		dialog.show();
	}
	@Override
	public void dissLoadDialog() {
		dialog.dismiss();
	}
	@Override
	public void onSuccess(MonthYearTimeBean bean, List<CityAirBean> result) {
		lvStatistical.setVisibility(View.VISIBLE);
		imgNodata.setVisibility(View.GONE);
		if (listAirBeans!=null&&listAirBeans.size()>0) {
			listAirBeans.clear();
		}
		listAirBeans.addAll(result);
		monthYearAdapter.notifyDataSetChanged();
	}
	@Override
	public void onError(Exception error) {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}
	@Override
	public void onEmpty() {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}

}
