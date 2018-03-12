package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: YLJBQK.java
 * Description: 窑炉基本情况
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 下午04:15:16
 */
public class YLJBQK extends BaseClass implements Serializable, IDetailed, IList {
	
	/** 实体类的名字 */
	public static final String BusinessClassName = "YLJBQK";
	/** 获取该实体类列表样式的标题 */
	private  final String ListStyleName = "ZDWRY_YLJBQK";
	/** 获取该实体类数据详情样式的标题 */
	private  final String DetailedStyleName = "ZDWRY_YLJBQK";
	/** 该实体类所在表的主键 */
	private  final String primaryKey = "Guid";
	/** 用于对该实体类进行排序查询，此处用于sql语句拼接进行排序查询 */
	private  final String order = "qymc desc"; // 排序的字段
	/** 查询该实体类的相关信息所用的表名 */
	private  final String tableName = "V_ZDWRY_YLJBQK";
	/** 该实体类在显示详情是所用的名字标题 */
	private String DetailedTitleText = "窑炉基本情况";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private String GridTitleText = "窑炉基本情况列表";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;

	@Override
	public String GetKeyField() {
		return primaryKey;
	}

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}

	@Override
	public String getCurrentID() {
		return CurrentID;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public String getDetailedTitleText() {
		return DetailedTitleText;
	}

	@Override
	public String getListTitleText() {
		return GridTitleText;
	}

	@Override
	public String GetTableName() {
		return tableName;
	}

	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "YLJBQK");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap, order);
	}

	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "YLJBQK");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		return null;
	}
	
	@Override
	public String getBottomMenuName() {
		return null;
	}
}
