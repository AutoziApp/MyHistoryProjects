package com.jy.environment.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.qqtheme.framework.picker.DatePicker;

import com.jy.environment.R;
import com.jy.environment.adapter.TimeDataAdapter;
import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.mvpview.ITimeDataView;
import com.jy.environment.presenter.TimeDataPresenter;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.webservice.UrlComponent;

public class DayTimeFragment extends RealTimeDayBaseFragment implements
		ITimeDataView, OnClickListener {
	public static final int DAY_TIME = 2;
	private TextView tvStartTime;
	private ListView lvStatistical;
	private ImageView imgNodata;
	private List<CitymeanBean> timeList = new ArrayList<CitymeanBean>();
	private TimeDataAdapter timeDataAdapter;
	private TimeDataPresenter timeDataPresenter;
	Dialog dialog;
	private Context mContext;

	public static DayTimeFragment newInstance() {
		Bundle args = new Bundle();
		// args.putString(cityName,"cityName");
		DayTimeFragment fragment = new DayTimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getLayoutResources() {
		return R.layout.fragment_realtime;
	}

	@Override
	public void initView(View view) {
		mContext = getActivity();
		view.findViewById(R.id.ll_timeSelect_container).setVisibility(
				View.VISIBLE);
		tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
		tvStartTime.setOnClickListener(this);
		timeDataPresenter = new TimeDataPresenter(this);
		dialog = CommonUtil.getCustomeDialog(getActivity(),
				R.style.load_dialog, R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		lvStatistical = (ListView) view.findViewById(R.id.lv_statistical);
		imgNodata = (ImageView) view.findViewById(R.id.img_nodata);
		timeDataAdapter = new TimeDataAdapter(timeList, mContext, DAY_TIME);
		lvStatistical.setAdapter(timeDataAdapter);
	}

	@Override
	public void initData() {
		initDate();
	}

	/**
	 * 初始化时间
	 * 
	 * @author tianfy
	 */
	private void initDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		String defaultStartDate = sdf.format(getYesterday(date)); // 格式化前一天
		tvStartTime.setText(defaultStartDate);
		String url = UrlComponent.getDateTimeValue + defaultStartDate;
		timeDataPresenter.requestTimeData(url);

	}

	// 获取当前时间的前一天
	private Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		date = calendar.getTime(); // 得到前一天的时间
		return date;
	}

	/**
	 * 显示等待框的回调
	 * 
	 * @author tianfy
	 */
	@Override
	public void showLoadDialog() {
		dialog.show();
	}

	/**
	 * 等待框消失的回调
	 * 
	 * @author tianfy
	 */
	@Override
	public void dissLoadDialog() {
		dialog.dismiss();
	}

	/**
	 * 请求成功的回调
	 * 
	 * @author tianfy
	 */
	@Override
	public void onSuccess(RealTimeBean bean, List<CitymeanBean> result) {
		lvStatistical.setVisibility(View.VISIBLE);
		imgNodata.setVisibility(View.GONE);
		if (timeList != null && timeList.size() > 0) {
			timeList.clear();
		}
		timeList.addAll(result);
		timeDataAdapter.notifyDataSetChanged();
	}

	/**
	 * 请求失败的回调
	 * 
	 * @author tianfy
	 */
	@Override
	public void onError(Exception error) {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}

	/**
	 * 请求为空的回调
	 * 
	 * @author tianfy
	 */
	@Override
	public void onEmpty() {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// 每次展示时间选择器前 先获取当天textView中的年、月、日
		String currentTime = tvStartTime.getText().toString();
		int year = Integer.parseInt(currentTime.substring(0, 4));
		int month = Integer.parseInt(currentTime.substring(5, 7));
		int day = Integer.parseInt(currentTime.substring(8));
		onYearMonthDayPicker(year, month, day);
	}

	/**
	 * 弹出时间选择器的方法
	 * 
	 * @author tianfy
	 * @param day2
	 * @param month2
	 * @param year2
	 */
	public void onYearMonthDayPicker(int year, int month, int day) {
		final DatePicker picker = new DatePicker(getActivity());
		picker.setCanceledOnTouchOutside(true);//
		picker.setUseWeight(true);
		picker.setGravity(Gravity.CENTER_VERTICAL);
		picker.setRangeStart(2007, 1, 1);
		picker.setRangeEnd(2027, 1, 1);
		picker.setSelectedItem(year, month, day);
		picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
			@Override
			public void onDatePicked(String year, String month, String day) {
				tvStartTime.setText(year + "-" + month + "-" + day);
				String url = UrlComponent.getDateTimeValue + year + month + day;
				timeDataPresenter.requestTimeData(url);
			}
		});
		picker.show();
	}
}
