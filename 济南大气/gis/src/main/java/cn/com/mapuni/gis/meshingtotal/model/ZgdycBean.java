package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/21.
 */

public class ZgdycBean implements Serializable {


    private String PolSorCode;
    private String EntName;
    private double Latitude;
    private double Longitude;
    private String noise;
    private String PM10;

    public ZgdycBean(String entName, double latitude, double longitude, String noise, String PM10) {
        this.EntName = entName;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.noise = noise;
        this.PM10 = PM10;
    }

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
