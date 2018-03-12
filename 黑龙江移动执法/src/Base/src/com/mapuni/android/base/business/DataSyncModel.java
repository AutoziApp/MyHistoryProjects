package com.mapuni.android.base.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.os.Environment;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.WebServiceProvider;

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
	public String SERVICE_TEMP_ZIP = "";

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
		/** ��ȡ�����±������ʱ�� */
		String updatetime = "";

		if (updateOrFetchAllData) {
			updatetime = synchronizeFetchTableLastestTimeStamp(table);
			Log.e("hello", "updatetime-------" + table + "----------" + updatetime);
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

				if (resultResponse != null && !"".equals(resultResponse) && !"null".equals(resultResponse)) {
					FileHelper fileHelper = new FileHelper();

					if (!updateOrFetchAllData && index == 1) {
						/** ɾ���ñ���������ݣ����������е����� */
						SqliteUtil.getInstance().deleteAllTableDataByTables(table);
					}
					SERVICE_TEMP_ZIP = UUID.randomUUID() + "";
					fileHelper.convertBase64StringToLocalFile(resultResponse, DEST_DIR, SERVICE_TEMP_ZIP);

					ArrayList<File> files = fileHelper.deZipFiles(DEST_DIR + SERVICE_TEMP_ZIP, DEST_DIR);

					for (File file : files) {
						try {
							/** ����xml�ַ������� */
							InputStream inputStream = new FileInputStream(file);
							
							if (table.toString().equals("PC_Users")) {
							}
							/** ����xml���ݵ�ͬʱ�������ݿ����ݵ�¼����� */
							result += XmlHelper.insertTableDataWhenXmlPullParser(table, inputStream, XmlHelper.NODE_LEVEL1, updateOrFetchAllData);

							/** ɾ����ѹ�����ʱ�ļ� */
							if (inputStream != null) {
								inputStream.close();
							}
							file.delete();
						} catch (FileNotFoundException e) {
							Log.e(TAG, e.getMessage());
							Log.v(TAG, "�� --> " + table + " " + getSyncState(SYNC_FAIL));
							result = SYNC_FAIL;
						}
					}
					if (table.toString().equals("T_Attachment")) {
						String[] AttachUnit=new String[]{"14","21","22","23","24","15","16","17"};
						
						//String[] AttachUnit=new String[]{"15","16","17"};
						
						for (int i = 0; i < AttachUnit.length; i++) {
							
					
						
						ArrayList<HashMap<String, Object>> params2 = new ArrayList<HashMap<String, Object>>();

						HashMap<String, Object> param2 = new HashMap<String, Object>();
						param2.put("RegionCode", Global.getGlobalInstance().getAreaCode());
						param2.put("AttachUnit", AttachUnit[i]);
						param2.put("UpdateTime", updatetime);
						param2.put("PageIndex", 1);
						param2.put("PageSize", SYNC_ONCE_NUMBER+1000);
						params2.add(param2);
						
						String callWebService = (String) WebServiceProvider.callWebService(Global.NAMESPACE, "GetSys_Attachment", params2, Global.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
					
					
						
						
						if(!"".equals(callWebService)&&callWebService!=null){
							
							String[] node = { "Guid", "FileName", "FilePath", "Extension",
									"FK_Unit", "FK_Id", "Remark", "LinkUrl",
									"Thumbnail","UpdateTime", "FileType"};
							
							ArrayList<HashMap<String, Object>> list = JsonHelper.paseJSON(
									callWebService, node);
							
							if (list.size()>0) {
								
								for (int j = 0; j < list.size(); j++) {
									ContentValues updateValues = new ContentValues();
									String FilePath = list.get(j).get("FilePath").toString();
									HashMap<String, Object> map;
									if (FilePath.length()!=0) {
										  String[] sss=FilePath.split("/");
										  
										  String string2 = sss[sss.length-1];
									     String[]  strings=string2.split("\\.");
									 	updateValues.put("guid",strings[0] );
										 
									 map=new HashMap<String, Object>();
										
										map.put("guid", strings[0]);
									}else{
									 	updateValues.put("guid", list.get(j).get("Guid")
												.toString());
									 	 map=new HashMap<String, Object>();
											
											map.put("guid", list.get(j).get("Guid").toString());
									}
							
								     
								
									updateValues.put("FileName", list.get(j).get("FileName")
											.toString());
									updateValues.put("FilePath", FilePath);
									updateValues.put("Extension", list.get(j).get("Extension")
											.toString());
									//�޸�
									if ( list.get(j).get("FK_Unit")
											.toString().equals("YYZZ")) {
										updateValues.put("FK_Unit",31);
									} else if ( list.get(j).get("FK_Unit")
											.toString().equals("SCGYLCT")) {
										updateValues.put("FK_Unit",98);
									}else if ( list.get(j).get("FK_Unit")
											.toString().equals("ZZJGDMTP")) {
										updateValues.put("FK_Unit",32);
									}else if ( list.get(j).get("FK_Unit")
											.toString().equals("QYZPMSYT")) {
										updateValues.put("FK_Unit",99);
									}else{
										updateValues.put("FK_Unit",list.get(j).get("FK_Unit")
												.toString());
									}
									
									//
									updateValues.put("FK_Id", list.get(j).get("FK_Id")
											.toString());
									updateValues.put("Remark", list.get(j).get("Remark")
											.toString());
									updateValues.put("LinkUrl", list.get(j).get("LinkUrl")
											.toString());
									updateValues.put("UpdateTime",
											list.get(j).get("UpdateTime").toString());
									updateValues.put("FileType", list.get(j).get("FileType")
											.toString());
								
									ArrayList<HashMap<String,Object>> list2 = SqliteUtil.getInstance().getList("T_Attachment", map);
									if (list2.size()>0) {
										SqliteUtil.getInstance().update("T_Attachment", updateValues, "guid=?", new String[]{list.get(j).get("Guid")
												.toString()});
									}else{
										
										result+=SqliteUtil.getInstance().insert(updateValues,
												"T_Attachment");
									}
								
								}
						
							}
							
						}else{
							result = SYNC_SERVICE_ERR;
						}
						}
						}
					
				} else {
					result = SYNC_SERVICE_ERR;
				}
				index++;
			} while (result != 0 && result % SYNC_ONCE_NUMBER == 0);// ���ΪSYNC_ONCE_NUMBER�ı��������ͬ��
		} catch (Exception e) {
			e.printStackTrace();
			result = SYNC_SERVICE_ERR;
		} finally {
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
			HashMap<String, Object> data = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
					"SELECT strftime('%Y-%m-%d %H:%M:%f',UPDATETIME,'localtime') as UpdateTime from SYNCHRONIZE_LOG a where a.[TABLENAME] = '" + table + "';");
			timestamp = data.get("updatetime").toString();
		} catch (Exception e) {
			OtherTools.showExceptionLog("��ȡ����ʱ���쳣");
		}
		return timestamp;
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
