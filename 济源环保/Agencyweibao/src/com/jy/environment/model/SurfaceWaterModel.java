package com.jy.environment.model;

import java.util.Date;

public class SurfaceWaterModel {

	String target_quality;
	String code;
	String mow_quality;
	String monitor_point;
	String baidu_lng_d;
	String baidu_lat_d;
	String c_function;
	String c_river;
	String c_water;
	String city;
	String resource;
	String r_time;
	String r_week;
	
	private Date updateTime;
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getTarget_quality() {
		return target_quality;
	}
	public void setTarget_quality(String target_quality) {
		this.target_quality = target_quality;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMow_quality() {
		return mow_quality;
	}
	public void setMow_quality(String mow_quality) {
		this.mow_quality = mow_quality;
	}
	public String getMonitor_point() {
		return monitor_point;
	}
	public void setMonitor_point(String monitor_point) {
		this.monitor_point = monitor_point;
	}
	public String getBaidu_lng_d() {
		return baidu_lng_d;
	}
	public void setBaidu_lng_d(String baidu_lng_d) {
		this.baidu_lng_d = baidu_lng_d;
	}
	public String getBaidu_lat_d() {
		return baidu_lat_d;
	}
	public void setBaidu_lat_d(String baidu_lat_d) {
		this.baidu_lat_d = baidu_lat_d;
	}
	public String getC_function() {
		return c_function;
	}
	public void setC_function(String c_function) {
		this.c_function = c_function;
	}
	public String getC_river() {
		return c_river;
	}
	public void setC_river(String c_river) {
		this.c_river = c_river;
	}
	public String getC_water() {
		return c_water;
	}
	public void setC_water(String c_water) {
		this.c_water = c_water;
	}
	public String getCity() {
		return city.replace("市", "").replace("地区", "");
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getR_time() {
		return r_time;
	}
	public void setR_time(String r_time) {
		this.r_time = r_time;
	}
	public String getR_week() {
		return r_week;
	}
	public void setR_week(String r_week) {
		this.r_week = r_week;
	}
	public SurfaceWaterModel(String target_quality, String code,
			String mow_quality, String monitor_point, String baidu_lng_d,
			String baidu_lat_d, String c_function, String c_river,
			String c_water, String city, String resource, String r_time,
			String r_week) {
		super();
		this.target_quality = target_quality;
		this.code = code;
		this.mow_quality = mow_quality;
		this.monitor_point = monitor_point;
		this.baidu_lng_d = baidu_lng_d;
		this.baidu_lat_d = baidu_lat_d;
		this.c_function = c_function;
		this.c_river = c_river;
		this.c_water = c_water;
		this.city = city;
		this.resource = resource;
		this.r_time = r_time;
		this.r_week = r_week;
	}
	@Override
	public String toString() {
		return "SurfaceWaterModel [target_quality=" + target_quality
				+ ", code=" + code + ", mow_quality=" + mow_quality
				+ ", monitor_point=" + monitor_point + ", baidu_lng_d="
				+ baidu_lng_d + ", baidu_lat_d=" + baidu_lat_d
				+ ", c_function=" + c_function + ", c_river=" + c_river
				+ ", c_water=" + c_water + ", city=" + city + ", resource="
				+ resource + ", r_time=" + r_time + ", r_week=" + r_week
				+ ", updateTime=" + updateTime + "]";
	}
	
	
}
