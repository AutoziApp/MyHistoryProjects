package cn.com.mapuni.meshing.model;

import java.io.Serializable;

public class XiaFaTaskModel implements Serializable {
	 /**
     * id : D5E83A36-561E-49E9-BEC0-DC7D91A1FA63
     * taskId : D05EFD74-C316-4B61-AE45-48C620AA66B6
     * taskName : 测试任务
     * taskType : shuiti,yangzhi
     * taskTypeName : 公共水体,畜禽养殖单位
     * patrolScope : 全市范围
     * importance : importance01
     * importanceName : 普通
     * remark : 全市范围日常巡查。
     * sendGrid : 3701130607
     * createGridName : 马山镇办事处
     * beginTime : 2017-03-29 10:47:47
     * endTime : 2017-04-29 10:47:47
     * status : 0
     * statusName : 未签收
     * reminded : 0
     */

    private String id;
    private String taskId;
    private String taskName;
    private String taskType;
    private String taskTypeName;
    private String patrolScope;
    private String importance;
    private String importanceName;
    private String remark;
    private String sendGrid;
    private String createGridName;
    private String beginTime;
    private String endTime;
    private String status;
    private String statusName;
    private String reminded;
    private String applyfor;
    public String getApplyfor() {
		return applyfor;
	}

	public void setApplyfor(String applyfor) {
		this.applyfor = applyfor;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public String getPatrolScope() {
        return patrolScope;
    }

    public void setPatrolScope(String patrolScope) {
        this.patrolScope = patrolScope;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getImportanceName() {
        return importanceName;
    }

    public void setImportanceName(String importanceName) {
        this.importanceName = importanceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendGrid() {
        return sendGrid;
    }

    public void setSendGrid(String sendGrid) {
        this.sendGrid = sendGrid;
    }

    public String getCreateGridName() {
        return createGridName;
    }

    public void setCreateGridName(String createGridName) {
        this.createGridName = createGridName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getReminded() {
        return reminded;
    }

    public void setReminded(String reminded) {
        this.reminded = reminded;
    }

}
