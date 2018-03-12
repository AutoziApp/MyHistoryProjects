package cn.com.mapuni.meshing.base.dataprovider;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;


/**
 * sqlite 常用方法
 * @author xugf@mapuni.com
 *
 */
public class SqliteUtil implements Serializable{	
	/**FileName: SqliteUtil.java
	 * Description:
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-7 下午01:50:02
	 */
	private  final long serialVersionUID = 7544052462570119645L;

	/*public static final String BASE_PATH = "/sdcard/mapuni/";
	private static final String TAG = "SqliteUtil";
	sqlite存放的位置*/
	public static  String DB_FULL_PATH = PathManager.SDCARD_DB_LOCAL_PATH;
	
	private static  Context context;
	
	public  SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
	
	private static  SqliteUtil Instance = new SqliteUtil();

	private  final String TAG = "SqliteUtil";
	
//	private ArrayList<String> tables;

	private SqliteUtil() {
	}

	public  static SqliteUtil getInstance() {
		
		return Instance;
	}
    public  static SqliteUtil getInstance(Context mcontext) {
    	context=mcontext;
		return Instance;
	}
	
    /**
	 * 删除任务编辑里面添加的企业
	 * 
	 * @return
	 * 
	 */
	public void deleteCompanyBySql(String sql) {
		execute(sql);
	}

    
	/**
	 * 
	 * 获取数据库连接
	 * 
	 * @return SQLiteDatabase
	 *
	 * 
	 */
	public SQLiteDatabase  getConnection() {
		if (db != null) {
			if (db.isOpen()) {
//				LogUtil.v(TAG , "getConnection successful .");
				return db;
			}
		}
		try {
			db = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
		} catch (Exception e) {
			FileNotFoundException fe=new FileNotFoundException("数据库打开异常：" + "\n" + DB_FULL_PATH
					+ "\t\t找不到文件\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(fe, TAG);
			fe.printStackTrace();
			if(context!=null){
				Toast.makeText(context, "缺少数据库文件程序无法运行", Toast.LENGTH_LONG).show();
			}
			
		}
		LogUtil.v(TAG,"getConnection successful .");
		return db;
	}
	
	/**
	 * 删除所有的表
	 * 
	 */
	public void dropAllTables()  {
		String[] tables = getDBTables();
		if (tables.length > 0) {
			dropTablesByTables(tables);
			LogUtil.v(TAG, "dropAllTables successful . ");
		}
	}
	
	
	
	/**
	 * 获取当前数据库所有表名
	 * @return
	 * 
	 */
	public String[] getDBTables()  {
		db = getConnection();
		Cursor c = null;
		try {
			c = db.rawQuery( 
					"select name from sqlite_master where type='table' order by name", null);
		} catch (Exception e) {
			SQLiteException se=new SQLiteException("数据库查询异常：获取当前数据库所有表名失败(getDBTables)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			se.printStackTrace();
			return new String[0];
			//throw new SQLiteException("数据库查询异常");
			}
		StringBuilder sb = new StringBuilder();
		while (c.moveToNext()) {
			sb.append(c.getString(0) + ",");
		}
		c.close();
		String tables = sb.toString();
		if (tables.indexOf(",") != -1) {
			tables = tables.substring(0, tables.length() - 1);
		}
//		LogUtil.v(TAG, "getDBTables tables --> " + tables.toString());
		return tables.split(",");
	}
	
	/**
	 * 获取表的所有列名
	 * 
	 * @param table
	 * @return
	 * 
	 */
	public String[] getTableColumns(String table) throws  Exception {
		Cursor c = null;
		try {
			c = query(table, null, null, null, null, null, null);
		} catch (Exception e) {
			SQLiteException se=new SQLiteException(table+"获取所有列名查询异常：(getTableColumns)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			se.printStackTrace();
			throw new SQLiteException("数据库查询异常");
		}
		String[] columns = c.getColumnNames();
		c.close();
		StringBuilder sb = new StringBuilder();
		for (String s : columns) {
			sb.append(s + ",");
		}
		String tableColumns = sb.toString();
		if (tableColumns.indexOf(",") != -1) {
			tableColumns = tableColumns.substring(0, tableColumns.length() - 1);
		}
		LogUtil.v(TAG,
				"getTableColumns " + table + " columns --> "
						+ tableColumns.toString());
		return tableColumns.split(",");
	}
	
	/**
	 * 获取该表的主键（主键特点是表结构的第一个列名）
	 * 
	 * @param table
	 * @return
	 * 
	 */
	public  String getTablePrimaryKey(String table) throws Exception {
		String primary = getTableColumns(table)[0];
		LogUtil.v(TAG,
				"getTablePrimaryKey " + table + " primary key --> "
						+ primary);
		return primary;
	}
	
	/**
	 * 检查数据库是否存在该表
	 *
	 * @param table  表名
	 * @return exists : true 
	 * 
	 */
	public boolean checkTableExists(String table)  {
//		String[] tables = getDBTables();
//		if(tables == null || tables.size() == 0){
//			tables = (ArrayList<String>) Arrays.asList(getDBTables());
//			if(tables == null || tables.size() == 0)
//				return false;
//		}
//		return tables.contains(table);
		return true;
	}
	
	/**
	 * 查询表(此方法在调用完成后必须关闭游标)
	 *
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 * 
	 */
	public Cursor query(String table, String[] columns, String selection,String[] selectionArgs, String groupBy, String having,
			String orderBy) throws Exception {

		db = getConnection();
		Cursor c = null;
		try {
			c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		} catch (SQLiteException e) {
			SQLiteException se=new SQLiteException(table+"数据库查询异常：(query)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw  new SQLiteException("数据库查询异常") ;
			
		}
		return c;
	}
	
	/**
	 * 查询该表的所有数据，返回ArrayList<HashMap<String, Object>>
	 * 
	 * @param table
	 * @return
	 * 
	 */
	public ArrayList<HashMap<String, Object>> queryAllTableDataByTableReturnArrayListHashMap(
			String table) throws Exception {
		
		LogUtil.v(TAG, "queryAllTableDataByTableReturnArrayListHashMap parameters : table --> " + table);
		
		Cursor c = null;
		try {
			c = query(table, null, null, null, null, null, null);
		} catch (Exception e) {
			SQLiteException se=new SQLiteException(table+"数据库查询异常：(queryAllTableDataByTableReturnArrayListHashMap)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		return convertCursorToArrayListHashMap(c);
	}
	
	/**
	 * 查询指定的sql语句(此方法在调用完成后必须关闭游标)
	 * 
	 * @param sql
	 * @return
	 * 
	 */
	public Cursor queryBySql(String sql)  {
		System.out.println("sql+++sql++++>>>>"+sql);
		Cursor c = null;
		
			try {
				db = getConnection();
				c = db.rawQuery(sql, null);
			} catch (Exception e) {
				
				SQLiteException se=new SQLiteException("sql语句错误："+sql+"数据库查询异常：(queryBySql)\n"+e.getMessage());
				RecordLog.WriteCaughtEXP(se, TAG);
				LogUtil.i(TAG, se.getMessage());
			}

		System.out.println("c>>>>>"+c);
		return c;
	}
	
	/**
	 * 查询指定的sql语句，返回ArrayList<HashMap<String, Object>>
	 * 
	 * @param sql
	 * @return 如果sql语句错误返回 new ArrayList<HashMap<String,Object>>()
	 * 
	 */
	public ArrayList<HashMap<String, Object>> queryBySqlReturnArrayListHashMap(String sql)  {
		
		System.out.println("sql>>>>"+sql);
		Cursor data_cursor = queryBySql(sql);
		if(data_cursor!=null){
			System.out.println("data_cursor>>>"+data_cursor);
			return convertCursorToArrayListHashMap(data_cursor);
		}else{
			return new ArrayList<HashMap<String,Object>>();
		}
		
	}
	
	/**
	 * 根据表的主键查询表数据(此方法在调用完成后必须关闭游标)
	 * 
	 * @param table
	 * @param primaryKey
	 * @return
	 * @throws SQLiteException 
	 */
	public Cursor queryTableDataByTableId(String table,	HashMap<String, String> primaryKey) throws SQLiteException {
		String key      = primaryKey.get("key");
		String keyValue = primaryKey.get("keyValue");
		
		LogUtil.v(TAG, "queryTableDataByTableId parameters : table --> " + table
				+ "; primaryKey --> " + primaryKey.toString());
		
		db = getConnection();
		String sql = "SELECT * FROM [" + table + "] WHERE [" + key + "] = '" + keyValue + "'";
		
		Cursor c = null;
		try {
			c = db.rawQuery(sql, null);			
		} catch (Exception e) {
			SQLiteException se=new SQLiteException(table+"sql语句为："+sql+","+"数据库查询异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		
		return c;
	}
	
	/**
	 * 根据表的主键查询表数据，返回HashMap数据结果集
	 * 
	 * @param table
	 * @param primaryKey
	 * @return
	 * @throws FileNotFoundException 
	 */
	public HashMap<String, Object> queryTableDataByTableIdReturnHashMap(String table, HashMap<String, String> primaryKey) {
		
		LogUtil.v(TAG, "queryTableDataByTableIdReturnHashMap parameters : table --> " + table
				+ "; primaryKey --> " + primaryKey.toString());
		
		Cursor detail_cursor = queryTableDataByTableId(table, primaryKey);
		return convertCursorToHashMap(detail_cursor);
	}
	
	/**
	 * 多行数据插入方法
	 * 
	 * @param values
	 * @param table
	 * @throws FileNotFoundException 
	 */
	public long insert(ArrayList<ContentValues> values, String table)throws SQLiteException {
		db = getConnection();
		
		long count = 0;
		
		try {
			db.beginTransaction();
			for (ContentValues contentValues : values) {
				
				if (db.insert(table, null, contentValues) > 0) {
					count ++;
				}
			}
			
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			SQLiteException se=new SQLiteException(table+"数据库插入数据异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw new SQLiteException("数据库插入异常");
		}
		
		return count;
	}
	/**
	 * 多行数据插入方法 没有事务操作
	 * 
	 * @param values
	 * @param table
	 * @throws FileNotFoundException 
	 */
	public long insertNoTransaction(ArrayList<ContentValues> values, String table)throws SQLiteException {
		db = getConnection();
		
		long count = 0;
		
		try {
			
			for (ContentValues contentValues : values) {
				
				if (db.insert(table, null, contentValues) > 0) {
					count ++;
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			SQLiteException se=new SQLiteException(table+"数据库插入数据异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw new SQLiteException("数据库插入异常");
		}
		
		return count;
	}
	
	
	/**
	 * 单行插入
	 * @param values
	 * @param table
	 * @return
	 */
	public long insert(ContentValues values, String table)
			  {
		db = getConnection();
		
		long count = 0;
		
		try {
			db.beginTransaction();
				
			if (db.insert(table, null, values) > 0) {
				count ++;
			}
			
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			SQLiteException se=new SQLiteException(table+"数据库插入数据异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		
		return count;
	}
	
	public long insert1(ContentValues values, String table)
	{
		db = getConnection();
		
		long count = 0;
		
		try {
			db.beginTransaction();
		count=	db.insert(table, null, values);
			
		
			
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			SQLiteException se=new SQLiteException(table+"数据库插入数据异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		
		return count;
	}
	
	/**
	 * 更新数据库表操作
	 *
	 * @param table		        表名
	 * @param updateValues 要更新的数据列名和对应的值
	 * @param whereClause  条件，如 id = ?
	 * @param whereArgs	        条件参数，如 new String[]{ id }
	 * @return
	 * @throws FileNotFoundException
	 */
	public int update(String table, ContentValues updateValues,
			String whereClause, String[] whereArgs) throws FileNotFoundException {
		db = getConnection();
		int count = db.update(table, updateValues, whereClause, whereArgs);
		
		return count;
	}
	
	/**
	 * 多行数据删除方法
	 * 
	 * @param primaryKey 主键的名称和主键的值，多个主键值之间以逗号来间隔
	 * @param table  要操作的表名
	 * @throws FileNotFoundException 
	 */
	public void delete(HashMap<String, String> primaryKey, String table)
			{
		
		/*获取主键的名称和对应的值*/
		String key      = primaryKey.get("key");
		String keyValue = primaryKey.get("keyValue");
		
		LogUtil.v(TAG, "delete : table --> " + table
				+ "; primaryKey --> " + primaryKey.toString());
		
		db = getConnection();
		
		/*条件语句*/
		String whereString = key + " in (?)";
		
		try {
			/*执行删除操作*/
			db.beginTransaction();
			db.delete(table, whereString, new String[] { keyValue });
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			SQLiteException se=new SQLiteException("数据库删除数据异常：\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}		
		
	}
	
	/**
	 * 多行数据删除方法
	 * 
	 * @param primaryKey 主键的名称和主键的值，多个主键值之间以逗号来间隔
	 * @param table  要操作的表名
	 * @throws FileNotFoundException 
	 */
	public void delete(String key,ArrayList<String> keyValue, String table) {
		/*获取主键的名称和对应的值*/
		String[] valueStrings = new String[keyValue.size()];
		for(Integer i=0;i<keyValue.size();i++)
		{
			valueStrings[i] =keyValue.get(i);
		}
		LogUtil.v(TAG, "delete : table --> " + table
				+ "; primaryKey --> ");
		db = getConnection();
		try {
			/*执行删除操作*/
			db.beginTransaction();
			String whereString = GetClauseSQL(valueStrings);
			String deleteSql = "DELETE FROM "+table+" WHERE 1=1 AND "+key +" in ("+whereString+")";
			db.execSQL(deleteSql);
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			RecordLog.WriteCaughtEXP(new SQLiteException("表"+table+"删除数据异常：\n" + e.getMessage()), TAG);
			throw new SQLiteException("表"+table+"删除数据异常：\n" + e.getMessage());
		}
		
	}
	/**
	 * 多行数据删除方法  没有事务操作 用于批量操作数据库，在外部手动加上事务操作
	 * 
	 * @param primaryKey 主键的名称和主键的值，多个主键值之间以逗号来间隔
	 * @param keyValue 要删除的数据条件值
	 * @param table  要操作的表名
	 */
	public void deleteNoTransaction(String primaryKey,ArrayList<String> keyValue, String table){

		/*获取主键的名称和对应的值*/
		String[] valueStrings = new String[keyValue.size()];
		for(Integer i=0;i<keyValue.size();i++)
		{
			valueStrings[i] =keyValue.get(i);
		}
		LogUtil.v(TAG, "delete : table --> " + table+ "; primaryKey --> ");
	
		try {
			/*执行删除操作*/
		
			String whereString = GetClauseSQL(valueStrings);
			String deleteSql = "DELETE FROM "+table+" WHERE 1=1 AND "+primaryKey +" in ("+whereString+")";
			db.execSQL(deleteSql);
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("表"+table+"删除数据异常：\n" + e.getMessage()), TAG);
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * 获取条件语句
	 * @param valueStrings
	 * @return
	 */
	private String GetClauseSQL(String[] valueStrings)
	{
		String returnValue ="'"+valueStrings[0]+"'";
		if(valueStrings.length>1)
		{
			for(int i=1;i<valueStrings.length;i++)
			{			
				returnValue=returnValue+",'"+valueStrings[i]+"'";
			}
		}
		
		return returnValue;
	}
	
	/**
	 * 根据提供的所有表名，删除该表的所有数据
	 * 
	 * @param tables 多个表名之间以逗号间隔
	 * @throws FileNotFoundException 
	 */
	public void deleteAllTableDataByTables(String tables) throws SQLiteException {
		
		String[] tableArrayString = tables.split(",");
		
		try {
			/*删除数据库中所有的表*/
			for (String table : tableArrayString) {
				String sql = "DELETE FROM [" + table + "] WHERE 1=1";
				execute(sql);
			}
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("数据库删除数据异常：\n" + e.getMessage()), TAG);
			throw new SQLiteException("数据库删除数据异常");
		}
		
	}
	
	/**
	 * 根据提供的表名，将该表从数据库中删除掉
	 * 
	 * @param table 表名集合
	 * @throws FileNotFoundException 
	 */
	public void dropTablesByTables(String[] tables) throws  SQLiteException {
		
		db = getConnection();

		try {
			/*删除数据库中所有的表*/
			db.beginTransaction();
			for (String table : tables) {
				String sql = "DROP TABLE IF EXISTS [" + table + "]";
				LogUtil.v(TAG, "dropTablesByTables sql --> " + sql);
				execute(sql);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			RecordLog.WriteCaughtEXP(new SQLiteException("数据库删除表异常：(dropTablesByTables)\n" + e.getMessage()), TAG);
			throw new SQLiteException("数据库删除表异常");
		}
		
	}
	
	/**
	 * 执行SQL语句
	 * 执行语句为事物操作
	 * @param sql
	 * @throws FileNotFoundException 
	 */
	public void execute(String sql) throws  SQLiteException {
		db = getConnection();
		
		LogUtil.v(TAG, "execute sql --> " + sql);
		db.beginTransaction();
		try {
			db.execSQL(sql);
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			RecordLog.WriteCaughtEXP(new SQLiteException("sql:"+sql+",SQL语句异常：(execute)\n" + e.getMessage()), TAG);
			throw new SQLiteException("SQL语句异常：\n" + e.getMessage());
		}
		
	}
	
	/**
	 * 将ArrayList<HashMap<String, Object>>重新组装为ArrayList<ContentValues>的数据格式
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<ContentValues> convertArrayListHashMapToArrayListContentValues(ArrayList<HashMap<String, Object>> data) {
		
		ArrayList<ContentValues> convertedData = new ArrayList<ContentValues>();
		ContentValues contentValues		       = null;
		
		try {
			/*重新组装一下数据*/
			for (HashMap<String, Object> dataRow : data) {
				Iterator<Map.Entry<String, Object>> dataRowIterator = dataRow.entrySet().iterator();
				contentValues 			 							= new ContentValues();
				while (dataRowIterator.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>)dataRowIterator.next();
					contentValues.put(String.valueOf(entry.getKey()), 
							String.valueOf(entry.getValue()));
				}
				if (contentValues.size() > 0) {
					convertedData.add(contentValues);
				}
			}
		} catch (Exception e) {
			/*RecordLog.WriteCaughtEXP(new SQLiteException(
					"将ArrayList<HashMap<String, Object>>重新组装为ArrayList<ContentValues>过程中发生异常：\n"
					+ e.getMessage()), TAG); */
			throw new SQLiteException(
					"将ArrayList<HashMap<String, Object>>重新组装为ArrayList<ContentValues>过程中发生异常：\n"
							+ e.getMessage());
		}
		
		return convertedData;
	}

	
	
	
	/**
	 * 读取Cursor数据，组装成ArrayList<HashMap<String, Object>>返回
	 * 
	 * @param cursor_data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> convertCursorToArrayListHashMap(Cursor data_cursor) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> dataRow     	= null;
		HashMap<String, Integer> dataKey    	= null; 
		
		try {
			String[] tableColumon = data_cursor.getColumnNames();//得到所有的key
			dataKey=new HashMap<String, Integer>();//初始化key对其在本行的位置
			Integer index= null;
			while (data_cursor.moveToNext()) {	
				dataRow = new HashMap<String, Object>();
				for (String col : tableColumon) {
					/*列值如果为null，则赋“”*/
					index=dataKey.get(col);
					if(index==null){
						index=data_cursor.getColumnIndex(col);
						dataKey.put(col, index);
					}
					String colValue = data_cursor.getString(index);//得到当前Cursor 对应key的脚标，然后取得对应的值
					if (colValue == null)  {
						colValue = "";
					}
					dataRow.put(col.toLowerCase(), colValue);
				}
				data.add(dataRow);
			
//			
			}
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("用户解析游标数据出错：\n" + e.getMessage()), TAG);
			//throw new SQLiteException("用户解析游标数据出错：\n" + e.getMessage());
		} finally{
			if(data_cursor!=null){
				data_cursor.close();
			}
			
		}
		System.out.println("解析后data数量>>>"+data.size());
		return data;
	}
	
	/**
	 * 读取Cursor数据，组装成HashMap<String, Object>返回
	 * 
	 * @param cursor_data
	 * @return
	 */
	public HashMap<String, Object> convertCursorToHashMap(Cursor data_cursor) {
		HashMap<String, Object> dataRow = new HashMap<String, Object>();
		try {
			String[] tableColumon		= data_cursor.getColumnNames();
			while (data_cursor.moveToNext()) {
				for (String col : tableColumon) {
					/*列值如果为null，则赋“”*/
					String colValue = data_cursor.getString(data_cursor.getColumnIndex(col));
					if (colValue == null)  {
						colValue = "";
					}
					dataRow.put(col.toLowerCase(), colValue);
				}
			}
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("用户解析游标数据出错：\n" + e.getMessage()), TAG);
			//throw new SQLiteException("用户解析游标数据出错：\n" + e.getMessage());
		} finally{
			data_cursor.close();
		}
		
		return dataRow;
	}
	
	/**
	 * 读取一列的所有数据
	 *
	 * @param data_cursor
	 * @return
	 */
	public String[] convertCursorToStringArray(Cursor data_cursor) {
		StringBuilder row_one = new StringBuilder("");
		while (data_cursor.moveToNext()) {
			row_one.append(data_cursor.getString(0) + ",");
		}
		data_cursor.close();
		
		if (row_one.toString().indexOf(",") != -1) {
			row_one = new StringBuilder(row_one.toString().substring(0,
					row_one.toString().length() - 1));
		}
		return row_one.toString().split(",");
	}
	/**
	 * Description: 查询表中某字段的最大值 
	 * @param column 字段名 其类型必须为int类型
	 * @param table
	 * @return
	 * @author Administrator
	 * @Create at: 2013-9-4 下午2:36:36
	 */
	public int queryMaxValue(String column,String table) {
		db=getConnection();
		int count=-1;
		Cursor c = null;
		String mysql="select max("+column+") from "+table;
		
		try {
			c=db.rawQuery(mysql, null);
			
			if(c.moveToNext()){
				count=c.getInt(0);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
		}
		return count;
		
		
		
	}
	/**
	 * 根据SQL语句查询数据库中的一行，而且是唯一的一行，以HashMap<String,Object> 的形势返回,查询详细信息时使用
	 * @param sql	SQL语句
	 * @return		
	 * @throws Exception
	 */
	public HashMap<String, Object> getDataMapBySqlForDetailed(String sql){
		LogUtil.d("My Sql Is ===>", sql);
		HashMap<String, Object> map	= null;
		Cursor c = null;
		try {
			map	= new HashMap<String,Object>();
			db = getConnection();
			c = db.rawQuery(sql, null);
			String[] columnNames = c.getColumnNames();
			if(c.getCount()>0){
				if(c.moveToFirst()){
					for (String key : columnNames) {
						map.put(key.toLowerCase(), c.getString(c.getColumnIndex(key)));
					}
				}
			}
		} catch (Exception e) {
			LogUtil.d("SqliteUtil", "数据库连接异常");
			e.printStackTrace();
		} finally{
			if(c!=null){
				c.close();
			}
		}
		return map;
	}
	
	/**
	 * 根据SQL语句查询某一列
	 * @param sql	SQL语句
	 * @return		
	 * @throws Exception
	 */
	public String getDepidByUserid(String sql){
		Cursor c = null;
		String result = "";
		try {
			db = getConnection();
			c = db.rawQuery(sql, null);
			String[] columnNames = c.getColumnNames();
			if(c.getCount()>0){
				if(c.moveToFirst()){
					for (String key : columnNames) {
						result = c.getString(c.getColumnIndex(key));
					}
				}
			}
		} catch (Exception e) {
			LogUtil.d("SqliteUtil", "数据库连接异常");
			e.printStackTrace();
		} finally{
			if(c!=null){
				c.close();
			}
			if(result == null) {
				result = "";
			}
		}
		return result;
		
	}
	
	public String getDepidByDepName(String sql){
		Cursor c = null;
		String result = "";
		try {
			db = getConnection();
			c = db.rawQuery(sql, null);
			String[] columnNames = c.getColumnNames();
			if(c.getCount()>0){
				if(c.moveToFirst()){
					for (String key : columnNames) {
						result = c.getString(c.getColumnIndex(key));
					}
				}
			}
		} catch (Exception e) {
			LogUtil.d("SqliteUtil", "数据库连接异常");
			e.printStackTrace();
		} finally{
			if(c!=null){
				c.close();
			}
		}
		return result;
		
	}
	
	
	
	

	/**
	 * 关闭数据库
	 */
	
	public void closeDataBase(){
		db.close();
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql);

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());
			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
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
				/*sql.append("and " + condition.getKey() + " = '"
						+ condition.getValue() + "'");*/
				sql.append("and (" + condition.getKey() + " = '"
						+ condition.getValue() + "')");
			}
		}
		try {
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			HashMap<String, Object> data = 
					queryTableDataByTableIdReturnHashMap(table, primaryKey);

			LogUtil.v(TAG, "getDetailed return data.size --> " + data.size());

			return data;
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return new HashMap<String, Object>();
	}


	/**
	 * 更新最新数据或更新全部数据
	 * 
	 * @param data
	 *            要更新的数据
	 * @param table
	 *            要更新的某一张表的表名
	 * @param updateOrFetchAllData       
	 *            fasle 更新全部  true 更新最新     
	 * @return
	 */
	public   boolean synchronizeOneTableUpdateOrFetchAll(ArrayList<HashMap<String, Object>> data, String table,
			boolean updateOrFetchAllData) {
		/* 转换一下数据格式 */
		ArrayList<ContentValues> insertData = convertArrayListHashMapToArrayListContentValues(data);

		/* 录入数据库中 */
		if (insertData != null && insertData.size() > 0) {
			try {
				if (updateOrFetchAllData) {
					ArrayList<String> value = new ArrayList<String>();
					
					String primaryKey = getTablePrimaryKey(table);
					for (HashMap<String, Object> map : data) {
						value.add((String) map.get(primaryKey));
						
					}
					if (value.size() > 0) {
						delete(primaryKey, value, table);
					}
				}
				long count = insert(insertData, table);

				if (count > 0) {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " successful , effect " + count + " row .");
				} else {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " fail , effect 0 row .");
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
				return false;
			}
		}
		return true;
	}

	public  boolean synchronizeOneTable(// 将ArrayList封装的数据插入到表中
			ArrayList<HashMap<String, Object>> data, String table) {
		/* 转换一下数据格式 */
		ArrayList<ContentValues> insertData = 
				convertArrayListHashMapToArrayListContentValues(data);

		/* 录入数据库中 */
		if (insertData != null && insertData.size() > 0) {
			try {

				long count = insert(insertData, table);

				if (count > 0) {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " successful , effect " + count + " row .");
				} else {
					LogUtil.v(TAG, "synchronizeOneTableLastestData " + table
							+ " fail , effect 0 row .");
				}
			} catch (SQLiteException e) {
				LogUtil.e(TAG, e.getMessage());
				return false;
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
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
			LogUtil.e(TAG, e.getMessage());
			return "";
		}
		return resultResponse == null || "".equals(resultResponse)
				|| "null".equals(resultResponse) ? "" : resultResponse;
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
			update(table, updateValues, whereClause, whereArgs);
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
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
			execute(sql);
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
	 * Description: 递归查询,结果排序
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
	public void recursiveQueryOrder(String tableName,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> conditions, String coulm, Boolean itself,String order) {
		Iterator iter = conditions.entrySet().iterator();
		Map.Entry entry = (Map.Entry) iter.next();
		String key = entry.getKey().toString().toLowerCase();
		String value = entry.getValue().toString();
		if (itself) {
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put(coulm, value);
			data.addAll(getOrderList(tableName, condition,order));// 查询出最上级 的节点数据
		}
		recursiveQueryChildOrder(tableName, data, conditions, coulm, key,order);
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
	 * Description: 递归查询子元素,排序
	 */
	public void recursiveQueryChildOrder(String tableName,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> conditions, String coulm, String key,String order) {
		ArrayList<HashMap<String, Object>> mdata = getOrderList(tableName,
				conditions,order);

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
	 *            排序规则 
	 * author wanglg
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
		if(order!=null && !order.equals("")){
			sql.append(" order by " + order);
		}
		if(limit!=null &&!limit.equals("")){
			sql.append(" limit " + limit);
		}
		
		try {
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();

	}
	/**
	 * Description: 联合查询 拼sql条件查询
	 * @param sqlstr 联合查询sql语句
	 * @param conditions  条件集合
	 * @param limit 分页 
	 * @param order 排序
	 * @return
	 * @author wangliugeng
	 * @Create at: 2013-9-5 下午5:41:14
	 */
	public ArrayList<HashMap<String, Object>> getUnionQuery(String sqlstr,HashMap<String, Object> conditions
			,String limit, String order){
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
		if(order!=null && !order.equals("")){
			sql.append(" order by " + order);
		}
		if(limit!=null &&!limit.equals("")){
			sql.append(" limit " + limit);
		}
		
		try {
			ArrayList<HashMap<String, Object>> data = 
					queryBySqlReturnArrayListHashMap(sql.toString());

			LogUtil.v(TAG, "getList return data.size --> " + data.size());

			return data;
		} catch (SQLiteException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
		}
		return new ArrayList<HashMap<String, Object>>();
		
	}
	/**
	 * 查询符合条件的数据条数
	 * @param table 表名
	 * @param conditions 条件
	 * @return
	 */
	public int getNeedNumByCondition(String table,HashMap<String, Object> conditions){
		StringBuilder sql = new StringBuilder("select count(*) from "+table+" where 1=1 ");
		if (conditions != null && conditions.size() > 0) {
			Iterator<Entry<String, Object>> iterator = conditions.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> condition = iterator.next();

				if (!condition.getValue().equals("")) {
					sql.append(" and (" + condition.getKey().trim() + "= '"
							+ condition.getValue() + "')");
				}

			}
		}
		db=getConnection();
		
		int count=-1;
		Cursor c = null;
		String mysql=sql.toString();
		
		try {
			c=db.rawQuery(mysql, null);
			
			if(c.moveToNext()){
				count=c.getInt(0);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
		}
		return count;
		
		
	}
}
