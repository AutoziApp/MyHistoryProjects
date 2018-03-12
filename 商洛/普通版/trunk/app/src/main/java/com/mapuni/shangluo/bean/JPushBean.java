package com.mapuni.shangluo.bean;

/**
 * Created by 15225 on 2017/9/20.
 */

public class JPushBean {


    /**
     * isTask : true
     * isNews : false
     * newsUuid :
     * taskUuid : 4028f8105e8831bf015e886e49f200a9
     * taskType : 0
     */

    private boolean isTask;
    private boolean isNews;
    private String newsUuid;
    private String taskUuid;
    private String taskType;
    private String delayApplyTaskType;

    public boolean isIsTask() {
        return isTask;
    }

    public void setIsTask(boolean isTask) {
        this.isTask = isTask;
    }

    public boolean isIsNews() {
        return isNews;
    }

    public void setIsNews(boolean isNews) {
        this.isNews = isNews;
    }

    public String getNewsUuid() {
        return newsUuid;
    }

    public void setNewsUuid(String newsUuid) {
        this.newsUuid = newsUuid;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDelayApplyTaskType() {
        return delayApplyTaskType;
    }

    public void setDelayApplyTaskType(String delayApplyTaskType) {
        this.delayApplyTaskType = delayApplyTaskType;
    }
}
