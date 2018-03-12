package com.mapuni.android.helper;

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
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: ZJKXX.java Description: 专家库信息
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 下午04:54:02
 */
public class ZJKXX extends BaseClass implements IDetailed, IList, IQuery, Serializable {

	/** 实体类的名字 */
	public static final String BusinessClassName = "ZJKXX";
	/** 获取该实体类列表样式的标题 */
	private final String ListStyleName = "HJYJ_ZJK";
	/** 获取该实体类数据详情样式的标题 */
	private final String DetailedStyleName = "HJYJ_ZJK";
	/** 获取该实体类查询样式的标题 */
	private final String QueryStyleName = "ZJK";
	/** 该实体类所在表的主键 */
	private final String primaryKey = "ExpID";
	/** 查询该实体类的相关信息所用的表名 */
	private final String tableName = "PC_Exp";
	/** 该实体类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "专家库信息详情";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private final String ListTitleText = "专家库信息列表";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private final String QueryTitleText = "专家库信息查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;

	HashMap<String, Object> styleList = null;

	public ZJKXX() {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(Global.getGlobalInstance()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
	 * @author 王红娟 Create at: 2012-12-4 下午04:56:48
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		String order = x + "," + j;
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
		String coulm = "ExpID,Exp_Name,Exp_Sex,Exp_Date,Exp_Political,Exp_Native,Exp_Place,Exp_Photo,Exp_WorkUnit,Exp_Job,Exp_HighEdt,Exp_Major,Exp_Tel,Exp_ExpertType,UpdateTime";
		String limit = getOrder();
		return DBHelper.getLimitDataList(tableName, coulm, null, limit, null);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		String coulm = "ExpID,Exp_Name,Exp_Sex,Exp_Date,Exp_Political,Exp_Native,Exp_Place,Exp_Photo,Exp_WorkUnit,Exp_Job,Exp_HighEdt,Exp_Major,Exp_Tel,Exp_ExpertType,UpdateTime";
		String limit = getOrder();
		return DBHelper.getLimitDataList(tableName, coulm, fliterHashMap, limit, null);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content) throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName, getStyleQueryInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
		return SqliteUtil.getInstance().getDetailed(tableName, primaryKeyMap);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName, getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName, String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName, querycondition);
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
