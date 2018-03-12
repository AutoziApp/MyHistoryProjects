package com.mapuni.android.dataprovider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * SQLite ���ݿ������
 * 
 * @author xugf@mapuni.com
 * 
 */
public class SQLiteDataProvider {

	private static final String TAG = "SQLiteHelper";

	private static SqliteUtil sqliteUitl = SqliteUtil.getInstance(); // sqlite���ò����Ĺ�����

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
	 * ���ݱ�����ȡ�б�����
	 * 
	 * @param table
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table) {

		// ArrayList<HashMap<String, Object>> data = getList(table, conditions);
		String sql = "select * from " + table;
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql);

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * ���ݱ��������ѯ�ֶλ�ȡspinner����
	 * 
	 * @param table
	 * @param column
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getList(String table, String column) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT " + column + " FROM " + table);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��в�ѯ��������
	 * 
	 * @param colum
	 *            ��Ҫ��ѯ����
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String colum, HashMap<String, Object> conditions, String table) {
		StringBuilder sql = new StringBuilder("select " + colum + " from " + table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();
				sql.append("and " + condition.getKey() + " = '" + condition.getValue() + "'");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * ģ����ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param colum
	 *            ��Ҫ��ѯ���ֶ�
	 * @param like
	 *            ģ����ѯ����
	 * */
	public ArrayList<HashMap<String, Object>> getObscureList(String table, String colum, String like) {
		StringBuilder sql = new StringBuilder("select " + colum + " from " + table + " where " + colum + " like " + "'%" + like + "%'");
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��в�ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table, HashMap<String, Object> conditions) {

		StringBuilder sql = new StringBuilder("select * from " + table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();
				sql.append("and (" + condition.getKey() + " = '" + condition.getValue() + "')");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.i("==sql==", sql.toString());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table, HashMap<String, Object> conditions, String order) {

		StringBuilder sql = new StringBuilder("select * from " + table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table, String column, HashMap<String, Object> conditions, String order) {

		StringBuilder sql = new StringBuilder("select " + column + " from " + table + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getOrderList(String table, HashMap<String, Object> conditions, String order, String sqlstr) {

		StringBuilder sql = new StringBuilder(sqlstr);

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	public ArrayList<HashMap<String, Object>> getDataOrderList(String table, HashMap<String, Object> conditions, String column, String order) {

		StringBuilder sql = new StringBuilder("select * from " + table + " where RWLX<>'003' and " + column + " in (");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
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
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/** һ��һ�����μ�����ݲ�ѯ */
	public ArrayList<HashMap<String, Object>> getDataOrderListEnterprise(String table, HashMap<String, Object> conditions, String column, String order) {

		StringBuilder sql = new StringBuilder("select * from " + table + " where " + column + " in (");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
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
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * �����ṩ���������ڱ��в�ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ���� ������֮���� and ���ӣ�: whereName : Ҫ���������� whereOperate : Ҫ������ʽ��>
	 *            = < like .....�� whereValue : ����ֵ
	 * @return default : new ArrayList<HashMap<String, Object>>()
	 */
	public ArrayList<HashMap<String, Object>> getList(String table, ArrayList<HashMap<String, Object>> conditions) {

		StringBuilder sql = new StringBuilder("select * from " + table + " where 1=1 ");

		if (conditions != null) {
			for (HashMap<String, Object> mapConditions : conditions) {

				sql.append("and " + mapConditions.get("whereName") + " " + mapConditions.get("whereOperate") + " '" + mapConditions.get("whereValue") + "'");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * ���ݱ�����ȡ��ϸ��Ϣ
	 * 
	 * @param table
	 * @param primaryKey
	 *            ������ֵ��1�����������ƣ�key 2��������ֵ�� keyValue
	 * @return
	 */
	public HashMap<String, Object> getDetailed(String table, HashMap<String, String> primaryKey) {
		try {
			HashMap<String, Object> data = sqliteUitl.queryTableDataByTableIdReturnHashMap(table, primaryKey);

			Log.v(TAG, "getDetailed return data.size --> " + data.size());

			return data;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return new HashMap<String, Object>();
	}

	/**
	 * ��������
	 * 
	 * @param values
	 * @param table
	 * @return
	 */
	public boolean insert(ArrayList<ContentValues> values, String table) {
		try {
			return sqliteUitl.insert(values, table) > 0;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * ���ݿ��Ƿ���ڸñ�
	 * 
	 * @param table
	 * @return
	 */
	public boolean checkTableExists(String table) {

		return sqliteUitl.checkTableExists(table);

	}

	/**
	 * �����������ݻ����ȫ������
	 * 
	 * @param data
	 *            Ҫ���µ�����
	 * @param table
	 *            Ҫ���µ�ĳһ�ű�ı���
	 * @return
	 */
	public static boolean synchronizeOneTableUpdateOrFetchAll(ArrayList<HashMap<String, Object>> data, String table, boolean updateOrFetchAllData) {
		/* ת��һ�����ݸ�ʽ */
		ArrayList<ContentValues> insertData = SqliteUtil.convertArrayListHashMapToArrayListContentValues(data);

		/* ¼�����ݿ��� */
		if (insertData != null && insertData.size() > 0) {
			try {
				if (updateOrFetchAllData) {
					ArrayList<String> value = new ArrayList<String>();

					String primaryKey = sqliteUitl.getTablePrimaryKey(table);
					for (HashMap<String, Object> map : data) {
						value.add((String) map.get(primaryKey));
					}
					if (value.size() > 0) {
						sqliteUitl.deleteNoTransaction(primaryKey, value, table);
					}

					// ����Ϊ�˲��Ⱥ�̨T_ZFWS_XWJLWD������ȱ�ݣ��ñ�û��GUID��ֻ�ܵ�����ɾ������
					if ("T_ZFWS_XWJLWD".equals(table)) {
						for (HashMap<String, Object> d : data) {
							String taskID = d.get("TaskID").toString();
							String entID = d.get("EntID").toString();
							String comeFrom = d.get("ComeFrom").toString();
							SqliteUtil.getInstance().deleteTransactioneExecSQL(
									"delete from " + table + " where TaskID = '" + taskID + "' and EntID = '" + entID + "' and ComeFrom = '" + comeFrom + "';");
						}
					}
				}
				long count = sqliteUitl.insertNoTransaction(insertData, table);

				if (count > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean synchronizeOneTable(// ��ArrayList��װ�����ݲ��뵽����
			ArrayList<HashMap<String, Object>> data, String table) {
		/* ת��һ�����ݸ�ʽ */
		ArrayList<ContentValues> insertData = SqliteUtil.convertArrayListHashMapToArrayListContentValues(data);

		/* ¼�����ݿ��� */
		if (insertData != null && insertData.size() > 0) {
			try {

				long count = sqliteUitl.insert(insertData, table);

				if (count > 0) {
					Log.v(TAG, "synchronizeOneTableLastestData " + table + " successful , effect " + count + " row .");
				} else {
					Log.v(TAG, "synchronizeOneTableLastestData " + table + " fail , effect 0 row .");
				}
			} catch (SQLiteException e) {
				Log.e(TAG, e.getMessage());
				return false;
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ȡ�����ļ���URL
	 * 
	 * @param table
	 *            ����
	 * @param column_fileContent
	 *            �ļ���������
	 * @param column_fileName
	 *            �ļ���������
	 * @param column_fileId
	 *            �ļ�Ψһ��ʶ����
	 * @param textId
	 *            �ļ�Ψһ��ʶ��ֵ
	 * @param namespace
	 *            webserice�����ռ�
	 * @param url
	 *            webserice��url
	 * @param methodName
	 *            webserice�ķ�����
	 * 
	 * @return
	 */
	public String getFileURL(String table, String column_fileContent, String column_fileName, String column_fileId, String textId, String namespace, String url, String methodName) {

		/* ����webserice�Ĳ������� */
		ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", table);
		params.add(param);

		String columns = "content:" + column_fileContent + ";filename:" + column_fileName + ";id:" + column_fileId;

		param = new HashMap<String, Object>();
		param.put("columns", columns);
		params.add(param);

		param = new HashMap<String, Object>();
		param.put("idvalue", textId);
		params.add(param);

		/* �Ӻ�̨ȡ��ĳһ�ű��xml�ַ������� */
		String resultResponse = "";
		try {
			resultResponse = String.valueOf(WebServiceProvider.callWebService(namespace, methodName, params, url, WebServiceProvider.RETURN_BOOLEAN, true));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return "";
		}
		return resultResponse == null || "".equals(resultResponse) || "null".equals(resultResponse) ? "" : resultResponse;
	}

	/**
	 * ��ѯָ����sql��䣬����ArrayList<HashMap<String, Object>>
	 * 
	 * @param sql
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> queryBySqlReturnArrayListHashMap(String sql) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		try {
			data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return data;
	}

	/**
	 * �������ݿ�����
	 * 
	 * @param table
	 *            ����
	 * @param updateValues
	 *            Ҫ���µ����������Ͷ�Ӧ��ֵ
	 * @param whereClause
	 *            �������� id = ?
	 * @param whereArgs
	 *            ������������ new String[]{ id }
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean updateTable(String table, ContentValues updateValues, String whereClause, String[] whereArgs) {
		try {
			sqliteUitl.update(table, updateValues, whereClause, whereArgs);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * ִ��SQL���
	 * 
	 * @param sql
	 * @return
	 */
	public boolean ExecSQL(String sql) {
		try {
			sqliteUitl.execute(sql);
			return true;
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * �����û������ֵ���������� and (name= '1' or name like '%��%') ����䣬sql����Ż�query list ʹ��
	 * 
	 * @param filterMap
	 *            �û����������
	 * @return ������ and (name= '1' or name like '%��%') �����
	 */
	public String getFilterForSql(HashMap<String, Object> filterMap) {
		StringBuffer sql = new StringBuffer();
		if (filterMap != null && filterMap.size() > 0) {
			Iterator<Entry<String, Object>> iterator = filterMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
				}

			}
		}
		return sql.toString();
	}

	/**
	 * �����û������ֵ���������� xxID = 'xxx' ����䣬sql����Ż�detail ʹ��
	 * 
	 * @param filterMap
	 *            ���listViewĳһ��ʱ����������
	 * @return ������ xxID = 'xxx' �����
	 */
	public String getFilterForSqlDetail(HashMap<String, String> filterMap) {
		String key = filterMap.get("key");
		String keyValue = filterMap.get("keyValue");
		String sql = " [" + key + "] = '" + keyValue + "'";
		return sql;
	}

	/**
	 * Description: �ݹ��ѯ
	 * 
	 * @param tableName
	 *            ����
	 * @param data
	 *            �洢����
	 * @param conditions
	 *            ��ѯ���� condition= {DepParentID=depta1} ����ڵ��ѯ�����ݺ��������������ݹ������ѯ
	 * @param coulm
	 * @param itself
	 *            �Ƿ��ѯ����ڵ�����
	 * @author wanglg
	 * @Create at: 2013-6-4 ����8:39:47
	 */
	public void recursiveQuery(String tableName, ArrayList<HashMap<String, Object>> data, HashMap<String, Object> conditions, String coulm, Boolean itself) {
		Iterator iter = conditions.entrySet().iterator();
		Map.Entry entry = (Map.Entry) iter.next();
		String key = entry.getKey().toString().toLowerCase();
		String value = entry.getValue().toString();
		if (itself) {
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put(coulm, value);
			data.addAll(getList(tableName, condition));// ��ѯ�����ϼ� �Ľڵ�����
		}
		recursiveQueryChild(tableName, data, conditions, coulm, key);
	}

	/**
	 * Description: �ݹ��ѯ��Ԫ��
	 * 
	 * @param tableName
	 * @param data
	 * @param conditions
	 *            �ݹ����� ���� DepParentID=depta1
	 * @param coulm
	 * @param key
	 * @author wanglg
	 * @Create at: 2013-6-4 ����11:37:21
	 */
	public void recursiveQueryChild(String tableName, ArrayList<HashMap<String, Object>> data, HashMap<String, Object> conditions, String coulm, String key) {
		ArrayList<HashMap<String, Object>> mdata = getList(tableName, conditions);

		for (HashMap<String, Object> map : mdata) {
			data.add(map);
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put(key, map.get(coulm));
			recursiveQueryChild(tableName, data, condition, coulm, key);
		}
	}

	/**
	 * ��ҳ��ȡ���ݿ�����
	 * 
	 * @param tableName
	 *            ����
	 * @param coulm
	 *            �ֶ��� �ö��ŷָ�
	 * @param conditions
	 *            ��ѯ����
	 * @param limit
	 *            ��ҳ ���� 0,30
	 * @param order
	 *            ������� author wanglg
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getLimitDataList(String tableName, String coulm, HashMap<String, Object> conditions, String limit, String order) {

		StringBuilder sql = new StringBuilder("select " + coulm + " from " + tableName + " where 1=1 ");

		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append("and (" + condition.getKey().trim() + "= '" + condition.getValue() + "')");
				}

			}
		}
		sql.append(" order by " + order);
		sql.append(" limit " + limit);
		try {
			ArrayList<HashMap<String, Object>> data = sqliteUitl.queryBySqlReturnArrayListHashMap(sql.toString());

			Log.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();

	}
}
