/**
 * 
 */
package com.mapuni.android.base.interfaces;

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
	public static final String BASE_PATH = Environment
			.getExternalStorageDirectory() + "/mapuni/";
	/** 系统部署文件MobileEnforcement文件夹路径 */
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH
			+ "MobileEnforcement/";

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
	public static final String SDCARD_UserPhoto_LOCAL_PATH = BASE_PATH
			+ "userphoto";
	/** 手机端更新apk的本地目录路径，即AutoUpdate文件夹 */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "AutoUpdate";
	/** 手机端更新apk的本地文件路径，即update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "AutoUpdate/update.apk";
	/** 手机端数据库MobileEnforcement.db文件存放路径 */
	public static final String SDCARD_DB_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/MobileEnforcement.db";
	/** 手机端config文件路径 */
	public static final String SDCARD_CONFIG_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/config.xml";
	/** 手机端data文件目录 */
	public static final String SDCARD_DATA_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data";
	/** 手机端日志文件目录 */
	public static final String SDCARD_LOG_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/log";
	/** 手机端存放备份数据库的文件目录，即datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/datebaseCopy/";
	/** 手机端存放备份数据库的文件路径，即MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "data/datebaseCopy/MobileEnforcement.db";
	/** 手机端同步附件存放的本地路径，即fj */
	public static final String SDCARD_FJ_LOCAL_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/";
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = SDCARD_RASK_DATA_PATH
			+ "data/video_config.xml";
	/** 手机端执法文书存放路径，即sitelaw */
	public static final String SITELAWRECORD_PATH = SDCARD_RASK_DATA_PATH
			+ "data/sitelaw";

	/** 手机端执法百事通存放路径，即ZFBST */
	public static final String LAWKNOWALL_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/ZFBST";
	
	/** 手机端环境监察执法手册存放路径*/
	public static final String HJJCZFSC_PATH = SDCARD_RASK_DATA_PATH
			+ "fj/环境监察执法手册";

	/** 手机端下载数据的临时文件目录，即DataTemp */
	public static final String SDCARD_TEMP_PATH = BASE_PATH + "DataTemp/";

	/* 链接服务器路径 */
	/** 手机端链接WebService的路径 */
	public static final String WEBSERVICE_URL = "/WebService/MobileEnforcementWebService.asmx";
	/** 手机端链接OA的WebService的路径 */
	public static final String OAWEBSERVICE_URL = "/MobileEnforcement/WebService/OAWebService.asmx";
	/** 命名空间 */
	public static final String NAMESPACE = "http://tempuri.org/";
}
