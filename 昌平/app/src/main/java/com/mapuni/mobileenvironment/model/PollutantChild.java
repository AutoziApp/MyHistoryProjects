package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by yawei on 2016/12/13.
 */
public class PollutantChild {

    /**
     * result : 1
     * WarmRange : [{"WarmType":2,"WarnLower":null,"WarnUpper":2.99}]
     * MonitorTime : [2016/12/15 4:10:00,2016/12/15 4:05:00,2016/12/15 4:00:00,2016/12/15 3:55:00,2016/12/15 3:50:00,2016/12/15 3:45:00,2016/12/15 3:40:00,2016/12/15 3:35:00,2016/12/15 3:30:00,2016/12/15 3:25:00,2016/12/15 3:20:00,2016/12/15 3:15:00,2016/12/15 3:10:00,2016/12/15 3:05:00,2016/12/15 3:00:00]
     * Value : [2.100000,1.600000,1.200000,0.700000,2.500000,1.200000,2.800000,0.700000,2.900000,0.600000,0.100000,1.100000,2.600000,0.800000,0.700000]
     * TotalRows : 300
     */

    private int result;
    private String MonitorTime;
    private String Value;
    private int TotalRows;
    private List<WarmRangeBean> WarmRange;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMonitorTime() {
        return MonitorTime;
    }

    public void setMonitorTime(String MonitorTime) {
        this.MonitorTime = MonitorTime;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public int getTotalRows() {
        return TotalRows;
    }

    public void setTotalRows(int TotalRows) {
        this.TotalRows = TotalRows;
    }

    public List<WarmRangeBean> getWarmRange() {
        return WarmRange;
    }

    public void setWarmRange(List<WarmRangeBean> WarmRange) {
        this.WarmRange = WarmRange;
    }

    public static class WarmRangeBean {
        /**
         * WarmType : 2
         * WarnLower : null
         * WarnUpper : 2.99
         */

        private int WarmType;
        private Object WarnLower;
        private double WarnUpper;

        public int getWarmType() {
            return WarmType;
        }

        public void setWarmType(int WarmType) {
            this.WarmType = WarmType;
        }

        public Object getWarnLower() {
            return WarnLower;
        }

        public void setWarnLower(Object WarnLower) {
            this.WarnLower = WarnLower;
        }

        public double getWarnUpper() {
            return WarnUpper;
        }

        public void setWarnUpper(double WarnUpper) {
            this.WarnUpper = WarnUpper;
        }
    }
}
