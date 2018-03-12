package com.jy.environment.model;

import java.util.Date;

public class ProvincePoint {

	private String REGIONNAME;
    private String STATIONNAME;
    private double LONGITUDE;
    private double LATITUDE;
    private int AQI;
    private int SO2;
    private int NO2;
    private double CO;
    private int O3;
    private int PM10;
    private int PM25;

    public String getREGIONNAME() {
        return REGIONNAME;
    }

    public void setREGIONNAME(String REGIONNAME) {
        this.REGIONNAME = REGIONNAME;
    }

    public String getSTATIONNAME() {
        return STATIONNAME;
    }

    public void setSTATIONNAME(String STATIONNAME) {
        this.STATIONNAME = STATIONNAME;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public int getAQI() {
        return AQI;
    }

    public void setAQI(int AQI) {
        this.AQI = AQI;
    }

    public int getSO2() {
        return SO2;
    }

    public void setSO2(int SO2) {
        this.SO2 = SO2;
    }

    public int getNO2() {
        return NO2;
    }

    public void setNO2(int NO2) {
        this.NO2 = NO2;
    }

    public double getCO() {
        return CO;
    }

    public void setCO(double CO) {
        this.CO = CO;
    }

    public int getO3() {
        return O3;
    }

    public void setO3(int O3) {
        this.O3 = O3;
    }

    public int getPM10() {
        return PM10;
    }

    public void setPM10(int PM10) {
        this.PM10 = PM10;
    }

    public int getPM25() {
        return PM25;
    }

    public void setPM25(int PM25) {
        this.PM25 = PM25;
    }

	@Override
	public String toString() {
		return "ProvincePoint [REGIONNAME=" + REGIONNAME + ", STATIONNAME=" + STATIONNAME + ", LONGITUDE=" + LONGITUDE
				+ ", LATITUDE=" + LATITUDE + ", AQI=" + AQI + ", SO2=" + SO2 + ", NO2=" + NO2 + ", CO=" + CO + ", O3="
				+ O3 + ", PM10=" + PM10 + ", PM25=" + PM25 + "]";
	}
	
}
