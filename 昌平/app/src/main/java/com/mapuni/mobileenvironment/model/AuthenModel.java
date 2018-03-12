package com.mapuni.mobileenvironment.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yawei on 2017/2/27.
 */

public class AuthenModel implements Serializable {

    /**
     * isSuccess : true
     * result : 瀹℃牳涓�
     * info : [{"ID":8,"DEVICEID":"862872021332381","PHONEBRAND":"涓夋槦note2","PHONENUM":"13301381303","USERNAME":"濮氱","ANDROIDVERSION":"1.3","OTHER":"","ISPAST":"瀹℃牳涓�"}]
     */

    private boolean isSuccess;
    private String result;
    private List<InfoBean> info;

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }

    private String linkInfo;

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable{
        /**
         * ID : 8
         * DEVICEID : 862872021332381
         * PHONEBRAND : 涓夋槦note2
         * PHONENUM : 13301381303
         * USERNAME : 濮氱
         * ANDROIDVERSION : 1.3
         * OTHER :
         * ISPAST : 瀹℃牳涓�
         */

        private int ID;
        private String DEVICEID;
        private String PHONEBRAND;
        private String PHONENUM;
        private String USERNAME;
        private String ANDROIDVERSION;
        private String OTHER;
        private String ISPAST;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getDEVICEID() {
            return DEVICEID;
        }

        public void setDEVICEID(String DEVICEID) {
            this.DEVICEID = DEVICEID;
        }

        public String getPHONEBRAND() {
            return PHONEBRAND;
        }

        public void setPHONEBRAND(String PHONEBRAND) {
            this.PHONEBRAND = PHONEBRAND;
        }

        public String getPHONENUM() {
            return PHONENUM;
        }

        public void setPHONENUM(String PHONENUM) {
            this.PHONENUM = PHONENUM;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getANDROIDVERSION() {
            return ANDROIDVERSION;
        }

        public void setANDROIDVERSION(String ANDROIDVERSION) {
            this.ANDROIDVERSION = ANDROIDVERSION;
        }

        public String getOTHER() {
            return OTHER;
        }

        public void setOTHER(String OTHER) {
            this.OTHER = OTHER;
        }

        public String getISPAST() {
            return ISPAST;
        }

        public void setISPAST(String ISPAST) {
            this.ISPAST = ISPAST;
        }
    }
}
