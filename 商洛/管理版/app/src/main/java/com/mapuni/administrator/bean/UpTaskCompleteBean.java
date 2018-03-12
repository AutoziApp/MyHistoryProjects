package com.mapuni.administrator.bean;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/8/31 16:40
 * @change
 * @chang time
 * @class describe
 */

public class UpTaskCompleteBean {


    /**
     * total : 0
     * rows : {"uuid":"4028f8fb5e374180015e374ff89c001b","supervisionObjectName":"玖龙橡胶厂","supervisionObjectTypeName":"工业污染","gridName":"窑头社区居委会","userRealname":"李杨柳","number":"2017083100004","telephone":"185696956256","patrolProblemType":"企事业单位和其他生产经营者","description":"红中","longitude":"113.564944","latitude":"34.824083","createTime":"2017-08-31 16:02:21"}
     */

    private int total;
    private RowsBean rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RowsBean getRows() {
        return rows;
    }

    public void setRows(RowsBean rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * uuid : 4028f8fb5e374180015e374ff89c001b
         * supervisionObjectName : 玖龙橡胶厂
         * supervisionObjectTypeName : 工业污染
         * gridName : 窑头社区居委会
         * userRealname : 李杨柳
         * number : 2017083100004
         * telephone : 185696956256
         * patrolProblemType : 企事业单位和其他生产经营者
         * description : 红中
         * longitude : 113.564944
         * latitude : 34.824083
         * createTime : 2017-08-31 16:02:21
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
        private String createTime;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
