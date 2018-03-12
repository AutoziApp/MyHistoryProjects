package com.jy.environment.model;

public class AlarmItemModel {
	private String province;
	private String town;
	private String title;
	private String message;
	private String time;
	private String url;
	private String alarm;
	
	public AlarmItemModel() {
		super();
	}
	
	public AlarmItemModel(String province, String town, String title,
			String message, String time, String url, String alarm) {
		super();
		this.province = province;
		this.town = town;
		this.title = title;
		this.message = message;
		this.time = time;
		this.url = url;
		this.alarm = alarm;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	
}