package com.mapuni.caremission_ens.presenter;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.mapuni.caremission_ens.bean.DetailBean;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/3/30.
 */

public class CarDBInfoControl extends BaseControl {
    private String vin;
    private String carNum;
    private NetControl netControl;
    private String title;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTitle() {
        return "车辆详细信息";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CarDBInfoControl(String vin, String carNum) {
        this.vin = vin;
        this.carNum = carNum;
    }

    private String[] keys = new String[]{"CHECKTIME", "CHECKMETHOD", "VIN", "CHECKDATASTATE", "NEXTCHECKDATE", "CHECKRESULT",
            "RECHECKINFO", "CARCARDNUMBER", "LIMITEDDATE", "STATIONSHORTNAME"};
    private String[] names = new String[]{"检测时间:", "检测方法:", "车辆vin:", "检测数据状态:", "下次检测时间:",
            "检测结果:", "检测信息:", "车牌号:", "检测有效期截止日期:", "检测站名称:"};

    private String[] key = new String[]{"JCBGDBH", "HPHM", "CPYS", "VIN", "JCXMC", "JYJGMC",
            "JCFF", "JYSJ", "ZZPDJG", "CLJCYXQ"};
    private String[] name = new String[]{"检测报告单编号:", "号牌号码:", "车牌颜色:", "vin号码:", "检测机构名称:",
            "检测线名称:", "检测方法:", "检测时间:", "检测结果:", "车辆检测有效期:"};

    @Override
    public void requestData(StringCallback call) {
        if (netControl == null) {
            netControl = new NetControl();
        }
//        HashMap map = new HashMap();
//        map.put("cphm", carNum);
//        map.put("vin", vin);
        netControl.requestForCarDBInfo(carNum,vin, call);
    }

    @Override
    public List transData(String response) {
        Log.i("Lybin", response);
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
                String value = object.getString(key[i]);
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
