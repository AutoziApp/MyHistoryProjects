package com.jy.environment.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.SDCardUtils;

public class ShareInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0l;

	private String _user_name="";
	private String _content="";
	private Bitmap _headimg=null;
	private Bitmap _contentimg=null;
	private String _time="";

	public String get_user_name() {
		return _user_name;
	}
	public void set_user_name(String _user_name) {
		this._user_name = _user_name;
	}
	public String get_content() {
		return _content;
	}
	public void set_content(String _content) {
		this._content = _content;
	}
	public Bitmap get_headimg() {
		return _headimg;
	}
	public void set_headimg(Bitmap _headimg) {
		this._headimg = _headimg;
	}
	public Bitmap get_contentimg() {
		return _contentimg;
	}
	public void set_contentimg(Bitmap _contentimg) {
		this._contentimg = _contentimg;
	}
	public String get_time() {
		return _time;
	}
	public void set_time(String _time) {
		this._time = _time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public static ShareInfo GetShareInfoByJson(JSONObject j){
		if(j==null){
			return null;
		}
		ShareInfo model=new ShareInfo();
		try {
			model.set_user_name(j.get("uername").toString());
			model.set_content(j.get("content").toString());
			model.set_contentimg(CommonUtil.HexStringToBitmap( j.get("photo").toString()));
			SDCardUtils.storeInSD(model.get_contentimg());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}

}
