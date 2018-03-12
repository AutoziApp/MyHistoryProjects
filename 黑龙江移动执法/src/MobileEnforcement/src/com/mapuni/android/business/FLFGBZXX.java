package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: FLFGBZXX.java
 * Description: 法律法规业务类
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-11-30 下午05:15:18
 */
public class FLFGBZXX extends BaseClass implements IDetailed, IList, IQuery,
		Serializable {
	
	/**业务类的名字*/
	public static final String BusinessClassName = "FLFGBZXX";
	/**获取该业务类列表样式的标题*/
	private  final String ListStyleName = "HJYJ_FLFGBZ";
	/**获取该业务类数据详情样式的标题 */
	private  final String DetailedStyleName = "HJYJ_FLFGBZ";
	/**获取该业务类查询样式的标题*/
	private  final String QueryStyleName = "FLFGBZ";
	/**该业务类所在表的主键*/
	private  final String primaryKey = "Guid";
	/**查询该业务类的相关信息所用的表名*/
	private  final String tableName = "T_ZSK_FLFGBZ";
	/**该业务类在显示详情是所用的名字标题*/
	private String DetailedTitleText = "法律法规标准信息详情";
	/**该业务类在显示列表的时候所用的名字标题*/
	private String ListTitleText = "法律法规标准信息列表";
	/**该业务类在执行查询操作的时候所用的名字标题*/
	private String QueryTitleText = "法律法规标准信息查询";
	/**初始化当前该业务类列表滚动的次数*/
	public int  ListScrollTimes=1;
	/**当前对象的id值*/
	private String CurrentID = "";
	/** 该业务类的筛选条件集合*/
	private HashMap<String, Object> Filter;

	/**
	 * Description:  用来实现查询列表的时候进行分页显示
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String
	 * String
	 * @author 包坤
	 * Create at: 2012-11-30 下午05:18:56
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count  - count; 
		int j = count;
		String order = "ymbt asc limit " + x + "," + j;
		return order;
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
	public String getListTitleText() {
		return ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap,getOrder());
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return styleList;
	}


	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		return null;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName,
					getStyleQueryInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "FLFGBZXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
		}

	@Override
	public String getDetailedTitleText() {
		return DetailedTitleText;
	}

	@Override
	public String getCurrentID() {
		return CurrentID;
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
			ExceptionManager.WriteCaughtEXP(e, "FLFGBZXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String GetKeyField() {
		return primaryKey;
	}

	@Override
	public String GetTableName() {
		return tableName;
	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata=null;
		spinnerdata=BaseClass.DBHelper.getList(AdapterFileName,querycondition);
		return spinnerdata;
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
