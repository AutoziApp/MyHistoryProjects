package com.jy.environment.model;

public class SO2 {
	private int so2 = 0;

	

	private String time;

	public SO2() {
		super();
	}
	
	public SO2(int so2, String time) {
		super();
		this.so2 = so2;
		this.time = time;
	}

	public int getSo2() {
		return so2;
	}

	public void setSo2(int so2) {
		this.so2 = so2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "SO2Info24H [so2=" + so2 + ", time=" + time + "]";
	}
	

}
