package com.mapuni.android.infoQuery;

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
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: HBSTSYSXX.java
 * Description: 环评三同时
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午09:38:56
 */
public class HBSTSYSXX extends BaseClass implements Serializable, IDetailed,
		IQuery, IList {
	
	/**实体类的名字*/
	public static final String BusinessClassName = "HBSTSYSXX";
	/**获取该实体类列表样式的标题*/
	private static final String ListStyleName = "WRY_HBSTSYS";
	/**获取该实体类数据详情样式的标题*/
	private static final String DetailedStyleName = "WRY_HBSTSYS";
	/**获取该实体类查询样式的标题*/
	private static final String QueryStyleName = "HBSTSYS";
	/**该实体类所在表的主键*/
	private static final String primaryKey = "Guid";
	/** 获取该实体类底部菜单的标题 */
	private static final String BottomMenuName = "XQ";
	/**查询该实体类的相关信息所用的表名*/
	private static final String tableName = "V_WRY_HPSTSYS";
	/**该实体类在显示详情是所用的名字标题*/
	private String DetailedTitleText = "环评三同时验收信息详情";
	/**该实体类在显示列表的时候所用的名字标题*/
	private String ListTitleText = "环评三同时验收信息列表";
	/**该实体类在执行查询操作的时候所用的名字标题*/
	private String QueryTitleText = "环评三同时验收信息查询";
	/**初始化当前该实体类列表滚动的次数*/
	public int  ListScrollTimes=1;
	/**当前对象的id值*/
	private String CurrentID = "";
	/**该实体类的筛选条件集合*/
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;
	public HBSTSYSXX(){
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
	 * Create at: 2012-12-3 上午09:41:54
	 */
	public String getOrder() {
		 int count = Global.getGlobalInstance().getListNumber(); 
		int x=GetListScrolltimes()*count-count;
		int j = count;
//		String order="wjsj desc limit "+x+","+j;
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
//		return BaseClass.DBHelper.getList(tableName);
		String sql = "SELECT T_WRY_HPSTSYS.guid guid,wjbh,qymc,wjsj FROM T_WRY_HPSTSYS LEFT JOIN T_WRY_QYJBXX ON T_WRY_HPSTSYS.QYDM = T_WRY_QYJBXX.QYDM";
		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
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
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "HBSTSYSXX");
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
			ExceptionManager.WriteCaughtEXP(e, "HBSTSYSXX");
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
			ExceptionManager.WriteCaughtEXP(e, "HBSTSYSXX");
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
		String interestStr=Global.getGlobalInstance().getAdministrative();
		String[] interests=interestStr.split("、");
		StringBuffer interestStrB=new StringBuffer();
		for(String interest:interests){
			interestStrB.append(" or '"+interest+"' ");
		}
		AdapterFileName+=" where  xzqh='1'"+interestStrB.toString();
		spinnerdata=BaseClass.DBHelper.getList(AdapterFileName,querycondition);
		return spinnerdata;
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		ArrayList<HashMap<String, Object>> BottomMenuList = null;
		try {
			BottomMenuList = XmlHelper.getMenuFromXml(context, BottomMenuName,"item","bottommenu",getBottomMenuInputStream(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BottomMenuList;
	}
	
	@Override
	public String getBottomMenuName() {
		return BottomMenuName;
	}
}
