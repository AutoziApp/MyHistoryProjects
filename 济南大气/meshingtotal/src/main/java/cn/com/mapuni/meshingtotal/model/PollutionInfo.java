package cn.com.mapuni.meshingtotal.model;

import java.util.List;

/**
 * Created by 15225 on 2017/7/13.
 */

public class PollutionInfo {

    /**
     * ret : 0
     * msg : [{"wetherInfo":{"aqi":"58","PollutionLevel":"Ⅱ"},"AirInfo":{"Pm25":"33","Pm10":"66","O3":"98","So2":"11","No2":"15","Co":" 0.529"},"stationInfo":[{"stationName":"机床二厂 ","StationCode":"283","Aqi":"72.000000"},{"stationName":"科干所","StationCode":"284","Aqi":"70.000000"},{"stationName":"农科所","StationCode":"286","Aqi":"67.000000"},{"stationName":"济南化工厂","StationCode":"280","Aqi":"65.000000"},{"stationName":"市监测站","StationCode":"281","Aqi":"62.000000"},{"stationName":"省种子仓库","StationCode":"282","Aqi":"58.000000"},{"stationName":"长清党校","StationCode":"287","Aqi":"57.000000"},{"stationName":"开发区","StationCode":"285","Aqi":"46.000000"},{"stationName":"商河城区","StationCode":"306","Aqi":"115.000000"},{"stationName":"跑马岭","StationCode":"295","Aqi":"113.000000"}]}]
     */

    private String ret;
    private List<MsgBean> msg;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * wetherInfo : {"aqi":"58","PollutionLevel":"Ⅱ"}
         * AirInfo : {"Pm25":"33","Pm10":"66","O3":"98","So2":"11","No2":"15","Co":" 0.529"}
         * stationInfo : [{"stationName":"机床二厂 ","StationCode":"283","Aqi":"72.000000"},{"stationName":"科干所","StationCode":"284","Aqi":"70.000000"},{"stationName":"农科所","StationCode":"286","Aqi":"67.000000"},{"stationName":"济南化工厂","StationCode":"280","Aqi":"65.000000"},{"stationName":"市监测站","StationCode":"281","Aqi":"62.000000"},{"stationName":"省种子仓库","StationCode":"282","Aqi":"58.000000"},{"stationName":"长清党校","StationCode":"287","Aqi":"57.000000"},{"stationName":"开发区","StationCode":"285","Aqi":"46.000000"},{"stationName":"商河城区","StationCode":"306","Aqi":"115.000000"},{"stationName":"跑马岭","StationCode":"295","Aqi":"113.000000"}]
         */

        private WetherInfoBean wetherInfo;
        private AirInfoBean AirInfo;
        private List<StationInfoBean> stationInfo;

        public WetherInfoBean getWetherInfo() {
            return wetherInfo;
        }

        public void setWetherInfo(WetherInfoBean wetherInfo) {
            this.wetherInfo = wetherInfo;
        }

        public AirInfoBean getAirInfo() {
            return AirInfo;
        }

        public void setAirInfo(AirInfoBean AirInfo) {
            this.AirInfo = AirInfo;
        }

        public List<StationInfoBean> getStationInfo() {
            return stationInfo;
        }

        public void setStationInfo(List<StationInfoBean> stationInfo) {
            this.stationInfo = stationInfo;
        }

        public static class WetherInfoBean {
            /**
             * aqi : 58
             * PollutionLevel : Ⅱ
             */

            private String aqi;
            private String PollutionLevel;

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getPollutionLevel() {
                return PollutionLevel;
            }

            public void setPollutionLevel(String PollutionLevel) {
                this.PollutionLevel = PollutionLevel;
            }
        }

        public static class AirInfoBean {
            /**
             * Pm25 : 33
             * Pm10 : 66
             * O3 : 98
             * So2 : 11
             * No2 : 15
             * Co :  0.529
             */

            private String Pm25;
            private String Pm10;
            private String O3;
            private String So2;
            private String No2;
            private String Co;

            public String getPm25() {
                return Pm25;
            }

            public void setPm25(String Pm25) {
                this.Pm25 = Pm25;
            }

            public String getPm10() {
                return Pm10;
            }

            public void setPm10(String Pm10) {
                this.Pm10 = Pm10;
            }

            public String getO3() {
                return O3;
            }

            public void setO3(String O3) {
                this.O3 = O3;
            }

            public String getSo2() {
                return So2;
            }

            public void setSo2(String So2) {
                this.So2 = So2;
            }

            public String getNo2() {
                return No2;
            }

            public void setNo2(String No2) {
                this.No2 = No2;
            }

            public String getCo() {
                return Co;
            }

            public void setCo(String Co) {
                this.Co = Co;
            }
        }

        public static class StationInfoBean {
            /**
             * stationName : 机床二厂
             * StationCode : 283
             * Aqi : 72.000000
             */

            private String stationName;
            private String StationCode;
            private String Aqi;

            public String getStationName() {
                return stationName;
            }

            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            public String getStationCode() {
                return StationCode;
            }

            public void setStationCode(String StationCode) {
                this.StationCode = StationCode;
            }

            public String getAqi() {
                return Aqi;
            }

            public void setAqi(String Aqi) {
                this.Aqi = Aqi;
            }
        }
    }
}
