package com.yutu.car.bean;

import java.util.List;

/**
 * @name CarEmission_MG
 * @class name：com.yutu.car.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/10/28 15:20
 * @change
 * @chang time
 * @class describe
 */

public class CoAccountDetailBean {
    
    /**
     * flag : 1
     * data : [{"CLS":0,"PFBZ":"国0","PFL":0},{"CLS":0,"PFBZ":"国1","PFL":0},{"CLS":0,"PFBZ":"国2","PFL":0},{"CLS":0,"PFBZ":"国3","PFL":0},{"CLS":55,"PFBZ":"国4","PFL":0},{"CLS":103,"PFBZ":"国5","PFL":0}]
     * pfzl : 0
     * clzs : 158
     */

    private int flag;
    private int pfzl;
    private int clzs;
    private List<DataBean> data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getPfzl() {
        return pfzl;
    }

    public void setPfzl(int pfzl) {
        this.pfzl = pfzl;
    }

    public int getClzs() {
        return clzs;
    }

    public void setClzs(int clzs) {
        this.clzs = clzs;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * CLS : 0
         * PFBZ : 国0
         * PFL : 0
         */

        private int CLS;
        private String PFBZ;
        private int PFL;

        public int getCLS() {
            return CLS;
        }

        public void setCLS(int CLS) {
            this.CLS = CLS;
        }

        public String getPFBZ() {
            return PFBZ;
        }

        public void setPFBZ(String PFBZ) {
            this.PFBZ = PFBZ;
        }

        public int getPFL() {
            return PFL;
        }

        public void setPFL(int PFL) {
            this.PFL = PFL;
        }
    }
}
