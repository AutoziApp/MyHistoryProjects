package com.jy.environment.model;

import java.util.List;

/**
 * Created by yang on 2017/12/4.
 */

public class TongHuanBiBean {

    /**
     * flag : true
     * msg : 成功
     * detail : {"data":[{"CITYNAME":"郑州市","SO2":"-25","NO2":"-3.7","CO":"-15.79","O3":"18.87","PM10":"-2.17","PM25":"1.41","COMPOSITE":"-0.13"},{"CITYNAME":"开封市","SO2":"-29.63","NO2":"-10.53","CO":"-20.41","O3":"24.24","PM10":"-2.54","PM25":"-1.52","COMPOSITE":"-1.47"},{"CITYNAME":"洛阳市","SO2":"-35.9","NO2":"-13.33","CO":"-23.96","O3":"21.7","PM10":"7.32","PM25":"5.48","COMPOSITE":"4.34"},{"CITYNAME":"平顶山市","SO2":"-17.24","NO2":"-9.76","CO":"6.69","O3":"17.48","PM10":"0.84","PM25":"-1.45","COMPOSITE":"0.37"},{"CITYNAME":"安阳市","SO2":"-34","NO2":"-2.08","CO":"-9.38","O3":"41.11","PM10":"1.39","PM25":"12","COMPOSITE":"4.94"},{"CITYNAME":"鹤壁市","SO2":"-24.39","NO2":"-17.65","CO":"-6.85","O3":"33.33","PM10":"2.5","PM25":"-1.54","COMPOSITE":"1.42"},{"CITYNAME":"新乡市","SO2":"-27.5","NO2":"0","CO":"4.86","O3":"32","PM10":"-14.6","PM25":"-17.72","COMPOSITE":"-6.87"},{"CITYNAME":"焦作市","SO2":"-35","NO2":"-10.64","CO":"-13.32","O3":"30.93","PM10":"2.99","PM25":"1.3","COMPOSITE":"0.05"},{"CITYNAME":"濮阳市","SO2":"-28.57","NO2":"-14.63","CO":"10.42","O3":"11.21","PM10":"-10.69","PM25":"6.45","COMPOSITE":"-2.52"},{"CITYNAME":"许昌市","SO2":"-11.11","NO2":"-10.87","CO":"-22.7","O3":"22.45","PM10":"-9.4","PM25":"1.61","COMPOSITE":"-2.56"},{"CITYNAME":"漯河市","SO2":"-51.72","NO2":"-15.79","CO":"-15.42","O3":"3.77","PM10":"-11.29","PM25":"-15.49","COMPOSITE":"-13.71"},{"CITYNAME":"三门峡市","SO2":"-33.33","NO2":"5.41","CO":"-23.83","O3":"16.5","PM10":"-12.1","PM25":"-3.17","COMPOSITE":"-6.66"},{"CITYNAME":"南阳市","SO2":"-33.33","NO2":"-7.14","CO":"7.69","O3":"24.51","PM10":"-7.96","PM25":"-10.17","COMPOSITE":"-2.23"},{"CITYNAME":"商丘市","SO2":"-47.83","NO2":"0","CO":"-4.23","O3":"17.65","PM10":"-8.87","PM25":"-20.27","COMPOSITE":"-8.85"},{"CITYNAME":"信阳市","SO2":"-14.29","NO2":"-7.41","CO":"2.2","O3":"12.63","PM10":"-6.59","PM25":"-7.41","COMPOSITE":"-6.23"},{"CITYNAME":"周口市","SO2":"-15","NO2":"3.57","CO":"1.74","O3":"20","PM10":"-4.63","PM25":"-12.5","COMPOSITE":"-0.29"},{"CITYNAME":"驻马店市","SO2":"-46.88","NO2":"-10.81","CO":"14.46","O3":"15.53","PM10":"-12.93","PM25":"-14.29","COMPOSITE":"-9.51"},{"CITYNAME":"济源市","SO2":"-27.87","NO2":"-10","CO":"-25.1","O3":"23.6","PM10":"2.63","PM25":"0","COMPOSITE":"-3.76"}]}
     */

    private boolean flag;
    private String msg;
    private DetailBean detail;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * CITYNAME : 郑州市
             * SO2 : -25
             * NO2 : -3.7
             * CO : -15.79
             * O3 : 18.87
             * PM10 : -2.17
             * PM25 : 1.41
             * COMPOSITE : -0.13
             */

            private String CITYNAME;
            private String SO2;
            private String NO2;
            private String CO;
            private String O3;
            private String PM10;
            private String PM25;
            private String COMPOSITE;

            public String getCITYNAME() {
                return CITYNAME;
            }

            public void setCITYNAME(String CITYNAME) {
                this.CITYNAME = CITYNAME;
            }

            public String getSO2() {
                return SO2;
            }

            public void setSO2(String SO2) {
                this.SO2 = SO2;
            }

            public String getNO2() {
                return NO2;
            }

            public void setNO2(String NO2) {
                this.NO2 = NO2;
            }

            public String getCO() {
                return CO;
            }

            public void setCO(String CO) {
                this.CO = CO;
            }

            public String getO3() {
                return O3;
            }

            public void setO3(String O3) {
                this.O3 = O3;
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

            public String getCOMPOSITE() {
                return COMPOSITE;
            }

            public void setCOMPOSITE(String COMPOSITE) {
                this.COMPOSITE = COMPOSITE;
            }
        }
    }
}
