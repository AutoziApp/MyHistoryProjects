package com.jy.environment.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;

import com.jy.environment.R;
import com.jy.environment.adapter.MonthYearAdapter;
import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.mvpview.IMonthYearView;
import com.jy.environment.presenter.MonthYearPresenter;
import com.jy.environment.presenter.TimeDataPresenter;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.webservice.UrlComponent;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MonthTimeFragment extends RealTimeDayBaseFragment implements OnClickListener ,IMonthYearView{
	
	private TextView tvStartTime;
	private ListView lvStatistical;
	private ImageView imgNodata;
	Dialog dialog;
	private MonthYearPresenter monthYearPresenter;
	private List<CityAirBean> listAirBeans=new ArrayList<CityAirBean>();
	private MonthYearAdapter monthYearAdapter;

	public static MonthTimeFragment newInstance() {
	    Bundle args = new Bundle();
//	    args.putString(cityName,"cityName");
	    MonthTimeFragment fragment = new MonthTimeFragment();
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
		tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
		tvStartTime.setOnClickListener(this);
		lvStatistical = (ListView) view.findViewById(R.id.lv_statistical);
		imgNodata = (ImageView) view.findViewById(R.id.img_nodata);
		monthYearAdapter = new MonthYearAdapter(listAirBeans,getActivity());
		lvStatistical.setAdapter(monthYearAdapter);
	}


	@Override
	public void initData() {
		monthYearPresenter = new MonthYearPresenter(this);

		initDate();//初始化日期
	}
	/**
	 * 初始化日期
	 * @author tianfy
	 */
	private void initDate() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");    
		String date=sdf.format(new Date()); 
		tvStartTime.setText(date);
		String url=UrlComponent.getMonthDataUrl;
		
		String year =date.substring(0, 4); 
		String month=date.substring(5);
		
		monthYearPresenter.requestTimeData(url+year+month);
	}

	@Override
	public void onClick(View v) {
		String date= tvStartTime.getText().toString();
		int year =Integer.parseInt(date.substring(0, 4)); 
		int month=Integer.parseInt(date.substring(5));
		
		onYearMonthPicker(year, month);
	}
	
    public void onYearMonthPicker(int year,int month) {
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH);
        picker.setGravity( Gravity.CENTER_VERTICAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setRangeStart(2010, 1, 1);
        picker.setRangeEnd(2027, 1, 1);
        picker.setSelectedItem(year, month);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
            	tvStartTime.setText(year+"-"+month);
            	String url=UrlComponent.getMonthDataUrl;
            	monthYearPresenter.requestTimeData(url+year+month);
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
	public void onError(Exception error) {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}

	@Override
	public void onEmpty() {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
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
}
