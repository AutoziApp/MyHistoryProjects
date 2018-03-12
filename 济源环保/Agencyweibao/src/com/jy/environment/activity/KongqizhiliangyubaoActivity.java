package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.view.LineChart;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.HistogramView;
import com.umeng.analytics.MobclickAgent;

public class KongqizhiliangyubaoActivity extends ActivityBase implements
		OnClickListener {
	// private int[] data = new int[] { 4000, 1000, 1000, 2000, 3000, 5000, 8000
	// };
	// private int[] text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
	private List<Integer> data = new ArrayList<Integer>();
	private List<Integer> text = new ArrayList<Integer>();
	private LineChart linechart;
	private String city;
	private List<String> xWeeks = new ArrayList<String>();
	private TextView yucetv1, yucetv2, yucetv3, yucetv4, yucetv5, yucetv6,
			yucetv7, yucetv8, yucetv9, yucetv10, yucetv11, yucetv12, yucetv13,
			yucetv14, yucetv15, yucetv16, yucetv17, yucetv18, yucetv19,
			yucetv20, yucetv21;
	private LinearLayout yucelinear1, yucelinear2, yucelinear3, yucelinear4,
			yucelinear5, yucelinear6, yucelinear7, scroll_layout;
	private ImageView activity_main_suggest;
	private View fenge1, fenge2, fenge3, fenge4, fenge5, fenge6;
	Dialog dialog;
	private ImageView air_biaoz;
	private TextView air_biaoz2;
	public static int size = 4;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.kongqizhiliangyubao);
		city = getIntent().getStringExtra("city");
		initView();
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		GetThreeDayAqiTrendModel aqiTrendModel = new GetThreeDayAqiTrendModel();
		aqiTrendModel.execute();
	}

	private void initView() {
		// TODO Auto-generated method stub
		yucetv1 = (TextView) findViewById(R.id.yuce_tv1);
		yucetv2 = (TextView) findViewById(R.id.yucetv2);
		yucetv3 = (TextView) findViewById(R.id.yucetv3);
		yucetv4 = (TextView) findViewById(R.id.yucetv4);
		yucetv5 = (TextView) findViewById(R.id.yucetv5);
		yucetv6 = (TextView) findViewById(R.id.yucetv6);
		yucetv7 = (TextView) findViewById(R.id.yucetv7);
		yucetv8 = (TextView) findViewById(R.id.yucetv8);
		yucetv9 = (TextView) findViewById(R.id.yucetv9);
		yucetv10 = (TextView) findViewById(R.id.yucetv10);
		yucetv11 = (TextView) findViewById(R.id.yucetv11);
		yucetv12 = (TextView) findViewById(R.id.yucetv12);
		yucetv13 = (TextView) findViewById(R.id.yucetv13);
		yucetv14 = (TextView) findViewById(R.id.yucetv14);
		yucetv15 = (TextView) findViewById(R.id.yucetv15);
		yucetv16 = (TextView) findViewById(R.id.yucetv16);
		yucetv17 = (TextView) findViewById(R.id.yucetv17);
		yucetv18 = (TextView) findViewById(R.id.yucetv18);
		yucetv19 = (TextView) findViewById(R.id.yucetv19);
		yucetv20 = (TextView) findViewById(R.id.yucetv20);
		yucetv21 = (TextView) findViewById(R.id.yucetv21);
		fenge1 = (View) findViewById(R.id.fenge1);
		fenge2 = (View) findViewById(R.id.fenge2);
		fenge3 = (View) findViewById(R.id.fenge3);
		fenge4 = (View) findViewById(R.id.fenge4);
		fenge5 = (View) findViewById(R.id.fenge5);
		fenge6 = (View) findViewById(R.id.fenge6);
		air_biaoz = (ImageView) findViewById(R.id.air_biaoz);
		air_biaoz2 = (TextView) findViewById(R.id.air_biaoz2);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(this);
		yucelinear1 = (LinearLayout) findViewById(R.id.yucelinear1);
		yucelinear2 = (LinearLayout) findViewById(R.id.yucelinear2);
		yucelinear3 = (LinearLayout) findViewById(R.id.yucelinear3);
		yucelinear4 = (LinearLayout) findViewById(R.id.yucelinear4);
		yucelinear5 = (LinearLayout) findViewById(R.id.yucelinear5);
		yucelinear6 = (LinearLayout) findViewById(R.id.yucelinear6);
		yucelinear7 = (LinearLayout) findViewById(R.id.yucelinear7);
		scroll_layout = (LinearLayout) findViewById(R.id.scroll_layout);
	}

	class GetThreeDayAqiTrendModel extends
			AsyncTask<String, Void, List<ThreeDayAqiTrendModel>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!NetUtil.isNetworkConnected(KongqizhiliangyubaoActivity.this)) {
				ToastUtil.showShort(KongqizhiliangyubaoActivity.this, "无网络");
				try {
					dialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				return;
			}
			if (null != dialog && !dialog.isShowing()) {
				dialog.show();
			}
		}

		List<ThreeDayAqiTrendModel> aqiTrendModels = null;
		private int[] data_line_min;
		private int[] data_line_max;
		private String[] dateValue;

		@Override
		protected List<ThreeDayAqiTrendModel> doInBackground(String... params) {
			// String url = UrlComponent.getThreeDayForest;
			String url = UrlComponent.getThreeDayForest2;
			MyLog.i("ThreeDay" + url);
			String city = WeiBaoApplication.selectedCity;
			BusinessSearch search = new BusinessSearch();
			List<ThreeDayAqiTrendModel> _Result = new ArrayList<ThreeDayAqiTrendModel>();
			try {
				_Result = search.getThreeDayAqiTrendModels(url, city);
				MyLog.i("ThreeDay details:" + _Result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(List<ThreeDayAqiTrendModel> result) {
			try {
				MyLog.i("weibao result:" + result.size());
				super.onPostExecute(result);
				/* dialog.dismiss(); */
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
				}
//				size = result.size() > 7 ? 7 : result.size();
				linechart = (LineChart) findViewById(R.id.linechart);
				scroll_layout.setVisibility(View.VISIBLE);
				air_biaoz.setVisibility(View.VISIBLE);
				air_biaoz2.setVisibility(View.VISIBLE);
				aqiTrendModels = result;
				MyLog.i("detailModels>>>>>"
						+ aqiTrendModels.get(0).getFORECASTTIME());
				initData(result);
				data_line_min=new int[result.size()];
				data_line_max=new int[result.size()];
				dateValue=new String[result.size()];
				for (int i = 0; i < result.size(); i++) {
					ThreeDayAqiTrendModel aqiTrendModel = result.get(i);
					xWeeks.add(aqiTrendModel.getFORECASTTIME());
					data.add(aqiTrendModel.getMINAIRLEVEL());
					data_line_min[i]=aqiTrendModel.getMINAIRLEVEL();
					data_line_max[i]=aqiTrendModel.getMAXAIRLEVEL();
					dateValue[i]=aqiTrendModel.getFORECASTTIME().substring(5, 10);
		
				
					data.add(aqiTrendModel.getMAXAIRLEVEL());
				}
//				for (int i = 0; i < xWeeks.size(); i++) {
//					try {
//						xWeeks.set(i, xWeeks.get(i).substring(0, 10));
//
//					} catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//					}
//					MyLog.i("xWeeks" + xWeeks.get(i).toString());
//				}
				MyLog.i("data" + data.size());
				for (int i = 0; i < data.size(); i++) {
					MyLog.i("data" + data.get(i).toString());
				}
				
				
				linechart.defaultType=LineChart.DAY_WEEK;
				linechart.setXValue(dateValue);
				linechart.setXCount(6, data_line_min.length);
				linechart.setYCount(7, data_line_min.length);
				linechart.setDate(data_line_min,data_line_max);
//				
				
//				histogramView.setWeeks(xWeeks);
//				histogramView.setProgress(data);
			
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.e("weibao Exception", e);
			}
		}
	}

	private void initData(List<ThreeDayAqiTrendModel> result) {
		// TODO Auto-generated method stub
		int min, max;
		if (result.size() == 4) {
			fenge1.setVisibility(View.GONE);
			fenge2.setVisibility(View.GONE);
			fenge3.setVisibility(View.GONE);
			fenge4.setVisibility(View.GONE);
			fenge5.setVisibility(View.GONE);
			fenge6.setVisibility(View.GONE);
			yucetv19.setVisibility(View.GONE);
			yucetv20.setVisibility(View.GONE);
			yucetv21.setVisibility(View.GONE);
			yucelinear5.setVisibility(View.GONE);
			yucelinear6.setVisibility(View.GONE);
			yucelinear7.setVisibility(View.GONE);
			yucetv1.setText(result.get(0).getFORECASTTIME().substring(5, 10));
			yucetv2.setText(result.get(1).getFORECASTTIME().substring(5, 10));
			yucetv3.setText(result.get(2).getFORECASTTIME().substring(5, 10));
			yucetv4.setText(result.get(3).getFORECASTTIME().substring(5, 10));
			min = result.get(0).getMINAIRLEVEL();
			max = result.get(0).getMAXAIRLEVEL();
			yucetv5.setBackgroundResource(getDuValueRes(min));
			yucetv6.setBackgroundResource(getDuValueRes(max));
			yucetv5.setText(getDuValue(min,result.get(0).getMINAIRLEVEL1()));
			yucetv6.setText(getDuValue(max,result.get(0).getMAXAIRLEVEL1()));
			min = result.get(1).getMINAIRLEVEL();
			max = result.get(1).getMAXAIRLEVEL();
			yucetv7.setBackgroundResource(getDuValueRes(min));
			yucetv8.setBackgroundResource(getDuValueRes(max));
			yucetv7.setText(getDuValue(min,result.get(1).getMINAIRLEVEL1()));
			yucetv8.setText(getDuValue(max,result.get(1).getMAXAIRLEVEL1()));
			min = result.get(2).getMINAIRLEVEL();
			max = result.get(2).getMAXAIRLEVEL();
			yucetv9.setBackgroundResource(getDuValueRes(min));
			yucetv10.setBackgroundResource(getDuValueRes(max));
			yucetv9.setText(getDuValue(min,result.get(2).getMINAIRLEVEL1()));
			yucetv10.setText(getDuValue(max,result.get(2).getMAXAIRLEVEL1()));
			min = result.get(3).getMINAIRLEVEL();
			max = result.get(3).getMAXAIRLEVEL();
			yucetv11.setBackgroundResource(getDuValueRes(min));
			yucetv12.setBackgroundResource(getDuValueRes(max));
			yucetv11.setText(getDuValue(min,result.get(3).getMINAIRLEVEL1()));
			yucetv12.setText(getDuValue(max,result.get(3).getMAXAIRLEVEL1()));
		} else if (result.size() == 5) {
			fenge1.setVisibility(View.VISIBLE);
			fenge2.setVisibility(View.GONE);
			fenge3.setVisibility(View.GONE);
			fenge4.setVisibility(View.VISIBLE);
			fenge5.setVisibility(View.GONE);
			fenge6.setVisibility(View.GONE);
			yucetv19.setVisibility(View.VISIBLE);
			yucetv20.setVisibility(View.GONE);
			yucetv21.setVisibility(View.GONE);
			yucelinear5.setVisibility(View.VISIBLE);
			yucelinear6.setVisibility(View.GONE);
			yucelinear7.setVisibility(View.GONE);
			yucetv1.setText(result.get(0).getFORECASTTIME().substring(5, 10));
			yucetv2.setText(result.get(1).getFORECASTTIME().substring(5, 10));
			yucetv3.setText(result.get(2).getFORECASTTIME().substring(5, 10));
			yucetv4.setText(result.get(3).getFORECASTTIME().substring(5, 10));
			yucetv19.setText(result.get(4).getFORECASTTIME().substring(5, 10));
			min = result.get(0).getMINAIRLEVEL();
			max = result.get(0).getMAXAIRLEVEL();
			yucetv5.setBackgroundResource(getDuValueRes(min));
			yucetv6.setBackgroundResource(getDuValueRes(max));
			yucetv5.setText(getDuValue(min,result.get(0).getMINAIRLEVEL1()));
			yucetv6.setText(getDuValue(max,result.get(0).getMAXAIRLEVEL1()));
			min = result.get(1).getMINAIRLEVEL();
			max = result.get(1).getMAXAIRLEVEL();
			yucetv7.setBackgroundResource(getDuValueRes(min));
			yucetv8.setBackgroundResource(getDuValueRes(max));
			yucetv7.setText(getDuValue(min,result.get(1).getMINAIRLEVEL1()));
			yucetv8.setText(getDuValue(max,result.get(1).getMAXAIRLEVEL1()));
			min = result.get(2).getMINAIRLEVEL();
			max = result.get(2).getMAXAIRLEVEL();
			yucetv9.setBackgroundResource(getDuValueRes(min));
			yucetv10.setBackgroundResource(getDuValueRes(max));
			yucetv9.setText(getDuValue(min,result.get(2).getMINAIRLEVEL1()));
			yucetv10.setText(getDuValue(max,result.get(2).getMAXAIRLEVEL1()));
			min = result.get(3).getMINAIRLEVEL();
			max = result.get(3).getMAXAIRLEVEL();
			yucetv11.setBackgroundResource(getDuValueRes(min));
			yucetv12.setBackgroundResource(getDuValueRes(max));
			yucetv11.setText(getDuValue(min,result.get(3).getMINAIRLEVEL1()));
			yucetv12.setText(getDuValue(max,result.get(3).getMAXAIRLEVEL1()));
			min = result.get(4).getMINAIRLEVEL();
			max = result.get(4).getMAXAIRLEVEL();
			yucetv13.setBackgroundResource(getDuValueRes(min));
			yucetv14.setBackgroundResource(getDuValueRes(max));
			yucetv13.setText(getDuValue(min,result.get(4).getMINAIRLEVEL1()));
			yucetv14.setText(getDuValue(max,result.get(4).getMAXAIRLEVEL1()));
		} else if (result.size() == 6) {
			fenge1.setVisibility(View.VISIBLE);
			fenge2.setVisibility(View.VISIBLE);
			fenge3.setVisibility(View.GONE);
			fenge4.setVisibility(View.VISIBLE);
			fenge5.setVisibility(View.VISIBLE);
			fenge6.setVisibility(View.GONE);
			yucetv19.setVisibility(View.VISIBLE);
			yucetv20.setVisibility(View.VISIBLE);
			yucetv21.setVisibility(View.GONE);
			yucelinear5.setVisibility(View.VISIBLE);
			yucelinear6.setVisibility(View.VISIBLE);
			yucelinear7.setVisibility(View.GONE);
			yucetv1.setText(result.get(0).getFORECASTTIME().substring(5, 10));
			yucetv2.setText(result.get(1).getFORECASTTIME().substring(5, 10));
			yucetv3.setText(result.get(2).getFORECASTTIME().substring(5, 10));
			yucetv4.setText(result.get(3).getFORECASTTIME().substring(5, 10));
			yucetv19.setText(result.get(4).getFORECASTTIME().substring(5, 10));
			yucetv20.setText(result.get(5).getFORECASTTIME().substring(5, 10));
			min = result.get(0).getMINAIRLEVEL();
			max = result.get(0).getMAXAIRLEVEL();
			yucetv5.setBackgroundResource(getDuValueRes(min));
			yucetv6.setBackgroundResource(getDuValueRes(max));
			yucetv5.setText(getDuValue(min,result.get(0).getMINAIRLEVEL1()));
			yucetv6.setText(getDuValue(max,result.get(0).getMAXAIRLEVEL1()));
			min = result.get(1).getMINAIRLEVEL();
			max = result.get(1).getMAXAIRLEVEL();
			yucetv7.setBackgroundResource(getDuValueRes(min));
			yucetv8.setBackgroundResource(getDuValueRes(max));
			yucetv7.setText(getDuValue(min,result.get(1).getMINAIRLEVEL1()));
			yucetv8.setText(getDuValue(max,result.get(1).getMAXAIRLEVEL1()));
			min = result.get(2).getMINAIRLEVEL();
			max = result.get(2).getMAXAIRLEVEL();
			yucetv9.setBackgroundResource(getDuValueRes(min));
			yucetv10.setBackgroundResource(getDuValueRes(max));
			yucetv9.setText(getDuValue(min,result.get(2).getMINAIRLEVEL1()));
			yucetv10.setText(getDuValue(max,result.get(2).getMAXAIRLEVEL1()));
			min = result.get(3).getMINAIRLEVEL();
			max = result.get(3).getMAXAIRLEVEL();
			yucetv11.setBackgroundResource(getDuValueRes(min));
			yucetv12.setBackgroundResource(getDuValueRes(max));
			yucetv11.setText(getDuValue(min,result.get(3).getMINAIRLEVEL1()));
			yucetv12.setText(getDuValue(max,result.get(3).getMAXAIRLEVEL1()));
			min = result.get(4).getMINAIRLEVEL();
			max = result.get(4).getMAXAIRLEVEL();
			yucetv13.setBackgroundResource(getDuValueRes(min));
			yucetv14.setBackgroundResource(getDuValueRes(max));
			yucetv13.setText(getDuValue(min,result.get(4).getMINAIRLEVEL1()));
			yucetv14.setText(getDuValue(max,result.get(4).getMAXAIRLEVEL1()));
			min = result.get(5).getMINAIRLEVEL();
			max = result.get(5).getMAXAIRLEVEL();
			yucetv15.setBackgroundResource(getDuValueRes(min));
			yucetv16.setBackgroundResource(getDuValueRes(max));
			yucetv15.setText(getDuValue(min,result.get(5).getMINAIRLEVEL1()));
			yucetv16.setText(getDuValue(max,result.get(5).getMAXAIRLEVEL1()));
		} else if (result.size() == 7) {
			fenge1.setVisibility(View.VISIBLE);
			fenge2.setVisibility(View.VISIBLE);
			fenge3.setVisibility(View.VISIBLE);
			fenge4.setVisibility(View.VISIBLE);
			fenge5.setVisibility(View.VISIBLE);
			fenge6.setVisibility(View.VISIBLE);
			yucetv19.setVisibility(View.VISIBLE);
			yucetv20.setVisibility(View.VISIBLE);
			yucetv21.setVisibility(View.VISIBLE);
			yucelinear5.setVisibility(View.VISIBLE);
			yucelinear6.setVisibility(View.VISIBLE);
			yucelinear7.setVisibility(View.VISIBLE);
			yucetv1.setText(result.get(0).getFORECASTTIME().substring(5, 10));
			yucetv2.setText(result.get(1).getFORECASTTIME().substring(5, 10));
			yucetv3.setText(result.get(2).getFORECASTTIME().substring(5, 10));
			yucetv4.setText(result.get(3).getFORECASTTIME().substring(5, 10));
			yucetv19.setText(result.get(4).getFORECASTTIME().substring(5, 10));
			yucetv20.setText(result.get(5).getFORECASTTIME().substring(5, 10));
			yucetv21.setText(result.get(6).getFORECASTTIME().substring(5, 10));
			min = result.get(0).getMINAIRLEVEL();
			max = result.get(0).getMAXAIRLEVEL();
			yucetv5.setBackgroundResource(getDuValueRes(min));
			yucetv6.setBackgroundResource(getDuValueRes(max));
			yucetv5.setText(getDuValue(min,result.get(0).getMINAIRLEVEL1()));
			yucetv6.setText(getDuValue(max,result.get(0).getMAXAIRLEVEL1()));
			min = result.get(1).getMINAIRLEVEL();
			max = result.get(1).getMAXAIRLEVEL();
			yucetv7.setBackgroundResource(getDuValueRes(min));
			yucetv8.setBackgroundResource(getDuValueRes(max));
			yucetv7.setText(getDuValue(min,result.get(1).getMINAIRLEVEL1()));
			yucetv8.setText(getDuValue(max,result.get(1).getMAXAIRLEVEL1()));
			min = result.get(2).getMINAIRLEVEL();
			max = result.get(2).getMAXAIRLEVEL();
			yucetv9.setBackgroundResource(getDuValueRes(min));
			yucetv10.setBackgroundResource(getDuValueRes(max));
			yucetv9.setText(getDuValue(min,result.get(2).getMINAIRLEVEL1()));
			yucetv10.setText(getDuValue(max,result.get(2).getMAXAIRLEVEL1()));
			min = result.get(3).getMINAIRLEVEL();
			max = result.get(3).getMAXAIRLEVEL();
			yucetv11.setBackgroundResource(getDuValueRes(min));
			yucetv12.setBackgroundResource(getDuValueRes(max));
			yucetv11.setText(getDuValue(min,result.get(3).getMINAIRLEVEL1()));
			yucetv12.setText(getDuValue(max,result.get(3).getMAXAIRLEVEL1()));
			min = result.get(4).getMINAIRLEVEL();
			max = result.get(4).getMAXAIRLEVEL();
			yucetv13.setBackgroundResource(getDuValueRes(min));
			yucetv14.setBackgroundResource(getDuValueRes(max));
			yucetv13.setText(getDuValue(min,result.get(4).getMINAIRLEVEL1()));
			yucetv14.setText(getDuValue(max,result.get(4).getMAXAIRLEVEL1()));
			min = result.get(5).getMINAIRLEVEL();
			max = result.get(5).getMAXAIRLEVEL();
			yucetv15.setBackgroundResource(getDuValueRes(min));
			yucetv16.setBackgroundResource(getDuValueRes(max));
			yucetv15.setText(getDuValue(min,result.get(5).getMINAIRLEVEL1()));
			yucetv16.setText(getDuValue(max,result.get(5).getMAXAIRLEVEL1()));
			min = result.get(6).getMINAIRLEVEL();
			max = result.get(6).getMAXAIRLEVEL();
			yucetv17.setBackgroundResource(getDuValueRes(min));
			yucetv18.setBackgroundResource(getDuValueRes(max));
			yucetv17.setText(getDuValue(min,result.get(6).getMINAIRLEVEL1()));
			yucetv18.setText(getDuValue(max,result.get(6).getMAXAIRLEVEL1()));
		}

	}

	public static String getDuValue(int aqi,String MAXAIRLEVEL1) {
		String xx = "优";
		if (aqi == 0) {
			xx = "--";
		} else if (aqi == 1) {
			xx = "优";
		} else if (aqi == 2) {
			xx = "良";
		} else if (aqi == 3) {
			xx = "轻度";
		} else if (aqi == 4) {
			xx = "中度";
		} else if (aqi == 5) {
			xx = "重度";
		} else {
			xx = "严重";
		}
		if (MAXAIRLEVEL1.contains("*")) {
			xx=xx+"*";
		}
		return xx;
	}

	public static int getDuValueRes(int aqi) {
		int bg = R.drawable.aqi_level_1;
		if (aqi == 1) {
			bg = R.drawable.aqi_level_1;
		} else if (aqi == 2) {
			bg = R.drawable.aqi_level_2;
		} else if (aqi == 3) {
			bg = R.drawable.aqi_level_3;
		} else if (aqi == 4) {
			bg = R.drawable.aqi_level_4;
		} else if (aqi == 5) {
			bg = R.drawable.aqi_level_5;
		} else if (aqi == 0) {
			bg = R.drawable.aqi_level_7;
		} else {
			bg = R.drawable.aqi_level_6;
		}
		return bg;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_main_suggest:
			finish();
			break;

		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("yubaoActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("yubaoActivity");
		MobclickAgent.onPause(this);
	}
}
