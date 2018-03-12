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
 * FileName: Global.java Description: ϵͳȫ��Ӧ����
 * 
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����03:44:43
 */
// wsc public class Global extends Application {
public class Global extends FrontiaApplication {
	/** Globalʵ�������� */
	private static Global GlobalInstance;

	/** ϵͳ�����ļ��ĸ�Ŀ¼mapuni�ļ���·�� */
	public final String BASE_PATH = PathManager.BASE_PATH;

	/** ϵͳ�����ļ�MobileEnforcement�ļ���·�� */
	public static final String SDCARD_RASK_DATA_PATH = PathManager.SDCARD_RASK_DATA_PATH;
	/** ��̨verson.xml�ļ�·�� */
	public static final String APK_CODE_URL = PathManager.APK_CODE_URL;
	/** ��̨config.xml�ļ�·�� */
	public static final String CONFIG_CODE_URL = PathManager.CONFIG_CODE_URL;
	/** ��̨video_config.xml�ļ�·�� */
	public static final String VIDEOCOFIG_CODE_URL = PathManager.VIDEOCOFIG_CODE_URL;
	/** ��̨���apk���µ�·�� */
	public static final String APK_DOWN_URL = PathManager.APK_DOWN_URL;
	/** ��̨������ݿ�汾��verson.xml�ļ�·�� */
	public static final String DB_CODE_URL = PathManager.DB_CODE_URL;
	/** ��̨����ֻ����ֻ����ļ�·�� */
	public static final String DE_DOWN_URL = PathManager.DE_DOWN_URL;
	/** ��̨������񸽼���·������TaskExecute�ļ���·�� */
	public static final String JCJLRWFJ_DOWN_URL = PathManager.JCJLRWFJ_DOWN_URL;
	/** �ֻ��˸���apk�ı���Ŀ¼·������AutoUpdate�ļ��� */
	public static final String SDCARD_AutoUpdate_LOCAL_PATH = PathManager.SDCARD_AutoUpdate_LOCAL_PATH;
	/** �ֻ��˸���apk�ı����ļ�·������update.apk */
	public static final String SDCARD_APK_LOCAL_PATH = PathManager.SDCARD_APK_LOCAL_PATH;
	/** �ֻ������ݿ�MobileEnforcement.db�ļ����·�� */
	public static final String SDCARD_DB_LOCAL_PATH = PathManager.SDCARD_DB_LOCAL_PATH;
	/** �ֻ���config�ļ�·�� */
	public static final String SDCARD_CONFIG_LOCAL_PATH = PathManager.SDCARD_CONFIG_LOCAL_PATH;
	/** �ֻ���data�ļ�Ŀ¼ */
	public static final String SDCARD_DATA_LOCAL_PATH = PathManager.SDCARD_DATA_LOCAL_PATH;
	/** �ֻ�����־�ļ�Ŀ¼ */
	public static final String SDCARD_LOG_LOCAL_PATH = PathManager.SDCARD_LOG_LOCAL_PATH;
	/** �ֻ��˴�ű������ݿ���ļ�Ŀ¼����datebaseCopy */
	public static final String SDCARD_datebaseCopy_LOCAL_PATH = PathManager.SDCARD_datebaseCopy_LOCAL_PATH;
	/** �ֻ��˴�ű������ݿ���ļ�·������MobileEnforcement.db */
	public static final String SDCARD_CopyDB_LOCAL_PATH = PathManager.SDCARD_CopyDB_LOCAL_PATH;
	/** �ֻ���ͬ��������ŵı���·������fj */
	public static final String SDCARD_FJ_LOCAL_PATH = PathManager.SDCARD_FJ_LOCAL_PATH;
	/***/
	public static final String SDCARD_VIDEODATA_CONFIG = PathManager.SDCARD_VIDEODATA_CONFIG;
	/** �ֻ���ִ��������·������sitelaw */
	public static final String SITELAWRECORD_PATH = PathManager.SITELAWRECORD_PATH;
	/** �ֻ���ִ������ͨ���·������ZFBST */
	public static final String LAWKNOWALL_PATH = PathManager.LAWKNOWALL_PATH;
	/** �ֻ��˷��ɷ��滷�����ִ���ֲ���·�� */
	public static final String HJJCZFSC_PATH = PathManager.HJJCZFSC_PATH;

	/** �ֻ����������ݵ���ʱ�ļ�Ŀ¼����DataTemp */
	public static final String SDCARD_TEMP_PATH = PathManager.SDCARD_TEMP_PATH;

	/** �ֻ�������WebService��·�� */
	public static final String WEBSERVICE_URL = PathManager.WEBSERVICE_URL;
	/** �ֻ�������OA��WebService��·�� */
	public static final String OAWEBSERVICE_URL = PathManager.OAWEBSERVICE_URL;
	/** �����ռ� */
	public static final String NAMESPACE = PathManager.NAMESPACE;
	/** Ĭ�ϵ����ӷ������ĵ�ַ */
	//private String url = "http://192.168.5.112";
	private String url = "http://192.168.15.48:8189";
	
	/** app ��½״̬ */
	public static final int APP_LOGIN_DURING = 200;
	private int appStatus = -1;
	/** app��������״̬ */
	public static final int APP_RUN_NORMAL = 201;

	/** app �������ݿ�״̬ */
	public static final int APP_SQLITE_ACCESSING = 202;

	/** app ����״̬ */
	public static final int APP_NETWORK_CONNECTION = 203;

	/** app�˳�״̬ */
	public static final int APP_EXIT_DURING = 204;

	/** ��ǰId */
	private String CurrentID = null;
	/** �����û�������λ */
	private String userUnit = "�����л�������";
	private String userUnitforquestion = "�����л�������";
	/** �����û��� */
	private String username = "";
	/** �����û��� id */
	private String userid = "";
	/** �����û���½���� */
	private String password = "";
	/** �����û���ʵ���� */
	private String realName = "";
	/** �����û��������� */
	private String areaCode = "";

	/** ��map��ͼ���Ƿ�����ֳ�ִ���� */
	private boolean mapXczfAddHistory = false;
	/** ͨѶ¼ҳ���ѯ������ */
	private boolean searchCondition = false;
	/** ����ϵͳ���� */
	private String systype = "YDZF";
	/** �����������ѵ���ʾ */
	private boolean showAlter;
	/** ���õ�ǰ�����ID */
	private String currentApnId;
	/** �Ƿ����apn��Ĭ��Ϊfalse */
	private boolean updataapn = false;

	/** �����û��������� */
	private String administrative = "";
	/** �û��������� */
	private String depId = "";
	/** �û�Ȩ�޼��� */
	private HashMap<String, Object> authoritylist = new HashMap<String, Object>();
	/** ���÷��������� */
	private String Systemname = "";
	/** ��ǰ��¼�û���Ȩ�޼��� */
	private String[] currentUserAuthority = null;
	/** ҳ�洫�����ݵ�Bundle���� */
	private Bundle bundle = null;

	/** ϵͳȨ�ޱ������ */
	public static final String SystemConfig = "Mob_SystemConfig Order By sort";
	/***/
	private int menuHeight;
	/** ��ǰ�Ƿ���ͬ������ */
	public static Boolean IsDataSync = false;
	/** ��־ʹ����Ч��������չ����ListView���Ǿ��еĵ���DialogЧ��չʾ�� */
	public Boolean useNewStyleBoolean = true;
	/** ��ǰid */
	private String itemId;

	/** true��ʾ�����ļ����֣�false��ʾ���ݿ��ȡ���� */
	public static final boolean isTestStyle = true;

	/** ��ʶ���ݿ��в�ͬ�����ʾ */
	private HashMap<String, Object> type_DB;
	/** �Ƿ���ʾ���Ͻǲ˵��еķ��ɷ��棬Ӧ���ֲᣬȫ�ֲ��� */
	public boolean isShowGd = false;
	/** ����״̬�Ƿ��иı��ʶ���иı����������б������������onrestart����ˢ������ */
	private boolean TaskStateChange = false;

	/**
	 * Description: ��ȡ��ǰ�����ID
	 * 
	 * @return ��ǰ�����ID String
	 * @author ����Ԫ Create at: 2012-12-5 ����04:52:24
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * Description: ���õ�ǰ�Ե�ID
	 * 
	 * @param itemId
	 *            ��ǰ�����ID void
	 * @author ����Ԫ Create at: 2012-12-5 ����04:53:05
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * Description:
	 * 
	 * @return int
	 * @author ����Ԫ Create at: 2012-12-5 ����04:53:35
	 */
	public int getMenuHeight() {
		return menuHeight;
	}

	/**
	 * Description:
	 * 
	 * @param menuHeight
	 *            void
	 * @author ����Ԫ Create at: 2012-12-5 ����04:54:19
	 */
	public void setMenuHeight(int menuHeight) {
		this.menuHeight = menuHeight;
	}

	/**
	 * Description: ��ȡ��ǰ��¼�û������Ĳ���ID
	 * 
	 * @return ����ID String
	 * @author ����Ԫ Create at: 2012-12-5 ����04:55:39
	 */
	public String getDepId() {
		return depId;
	}

	/**
	 * Description: ���õ�ǰ��¼�û��������ŵ�ID
	 * 
	 * @param depId
	 *            ����IDֵ void
	 * @author ����Ԫ Create at: 2012-12-5 ����04:56:08
	 */
	public void setDepId(String depId) {
		this.depId = depId;
		baseUser.setDepID(depId);
	}

	/**
	 * Description: ��ȡGlobal����
	 * 
	 * @return Globalʵ�������� Global
	 * @author ����Ԫ Create at: 2012-12-5 ����04:59:41
	 */
	public static Global getGlobalInstance() {
		return GlobalInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		GlobalInstance = this;
		
		

		/* Volley���� */
		// ����Volley��Http�������
		volleyQueue = Volley.newRequestQueue(getApplicationContext());
		
		
		ExceptionManager crashHandler = ExceptionManager.getInstance();
		crashHandler.init(this);


	}

	// ����Volley��HTTP������нӿ�
	public static RequestQueue getRequestQueue() {
		return volleyQueue;
	}


	/**
	 * Description: ��ȡ��ǰ�û�ϵͳȨ��
	 * 
	 * @return �û�Ȩ�� String[]
	 * @author ����Ԫ Create at: 2012-12-5 ����05:00:16
	 */
	public String[] getCurrentUserAuthority() {
		return currentUserAuthority;
	}

	/**
	 * Description: ���õ�ǰϵͳȨ��
	 * 
	 * @param s
	 *            �û�Ȩ�� void
	 * @author Administrator Create at: 2012-12-5 ����05:00:46
	 */
	public void setCurrentUserAuthority(String[] s) {
		currentUserAuthority = s;
	}

	/**
	 * Description: ��ȡ��ǰBundle����
	 * 
	 * @return Bundle���� Bundle
	 * @author ����Ԫ Create at: 2012-12-5 ����05:01:03
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * Description: ���õ�ǰBundle����
	 * 
	 * @param bundle
	 *            Bundle���� void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:01:37
	 */
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * Description: �����ļ������֣���Ҫ��׺������ȡ�ļ���Bitpmap
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            Bitpmap����
	 * @return ��������Ҫ��darwable Bitmap
	 * @author ����Ԫ Create at: 2012-12-5 ����05:02:07
	 */
	public static Bitmap getRes(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	/**
	 * Description: ���ر���ͼƬ
	 * 
	 * @param name
	 *            ͼƬ����
	 * @return Bitmap���� Bitmap
	 * @author ����Ԫ Create at: 2012-12-5 ����05:03:22
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
	 * Description: ��ȡ��������ַ
	 * 
	 * @return ��������ַ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:07:02
	 */
	public String getSystemurl() {
		return url;
	}

	/**
	 * Description: ���÷�������ַ
	 * 
	 * @param url
	 *            ��������ַ void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:07:21
	 */
	public void setSystemurl(String url) {

		this.url = url;
	}

	/**
	 * Description: ��ȡϵͳ����
	 * 
	 * @return ϵͳ���� String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:07:53
	 */
	public String getSystemname() {
		return Systemname;
	}

	/**
	 * Description: ����ϵͳ����
	 * 
	 * @param systemname
	 *            ϵͳ���� void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:42:58
	 */
	public void setSystemname(String systemname) {
		Systemname = systemname;
	}

	/**
	 * Description: ���õ�ǰID
	 * 
	 * @param CurrentID
	 *            IDֵ void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:43:21
	 */
	public void setCurrentID(String CurrentID) {
		this.CurrentID = CurrentID;
	}

	/**
	 * Description: ��ȡ��ǰID
	 * 
	 * @return IDֵ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:44:03
	 */
	public String getCurrentID() {
		return CurrentID;
	}

	/**
	 * Description: ��ȡ��ǰ�û���Ȩ�޼���
	 * 
	 * @return Ȩ�޼��� ArrayList<HashMap<String,Object>>
	 * @author ����Ԫ Create at: 2012-12-5 ����05:44:30
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
	 * Description: ���õ�ǰ�û���Ȩ�޼���
	 * 
	 * @param authoritylist
	 *            Ȩ�޼���ֵ void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:45:03
	 */
	public void setAuthoritylist(HashMap<String, Object> authoritylist) {
		this.authoritylist = authoritylist;
	}

	/**
	 * Description: ��ȡ��ǰ��¼�û�������
	 * 
	 * @return �û����� String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:45:36
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * ��ȡ�û�������λ
	 * 
	 * @return
	 */
	public String getUserUnit() {
		return userUnit;
	}

	/**
	 * Description: ���õ�ǰ�û�������
	 * 
	 * @param username
	 *            �û����� void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:45:58
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Description: ��ȡ��ǰ�û���������
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * Description: ���õ�ǰ�û���������
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
		baseUser.setSyncDataRegionCode(areaCode);
	}

	/**
	 * Description: ��ȡͨѶ¼ҳ���ѯ�������ı�־ true ��������
	 */
	public boolean isSearchCondition() {
		return searchCondition;
	}

	/**
	 * Description: ����ͨѶ¼ҳ���ѯ�������ı�־ true ��������
	 */

	public void setSearchCondition(boolean searchCondition) {
		this.searchCondition = searchCondition;
	}

	/**
	 * Description: ��ȡ��ǰ��map��ͼ���Ƿ��ֳ�ִ���� true �� ��ִ����¼�и���
	 */
	public boolean isMapXczfAddHistory() {
		return mapXczfAddHistory;
	}

	/**
	 * Description: ���õ�ǰ��map��ͼ���Ƿ��ֳ�ִ���� true �� ��ִ����¼�и���
	 */
	public void setMapXczfAddHistory(boolean mapXczfAddHistory) {
		this.mapXczfAddHistory = mapXczfAddHistory;
	}

	/**
	 * Description: ��ȡ��ǰ��¼�û���ID
	 * 
	 * @return �û�IDֵ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:46:20
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
	 * ��ȡ�û���ʵ����
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
	 * Description: ���ǵ�ǰ�û���ID
	 * 
	 * @param userid
	 *            �û���ID void
	 * @author Administrator Create at: 2012-12-5 ����05:46:50
	 */
	public void setUserid(String userid) {
		this.userid = userid;
		baseUser.setUserID(userid);
	}

	/**
	 * Description:��ȡ�û���½����
	 * 
	 * @return
	 * @author wanglg
	 * @Create at: 2013-4-17 ����11:07:58
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Description: ���õ�½����
	 * 
	 * @param password
	 * @author wanglg
	 * @Create at: 2013-4-17 ����11:08:12
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Description: ��ȡ��������
	 * 
	 * @return true�����ѣ�false�������� boolean
	 * @author ����Ԫ Create at: 2012-12-5 ����05:47:14
	 */
	public boolean getShowAlter() {
		return showAlter;
	}

	/**
	 * Description: ������������
	 * 
	 * @return true�����ѣ�false�������� boolean
	 * @author ����Ԫ Create at: 2012-12-5 ����05:50:25
	 */
	public void setShowAlter(boolean showAlter) {
		this.showAlter = showAlter;
	}

	/**
	 * Description: ��ȡ��ǰAPN��ID
	 * 
	 * @return APN��IDֵ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:50:49
	 */
	public String getCurrentApnId() {
		return currentApnId;
	}

	/**
	 * Description: ���õ�ǰAPN��ID
	 * 
	 * @return APN��IDֵ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:51:22
	 */
	public void setCurrentApnId(String currentApnId) {
		this.currentApnId = currentApnId;
	}

	/**
	 * Description: ��ȡ�Ƿ����APN
	 * 
	 * @return true�����£�false�������� boolean
	 * @author ����Ԫ Create at: 2012-12-5 ����05:51:38
	 */
	public boolean isUpdataapn() {
		return updataapn;
	}

	/**
	 * Description: �����Ƿ����APN
	 * 
	 * @return true�����£�false�������� boolean
	 * @author ����Ԫ Create at: 2012-12-5 ����05:52:34
	 */
	public void setUpdataapn(boolean updataapn) {
		this.updataapn = updataapn;
	}

	/**
	 * Description: ��ȡ��ǰ�û�����Ͻ������
	 * 
	 * @return ����Ͻ����ֵ String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:52:54
	 */
	public String getAdministrative() {
		return administrative;
	}

	/**
	 * Description: ���õ�ǰ�û��Ĺ�Ͻ����
	 * 
	 * @param administrative
	 *            ����Ͻ����ֵ void
	 * @author ����Ԫ Create at: 2012-12-5 ����05:53:27
	 */
	public void setAdministrative(String administrative) {
		this.administrative = administrative;
		baseUser.setRegionCode(administrative);
	}

	/**
	 * Description: ��ȡ��ǰϵͳʱ��
	 * 
	 * @return ʱ���ַ��� ��ʽΪ yyyy-MM-dd HH:mm:ss String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:54:46
	 */
	public String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Description: ��ȡ��ǰϵͳʱ��
	 * 
	 * @param timeStyle
	 *            Ҫ��ʾ��ʱ���ʽ
	 * @return ʱ���ַ��� String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:54:46
	 */
	public String getDate(String timeStyle) {
		SimpleDateFormat format = new SimpleDateFormat(timeStyle);
		return format.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Description: ת��ʱ���ʽ
	 * 
	 * @param timeStyle
	 *            Ҫ��ʾ��ʱ���ʽ
	 * @param date
	 *            ʱ��
	 * @return ʱ���ַ��� String
	 * @author ����Ԫ Create at: 2012-12-5 ����05:54:46
	 */
	public String getDate(Date date, String timeStyle) {
		SimpleDateFormat format = new SimpleDateFormat(timeStyle);
		return format.format(date);
	}

	/**
	 * Description:��ѯ�б��һ�μ��ص����������30��
	 * 
	 * @return ���� int
	 * @author ����Ԫ Create at: 2012-12-5 ����05:56:40
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

	// true����ʾ���Ͻǲ˵��еķ��ɷ��棬Ӧ���ֲᣬȫ�ֲ��ң�false��֮
	public void setShowGd(boolean isShowGd) {
		this.isShowGd = isShowGd;
	}

	public boolean getTaskStateChange() {
		return TaskStateChange;
	}

	public void setTaskStateChange(boolean TaskStateChange) {
		this.TaskStateChange = TaskStateChange;
	}

	/** �����û����������ʼ��ȫ������ */
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
				// ���ص�ǰ�û���Ȩ�޼�
				HashMap<String, Object> authoritylist = users_instance.checkAuthor();
				setAuthoritylist(authoritylist);
				HashMap<String, Object> map = null;
				try {
					map = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select a.[RegionName],a.[POrgTitle],a.[OrgTitle] from Region a where a.[RegionCode] = '" + getAreaCode() + "'");
					userUnit = map.get("porgtitle").toString();
					userUnitforquestion = map.get("orgtitle").toString();
					if (userUnit.equals("")) {
						userUnit = map.get("regionname").toString() + "�������������֣�";
					}
					OtherTools.showLog("��ǰ�û���������λ--" + userUnit);
				} catch (Exception e) {
					if(map != null && map.get("regionname") != null) {
						userUnit = map.get("regionname").toString() + "�������������֣�";
					}
				}
			}
		}
	}

	public String getUserUnitforquestion() {
		return userUnitforquestion;
	}

	/** �����û����������ʼ��ȫ������ */
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
				// ���ص�ǰ�û���Ȩ�޼�
				HashMap<String, Object> authoritylist = users_instance.checkAuthor();
				setAuthoritylist(authoritylist);
				HashMap<String, Object> map = null;
				try {
					map = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select a.[RegionName],a.[POrgTitle],a.[OrgTitle] from Region a where a.[RegionCode] = '" + getAreaCode() + "'");
					userUnit = map.get("porgtitle").toString();
					userUnitforquestion = map.get("orgtitle").toString();

					if (userUnit.equals("")) {
						userUnit = map.get("regionname").toString() + "�������������֣�";
					}
					OtherTools.showLog("��ǰ�û���������λ--" + userUnit);
				} catch (Exception e) {
					if(map != null && map.get("regionname") != null) {
						userUnit = map.get("regionname").toString() + "�������������֣�";
					}
				}
			}
		}
	}

	/** ����app״̬,���á����ñ���״̬���� */
	public void setAppStatus(int Status) {
		this.appStatus = Status;
	}

	/** ��ȡapp״̬ */
	public int getAppStatus() {
		return appStatus;
	}

	private BaseUser baseUser;

	private static RequestQueue volleyQueue;

	/**
	 * @return ���ص�ǰ��¼�û�����
	 */
	public BaseUser getCurrentUser() {
		return baseUser;
	}

	/**
	 * ��ʼ����ǰ�û�����
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
				/** ְ��Ϊ0,���ɶ�Ӧ����,������� */
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
