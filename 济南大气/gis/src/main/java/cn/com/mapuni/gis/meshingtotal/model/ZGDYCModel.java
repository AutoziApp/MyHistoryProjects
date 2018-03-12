package cn.com.mapuni.gis.meshingtotal.model;

/**
 * Created by Administrator on 2017/4/20.
 */

public class ZGDYCModel {
    public ZGDYCModel() {
    }

    public ZGDYCModel(String polSorCode, String entName, double latitude, double longitude, String noise, String PM10) {
        PolSorCode = polSorCode;
        EntName = entName;
        Latitude = latitude;
        Longitude = longitude;
        this.noise = noise;
        this.PM10 = PM10;
    }

    /**
     * PolSorCode : 1211000984
     * EntName : 淄博路-烟台路路口
     * Latitude : 36.66923
     * Longitude : 116.92177
     * noise : 56.0
     * PM10 : 62.0
     */

    private String PolSorCode;
    private String EntName;
    private double Latitude;
    private double Longitude;
    private String noise;
    private String PM10;

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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }
}
