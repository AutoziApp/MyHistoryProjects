package com.jy.environment.presenter;

import java.util.List;

import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;
import com.jy.environment.moudle.MonthAndYearMoudle;
import com.jy.environment.mvpview.IMonthYearView;

public class MonthYearPresenter implements IMonthYearPresenter{

	private IMonthYearView iMonthYearView;
	private MonthAndYearMoudle monthAndYearMoudle;
	public MonthYearPresenter(IMonthYearView iMonthYearView) {
		this.iMonthYearView=iMonthYearView;
		monthAndYearMoudle = new MonthAndYearMoudle(this);
	}

	@Override
	public void requestTimeData(String url) {
		monthAndYearMoudle.requestMonthAndYearData(url);
	}

	@Override
	public void onSuccess(MonthYearTimeBean monthYearTimeBean,
			List<CityAirBean> municipalities) {
		iMonthYearView.onSuccess(monthYearTimeBean, municipalities);
	}

	@Override
	public void onError(Exception error) {
		iMonthYearView.onError(error);
	}

	@Override
	public void onEmpty() {
		iMonthYearView.onEmpty();
	}

	@Override
	public void showLoadDialog() {
		iMonthYearView.showLoadDialog();
	}

	@Override
	public void disLoadDialog() {
		iMonthYearView.dissLoadDialog();
	}

}
