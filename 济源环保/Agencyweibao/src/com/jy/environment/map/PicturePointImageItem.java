package com.jy.environment.map;

/**
 * 要在地图上显示的用户分享图片的单个图片的相关数据描述
 * @author shang
 * 
 */
public class PicturePointImageItem {
	/**
	 * @param _id 图片编号
	 * @param smallpath 缩略图路径
	 * @param realpath 大图路径
	 */
	public PicturePointImageItem(int _id, String smallpath, String realpath) {
		super();
		this._id = _id;
		this.smallpath = smallpath;
		this.realpath = realpath;
	}
	private int _id=0;
	private String smallpath="";
	private String realpath="";
	public String getSmallpath() {
		return smallpath;
	}
	public void setSmallpath(String smallpath) {
		this.smallpath = smallpath;
	}
	public String getRealpath() {
		return realpath;
	}
	public void setRealpath(String realpath) {
		this.realpath = realpath;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
}
