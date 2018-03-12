package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by yang on 2018/1/15.
 */

public class QianDaoRecordBean {

    /**
     * total : 1
     * rows : [{"uuid":"4028f88a60f837be0160f858baf20013","usersUuid":"b7a7195e07384742a34c3683b5f208a2","usersName":"张存林","longitude":"113.56499","latitude":"34.824047","address":"河南省郑州市中原区莲花街65号靠近中国农业银行(郑州工业园分理处)","registerType":null,"status":null,"createTime":"2018-01-15 13:44:12"}]
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * uuid : 4028f88a60f837be0160f858baf20013
         * usersUuid : b7a7195e07384742a34c3683b5f208a2
         * usersName : 张存林
         * longitude : 113.56499
         * latitude : 34.824047
         * address : 河南省郑州市中原区莲花街65号靠近中国农业银行(郑州工业园分理处)
         * registerType : null
         * status : null
         * createTime : 2018-01-15 13:44:12
         */

        private String uuid;
        private String usersUuid;
        private String usersName;
        private String longitude;
        private String latitude;
        private String address;
        private Object registerType;
        private Object status;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUsersUuid() {
            return usersUuid;
        }

        public void setUsersUuid(String usersUuid) {
            this.usersUuid = usersUuid;
        }

        public String getUsersName() {
            return usersName;
        }

        public void setUsersName(String usersName) {
            this.usersName = usersName;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getRegisterType() {
            return registerType;
        }

        public void setRegisterType(Object registerType) {
            this.registerType = registerType;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
