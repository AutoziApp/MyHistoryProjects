package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Administrator
 * @time 2017/11/30 9:58
 * @change
 * @chang time
 * @class describe
 */

public class RealTimeBean {
    /**
     * flag : true
     * msg : 成功
     * updatetime : 2017-09-25 13:00:00
     * detail : {"municipalities":[{"CITYNAME":"郑州市","PM10":"52","TOTALPM10":"68","PM25":"30","TOTALPM25":"33","AQI":"51","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"开封市","PM10":"68","TOTALPM10":"73","PM25":"55","TOTALPM25":"46","AQI":"75","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"洛阳市","PM10":"59","TOTALPM10":"50","PM25":"35","TOTALPM25":"28","AQI":"55","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"平顶山市","PM10":"33","TOTALPM10":"32","PM25":"28","TOTALPM25":"29","AQI":"41","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"安阳市","PM10":"143","TOTALPM10":"155","PM25":"68","TOTALPM25":"74","AQI":"97","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"鹤壁市","PM10":"136","TOTALPM10":"130","PM25":"78","TOTALPM25":"55","AQI":"104","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"新乡市","PM10":"99","TOTALPM10":"74","PM25":"56","TOTALPM25":"39","AQI":"77","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"焦作市","PM10":"125","TOTALPM10":"93","PM25":"78","TOTALPM25":"58","AQI":"104","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"濮阳市","PM10":"114","TOTALPM10":"120","PM25":"52","TOTALPM25":"67","AQI":"82","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"许昌市","PM10":"37","TOTALPM10":"29","PM25":"21","TOTALPM25":"22","AQI":"37","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"漯河市","PM10":"19","TOTALPM10":"18","PM25":"14","TOTALPM25":"15","AQI":"21","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"三门峡市","PM10":"41","TOTALPM10":"30","PM25":"35","TOTALPM25":"21","AQI":"50","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"南阳市","PM10":"15","TOTALPM10":"15","PM25":"10","TOTALPM25":"7","AQI":"20","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"商丘市","PM10":"41","TOTALPM10":"75","PM25":"19","TOTALPM25":"38","AQI":"41","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"信阳市","PM10":"17","TOTALPM10":"25","PM25":"10","TOTALPM25":"13","AQI":"17","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"周口市","PM10":"31","TOTALPM10":"22","PM25":"17","TOTALPM25":"16","AQI":"31","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"驻马店市","PM10":"13","TOTALPM10":"14","PM25":"11","TOTALPM25":"13","AQI":"16","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"济源市","PM10":"64","TOTALPM10":"64","PM25":"55","TOTALPM25":"43","AQI":"75","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"}],"citymean":{"CITYNAME":"市均值","PM10":"62","TOTALPM10":"60","PM25":"37","TOTALPM25":"34","AQI":"-","PRIMARYPOLLUTANT":"-","AIRLEVEL":"-"},"straightcounty":[{"CITYNAME":"巩义市","PM10":"42","TOTALPM10":"65","PM25":"33","TOTALPM25":"38","AQI":"48","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"兰考县","PM10":"92","TOTALPM10":"131","PM25":"38","TOTALPM25":"53","AQI":"71","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"汝州市","PM10":"67","TOTALPM10":"44","PM25":"45","TOTALPM25":"27","AQI":"63","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"滑县","PM10":"141","TOTALPM10":"130","PM25":"86","TOTALPM25":"74","AQI":"114","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"长垣县","PM10":"124","TOTALPM10":"109","PM25":"107","TOTALPM25":"74","AQI":"140","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"邓州市","PM10":"19","TOTALPM10":"15","PM25":"26","TOTALPM25":"14","AQI":"38","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"永城市","PM10":"26","TOTALPM10":"44","PM25":"21","TOTALPM25":"40","AQI":"31","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"固始县","PM10":"12","TOTALPM10":"18","PM25":"9","TOTALPM25":"11","AQI":"13","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"鹿邑县","PM10":"22","TOTALPM10":"46","PM25":"16","TOTALPM25":"30","AQI":"23","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"新蔡县","PM10":"16","TOTALPM10":"16","PM25":"11","TOTALPM25":"12","AQI":"16","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"}],"countymean":{"CITYNAME":"直管县","PM10":"56","TOTALPM10":"62","PM25":"39","TOTALPM25":"37","AQI":"-","PRIMARYPOLLUTANT":"-","AIRLEVEL":"-"}}
     */

    private String flag;
    private String msg;
    private String updatetime;
    private DetailBean detail;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        /**
         * municipalities : [{"CITYNAME":"郑州市","PM10":"52","TOTALPM10":"68","PM25":"30","TOTALPM25":"33","AQI":"51","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"开封市","PM10":"68","TOTALPM10":"73","PM25":"55","TOTALPM25":"46","AQI":"75","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"洛阳市","PM10":"59","TOTALPM10":"50","PM25":"35","TOTALPM25":"28","AQI":"55","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"平顶山市","PM10":"33","TOTALPM10":"32","PM25":"28","TOTALPM25":"29","AQI":"41","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"安阳市","PM10":"143","TOTALPM10":"155","PM25":"68","TOTALPM25":"74","AQI":"97","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"鹤壁市","PM10":"136","TOTALPM10":"130","PM25":"78","TOTALPM25":"55","AQI":"104","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"新乡市","PM10":"99","TOTALPM10":"74","PM25":"56","TOTALPM25":"39","AQI":"77","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"焦作市","PM10":"125","TOTALPM10":"93","PM25":"78","TOTALPM25":"58","AQI":"104","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"濮阳市","PM10":"114","TOTALPM10":"120","PM25":"52","TOTALPM25":"67","AQI":"82","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"许昌市","PM10":"37","TOTALPM10":"29","PM25":"21","TOTALPM25":"22","AQI":"37","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"漯河市","PM10":"19","TOTALPM10":"18","PM25":"14","TOTALPM25":"15","AQI":"21","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"三门峡市","PM10":"41","TOTALPM10":"30","PM25":"35","TOTALPM25":"21","AQI":"50","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"南阳市","PM10":"15","TOTALPM10":"15","PM25":"10","TOTALPM25":"7","AQI":"20","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"商丘市","PM10":"41","TOTALPM10":"75","PM25":"19","TOTALPM25":"38","AQI":"41","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"信阳市","PM10":"17","TOTALPM10":"25","PM25":"10","TOTALPM25":"13","AQI":"17","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"周口市","PM10":"31","TOTALPM10":"22","PM25":"17","TOTALPM25":"16","AQI":"31","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"驻马店市","PM10":"13","TOTALPM10":"14","PM25":"11","TOTALPM25":"13","AQI":"16","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"济源市","PM10":"64","TOTALPM10":"64","PM25":"55","TOTALPM25":"43","AQI":"75","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"}]
         * citymean : {"CITYNAME":"市均值","PM10":"62","TOTALPM10":"60","PM25":"37","TOTALPM25":"34","AQI":"-","PRIMARYPOLLUTANT":"-","AIRLEVEL":"-"}
         * straightcounty : [{"CITYNAME":"巩义市","PM10":"42","TOTALPM10":"65","PM25":"33","TOTALPM25":"38","AQI":"48","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"兰考县","PM10":"92","TOTALPM10":"131","PM25":"38","TOTALPM25":"53","AQI":"71","PRIMARYPOLLUTANT":"PM10","AIRLEVEL":"2"},{"CITYNAME":"汝州市","PM10":"67","TOTALPM10":"44","PM25":"45","TOTALPM25":"27","AQI":"63","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"2"},{"CITYNAME":"滑县","PM10":"141","TOTALPM10":"130","PM25":"86","TOTALPM25":"74","AQI":"114","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"长垣县","PM10":"124","TOTALPM10":"109","PM25":"107","TOTALPM25":"74","AQI":"140","PRIMARYPOLLUTANT":"PM2.5","AIRLEVEL":"3"},{"CITYNAME":"邓州市","PM10":"19","TOTALPM10":"15","PM25":"26","TOTALPM25":"14","AQI":"38","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"永城市","PM10":"26","TOTALPM10":"44","PM25":"21","TOTALPM25":"40","AQI":"31","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"固始县","PM10":"12","TOTALPM10":"18","PM25":"9","TOTALPM25":"11","AQI":"13","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"鹿邑县","PM10":"22","TOTALPM10":"46","PM25":"16","TOTALPM25":"30","AQI":"23","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"},{"CITYNAME":"新蔡县","PM10":"16","TOTALPM10":"16","PM25":"11","TOTALPM25":"12","AQI":"16","PRIMARYPOLLUTANT":"-","AIRLEVEL":"1"}]
         * countymean : {"CITYNAME":"直管县","PM10":"56","TOTALPM10":"62","PM25":"39","TOTALPM25":"37","AQI":"-","PRIMARYPOLLUTANT":"-","AIRLEVEL":"-"}
         */

        private CitymeanBean citymean;
        private CountymeanBean countymean;
        private List<MunicipalitiesBean> municipalities;
        private List<StraightcountyBean> straightcounty;

        public CitymeanBean getCitymean() {
            return citymean;
        }

        public void setCitymean(CitymeanBean citymean) {
            this.citymean = citymean;
        }

        public CountymeanBean getCountymean() {
            return countymean;
        }

        public void setCountymean(CountymeanBean countymean) {
            this.countymean = countymean;
        }

        public List<MunicipalitiesBean> getMunicipalities() {
            return municipalities;
        }

        public void setMunicipalities(List<MunicipalitiesBean> municipalities) {
            this.municipalities = municipalities;
        }

        public List<StraightcountyBean> getStraightcounty() {
            return straightcounty;
        }

        public void setStraightcounty(List<StraightcountyBean> straightcounty) {
            this.straightcounty = straightcounty;
        }

        public static class CitymeanBean {
            /**
             * CITYNAME : 市均值
             * PM10 : 62
             * TOTALPM10 : 60
             * PM25 : 37
             * TOTALPM25 : 34
             * AQI : -
             * PRIMARYPOLLUTANT : -
             * AIRLEVEL : -
             */

            private String CITYNAME;
            private String PM10;
            private String TOTALPM10;
            private String PM25;
            private String TOTALPM25;
            private String AQI;
            private String PRIMARYPOLLUTANT;
            private String AIRLEVEL;

            public String getCITYNAME() {
                return CITYNAME;
            }

            public void setCITYNAME(String CITYNAME) {
                this.CITYNAME = CITYNAME;
            }

            public String getPM10() {
                return PM10;
            }

            public void setPM10(String PM10) {
                this.PM10 = PM10;
            }

            public String getTOTALPM10() {
                return TOTALPM10;
            }

            public void setTOTALPM10(String TOTALPM10) {
                this.TOTALPM10 = TOTALPM10;
            }

            public String getPM25() {
                return PM25;
            }

            public void setPM25(String PM25) {
                this.PM25 = PM25;
            }

            public String getTOTALPM25() {
                return TOTALPM25;
            }

            public void setTOTALPM25(String TOTALPM25) {
                this.TOTALPM25 = TOTALPM25;
            }

            public String getAQI() {
                return AQI;
            }

            public void setAQI(String AQI) {
                this.AQI = AQI;
            }

            public String getPRIMARYPOLLUTANT() {
                return PRIMARYPOLLUTANT;
            }

            public void setPRIMARYPOLLUTANT(String PRIMARYPOLLUTANT) {
                this.PRIMARYPOLLUTANT = PRIMARYPOLLUTANT;
            }

            public String getAIRLEVEL() {
                return AIRLEVEL;
            }

            public void setAIRLEVEL(String AIRLEVEL) {
                this.AIRLEVEL = AIRLEVEL;
            }
        }

        public static class CountymeanBean {
            /**
             * CITYNAME : 直管县
             * PM10 : 56
             * TOTALPM10 : 62
             * PM25 : 39
             * TOTALPM25 : 37
             * AQI : -
             * PRIMARYPOLLUTANT : -
             * AIRLEVEL : -
             */

            private String CITYNAME;
            private String PM10;
            private String TOTALPM10;
            private String PM25;
            private String TOTALPM25;
            private String AQI;
            private String PRIMARYPOLLUTANT;
            private String AIRLEVEL;

            public String getCITYNAME() {
                return CITYNAME;
            }

            public void setCITYNAME(String CITYNAME) {
                this.CITYNAME = CITYNAME;
            }

            public String getPM10() {
                return PM10;
            }

            public void setPM10(String PM10) {
                this.PM10 = PM10;
            }

            public String getTOTALPM10() {
                return TOTALPM10;
            }

            public void setTOTALPM10(String TOTALPM10) {
                this.TOTALPM10 = TOTALPM10;
            }

            public String getPM25() {
                return PM25;
            }

            public void setPM25(String PM25) {
                this.PM25 = PM25;
            }

            public String getTOTALPM25() {
                return TOTALPM25;
            }

            public void setTOTALPM25(String TOTALPM25) {
                this.TOTALPM25 = TOTALPM25;
            }

            public String getAQI() {
                return AQI;
            }

            public void setAQI(String AQI) {
                this.AQI = AQI;
            }

            public String getPRIMARYPOLLUTANT() {
                return PRIMARYPOLLUTANT;
            }

            public void setPRIMARYPOLLUTANT(String PRIMARYPOLLUTANT) {
                this.PRIMARYPOLLUTANT = PRIMARYPOLLUTANT;
            }

            public String getAIRLEVEL() {
                return AIRLEVEL;
            }

            public void setAIRLEVEL(String AIRLEVEL) {
                this.AIRLEVEL = AIRLEVEL;
            }
        }

        public static class MunicipalitiesBean {
            /**
             * CITYNAME : 郑州市
             * PM10 : 52
             * TOTALPM10 : 68
             * PM25 : 30
             * TOTALPM25 : 33
             * AQI : 51
             * PRIMARYPOLLUTANT : PM10
             * AIRLEVEL : 2
             */

            private String CITYNAME;
            private String PM10;
            private String TOTALPM10;
            private String PM25;
            private String TOTALPM25;
            private String AQI;
            private String PRIMARYPOLLUTANT;
            private String AIRLEVEL;

            public String getCITYNAME() {
                return CITYNAME;
            }

            public void setCITYNAME(String CITYNAME) {
                this.CITYNAME = CITYNAME;
            }

            public String getPM10() {
                return PM10;
            }

            public void setPM10(String PM10) {
                this.PM10 = PM10;
            }

            public String getTOTALPM10() {
                return TOTALPM10;
            }

            public void setTOTALPM10(String TOTALPM10) {
                this.TOTALPM10 = TOTALPM10;
            }

            public String getPM25() {
                return PM25;
            }

            public void setPM25(String PM25) {
                this.PM25 = PM25;
            }

            public String getTOTALPM25() {
                return TOTALPM25;
            }

            public void setTOTALPM25(String TOTALPM25) {
                this.TOTALPM25 = TOTALPM25;
            }

            public String getAQI() {
                return AQI;
            }

            public void setAQI(String AQI) {
                this.AQI = AQI;
            }

            public String getPRIMARYPOLLUTANT() {
                return PRIMARYPOLLUTANT;
            }

            public void setPRIMARYPOLLUTANT(String PRIMARYPOLLUTANT) {
                this.PRIMARYPOLLUTANT = PRIMARYPOLLUTANT;
            }

            public String getAIRLEVEL() {
                return AIRLEVEL;
            }

            public void setAIRLEVEL(String AIRLEVEL) {
                this.AIRLEVEL = AIRLEVEL;
            }
        }

        public static class StraightcountyBean {
            /**
             * CITYNAME : 巩义市
             * PM10 : 42
             * TOTALPM10 : 65
             * PM25 : 33
             * TOTALPM25 : 38
             * AQI : 48
             * PRIMARYPOLLUTANT : -
             * AIRLEVEL : 1
             */

            private String CITYNAME;
            private String PM10;
            private String TOTALPM10;
            private String PM25;
            private String TOTALPM25;
            private String AQI;
            private String PRIMARYPOLLUTANT;
            private String AIRLEVEL;

            public String getCITYNAME() {
                return CITYNAME;
            }

            public void setCITYNAME(String CITYNAME) {
                this.CITYNAME = CITYNAME;
            }

            public String getPM10() {
                return PM10;
            }

            public void setPM10(String PM10) {
                this.PM10 = PM10;
            }

            public String getTOTALPM10() {
                return TOTALPM10;
            }

            public void setTOTALPM10(String TOTALPM10) {
                this.TOTALPM10 = TOTALPM10;
            }

            public String getPM25() {
                return PM25;
            }

            public void setPM25(String PM25) {
                this.PM25 = PM25;
            }

            public String getTOTALPM25() {
                return TOTALPM25;
            }

            public void setTOTALPM25(String TOTALPM25) {
                this.TOTALPM25 = TOTALPM25;
            }

            public String getAQI() {
                return AQI;
            }

            public void setAQI(String AQI) {
                this.AQI = AQI;
            }

            public String getPRIMARYPOLLUTANT() {
                return PRIMARYPOLLUTANT;
            }

            public void setPRIMARYPOLLUTANT(String PRIMARYPOLLUTANT) {
                this.PRIMARYPOLLUTANT = PRIMARYPOLLUTANT;
            }

            public String getAIRLEVEL() {
                return AIRLEVEL;
            }

            public void setAIRLEVEL(String AIRLEVEL) {
                this.AIRLEVEL = AIRLEVEL;
            }
        }
    }
}
