package cn.com.mapuni.gis.meshingtotal.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/20.
 */

public class PollutionSourceBean1 implements Serializable{

    /**
     * PolSorCode : null
     * EntName : 济南天邦化工有限公司
     * Address : 济南玉泉生物药电有限公司西偏北136米附近
     * Latitude : 37.150774
     * Longitude : 117.124765
     * XZQYBM : null
     * QYZT : null
     * enterprisetypr : 工业企业
     * gridCode : null
     */

    private Object PolSorCode;
    private String EntName;
    private String Address;
    private double Latitude;
    private double Longitude;
    private Object XZQYBM;
    private Object QYZT;
    private String enterprisetypr;
    private Object gridCode;
    public PollutionSourceBean1(String EntName, double Latitude, double Longitude, String enterprisetypr){
        this.EntName=EntName;
        this.Latitude=Latitude;
        this.Longitude=Longitude;
        this.enterprisetypr=enterprisetypr;
    }

    public Object getPolSorCode() {
        return PolSorCode;
    }

    public void setPolSorCode(Object PolSorCode) {
        this.PolSorCode = PolSorCode;
    }

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
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

    public Object getXZQYBM() {
        return XZQYBM;
    }

    public void setXZQYBM(Object XZQYBM) {
        this.XZQYBM = XZQYBM;
    }

    public Object getQYZT() {
        return QYZT;
    }

    public void setQYZT(Object QYZT) {
        this.QYZT = QYZT;
    }

    public String getEnterprisetypr() {
        return enterprisetypr;
    }

    public void setEnterprisetypr(String enterprisetypr) {
        this.enterprisetypr = enterprisetypr;
    }

    public Object getGridCode() {
        return gridCode;
    }

    public void setGridCode(Object gridCode) {
        this.gridCode = gridCode;
    }
}
