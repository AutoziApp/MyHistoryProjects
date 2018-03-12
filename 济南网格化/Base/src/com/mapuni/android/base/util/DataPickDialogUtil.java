package com.mapuni.android.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mapuni.android.base.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;



/**
 * 时间选择器工具类
 * 
 * @author tianfy
 * 
 * @version 创建时间：2017-3-9 下午12:52:41
 */
public class DataPickDialogUtil {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Activity activity;
	private AlertDialog ad;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param
	 * 
	 */
	public DataPickDialogUtil(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	@SuppressLint("NewApi") public AlertDialog dateTimePicKDialog(final TextView inputDate) {

		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater()
				.inflate(R.layout.tfy_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		datePicker.setMinDate(0);
		datePicker.setVisibility(View.VISIBLE);
		timePicker.setVisibility(View.GONE);
		ad = new AlertDialog.Builder(activity)
				.setTitle("请设置时间")
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						StringBuffer sbData = new StringBuffer().append(String
								.format("%d-%02d-%02d", datePicker.getYear(),
										datePicker.getMonth() + 1,
										datePicker.getDayOfMonth()));
						inputDate.setText(sbData.toString());
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(new SimpleDateFormat("yyyy-MM-dd")
								.format(new Date()));
					}
				}).show();
		return ad;
	}

	@SuppressLint("NewApi")
	public AlertDialog TimePicKDialog(final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater()
				.inflate(R.layout.tfy_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		datePicker.setVisibility(View.VISIBLE);
		timePicker.setVisibility(View.VISIBLE);
		// 是否使用24小时制
		timePicker.setIs24HourView(true);
		ad = new AlertDialog.Builder(activity,AlertDialog.THEME_HOLO_LIGHT)
				.setTitle("请设置时间")
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						StringBuffer sbDataTime = new StringBuffer();
						sbDataTime.append(String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						sbDataTime.append(" "+String.format("%02d:%02d",
								timePicker.getCurrentHour(),
								timePicker.getCurrentMinute()));
						sbDataTime.append(":00");
						inputDate.setText(sbDataTime.toString());
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(DataUtils.getToadyZeroTime()+":00");
					}
				}).show();
		return ad;
	}
	
	
	/**
	 * 弹出日期时间选择框方法
	 * 只获取年月
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog mothTimePicKDialog(final TextView inputDate) {

		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater()
				.inflate(R.layout.tfy_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		datePicker.setVisibility(View.VISIBLE);
		timePicker.setVisibility(View.GONE);
		ad = new AlertDialog.Builder(activity)
				.setTitle("请设置时间")
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						StringBuffer sbData = new StringBuffer().append(String
								.format("%d-%02d", datePicker.getYear(),
										datePicker.getMonth() + 1));
						inputDate.setText(sbData.toString());
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(new SimpleDateFormat("yyyy-MM")
								.format(new Date()));
					}
				}).show();
		return ad;
	}
}
