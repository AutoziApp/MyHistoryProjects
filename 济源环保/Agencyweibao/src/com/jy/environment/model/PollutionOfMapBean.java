package com.jy.environment.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 15225 on 2017/10/31.
 */

public class PollutionOfMapBean {

    /**
     * flag : true
     * msg : 成功
     * data : {"totleNum":"3","updateTime":"2017-09-25 13:00:00","stationList":[{"stationName":"高压开关厂","stationCode":"41040101","factorLevel":"1","lng":"113.306","lat":"33.721","factorValue":"20"},{"stationName":"平顶山工学院","stationCode":"41040102","factorLevel":"1","lng":"113.182","lat":"33.737","factorValue":"43"},{"stationName":"规划设计院","stationCode":"41040103","factorLevel":"1","lng":"113.292","lat":"33.739","factorValue":"35"}]}
     * ret : null
     */

    private String flag;
    private String msg;
    private DataBean data;
    private Object ret;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getRet() {
        return ret;
    }

    public void setRet(Object ret) {
        this.ret = ret;
    }

    public static class DataBean {
        /**
         * totleNum : 3
         * updateTime : 2017-09-25 13:00:00
         * stationList : [{"stationName":"高压开关厂","stationCode":"41040101","factorLevel":"1","lng":"113.306","lat":"33.721","factorValue":"20"},{"stationName":"平顶山工学院","stationCode":"41040102","factorLevel":"1","lng":"113.182","lat":"33.737","factorValue":"43"},{"stationName":"规划设计院","stationCode":"41040103","factorLevel":"1","lng":"113.292","lat":"33.739","factorValue":"35"}]
         */

        private String totleNum;
        private String updateTime;
        private List<StationListBean> stationList;

        public String getTotleNum() {
            return totleNum;
        }

        public void setTotleNum(String totleNum) {
            this.totleNum = totleNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<StationListBean> getStationList() {
            return stationList;
        }

        public void setStationList(List<StationListBean> stationList) {
            this.stationList = stationList;
        }

        public static class StationListBean implements Serializable{
            /**
             * stationName : 高压开关厂
             * stationCode : 41040101
             * factorLevel : 1
             * lng : 113.306
             * lat : 33.721
             * factorValue : 20
             */

            private String stationName;
            private String stationCode;
            private String factorLevel;
            private double lng;
            private double lat;
            private String factorValue;

            public String getStationName() {
                return stationName;
            }

            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            public String getStationCode() {
                return stationCode;
            }

            public void setStationCode(String stationCode) {
                this.stationCode = stationCode;
            }

            public String getFactorLevel() {
                return factorLevel;
            }

            public void setFactorLevel(String factorLevel) {
                this.factorLevel = factorLevel;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public String getFactorValue() {
                return factorValue;
            }

            public void setFactorValue(String factorValue) {
                this.factorValue = factorValue;
            }
        }
    }
}
