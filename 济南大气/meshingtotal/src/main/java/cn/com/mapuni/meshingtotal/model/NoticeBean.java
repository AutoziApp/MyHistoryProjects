package cn.com.mapuni.meshingtotal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 15225 on 2017/7/5.
 */

public class NoticeBean {

    /**
     * state : 0
     * msg : success
     * data : [{"aIndex":1,"aid":3903,"EntCode":3,"OutportCode":7,"MonitorTime":"2017-07-05 08:00:00","PollutantCode":"PM2P5","PollutantTypeCode":2,"alarmData":86.888888,"minData":86.888888,"maxData":141.25,"alarmType":"小时均值超标","endtime":"2017-07-05 13:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"鸿铭渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM2.5","StandardVal":75,"Unit":"mg/M3"},{"aIndex":2,"aid":3904,"EntCode":3,"OutportCode":8,"MonitorTime":"2017-07-05 08:00:00","PollutantCode":"PM2P5","PollutantTypeCode":2,"alarmData":97.222222,"minData":75.333333,"maxData":143.375,"alarmType":"小时均值超标","endtime":"2017-07-05 13:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"顺兴浩宇渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM2.5","StandardVal":75,"Unit":"mg/M3"},{"aIndex":3,"aid":3905,"EntCode":3,"OutportCode":8,"MonitorTime":"2017-07-05 08:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":196.888888,"minData":148.333333,"maxData":365.375,"alarmType":"小时均值超标","endtime":"2017-07-05 13:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"顺兴浩宇渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":4,"aid":3900,"EntCode":2,"OutportCode":23,"MonitorTime":"2017-07-05 07:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":102,"minData":102,"maxData":252.75,"alarmType":"小时均值超标","endtime":"2017-07-05 12:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"旅游路-港西路路口","EntName":"主干道","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":5,"aid":3901,"EntCode":3,"OutportCode":7,"MonitorTime":"2017-07-05 07:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":129.888888,"minData":129.888888,"maxData":249.375,"alarmType":"小时均值超标","endtime":"2017-07-05 12:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"鸿铭渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":6,"aid":3902,"EntCode":3,"OutportCode":10,"MonitorTime":"2017-07-05 07:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":143.444444,"minData":100.25,"maxData":157.222222,"alarmType":"小时均值超标","endtime":"2017-07-05 12:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"长拓渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":7,"aid":3895,"EntCode":2,"OutportCode":14,"MonitorTime":"2017-07-05 06:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":194.2,"minData":166,"maxData":222,"alarmType":"小时均值超标","endtime":"2017-07-05 11:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"淄博路-烟台路路口","EntName":"主干道","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":8,"aid":3896,"EntCode":2,"OutportCode":24,"MonitorTime":"2017-07-05 06:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":117,"minData":102.5,"maxData":198.25,"alarmType":"小时均值超标","endtime":"2017-07-05 11:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"黄冈路-小清河北路路口","EntName":"主干道","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":9,"aid":3897,"EntCode":3,"OutportCode":2,"MonitorTime":"2017-07-05 06:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":112.111111,"minData":101.75,"maxData":200.75,"alarmType":"小时均值超标","endtime":"2017-07-05 11:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"洛威渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"},{"aIndex":10,"aid":3898,"EntCode":3,"OutportCode":4,"MonitorTime":"2017-07-05 06:00:00","PollutantCode":"PM10","PollutantTypeCode":2,"alarmData":188.555555,"minData":154,"maxData":227.111111,"alarmType":"小时均值超标","endtime":"2017-07-05 11:00:00","alarmbsc":"0","alarmbs":0,"alarmcount":0,"AduitState":0,"SendState":0,"disp":0,"Remark":null,"IsSure":0,"IsPush":false,"alarmstate":null,"astate":null,"OutportName":"卓威渣土场","EntName":"渣土场","RegionName":"市辖区","RegionCode":370101000,"PollutantName":"PM10","StandardVal":100,"Unit":"mg/M3"}]
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

    public static class DataBean   implements Serializable{
        /**
         * aIndex : 1
         * aid : 3903
         * EntCode : 3
         * OutportCode : 7
         * MonitorTime : 2017-07-05 08:00:00
         * PollutantCode : PM2P5
         * PollutantTypeCode : 2
         * alarmData : 86.888888
         * minData : 86.888888
         * maxData : 141.25
         * alarmType : 小时均值超标
         * endtime : 2017-07-05 13:00:00
         * alarmbsc : 0
         * alarmbs : 0
         * alarmcount : 0
         * AduitState : 0
         * SendState : 0
         * disp : 0
         * Remark : null
         * IsSure : 0
         * IsPush : false
         * alarmstate : null
         * astate : null
         * OutportName : 鸿铭渣土场
         * EntName : 渣土场
         * RegionName : 市辖区
         * RegionCode : 370101000
         * PollutantName : PM2.5
         * StandardVal : 75.0
         * Unit : mg/M3
         */

        private int aIndex;
        private int aid;
        private int EntCode;
        private int OutportCode;
        private String MonitorTime;
        private String PollutantCode;
        private int PollutantTypeCode;
        private double alarmData;
        private double minData;
        private double maxData;
        private String alarmType;
        private String endtime;
        private String alarmbsc;
        private int alarmbs;
        private int alarmcount;
        private int AduitState;
        private int SendState;
        private int disp;
        private String Remark;
        private int IsSure;
        private boolean IsPush;
        private String alarmstate;
        private String astate;
        private String OutportName;
        private String EntName;
        private String RegionName;
        private int RegionCode;
        private String PollutantName;
        private double StandardVal;
        private String Unit;

        public int getAIndex() {
            return aIndex;
        }

        public void setAIndex(int aIndex) {
            this.aIndex = aIndex;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getEntCode() {
            return EntCode;
        }

        public void setEntCode(int EntCode) {
            this.EntCode = EntCode;
        }

        public int getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(int OutportCode) {
            this.OutportCode = OutportCode;
        }

        public String getMonitorTime() {
            return MonitorTime;
        }

        public void setMonitorTime(String MonitorTime) {
            this.MonitorTime = MonitorTime;
        }

        public String getPollutantCode() {
            return PollutantCode;
        }

        public void setPollutantCode(String PollutantCode) {
            this.PollutantCode = PollutantCode;
        }

        public int getPollutantTypeCode() {
            return PollutantTypeCode;
        }

        public void setPollutantTypeCode(int PollutantTypeCode) {
            this.PollutantTypeCode = PollutantTypeCode;
        }

        public double getAlarmData() {
            return alarmData;
        }

        public void setAlarmData(double alarmData) {
            this.alarmData = alarmData;
        }

        public double getMinData() {
            return minData;
        }

        public void setMinData(double minData) {
            this.minData = minData;
        }

        public double getMaxData() {
            return maxData;
        }

        public void setMaxData(double maxData) {
            this.maxData = maxData;
        }

        public String getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(String alarmType) {
            this.alarmType = alarmType;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getAlarmbsc() {
            return alarmbsc;
        }

        public void setAlarmbsc(String alarmbsc) {
            this.alarmbsc = alarmbsc;
        }

        public int getAlarmbs() {
            return alarmbs;
        }

        public void setAlarmbs(int alarmbs) {
            this.alarmbs = alarmbs;
        }

        public int getAlarmcount() {
            return alarmcount;
        }

        public void setAlarmcount(int alarmcount) {
            this.alarmcount = alarmcount;
        }

        public int getAduitState() {
            return AduitState;
        }

        public void setAduitState(int AduitState) {
            this.AduitState = AduitState;
        }

        public int getSendState() {
            return SendState;
        }

        public void setSendState(int SendState) {
            this.SendState = SendState;
        }

        public int getDisp() {
            return disp;
        }

        public void setDisp(int disp) {
            this.disp = disp;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public int getIsSure() {
            return IsSure;
        }

        public void setIsSure(int IsSure) {
            this.IsSure = IsSure;
        }

        public boolean isIsPush() {
            return IsPush;
        }

        public void setIsPush(boolean IsPush) {
            this.IsPush = IsPush;
        }

        public String getAlarmstate() {
            return alarmstate;
        }

        public void setAlarmstate(String alarmstate) {
            this.alarmstate = alarmstate;
        }

        public String getAstate() {
            return astate;
        }

        public void setAstate(String astate) {
            this.astate = astate;
        }

        public String getOutportName() {
            return OutportName;
        }

        public void setOutportName(String OutportName) {
            this.OutportName = OutportName;
        }

        public String getEntName() {
            return EntName;
        }

        public void setEntName(String EntName) {
            this.EntName = EntName;
        }

        public String getRegionName() {
            return RegionName;
        }

        public void setRegionName(String RegionName) {
            this.RegionName = RegionName;
        }

        public int getRegionCode() {
            return RegionCode;
        }

        public void setRegionCode(int RegionCode) {
            this.RegionCode = RegionCode;
        }

        public String getPollutantName() {
            return PollutantName;
        }

        public void setPollutantName(String PollutantName) {
            this.PollutantName = PollutantName;
        }

        public double getStandardVal() {
            return StandardVal;
        }

        public void setStandardVal(double StandardVal) {
            this.StandardVal = StandardVal;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }
}
