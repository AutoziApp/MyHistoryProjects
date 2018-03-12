package com.mapuni.android.base.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Toast;

public class OtherTools {
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showSQLLog(String msg) {
		LogUtil.e("mapuni", msg);
	}

	public static void showLog(String msg) {
		Log.e("mapuni", msg);
	}

	public static void showExceptionLog(String msg) {
		Log.e("mapuni", "---" + msg + "---");
	}

	public static void showAlertDialog(Context context, CharSequence title, CharSequence msg, OnClickListener positiveListener, CharSequence tip, OnClickListener neutralListener) {
		getAlertDialog(context, title, msg, positiveListener, tip, neutralListener).show();
	}

	public static void showAlertDialog(Context context, CharSequence msg, OnClickListener listener) {
		showAlertDialog(context, "提示", msg, listener, null, null);
	}

	public static Builder getAlertDialog(Context context, CharSequence title, CharSequence msg, OnClickListener positiveListener, CharSequence tip, OnClickListener neutralListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", positiveListener);
		builder.setNegativeButton("取消", null);
		builder.setNeutralButton(tip, neutralListener);
		return builder;
	}
}
