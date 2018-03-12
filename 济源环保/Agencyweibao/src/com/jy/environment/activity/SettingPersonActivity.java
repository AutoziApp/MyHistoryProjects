package com.jy.environment.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ui.RecognizerDialog;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.AqiModel;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.model.YuCeModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.util.timepicker.NumericWheelAdapter;
import com.jy.environment.util.timepicker.OnWheelScrollListener;
import com.jy.environment.util.timepicker.WheelView;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

public class SettingPersonActivity extends ActivityBase implements
		OnClickListener {
	private TextView liebiao, map, youliangtianshu, zhuangkuang, tishi_niandu,
			center, statistics_start_time, statistics_start_timee,
			statistics_end_time, yubao, statistics_search, air_headtv1,
			air_headtv2, tv_month, tv_start_date_label, yubao_tv5, yubao_tv6;
	private ImageView nodata;
	private ListView monitoring_listview;
	private int leftPressDefault = R.drawable.left_default;
	private int leftPress = R.drawable.left_press;
	private int centerPressDefault = R.drawable.menu_center_default;
	private int centerPress = R.drawable.menu_center_press;
	private int rightPressDefault = R.drawable.right_default;
	private int rightPress = R.drawable.right_press;
	Dialog dialog;
	private MyAdapter myAdapter;
	private GetMonitoringAqiTask getMonitoringAqiTask;
	private int isliebiao = 0;// 0代表实时，1代表日报，2代表预报，3代表月累计，4代表年累计
	private Resources resource;
	private PopupWindow menuWindow;
	private RelativeLayout tishi;
	private LinearLayout yubao_head, xuanzeshijian, tishi_xuanze, ll_end_date,
			air_head, yuce_lin,youliangtian_lin;
	private TextView yubao_city, yubao_tv1, yubao_tv2, yubao_tv3, yubao_tv4,
			first_polluction;
	private ImageView oldbutton;
	
	// 传递的时间区间
	private String yucebao1, yucebao2, yucebao3;
	private String dateStar = "";
	private String dateStarr = "";
	private String dateEnd = "";
	private String dateMonth = "", date = "";
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private SharedPreferences shares;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private long lastUpdateTime;
	private String[] yubaoStatus = new String[] { "优   ", "良   ", "轻度", "中度",
			"重度", "严重", "暂无" };
	private int[] yubaoImg = new int[] { R.drawable.air_icon1,
			R.drawable.air_icon2, R.drawable.air_icon3, R.drawable.air_icon4,
			R.drawable.air_icon5, R.drawable.air_icon6, R.drawable.air_iconkong };
	private YuCeAdapter yuCeAdapter;
	private GetYuCeAqiTask getYuCeAqiTask;
	private static final int StartTimeValueCode = 101;
	private static final int EndTimeValueCode = 102;
	private static final int CenterTimeValueCode = 103;
	private static final int MonthTimeValueCode = 104;
	private int month_year = 0;
	private long exitTime = 0;
	private boolean isFirst = true;
	private boolean isShowOld=false;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case StartTimeValueCode:
				date = (String) msg.obj;
				dateStar = (String) msg.obj;
				statistics_start_time.setText(dateStar);
				if (isliebiao == 1) {
					isFirst = false;
				}
				getMonitoringAqiTask = new GetMonitoringAqiTask();
				getMonitoringAqiTask.execute("");
				break;
			case EndTimeValueCode:
				dateEnd = (String) msg.obj;
				statistics_end_time.setText(dateEnd);
				break;
			case CenterTimeValueCode:
				dateStarr = (String) msg.obj;
				statistics_start_timee.setText(dateStarr);
				break;
			case MonthTimeValueCode:
				isliebiao = 5;
				dateStarr = (String) msg.obj;
				dateMonth = dateStarr;
				statistics_start_timee.setText(dateMonth);
				getMonitoringAqiTask = new GetMonitoringAqiTask();
				getMonitoringAqiTask.execute("");
				break;
			default:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_person_activity);
		resource = (Resources) getResources();
		dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
				R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
		shares = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		lastUpdateTime = shares.getLong("lastUpdateTime", 0);
		preferencesUtil = SharedPreferencesUtil.getInstance(this);
		isShowOld=preferencesUtil.getoldshow();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		liebiao = (TextView) findViewById(R.id.liebiao);
		center = (TextView) findViewById(R.id.center);
		map = (TextView) findViewById(R.id.map);
		tv_month = (TextView) findViewById(R.id.tv_month);
		ll_end_date = (LinearLayout) findViewById(R.id.ll_end_date);
		yubao_head = (LinearLayout) findViewById(R.id.yubao_head);
		air_head = (LinearLayout) findViewById(R.id.air_head);
		tv_start_date_label = (TextView) findViewById(R.id.tv_start_date_label);
		statistics_start_time = (TextView) findViewById(R.id.statistics_start_time);
		statistics_start_timee = (TextView) findViewById(R.id.statistics_start_timee);
		statistics_end_time = (TextView) findViewById(R.id.statistics_end_time);
		statistics_search = (TextView) findViewById(R.id.statistics_search);
		yubao_city = (TextView) findViewById(R.id.yubao_city);
		yubao_tv1 = (TextView) findViewById(R.id.yubao_tv1);
		yubao_tv2 = (TextView) findViewById(R.id.yubao_tv2);
		yubao_tv3 = (TextView) findViewById(R.id.yubao_tv3);
		yubao_tv4 = (TextView) findViewById(R.id.yubao_tv4);
		yubao_tv5 = (TextView) findViewById(R.id.yubao_tv5);
		first_polluction = (TextView) findViewById(R.id.first_polluction);
		air_headtv1 = (TextView) findViewById(R.id.air_headtv1);
		air_headtv2 = (TextView) findViewById(R.id.air_headtv2);
		xuanzeshijian = (LinearLayout) findViewById(R.id.xuanzeshijian);
		tishi_xuanze = (LinearLayout) findViewById(R.id.tishi_xuanze);
		yuce_lin = (LinearLayout) findViewById(R.id.yuce_lin);
		tishi = (RelativeLayout) findViewById(R.id.tishi);
		xuanzeshijian.setVisibility(View.GONE);
		tishi_xuanze.setVisibility(View.GONE);
		yubao = (TextView) findViewById(R.id.yubao);
		nodata = (ImageView) findViewById(R.id.nodata);
		tishi_niandu = (TextView) findViewById(R.id.tishi_niandu);
		youliangtianshu = (TextView) findViewById(R.id.youliangtianshu);
		zhuangkuang = (TextView) findViewById(R.id.zhuangkuang);
		monitoring_listview = (ListView) findViewById(R.id.monitoring_listview);
		youliangtian_lin=(LinearLayout) findViewById(R.id.youliangtianshu_liner);
		oldbutton=(ImageView) findViewById(R.id.oldActivity);
//		if (isShowOld) {
//			oldbutton.setOnClickListener(this);
//			oldbutton.setVisibility(View.VISIBLE);
//		}else {
//			oldbutton.setOnClickListener(null);
//			oldbutton.setVisibility(View.GONE);
//		}
		
		liebiao.setOnClickListener(this);
		center.setOnClickListener(this);
		yubao.setOnClickListener(this);
		map.setOnClickListener(this);
		tv_month.setOnClickListener(this);
		statistics_start_timee.setOnClickListener(this);
		statistics_search.setOnClickListener(this);
		initCalendar(statistics_start_time, statistics_start_timee,
				statistics_end_time);
		statistics_start_time.setOnClickListener(this);
		statistics_end_time.setOnClickListener(this);

	}

	private void initCalendar(TextView startTv, TextView tv1, TextView tv2) {
		Calendar calbefore = Calendar.getInstance();
		calbefore.setTime(new Date());
		calbefore.add(Calendar.MONTH, -1);
		if ((calbefore.get(Calendar.MONTH) + 1) < 10) {
			dateStar = calbefore.get(Calendar.YEAR) + "-" + "0"
					+ ((calbefore.get(Calendar.MONTH) + 2)) + "-"
					+ (calbefore.get(Calendar.DATE) - 1);
			dateEnd = calbefore.get(Calendar.YEAR) + "-" + "0"
					+ ((calbefore.get(Calendar.MONTH) + 2)) + "-"
					+ (calbefore.get(Calendar.DATE) - 1);
			MyLog.i("dateStarr4" + dateStarr);
		} else {
			dateStar = calbefore.get(Calendar.YEAR) + "-"
					+ ((calbefore.get(Calendar.MONTH) + 1)) + "-"
					+ (calbefore.get(Calendar.DATE) - 1);
			dateEnd = calbefore.get(Calendar.YEAR) + "-"
					+ ((calbefore.get(Calendar.MONTH) + 1)) + "-"
					+ (calbefore.get(Calendar.DATE) - 1);
			MyLog.i("dateStarr5" + dateStarr);
		}
		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		String defaultStartDate = sdf.format(dBefore); // 格式化前一天
		String defaultEndDate = sdf.format(dNow); // 格式化当前时间
		dateStar = defaultStartDate.substring(0, 10);
		dateEnd = defaultStartDate.substring(0, 10);
		MyLog.i("dateStarr3" + dateStarr);
		dateMonth = defaultEndDate.substring(0, 7);
		String month, day, year;
		year = defaultEndDate.substring(0, 4);
		month = defaultEndDate.substring(5, 7);
		day = defaultEndDate.substring(8, 10);
		if (day.equals("01")) {
			if (month.equals("01")) {
				year = (Integer.parseInt(year) - 1) + "";
				month = 12 + "";
			} else {
				month = (Integer.parseInt(month) - 1) + "";
			}
		}
		if (month.length() == 1) {
			month = "0" + month;
		}
		dateMonth = year + "-" + month;
		date = defaultStartDate.substring(0, 10);
		startTv.setText(defaultStartDate.substring(0, 10));
		
		Calendar calbenow = Calendar.getInstance();
		if (month_year == 365) {
			dateStarr = calbenow.get(Calendar.YEAR) + "-" + "01" + "-" + "01";
		}
		MyLog.i("dateStarr2" + dateStarr);
		if (month_year == 30) {
			dateStarr = calbenow.get(Calendar.YEAR) + "-" + "01";
		}
		MyLog.i("dateStarr1" + dateStarr);
		tv1.setText(dateStarr);
		tv2.setText(defaultStartDate.substring(0, 10));
	}

	// 获取监测站数据
	private class GetMonitoringAqiTask extends
			AsyncTask<String, Void, List<AqiModel>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (!NetUtil.isNetworkConnected(SettingPersonActivity.this)) {
				ToastUtil.showShort(SettingPersonActivity.this, "无网络");
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

		@Override
		protected List<AqiModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			String url = "";
			String begin = "";
			String month = "";
			MyLog.i("dateStarrsss" + dateStarr);
			MyLog.i("dateStarrsss22" + dateEnd);
			MyLog.i("dateStar" + dateStar);
			if (isliebiao == 0) {
				url = UrlComponent.getLeiJiNongDuValue;
			} else if (isliebiao == 1) {
				if (isFirst) {
					begin = "";
				} else {
					begin = dateStar;
				}
				url = UrlComponent.getRiBaoValue;
			} else if (isliebiao == 5) {
				begin = "";
				dateEnd = "";
				month = dateMonth;
				url = UrlComponent.getNongDuValue;
			} else {
				begin = dateStarr;
				url = UrlComponent.getNongDuValue;
			}
			List<AqiModel> _Result;
			try {
				_Result = search.getMonitoringAqi(url, isliebiao, begin,
						dateEnd, month);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<AqiModel> result) {
			super.onPostExecute(result);
			if (null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (result == null || result.size() == 0) {
				nodata.setVisibility(View.VISIBLE);
				monitoring_listview.setVisibility(View.GONE);
				return;
			} else {
				yubao_head.setVisibility(View.GONE);
				yuce_lin.setVisibility(View.GONE);
				air_head.setVisibility(View.VISIBLE);
				nodata.setVisibility(View.GONE);
				monitoring_listview.setVisibility(View.VISIBLE);
			}
			if (isliebiao == 0) {
				tishi_niandu.setTextColor(getResources().getColor(
						R.color.setnormal));
				first_polluction.setVisibility(View.VISIBLE);
				tishi_niandu.setVisibility(View.VISIBLE);
				tishi_niandu.setText("更新时间:" + result.get(0).getMONIDATE());
				tishi_xuanze.setVisibility(View.GONE);
			} else if (isliebiao == 1) {
				tishi_niandu.setVisibility(View.GONE);
				zhuangkuang.setVisibility(View.GONE);
				try {
					date = result.get(0).getTime();
				} catch (Exception e) {
					// TODO: handle exception
				}
				statistics_start_time.setText(result.get(0).getTime());
			} else if (isliebiao == 5) {
				tishi_niandu.setTextColor(getResources().getColor(
						R.color.setbefore));
				if (preferencesUtil.getQuanXian()) {
					tishi_niandu.setVisibility(View.VISIBLE);
					tishi_niandu.setText("同比  " + result.get(0).getYear()
							+ "  月" + "空气质量");
				} else {
					tishi_niandu.setVisibility(View.GONE);
					tishi_niandu.setText("同比  " + result.get(0).getYear()
							+ "  月" + "空气质量");
				}

			} else {
				Calendar calbefore = Calendar.getInstance();
				if (!dateStarr.equals("")) {
					// tishi_niandu.setText(dateStarr + "至" + dateEnd + "空气质量");
					tishi_niandu.setTextColor(getResources().getColor(
							R.color.setbefore));

					if (preferencesUtil.getQuanXian()) {
						tishi_niandu.setVisibility(View.VISIBLE);
						tishi_niandu.setText("同比  " + result.get(0).getYear()
								+ "至" + result.get(0).getNowyear() + "空气质量");
					} else {
						tishi_niandu.setText("同比  " + result.get(0).getYear()
								+ "至" + result.get(0).getNowyear() + "空气质量");
						tishi_niandu.setVisibility(View.GONE);
					}

				} else {
					tishi_niandu.setVisibility(View.VISIBLE);
					tishi_niandu.setTextColor(getResources().getColor(
							R.color.setbefore));
					tishi_niandu
							.setText(calbefore.get(Calendar.YEAR) + "年空气质量");
				}
			}
			try {
				myAdapter = new MyAdapter();
				myAdapter.bindData(result);
				monitoring_listview.setAdapter(myAdapter);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	class MyAdapter extends BaseAdapter {
		private List<AqiModel> detailModels;

		public void bindData(List<AqiModel> detailModels) {
			this.detailModels = detailModels;
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
			AqiModel model = detailModels.get(position);
			String city = model.getCITY();
			ViewHolder holder = null;
			holder = new ViewHolder();
			if (!(city.equals("市平均") || city.equals("县平均"))) {
				convertView = LayoutInflater.from(SettingPersonActivity.this)
						.inflate(R.layout.monitor_item, parent,false);
				holder = new ViewHolder();
				holder.rank = (TextView) convertView.findViewById(R.id.rank);
				holder.city = (TextView) convertView.findViewById(R.id.city);
				holder.city_suoshu = (TextView) convertView
						.findViewById(R.id.city_suoshu);
				holder.aqi = (TextView) convertView.findViewById(R.id.aqi);
				holder.first_polluction = (TextView) convertView
						.findViewById(R.id.first_polluction);
				holder.grade = (TextView) convertView.findViewById(R.id.grade);
				holder.linearyincang = (LinearLayout) convertView
						.findViewById(R.id.linearyincang);
			}
//			else if(isliebiao == 2 || isliebiao == 5){
//				convertView = LayoutInflater.from(SettingPersonActivity.this)
//						.inflate(R.layout.monitor_item_old, parent,false);
//				holder = new ViewHolder();
//				holder.rank = (TextView) convertView.findViewById(R.id.rank);
//				holder.city = (TextView) convertView.findViewById(R.id.city);
//				holder.city_suoshu = (TextView) convertView
//						.findViewById(R.id.city_suoshu);
//				holder.aqi = (TextView) convertView.findViewById(R.id.aqi);
//				holder.first_polluction = (TextView) convertView
//						.findViewById(R.id.first_polluction);
//				holder.grade = (TextView) convertView.findViewById(R.id.grade);
//				holder.linearyincang = (LinearLayout) convertView
//						.findViewById(R.id.linearyincang);
//			}
			else{
				convertView = LayoutInflater.from(SettingPersonActivity.this)
						.inflate(R.layout.monitor_itemm,  parent,false);
				holder = new ViewHolder();
				holder.rank = (TextView) convertView.findViewById(R.id.rank);
				holder.city = (TextView) convertView.findViewById(R.id.city);
				holder.city_suoshu = (TextView) convertView
						.findViewById(R.id.city_suoshu);
				holder.aqi = (TextView) convertView.findViewById(R.id.aqi);
				holder.first_polluction = (TextView) convertView
						.findViewById(R.id.first_polluction);
				holder.grade = (TextView) convertView.findViewById(R.id.grade);
				holder.linearyincang = (LinearLayout) convertView
						.findViewById(R.id.linearyincang);
				holder.item = (LinearLayout) convertView
						.findViewById(R.id.item);
			}
			try {
				int pmtv1 = Integer.parseInt(model.getPM25());
				int pmtv2 = Integer.parseInt(model.getPM10());
				holder.item.setBackgroundColor(Color.parseColor("#27b3ae"));
//				holder.item.setBackgroundResource(R.drawable.list_bg_air_avg);
			} catch (Exception e) {
				// TODO: handle exception
				try {
					int pmtv1 = Integer.parseInt(model.getPM25().substring(0,
							model.getPM25().indexOf("/")));
					int pmtv2 = Integer.parseInt(model.getPM10().substring(0,
							model.getPM10().indexOf("/")));
					holder.item.setBackgroundColor(Color.parseColor("#27b3ae"));
//					holder.item.setBackgroundResource(R.drawable.list_bg_air_avg);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if (isliebiao == 2 || isliebiao == 5) {
				holder.linearyincang.setVisibility(View.GONE);
				holder.first_polluction.setVisibility(View.VISIBLE);
				holder.aqi.setVisibility(View.GONE);
				holder.rank.setText(model.getCITY());
				String pm10 = (model.getPM10().equals("0") || model.getPM10()
						.equals("-1")) ? "--" : model.getPM10() + "";
				String pm25 = (model.getPM25().equals("0") || model.getPM25()
						.equals("-1")) ? "--" : model.getPM25() + "";
				String o3 = model.getO3();
				String cnt = model.getCNT();
				// String cnt = modelgetCNT().equals("0") ? "--" :
				// model.getCNT() + "";
				holder.city.setText(pm10 + "");
				holder.city_suoshu.setText(pm25 + "");
				holder.aqi.setText(model.getO3());
				holder.first_polluction.setText(model.getCNT());
				SpannableString styledText;
				try {
					if (preferencesUtil.getQuanXian()) {

						styledText = new SpannableString(holder.city.getText()
								.toString());
						styledText.setSpan(
								new TextAppearanceSpan(
										SettingPersonActivity.this,
										R.style.stylebefore),
								pm10.indexOf("/") + 1, pm10.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						holder.city.setText(styledText);
						// pm2.5
						styledText = new SpannableString(holder.city_suoshu
								.getText().toString());
						styledText.setSpan(
								new TextAppearanceSpan(
										SettingPersonActivity.this,
										R.style.stylebefore),
								pm25.indexOf("/") + 1, pm25.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						holder.city_suoshu.setText(styledText);
						// o3
						styledText = new SpannableString(holder.aqi.getText()
								.toString());
						styledText.setSpan(
								new TextAppearanceSpan(
										SettingPersonActivity.this,
										R.style.stylebefore),
								o3.indexOf("/") + 1, o3.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						holder.aqi.setText(styledText,
								TextView.BufferType.SPANNABLE);
						try {
							int first_polluctionl = holder.first_polluction
									.getText().toString().indexOf("/");
							holder.first_polluction
									.setText(holder.first_polluction.getText()
											.toString()
											.substring(0, first_polluctionl));
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						try {
							int cityl = holder.city.getText().toString()
									.indexOf("/");
							holder.city.setText(holder.city.getText()
									.toString().substring(0, cityl));
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							int city_suoshul = holder.city_suoshu.getText()
									.toString().indexOf("/");
							holder.city_suoshu.setText(holder.city_suoshu
									.getText().toString()
									.substring(0, city_suoshul));
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							int aqil = holder.aqi.getText().toString()
									.indexOf("/");
							holder.aqi.setText(holder.aqi.getText().toString()
									.substring(0, aqil));
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							int first_polluctionl = holder.first_polluction
									.getText().toString().indexOf("/");
							holder.first_polluction
									.setText(holder.first_polluction.getText()
											.toString()
											.substring(0, first_polluctionl));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					// SpannableStringBuilder builder = new
					// SpannableStringBuilder(
					// holder.city.getText().toString());
					// // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
					// ForegroundColorSpan redSpan = new ForegroundColorSpan(
					// getResources().getColor(R.color.setbefore));
					// builder.setSpan(redSpan, pm10.indexOf("/") + 1,
					// pm10.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// holder.city.setText(builder);
					// SpannableStringBuilder builderafter = new
					// SpannableStringBuilder(
					// holder.city_suoshu.getText().toString());
					// // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
					// ForegroundColorSpan redSpanafter = new
					// ForegroundColorSpan(
					// getResources().getColor(R.color.setbefore));
					// builderafter.setSpan(redSpanafter, pm25.indexOf("/") + 1,
					// pm25.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// holder.city_suoshu.setText(builderafter);
					// SpannableStringBuilder buildero3 = new
					// SpannableStringBuilder(
					// holder.aqi.getText().toString());
					// // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
					// ForegroundColorSpan redSpano3 = new ForegroundColorSpan(
					// getResources().getColor(R.color.setbefore));
					// buildero3.setSpan(redSpano3, o3.indexOf("/") + 1,
					// o3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// holder.aqi.setText(buildero3);
					// SpannableStringBuilder buildercnt = new
					// SpannableStringBuilder(
					// holder.first_polluction.getText().toString());
					// // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
					// ForegroundColorSpan redSpancnt = new ForegroundColorSpan(
					// getResources().getColor(R.color.setbefore));
					// buildercnt.setSpan(redSpancnt, cnt.indexOf("/") + 1,
					// cnt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// holder.first_polluction.setText(buildercnt);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (isliebiao == 1) {

				try {
					holder.linearyincang.setVisibility(View.VISIBLE);
					holder.first_polluction.setVisibility(View.VISIBLE);
			
					holder.rank.setText(model.getCITY());
					String pm10 = (model.getPM10().equals("0") || model
							.getPM10().equals("-1")) ? "--" : model.getPM10()
							+ "";
					String pm25 = (model.getPM25().equals("0") || model
							.getPM25().equals("-1")) ? "--" : model.getPM25()
							+ "";
					String aqi = (model.getAQI().equals("0") || model.getAQI()
							.equals("-1")) ? "--" : model.getAQI() + "";
					// if (model.getCITY().equals("市平均")
					// || model.getCITY().equals("县平均")) {
					// aqi = "--";
					// }
					holder.city.setText(pm10 + "");
					holder.city_suoshu.setText(pm25 + "");
					
					if (!((city.equals("市平均") || city.equals("县平均")))) {
						holder.city.setBackgroundColor(CommonUtil
								.getColorByPM10(pm10 + ""));
						holder.city_suoshu.setBackgroundColor(CommonUtil
								.getColorByPM25(pm25 + ""));
					}
					
					
					holder.aqi.setText(aqi + "");
//					try {
//						if (!aqi.equals("--")) {
//							holder.grade.setBackgroundResource(CommonUtil
//									.getRIdByAQI(model.getAQI() + ""));
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
					String PRIMARYPOLLUTAN = model.getPRIMARYPOLLUTANT();
					if (PRIMARYPOLLUTAN != null) {
						if (PRIMARYPOLLUTAN.contains("臭氧")) {
							PRIMARYPOLLUTAN = "O3";
						} else if (PRIMARYPOLLUTAN.contains("一氧化碳")) {
							PRIMARYPOLLUTAN = "CO";
						} else if (PRIMARYPOLLUTAN.contains("二氧化硫")) {
							PRIMARYPOLLUTAN = "SO2";
						} else if (PRIMARYPOLLUTAN.contains("二氧化氮")) {
							PRIMARYPOLLUTAN = "NO2";
						}
						holder.first_polluction.setText(PRIMARYPOLLUTAN);
					} else {
						holder.first_polluction.setText("--");
					}
					String aqilevel = CommonUtil.getDengJiByAQI(aqi);
					holder.linearyincang.setVisibility(View.GONE);
//					try {
//						if (aqi.equals("--")) {
//							holder.grade.setText("--");
//						} else {
//							holder.grade.setText(aqilevel);
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			
			}else if (isliebiao == 0) {
				try {
					holder.linearyincang.setVisibility(View.VISIBLE);
					holder.first_polluction.setVisibility(View.VISIBLE);
					holder.rank.setText(model.getCITY());
					String pm10 = (model.getPM10().equals("0") || model
							.getPM10().equals("-1")) ? "--" : model.getPM10()
							+ "";
					String pm25 = (model.getPM25().equals("0") || model
							.getPM25().equals("-1")) ? "--" : model.getPM25()
							+ "";
					String aqi = (model.getAQI().equals("0") || model.getAQI()
							.equals("-1")) ? "--" : model.getAQI() + "";
					// if (model.getCITY().equals("市平均")
					// || model.getCITY().equals("县平均")) {
					// aqi = "--";
					// }
					holder.city.setText(pm10 + "");
					holder.city_suoshu.setText(pm25 + "");
					holder.aqi.setText(aqi + "");
					try {
						if (!aqi.equals("--")) {
							holder.grade.setBackgroundResource(CommonUtil
									.getRIdByAQI(model.getAQI() + ""));
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					String PRIMARYPOLLUTAN = model.getPRIMARYPOLLUTANT();
					if (PRIMARYPOLLUTAN != null) {
						if (PRIMARYPOLLUTAN.contains("臭氧")) {
							PRIMARYPOLLUTAN = "O3";
						} else if (PRIMARYPOLLUTAN.contains("一氧化碳")) {
							PRIMARYPOLLUTAN = "CO";
						} else if (PRIMARYPOLLUTAN.contains("二氧化硫")) {
							PRIMARYPOLLUTAN = "SO2";
						} else if (PRIMARYPOLLUTAN.contains("二氧化氮")) {
							PRIMARYPOLLUTAN = "NO2";
						}
						holder.first_polluction.setText(PRIMARYPOLLUTAN);
					} else {
						holder.first_polluction.setText("--");
					}
					String aqilevel = CommonUtil.getDengJiByAQI(aqi);
					try {
						if (aqi.equals("--")) {
							holder.grade.setText("--");
						} else {
							holder.grade.setText(aqilevel);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
//			if (model.getCITY().equals("市平均")||model.getCITY().equals("县平均")||model.getCITY().equals("滑县")) {
//				
//			}else {
//				String city_new=model.getCITY().replace("市", "").replace("县", "");
//				holder.rank.setText(city_new);
//			}
			return convertView;
		}
	}

	static class ViewHolder {
		private TextView rank;
		private TextView city;
		private TextView city_suoshu;
		private TextView aqi;
		private TextView first_polluction;
		private TextView grade;
		private LinearLayout linearyincang;
		private LinearLayout item;

	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SpannableString styledText;
		SpannableString styledPmText;
		String pm10;
		String pm25;
		String o3;
		String cnt;
		switch (v.getId()) {
		case R.id.liebiao://实时
			first_polluction.setVisibility(View.VISIBLE);
			MobclickAgent.onEvent(SettingPersonActivity.this, "WeatherShishi");
			isliebiao = 0;
			yubao_head.setVisibility(View.GONE);
			yuce_lin.setVisibility(View.GONE);
			air_head.setVisibility(View.VISIBLE);
			xuanzeshijian.setVisibility(View.GONE);
			tishi_xuanze.setVisibility(View.GONE);
			tishi_niandu.setVisibility(View.GONE);
			tishi.setVisibility(View.VISIBLE);
			youliangtianshu.setText("AQI");
			air_headtv1.setText("PM10");
			air_headtv2.setText("PM2.5");
			first_polluction.setText("首要\n污染物");
			youliangtianshu.setText("AQI");
			youliangtian_lin.getChildAt(1).setVisibility(View.INVISIBLE);
			zhuangkuang.setVisibility(View.VISIBLE);
			{
				liebiao.setBackgroundResource(leftPress);
				map.setBackgroundResource(rightPressDefault);
				center.setBackgroundResource(centerPressDefault);
				yubao.setBackgroundResource(centerPressDefault);
				tv_month.setBackgroundResource(centerPressDefault);
				ColorStateList csl1 = (ColorStateList) resource
						.getColorStateList(R.color.title_press_color);
				ColorStateList csl2 = (ColorStateList) resource
						.getColorStateList(R.color.title_default_color);
				liebiao.setTextColor(csl2);
				center.setTextColor(csl1);
				map.setTextColor(csl1);
				yubao.setTextColor(csl1);
				tv_month.setTextColor(csl1);
			}
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
			break;
		case R.id.center://日报
			first_polluction.setVisibility(View.VISIBLE);
			MobclickAgent.onEvent(SettingPersonActivity.this, "WeatherRibao");
			yubao_head.setVisibility(View.GONE);
			yuce_lin.setVisibility(View.GONE);
			air_head.setVisibility(View.VISIBLE);
			air_headtv1.setText("PM10");
			air_headtv2.setText("PM2.5");
			first_polluction.setText("首要\n污染物");
			youliangtianshu.setText("AQI");
			youliangtian_lin.getChildAt(1).setVisibility(View.INVISIBLE);
			tishi_xuanze.setVisibility(View.VISIBLE);
			tishi_niandu.setVisibility(View.GONE);
			xuanzeshijian.setVisibility(View.GONE);
			tishi.setVisibility(View.VISIBLE);
			tishi_niandu.setVisibility(View.GONE);
			tishi_xuanze.setVisibility(View.VISIBLE);
			isliebiao = 1;
			youliangtianshu.setText("AQI");
			zhuangkuang.setVisibility(View.VISIBLE);
			liebiao.setBackgroundResource(leftPressDefault);
			map.setBackgroundResource(rightPressDefault);
			yubao.setBackgroundResource(centerPressDefault);
			center.setBackgroundResource(centerPress);
			tv_month.setBackgroundResource(centerPressDefault);
			{
				ColorStateList csl1 = (ColorStateList) resource
						.getColorStateList(R.color.title_press_color);
				ColorStateList csl2 = (ColorStateList) resource
						.getColorStateList(R.color.title_default_color);
				liebiao.setTextColor(csl1);
				center.setTextColor(csl2);
				map.setTextColor(csl1);
				yubao.setTextColor(csl1);
				tv_month.setTextColor(csl1);
			}
			dateStar = "";
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
			break;
		case R.id.yubao: {//预报
			MobclickAgent.onEvent(SettingPersonActivity.this, "WeatherYubao");
			yubao_head.setVisibility(View.VISIBLE);
			yuce_lin.setVisibility(View.VISIBLE);
			air_head.setVisibility(View.GONE);
			liebiao.setBackgroundResource(leftPressDefault);
			map.setBackgroundResource(rightPressDefault);
			center.setBackgroundResource(centerPressDefault);
			yubao.setBackgroundResource(centerPress);
			tv_month.setBackgroundResource(centerPressDefault);
			ColorStateList csl1 = (ColorStateList) resource
					.getColorStateList(R.color.title_press_color);
			ColorStateList csl2 = (ColorStateList) resource
					.getColorStateList(R.color.title_default_color);
			liebiao.setTextColor(csl1);
			center.setTextColor(csl1);
			yubao.setTextColor(csl2);
			map.setTextColor(csl1);
			tv_month.setTextColor(csl1);
		}
			tishi_xuanze.setVisibility(View.VISIBLE);
			tishi_niandu.setVisibility(View.GONE);
			xuanzeshijian.setVisibility(View.GONE);
			tishi.setVisibility(View.VISIBLE);
			isliebiao = 4;
			tishi.setVisibility(View.GONE);
			zhuangkuang.setVisibility(View.GONE);
			getYuCeAqiTask = new GetYuCeAqiTask();
			getYuCeAqiTask.execute("");
			break;
		case R.id.map://年累计
			first_polluction.setVisibility(View.VISIBLE);
			MobclickAgent.onEvent(SettingPersonActivity.this, "WeatherYear");
			month_year = 365;
			yubao_head.setVisibility(View.GONE);
			yuce_lin.setVisibility(View.GONE);
			air_head.setVisibility(View.VISIBLE);
			
			statistics_search.setVisibility(View.VISIBLE);
			initCalendar(statistics_start_time, statistics_start_timee,
					statistics_end_time);

			if (preferencesUtil.getQuanXian()) {
				pm10 = "PM10/同比";
				pm25 = "PM2.5/同比";
				o3 = "O3/同比";
				cnt = "优良天数";
				air_headtv1.setText(pm10);
				air_headtv2.setText(pm25);
//				youliangtianshu.setText(o3);
				youliangtianshu.setText(cnt);
//				transText(air_headtv1, pm10);
//				transText(air_headtv2, pm25);
//				transText(youliangtianshu, o3);
//				transText(first_polluction, cnt);
			} else {
				pm10 = "PM10";
				pm25 = "PM2.5";
				o3 = "O3";
				cnt = "优良天数";
				air_headtv1.setText(pm10);
				air_headtv2.setText(pm25);
//				youliangtianshu.setText(o3);
				youliangtianshu.setText(cnt);
			}
			tishi_xuanze.setVisibility(View.GONE);
			xuanzeshijian.setVisibility(View.VISIBLE);
			ll_end_date.setVisibility(View.VISIBLE);
			tishi.setVisibility(View.GONE);
			tishi_niandu.setVisibility(View.GONE);
			isliebiao = 2;
			xuanzeshijian.setVisibility(View.VISIBLE);
			tishi.setVisibility(View.GONE);
			tishi_xuanze.setVisibility(View.GONE);
			tv_start_date_label.setText("开始日期:");
			Calendar calbefore1 = Calendar.getInstance();
			zhuangkuang.setVisibility(View.GONE);
			first_polluction.setVisibility(View.GONE);
			youliangtian_lin.getChildAt(1).setVisibility(View.VISIBLE);
			liebiao.setBackgroundResource(leftPressDefault);
			center.setBackgroundResource(centerPressDefault);
			yubao.setBackgroundResource(centerPressDefault);
			map.setBackgroundResource(rightPress);
			tv_month.setBackgroundResource(centerPressDefault);
			{
				ColorStateList csl1 = (ColorStateList) resource
						.getColorStateList(R.color.title_press_color);
				ColorStateList csl2 = (ColorStateList) resource
						.getColorStateList(R.color.title_default_color);
				liebiao.setTextColor(csl1);
				center.setTextColor(csl1);
				yubao.setTextColor(csl1);
				map.setTextColor(csl2);
				tv_month.setTextColor(csl1);
			}
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
			break;
		case R.id.tv_month://月累计
			first_polluction.setVisibility(View.VISIBLE);
			MobclickAgent.onEvent(SettingPersonActivity.this, "WeatherMonth");
			yubao_head.setVisibility(View.GONE);
			yuce_lin.setVisibility(View.GONE);
			air_head.setVisibility(View.VISIBLE);
			month_year = 30;
			statistics_search.setVisibility(View.INVISIBLE);
			initCalendar(statistics_start_time, statistics_start_timee,
					statistics_end_time);
			statistics_start_timee.setText(dateMonth);
			// pm10 = "PM10/同比";
			// pm25 = "PM2.5/同比";
			// o3 = "O3/同比";
			// cnt = "优良天数\n/同比";
			// transText(air_headtv1,pm10);
			// transText(air_headtv2,pm25);
			// transText(youliangtianshu,o3);
			// transText(first_polluction,cnt);
			if (preferencesUtil.getQuanXian()) {
				pm10 = "PM10/同比";
				pm25 = "PM2.5/同比";
				o3 = "O3/同比";
				cnt = "优良天数";
				
				air_headtv1.setText(pm10);
				air_headtv2.setText(pm25);
//				youliangtianshu.setText(o3);
				youliangtianshu.setText(cnt);
			} else {
				pm10 = "PM10";
				pm25 = "PM2.5";
				o3 = "O3";
				cnt = "优良天数";
				air_headtv1.setText(pm10);
				air_headtv2.setText(pm25);
//				youliangtianshu.setText(o3);
				youliangtianshu.setText(cnt);
			}
			tishi_xuanze.setVisibility(View.GONE);
			tv_start_date_label.setText("选择月份:");
			ll_end_date.setVisibility(View.INVISIBLE);
			xuanzeshijian.setVisibility(View.VISIBLE);
			tishi.setVisibility(View.GONE);
			isliebiao = 5;
			xuanzeshijian.setVisibility(View.VISIBLE);
			tishi_xuanze.setVisibility(View.GONE);
			tishi_niandu.setVisibility(View.GONE);
			Calendar calbefore2 = Calendar.getInstance();
			
//			youliangtianshu.setVisibility(View.GONE);	
			youliangtian_lin.getChildAt(1).setVisibility(View.VISIBLE);
			first_polluction.setVisibility(View.GONE);
			zhuangkuang.setVisibility(View.GONE);
			liebiao.setBackgroundResource(leftPressDefault);
			center.setBackgroundResource(centerPressDefault);
			yubao.setBackgroundResource(centerPressDefault);
			map.setBackgroundResource(rightPressDefault);
			tv_month.setBackgroundResource(centerPress);
			{
				ColorStateList csl1 = (ColorStateList) resource
						.getColorStateList(R.color.title_press_color);
				ColorStateList csl2 = (ColorStateList) resource
						.getColorStateList(R.color.title_default_color);
				liebiao.setTextColor(csl1);
				center.setTextColor(csl1);
				yubao.setTextColor(csl1);
				map.setTextColor(csl1);
				tv_month.setTextColor(csl2);
			}
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
			break;
		case R.id.statistics_end_time:
			showPopwindow(getDataPick(EndTimeValueCode));
			break;
		case R.id.statistics_start_time:
			showPopwindow(getDataPick(StartTimeValueCode));
			break;
		case R.id.statistics_start_timee:
			if (month_year == 365) {
				showPopwindow(getDataPick(CenterTimeValueCode));
			} else {
				showPopwindow(getDateMonth(MonthTimeValueCode));
			}
			break;
		case R.id.statistics_search:
			MobclickAgent.onEvent(SettingPersonActivity.this,
					"WeatherSelectTime");
			if (month_year == 365) {
				if (dateStar.equals("") || dateEnd.equals("")) {
					ToastUtil.showShort(SettingPersonActivity.this,
							"开始时间或结束时间没有选择");
					return;
				}
				boolean flag = checkTime(dateStarr, dateEnd);
				if (!dateStarr.substring(0, 4).equals(dateEnd.substring(0, 4))) {
					ToastUtil.showShort(SettingPersonActivity.this,
							"只能选择同一年时间段");
					return;
				}
				if (!flag) {
					ToastUtil.showShort(SettingPersonActivity.this,
							"选择时间有误，请重新选择");
					return;
				}
			}
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
			break;
		case R.id.oldActivity:
			Intent intent=new Intent(this,SettingPersonOldActivity.class);
			startActivity(intent);
			break;
		}

	}

	private boolean checkTime(String begin, String end) {
		// TODO Auto-generated method stub
		int result = begin.compareTo(end);
		if (result == 0)
			return false;
		else if (result < 0)
			return true;
		else
			return false;
	}

	private View getDataPick(final int typeCode) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(SettingPersonActivity.this)
				.inflate(R.layout.datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, curYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth - 1);
		day.setLabel("日");
		day.setCyclic(true);
		curYear = Integer.parseInt(date.substring(0, 4));
		curMonth = Integer.parseInt(date.substring(5, 7));
		curDate = Integer.parseInt(date.substring(8));
		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "";
				int dayy = day.getCurrentItem() + 1;
				String dayTime = dayy + "";
				if (dayTime.length() == 1) {
					dayTime = "0" + dayTime;
				}
				if ((month.getCurrentItem() + 1) < 10) {
					str = (year.getCurrentItem() + 1950) + "-" + "0"
							+ (month.getCurrentItem() + 1) + "-" + dayTime;
				} else {
					str = (year.getCurrentItem() + 1950) + "-"
							+ (month.getCurrentItem() + 1) + "-" + dayTime;
				}
				// String str = (year.getCurrentItem() + 1950) + "-"
				// + (month.getCurrentItem() + 1) + "-"
				// + (day.getCurrentItem() + 1);
				Message msg = new Message();

				msg.what = typeCode;
				msg.obj = str;
				handler.sendMessage(msg);
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	private View getDateMonth(final int typeCode) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(SettingPersonActivity.this)
				.inflate(R.layout.datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, curYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		day.setVisibility(View.GONE);
		initDay(curYear, curMonth - 1);
		day.setLabel("日");
		day.setCyclic(true);
		curYear = Integer.parseInt(date.substring(0, 4));
		curMonth = Integer.parseInt(date.substring(5, 7));
		curDate = Integer.parseInt(date.substring(8));
		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = "";
				int dayy = day.getCurrentItem() + 1;
				String dayTime = dayy + "";
				if (dayTime.length() == 1) {
					dayTime = "0" + dayTime;
				}
				if ((month.getCurrentItem() + 1) < 10) {
					str = (year.getCurrentItem() + 1950) + "-" + "0"
							+ (month.getCurrentItem() + 1);
				} else {
					str = (year.getCurrentItem() + 1950) + "-"
							+ (month.getCurrentItem() + 1);
				}

				Message msg = new Message();

				msg.what = typeCode;
				msg.obj = str;
				handler.sendMessage(msg);
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			initDay(n_year, n_month);
		}
	};

	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	// 获取监测站数据
	private class GetYuCeAqiTask extends
			AsyncTask<String, Void, List<YuCeModel>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (!NetUtil.isNetworkConnected(SettingPersonActivity.this)) {
				ToastUtil.showShort(SettingPersonActivity.this, "无网络");
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

		@Override
		protected List<YuCeModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			String url = "";
			url = UrlComponent.getYuBaoURL;
			isliebiao = 4;
			List<YuCeModel> _Result;
			try {
				_Result = search.getYuCeAqi(url);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<YuCeModel> result) {
			super.onPostExecute(result);
			if (null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				if (result == null || result.size() == 0) {
					nodata.setVisibility(View.VISIBLE);
					monitoring_listview.setVisibility(View.GONE);
					return;
				} else {
					yubao_head.setVisibility(View.VISIBLE);
					yuce_lin.setVisibility(View.VISIBLE);
					air_head.setVisibility(View.GONE);
					nodata.setVisibility(View.GONE);
					monitoring_listview.setVisibility(View.VISIBLE);
					yubao_head.setVisibility(View.VISIBLE);
					air_head.setVisibility(View.GONE);
					yubao_tv1.setText(result.get(0).getTrendModels().get(0)
							.getFORECASTTIME());
					yubao_tv2.setText(result.get(0).getTrendModels().get(1)
							.getFORECASTTIME());
					yubao_tv3.setText(result.get(0).getTrendModels().get(2)
							.getFORECASTTIME());
					yubao_tv4.setText(result.get(0).getTrendModels().get(3)
							.getFORECASTTIME());
					yubao_tv5.setText(result.get(0).getTrendModels().get(4)
							.getFORECASTTIME());
					yuCeAdapter = new YuCeAdapter();
					yuCeAdapter.bindData(result);
					monitoring_listview.setAdapter(yuCeAdapter);
				}
			} catch (Exception e) {
				// TODO: handle exception
				nodata.setVisibility(View.VISIBLE);
				monitoring_listview.setVisibility(View.GONE);
			}
		}
	}

	class YuCeAdapter extends BaseAdapter {
		private List<YuCeModel> yuCeModels;

		public void bindData(List<YuCeModel> yuCeModels) {
			this.yuCeModels = yuCeModels;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return yuCeModels.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return yuCeModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			YuCeModel model = yuCeModels.get(position);
			List<ThreeDayAqiTrendModel> trendModels = model.getTrendModels();
			YuBaoViewHolder holder = null;
			if (convertView == null) {
				holder = new YuBaoViewHolder();
				convertView = LayoutInflater.from(SettingPersonActivity.this)
						.inflate(R.layout.yubao, null);
				holder.yubao_city = (TextView) convertView
						.findViewById(R.id.yubao_city);
				holder.yubao_image1 = (TextView) convertView
						.findViewById(R.id.yubao_image1);
				holder.yubao_image2 = (TextView) convertView
						.findViewById(R.id.yubao_image2);
				holder.yubao_image3 = (TextView) convertView
						.findViewById(R.id.yubao_image3);
				holder.yubao_image4 = (TextView) convertView
						.findViewById(R.id.yubao_image4);
				holder.yubao_image5 = (TextView) convertView
						.findViewById(R.id.yubao_image5);
				holder.yubao_image6 = (TextView) convertView
						.findViewById(R.id.yubao_image6);
				holder.yubao_image7 = (TextView) convertView
						.findViewById(R.id.yubao_image7);
				holder.yubao_image8 = (TextView) convertView
						.findViewById(R.id.yubao_image8);
				holder.yubao_image9 = (TextView) convertView
						.findViewById(R.id.yubao_image9);
				holder.yubao_image10 = (TextView) convertView
						.findViewById(R.id.yubao_image10);
				convertView.setTag(holder);
			} else {
				holder = (YuBaoViewHolder) convertView.getTag();
			}
			holder.yubao_city.setText(model.getCity());
			try {
				setResource(holder.yubao_image1,trendModels.get(0).getMINAIRLEVEL(),trendModels.get(0).getMINAIRLEVEL1());
				setResource(holder.yubao_image2,trendModels.get(0).getMAXAIRLEVEL(),trendModels.get(0).getMAXAIRLEVEL1());
				setResource(holder.yubao_image3,trendModels.get(1).getMINAIRLEVEL(),trendModels.get(1).getMINAIRLEVEL1());
				setResource(holder.yubao_image4,trendModels.get(1).getMAXAIRLEVEL(),trendModels.get(1).getMAXAIRLEVEL1());
				setResource(holder.yubao_image5,trendModels.get(2).getMINAIRLEVEL(),trendModels.get(2).getMINAIRLEVEL1());
				setResource(holder.yubao_image6,trendModels.get(2).getMAXAIRLEVEL(),trendModels.get(2).getMAXAIRLEVEL1());
				setResource(holder.yubao_image7,trendModels.get(3).getMINAIRLEVEL(),trendModels.get(3).getMINAIRLEVEL1());
				setResource(holder.yubao_image8,trendModels.get(3).getMAXAIRLEVEL(),trendModels.get(3).getMAXAIRLEVEL1());
				setResource(holder.yubao_image9,trendModels.get(4).getMINAIRLEVEL(),trendModels.get(4).getMINAIRLEVEL1());
				setResource(holder.yubao_image10,trendModels.get(4).getMAXAIRLEVEL(),trendModels.get(4).getMAXAIRLEVEL1());
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
		}

		private void setResource(TextView textview, int level, String level1) {
			// TODO Auto-generated method stub
			MyLog.i(level1);
			textview.setBackgroundResource(tranImg(level));
			if (level1.contains("*")) {
				MyLog.i("add *");
				textview.setTextColor(Color.WHITE);
				textview.setText("*");
			}else{
				textview.setText("");
			}
		}
	}

	static class YuBaoViewHolder {
		private TextView yubao_city;
		private TextView yubao_image1;
		private TextView yubao_image2;
		private TextView yubao_image3;
		private TextView yubao_image4;
		private TextView yubao_image5;
		private TextView yubao_image6;
		private TextView yubao_image7;
		private TextView yubao_image8;
		private TextView yubao_image9;
		private TextView yubao_image10;
	}

	public String tranStatus(int m) {
		if (m == 1) {
			return yubaoStatus[0];
		} else if (m == 2) {
			return yubaoStatus[1];
		} else if (m == 3) {
			return yubaoStatus[2];
		} else if (m == 4) {
			return yubaoStatus[3];
		} else if (m == 5) {
			return yubaoStatus[4];
		} else if (m == 6) {
			return yubaoStatus[5];
		} else {
			return yubaoStatus[0];
		}
	}

	public int tranImg(int m) {
		if (m == 1) {
			return yubaoImg[0];
		} else if (m == 2) {
			return yubaoImg[1];
		} else if (m == 3) {
			return yubaoImg[2];
		} else if (m == 4) {
			return yubaoImg[3];
		} else if (m == 5) {
			return yubaoImg[4];
		} else if (m == 6) {
			return yubaoImg[5];
		} else {
			return yubaoImg[0];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public SharedPreferencesUtil preferencesUtil;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WeatherActivity");
		MobclickAgent.onResume(this);
		isShowOld=SharedPreferencesUtil.getInstance(this).getoldshow();
//		isShowOld=preferencesUtil.getoldshow();
		tishi_niandu.setVisibility(View.GONE);
		if (isliebiao != 4) {
			getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
		} else {
			getYuCeAqiTask = new GetYuCeAqiTask();
			getYuCeAqiTask.execute("");
		}
//		MyLog.i("onResume");
//		if (isShowOld) {
//			oldbutton.setOnClickListener(this);
//			oldbutton.setVisibility(View.VISIBLE);
//		}else {
//			oldbutton.setOnClickListener(null);
//			oldbutton.setVisibility(View.GONE);
//		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WeatherActivity");
		MobclickAgent.onPause(this);
	}

	private void transText(TextView tv, String data) {
		SpannableString styledText;
		styledText = new SpannableString(data);
		styledText.setSpan(new TextAppearanceSpan(SettingPersonActivity.this,
				R.style.stylenormal), 0, data.indexOf("/") + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(styledText, TextView.BufferType.SPANNABLE);
		styledText.setSpan(new TextAppearanceSpan(SettingPersonActivity.this,
				R.style.stylebefore), data.indexOf("/") + 1, data.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(styledText, TextView.BufferType.SPANNABLE);
	}
}