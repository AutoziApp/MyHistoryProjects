package com.mapuni.car.mvp.main.model;

import java.util.List;

/**
 * Created by yawei on 2017/8/28.
 */

public class ConfigBean {

    /**
     * cphmqz : 豫
     * clCphmqz : [{"pkid":"京","jc":"京"},{"pkid":"川","jc":"川"},{"pkid":"粤","jc":"粤"},{"pkid":"豫","jc":"豫"}]
     * qdfs : [{"pkid":"1","jc":"前驱"},{"pkid":"2","jc":"后驱"},{"pkid":"3","jc":"分时四驱"},{"pkid":"4","jc":"全时四驱"}]
     * hplb : [{"pkid":"01","jc":"大型汽车"},{"pkid":"02","jc":"小型汽车"},{"pkid":"03","jc":"使馆汽车"},{"pkid":"04","jc":"领馆汽车"},{"pkid":"05","jc":"境外汽车"},{"pkid":"06","jc":"外籍汽车"},{"pkid":"07","jc":"普通摩托车"},{"pkid":"08","jc":"轻便摩托车"},{"pkid":"09","jc":"使馆摩托车"},{"pkid":"10","jc":"领馆摩托车"},{"pkid":"11","jc":"境外摩托车"},{"pkid":"12","jc":"外籍摩托车"},{"pkid":"13","jc":"低速车"},{"pkid":"14","jc":"拖拉机"},{"pkid":"15","jc":"挂车"},{"pkid":"16","jc":"教练汽车"},{"pkid":"17","jc":"教练摩托车"},{"pkid":"18","jc":"试验汽车"},{"pkid":"19","jc":"试验摩托车"},{"pkid":"20","jc":"临时入境汽车"},{"pkid":"21","jc":"临时入境摩托车"},{"pkid":"22","jc":"临时行驶车"},{"pkid":"23","jc":"警用汽车"},{"pkid":"24","jc":"警用摩托"},{"pkid":"99","jc":"其他"}]
     */

    private String cphmqz;
    private List<ClCphmqzBean> clCphmqz;
    private List<QdfsBean> qdfs;
    private List<HplbBean> hplb;

    public String getCphmqz() {
        return cphmqz;
    }

    public void setCphmqz(String cphmqz) {
        this.cphmqz = cphmqz;
    }

    public List<ClCphmqzBean> getClCphmqz() {
        return clCphmqz;
    }

    public void setClCphmqz(List<ClCphmqzBean> clCphmqz) {
        this.clCphmqz = clCphmqz;
    }

    public List<QdfsBean> getQdfs() {
        return qdfs;
    }

    public void setQdfs(List<QdfsBean> qdfs) {
        this.qdfs = qdfs;
    }

    public List<HplbBean> getHplb() {
        return hplb;
    }

    public void setHplb(List<HplbBean> hplb) {
        this.hplb = hplb;
    }

    public static class ClCphmqzBean {
        /**
         * pkid : 京
         * jc : 京
         */

        private String pkid;
        private String jc;

        public String getPkid() {
            return pkid;
        }

        public void setPkid(String pkid) {
            this.pkid = pkid;
        }

        public String getJc() {
            return jc;
        }

        public void setJc(String jc) {
            this.jc = jc;
        }
    }

    public static class QdfsBean {
        /**
         * pkid : 1
         * jc : 前驱
         */

        private String pkid;
        private String jc;

        public String getPkid() {
            return pkid;
        }

        public void setPkid(String pkid) {
            this.pkid = pkid;
        }

        public String getJc() {
            return jc;
        }

        public void setJc(String jc) {
            this.jc = jc;
        }
    }

    public static class HplbBean {
        /**
         * pkid : 01
         * jc : 大型汽车
         */

        private String pkid;
        private String jc;

        public String getPkid() {
            return pkid;
        }

        public void setPkid(String pkid) {
            this.pkid = pkid;
        }

        public String getJc() {
            return jc;
        }

        public void setJc(String jc) {
            this.jc = jc;
        }
    }
}
