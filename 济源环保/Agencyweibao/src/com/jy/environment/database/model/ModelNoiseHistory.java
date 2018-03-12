package com.jy.environment.database.model;

public class ModelNoiseHistory {
	private long mID;
	private String time;
	private int phoneNumber;
	private String location;//未定位时为null
	private String imei;
	private String mResult; // 存放监测值
	private String userName;
	private String userId;
	private String longitude;
	private String latitude;
	private String isupload;
	public long getmID() {
		return mID;
	}
	public void setmID(long mID) {
		this.mID = mID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getmResult() {
		return mResult;
	}
	public void setmResult(String mResult) {
		this.mResult = mResult;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public String getIsupload() {
		return isupload;
	}
	public void setIsupload(String isupload) {
		this.isupload = isupload;
	}
	@Override
	public String toString() {
		return "ModelNoiseHistory [mID=" + mID + ", time=" + time
				+ ", phoneNumber=" + phoneNumber + ", location=" + location
				+ ", imei=" + imei + ", mResult=" + mResult + ", userName="
				+ userName + ", userId=" + userId + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", isupload=" + isupload + "]";
	}

	
	
	
	
	
}
