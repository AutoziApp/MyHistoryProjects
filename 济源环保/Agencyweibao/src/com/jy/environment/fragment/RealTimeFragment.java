package com.jy.environment.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.adapter.TimeDataAdapter;
import com.jy.environment.model.RealTimeBean;
import com.jy.environment.model.RealTimeBean.DetailBean.CitymeanBean;
import com.jy.environment.mvpview.ITimeDataView;
import com.jy.environment.presenter.TimeDataPresenter;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.webservice.UrlComponent;

/**
 * @ClassName: RealTimeFragment
 * @Description: 实时数据fragment
 * @author tianfy
 * @date 2017-11-30 上午10:58:49
 */
public class RealTimeFragment extends RealTimeDayBaseFragment implements
		ITimeDataView {

	private TextView tvUpdateTime;
	private ListView lvStatistical;
	private ImageView imgNodata;
	private List<CitymeanBean> timeList = new ArrayList<CitymeanBean>();
	private TimeDataAdapter timeDataAdapter;
	private Context mContext;
	private TimeDataPresenter timeDataPresenter;
	Dialog dialog;
	public static final int REAL_TIME=1;
	public static RealTimeFragment newInstance() {
		Bundle args = new Bundle();
		// args.putString(cityName,"cityName");
		RealTimeFragment fragment = new RealTimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getLayoutResources() {
		return R.layout.fragment_realtime;
	}

	public void initView(View view) {
		mContext = getActivity();
		//控制类presenter
		timeDataPresenter = new TimeDataPresenter(this);
		dialog = CommonUtil.getCustomeDialog(getActivity(), R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		tvUpdateTime = (TextView) view.findViewById(R.id.tv_updateTime);
		lvStatistical = (ListView) view.findViewById(R.id.lv_statistical);
		imgNodata = (ImageView) view.findViewById(R.id.img_nodata);
		view.findViewById(R.id.zhuangkuang).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ll_updateTime_container).setVisibility(View.VISIBLE);
		//设置数据适配器
		timeDataAdapter = new TimeDataAdapter(timeList, mContext,REAL_TIME);
		lvStatistical.setAdapter(timeDataAdapter);
	}

	public void initData() {
		String realTimeUrl = UrlComponent.getRealTimeValue;
		//请求网络
		timeDataPresenter.requestTimeData(realTimeUrl);
	}
	/**
	 * 请求失败的回调
	 * @author tianfy
	 */
	@Override
	public void onError(Exception error) {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}
	/**
	 * 请求成功的回调
	 * @author tianfy
	 */
	@Override
	public void onSuccess(RealTimeBean bean, List<CitymeanBean> result) {
		lvStatistical.setVisibility(View.VISIBLE);
		imgNodata.setVisibility(View.GONE);
		tvUpdateTime.setText(bean.getUpdatetime());
		timeList.addAll(result);
		timeDataAdapter.notifyDataSetChanged();
	}
	/**
	 * 展示等待框回调
	 * @author tianfy
	 */
	@Override
	public void showLoadDialog() {
		dialog.show();
	}
	/**
	 * 等待框消失回调
	 * @author tianfy
	 */
	@Override
	public void dissLoadDialog() {
		dialog.dismiss();
	}
	/**
	 * 数据为空的回调
	 * @author tianfy
	 */
	@Override
	public void onEmpty() {
		lvStatistical.setVisibility(View.GONE);
		imgNodata.setVisibility(View.VISIBLE);
	}
}
