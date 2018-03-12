package cn.com.mapuni.meshingtotal.model;

import java.util.List;

/**
 * Created by 15225 on 2017/7/5.
 */

public class NoticeDetailBean {
    /**
     * state : 0
     * msg : success
     * data : [{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":85.3,"StandardVal":75,"Overtimes":0.137},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":89.9,"StandardVal":75,"Overtimes":0.199},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":93.1,"StandardVal":75,"Overtimes":0.241},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":93.3,"StandardVal":75,"Overtimes":0.244},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":93.133333,"StandardVal":75,"Overtimes":0.242},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":95.15,"StandardVal":75,"Overtimes":0.269},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":100.3,"StandardVal":75,"Overtimes":0.337},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":100,"StandardVal":75,"Overtimes":0.333},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":91.9,"StandardVal":75,"Overtimes":0.225},{"EntName":"建筑工地","OutportName":"山东中医药大学长清校区","OutportCode":"45","PollutantCode":"PM2P5","PollutantName":"PM2.5","OverValue":92.6,"StandardVal":75,"Overtimes":0.235}]
     */

    private String state;
    private String msg;
    private List<DataBean> data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * EntName : 建筑工地
         * OutportName : 山东中医药大学长清校区
         * OutportCode : 45
         * PollutantCode : PM2P5
         * PollutantName : PM2.5
         * OverValue : 85.3
         * StandardVal : 75.0
         * Overtimes : 0.137
         */

        private String EntName;
        private String OutportName;
        private String OutportCode;
        private String PollutantCode;
        private String PollutantName;
        private double OverValue;
        private double StandardVal;
        private double Overtimes;

        public String getEntName() {
            return EntName;
        }

        public void setEntName(String EntName) {
            this.EntName = EntName;
        }

        public String getOutportName() {
            return OutportName;
        }

        public void setOutportName(String OutportName) {
            this.OutportName = OutportName;
        }

        public String getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(String OutportCode) {
            this.OutportCode = OutportCode;
        }

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

        public double getOverValue() {
            return OverValue;
        }

        public void setOverValue(double OverValue) {
            this.OverValue = OverValue;
        }

        public double getStandardVal() {
            return StandardVal;
        }

        public void setStandardVal(double StandardVal) {
            this.StandardVal = StandardVal;
        }

        public double getOvertimes() {
            return Overtimes;
        }

        public void setOvertimes(double Overtimes) {
            this.Overtimes = Overtimes;
        }
    }
}
