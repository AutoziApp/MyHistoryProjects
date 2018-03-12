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

public class EnProControl extends BaseControl {
    private String id;
    private String title;
    private NetControl netControl;
    //    private String[] keys = new String[]{"HC5025EL","ERRORRATECO2540","HC5025STR","CO5025ED",
//            "STATIONNAME","CHECKDATASTATE","ERRORRATENO5025", "CANCLEPEOPLE","HC5025ED",
//            "CANCLEREASON","CARCARDNUMBER", "NO5025ED","ERRORRATEHC2540","ERRORRATENO2540","HC2540EL",
//            "CHECKRESULT","NO2540ED","CO5025EL", "CO2540ED","CHECKTIME","HC2540ED","NO2540EL", "CO2540STR",
//            "NO5025STR","OWNERNAME","HUMIDITY","CHECKTYPENAME","RECHECKINFO","HC2540STR","CO5025STR","NO5025EL",
//            "CHECKMETHODNAME","STAFFNAME", "CO2540EL","ERRORRATEHC5025","PRESSURE","NO2540STR","ERRORRATECO5025",
//            "CANCLEDATE","TEMPERATURE"};
//    private String[] names = new String[]{
//            "5025工况CO限定值:--","540NO偏差率:--","5025工况CO检测值（修约后的值）:",
//            "5025工况CO判定--","检测站名称:--","数据状态:--",
//            "5025NO偏差率:--","作废人:--","5025工况HC判定:--",
//            "作废原因:--","车牌号--:","5025工况NO判定:--",
//            "2540HC偏差率:--","2540NO偏差率:--","2540工况HC限定值:--",
//            "检测结果:--","2540工况NO判定:--","5025工况CO限定值:",
//            "2540工况CO判定:--","检测时间:--","2540工况HC判定:--",
//            "2540工况NO限定值:--","2540工况CO检测值（修约后的值）:--","5025工况NO检测值（修约后的值）:--",
//            "所属人:--","相对湿度:--","检测类型名称:--",
//            "检测信息:","2540工况HC检测值（修约后的值）:--","5025工况CO检测值（修约后的值）:--",
//            "5025工况NO限定值:--","检测方法:--","操作人:--",
//            "2540工况CO限定值:--","5025 HC 偏差率:--","气压:",
//            "2540工况NO检测值（修约后的值）:--","5025CO偏差率:--","作废时间:--",
//            "温度:--"
//    };
    private String[] keys = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "CANCLEPEOPLE",
            "CANCLEREASON",
            "CANCLEDATE",
            "OWNERNAME",
            "STAFFNAME",
           // "CHECKMETHOD",
            "CO5025STR",
            "HC5025STR",
            "NO5025STR",
            "CO2540STR",
            "HC2540STR",
            "NO2540STR",
            "CO5025EL",
            "HC5025EL",
            "NO5025EL",
            "CO2540EL",
            "HC2540EL",
            "NO2540EL",
            "CO5025ED",
            "HC5025ED",
            "NO5025ED",
            "CO2540ED",
            "HC2540ED",
            "NO2540ED",
//            "HC2540STR",
//            "CO5025STR",
//            "NO5025STR",
//            "HC5025STR",
//            "CO2540EL",
//            "NO2540EL",
//            "HC2540EL",
//            "CO5025EL",
//            "NO5025EL",
//            "HC5025EL",
//            "CO2540ED",
//            "NO2540ED",
//            "HC2540ED",
//            "CO5025ED",
//            "NO5025ED",
//            "HC2540ED",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keysA = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "CANCLEPEOPLE",
            "CANCLEREASON",
            "CANCLEDATE",
            "OWNERNAME",
            "STAFFNAME",
             "COLSTR",
            "COLOWVALUEEL",
            "COLOWVALUEED",
            "HCLSTR",
            "HCLOWVALUEEL",
            "HCLOWVALUEED",
            "COHSTR",
            "COHIGHVALUEEL",
           "COHIGHVALUEED",
           "HCHSTR",
           "HCHIGHVALUEE",
           "HCHIGHVALUEED",
            "LAMBDAVALUEEL",
            "LAMBDAVALUEED",
            "LAMSTR",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keysB = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "CANCLEPEOPLE",
            "CANCLEREASON",
            "CANCLEDATE",
            "OWNERNAME",
            "STAFFNAME",
           // "CHECKMETHOD",
            "S1STR",
            "S2STR",
            "S3STR",
            "AVGSTR",
            "SMOKEVALUEEL",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keysC = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "CANCLEPEOPLE",
            "CANCLEREASON",
            "CANCLEDATE",
            "OWNERNAME",
            "STAFFNAME",
           // "CHECKMETHOD",
            "S100STR",
            "S90STR",
            "S80STR",
            "POWSTR",
            "SPEEDSTR",
            "EL100",
            "EL90",
            "EL80",
            "POWEREL",
            "MORTORSPEEDEL",
            "ED100",
            "ED90",
            "ED80",
            "POWERED",
            "MORTORSPEEDED",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keys2 = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "OWNERNAME",
            "STAFFNAME",
           // "CHECKMETHOD",
            "CO5025STR",
            "HC5025STR",
            "NO5025STR",
            "CO2540STR",
            "HC2540STR",
            "NO2540STR",
            "CO5025EL",
            "HC5025EL",
            "NO5025EL",
            "CO2540EL",
            "HC2540EL",
            "NO2540EL",
            "CO5025ED",
            "HC5025ED",
            "NO5025ED",
            "CO2540ED",
            "HC2540ED",
            "NO2540ED",
//            "CO2540STR",
//            "NO2540STR",
//            "HC2540STR",
//            "CO5025STR",
//            "NO5025STR",
//            "HC5025STR",
//            "CO2540EL",
//            "NO2540EL",
//            "HC2540EL",
//            "CO5025EL",
//            "NO5025EL",
//            "HC5025EL",
//            "CO2540ED",
//            "NO2540ED",
//            "HC2540ED",
//            "CO5025ED",
//            "NO5025ED",
//            "HC2540ED",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };

    private String[] keys2A = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "OWNERNAME",
            "STAFFNAME",
          //  "CHECKMETHOD",
            "COLSTR",
            "COLOWVALUEEL",
            "COLOWVALUEED",
            "HCLSTR",
            "HCLOWVALUEEL",
            "HCLOWVALUEED",
            "COHSTR",
            "COHIGHVALUEEL",
            "COHIGHVALUEED",
            "HCHSTR",
            "HCHIGHVALUEEL",
            "HCHIGHVALUEED",
            "LAMBDAVALUEEL",
            "LAMBDAVALUEED",
            "LAMSTR",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keys2B = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "OWNERNAME",
            "STAFFNAME",
          //  "CHECKMETHOD",
            "S1STR",
            "S2STR",
            "S3STR",
            "AVGSTR",
            "SMOKEVALUEEL",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] keys2C = new String[]{
            "STATIONNAME",
            "CARCARDNUMBER",
            "CHECKTIME",
            "CHECKTYPENAME",
            "CHECKMETHODNAME",
            "CHECKDATASTATE",
            "OWNERNAME",
            "STAFFNAME",
           // "CHECKMETHOD",
            "S100STR",
            "S90STR",
            "S80STR",
            "POWSTR",
            "SPEEDSTR",
            "EL100",
            "EL90",
            "EL80",
            "POWEREL",
            "MORTORSPEEDEL",
            "ED100",
            "ED90",
            "ED80",
            "POWERED",
            "MORTORSPEEDED",
            "CHECKRESULT",
            "RECHECKINFO",
            "TEMPERATURE",
            "HUMIDITY",
            "PRESSURE",

    };
    private String[] names = new String[]{

            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "作废人:",
            "作废原因:",
            "作废时间:",
            "所属人:",
            "操作人:",
            //"监测:",
            "5025工况CO检测值:",
            "5025工况HC检测值:",
            "5025工况NO检测值:",
            "2540工况CO检测值:",
            "2540工况HC检测值:",
            "2540工况NO检测值:",
            "5025工况CO限定值:",
            "5025工况HC限定值:",
            "5025工况NO限定值:",
            "2540工况CO限定值:",
            "2540工况HC限定值:",
            "2540工况NO限定值:",
            "5025工况CO判定:",
            "5025工况HC判定:",
            "5025工况NO判定:",
            "2540工况CO判定:",
            "2540工况HC判定:",
            "2540工况NO判定:",
//            "2540工况CO检测值:",
//            "2540工况NO检测值:",
//            "2540工况HC检测值:",
//            "5025工况CO检测值:",
//            "5025工况NO检测值:",
//            "5025工况HC检测值:",
//            "2540工况CO限定值:",
//            "2540工况NO限定值:",
//            "2540工况HC限定值:",
//            "5025工况CO限定值:",
//            "5025工况NO限定值:",
//            "5025工况HC限定值:",
//            "2540工况CO判定:",
//            "2540工况NO判定:",
//            "2540工况HC判定:",
//            "5025工况CO判定:",
//            "5025工况NO判定:",
//            "5025工况HC判定:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };
    private String[] namesA= new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "作废人:",
            "作废原因:",
            "作废时间:",
            "所属人:",
            "操作人:",
            //"监测:",
            "低怠速CO:",
            "低怠速CO限值:",
            "低怠速CO判定:",
            "低怠速HC:",
            "低怠速HC限值:",
            "低怠速HC判定:",
            "高怠速CO:",
            "高怠速CO限值:",
            "高怠速CO判定:",
            "高怠速HC:",
            "高怠速HC限值:",
            "高怠速HC判定:",
            "过量系数限值:",
            "过量系数判定:",
            "过量系数测试结果:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };

    private String[] namesB = new String[]{

            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "作废人:",
            "作废原因:",
            "作废时间:",
            "所属人:",
            "操作人:",
           // "监测:",
            "第一次 烟度值:",
            "第二次 烟度值:",
            "第三次 烟度值:",
            "平均值:",
            "限值:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };

    private String[] namesC = new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "作废人:",
            "作废原因:",
            "作废时间:",
            "所属人:",
            "操作人:",
           // "监测:",
            "S100STR100检测值:",
            "S90STR90检测值:",
            "S80STR80检测值:",
            "实测最大轮边功率:",
            "对应发动机转速:",
            "EL100检测限值:",
            "EL90检测限值:",
            "EL80检测限值:",
            "实测最大轮边功率限值:",
            "对应发动机转速限值:",
            "ED100检测判定:",
            "ED90检测判定:",
            "ED80检测判定:",
            "实测最大轮边功率判定:",
            "对应发动机转速判定:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };
    private String[] names2 = new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "所属人:",
            "操作人:",
           // "监测:",
            "5025工况CO检测值:",
            "5025工况HC检测值:",
            "5025工况NO检测值:",
            "2540工况CO检测值:",
            "2540工况HC检测值:",
            "2540工况NO检测值:",
            "5025工况CO限定值:",
            "5025工况HC限定值:",
            "5025工况NO限定值:",
            "2540工况CO限定值:",
            "2540工况HC限定值:",
            "2540工况NO限定值:",
            "5025工况CO判定:",
            "5025工况HC判定:",
            "5025工况NO判定:",
            "2540工况CO判定:",
            "2540工况HC判定:",
            "2540工况NO判定:",
//            "2540工况CO检测值:",
//            "2540工况NO检测值:",
//            "2540工况HC检测值:",
//            "5025工况CO检测值:",
//            "5025工况NO检测值:",
//            "5025工况HC检测值:",
//            "2540工况CO限定值:",
//            "2540工况NO限定值:",
//            "2540工况HC限定值:",
//            "5025工况CO限定值:",
//            "5025工况NO限定值:",
//            "5025工况HC限定值:",
//            "2540工况CO判定:",
//            "2540工况NO判定:",
//            "2540工况HC判定:",
//            "5025工况CO判定:",
//            "5025工况NO判定:",
//            "5025工况HC判定:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };
    private String[] names2A = new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "所属人:",
            "操作人:",
            //"监测:",
            "低怠速CO:",
            "低怠速CO限值:",
            "低怠速CO判定:",
            "低怠速HC:",
            "低怠速HC限值:",
            "低怠速HC判定:",
            "高怠速CO:",
            "高怠速CO限值:",
            "高怠速CO判定:",
            "高怠速HC:",
            "高怠速HC限值:",
            "高怠速HC判定:",
            "过量系数限值:",
            "过量系数判定:",
            "过量系数测试结果:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",

    };
    private String[] names2B = new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "所属人:",
            "操作人:",
           // "监测:",
            "第一次 烟度值:",
            "第二次 烟度值:",
            "第三次 烟度值:",
            "平均值:",
            "限值:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",
    };
    private String[] names2C = new String[]{
            "检测站名称:",
            "车牌号:",
            "检测时间:",
            "检测类型名称:",
            "检测方法:",
            "数据状态:",
            "所属人:",
            "操作人:",
           // "监测:",
            "S100STR100检测值:",
            "S90STR90检测值:",
            "S80STR80检测值:",
            "实测最大轮边功率:",
            "对应发动机转速:",
            "EL100检测限值:",
            "EL90检测限值:",
            "EL80检测限值:",
            "实测最大轮边功率限值:",
            "对应发动机转速限值:",
            "ED100检测判定:",
            "ED90检测判定:",
            "ED80检测判定:",
            "实测最大轮边功率判定:",
            "对应发动机转速判定:",
            "检测结果:",
            "检测信息:",
            "温度:",
            "相对湿度:",
            "气压:",
    };

    //    1、检测站名称//    2、车牌//    3、检测时间//    4、类型//    5、方法//    6、数据状态
//    7、车辆所属人//    8、操作人//    9、检测值 （CO2540 NO2540 HC2540   CO5025 NO5025 HC5025 ）
//    10、限定值//    11、判定结果//    12、检测结果//    13、温度//    14、湿度
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
        LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
        String state = (String) _Map.get("CHECKDATASTATE");
        String thod = (String) _Map.get("CHECKMETHOD");
        if ("作废".equals(state)&&thod.equals("WT")) {
            return addBean(_Map, keys, names);
        } else if("作废".equals(state)&&thod.equals("DB")){
            return addBean(_Map, keysA, namesA);
        } else if("作废".equals(state)&&thod.equals("TG")){
            return addBean(_Map, keysB, namesB);
        }else if("作废".equals(state)&&thod.equals("ED")){
            return addBean(_Map, keysC, namesC);
        }else if(!"作废".equals(state)&&thod.equals("WT")){
            return addBean(_Map, keys2, names2);
        }else if(!"作废".equals(state)&&thod.equals("DB")){
            return addBean(_Map, keys2A, names2A);
        }else if(!"作废".equals(state)&&thod.equals("TG")){
            return addBean(_Map, keys2B, names2B);
        }else if(!"作废".equals(state)&&thod.equals("ED")){
            return addBean(_Map, keys2C, names2C);
        }
        return addBean(_Map, keys2C, names2C);
    }

    @Override
    public List transDataJX(JSONObject jsonObject) {
        return null;
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
