package com.mapuni.mobileenvironment.model;

import java.io.Serializable;

/**
 * Created by Mai on 2017/2/9.
 */

public class HistoryRecord implements Serializable {
    private String deviceId;
    private String Tag;

    public HistoryRecord() {
    }

    public HistoryRecord(String deviceId, String tag) {
        this.deviceId = deviceId;
        Tag = tag;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
