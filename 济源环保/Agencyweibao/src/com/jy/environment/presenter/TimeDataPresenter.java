package com.jy.environment.presenter;

import java.util.List;

import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.moudle.MonthAndYearMoudle;
import com.jy.environment.moudle.TimeDataMoudle;
import com.jy.environment.mvpview.ITimeDataView;

/**
 * 
 * @ClassName: TimeDataPresenter 
 * @Description: presenter控制类
 * @author tianfy
 * @date 2017-11-30 下午5:01:48
 */
public class TimeDataPresenter implements ITimeDataPresenter{
	
	private TimeDataMoudle timeDataMoudle;
	private MonthAndYearMoudle monthAndYearMoudle;
	private ITimeDataView iTimeDataView;
	public TimeDataPresenter(ITimeDataView iTimeDataView) {
		this.iTimeDataView=iTimeDataView;
		timeDataMoudle = new TimeDataMoudle(this);
	}
	//请求网络
	@Override
	public void requestTimeData(String url) {
		timeDataMoudle.requestTimeData(url);
	}
	@Override
	public void onSuccess(RealTimeBean realTimeBean,
			List<CitymeanBean> municipalities) {
		iTimeDataView.onSuccess(realTimeBean, municipalities);
	}
	@Override
	public void onError(Exception error) {
		iTimeDataView.onError(error);
	}

	@Override
	public void onEmpty() {
		iTimeDataView.onEmpty();
	}
	@Override
	public void showLoadDialog() {
		iTimeDataView.showLoadDialog();
	}
	@Override
	public void disLoadDialog() {
		iTimeDataView.dissLoadDialog();
	}

	
}
