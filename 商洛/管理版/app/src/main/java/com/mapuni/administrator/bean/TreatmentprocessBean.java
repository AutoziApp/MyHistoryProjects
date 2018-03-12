package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.bean
 * @class 查询记录中 上报 下发 延时 处理流程通用Bean
 * @anthor Tianfy
 * @time 2017/9/1 13:27
 * @change
 * @chang time
 * @class describe
 */

public class TreatmentprocessBean {

    /**
     * total : 1
     * rows : [{"uuid":"4028f8fb5e35d059015e360b733f0036","taskUuid":"4028f8fb5e30acff015e30b582c6000a","taskType":1,"taskTypeName":"下发","taskName":null,"startTime":"2017-08-30 00:00:00","endTime":"2017-09-04 00:00:00","createTime":"2017-08-31 10:07:53","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":3,"operationTypeName":"转办","opinion":"aaaaaaaaa","remindFlag":0,"currentUserUuid":"4028b88e5e045413015e0478e577000d","judgeRemind":0,"nextHandlerName":"商州区卫生局","remindTime":"2017-08-30 00:00:00","finishTime":"2017-09-04 00:00:00","remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":null,"longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null}]
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
         * uuid : 4028f8fb5e35d059015e360b733f0036
         * taskUuid : 4028f8fb5e30acff015e30b582c6000a
         * taskType : 1
         * taskTypeName : 下发
         * taskName : null
         * startTime : 2017-08-30 00:00:00
         * endTime : 2017-09-04 00:00:00
         * createTime : 2017-08-31 10:07:53
         * gridUuid : 4028b88e5de9ecf1015dea5e831c0018
         * gridName : 城关街道办事处
         * handlerUserUuid : 4028b88e5e045413015e0478e577000d
         * handlerUser : 张飞雄
         * handlerUserType : 0
         * handlerGridType : 1
         * operationType : 3
         * operationTypeName : 转办
         * opinion : aaaaaaaaa
         * remindFlag : 0
         * currentUserUuid : 4028b88e5e045413015e0478e577000d
         * judgeRemind : 0
         * nextHandlerName : 商州区卫生局
         * remindTime : 2017-08-30 00:00:00
         * finishTime : 2017-09-04 00:00:00
         * remindOpinion : null
         * delayApplyTaskType : 0
         * taskStatusName : null
         * longitude : null
         * latitude : null
         * grid_Name : null
         * gridLevel : null
         * userFullName : null
         * address : null
         * description : null
         */

        private String uuid;
        private String taskUuid;
        private int taskType;
        private String taskTypeName;
        private Object taskName;
        private String startTime;
        private String endTime;
        private String createTime;
        private String gridUuid;
        private String gridName;
        private String handlerUserUuid;
        private String handlerUser;
        private int handlerUserType;
        private int handlerGridType;
        private int operationType;
        private String operationTypeName;
        private String opinion;
        private int remindFlag;
        private String currentUserUuid;
        private int judgeRemind;
        private String nextHandlerName;
        private String remindTime;
        private String finishTime;
        private Object remindOpinion;
        private int delayApplyTaskType;
        private Object taskStatusName;
        private Object longitude;
        private Object latitude;
        private Object grid_Name;
        private Object gridLevel;
        private Object userFullName;
        private Object address;
        private Object description;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTaskUuid() {
            return taskUuid;
        }

        public void setTaskUuid(String taskUuid) {
            this.taskUuid = taskUuid;
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

        public Object getTaskName() {
            return taskName;
        }

        public void setTaskName(Object taskName) {
            this.taskName = taskName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getGridUuid() {
            return gridUuid;
        }

        public void setGridUuid(String gridUuid) {
            this.gridUuid = gridUuid;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getHandlerUserUuid() {
            return handlerUserUuid;
        }

        public void setHandlerUserUuid(String handlerUserUuid) {
            this.handlerUserUuid = handlerUserUuid;
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

        public int getHandlerGridType() {
            return handlerGridType;
        }

        public void setHandlerGridType(int handlerGridType) {
            this.handlerGridType = handlerGridType;
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

        public String getCurrentUserUuid() {
            return currentUserUuid;
        }

        public void setCurrentUserUuid(String currentUserUuid) {
            this.currentUserUuid = currentUserUuid;
        }

        public int getJudgeRemind() {
            return judgeRemind;
        }

        public void setJudgeRemind(int judgeRemind) {
            this.judgeRemind = judgeRemind;
        }

        public String getNextHandlerName() {
            return nextHandlerName;
        }

        public void setNextHandlerName(String nextHandlerName) {
            this.nextHandlerName = nextHandlerName;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public Object getRemindOpinion() {
            return remindOpinion;
        }

        public void setRemindOpinion(Object remindOpinion) {
            this.remindOpinion = remindOpinion;
        }

        public int getDelayApplyTaskType() {
            return delayApplyTaskType;
        }

        public void setDelayApplyTaskType(int delayApplyTaskType) {
            this.delayApplyTaskType = delayApplyTaskType;
        }

        public Object getTaskStatusName() {
            return taskStatusName;
        }

        public void setTaskStatusName(Object taskStatusName) {
            this.taskStatusName = taskStatusName;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getGrid_Name() {
            return grid_Name;
        }

        public void setGrid_Name(Object grid_Name) {
            this.grid_Name = grid_Name;
        }

        public Object getGridLevel() {
            return gridLevel;
        }

        public void setGridLevel(Object gridLevel) {
            this.gridLevel = gridLevel;
        }

        public Object getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(Object userFullName) {
            this.userFullName = userFullName;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }
    }
}
