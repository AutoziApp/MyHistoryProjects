package com.jy.environment.model;

public class AqiModel {
	private String CITY;
	private String AIRLEVEL;
	private String PM10;
	private String PM25;
	private String O3;
	private String CNT;
	private String AQI;
	private String PRIMARYPOLLUTANT;
	private String MONIDATE;
	private String type;
	private String year;
	private String nowyear;
	private String time;
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public String getAIRLEVEL() {
		return AIRLEVEL;
	}
	public void setAIRLEVEL(String aIRLEVEL) {
		AIRLEVEL = aIRLEVEL;
	}
	public String getPM10() {
		return PM10;
	}
	public String getNowyear() {
		return nowyear;
	}
	public void setNowyear(String nowyear) {
		this.nowyear = nowyear;
	}
	public void setPM10(String pM10) {
		PM10 = pM10;
	}
	public String getPM25() {
		return PM25;
	}
	public void setPM25(String pM25) {
		PM25 = pM25;
	}
	public String getO3() {
		return O3;
	}
	public void setO3(String o3) {
		O3 = o3;
	}
	public String getCNT() {
		return CNT;
	}
	public void setCNT(String cNT) {
		CNT = cNT;
	}
	public String getAQI() {
		return AQI;
	}
	public void setAQI(String aQI) {
		AQI = aQI;
	}
	public String getPRIMARYPOLLUTANT() {
		return PRIMARYPOLLUTANT;
	}
	public void setPRIMARYPOLLUTANT(String pRIMARYPOLLUTANT) {
		PRIMARYPOLLUTANT = pRIMARYPOLLUTANT;
	}
	public String getMONIDATE() {
		return MONIDATE;
	}
	public void setMONIDATE(String mONIDATE) {
		MONIDATE = mONIDATE;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AqiModel [CITY=" + CITY + ", AIRLEVEL=" + AIRLEVEL + ", PM10="
				+ PM10 + ", PM25=" + PM25 + ", O3=" + O3 + ", CNT=" + CNT
				+ ", AQI=" + AQI + ", PRIMARYPOLLUTANT=" + PRIMARYPOLLUTANT
				+ ", MONIDATE=" + MONIDATE + ", type=" + type + ", year="
				+ year + "]";
	}

	
	
}
