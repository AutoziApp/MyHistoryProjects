package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/20.
 */

public class AriQualityBean implements Serializable {


    private String PositionName;
    private double Longitude;
    private double Latitude;
    private int AQI;
    private String PrimaryPollutant;
    private int  StationCode;

    public AriQualityBean(String positionName, double longitude, double latitude, int AQI, String primaryPollutant,int StationCode) {
        PositionName = positionName;
        Longitude = longitude;
        Latitude = latitude;
        this.AQI = AQI;
        PrimaryPollutant = primaryPollutant;
        this.StationCode=StationCode;
    }

    public int getStationCode() {
        return StationCode;
    }

    public void setStationCode(int stationCode) {
        StationCode = stationCode;
    }

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String PositionName) {
        this.PositionName = PositionName;
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

    public int getAQI() {
        return AQI;
    }

    public void setAQI(int AQI) {
        this.AQI = AQI;
    }

    public String getPrimaryPollutant() {
        return PrimaryPollutant;
    }

    public void setPrimaryPollutant(String PrimaryPollutant) {
        this.PrimaryPollutant = PrimaryPollutant;
    }
}
