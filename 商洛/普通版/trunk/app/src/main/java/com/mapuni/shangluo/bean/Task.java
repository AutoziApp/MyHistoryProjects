package com.mapuni.shangluo.bean;

/**
 * Created by 15225 on 2017/8/17.
 */

public class Task {
    private int Id;//序号
    private String TaskName;//任务名称
    private String EndDate;//截止日期
    private String TaskSource;//任务来源
    private String TaskDetail;//详情

    public Task() {}

    public Task(int id, String taskName, String endDate, String taskSource, String taskDetail) {
        Id = id;
        TaskName = taskName;
        EndDate = endDate;
        TaskSource = taskSource;
        TaskDetail = taskDetail;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getTaskSource() {
        return TaskSource;
    }

    public void setTaskSource(String taskSource) {
        TaskSource = taskSource;
    }

    public String getTaskDetail() {
        return TaskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        TaskDetail = taskDetail;
    }
}
