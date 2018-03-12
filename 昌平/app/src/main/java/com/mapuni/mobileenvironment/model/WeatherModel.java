package com.mapuni.mobileenvironment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yawei on 2016/12/26.
 */
public class WeatherModel {
    /**
     * flag : true
     * nowWeather : [{"city":"昌平","weather":"阴转多云","temp":"4℃~-7℃","date":"12/26","weekday":"星期一","feelTemp":"3℃","realTime":"今天 13:00发布","level":"优","PM2Dot5Data":"26","pm25":"18","position_name":"北京","level_near":"优","PM2Dot5Data_near":"26","pm25_near":"18","position_name_near":"北京","WD":"西风","WS":"3级","SD":"26%","Lunar":"农历十一月二十八"}]
     * weather : [{"temp":"4℃~-7℃","weather":"阴转多云","date":"12/26","week":"今天"},{"temp":"2℃~-7℃","weather":"晴转多云","date":"12/27","week":"明天"},{"temp":"4℃~-7℃","weather":"多云转晴","date":"12/28","week":"周三"},{"temp":"0℃~-6℃","weather":"晴转多云","date":"12/29","week":"周四"},{"temp":"1℃~-7℃","weather":"多云转晴","date":"12/30","week":"周五"},{"temp":"4℃~-5℃","weather":"晴","date":"12/31","week":"周六"}]
     * index : [{"index_cy":"冷","index_cy_xs":"建议着棉衣加羊毛衫等冬季服装。","index_uv":"中等","index_uv_xs":"外出除带防护帽和太阳镜外，请涂擦防晒霜","index_tr":"视情况而定","index_tr_xs":"天气不错，可以出去旅游一下的","index_co":"一般","index_co_xs":"基本无污染，天气情况较好，总体感觉比较舒适","index_cl":"不适宜","index_cl_xs":"今天的天气状况不适宜晨练","index_ls":"","index_ls_xs":"基本无污染，天气情况较好，适宜晾晒","index_xc":"较适宜","index_xc_xs":"基本无污染，天气情况较好，适宜洗车"}]
     */

    private boolean flag;
    private List<NowWeatherBean> nowWeather;
    private List<WeatherBean> weather;
    private List<IndexBean> index;
    private ArrayList overList;

    public ArrayList getOverList() {
        return overList;
    }

    public void setOverList(ArrayList overList) {
        this.overList = overList;
    }

    public boolean isFlag() {
        return flag;

    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<NowWeatherBean> getNowWeather() {
        return nowWeather;
    }

    public void setNowWeather(List<NowWeatherBean> nowWeather) {
        this.nowWeather = nowWeather;
    }

    public List<WeatherBean> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherBean> weather) {
        this.weather = weather;
    }

    public List<IndexBean> getIndex() {
        return index;
    }

    public void setIndex(List<IndexBean> index) {
        this.index = index;
    }

    public static class NowWeatherBean {
        /**
         * city : 昌平
         * weather : 阴转多云
         * temp : 4℃~-7℃
         * date : 12/26
         * weekday : 星期一
         * feelTemp : 3℃
         * realTime : 今天 13:00发布
         * level : 优
         * PM2Dot5Data : 26
         * pm25 : 18
         * position_name : 北京
         * level_near : 优
         * PM2Dot5Data_near : 26
         * pm25_near : 18
         * position_name_near : 北京
         * WD : 西风
         * WS : 3级
         * SD : 26%
         * Lunar : 农历十一月二十八
         */

        private String city;
        private String weather;
        private String temp;
        private String date;
        private String weekday;
        private String feelTemp;
        private String realTime;
        private String level;
        private String PM2Dot5Data;
        private String pm25;
        private String position_name;
        private String level_near;
        private String PM2Dot5Data_near;
        private String pm25_near;
        private String position_name_near;
        private String WD;
        private String WS;
        private String SD;
        private String Lunar;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeekday() {
            return weekday;
        }

        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getFeelTemp() {
            return feelTemp;
        }

        public void setFeelTemp(String feelTemp) {
            this.feelTemp = feelTemp;
        }

        public String getRealTime() {
            return realTime;
        }

        public void setRealTime(String realTime) {
            this.realTime = realTime;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPM2Dot5Data() {
            return PM2Dot5Data;
        }

        public void setPM2Dot5Data(String PM2Dot5Data) {
            this.PM2Dot5Data = PM2Dot5Data;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getPosition_name() {
            return position_name;
        }

        public void setPosition_name(String position_name) {
            this.position_name = position_name;
        }

        public String getLevel_near() {
            return level_near;
        }

        public void setLevel_near(String level_near) {
            this.level_near = level_near;
        }

        public String getPM2Dot5Data_near() {
            return PM2Dot5Data_near;
        }

        public void setPM2Dot5Data_near(String PM2Dot5Data_near) {
            this.PM2Dot5Data_near = PM2Dot5Data_near;
        }

        public String getPm25_near() {
            return pm25_near;
        }

        public void setPm25_near(String pm25_near) {
            this.pm25_near = pm25_near;
        }

        public String getPosition_name_near() {
            return position_name_near;
        }

        public void setPosition_name_near(String position_name_near) {
            this.position_name_near = position_name_near;
        }

        public String getWD() {
            return WD;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public String getWS() {
            return WS;
        }

        public void setWS(String WS) {
            this.WS = WS;
        }

        public String getSD() {
            return SD;
        }

        public void setSD(String SD) {
            this.SD = SD;
        }

        public String getLunar() {
            return Lunar;
        }

        public void setLunar(String Lunar) {
            this.Lunar = Lunar;
        }
    }

    public static class WeatherBean {
        /**
         * temp : 4℃~-7℃
         * weather : 阴转多云
         * date : 12/26
         * week : 今天
         */

        private String temp;
        private String weather;
        private String date;
        private String week;

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }

    public static class IndexBean {
        /**
         * index_cy : 冷
         * index_cy_xs : 建议着棉衣加羊毛衫等冬季服装。
         * index_uv : 中等
         * index_uv_xs : 外出除带防护帽和太阳镜外，请涂擦防晒霜
         * index_tr : 视情况而定
         * index_tr_xs : 天气不错，可以出去旅游一下的
         * index_co : 一般
         * index_co_xs : 基本无污染，天气情况较好，总体感觉比较舒适
         * index_cl : 不适宜
         * index_cl_xs : 今天的天气状况不适宜晨练
         * index_ls :
         * index_ls_xs : 基本无污染，天气情况较好，适宜晾晒
         * index_xc : 较适宜
         * index_xc_xs : 基本无污染，天气情况较好，适宜洗车
         */

        private String index_cy;
        private String index_cy_xs;
        private String index_uv;
        private String index_uv_xs;
        private String index_tr;
        private String index_tr_xs;
        private String index_co;
        private String index_co_xs;
        private String index_cl;
        private String index_cl_xs;
        private String index_ls;
        private String index_ls_xs;
        private String index_xc;
        private String index_xc_xs;

        public String getIndex_cy() {
            return index_cy;
        }

        public void setIndex_cy(String index_cy) {
            this.index_cy = index_cy;
        }

        public String getIndex_cy_xs() {
            return index_cy_xs;
        }

        public void setIndex_cy_xs(String index_cy_xs) {
            this.index_cy_xs = index_cy_xs;
        }

        public String getIndex_uv() {
            return index_uv;
        }

        public void setIndex_uv(String index_uv) {
            this.index_uv = index_uv;
        }

        public String getIndex_uv_xs() {
            return index_uv_xs;
        }

        public void setIndex_uv_xs(String index_uv_xs) {
            this.index_uv_xs = index_uv_xs;
        }

        public String getIndex_tr() {
            return index_tr;
        }

        public void setIndex_tr(String index_tr) {
            this.index_tr = index_tr;
        }

        public String getIndex_tr_xs() {
            return index_tr_xs;
        }

        public void setIndex_tr_xs(String index_tr_xs) {
            this.index_tr_xs = index_tr_xs;
        }

        public String getIndex_co() {
            return index_co;
        }

        public void setIndex_co(String index_co) {
            this.index_co = index_co;
        }

        public String getIndex_co_xs() {
            return index_co_xs;
        }

        public void setIndex_co_xs(String index_co_xs) {
            this.index_co_xs = index_co_xs;
        }

        public String getIndex_cl() {
            return index_cl;
        }

        public void setIndex_cl(String index_cl) {
            this.index_cl = index_cl;
        }

        public String getIndex_cl_xs() {
            return index_cl_xs;
        }

        public void setIndex_cl_xs(String index_cl_xs) {
            this.index_cl_xs = index_cl_xs;
        }

        public String getIndex_ls() {
            return index_ls;
        }

        public void setIndex_ls(String index_ls) {
            this.index_ls = index_ls;
        }

        public String getIndex_ls_xs() {
            return index_ls_xs;
        }

        public void setIndex_ls_xs(String index_ls_xs) {
            this.index_ls_xs = index_ls_xs;
        }

        public String getIndex_xc() {
            return index_xc;
        }

        public void setIndex_xc(String index_xc) {
            this.index_xc = index_xc;
        }

        public String getIndex_xc_xs() {
            return index_xc_xs;
        }

        public void setIndex_xc_xs(String index_xc_xs) {
            this.index_xc_xs = index_xc_xs;
        }
    }
}
