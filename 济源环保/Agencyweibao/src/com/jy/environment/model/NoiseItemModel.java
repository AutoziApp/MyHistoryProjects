package com.jy.environment.model;

public class NoiseItemModel {
	private String id;
	private String user_id;
	private String value;
	private String address;
	private String longitude;
	private String latitude;
	private String update_time;
	private String create_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		return "NoiseItemModel [id=" + id + ", user_id=" + user_id + ", value="
				+ value + ", address=" + address + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", update_time=" + update_time
				+ ", create_time=" + create_time + "]";
	}
	
	
	

}
