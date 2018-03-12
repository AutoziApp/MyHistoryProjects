package com.mapuni.administrator.bean;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor tianfy
 * @time 2017/10/18 14:56
 * @change
 * @chang time
 * @class describe
 */

public class GridNameBean {
    
    private String name;
    private String uuid;
    private String createTime;
    private int judgeDetailsStatus;
    private String issuedTaskStatus;

    public String getIssuedTaskStatus() {
        return issuedTaskStatus;
    }

    public void setIssuedTaskStatus(String issuedTaskStatus) {
        this.issuedTaskStatus = issuedTaskStatus;
    }

    public int getJudgeDetailsStatus() {
        return judgeDetailsStatus;
    }

    public void setJudgeDetailsStatus(int judgeDetailsStatus) {
        this.judgeDetailsStatus = judgeDetailsStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
