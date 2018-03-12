package com.mapuni.administrator.bean;

import java.util.List;

/**
 * Created by 15225 on 2017/9/21.
 */

public class Trend {


    /**
     * gridName : null
     * handledRecordCount : 0
     * handlingRecordCount : 0
     * ratio : null
     * recordTatolCount : 0
     * problemTypeName : null
     * problemTypeCount : 0
     * correspondingTime : 2017-09-15
     * tatolUsersCount : 0
     * onlineUsersCount : 0
     * userRealname : null
     * reportCount : 0
     * delayCount : 0
     * actuallySignCount : 0
     * shouldSignCount : 0
     * signRate : null
     * patrolProblemDictCount : [{"name":"企事业单位和其他生产经营者","patrolProblemDictCount":0},{"name":"施工工地","patrolProblemDictCount":0},{"name":"各类道路","patrolProblemDictCount":0},{"name":"散乱污企业","patrolProblemDictCount":0},{"name":"公共水体","patrolProblemDictCount":0},{"name":"畜牧养殖单位","patrolProblemDictCount":0},{"name":"秸秆垃圾焚烧","patrolProblemDictCount":0},{"name":"其他问题","patrolProblemDictCount":0}]
     */

    private String gridName;
    private int handledRecordCount;
    private int handlingRecordCount;
    private String ratio;
    private int recordTatolCount;
    private String problemTypeName;
    private int problemTypeCount;
    private String correspondingTime;
    private int tatolUsersCount;
    private int onlineUsersCount;
    private String userRealname;
    private int reportCount;
    private int delayCount;
    private int actuallySignCount;
    private int shouldSignCount;
    private String signRate;
    private List<PatrolProblemDictCountBean> patrolProblemDictCount;

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

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
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

    public String getCorrespondingTime() {
        return correspondingTime;
    }

    public void setCorrespondingTime(String correspondingTime) {
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

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }

    public int getActuallySignCount() {
        return actuallySignCount;
    }

    public void setActuallySignCount(int actuallySignCount) {
        this.actuallySignCount = actuallySignCount;
    }

    public int getShouldSignCount() {
        return shouldSignCount;
    }

    public void setShouldSignCount(int shouldSignCount) {
        this.shouldSignCount = shouldSignCount;
    }

    public String getSignRate() {
        return signRate;
    }

    public void setSignRate(String signRate) {
        this.signRate = signRate;
    }

    public List<PatrolProblemDictCountBean> getPatrolProblemDictCount() {
        return patrolProblemDictCount;
    }

    public void setPatrolProblemDictCount(List<PatrolProblemDictCountBean> patrolProblemDictCount) {
        this.patrolProblemDictCount = patrolProblemDictCount;
    }

    public static class PatrolProblemDictCountBean {
        /**
         * name : 企事业单位和其他生产经营者
         * patrolProblemDictCount : 0
         */

        private String name;
        private int patrolProblemDictCount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPatrolProblemDictCount() {
            return patrolProblemDictCount;
        }

        public void setPatrolProblemDictCount(int patrolProblemDictCount) {
            this.patrolProblemDictCount = patrolProblemDictCount;
        }
    }
}
