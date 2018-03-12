package com.mapuni.car.mvp.login.model;

/**
 * Created by YZP on 2017/12/7.
 */
public class CarTypeBean {
    private String name;
    private String code;

    public CarTypeBean(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
