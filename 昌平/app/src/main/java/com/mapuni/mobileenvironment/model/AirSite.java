package com.mapuni.mobileenvironment.model;

import java.util.List;

/**
 * Created by Mai on 2016/12/19.
 */

public class AirSite {

    @Override
    public String toString() {
        return "AirSite{" +
                "ret=" + ret +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * ret : 0
     * time : 2016-12-19 17:28:23
     * data : [{"address":"兴寿村委会","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ5200026B0515"},"deviceid":"A1YQ5200026B0515","district":"昌平区","dname":"044兴寿村委会","dtype":"ATLAS3.0","installtime":1480435200000,"lat":40.22966,"lon":116.414398,"poitype":"农村","province":"北京"},{"address":"教学楼6楼天台","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ5200042D0518"},"deviceid":"A1YQ5200042D0518","district":"昌平","dname":"005阳坊中学","dtype":"ATLAS3.0","installtime":1478793600000,"lat":40.154125,"lon":116.153885,"poitype":"道路","province":"北京"},{"address":"棉辛路","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520101BC051D"},"deviceid":"A1YQ520101BC051D","district":"昌平区","dname":"012 西辛峰村社区服务站","dtype":"ATLAS3.0","installtime":1479916800000,"lat":40.206696,"lon":116.342163,"poitype":"道路","province":"北京"},{"address":"锥石口村村委会","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520102400511"},"deviceid":"A1YQ520102400511","district":"昌平区","dname":"025 锥石口村社区卫生服务站","dtype":"ATLAS3.0","installtime":1480003200000,"lat":40.331554,"lon":116.222786,"poitype":"道路","province":"北京"},{"address":"八口村村委会大门口北十米配电房","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ5201026C0513"},"deviceid":"A1YQ5201026C0513","district":"昌平区","dname":"001八口村","dtype":"ATLAS3.0","installtime":1478707200000,"lat":40.15395,"lon":116.175415,"poitype":"道路","province":"北京"},{"address":"镇政府楼顶","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520102B80510"},"deviceid":"A1YQ520102B80510","district":"昌平区","dname":"019 崔村镇政府","dtype":"ATLAS3.0","installtime":1480003200000,"lat":40.226379,"lon":116.359306,"poitype":"道路","province":"北京"},{"address":"麻峪路北口公共厕所","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520102F30515"},"deviceid":"A1YQ520102F30515","district":"昌平区","dname":"023麻峪上村","dtype":"ATLAS3.0","installtime":1480003200000,"lat":40.249802,"lon":116.387909,"poitype":"农村","province":"北京"},{"address":"小学体育器材室楼顶","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520103A30515"},"deviceid":"A1YQ520103A30515","district":"昌平区","dname":"004四家庄小学","dtype":"ATLAS3.0","installtime":1478707200000,"lat":40.172516,"lon":116.16259,"poitype":"道路","province":"北京"},{"address":"阳坊镇政府新楼顶","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ520204770513"},"deviceid":"A1YQ520204770513","district":"昌平","dname":"007阳坊镇政府","dtype":"ATLAS3.0","installtime":1478793600000,"lat":40.144642,"lon":116.143806,"poitype":"道路","province":"北京"},{"address":"村委会西浴池屋顶","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ52030242051D"},"deviceid":"A1YQ52030242051D","district":"昌平区","dname":"003后白虎涧村","dtype":"ATLAS3.0","installtime":1478707200000,"lat":40.133232,"lon":116.129433,"poitype":"道路","province":"北京"},{"address":"东新城村委会","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ5203029B0515"},"deviceid":"A1YQ5203029B0515","district":"昌平区","dname":"048 东新城村","dtype":"ATLAS3.0","installtime":1480521600000,"lat":40.224941,"lon":116.465691,"poitype":"农村","province":"北京"},{"address":"村委会门卫房","attachList":[],"city":"北京","dStatus":{"deviceStatus":1,"deviceidS":"A1YQ52040368051C"},"deviceid":"A1YQ52040368051C","district":"昌平区","dname":"002东贯口市村","dtype":"ATLAS3.0","installtime":1478707200000,"lat":40.141319,"lon":116.16671,"poitype":"道路","province":"北京"}]
     */

    private int ret; //请求结果 0 成功  1 失败
    private String time;
    private List<DataBean> data;//设备信息数组

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
         * address : 兴寿村委会
         * attachList : []
         * city : 北京
         * dStatus : {"deviceStatus":1,"deviceidS":"A1YQ5200026B0515"}
         * deviceid : A1YQ5200026B0515
         * district : 昌平区
         * dname : 044兴寿村委会
         * dtype : ATLAS3.0
         * installtime : 1480435200000
         * lat : 40.22966
         * lon : 116.414398
         * poitype : 农村
         * province : 北京
         */

        private String address;
        private String city;
        private DStatusBean dStatus;
        private String deviceid;
        private String district;
        private String dname;
        private String dtype;
        private long installtime;
        private double lat;
        private double lon;
        private String poitype;
        private String province;
        private List<?> attachList;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public DStatusBean getDStatus() {
            return dStatus;
        }

        public void setDStatus(DStatusBean dStatus) {
            this.dStatus = dStatus;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getDtype() {
            return dtype;
        }

        public void setDtype(String dtype) {
            this.dtype = dtype;
        }

        public long getInstalltime() {
            return installtime;
        }

        public void setInstalltime(long installtime) {
            this.installtime = installtime;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getPoitype() {
            return poitype;
        }

        public void setPoitype(String poitype) {
            this.poitype = poitype;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<?> getAttachList() {
            return attachList;
        }

        public void setAttachList(List<?> attachList) {
            this.attachList = attachList;
        }

        public static class DStatusBean {
            /**
             * deviceStatus : 1
             * deviceidS : A1YQ5200026B0515
             */

            private int deviceStatus;
            private String deviceidS;

            public int getDeviceStatus() {
                return deviceStatus;
            }

            public void setDeviceStatus(int deviceStatus) {
                this.deviceStatus = deviceStatus;
            }

            public String getDeviceidS() {
                return deviceidS;
            }

            public void setDeviceidS(String deviceidS) {
                this.deviceidS = deviceidS;
            }
        }
    }
}
