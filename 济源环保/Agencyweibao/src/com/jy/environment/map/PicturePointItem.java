package com.jy.environment.map;

import java.util.List;

import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.CoordinateConvert;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.jy.environment.map.PicturePointImageItem;

/**
 * 地图显示用户分享图片的相关数据对象
 * @author shang
 *
 */
public class PicturePointItem {
	/**
	 * @param id 用户编号
	 * @param username 用户名
	 * @param province
	 * @param city
	 * @param b  经度
	 * @param l  纬度 
	 * @param area  分享的详细地理位置
	 * @param time  时间
	 * @param pic  图片列表
	 */
	public PicturePointItem(int id, String username,String province, String city, double b,
			double l, String area, String time, int postid,String post,List<PicturePointImageItem> pic) {
		super();
		this.id = id;
		this.username = username;
		this.province = province;
		this.city = city;
		this.b = b;
		this.l = l;
		this.area = area;
		this.time = time;
		this.postid = postid;
		this.post = post;
		this.pic = pic;
	}
	private int id = 0;
	private String username="";
	private String province = "";
	private String city = "";
	private double b = 0.0f;
	private double l = 0.0f;
	private String area = "";
	private String time = "";
	private int postid = 0;
	private String post = "";
	private List<PicturePointImageItem> pic=null;
	/**
	 * 获取百度格式的坐标
	 * @return
	 */
	public LatLng getbaiduptLoc()
	{
		LatLng pt = new LatLng(l,b);
//		pt = CoordinateConvert.fromGcjToBaidu(pt);
		return pt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getL() {
		return l;
	}
	public void setL(double l) {
		this.l = l;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<PicturePointImageItem> getPic() {
		return pic;
	}
	public void setPic(List<PicturePointImageItem> pic) {
		this.pic = pic;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public int getPostid() {
		return postid;
	}
	public void setPostid(int postid) {
		this.postid = postid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
