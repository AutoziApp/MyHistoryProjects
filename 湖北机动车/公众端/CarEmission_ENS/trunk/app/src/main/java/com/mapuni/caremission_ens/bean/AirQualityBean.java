package com.mapuni.caremission_ens.bean;

import java.util.List;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/10/25 9:58
 * @change
 * @chang time
 * @class 空气质量
 */

public class AirQualityBean {
    
    /**
     * info : [{"AQI":51,"CO":"0.4","NO2":"25","O3":"44","PKID":"1504868092467_77","PM10":"52","PM25":"9","SO2":"12","STATIONNAME":"武汉市"}]
     * result : 1
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
         * AQI : 51
         * CO : 0.4
         * NO2 : 25
         * O3 : 44
         * PKID : 1504868092467_77
         * PM10 : 52
         * PM25 : 9
         * SO2 : 12
         * STATIONNAME : 武汉市
         */

        private int AQI;
        private String CO;
        private String NO2;
        private String O3;
        private String PKID;
        private String PM10;
        private String PM25;
        private String SO2;
        private String STATIONNAME;

        public int getAQI() {
            return AQI;
        }

        public void setAQI(int AQI) {
            this.AQI = AQI;
        }

        public String getCO() {
            return CO;
        }

        public void setCO(String CO) {
            this.CO = CO;
        }

        public String getNO2() {
            return NO2;
        }

        public void setNO2(String NO2) {
            this.NO2 = NO2;
        }

        public String getO3() {
            return O3;
        }

        public void setO3(String O3) {
            this.O3 = O3;
        }

        public String getPKID() {
            return PKID;
        }

        public void setPKID(String PKID) {
            this.PKID = PKID;
        }

        public String getPM10() {
            return PM10;
        }

        public void setPM10(String PM10) {
            this.PM10 = PM10;
        }

        public String getPM25() {
            return PM25;
        }

        public void setPM25(String PM25) {
            this.PM25 = PM25;
        }

        public String getSO2() {
            return SO2;
        }

        public void setSO2(String SO2) {
            this.SO2 = SO2;
        }

        public String getSTATIONNAME() {
            return STATIONNAME;
        }

        public void setSTATIONNAME(String STATIONNAME) {
            this.STATIONNAME = STATIONNAME;
        }
    }
}
