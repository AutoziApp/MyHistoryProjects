package com.mapuni.car.mvp.searchcar.gotoview.model;

/**
 * Created by YZP on 2017/11/21.
 */
public class CarListBean {
    private String name;
    private String value;
    private int flag;

    public CarListBean(String name, String value, int flag) {
        this.name = name;
        this.value = value;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
