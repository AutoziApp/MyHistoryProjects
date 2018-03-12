package com.mapuni.car.mvp.searchcar.model;

import java.util.Map;

public class CarSearchDetailBean {
    /**
     * isExist : 1
     * carInfo : {"pkid":"1511420549624_baixingong","STATIONPKID":"4201050031","CARPKID":"1456381265950_baixg","CARCARDNUMBER":"川A22233","fueltype":null,"oldcheckmethod":null,"newcheckmethod":null,"xgr":null,"xgsj":null,"xgyy":null,"fj1":null}
     */

    private int isExist;
    /**
     * pkid : 1511420549624_baixingong
     * STATIONPKID : 4201050031
     * CARPKID : 1456381265950_baixg
     * CARCARDNUMBER : 川A22233
     * fueltype : null
     * oldcheckmethod : null
     * newcheckmethod : null
     * xgr : null
     * xgsj : null
     * xgyy : null
     * fj1 : null
     */
   private String errInfo;

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    private Map<String,String> carInfo;

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public Map<String,String> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(Map<String,String> carInfo) {
        this.carInfo = carInfo;
    }

    public static class CarInfoBean {
        private String pkid;
        private String STATIONPKID;
        private String CARPKID;
        private String CARCARDNUMBER;
        private Object fueltype;
        private Object oldcheckmethod;
        private Object newcheckmethod;
        private Object xgr;
        private Object xgsj;
        private Object xgyy;
        private Object fj1;

        public String getPkid() {
            return pkid;
        }

        public void setPkid(String pkid) {
            this.pkid = pkid;
        }

        public String getSTATIONPKID() {
            return STATIONPKID;
        }

        public void setSTATIONPKID(String STATIONPKID) {
            this.STATIONPKID = STATIONPKID;
        }

        public String getCARPKID() {
            return CARPKID;
        }

        public void setCARPKID(String CARPKID) {
            this.CARPKID = CARPKID;
        }

        public String getCARCARDNUMBER() {
            return CARCARDNUMBER;
        }

        public void setCARCARDNUMBER(String CARCARDNUMBER) {
            this.CARCARDNUMBER = CARCARDNUMBER;
        }

        public Object getFueltype() {
            return fueltype;
        }

        public void setFueltype(Object fueltype) {
            this.fueltype = fueltype;
        }

        public Object getOldcheckmethod() {
            return oldcheckmethod;
        }

        public void setOldcheckmethod(Object oldcheckmethod) {
            this.oldcheckmethod = oldcheckmethod;
        }

        public Object getNewcheckmethod() {
            return newcheckmethod;
        }

        public void setNewcheckmethod(Object newcheckmethod) {
            this.newcheckmethod = newcheckmethod;
        }

        public Object getXgr() {
            return xgr;
        }

        public void setXgr(Object xgr) {
            this.xgr = xgr;
        }

        public Object getXgsj() {
            return xgsj;
        }

        public void setXgsj(Object xgsj) {
            this.xgsj = xgsj;
        }

        public Object getXgyy() {
            return xgyy;
        }

        public void setXgyy(Object xgyy) {
            this.xgyy = xgyy;
        }

        public Object getFj1() {
            return fj1;
        }

        public void setFj1(Object fj1) {
            this.fj1 = fj1;
        }
    }
}