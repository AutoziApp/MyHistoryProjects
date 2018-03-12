package com.mapuni.car.mvp.detailcar.model;

/**
 * Created by YZP on 2018/1/11.
 */
public class EventBean {
    private String isExist;
    private String title;
    public EventBean(String isExist) {
        this.isExist = isExist;
    }

    public String getIsExist() {
        return isExist;
    }

    public void setIsExist(String isExist) {
        this.isExist = isExist;
    }
}
