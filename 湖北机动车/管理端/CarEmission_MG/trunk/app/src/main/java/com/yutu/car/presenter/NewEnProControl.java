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

public class NewEnProControl extends BaseControl {
    private String id;
    private String title;
    private NetControl netControl;
    private String[] keys = new String[]{"HPHM","CPYS","VIN","JCBGDBH",
                     "JCXMC","JYJGMC","JCFF", "JYSJ","ZZPDJG","CLJCYXQ"};
    private String[] names = new String[]{"车牌号码","车牌颜色","车架号","检测报告单编号",
            "检测线名称","检测机构名称","检测方法", "检测时间","检测结果","车辆检测有效期"};

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
        netControl.requestForEnPrListDetials(id, call);
    }

    @Override
    public List transData(String response) {

        Map map = JsonUtil.jsonToMap(response);
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("info");
        return addBean(_Map, keys, names);
    }

    @Override
    public List transDataJX(JSONObject object) {

        List list = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            DetailBean bean = new DetailBean();
            try {
                String value = object.getString(keys[i]);
                bean.setTitle(names[i]);
                bean.setValue(value);
                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List addBean(LinkedTreeMap _Map, String[] key, String[] name) {
        List list = new ArrayList();
        for (int i = 0; i < key.length; i++) {
            DetailBean bean = new DetailBean();
            String value = _Map.get(key[i]) + "";
            if("检测信息:".equals((name[i]))){
                bean.setTitle(name[i]);
                if("0.0".equals(value)){
                    bean.setValue("首次检测");
                }else if("1.0".equals(value)){
                    bean.setValue("第一次复检");
                }else if("2.0".equals(value)){
                    bean.setValue("第二次复检");
                }else {
                    bean.setValue("两次以上复检");
                }
            }else {
                bean.setTitle(name[i]);
                bean.setValue(value);
            }

            list.add(bean);
        }
        return list;
    }

}
