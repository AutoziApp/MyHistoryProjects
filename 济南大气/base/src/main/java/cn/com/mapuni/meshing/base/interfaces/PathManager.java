package cn.com.mapuni.meshing.base.interfaces;

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
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "Meshing/";

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
	public static final String SDCARD_CopyDB_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/datebaseCopy/MobileEnforcement.db";
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
     
	//-----------------------���Ի���--------------------------
	
	//-----------------------���Ի���--------------------------
	public static final String  QIANDAO_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/InsAttendancebook.do";
	public static final String  PAIWU_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Selinformation.do";
	public static final String  PAIWUZHILI_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Pollutanttreatment.do";
	public static final String QYKH_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Regionalassessment.do";
	//-----------------------��ʽ����--------------------------
	public static final String  BASE_URL = "http://171.8.66.103:8184";
	public static final String SHENGCHANSHESHI_URL=BASE_URL+"/JiNanhuanbaoms/task/Operationofproduction.do";
	public static final String CUIBAN_URL=BASE_URL+"/JiNanhuanbaoms/task/Presssth.do";
	public static final String CHUZHIFANKUI_URL=BASE_URL+"/JiNanhuanbaoms/task/Handlingfeedback.do";
	public static final String  BASE_LOGIN_URL = "http://171.8.66.103:8033";
	public static final String LONGIN_URL  =BASE_LOGIN_URL + "/WebService/RM_WebServiceForJava.asmx";
	public static final String GETDBTASK_URL=BASE_URL+"/JiNanhuanbaoms/task/selDaochuli.do";
	public static final String DBUPLOAD_URL=BASE_URL+"/JiNanhuanbaoms/task/shangChuanFanKiu.do";
	public static final String YIWANCHENG_URL=BASE_URL+"/JiNanhuanbaoms/task/selBeProcessed.do";
	public static final String DAICHILI_URL=BASE_URL+"/JiNanhuanbaoms/task/selBeProcessed.do";
	public static final String WURANYUANXX_URL=BASE_URL+"/JiNanhuanbaoms/task/selWuranyuan.do";
	public static final String XCRW_URL=BASE_URL+"/JiNanhuanbaoms/task/insertaskFeedback.do";
	public static final String XCJL_URL=BASE_URL+"/JiNanhuanbaoms/task/Patrolrecord.do";
	//-----------------------��ʽ����--------------------------

	public static final String JINAN_URL = "http://119.164.253.236:8112/AppService/JLWebServiceService.asmx";//�������ӿڽ����ַ
//	public static final String JINAN_URL = "http://192.168.15.48:8071/JNWebserviceForApp/JLWebServiceService.asmx";//�������ӿڽ����ַ
	public static final String LOGIN_URL = "/Login";//��¼�ӿ�
	public static final String getAllEnterpriseInfo = "/getAllEnterpriseInfo";//��ȡ��ȾԴ��Ϣ
	public static final String getDepCheckInfo="/GetDeptCheckInfo";//��ȡ���ſ�����Ϣ�б�
	public static final String getDepCheckDetail="/GetDeptCheckDetail";//��ȡ���ſ���������Ϣ
    public static final String getGridRecodeInfo="/getProblemRecord";//��ȡ��������
	public static final String getSubStationAirDataMH="/getSubStationAirDataMH";//��ȡ��������
	public static final String getPollutionSourceInfo="/getHourAlarmRateData";//��ȾԴ
	public static final String getAllSolidmentSpotsMonitorInfo="/getAllSolidmentSpotsMonitor";//������ʵʱ�������
	public static final String getAllArtRoadMonitorDataInfo="/getAllArtRoadMonitorData";//���ɵ��ﳾʵʱ�������
	public static final String getAllContSpotsMonitorDataForAppInfo="/getAllContSpotsMonitorDataForApp";//�����ﳾ
	public static final String getAllOneHourZTCloctionArrayInfo="/getAllOneHourZTCloctionArray";//������GPS
	public static final String getAllBareLandInfo="/getAllBareLandInfo";//���������̻���¶����
	public static final String getAlarmList="http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReport";//��ȡ����Ԥ��list
	public static final String getAlarmDetail="http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReportDetail";//��ȡ����Ԥ��detail
	public static final String getHomeAQI="http://119.164.253.237:8033/Service/AppService.asmx/GetAQIOfAllSite";//��ȡs��ҳaqi������
	public static final String getHomeWeather="http://www.micromap.com.cn:8080/epservice/v1.2/api/weather/getAll/����/0/0?token=YFJYeRKQouE0bWylekXl";//��ȡ��ҳ��������
	public static final String IMG_URL_JINAN = "http://119.164.253.236:8033/jinan";//����ͼƬ·��ǰ׺
	public static final String APK_CODE_URL_JINAN = "http://119.164.253.236:8112/apk/version_air.xml";//����apk�Զ����������ļ�
	public static final String APK_DOWN_URL_JINAN = "http://119.164.253.236:8112/apk/Meshing_Air.apk";//����apk�Զ�����·��


	
	/* ���ӷ�����·�� */
	/** �ֻ�������WebService��·�� */
	public static final String WEBSERVICE_URL = "/JiNan_WebService/services";
	/** �ֻ�������OA��WebService��·�� */
	public static final String OAWEBSERVICE_URL = "/MobileEnforcement/WebService/OAWebService.asmx";
	/** �����ռ� */
	public static final String NAMESPACE = "http://tempuri.org/";
	
	
	
}
