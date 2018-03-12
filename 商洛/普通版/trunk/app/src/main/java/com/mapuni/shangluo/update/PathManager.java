/**
 *
 */

package com.mapuni.shangluo.update;

import android.os.Environment;

public class PathManager {
	/* 公共路径 */
	/** 系统部署文件的跟目录mapuni文件夹路径 */
	public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/mapuni/";
	/** 系统部署文件MobileEnforcement文件夹路径 */
	public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "MobileWangGeHua/";
	
	/** 手机端更新apk的本地目录路径，即AutoUpdate文件夹 */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate";
	/** 手机端更新apk的本地文件路径，即update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "AutoUpdate/shangluo.apk";

	/**
	 * baseurl
	 */
	public static final String BASE_URL="http://192.168.120.182:8080";


	/**
	 * 检查更新
	 */
	public static final String VERSION_URL=BASE_URL+"/AppUpdate/version.xml";

}
