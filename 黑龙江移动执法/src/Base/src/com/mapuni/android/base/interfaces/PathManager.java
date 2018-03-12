/**
 * 
 */
package com.mapuni.android.base.interfaces;

import android.os.Environment;

/**
 * FileName: PathManager.java Description:Ӧ�ó�������
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-7 ����01:10:48
 */
public class PathManager {
	/* ����·�� */
	/** ϵͳ�����ļ��ĸ�Ŀ¼mapuni�ļ���·�� */
	public static final String BASE_PATH = Environment
			.getExternalStorageDirectory() + "/mapuni/";
	/** ϵͳ�����ļ�MobileEnforcement�ļ���·�� */
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH
			+ "MobileEnforcement/";

	/* ��̨·�� */
	/** ��̨verson.xml�ļ�·�� */
	public static final String APK_CODE_URL = "/AutoUpdate/app/verson.xml";// �汾��ϢURL��׺
	/** ��̨config.xml�ļ�·�� */
	public static final String CONFIG_CODE_URL = "/AutoUpdate/config/config.xml";
	/** ��̨video_config.xml�ļ�·�� */
	public static final String VIDEOCOFIG_CODE_URL = "/AutoUpdate/video_config/video_config.xml";
	/** ��̨���apk���µ�·�� */
	public static final String APK_DOWN_URL = "/AutoUpdate/app/MobileEnforcement.zip";
	/** ��̨������ݿ�汾��verson.xml�ļ�·�� */
	public static final String DB_CODE_URL = "/AutoUpdate/db/verson.xml";
	/** ��̨����ֻ����ֻ����ļ�·�� */
	public static final String DE_DOWN_URL = "/AutoUpdate/db/MobileEnforcement.zip";
	/** ��̨������񸽼���·������TaskExecute�ļ���·�� */
	public static final String JCJLRWFJ_DOWN_URL = "/MobileEnforcement/Attach/TaskExecute/";

	/* �ֻ���·�� */
	/** �ֻ�������ʶ���Ųɼ���ͼ�� */
	public static final String SDCARD_UserPhoto_LOCAL_PATH = BASE_PATH
			+ "userphoto";
	/** �ֻ��˸���apk�ı���Ŀ¼·������AutoUpdate�ļ��� */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "AutoUpdate";
	/** �ֻ��˸���apk�ı����ļ�·������update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "AutoUpdate/update.apk";
	/** �ֻ������ݿ�MobileEnforcement.db�ļ����·�� */
	public static final String SDCARD_DB_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/MobileEnforcement.db";
	/** �ֻ���config�ļ�·�� */
	public static final String SDCARD_CONFIG_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/config.xml";
	/** �ֻ���data�ļ�Ŀ¼ */
	public static final String SDCARD_DATA_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data";
	/** �ֻ�����־�ļ�Ŀ¼ */
	public static final String SDCARD_LOG_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/log";
	/** �ֻ��˴�ű������ݿ���ļ�Ŀ¼����datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/datebaseCopy/";
	/** �ֻ��˴�ű������ݿ���ļ�·������MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/datebaseCopy/MobileEnforcement.db";
	/** �ֻ���ͬ��������ŵı���·������fj */
	public static final String SDCARD_FJ_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/";
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = SDCARD_RASK_DATA_PATH
			+ "data/video_config.xml";
	/** �ֻ���ִ��������·������sitelaw */
	public static final String SITELAWRECORD_PATH = SDCARD_RASK_DATA_PATH
			+ "data/sitelaw";

	/** �ֻ���ִ������ͨ���·������ZFBST */
	public static final String LAWKNOWALL_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/ZFBST";
	
	/** �ֻ��˻������ִ���ֲ���·��*/
	public static final String HJJCZFSC_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/�������ִ���ֲ�";

	/** �ֻ����������ݵ���ʱ�ļ�Ŀ¼����DataTemp */
	public static final String SDCARD_TEMP_PATH = BASE_PATH + "DataTemp/";

	/* ���ӷ�����·�� */
	/** �ֻ�������WebService��·�� */
	public static final String WEBSERVICE_URL = "/WebService/MobileEnforcementWebService.asmx";
	/** �ֻ�������OA��WebService��·�� */
	public static final String OAWEBSERVICE_URL = "/MobileEnforcement/WebService/OAWebService.asmx";
	/** �����ռ� */
	public static final String NAMESPACE = "http://tempuri.org/";
}
