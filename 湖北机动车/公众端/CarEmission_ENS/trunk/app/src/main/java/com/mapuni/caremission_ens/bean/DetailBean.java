package com.mapuni.caremission_ens.bean;

import java.io.Serializable;

/**
 * Created by yawei on 2017/3/30.
 */

public class DetailBean {
    private String title;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
