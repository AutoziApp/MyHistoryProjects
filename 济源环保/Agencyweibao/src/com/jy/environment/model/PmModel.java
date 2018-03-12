package com.jy.environment.model;

public class PmModel {
	private String TIME;
	private String PM25;
	private String PM10;
	private String O3;
	private String SO2;
	private String NO2;
	private String CO;
	private String Wind;
	private String shiDu;
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String tIME) {
		TIME = tIME;
	}
	public String getPM25() {
		return PM25;
	}
	public void setPM25(String pM25) {
		PM25 = pM25;
	}
	public String getPM10() {
		return PM10;
	}
	public void setPM10(String pM10) {
		PM10 = pM10;
	}
	public String getO3() {
		return O3;
	}
	public void setO3(String o3) {
		O3 = o3;
	}
	public String getSO2() {
		return SO2;
	}
	public void setSO2(String sO2) {
		SO2 = sO2;
	}
	public String getNO2() {
		return NO2;
	}
	public void setNO2(String nO2) {
		NO2 = nO2;
	}
	public String getCO() {
		return CO;
	}
	public void setCO(String cO) {
		CO = cO;
	}
	
	public String getWind() {
		return Wind;
	}
	public void setWind(String wind) {
		Wind = wind;
	}
	public String getShiDu() {
		return shiDu;
	}
	public void setShiDu(String shiDu) {
		this.shiDu = shiDu;
	}
	@Override
	public String toString() {
		return "PmModel [TIME=" + TIME + ", PM25=" + PM25 + ", PM10=" + PM10
				+ ", O3=" + O3 + ", SO2=" + SO2 + ", NO2=" + NO2 + ", CO=" + CO
				+ "]";
	}
	
}
