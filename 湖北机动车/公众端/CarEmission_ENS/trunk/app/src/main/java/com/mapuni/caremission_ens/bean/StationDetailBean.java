package com.mapuni.caremission_ens.bean;

import java.util.List;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.bean
 * @class describe
 * @anthor Administrator
 * @time 2017/10/23 17:11
 * @change
 * @chang time
 * @class describe
 */

public class StationDetailBean {


    /**
     * info : [{"FRDB":"刘建军","HBFZR":"彭建何","JC":"樊城区","JGDZ":"襄阳市人民西路柿铺","JLRZBH":"161705070076","JYJGMC":"襄阳市安安机动车检测有限公司","LXDH":"13908670199","LXR":"吴元修","TYSHXYDM":"914206007570308857","ZCRQ":1466352000000,"ZZRDYXQ":1643558400000}]
     * result : 1
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
         * FRDB : 刘建军
         * HBFZR : 彭建何
         * JC : 樊城区
         * JGDZ : 襄阳市人民西路柿铺
         * JLRZBH : 161705070076
         * JYJGMC : 襄阳市安安机动车检测有限公司
         * LXDH : 13908670199
         * LXR : 吴元修
         * TYSHXYDM : 914206007570308857
         * ZCRQ : 1466352000000
         * ZZRDYXQ : 1643558400000
         */

        private String FRDB;
        private String HBFZR;
        private String JC;
        private String JGDZ;
        private String JLRZBH;
        private String JYJGMC;
        private String LXDH;
        private String LXR;
        private String TYSHXYDM;
        private long ZCRQ;
        private long ZZRDYXQ;

        public String getFRDB() {
            return FRDB;
        }

        public void setFRDB(String FRDB) {
            this.FRDB = FRDB;
        }

        public String getHBFZR() {
            return HBFZR;
        }

        public void setHBFZR(String HBFZR) {
            this.HBFZR = HBFZR;
        }

        public String getJC() {
            return JC;
        }

        public void setJC(String JC) {
            this.JC = JC;
        }

        public String getJGDZ() {
            return JGDZ;
        }

        public void setJGDZ(String JGDZ) {
            this.JGDZ = JGDZ;
        }

        public String getJLRZBH() {
            return JLRZBH;
        }

        public void setJLRZBH(String JLRZBH) {
            this.JLRZBH = JLRZBH;
        }

        public String getJYJGMC() {
            return JYJGMC;
        }

        public void setJYJGMC(String JYJGMC) {
            this.JYJGMC = JYJGMC;
        }

        public String getLXDH() {
            return LXDH;
        }

        public void setLXDH(String LXDH) {
            this.LXDH = LXDH;
        }

        public String getLXR() {
            return LXR;
        }

        public void setLXR(String LXR) {
            this.LXR = LXR;
        }

        public String getTYSHXYDM() {
            return TYSHXYDM;
        }

        public void setTYSHXYDM(String TYSHXYDM) {
            this.TYSHXYDM = TYSHXYDM;
        }

        public long getZCRQ() {
            return ZCRQ;
        }

        public void setZCRQ(long ZCRQ) {
            this.ZCRQ = ZCRQ;
        }

        public long getZZRDYXQ() {
            return ZZRDYXQ;
        }

        public void setZZRDYXQ(long ZZRDYXQ) {
            this.ZZRDYXQ = ZZRDYXQ;
        }
    }
}
