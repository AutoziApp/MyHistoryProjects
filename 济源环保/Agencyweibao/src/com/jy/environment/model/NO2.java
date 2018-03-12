package com.jy.environment.model;

public class NO2 {
	private int no2 = 0;

	

	private String time;

	public NO2() {
		super();
	}
	
	public NO2(int no2, String time) {
		super();
		this.no2 = no2;
		this.time = time;
	}

	public int getNo2() {
		return no2;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "NO2Info24H [so2=" + no2 + ", time=" + time + "]";
	}
	

}
