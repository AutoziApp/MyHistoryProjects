package com.yutu.car.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.yutu.car.R;


public class DataPickDialogUtil {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Activity activity;
	private AlertDialog ad;

	/**
	 * 时间选择器工具类
	 * 
	 * @author tianfy
	 *
	 * @version 创建时间：2017-3-9 下午12:52:41
	 * 
	 */
	public DataPickDialogUtil(Activity activity) {
		this.activity = activity;
	}

	public AlertDialog dateTimePicKDialog(final TextView inputDate) {

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

	public AlertDialog TimePicKDialog(final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater()
				.inflate(R.layout.tfy_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		datePicker.setVisibility(View.VISIBLE);
		timePicker.setVisibility(View.VISIBLE);
		timePicker.setIs24HourView(true);
		ad = new AlertDialog.Builder(activity)
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
						inputDate.setText(sbDataTime.toString());
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(DataUtils.getToadyZeroTime());
					}
				}).show();
		return ad;
	}
}
