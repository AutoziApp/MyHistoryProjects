package com.mapuni.car.mvp.ywrequest.model;

/**
 * Created by yawei on 2017/8/30.
 */

public class SelectValueBean {
    private String name;
    private String[] selects;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSelects() {
        return selects;
    }

    public void setSelects(String[] selects) {
        this.selects = selects;
    }

    public String getValue() {
        if(value==null)
            return null;
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
