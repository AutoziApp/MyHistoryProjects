package com.yutu.car.presenter;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public abstract class BaseControl implements Serializable {
    public String id;
    public String title;
    private String startTime;
    private String endTime;
    private String pkid;
    private String  checkResultid;

    public String getCheckResultid() {
        return checkResultid;
    }

    public void setCheckResultid(String checkResultid) {
        this.checkResultid = checkResultid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public abstract  String getId() ;
    public abstract  void setId(String id);

    public abstract  String getTitle();

    public abstract  void setTitle(String title) ;

    public abstract void requestData(StringCallback call);
    public abstract List transData(String response);
    public abstract List transDataJX(JSONObject jsonObject) ;

}
