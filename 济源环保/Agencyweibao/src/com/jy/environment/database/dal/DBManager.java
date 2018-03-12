package com.jy.environment.database.dal;

import com.jy.environment.util.MyLog;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private static DBManager dbManager;
	private static SQLiteDatabase db;

	// 使用单例模式管理数据库，防止操作数据库异常
	public static DBManager getInstances(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	// 插入数据库
	public boolean insertSQLite(Context context, String tableName,
			String nullColumnHack, ContentValues values) {
		SQLiteDatabase database = DbHelper.getInstance(context)
				.getWritableDatabase();
		long raw = database.insert(tableName, null, values);
		// database.close();
		if (raw > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 插入和替换数据库
	public boolean replace(Context context, String tableName,
			String nullColumnHack, ContentValues values) {
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		long raw = db.replace(tableName, null, values);
		// db.close();
		if (raw > 0) {
			return true;
		} else {
			return false;
		}
	}

	// public Cursor query(Context context, boolean distinct, String table,
	// String[] columns, String selection, String[] selectionArgs,
	// String groupBy, String having, String orderBy, String limit){
	// SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
	// Cursor c = db.query(distinct, table, columns, selection, selectionArgs,
	// groupBy, having, orderBy, limit);
	// db.close();
	// return c;
	// }

	// public boolean rawQuery(Context context, String sql, String[]
	// selectionArgs) {
	// boolean flag = false;
	// SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
	// Cursor c = db.rawQuery(sql, selectionArgs);
	// if (c.getCount() > 0) {
	// flag = true;
	// }
	// db.close();
	// return flag;
	// }
	//
	// 查看最新信息
	public Cursor queryUIC(Context context, String userId, String publicId) {

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
		String where = "userID = ? and account_id_num = ? and isread = ? ";
		String[] whereValue = { userId, publicId, "0" };
		Cursor c = db.query("uic", null, where, whereValue, null, null,
				"_id desc");
		// db.close();
		return c;
		// String sql =
		// "select top 2 * from uic where userID = "+userId+" and account_id_num = "+publicId+" order by _id desc"
		// ;
		// Cursor c=db.rawQuery(sql, null);
	}

	// 查询uic表中历史消息
	public Cursor queryUICHistory(Context context, String userId,
			String publicId, String shumu, int page) {

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
		String sql = "";
		int m = 0;

		m = page * 5;
		if (userId.equals("0")) {
			sql = "SELECT  *   FROM nouic WHERE userID = " + userId
					+ " and account_id_num = " + publicId
					+ " ORDER BY _id DESC LIMIT  5   OFFSET " + m;
		} else {
			sql = "SELECT *   FROM uic WHERE userID = " + userId
					+ " and account_id_num = " + publicId
					+ " ORDER BY _id DESC LIMIT  5   OFFSET " + m;
		}

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return c;
	}

	// 查询用户关注的公众号
	public Cursor query(Context context, String table, String userId) {
		Cursor cursor = null;
		SQLiteDatabase database = DbHelper.getInstance(context)
				.getWritableDatabase();
		database.beginTransaction();
		String sql;
		if (table.equals(DBInfo.TABLE_JINGPIN)) {
			sql = "select * from " + table;
		} else {
			sql = "select * from " + table + " where userID = " + userId;
		}
		MyLog.i(">>>>>>>>sql" + sql);
		cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		// database.close();
		database.endTransaction();
		return cursor;

	}

	// 更新信息表的读状态
	public int update(Context context, String table, String userId,
			String publicId, ContentValues values) {
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String where = "userID = ? and account_id_num = ?";
		String[] whereValue = { userId, publicId };
		int m = db.update(table, values, where, whereValue);
		// db.close();
		return m;

	}

	public int selectUic(Context context, String table, String userId,
			String publicId) {
		Cursor c = null;
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String sql = "select xiaoxi_id from " + table + " where userID= "
				+ userId + " and account_id_num= " + publicId
				+ " and isread = 0";
		MyLog.i(">>>>>>jisighg"+sql);
		c = db.rawQuery(sql, null);
		// db.close();
		if (c != null) {
			return c.getCount();
		} else {
			return -1;
		}
	}

	// 查询所有公众号的消息条目数
	public int selectUicAll(Context context, String table, String userId) {
		Cursor c = null;
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String sql = "select xiaoxi_id from " + table + " where userID= "
				+ userId + " and isread = 0";
		MyLog.i(">>>>>>>>>sql" + sql);
		c = db.rawQuery(sql, null);
		// db.close();
		if (c != null) {
			return c.getCount();
		} else {
			return -1;
		}
	}

	public Cursor selectMes(Context context, String table, String userId,
			String publicId) {
		Cursor c = null;
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String xiaoxi_id = userId + "*" + publicId;
		String sql = "select xiaoxi_id from " + table + " where userID= "
				+ userId + " and account_id_num= " + publicId
				+ " and isread = 0 " + " and isfirst = 0";
		c = db.rawQuery(sql, null);
		// db.close();
		return c;
	}

	// public Map<String, String> rawQuerysMap(Context context, String sql,
	// String[] selectionArgs) {
	// Map<String, String> map = new HashMap<String, String>();
	// SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
	// Cursor c = db.rawQuery(sql, selectionArgs);
	// if (c.getCount() > 0) {
	// c.moveToFirst();
	// for (int i = 0; i < c.getColumnCount(); i++) {
	// map.put(c.getColumnName(i), c.getString(i));
	// }
	// }
	// db.close();
	// return map;
	// }
	//
	// public List<Map<String, String>> rawQuerysList(Context context, String
	// sql,
	// String[] selectionArgs) {
	// List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	// SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
	// Cursor c = db.rawQuery(sql, selectionArgs);
	// if (c.getCount() > 0) {
	// c.moveToFirst();
	// while (true) {
	// Map<String, String> map = new HashMap<String, String>();
	// for (int i = 0; i < c.getColumnCount(); i++) {
	// map.put(c.getColumnName(i), c.getString(i));
	// }
	// list.add(map);
	// if (!c.moveToNext()) {
	// break;
	// }
	// }
	// }
	// db.close();
	// return list;
	// }

	public void deleteSQLiteTable(Context context, String table) {
		try {
			SQLiteDatabase database = DbHelper.getInstance(context)
					.getWritableDatabase();
			String sql = "delete from " + table;
			database.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
		}// 确保只有最新的精品推荐内容
			// database.close();
	}

	public boolean deleteSQLiteQuanZhu(Context context, String table,
			String userID, String publicID) {
		boolean flag = false;
		SQLiteDatabase database = DbHelper.getInstance(context)
				.getWritableDatabase();
		String sql;
		if (table.equals(DBInfo.TABLE_NAME_MAINDATA)) {
			sql = "delete from " + table + " where userID= " + userID
					+ " and account_id_num = " + publicID;
		} else {
			sql = "delete from " + table + " where userID= " + userID
					+ " and publicID = " + publicID;
		}
		MyLog.i(">>>>>>>>>sgagg" + sql);
		// database.beginTransaction();
		database.execSQL(sql);// 确保关注信息从表里面取消
		flag = true;
		// database.endTransaction();
		return flag;
	}

	// 根据消息id 查询数据
	public Cursor selectUicXiaoXI(Context context, String table,
			String xiaoxi_id) {
		Cursor c = null;
		SQLiteDatabase db = DbHelper.getInstance(context).getWritableDatabase();
		String sql = "select * from " + table + " where xiaoxi_id= "
				+ xiaoxi_id;
		c = db.rawQuery(sql, null);
		// db.close();
		c.moveToFirst();
		return c;
	}

}
