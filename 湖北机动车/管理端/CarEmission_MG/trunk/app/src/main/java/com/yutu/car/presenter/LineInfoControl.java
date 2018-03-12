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

public class LineInfoControl extends BaseControl{
    private String id;
    private String title;
    private NetControl netControl;
    /*{"flag":1,"data":{"lineName":"柴油线","lineType":"重柴线","lineState":"正常",
    "lineIp":"10.22.254.141","deviceBrand":"安车","sqState":"已授权","carCardNumber":"无","allNum":0,
    "okNum":0,"hgl":"----","hglqk":"正常","jdyxsj":415,"bhdqk":"未饱和","checkErrNum":0,"fxycsl":0,
    "pzsbycsl":0,"hhgl":1,"lhgl":0.4,"bhd":20}}*/
    private String[] keys = new String[]{"lineName","lineType","lineState",
            "lineIp","deviceBrand","sqState", "carCardNumber","allNum", "okNum",
            "hgl","hglqk","jdyxsj","bhdqk","checkErrNum","fxycsl","pzsbycsl","hhgl","lhgl","bhd"};

    /*lineName 检测线名称   lineType 检测线类型   lineState 检测线状态
lineIp 检测线IP  deviceBrand 设备商   sqState  授权状态
carCardNumber 在检车辆   allNum 检测总数   okNum 合格数
hgl 合格率
hglqk 合格率情况     jdyxsj 检定有效期（天）    bhdqk 饱和度情况
checkErrNum 检测异常数量   fxycsl 过程数据分析异常数量   pzsbycsl 拍照及识别异常数量
hhgl 检测异常报警上限   lhgl 检测异常报警下限   bhd 设定的饱和度*/
    private String[] names = new String[]{"检测线名称:","检测线类型:","检测线状态:",
            "检测线IP:","设备商:",
            "授权状态:","在检车辆:",
            "检测总数:","合格数:",
            "合格率:","合格率情况:","检定有效期(天):","饱和度情况:","检测异常数量:",
            "过程数据分析异常数量:","拍照及识别异常数量:","检测异常报警上限:","检测异常报警下限:","设定的饱和度:",};

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
        netControl.requestForCheckLine(id,call);
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
