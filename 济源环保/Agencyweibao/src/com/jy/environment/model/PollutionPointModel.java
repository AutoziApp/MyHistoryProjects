package com.jy.environment.model;

import java.util.Date;

public class PollutionPointModel {

	private String name;
	private String type;
	private String 	sort;
	private String city;
	private String district;
	private String address;
	private String lat;
	private String lng;
	private String usid;
	private Date updateTime;
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCity() {
		return city.replace("市", "").replace("地区", "");
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
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
	public String getUsid() {
		return usid;
	}
	public void setUsid(String usid) {
		this.usid = usid;
	}
	public PollutionPointModel(String name, String type, String sort,
			String city, String district, String address, String lat,
			String lng, String usid) {
		super();
		this.name = name;
		this.type = type;
		this.sort = sort;
		this.city = city;
		this.district = district;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.usid = usid;
	}
	@Override
	public String toString() {
		return "PollutionPointModel [name=" + name + ", type=" + type
				+ ", sort=" + sort + ", city=" + city + ", district="
				+ district + ", address=" + address + ", lat=" + lat + ", lng="
				+ lng + ", usid=" + usid + "]";
	}
	
	
}
