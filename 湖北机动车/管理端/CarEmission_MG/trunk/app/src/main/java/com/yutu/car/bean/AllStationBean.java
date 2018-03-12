package com.yutu.car.bean;

import java.util.List;

/**
 * Created by yawei on 2017/3/28.
 */

public class AllStationBean {
    /**
     * result : 1
     * info : [{"xzqh":"4202","stationinfo":[],"qhmc":"黄石市"},{"xzqh":"4203","stationinfo":[],"qhmc":"十堰市"},{"xzqh":"4205","stationinfo":[],"qhmc":"宜昌市"},{"xzqh":"4206","stationinfo":[{"stationname":"枣阳市环宇通机动车检测有限公司","stationpkid":"42068303"},{"stationname":"襄阳市安安机动车检测有限公司","stationpkid":"42060601"},{"stationname":"老河口市顺通机动车检测有限公司","stationpkid":"42068201"},{"stationname":"襄阳东盛源汽车部件有限公司","stationpkid":"42060704"},{"stationname":"襄阳市汇宝机动车代理服务有限公司","stationpkid":"42060103"},{"stationname":"襄阳金华港汽车零部件有限公司","stationpkid":"42060102"},{"stationname":"襄阳市正益汽车检测技术服务有限公司","stationpkid":"42060101"},{"stationname":"宜城市鹏展服务有限公司","stationpkid":"42068403"},{"stationname":"宜城市一通机动车辆服务有限公司","stationpkid":"42068401"},{"stationname":"湖北玉阳开拓机动车服务有限公司","stationpkid":"42068302"},{"stationname":"枣阳市恒昌机动车综合性能检测站","stationpkid":"42068301"},{"stationname":"襄阳市和平汽车检测有限公司","stationpkid":"42060201"},{"stationname":"襄阳顺发机动车检测有限公司","stationpkid":"42060707"},{"stationname":"襄阳胜超机动车检测有限公司","stationpkid":"42060706"},{"stationname":"襄阳市南漳县鼎盛车辆检测站","stationpkid":"42062402"},{"stationname":"襄阳市襄州区车城机动车检测站","stationpkid":"42060705"},{"stationname":"保康县力源机动车辆综合性能检测有限责任公司","stationpkid":"42062602"},{"stationname":"襄阳瑞森汽车检测有限公司","stationpkid":"42060105"},{"stationname":"老河口市天通服务有限责任公司","stationpkid":"42068202"},{"stationname":"襄阳市麒麟机动车检测站","stationpkid":"42060202"},{"stationname":"南漳县惠安机动车检测有限责任公司","stationpkid":"42062401"},{"stationname":"谷城洪景机动车检测公司","stationpkid":"42062501"},{"stationname":"襄阳四方博锐机动车检测服务有限公司","stationpkid":"42060104"}],"qhmc":"襄阳市"},{"xzqh":"4207","stationinfo":[],"qhmc":"鄂州市"},{"xzqh":"4208","stationinfo":[],"qhmc":"荆门市"},{"xzqh":"4209","stationinfo":[],"qhmc":"孝感市"},{"xzqh":"4210","stationinfo":[],"qhmc":"荆州市"},{"xzqh":"4211","stationinfo":[],"qhmc":"黄冈市"},{"xzqh":"4212","stationinfo":[],"qhmc":"咸宁市"},{"xzqh":"4213","stationinfo":[],"qhmc":"随州市"},{"xzqh":"4228","stationinfo":[],"qhmc":"恩施州"},{"xzqh":"429004","stationinfo":[],"qhmc":"仙桃市"},{"xzqh":"429005","stationinfo":[],"qhmc":"潜江市"},{"xzqh":"429006","stationinfo":[],"qhmc":"天门市"},{"xzqh":"429021","stationinfo":[{"stationname":"神农架汇海机动车检测有限公司","stationpkid":"42902101"}],"qhmc":"神农架"},{"xzqh":"4201","stationinfo":[],"qhmc":"武汉市"}]
     */

    private int result;
    private List<InfoBean> info;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
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
         * xzqh : 4202
         * stationinfo : []
         * qhmc : 黄石市
         */

        private String xzqh;
        private String qhmc;
        private List<StationInfoBean> stationinfo;

        public String getXzqh() {
            return xzqh;
        }

        public void setXzqh(String xzqh) {
            this.xzqh = xzqh;
        }

        public String getQhmc() {
            return qhmc;
        }

        public void setQhmc(String qhmc) {
            this.qhmc = qhmc;
        }

        public List<StationInfoBean> getStationinfo() {
            return stationinfo;
        }

        public void setStationinfo(List<StationInfoBean> stationinfo) {
            this.stationinfo = stationinfo;
        }



        public static class StationInfoBean{
//            stationname: "襄阳四方博锐机动车检测服务有限公司",
//            stationpkid: "42060104"


            private String stationname;
            private String stationpkid;


            public String getStationname() {
                return stationname;
            }

            public void setStationname(String stationname) {
                this.stationname = stationname;
            }

            public String getStationpkid() {
                return stationpkid;
            }

            public void setStationpkid(String stationpkid) {
                this.stationpkid = stationpkid;
            }
        }
    }
}
