package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by 15225 on 2017/8/25.
 */

public class XiafaBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : 4028b88e5de9ecf1015dea9f097c0024
         * code : 611002001005111
         * name : 窑头社区居委会
         * level : 4
         * gridType : 1
         * sort : 0
         * isEnable : 0
         * createTime : 2017-08-16 18:37:57
         * updateTime : 2017-08-17 13:29:44
         */

        private String uuid;
        private String code;
        private String name;
        private int level;
        private int gridType;
        private int sort;
        private int isEnable;
        private String createTime;
        private String updateTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getGridType() {
            return gridType;
        }

        public void setGridType(int gridType) {
            this.gridType = gridType;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(int isEnable) {
            this.isEnable = isEnable;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
