package com.mapuni.car.mvp.ywrequest.model;

/**
 * Created by yawei on 2017/8/30.
 */

public class CarRequestDetailBean {


    /**
     * isExist : 1
     * checkInfo : {"pkid":null,"carcardnumber":"川AA1111","carCardColor":"blue","carversion":null,"vin":null,"ownername":null,"stationpkid":null,"stationname":null,"checkresult":null,"checktime":null}
     */

    private int isExist;
    private CheckInfoBean checkInfo;

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public CheckInfoBean getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(CheckInfoBean checkInfo) {
        this.checkInfo = checkInfo;
    }

    public static class CheckInfoBean {
        /**
         * pkid : null
         * carcardnumber : 川AA1111
         * carCardColor : blue
         * carversion : null
         * vin : null
         * ownername : null
         * stationpkid : null
         * stationname : null
         * checkresult : null
         * checktime : null
         */

        private Object pkid;
        private String carcardnumber;
        private String carCardColor;
        private Object carversion;
        private Object vin;
        private Object ownername;
        private Object stationpkid;
        private Object stationname;
        private Object checkresult;
        private Object checktime;

        public Object getPkid() {
            return pkid;
        }

        public void setPkid(Object pkid) {
            this.pkid = pkid;
        }

        public String getCarcardnumber() {
            return carcardnumber;
        }

        public void setCarcardnumber(String carcardnumber) {
            this.carcardnumber = carcardnumber;
        }

        public String getCarCardColor() {
            return carCardColor;
        }

        public void setCarCardColor(String carCardColor) {
            this.carCardColor = carCardColor;
        }

        public Object getCarversion() {
            return carversion;
        }

        public void setCarversion(Object carversion) {
            this.carversion = carversion;
        }

        public Object getVin() {
            return vin;
        }

        public void setVin(Object vin) {
            this.vin = vin;
        }

        public Object getOwnername() {
            return ownername;
        }

        public void setOwnername(Object ownername) {
            this.ownername = ownername;
        }

        public Object getStationpkid() {
            return stationpkid;
        }

        public void setStationpkid(Object stationpkid) {
            this.stationpkid = stationpkid;
        }

        public Object getStationname() {
            return stationname;
        }

        public void setStationname(Object stationname) {
            this.stationname = stationname;
        }

        public Object getCheckresult() {
            return checkresult;
        }

        public void setCheckresult(Object checkresult) {
            this.checkresult = checkresult;
        }

        public Object getChecktime() {
            return checktime;
        }

        public void setChecktime(Object checktime) {
            this.checktime = checktime;
        }
    }
}
