package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: TZGGXX.java
 * Description: 通知公示信息
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 下午02:15:17
 */
public class TZGGXX extends BaseClass implements Serializable, IList,
		IDetailed, IQuery {
	
	/** 实体类的名字 */
	public static final String BusinessClassName = "TZGGXX";
	/** 获取该实体类列表样式的标题 */
	private  final String ListStyleName = "TZGGLB";
	/** 获取该实体类数据详情样式的标题 */
	private  final String DetailedStyleName = "TZGGXX";
	/** 获取该实体类查询样式的标题 */
	private  final String QueryStyleName = "TZGGCX";
	/** 该实体类所在表的主键 */
	private  final String primaryKey = "id";
	/** 查询该实体类的相关信息所用的表名 */
	private  final String TableName = "T_YDZF_TZGG";
	/** 该实体类在显示详情是所用的名字标题 */
	private String DetailedTitleText = "通知公示详情";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private  final String TitleName = "通知公示列表";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private  String QueryTitleText = "通知公示查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";

	HashMap<String, Object> styleList = null;
	public TZGGXX(){
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(Global.getGlobalInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-4 下午02:19:42
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
//		String order = "faburiqi desc limit " + x + "," + j;
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
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
		return spinnerdata;
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
		ArrayList<HashMap<String, Object>> styleDetailed = getStyleDetailed(Global
				.getGlobalInstance());
		Object mySql = styleDetailed.get(styleDetailed.size() - 1).get("queryHint")
				+ SQLiteDataProvider.getInstance().getFilterForSqlDetail(
						primaryKeyMap);
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String getListTitleText() {
		return TitleName;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		Object object = styleList.get("mysql");
		String sql = object+getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		String newSql = sql.replace(",,", "");
		LogUtil.d("newSql sql is ===>", newSql);
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		Object object = styleList.get("mysql");
		String sql = object+getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		String newSql = null;
		if("".equals(filterSql)){
			newSql = sql.replace(",,", "");
		}else{
			newSql = sql.replace(",,", filterSql);
		}
		LogUtil.d("newSql sql is ===>", newSql);
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "TZGGXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return null;
	}

	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetTableName() {

		return TableName;
	}

	/* 石家庄"移动办公"需求变更 */
	/**
	 * Description: 获取三天之内的通知公告
	 * @return 通知公告信息
	 * ArrayList<HashMap<String,Object>>
	 * @author 王红娟
	 * Create at: 2012-12-4 下午02:20:01
	 */
	public ArrayList<HashMap<String, Object>> getLastThreeDaysTZGGXX() {
		
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -3);
			String oldDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar
					.getTime());
			String sql = "SELECT * FROM T_YDZF_TZGG WHERE FABURIQI > '"
					+ oldDate + "'";
			return SQLiteDataProvider.getInstance()
					.queryBySqlReturnArrayListHashMap(sql);
		
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
