package com.yutu.car.presenter;

import com.yutu.car.activity.LoginActivity;
import com.yutu.car.config.PathManager;
import com.yutu.car.utils.DesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.password;
import static android.R.attr.type;

/**
 * Created by yawei on 2017/3/28.
 */

public class NetControl {
//    检测站信息
    public void requestForAllStation(StringCallback call){
        requestNetByGet(call, PathManager.GetRegionStation);
    }

    //维修站信息
    public void requestForAllWXStation(StringCallback call){
        requestNetByGet(call, PathManager.GETWXStation);
    }
    //查看管理端信息
    public  void requestForManageMMs(int page, StringCallback callback){
        Map map=new HashMap();
        map.put("page",page+"");
        requestNetByPost(map,callback, PathManager.GetManageMms);
    }
    //查看管理端信息详情
    public void requestForManageMmsDetails(String id,StringCallback callback){
        Map map=new HashMap();
        map.put("pkid",id);
        requestNetByPost(map,callback, PathManager.GetManageMmsDetails);
    }


//    查询监测站详情
    public void requestForStationSearch(String id,StringCallback call){
        Map map = new HashMap();
        map.put("jgbh",id);
        requestNetByPost(map,call, PathManager.GetCheckStationInfo);
    }

    //    查询维修监测站详情
    public void requestForWXStationSearch(String id,StringCallback call){
        Map map = new HashMap();
        map.put("jgbh",id);
        requestNetByPost(map,call, PathManager.GetCheckWXStationInfo);
    }
    //查询监测站详情
    public void requestForCheckLine(String id,StringCallback call){
        Map map = new HashMap();
        map.put("checkLineId",id);
        requestNetByPost(map,call, PathManager.getCheckLine);
    }
    //查询车辆公开信息
    public void requestForCarInfo(String carNum,String vin,StringCallback call){
        String url= PathManager.GetCarInfo+"?"+"cphm="+carNum+"&vin="+vin;
        requestNetByGet(call, url);
    }
    //查询车辆达标信息
    public void requestForCarDBInfo(String carNum,String vin,StringCallback call){
        String url= PathManager.GetCarDBInfo+"?"+"cphm="+carNum+"&vin="+vin;
        requestNetByGet(call, url);
    }
    
    
    
    //车辆信息
    public void  requestForCarManage(int page,StringCallback call){
        Map map=new HashMap();
        map.put("page",page+"");
        requestNetByPost(map,call,PathManager.GetTestUri);
    }
    //车辆信息查询
    public void  requestSearchForCarManage(String carNum,String vinNum,StringCallback call){
//        Map map=new HashMap();
//        map.put("carCardNumber",carNum);
//        map.put("vin",vinNum);
        String url=PathManager.GetSearchCarMessageUri+"carCardNumber="+carNum+"&vin="+vinNum;
        requestNetByGet(call,url);
    }
    //检测站名称
    public  void requestCheckStationName(StringCallback call){
        requestNetByGet(call,PathManager.GetCheckStationName);
    }
    //检测数量
    public void requestForCheckCount(String startTime,String endTime,String id,String pkid,StringCallback call){
        Map map=new HashMap();
        map.put("startDate",startTime);
        map.put("endDate",endTime);
        map.put("type",id);
        map.put("stationId",pkid);
        requestNetByPost(map,call, PathManager.GetCheckCountInforUri);
    }
    //检测检测合格率
    public void requestForCheckPassPerCount(String startTime,String endTime,String id,String pkid,StringCallback call){
        Map map=new HashMap();
        map.put("startDate",startTime);
        map.put("endDate",endTime);
        map.put("type",id);
        map.put("stationId",pkid);
        requestNetByPost(map,call, PathManager.GetCheckCountInforUri);
    }
    //首检合格率
    public void requestForFirstCheckPassPerCount(String startTime,String endTime,String id,String pkid,StringCallback call){
        Map map=new HashMap();
        map.put("startDate",startTime);
        map.put("endDate",endTime);
        map.put("type",id);
        map.put("stationId",pkid);
        requestNetByPost(map,call, PathManager.GetCheckCountInforUri);
    }
    //复检合格率
    public void requestForSecondCheckPassPerCount(String startTime,String endTime,String id,String pkid,StringCallback call){
        Map map=new HashMap();
        map.put("startDate",startTime);
        map.put("endDate",endTime);
        map.put("type",id);
        map.put("stationId",pkid);
        requestNetByPost(map,call, PathManager.GetCheckCountInforUri);
    }
    //环保监测报告核发列表
    public void requestForEnPrList(int page,String startTime,String endTime,StringCallback callback){
        Map map=new HashMap();
        map.put("page",page+"");
        map.put("startCheckTime",startTime);
        map.put("endCheckTime",endTime);
        requestNetByPost(map,callback, PathManager.GetEnvironmentalCheckList);
    }

    //环保监测报告核发列表详情
    public void requestForEnPrListDetials(String jgbh ,StringCallback callback
    ){
        Map map=new HashMap();
        map.put("jgbh",jgbh);
        requestNetByPost(map,callback, PathManager.GetEnvironmentalCheckListDetails);
    }
    //车辆详细信息
    public void requestForCarManageinfo(String id,StringCallback call){
        Map map = new HashMap();
        map.put("pkid",id);
        requestNetByPost(map,call, PathManager.GetCareMaageInforUri);
    }
//    public void requestForCheckoutStation( String checkStation, String noPassReason, String userId, String pkid, StringCallback callback){
//        Map map=new HashMap();
//        if(noPassReason==null)
//            noPassReason=" ";
//        String s = null;
//        String str = null;
//        try {
//            s = DesUtils.encryptDES(noPassReason,"mapuni12");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        map.put("checkState",checkStation);
//        map.put("noPassReason",s);
//        map.put("userId",userId);
//        map.put("pkid",pkid);
//        requestNetByPost(map,callback, PathManager.GetCheckoutStation);
//    }

    public void requestForCheckoutStation( String jgbh, String nr, String jg, StringCallback callback){
        Map map=new HashMap();
        if(nr==null)
            nr=" ";
        map.put("jgbh",jgbh);
        map.put("nr",nr);
        map.put("jg",jg);
        requestNetByPost(map,callback, PathManager.GetCheckoutStation);
    }
    public void requestForRowCheckNoQualified(String content,String pkit,String checkResultid,String dataTime,StringCallback callback){
        Map map=new HashMap();
        String s = "";
        try {
            s = DesUtils.encryptDES(content,"mapuni12");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("content",s);
        map.put("carID",pkit);
        map.put("checkDate",dataTime);
        map.put("checkResultID",checkResultid);
        map.put("optionid", LoginActivity.UserName+System.currentTimeMillis());
        requestNetByPost(map,callback, PathManager.GetRowCheckNoQualified);

    }
    //    地图信息
    public void requestForGis(StringCallback call){
        requestNetByGet(call, PathManager.GetPointFilter);
    }
//    技术文档
    public void requestForDoc(StringCallback call){
        requestNetByGet(call, PathManager.GetAllFile);
    }
    //    检测线列表
    public void requestForCheckStationLine(StringCallback call){
        requestNetByGet(call, PathManager.getCheckStationLine);
    }
    //登录
    public void  requestForUsernameAndPassword(String userName, String password, String type, String bbh, StringCallback callback){
        
        
        String url= PathManager.GetUsernameAndPassword+"?userid="+userName+"&password="+ password
                +"&type="+type+"&bbh="+bbh;
        requestNetByGet(callback, url);

//        requestNetByPost(map,callback, PathManager.GetUsernameAndPassword);

    }
//    文档下载
    public  void downFile(String url,FileCallBack call,int id){
       requestNetByGet(call,url,id);
    }
    private void requestNetByPost(Map params,Callback call,String uri){
        OkHttpUtils.post().url(uri).params(params).build().execute(call);
    }
    private void requestNetByGet(Callback call,String uri){
        OkHttpUtils.get().url(uri).build().execute(call);

    }
    private void requestNetByGet(FileCallBack call, String uri, int i){
        OkHttpUtils.get().url(uri).id(i).build().execute(call);
    }
    //获取碳氧化物核算接口
    public static void requestCOtotal(String carType,Callback callback) {
        
        String url=PathManager.getCOtotal+"carType="+carType;
        OkHttpUtils.get().url(url).build().execute(callback);
    }
    //请求空气质量
    public void requestAirQuality(String cityName, Callback call) {
        String url=PathManager.getAirQuality+"mc="+cityName;
        requestNetByGet(call,url);
    }
    //请求违规操作数据
    public void requestIllegalOperation(Callback call, int page, String rows) {
        String url=PathManager.getIllegalOperation+"page="+page+"&rows="+rows;
        requestNetByGet(call,url);
    }

    //请求城市列表
    public void requestCityData(Callback call) {
        String url=PathManager.getCityData;
        requestNetByGet(call,url);
    }
}
