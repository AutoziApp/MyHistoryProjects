package com.mapuni.android.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: HBFLXX.java
 * Description: 环保法律
 * @author 包坤
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午09:32:15
 */
public class HBFLXX extends BaseClass implements IDetailed, IList, IQuery,
		Serializable {
	
	/**实体类的名字*/
	public static final String BusinessClassName = "HBFLXX";
	/**获取该实体类列表样式的标题*/
	private  final String ListStyleName = "HJYJ_FLFGBZ";
	/**获取该实体类数据详情样式的标题*/
	private  final String DetailedStyleName = "HJYJ_FLFGBZ";
	/**获取该实体类查询样式的标题*/
	private  final String QueryStyleName = "FLFGBZ";
	/**该实体类所在表的主键*/
	private  final String primaryKey = "Guid";
	/**查询该实体类的相关信息所用的表名*/
	private  final String tableName = "T_ZSK_FLFGBZ";
	/** 该实体类的数据类别 */
	private String FileType = "";
	/**该实体类在显示详情是所用的名字标题*/
	private String DetailedTitleText = "信息详情";
	/**该实体类在显示列表的时候所用的名字标题*/
	private String ListTitleText = "信息列表";
	/**该实体类在执行查询操作的时候所用的名字标题*/
	private String QueryTitleText = "信息查询";
	/**初始化当前该实体类列表滚动的次数*/
	public int  ListScrollTimes=1;
	/**当前对象的id值*/
	private String CurrentID = "";
	/**该实体类的筛选条件集合*/
	private HashMap<String, Object> Filter;

	HashMap<String, Object> styleList = null;
	public HBFLXX(){
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
	 * @author 包坤
	 * Create at: 2012-12-3 上午09:34:34
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber(); 
		int x = GetListScrolltimes() * count  - count;
		int j = count;
//		String order = "ymbt asc limit " + x + "," + j;
		String order = x + "," + j;
		return order;
	}
	
	public String getFileType() {
		return FileType;
	}
	public void setFileType(String fileType) {
		FileType = fileType;
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
		return getFileType()+ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		String column = "Guid,YMBT,SJLB,SCSJ";
		return BaseClass.DBHelper.getList(tableName,column);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		if(String.valueOf(DisplayUitl.getSettingValue(Global.getGlobalInstance(), "menustyle", 0)).equals("1")){
			if(fliterHashMap==null){
				fliterHashMap = new HashMap<String, Object>();
			}
			//fliterHashMap.put("sjlb", Global.getGlobalInstance().getType_DB().get("sjlb"));
		}
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
		Log.d("newSql sql is ===>", newSql);
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
		return getFileType()+QueryTitleText;
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
		return getFileType()+DetailedTitleText;
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
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName, String querycondition) {
		List<HashMap<String, Object>> spinnerdata = new ArrayList<HashMap<String, Object>>();
		return spinnerdata=BaseClass.DBHelper.getList(AdapterFileName,querycondition);
	
		
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
