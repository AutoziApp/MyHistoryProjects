package com.mapuni.car.mvp.listcar.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by YZP on 2017/11/29.
 */
public class CarCheckBean {

    /**
     * isSuccess : 1
     * total : 1
     * data : [{"PKID":"1511955616006_baixingong","CARCARDCOLOR":"蓝牌","INPUTTIME":"2017-11-29 19:40:16","CARCARDNUMBER":"川A11111","CHECKMETHOD":"DB"}]
     */

    private String isSuccess;
    private int total;
    private List<DataBean> data;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * PKID : 1511955616006_baixingong
         * CARCARDCOLOR : 蓝牌
         * INPUTTIME : 2017-11-29 19:40:16
         * CARCARDNUMBER : 川A11111
         * CHECKMETHOD : DB
         */
        private String PKID;
        private String CARCARDCOLOR;
        private String INPUTTIME;
        private String CARCARDNUMBER;
        private String CHECKMETHOD;
        private String CHECKTIME;
        private String CHARCARDCOLOR;
        private String  XGSJ;
        private String NEWCHECKMETHOD;
        private String APPLYTIME;
         private String CHECKDATE;
        private String STATE;
        public String getSTATE() {
            return STATE;
        }

        public void setSTATE(String STATE) {
            this.STATE = STATE;
        }

        public String getCHECKDATE() {
            return CHECKDATE;
        }

        public void setCHECKDATE(String CHECKDATE) {
            this.CHECKDATE = CHECKDATE;
        }

        public String getAPPLYTIME() {
            return APPLYTIME;
        }

        public void setAPPLYTIME(String APPLYTIME) {
            this.APPLYTIME = APPLYTIME;
        }

        public String getNEWCHECKMETHOD() {
            return NEWCHECKMETHOD;
        }

        public void setNEWCHECKMETHOD(String NEWCHECKMETHOD) {
            this.NEWCHECKMETHOD = NEWCHECKMETHOD;
        }

        public String getXGSJ() {
            return XGSJ;
        }

        public void setXGSJ(String XGSJ) {
            this.XGSJ = XGSJ;
        }

        public String getCHARCARDCOLOR() {
            return CHARCARDCOLOR;
        }

        public void setCHARCARDCOLOR(String CHARCARDCOLOR) {
            this.CHARCARDCOLOR = CHARCARDCOLOR;
        }

        public String getCHECKTIME() {
            return CHECKTIME;
        }

        public void setCHECKTIME(String CHECKTIME) {
            this.CHECKTIME = CHECKTIME;
        }

        public String getPKID() {
            return PKID;
        }

        public void setPKID(String PKID) {
            this.PKID = PKID;
        }

        public String getCARCARDCOLOR() {
            return CARCARDCOLOR;
        }

        public void setCARCARDCOLOR(String CARCARDCOLOR) {
            this.CARCARDCOLOR = CARCARDCOLOR;
        }

        public String getINPUTTIME() {
            return INPUTTIME;
        }

        public void setINPUTTIME(String INPUTTIME) {
            this.INPUTTIME = INPUTTIME;
        }

        public String getCARCARDNUMBER() {
            return CARCARDNUMBER;
        }

        public void setCARCARDNUMBER(String CARCARDNUMBER) {
            this.CARCARDNUMBER = CARCARDNUMBER;
        }

        public String getCHECKMETHOD() {
            if(TextUtils.isEmpty(CHECKMETHOD)){
                CHECKMETHOD="--";
            }
            return CHECKMETHOD;
        }

        public void setCHECKMETHOD(String CHECKMETHOD) {
            this.CHECKMETHOD = CHECKMETHOD;
        }
    }
}
