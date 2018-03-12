package com.jy.environment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	private Context context;

	/**
	 * sharedPreferences key
	 */
	private final String SHAREDPREFERENCES_KEY;
	private static final String KEY_QUANXIAN = "key.quanxian";
	private static final String KEY_IS_FIRST_USE = "key.isFirstUser";
	private static final String KEY_IS_FIRST_START = "key.isFirstStart";
	private static final String KEY_IS_FIRST_UPLOAD = "key.isFirstUpload";
	private static final String KEY_AUTO_LOGIN = "key.autologin";
	private static final String KEY_AUTO_UPLOAD = "key.autoupload";
	private static final String KEY_AUTO_Checked = "key.autouploadchecked";
	private static final String KEY_SHOWOLD = "key.showold";
	public static final String MESSAGE_NOTIFY_KEY = "message_notify";
	public static final String ACESS_NEWS_KEY = "message_notify";
	private static SharedPreferencesUtil instance;

	/**
	 * 
	 * @Title: getInstance
	 * @Description: get SharedPreferencesUtils instance
	 * @param context
	 * @return SharedPreferencesUtils
	 * @throws
	 */
	public static SharedPreferencesUtil getInstance(Context context) {
		if (instance == null)
			instance = new SharedPreferencesUtil(context);

		return instance;
	}

	private SharedPreferencesUtil(Context context) {
		this.context = context;
		SHAREDPREFERENCES_KEY = context.getPackageName();
	}

	public boolean getChecked() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(KEY_AUTO_Checked, true);
	}

	public void setChecked(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(KEY_AUTO_Checked, flag).commit();
	}

	public String getYuy() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getString("yuy", "xiaoyan");

	}

	// 接收消息按钮的状态
	public boolean getSwitchButton(String useridid) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(useridid, true);
	}

	public int getDi() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);

		return sharedPreferences.getInt("di", 0);
	}

	public void SetDi(int di) {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("di", di);
		editor.commit();
	}

	public boolean getBeijing() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);

		return sharedPreferences.getBoolean("beijing", true);
	}

	public void SetBeijing(Boolean di) {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("beijing", di);
		editor.commit();
	}

	// 接受信息按钮状态的改变
	public void Setbutton(String useridid, boolean flag) {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(useridid, flag);
		editor.commit();
	}

	public void SetYuy(String mingc) {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("yuy", mingc);
		editor.commit();
	}

	/**
	 * get is auto login
	 */
	public boolean getIsAutoLogin() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		boolean flag = sharedPreferences.getBoolean(KEY_AUTO_LOGIN, false);
		return flag;
	}

	/**
	 * get is auto upload
	 */
	public boolean getIsAutoUpload() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		boolean flag = sharedPreferences.getBoolean(KEY_AUTO_UPLOAD, false);
		return flag;
	}

	public boolean getMsgNotify() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(MESSAGE_NOTIFY_KEY, true);
	}

	public boolean getSmsNotify() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("sms", true);
	}

	public void setSmsNotify(boolean isChecked) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("sms", isChecked);
		editor.commit();
	}

	public boolean getAcceptNews() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(ACESS_NEWS_KEY, true);
	}

	public void setAcceptNews(boolean isChecked) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(ACESS_NEWS_KEY, isChecked);
		editor.commit();
	}

	public void setMsgNotify(boolean isChecked) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(MESSAGE_NOTIFY_KEY, isChecked);
		editor.commit();
	}

	public boolean getnotificationShow() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("notificationShow", true);
	}

	public void setnotificationShow(boolean isChecked) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("notificationShow", isChecked);
		editor.commit();
	}

	public boolean getisDingwei() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("dingwei", true);
	}

	public void setIsDingwei(boolean isChecked) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("dingwei", isChecked);
		editor.commit();
	}

	/**
	 * set auto login
	 */
	public void setIsAutoLogin(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_AUTO_LOGIN, flag);
		editor.commit();
	}

	/**
	 * set auto upload
	 */
	public void setIsAutoUpload(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_AUTO_UPLOAD, flag);
		editor.commit();
	}

	/**
	 * get the first 搜集设备信息
	 * 
	 * @return
	 */
	public boolean IsFirstUpload() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		boolean flag = sharedPreferences.getBoolean(KEY_IS_FIRST_UPLOAD, true);
		return flag;
	}

	/**
	 * set get the first 搜集设备信息
	 * 
	 * @param flag
	 */
	public void setIsFirstUpload(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_FIRST_UPLOAD, flag);
		editor.commit();
	}

	/**
	 * get is first use application flag
	 * 
	 * @return
	 */
	public boolean IsFirstUse() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		boolean flag = sharedPreferences.getBoolean(KEY_IS_FIRST_USE, true);
		return flag;
	}

	/**
	 * set first use application flag
	 * 
	 * @param flag
	 */
	public void setIsFirstUse(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_FIRST_USE, flag);
		editor.commit();
	}

	/**
	 * get is first use application flag
	 * 
	 * @return
	 */
	public boolean IsFirstStart() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		boolean flag = sharedPreferences.getBoolean(KEY_IS_FIRST_START, true);
		return flag;
	}

	/**
	 * set first use application flag
	 * 
	 * @param flag
	 */
	public void setIsFirstStart(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_IS_FIRST_START, flag);
		editor.commit();
	}

	/**
	 * @Title: get
	 * @Description: get long value form SharedPreferences
	 * @param key
	 * @param defValue
	 * @return long
	 * @throws
	 */
	public long get(String key, long defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		return sharedPreferences.getLong(key, defValue);
	}

	/**
	 * @Title: put
	 * @Description: put long value to SharedPreferences
	 * @param key
	 * @param value
	 *            void
	 * @throws
	 */
	public void put(String key, long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * @Title: get
	 * @Description: get string value form SharedPreferences
	 * @param key
	 * @param defValue
	 * @return String
	 * @throws
	 */
	public String get(String key, String defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		return sharedPreferences.getString(key, defValue);
	}

	/**
	 * @Title: put
	 * @Description: put string value to SharedPreferences
	 * @param key
	 * @param value
	 *            void
	 * @throws
	 */
	public void put(String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * @Title: get
	 * @Description: get int value form SharedPreferences
	 * @param key
	 * @param defValue
	 * @return int
	 * @throws
	 */
	public int get(String key, int defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * @Title: put
	 * @Description: put int value to SharedPreferences
	 * @param key
	 * @param value
	 *            void
	 * @throws
	 */
	public void put(String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * @Title: get
	 * @Description: get boolean value form SharedPreferences
	 * @param key
	 * @param defValue
	 * @return boolean
	 * @throws
	 */
	public boolean get(String key, boolean defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * @Title: put
	 * @Description: put boolean value to SharedPreferences
	 * @param key
	 * @param value
	 *            void
	 * @throws
	 */
	public void put(String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * @Title: get
	 * @Description: get float value form SharedPreferences
	 * @param key
	 * @param defValue
	 * @return float
	 * @throws
	 */
	public float get(String key, float defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		return sharedPreferences.getFloat(key, defValue);
	}

	/**
	 * @Title: put
	 * @Description: put float value to SharedPreferences
	 * @param key
	 * @param value
	 *            void
	 * @throws
	 */
	public void put(String key, float value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, 0);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	
	/**
	 * true 代表最高权限，false为正常权限，默认正常
	 * @return
	 */
	public boolean getQuanXian() {
//		SharedPreferences sharedPreferences = context.getSharedPreferences(
//				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
//		return sharedPreferences.getBoolean(KEY_QUANXIAN, false);
		return true;
	}

	public void setQuanXian(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(KEY_QUANXIAN, flag).commit();
	}
	

	public boolean getoldshow() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(KEY_SHOWOLD, false);
	}

	public void setoldshow(boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(KEY_SHOWOLD, flag).commit();
	}
}
