package com.jy.environment.mvpview;

import java.util.List;

import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;



public interface IMonthYearView {
	
	void showLoadDialog();
	void dissLoadDialog();
	void onSuccess(MonthYearTimeBean bean,List<CityAirBean> result);
	void onError(Exception error);
	void onEmpty();
}
