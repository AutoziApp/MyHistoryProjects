package com.jy.environment.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 应用程序更新实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable {

	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "oschina";

	private double versionCode;

	private String downloadUrl;
	private String updateLog;



	public double getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(double versionCode) {
		this.versionCode = versionCode;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static Update parse(String inputStream) throws IOException {
		Update update = null;
		try {

			// 获得XmlPullParser解析器

			JSONObject jsonObject = new JSONObject(inputStream);
		
				update = new Update();
				update.setDownloadUrl(jsonObject.getString("url"));
				update.setUpdateLog(jsonObject.getString("msg"));
				update.setVersionCode(jsonObject.getDouble("versionCode"));
		
		} catch (Exception e) {
			// TODO: handle exception
		}

		return update;
	}
}
