package com.mapuni.car.mvp.detailcar.model;

/**
 * Created by yawei on 2017/8/30.
 */

public class EditValueBean {
    private String name;
    private String color;
    private String value;
    private boolean isEndItem;
    private String maxLength;
    private String key;
    private String title;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public boolean isEndItem() {
        return isEndItem;
    }

    public void setEndItem(boolean endItem) {
        isEndItem = endItem;
    }

}
