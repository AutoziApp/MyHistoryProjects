package com.mapuni.android.taskmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.interfaces.IController;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.enforcement.SiteEvidenceActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
/**
 * ������������
 * @author wsc
 *
 */
public class TaskManagerController implements IController {

	private static Context mContext;

	/** Dialog��ʾ��ʽ ȫ����ʾ */
	private final int style = android.R.style.Theme_NoTitleBar;

	/** ���Ƴ�ʼ�� */
	public final int TASK_MANAGER_INIT = 100;

	/** �����˳� */
	public final static int TASK_MANAGER_EXIT = 101;
     
	/** ������� */
	public final int CREATE_TASK = 200;
	/** �ϴ��ѱ��汾������ */
	public final int UPLOAD_TASK = 201;
	/** ѡ�������ҵ */
	public final int SECECT_ADD_COMPANY=202;
	/** �����ѯ�б� */
	public final int TASK_SEARCH_RESULT_LIST=203;
	/** �����ѯ��ϸ */
	public final int TASK_SEARCH_RESULT_DETAIL=204;
	
	public static TaskManagerController singleHelperC = null;

	/** ��ҵId */
	private String qyid;
	/** ������ */
	private String rwbh;
	/** ��� */
	private String type;
	/** �������*/
	private RWXX rwxx;

	//private CreateTaskDialog createTaskDialog=null;

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			qyid = bundle.getString("qyid");
			switch (msg.what) {

			case TASK_MANAGER_INIT:
				ModleInit();
				break;

			case TASK_MANAGER_EXIT:
				ModleEixt();
				break;

				// ����������
			case CREATE_TASK:
			/*	createTaskDialog=new CreateTaskDialog(mContext,style);
				createTaskDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				createTaskDialog.showWithPara("");
				createTaskDialog.show();*/
				break;
				//�ϴ��Ѿ��ݴ汾�ص�����
			case UPLOAD_TASK:
				/*createTaskDialog=new CreateTaskDialog(mContext,style);
				createTaskDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				createTaskDialog.showWithPara(qyid);
				createTaskDialog.show();*/
				break;
			case SECECT_ADD_COMPANY:
				/*SelectAddCompanyDialog selectAddCompanyDialog=new SelectAddCompanyDialog(mContext,style);
				selectAddCompanyDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				rwbh = bundle.getString("rwbh");
				selectAddCompanyDialog.showWithPara(rwbh);
				selectAddCompanyDialog.show();*/
				break;
			case TASK_SEARCH_RESULT_LIST:
				TaskSearchResultListDialog taskSearchResultListDialog=new TaskSearchResultListDialog(mContext,style);
				/*taskSearchResultListDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);*/
				ArrayList<HashMap<String, Object>> getSearchConditionsList=(ArrayList<HashMap<String, Object>>)bundle.getSerializable("SearchConditionsList");
				taskSearchResultListDialog.showWithPara(getSearchConditionsList);
				taskSearchResultListDialog.show();
				break;
			case TASK_SEARCH_RESULT_DETAIL:
				TaskSearchResultDetailDialog taskSearchResultDetailDialog=new TaskSearchResultDetailDialog(mContext,style);
//				taskSearchResultDetailDialog.getWindow().setType(
//						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				rwxx =(RWXX)bundle.getSerializable("RWXX");
				String rwbh_byk=bundle.getString("rwbh");
				String letaskid=bundle.getString("letaskid");
				String rwzt=bundle.getString("rwzt");
				String rwlx=bundle.getString("rwlx");
				
				taskSearchResultDetailDialog.showWithParaObject(rwxx,rwbh_byk,letaskid,rwzt,rwlx);
				
				taskSearchResultDetailDialog.show();
				break;
			}

		}
	};

	public static TaskManagerController getInstance() {
		return SingletonHolder.singleHelperC;
	}
	public static TaskManagerController getInstance(Context context) {
		mContext = context;
		singleHelperC = new TaskManagerController(context);
		return singleHelperC;
	}
	private static class SingletonHolder {

		static final TaskManagerController singleHelperC = new TaskManagerController();
	}

	public TaskManagerController() {
		mContext = Global.getGlobalInstance().getApplicationContext();
		
		mHandler.sendEmptyMessage(TASK_MANAGER_INIT);
	}


	public TaskManagerController(Context context) {
		mHandler.sendEmptyMessage(TASK_MANAGER_INIT);
	}


	@Override
	public boolean ModleInit() {
		return false;
	}

	@Override
	public void OpenMainDialog() {

	}

	@Override
	public void ModleCancel() {

	}

	@Override
	public void ModleEixt() {

	}

	@Override
	public void ShowDialog() {

	}

	@Override
	public void HideDialog() {

	}
	public void openView(int State) {
		mHandler.sendEmptyMessage(State);
	}

	public void openParaView(int DialogCode, String qyid, String rwbh) {
		
		Message msg = mHandler.obtainMessage();
		msg.what = DialogCode;
		Bundle bundle = new Bundle();
		bundle.putString("qyid", qyid);
		bundle.putString("rwbh", rwbh);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
	public void openArrayListParaView(int DialogCode,ArrayList<HashMap<String, Object>> getSearchCondition) {
		Message msg = mHandler.obtainMessage();
		msg.what = DialogCode;
		Bundle bundle = new Bundle();
		bundle.putSerializable("SearchConditionsList", getSearchCondition);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
	public void openObjectParaView(int DialogCode,Serializable object,String rwbh,String taskid,String rwzt,String rwlx) {
		Message msg = mHandler.obtainMessage();
		msg.what = DialogCode;
		Bundle bundle = new Bundle();
		bundle.putSerializable("RWXX", object);
		bundle.putString("rwbh", rwbh);
		bundle.putString("letaskid", taskid);
		bundle.putString("rwzt", rwzt);
		bundle.putString("rwlx", rwlx);
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}
}
