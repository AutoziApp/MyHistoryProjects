package com.jy.environment.model;

public class AqiStationModel {

	// {"position":"万寿西宫","aqi":"127","pm25":"96","pm10":"102","no2":"71","so2":"2","co":"1.1","o3":"30"}

	private String position;
	

	private String stationcode;
	private String monidate;
	private int aqi;
	private String pm25;
	private String pm10;
	private String no2;
	private String so2;
	private String co;
	private String o3;
	public String getMonidate() {
		return monidate;
	}

	public void setMonidate(String monidate) {
		this.monidate = monidate;
	}
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	

	@Override
	public String toString() {
		return "AqiStationModel [position=" + position + ", stationcode=" + stationcode + ", monidate=" + monidate
				+ ", aqi=" + aqi + ", pm25=" + pm25 + ", pm10=" + pm10 + ", no2=" + no2 + ", so2=" + so2 + ", co=" + co
				+ ", o3=" + o3 + "]";
	}

	public AqiStationModel(String position, int aqi, String pm25, String pm10,
			String no2, String so2, String co, String o3) {
		super();
		this.position = position;
		this.aqi = aqi;
		this.pm25 = pm25;
		this.pm10 = pm10;
		this.no2 = no2;
		this.so2 = so2;
		this.co = co;
		this.o3 = o3;
	}

	public AqiStationModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStationcode() {
		return stationcode;
	}

	public void setStationcode(String stationcode) {
		this.stationcode = stationcode;
	}

}
