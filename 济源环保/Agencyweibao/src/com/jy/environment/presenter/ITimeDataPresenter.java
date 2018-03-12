package com.jy.environment.presenter;

import java.util.List;

import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;

public interface ITimeDataPresenter {

	void requestTimeData(String url);

	void onSuccess(RealTimeBean realTimeBean, List<CitymeanBean> municipalities);

	void onError(Exception error);

	void onEmpty();

	void showLoadDialog();

	void disLoadDialog();

}
