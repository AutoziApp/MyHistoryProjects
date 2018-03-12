/**
 * 
 */
package com.mapuni.android.base.interfaces;

import com.mapuni.android.base.Global;

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
	public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/mapuni/";
	/** ϵͳ�����ļ�MobileEnforcement�ļ���·�� */
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "MobileEnforcement/";

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
	public static final String SDCARD_UserPhoto_LOCAL_PATH = BASE_PATH + "userphoto";
	/** �ֻ��˸���apk�ı���Ŀ¼·������AutoUpdate�ļ��� */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate";
	/** �ֻ��˸���apk�ı����ļ�·������update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate/update.apk";
	/** �ֻ������ݿ�MobileEnforcement.db�ļ����·�� */
	public static final String SDCARD_DB_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/MobileEnforcement.db";
	/** �ֻ���config�ļ�·�� */
	public static final String SDCARD_CONFIG_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/config.xml";
	/**
	 * ��ʷִ����¼���õ�·��
	 */
	public static final String ROOT_VIDEO_PATH = PathManager.BASE_PATH + "historyVideo/";
	/** �ֻ���data�ļ�Ŀ¼ */
	public static final String SDCARD_DATA_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data";
	/** �ֻ�����־�ļ�Ŀ¼ */
	public static final String SDCARD_LOG_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/log";
	/** �ֻ��˴�ű������ݿ���ļ�Ŀ¼����datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/datebaseCopy/";
	/** �ֻ��˴�ű������ݿ���ļ�·������MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/datebaseCopy/MobileEnforcement.db";
	/** �ֻ���ͬ��������ŵı���·������fj */
	public static final String SDCARD_FJ_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "fj/";
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = SDCARD_RASK_DATA_PATH + "data/video_config.xml";
	/** �ֻ���ִ��������·������sitelaw */
	public static final String SITELAWRECORD_PATH = SDCARD_RASK_DATA_PATH + "data/sitelaw";

	/** �ֻ���ִ������ͨ���·������ZFBST */
	public static final String LAWKNOWALL_PATH = SDCARD_RASK_DATA_PATH + "fj/ZFBST";

	/** �ֻ��˻������ִ���ֲ���·�� */
	public static final String HJJCZFSC_PATH = SDCARD_RASK_DATA_PATH + "fj/�������ִ���ֲ�";

	/** �ֻ����������ݵ���ʱ�ļ�Ŀ¼����DataTemp */
	public static final String SDCARD_TEMP_PATH = BASE_PATH + "DataTemp/";

	// -----------------------���Ի���--------------------------

	// -----------------------���Ի���--------------------------
	public static final String QIANDAO_URL = "http://119.164.253.236:8184/JiNanhuanbaoms/task/InsAttendancebook.do";
	public static final String PAIWU_URL = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Selinformation.do";
	public static final String PAIWUZHILI_URL = "http://192.168.15.66:8080/JiNanhuanbaoms/task/Pollutanttreatment.do";
	// -----------------------��ʽ����--------------------------
	// public static final String BASE_URL = "http://192.168.1.171:8080";
	/** ���ز��Ե�ַ */
	public static final String BASE__TEST_URL = "http://192.168.1.151:8080";
	/** ��˾��������ַ */
	public static final String BASE_URL = "http://171.8.66.103:8184";
	/** ���α��ز��� */
//	public static final String BASE_URL_JINNAN = "http://192.168.120.237:8184";
	/**237���ϻ����ַ�������ַ����*/
//	public static final String BASE_URL_JINNAN = "http://119.164.253.237:8184";
	/**237ͼƬ����·��  ��ʽ*/
//	public static final String IMG_URL_JINAN = "http://119.164.253.237:8112/jinantupian";
	
//	/**���Է�������ַ ����*/
//	public static final String BASE_URL_JINNAN = "http://192.168.120.237:8184";
	/**236���ϻ����ַ�������ַ ��ʽ*/
	public static final String BASE_URL_JINNAN = "http://119.164.253.236:8184";
//	/**236	ͼƬ��ʽ·�� ��ʽ*/
	public static final String IMG_URL_JINAN = "http://119.164.253.236:8033/jinan";
	
	
//	/**236	�ĵ�����������ʽ·�� ��ʽ*/
	public static final String DOC_URL_JINAN = "http://119.164.253.236:8033/zhidu";
	public static final String SHENGCHANSHESHI_URL = BASE_URL + "/JiNanhuanbaoms/task/Operationofproduction.do";
	public static final String CUIBAN_URL = BASE_URL + "/JiNanhuanbaoms/task/Presssth.do";
	public static final String CHUZHIFANKUI_URL = BASE_URL + "/JiNanhuanbaoms/task/Handlingfeedback.do";
	public static final String BASE_LOGIN_URL = "http://171.8.66.103:8033";
	public static final String LONGIN_URL = BASE_LOGIN_URL + "/WebService/RM_WebServiceForJava.asmx";
	public static final String GETDBTASK_URL = BASE_URL + "/JiNanhuanbaoms/task/selDaochuli.do";
	public static final String DBUPLOAD_URL = BASE_URL + "/JiNanhuanbaoms/task/shangChuanFanKiu.do";
	public static final String YIWANCHENG_URL = BASE_URL + "/JiNanhuanbaoms/task/selBeProcessed.do";
	public static final String DAICHILI_URL = BASE_URL + "/JiNanhuanbaoms/task/selBeProcessed.do";
	public static final String WURANYUANXX_URL = BASE_URL + "/JiNanhuanbaoms/task/selWuranyuan.do";
	public static final String XCRW_URL = BASE_URL + "/JiNanhuanbaoms/task/insertaskFeedback.do";
	public static final String SHANGBAO_URL = BASE_URL + "/JiNanhuanbaoms/task/shangbao.do";
	public static final String XCJL_URL = BASE_URL + "/JiNanhuanbaoms/task/Patrolrecord.do";
	public static final String LONGIN_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/user/login.do";//��½
	public static final String SHANGBAO_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/patrol/save.do";//Ѳ���ϱ�
	public static final String GETXUNCHAOBJECT_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/patrolObject/list.do";//��ȡѲ�����
	public static final String GETDBTASK_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/work/list.do";//��ȡ��������
	public static final String GETXFTASK_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/work/task/list.do";//��ȡ�·�����
	public static final String SHANGCHUANFANKUI_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/patrol/update.do";//�ϴ�����
	public static final String DOCSEARCH_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/grid/seleteByFileName";//�ĵ�����
	public static final String XUNCHAJILU_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/work.do";//Ѳ���¼
	public static final String QIANSHOU_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/task/process.do";//����ǩ��/������
	public static final String YANSHIDETAIL_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/task/send.do";//��ʱ��������ӿ�
	public static final String YANSHIUPLOAD_URL_JINAN = BASE_URL_JINNAN + "/JiNanhuanbaoms/delty/insert.do";//��ʱ��������ӿ�
	public static final String SIJI_XUCHAYUAN_URL=BASE_URL_JINNAN+"/JiNanhuanbaoms/work.do";//
	public static final String SHANGBAO_URL_JINAN_SAVE=BASE_URL_JINNAN+"/JiNanhuanbaoms/patrol/update";//
	public static final String GETPATROPLAN_URL_JINAN=BASE_URL_JINNAN+"/JiNanhuanbaoms/patrolPlan/getPatrolPlanListForApp.do";//��ȡѲ��ƻ�
	public static final String JUDGESESSION_URL_JINAN=BASE_URL_JINNAN+"/JiNanhuanbaoms/patrolPlan/validateSession.do";//�ж�session�Ƿ���Ч

	
	public static final String APK_CODE_URL_JINAN = "http://119.164.253.236:8112/apk/version.xml";//����apk�Զ����������ļ�
	public static final String APK_DOWN_URL_JINAN = "http://119.164.253.236:8112/apk/Meshing.apk";//����apk�Զ�����·��
	// -----------------------��ʽ����--------------------------
	//��̨�ϴ���γ�Ƚӿ�
	public static final String SCJW = BASE_URL_JINNAN + "/JiNanhuanbaoms/user/insertTrajectory";//�ϴ���γ��
	//ǩ���ϴ� ��ַ
	public static final String QIANDAO_SIGNIN_URL=BASE_URL_JINNAN+"/JiNanhuanbaoms/signed/save.do";
	//ǩ���㼣 ��ȡ���ݵ�ַ
	public static final String ZUJI_URL=BASE_URL_JINNAN+"/JiNanhuanbaoms/signed/totalSigned";
	public static final String ZUJI_IMG_URL=BASE_URL_JINNAN+"/JiNanhuanbaoms/signed/signedImg";
	/* ���ӷ�����·�� */
	/** �ֻ�������WebService��·�� */
	public static final String WEBSERVICE_URL = "/JiNan_WebService/services";
	/** �ֻ�������OA��WebService��·�� */
	public static final String OAWEBSERVICE_URL = "/MobileEnforcement/WebService/OAWebService.asmx";
	/** �����ռ� */
	public static final String NAMESPACE = "http://tempuri.org/";

	public static final String Air_Child_Station_Url = "http://119.164.253.236:8112/JNgridGIS/Ashx/getSubStationAirQuality.ashx";
}
