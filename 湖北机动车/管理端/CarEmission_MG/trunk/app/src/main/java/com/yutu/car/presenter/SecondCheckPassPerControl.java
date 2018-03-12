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

public class SecondCheckPassPerControl extends BaseControl{
    private String id;
    private String title;
    private String startTime;
    private String endTime;
    private String pkid;
    private NetControl netControl;
    private String[] keys = new String[]{"hj_cls","hj_hgs","hj_hgl","sj_cls",
            "sj_hgs","sj_hgl","fc1_hj","fc1_hgs","fc1_hgl",
            "fc2_hj","fc2_hgl","fc2_hgs","fc3_hj","fc3_hgs","fc3_hgl","stationname","regionname"};
    private String[] names = new String[]{"合计车辆数:","合计合格数:","合计合格率:","首检车辆数:",
            "首检合格数:","首检合格率:","一次复测合计:","一次复测合格数:"
            ,"一次复测合格率:","二次复测合计:","二次复测合格数:","二次复测合格率:",
            "两次以上复测合计:","两次以上复测合格数:","两次以上复测合格率:","检测站名称:","区域名称:"};

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
        netControl.requestForSecondCheckPassPerCount(startTime,endTime,id,pkid,call);
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
