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

	public final static int HBFL = 501;// ��������
	public final static int HBBZ = 502;// ������׼
	public final static int ZDWJ = 503;// �ƶ��ļ�
	public final static int WHP = 504;// Σ��Ʒ
	public final static int YJYA = 505;// Ӧ��Ԥ��
	public final static int ZJK = 506;// ר�ҿ�
	public final static int ADDRBOOKS = 507;// ͨ¼¼
	public final static int DETAILINFO = 600;// ��ϸ��Ϣҳ��

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
		/** Dialog����ȫ�� */
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
		/** Dialog����ȫ�� */
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

			/** �������� */
			case HBFL:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.HBFL);
				break;
			/** ������׼ */
			case HBBZ:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.HBBZ);
				break;
			/** �ƶ��ļ� */
			case ZDWJ:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.ZDWJ);
				break;
			/** Σ��Ʒ */
			case WHP:
				Env_Item_Dialog.show(EnvManualController.WHP);
				break;
			/** Ӧ��Ԥ�� */
			case YJYA:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.YJYA);
				break;
			/** ר�ҿ� */
			case ZJK:
				EnvManualInit();
				Env_Item_Dialog.show(EnvManualController.ZJK);
				break;
			/** ��ϸҳ����Ϣ */
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
	 * ��Dialog
	 * */
	public void openDialog(int DialogCode) {
		mHandler.sendEmptyMessage(DialogCode);
	}

	/**
	 * ����Dialog
	 * */
	public void hideDialog(int DialogCode) {
		mHandler.sendEmptyMessage(DialogCode);
	}

}
