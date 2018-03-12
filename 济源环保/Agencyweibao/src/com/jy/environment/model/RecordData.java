package com.jy.environment.model;

import java.io.Serializable;


public class RecordData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userid;
	private String value;
	private String address;
	private String lat;
	private String lng;
	private String update_time;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public RecordData(String userid, String value, String address, String lat,
			String lng, String update_time) {
		super();
		this.userid = userid;
		this.value = value;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.update_time = update_time;
	}
	
	

	
	
	
	
	

}
