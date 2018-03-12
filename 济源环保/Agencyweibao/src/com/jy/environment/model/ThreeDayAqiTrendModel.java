package com.jy.environment.model;

public class ThreeDayAqiTrendModel {
	private String CITY;
	private String aqi;
	private int MINAIRLEVEL;
	private int MAXAIRLEVEL;
	private String MONITORTIME;
	private String FORECASTTIME;
	private String MINAIRLEVEL1;
	private String MAXAIRLEVEL1;
	
	

	


	public String getMINAIRLEVEL1() {
		return MINAIRLEVEL1;
	}

	public void setMINAIRLEVEL1(String mINAIRLEVEL1) {
		MINAIRLEVEL1 = mINAIRLEVEL1;
	}

	public String getMAXAIRLEVEL1() {
		return MAXAIRLEVEL1;
	}

	public void setMAXAIRLEVEL1(String mAXAIRLEVEL1) {
		MAXAIRLEVEL1 = mAXAIRLEVEL1;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
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
		return "ThreeDayAqiTrendModel [CITY=" + CITY + ", MINAIRLEVEL="
				+ MINAIRLEVEL + ", MAXAIRLEVEL=" + MAXAIRLEVEL
				+ ", MONITORTIME=" + MONITORTIME + ", FORECASTTIME="
				+ FORECASTTIME + "]";
	}

	public int getMINAIRLEVEL() {
		return MINAIRLEVEL;
	}

	public void setMINAIRLEVEL(int mINAIRLEVEL) {
		MINAIRLEVEL = mINAIRLEVEL;
	}

	public int getMAXAIRLEVEL() {
		return MAXAIRLEVEL;
	}

	public void setMAXAIRLEVEL(int mAXAIRLEVEL) {
		MAXAIRLEVEL = mAXAIRLEVEL;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	
}
