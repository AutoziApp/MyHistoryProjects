package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2017/1/18.
 */

public class AirDayHistory {
    @Override
    public String toString() {
        return "AirDayHistory{" +
                "ret=" + ret +
                ", time='" + time + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * ret : 0
     * time : 2017-01-13 09:45:01
     * data : [{"aqi":38,"autoId":224,"co":0.533333,"deviceId":"1002A","most":"无","no2":15.75,"o3":80,"o3EH":76.75,"pm10":30.1579,"pm25":20.125,"reserve2L":"定陵","so2":7.70833,"time":1484067600000},{"aqi":38,"autoId":4065,"co":0.422727,"deviceId":"1002A","most":"无","no2":11.0455,"o3":83,"o3EH":76.875,"pm10":28,"pm25":17.4545,"reserve2L":"定陵","so2":5.45455,"time":1484154000000}]
     * msg : 请求成功！
     */

    private int ret;
    private String time;
    private String msg;
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
         * aqi : 38
         * autoId : 224
         * co : 0.533333
         * deviceId : 1002A
         * most : 无
         * no2 : 15.75
         * o3 : 80
         * o3EH : 76.75
         * pm10 : 30.1579
         * pm25 : 20.125
         * reserve2L : 定陵
         * so2 : 7.70833
         * time : 1484067600000
         */

        private double aqi;
        private double autoId;
        private double co;
        private String deviceId;
        private String most;
        private double no2;
        private double o3;
        private double o3EH;
        private double pm10;
        private double pm25;
        private String reserve2L;
        private double so2;
        private long time;

        public double getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public double getAutoId() {
            return autoId;
        }

        public void setAutoId(int autoId) {
            this.autoId = autoId;
        }

        public double getCo() {
            return co;
        }

        public void setCo(double co) {
            this.co = co;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
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

        public void setNo2(double no2) {
            this.no2 = no2;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(int o3) {
            this.o3 = o3;
        }

        public double getO3EH() {
            return o3EH;
        }

        public void setO3EH(double o3EH) {
            this.o3EH = o3EH;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(double pm10) {
            this.pm10 = pm10;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(double pm25) {
            this.pm25 = pm25;
        }

        public String getReserve2L() {
            return reserve2L;
        }

        public void setReserve2L(String reserve2L) {
            this.reserve2L = reserve2L;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(double so2) {
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
