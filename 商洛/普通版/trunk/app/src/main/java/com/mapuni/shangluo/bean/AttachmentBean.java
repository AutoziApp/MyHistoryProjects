package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */
public class AttachmentBean {
    /**
     * total : 1
     * rows : [{"uuid":"4028f8fb5e26eb02015e26f679df000e","name":"osc_camera_20170720084447.jpg","filePath":"/201708/28/11/50/","extension":"jpg","size":"1.46MB","md5":"04d08798b0819cb5a2c36ce5809ac4c4","createTime":"2017-08-28 11:50:40","updateTime":"2017-08-28 11:50:40"}]
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
         * uuid : 4028f8fb5e26eb02015e26f679df000e
         * name : osc_camera_20170720084447.jpg
         * filePath : /201708/28/11/50/
         * extension : jpg
         * size : 1.46MB
         * md5 : 04d08798b0819cb5a2c36ce5809ac4c4
         * createTime : 2017-08-28 11:50:40
         * updateTime : 2017-08-28 11:50:40
         */

        private String uuid;
        private String name;
        private String filePath;
        private String extension;
        private String size;
        private String md5;
        private String createTime;
        private String updateTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
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
