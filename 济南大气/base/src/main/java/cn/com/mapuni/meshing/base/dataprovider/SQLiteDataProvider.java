package cn.com.mapuni.meshing.base.dataprovider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;


/**
 * SQLite 数据库帮助类
 * 
 * @author xugf@mapuni.com
 * 
 */
public class SQLiteDataProvider {

	private static final String TAG = "SQLiteHelper";

	private static SqliteUtil sqliteUitl = SqliteUtil.getInstance(); // sqlite常用操作的工具类

	private static SQLiteDataProvider sqliteHelper = null;

	private SQLiteDataProvider() {
	}

	public synchronized static SQLiteDataProvider getInstance() {
		if (sqliteHelper == null) {
			sqliteHelper = new SQLiteDataProvider();
		}
		return sqliteHelper;
	}

	/**
	 * 根据表名获取列表数据
	 * 
	 * @param table
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table) {

		// ArrayList<HashMap<String, Object>> data = getList(table, conditions);
		String sql = "select * from " + table;
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql);

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据表名和需查询字段获取spinner数据
	 * 
	 * @param table
	 * @param column
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getList(String table,
			String column) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT " + column
				+ " FROM " + table);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中查询单列数据
	 * 
	 * @param colum
	 *            需要查询的列
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String colum,
			HashMap<String, Object> conditions, String table) {
		StringBuilder sql = new StringBuilder("select " + colum + " from "
				+ table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();
				sql.append("and " + condition.getKey() + " = '"
						+ condition.getValue() + "'");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 模糊查询方法
	 * 
	 * @param table
	 *            表名
	 * @param colum
	 *            需要查询的字段
	 * @param like
	 *            模糊查询条件
	 * */
	public ArrayList<HashMap<String, Object>> getObscureList(String table,
			String colum, String like) {
		StringBuilder sql = new StringBuilder("select " + colum + " from "
				+ table + " where " + colum + " like " + "'%" + like + "%'");
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中查询数据
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table,
			HashMap<String, Object> conditions) {

		StringBuilder sql = new StringBuilder("select * from " + table
				+ " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();
				sql.append("and (" + condition.getKey() + " = '"
						+ condition.getValue() + "')");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.i("==sql==", sql.toString());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中排序查询数据
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table,
			HashMap<String, Object> conditions, String order) {

		StringBuilder sql = new StringBuilder("select * from " + table
				+ " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中排序查询数据
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table,
			String column, HashMap<String, Object> conditions, String order) {

		StringBuilder sql = new StringBuilder("select " + column + " from "
				+ table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中排序查询数据
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件（条件之间用and连接，条件的操作是用 = 操作符匹配列名值）
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table,
			HashMap<String, Object> conditions, String order, String sqlstr) {

		StringBuilder sql = new StringBuilder(sqlstr);

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	public ArrayList<HashMap<String, Object>> getDataOrderList(String table,
			HashMap<String, Object> conditions, String column, String order) {

		StringBuilder sql = new StringBuilder("select * from " + table
				+ " where RWLX<>'003' and " + column + " in (");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("'" + condition.getValue() + "'" + ",");
				}

			}
			sql.delete(sql.length() - 1, sql.length());
			sql.append(") order by " + order);
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/** 一企一档历次检查数据查询 */
	public ArrayList<HashMap<String, Object>> getDataOrderListEnterprise(
			String table, HashMap<String, Object> conditions, String column,
			String order) {

		StringBuilder sql = new StringBuilder("select * from " + table
				+ " where " + column + " in (");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("'" + condition.getValue() + "'" + ",");
				}

			}
			sql.delete(sql.length() - 1, sql.length());
			sql.append(") order by " + order);
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据提供的条件，在表中查询数据
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            筛选条件 （条件之间用 and 连接）: whereName : 要搜索的列名 whereOperate : 要操作方式（>
	 *            = < like .....） whereValue : 条件值
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table,
			ArrayList<HashMap<String, Object>> conditions) {

		StringBuilder sql = new StringBuilder("select * from " + table
				+ " where 1=1 ");

		if (conditions != null) {
			for (HashMap<String, Object> mapConditions : conditions) {

				sql.append("and " + mapConditions.get("whereName") + " "
						+ mapConditions.get("whereOperate") + " '"
						+ mapConditions.get("whereValue") + "'");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * 根据表名获取详细信息
	 * 
	 * @param table
	 * @param primaryKey
	 *            放两个值：1，主键的名称：key 2，主键的值： keyValue
	 * @return
	 */
	public HashMap<String, Object> getDetailed(String table,
			HashMap<String, String> primaryKey) {
		try {
			HashMap<String, Object> data = sqliteUitl
					.queryTableDataByTableIdReturnHashMap(table, primaryKey);

			LogUtil.v(TAG, "getDetailed return data.size --> " + data.size());

			return data;
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return new HashMap<String, Object>();
	}

	/**
	 * 插入数据
	 * 
	 * @param values
	 * @param table
	 * @return
	 */
	public boolean insert(ArrayList<ContentValues> values, String table) {
		try {
			return sqliteUitl.insert(values, table) > 0;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return false;
	}

	/**
	 * 数据库是否存在该表
	 * 
	 * @param table
	 * @return
	 */
	public boolean checkTableExists(String table) {

		return sqliteUitl.checkTableExists(table);

	}

	/**
	 * 更新最新数据或更新全部数据
	 * 
	 * @param data
	 *            要更新的数据
	 * @param table
	 *            要更新的某一张表的表名
	 * @return
	 */
	public static boolean synchronizeOneTableUpdateOrFetchAll(
			// 将ArrayList封装的数据插入到表中
			ArrayList<HashMap<String, Object>> data, String table,
			boolean updateOrFetchAllData) {
		/* 转换一下数据格式 */
		ArrayList<ContentValues> insertData = SqliteUtil
				.convertArrayListHashMapToArrayListContentValues(data);

		/* 录入数据库中 */
		if (insertData != null && insertData.size() > 0) {
			try {
				if (updateOrFetchAllData) {
					//过滤要删除的数据
					ArrayList<HashMap<String, Object>> temp = filterData(data, table);
					if(temp == null || temp.size() == 0) {
						temp = data;
					}
					//删除过滤后的数据
					ArrayList<String> value = new ArrayList<String>();
					String primaryKey = sqliteUitl.getTablePrimaryKey(table);
					for (HashMap<String, Object> map : temp) {
						value.add((String) map.get(primaryKey));
					}
					if (value.size() > 0) {
						sqliteUitl.deleteNoTransaction(primaryKey, value, table);
					}
				}
				long count = sqliteUitl.insertNoTransaction(insertData, table);

				if (count > 0) {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " successful , effect " + count + " row .");
				} else {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " fail , effect 0 row .");
				}
			} catch (Exception e) {
				LogUtil.e(TAG, "//" + e.getMessage());
				return false;
			}
		}
		return true;
	}

	// lfwang 同步更新时 防止数据库里边产生两条记录
	private static ArrayList<HashMap<String, Object>> filterData(ArrayList<HashMap<String, Object>> data, String table) {
		ArrayList<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		// 减排监察记录表
		if (table.equals("T_ZFWS_JPJC")) {
			data = parseLowerList(data);
			for (int i = 0; i < data.size(); i++) {
				String sql = "select guid from T_ZFWS_JPJC where taskid = '" + data.get(i).get("taskid") + "' and entcode = '" + data.get(i).get("entcode") + "'";
				String guid = SqliteUtil.getInstance().getDepidByDepName(sql);
				if(guid != null && !guid.equals("")) {
					map = new HashMap<String, Object>();
					map.put("Guid", guid);
				} else {
					map = new HashMap<String, Object>();
					map.put("Guid", data.get(i).get("guid"));
				}
				temp.add(map);
			}
			// 现场检查笔录
		} else if (table.equals("T_ZFWS_KCBL")) {
			data = parseLowerList(data);
			for (int i = 0; i < data.size(); i++) {
				String sql = "select guid from T_ZFWS_KCBL where taskid = '" + data.get(i).get("taskid") + "' and entcode = '" + data.get(i).get("entcode") + "'";
				String guid = SqliteUtil.getInstance().getDepidByDepName(sql);
				if(guid != null && !guid.equals("")) {
					map = new HashMap<String, Object>();
					map.put("Guid", guid);
				} else {
					map = new HashMap<String, Object>();
					map.put("Guid", data.get(i).get("guid"));
				}
				temp.add(map);
			}
			// 询问笔录表
		} else if (table.equals("T_ZFWS_XWBL")) {
			data = parseLowerList(data);
			for (int i = 0; i < data.size(); i++) {
				String sql = "select guid from T_ZFWS_XWBL where taskid = '" + data.get(i).get("taskid") + "' and entcode = '" + data.get(i).get("entcode") + "'";
				String guid = SqliteUtil.getInstance().getDepidByDepName(sql);
				if(guid != null && !guid.equals("")) {
					map = new HashMap<String, Object>();
					map.put("Guid", guid);
				} else {
					map = new HashMap<String, Object>();
					map.put("Guid", data.get(i).get("guid"));
				}
				temp.add(map);
			}
			// 立案审批表
		} else if (table.equals("T_ZFWS_LASP")) {
			data = parseLowerList(data);
			for (int i = 0; i < data.size(); i++) {
				String sql = "select guid from T_ZFWS_LASP where taskid = '" + data.get(i).get("taskid") + "' and entid = '" + data.get(i).get("entid") + "'";
				String guid = SqliteUtil.getInstance().getDepidByDepName(sql);
				if(guid != null && !guid.equals("")) {
					map = new HashMap<String, Object>();
					map.put("Guid", guid);
				} else {
					map = new HashMap<String, Object>();
					map.put("Guid", data.get(i).get("guid"));
				}
				temp.add(map);
			}
		}
		return temp;
	}
	
	/**
	 * 转成小写的key
	 * 
	 * @param data
	 * @return
	 */
	private static ArrayList<HashMap<String, Object>> parseLowerList(
			ArrayList<HashMap<String, Object>> data) {
		ArrayList<HashMap<String, Object>> parseData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = data.get(i).entrySet().iterator();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				map.put(entry.getKey().toString().toLowerCase(),
						entry.getValue());
			}
			parseData.add(map);

		}
		return parseData;
	}

	public static boolean synchronizeOneTable(// 将ArrayList封装的数据插入到表中
			ArrayList<HashMap<String, Object>> data, String table) {
		/* 转换一下数据格式 */
		ArrayList<ContentValues> insertData = sqliteUitl
				.convertArrayListHashMapToArrayListContentValues(data);

		/* 录入数据库中 */
		if (insertData != null && insertData.size() > 0) {
			try {

				long count = sqliteUitl.insert(insertData, table);

				if (count > 0) {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " successful , effect " + count + " row .");
				} else {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " fail , effect 0 row .");
				}
			} catch (SQLiteException e) {
				LogUtil.e(TAG, "//" + e.getMessage());
				return false;
			} catch (Exception e) {
				LogUtil.e(TAG, "//" + e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取下载文件的URL
	 * 
	 * @param table
	 *            表名
	 * @param column_fileContent
	 *            文件内容列名
	 * @param column_fileName
	 *            文件名称列名
	 * @param column_fileId
	 *            文件唯一标识列名
	 * @param textId
	 *            文件唯一标识列值
	 * @param namespace
	 *            webserice命名空间
	 * @param url
	 *            webserice的url
	 * @param methodName
	 *            webserice的方法名
	 * 
	 * @return
	 */
	public String getFileURL(String table, String column_fileContent,
			String column_fileName, String column_fileId, String textId,
			String namespace, String url, String methodName) {

		/* 调用webserice的参数设置 */
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", table);
		params.add(param);

		String columns = "content:" + column_fileContent + ";filename:"
				+ column_fileName + ";id:" + column_fileId;

		param = new HashMap<String, Object>();
		param.put("columns", columns);
		params.add(param);

		param = new HashMap<String, Object>();
		param.put("idvalue", textId);
		params.add(param);

		/* 从后台取得某一张表的xml字符串数据 */
		String resultResponse = "";
		try {
			resultResponse = String.valueOf(WebServiceProvider.callWebService(
					namespace, methodName, params, url,
					WebServiceProvider.RETURN_BOOLEAN, true));
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
			return "";
		}
		return resultResponse == null || "".equals(resultResponse)
				|| "null".equals(resultResponse) ? "" : resultResponse;
	}

	/**
	 * 查询指定的sql语句，返回ArrayList<HashMap<String, Object>>
	 * 
	 * @param sql
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> queryBySqlReturnArrayListHashMap(
			String sql) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		try {
			data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return data;
	}

	/**
	 * 更新数据库表操作
	 * 
	 * @param table
	 *            表名
	 * @param updateValues
	 *            要更新的数据列名和对应的值
	 * @param whereClause
	 *            条件，如 id = ?
	 * @param whereArgs
	 *            条件参数，如 new String[]{ id }
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean updateTable(String table, ContentValues updateValues,
			String whereClause, String[] whereArgs) {
		try {
			sqliteUitl.update(table, updateValues, whereClause, whereArgs);
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		}
		return false;
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	public boolean ExecSQL(String sql) {
		try {
			sqliteUitl.execute(sql);
			return true;
		} catch (Exception e) {
			LogUtil.i(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * 根据用户输入的值生成类似于 and (name= '1' or name like '%李%') 的语句，sql语句优化query list 使用
	 * 
	 * @param filterMap
	 *            用户输入的条件
	 * @return 类似于 and (name= '1' or name like '%李%') 的语句
	 */
	public String getFilterForSql(HashMap<String, Object> filterMap) {
		StringBuffer sql = new StringBuffer();
		if (filterMap != null && filterMap.size() > 0) {
			Iterator<Entry<String, Object>> iterator = filterMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		return sql.toString();
	}

	/**
	 * 根据用户输入的值生成类似于 xxID = 'xxx' 的语句，sql语句优化detail 使用
	 * 
	 * @param filterMap
	 *            点击listView某一项时产生的条件
	 * @return 类似于 xxID = 'xxx' 的语句
	 */
	public String getFilterForSqlDetail(HashMap<String, String> filterMap) {
		String key = filterMap.get("key");
		String keyValue = filterMap.get("keyValue");
		String sql = " [" + key + "] = '" + keyValue + "'";
		return sql;
	}

	/**
	 * Description: 递归查询
	 * 
	 * @param tableName
	 *            表名
	 * @param data
	 *            存储数据
	 * @param conditions
	 *            查询条件 condition= {DepParentID=depta1} 顶层节点查询出数据后重新生成条件递归继续查询
	 * @param coulm
	 * @param itself
	 *            是否查询最顶级节点数据
	 * @author wanglg
	 * @Create at: 2013-6-4 上午8:39:47
	 */
	public void recursiveQuery(String tableName,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> conditions, String coulm, Boolean itself) {
		Iterator iter = conditions.entrySet().iterator();
		Map.Entry entry = (Map.Entry) iter.next();
		String key = entry.getKey().toString().toLowerCase();
		String value = entry.getValue().toString();
		if (itself) {
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put(coulm, value);
			data.addAll(getList(tableName, condition));// 查询出最上级 的节点数据
		}
		recursiveQueryChild(tableName, data, conditions, coulm, key);
	}

	/**
	 * Description: 递归查询子元素
	 * 
	 * @param tableName
	 * @param data
	 * @param conditions
	 *            递归条件 例如 DepParentID=depta1
	 * @param coulm
	 * @param key
	 * @author wanglg
	 * @Create at: 2013-6-4 上午11:37:21
	 */
	public void recursiveQueryChild(String tableName,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> conditions, String coulm, String key) {
		ArrayList<HashMap<String, Object>> mdata = getList(tableName,
				conditions);

		for (HashMap<String, Object> map : mdata) {
			data.add(map);
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put(key, map.get(coulm));
			recursiveQueryChild(tableName, data, condition, coulm, key);
		}
	}

	/**
	 * 分页获取数据库数据
	 * 
	 * @param tableName
	 *            表名
	 * @param coulm
	 *            字段名 用逗号分隔
	 * @param conditions
	 *            查询条件
	 * @param limit
	 *            分页 列如 0,30
	 * @param order
	 *            排序规则 author wanglg
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getLimitDataList(
			String tableName, String coulm, HashMap<String, Object> conditions,
			String limit, String order) {

		StringBuilder sql = new StringBuilder("select " + coulm + " from "
				+ tableName + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		sql.append(" limit " + limit);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl
					.queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, "//" + e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();

	}
}
