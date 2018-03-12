package com.yutu.car.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.yutu.car.bean.DetailBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/3/30.
 */

public class CheckCountControl extends BaseControl{
    private String id;
    private String title;
    private String startTime;
    private String endTime;
    private String pkid;
    private NetControl netControl;
    private String[] keys = new String[]{"TOTALNUM","QUALIFIEDNUM","NOCHECKNUM","UNQUALIFIEDNUM",
            "CHECKNUM","STATIONNAME"};
    private String[] names = new String[]{"总数:","合格总数:","未通过总数:","不合格总数",
            "审核通过总数:","站点名称:"};

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public void requestData(StringCallback call){
        if(netControl==null){
            netControl = new NetControl();
        }
        netControl.requestForCheckCount(startTime,endTime,id,pkid,call);
    }

    @Override
    public List transData(String response) {
        List list = new ArrayList();
        Map map = JsonUtil.jsonToMap(response);
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
        for(int i = 0;i<keys.length;i++){
            DetailBean bean = new DetailBean();
            String value = _Map.get(keys[i])+"";
            bean.setTitle(names[i]);
            bean.setValue(value);
            list.add(bean);
        }
        return list;
    }

    @Override
    public List transDataJX(JSONObject jsonObject) {
        return null;
    }


}
