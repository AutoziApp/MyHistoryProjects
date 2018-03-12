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
 * FileName: GLJBQK.java
 * Description:锅炉基本情况业务类
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午09:04:53
 */
public class GLJBQK extends BaseClass implements Serializable, IDetailed,
		 IList {
	
	/**业务类的名字*/
	public  final static String BusinessClassName = "GLJBQK";
	/**获取该业务类列表样式的标题*/
	private  final String ListStyleName = "ZDWRY_GLJBQK";
	/**获取该业务类数据详情样式的标题*/
	private  final String DetailedStyleName = "ZDWRY_GLJBQK";
	/**该业务类所在表的主键*/
	private  final String primaryKey = "Guid";
	/**用于查询的排序字段，用来拼接sql语句*/
	private  final String order="qymc asc";										     //排序的字段
	/**查询该业务类的相关信息所用的表名*/
	private  final String tableName = "V_ZDWRY_GLJBQK";//表名
	/**该业务类在显示详情是所用的名字标题*/
	private String DetailedTitleText = "锅炉基本情况详情";
	/**该业务类在显示列表的时候所用的名字标题*/
	private String GridTitleText = "锅炉基本情况列表";
	/**初始化当前该业务类列表滚动的次数，目前还没用到*/
	public int  ListScrollTimes;
	/**当前对象的id值*/
	private String CurrentID = "";
	/**该业务类的筛选条件集合*/
	private HashMap<String, Object> Filter;
	
	
	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}
	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}
	@Override
	public String GetKeyField() {
		return primaryKey;
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
			//获取需要显示的信息
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GLJBQK");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}


	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

    /**
     *  Description:通过条件获取锅炉信息列表
     * @param fliterHashMap 获取列表信息的限制条件
     * @author 王红娟
     * @return ArrayList<HashMap<String, Object>> 返回列表信息
     * Create at: 2012-12-3 上午09:12:55
     */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap,order);
	}
	
	/**
	 * Description: 给业务类的查询限制条件赋值
	 * @param filter 要获取数据的条件
	 * void 
	 * @author 王红娟
	 * Create at: 2012-12-3 上午09:12:55
	 */
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
		}


	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			//获取样式
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GLJBQK");
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
