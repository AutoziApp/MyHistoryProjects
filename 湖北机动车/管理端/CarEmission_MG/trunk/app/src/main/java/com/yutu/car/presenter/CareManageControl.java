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

public class CareManageControl extends BaseControl{
    private String id;
    private String title;
    private NetControl netControl;
    private String[] keys = new String[]{"FUELTYPENAME","COLORNAME","VIN",
            "OWNERNAME","CARCARDNUMBER","OUTFITWEIGHT",
            "MODENAME","CHECKPEOPLE",
            "REGISTERDATE","MAXWEIGHT","UESDEQUALITYNAME","CARBRAND","CARDTYPENAME","ENGINENUMBER"};
    private String[] names = new String[]{"燃料种类:","车牌颜色:","车辆vin代码:",
            "所有者姓名:","车牌号:",
            "整备质量:","车辆类型:",
            "核定载人数:","车辆登记日期:",
            "最大质量:","使用性质:","车辆品牌:","号牌类别:","发动机号码:"};

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
        netControl.requestForCarManageinfo(id,call);
    }

    @Override
    public List transData(String response) {
        List list = new ArrayList();
        Map map = JsonUtil.jsonToMap(response);
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
        for(int i = 0;i<keys.length;i++){
            DetailBean bean = new DetailBean();
            String value = (String) _Map.get(keys[i]);
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
