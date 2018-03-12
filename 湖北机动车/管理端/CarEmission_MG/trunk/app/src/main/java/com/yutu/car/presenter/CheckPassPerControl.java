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

public class CheckPassPerControl extends BaseControl{
    private String id;
    private String title;
    private String startTime;
    private String endTime;
    private String pkid;
    private NetControl netControl;
    private String[] keys = new String[]{"hj_cls","hj_hgs","hj_hgl","im_cls",
            "im_hgs","im_hgl","ld_hj_cls","ld_hj_hgs","ld_hj_hgl",
            "tsi_cls","tsi_hgs","tsi_hgl","snap_cls","snap_hgs","snap_hgl","stationname","regionname"};
    private String[] names = new String[]{"合计车辆数:","合计合格数:","合计合格率:","简易瞬态工况法车辆数",
            "简易瞬态工况法合格数:","简易瞬态工况法合格率:","加载减速工况法车辆数:","加载减速工况法合格数:"
            ,"加载减速工况法合格率:","双怠速法车辆数:","双怠速法合格数:","双怠速法合格率:",
            "不透光烟度法车辆数:","不透光烟度法合格数:","不透光烟度法合格率:","检测站名称:","区域名称:"};

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
        netControl.requestForCheckPassPerCount(startTime,endTime,id,pkid,call);
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
