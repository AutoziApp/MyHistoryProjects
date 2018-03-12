package com.jy.environment.moudle;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import com.google.gson.Gson;
import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.presenter.ITimeDataPresenter;
import com.jy.environment.util.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
/**
 * 
 * @ClassName: TimeDataMoudle 
 * @Description: moudle类 请求网络解析数据操作
 * @author tianfy
 * @date 2017-11-30 下午5:01:11
 */
public class TimeDataMoudle {

	private ITimeDataPresenter iTimeDataPresenter;
	private Gson gson;

	public TimeDataMoudle(ITimeDataPresenter iTimeDataPresenter) {
		this.iTimeDataPresenter = iTimeDataPresenter;
		gson = new Gson();
	}

	public void requestTimeData(String url) {
		HttpUtils.getRealTimeData(url, new TimeDataCallback());
	}

	class TimeDataCallback extends StringCallback {

		@Override
		public void onBefore(Request request, int id) {
			super.onBefore(request, id);
			iTimeDataPresenter.showLoadDialog();
		}

		@Override
		public void onError(Call arg0, Exception error, int arg2) {
			iTimeDataPresenter.disLoadDialog();
			iTimeDataPresenter.onError(error);
		}

		@Override
		public void onResponse(String result, int arg1) {
			RealTimeBean realTimeBean = gson.fromJson(result,
					RealTimeBean.class);
			if ("true".equals(realTimeBean.getFlag())
					&& "成功".equals(realTimeBean.getMsg())) {
				iTimeDataPresenter.disLoadDialog();
				DetailBean detail = realTimeBean.getDetail();
				List<CitymeanBean> municipalities = detail.getMunicipalities();
				CitymeanBean citymean = detail.getCitymean();
				List<CitymeanBean> straightcounty = detail.getStraightcounty();
				CitymeanBean countymean = detail.getCountymean();
				if (municipalities != null) {
					if (citymean!=null) {
						municipalities.add(citymean);						
					}
					if (straightcounty!=null) {
						municipalities.addAll(straightcounty);						
					}
					if (countymean!=null) {
						municipalities.add(countymean);						
					}
					iTimeDataPresenter.onSuccess(realTimeBean, municipalities);
				} else {
					iTimeDataPresenter.disLoadDialog();
					iTimeDataPresenter.onEmpty();
				}
			} else {
				iTimeDataPresenter.disLoadDialog();
				iTimeDataPresenter.onEmpty();
			}
		}
	}

}
