package com.mapuni.caremission_ens.presenter;

import android.content.pm.LauncherApps;
import android.util.Log;

import com.mapuni.caremission_ens.config.PathManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Created by yawei on 2017/3/28.
 */

public class NetControl {
//    检修站信息
    public void requestForAllJXStation(StringCallback call){
        requestNetByGet(call, PathManager.GetRegionStation);
    }
    //维修站信息
    public void requestForAllWXStation(StringCallback call){
        requestNetByGet(call, PathManager.GETWXStation);
    }
    
    
//    查询检修监测站详情
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
//    查询车辆公开信息
    public void requestForCarInfo(String carNum,String vin,StringCallback call){
        String url= PathManager.GetCarInfo+"?"+"cphm="+carNum+"&vin="+vin;
        requestNetByGet(call, url);
    }
    //查询车辆达标信息
    public void requestForCarDBInfo(String carNum,String vin,StringCallback call){
        String url= PathManager.GetCarDBInfo+"?"+"cphm="+carNum+"&vin="+vin;
        requestNetByGet(call, url);
    }
    
    //    地图信息
    public void requestForGis(StringCallback call){
        requestNetByGet(call, PathManager.GetPointFilter);
    }
//    技术文档
    public void requestForDoc(StringCallback call){
        requestNetByGet(call, PathManager.GetAllFile);
    }
//    文档下载
    public  void downFile(String url,FileCallBack call,int id){
       requestNetByGet(call,url,id);
    }
//    通知列表
    public void allNews(int page,StringCallback call){
        Map map = new HashMap();
        map.put("page",page+"");
        map.put("rows",10+"");
        requestNetByPost(map,call, PathManager.GetAllCommonInfo);
    }
//    通知内容
    public void newsContent(String pkid,StringCallback call){
        Map map = new HashMap();
        map.put("pkid",pkid);
        requestNetByPost(map,call, PathManager.GetInfoContent);
    }
//   更新消息数量
    public void messageCount(String time,StringCallback call){
        Map map = new HashMap();
        map.put("time",time);
        requestNetByPost(map,call, PathManager.GetNewMessageCount);
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
