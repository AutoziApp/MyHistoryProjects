package com.yutu.car.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.yutu.car.bean.DetailBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/3/30.
 */

public class CarDBInfoControl extends BaseControl{
    private  Context context;
    private String vin;
    private String carNum;
    private NetControl netControl;
    private String title;
    private String pkid,checkTime;
    private String checkResultid ;

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String getCheckResultid() {
        return checkResultid;
    }
    @Override
    public void setCheckResultid(String checkResultid) {
        this.checkResultid = checkResultid;
    }

    @Override
    public String getPkid() {
        return pkid;
    }

    @Override
    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    @Override
    public String getId() {
        return checkTime;
    }

    @Override
    public void setId(String id) {

    }

    public String getTitle() {
        return "车辆详细信息";
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public CarDBInfoControl(){

    }

    public CarDBInfoControl(String vin, String carNum){
        this.vin = vin;
        this.carNum = carNum;

    }
    private String[] keys = new String[]{"CHECKTIME","OWNERNAME",
            "CHECKRESULT", "STATIONNAME","PKID","CHECKRESULTID"};//,
    private String[] names = new String[]{"检测时间:","车辆所有者:","检测结果:"
            ,"检测站:","车辆ID","检测结果ID"};

    private String[] key = new String[]{"JCBGDBH", "HPHM", "CPYS", "VIN", "JCXMC", "JYJGMC",
            "JCFF", "JYSJ", "ZZPDJG", "CLJCYXQ"};
    private String[] name = new String[]{"检测报告单编号:", "号牌号码:", "车牌颜色:", "vin号码:", "检测机构名称:",
            "检测线名称:", "检测方法:", "检测时间:", "检测结果:", "车辆检测有效期:"};

    @Override
    public void requestData(StringCallback call){
        if(netControl==null){
            netControl = new NetControl();
        }
        HashMap map = new HashMap();
        map.put("carCardNumber",carNum);
        map.put("vin",vin);
        Log.d("lvcheng","carNum="+carNum+" vin="+vin);
        netControl.requestForCarDBInfo(carNum,vin, call);
    }

    @Override
    public List transData(String response) {
        Log.i("Lybin",response);
        List list = new ArrayList();
        Map map = JsonUtil.jsonToMap(response);
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
        for(int i = 0;i<keys.length;i++){
            DetailBean bean = new DetailBean();
            String value = (String) _Map.get(keys[i]);
            if(keys[i].equals("CHECKTIME")){
                checkTime=_Map.get("CHECKTIME").toString();
                bean.setTitle(names[i]);
                bean.setValue(value);
                list.add(bean);
            }else if(keys[i].equals("PKID")){
                pkid=_Map.get("PKID").toString();
            }else if(keys[i].equals("CHECKRESULTID")){
              checkResultid=_Map.get("CHECKRESULTID").toString();
            }else {
                bean.setTitle(names[i]);
                bean.setValue(value);
                list.add(bean);
            }

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
