package com.jy.environment.presenter;

import java.util.List;

import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;

public interface IMonthYearPresenter {
	void requestTimeData(String url);

	void onSuccess(MonthYearTimeBean monthYearTimeBean, List<CityAirBean> municipalities);

	void onError(Exception error);

	void onEmpty();

	void showLoadDialog();

	void disLoadDialog();
}
