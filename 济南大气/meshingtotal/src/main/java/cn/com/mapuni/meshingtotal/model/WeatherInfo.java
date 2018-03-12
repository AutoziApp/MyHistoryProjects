package cn.com.mapuni.meshingtotal.model;

import java.util.List;

/**
 * Created by 15225 on 2017/7/13.
 */

public class WeatherInfo {

    /**
     * flag : true
     * nowWeather : [{"city":"����","weather":"��ת����","temp":"37��~28��","date":"07/13","weekday":"������","feelTemp":"30��","realTime":"���� 07:58����","level":"��","PM2Dot5Data":"53","pm25":"25","position_name":"����","level_near":"��","PM2Dot5Data_near":"53","pm25_near":"25","position_name_near":"����","WD":"�Ϸ�","WS":"2��","SD":"70%","Lunar":"ũ�����¶�ʮ"}]
     * weather : [{"temp":"37��~28��","weather":"��ת����","date":"07/13","week":"����"},{"temp":"35��~23��","weather":"��ת������","date":"07/14","week":"����"},{"temp":"31��~22��","weather":"������","date":"07/15","week":"����"},{"temp":"31��~26��","weather":"������","date":"07/16","week":"����"},{"temp":"33��~27��","weather":"����","date":"07/17","week":"��һ"},{"temp":"34��~28��","weather":"����ת��","date":"07/18","week":"�ܶ�"}]
     * index : [{"index_cy":"����","index_cy_xs":"���鴩�������̿�������ļ���װ��","index_uv":"��","index_uv_xs":"������ȡ������ñ��̫�����ȷ�����ʩ","index_tr":"���������","index_tr_xs":"�����������Գ�ȥ����һ�µ�","index_co":"һ��","index_co_xs":"��������Ⱦ����������Ϻã�����о��Ƚ�����","index_cl":"������","index_cl_xs":"���������״�������˳���","index_ls":"","index_ls_xs":"��������Ⱦ����������Ϻã�������ɹ","index_xc":"������","index_xc_xs":"��������Ⱦ����������Ϻã�����ϴ��"}]
     */

    private boolean flag;
    private List<NowWeatherBean> nowWeather;
    private List<WeatherBean> weather;
    private List<IndexBean> index;

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
         * city : ����
         * weather : ��ת����
         * temp : 37��~28��
         * date : 07/13
         * weekday : ������
         * feelTemp : 30��
         * realTime : ���� 07:58����
         * level : ��
         * PM2Dot5Data : 53
         * pm25 : 25
         * position_name : ����
         * level_near : ��
         * PM2Dot5Data_near : 53
         * pm25_near : 25
         * position_name_near : ����
         * WD : �Ϸ�
         * WS : 2��
         * SD : 70%
         * Lunar : ũ�����¶�ʮ
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
         * temp : 37��~28��
         * weather : ��ת����
         * date : 07/13
         * week : ����
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
         * index_cy : ����
         * index_cy_xs : ���鴩�������̿�������ļ���װ��
         * index_uv : ��
         * index_uv_xs : ������ȡ������ñ��̫�����ȷ�����ʩ
         * index_tr : ���������
         * index_tr_xs : �����������Գ�ȥ����һ�µ�
         * index_co : һ��
         * index_co_xs : ��������Ⱦ����������Ϻã�����о��Ƚ�����
         * index_cl : ������
         * index_cl_xs : ���������״�������˳���
         * index_ls :
         * index_ls_xs : ��������Ⱦ����������Ϻã�������ɹ
         * index_xc : ������
         * index_xc_xs : ��������Ⱦ����������Ϻã�����ϴ��
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
