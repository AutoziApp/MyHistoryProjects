package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/18 9:52
 * @change
 * @chang time
 * @class describe
 */

public class TaskSumBean {
    /**
     * total : 8
     * rows : [{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"企事业单位和其他生产经营者","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"施工工地","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"各类道路","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"散乱污企业","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"公共水体","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"畜牧养殖单位","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"秸秆垃圾焚烧","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0},{"gridName":null,"handledRecordCount":0,"handlingRecordCount":0,"ratio":null,"recordTatolCount":0,"problemTypeName":"其他问题","problemTypeCount":0,"correspondingTime":null,"tatolUsersCount":0,"onlineUsersCount":0}]
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
         * gridName : null
         * handledRecordCount : 0
         * handlingRecordCount : 0
         * ratio : null
         * recordTatolCount : 0
         * problemTypeName : 企事业单位和其他生产经营者
         * problemTypeCount : 0
         * correspondingTime : null
         * tatolUsersCount : 0
         * onlineUsersCount : 0
         */

        private Object gridName;
        private int handledRecordCount;
        private int handlingRecordCount;
        private Object ratio;
        private int recordTatolCount;
        private String problemTypeName;
        private int problemTypeCount;
        private Object correspondingTime;
        private int tatolUsersCount;
        private int onlineUsersCount;

        public Object getGridName() {
            return gridName;
        }

        public void setGridName(Object gridName) {
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

        public Object getRatio() {
            return ratio;
        }

        public void setRatio(Object ratio) {
            this.ratio = ratio;
        }

        public int getRecordTatolCount() {
            return recordTatolCount;
        }

        public void setRecordTatolCount(int recordTatolCount) {
            this.recordTatolCount = recordTatolCount;
        }

        public String getProblemTypeName() {
            return problemTypeName;
        }

        public void setProblemTypeName(String problemTypeName) {
            this.problemTypeName = problemTypeName;
        }

        public int getProblemTypeCount() {
            return problemTypeCount;
        }

        public void setProblemTypeCount(int problemTypeCount) {
            this.problemTypeCount = problemTypeCount;
        }

        public Object getCorrespondingTime() {
            return correspondingTime;
        }

        public void setCorrespondingTime(Object correspondingTime) {
            this.correspondingTime = correspondingTime;
        }

        public int getTatolUsersCount() {
            return tatolUsersCount;
        }

        public void setTatolUsersCount(int tatolUsersCount) {
            this.tatolUsersCount = tatolUsersCount;
        }

        public int getOnlineUsersCount() {
            return onlineUsersCount;
        }

        public void setOnlineUsersCount(int onlineUsersCount) {
            this.onlineUsersCount = onlineUsersCount;
        }
    }
}
