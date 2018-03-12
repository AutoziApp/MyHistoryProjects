package com.jy.environment.model;

import java.util.List;

public class MonitorModel {
	private String CITYCODE;
	private String MONIDATE;
	private int SO2;
	private int NO2;
	private double CO;
	private int O3;
	private int PM25;
	private int PM10;
	private int AQI;
	private String PRIMARYPOLLUTANT;
	private String AIRLEVEL;
	private String STATIONTYPE;
	private List<WeatherInfo24> weatherInfo24s;
	private List<PM10Info24H> pm10Info24Hs;
	private List<PM25> pm25Info24Hs;
	private List<SO2> so2Info24Hs;
	private List<NO2> no2Info24Hs;
	public String getCITYCODE() {
		return CITYCODE;
	}
	public void setCITYCODE(String cITYCODE) {
		CITYCODE = cITYCODE;
	}
	public String getMONIDATE() {
		return MONIDATE;
	}
	public void setMONIDATE(String mONIDATE) {
		MONIDATE = mONIDATE;
	}
	public int getSO2() {
		return SO2;
	}
	public void setSO2(int sO2) {
		SO2 = sO2;
	}
	public int getNO2() {
		return NO2;
	}
	public void setNO2(int nO2) {
		NO2 = nO2;
	}
	public double getCO() {
		return CO;
	}
	public void setCO(double cO) {
		CO = cO;
	}
	public int getO3() {
		return O3;
	}
	public void setO3(int o3) {
		O3 = o3;
	}
	public int getPM25() {
		return PM25;
	}
	public void setPM25(int pM25) {
		PM25 = pM25;
	}
	public int getPM10() {
		return PM10;
	}
	public void setPM10(int pM10) {
		PM10 = pM10;
	}
	public int getAQI() {
		return AQI;
	}
	public void setAQI(int aQI) {
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
	public String getSTATIONTYPE() {
		return STATIONTYPE;
	}
	public void setSTATIONTYPE(String sTATIONTYPE) {
		STATIONTYPE = sTATIONTYPE;
	}
	public List<WeatherInfo24> getWeatherInfo24s() {
		return weatherInfo24s;
	}
	public void setWeatherInfo24s(List<WeatherInfo24> weatherInfo24s) {
		this.weatherInfo24s = weatherInfo24s;
	}
	public List<PM10Info24H> getPm10Info24Hs() {
		return pm10Info24Hs;
	}
	public void setPm10Info24Hs(List<PM10Info24H> pm10Info24Hs) {
		this.pm10Info24Hs = pm10Info24Hs;
	}
	public List<PM25> getPm25Info24Hs() {
		return pm25Info24Hs;
	}
	public void setPm25Info24Hs(List<PM25> pm25Info24Hs) {
		this.pm25Info24Hs = pm25Info24Hs;
	}
	public List<SO2> getSO2Info24Hs() {
		return so2Info24Hs;
	}
	public void setSO2Info24Hs(List<SO2> so2Info24Hs) {
		this.so2Info24Hs = so2Info24Hs;
	}
	public List<NO2> getNO2Info24Hs() {
		return no2Info24Hs;
	}
	public void setNO2Info24Hs(List<NO2> no2Info24Hs) {
		this.no2Info24Hs = no2Info24Hs;
	}
	public MonitorModel() {
		super();
	}
	public MonitorModel(String cITYCODE, String mONIDATE, int sO2, int nO2, double cO, int o3, int pM25, int pM10,
			int aQI, String pRIMARYPOLLUTANT, String aIRLEVEL, String sTATIONTYPE, List<WeatherInfo24> weatherInfo24s,
			List<PM10Info24H> pm10Info24Hs, List<PM25> pm25Info24Hs) {
		super();
		CITYCODE = cITYCODE;
		MONIDATE = mONIDATE;
		SO2 = sO2;
		NO2 = nO2;
		CO = cO;
		O3 = o3;
		PM25 = pM25;
		PM10 = pM10;
		AQI = aQI;
		PRIMARYPOLLUTANT = pRIMARYPOLLUTANT;
		AIRLEVEL = aIRLEVEL;
		STATIONTYPE = sTATIONTYPE;
		this.weatherInfo24s = weatherInfo24s;
		this.pm10Info24Hs = pm10Info24Hs;
		this.pm25Info24Hs = pm25Info24Hs;
	}
	@Override
	public String toString() {
		return "MonitorModel [CITYCODE=" + CITYCODE + ", MONIDATE=" + MONIDATE + ", SO2=" + SO2 + ", NO2=" + NO2
				+ ", CO=" + CO + ", O3=" + O3 + ", PM25=" + PM25 + ", PM10=" + PM10 + ", AQI=" + AQI
				+ ", PRIMARYPOLLUTANT=" + PRIMARYPOLLUTANT + ", AIRLEVEL=" + AIRLEVEL + ", STATIONTYPE=" + STATIONTYPE
				+ ", weatherInfo24s=" + weatherInfo24s + ", pm10Info24Hs=" + pm10Info24Hs + ", pm25Info24Hs="
				+ pm25Info24Hs + "]";
	}

	
}
