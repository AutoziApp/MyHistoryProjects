package com.mapuni.android.helper;

import java.io.File;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.R;
import com.mapuni.android.base.interfaces.IController;

public class HelperController implements IController {

	private final Context mContext;
	private DialogInterface mDialogInterface;
	private Window mWindow;

	public static final int HANDLE_Helper_init = 100;
	public static final int HANDLE_Helper_exit = 101;

	public final static int HANDLE_OPEN_HelperDialog = 200;
	public final static int HANDLE_OPEN_DataSynDialog = 201;
	public final static int HANDLE_OPEN_AddressBookDialog = 202;
	public final static int HANDLE_OPEN_EnvManualDialog = 203;
	public final static int HANDLE_OPEN_LawKnowAllDialog = 204;
	public final static int HANDLE_OPEN_SettingDialog = 205; 
	//public final static int HANDLE_OPEN_AddBusinessActivity=206;


	public final static int HANDLE_HIDE_HelperDialog = 300;
	public final static int HANDLE_HIDE_DataSynDialog = 301;
	public final static int HANDLE_HIDE_AddressBookDialog = 302;
	public final static int HANDLE_HIDE_EnvManualDialog = 303;
	public final static int HANDLE_HIDE_LawKnowAllDialog = 304;
	public final static int HANDLE_HIDE_SettingDialog = 305;
	public final static int HANDLE_HIDE_EnvManualDetailDialog = 306;
	//public final static int HANDLE_HIDE_AddBusinessActivity=307;

	private HelperDialog Help_Dialog = null;
	private DataSynDialog DataSyn_Dialog = null;
	private AddressBookDialog AddressBook_Dialog = null;
	private EnvManualDialog EnvManual_Dialog = null;
	private LawKnowAllDialog LawKnow_Dialog = null;
	private SettingDialog Setting_Dialog = null;
	private LawRegulationDialog LawRegulationDialog=null;
	private AddBusinessActivity AddBusinessActivity=null;


	public HelperController() {
		mContext = Global.getGlobalInstance().getApplicationContext();
		mHandler.sendEmptyMessage(HANDLE_Helper_init);
	}

	private static class SingletonHolder {

		static final HelperController singleHelperC = new HelperController();
	}

	public static HelperController getInstance() {
		return SingletonHolder.singleHelperC;
	}

	@Override
	public boolean ModleInit() {
		Help_Dialog = new HelperDialog(mContext);
		Help_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		DataSyn_Dialog = new DataSynDialog(mContext);
		DataSyn_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		AddressBook_Dialog = new AddressBookDialog(mContext);
		AddressBook_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);



		EnvManual_Dialog = new EnvManualDialog(mContext);
		EnvManual_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		LawKnow_Dialog = new LawKnowAllDialog(mContext);
		LawKnow_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		Setting_Dialog = new SettingDialog(mContext);
		Setting_Dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		LawRegulationDialog =new LawRegulationDialog(mContext);
		LawRegulationDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		AddBusinessActivity=new AddBusinessActivity();

		return true;
	}

	@Override
	public void OpenMainDialog() {
		mHandler.sendEmptyMessage(HANDLE_OPEN_HelperDialog);
	}

	@Override
	public void ModleCancel() {

	}
	
    
	public void ModleEixt() {
		Help_Dialog = null;
		DataSyn_Dialog = null;
		AddressBook_Dialog = null;
		EnvManual_Dialog = null;
		LawKnow_Dialog = null;
		Setting_Dialog = null;
		LawRegulationDialog=null;
		AddBusinessActivity=null;
	}

	public void ShowDialog() {
	}

	public void HideDialog() {
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_Helper_init:
				ModleInit();
				break;

			case HANDLE_Helper_exit:
				ModleEixt();
				break;

				/** 帮助模块：打开与隐藏 */
			case HANDLE_OPEN_HelperDialog:
				Help_Dialog.show();
				break;
			case HANDLE_HIDE_HelperDialog:
				Help_Dialog.cancel();
				break;

				/** 数据同步：打开与隐藏 */
			case HANDLE_OPEN_DataSynDialog:
				DataSyn_Dialog.show();
				break;
			case HANDLE_HIDE_DataSynDialog:
				DataSyn_Dialog.cancel();
				break;

				/** 通讯录：打开与隐藏 */
			case HANDLE_OPEN_AddressBookDialog:
				AddressBook_Dialog.show();
				break;
			case HANDLE_HIDE_AddressBookDialog:
				AddressBook_Dialog.cancel();
				break;

				/**添加企业*/
		/*	case HANDLE_OPEN_AddBusinessActivity:
				addCompany();
				break;
			case HANDLE_HIDE_AddBusinessActivity:
				//AddBusinessActivity.finish();
				break;*/

				/** 环保手册：打开与隐藏 *//*
			case HANDLE_OPEN_EnvManualDialog:
				EnvManual_Dialog.show();
				break;
			case HANDLE_HIDE_EnvManualDialog:
				EnvManual_Dialog.cancel();
				break;*/
				/** 法规标准：打开与隐藏 */
			case HANDLE_OPEN_EnvManualDialog:
				String fjpath = Global.HJJCZFSC_PATH;
				File files = new File(fjpath);
				String fjs[] = files.list();
				if (fjs == null) {
					Toast.makeText(mContext, "法律法规标准文件夹为空！", 0)
					.show();
					break;
				}else{
					LawRegulationDialog.show();
					break;
				}
			case HANDLE_HIDE_EnvManualDialog:
				LawRegulationDialog.cancel();
				break;


				/** 执法百事通：打开与隐藏 */
			case HANDLE_OPEN_LawKnowAllDialog:
				LawKnow_Dialog.show();
				break;
			case HANDLE_HIDE_LawKnowAllDialog:
				LawKnow_Dialog.cancel();
				break;

				/** 系统管理：打开与隐藏 */
			case HANDLE_OPEN_SettingDialog:
				Setting_Dialog.show();
				break;
			case HANDLE_HIDE_SettingDialog:
				Setting_Dialog.cancel();
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
