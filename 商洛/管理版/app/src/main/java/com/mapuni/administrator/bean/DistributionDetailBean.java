package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Administrator
 * @time 2017/10/19 16:15
 * @change
 * @chang time
 * @class describe
 */

public class DistributionDetailBean {
    /**
     * rows : [{"createTime":"2017-10-19 15:22:19","currentUserUuid":"05611979a2684f8299f4159bd8add02a","delayApplyTaskType":0,"endTime":"2017-10-20 23:59:59","finishTime":"2017-10-20 23:59:59","gridName":"商洛市","gridUuid":"ed4bcf033ed742718044541021fcb569","handlerGridType":1,"handlerUser":"杨晓芸","handlerUserType":0,"handlerUserUuid":"6dd7fcba78f44d7bad4104406f1727fd","handlerUuid":"6dd7fcba78f44d7bad4104406f1727fd","judgeRemind":2,"judgeReturn":0,"nextHandlerName":"柞水县","operationType":2,"operationTypeName":"下发","opinion":"下发任务","remindFlag":0,"remindTime":"2017-10-19 00:00:00","startTime":"2017-10-19 00:00:00","taskType":1,"taskTypeName":"下发","taskUuid":"4028f8105f3211ec015f33821fbd003d","uuid":"4028f8105f3211ec015f3382ecc90040"}]
     * total : 1
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
         * createTime : 2017-10-19 15:22:19
         * currentUserUuid : 05611979a2684f8299f4159bd8add02a
         * delayApplyTaskType : 0
         * endTime : 2017-10-20 23:59:59
         * finishTime : 2017-10-20 23:59:59
         * gridName : 商洛市
         * gridUuid : ed4bcf033ed742718044541021fcb569
         * handlerGridType : 1
         * handlerUser : 杨晓芸
         * handlerUserType : 0
         * handlerUserUuid : 6dd7fcba78f44d7bad4104406f1727fd
         * handlerUuid : 6dd7fcba78f44d7bad4104406f1727fd
         * judgeRemind : 2
         * judgeReturn : 0
         * nextHandlerName : 柞水县
         * operationType : 2
         * operationTypeName : 下发
         * opinion : 下发任务
         * remindFlag : 0
         * remindTime : 2017-10-19 00:00:00
         * startTime : 2017-10-19 00:00:00
         * taskType : 1
         * taskTypeName : 下发
         * taskUuid : 4028f8105f3211ec015f33821fbd003d
         * uuid : 4028f8105f3211ec015f3382ecc90040
         */

        private String createTime;
        private String currentUserUuid;
        private int delayApplyTaskType;
        private String endTime;
        private String finishTime;
        private String gridName;
        private String gridUuid;
        private int handlerGridType;
        private String handlerUser;
        private int handlerUserType;
        private String handlerUserUuid;
        private String handlerUuid;
        private int judgeRemind;
        private int judgeReturn;
        private String nextHandlerName;
        private int operationType;
        private String operationTypeName;
        private String opinion;
        private int remindFlag;
        private String remindTime;
        private String startTime;
        private int taskType;
        private String taskTypeName;
        private String taskUuid;
        private String uuid;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCurrentUserUuid() {
            return currentUserUuid;
        }

        public void setCurrentUserUuid(String currentUserUuid) {
            this.currentUserUuid = currentUserUuid;
        }

        public int getDelayApplyTaskType() {
            return delayApplyTaskType;
        }

        public void setDelayApplyTaskType(int delayApplyTaskType) {
            this.delayApplyTaskType = delayApplyTaskType;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getGridUuid() {
            return gridUuid;
        }

        public void setGridUuid(String gridUuid) {
            this.gridUuid = gridUuid;
        }

        public int getHandlerGridType() {
            return handlerGridType;
        }

        public void setHandlerGridType(int handlerGridType) {
            this.handlerGridType = handlerGridType;
        }

        public String getHandlerUser() {
            return handlerUser;
        }

        public void setHandlerUser(String handlerUser) {
            this.handlerUser = handlerUser;
        }

        public int getHandlerUserType() {
            return handlerUserType;
        }

        public void setHandlerUserType(int handlerUserType) {
            this.handlerUserType = handlerUserType;
        }

        public String getHandlerUserUuid() {
            return handlerUserUuid;
        }

        public void setHandlerUserUuid(String handlerUserUuid) {
            this.handlerUserUuid = handlerUserUuid;
        }

        public String getHandlerUuid() {
            return handlerUuid;
        }

        public void setHandlerUuid(String handlerUuid) {
            this.handlerUuid = handlerUuid;
        }

        public int getJudgeRemind() {
            return judgeRemind;
        }

        public void setJudgeRemind(int judgeRemind) {
            this.judgeRemind = judgeRemind;
        }

        public int getJudgeReturn() {
            return judgeReturn;
        }

        public void setJudgeReturn(int judgeReturn) {
            this.judgeReturn = judgeReturn;
        }

        public String getNextHandlerName() {
            return nextHandlerName;
        }

        public void setNextHandlerName(String nextHandlerName) {
            this.nextHandlerName = nextHandlerName;
        }

        public int getOperationType() {
            return operationType;
        }

        public void setOperationType(int operationType) {
            this.operationType = operationType;
        }

        public String getOperationTypeName() {
            return operationTypeName;
        }

        public void setOperationTypeName(String operationTypeName) {
            this.operationTypeName = operationTypeName;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public int getRemindFlag() {
            return remindFlag;
        }

        public void setRemindFlag(int remindFlag) {
            this.remindFlag = remindFlag;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }

        public String getTaskTypeName() {
            return taskTypeName;
        }

        public void setTaskTypeName(String taskTypeName) {
            this.taskTypeName = taskTypeName;
        }

        public String getTaskUuid() {
            return taskUuid;
        }

        public void setTaskUuid(String taskUuid) {
            this.taskUuid = taskUuid;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
