package com.mapuni.caremission_ens.bean;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yawei on 2017/3/29.
 */

public class MapBean {

    /**
     * flag : 1
     * stationInfo : [{"PKID":"1404020061","FZRPHONE":"15972671251","LONGITUDE":112.22501,"ISOPEN":"营业","LATITUDE":30.97407,"STATIONNAME":"荆门市万畅汽车服务有限公司"},{"PKID":"4208220021","FZRPHONE":"13997935597","LONGITUDE":30.697307,"ISOPEN":"营业","LATITUDE":112.545452,"STATIONNAME":"沙洋县平安机动车检测服务有限公司"},{"PKID":"4201130031","FZRPHONE":"13807260380","LONGITUDE":112.19876,"ISOPEN":"营业","LATITUDE":30.98009,"STATIONNAME":"荆门福元机动车检测有限公司"},{"PKID":"4208210011","FZRPHONE":"13886903688","LONGITUDE":133.15218,"ISOPEN":"营业","LATITUDE":31.0153,"STATIONNAME":"京山南强机动车综合性能检测有限公司"},{"PKID":"4201130021","FZRPHONE":"13085188338","LONGITUDE":112.24309,"ISOPEN":"营业","LATITUDE":31.04128,"STATIONNAME":"荆门骏腾机动车检测有限公司"},{"PKID":"4208220011","FZRPHONE":"15872918186","LONGITUDE":112.57972,"ISOPEN":"营业","LATITUDE":30.65706,"STATIONNAME":"沙洋凯达机动车检测有限公司"},{"PKID":"4208000011","FZRPHONE":"13908695028","LONGITUDE":112.20069,"ISOPEN":"营业","LATITUDE":31.0159,"STATIONNAME":"荆门市中辰机动车检测有限公司"},{"PKID":"4208810021","FZRPHONE":"15072004422","LONGITUDE":112.57168,"ISOPEN":"营业","LATITUDE":31.14986,"STATIONNAME":"钟祥楚玉机动车检测有限公司"},{"PKID":"4208220031","FZRPHONE":"18986961636","LONGITUDE":30,"ISOPEN":"营业","LATITUDE":121,"STATIONNAME":"沙洋县城南机动车检测服务有限公司"},{"PKID":"4201130061","FZRPHONE":"18162990649","LONGITUDE":112.21846,"ISOPEN":"营业","LATITUDE":31.05336,"STATIONNAME":"荆门万里机动车检测服务有限公司"},{"PKID":"4201130081","FZRPHONE":"13658797979","LONGITUDE":112.858611,"ISOPEN":"营业","LATITUDE":30.833888,"STATIONNAME":"荆门市五三顺畅机动车检测有限公司"},{"PKID":"4208810031","FZRPHONE":"15271760001","LONGITUDE":131,"ISOPEN":"营业","LATITUDE":111,"STATIONNAME":"钟祥市恒旺机动车安全检测服务有限公司"},{"PKID":"4201130071","FZRPHONE":"18086285222","LONGITUDE":112.242179,"ISOPEN":"营业","LATITUDE":30.972695,"STATIONNAME":"耀东检测站"},{"PKID":"4208810041","FZRPHONE":"13886929668","LONGITUDE":112.323641,"ISOPEN":"营业","LATITUDE":31.438373,"STATIONNAME":"钟祥市炎鑫机动车综合性能检测有限公司"},{"PKID":"4208000021","FZRPHONE":"13333333333","LONGITUDE":11,"ISOPEN":"不营业","LATITUDE":11,"STATIONNAME":"测试1"},{"PKID":"4208810051","FZRPHONE":"15072898202","LONGITUDE":30.57,"ISOPEN":"营业","LATITUDE":112.13,"STATIONNAME":"荆门市三峰贸易有限公司"}]
     * limitPoint : [[{"LATITUDE":109.4852491896,"LONGITUDE":30.2950140856},{"LATITUDE":109.4847821896,"LONGITUDE":30.2962920856},{"LATITUDE":109.4820984479,"LONGITUDE":30.2996319883},{"LATITUDE":109.4813434479,"LONGITUDE":30.3009729883},{"LATITUDE":109.4805534479,"LONGITUDE":30.3041219883},{"LATITUDE":109.4806974479,"LONGITUDE":30.3085809883},{"LATITUDE":109.4819998174,"LONGITUDE":30.3120757789},{"LATITUDE":109.4805628174,"LONGITUDE":30.3156927789},{"LATITUDE":109.4803108174,"LONGITUDE":30.3176567789},{"LATITUDE":109.4804227639,"LONGITUDE":30.321960156},{"LATITUDE":109.4809617639,"LONGITUDE":30.326479156},{"LATITUDE":109.4805279699,"LONGITUDE":30.330187575},{"LATITUDE":109.4808149699,"LONGITUDE":30.333927575},{"LATITUDE":109.4808869699,"LONGITUDE":30.334956575},{"LATITUDE":109.4826119699,"LONGITUDE":30.337511575},{"LATITUDE":109.4825709125,"LONGITUDE":30.3402844466},{"LATITUDE":109.4854819125,"LONGITUDE":30.3420924466},{"LATITUDE":109.4874219125,"LONGITUDE":30.3426224466},{"LATITUDE":109.4884279125,"LONGITUDE":30.3439314466}]]
     */

    private int flag;
    private List<StationInfoBean> stationInfo;
    private List<List<LimitPointBean>> limitPoint;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<StationInfoBean> getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(List<StationInfoBean> stationInfo) {
        this.stationInfo = stationInfo;
    }

    public List<List<LatLng>> getLimitPoint() {
        if(limitPoint.size()>0){
            List data = new ArrayList();
            for(List<LimitPointBean> list:limitPoint){
                List _List = new ArrayList();
                for(LimitPointBean bean:list){
                    _List.add(bean.getLatLng());
                }
                data.add(_List);
            }
            return data;
        }
        return null;
    }

    public void setLimitPoint(List<List<LimitPointBean>> limitPoint) {
        this.limitPoint = limitPoint;
    }

    public static class StationInfoBean {
        /**
         * PKID : 1404020061
         * FZRPHONE : 15972671251
         * LONGITUDE : 112.22501
         * ISOPEN : 营业
         * LATITUDE : 30.97407
         * STATIONNAME : 荆门市万畅汽车服务有限公司
         */

        private String PKID;
        private String FZRPHONE;
        private double LONGITUDE;
        private String ISOPEN;
        private double LATITUDE;
        private String STATIONNAME;

        public String getPKID() {
            return PKID;
        }

        public void setPKID(String PKID) {
            this.PKID = PKID;
        }

        public String getFZRPHONE() {
            return FZRPHONE;
        }

        public void setFZRPHONE(String FZRPHONE) {
            this.FZRPHONE = FZRPHONE;
        }

        public double getLONGITUDE() {
            return LONGITUDE;
        }

        public void setLONGITUDE(double LONGITUDE) {
            this.LONGITUDE = LONGITUDE;
        }

        public String getISOPEN() {
            return ISOPEN;
        }

        public void setISOPEN(String ISOPEN) {
            this.ISOPEN = ISOPEN;
        }

        public double getLATITUDE() {
            return LATITUDE;
        }

        public void setLATITUDE(double LATITUDE) {
            this.LATITUDE = LATITUDE;
        }

        public String getSTATIONNAME() {
            return STATIONNAME;
        }

        public void setSTATIONNAME(String STATIONNAME) {
            this.STATIONNAME = STATIONNAME;
        }
    }

    public static class LimitPointBean {
        /**
         * LATITUDE : 109.4852491896
         * LONGITUDE : 30.2950140856
         */

        private double LATITUDE;
        private double LONGITUDE;

        public double getLATITUDE() {
            return LATITUDE;
        }

        public void setLATITUDE(double LATITUDE) {
            this.LATITUDE = LATITUDE;
        }

        public double getLONGITUDE() {
            return LONGITUDE;
        }
        public LatLng getLatLng(){
            LatLng latLng = new LatLng(LATITUDE,LONGITUDE);
            return latLng;
        }
        public void setLONGITUDE(double LONGITUDE) {
            this.LONGITUDE = LONGITUDE;
        }
    }
}
