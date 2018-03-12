package com.mapuni.android.login;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import com.mapuni.android.base.interfaces.IController;
import com.mapuni.android.base.util.Apn;

public class LoginController implements IController {
	private final int style = android.R.style.Theme_NoTitleBar;
	private static Context mContext;
	private static Handler mainHandler;

	public static final int HANDLE_Login_init = 100;
	public static final int HANDLE_Login_exit = 101;
	public final int YUTU_HBT_LOGIN = 105;

	/** 打开登陆flash页面 */
	public final int HANDLE_OPEN_LoginFlashDialog = 200;

	/** 打开登陆页面 */
	public final static int HANDLE_OPEN_LoginDialog = 201;

	/** 关闭登陆flash页面 */
	public final static int HANDLE_HIDE_LoginFlashDialog = 300;

	/** 显示主页面登陆页面 */
	public final static int HANDLE_HIDE_LoginDialog = 301;

	/** 关闭登陆页面 */
	public final static int HANDLE_HIDE_LoginDismiss = 302;

	private LoginFlashDialog login_FlashDialog = null;
	private LoginDialog login_Dialog = null;
	private static LoginController singleLoginC = null;

	public LoginController(Context context) {
		mContext = context;
		mHandler.sendEmptyMessage(HANDLE_Login_init);
	}

	public LoginController(Context context, DialogInterface di, Window window) {
		mContext = context;
		mHandler.sendEmptyMessage(HANDLE_Login_init);
	}

	public LoginController(Context context, Handler handler) {
		mContext = context;
		mainHandler = handler;
		mHandler.sendEmptyMessage(HANDLE_Login_init);

	}

	public static LoginController getInstance(Context context) {
		mContext = context;
		if (singleLoginC == null) {
			singleLoginC = new LoginController(mContext);
		}
		return singleLoginC;
	}

	public static LoginController getInstance(Context context, Handler handler) {
		mContext = context;
		mainHandler = handler;
		if (singleLoginC == null) {
			singleLoginC = new LoginController(mContext, mainHandler);
		}
		return singleLoginC;
	}

	@Override
	public boolean ModleInit() {
		/** 登陆界面 */
		login_Dialog = new LoginDialog(mContext, style);
		/** 登陆Flash界面 */
		login_FlashDialog = new LoginFlashDialog(mContext, style);
		return true;
	}

	@Override
	public void ModleEixt() {
		login_Dialog = null;
		login_FlashDialog = null;
	}

	@Override
	public void OpenMainDialog() {
	}

	@Override
	public void ModleCancel() {
	}

	@Override
	public void ShowDialog() {
	}

	@Override
	public void HideDialog() {
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_Login_init:
				ModleInit();
				break;
			case HANDLE_Login_exit:
				ModleEixt();
				break;
			/** 登陆开始的Flash模块：打开与隐藏 */
			case HANDLE_OPEN_LoginFlashDialog:
				login_FlashDialog.show();
				break;
			case HANDLE_HIDE_LoginFlashDialog:
				login_FlashDialog.dismiss();
				break;
			/** 登陆开始的模块：打开与隐藏 */
			case HANDLE_OPEN_LoginDialog:
				login_Dialog.show();
				
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle("APN设置提示")
						.setMessage("是否切换到专用网络  ?")
						.setPositiveButton("设置", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent apnSetIntent = new Intent(Settings.ACTION_APN_SETTINGS);
								mContext.startActivity(apnSetIntent);
								dialog.dismiss();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();

				
				Log.e("hello", "登录");
				hideDialog(LoginController.HANDLE_HIDE_LoginFlashDialog);
				break;
			case HANDLE_HIDE_LoginDialog:
				Message message = mainHandler.obtainMessage();
				message.what = YUTU_HBT_LOGIN;
				mainHandler.sendMessage(message);
				break;
			case HANDLE_HIDE_LoginDismiss:
				login_Dialog.dismissLoginDialog();
				login_Dialog.dismiss();
				break;
			}

		}
	};

	/**
	 * 打开Dialog
	 * */
	public void openDialog(int DialogCode) {
		mHandler.sendEmptyMessage(DialogCode);
	}

	/**
	 * 隐藏Dialog
	 * */
	public void hideDialog(int DialogCode) {
		mHandler.sendEmptyMessage(DialogCode);
	}
}
