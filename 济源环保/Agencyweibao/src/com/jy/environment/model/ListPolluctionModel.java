package com.jy.environment.model;

public class ListPolluctionModel {
	private String time;
	private int aqi;
	private int pm25;
	private int pm10;
	private int so2;
	private int no2;
	private int o3;
	private double co;
	private String airlevel;
	private String primarypollutant;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public int getPm25() {
		return pm25;
	}

	public void setPm25(int pm25) {
		this.pm25 = pm25;
	}

	public int getPm10() {
		return pm10;
	}

	public void setPm10(int pm10) {
		this.pm10 = pm10;
	}

	public int getSo2() {
		return so2;
	}

	public void setSo2(int so2) {
		this.so2 = so2;
	}

	public int getNo2() {
		return no2;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public int getO3() {
		return o3;
	}

	public void setO3(int o3) {
		this.o3 = o3;
	}

	public double getCo() {
		return co;
	}

	public void setCo(double co) {
		this.co = co;
	}

	public String getAirlevel() {
		return airlevel;
	}

	public void setAirlevel(String airlevel) {
		this.airlevel = airlevel;
	}

	public String getPrimarypollutant() {
		return primarypollutant;
	}

	public void setPrimarypollutant(String primarypollutant) {
		this.primarypollutant = primarypollutant;
	}

	@Override
	public String toString() {
		return "ListPolluctionModel [time=" + time + ", aqi=" + aqi + ", pm25="
				+ pm25 + ", pm10=" + pm10 + ", so2=" + so2 + ", no2=" + no2
				+ ", o3=" + o3 + ", co=" + co + ", airlevel=" + airlevel
				+ ", primarypollutant=" + primarypollutant + "]";
	}

}