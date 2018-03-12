package com.mapuni.android.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.dom4j.DocumentException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.WebServiceProvider;

/**
 * FileName: DisplayUitl.java Description: ������
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-6 ����02:53:23
 */

public class DisplayUitl {

	/**
	 * ͬ������
	 */
	public static final String SYNCDATA = "sync_data";
	/**
	 * ״̬��������ʾ
	 */
	public static final String STATUS_BAR_TIPS = "status_bar_tips";
	/**
	 * ��������
	 */
	public static final String VOICE = "voice";
	/**
	 * ����ʶ��
	 */
	public static final String FACEDECTOR = "facedector";
	/**
	 * �Զ���¼
	 */
	public static final String AUTOLOGIN = "auto_login";
	/**
	 * ˢ������
	 */
	public static final String REFRESHDATA = "refresh_data";
	/**
	 * �˵���ʽ
	 */
	public static final String MENUSTYLE = "menustyle";
	/**
	 * ���������ʽ
	 */
	public static final String TASKMANAGERSTYLE = "taskmanagerstyle";
	/**
	 * �б������С
	 */
	public static final String TEXTSIZE = "textsize";
	/**
	 * �б�һ�μ�����������
	 */
	public static final String LISTLOADNUM = "listloadnum";
	/**
	 * ͬ������һ����������
	 */
	public static final String SYNCDATANUM = "syncdatanum";
	/**
	 * �Զ�ͬ��ʱ����
	 */
	public static final String AUTOASYNCDURATION = "autosyncduration";

	/**
	 * button�����ʱ��
	 */
	private static long lastClickTime;

	/**
	 * Description: ���ع���ϵͳҳ���html���
	 * 
	 * @param context
	 *            ������
	 * @return html��� String
	 * @author ����� Create at: 2012-12-6 ����02:52:47
	 */
	public static String getHtml(Context context) {
		String bbh = new BaseAutoUpdate().getTargetSystemVersionNumber(context);
		String html = "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" + "<tr>" + "<td height='100%' valign='top' align='center'>"
				+ "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" + " <tr>"
				+ " <td background='images/title01.gif' style='width: 100%; height: 37px; text-align: center; color: #1f6baf; font-weight: bold;'> ��������ƶ�ִ��ϵͳ</td>" + "</tr>"
				+ "<tr>" + "<td style='width: 100%; height: 37px; text-align: left; color: #1f6baf; font-weight: bold;'>"
				+ " <table border='0' cellspacing='0' cellpadding='0' align='center'>" + " <tr>"
				+ "  <td align='center' style=' width: 100%; height: 37px; color: #1f6baf; font-weight: bold;'>" + "    �汾�ţ�"
				+ bbh
				+ ""
				+ " </td>"
				+ " </tr>"
				+ " <tr>"
				+ "  <td style='width: 100%; height: 37px; color: #1f6baf; font-weight: bold;'>"
				+ "  <table width='100%' border='0' cellspacing='0' cellpadding='0'>"
				+ "   <tr>"
				+ "  <td>"
				+ "    <img src='file:///android_asset/yutu.png' width='40' height='33' />"
				+ "  </td>"
				+ " <td>"
				+ "    �п���ͼ�Ƽ��ɷ����޹�˾"
				+ "         </td>"
				+ "        </tr>"
				+ "      </table>"
				+ "    </td>"
				+ " </tr>"
				+ "  <tr>"
				+ " <td style='width: 100%; height: 37px; color: #1f6baf; font-weight: bold; text-align: center;'>"
				+ "  ��������Ȩ��" + " </td>" + "</tr>" + "  </table>" + "  </td>" + "</tr>" + "</table>" + " </td>" + " </tr>" + "</table>";
		return html;
	}

	/**
	 * Description: ���ַ���ת��Ϊ����ĸ
	 * 
	 * @param str
	 *            Ҫת�����ַ���
	 * @return �ַ���������ĸ String
	 * @author ����� Create at: 2012-12-6 ����02:53:49
	 */
	public static String getPinYinFirstChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			/** ��ȡ���ֵ�����ĸ */
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * ��ȡgis_config�ļ��½ڵ�Ϊnode�������ӽڵ����ݼ���
	 * 
	 * @param node
	 *            �ڵ�
	 * @author wanglg
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getMapListXML(String node) {
		try {
			String url = "/sdcard/mapuni/MobileEnforcement/data/gis_config.xml";
			FileInputStream fis = new FileInputStream(url);

			ArrayList<HashMap<String, Object>> dataXMLList = XmlHelper.getList(fis, node);
			return dataXMLList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��ͼͼ��������Ϣ
	 * 
	 * @return ������Ϣ����
	 */
	public static ArrayList<HashMap<String, Object>> getMapLayerData() {

		String url = "/sdcard/mapuni/MobileEnforcement/data/gis_config.xml";
		try {
			FileInputStream fis = new FileInputStream(url);

			ArrayList<HashMap<String, Object>> dataXMLList = XmlHelper.getList(fis, "item");
			return dataXMLList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Description: �ж��û��Ƿ���Ȩ��
	 * 
	 * @param authority
	 *            Ȩ��id
	 * @return true:�У�false��û�� boolean
	 * @author ����� Create at: 2012-12-6 ����02:54:33
	 */
	public static boolean getAuthority(String authority) {
		boolean authbool = false;
		HashMap<String, Object> authorityMap = null;
		String username = Global.getGlobalInstance().getUsername();
		if ("yutu".equalsIgnoreCase(username)) {
			authbool = true;
		} else {
			authorityMap = Global.getGlobalInstance().getAuthoritylist();
			if (authorityMap.get(authority) != null) {
				authbool = true;
			}
		}

		return true;
	}

	/**
	 * Description: �ж��û��Ƿ��ǰ칫�ҵ�
	 * 
	 * @param authority
	 *            Ȩ��id
	 * @return true:�У�false��û�� boolean
	 * @author ����� Create at: 2012-12-6 ����02:54:33
	 */
	public static boolean isOfficePerson() {
		boolean temp = false;
		String currentid = Global.getGlobalInstance().getUserid().toString();
		String sql = "select depid from PC_Users where userid = '" + currentid + "' ";
		String depidByCurrentID = SqliteUtil.getInstance().getDepidByUserid(sql);
		String sql1 = "select depid from PC_DepartmentInfo where DepName = '�칫��'";
		String depidByDepName = SqliteUtil.getInstance().getDepidByDepName(sql1);
		if (depidByCurrentID.equals(depidByDepName)) {
			temp = true;
		}

		return temp;
	}

	/**
	 * Description: �ж��û��Ƿ�����ִ�е��� ������ �Ƴ� �� ִ���ˣ�
	 * 
	 * @return true:��ִ���ˣ�false������ִ����
	 * 
	 */
	public static boolean isExecutor() {
		boolean is = false;
		String currentid = Global.getGlobalInstance().getUserid().toString();
		String sql = "select zw  from pc_users where userid = '" + currentid + "'";
		String zwByCurrentID = SqliteUtil.getInstance().getDepidByUserid(sql);
		if (zwByCurrentID != null && !zwByCurrentID.equals("")) {
			int zw = Integer.parseInt(zwByCurrentID);
			if (zw == 3 || zw == 4) {
				is = true;
			}

		}
		return is;

	}

	/**
	 * Description: �򿪲�ͬ�ĸ���
	 * 
	 * @param filepath
	 *            �ļ�·��
	 * @param context
	 *            ������ void
	 * @author ����� Create at: 2012-12-6 ����02:55:11
	 */
	public static void openfile(String filepath, Context context) {
		File file = new File(filepath);
		String filetyle = FileHelper.getFileType(file);
		// Intent intent = null;
		if (filetyle.equals("html")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.HTML.getType(), filepath, false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("doc")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.WORD.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("docx")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.WORD.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("pdf")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.PDF.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("xls")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.EXCEL.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("xlsx")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.EXCEL.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("ppt")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.PPT.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("avi")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.VIDEO.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("mp3")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.AUDIO.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("amr")) {
			// int i =filepath.lastIndexOf("/");
			FileHelper.OpenFile(FileHelper.FileType.AUDIO.getType(), "", false, context, file);
		} else if (filetyle.equals("jpg")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.IMAGE.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("png")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.IMAGE.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("txt")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.TXT.getType(), filepath, false, context, file);
			// context.startActivity(intent);
		} else if (filetyle.equals("mp4")) {
			// intent =
			FileHelper.OpenFile(FileHelper.FileType.VIDEO.getType(), "", false, context, file);
			// context.startActivity(intent);
		} else {
			Toast.makeText(context, "�ļ���ʽ��֧��", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Description: ��ȡϵͳ������Ϣ
	 * 
	 * @param filepath
	 *            �ļ�·��
	 * @param context
	 *            ������
	 * @return ��Ӧ��HashMap HashMap<String, Object>
	 * @author ����� Create at: 2012-12-6 ����02:55:11
	 */
	public static HashMap<String, Object> getDataXML(String typeName) {
		try {
			String url = "/sdcard/mapuni/MobileEnforcement/data/config.xml";
			FileInputStream fis = new FileInputStream(url);

			ArrayList<HashMap<String, Object>> dataXMLList = XmlHelper.getList(fis, "item");
			for (HashMap<String, Object> dataMap : dataXMLList) {
				if (typeName.equalsIgnoreCase(dataMap.get("typename").toString())) {
					return dataMap;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: ��ȡ��Ƶ����������Ϣ
	 * 
	 * @return ������Ƶ�����ļ���Ϣ ArrayList<HashMap<String,Object>>
	 * @author Administrator Create at: 2012-12-6 ����02:57:10
	 */
	public static ArrayList<HashMap<String, Object>> getVideoData() {
		String url = PathManager.SDCARD_VIDEODATA_CONFIG;
		try {
			FileInputStream fis = new FileInputStream(url);
			ArrayList<HashMap<String, Object>> dataXMLList = XmlHelper.getList(fis, "item");
			return dataXMLList;
		} catch (FileNotFoundException e) {
			Log.i("AllVideoOnlineActivity", "δ�ҵ��ļ���" + url);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: ����data_user.xml
	 * 
	 * @return ����data_user.xml�ļ�����Ϣ List<HashMap<String,String>>
	 * @author Administrator Create at: 2012-12-6 ����02:57:29
	 */
	/*
	 * public static List<HashMap<String, String>> getLoginfo() { try {
	 *//** ����assets�����ر������� */
	/*
	 * InputStream in = new FileInputStream(new File(BaseClass.DATA_PATH +
	 * "data_user.xml")); XmlPullParser xmlparser = Xml.newPullParser();
	 * xmlparser.setInput(in, "utf-8"); int event = xmlparser.getEventType();
	 * List<HashMap<String, String>> list = new ArrayList<HashMap<String,
	 * String>>();
	 * 
	 * HashMap<String, String> map = new HashMap<String, String>(); while (event
	 * != XmlPullParser.END_DOCUMENT) { switch (event) { case
	 * XmlPullParser.START_DOCUMENT: break; case XmlPullParser.START_TAG: if
	 * (xmlparser.getName().equals("item")) { map = new HashMap<String,
	 * String>(); } else if (xmlparser.getName().equals("username")) { String
	 * username = xmlparser.nextText(); map.put("username", username); } else if
	 * (xmlparser.getName().equals("password")) { String password =
	 * xmlparser.nextText(); map.put("password", password); } else if
	 * (xmlparser.getName().equals("authority")) { String authority =
	 * xmlparser.nextText(); map.put("authority", authority); list.add(map); map
	 * = null; } break; case XmlPullParser.END_TAG: break; } event =
	 * xmlparser.next(); } return list; } catch (IOException e) {
	 * ExceptionManager.WriteCaughtEXP(e, "Login"); // Log.e(TAG,
	 * e.getMessage()); } catch (XmlPullParserException e) {
	 * ExceptionManager.WriteCaughtEXP(e, "Login"); // Log.e(TAG,
	 * e.getMessage()); } return null; }
	 */

	/**
	 * Description: ���ر����汾����
	 * 
	 * @param context
	 *            ������
	 * @return �����汾���� String
	 * @author ����� Create at: 2012-12-6 ����02:58:05
	 */
	public static String getName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			return pm.getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "Login");
			// Log.e(TAG, e.getMessage());
		}
		return null;
	}

	/**
	 * Description: ���ر����汾��
	 * 
	 * @param context
	 *            ������
	 * @return �����汾�� int
	 * @author ����� Create at: 2012-12-6 ����02:58:33
	 */
	public static int getCode(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			return pm.getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "Login");
			// Log.e(TAG, e.getMessage());
		}
		return 0;
	}

	public static String getListColumnString(HashMap<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = String.valueOf(entry.getKey());
			String value = String.valueOf(entry.getValue());
			if ("name".equals(key) || "lefticon".equals(key) || "righticon".equals(key) || "".equals(value)) {
				continue;
			} else {
				if (!(sb.length() == 0)) {
					sb.append(",");
				}
				sb.append(value);
			}
		}
		return sb.toString();
	}

	/**
	 * Description: ����key���ض�Ӧ������ֵ
	 * 
	 * @param context
	 *            ������
	 * @param key
	 *            ��
	 * @param defValue
	 *            Ĭ��ֵ
	 * @return ����getSharedPreferences��setting����ֵ Object
	 * @author ����� Create at: 2012-12-6 ����03:03:00
	 */
	public static Object getSettingValue(Context context, String key, Object defValue) {
		// SharedPreferences sp = context.getSharedPreferences("setting",
		// Context.MODE_PRIVATE);
		// if (SYNCDATA.equals(key) || STATUS_BAR_TIPS.equals(key) ||
		// VOICE.equals(key)
		// || AUTOLOGIN.equals(key) || REFRESHDATA.equals(key)) {
		// boolean result = sp.getBoolean(key, true);
		// return result;
		// } else {
		// int result = sp.getInt(key, (Integer) defValue);
		// return result;
		// }

		SqliteUtil su = SqliteUtil.getInstance();
		Object value = null;
		String sql = "select SETVALUE from T_USER_SETTING where SYSCODE = '" + Global.getGlobalInstance().getSystemtype() + "' and SETKEY = '" + key + "'";
		ArrayList<HashMap<String, Object>> result = su.queryBySqlReturnArrayListHashMap(sql);
		if (result != null && result.size() > 0)
			value = result.get(0).get("setvalue");
		if (value == null || value.equals(""))
			value = defValue;
		return value;
	}

	/**
	 * Description: ��ȡϵͳ�汾��
	 * 
	 * @param context
	 *            ������
	 * @return ��ǰϵͳ�汾�� String
	 * @author ����� Create at: 2012-12-6 ����02:44:48
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			return pm.getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ������������
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setSettingValue(Context context, String key, Object value) {
		String sql;
		Object oldValue = getSettingValue(context, key, null);
		if (oldValue == null) {
			sql = "insert into T_USER_SETTING('SYSCODE','SETKEY','SETVALUE') VALUES('" + Global.getGlobalInstance().getSystemtype() + "','" + key + "','" + value + "')";
		} else {
			sql = "update T_USER_SETTING set SETVALUE = '" + value + "' where SETKEY = '" + key + "' and SYSCODE = '" + Global.getGlobalInstance().getSystemtype() + "'";
		}
		// Toast.makeText(this, key+"=="+value.toString(), 0).show();
		SqliteUtil.getInstance().execute(sql);
	}

	/**
	 * Description:����Base��App��һЩ��Ϣ ˵���� key--------Description <li>update_apn
	 * �Ƿ��л�apn <li>sys_name ��ǰϵͳ���� <li>sys_type ��ǰϵͳ���� <li>system_name ��ǰϵͳ����
	 * <li>url ��������ַ <li>username �û��� <li>userid �û�ID <li>DepId ����ID <li>
	 * administrative �û��������� <li>url ��������ַ
	 * 
	 * @param context
	 * @param key
	 * @param info
	 * @author Administrator<br>
	 *         Create at: 2013-2-27 ����11:01:11
	 */
	public static boolean saveAppInfoDataToPreference(Context context, String key, String info) {
		SharedPreferences mPreferences = context.getSharedPreferences("app_info", context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor mEditor = mPreferences.edit();
		mEditor.putString(key, info);
		return mEditor.commit();
	}

	/**
	 * Description:ȡ��Base��App��һЩ��Ϣ
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-2-27 ����11:01:33
	 */
	public static String getAppInfoDataToPreference(Context context, String key, String defValue) {
		String value = "";
		SharedPreferences mPreferences = context.getSharedPreferences("app_info", Context.MODE_WORLD_WRITEABLE);
		value = mPreferences.getString(key, defValue);
		return value;
	}

	/**
	 * Description:������Ļ�ֱ��ʵ����飬0λ��Ϊ��ȣ�1λ��Ϊ�߶�
	 * 
	 * @param context
	 * @return ��Ļ�ֱ��ʵ�����
	 * @author wanglg
	 * @Create at: 2013-5-2 ����2:05:17
	 */
	public static int[] getMobileResolution(Context context) {
		int[] args = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		args[0] = dm.widthPixels;
		args[1] = dm.heightPixels;
		return args;

	}

	/**
	 * Description: ��SharedPreferences�洢�ж�ȡ����
	 * 
	 * @param context
	 * @param perferencesName
	 *            SharedPreferences����
	 * @param key
	 *            �洢��key
	 * @return value
	 * @author wanglg
	 * @Create at: 2013-6-5 ����11:22:40
	 */
	public static String readPreferences(Context context, String perferencesName, String key) {
		SharedPreferences preferences = context.getSharedPreferences(perferencesName, context.MODE_PRIVATE);
		String result = preferences.getString(key, "");
		return result;
	}

	/**
	 * Description:��SharedPreferences�洢��д������
	 * 
	 * @param context
	 * @param perferencesName
	 *            SharedPreferences����
	 * @param key
	 *            д��key
	 * @param value
	 * @author wanglg
	 * @Create at: 2013-6-5 ����11:22:47
	 */
	public static void writePreferences(Context context, String perferencesName, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(perferencesName, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Stringת��Ϊʱ�� �ַ�����ʽΪyyyy-MM-dd hh:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static Date ParseDate(String str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date addTime = null;
		try {
			addTime = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return addTime;
	}

	/**
	 * Description:�жϰ�ť�ǲ�����Ч��� ---�����ã�button������ε���ᴥ����ε���¼�����һЩ����
	 * 
	 * @return ��Ч�������true ����Ч�������false �����ж��Ƿ���Чʱ����Ϊ0.8��
	 * @author wanglg
	 * @Create at: 2013-7-23 ����2:48:57
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * ������ת��Ϊ�ַ��������أ�
	 * 
	 * @param date
	 * @param format
	 *            :ʱ���ʽ���������yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String ParseDateToString(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);
	}

	/**
	 * ������ת��Ϊ�ַ��� ��ʽΪ yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String ParseDateToString(Date date) {
		return ParseDateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ��ȡ��������ʱ��
	 */

	public static String getServerTime() {
		String UPDATETIME = "";
		try {
			String result = (String) WebServiceProvider.callWebService(Global.NAMESPACE, "GetServerDateTime", new ArrayList<HashMap<String, Object>>(), Global.getGlobalInstance()
					.getSystemurl() + Global.WEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);
			if (result != null) {
				UPDATETIME = result;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return UPDATETIME;
	}

	/**
	 * ��ʽ��ʱ�乤����
	 * 
	 * @param ����ʱ���ʽΪ
	 *            :yyyyMMddhhmmss
	 * @return �����ʽΪ:MM-dd hh:mm
	 */
	public static String format(String s) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddhhmmss");
		SimpleDateFormat format = null;
		Date res = null;
		try {
			res = formater.parse(s);
			format = new SimpleDateFormat("MM-dd hh:mm");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format.format(res);
	}

}
