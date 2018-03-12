package cn.com.mapuni.meshing.base.interfaces;

import android.os.Environment;

/**
 * FileName: PathManager.java Description:应用常量管理
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-7 下午01:10:48
 */
public class PathManager {
	/* 公共路径 */
	/** 系统部署文件的跟目录mapuni文件夹路径 */
	public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/mapuni/";
	/** 系统部署文件MobileEnforcement文件夹路径 */
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "Meshing/";

	/* 后台路径 */
	/** 后台verson.xml文件路径 */
	public static final String APK_CODE_URL = "/AutoUpdate/app/verson.xml";// 版本信息URL后缀
	/** 后台config.xml文件路径 */
	public static final String CONFIG_CODE_URL = "/AutoUpdate/config/config.xml";
	/** 后台video_config.xml文件路径 */
	public static final String VIDEOCOFIG_CODE_URL = "/AutoUpdate/video_config/video_config.xml";
	/** 后台存放apk更新的路径 */
	public static final String APK_DOWN_URL = "/AutoUpdate/app/MobileEnforcement.zip";
	/** 后台存放数据库版本的verson.xml文件路径 */
	public static final String DB_CODE_URL = "/AutoUpdate/db/verson.xml";
	/** 后台存放手机端手机的文件路径 */
	public static final String DE_DOWN_URL = "/AutoUpdate/db/MobileEnforcement.zip";
	/** 后台存放任务附件的路径，即TaskExecute文件夹路径 */
	public static final String JCJLRWFJ_DOWN_URL = "/MobileEnforcement/Attach/TaskExecute/";

	/* 手机端路径 */
	/** 手机端人脸识别存放采集的图像 */
	public static final String SDCARD_UserPhoto_LOCAL_PATH = BASE_PATH + "userphoto";
	/** 手机端更新apk的本地目录路径，即AutoUpdate文件夹 */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate";
	/** 手机端更新apk的本地文件路径，即update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate/update.apk";
	/** 手机端数据库MobileEnforcement.db文件存放路径 */
	public static final String SDCARD_DB_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/MobileEnforcement.db";
	/** 手机端config文件路径 */
	public static final String SDCARD_CONFIG_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/config.xml";
	/**
	 * 历史执法记录放置的路径
	 */
	public static final String ROOT_VIDEO_PATH = PathManager.BASE_PATH + "historyVideo/";
	/** 手机端data文件目录 */
	public static final String SDCARD_DATA_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data";
	/** 手机端日志文件目录 */
	public static final String SDCARD_LOG_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/log";
	/** 手机端存放备份数据库的文件目录，即datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/datebaseCopy/";
	/** 手机端存放备份数据库的文件路径，即MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/datebaseCopy/MobileEnforcement.db";
	/** 手机端同步附件存放的本地路径，即fj */
	public static final String SDCARD_FJ_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "fj/";
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = SDCARD_RASK_DATA_PATH + "data/video_config.xml";
	/** 手机端执法文书存放路径，即sitelaw */
	public static final String SITELAWRECORD_PATH = SDCARD_RASK_DATA_PATH + "data/sitelaw";

	/** 手机端执法百事通存放路径，即ZFBST */
	public static final String LAWKNOWALL_PATH = SDCARD_RASK_DATA_PATH + "fj/ZFBST";

	/** 手机端环境监察执法手册存放路径 */
	public static final String HJJCZFSC_PATH = SDCARD_RASK_DATA_PATH + "fj/环境监察执法手册";

	/** 手机端下载数据的临时文件目录，即DataTemp */
	public static final String SDCARD_TEMP_PATH = BASE_PATH + "DataTemp/";
     
	//-----------------------测试环境--------------------------
	
	//-----------------------测试环境--------------------------
	public static final String  QIANDAO_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/InsAttendancebook.do";
	public static final String  PAIWU_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Selinformation.do";
	public static final String  PAIWUZHILI_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Pollutanttreatment.do";
	public static final String QYKH_URL="http://192.168.15.66:8080/JiNanhuanbaoms/task/Regionalassessment.do";
	//-----------------------正式环境--------------------------
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
	//-----------------------正式环境--------------------------

	public static final String JINAN_URL = "http://119.164.253.236:8112/AppService/JLWebServiceService.asmx";//服务器接口界面地址
//	public static final String JINAN_URL = "http://192.168.15.48:8071/JNWebserviceForApp/JLWebServiceService.asmx";//服务器接口界面地址
	public static final String LOGIN_URL = "/Login";//登录接口
	public static final String getAllEnterpriseInfo = "/getAllEnterpriseInfo";//获取污染源信息
	public static final String getDepCheckInfo="/GetDeptCheckInfo";//获取部门考核信息列表
	public static final String getDepCheckDetail="/GetDeptCheckDetail";//获取部门考核详情信息
    public static final String getGridRecodeInfo="/getProblemRecord";//获取网格化信心
	public static final String getSubStationAirDataMH="/getSubStationAirDataMH";//获取空气质量
	public static final String getPollutionSourceInfo="/getHourAlarmRateData";//污染源
	public static final String getAllSolidmentSpotsMonitorInfo="/getAllSolidmentSpotsMonitor";//渣土场实时监测数据
	public static final String getAllArtRoadMonitorDataInfo="/getAllArtRoadMonitorData";//主干道扬尘实时监测数据
	public static final String getAllContSpotsMonitorDataForAppInfo="/getAllContSpotsMonitorDataForApp";//建筑扬尘
	public static final String getAllOneHourZTCloctionArrayInfo="/getAllOneHourZTCloctionArray";//渣土车GPS
	public static final String getAllBareLandInfo="/getAllBareLandInfo";//建成区可绿化裸露土地
	public static final String getAlarmList="http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReport";//获取超标预警list
	public static final String getAlarmDetail="http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReportDetail";//获取超标预警detail
	public static final String getHomeAQI="http://119.164.253.237:8033/Service/AppService.asmx/GetAQIOfAllSite";//获取s首页aqi等数据
	public static final String getHomeWeather="http://www.micromap.com.cn:8080/epservice/v1.2/api/weather/getAll/济南/0/0?token=YFJYeRKQouE0bWylekXl";//获取首页天气数据
	public static final String IMG_URL_JINAN = "http://119.164.253.236:8033/jinan";//济南图片路径前缀
	public static final String APK_CODE_URL_JINAN = "http://119.164.253.236:8112/apk/version_air.xml";//济南apk自动更新配置文件
	public static final String APK_DOWN_URL_JINAN = "http://119.164.253.236:8112/apk/Meshing_Air.apk";//济南apk自动更新路径


	
	/* 链接服务器路径 */
	/** 手机端链接WebService的路径 */
	public static final String WEBSERVICE_URL = "/JiNan_WebService/services";
	/** 手机端链接OA的WebService的路径 */
	public static final String OAWEBSERVICE_URL = "/MobileEnforcement/WebService/OAWebService.asmx";
	/** 命名空间 */
	public static final String NAMESPACE = "http://tempuri.org/";
	
	
	
}
