package com.jy.environment.mvpview;

import java.util.List;

import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;

public interface ITimeDataView {
	
	void showLoadDialog();
	void dissLoadDialog();
	void onSuccess(RealTimeBean bean,List<CitymeanBean> result);
	void onError(Exception error);
	void onEmpty();
}
