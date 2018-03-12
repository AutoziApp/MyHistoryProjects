package com.mapuni.shangluo.bean;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/8/30 11:39
 * @change
 * @chang time
 * @class describe
 */

public class DelayTaskBean {
    /**
     * status : 0
     * msg : success
     * data : {"uuid":"4028f8fb5e30acff015e30c524560020","supervisionObjectName":"360","supervisionObjectTypeName":"餐饮单位","gridName":"窑头社区居委会","userRealname":"李杨柳","number":"2017083000001","telephone":"185696956256","patrolProblemType":"各类道路","description":"鄙视的","longitude":"113.648268","latitude":"34.829374","applyStartTime":"2017-08-31 00:00:00","applyEndTime":"2017-09-08 00:00:00","applyReason":"测试","createTime":"2017-08-30 09:32:59"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : 4028f8fb5e30acff015e30c524560020
         * supervisionObjectName : 360
         * supervisionObjectTypeName : 餐饮单位
         * gridName : 窑头社区居委会
         * userRealname : 李杨柳
         * number : 2017083000001
         * telephone : 185696956256
         * patrolProblemType : 各类道路
         * description : 鄙视的
         * longitude : 113.648268
         * latitude : 34.829374
         * applyStartTime : 2017-08-31 00:00:00
         * applyEndTime : 2017-09-08 00:00:00
         * applyReason : 测试
         * createTime : 2017-08-30 09:32:59
         */

        private String uuid;
        private String supervisionObjectName;
        private String supervisionObjectTypeName;
        private String gridName;
        private String userRealname;
        private String number;
        private String telephone;
        private String patrolProblemType;
        private String description;
        private String longitude;
        private String latitude;
        private String applyStartTime;
        private String applyEndTime;
        private String applyReason;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getSupervisionObjectName() {
            return supervisionObjectName;
        }

        public void setSupervisionObjectName(String supervisionObjectName) {
            this.supervisionObjectName = supervisionObjectName;
        }

        public String getSupervisionObjectTypeName() {
            return supervisionObjectTypeName;
        }

        public void setSupervisionObjectTypeName(String supervisionObjectTypeName) {
            this.supervisionObjectTypeName = supervisionObjectTypeName;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getPatrolProblemType() {
            return patrolProblemType;
        }

        public void setPatrolProblemType(String patrolProblemType) {
            this.patrolProblemType = patrolProblemType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getApplyStartTime() {
            return applyStartTime;
        }

        public void setApplyStartTime(String applyStartTime) {
            this.applyStartTime = applyStartTime;
        }

        public String getApplyEndTime() {
            return applyEndTime;
        }

        public void setApplyEndTime(String applyEndTime) {
            this.applyEndTime = applyEndTime;
        }

        public String getApplyReason() {
            return applyReason;
        }

        public void setApplyReason(String applyReason) {
            this.applyReason = applyReason;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
