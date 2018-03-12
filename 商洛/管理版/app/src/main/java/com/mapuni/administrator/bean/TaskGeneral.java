package com.mapuni.administrator.bean;

/**
 * Created by 15225 on 2017/8/21.
 */

public class TaskGeneral {

    /**
     * uuid : 4028f8fb5e03b3bd015e0408d4e4003f
     * taskUuid : 4028f8fb5e03b3bd015e0408d494003b
     * taskType : 0
     * taskTypeName : 上报
     * taskName : ddd
     * startTime : null
     * endTime : null
     * delayApplyStatus : 0
     * createTime : 2017-08-21 17:04:01
     * remindFlag : null
     * remindTime : null
     * finishTime : null
     * remindOpinion : null
     */

    private String uuid;
    private String taskUuid;
    private int taskType;
    private String taskTypeName;
    private String taskName;
    private Object startTime;
    private Object endTime;
    private int delayApplyStatus;
    private int delayApplyTaskType;
    private String createTime;
    private Object remindFlag;
    private Object remindTime;
    private Object finishTime;
    private Object remindOpinion;

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

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public int getDelayApplyStatus() {
        return delayApplyStatus;
    }

    public void setDelayApplyStatus(int delayApplyStatus) {
        this.delayApplyStatus = delayApplyStatus;
    }
    public int getDelayApplyTaskType() {
        return delayApplyTaskType;
    }

    public void setDelayApplyTaskType(int delayApplyTaskType) {
        this.delayApplyTaskType = delayApplyTaskType;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(Object remindFlag) {
        this.remindFlag = remindFlag;
    }

    public Object getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Object remindTime) {
        this.remindTime = remindTime;
    }

    public Object getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Object finishTime) {
        this.finishTime = finishTime;
    }

    public Object getRemindOpinion() {
        return remindOpinion;
    }

    public void setRemindOpinion(Object remindOpinion) {
        this.remindOpinion = remindOpinion;
    }
}
