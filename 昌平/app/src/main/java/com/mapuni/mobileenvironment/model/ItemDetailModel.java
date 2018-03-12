package com.mapuni.mobileenvironment.model;

import java.io.Serializable;


public class ItemDetailModel implements Serializable {
    private String name;
    private String tag;
    private String deviceId;
    private String flag;
    private String most;
    private double id;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ItemDetailModel(String name, String tag, String deviceId) {
        this.name = name;
        this.tag = tag;
        this.deviceId = deviceId;
    }

    public ItemDetailModel(String name, String tag, String deviceId, String most) {
        this.name = name;
        this.tag = tag;
        this.deviceId = deviceId;
        this.most = most;
    }

    public ItemDetailModel(String name, String tag, String deviceId, String most, String flag) {
        this.name = name;
        this.tag = tag;
        this.deviceId = deviceId;
        this.most = most;
        this.flag = flag;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }


    public ItemDetailModel() {
    }

    public ItemDetailModel(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMost() {
        return most;
    }

    public void setMost(String most) {
        this.most = most;
    }
}
