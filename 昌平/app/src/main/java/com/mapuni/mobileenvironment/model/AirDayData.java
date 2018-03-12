package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2016/12/22.
 */

public class AirDayData {
    @Override
    public String toString() {
        return "AirDayData{" +
                "ret=" + ret +
                ", time='" + time + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * ret : 0
     * time : 2016-11-30 16:20:40
     * data : [{"aQI":71,"co":-1,"coIAQI":-1,"deviceName":"天通苑太平庄北街","deviceid":"A1ZQ4200076D0324","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":36,"pm25Average":51.934784,"pm25AverageIAQI":71,"pm25IAQI":51,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":126,"co":-1,"coIAQI":-1,"deviceName":"昌平体育馆东侧","deviceid":"A1ZQ4201061E0322","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":96,"pm25Average":78.891304,"pm25AverageIAQI":105,"pm25IAQI":126,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":105,"co":-1,"coIAQI":-1,"deviceName":"南邵","deviceid":"A1ZQ4201079F0328","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":79,"pm25Average":76.608696,"pm25AverageIAQI":102,"pm25IAQI":105,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":111,"co":-1,"coIAQI":-1,"deviceName":"龙腾苑二区","deviceid":"A1ZQ4202079E0322","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":84,"pm25Average":83.521736,"pm25AverageIAQI":111,"pm25IAQI":111,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":64,"co":-1,"coIAQI":-1,"deviceName":"白浮南桥","deviceid":"A1ZQ420306680329","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":39,"pm25Average":46.413044,"pm25AverageIAQI":64,"pm25IAQI":55,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":29,"co":-1,"coIAQI":-1,"deviceName":"和谐家园","deviceid":"A1ZQ420307DD032A","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":20,"pm25Average":19.913044,"pm25AverageIAQI":28,"pm25IAQI":29,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":57,"co":-1,"coIAQI":-1,"deviceName":"龙禧苑一区","deviceid":"A1ZQ42040657032D","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":21,"pm25Average":40.891304,"pm25AverageIAQI":57,"pm25IAQI":30,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":129,"co":-1,"coIAQI":-1,"deviceName":"北京涉外学院","deviceid":"A1ZQ420406BE032E","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":98,"pm25Average":48.108696,"pm25AverageIAQI":66,"pm25IAQI":129,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":3,"co":-1,"coIAQI":-1,"deviceName":"回龙观车站","deviceid":"A1ZQ420407550328","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":1,"pm25Average":1.8043479,"pm25AverageIAQI":3,"pm25IAQI":1,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":47,"co":-1,"coIAQI":-1,"deviceName":"沙河局","deviceid":"A1ZQ420507890324","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":22,"pm25Average":33.152172,"pm25AverageIAQI":47,"pm25IAQI":31,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"co":-1,"coIAQI":-1,"deviceName":"生命科技园","deviceid":"A1ZQ420507BF032D","most":"-","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":-1,"pm25Average":-1,"pm25AverageIAQI":-1,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":29,"co":-1,"coIAQI":-1,"deviceName":"歇甲庄西北","deviceid":"A1ZQ420707410322","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":-1,"pm25Average":20.282608,"pm25AverageIAQI":29,"pm25IAQI":-1,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":78,"co":-1,"coIAQI":-1,"deviceName":"昌平二屯","deviceid":"A1ZQ420907900320","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":57,"pm25Average":52.695652,"pm25AverageIAQI":72,"pm25IAQI":78,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":23,"co":-1,"coIAQI":-1,"deviceName":"昌盛路（昌盛园小区）","deviceid":"A1ZQ420A064A0327","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":13,"pm25Average":16.282608,"pm25AverageIAQI":23,"pm25IAQI":19,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":27,"co":-1,"coIAQI":-1,"deviceName":"风雅苑","deviceid":"A1ZQ420A07120325","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":15,"pm25Average":19.043478,"pm25AverageIAQI":27,"pm25IAQI":21,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":3,"co":-1,"coIAQI":-1,"deviceName":"史各庄","deviceid":"A1ZQ420A074C0322","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":2,"pm25Average":1.9347826,"pm25AverageIAQI":3,"pm25IAQI":3,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":167,"co":-1,"coIAQI":-1,"deviceName":"路庄","deviceid":"A1ZQ420A07770320","most":"pm25","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":127,"pm25Average":79.891304,"pm25AverageIAQI":106,"pm25IAQI":167,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":1,"co":-1,"coIAQI":-1,"deviceName":"玫瑰园南门","deviceid":"A1ZQ420A07B60321","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":0,"pm25Average":0.5217391,"pm25AverageIAQI":1,"pm25IAQI":0,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1},{"aQI":119,"co":-1,"coIAQI":-1,"deviceName":"化庄局站","deviceid":"A1ZQ420E07E1032A","most":"pm25Average","no2":-1,"no2IAQI":-1,"o3":-1,"o3Average":-1,"o3AverageIAQI":-1,"o3IAQI":-1,"pm10":-1,"pm10Average":-1,"pm10AverageIAQI":-1,"pm10IAQI":-1,"pm25":57,"pm25Average":89.891304,"pm25AverageIAQI":119,"pm25IAQI":78,"so2":-1,"so2IAQI":-1,"time":1480438800000,"type":1}]
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
         * aQI : 71
         * co : -1
         * coIAQI : -1
         * deviceName : 天通苑太平庄北街
         * deviceid : A1ZQ4200076D0324
         * most : pm25Average
         * no2 : -1
         * no2IAQI : -1
         * o3 : -1
         * o3Average : -1
         * o3AverageIAQI : -1
         * o3IAQI : -1
         * pm10 : -1
         * pm10Average : -1
         * pm10AverageIAQI : -1
         * pm10IAQI : -1
         * pm25 : 36
         * pm25Average : 51.934784
         * pm25AverageIAQI : 71
         * pm25IAQI : 51
         * so2 : -1
         * so2IAQI : -1
         * time : 1480438800000
         * type : 1
         */

        private double aQI;
        private double co;
        private double coIAQI;
        private String deviceName;
        private String deviceid;
        private String most;
        private double no2;
        private double no2IAQI;
        private double o3;
        private double o3Average;
        private double o3AverageIAQI;
        private double o3IAQI;
        private double pm10;
        private double pm10Average;
        private double pm10AverageIAQI;
        private double pm10IAQI;
        private double pm25;
        private double pm25Average;
        private double pm25AverageIAQI;
        private double pm25IAQI;
        private double so2;
        private double so2IAQI;
        private long time;
        private double type;

        public double getAQI() {
            return aQI;
        }

        public void setAQI(int aQI) {
            this.aQI = aQI;
        }

        public double getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public double getCoIAQI() {
            return coIAQI;
        }

        public void setCoIAQI(int coIAQI) {
            this.coIAQI = coIAQI;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
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

        public double getNo2IAQI() {
            return no2IAQI;
        }

        public void setNo2IAQI(int no2IAQI) {
            this.no2IAQI = no2IAQI;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(int o3) {
            this.o3 = o3;
        }

        public double getO3Average() {
            return o3Average;
        }

        public void setO3Average(int o3Average) {
            this.o3Average = o3Average;
        }

        public double getO3AverageIAQI() {
            return o3AverageIAQI;
        }

        public void setO3AverageIAQI(int o3AverageIAQI) {
            this.o3AverageIAQI = o3AverageIAQI;
        }

        public double getO3IAQI() {
            return o3IAQI;
        }

        public void setO3IAQI(int o3IAQI) {
            this.o3IAQI = o3IAQI;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(int pm10) {
            this.pm10 = pm10;
        }

        public double getPm10Average() {
            return pm10Average;
        }

        public void setPm10Average(int pm10Average) {
            this.pm10Average = pm10Average;
        }

        public double getPm10AverageIAQI() {
            return pm10AverageIAQI;
        }

        public void setPm10AverageIAQI(int pm10AverageIAQI) {
            this.pm10AverageIAQI = pm10AverageIAQI;
        }

        public double getPm10IAQI() {
            return pm10IAQI;
        }

        public void setPm10IAQI(int pm10IAQI) {
            this.pm10IAQI = pm10IAQI;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public double getPm25Average() {
            return pm25Average;
        }

        public void setPm25Average(double pm25Average) {
            this.pm25Average = pm25Average;
        }

        public double getPm25AverageIAQI() {
            return pm25AverageIAQI;
        }

        public void setPm25AverageIAQI(int pm25AverageIAQI) {
            this.pm25AverageIAQI = pm25AverageIAQI;
        }

        public double getPm25IAQI() {
            return pm25IAQI;
        }

        public void setPm25IAQI(int pm25IAQI) {
            this.pm25IAQI = pm25IAQI;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(int so2) {
            this.so2 = so2;
        }

        public double getSo2IAQI() {
            return so2IAQI;
        }

        public void setSo2IAQI(int so2IAQI) {
            this.so2IAQI = so2IAQI;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public double getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
