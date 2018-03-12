package com.jy.environment.model;

public class PM25 {
	private int pm25 = 0;

	

	private String time;

	public PM25() {
		super();
	}
	
	public PM25(int pm25, String time) {
		super();
		this.pm25 = pm25;
		this.time = time;
	}

	public int getPm25() {
		return pm25;
	}

	public void setPm25(int pm25) {
		this.pm25 = pm25;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "PM25Info24H [pm25=" + pm25 + ", time=" + time + "]";
	}
}
