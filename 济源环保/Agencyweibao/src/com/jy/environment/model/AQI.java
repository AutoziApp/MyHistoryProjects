package com.jy.environment.model;

public class AQI {
	private int aqi = 0;
	private String time;
	public AQI(int aqi, String time) {
		super();
		this.aqi = aqi;
		this.time = time;
	}
	public AQI() {
		super();
	}
	public int getAqi() {
		return aqi;
	}
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AQI [aqi=" + aqi + ", time=" + time + ", getAqi()=" + getAqi() + ", getTime()=" + getTime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
}
