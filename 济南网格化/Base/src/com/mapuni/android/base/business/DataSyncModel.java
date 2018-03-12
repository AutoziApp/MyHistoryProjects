package com.mapuni.android.base.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.os.Environment;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.WebServiceProvider;

public class DataSyncModel {
	/** 用于log输出的TAG */
	private final String TAG = "DataSyncModel";
	/** 数据同步获取服务器的时间 */
	public String UPDATETIME;

	/** 同步失败 */
	public final static int SYNC_FAIL = -1;
	/** 同步超时 */
	public final static int SYNC_TIMEOUT = -2;

	/** 同步服务器出错 */
	public final static int SYNC_SERVICE_ERR = -3;

	/** 本地插入出错 */
	public final static int SYNC_INSERT_ERR = -4;

	/** 一次请求服务器端数据最大数量 */
	public int SYNC_ONCE_NUMBER = 2000;
	/** 设置临时文件存储位置 */
	public final String DEST_DIR = Environment.getExternalStorageDirectory().getPath() + "/mapuni/DataTemp/";
	/** 同步回来的数据压缩文件的文件名称 */
	public String SERVICE_TEMP_ZIP = "";

	/** 同步表名 */
	private final String TABLE_SYNCHRONIZE_LOG = "SYNCHRONIZE_LOG";
	/** 执行同步操作所调用的方法 */
	private final String METHOD_NAME = "synchronizeOneTableLastestDataForClient";

	/**
	 * 修改后数据同步方法
	 * 
	 * @param updateOrFetchAllData
	 *            false 同步全部数据 true 同步最新数据
	 * @param table
	 *            数据表名字
	 * @return -1，同步失败 -2 同步超时 -3 同步服务器异常 可能参数不对 或者网络不通
	 * @author wangliugeng
	 */
	public int synchronizeFetchServerData(boolean updateOrFetchAllData, String table) {

		/** 分页标志 */
		int index = 1;
		/** 同步结果 */
		int result = 0;
		/** 执行同步操作先获取服务器当前时间 */
		getServerTime();
		/** 获取被更新表的最新时间 */
		String updatetime = "";

		if (updateOrFetchAllData) {
			updatetime = synchronizeFetchTableLastestTimeStamp(table);
			if(updatetime.contains(".")) {
				updatetime = updatetime.substring(0, updatetime.indexOf("."));
			}
			Log.e("hello", "updatetime-------" + table + "----------" + updatetime);
		}
		try {
			do {
				String resultResponse = "";

				/** 设置webserice中的参数 */
				ArrayList<LinkedHashMap<String, Object>> params = new ArrayList<LinkedHashMap<String, Object>>();

				/** 表名 */
				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
				param.put("tableName", table);
				params.add(param);

				/** 页数1,2,3 */
				param = new LinkedHashMap<String, Object>();
				param.put("PageIndex", index);
				params.add(param);

				/** 请求数量10000 */
				param = new LinkedHashMap<String, Object>();
				/** 危化品情况特殊，单独处理 */
				if ("T_ZSK_DangerChem".equals(table)) {
					SYNC_ONCE_NUMBER = 400;
				} else {
					SYNC_ONCE_NUMBER = 2000;
				}
				param.put("PageSize", SYNC_ONCE_NUMBER);
				params.add(param);

				/** 时间戳 */
				param = new LinkedHashMap<String, Object>();
				param.put("updateTime", updatetime);
				params.add(param);
				LogUtil.v("index", "TableName:" + table + "  index:" + index + "");
				/** 从后台取得某一张表的要更新的xml字符串数据 */
				try {
					resultResponse = String.valueOf(WebServiceProvider.callWebService(Global.NAMESPACE, METHOD_NAME, params, Global.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true));
				} catch (IOException e1) {
					e1.printStackTrace();
					return SYNC_TIMEOUT;
				}

				if (resultResponse != null && !"".equals(resultResponse) && !"null".equals(resultResponse)) {
					FileHelper fileHelper = new FileHelper();

					if (!updateOrFetchAllData && index == 1) {
						/** 删除该表的所有数据，来更新所有的数据 */
						SqliteUtil.getInstance().deleteAllTableDataByTables(table);
					}
					SERVICE_TEMP_ZIP = UUID.randomUUID() + "";
					fileHelper.convertBase64StringToLocalFile(resultResponse, DEST_DIR, SERVICE_TEMP_ZIP);

					ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR + SERVICE_TEMP_ZIP, DEST_DIR);

					for (File file : files) {
						try {
							/** 解析xml字符串数据 */
							InputStream inputStream = new FileInputStream(file);

							/** 解析xml数据的同时进行数据库数据的录入操作 */
							int temp = XmlHelper.insertTableDataWhenXmlPullParser(table, inputStream, XmlHelper.NODE_LEVEL1, updateOrFetchAllData);
							result += temp;

							/** 删除解压后的临时文件 */
							if (inputStream != null) {
								inputStream.close();
							}
							file.delete();
							
							/** 录入成功则执行同步表日志 */
							if ((result != 0 && result % SYNC_ONCE_NUMBER != 0) || (index > 1 && temp == 0)) {// 同步完成
								synchronizeLogTableUpdateState(table);
								LogUtil.v(TAG, "表 --> " + table + " " + getSyncState(1));
							}
						} catch (FileNotFoundException e) {
							Log.e(TAG, e.getMessage());
							Log.v(TAG, "表 --> " + table + " " + getSyncState(SYNC_FAIL));
							result = SYNC_FAIL;
						}
					}
				} else {
					result = SYNC_SERVICE_ERR;
				}
				index++;
			} while (result != 0 && result % SYNC_ONCE_NUMBER == 0);// 如果为SYNC_ONCE_NUMBER的倍数则继续同步
		} catch (Exception e) {
			e.printStackTrace();
			result = SYNC_SERVICE_ERR;
		} finally {
		}
		return result;
	}
	
	/**
	 * 获取服务器的时间
	 */
	public void getServerTime() {
		UPDATETIME = Global.getGlobalInstance().getDate();
		try {
			String result = (String) WebServiceProvider.callWebService(Global.NAMESPACE, "GetServerDateTime", new ArrayList<LinkedHashMap<String, Object>>(), Global.getGlobalInstance()
					.getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
			if (result != null) {
				UPDATETIME = result;
				System.out.println("获取服务器时间BaseDataSync=====" + result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: 更新完表数据后录入同步表
	 * 
	 * @param updateTable
	 *            更新被同步表在同步表中的时间 void
	 * @author 王留庚 Create at: 2012-11-30 下午03:39:10
	 */
	public void synchronizeLogTableUpdateState(String updateTable) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TABLENAME", updateTable);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList(TABLE_SYNCHRONIZE_LOG, conditions);
		ContentValues updateValues = new ContentValues();
		updateValues.put("UPDATETIME", UPDATETIME);
		if (data != null && data.size() > 0) {// 存在，执行更新操作
			String[] whereArgs = { updateTable };
			try {
				SqliteUtil.getInstance().update(TABLE_SYNCHRONIZE_LOG, updateValues, "TABLENAME=?", whereArgs);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {// 不存在，执行插入操作
			int id = SqliteUtil.getInstance().queryMaxValue("id", TABLE_SYNCHRONIZE_LOG);
			updateValues.put("ID", id + 1);
			updateValues.put("TABLENAME", updateTable);
			SqliteUtil.getInstance().insert(updateValues, TABLE_SYNCHRONIZE_LOG);
		}

	}

	/**
	 * Description: 获取表的最新时间戳 如果时间戳返回为“”，则同步该表的全部数据；反之，则根据时间戳来同步要同步的最新数据
	 * 
	 * @param table
	 *            被同步的表名称
	 * @return default : ""，返回被同步表的时间戳 String
	 * @author 王留庚 Create at: 2012-11-30 下午03:37:30
	 */
	public String synchronizeFetchTableLastestTimeStamp(String table) {
//		String timestamp = "";
//		try {
//			HashMap<String, Object> data = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
//					"SELECT strftime('%Y-%m-%d %H:%M:%f',UPDATETIME,'localtime') as UpdateTime from SYNCHRONIZE_LOG a where a.[TABLENAME] = '" + table + "';");
//			timestamp = data.get("updatetime").toString();
//		} catch (Exception e) {
//			OtherTools.showExceptionLog("获取更新时间异常");
//		}
		String timestamp = "";
		try {
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("TABLENAME", table);
			ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("UPDATETIME", conditions, TABLE_SYNCHRONIZE_LOG);
			timestamp = data.get(0).get("updatetime").toString();
		} catch (Exception e) {
			LogUtil.e(TAG,"//"+ e.getMessage());
		}
		return timestamp;
	}

	/**
	 * Description: 获取同步状态
	 * 
	 * @param state
	 *            状态值
	 * @return 返回同步结果 String
	 * @author 王留庚 Create at: 2012-11-30 下午03:12:51
	 */
	public String getSyncState(int state) {
		switch (state) {
		case SYNC_FAIL:
			return "失败";
		case SYNC_SERVICE_ERR:
			return "服务端异常";
		case SYNC_TIMEOUT:
			return "连接服务器超时";
		default:
			return "同步成功";
		}

	}

	public void syncServiceData(ArrayList<String> tables, Boolean updateOrFetchAllData) {
		for (String table : tables) {
			synchronizeFetchServerData(updateOrFetchAllData, table);
		}

	}

}
