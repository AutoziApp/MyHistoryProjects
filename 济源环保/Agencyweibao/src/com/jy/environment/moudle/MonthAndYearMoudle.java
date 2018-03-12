package com.jy.environment.moudle;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import com.google.gson.Gson;
import com.jy.environment.model.MonthYearTimeBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean;
import com.jy.environment.model.MonthYearTimeBean.DetailBean.CityAirBean;
import com.jy.environment.presenter.IMonthYearPresenter;
import com.jy.environment.util.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class MonthAndYearMoudle {
	private IMonthYearPresenter iMonthYearPresenter;
	private Gson gosn;

	public MonthAndYearMoudle(IMonthYearPresenter iMonthYearPresenter) {
		this.iMonthYearPresenter = iMonthYearPresenter;
		gosn = new Gson();
	}

	public void requestMonthAndYearData(String url) {
		HttpUtils.getRealTimeData(url, new MonthAndYearCallback());

	}

	class MonthAndYearCallback extends StringCallback {
		@Override
		public void onBefore(Request request, int id) {
			// TODO Auto-generated method stub
			super.onBefore(request, id);
			iMonthYearPresenter.showLoadDialog();
		}

		@Override
		public void onError(Call arg0, Exception error, int arg2) {
			iMonthYearPresenter.disLoadDialog();
			iMonthYearPresenter.onEmpty();
		}
		@Override
		public void onResponse(String result, int arg1) {
			MonthYearTimeBean monthYearTimeBean = gosn.fromJson(result,
					MonthYearTimeBean.class);

			if ("true".equals(monthYearTimeBean.getFlag())
					&& "成功".equals(monthYearTimeBean.getMsg())) {
				iMonthYearPresenter.disLoadDialog();
				DetailBean detail = monthYearTimeBean.getDetail();
				List<CityAirBean> municipalities = detail.getMunicipalities();
				CityAirBean citymean = detail.getCitymean();
				List<CityAirBean> straightcounty = detail.getStraightcounty();
				CityAirBean countymean = detail.getCountymean();
				if (municipalities != null || citymean != null
						|| straightcounty != null || countymean != null) {
					if(citymean!=null)
					municipalities.add(citymean);
					if(straightcounty!=null)
					municipalities.addAll(straightcounty);
					if(countymean!=null)
					municipalities.add(countymean);
					iMonthYearPresenter.onSuccess(monthYearTimeBean, municipalities);
				} else {
					iMonthYearPresenter.disLoadDialog();
					iMonthYearPresenter.onEmpty();
				}
			} else {
				iMonthYearPresenter.disLoadDialog();
				iMonthYearPresenter.onEmpty();
			}
		}
	}
}
