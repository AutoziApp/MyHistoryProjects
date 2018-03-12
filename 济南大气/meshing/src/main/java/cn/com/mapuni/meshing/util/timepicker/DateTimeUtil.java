package cn.com.mapuni.meshing.util.timepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import cn.com.mapuni.meshingtotal.R;
import com.tianditu.android.maps.MapView.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class DateTimeUtil {
	private Context mContext;
	private PopupWindow menuWindow;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private String date = "";

	public DateTimeUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/**
	 * 初始化时间
	 */
	public void initCalendar(TextView textView) {
		Date datee = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(datee);
		calendar.add(calendar.DATE, 0);// 把日期往后增加一天.整数往后推,负数往前移动
		datee = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		date = formatter.format(datee);

		textView.setText(date);
	}

	public void showPopwindow(TextView textView, final Dialog dialog) {
		View view = getDateDay(textView);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.alpha = 0.4f;
		dialog.getWindow().setAttributes(lp);
		menuWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		// 使其聚集
		menuWindow.setFocusable(true);
		// 设置允许在外点击消失
		menuWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		menuWindow.setBackgroundDrawable(new BitmapDrawable());

		menuWindow.showAtLocation(dialog.getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				lp.alpha = 1;
				dialog.getWindow().setAttributes(lp);
			}
		});
	}
	
	private View getDateDay(final TextView textView) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(mContext).inflate(R.layout.datapick, null);

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
					str = (year.getCurrentItem() + 1950) + "-" + "0" + (month.getCurrentItem() + 1) + "-" + dayTime;
				} else {
					str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + dayTime;
				}

				textView.setText(str);
				
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

	private View getDateMonth(final TextView textView) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(mContext).inflate(R.layout.datapick, null);

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
					str = (year.getCurrentItem() + 1950) + "-" + "0" + (month.getCurrentItem() + 1);
				} else {
					str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1);
				}

				textView.setText(str);
				
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

	private View getDateYear(final TextView textView) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = LayoutInflater.from(mContext).inflate(R.layout.datapick, null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 20, 200, 20);
		year = (WheelView) view.findViewById(R.id.year);
		year.setLayoutParams(params);
		year.setAdapter(new NumericWheelAdapter(1950, curYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setVisibility(View.GONE);
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
					str = (year.getCurrentItem() + 1950) + "";
				} else {
					str = (year.getCurrentItem() + 1950) + "";
				}

				textView.setText(str);
				
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

}
