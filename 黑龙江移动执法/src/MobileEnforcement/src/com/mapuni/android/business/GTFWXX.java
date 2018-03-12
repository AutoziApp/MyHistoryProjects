package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: GTFWXX.java Description: 固体废物业务类
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-3 上午09:14:24
 */
public class GTFWXX extends BaseClass implements Serializable, IDetailed, IList {

	/** 业务类的名字 */
	public static final String BusinessClassName = "GTFWXX";
	/** 获取该业务类列表样式的标题 */
	private  final String ListStyleName = "ZDWRY_GTFW";
	/** 获取该业务类数据详情样式的标题 */
	private  final String DetailedStyleName = "ZDWRY_GTFW";
	/** 该业务类所在表的主键 */
	private  final String primaryKey = "Id";
	/** 用于查询的排序字段，用来拼接sql语句 */
	private  final String order = "wxfwmc asc"; // 排序的字段
	/** 查询该业务类的相关信息所用的表名 */
	private  final String tableName = "V_ZDWRY_GTFW";
	/** 该业务类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "固体废物信息详情";
	/** 该业务类在显示列表的时候所用的名字标题 */
	private final String GridTitleText = "固体废物信息列表";
	/** 初始化当前该业务类列表滚动的次数，目前还没用到 */
	public int ListScrollTimes;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该业务类的筛选条件集合 */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;

	public GTFWXX() {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(Global.getGlobalInstance()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
	 * @author 王红娟 Create at: 2012-12-4 上午10:12:05
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		// String order = "wjsj desc,QYXM desc limit " + x + "," + j;
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
		// HashMap<String, String> primaryKeyMap = new HashMap<String,
		// String>();
		// primaryKeyMap.put("key", primaryKey);
		// primaryKeyMap.put("keyValue", id);
		// ArrayList<HashMap<String, Object>> styleDetailed =
		// getStyleDetailed(Global
		// .getGlobalInstance());
		// Object mySql = styleDetailed.get(styleDetailed.size() - 1).get(
		// "queryHint")
		// + SQLiteDataProvider.getInstance().getFilterForSqlDetail(
		// primaryKeyMap);
		Object mySql = "SELECT a.* , b.* FROM T_WRY_GTFW as a LEFT JOIN "
				+ "(select qymc,qydm from T_WRY_QYJBXX where qydm = (select wrybh from T_WRY_GTFW where id = "
				+ id
				+ " )) as b "
				+ "ON a . WRYBH = b . QYDM where b.qydm =(select wrybh from T_WRY_GTFW where id = "
				+ id + " ) and a.id = " + id;
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(
				String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GTFWXX");
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
	 * Description:通过条件获取固体废物信息列表
	 * 
	 * @param fliterHashMap
	 *            获取列表信息的限制条件
	 * @author 王红娟
	 * @return ArrayList<HashMap<String, Object>> 返回列表信息 Create at:
	 *         2012-12-3上午09:12:55
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		// Object object = styleList.get("mysql");
		// String sql = (String) object;
		// SqliteUtil su = SqliteUtil.getInstance();
		//
		// String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		// String newSql = null;
		// if ("".equals(filterSql)) {
		// newSql = sql.replace(",,", "");
		// } else {
		// newSql = sql.replace(",,", filterSql);
		// }
		// Log.d("newSql sql is ===>", newSql);
		String wrybh = fliterHashMap.get("qydm").toString();
		String newSql = "SELECT a.* , b.* FROM T_WRY_GTFW as a LEFT JOIN "
				+ "(select qymc,qydm from T_WRY_QYJBXX where qydm ='" + wrybh
				+ "') as b " + "ON a . WRYBH = b . QYDM where b.qydm ='"
				+ wrybh + "' limit ";

		newSql = newSql + getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	/**
	 * Description: 给业务类的查询限制条件赋值
	 * 
	 * @param filter
	 *            要获取数据的条件 void
	 * @author 王红娟 Create at: 2012-12-3 上午09:17:57
	 */
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
			ExceptionManager.WriteCaughtEXP(e, "GTFWXX");
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
