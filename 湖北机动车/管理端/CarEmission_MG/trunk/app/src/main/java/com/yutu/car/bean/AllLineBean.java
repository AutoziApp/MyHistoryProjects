package com.yutu.car.bean;

import java.util.List;

/**
 * Created by yawei on 2017/4/24.
 */

public class AllLineBean {

    /**
     * flag : 1
     * data : [{"LINES":[{"PKID":"1404020061013","LINENAME":"柴油线"},{"PKID":"1404020061011","LINENAME":"汽油一线"},{"PKID":"1404020061014","LINENAME":"柴气混合线"}],"STATIONNAME":"荆门市万畅汽车服务有限公司"},{"LINES":[{"PKID":"4208220021014","LINENAME":"柴汽混合线"}],"STATIONNAME":"沙洋县平安机动车检测服务有限公司"},{"LINES":[{"PKID":"4201130031014","LINENAME":"柴汽混合线"},{"PKID":"4201130031011","LINENAME":"汽油线"},{"PKID":"4201130031013","LINENAME":"重柴线"}],"STATIONNAME":"荆门福元机动车检测有限公司"},{"LINES":[{"PKID":"4208210011013","LINENAME":"重柴线"},{"PKID":"4208210011014","LINENAME":"柴汽混合线"},{"PKID":"4208210011011","LINENAME":"汽油线"}],"STATIONNAME":"京山南强机动车综合性能检测有限公司"},{"LINES":[{"PKID":"4201130021034","LINENAME":"柴气混合线2"},{"PKID":"4201130021014","LINENAME":"柴汽混合线"},{"PKID":"4201130021012","LINENAME":"重柴线"}],"STATIONNAME":"荆门骏腾机动车检测有限公司"},{"LINES":[{"PKID":"4208220011014","LINENAME":"柴汽混合线"},{"PKID":"4208220011013","LINENAME":"重柴线"}],"STATIONNAME":"沙洋凯达机动车检测有限公司"},{"LINES":[{"PKID":"4208000011014","LINENAME":"环保检测线"}],"STATIONNAME":"荆门市中辰机动车检测有限公司"},{"LINES":[{"PKID":"4208810021011","LINENAME":"汽油检测一号线"},{"PKID":"4208810021021","LINENAME":"汽油检测二号线"},{"PKID":"4208810021013","LINENAME":"重柴大车检测线"},{"PKID":"4208810021014","LINENAME":"柴汽检测混合线"}],"STATIONNAME":"钟祥楚玉机动车检测有限公司"},{"LINES":[{"PKID":"4208220031014","LINENAME":"环保线"}],"STATIONNAME":"沙洋县城南机动车检测服务有限公司"},{"LINES":[{"PKID":"4201130061014","LINENAME":"柴气混合检测线"},{"PKID":"4201130061013","LINENAME":"重柴检测线"}],"STATIONNAME":"荆门万里机动车检测服务有限公司"},{"LINES":[{"PKID":"4201130081014","LINENAME":"柴汽混合线"},{"PKID":"4201130081011","LINENAME":"汽油检测线"}],"STATIONNAME":"荆门市五三顺畅机动车检测有限公司"},{"LINES":[{"PKID":"4208810031014","LINENAME":"柴汽混合一线"},{"PKID":"4208810031024","LINENAME":"柴汽混合二线"}],"STATIONNAME":"钟祥市恒旺机动车安全检测服务有限公司"},{"LINES":[{"PKID":"4201130071011","LINENAME":"汽油检测一号线"},{"PKID":"4201130071013","LINENAME":"重柴大车三号线"},{"PKID":"4201130071014","LINENAME":"柴汽混合二号线"}],"STATIONNAME":"耀东检测站"},{"LINES":[{"PKID":"4208810041013","LINENAME":"大车重柴线"},{"PKID":"4208810041014","LINENAME":"柴汽混合线"}],"STATIONNAME":"钟祥市炎鑫机动车综合性能检测有限公司"},{"LINES":[],"STATIONNAME":"测试1"},{"LINES":[{"PKID":"4208810051014","LINENAME":"柴汽混合线"},{"PKID":"4208810051013","LINENAME":"重柴线"}],"STATIONNAME":"荆门市三峰贸易有限公司"},{"LINES":[],"STATIONNAME":"dfgdfgdf"}]
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
         * LINES : [{"PKID":"1404020061013","LINENAME":"柴油线"},{"PKID":"1404020061011","LINENAME":"汽油一线"},{"PKID":"1404020061014","LINENAME":"柴气混合线"}]
         * STATIONNAME : 荆门市万畅汽车服务有限公司
         */

        private String STATIONNAME;
        private List<LINESBean> LINES;

        public String getSTATIONNAME() {
            return STATIONNAME;
        }

        public void setSTATIONNAME(String STATIONNAME) {
            this.STATIONNAME = STATIONNAME;
        }

        public List<LINESBean> getLINES() {
            return LINES;
        }

        public void setLINES(List<LINESBean> LINES) {
            this.LINES = LINES;
        }

        public static class LINESBean {
            /**
             * PKID : 1404020061013
             * LINENAME : 柴油线
             */

            private String PKID;
            private String LINENAME;

            public String getPKID() {
                return PKID;
            }

            public void setPKID(String PKID) {
                this.PKID = PKID;
            }

            public String getLINENAME() {
                return LINENAME;
            }

            public void setLINENAME(String LINENAME) {
                this.LINENAME = LINENAME;
            }
        }
    }
}
