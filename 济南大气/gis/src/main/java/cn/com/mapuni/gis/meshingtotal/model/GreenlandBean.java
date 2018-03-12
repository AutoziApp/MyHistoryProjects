package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/22.
 */

public class GreenlandBean implements Serializable {
    private String LandName;
    private String Address;
    private double Longitude;
    private double Latitude;
    private String LandRights;
    private String LandType;
    private String LandSize;
    private String GreenStartTime;
    private String GreenEndTime;
    private String GreenType;

    public GreenlandBean(String landName, String address, double longitude, double latitude, String landRights, String landType, String landSize, String greenStartTime, String greenEndTime, String greenType) {
        LandName = landName;
        Address = address;
        Longitude = longitude;
        Latitude = latitude;
        LandRights = landRights;
        LandType = landType;
        LandSize = landSize;
        GreenStartTime = greenStartTime;
        GreenEndTime = greenEndTime;
        GreenType = greenType;
    }

    public String getLandName() {
        return LandName;
    }

    public void setLandName(String LandName) {
        this.LandName = LandName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
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

    public String getLandRights() {
        return LandRights;
    }

    public void setLandRights(String LandRights) {
        this.LandRights = LandRights;
    }

    public String getLandType() {
        return LandType;
    }

    public void setLandType(String LandType) {
        this.LandType = LandType;
    }

    public String getLandSize() {
        return LandSize;
    }

    public void setLandSize(String LandSize) {
        this.LandSize = LandSize;
    }

    public String getGreenStartTime() {
        return GreenStartTime;
    }

    public void setGreenStartTime(String GreenStartTime) {
        this.GreenStartTime = GreenStartTime;
    }

    public String getGreenEndTime() {
        return GreenEndTime;
    }

    public void setGreenEndTime(String GreenEndTime) {
        this.GreenEndTime = GreenEndTime;
    }

    public String getGreenType() {
        return GreenType;
    }

    public void setGreenType(String GreenType) {
        this.GreenType = GreenType;
    }
}
