package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */
public class MessageListBean {

    /**
     * total : 1
     * rows : [{"uuid":"4028f8fb5e363459015e364ad52e0010","senderName":"admin","title":"555","content":"554551511","reciveObjectUuid ":"4028b88e5e03f3a4015e03fada1c0000","reciveObjectName":"0001","reciveObjectType":0,"status":0,"createTime":"2017-08-31 11:17:07","updateTime":null}]
     */

    private int total;
    /**
     * uuid : 4028f8fb5e363459015e364ad52e0010
     * senderName : admin
     * title : 555
     * content : 554551511
     * reciveObjectUuid  : 4028b88e5e03f3a4015e03fada1c0000
     * reciveObjectName : 0001
     * reciveObjectType : 0
     * status : 0
     * createTime : 2017-08-31 11:17:07
     * updateTime : null
     */

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
        private String uuid;
        private String senderName;
        private String title;
        private String content;
        private String reciveObjectUuid;
        private String reciveObjectName;
        private int reciveObjectType;
        private int status;
        private String createTime;
        private Object updateTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReciveObjectUuid() {
            return reciveObjectUuid;
        }

        public void setReciveObjectUuid(String reciveObjectUuid) {
            this.reciveObjectUuid = reciveObjectUuid;
        }

        public String getReciveObjectName() {
            return reciveObjectName;
        }

        public void setReciveObjectName(String reciveObjectName) {
            this.reciveObjectName = reciveObjectName;
        }

        public int getReciveObjectType() {
            return reciveObjectType;
        }

        public void setReciveObjectType(int reciveObjectType) {
            this.reciveObjectType = reciveObjectType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }
    }
}
