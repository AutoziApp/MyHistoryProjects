package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.activity.DiscoverNearbyListActivity.listviewadapter;
import com.jy.environment.activity.SettingPersonActivity.MyAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.AqiModel;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.model.ThreeDayForestModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;

public class ThreeDayAqiTrend extends ActivityBase {
	private List<ThreeDayAqiTrendModel> detailModels = new ArrayList<ThreeDayAqiTrendModel>();
	private GetThreeDayAqiTrendModel getThreeDayAqiTrendModel;
	private ListView three_day_aqi_trend_listview;
	private MyAdapter myAdapter;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_three_day_aqi_trend);
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		three_day_aqi_trend_listview = (ListView) findViewById(R.id.three_day_aqi_trend_listview);
		getThreeDayAqiTrendModel = new GetThreeDayAqiTrendModel();
		getThreeDayAqiTrendModel.execute();

	}

	class MyAdapter extends BaseAdapter {
		private List<ThreeDayAqiTrendModel> list = new ArrayList<ThreeDayAqiTrendModel>();

		public void bindData(List<ThreeDayAqiTrendModel> detailModels) {
			this.list = detailModels;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return detailModels.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return detailModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ThreeDayAqiTrendModel model = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ThreeDayAqiTrend.this)
						.inflate(R.layout.three_day_aqi_trend_item, null);
				holder = new ViewHolder();

				holder.city = (TextView) convertView.findViewById(R.id.city);
				holder.minL = (TextView) convertView.findViewById(R.id.minL);
				holder.maxL = (TextView) convertView.findViewById(R.id.maxL);
				holder.monitor = (TextView) convertView
						.findViewById(R.id.monitor);
				holder.forecast = (TextView) convertView
						.findViewById(R.id.forecast);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.city.setText(model.getCITY());
			holder.minL.setText(model.getMINAIRLEVEL());
			holder.maxL.setText(model.getMAXAIRLEVEL());
			holder.monitor.setText(model.getMONITORTIME());
			holder.forecast.setText(model.getFORECASTTIME());

			return convertView;
		}

		class ViewHolder {

			private TextView city;
			private TextView minL;
			private TextView maxL;
			private TextView monitor;
			private TextView forecast;

		}
	}

	class GetThreeDayAqiTrendModel extends
			AsyncTask<String, Void, List<ThreeDayAqiTrendModel>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// dialog.show();
		}

		@Override
		protected List<ThreeDayAqiTrendModel> doInBackground(String... params) {
			String url = UrlComponent.getThreeDayAqiTrend;
			MyLog.i("ThreeDay" + url);
			String city = "郑州";
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
				MyLog.i("weibao result:" + result);
				super.onPostExecute(result);
				dialog.dismiss();
				if (((result.size()) == 0)) {
					/*
					 * environment_rank_details_tv12.setText("--");
					 * environment_rank_details_tv13.setText("--");
					 * environment_rank_details_tv14.setText("--")
					 */;
				}

				MyLog.i("detailModels======" + detailModels.toString());
				myAdapter = new MyAdapter();
				myAdapter.bindData(result);
				three_day_aqi_trend_listview.setAdapter(myAdapter);

			} catch (Exception e) {
				e.printStackTrace();
				MyLog.e("weibao Exception", e);
			}
		}
	}
}
