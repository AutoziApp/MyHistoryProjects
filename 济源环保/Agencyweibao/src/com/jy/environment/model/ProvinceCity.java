package com.jy.environment.model;

public class ProvinceCity {
	/**
     * CITY : 郑州市
     * ISPROCITY : 1
     * SO2 : 21
     * SO2IAQI : 7
     * NO2 : 32
     * NO2IAQI : 16
     * CO : 1.012
     * COIAQI : 11
     * O3 : 84
     * O3IAQI : 27
     * O38H : 0
     * O38HIAQI : 0
     * PM25 : 40
     * PM25IAQI : 57
     * PM10 : 189
     * AQI : 120
     * PRIMARYPOLLUTANT : PM10
     * AIRLEVEL : 轻度污染
     * MONIDATE : 2017-04-25 14:00:00
     */

    private String CITY;
    private int ISPROCITY;
    private String SO2;
    private int SO2IAQI;
    private String NO2;
    private int NO2IAQI;
    private String CO;
    private int COIAQI;
    private String O3;
    private int O3IAQI;
    private int O38H;
    private int O38HIAQI;
    private String PM25;
    private int PM25IAQI;
    private String PM10;
    private String AQI;
    private String PRIMARYPOLLUTANT;
    private String AIRLEVEL;
    private String MONIDATE;
	public ProvinceCity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProvinceCity(String cITY, int iSPROCITY, String sO2, int sO2IAQI, String nO2, int nO2IAQI, String cO,
			int cOIAQI, String o3, int o3iaqi, int o38h, int o38hiaqi, String pM25, int pM25IAQI, String pM10,
			String aQI, String pRIMARYPOLLUTANT, String aIRLEVEL, String mONIDATE) {
		super();
		CITY = cITY;
		ISPROCITY = iSPROCITY;
		SO2 = sO2;
		SO2IAQI = sO2IAQI;
		NO2 = nO2;
		NO2IAQI = nO2IAQI;
		CO = cO;
		COIAQI = cOIAQI;
		O3 = o3;
		O3IAQI = o3iaqi;
		O38H = o38h;
		O38HIAQI = o38hiaqi;
		PM25 = pM25;
		PM25IAQI = pM25IAQI;
		PM10 = pM10;
		AQI = aQI;
		PRIMARYPOLLUTANT = pRIMARYPOLLUTANT;
		AIRLEVEL = aIRLEVEL;
		MONIDATE = mONIDATE;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public int getISPROCITY() {
		return ISPROCITY;
	}
	public void setISPROCITY(int iSPROCITY) {
		ISPROCITY = iSPROCITY;
	}
	public String getSO2() {
		return SO2;
	}
	public void setSO2(String sO2) {
		SO2 = sO2;
	}
	public int getSO2IAQI() {
		return SO2IAQI;
	}
	public void setSO2IAQI(int sO2IAQI) {
		SO2IAQI = sO2IAQI;
	}
	public String getNO2() {
		return NO2;
	}
	public void setNO2(String nO2) {
		NO2 = nO2;
	}
	public int getNO2IAQI() {
		return NO2IAQI;
	}
	public void setNO2IAQI(int nO2IAQI) {
		NO2IAQI = nO2IAQI;
	}
	public String getCO() {
		return CO;
	}
	public void setCO(String cO) {
		CO = cO;
	}
	public int getCOIAQI() {
		return COIAQI;
	}
	public void setCOIAQI(int cOIAQI) {
		COIAQI = cOIAQI;
	}
	public String getO3() {
		return O3;
	}
	public void setO3(String o3) {
		O3 = o3;
	}
	public int getO3IAQI() {
		return O3IAQI;
	}
	public void setO3IAQI(int o3iaqi) {
		O3IAQI = o3iaqi;
	}
	public int getO38H() {
		return O38H;
	}
	public void setO38H(int o38h) {
		O38H = o38h;
	}
	public int getO38HIAQI() {
		return O38HIAQI;
	}
	public void setO38HIAQI(int o38hiaqi) {
		O38HIAQI = o38hiaqi;
	}
	public String getPM25() {
		return PM25;
	}
	public void setPM25(String pM25) {
		PM25 = pM25;
	}
	public int getPM25IAQI() {
		return PM25IAQI;
	}
	public void setPM25IAQI(int pM25IAQI) {
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
	public String getMONIDATE() {
		return MONIDATE;
	}
	public void setMONIDATE(String mONIDATE) {
		MONIDATE = mONIDATE;
	}
	@Override
	public String toString() {
		return "ProvinceCity [CITY=" + CITY + ", ISPROCITY=" + ISPROCITY + ", SO2=" + SO2 + ", SO2IAQI=" + SO2IAQI
				+ ", NO2=" + NO2 + ", NO2IAQI=" + NO2IAQI + ", CO=" + CO + ", COIAQI=" + COIAQI + ", O3=" + O3
				+ ", O3IAQI=" + O3IAQI + ", O38H=" + O38H + ", O38HIAQI=" + O38HIAQI + ", PM25=" + PM25 + ", PM25IAQI="
				+ PM25IAQI + ", PM10=" + PM10 + ", AQI=" + AQI + ", PRIMARYPOLLUTANT=" + PRIMARYPOLLUTANT
				+ ", AIRLEVEL=" + AIRLEVEL + ", MONIDATE=" + MONIDATE + "]";
	}
    
}
