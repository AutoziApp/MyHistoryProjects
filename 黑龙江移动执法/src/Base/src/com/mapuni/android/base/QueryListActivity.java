package com.mapuni.android.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mapuni.android.base.business.BaseDialogFactory;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: QueryListActivity.java Description: ���в�ѯ���ܵ��б�
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-6 ����03:43:31
 */
public class QueryListActivity extends ListActivity {
	/** ���� */
	private IQuery queryObj;
	/** ��Ų�ѯ��ʽ */
	@SuppressWarnings("unused")
	private List<HashMap<String, Object>> extraInfoForQuery;
	/** ��ѯ������ */
	SqliteUtil sqlite = SqliteUtil.getInstance();
	BaseDialogFactory dialogfactory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setSearchButtonVisiable(true);
		queryImg.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				QueryListActivity.this.showResearchDialog();
			}
		});
		queryObj = (IQuery) businessObj;
		extraInfoForQuery = queryObj.getStyleQuery(QueryListActivity.this);
		if (extraInfoForQuery.size()==0) {
			queryImg.setVisibility(View.GONE);
		}
	
	}

	/**
	 * ��д���ҷ���
	 */
	@Override
	public void queryDate() {
		showedittask = true;
	//	QueryListActivity.this.showResearchDialog();
	}

	/**
	 * Description: ��ʾ��ѯ��dialog
	 * 
	 * void
	 * 
	 * @author ����� Create at: 2012-12-6 ����03:46:39
	 */
	protected void showResearchDialog() {

		if (showedittask) {
			/** ��ʼ������bundle����bundleװ��Intent������תҳ�� */
			Bundle bundle = new Bundle();
			bundle.putBoolean("IsShowTitle", true);
			bundle.putString("TitleText", "����༭");
			Intent _Intent = new Intent();
			_Intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.taskmanager.TaskEditActivity");
			_Intent.putExtras(bundle);
			startActivity(_Intent);
		} else {
			queryObj = (IQuery) businessObj;
			extraInfoForQuery = queryObj.getStyleQuery(QueryListActivity.this);
			if (extraInfoForQuery.size()==0) {
				queryImg.setVisibility(View.GONE);
			}
			
		for (int i = 0; i < extraInfoForQuery.size(); i++) {
			String extrainfo = extraInfoForQuery.get(i).toString();
			if (extrainfo.contains("����")) {
				AlertDialog.Builder builder = new Builder(QueryListActivity.this);
				builder.setTitle("�������ѯΣ��Ʒ����");    //���öԻ������
				builder.setIcon(R.drawable.icon_mapuni_white);  
				final EditText edit = new EditText(QueryListActivity.this);
				builder.setView(edit);
				builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String input = edit.getText().toString().trim();
					HashMap<String, Object> returnValueMap = new HashMap<String, Object>();
					returnValueMap.put("cname", "1' or cname like '%"+input+"%");
					businessObj.setListScrolltimes(1);
					if (Global.getGlobalInstance().isSearchCondition()) {
						returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
						Global.getGlobalInstance().setSearchCondition(false);
					}
					rewriteSrarch(returnValueMap);
					dialog.dismiss();
				}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				});
				
				builder.setCancelable(true);    //���ð�ť�Ƿ���԰����ؼ�ȡ��,false�򲻿���ȡ��
				AlertDialog dialog = builder.create();  //�����Ի���
				dialog.setCanceledOnTouchOutside(true); //���õ�����ʧȥ�����Ƿ�����,��������������ط��Ƿ�����
		
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}else if (extrainfo.contains("����")) {
				AlertDialog.Builder builder = new Builder(QueryListActivity.this);
				builder.setTitle("������ר������");    //���öԻ������
				builder.setIcon(R.drawable.icon_mapuni_white);  
				final EditText edit = new EditText(QueryListActivity.this);
				builder.setView(edit);
				builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String input = edit.getText().toString().trim();
					HashMap<String, Object> returnValueMap = new HashMap<String, Object>();
					returnValueMap.put("exp_name", "1' or exp_name like '%"+input+"%");
					returnValueMap.put("regionname2", "");
					businessObj.setListScrolltimes(1);
					if (Global.getGlobalInstance().isSearchCondition()) {
						returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
						Global.getGlobalInstance().setSearchCondition(false);
					}
					rewriteSrarch(returnValueMap);
					dialog.dismiss();
				}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				});
				
				builder.setCancelable(true);    //���ð�ť�Ƿ���԰����ؼ�ȡ��,false�򲻿���ȡ��
				AlertDialog dialog = builder.create();  //�����Ի���
				dialog.setCanceledOnTouchOutside(true); //���õ�����ʧȥ�����Ƿ�����,��������������ط��Ƿ�����
		
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}else if (extrainfo.contains("�ļ�����")) {
				AlertDialog.Builder builder = new Builder(QueryListActivity.this);
				builder.setTitle("�������ļ�����");    //���öԻ������
				builder.setIcon(R.drawable.icon_mapuni_white);  
				final EditText edit = new EditText(QueryListActivity.this);
				builder.setView(edit);
				builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String input = edit.getText().toString().trim();
					HashMap<String, Object> returnValueMap = new HashMap<String, Object>();
					returnValueMap.put("bt", "1' or bt like '%"+input+"%");
					returnValueMap.put("regionname2", "");
					businessObj.setListScrolltimes(1);
					if (Global.getGlobalInstance().isSearchCondition()) {
						returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
						Global.getGlobalInstance().setSearchCondition(false);
					}
					rewriteSrarch(returnValueMap);
					dialog.dismiss();
				}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
				});
				
				builder.setCancelable(true);    //���ð�ť�Ƿ���԰����ؼ�ȡ��,false�򲻿���ȡ��
				AlertDialog dialog = builder.create();  //�����Ի���
				dialog.setCanceledOnTouchOutside(true); //���õ�����ʧȥ�����Ƿ�����,��������������ط��Ƿ�����
		
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}else if(extrainfo.contains("����")){
				dialogfactory = new BaseDialogFactory(QueryListActivity.this, "", "", queryObj);
				AlertDialog.Builder builder = dialogfactory.showResearchDialog();
				builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
						HashMap<String, Object> returnValueMap = dialogfactory.BuildQueryValue();
						businessObj.setListScrolltimes(1);
						if (Global.getGlobalInstance().isSearchCondition()) {
							returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
							Global.getGlobalInstance().setSearchCondition(false);
						}
						rewriteSrarch(returnValueMap);
	
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
	
					}
				});
				AlertDialog alertDialog = builder.create();
				((Dialog) alertDialog).setCanceledOnTouchOutside(true);
				alertDialog.show();
			}
		}
		
//		if (extraInfoForQuery.size()==0) {
//			dialogfactory = new BaseDialogFactory(QueryListActivity.this, "", "", queryObj);
//			AlertDialog.Builder builder = dialogfactory.showResearchDialog();
//			builder.setPositiveButton("��ѯ", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//					HashMap<String, Object> returnValueMap = dialogfactory.BuildQueryValue();
//					businessObj.setListScrolltimes(1);
//					if (Global.getGlobalInstance().isSearchCondition()) {
//						returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
//						Global.getGlobalInstance().setSearchCondition(false);
//					}
//					rewriteSrarch(returnValueMap);
//
//				}
//			});
//			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//				}
//			});
//			AlertDialog alertDialog = builder.create();
//			((Dialog) alertDialog).setCanceledOnTouchOutside(true);
//			alertDialog.show();
//		}

		}
	}

	/**
	 * Description: �ṩһ����������������ѯ��������ʱ����д�˷���
	 * 
	 * @param returnValueMap
	 *            ��ѯ�б����ݵ����� void
	 * @author ����� Create at: 2012-12-6 ����03:53:55
	 */
	public void rewriteSrarch(HashMap<String, Object> returnValueMap) {
		setQueryCondition(returnValueMap);
		ArrayList<HashMap<String, Object>> mliList = businessObj.getDataList(returnValueMap);
		if (mliList == null) {
			dataList = new ArrayList<HashMap<String, Object>>();
		} else {
			dataList = mliList;
		}
		LoadList(bundle, dataList, style);
	}

	public void setQueryCondition(HashMap<String, Object> condition) {
		super.fliterHashMap = condition;
		super.i = 1;
		return;
	}
}
