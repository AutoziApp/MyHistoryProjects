package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by yang on 2018/1/19.
 */

public class KnowledgeListBean {


    /**
     * total : 1
     * rows : [{"uuid":"4028f8ef610c1a0d01610c48b7ed000b","title":"111","content":"1111","createTime":"2018-01-19 10:39:07"}]
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
         * uuid : 4028f8ef610c1a0d01610c48b7ed000b
         * title : 111
         * content : 1111
         * createTime : 2018-01-19 10:39:07
         */

        private String uuid;
        private String title;
        private String content;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
