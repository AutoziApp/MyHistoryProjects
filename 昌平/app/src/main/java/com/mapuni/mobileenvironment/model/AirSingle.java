package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2016/12/20.
 */

public class AirSingle {

    /**
     * ret : 0
     * time : 2016-12-20 15:07:19
     * data : [{"aqi":140,"autoid":2,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm25","no2":-1,"o3":-1,"pm10":228,"pm25":107,"so2":-1,"time":1478019600000},{"aqi":139,"autoid":49,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm25","no2":-1,"o3":-1,"pm10":226,"pm25":106,"so2":-1,"time":1478023200000},{"aqi":128,"autoid":96,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":205,"pm25":97,"so2":-1,"time":1478026800000},{"aqi":120,"autoid":143,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":190,"pm25":90,"so2":-1,"time":1478030400000},{"aqi":117,"autoid":190,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":184,"pm25":88,"so2":-1,"time":1478034000000},{"aqi":115,"autoid":237,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":180,"pm25":86,"so2":-1,"time":1478037600000},{"aqi":118,"autoid":284,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":185,"pm25":88,"so2":-1,"time":1478041200000},{"aqi":125,"autoid":331,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":199,"pm25":95,"so2":-1,"time":1478044800000},{"aqi":156,"autoid":378,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm25","no2":-1,"o3":-1,"pm10":254,"pm25":119,"so2":-1,"time":1478048400000},{"aqi":114,"autoid":425,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":177,"pm25":85,"so2":-1,"time":1478052000000},{"aqi":100,"autoid":472,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":149,"pm25":72,"so2":-1,"time":1478055600000},{"aqi":101,"autoid":519,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":152,"pm25":73,"so2":-1,"time":1478059200000},{"aqi":84,"autoid":566,"co":-1,"deviceid":"A1YQ5200042D0518","most":"pm10","no2":-1,"o3":-1,"pm10":117,"pm25":57,"so2":-1,"time":1478062800000}]
     */

    private int ret;
    private String time;
    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aqi : 140
         * autoid : 2
         * co : -1
         * deviceid : A1YQ5200042D0518
         * most : pm25
         * no2 : -1
         * o3 : -1
         * pm10 : 228
         * pm25 : 107
         * so2 : -1
         * time : 1478019600000
         */

        private double aqi;
        private double autoid;
        private double co;
        private String deviceid;
        private String most;
        private double no2;
        private double o3;
        private double pm10;
        private double pm25;
        private double so2;
        private long time;

        public double getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public double getAutoid() {
            return autoid;
        }

        public void setAutoid(int autoid) {
            this.autoid = autoid;
        }

        public double getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getMost() {
            return most;
        }

        public void setMost(String most) {
            this.most = most;
        }

        public double getNo2() {
            return no2;
        }

        public void setNo2(int no2) {
            this.no2 = no2;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(int o3) {
            this.o3 = o3;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(int pm10) {
            this.pm10 = pm10;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(int so2) {
            this.so2 = so2;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
