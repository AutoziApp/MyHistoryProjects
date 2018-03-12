package com.mapuni.android.base.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.DocumentException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.BaseAutoUpdate;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.ConfigManager;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * FileName: DataSync.java Description: 数据同步业务类
 * 
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-11-30 下午02:47:28
 */
@SuppressWarnings("serial")
public class BaseDataSync extends BaseClass implements Serializable, IList {
	/** 用于log输出的TAG */
	private  final String TAG = "DataSync";
	/** 业务类的名字 */
	public static final String BusinessClassName = "BaseDataSync";
	/** 执行同步操作所调用的方法 */
	private  final String METHOD_NAME = "synchronizeOneTableLastestDataForClient";
	/** 同步xml里边的id */
	private  final String PRIMARY_KEY = "id";
	/** 数据同步列表样式在xml里边的标题名字 */
	private  final String LIST_STYLE_NAME = "SYNCHRONIZE_LOG";
	/** 数据同步所读取的xml文件名称 */
	private  final String SYNC_NAME_REFLECT = "datasync_config.xml";
	/** 数据同步xml文件的table标签名字 */
	public  final static String SYNC_TABLE_REFLECT = "table";
	/** 数据同步获取服务器的时间 */
	public String UPDATETIME;
	/** 数据同步显示的列表的名字 */
	private final String list_title_text = "数据同步";
	/** 当前所同步的表的在SYNCHRONIZE_LOG表中的id */
	@SuppressWarnings("unused")
	private String current_id = "";
	/** 同步xml文件里的tablename标签 */
	private final String table_name = COLUMN_TABLENAME;
	/** 同步业务类列表滚动的次数 */
	public int ListScrollTimes;
	/** sqlite常用操作的工具类 */
	
	
	
	/** 同步表名 */
	public  final String TABLE_SYNCHRONIZE_LOG = "SYNCHRONIZE_LOG";
	/** 被同步的表名 */
	public static final String COLUMN_TABLENAME = "TABLENAME";
	/** 被同步的表上次更新的时间 */
	public static final String COLUMN_UPDATETIME = "UPDATETIME";
	/** 主键 */
	public  final String COLUMN_ID = "ID";
	/** 设置临时文件存储位置 */
	public  final String DEST_DIR = "/sdcard/mapuni/DataTemp/";
	/** 同步回来的数据压缩文件的文件名称 */
	public  final String SERVICE_TEMP_ZIP = "service.zip";
	/***/
	public  Boolean IsDataSync = false;
	/** 同步失败 */
	public  final static int SYNC_FAIL = 0;
	/** 同步成功 */
	public  final static int SYNC_SUCCESSFUL = 1;
	/** 没有同步数据 */
	public  final static int SYNC_NO_DATA = 2;
	/** 服务端异常 */
	public  final static int SYNC_SERVER_FAILL = 3;
	/** 一次请求服务器端数据最大数量 */
	public int SYNC_ONCE_NUMBER = 2000;

	/**
	 * Description: 获取同步状态
	 * 
	 * @param state
	 *            状态值
	 * @return 返回同步结果 String
	 * @author 王留庚 Create at: 2012-11-30 下午03:12:51
	 */
	public static String getSyncState(int state) {
		switch (state) {
		case SYNC_SUCCESSFUL:
			return "同步成功";
		case SYNC_NO_DATA:
			return "没有同步数据";
		case SYNC_SERVER_FAILL:
			return "服务端异常";
		default:
			return "同步失败";
		}
		
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
		return PRIMARY_KEY;
	}

	@Override
	public String GetTableName() {
		return table_name;
	}

	@Override
	public String getListTitleText() {
		return list_title_text;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		current_id = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		// ArrayList<HashMap<String, Object>> data =
		// BaseClass.DBHelper.getList(table_name);
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		return data;
	}

	/**
	 * Description: 获取同步表的所有数据
	 * 
	 * @return 返回读取数据库同步表所有的数据集合 ArrayList<HashMap<String,Object>>
	 * @author 王留庚 Create at: 2012-11-30 下午03:13:47
	 */
	public ArrayList<HashMap<String, Object>> getDataSyncList() {
		ArrayList<HashMap<String, Object>> data = BaseClass.DBHelper
				.getList(table_name);
		return data;
	}

	/**
	 * Description: 对表名进行了汉化
	 * 
	 * @param context
	 *            上下文
	 * @return 返回得到的列表集合 ArrayList<HashMap<String,Object>>
	 * @author 王留庚 Create at: 2012-11-30 下午03:14:23
	 */
	@SuppressWarnings("static-access")
	public ArrayList<HashMap<String, Object>> getDataList(Context context) {

		/*
		 * if (!DBHelper.checkTableExists(table_name))
		 * {//该表若不存在则初始化一个同步表，同步表内容直接由SYNCHRONIZE_LOG_TABLE.xml读出
		 * DBHelper.synchronizeLogTableInit(context); }
		 */

		SYNC_ONCE_NUMBER = (Integer) DisplayUitl.getSettingValue(context,
				DisplayUitl.SYNCDATANUM, 2000);
		Log.i(TAG, "Change Number to " + SYNC_ONCE_NUMBER);

		ArrayList<HashMap<String, Object>> dataOrig = getDataSyncList();
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		ArrayList<HashMap<String, Object>> dataStyle = getTablenameCN(context);
		for (HashMap<String, Object> dataRow : dataOrig) {
			String column_tablename = table_name.toLowerCase();
			for (HashMap<String, Object> styleRow : dataStyle) {
				if (styleRow.get(SYNC_TABLE_REFLECT).equals(
						dataRow.get(column_tablename))) {
					/* 覆盖英文表名 */
					dataRow.put(column_tablename,
							styleRow.get(column_tablename));
				}
			}
			data.add(dataRow);
		}

		return data;
	}

	/**
	 * Description: 获取同步表的中文表名
	 * 
	 * @param context
	 *            上下文
	 * @return 返回同步xml的中文表名 ArrayList<HashMap<String,Object>>
	 * @author Administrator Create at: 2012-11-30 下午03:19:45
	 */
	@SuppressWarnings("static-access")
	public ArrayList<HashMap<String, Object>> getTablenameCN(Context context) {
		try {
			ArrayList<HashMap<String, Object>> tablename = BaseClass.xmlHelper
					.getDataFromXmlStream(context.getResources().getAssets()
							.open(SYNC_NAME_REFLECT), XmlHelper.NODE_LEVEL1);
			ArrayList<HashMap<String, Object>> tablenameCN = new ArrayList<HashMap<String, Object>>();
			for (HashMap<String, Object> tableMap : tablename) {
				if (DisplayUitl.getAuthority(tableMap.get("qxid").toString())) {// 判断当前用户是否有权限
					tablenameCN.add(tableMap);
				}
			}
			return tablenameCN;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		return null;
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(LIST_STYLE_NAME,
					getStyleListInputStream(context));
		} catch (Exception e) {
			Log.e(TAG, "getStyleList IOException : \n" + e.getMessage());
		}
		return styleList;
	}

	/**
	 * Description: 用来得到我所勾选的要同步的所有表的名字
	 * 
	 * @param updateOrFetchAllData
	 *            同步最新还是同步全部数据
	 * @param context
	 *            上下文
	 * @param NeedSystableNum
	 *            要同步的表在同步配置文件中的id号
	 * @return String[] 返回被同步表的名字
	 * @author 王留庚 Create at: 2012-11-30 下午03:22:28
	 */
	public String[] getNeedSysTableName(boolean updateOrFetchAllData,
			Context context, String[] NeedSystableNum) {
		BaseDataSync dataSync = new BaseDataSync();
		ArrayList<HashMap<String, Object>> data = dataSync
				.getTablenameCN(context);
		StringBuilder tablesSB = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Object> dataRow = data.get(i);
			for (int j = 0; j < NeedSystableNum.length; j++) {
				if (dataRow.get("id").toString().equals(NeedSystableNum[j])) {
					Object objTable = dataRow
							.get(BaseDataSync.SYNC_TABLE_REFLECT);
					tablesSB.append(objTable.toString() + ",");
				}
			}

		}

		if (tablesSB.indexOf(",") != -1) {
			tablesSB = new StringBuilder(tablesSB.substring(0,
					tablesSB.length() - 1));
		}

		Log.v(TAG, (updateOrFetchAllData ? "更新数据" : "全部数据") + "同步表： --> "
				+ tablesSB.toString());
		String[] tables = tablesSB.toString().split(",");
		return tables;

	}

	@Override
	public HashMap<String, Object> getFilter() {
		return null;
	}

	/**
	 * Description: 获取表的更新时间组成的json
	 * 
	 * @param tables
	 *            所需要的表的数组
	 * @return 返回时间戳的json String
	 * @author 王留庚 Create at: 2012-11-30 下午03:21:03
	 */
	public String getUpdateJson(String[] tables) {
		if (tables == null || tables.length == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		int len = tables.length;
		for (int i = 0; i < len; i++) {
			String updatetime = synchronizeFetchTableLastestTimeStamp(tables[i]);
			if (i == 0) {
				sb.append("{");
			}
			sb.append("\"");
			sb.append(tables[i]);
			sb.append("\":\"");
			sb.append(updatetime);
			sb.append("\",");
			if (i == len - 1) {
				sb.deleteCharAt(sb.lastIndexOf(","));
				sb.append("}");
			}
		}

		return sb.toString();
	}



	/**
	 * Description: 同步成功后下载附件
	 * 
	 * @param table
	 *            同步的表名
	 * @param hturl
	 *            同步的地址 void
	 * @author Administrator Create at: 2012-12-3 上午09:59:13
	 */
	public void downfj(String table, String hturl,String fileattachmentpath,String oldtime) {
		ArrayList<HashMap<String, Object>> wjlist = new ArrayList<HashMap<String,Object>>();
    File f = null ;
	if (!oldtime.equals("")) {/*
		ArrayList<HashMap<String, Object>> conditions = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> cond = new HashMap<String, Object>();
		cond.put("whereName", "updatetime");
		cond.put("whereOperate", ">");
		cond.put("whereValue", oldtime);
		conditions.add(cond);
		wjlist = BaseClass.DBHelper.getList(table, conditions);

	*/
		if(table.equals("T_YDZF_RWXX_FileAttachment")){
			ArrayList<HashMap<String,Object>> params=new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> param=new HashMap<String, Object>();
			
			param.put("updateTime", oldtime);
			param.put("PageIndex", 1);
			param.put("PageSize", SYNC_ONCE_NUMBER);
			param.put("tableName", table);
			params.add(param);
			String result="";
			Log.i(TAG, "updateTime-->"+oldtime+"");
			try {
				result=(String) WebServiceProvider.callWebService(Global.NAMESPACE, "synchronizeOneTableLastestDataForClientToJson", 
						params, Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
				String [] node={"GUID","RWBH","FileAttachmentPath","FileAttachmentName","UpdateTime",
		        "SyncDataRegionCode"};
				if(result==null || result.equals("[]")||result.equals("")){
					return;
				}
				wjlist=JsonHelper.paseJSON(result, node);
				if(wjlist==null ||wjlist.size()==0){
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	} else {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		wjlist = BaseClass.DBHelper.getList(fileattachmentpath, conditions, table);

	}

	String name = null;

		
		for (HashMap<String, Object> map : wjlist) {
			if (map.get(fileattachmentpath).toString().length() < 1)
				continue;
			name = (String) map.get(fileattachmentpath);

			String serverURL = Global
					.getGlobalInstance().getSystemurl();
			String uriStr = "";
			try {
			uriStr = serverURL + "/" + hturl
						+ java.net.URLEncoder.encode(name, "UTF-8");
			URL url = new URL(uriStr);

			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			InputStream in = con.getInputStream();
			FileOutputStream fos = null;
			if (in != null) {

				String[] tab = table.split("_");
				f = new File(PathManager.SDCARD_FJ_LOCAL_PATH
						+ tab[tab.length - 1]);// 如果没有目录先建立目录
				if (!f.exists())
					f.mkdirs();
				File fil = new File(PathManager.SDCARD_FJ_LOCAL_PATH
						+ tab[tab.length - 1] + "/" + name);// 有目录之后建文件
				fos = new FileOutputStream(fil);

				byte[] bytes = new byte[1024];
				int flag = -1;
				int count = 0;// 文件总字节长度
				while ((flag = in.read(bytes)) != -1) {// 若未读到文件末尾则一直读取
					fos.write(bytes, 0, flag);
					count += flag;

				}
				fos.flush();
				fos.close();
				Log.v(TAG, "同步附件   " + name + " 成功");
			}
			} catch (ClientProtocolException e) {
				ExceptionManager.WriteCaughtEXP(e, "BaseDataSync");
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
				Log.e(TAG, name + "数据同步失败");

			} catch (IOException e) {
				ExceptionManager.WriteCaughtEXP(e, "BaseDataSync");
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
				Log.e(TAG, name + "数据同步失败");
				continue;

			}
		}
/*		if(f!=null){
			File[] fs = f.listFiles();
			SqliteUtil sqlite = SqliteUtil.getInstance();
			Cursor cusor;
			for (int i = 0; i < fs.length; i++) {
				cusor = sqlite.queryBySql("select " + fileattachmentpath + " from "
						+ table);
				String[] str = sqlite.convertCursorToStringArray(cusor);
				boolean bool = true;
				for (int j = 0; j < str.length; j++) {
					if (str[j].equals(fs[i].getName())) {
						bool = false;
						break;
					}
				}
				if (bool)
					fs[i].delete();
			}
		}*/
		}
	/**
	 * 删除某个目录下包含的文件
	 * 
	 * @param url
	 *            目录
	 * @param filename
	 *            要删除的文件名称数组
	 */
	public void DeleteFile(String url, String[] filename) {
		File file = new File(url);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (filename.toString().contains(files[i].getName())) {
				files[i].delete();
			}
		}
	}

	
	/**
	 * Description: 本地数据同步后台数据
	 * 
	 * @param updateOrFetchAllData
	 *            true代表同步最新 false 代表同步全部
	 * @param table
	 *            同步的表名
	 * @return int类型 int
	 * @throws IOException
	 * @author 王留庚 Create at: 2012-11-30 下午03:29:00
	 */
	public int synchronizeFetchServerData(boolean updateOrFetchAllData,
			String table) throws IOException {
		/*
		 * return
		 * BaseClass.DBHelper.synchronizeFetchServerData(updateOrFetchAllData,
		 * table, Global.NAMESPACE, global.getUrl()+Global.WEBSERVICE_URL,
		 * METHOD_NAME, Global.getGlobalInstance().getSname());
		 */

		// 判断是否为权限表
		/*
		 * if (table.equalsIgnoreCase("PC_UserModelPession")) {
		 * updateOrFetchAllData = false; }
		 */
		BaseAutoUpdate upadte = new BaseAutoUpdate();
		if (table.equals("config.xml") || table.equals("gis_config.xml")
				|| table.equals("video_config.xml")) {
			String num = null;

			String gis_configWebUrl = Global.getGlobalInstance().getSystemurl()
					+ "/AutoUpdate/app/verson.xml";
			String Configurl = "/sdcard/mapuni/MobileEnforcement/data/config.xml";
			String configWebUrl = Global.getGlobalInstance().getSystemurl()
					+ "/AutoUpdate/app/verson.xml";

			ArrayList<HashMap<String, Object>> hash = new ArrayList<HashMap<String, Object>>();
			try {
				if (table.equals("gis_config.xml")) {
					String GisConfigurl = "/sdcard/mapuni/MobileEnforcement/data/gis_config.xml";
					File file = new File(GisConfigurl);
					if (!file.exists()) {
						FileHelper.downLoadFile(Global.getGlobalInstance()
								.getSystemurl()
								+ "/AutoUpdate/gis_config/gis_config.xml",
								GisConfigurl);
						return SYNC_SUCCESSFUL;
					}
					FileInputStream gis_configfis = new FileInputStream(
							GisConfigurl);
					hash = XmlHelper.getList(gis_configfis, "verson");
					if (hash.size() != 0) {
						/** 不为空，遍历下载 */
						for (HashMap<String, Object> hashMap : hash) {
							num = hashMap.get("num").toString();
						}
						HashMap<String, String> ReadVerson = new HashMap<String, String>();
						ReadVerson = upadte.readVerson(gis_configWebUrl);
						if (ReadVerson != null) {
							String verson = ReadVerson.get("verson").toString();
							if ((verson.compareTo(num)) > 0) {
								/** 下载 */
								FileHelper
										.downLoadFile(
												Global.getGlobalInstance()
														.getSystemurl()
														+ "/AutoUpdate/gis_config/gis_config.xml",
												GisConfigurl);
							} else {
								return SYNC_SUCCESSFUL;
							}
						} else {
							return SYNC_SERVER_FAILL;
						}
					} else {
						/** 为空，直接下载 */
						FileHelper.downLoadFile(Global.getGlobalInstance()
								.getSystemurl()
								+ "/AutoUpdate/gis_config/gis_config.xml",
								GisConfigurl);
					}
				} else if (table.equals("config.xml")) {
					/** 下载config.xml,num是config版本号 */
					String confignum = ConfigManager.getValue("config",
							"versonnum");
					HashMap<String, String> configReadVerson = new HashMap<String, String>();
					configReadVerson = upadte.readVerson(configWebUrl);
					if (configReadVerson != null
							&& confignum != null
							&& configReadVerson.get("verson") != null
							&& (String.valueOf(configReadVerson.get("verson"))
									.compareTo(confignum)) > 0) {
						/** 下载 */
						FileHelper.downLoadFile(Global.getGlobalInstance()
								.getSystemurl() + PathManager.CONFIG_CODE_URL,
								
								Configurl);

						return SYNC_SUCCESSFUL;
					} else if (configReadVerson == null) {
						return SYNC_SERVER_FAILL;
					} else {
						return SYNC_SUCCESSFUL;
					}
				} else if (table.equals("video_config.xml")) {
					FileHelper.downLoadFile(Global.getGlobalInstance()
							.getSystemurl() + PathManager.VIDEOCOFIG_CODE_URL,
							PathManager.SDCARD_VIDEODATA_CONFIG);
					return SYNC_SUCCESSFUL;
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} else {
			/** 分页标志 */
			int index = 1;
			int result = 0;
			/** 执行同步操作先获取服务器当前时间 */
			getServerTime();
			do {
				/** 获取被更新表的最新时间 */
				String updatetime = "";
				try {
					if (updateOrFetchAllData) {
						updatetime = synchronizeFetchTableLastestTimeStamp(table);
					}
				} catch (Exception e) {
					Log.v(TAG, e.getMessage());
					updatetime = "";
					Log.v(TAG, "表 --> " + table + " " + getSyncState(SYNC_FAIL));
					return SYNC_FAIL;
				}
				String resultResponse = "";

				/** 设置webserice中的参数 */
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

				/** 表名 */
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("tableName", table);
				params.add(param);

				/** 页数1,2,3 */
				param = new HashMap<String, Object>();
				param.put("PageIndex", index);
				params.add(param);

				/** 请求数量10000 */
				param = new HashMap<String, Object>();
				/** 危化品情况特殊，单独处理 */
				if ("T_ZSK_DangerChem".equals(table)) {
					param.put("PageSize", 400);
				} else {
					param.put("PageSize", SYNC_ONCE_NUMBER);
				}
				params.add(param);

				/** 时间戳 */
				param = new HashMap<String, Object>();
				param.put("updateTime", updatetime);
				params.add(param);
				Log.v("index", "TableName:" + table + "  index:" + index + "");
				/** 从后台取得某一张表的要更新的xml字符串数据 */
				resultResponse = String.valueOf(WebServiceProvider
						.callWebService(Global.NAMESPACE, METHOD_NAME, params,
								Global.getGlobalInstance().getSystemurl()
										+ Global.WEBSERVICE_URL,
								WebServiceProvider.RETURN_STRING, true));

				if (resultResponse == null || resultResponse.equals("null")
						|| resultResponse.equals("")) {
					int num = 1;
					do {
						resultResponse = String
								.valueOf(WebServiceProvider.callWebService(
										Global.NAMESPACE, METHOD_NAME, params,
										Global.getGlobalInstance()
												.getSystemurl()
												+ Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_STRING, true));
						num++;
						Log.v("Exception", "异常" + index + "" + "   " + table);
						Log.v("index", "TableName:" + table + "  index:"
								+ index + "");
						if (resultResponse != null
								&& !"".equals(resultResponse)
								&& !"null".equals(resultResponse))
							break;
					} while (num < 3 && updateOrFetchAllData == false);
				}

				if (resultResponse != null && !"".equals(resultResponse)
						&& !"null".equals(resultResponse)) {
					  FileHelper fileHelper = new FileHelper();

					try {
						if (!updateOrFetchAllData && index == 1) {
							/** 删除该表的所有数据，来更新所有的数据 */
							SqliteUtil.getInstance().deleteAllTableDataByTables(table);
						}
						String[] tab = table.split("_");
						File f = new File(
								"/sdcard/mapuni/MobileEnforcement/fj/"
										+ tab[tab.length - 1]);// 如果没有目录先建立目录
						if (f.exists()) {
							f.delete();
						}
					} catch (Exception e1) {
						Log.e(TAG, e1.getMessage());
						Log.v(TAG, "表 --> " + table + " "
								+ getSyncState(SYNC_FAIL));
						return SYNC_FAIL;
					}

					fileHelper.convertBase64StringToLocalFile(resultResponse,
							DEST_DIR, SERVICE_TEMP_ZIP);

					ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR
							+ SERVICE_TEMP_ZIP, DEST_DIR);

					for (File file : files) {
						try {
							/** 解析xml字符串数据 */
							InputStream inputStream = new FileInputStream(file);

							/** 解析xml数据的同时进行数据库数据的录入操作 */
							result = XmlHelper
									.insertTableDataWhenXmlPullParser(table,
											inputStream, XmlHelper.NODE_LEVEL1,
											updateOrFetchAllData);

							/** 删除解压后的临时文件 */
							file.delete();

							/** 录入成功则执行同步表日志 */
							try {
								synchronizeLogTableUpdateState(table);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (result == 0) {
								Log.v(TAG, "表 --> " + table + " "
										+ getSyncState(SYNC_NO_DATA));
								return SYNC_NO_DATA;
							}
							Log.v(TAG, "表 --> " + table + " "
									+ getSyncState(SYNC_SUCCESSFUL));
						} catch (FileNotFoundException e) {
							Log.e(TAG, e.getMessage());
							Log.v(TAG, "表 --> " + table + " "
									+ getSyncState(SYNC_FAIL));
							return SYNC_FAIL;
						}
					}
				} else {
					return SYNC_SERVER_FAILL;
				}
				index++;
			} while (result == SYNC_ONCE_NUMBER);
		}

		return SYNC_SUCCESSFUL;

	}

	/**
	 * 
	 */
	public void insertTableWithResult(String table, String resultResponse,
			Boolean updateOrFetchAllData) {

		if (resultResponse != null && !"".equals(resultResponse)
				&& !"null".equals(resultResponse)) {
			 FileHelper fileHelper = new FileHelper();
			try {
				if (!updateOrFetchAllData) {
					/** 删除该表的所有数据，来更新所有的数据 */
					SqliteUtil.getInstance().deleteAllTableDataByTables(table);
				}

			} catch (Exception e1) {
				Log.e(TAG, e1.getMessage());
				Log.v(TAG, "表 --> " + table + " " + getSyncState(SYNC_FAIL));
				return;
			}

			fileHelper.convertBase64StringToLocalFile(resultResponse, DEST_DIR,
					SERVICE_TEMP_ZIP);

			ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR
					+ SERVICE_TEMP_ZIP, DEST_DIR);

			for (File file : files) {
				try {
					/** 解析xml字符串数据 */
					InputStream inputStream = new FileInputStream(file);

					/** 解析xml数据的同时进行数据库数据的录入操作 */
					int result = XmlHelper.insertTableDataWhenXmlPullParser(
							table, inputStream, XmlHelper.NODE_LEVEL1,
							updateOrFetchAllData);

					/** 删除解压后的临时文件 */
					file.delete();

					if (result == 0) {
						Log.v(TAG, "表 --> " + table + " "
								+ getSyncState(SYNC_NO_DATA));
						return;
					}
					Log.v(TAG, "表 --> " + table + " "
							+ getSyncState(SYNC_SUCCESSFUL));
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage());
					Log.v(TAG, "表 --> " + table + " " + getSyncState(SYNC_FAIL));
					return;
				}
			}
		} else {
			return;
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

		String timestamp = "";

		try {
			if (!SqliteUtil.getInstance().checkTableExists(TABLE_SYNCHRONIZE_LOG)) {
				/** 同步表初始化 */
				synchronizeLogTableInit(null);
				/** 被同步表录入到同步表中 */
				synchronizeLogTableUpdateState(table);
			}

			String sql = "SELECT [" + COLUMN_UPDATETIME + "] FROM ["
					+ TABLE_SYNCHRONIZE_LOG + "] WHERE [" + COLUMN_TABLENAME
					+ "]='" + table + "' ORDER BY [" + COLUMN_UPDATETIME
					+ "] DESC LIMIT 1";
			Cursor c = SqliteUtil.getInstance().queryBySql(sql);
			timestamp = SqliteUtil.getInstance().convertCursorToStringArray(c)[0];

			Log.v(TAG, "synchronizeFetchTableLastestTimeStamp(" + table
					+ ") --> timestamp : " + timestamp);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return timestamp;
	}

	/**
	 * 获取服务器的时间
	 */
	public void getServerTime() {
		UPDATETIME = Global.getGlobalInstance().getDate();
		try {
			String result = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetServerDateTime",
					new ArrayList<HashMap<String, Object>>(), Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
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
		try {
			/** 同步表如果不存在就创建 */
			if (!SqliteUtil.getInstance().checkTableExists(TABLE_SYNCHRONIZE_LOG)) {
				synchronizeLogTableInit(null);
			}

			String existsSql = "Select [" + COLUMN_TABLENAME + "] From ["
					+ TABLE_SYNCHRONIZE_LOG + "] where [" + COLUMN_TABLENAME
					+ "]='" + updateTable + "' order by [" + COLUMN_UPDATETIME
					+ "] DESC Limit 1;";
			Cursor existsC = SqliteUtil.getInstance().queryBySql(existsSql);
			/*
			 * ArrayList<HashMap<String, Object>> c =
			 * sqliteUitl.queryBySqlReturnArrayListHashMap(existsSql); String
			 * exitstsTable=(String) c.get(0).get("updatetime");
			 */
			String exitstsTable = SqliteUtil.getInstance()
					.convertCursorToStringArray(existsC)[0];

			/** 执行语句影响的行数 */
			long count = 0;

			if (exitstsTable.length() > 0) {
				/** 同步表中如果有该表的信息则执行更新操作 */
				ContentValues updateValues = new ContentValues();
				updateValues.put(COLUMN_UPDATETIME, UPDATETIME);
				String whereClause = COLUMN_TABLENAME + "=?";
				String[] whereArgs = { updateTable };

				count = SqliteUtil.getInstance().update(TABLE_SYNCHRONIZE_LOG, updateValues,
						whereClause, whereArgs);
			}
			if (count > 0) {
				Log.v(TAG, "synchronizeLogTableUpdateState " + updateTable
						+ " successful , effect " + count + " row .");
			} else {
				

				int ID = SqliteUtil.getInstance().queryMaxValue("id", TABLE_SYNCHRONIZE_LOG);
				if (ID == -1) {
					Log.i("wang", "同步表错误");
					throw new RuntimeException("同步表错误");

				}
				

				ContentValues newupdate = new ContentValues();
				newupdate.put(COLUMN_ID, ++ID +  "");
				newupdate.put(COLUMN_TABLENAME, updateTable);
				newupdate.put(COLUMN_UPDATETIME, UPDATETIME);
				ArrayList<ContentValues> tablevalue = new ArrayList<ContentValues>();
				tablevalue.add(newupdate);
				SqliteUtil.getInstance().insert(tablevalue, TABLE_SYNCHRONIZE_LOG);
				Log.v(TAG, "synchronizeLogTableUpdateState " + updateTable
						+ " fail , effect 0 row .");

			}
		} catch (NumberFormatException e) {
			Log.e(TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * Description: 本地初始化同步表 创建同步表并调用插入方法把xml中的各个表插入
	 * 
	 * @param context
	 *            上下文 void
	 * @author 王留庚 Create at: 2012-11-30 下午03:40:30
	 */

	public void synchronizeLogTableInit(Context context) {

		String sql = "CREATE TABLE IF NOT EXISTS ["
				+ TABLE_SYNCHRONIZE_LOG
				+ "] ("
				+ "["
				+ COLUMN_ID
				+ "] INTEGER NOT NULL ON CONFLICT ROLLBACK UNIQUE ON CONFLICT ROLLBACK, "
				+ "[" + COLUMN_TABLENAME + "] VARCHAR, " + "["
				+ COLUMN_UPDATETIME + "] DATE)";
		try {
			SqliteUtil.getInstance().execute(sql);

			if (SqliteUtil.getInstance().checkTableExists(TABLE_SYNCHRONIZE_LOG)) {

				Log.v(TAG,
						"synchronizeLogTableInit create tongbu table successful .");

				/** 初始化被同步的表数据 */
				try {
					InputStream in = context.getResources().getAssets()
							.open("SYNCHRONIZE_LOG_TABLE.xml");
					XmlHelper help = new XmlHelper();
					@SuppressWarnings("static-access")
					ArrayList<HashMap<String, Object>> tablesList = help
							.getDataFromXmlStream(in, XmlHelper.NODE_LEVEL1);

					for (int i = 0; i < tablesList.size(); i++) {
						String syncTable = tablesList.get(i).get("table")// table为配置文件的字段
								.toString();
						if (syncTableDataInit(syncTable) > 0) {
							Log.v(TAG, "被同步表 -->" + syncTable + " 初始化成功！ ");
						} else {
							Log.v(TAG, "被同步表 -->" + syncTable + " 初始化失败！ ");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {

					e.printStackTrace();
				}
			} else {
				Log.v(TAG, "synchronizeLogTableInit create tongbu table fail .");
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * Description: 初始化同步表的数据 同步表插入数据方法（syncTable为从xml中读出的一条数据即表名）
	 * 
	 * @param syncTable
	 *            被同步表名称
	 * @return long 大于1这成功
	 * @author 王留庚 Create at: 2012-11-30 下午03:41:35
	 */

	public long syncTableDataInit(String syncTable) {
		try {
			String existsSql = "Select [" + COLUMN_TABLENAME + "] From ["
					+ TABLE_SYNCHRONIZE_LOG + "] where [" + COLUMN_TABLENAME
					+ "]='" + syncTable + "' order by [" + COLUMN_UPDATETIME
					+ "] DESC Limit 1;";

			Cursor existsC = SqliteUtil.getInstance().queryBySql(existsSql);
			String exitstsTable = SqliteUtil.getInstance()
					.convertCursorToStringArray(existsC)[0];

			if (exitstsTable.length() < 1) {

				/* 同步表中没有该表的信息则执行该表的初始化操作 */
				String sqlID = "SELECT (SELECT [" + COLUMN_ID + "] FROM ["
						+ TABLE_SYNCHRONIZE_LOG + "] ORDER BY [" + COLUMN_ID
						+ "] DESC LIMIT 1) + 1";
				Cursor c = SqliteUtil.getInstance().queryBySql(sqlID);

				String sqlIDReturn = SqliteUtil.getInstance().convertCursorToStringArray(c)[0];

				int id = 1;

				if (null != sqlIDReturn && !"".equals(sqlIDReturn)
						&& !"null".equals(sqlIDReturn.toLowerCase())) {
					id = Integer.valueOf(sqlIDReturn);
				}

				ArrayList<ContentValues> values = new ArrayList<ContentValues>();
				ContentValues contentValues = new ContentValues();
				contentValues.put(COLUMN_ID, id);
				contentValues.put(COLUMN_TABLENAME, syncTable);
				contentValues.put(COLUMN_UPDATETIME, UPDATETIME);
				values.add(contentValues);

				return SqliteUtil.getInstance().insert(values, TABLE_SYNCHRONIZE_LOG);
			}
		} catch (NumberFormatException e) {
			Log.e(TAG, e.getMessage());
			return 0;
		} catch (SQLiteException e) {
			Log.e(TAG, e.getMessage());
			return 0;
		}
		return 0;
	}

}
