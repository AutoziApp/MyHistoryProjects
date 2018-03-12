package com.mapuni.android.helper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.mapuni.android.base.Global;

public class EnvManualController {

	private final Context mContext;
	private DialogInterface mDialogInterface;
	private Window mWindow;

	public static final int HANDLE_EnvManual_init = 100;
	public static final int HANDLE_EnvManual_exit = 101;

	public final static int HBFL = 501;// 环保法律
	public final static int HBBZ = 502;// 环保标准
	public final static int ZDWJ = 503;// 制度文件
	public final static int WHP = 504;// 危化品
	public final static int YJYA = 505;// 应急预案
	public final static int ZJK = 506;// 专家库
	public final static int ADDRBOOKS = 507;// 通录录
	public final static int DETAILINFO = 600;// 详细信息页面

	private EnvManual_Item_Dialog Env_Item_Dialog = null;
	private EnvManual_Detail_Dialog Env_Detail_Dialog = null;
	private EnvManualDialog EnvDialog = null;

	private HashMap<String, Object> map;
	private int itemCode;

	public EnvManualController() {
		mContext = Global.getGlobalInstance().getApplicationContext();
		mHandler.sendEmptyMessage(HANDLE_EnvManual_init);
	}

	private static class SingletonHolder {

		static final EnvManualController singleEnvManualC = new EnvManualController();
	}

	public static EnvManualController getInstance() {
		return SingletonHolder.singleEnvManualC;
	}

	public void EnvManualInit() {
		/** Dialog设置全屏 */
		// Env_Item_Dialog = new EnvManual_Item_Dialog(mContext,
		// android.R.style.Theme_NoTitleBar);
		Env_Item_Dialog = new EnvManual_Item_Dialog(mContext);
		Env_Item_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	}

	public void EnvManualExit() {
		EnvDialog = null;
	}

	public void EnvManualDetailInit() {
		/** Dialog设置全屏 */
		// Env_Item_Dialog = new EnvManual_Item_Dialog(mContext,
		// android.R.style.Theme_NoTitleBar);
		Env_Detail_Dialog = new EnvManual_Detail_Dialog(mContext);
		Env_Detail_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_EnvManual_init:
				EnvManualInit();
				break;

			case HANDLE_EnvManual_exit:
				EnvManualExit();
				break;

			/** 环保法律 */
			case HBFL:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.HBFL);
				break;
			/** 环保标准 */
			case HBBZ:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.HBBZ);
				break;
			/** 制度文件 */
			case ZDWJ:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.ZDWJ);
				break;
			/** 危化品 */
			case WHP:
				Env_Item_Dialog.show(EnvManualController.WHP);
				break;
			/** 应急预案 */
			case YJYA:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.YJYA);
				break;
			/** 专家库 */
			case ZJK:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.ZJK);
				break;
			/** 详细页面信息 */
			case DETAILINFO:
				EnvManualDetailInit();
				Bundle bundle = msg.getData();
				map = (HashMap<String, Object>) bundle.getSerializable("map");
				itemCode = bundle.getInt("itemCode");
				try {
					Env_Detail_Dialog.showView(map, itemCode);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Env_Detail_Dialog.show();
			}

		}
	};

	public void openOtherDialog(int DialogCode, HashMap<String, Object> map,
			int ItemCode) {
		this.map = map;
		Message msg = mHandler.obtainMessage();
		msg.what = DETAILINFO;
		Bundle bundle = new Bundle();
		bundle.putSerializable("map", map);
		bundle.putInt("itemCode", ItemCode);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

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
