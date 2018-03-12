package com.mapuni.administrator.bean;

import java.util.List;

/**
 * Created by 15225 on 2017/9/22.
 */

public class TaskNuberOfGridNumberBean {


    /**
     * total : 5
     * rows : [{"userRealname":"吴刚娃","count":0},{"userRealname":"杨新朝","count":0},{"userRealname":"李中升","count":0},{"userRealname":"曾元会","count":0},{"userRealname":"李胜山","count":0}]
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
         * userRealname : 吴刚娃
         * count : 0
         */

        private String userRealname;
        private int count;

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
