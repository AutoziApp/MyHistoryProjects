package com.mapuni.android.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.frontia.FrontiaApplication;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.user.BaseUser;
import com.mapuni.android.user.BoardLeadership;
import com.mapuni.android.user.DeputyLeader;
import com.mapuni.android.user.ExecuteLeader;
import com.mapuni.android.user.ExecuteMan;
import com.mapuni.android.user.OfficeMan;

/**
 * FileName: Global.java Description: 系统全局应用类
 * 
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 下午03:44:43
 */
// wsc public class Global extends Application {
public class Global extends FrontiaApplication {
	/** Global实例化对象 */
	private static Global GlobalInstance;

	/** 系统部署文件的跟目录mapuni文件夹路径 */
	public final String BASE_PATH = PathManager.BASE_PATH;

	/** 系统部署文件MobileEnforcement文件夹路径 */
	public static final String SDCARD_RASK_DATA_PATH = PathManager.SDCARD_RASK_DATA_PATH;
	/** 后台verson.xml文件路径 */
	public static final String APK_CODE_URL = PathManager.APK_CODE_URL;
	/** 后台config.xml文件路径 */
	public static final String CONFIG_CODE_URL = PathManager.CONFIG_CODE_URL;
	/** 后台video_config.xml文件路径 */
	public static final String VIDEOCOFIG_CODE_URL = PathManager.VIDEOCOFIG_CODE_URL;
	/** 后台存放apk更新的路径 */
	public static final String APK_DOWN_URL = PathManager.APK_DOWN_URL;
	/** 后台存放数据库版本的verson.xml文件路径 */
	public static final String DB_CODE_URL = PathManager.DB_CODE_URL;
	/** 后台存放手机端手机的文件路径 */
	public static final String DE_DOWN_URL = PathManager.DE_DOWN_URL;
	/** 后台存放任务附件的路径，即TaskExecute文件夹路径 */
	public static final String JCJLRWFJ_DOWN_URL = PathManager.JCJLRWFJ_DOWN_URL;
	/** 手机端更新apk的本地目录路径，即AutoUpdate文件夹 */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = PathManager.SDCARD_AutoUpdate_LOCAL_PATH;
	/** 手机端更新apk的本地文件路径，即update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = PathManager.SDCARD_APK_LOCAL_PATH;
	/** 手机端数据库MobileEnforcement.db文件存放路径 */
	public static final String SDCARD_DB_LOCAL_PATH = PathManager.SDCARD_DB_LOCAL_PATH;
	/** 手机端config文件路径 */
	public static final String SDCARD_CONFIG_LOCAL_PATH = PathManager.SDCARD_CONFIG_LOCAL_PATH;
	/** 手机端data文件目录 */
	public static final String SDCARD_DATA_LOCAL_PATH = PathManager.SDCARD_DATA_LOCAL_PATH;
	/** 手机端日志文件目录 */
	public static final String SDCARD_LOG_LOCAL_PATH = PathManager.SDCARD_LOG_LOCAL_PATH;
	/** 手机端存放备份数据库的文件目录，即datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = PathManager.SDCARD_datebaseCopy_LOCAL_PATH;
	/** 手机端存放备份数据库的文件路径，即MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = PathManager.SDCARD_CopyDB_LOCAL_PATH;
	/** 手机端同步附件存放的本地路径，即fj */
	public static final String SDCARD_FJ_LOCAL_PATH = PathManager.SDCARD_FJ_LOCAL_PATH;
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = PathManager.SDCARD_VIDEODATA_CONFIG;
	/** 手机端执法文书存放路径，即sitelaw */
	public static final String SITELAWRECORD_PATH = PathManager.SITELAWRECORD_PATH;
	/** 手机端执法百事通存放路径，即ZFBST */
	public static final String LAWKNOWALL_PATH = PathManager.LAWKNOWALL_PATH;
	/** 手机端法律法规环境监察执法手册存放路径 */
	public static final String HJJCZFSC_PATH = PathManager.HJJCZFSC_PATH;

	/** 手机端下载数据的临时文件目录，即DataTemp */
	public static final String SDCARD_TEMP_PATH = PathManager.SDCARD_TEMP_PATH;

	/** 手机端链接WebService的路径 */
	public static final String WEBSERVICE_URL = PathManager.WEBSERVICE_URL;
	/** 手机端链接OA的WebService的路径 */
	public static final String OAWEBSERVICE_URL = PathManager.OAWEBSERVICE_URL;
	/** 命名空间 */
	public static final String NAMESPACE = PathManager.NAMESPACE;
	/** 默认的链接服务器的地址 */
	//private String url = "http://192.168.5.112";
	private String url = "http://192.168.15.48:8189";
	
	/** app 登陆状态 */
	public static final int APP_LOGIN_DURING = 200;
	private int appStatus = -1;
	/** app正常运行状态 */
	public static final int APP_RUN_NORMAL = 201;

	/** app 访问数据库状态 */
	public static final int APP_SQLITE_ACCESSING = 202;

	/** app 联网状态 */
	public static final int APP_NETWORK_CONNECTION = 203;

	/** app退出状态 */
	public static final int APP_EXIT_DURING = 204;

	/** 当前Id */
	private String CurrentID = null;
	/** 设置用户所属单位 */
	private String userUnit = "鸡西市环境监察局";
	private String userUnitforquestion = "鸡西市环境监察局";
	/** 设置用户名 */
	private String username = "";
	/** 设置用户名 id */
	private String userid = "";
	/** 设置用户登陆密码 */
	private String password = "";
	/** 设置用户真实名字 */
	private String realName = "";
	/** 设置用户所属地区 */
	private String areaCode = "";

	/** 在map地图上是否进行现场执法了 */
	private boolean mapXczfAddHistory = false;
	/** 通讯录页面查询加条件 */
	private boolean searchCondition = false;
	/** 设置系统类型 */
	private String systype = "YDZF";
	/** 设置任务提醒的显示 */
	private boolean showAlter;
	/** 设置当前接入点ID */
	private String currentApnId;
	/** 是否更新apn，默认为false */
	private boolean updataapn = false;

	/** 设置用户行政区划 */
	private String administrative = "";
	/** 用户所属部门 */
	private String depId = "";
	/** 用户权限集合 */
	private HashMap<String, Object> authoritylist = new HashMap<String, Object>();
	/** 设置服务器名称 */
	private String Systemname = "";
	/** 当前登录用户的权限集合 */
	private String[] currentUserAuthority = null;
	/** 页面传递数据的Bundle对象 */
	private Bundle bundle = null;

	/** 系统权限表格名称 */
	public static final String SystemConfig = "Mob_SystemConfig Order By sort";
	/***/
	private int menuHeight;
	/** 当前是否有同步任务 */
	public static Boolean IsDataSync = false;
	/** 标志使用新效果（向下展开的ListView或是旧有的弹出Dialog效果展示） */
	public Boolean useNewStyleBoolean = true;
	/** 当前id */
	private String itemId;

	/** true表示配置文件布局，false表示数据库读取布局 */
	public static final boolean isTestStyle = true;

	/** 标识数据库中不同类别显示 */
	private HashMap<String, Object> type_DB;
	/** 是否显示右上角菜单中的法律法规，应急手册，全局查找 */
	public boolean isShowGd = false;
	/** 任务状态是否有改变标识，有改变则在任务列表类的生命周期onrestart函数刷新数据 */
	private boolean TaskStateChange = false;

	/**
	 * Description: 获取当前对象的ID
	 * 
	 * @return 当前对象的ID String
	 * @author 张信元 Create at: 2012-12-5 下午04:52:24
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * Description: 设置当前对的ID
	 * 
	 * @param itemId
	 *            当前对象的ID void
	 * @author 张信元 Create at: 2012-12-5 下午04:53:05
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * Description:
	 * 
	 * @return int
	 * @author 张信元 Create at: 2012-12-5 下午04:53:35
	 */
	public int getMenuHeight() {
		return menuHeight;
	}

	/**
	 * Description:
	 * 
	 * @param menuHeight
	 *            void
	 * @author 张信元 Create at: 2012-12-5 下午04:54:19
	 */
	public void setMenuHeight(int menuHeight) {
		this.menuHeight = menuHeight;
	}

	/**
	 * Description: 获取当前登录用户所属的部门ID
	 * 
	 * @return 部门ID String
	 * @author 张信元 Create at: 2012-12-5 下午04:55:39
	 */
	public String getDepId() {
		return depId;
	}

	/**
	 * Description: 设置当前登录用户所属部门的ID
	 * 
	 * @param depId
	 *            部门ID值 void
	 * @author 张信元 Create at: 2012-12-5 下午04:56:08
	 */
	public void setDepId(String depId) {
		this.depId = depId;
		baseUser.setDepID(depId);
	}

	/**
	 * Description: 获取Global对象
	 * 
	 * @return Global实例化对象 Global
	 * @author 张信元 Create at: 2012-12-5 下午04:59:41
	 */
	public static Global getGlobalInstance() {
		return GlobalInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		GlobalInstance = this;
		
		

		/* Volley配置 */
		// 建立Volley的Http请求队列
		volleyQueue = Volley.newRequestQueue(getApplicationContext());
		
		
		ExceptionManager crashHandler = ExceptionManager.getInstance();
		crashHandler.init(this);


	}

	// 开放Volley的HTTP请求队列接口
	public static RequestQueue getRequestQueue() {
		return volleyQueue;
	}


	/**
	 * Description: 获取当前用户系统权限
	 * 
	 * @return 用户权限 String[]
	 * @author 张信元 Create at: 2012-12-5 下午05:00:16
	 */
	public String[] getCurrentUserAuthority() {
		return currentUserAuthority;
	}

	/**
	 * Description: 设置当前系统权限
	 * 
	 * @param s
	 *            用户权限 void
	 * @author Administrator Create at: 2012-12-5 下午05:00:46
	 */
	public void setCurrentUserAuthority(String[] s) {
		currentUserAuthority = s;
	}

	/**
	 * Description: 获取当前Bundle对象
	 * 
	 * @return Bundle对象 Bundle
	 * @author 张信元 Create at: 2012-12-5 下午05:01:03
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * Description: 设置当前Bundle对象
	 * 
	 * @param bundle
	 *            Bundle对象 void
	 * @author 张信元 Create at: 2012-12-5 下午05:01:37
	 */
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * Description: 根据文件的名字（不要后缀）来获取文件的Bitpmap
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            Bitpmap名字
	 * @return 返回所需要的darwable Bitmap
	 * @author 张信元 Create at: 2012-12-5 下午05:02:07
	 */
	public static Bitmap getRes(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	/**
	 * Description: 加载本地图片
	 * 
	 * @param name
	 *            图片名称
	 * @return Bitmap对象 Bitmap
	 * @author 张信元 Create at: 2012-12-5 下午05:03:22
	 */
	public static Bitmap getLoacalBitmap(String name) {
		try {
			String url = "/sdcard/mapuni/MobileEnforcement/image/" + name;
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Description: 获取服务器地址
	 * 
	 * @return 服务器地址 String
	 * @author 张信元 Create at: 2012-12-5 下午05:07:02
	 */
	public String getSystemurl() {
		return url;
	}

	/**
	 * Description: 设置服务器地址
	 * 
	 * @param url
	 *            服务器地址 void
	 * @author 张信元 Create at: 2012-12-5 下午05:07:21
	 */
	public void setSystemurl(String url) {

		this.url = url;
	}

	/**
	 * Description: 获取系统名称
	 * 
	 * @return 系统名称 String
	 * @author 张信元 Create at: 2012-12-5 下午05:07:53
	 */
	public String getSystemname() {
		return Systemname;
	}

	/**
	 * Description: 设置系统名称
	 * 
	 * @param systemname
	 *            系统名称 void
	 * @author 张信元 Create at: 2012-12-5 下午05:42:58
	 */
	public void setSystemname(String systemname) {
		Systemname = systemname;
	}

	/**
	 * Description: 设置当前ID
	 * 
	 * @param CurrentID
	 *            ID值 void
	 * @author 张信元 Create at: 2012-12-5 下午05:43:21
	 */
	public void setCurrentID(String CurrentID) {
		this.CurrentID = CurrentID;
	}

	/**
	 * Description: 获取当前ID
	 * 
	 * @return ID值 String
	 * @author 张信元 Create at: 2012-12-5 下午05:44:03
	 */
	public String getCurrentID() {
		return CurrentID;
	}

	/**
	 * Description: 获取当前用户的权限集合
	 * 
	 * @return 权限集合 ArrayList<HashMap<String,Object>>
	 * @author 张信元 Create at: 2012-12-5 下午05:44:30
	 */
	public HashMap<String, Object> getAuthoritylist() {
		if (authoritylist == null || authoritylist.size() == 0) {
			// SharedPreferences sp =
			// PreferenceManager.getDefaultSharedPreferences(this);
			// SharedPreferences sp1 = getSharedPreferences("users",
			// MODE_WORLD_READABLE);
			// if(sp != null){
			// String userId = sp.getString("userID", "");
			// if(!"".equals(userId)){
			// setUserid(userId);
			// authoritylist = new Users().checkAuthor();
			// setAuthoritylist(authoritylist);
			// }
			// }
		}
		return authoritylist;
	}

	/**
	 * Description: 设置当前用户的权限集合
	 * 
	 * @param authoritylist
	 *            权限集合值 void
	 * @author 张信元 Create at: 2012-12-5 下午05:45:03
	 */
	public void setAuthoritylist(HashMap<String, Object> authoritylist) {
		this.authoritylist = authoritylist;
	}

	/**
	 * Description: 获取当前登录用户的名称
	 * 
	 * @return 用户名称 String
	 * @author 张信元 Create at: 2012-12-5 下午05:45:36
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 获取用户所属单位
	 * 
	 * @return
	 */
	public String getUserUnit() {
		return userUnit;
	}

	/**
	 * Description: 设置当前用户的名称
	 * 
	 * @param username
	 *            用户名称 void
	 * @author 张信元 Create at: 2012-12-5 下午05:45:58
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Description: 获取当前用户所属地区
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * Description: 设置当前用户所属地区
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		baseUser.setSyncDataRegionCode(areaCode);
	}

	/**
	 * Description: 获取通讯录页面查询加条件的标志 true ：加条件
	 */
	public boolean isSearchCondition() {
		return searchCondition;
	}

	/**
	 * Description: 设置通讯录页面查询加条件的标志 true ：加条件
	 */

	public void setSearchCondition(boolean searchCondition) {
		this.searchCondition = searchCondition;
	}

	/**
	 * Description: 获取当前在map地图上是否现场执法了 true ： 在执法记录中更新
	 */
	public boolean isMapXczfAddHistory() {
		return mapXczfAddHistory;
	}

	/**
	 * Description: 设置当前在map地图上是否现场执法了 true ： 在执法记录中更新
	 */
	public void setMapXczfAddHistory(boolean mapXczfAddHistory) {
		this.mapXczfAddHistory = mapXczfAddHistory;
	}

	/**
	 * Description: 获取当前登录用户的ID
	 * 
	 * @return 用户ID值 String
	 * @author 张信元 Create at: 2012-12-5 下午05:46:20
	 */
	public String getUserid() {
		return userid;
	}

	private String zw = "";

	public String getUserZW() {
		return zw;
	}

	public void setUserZW(String zw) {
		this.zw = zw;
	}

	/**
	 * 获取用户真实名字
	 * 
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * Description: 这是当前用户的ID
	 * 
	 * @param userid
	 *            用户的ID void
	 * @author Administrator Create at: 2012-12-5 下午05:46:50
	 */
	public void setUserid(String userid) {
		this.userid = userid;
		baseUser.setUserID(userid);
	}

	/**
	 * Description:获取用户登陆密码
	 * 
	 * @return
	 * @author wanglg
	 * @Create at: 2013-4-17 上午11:07:58
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Description: 设置登陆密码
	 * 
	 * @param password
	 * @author wanglg
	 * @Create at: 2013-4-17 上午11:08:12
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Description: 获取任务提醒
	 * 
	 * @return true：提醒，false：不提醒 boolean
	 * @author 张信元 Create at: 2012-12-5 下午05:47:14
	 */
	public boolean getShowAlter() {
		return showAlter;
	}

	/**
	 * Description: 设置任务提醒
	 * 
	 * @return true：提醒，false：不提醒 boolean
	 * @author 张信元 Create at: 2012-12-5 下午05:50:25
	 */
	public void setShowAlter(boolean showAlter) {
		this.showAlter = showAlter;
	}

	/**
	 * Description: 获取当前APN的ID
	 * 
	 * @return APN的ID值 String
	 * @author 张信元 Create at: 2012-12-5 下午05:50:49
	 */
	public String getCurrentApnId() {
		return currentApnId;
	}

	/**
	 * Description: 设置当前APN的ID
	 * 
	 * @return APN的ID值 String
	 * @author 张信元 Create at: 2012-12-5 下午05:51:22
	 */
	public void setCurrentApnId(String currentApnId) {
		this.currentApnId = currentApnId;
	}

	/**
	 * Description: 获取是否更新APN
	 * 
	 * @return true：更新，false：不更新 boolean
	 * @author 张信元 Create at: 2012-12-5 下午05:51:38
	 */
	public boolean isUpdataapn() {
		return updataapn;
	}

	/**
	 * Description: 设置是否更新APN
	 * 
	 * @return true：更新，false：不更新 boolean
	 * @author 张信元 Create at: 2012-12-5 下午05:52:34
	 */
	public void setUpdataapn(boolean updataapn) {
		this.updataapn = updataapn;
	}

	/**
	 * Description: 获取当前用户所管辖的区域
	 * 
	 * @return 所管辖区域值 String
	 * @author 张信元 Create at: 2012-12-5 下午05:52:54
	 */
	public String getAdministrative() {
		return administrative;
	}

	/**
	 * Description: 设置当前用户的管辖区域
	 * 
	 * @param administrative
	 *            所管辖区域值 void
	 * @author 张信元 Create at: 2012-12-5 下午05:53:27
	 */
	public void setAdministrative(String administrative) {
		this.administrative = administrative;
		baseUser.setRegionCode(administrative);
	}

	/**
	 * Description: 获取当前系统时间
	 * 
	 * @return 时间字符串 格式为 yyyy-MM-dd HH:mm:ss String
	 * @author 张信元 Create at: 2012-12-5 下午05:54:46
	 */
	public String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Description: 获取当前系统时间
	 * 
	 * @param timeStyle
	 *            要显示的时间格式
	 * @return 时间字符串 String
	 * @author 张信元 Create at: 2012-12-5 下午05:54:46
	 */
	public String getDate(String timeStyle) {
		SimpleDateFormat format = new SimpleDateFormat(timeStyle);
		return format.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Description: 转换时间格式
	 * 
	 * @param timeStyle
	 *            要显示的时间格式
	 * @param date
	 *            时间
	 * @return 时间字符串 String
	 * @author 张信元 Create at: 2012-12-5 下午05:54:46
	 */
	public String getDate(Date date, String timeStyle) {
		SimpleDateFormat format = new SimpleDateFormat(timeStyle);
		return format.format(date);
	}

	/**
	 * Description:查询列表第一次加载的最大条数（30）
	 * 
	 * @return 条数 int
	 * @author 张信元 Create at: 2012-12-5 下午05:56:40
	 */
	public int getListNumber() {
		return 30;
		/*
		 * (Integer)DisplayUitl.getSettingValue(this, DisplayUitl.LISTLOADNUM,
		 * 30);
		 */
	}

	public HashMap<String, Object> getType_DB() {
		return type_DB;
	}

	public void setType_DB(HashMap<String, Object> type_DB) {
		this.type_DB = type_DB;
	}

	public String getSystemtype() {
		return systype;
	}

	public void setSystemtype(String systype) {
		this.systype = systype;
	}

	public boolean isShowGd() {
		return isShowGd;
	}

	// true不显示右上角菜单中的法律法规，应急手册，全局查找，false反之
	public void setShowGd(boolean isShowGd) {
		this.isShowGd = isShowGd;
	}

	public boolean getTaskStateChange() {
		return TaskStateChange;
	}

	public void setTaskStateChange(boolean TaskStateChange) {
		this.TaskStateChange = TaskStateChange;
	}

	/** 根据用户名和密码初始化全局数据 */
	public void initGlobalData(String user, String pass) {
		BaseUsers users_instance = new BaseUsers();
		ArrayList<HashMap<String, Object>> mapList = users_instance.checkUsers(user, pass);
		if (mapList != null) {
			if (mapList.size() > 0) {
				String jobTitle = mapList.get(0).get("zw").toString();
				setUserZW(jobTitle);
				initCurrentUser(jobTitle);
				setUsername(user);
				setPassword(pass);
				setUserid(mapList.get(0).get("userid").toString());
				setRealName(mapList.get(0).get("u_realname").toString());
				setDepId(mapList.get(0).get("depid").toString());
				setAdministrative(mapList.get(0).get("regioncode").toString());
				setAreaCode(mapList.get(0).get("syncdataregioncode").toString());
				// 加载当前用户的权限集
				HashMap<String, Object> authoritylist = users_instance.checkAuthor();
				setAuthoritylist(authoritylist);
				HashMap<String, Object> map = null;
				try {
					map = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select a.[RegionName],a.[POrgTitle],a.[OrgTitle] from Region a where a.[RegionCode] = '" + getAreaCode() + "'");
					userUnit = map.get("porgtitle").toString();
					userUnitforquestion = map.get("orgtitle").toString();
					if (userUnit.equals("")) {
						userUnit = map.get("regionname").toString() + "环境保护厅（局）";
					}
					OtherTools.showLog("当前用户的所属单位--" + userUnit);
				} catch (Exception e) {
					if(map != null && map.get("regionname") != null) {
						userUnit = map.get("regionname").toString() + "环境保护厅（局）";
					}
				}
			}
		}
	}

	public String getUserUnitforquestion() {
		return userUnitforquestion;
	}

	/** 根据用户名和密码初始化全局数据 */
	public void initGlobalData(ArrayList<HashMap<String, Object>> mapList) {
		BaseUsers users_instance = new BaseUsers();
		if (mapList != null) {
			if (mapList.size() > 0) {
				String jobTitle = mapList.get(0).get("ZW").toString();
				setUserZW(jobTitle);
				initCurrentUser(jobTitle);
				setUsername(mapList.get(0).get("U_LoginName").toString());
				setPassword(mapList.get(0).get("U_Password").toString());
				setUserid(mapList.get(0).get("UserID").toString());
				setRealName(mapList.get(0).get("U_RealName").toString());

				setDepId(mapList.get(0).get("DepID").toString());
				setAdministrative(mapList.get(0).get("RegionCode").toString());
				setAreaCode(mapList.get(0).get("SyncDataRegionCode").toString());
				// 加载当前用户的权限集
				HashMap<String, Object> authoritylist = users_instance.checkAuthor();
				setAuthoritylist(authoritylist);
				HashMap<String, Object> map = null;
				try {
					map = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select a.[RegionName],a.[POrgTitle],a.[OrgTitle] from Region a where a.[RegionCode] = '" + getAreaCode() + "'");
					userUnit = map.get("porgtitle").toString();
					userUnitforquestion = map.get("orgtitle").toString();

					if (userUnit.equals("")) {
						userUnit = map.get("regionname").toString() + "环境保护厅（局）";
					}
					OtherTools.showLog("当前用户的所属单位--" + userUnit);
				} catch (Exception e) {
					if(map != null && map.get("regionname") != null) {
						userUnit = map.get("regionname").toString() + "环境保护厅（局）";
					}
				}
			}
		}
	}

	/** 设置app状态,少用、慎用避免状态混乱 */
	public void setAppStatus(int Status) {
		this.appStatus = Status;
	}

	/** 获取app状态 */
	public int getAppStatus() {
		return appStatus;
	}

	private BaseUser baseUser;

	private static RequestQueue volleyQueue;

	/**
	 * @return 返回当前登录用户对象
	 */
	public BaseUser getCurrentUser() {
		return baseUser;
	}

	/**
	 * 初始化当前用户类型
	 * 
	 * @param jobTitle
	 */
	public void initCurrentUser(String jobTitle) {
		try {
			int zw = 0;
			try {
				zw = Integer.valueOf(jobTitle);
			} catch (NumberFormatException e) {
				zw = 3;
			}
			switch (zw) {
			case 0:
				/** 职务为0,生成对应的类,以下如此 */
				baseUser = (BaseUser) (OfficeMan.class.newInstance());
				break;
			case 1:
				baseUser = (BaseUser) (BoardLeadership.class.newInstance());
				break;
			case 2:
				baseUser = (BaseUser) (DeputyLeader.class.newInstance());
				break;
			case 3:
				baseUser = (BaseUser) (ExecuteLeader.class.newInstance());
				break;
			case 4:
				baseUser = (BaseUser) (ExecuteMan.class.newInstance());
				break;
			}
			baseUser.setJobTitle(jobTitle);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
