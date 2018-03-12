package com.mapuni.mobileenvironment.model;


import java.util.List;

public class MapYan {

    /**
     * EntName : 北京崇德餐饮管理有限公司
     * EntCode : 130300002
     * Longitude : 116.368
     * Latitude : 40.198
     * WaterOutPut : []
     * GasOutput : [{"OutportCode":130300002201,"Pollutants":[{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气温度","Strength":0.6},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气动压","Strength":3},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气湿度","Strength":1.7},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气静压","Strength":2.9}]}]
     */

    private String EntName;
    private int EntCode;
    private double Longitude;
    private double Latitude;
    private List<?> WaterOutPut;
    private List<GasOutputBean> GasOutput;

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String EntName) {
        this.EntName = EntName;
    }

    public int getEntCode() {
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

    public List<?> getWaterOutPut() {
        return WaterOutPut;
    }

    public void setWaterOutPut(List<?> WaterOutPut) {
        this.WaterOutPut = WaterOutPut;
    }

    public List<GasOutputBean> getGasOutput() {
        return GasOutput;
    }

    public void setGasOutput(List<GasOutputBean> GasOutput) {
        this.GasOutput = GasOutput;
    }

    public static class GasOutputBean {
        /**
         * OutportCode : 130300002201
         * Pollutants : [{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气温度","Strength":0.6},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气动压","Strength":3},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气湿度","Strength":1.7},{"MonitorTime":"2017-02-28T23:55:00","PollutantName":"烟气静压","Strength":2.9}]
         */

        private long OutportCode;
        private List<PollutantsBean> Pollutants;

        public long getOutportCode() {
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
             * MonitorTime : 2017-02-28T23:55:00
             * PollutantName : 烟气温度
             * Strength : 0.6
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
