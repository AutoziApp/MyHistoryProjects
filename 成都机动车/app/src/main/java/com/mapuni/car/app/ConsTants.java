package com.mapuni.car.app;

import android.content.Context;
import android.os.Environment;

import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarTypeBean;
import com.mapuni.core.utils.SpUtil;
import com.mapuni.core.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/6/30.
 */

public class ConsTants {

    public static String regionCode = "";

    public static String UserId = "";

    public static String stationId = "";

    public static String Version = "";

    public static String CarNumFirst = "";
    public static String Base_Url = "";
//        public static String Default_Base_Url = "http://172.22.40.142:8889";
    public static String Default_Base_Url = "http://121.8.182.146:8888";
//public static String Default_Base_Url = "http://192.168.1.105:8886/";
//    public static String Base_Url = "http://192.168.111.103:8878/";
    /**
     * 系统部署文件的跟目录mobileemergency文件夹路径
     * /storage/emulated/0/mobilegrid/data/MobileEnforcement.db
     */
    public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/mobilegrid/";
    /**
     * 系统部署文件MobileEnforcement文件夹路径
     */
    public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "data/";
    /*手机端路径*/
    /**
     * 手机端更新apk的本地目录路径，即AutoUpdate文件夹
     */
    public static final String SDCARD_AutoUpdate_LOCAL_PATH = SDCARD_RASK_DATA_PATH
            + "AutoUpdate";
    /**
     * 手机端更新apk的本地文件路径，即update.apk
     */
    public static final String SDCARD_APK_LOCAL_PATH = SDCARD_RASK_DATA_PATH
            + "AutoUpdate/update.apk";
    //号牌颜色
    public static List CarTypeList;
    public static List MonitorTypeList;
    //车辆种类
    public static List CarModeList;
    //号牌种类
    public static List CarTypeNumList;
    //监测模式
    public static List CheckModeList;
    //车牌号码
    public static List CarNumList;
    public static HashMap<String, List<LoginCarTypeBean>> carMap = new HashMap<>();
    public static String PhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mapuni";
    private static String[] CarColors;
    private static String[] CarMonitorTypes = new String[]{};
    private static String[] CarModels = new String[]{};
    private static String[] CarTypeNums = new String[]{};
    private static String[] checkModes;
    private static LinkedHashMap<String, String> CarColorMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> CarCheckMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> CarNumMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> CarCheckMethodMap = new LinkedHashMap<>();
    private static Map<String, String> CarTypeMap = new HashMap<>();

    public static void setIp(Context context, String s) {
        s = s.replace("/", "");
        s = s.replaceAll("http:", "");
        Base_Url = "http://" + s + "/";
        SpUtil.putString(context, SpUtil.BaseIp, Base_Url);
        ToastUtils.showToast(context, "IP设置成功");
    }

    public static void initIp(Context context) {
        Base_Url = SpUtil.getString(context, SpUtil.BaseIp);
        if (Base_Url.equals("")) {
            Base_Url = Default_Base_Url;
        }
    }

    //号牌颜色
    public static String[] getCarColors() {
        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> cardcolor = carMap.get("cardcolor");
            if (cardcolor.size() > 0) {
                if (CarColors == null) {
                    CarColors = new String[cardcolor.size()];
                }
                for (int i = 0; i < cardcolor.size(); i++) {
                    CarColors[i] = cardcolor.get(i).getName();
                }
            }
        }
        return CarColors;
    }

    public static String[] getCarMonitorTypes() {
        if (CarMonitorTypes.length == 0 && MonitorTypeList != null) {
            CarMonitorTypes = new String[MonitorTypeList.size()];
            for (int i = 0; i < MonitorTypeList.size(); i++) {
                CarMonitorTypes[i] = ((CarTypeBean.DataBean.CODCARDCOLORBean) MonitorTypeList.get(i)).getName();
            }
            return CarMonitorTypes;
        }
        return CarMonitorTypes;
    }

    //车辆种类
    public static String[] getCarModels() {
        if (CarModels.length == 0 && CarModeList != null) {
            CarModels = new String[CarModeList.size()];
            for (int i = 0; i < CarModeList.size(); i++) {
                CarModels[i] = ((CarTypeBean.DataBean.CODCARMODEBean) CarModeList.get(i)).getName();
            }
            return CarModels;
        }
        return CarModels;
    }

    //车辆种类
    public static String[] getCarTypeNums() {
        if (CarTypeNums.length == 0 && CarTypeNumList != null) {
            CarTypeNums = new String[CarTypeNumList.size()];
            for (int i = 0; i < CarTypeNumList.size(); i++) {
                CarTypeNums[i] = ((CarTypeBean.DataBean.CODCARTYPEBean) CarTypeNumList.get(i)).getName();
            }
            return CarTypeNums;
        }
        return CarTypeNums;
    }

    //监测模式
    public static String[] getCarCheckMode() {

        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> checkmodeList = carMap.get("checkmode");
            if (checkmodeList.size() > 0) {
                if (checkModes == null) {
                    checkModes = new String[checkmodeList.size()];
                }
                for (int i = 0; i < checkmodeList.size(); i++) {
                    checkModes[i] = checkmodeList.get(i).getName();

                }
            }
        }
        return checkModes;
    }

    //车辆颜色Map
    public static LinkedHashMap<String, String> getCarColorMap() {
        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> cardcolor = carMap.get("cardcolor");
            for (int i = 0; i < cardcolor.size(); i++) {
                CarColorMap.put(cardcolor.get(i).getName(), cardcolor.get(i).getCode());
                CarColorMap.put(cardcolor.get(i).getCode(), cardcolor.get(i).getName());
            }
            return CarColorMap;
        }

        return CarColorMap;
    }

    //车辆检测模式
    public static LinkedHashMap<String, String> getCarCheckMap() {
        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> checkmodeList = carMap.get("checkmode");
            for (int i = 0; i < checkmodeList.size(); i++) {
                CarCheckMap.put(checkmodeList.get(i).getName(), checkmodeList.get(i).getCode());
            }
            return CarCheckMap;
        }

        return CarCheckMap;
    }

    //检测方法
    public static LinkedHashMap<String, String> getCheckMethodMap() {
        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> checkmethodList = carMap.get("checkmethod");
            for (int i = 0; i < checkmethodList.size(); i++) {
                CarCheckMethodMap.put(checkmethodList.get(i).getCode(), checkmethodList.get(i).getName());
            }
            return CarCheckMethodMap;
        }
        return CarCheckMethodMap;
    }
    public static LinkedHashMap<String, String> getCheckMethodMap1() {
        if (carMap != null && carMap.size() > 0) {
            List<LoginCarTypeBean> checkmethodList = carMap.get("checkmethod");
            for (int i = 0; i < checkmethodList.size(); i++) {
                CarCheckMethodMap.put(checkmethodList.get(i).getName(), checkmethodList.get(i).getCode());
            }
            return CarCheckMethodMap;
        }
        return CarCheckMethodMap;
    }
    public static LinkedHashMap<String, String> getCarNumMap() {
        if (CarNumMap.size() == 0 && CarNumList != null) {
            for (int i = 0; i < CarNumList.size(); i++) {
                String name = ((CarTypeBean.DataBean.CODCARDNAMEBean) CarNumList.get(i)).getName();
                String code = ((CarTypeBean.DataBean.CODCARDNAMEBean) CarNumList.get(i)).getCode();
                CarNumMap.put(code, name);
            }
            return CarNumMap;
        }
        return CarNumMap;
    }
}
