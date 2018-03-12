package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2017/1/4.
 * 地图污染源实体类
 */

public class MapSource {
    @Override
    public String toString() {
        return "MapSource{" +
                "EntName='" + EntName + '\'' +
                ", EntCode=" + EntCode +
                ", Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                ", WaterOutPut=" + WaterOutPut +
                ", GasOutput=" + GasOutput +
                '}';
    }

    /**
     * EntName : 鍖椾含鍖楁帶姹℃按鍑€鍖栧強鍥炵敤鏈夐檺鍏徃
     * EntCode : 130300001
     * Longitude : 116.45234
     * Latitude : 39.8123
     * WaterOutPut : [{"OutportCode":130300001101,"Pollutants":[{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"pH鍊�","Strength":0.7},{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"COD","Strength":2.8},{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"姘ㄦ爱","Strength":0.6}]}]
     * GasOutput : [{"OutportCode":130300001201,"Pollutants":[{"MonitorTime":"2016-12-31T23:55:00","PollutantName":"浜屾哀鍖栫～","Strength":2.55}]}]
     */

    private String EntName;
    private double EntCode;
    private double Longitude;
    private double Latitude;
    private List<WaterOutPutBean> WaterOutPut;
    private List<GasOutputBean> GasOutput;

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }

    public double getEntCode() {
        return EntCode;
    }

    public void setEntCode(int EntCode) {
        this.EntCode = EntCode;
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

    public List<WaterOutPutBean> getWaterOutPut() {
        return WaterOutPut;
    }

    public void setWaterOutPut(List<WaterOutPutBean> WaterOutPut) {
        this.WaterOutPut = WaterOutPut;
    }

    public List<GasOutputBean> getGasOutput() {
        return GasOutput;
    }

    public void setGasOutput(List<GasOutputBean> GasOutput) {
        this.GasOutput = GasOutput;
    }

    public static class WaterOutPutBean {
        /**
         * OutportCode : 130300001101
         * Pollutants : [{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"pH鍊�","Strength":0.7},{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"COD","Strength":2.8},{"MonitorTime":"2017-01-30T23:55:00","PollutantName":"姘ㄦ爱","Strength":0.6}]
         */

        private double OutportCode;
        private List<PollutantsBean> Pollutants;

        public double getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(long OutportCode) {
            this.OutportCode = OutportCode;
        }

        public List<PollutantsBean> getPollutants() {
            return Pollutants;
        }

        public void setPollutants(List<PollutantsBean> Pollutants) {
            this.Pollutants = Pollutants;
        }

        public static class PollutantsBean {
            /**
             * MonitorTime : 2017-01-30T23:55:00
             * PollutantName : pH鍊�
             * Strength : 0.7
             */

            private String MonitorTime;
            private String PollutantName;
            private double Strength;

            public String getMonitorTime() {
                return MonitorTime;
            }

            public void setMonitorTime(String MonitorTime) {
                this.MonitorTime = MonitorTime;
            }

            public String getPollutantName() {
                return PollutantName;
            }

            public void setPollutantName(String PollutantName) {
                this.PollutantName = PollutantName;
            }

            public double getStrength() {
                return Strength;
            }

            public void setStrength(double Strength) {
                this.Strength = Strength;
            }
        }
    }

    public static class GasOutputBean {
        /**
         * OutportCode : 130300001201
         * Pollutants : [{"MonitorTime":"2016-12-31T23:55:00","PollutantName":"浜屾哀鍖栫～","Strength":2.55}]
         */

        private double OutportCode;
        private List<PollutantsBeanX> Pollutants;

        public double getOutportCode() {
            return OutportCode;
        }

        public void setOutportCode(long OutportCode) {
            this.OutportCode = OutportCode;
        }

        public List<PollutantsBeanX> getPollutants() {
            return Pollutants;
        }

        public void setPollutants(List<PollutantsBeanX> Pollutants) {
            this.Pollutants = Pollutants;
        }

        public static class PollutantsBeanX {
            /**
             * MonitorTime : 2016-12-31T23:55:00
             * PollutantName : 浜屾哀鍖栫～
             * Strength : 2.55
             */

            private String MonitorTime;
            private String PollutantName;
            private double Strength;

            public String getMonitorTime() {
                return MonitorTime;
            }

            public void setMonitorTime(String MonitorTime) {
                this.MonitorTime = MonitorTime;
            }

            public String getPollutantName() {
                return PollutantName;
            }

            public void setPollutantName(String PollutantName) {
                this.PollutantName = PollutantName;
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
