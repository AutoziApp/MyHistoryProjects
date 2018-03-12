package com.mapuni.android.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
		super.setQueryButtonVisiable(false);
		super.setSearchButtonVisiable(true);
		queryImg.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				QueryListActivity.this.showResearchDialog();
			}
		});
	}

	/**
	 * ��д���ҷ���
	 */
	@Override
	public void queryDate() {
		showedittask = true;
		QueryListActivity.this.showResearchDialog();
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
			_Intent.setClassName("com.mapuni.android.MobileEnforcement",
					"com.mapuni.android.taskmanager.TaskEditActivity");
			_Intent.putExtras(bundle);
			startActivity(_Intent);
		} else {
			queryObj = (IQuery) businessObj;
			extraInfoForQuery = queryObj.getStyleQuery(QueryListActivity.this);
			dialogfactory = new BaseDialogFactory(QueryListActivity.this, "",
					"", queryObj);
			AlertDialog.Builder builder = dialogfactory.showResearchDialog();
			builder.setPositiveButton("��ѯ",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							HashMap<String, Object> returnValueMap = dialogfactory
									.BuildQueryValue();
							businessObj.setListScrolltimes(1);
							if(Global.getGlobalInstance().isSearchCondition()){
								returnValueMap.put("syncdataregioncode", Global.getGlobalInstance().getAreaCode());
								Global.getGlobalInstance().setSearchCondition(false);
							}
							rewriteSrarch(returnValueMap);
							
							
						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog alertDialog = builder.create();
			((Dialog) alertDialog).setCanceledOnTouchOutside(true);
			alertDialog.show();
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
		ArrayList<HashMap<String, Object>> mliList = businessObj
				.getDataList(returnValueMap);
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
