package com.mapuni.android.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.ListActivity;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enforcement.EnforcementActivity;

/**
 * FileName: XCZFXX.java Description: 现场执法信息
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 下午02:48:06
 */
public class XCZFXX extends RWXX {

	/** 实体类的名字 */
	public static final String BusinessClassName = "XCZFXX";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private  final String TitleName = "现场执法";
	/** 查询该实体类的相关信息所用的表名 */
	private  final String TableName = "V_YDZF_RWXX";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;
	/** 查询的基本信息 */
	private  final String QueryXCZFStyleName = "JBXX";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private  String QueryTitleText = "企业查询";

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	/**
	 * 返回现场执法任务列表
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		try {
			String useName = Global.getGlobalInstance().getUserid();
			String sql = "select distinct * from " + TableName
					+ " where rwlx='003' and FBR='" + useName
					+ "' order by FBSJ DESC ";
			return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					sql);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 通过条件筛选返回现场执法任务列表
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {

		StringBuilder sql = new StringBuilder("select * from " + TableName
				+ " where RWLX='003' and FBR='"
				+ Global.getGlobalInstance().getUserid() + "'");

		if (fliterHashMap != null && fliterHashMap.size() > 0) {
			Iterator<Entry<String, Object>> iterator = fliterHashMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
			sql.append(" order by " + " FBSJ desc");
		}
		try {
			ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(sql.toString());

			return data;
		} catch (SQLiteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<HashMap<String, Object>>();

	}

	/**
	 * 返回列表要显示的标题
	 */
	@Override
	public String getListTitleText() {
		return TitleName;
	}

	/** 返回标题的查询内容 */
	public String getStyleQuery() {
		return QueryXCZFStyleName;
	}

	/**
	 * 返回查询的限制条件
	 */
	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {
		Intent xczfintent = intent;
		Bundle bundle = new Bundle();
		XCZFXX rwlb;

		try {
			rwlb = (XCZFXX) BaseObjectFactory
					.createObject(XCZFXX.BusinessClassName);
			bundle.putSerializable("BusinessObj", rwlb);
			bundle.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
			bundle.putBoolean("IsShowTitle", true);
			bundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
			bundle.putBoolean("IsShowQUERYBtn", true);
			xczfintent = new Intent(context, EnforcementActivity.class);
			//xczfintent.putExtras(bundle);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return xczfintent;

	}

	/**
	 * 重写查询样式
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryXCZFStyleName,
					getStyleQueryInputStream(context));
			System.out.println("styleDetailList11+>>>>" + styleDetailList);
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "QYJBXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

}
