package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/21.
 */

public class ZTYCBean implements Serializable {

    private double Longitude;
    private double Latitude;
    private String EntName;
    private int PM10;
    private int PM2P5;
public ZTYCBean(double Longitude,double Latitude,String EntName,int PM10,int PM2P5){
    this.Longitude=Longitude;
    this.Latitude=Latitude;
    this.EntName=EntName;
    this.PM10=PM10;
    this.PM2P5=PM2P5;


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


    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }
    public int getPM10() {
        return PM10;
    }

    public void setPM10(int PM10) {
        this.PM10 = PM10;
    }

    public int getPM2P5() {
        return PM2P5;
    }

    public void setPM2P5(int PM2P5) {
        this.PM2P5 = PM2P5;
    }

}
