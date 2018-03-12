package com.jy.environment.model;

public class WeatherInfo7_tian {
	private int aqi=0;
	private String update_time="";
	public int getAqi() {
		return aqi;
	}
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
	    return "WeatherInfo7_tian [aqi=" + aqi + ", update_time="
		    + update_time + "]";
	}
	
	
}
