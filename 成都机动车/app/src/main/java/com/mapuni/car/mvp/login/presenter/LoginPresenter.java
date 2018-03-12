package com.mapuni.car.mvp.login.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.login.contract.LoginContract;
import com.mapuni.car.mvp.login.model.LoginBean;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.login.update.BaseAutoUpdate;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;
import com.mapuni.core.utils.SpUtil;
import com.mapuni.core.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

//import com.mapuni.autoupdate.BaseAutoUpdate;


public class LoginPresenter extends LoginContract.LoginPresenter {
    public int lTostY;
    private Context context;
    public boolean isRemember;
    public boolean isAuto;
    private String userName;
    private String key;
    HashMap<String, List<LoginCarTypeBean>> carMap = new HashMap<>();
    private String[] name;
    private Gson gson;

    @Override
    public void onStart() {
        context = mView.getContext();
        ConsTants.initIp(context);
//        ,"COD_CHECKTYPE"
        name = new String[]{"COD_DEVICECATEGORY", "COD_NOTUSEDGKFREASON", "COD_STATE", "COD_ISDOUBLEFULE",
                "COD_NEWENERGYTYPE", "COD_CHECKDATASTATE", "COD_OWNERTYPE", "COD_MANAGEAREA", "COD_FUELTYPE", "COD_LINETYPE",
                "COD_FUELPUMPFORM", "COD_USEDQUALITY", "COD_MAR_STANDARD", "COD_SENDPOINTTYPE", "COD_CARDCOLOR",
                "COD_CARBRAND", "COD_OILSUPPLY", "COD_REPAIRPROJECTSORT", "COD_DEVICEBRAND", "COD_MESSAGETYPE", "COD_CARMODE"
                , "COD_REGION", "COD_AUTHORIZATIONSTATE", "COD_DRIVEMODE", "COD_FUELSTANDARD", "COD_CARKIND", "COD_CARTYPE", "COD_CHECKSTATIONTYPE",
                "COD_CARDTYPE", "COD_SPEEDCHANGER", "COD_STANDARDMATTERTYPE", "COD_ENGINETYPE", "COD_CARDNAME", "COD_MAR_SUPPLEMENTCAUS", "COD_CARFIELD"
                , "COD_STATIONSTAFFBUSINESS", "COD_AIRINFLOW", "COD_CHECKMODE", "COD_MAR_CANCELCAUSE", "COD_NEWENERGYTYPE", "COD_DEPARTMENT",
                "COD_BADINFOTYPE", "COD_DEVICEPROERTIES", "COD_STROKENUMBER", "COD_GETWAY", "SYS_DUTY", "COD_CHECKMETHOD",
                "COD_ISSYJHQ","COD_CARAREA"};
        initLogin();
    }

    public void requstCarType() {
        ConsTants.initIp(context);
        addSubscribe(mModel.getList()
                .compose(RxUtil.<MyHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (e != null) {
                                    ToastUtils.showToast(context, e.getMessage());
                                }

                            }

                            @Override
                            public void onNext(Map map) {
//                               List cod_devicecategory = (List) map.get("COD_CARDNAME");
                                //         ToastUtils.showToast(context,"数据请求成功");
                                if (gson == null) {
                                    gson = new Gson();
                                }
                                for (int i = 0; i < name.length; i++) {
                                    Object o = map.get(name[i]);
                                    if (o != null) {
                                        List key = (List) map.get(name[i]);
                                        if (key != null && key.size() > 0) {
                                            String value = gson.toJson(key);
                                            List<LoginCarTypeBean> valueList = gson.fromJson(value, new TypeToken<List<LoginCarTypeBean>>() {
                                            }.getType());
                                            String[] split = name[i].split("_");
                                            String mode = "";
                                            for (int j = 0; j < split.length; j++) {
                                                if (!split[j].equals("COD")) {
                                                    mode = mode + split[j];
                                                }
                                            }
                                            carMap.put(mode.toLowerCase(), valueList);
                                        }
                                    }
                                }
                                List<LoginCarTypeBean> loginCarTypeBeen = carMap.get(0);
                                if (loginCarTypeBeen != null) {
                                    ToastUtils.showToast(context, loginCarTypeBeen.size() + "测试");
                                }
                                ConsTants.carMap = carMap;
                                mView.jumpActivity();
                            }
                        }

                ));
//        addSubscribe(mModel.getList()
//                .compose(RxUtil.<CarTypeBean>rxSchedulerHelper())
//                .subscribeWith(new CommonSubscriber<CarTypeBean>(mView){
//                    @Override
//                    public void onNext(CarTypeBean dataBeen) {
//                        CarTypeBean.DataBean data = dataBeen.getData();
//                        List<Map<String, String>> cod_airinflow = data.getCOD_CARDCOLOR();
//                        Map<String, String> map = cod_airinflow.get(0);
////                        String s = map.get("蓝色");
////                        String s1 = map.get("blue");
//                        Set<String> strings = map.keySet();
//                        for(String s:strings) {
//                            Log.e("sss",s + ":");
//                        }
//                        Log.e("kkkk", s1);
//                        Gson gson=new Gson();
//                        String s = gson.toJson(dataBeen);
//                        Log.e("kkkk",s);
//                        List<CarTypeBean.DataBean.CODDEVICECATEGORYBean> cod_devicecategory = dataBeen.getCOD_DEVICECATEGORY();
//                        //车牌颜色
//                        ConsTants.CarTypeList =dataBeen.getCOD_CARDCOLOR();
////                        ConsTants.MonitorType =carTypeBeen.getCOD_CARDCOLOR();
//                        //车辆种类
//                        ConsTants.CarModeList =dataBeen.getCOD_CARMODE();
//                        //号牌类型
//                        ConsTants.CarTypeNumList =dataBeen.getCOD_CARTYPE();
//                        //监测模式
//                        ConsTants.CheckModeList =dataBeen.getCOD_CHECKMODE();
//                        //车辆
//                        ConsTants.CarNumList =dataBeen.getCOD_CARDNAME();

//                        map.put("usedquality",carTypeBeen.getCOD_USEDQUALITY());
//                        map.put("kkk",carTypeBeen.getCOD_USEDQUALITY());
//                        List usedquality = map.get("usedquality");

//                        for(int i=0;i<carTypeBeen.getCOD_CARDNAME().size();i++){
//                            Log.e("car",carTypeBeen.getCOD_CARDNAME().get(i).getName()+":"+carTypeBeen.getCOD_CARDNAME().get(i).getCode());
//                        }

//                        CODCARDCOLORBean
//                        ConsTants.CarNumFirst = bean.getCphmqz();
//                        ConsTants.carMap =new_map;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                    }
//                }));

    }

    public void initLogin() {
        isRemember = SpUtil.getBoolean(context, SpUtil.RemmemberType);
        isAuto = SpUtil.getBoolean(context, SpUtil.AutoType);
        if (isRemember) {
            Map map = new HashMap();
            String userName = SpUtil.getString(context, SpUtil.UserName);
            String key = SpUtil.getString(context, SpUtil.MM);
            map.put("UserName", userName);
            map.put("Key", key);
            mView.initLoginStation(map, isRemember, isAuto);
            if (isAuto) {
                mView.startLogin();
            }
            return;
        }
        mView.initLoginStation(null, isRemember, isAuto);
    }

    public void login(String name, String key, String imei) {
//        if(checkInput(name)&&checkInput(key)){
        userName = name;
        SpUtil.putString(context, SpUtil.UserName, name);
//            SpUtil.putString(context, SpUtil.MM,encodeData(key));
        SpUtil.putString(context, SpUtil.MM, key);
        Map map = new HashMap();
        map.put("userId", name);
        map.put("loginPwd", key);
        map.put("imei", imei);
        map.put("version", getApkVersion(context));
//            map.put("version","V1.6.0");
        ConsTants.Version = getApkVersion(context);
        ConsTants.initIp(context);
        addSubscribe(mModel.getUserBean(map)
                .compose(RxUtil.<LoginBean>rxSchedulerHelper())
//                    .compose(RxUtil.<String>handleResult())
                .subscribeWith(
                        new CommonSubscriber<LoginBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(LoginBean result) {
                                if (result.getFlag().equals("1")) {
                                    SpUtil.putString(context, "stationPkid", result.getStationPkid() + "");
                                    SpUtil.putString(context, "regionCode", result.getRegionCode() + "");
                                    ConsTants.stationId = result.getStationPkid() + "";
                                    ConsTants.regionCode = result.getRegionCode() + "";
                                    ConsTants.UserId = userName;
                                    requstCarType();
//                                        mView.startActivity();
                                } else if (result.getFlag().equals("2")) {
                                    mView.stopLogin();
                                    Toast.makeText(context, "请升级应用，否则程序无法使用", Toast.LENGTH_SHORT).show();
                                    updateApk(result.getAutoUpdateUrl());
                                } else {
                                    mView.showError(result.getErr());
                                }
//                                    mView.loadImage(gankItemBeen.get(0).getUrl());
                            }
                        }
                ));
//        }
    }

    public boolean changeRememberType() {
        if (isRemember) {
            isRemember = false;
            SpUtil.putBoolean(context, SpUtil.RemmemberType, false);
            SpUtil.removeString(context, SpUtil.UserName);
            SpUtil.removeString(context, SpUtil.MM);
        } else {
            isRemember = true;
            SpUtil.putBoolean(context, SpUtil.RemmemberType, true);
        }
        return isRemember;
    }

    public boolean changeAutoType() {
        if (isAuto) {
            isAuto = false;
            SpUtil.putBoolean(context, SpUtil.AutoType, isAuto);
            return isAuto;
        } else {
            isAuto = true;
            SpUtil.putBoolean(context, SpUtil.AutoType, isAuto);
            return isAuto;
        }
    }

    public void updateApk(String url) {
        new BaseAutoUpdate().UPdateAPK(url, context, "测试更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                manager.killBackgroundProcesses(context.getPackageName());
            }
        });
    }


//    private StringCallback call = new StringCallback() {
//        @Override
//        public void onError(Call call, Exception e, int id) {
//            loadToast.error();
//        }
//
//        @Override
//        public void onResponse(String response, int id) {
//            Map map = JsonUtil.jsonToMap(response);
//            LoginResultBean loginResultBean = (LoginResultBean) JsonUtil.jsonToBean(response,LoginResultBean.class);
//            String result = (String) map.get("return_code");
//            if(result.equals("success")){
//                loadToast.success();
//                if(isRemember){
//                    SharepreferenceUtil.putString(context, SharepreferenceUtil.UserName,userName);
//                    String _Key = encodeData(key);
//                    SharepreferenceUtil.putString(context, SharepreferenceUtil.MM,_Key);
//                }
//                if(loginResultBean.getData()!=null && loginResultBean.getData().size() > 0) {
//                    if ("196".equals(loginResultBean.getData().get(0).getRole()) || "197".equals(loginResultBean.getData().get(0).getRole())) {
//                        SharepreferenceUtil.putBoolean(DataApplication.App,"isEntUser",true);
//                        Log.i("loginControl",response);
//                        ((LoginActivity) context).startOutLargeActivity(loginResultBean.getData().get(0).getEntCode());
//                    } else {
//                        SharepreferenceUtil.putBoolean(DataApplication.App,"isEntUser",false);
//                        ((LoginActivity) context).startMainActivity();
//                    }
//                }else {
//                    loadToast.error();
//                }
//
//            }else{
//                loadToast.error();
//            }
//        }
//    };


//    public static String decodeData(String encodedString) {
//
//            if (null == encodedString) {
//                return null;
//            }
//            return new String(Base64.decode(encodedString, Base64.DEFAULT));
//
//    }

    /**
     * 对给定的字符串进行base64加密操作
     */
//    public static String encodeData(String encodedString) {
//            if (null == encodedString) {
//                return null;
//            }
//            return  Base64.encodeToString(encodedString.getBytes(), Base64.DEFAULT);
//    }
    public static String getApkVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi.versionName + "";
    }


}
