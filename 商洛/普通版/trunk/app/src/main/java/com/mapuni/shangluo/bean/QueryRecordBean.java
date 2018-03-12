package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/8/31 11:02
 * @change
 * @chang time
 * @class describe
 */

public class QueryRecordBean {

    /**
     * total : 10
     * rows : [{"uuid":"4028f8fb5e35d059015e360e76c00042","taskUuid":"4028f8fb5e35d059015e360c6eca0039","taskType":2,"taskTypeName":"延时申请","taskName":"任务延时申请","startTime":"2017-09-01 00:00:00","endTime":"2017-09-08 00:00:00","createTime":"2017-08-31 10:11:11","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":11,"operationTypeName":"同意","opinion":"同意 6","remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":null,"remindTime":"2017-09-01 00:00:00","finishTime":"2017-09-08 00:00:00","remindOpinion":null,"delayApplyTaskType":1,"taskStatusName":"结束","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e35d059015e360b733f0036","taskUuid":"4028f8fb5e30acff015e30b582c6000a","taskType":1,"taskTypeName":"下发","taskName":"8.30-3","startTime":"2017-08-30 00:00:00","endTime":"2017-09-04 00:00:00","createTime":"2017-08-31 10:07:53","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":3,"operationTypeName":"转办","opinion":"aaaaaaaaa","remindFlag":0,"currentUserUuid":null,"judgeRemind":0,"nextHandlerName":"商州区卫生局","remindTime":"2017-08-30 00:00:00","finishTime":"2017-09-04 00:00:00","remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e35d059015e360527e00032","taskUuid":"4028f8fb5e30acff015e30b55cbe0009","taskType":1,"taskTypeName":"下发","taskName":"8.30-2","startTime":null,"endTime":null,"createTime":"2017-08-31 10:01:01","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":5,"operationTypeName":"办结","opinion":"555","remindFlag":null,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":"李熙平","remindTime":null,"finishTime":null,"remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e35d059015e35ef58a40020","taskUuid":"4028f8fb5e32561b015e325fc9d6000a","taskType":0,"taskTypeName":"上报","taskName":"ddff","startTime":null,"endTime":null,"createTime":"2017-08-31 09:37:11","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":5,"operationTypeName":"办结","opinion":"测试已办结任务","remindFlag":null,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":null,"remindTime":null,"finishTime":null,"remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"结束","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e35d059015e35d588520005","taskUuid":"4028f8fb5e30acff015e30d0b375003e","taskType":1,"taskTypeName":"下发","taskName":"8.30-4","startTime":"2017-08-30 00:00:00","endTime":"2017-08-31 00:00:00","createTime":"2017-08-31 09:09:00","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":2,"operationTypeName":"下发","opinion":null,"remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":"商州区卫生局","remindTime":"2017-08-30 00:00:00","finishTime":"2017-08-31 00:00:00","remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e32acfc015e32e3f21b000b","taskUuid":"4028f8fb5e30acff015e30b736dc0011","taskType":2,"taskTypeName":"延时申请","taskName":"任务延时申请","startTime":"2017-08-31 00:00:00","endTime":"2017-09-02 00:00:00","createTime":"2017-08-30 19:25:53","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":11,"operationTypeName":"同意","opinion":"同意 null","remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":null,"remindTime":"2017-08-31 00:00:00","finishTime":"2017-09-02 00:00:00","remindOpinion":null,"delayApplyTaskType":1,"taskStatusName":"结束","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e32561b015e3280009f0016","taskUuid":"4028f8fb5e30acff015e30cc471f0030","taskType":2,"taskTypeName":"延时申请","taskName":"任务延时申请","startTime":"2017-08-31 00:00:00","endTime":"2017-09-08 00:00:00","createTime":"2017-08-30 17:36:43","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":5,"operationTypeName":"办结","opinion":"测试","remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":null,"remindTime":"2017-08-31 00:00:00","finishTime":"2017-09-08 00:00:00","remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"结束","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e313b7f015e31b1d63c0016","taskUuid":"4028f8fb5e313b7f015e31b1d61d0015","taskType":2,"taskTypeName":"延时申请","taskName":"任务延时申请","startTime":"2017-08-30 00:00:00","endTime":"2017-08-31 00:00:00","createTime":"2017-08-30 13:51:31","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":1,"handlerGridType":1,"operationType":10,"operationTypeName":"延时申请","opinion":"试试","remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":"李熙平","remindTime":"2017-08-30 00:00:00","finishTime":"2017-08-31 00:00:00","remindOpinion":null,"delayApplyTaskType":1,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e30acff015e30c619570025","taskUuid":"4028f8fb5e30acff015e30c524560020","taskType":0,"taskTypeName":"上报","taskName":"鄙视的","startTime":null,"endTime":null,"createTime":"2017-08-30 09:34:02","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":1,"handlerGridType":1,"operationType":3,"operationTypeName":"转办","opinion":"转办测试","remindFlag":0,"currentUserUuid":null,"judgeRemind":0,"nextHandlerName":"商州区卫生局","remindTime":null,"finishTime":null,"remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null},{"uuid":"4028f8fb5e30acff015e30b685c8000e","taskUuid":"4028f8fb5e30acff015e30b522560008","taskType":1,"taskTypeName":"下发","taskName":"8.30-1","startTime":"2017-08-30 00:00:00","endTime":"2017-08-31 00:00:00","createTime":"2017-08-30 09:17:01","gridUuid":"4028b88e5de9ecf1015dea5e831c0018","gridName":"城关街道办事处","handlerUserUuid":"4028b88e5e045413015e0478e577000d","handlerUser":"张飞雄","handlerUserType":0,"handlerGridType":1,"operationType":2,"operationTypeName":"下发","opinion":"这个是下发的任务","remindFlag":0,"currentUserUuid":null,"judgeRemind":2,"nextHandlerName":"窑头社区居委会","remindTime":"2017-08-30 00:00:00","finishTime":"2017-08-31 00:00:00","remindOpinion":null,"delayApplyTaskType":0,"taskStatusName":"处理中","longitude":null,"latitude":null,"grid_Name":null,"gridLevel":null,"userFullName":null,"address":null,"description":null}]
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
         * uuid : 4028f8fb5e35d059015e360e76c00042
         * taskUuid : 4028f8fb5e35d059015e360c6eca0039
         * taskType : 2
         * taskTypeName : 延时申请
         * taskName : 任务延时申请
         * startTime : 2017-09-01 00:00:00
         * endTime : 2017-09-08 00:00:00
         * createTime : 2017-08-31 10:11:11
         * gridUuid : 4028b88e5de9ecf1015dea5e831c0018
         * gridName : 城关街道办事处
         * handlerUserUuid : 4028b88e5e045413015e0478e577000d
         * handlerUser : 张飞雄
         * handlerUserType : 0
         * handlerGridType : 1
         * operationType : 11
         * operationTypeName : 同意
         * opinion : 同意 6
         * remindFlag : 0
         * currentUserUuid : null
         * judgeRemind : 2
         * nextHandlerName : null
         * remindTime : 2017-09-01 00:00:00
         * finishTime : 2017-09-08 00:00:00
         * remindOpinion : null
         * delayApplyTaskType : 1
         * taskStatusName : 结束
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
        private String taskName;
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
        private Object currentUserUuid;
        private int judgeRemind;
        private Object nextHandlerName;
        private String remindTime;
        private String finishTime;
        private Object remindOpinion;
        private int delayApplyTaskType;
        private String taskStatusName;
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

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
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

        public Object getCurrentUserUuid() {
            return currentUserUuid;
        }

        public void setCurrentUserUuid(Object currentUserUuid) {
            this.currentUserUuid = currentUserUuid;
        }

        public int getJudgeRemind() {
            return judgeRemind;
        }

        public void setJudgeRemind(int judgeRemind) {
            this.judgeRemind = judgeRemind;
        }

        public Object getNextHandlerName() {
            return nextHandlerName;
        }

        public void setNextHandlerName(Object nextHandlerName) {
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

        public String getTaskStatusName() {
            return taskStatusName;
        }

        public void setTaskStatusName(String taskStatusName) {
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
