package com.yutu.car.bean;

import java.util.List;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/10/25 15:00
 * @change
 * @chang time
 * @class 城市名
 */

public class CityNameBean {


    /**
     * result : 1
     * info : [{"XZQH":"4202","JC":"黄石市"},{"XZQH":"4203","JC":"十堰市"},{"XZQH":"4205","JC":"宜昌市"},{"XZQH":"4206","JC":"襄阳市"},{"XZQH":"4207","JC":"鄂州市"},{"XZQH":"4208","JC":"荆门市"},{"XZQH":"4209","JC":"孝感市"},{"XZQH":"4210","JC":"荆州市"},{"XZQH":"4211","JC":"黄冈市"},{"XZQH":"4212","JC":"咸宁市"},{"XZQH":"4213","JC":"随州市"},{"XZQH":"4228","JC":"恩施州"},{"XZQH":"429004","JC":"仙桃市"},{"XZQH":"429005","JC":"潜江市"},{"XZQH":"429006","JC":"天门市"},{"XZQH":"429021","JC":"神农架"},{"XZQH":"4201","JC":"武汉市"}]
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
         * XZQH : 4202
         * JC : 黄石市
         */

        private String XZQH;
        private String JC;

        public String getXZQH() {
            return XZQH;
        }

        public void setXZQH(String XZQH) {
            this.XZQH = XZQH;
        }

        public String getJC() {
            return JC;
        }

        public void setJC(String JC) {
            this.JC = JC;
        }
    }
}
