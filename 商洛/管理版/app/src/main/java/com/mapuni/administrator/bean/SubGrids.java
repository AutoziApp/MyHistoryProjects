package com.mapuni.administrator.bean;

import java.util.List;

/**
 * Created by yang on 2017/12/19.
 */

public class SubGrids {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : 08d54aee57e54463a84698d2ab376c1d
         * code :
         * name : 两岔河村
         * level : 4
         * gridType : 1
         * sort : 0
         * isEnable : 0
         * createTime : 2017-11-28 10:38:14
         * updateTime : 2017-11-28 10:38:14
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
