package com.yutu.car.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.yutu.car.bean.DetailBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/3/30.
 */

public class StationInfoControl extends BaseControl {
    private String id;
    private String title;
    private NetControl netControl;
    private String[] keys = new String[]{"REGION", "STATIONNAME", "STATIONADDRESS",
            "FZRPHONE", "STATIONSTATE", "QIYOUCOUNT",
            "QINGCHAICOUNT", "ZHONGCHAICOUNT",
            "BUILDSTATECOUNT", "CHAIQIHUNHECOUNT", "BUSINESS", "NOTBUSINESS"};
    private String[] names = new String[]{"行政区名称:", "检测站名称:", "检测站地址:",
            "负责人电话:", "检测站状态:",
            "汽油检测线数量:", "轻柴检测线数量:",
            "重柴检测线数量:", "在建检测线数量:", "柴汽混合数量：",
            "营业:", "不营业:"};

    private String[] key=new String[]{"JYJGMC","JC","JGDZ","TYSHXYDM","JLRZBH","ZZRDYXQ","FRDB","LXR","HBFZR","LXDH","zcrq"};
    private String[] name=new String[]{"检测机构名称","区域","机构地址","统一社会信用代码","计量认证编码","资质认定有效期",
            "法人代表","联系人","环保负责人","联系电话","注册日期"};

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
    public void requestData(StringCallback call) {
        if (netControl == null) {
            netControl = new NetControl();
        }
        netControl.requestForStationSearch(id, call);
    }

    @Override
    public List transData(String response) {
        List list = new ArrayList();
        Map map = JsonUtil.jsonToMap(response);
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
        
        for (int i = 0; i < keys.length; i++) {
            DetailBean bean = new DetailBean();
            String value = (String) _Map.get(keys[i]);
            bean.setTitle(names[i]);
            bean.setValue(value);
            list.add(bean);
        }
        return list;
    }
    @Override
    public List transDataJX(JSONObject object) {
        List list = new ArrayList();
        for (int i = 0; i < key.length; i++) {
            DetailBean bean = new DetailBean();
            try {
                String value  = object.getString(key[i]);
                bean.setTitle(name[i]);
                bean.setValue(value);
                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
