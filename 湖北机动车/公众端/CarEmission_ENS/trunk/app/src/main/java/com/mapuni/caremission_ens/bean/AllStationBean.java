package com.mapuni.caremission_ens.bean;

import java.util.List;

/**
 * Created by yawei on 2017/3/28.
 */

public class AllStationBean {
    /**
     * flag : 1
     * data : [{"stations":[{"PKID":"4208000011","STATIONNAME":"荆门市中辰机动车检测有限公司"},{"PKID":"4208000021","STATIONNAME":"测试1"}],"regionName":"荆门市"},{"stations":[],"regionName":"东宝区"},{"stations":[{"PKID":"1404020061","STATIONNAME":"荆门市万畅汽车服务有限公司"},{"PKID":"4201130031","STATIONNAME":"荆门福元机动车检测有限公司"},{"PKID":"4201130021","STATIONNAME":"荆门骏腾机动车检测有限公司"},{"PKID":"4201130061","STATIONNAME":"荆门万里机动车检测服务有限公司"},{"PKID":"4201130071","STATIONNAME":"耀东检测站"},{"PKID":"4208810051","STATIONNAME":"荆门市三峰贸易有限公司"}],"regionName":"掇刀区"},{"stations":[{"PKID":"4208220021","STATIONNAME":"沙洋县平安机动车检测服务有限公司"},{"PKID":"4208220011","STATIONNAME":"沙洋凯达机动车检测有限公司"},{"PKID":"4208220031","STATIONNAME":"沙洋县城南机动车检测服务有限公司"}],"regionName":"沙洋县"},{"stations":[{"PKID":"4208210011","STATIONNAME":"京山南强机动车综合性能检测有限公司"}],"regionName":"京山县"},{"stations":[{"PKID":"4208810021","STATIONNAME":"钟祥楚玉机动车检测有限公司"},{"PKID":"4208810031","STATIONNAME":"钟祥市恒旺机动车安全检测服务有限公司"},{"PKID":"4208810041","STATIONNAME":"钟祥市炎鑫机动车综合性能检测有限公司"}],"regionName":"钟祥市"},{"stations":[{"PKID":"4201130081","STATIONNAME":"荆门市五三顺畅机动车检测有限公司"}],"regionName":"屈家岭"}]
     */

    private int flag;
    private List<DataBean> data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * stations : [{"PKID":"4208000011","STATIONNAME":"荆门市中辰机动车检测有限公司"}
         * ,{"PKID":"4208000021","STATIONNAME":"测试1"}]
         * regionName : 荆门市
         */

        private String regionName;
        private List<StationsBean> stations;

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public List<StationsBean> getStations() {
            return stations;
        }

        public void setStations(List<StationsBean> stations) {
            this.stations = stations;
        }

        public static class StationsBean {
            /**
             * PKID : 4208000011
             * STATIONNAME : 荆门市中辰机动车检测有限公司
             */

            private String PKID;
            private String STATIONNAME;

            public String getSTATIONSHORTNAME() {
                return STATIONSHORTNAME;
            }

            public void setSTATIONSHORTNAME(String STATIONSHORTNAME) {
                this.STATIONSHORTNAME = STATIONSHORTNAME;
            }

            private String STATIONSHORTNAME;

            public String getPKID() {
                return PKID;
            }

            public void setPKID(String PKID) {
                this.PKID = PKID;
            }

            public String getSTATIONNAME() {
                return STATIONNAME;
            }

            public void setSTATIONNAME(String STATIONNAME) {
                this.STATIONNAME = STATIONNAME;
            }
        }
    }
}
