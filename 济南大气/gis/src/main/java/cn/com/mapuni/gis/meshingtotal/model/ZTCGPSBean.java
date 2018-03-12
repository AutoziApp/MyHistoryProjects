package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/22.
 */

public class ZTCGPSBean  implements Serializable{


    private String CPHM;
    private double Latitude;
    private double Longitude;
    private String SHGS;

    public ZTCGPSBean(String CPHM, double latitude, double longitude, String SHGS)

    {
        this.CPHM = CPHM;
        Latitude = latitude;
        Longitude = longitude;
        this.SHGS = SHGS;
    }

    public String getCPHM() {
        return CPHM;
    }

    public void setCPHM(String CPHM) {
        this.CPHM = CPHM;
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

    public String getSHGS() {
        return SHGS;
    }

    public void setSHGS(String SHGS) {
        this.SHGS = SHGS;
    }
}
