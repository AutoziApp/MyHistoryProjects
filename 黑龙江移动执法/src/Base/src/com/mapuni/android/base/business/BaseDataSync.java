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
 * FileName: DataSync.java Description: ����ͬ��ҵ����
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����02:47:28
 */
@SuppressWarnings("serial")
public class BaseDataSync extends BaseClass implements Serializable, IList {
	/** ����log�����TAG */
	private  final String TAG = "DataSync";
	/** ҵ��������� */
	public static final String BusinessClassName = "BaseDataSync";
	/** ִ��ͬ�����������õķ��� */
	private  final String METHOD_NAME = "synchronizeOneTableLastestDataForClient";
	/** ͬ��xml��ߵ�id */
	private  final String PRIMARY_KEY = "id";
	/** ����ͬ���б���ʽ��xml��ߵı������� */
	private  final String LIST_STYLE_NAME = "SYNCHRONIZE_LOG";
	/** ����ͬ������ȡ��xml�ļ����� */
	private  final String SYNC_NAME_REFLECT = "datasync_config.xml";
	/** ����ͬ��xml�ļ���table��ǩ���� */
	public  final static String SYNC_TABLE_REFLECT = "table";
	/** ����ͬ����ȡ��������ʱ�� */
	public String UPDATETIME;
	/** ����ͬ����ʾ���б������ */
	private final String list_title_text = "����ͬ��";
	/** ��ǰ��ͬ���ı����SYNCHRONIZE_LOG���е�id */
	@SuppressWarnings("unused")
	private String current_id = "";
	/** ͬ��xml�ļ����tablename��ǩ */
	private final String table_name = COLUMN_TABLENAME;
	/** ͬ��ҵ�����б�����Ĵ��� */
	public int ListScrollTimes;
	/** sqlite���ò����Ĺ����� */
	
	
	
	/** ͬ������ */
	public  final String TABLE_SYNCHRONIZE_LOG = "SYNCHRONIZE_LOG";
	/** ��ͬ���ı��� */
	public static final String COLUMN_TABLENAME = "TABLENAME";
	/** ��ͬ���ı��ϴθ��µ�ʱ�� */
	public static final String COLUMN_UPDATETIME = "UPDATETIME";
	/** ���� */
	public  final String COLUMN_ID = "ID";
	/** ������ʱ�ļ��洢λ�� */
	public  final String DEST_DIR = "/sdcard/mapuni/DataTemp/";
	/** ͬ������������ѹ���ļ����ļ����� */
	public  final String SERVICE_TEMP_ZIP = "service.zip";
	/***/
	public  Boolean IsDataSync = false;
	/** ͬ��ʧ�� */
	public  final static int SYNC_FAIL = 0;
	/** ͬ���ɹ� */
	public  final static int SYNC_SUCCESSFUL = 1;
	/** û��ͬ������ */
	public  final static int SYNC_NO_DATA = 2;
	/** ������쳣 */
	public  final static int SYNC_SERVER_FAILL = 3;
	/** һ������������������������ */
	public int SYNC_ONCE_NUMBER = 2000;

	/**
	 * Description: ��ȡͬ��״̬
	 * 
	 * @param state
	 *            ״ֵ̬
	 * @return ����ͬ����� String
	 * @author ������ Create at: 2012-11-30 ����03:12:51
	 */
	public static String getSyncState(int state) {
		switch (state) {
		case SYNC_SUCCESSFUL:
			return "ͬ���ɹ�";
		case SYNC_NO_DATA:
			return "û��ͬ������";
		case SYNC_SERVER_FAILL:
			return "������쳣";
		default:
			return "ͬ��ʧ��";
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
	 * Description: ��ȡͬ�������������
	 * 
	 * @return ���ض�ȡ���ݿ�ͬ�������е����ݼ��� ArrayList<HashMap<String,Object>>
	 * @author ������ Create at: 2012-11-30 ����03:13:47
	 */
	public ArrayList<HashMap<String, Object>> getDataSyncList() {
		ArrayList<HashMap<String, Object>> data = BaseClass.DBHelper
				.getList(table_name);
		return data;
	}

	/**
	 * Description: �Ա��������˺���
	 * 
	 * @param context
	 *            ������
	 * @return ���صõ����б��� ArrayList<HashMap<String,Object>>
	 * @author ������ Create at: 2012-11-30 ����03:14:23
	 */
	@SuppressWarnings("static-access")
	public ArrayList<HashMap<String, Object>> getDataList(Context context) {

		/*
		 * if (!DBHelper.checkTableExists(table_name))
		 * {//�ñ������������ʼ��һ��ͬ����ͬ��������ֱ����SYNCHRONIZE_LOG_TABLE.xml����
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
					/* ����Ӣ�ı��� */
					dataRow.put(column_tablename,
							styleRow.get(column_tablename));
				}
			}
			data.add(dataRow);
		}

		return data;
	}

	/**
	 * Description: ��ȡͬ��������ı���
	 * 
	 * @param context
	 *            ������
	 * @return ����ͬ��xml�����ı��� ArrayList<HashMap<String,Object>>
	 * @author Administrator Create at: 2012-11-30 ����03:19:45
	 */
	@SuppressWarnings("static-access")
	public ArrayList<HashMap<String, Object>> getTablenameCN(Context context) {
		try {
			ArrayList<HashMap<String, Object>> tablename = BaseClass.xmlHelper
					.getDataFromXmlStream(context.getResources().getAssets()
							.open(SYNC_NAME_REFLECT), XmlHelper.NODE_LEVEL1);
			ArrayList<HashMap<String, Object>> tablenameCN = new ArrayList<HashMap<String, Object>>();
			for (HashMap<String, Object> tableMap : tablename) {
				if (DisplayUitl.getAuthority(tableMap.get("qxid").toString())) {// �жϵ�ǰ�û��Ƿ���Ȩ��
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
	 * Description: �����õ�������ѡ��Ҫͬ�������б������
	 * 
	 * @param updateOrFetchAllData
	 *            ͬ�����»���ͬ��ȫ������
	 * @param context
	 *            ������
	 * @param NeedSystableNum
	 *            Ҫͬ���ı���ͬ�������ļ��е�id��
	 * @return String[] ���ر�ͬ���������
	 * @author ������ Create at: 2012-11-30 ����03:22:28
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

		Log.v(TAG, (updateOrFetchAllData ? "��������" : "ȫ������") + "ͬ���� --> "
				+ tablesSB.toString());
		String[] tables = tablesSB.toString().split(",");
		return tables;

	}

	@Override
	public HashMap<String, Object> getFilter() {
		return null;
	}

	/**
	 * Description: ��ȡ��ĸ���ʱ����ɵ�json
	 * 
	 * @param tables
	 *            ����Ҫ�ı������
	 * @return ����ʱ�����json String
	 * @author ������ Create at: 2012-11-30 ����03:21:03
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
	 * Description: ͬ���ɹ������ظ���
	 * 
	 * @param table
	 *            ͬ���ı���
	 * @param hturl
	 *            ͬ���ĵ�ַ void
	 * @author Administrator Create at: 2012-12-3 ����09:59:13
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
						+ tab[tab.length - 1]);// ���û��Ŀ¼�Ƚ���Ŀ¼
				if (!f.exists())
					f.mkdirs();
				File fil = new File(PathManager.SDCARD_FJ_LOCAL_PATH
						+ tab[tab.length - 1] + "/" + name);// ��Ŀ¼֮���ļ�
				fos = new FileOutputStream(fil);

				byte[] bytes = new byte[1024];
				int flag = -1;
				int count = 0;// �ļ����ֽڳ���
				while ((flag = in.read(bytes)) != -1) {// ��δ�����ļ�ĩβ��һֱ��ȡ
					fos.write(bytes, 0, flag);
					count += flag;

				}
				fos.flush();
				fos.close();
				Log.v(TAG, "ͬ������   " + name + " �ɹ�");
			}
			} catch (ClientProtocolException e) {
				ExceptionManager.WriteCaughtEXP(e, "BaseDataSync");
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
				Log.e(TAG, name + "����ͬ��ʧ��");

			} catch (IOException e) {
				ExceptionManager.WriteCaughtEXP(e, "BaseDataSync");
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
				Log.e(TAG, name + "����ͬ��ʧ��");
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
	 * ɾ��ĳ��Ŀ¼�°������ļ�
	 * 
	 * @param url
	 *            Ŀ¼
	 * @param filename
	 *            Ҫɾ�����ļ���������
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
	 * Description: ��������ͬ����̨����
	 * 
	 * @param updateOrFetchAllData
	 *            true����ͬ������ false ����ͬ��ȫ��
	 * @param table
	 *            ͬ���ı���
	 * @return int���� int
	 * @throws IOException
	 * @author ������ Create at: 2012-11-30 ����03:29:00
	 */
	public int synchronizeFetchServerData(boolean updateOrFetchAllData,
			String table) throws IOException {
		/*
		 * return
		 * BaseClass.DBHelper.synchronizeFetchServerData(updateOrFetchAllData,
		 * table, Global.NAMESPACE, global.getUrl()+Global.WEBSERVICE_URL,
		 * METHOD_NAME, Global.getGlobalInstance().getSname());
		 */

		// �ж��Ƿ�ΪȨ�ޱ�
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
						/** ��Ϊ�գ��������� */
						for (HashMap<String, Object> hashMap : hash) {
							num = hashMap.get("num").toString();
						}
						HashMap<String, String> ReadVerson = new HashMap<String, String>();
						ReadVerson = upadte.readVerson(gis_configWebUrl);
						if (ReadVerson != null) {
							String verson = ReadVerson.get("verson").toString();
							if ((verson.compareTo(num)) > 0) {
								/** ���� */
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
						/** Ϊ�գ�ֱ������ */
						FileHelper.downLoadFile(Global.getGlobalInstance()
								.getSystemurl()
								+ "/AutoUpdate/gis_config/gis_config.xml",
								GisConfigurl);
					}
				} else if (table.equals("config.xml")) {
					/** ����config.xml,num��config�汾�� */
					String confignum = ConfigManager.getValue("config",
							"versonnum");
					HashMap<String, String> configReadVerson = new HashMap<String, String>();
					configReadVerson = upadte.readVerson(configWebUrl);
					if (configReadVerson != null
							&& confignum != null
							&& configReadVerson.get("verson") != null
							&& (String.valueOf(configReadVerson.get("verson"))
									.compareTo(confignum)) > 0) {
						/** ���� */
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
			/** ��ҳ��־ */
			int index = 1;
			int result = 0;
			/** ִ��ͬ�������Ȼ�ȡ��������ǰʱ�� */
			getServerTime();
			do {
				/** ��ȡ�����±������ʱ�� */
				String updatetime = "";
				try {
					if (updateOrFetchAllData) {
						updatetime = synchronizeFetchTableLastestTimeStamp(table);
					}
				} catch (Exception e) {
					Log.v(TAG, e.getMessage());
					updatetime = "";
					Log.v(TAG, "�� --> " + table + " " + getSyncState(SYNC_FAIL));
					return SYNC_FAIL;
				}
				String resultResponse = "";

				/** ����webserice�еĲ��� */
				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

				/** ���� */
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("tableName", table);
				params.add(param);

				/** ҳ��1,2,3 */
				param = new HashMap<String, Object>();
				param.put("PageIndex", index);
				params.add(param);

				/** ��������10000 */
				param = new HashMap<String, Object>();
				/** Σ��Ʒ������⣬�������� */
				if ("T_ZSK_DangerChem".equals(table)) {
					param.put("PageSize", 400);
				} else {
					param.put("PageSize", SYNC_ONCE_NUMBER);
				}
				params.add(param);

				/** ʱ��� */
				param = new HashMap<String, Object>();
				param.put("updateTime", updatetime);
				params.add(param);
				Log.v("index", "TableName:" + table + "  index:" + index + "");
				/** �Ӻ�̨ȡ��ĳһ�ű��Ҫ���µ�xml�ַ������� */
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
						Log.v("Exception", "�쳣" + index + "" + "   " + table);
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
							/** ɾ���ñ���������ݣ����������е����� */
							SqliteUtil.getInstance().deleteAllTableDataByTables(table);
						}
						String[] tab = table.split("_");
						File f = new File(
								"/sdcard/mapuni/MobileEnforcement/fj/"
										+ tab[tab.length - 1]);// ���û��Ŀ¼�Ƚ���Ŀ¼
						if (f.exists()) {
							f.delete();
						}
					} catch (Exception e1) {
						Log.e(TAG, e1.getMessage());
						Log.v(TAG, "�� --> " + table + " "
								+ getSyncState(SYNC_FAIL));
						return SYNC_FAIL;
					}

					fileHelper.convertBase64StringToLocalFile(resultResponse,
							DEST_DIR, SERVICE_TEMP_ZIP);

					ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR
							+ SERVICE_TEMP_ZIP, DEST_DIR);

					for (File file : files) {
						try {
							/** ����xml�ַ������� */
							InputStream inputStream = new FileInputStream(file);

							/** ����xml���ݵ�ͬʱ�������ݿ����ݵ�¼����� */
							result = XmlHelper
									.insertTableDataWhenXmlPullParser(table,
											inputStream, XmlHelper.NODE_LEVEL1,
											updateOrFetchAllData);

							/** ɾ����ѹ�����ʱ�ļ� */
							file.delete();

							/** ¼��ɹ���ִ��ͬ������־ */
							try {
								synchronizeLogTableUpdateState(table);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (result == 0) {
								Log.v(TAG, "�� --> " + table + " "
										+ getSyncState(SYNC_NO_DATA));
								return SYNC_NO_DATA;
							}
							Log.v(TAG, "�� --> " + table + " "
									+ getSyncState(SYNC_SUCCESSFUL));
						} catch (FileNotFoundException e) {
							Log.e(TAG, e.getMessage());
							Log.v(TAG, "�� --> " + table + " "
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
					/** ɾ���ñ���������ݣ����������е����� */
					SqliteUtil.getInstance().deleteAllTableDataByTables(table);
				}

			} catch (Exception e1) {
				Log.e(TAG, e1.getMessage());
				Log.v(TAG, "�� --> " + table + " " + getSyncState(SYNC_FAIL));
				return;
			}

			fileHelper.convertBase64StringToLocalFile(resultResponse, DEST_DIR,
					SERVICE_TEMP_ZIP);

			ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR
					+ SERVICE_TEMP_ZIP, DEST_DIR);

			for (File file : files) {
				try {
					/** ����xml�ַ������� */
					InputStream inputStream = new FileInputStream(file);

					/** ����xml���ݵ�ͬʱ�������ݿ����ݵ�¼����� */
					int result = XmlHelper.insertTableDataWhenXmlPullParser(
							table, inputStream, XmlHelper.NODE_LEVEL1,
							updateOrFetchAllData);

					/** ɾ����ѹ�����ʱ�ļ� */
					file.delete();

					if (result == 0) {
						Log.v(TAG, "�� --> " + table + " "
								+ getSyncState(SYNC_NO_DATA));
						return;
					}
					Log.v(TAG, "�� --> " + table + " "
							+ getSyncState(SYNC_SUCCESSFUL));
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage());
					Log.v(TAG, "�� --> " + table + " " + getSyncState(SYNC_FAIL));
					return;
				}
			}
		} else {
			return;
		}

	}

	/**
	 * Description: ��ȡ�������ʱ��� ���ʱ�������Ϊ��������ͬ���ñ��ȫ�����ݣ���֮�������ʱ�����ͬ��Ҫͬ������������
	 * 
	 * @param table
	 *            ��ͬ���ı�����
	 * @return default : ""�����ر�ͬ�����ʱ��� String
	 * @author ������ Create at: 2012-11-30 ����03:37:30
	 */
	public String synchronizeFetchTableLastestTimeStamp(String table) {

		String timestamp = "";

		try {
			if (!SqliteUtil.getInstance().checkTableExists(TABLE_SYNCHRONIZE_LOG)) {
				/** ͬ�����ʼ�� */
				synchronizeLogTableInit(null);
				/** ��ͬ����¼�뵽ͬ������ */
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
	 * ��ȡ��������ʱ��
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
				System.out.println("��ȡ������ʱ��BaseDataSync=====" + result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: ����������ݺ�¼��ͬ����
	 * 
	 * @param updateTable
	 *            ���±�ͬ������ͬ�����е�ʱ�� void
	 * @author ������ Create at: 2012-11-30 ����03:39:10
	 */
	public void synchronizeLogTableUpdateState(String updateTable) {
		try {
			/** ͬ������������ھʹ��� */
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

			/** ִ�����Ӱ������� */
			long count = 0;

			if (exitstsTable.length() > 0) {
				/** ͬ����������иñ����Ϣ��ִ�и��²��� */
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
					Log.i("wang", "ͬ�������");
					throw new RuntimeException("ͬ�������");

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
	 * Description: ���س�ʼ��ͬ���� ����ͬ�������ò��뷽����xml�еĸ��������
	 * 
	 * @param context
	 *            ������ void
	 * @author ������ Create at: 2012-11-30 ����03:40:30
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

				/** ��ʼ����ͬ���ı����� */
				try {
					InputStream in = context.getResources().getAssets()
							.open("SYNCHRONIZE_LOG_TABLE.xml");
					XmlHelper help = new XmlHelper();
					@SuppressWarnings("static-access")
					ArrayList<HashMap<String, Object>> tablesList = help
							.getDataFromXmlStream(in, XmlHelper.NODE_LEVEL1);

					for (int i = 0; i < tablesList.size(); i++) {
						String syncTable = tablesList.get(i).get("table")// tableΪ�����ļ����ֶ�
								.toString();
						if (syncTableDataInit(syncTable) > 0) {
							Log.v(TAG, "��ͬ���� -->" + syncTable + " ��ʼ���ɹ��� ");
						} else {
							Log.v(TAG, "��ͬ���� -->" + syncTable + " ��ʼ��ʧ�ܣ� ");
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
	 * Description: ��ʼ��ͬ��������� ͬ����������ݷ�����syncTableΪ��xml�ж�����һ�����ݼ�������
	 * 
	 * @param syncTable
	 *            ��ͬ��������
	 * @return long ����1��ɹ�
	 * @author ������ Create at: 2012-11-30 ����03:41:35
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

				/* ͬ������û�иñ����Ϣ��ִ�иñ�ĳ�ʼ������ */
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
