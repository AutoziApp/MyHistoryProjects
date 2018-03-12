package cn.com.mapuni.chart.meshingtotal.model;

import java.util.List;

/**
 * Created by 15225 on 2017/7/24.
 */

public class DepCheckInfoBean {

    /**
     * Result : true
     * CheckType : 时间段考核
     * CheckTimespan : 2017-07-01 00:00:00-2017-07-20 00:00:00
     * Data : [{"DeptID":"1","DeptName":"市建委","HourScore":"29.61","HourAlarmScore":"74.99","DayScore":"40.28","DayAlarmScore":"71.47","Score":"216.35"},{"DeptID":"2","DeptName":"市城管局","HourScore":"64.79","HourAlarmScore":"75.24","DayScore":"67.04","DayAlarmScore":"77.07","Score":"284.14"},{"DeptID":"4","DeptName":"市政公用局","HourScore":"74.41","HourAlarmScore":"73.80","DayScore":"81.18","DayAlarmScore":"70.65","Score":"300.04"},{"DeptID":"5","DeptName":"市监测站","HourScore":"84.35","HourAlarmScore":"96.00","DayScore":"70.94","DayAlarmScore":"94.70","Score":"345.99"}]
     */

    private String Result;
    private String CheckType;
    private String CheckTimespan;
    private List<DataBean> Data;

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getCheckType() {
        return CheckType;
    }

    public void setCheckType(String CheckType) {
        this.CheckType = CheckType;
    }

    public String getCheckTimespan() {
        return CheckTimespan;
    }

    public void setCheckTimespan(String CheckTimespan) {
        this.CheckTimespan = CheckTimespan;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * DeptID : 1
         * DeptName : 市建委
         * HourScore : 29.61
         * HourAlarmScore : 74.99
         * DayScore : 40.28
         * DayAlarmScore : 71.47
         * Score : 216.35
         */

        private String DeptID;
        private String DeptName;
        private String HourScore;
        private String HourAlarmScore;
        private String DayScore;
        private String DayAlarmScore;
        private String Score;

        public String getDeptID() {
            return DeptID;
        }

        public void setDeptID(String DeptID) {
            this.DeptID = DeptID;
        }

        public String getDeptName() {
            return DeptName;
        }

        public void setDeptName(String DeptName) {
            this.DeptName = DeptName;
        }

        public String getHourScore() {
            return HourScore;
        }

        public void setHourScore(String HourScore) {
            this.HourScore = HourScore;
        }

        public String getHourAlarmScore() {
            return HourAlarmScore;
        }

        public void setHourAlarmScore(String HourAlarmScore) {
            this.HourAlarmScore = HourAlarmScore;
        }

        public String getDayScore() {
            return DayScore;
        }

        public void setDayScore(String DayScore) {
            this.DayScore = DayScore;
        }

        public String getDayAlarmScore() {
            return DayAlarmScore;
        }

        public void setDayAlarmScore(String DayAlarmScore) {
            this.DayAlarmScore = DayAlarmScore;
        }

        public String getScore() {
            return Score;
        }

        public void setScore(String Score) {
            this.Score = Score;
        }
    }
}
