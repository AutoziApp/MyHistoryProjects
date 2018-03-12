package com.jy.environment.database.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static DbHelper dbHelper;// 一个helper
	// //创建存储头条,百科，资讯，经营，数据的数据库表
	// private static final String sql_maindata="CREATE TABLE IF NOT EXISTS "
	// +DBInfo.TABLE_NAME_MAINDATA+ " ("
	// +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
	// +"id INTEGER UNIQUE NOT NULL,"//UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
	// +"title VARCHAR(50),"
	// +"source VARCHAR(50),"
	// +"description VARCHAR(50),"
	// +"wap_thumb VARCHAR(50),"
	// +"create_time VARCHAR(50),"
	// +"nickname VARCHAR(50),"
	// +"category VARCHAR(50)"+");";//category种类，数据为头条,百科，资讯，经营，数据，用于区分是哪一种数据
	// //headline头条,baike百科,information资讯,manage经营,data数据

	private static final String sql_viewpagerinfo = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_NAME_MAINDATA
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "userID text,"// UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
			// 公众号id
			+ "account_id_num text,account_id text,xiaoxi_id text ,"
			+ "title text,author text,"
			+ "face_pic_url text,summary text,content text,"
			+ "isread text,isfirst text,message text,publish_time text ,unique(userID,xiaoxi_id));";
	private static final String sql_logininfo = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_NAME_NOUIC
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "userID text,"// UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
			// 公众号id
			+ "account_id_num text,account_id text,"
			+ "xiaoxi_id text ,title text,"
			+ "author text,face_pic_url text,summary text,content text,"
			+ "isread text,isfirst text,message text,publish_time text,unique(userID,xiaoxi_id));";
	// "id": "1",
	// "user_id": "123",
	// "name": "csgzh",
	// "fuction": "一般",
	// "password": "123",
	// "user_type": "普通",
	// "public_photo":
	// "http://59.108.37.71:8080/epservice/image/img/publicaccountinfo/20140508102918327.jpg"
	private static final String sql_jingpin = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_JINGPIN
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "publicID text UNIQUE NOT NULL,"// UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
			// 公众号id
			+ "userID text,name text,fuction text,"
			+ "password text ,user_idd text,user_type text,"
			+ "public_photo text);";
	// 用户关注的公众号表
	private static final String sql_userpublic = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_USERPUBLIC
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,userID text,"
			+ "publicID text UNIQUE NOT NULL,"// UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
			// 公众号id
			+ "name text,fuction text,password text,"
			+ "user_idd text,user_type text,public_photo text);";
	// 用户关注的公众号表
	private static final String sql_nouserpublic = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_NOUSERPUBLIC
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,userID text,"
			+ "publicID text UNIQUE NOT NULL,"// UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
			// 公众号id
			+ "name text,fuction text,password text,"
			+ "user_idd text,user_type text,public_photo text);";
	// 气象预警表
	private static final String sql_alarm = "CREATE TABLE IF NOT EXISTS "
			+ DBInfo.TABLE_ALARM
			+ " (province text,town text,"
			+ "title text,message text,isread text,time text,url text,alarm text,primary key (title,time));";

	// //存储收藏的表 publish_time
	// private static final String sql_collect="CREATE TABLE IF NOT EXISTS "
	// +DBInfo.TABLE_NAME_collect+ " ("
	// +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
	// +"id INTEGER UNIQUE NOT NULL,"//UNIQUE关键字，使用replace语句会进行判断，有就update，没有就插入
	// +"title VARCHAR(50),"
	// +"source VARCHAR(50),"
	// +"description VARCHAR(50),"
	// +"wap_thumb VARCHAR(50),"
	// +"create_time VARCHAR(50),"
	// +"nickname VARCHAR(50)"+");";
	//
	public DbHelper(Context context) {
		super(context, DBInfo.DBNAME, null, DBInfo.DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public static DbHelper getInstance(Context context) {// 单例模式
		if (dbHelper == null) {
			dbHelper = new DbHelper(context);
		}
		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql_viewpagerinfo);
		db.execSQL(sql_jingpin);
		db.execSQL(sql_userpublic);
		db.execSQL(sql_logininfo);
		db.execSQL(sql_nouserpublic);
		db.execSQL(sql_alarm);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (newVersion) {
		case 5:
			db.execSQL(sql_viewpagerinfo);
			db.execSQL(sql_jingpin);
			db.execSQL(sql_userpublic);
			db.execSQL(sql_logininfo);
			db.execSQL(sql_logininfo);
			db.execSQL(sql_nouserpublic);
			db.execSQL(sql_alarm);
			break;

		default:
			break;
		}

	}

	// private final static String DATABASE_NAME="chat_db";
	// private final static int DATABASE_VERSION=1;
	// private final static String TABLE_NAME="sec_pwd";
	// //用户公众号关系表
	// private final static String USERPUBLIC="upc";
	// public final static String FIELD_ID="_id";
	// public final static String FIELD_TITLE="sec_Title";
	//
	//
	// public dbHelper(Context context)
	// {
	// super(context, DATABASE_NAME,null, DATABASE_VERSION);
	// }
	// // "account_id_num": "1",
	// // "account_id": "csgzh",
	// // "title": "ceshilujing",
	// // "author": "q",
	// // "face_pic_url":
	// "http://59.108.37.71:8080/epservice/image/facepic/20140508101546Tulips.jpg",
	// // "summary": "ssss",
	// // "content": "<p>kankancheshi jieguo</p>
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// // TODO Auto-generated method stub
	// String
	// sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement,name text,date text,text text,isComMeg bool)";
	// //用户与公众号消息表
	// String sql1 =
	// "create table upc (_id integer autoincrement,userId text,publicId text,account_id_num text, account_id text,xiaoxi_id text not null unique,title text,author text,face_pic_url text,summary text,content text,isread bool,publish_time text)";
	//
	// db.execSQL(sql);
	// db.execSQL(sql1);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// // TODO Auto-generated method stub
	// String sql=" DROP TABLE IF EXISTS "+TABLE_NAME;
	// db.execSQL(sql);
	// onCreate(db);
	// }
	//
	// public Cursor select(String userId,String account_id_num,String table)
	// {
	// SQLiteDatabase db=this.getReadableDatabase();
	// String where = "userId = ? and account_id_num = ?";
	// String[] whereValue={userId,account_id_num};
	// Cursor cursor=db.query(table, null, where, whereValue, null, null,
	// " _id asc");
	// return cursor;
	// }
	// public Cursor selectXiao(String userId,String publicId,String table)
	// {
	// SQLiteDatabase db=this.getReadableDatabase();
	// String where = "userId = ? and account_id_num = ?";
	// String[] whereValue={userId,publicId};
	// Cursor cursor=db.query(table, null, where, whereValue, null, null,
	// " _id desc");
	// return cursor;
	// }
	//
	// public long insert(ContentValues cv,String table)
	// {
	// SQLiteDatabase db=this.getWritableDatabase();
	// long row=db.insert(table, null, cv);
	// return row;
	// }
	//
	// public void delete(String table,String userId,String account_id_num )
	// {
	// SQLiteDatabase db=this.getWritableDatabase();
	// String where = "userId = ? and account_id_num = ?";
	// String[] whereValue={userId,account_id_num};
	// db.delete(table, where, whereValue);
	// }
	//
	// public void update(String userId,String account_id_num,String
	// table,ContentValues cv)
	// {
	// SQLiteDatabase db=this.getWritableDatabase();
	// String where = "userId = ? and account_id_num = ?";
	// String[] whereValue={userId,account_id_num};
	// // ContentValues cv=new ContentValues();
	// // cv.put(FIELD_TITLE, Title);
	// db.update(table, cv, where, whereValue);
	// }
	//
	//
}