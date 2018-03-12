package com.mapuni.caremission_ens.bean;

import java.util.List;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/10/25 11:37
 * @change
 * @chang time
 * @class 违规操作
 */

public class IllegalOperationBean {
    
    /**
     * result : 1
     * info : [{"HPHM":"鄂A54321","CPYS":"蓝","CLXH":"13214","VIN":"1234678","JDCSYR":"lzx","YY":"操作违规","JLRQ":1504800000000},{"HPHM":"鄂A12345","CPYS":"蓝","CLXH":"13214","VIN":"1234678","JDCSYR":"lzx","YY":"检测违规","JLRQ":1504540800000},{"HPHM":"鄂A12345","CPYS":"蓝","CLXH":"13214","VIN":"1234678","JDCSYR":"lzx","YY":"作弊","JLRQ":1504454400000}]
     */

    private String result;
    private List<InfoBean> info;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * HPHM : 鄂A54321
         * CPYS : 蓝
         * CLXH : 13214
         * VIN : 1234678
         * JDCSYR : lzx
         * YY : 操作违规
         * JLRQ : 1504800000000
         */

        private String HPHM;
        private String CPYS;
        private String CLXH;
        private String VIN;
        private String JDCSYR;
        private String YY;
        private long JLRQ;

        public String getHPHM() {
            return HPHM;
        }

        public void setHPHM(String HPHM) {
            this.HPHM = HPHM;
        }

        public String getCPYS() {
            return CPYS;
        }

        public void setCPYS(String CPYS) {
            this.CPYS = CPYS;
        }

        public String getCLXH() {
            return CLXH;
        }

        public void setCLXH(String CLXH) {
            this.CLXH = CLXH;
        }

        public String getVIN() {
            return VIN;
        }

        public void setVIN(String VIN) {
            this.VIN = VIN;
        }

        public String getJDCSYR() {
            return JDCSYR;
        }

        public void setJDCSYR(String JDCSYR) {
            this.JDCSYR = JDCSYR;
        }

        public String getYY() {
            return YY;
        }

        public void setYY(String YY) {
            this.YY = YY;
        }

        public long getJLRQ() {
            return JLRQ;
        }

        public void setJLRQ(long JLRQ) {
            this.JLRQ = JLRQ;
        }
    }
}
