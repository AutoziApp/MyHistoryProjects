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
 * FileName: JCJLXX.java Description: 检查记录的业务类
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午10:49:35
 */
public class JCJLXX extends BaseClass implements Serializable, IDetailed, IList {

	/** 业务类的名字 */
	public static final String BusinessClassName = "JCJLXX";
	/** 获取该业务类列表样式的标题 */
	private  final String ListStyleName = "JCJL_RWXX";
	/** 获取该业务类数据详情样式的标题 */
	private  final String DetailedStyleName = "RWXX";
	/** 该业务类所在表的主键 */
	private  final String primaryKey = "Id";
	/** 用于查询的排序字段，用来拼接sql语句 */
	private  final String order = "FBSJ asc";
	/** 查询该业务类的相关信息所用的表名 */
	private  final String tableName = "T_YDZF_RWXX";
	/** 该业务类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "检查记录信息详情";
	/** 该业务类在显示列表的时候所用的名字标题 */
	private final String ListTitleText = "检查记录信息列表";
	/** 初始化当前该业务类列表滚动的次数，目前还没用到 */
	public int ListScrollTimes;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该业务类的筛选条件集合 */
	private HashMap<String, Object> Filter;
	/** 该业务类查询出的列表集合 */
	private ArrayList<HashMap<String, Object>> datalist;

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}

	public void setDatalist(ArrayList<HashMap<String, Object>> datalist) {
		this.datalist = datalist;
	}

	@Override
	public String getListTitleText() {

		return ListTitleText;
	}

	@Override
	public String getCurrentID() {

		return CurrentID;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		this.CurrentID = currentIDValue;

	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {

		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap, order);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			/**
			 * 通过解析style_list.xml来设置格式 用styleList集合来存储解析的数据
			 */
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "JCJLXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {

		return Filter;
	}

	/**
	 * Description: 为该对象设置查询的条件
	 * 
	 * @param filter
	 *            查询的条件集合 void
	 * @author 王红娟 Create at: 2012-12-3 上午10:54:21
	 */
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public String getDetailedTitleText() {

		return DetailedTitleText;
	}

	/**
	 * 通过用BaseClass.DBHelper.getDetailed方法获取表的详细信息
	 */
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
	}

	/**
	 * 通过XmlHelper.getStyleByName()方法来得到获取的样式信息 返回给一个styleDetailList集合
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return null;
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
