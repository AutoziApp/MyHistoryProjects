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
 * sqlite ���÷���
 * @author xugf@mapuni.com
 *
 */
public class SqliteUtil implements Serializable{	
	/**FileName: SqliteUtil.java
	 * Description:
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾
	 * Create at: 2012-12-7 ����01:50:02
	 */
	private  final long serialVersionUID = 7544052462570119645L;

	/*public static final String BASE_PATH = "/sdcard/mapuni/";
	private static final String TAG = "SqliteUtil";
	sqlite��ŵ�λ��*/
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
	 * ɾ������༭������ӵ���ҵ
	 * 
	 * @return
	 * 
	 */
	public void deleteCompanyBySql(String sql) {
		execute(sql);
	}

    
	/**
	 * 
	 * ��ȡ���ݿ�����
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
			FileNotFoundException fe=new FileNotFoundException("���ݿ���쳣��" + "\n" + DB_FULL_PATH
					+ "\t\t�Ҳ����ļ�\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(fe, TAG);
			fe.printStackTrace();
			if(context!=null){
				Toast.makeText(context, "ȱ�����ݿ��ļ������޷�����", Toast.LENGTH_LONG).show();
			}
			
		}
		LogUtil.v(TAG,"getConnection successful .");
		return db;
	}
	
	/**
	 * ɾ�����еı�
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
	 * ��ȡ��ǰ���ݿ����б���
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
			SQLiteException se=new SQLiteException("���ݿ��ѯ�쳣����ȡ��ǰ���ݿ����б���ʧ��(getDBTables)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			se.printStackTrace();
			return new String[0];
			//throw new SQLiteException("���ݿ��ѯ�쳣");
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
	 * ��ȡ�����������
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
			SQLiteException se=new SQLiteException(table+"��ȡ����������ѯ�쳣��(getTableColumns)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			se.printStackTrace();
			throw new SQLiteException("���ݿ��ѯ�쳣");
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
	 * ��ȡ�ñ�������������ص��Ǳ�ṹ�ĵ�һ��������
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
	 * ������ݿ��Ƿ���ڸñ�
	 *
	 * @param table  ����
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
	 * ��ѯ��(�˷����ڵ�����ɺ����ر��α�)
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
			SQLiteException se=new SQLiteException(table+"���ݿ��ѯ�쳣��(query)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw  new SQLiteException("���ݿ��ѯ�쳣") ;
			
		}
		return c;
	}
	
	/**
	 * ��ѯ�ñ���������ݣ�����ArrayList<HashMap<String, Object>>
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
			SQLiteException se=new SQLiteException(table+"���ݿ��ѯ�쳣��(queryAllTableDataByTableReturnArrayListHashMap)\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		return convertCursorToArrayListHashMap(c);
	}
	
	/**
	 * ��ѯָ����sql���(�˷����ڵ�����ɺ����ر��α�)
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
				
				SQLiteException se=new SQLiteException("sql������"+sql+"���ݿ��ѯ�쳣��(queryBySql)\n"+e.getMessage());
				RecordLog.WriteCaughtEXP(se, TAG);
				LogUtil.i(TAG, se.getMessage());
			}

		System.out.println("c>>>>>"+c);
		return c;
	}
	
	/**
	 * ��ѯָ����sql��䣬����ArrayList<HashMap<String, Object>>
	 * 
	 * @param sql
	 * @return ���sql�����󷵻� new ArrayList<HashMap<String,Object>>()
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
	 * ���ݱ��������ѯ������(�˷����ڵ�����ɺ����ر��α�)
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
			SQLiteException se=new SQLiteException(table+"sql���Ϊ��"+sql+","+"���ݿ��ѯ�쳣��\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		
		return c;
	}
	
	/**
	 * ���ݱ��������ѯ�����ݣ�����HashMap���ݽ����
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
	 * �������ݲ��뷽��
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
			SQLiteException se=new SQLiteException(table+"���ݿ���������쳣��\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw new SQLiteException("���ݿ�����쳣");
		}
		
		return count;
	}
	/**
	 * �������ݲ��뷽�� û���������
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
			SQLiteException se=new SQLiteException(table+"���ݿ���������쳣��\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw new SQLiteException("���ݿ�����쳣");
		}
		
		return count;
	}
	
	
	/**
	 * ���в���
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
			SQLiteException se=new SQLiteException(table+"���ݿ���������쳣��\n" + e.getMessage());
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
			SQLiteException se=new SQLiteException(table+"���ݿ���������쳣��\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}
		
		return count;
	}
	
	/**
	 * �������ݿ�����
	 *
	 * @param table		        ����
	 * @param updateValues Ҫ���µ����������Ͷ�Ӧ��ֵ
	 * @param whereClause  �������� id = ?
	 * @param whereArgs	        ������������ new String[]{ id }
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
	 * ��������ɾ������
	 * 
	 * @param primaryKey ���������ƺ�������ֵ���������ֵ֮���Զ��������
	 * @param table  Ҫ�����ı���
	 * @throws FileNotFoundException 
	 */
	public void delete(HashMap<String, String> primaryKey, String table)
			{
		
		/*��ȡ���������ƺͶ�Ӧ��ֵ*/
		String key      = primaryKey.get("key");
		String keyValue = primaryKey.get("keyValue");
		
		LogUtil.v(TAG, "delete : table --> " + table
				+ "; primaryKey --> " + primaryKey.toString());
		
		db = getConnection();
		
		/*�������*/
		String whereString = key + " in (?)";
		
		try {
			/*ִ��ɾ������*/
			db.beginTransaction();
			db.delete(table, whereString, new String[] { keyValue });
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			SQLiteException se=new SQLiteException("���ݿ�ɾ�������쳣��\n" + e.getMessage());
			RecordLog.WriteCaughtEXP(se, TAG);
			throw se;
		}		
		
	}
	
	/**
	 * ��������ɾ������
	 * 
	 * @param primaryKey ���������ƺ�������ֵ���������ֵ֮���Զ��������
	 * @param table  Ҫ�����ı���
	 * @throws FileNotFoundException 
	 */
	public void delete(String key,ArrayList<String> keyValue, String table) {
		/*��ȡ���������ƺͶ�Ӧ��ֵ*/
		String[] valueStrings = new String[keyValue.size()];
		for(Integer i=0;i<keyValue.size();i++)
		{
			valueStrings[i] =keyValue.get(i);
		}
		LogUtil.v(TAG, "delete : table --> " + table
				+ "; primaryKey --> ");
		db = getConnection();
		try {
			/*ִ��ɾ������*/
			db.beginTransaction();
			String whereString = GetClauseSQL(valueStrings);
			String deleteSql = "DELETE FROM "+table+" WHERE 1=1 AND "+key +" in ("+whereString+")";
			db.execSQL(deleteSql);
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			db.endTransaction();
			RecordLog.WriteCaughtEXP(new SQLiteException("��"+table+"ɾ�������쳣��\n" + e.getMessage()), TAG);
			throw new SQLiteException("��"+table+"ɾ�������쳣��\n" + e.getMessage());
		}
		
	}
	/**
	 * ��������ɾ������  û��������� ���������������ݿ⣬���ⲿ�ֶ������������
	 * 
	 * @param primaryKey ���������ƺ�������ֵ���������ֵ֮���Զ��������
	 * @param keyValue Ҫɾ������������ֵ
	 * @param table  Ҫ�����ı���
	 */
	public void deleteNoTransaction(String primaryKey,ArrayList<String> keyValue, String table){

		/*��ȡ���������ƺͶ�Ӧ��ֵ*/
		String[] valueStrings = new String[keyValue.size()];
		for(Integer i=0;i<keyValue.size();i++)
		{
			valueStrings[i] =keyValue.get(i);
		}
		LogUtil.v(TAG, "delete : table --> " + table+ "; primaryKey --> ");
	
		try {
			/*ִ��ɾ������*/
		
			String whereString = GetClauseSQL(valueStrings);
			String deleteSql = "DELETE FROM "+table+" WHERE 1=1 AND "+primaryKey +" in ("+whereString+")";
			db.execSQL(deleteSql);
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("��"+table+"ɾ�������쳣��\n" + e.getMessage()), TAG);
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * ��ȡ�������
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
	 * �����ṩ�����б�����ɾ���ñ����������
	 * 
	 * @param tables �������֮���Զ��ż��
	 * @throws FileNotFoundException 
	 */
	public void deleteAllTableDataByTables(String tables) throws SQLiteException {
		
		String[] tableArrayString = tables.split(",");
		
		try {
			/*ɾ�����ݿ������еı�*/
			for (String table : tableArrayString) {
				String sql = "DELETE FROM [" + table + "] WHERE 1=1";
				execute(sql);
			}
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("���ݿ�ɾ�������쳣��\n" + e.getMessage()), TAG);
			throw new SQLiteException("���ݿ�ɾ�������쳣");
		}
		
	}
	
	/**
	 * �����ṩ�ı��������ñ�����ݿ���ɾ����
	 * 
	 * @param table ��������
	 * @throws FileNotFoundException 
	 */
	public void dropTablesByTables(String[] tables) throws  SQLiteException {
		
		db = getConnection();

		try {
			/*ɾ�����ݿ������еı�*/
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
			RecordLog.WriteCaughtEXP(new SQLiteException("���ݿ�ɾ�����쳣��(dropTablesByTables)\n" + e.getMessage()), TAG);
			throw new SQLiteException("���ݿ�ɾ�����쳣");
		}
		
	}
	
	/**
	 * ִ��SQL���
	 * ִ�����Ϊ�������
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
			RecordLog.WriteCaughtEXP(new SQLiteException("sql:"+sql+",SQL����쳣��(execute)\n" + e.getMessage()), TAG);
			throw new SQLiteException("SQL����쳣��\n" + e.getMessage());
		}
		
	}
	
	/**
	 * ��ArrayList<HashMap<String, Object>>������װΪArrayList<ContentValues>�����ݸ�ʽ
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<ContentValues> convertArrayListHashMapToArrayListContentValues(ArrayList<HashMap<String, Object>> data) {
		
		ArrayList<ContentValues> convertedData = new ArrayList<ContentValues>();
		ContentValues contentValues		       = null;
		
		try {
			/*������װһ������*/
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
					"��ArrayList<HashMap<String, Object>>������װΪArrayList<ContentValues>�����з����쳣��\n"
					+ e.getMessage()), TAG); */
			throw new SQLiteException(
					"��ArrayList<HashMap<String, Object>>������װΪArrayList<ContentValues>�����з����쳣��\n"
							+ e.getMessage());
		}
		
		return convertedData;
	}

	
	
	
	/**
	 * ��ȡCursor���ݣ���װ��ArrayList<HashMap<String, Object>>����
	 * 
	 * @param cursor_data
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> convertCursorToArrayListHashMap(Cursor data_cursor) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> dataRow     	= null;
		HashMap<String, Integer> dataKey    	= null; 
		
		try {
			String[] tableColumon = data_cursor.getColumnNames();//�õ����е�key
			dataKey=new HashMap<String, Integer>();//��ʼ��key�����ڱ��е�λ��
			Integer index= null;
			while (data_cursor.moveToNext()) {	
				dataRow = new HashMap<String, Object>();
				for (String col : tableColumon) {
					/*��ֵ���Ϊnull���򸳡���*/
					index=dataKey.get(col);
					if(index==null){
						index=data_cursor.getColumnIndex(col);
						dataKey.put(col, index);
					}
					String colValue = data_cursor.getString(index);//�õ���ǰCursor ��Ӧkey�Ľű꣬Ȼ��ȡ�ö�Ӧ��ֵ
					if (colValue == null)  {
						colValue = "";
					}
					dataRow.put(col.toLowerCase(), colValue);
				}
				data.add(dataRow);
			
//			
			}
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("�û������α����ݳ���\n" + e.getMessage()), TAG);
			//throw new SQLiteException("�û������α����ݳ���\n" + e.getMessage());
		} finally{
			if(data_cursor!=null){
				data_cursor.close();
			}
			
		}
		System.out.println("������data����>>>"+data.size());
		return data;
	}
	
	/**
	 * ��ȡCursor���ݣ���װ��HashMap<String, Object>����
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
					/*��ֵ���Ϊnull���򸳡���*/
					String colValue = data_cursor.getString(data_cursor.getColumnIndex(col));
					if (colValue == null)  {
						colValue = "";
					}
					dataRow.put(col.toLowerCase(), colValue);
				}
			}
			
		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(new SQLiteException("�û������α����ݳ���\n" + e.getMessage()), TAG);
			//throw new SQLiteException("�û������α����ݳ���\n" + e.getMessage());
		} finally{
			data_cursor.close();
		}
		
		return dataRow;
	}
	
	/**
	 * ��ȡһ�е���������
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
	 * Description: ��ѯ����ĳ�ֶε����ֵ 
	 * @param column �ֶ��� �����ͱ���Ϊint����
	 * @param table
	 * @return
	 * @author Administrator
	 * @Create at: 2013-9-4 ����2:36:36
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
	 * ����SQL����ѯ���ݿ��е�һ�У�������Ψһ��һ�У���HashMap<String,Object> �����Ʒ���,��ѯ��ϸ��Ϣʱʹ��
	 * @param sql	SQL���
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
			LogUtil.d("SqliteUtil", "���ݿ������쳣");
			e.printStackTrace();
		} finally{
			if(c!=null){
				c.close();
			}
		}
		return map;
	}
	
	/**
	 * ����SQL����ѯĳһ��
	 * @param sql	SQL���
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
			LogUtil.d("SqliteUtil", "���ݿ������쳣");
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
			LogUtil.d("SqliteUtil", "���ݿ������쳣");
			e.printStackTrace();
		} finally{
			if(c!=null){
				c.close();
			}
		}
		return result;
		
	}
	
	
	
	

	/**
	 * �ر����ݿ�
	 */
	
	public void closeDataBase(){
		db.close();
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
	 * ���ݱ��������ѯ�ֶλ�ȡspinner����
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
	 * ģ����ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param colum
	 *            ��Ҫ��ѯ���ֶ�
	 * @param like
	 *            ģ����ѯ����
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
	 * �����ṩ���������ڱ��в�ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
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
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
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
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
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
	 * �����ṩ���������ڱ��������ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ����������֮����and���ӣ������Ĳ������� = ������ƥ������ֵ��
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

	/** һ��һ�����μ�����ݲ�ѯ */
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
	 * �����ṩ���������ڱ��в�ѯ����
	 * 
	 * @param table
	 *            ����
	 * @param condition
	 *            ɸѡ���� ������֮���� and ���ӣ�: whereName : Ҫ���������� whereOperate : Ҫ������ʽ��>
	 *            = < like .....�� whereValue : ����ֵ
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
	 * ���ݱ�����ȡ��ϸ��Ϣ
	 * 
	 * @param table
	 * @param primaryKey
	 *            ������ֵ��1�����������ƣ�key 2��������ֵ�� keyValue
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
	 * �����������ݻ����ȫ������
	 * 
	 * @param data
	 *            Ҫ���µ�����
	 * @param table
	 *            Ҫ���µ�ĳһ�ű�ı���
	 * @param updateOrFetchAllData       
	 *            fasle ����ȫ��  true ��������     
	 * @return
	 */
	public   boolean synchronizeOneTableUpdateOrFetchAll(ArrayList<HashMap<String, Object>> data, String table,
			boolean updateOrFetchAllData) {
		/* ת��һ�����ݸ�ʽ */
		ArrayList<ContentValues> insertData = convertArrayListHashMapToArrayListContentValues(data);

		/* ¼�����ݿ��� */
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

	public  boolean synchronizeOneTable(// ��ArrayList��װ�����ݲ��뵽����
			ArrayList<HashMap<String, Object>> data, String table) {
		/* ת��һ�����ݸ�ʽ */
		ArrayList<ContentValues> insertData = 
				convertArrayListHashMapToArrayListContentValues(data);

		/* ¼�����ݿ��� */
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
	public String getFileURL(String table, String column_fileContent,
			String column_fileName, String column_fileId, String textId,
			String namespace, String url, String methodName) {

		/* ����webserice�Ĳ������� */
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

		/* �Ӻ�̨ȡ��ĳһ�ű��xml�ַ������� */
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
	 * ִ��SQL���
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
	 * �����û������ֵ���������� and (name= '1' or name like '%��%') ����䣬sql����Ż�query list ʹ��
	 * 
	 * @param filterMap
	 *            �û����������
	 * @return ������ and (name= '1' or name like '%��%') �����
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
			data.addAll(getList(tableName, condition));// ��ѯ�����ϼ� �Ľڵ�����
		}
		recursiveQueryChild(tableName, data, conditions, coulm, key);
	}
	/**
	 * Description: �ݹ��ѯ,�������
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
			data.addAll(getOrderList(tableName, condition,order));// ��ѯ�����ϼ� �Ľڵ�����
		}
		recursiveQueryChildOrder(tableName, data, conditions, coulm, key,order);
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
	 * Description: �ݹ��ѯ��Ԫ��,����
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
	 *            ������� 
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
	 * Description: ���ϲ�ѯ ƴsql������ѯ
	 * @param sqlstr ���ϲ�ѯsql���
	 * @param conditions  ��������
	 * @param limit ��ҳ 
	 * @param order ����
	 * @return
	 * @author wangliugeng
	 * @Create at: 2013-9-5 ����5:41:14
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
	 * ��ѯ������������������
	 * @param table ����
	 * @param conditions ����
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
