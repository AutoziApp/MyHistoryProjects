package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/11/21 13:34
 * @change
 * @chang time
 * @class describe
 */

public class FenpeiBean {
    private List<RowsBean> rows;

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * uuid : 1a71b2cfd5a74aa6a3baad680fd380c6
         * loginName : shenkang
         * roleNames : null
         * userRealname : 沈康
         * gridName : 商洛市
         * sex : 2
         * telephone : 18709140203
         * email : 
         * address : 
         * isEnable : 0
         * sort : 0
         * createTime : 2017-09-26 08:57:44
         */

        private String uuid;
        private String loginName;
        private Object roleNames;
        private String userRealname;
        private String gridName;
        private int sex;
        private String telephone;
        private String email;
        private String address;
        private int isEnable;
        private int sort;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public Object getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(Object roleNames) {
            this.roleNames = roleNames;
        }

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(int isEnable) {
            this.isEnable = isEnable;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
