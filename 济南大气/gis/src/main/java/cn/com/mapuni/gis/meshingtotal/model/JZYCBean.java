package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/21.
 */

public class JZYCBean implements Serializable {


    private String EntName;
    private double Longitude;
    private double Latitude;
    private String Address;
    private String PolSorCode1;
    private String PolluteRate;

    public JZYCBean(String entName, double longitude, double latitude, String address, String polSorCode1, String polluteRate) {
        EntName = entName;
        Longitude = longitude;
        Latitude = latitude;
        Address = address;
        PolSorCode1 = polSorCode1;
        PolluteRate = polluteRate;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPolSorCode1() {
        return PolSorCode1;
    }

    public void setPolSorCode1(String PolSorCode1) {
        this.PolSorCode1 = PolSorCode1;
    }

    public String getPolluteRate() {
        return PolluteRate;
    }

    public void setPolluteRate(String PolluteRate) {
        this.PolluteRate = PolluteRate;
    }
}
