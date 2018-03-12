package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/20.
 */

public class PollutionSourceBean implements Serializable {
    private String Entname;
    private String name;
    private double Longitude;
    private double Latitude;
    private double SO2;
    private double NO;
    private double CO;
    private double PM;
    private double O2;
    private double wGas;
    private double t_SO2;
    private double t_NO;
    private double t_CO;
    private double t_PM;
    private double t_O2;
    private double t_wGas;
    private String status;

    public PollutionSourceBean(String Entname,String name, double longitude, double latitude, double SO2, double NO, double CO, double PM, double o2, double wGas, double t_SO2, double t_NO, double t_CO, double t_PM, double t_O2, double t_wGas, String status) {
        this.Entname=Entname;
        this.name = name;
        Longitude = longitude;
        Latitude = latitude;
        this.SO2 = SO2;
        this.NO = NO;
        this.CO = CO;
        this.PM = PM;
        O2 = o2;
        this.wGas = wGas;
        this.t_SO2 = t_SO2;
        this.t_NO = t_NO;
        this.t_CO = t_CO;
        this.t_PM = t_PM;
        this.t_O2 = t_O2;
        this.t_wGas = t_wGas;
        this.status = status;

    }

    public String getEntname() {
        return Entname;
    }

    public void setEntname(String entname) {
        Entname = entname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getwGas() {
        return wGas;
    }

    public void setwGas(double wGas) {
        this.wGas = wGas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getSO2() {
        return SO2;
    }

    public void setSO2(double SO2) {
        this.SO2 = SO2;
    }

    public double getNO() {
        return NO;
    }

    public void setNO(double NO) {
        this.NO = NO;
    }

    public double getCO() {
        return CO;
    }

    public void setCO(double CO) {
        this.CO = CO;
    }

    public double getPM() {
        return PM;
    }

    public void setPM(double PM) {
        this.PM = PM;
    }

    public double getO2() {
        return O2;
    }

    public void setO2(double O2) {
        this.O2 = O2;
    }

    public double getWGas() {
        return wGas;
    }

    public void setWGas(double wGas) {
        this.wGas = wGas;
    }

    public double getT_SO2() {
        return t_SO2;
    }

    public void setT_SO2(double t_SO2) {
        this.t_SO2 = t_SO2;
    }

    public double getT_NO() {
        return t_NO;
    }

    public void setT_NO(double t_NO) {
        this.t_NO = t_NO;
    }

    public double getT_CO() {
        return t_CO;
    }

    public void setT_CO(double t_CO) {
        this.t_CO = t_CO;
    }

    public double getT_PM() {
        return t_PM;
    }

    public void setT_PM(double t_PM) {
        this.t_PM = t_PM;
    }

    public double getT_O2() {
        return t_O2;
    }

    public void setT_O2(double t_O2) {
        this.t_O2 = t_O2;
    }

    public double getT_wGas() {
        return t_wGas;
    }

    public void setT_wGas(double t_wGas) {
        this.t_wGas = t_wGas;
    }
}
