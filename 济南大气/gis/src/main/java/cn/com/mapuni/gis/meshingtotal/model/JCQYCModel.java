package cn.com.mapuni.gis.meshingtotal.model;

/**
 * Created by Administrator on 2017/4/19.
 */

public class JCQYCModel {

    public JCQYCModel() {
    }

    public JCQYCModel(String polSorCode, String entName, double longitude, double latitude, double PM2P5, double PM10) {
        PolSorCode = polSorCode;
        EntName = entName;
        Longitude = longitude;
        Latitude = latitude;
        this.PM2P5 = PM2P5;
        this.PM10 = PM10;
    }

    /**
     * PolSorCode : 1012
     * EntName : 顺河高架工地监测点
     * Longitude : 117.015013
     * Latitude : 36.625238
     * PM2P5 : 68.8
     * PM10 : 95.5
     */

    private String PolSorCode;
    private String EntName;
    private double Longitude;
    private double Latitude;
    private double PM2P5;
    private double PM10;

    public String getPolSorCode() {
        return PolSorCode;
    }

    public void setPolSorCode(String PolSorCode) {
        this.PolSorCode = PolSorCode;
    }

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public double getPM2P5() {
        return PM2P5;
    }

    public void setPM2P5(double PM2P5) {
        this.PM2P5 = PM2P5;
    }

    public double getPM10() {
        return PM10;
    }

    public void setPM10(double PM10) {
        this.PM10 = PM10;
    }
}
