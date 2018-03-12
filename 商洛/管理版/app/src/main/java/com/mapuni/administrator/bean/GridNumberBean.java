package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/21 11:00
 * @change
 * @chang time
 * @class describe
 */

public class GridNumberBean {
    /**
     * rows : [{"actuallySignCount":0,"delayCount":0,"gridName":"夜村镇","handledRecordCount":0,"handlingRecordCount":0,"onlineUsersCount":0,"problemTypeCount":0,"recordTatolCount":0,"reportCount":0,"shouldSignCount":0,"tatolUsersCount":0},{"actuallySignCount":0,"delayCount":0,"gridName":"刘湾街道办事处","handledRecordCount":0,"handlingRecordCount":0,"onlineUsersCount":0,"problemTypeCount":0,"recordTatolCount":0,"reportCount":0,"shouldSignCount":0,"tatolUsersCount":0},{"actuallySignCount":0,"delayCount":0,"gridName":"陈塬街道办事处","handledRecordCount":0,"handlingRecordCount":0,"onlineUsersCount":0,"problemTypeCount":0,"recordTatolCount":0,"reportCount":0,"shouldSignCount":0,"tatolUsersCount":0},{"actuallySignCount":0,"delayCount":0,"gridName":"大赵峪街道办事处","handledRecordCount":0,"handlingRecordCount":0,"onlineUsersCount":0,"problemTypeCount":0,"recordTatolCount":0,"reportCount":0,"shouldSignCount":0,"tatolUsersCount":0},{"actuallySignCount":0,"delayCount":0,"gridName":"城关街道办事处","handledRecordCount":9,"handlingRecordCount":10,"onlineUsersCount":0,"problemTypeCount":0,"recordTatolCount":19,"reportCount":0,"shouldSignCount":0,"tatolUsersCount":0}]
     * total : 5
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
         * actuallySignCount : 0
         * delayCount : 0
         * gridName : 夜村镇
         * handledRecordCount : 0
         * handlingRecordCount : 0
         * onlineUsersCount : 0
         * problemTypeCount : 0
         * recordTatolCount : 0
         * reportCount : 0
         * shouldSignCount : 0
         * tatolUsersCount : 0
         */

        private int actuallySignCount;
        private int delayCount;
        private String gridName;
        private int handledRecordCount;
        private int handlingRecordCount;
        private int onlineUsersCount;
        private int problemTypeCount;
        private int recordTatolCount;
        private int reportCount;
        private int shouldSignCount;
        private int tatolUsersCount;
        
        public int getActuallySignCount() {
            return actuallySignCount;
        }

        public void setActuallySignCount(int actuallySignCount) {
            this.actuallySignCount = actuallySignCount;
        }

        public int getDelayCount() {
            return delayCount;
        }

        public void setDelayCount(int delayCount) {
            this.delayCount = delayCount;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public int getHandledRecordCount() {
            return handledRecordCount;
        }

        public void setHandledRecordCount(int handledRecordCount) {
            this.handledRecordCount = handledRecordCount;
        }

        public int getHandlingRecordCount() {
            return handlingRecordCount;
        }

        public void setHandlingRecordCount(int handlingRecordCount) {
            this.handlingRecordCount = handlingRecordCount;
        }

        public int getOnlineUsersCount() {
            return onlineUsersCount;
        }

        public void setOnlineUsersCount(int onlineUsersCount) {
            this.onlineUsersCount = onlineUsersCount;
        }

        public int getProblemTypeCount() {
            return problemTypeCount;
        }

        public void setProblemTypeCount(int problemTypeCount) {
            this.problemTypeCount = problemTypeCount;
        }

        public int getRecordTatolCount() {
            return recordTatolCount;
        }

        public void setRecordTatolCount(int recordTatolCount) {
            this.recordTatolCount = recordTatolCount;
        }

        public int getReportCount() {
            return reportCount;
        }

        public void setReportCount(int reportCount) {
            this.reportCount = reportCount;
        }

        public int getShouldSignCount() {
            return shouldSignCount;
        }

        public void setShouldSignCount(int shouldSignCount) {
            this.shouldSignCount = shouldSignCount;
        }

        public int getTatolUsersCount() {
            return tatolUsersCount;
        }

        public void setTatolUsersCount(int tatolUsersCount) {
            this.tatolUsersCount = tatolUsersCount;
        }
    }
}
