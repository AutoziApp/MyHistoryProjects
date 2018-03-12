package com.mapuni.car.mvp.detailcar.model;

/**
 * Created by yawei on 2017/8/30.
 */

public class ImageValueBean {
    private String name;
    private String color;
    private String value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        if(color==null||color.equals(""))
            return null;
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
