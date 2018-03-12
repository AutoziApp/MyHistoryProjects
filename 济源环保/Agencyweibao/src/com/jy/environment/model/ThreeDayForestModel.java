package com.jy.environment.model;

public class ThreeDayForestModel {
	private String CITY;
	private String SO2;
	private String SO2IAQI;
	private String NO2;
	private String NO2IAQI;
	private String CO;
	private String COIAQI;
	private String O3;
	private String O3IAQI;
	private String O38H;
	private String O38HIAQI;
	private String PM25;
	private String PM25IAQI;
	private String PM10;
	private String AQI;
	private String PRIMARYPOLLUTANT;
	private String AIRLEVEL;
	private String MONITORTIME;
	private String FORECASTTIME;
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public String getSO2() {
		return SO2;
	}
	public void setSO2(String sO2) {
		SO2 = sO2;
	}
	public String getSO2IAQI() {
		return SO2IAQI;
	}
	public void setSO2IAQI(String sO2IAQI) {
		SO2IAQI = sO2IAQI;
	}
	public String getNO2() {
		return NO2;
	}
	public void setNO2(String nO2) {
		NO2 = nO2;
	}
	public String getNO2IAQI() {
		return NO2IAQI;
	}
	public void setNO2IAQI(String nO2IAQI) {
		NO2IAQI = nO2IAQI;
	}
	public String getCO() {
		return CO;
	}
	public void setCO(String cO) {
		CO = cO;
	}
	public String getCOIAQI() {
		return COIAQI;
	}
	public void setCOIAQI(String cOIAQI) {
		COIAQI = cOIAQI;
	}
	public String getO3() {
		return O3;
	}
	public void setO3(String o3) {
		O3 = o3;
	}
	public String getO3IAQI() {
		return O3IAQI;
	}
	public void setO3IAQI(String o3iaqi) {
		O3IAQI = o3iaqi;
	}
	public String getO38H() {
		return O38H;
	}
	public void setO38H(String o38h) {
		O38H = o38h;
	}
	public String getO38HIAQI() {
		return O38HIAQI;
	}
	public void setO38HIAQI(String o38hiaqi) {
		O38HIAQI = o38hiaqi;
	}
	public String getPM25() {
		return PM25;
	}
	public void setPM25(String pM25) {
		PM25 = pM25;
	}
	public String getPM25IAQI() {
		return PM25IAQI;
	}
	public void setPM25IAQI(String pM25IAQI) {
		PM25IAQI = pM25IAQI;
	}
	public String getPM10() {
		return PM10;
	}
	public void setPM10(String pM10) {
		PM10 = pM10;
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
	public String getAIRLEVEL() {
		return AIRLEVEL;
	}
	public void setAIRLEVEL(String aIRLEVEL) {
		AIRLEVEL = aIRLEVEL;
	}
	public String getMONITORTIME() {
		return MONITORTIME;
	}
	public void setMONITORTIME(String mONITORTIME) {
		MONITORTIME = mONITORTIME;
	}
	public String getFORECASTTIME() {
		return FORECASTTIME;
	}
	public void setFORECASTTIME(String fORECASTTIME) {
		FORECASTTIME = fORECASTTIME;
	}
	@Override
	public String toString() {
		return "ThreeDayForestModel [CITY=" + CITY + ", SO2=" + SO2
				+ ", SO2IAQI=" + SO2IAQI + ", NO2=" + NO2 + ", NO2IAQI="
				+ NO2IAQI + ", CO=" + CO + ", COIAQI=" + COIAQI + ", O3=" + O3
				+ ", O3IAQI=" + O3IAQI + ", O38H=" + O38H + ", O38HIAQI="
				+ O38HIAQI + ", PM25=" + PM25 + ", PM25IAQI=" + PM25IAQI
				+ ", PM10=" + PM10 + ", AQI=" + AQI + ", PRIMARYPOLLUTANT="
				+ PRIMARYPOLLUTANT + ", AIRLEVEL=" + AIRLEVEL
				+ ", MONITORTIME=" + MONITORTIME + ", FORECASTTIME="
				+ FORECASTTIME + "]";
	}

}
