package com.jy.environment.model;

public class WeatherInfoYear {

	
	private int aqi=0;
	private String level="";
	private String update_time="";
	public int getAqi() {
		return aqi;
	}
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "WeatherInfoYear [aqi=" + aqi + ", level=" + level
				+ ", update_time=" + update_time + "]";
	}
	
		
}
