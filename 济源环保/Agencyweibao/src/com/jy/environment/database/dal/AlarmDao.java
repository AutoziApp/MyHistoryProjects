package com.jy.environment.database.dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jy.environment.database.model.ModelAlarmHistory;
import com.jy.environment.util.MyLog;

public class AlarmDao {
	private DbHelper dbHelper;

	public AlarmDao(Context context) {
		dbHelper = DbHelper.getInstance(context);
	}

	/**
	 * 保存预警信息list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<ModelAlarmHistory> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			for (ModelAlarmHistory alarm : contactList) {
				ContentValues values = new ContentValues();
				values.put("province", alarm.getProvince());
				values.put("town", alarm.getTown());
				values.put("title", alarm.getTitle());
				values.put("message", alarm.getMessage());
				values.put("time", alarm.getTime());
				values.put("url", alarm.getUrl());
				values.put("alarm", alarm.getAlarm());
				values.put("isread", "0");
				db.insert(DBInfo.TABLE_ALARM, null, values);
			}
		}
	}

	/**
	 * 获取预警信息list
	 * 
	 * @return
	 */
	public List<ModelAlarmHistory> getContactList(String city, String time) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ModelAlarmHistory> modeList = new ArrayList<ModelAlarmHistory>();
		if (db.isOpen()) {
			String sql = "select * from " + DBInfo.TABLE_ALARM
					+ " where isread = 0 and town =  " + "\'" + city + "\'"
					+ " and time like " + "\'" + time.substring(0, 10) + "%"
					+ "\'";
			String sqldelete = "delete from " + DBInfo.TABLE_ALARM
					+ " where time not like " + "\'" + time.substring(0, 10)
					+ "%" + "\'";
			Cursor cursor = db.rawQuery(sql
			/* + " desc" */, null);
			db.rawQuery(sqldelete, null);
			while (cursor.moveToNext()) {
				ModelAlarmHistory model = new ModelAlarmHistory();
				model.setProvince(cursor.getString(cursor
						.getColumnIndex("province")));
				model.setTown(cursor.getString(cursor.getColumnIndex("town")));
				model.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				model.setMessage(cursor.getString(cursor
						.getColumnIndex("message")));
				model.setTime(cursor.getString(cursor.getColumnIndex("time")));
				model.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				model.setAlarm(cursor.getString(cursor.getColumnIndex("alarm")));
				modeList.add(model);
			}
			return modeList;
		}
		return modeList;
	}

	// 读过就把状态置为1
	public void update(String city) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("isread", "1");
			db.update(DBInfo.TABLE_ALARM, values, "town =?",
					new String[] { city });
		}
	}
}
