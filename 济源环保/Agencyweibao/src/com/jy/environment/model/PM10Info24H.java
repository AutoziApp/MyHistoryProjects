package com.jy.environment.model;

public class PM10Info24H {
	private int pm10=0;
	private String time;
	public int getPm10() {
		return pm10;
	}
	public void setPm10(int pm10) {
		this.pm10 = pm10;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public PM10Info24H(int pm10, String time) {
		super();
		this.pm10 = pm10;
		this.time = time;
	}
	public PM10Info24H() {
		super();
	}
	@Override
	public String toString() {
		return "PM10Info24H [pm10=" + pm10 + ", time=" + time + "]";
	}
}
