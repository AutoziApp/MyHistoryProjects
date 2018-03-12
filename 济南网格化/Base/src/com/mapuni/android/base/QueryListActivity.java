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
 * FileName: QueryListActivity.java Description: 具有查询功能的列表
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-6 下午03:43:31
 */
public class QueryListActivity extends ListActivity {
	/** 变量 */
	private IQuery queryObj;
	/** 存放查询样式 */
	@SuppressWarnings("unused")
	private List<HashMap<String, Object>> extraInfoForQuery;
	/** 查询工具类 */
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
	 * 重写查找方法
	 */
	@Override
	public void queryDate() {
		showedittask = true;
		QueryListActivity.this.showResearchDialog();
	}

	/**
	 * Description: 显示查询的dialog
	 * 
	 * void
	 * 
	 * @author 王红娟 Create at: 2012-12-6 下午03:46:39
	 */
	protected void showResearchDialog() {

		if (showedittask) {
			/** 初始化数据bundle，将bundle装入Intent传入跳转页面 */
			Bundle bundle = new Bundle();
			bundle.putBoolean("IsShowTitle", true);
			bundle.putString("TitleText", "任务编辑");
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
			builder.setPositiveButton("查询",
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
			builder.setNegativeButton("取消",
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
	 * Description: 提供一个方法，当基本查询不能满足时，重写此方法
	 * 
	 * @param returnValueMap
	 *            查询列表数据的条件 void
	 * @author 王红娟 Create at: 2012-12-6 下午03:53:55
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
