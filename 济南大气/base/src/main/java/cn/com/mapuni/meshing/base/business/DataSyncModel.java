package cn.com.mapuni.meshing.base.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.os.Environment;
import android.util.Log;

import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.dataprovider.FileHelper;
import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;


public class DataSyncModel {
	/** ����log�����TAG */
	private final String TAG = "DataSyncModel";
	/** ����ͬ����ȡ��������ʱ�� */
	public String UPDATETIME;

	/** ͬ��ʧ�� */
	public final static int SYNC_FAIL = -1;
	/** ͬ����ʱ */
	public final static int SYNC_TIMEOUT = -2;

	/** ͬ������������ */
	public final static int SYNC_SERVICE_ERR = -3;

	/** ���ز������ */
	public final static int SYNC_INSERT_ERR = -4;

	/** һ������������������������ */
	public int SYNC_ONCE_NUMBER = 2000;
	/** ������ʱ�ļ��洢λ�� */
	public final String DEST_DIR = Environment.getExternalStorageDirectory().getPath() + "/mapuni/DataTemp/";
	/** ͬ������������ѹ���ļ����ļ����� */
	public final String SERVICE_TEMP_ZIP = "service.zip";

	/** ͬ������ */
	private final String TABLE_SYNCHRONIZE_LOG = "SYNCHRONIZE_LOG";
	/** ִ��ͬ�����������õķ��� */
	private final String METHOD_NAME = "synchronizeOneTableLastestDataForClient";

	/**
	 * �޸ĺ�����ͬ������
	 * 
	 * @param updateOrFetchAllData
	 *            false ͬ��ȫ������ true ͬ����������
	 * @param table
	 *            ���ݱ�����
	 * @return -1��ͬ��ʧ�� -2 ͬ����ʱ -3 ͬ���������쳣 ���ܲ������� �������粻ͨ
	 * @author wangliugeng
	 */
	public int synchronizeFetchServerData(boolean updateOrFetchAllData, String table) {

		/** ��ҳ��־ */
		int index = 1;
		/** ͬ����� */
		int result = 0;
		/** ִ��ͬ�������Ȼ�ȡ��������ǰʱ�� */
		getServerTime();
		/** ��ȡ�����±������ʱ�� */
		String updatetime = "";
		if (updateOrFetchAllData && !table.equals("T_Doc_Plan") && !table.equals("T_Doc_Case") && !table.equals("T_Bas_Expert") && !table.equals("T_ZSK_DangerChem")) {
			updatetime = synchronizeFetchTableLastestTimeStamp(table);
			LogUtil.e("hello", "updatetime-----------------" + updatetime);
		}
		try {
			do {
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
					SYNC_ONCE_NUMBER = 400;
				} else {
					SYNC_ONCE_NUMBER = 2000;
				}
				param.put("PageSize", SYNC_ONCE_NUMBER);
				params.add(param);

				/** ʱ��� */
				param = new HashMap<String, Object>();
				param.put("updateTime", updatetime);
				params.add(param);
				LogUtil.v("index", "TableName:" + table + "  index:" + index + "");
				/** �Ӻ�̨ȡ��ĳһ�ű��Ҫ���µ�xml�ַ������� */
				try {
					resultResponse = String.valueOf(WebServiceProvider.callWebService(Global.NAMESPACE, METHOD_NAME, params, Global.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true));
				} catch (IOException e1) {

					e1.printStackTrace();
					return SYNC_TIMEOUT;
				}

				/*
				 * if (resultResponse == null || resultResponse.equals("null")||
				 * resultResponse.equals("")) {//һ�ε���ͨ�ٵ���1��
				 * 
				 * try { resultResponse =
				 * String.valueOf(WebServiceProvider.callWebService(
				 * Global.NAMESPACE, METHOD_NAME, params,
				 * Global.getGlobalInstance() .getSystemurl() +
				 * Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING,
				 * true)); } catch (IOException e) {
				 * 
				 * e.printStackTrace(); return SYNC_TIMEOUT; }
				 * 
				 * LogUtil.v(TAG, "�쳣" + index + "" + "   " + table);
				 * 
				 * 
				 * 
				 * }
				 */

				if (resultResponse != null && !"".equals(resultResponse) && !"null".equals(resultResponse)) {
					FileHelper fileHelper = new FileHelper();

					/*wsc 3.25 if (index == 1) {
						SqliteUtil.getInstance().db.beginTransaction();// ��ʼ�������
					}*/
					if (!updateOrFetchAllData && index == 1) {
						/** ɾ���ñ���������ݣ����������е����� */
						SqliteUtil.getInstance().deleteAllTableDataByTables(table);
					}

					fileHelper.convertBase64StringToLocalFile(resultResponse, DEST_DIR, SERVICE_TEMP_ZIP);

					ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR + SERVICE_TEMP_ZIP, DEST_DIR);

					for (File file : files) {
						try {
							/** ����xml�ַ������� */
							InputStream inputStream = new FileInputStream(file);

							/** ����xml���ݵ�ͬʱ�������ݿ����ݵ�¼����� */
							result = result + XmlHelper.insertTableDataWhenXmlPullParser(table, inputStream, XmlHelper.NODE_LEVEL1, updateOrFetchAllData);

							/** ɾ����ѹ�����ʱ�ļ� */
							file.delete();

							/** ¼��ɹ���ִ��ͬ������־ */
							if (result != 0 && result % SYNC_ONCE_NUMBER != 0) {// ͬ�����
								synchronizeLogTableUpdateState(table);
							//wsc3.25	SqliteUtil.getInstance().db.setTransactionSuccessful();
								LogUtil.v(TAG, "�� --> " + table + " " + getSyncState(1));

							}

						} catch (FileNotFoundException e) {
							LogUtil.e(TAG,"//"+ e.getMessage());
							LogUtil.v(TAG, "�� --> " + table + " " + getSyncState(SYNC_FAIL));
							result = SYNC_FAIL;
						}
					}
				} else {
					result = SYNC_SERVICE_ERR;

				}
				index++;
			} while (result != 0 && result % SYNC_ONCE_NUMBER == 0);// ���ΪSYNC_ONCE_NUMBER�ı��������ͬ��
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = SYNC_SERVICE_ERR;

		} finally {
			//wsc3.25
			/*if (SqliteUtil.getInstance().db.inTransaction()) {// ��������
				SqliteUtil.getInstance().db.endTransaction();
			}*/
		}

		return result;

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
	 * ��ȡ��������ʱ��
	 */
	public void getServerTime() {
		UPDATETIME = Global.getGlobalInstance().getDate();
		try {
			String result = (String) WebServiceProvider.callWebService(Global.NAMESPACE, "GetServerDateTime", new ArrayList<HashMap<String, Object>>(), Global.getGlobalInstance()
					.getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
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
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TABLENAME", updateTable);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList(TABLE_SYNCHRONIZE_LOG, conditions);
		ContentValues updateValues = new ContentValues();
		updateValues.put("UPDATETIME", UPDATETIME);
		if (data != null && data.size() > 0) {// ���ڣ�ִ�и��²���
			String[] whereArgs = { updateTable };
			try {
				SqliteUtil.getInstance().update(TABLE_SYNCHRONIZE_LOG, updateValues, "TABLENAME=?", whereArgs);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {// �����ڣ�ִ�в������
			int id = SqliteUtil.getInstance().queryMaxValue("id", TABLE_SYNCHRONIZE_LOG);
			updateValues.put("ID", id + 1);
			updateValues.put("TABLENAME", updateTable);
			SqliteUtil.getInstance().insert(updateValues, TABLE_SYNCHRONIZE_LOG);
		}

	}

	/**
	 * Description: ��ȡͬ��״̬
	 * 
	 * @param state
	 *            ״ֵ̬
	 * @return ����ͬ����� String
	 * @author ������ Create at: 2012-11-30 ����03:12:51
	 */
	public String getSyncState(int state) {
		switch (state) {
		case SYNC_FAIL:
			return "ʧ��";
		case SYNC_SERVICE_ERR:
			return "������쳣";
		case SYNC_TIMEOUT:
			return "���ӷ�������ʱ";
		default:
			return "ͬ���ɹ�";
		}

	}

	public void syncServiceData(ArrayList<String> tables, Boolean updateOrFetchAllData) {
		for (String table : tables) {
			synchronizeFetchServerData(updateOrFetchAllData, table);
		}

	}

}
