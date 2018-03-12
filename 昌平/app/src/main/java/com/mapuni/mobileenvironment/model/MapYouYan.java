package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by 孙常明 on 2017/4/6.
 */
public class MapYouYan {

    /**
     * EntName : 北京吉利学院
     * EntCode : 130300001
     * OutportCode : 130300001201
     * OutportName : 吉利学院油烟排口
     * Longitude : 116.345677
     * Latitude : 40.214466
     * OnlineStatus : 1
     * gfj : 0
     * jhq : 0
     * Data : [{"Pollutants":[{"PollutantCode":"99","PollutantName":"烟气浓度","MonitorTime":"2017/4/6 14:13:00","Strength":4.19},{"PollutantCode":"s03","PollutantName":"烟气温度","MonitorTime":"2017/4/6 14:13:00","Strength":26},{"PollutantCode":"s05","PollutantName":"烟气湿度","MonitorTime":"2017/4/6 14:13:00","Strength":40}]}]
     */

    private String EntName;
    private String EntCode;
    private String OutportCode;
    private String OutportName;
    private double Longitude;
    private double Latitude;
    private int OnlineStatus;
    private int gfj;
    private int jhq;
    private List<DataBean> Data;

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }

    public String getEntCode() {
        return EntCode;
    }

    public void setEntCode(String EntCode) {
        this.EntCode = EntCode;
    }

    public String getOutportCode() {
        return OutportCode;
    }

    public void setOutportCode(String OutportCode) {
        this.OutportCode = OutportCode;
    }

    public String getOutportName() {
        return OutportName;
    }

    public void setOutportName(String OutportName) {
        this.OutportName = OutportName;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public int getOnlineStatus() {
        return OnlineStatus;
    }

    public void setOnlineStatus(int OnlineStatus) {
        this.OnlineStatus = OnlineStatus;
    }

    public int getGfj() {
        return gfj;
    }

    public void setGfj(int gfj) {
        this.gfj = gfj;
    }

    public int getJhq() {
        return jhq;
    }

    public void setJhq(int jhq) {
        this.jhq = jhq;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private List<PollutantsBean> Pollutants;

        public List<PollutantsBean> getPollutants() {
            return Pollutants;
        }

        public void setPollutants(List<PollutantsBean> Pollutants) {
            this.Pollutants = Pollutants;
        }

        public static class PollutantsBean {
            /**
             * PollutantCode : 99
             * PollutantName : 烟气浓度
             * MonitorTime : 2017/4/6 14:13:00
             * Strength : 4.19
             */

            private String PollutantCode;
            private String PollutantName;
            private String MonitorTime;
            private double Strength;

            public String getPollutantCode() {
                return PollutantCode;
            }

            public void setPollutantCode(String PollutantCode) {
                this.PollutantCode = PollutantCode;
            }

            public String getPollutantName() {
                return PollutantName;
            }

            public void setPollutantName(String PollutantName) {
                this.PollutantName = PollutantName;
            }

            public String getMonitorTime() {
                return MonitorTime;
            }

            public void setMonitorTime(String MonitorTime) {
                this.MonitorTime = MonitorTime;
            }

            public double getStrength() {
                return Strength;
            }

            public void setStrength(double Strength) {
                this.Strength = Strength;
            }
        }
    }
}
