package com.mapuni.caremission_ens.config;

import android.os.Environment;

/**
 * Created by yawei on 2017/3/28.
 */

public class PathManager {
//  pdf路径
    public static String Dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/PDF";

   public static String oldip = "http://220.202.61.114:8855";
//  public static String ip = "http://192.168.120.223:8855";
  public static String ip = "http://192.168.100.61:7755";
//    检修站信息
//    public static String GetRegionStation =ip+"/znzd/common/get-info!getRegionStation.action";
    public static String GetRegionStation =ip+"/st/app/jcjg.yt?xzqy=000000";
    //维修站信息
    public static String GETWXStation =ip+"/st/app/wx.yt?xzqy=000000";
    
    public static String GetPointFilter =ip+"/znzd/gis/point-filter.action";
    //检修站详情
    public static String GetCheckStationInfo =ip+"/st/app/jcjgxx.yt";
    //维修站详情
    public static String GetCheckWXStationInfo =ip+"/st/app/wxxx.yt";
    //车辆公开信息查询
    public static String GetCarInfo =ip+"/st/app/clxx.yt";
    //车辆达标信息查询
    public static String GetCarDBInfo =ip+"/st/app/dbcl.yt";
    
//    public static String GetAllFile =oldip+"/znzd/file/file-download.action";
//    public static String GetAllFile =oldip+"/znzd/file/file-download.action";
    public static String GetAllFile =ip+"/st/app/fgwj.yt";
    public static String GetAllCommonInfo =oldip+"/znzd/message/message-manage!commonInfo.action";
    public static String GetInfoContent = oldip+"/znzd/message/message-manage!getInfoContent.action";
    public static String GetNewMessageCount= oldip+"/znzd/message/message-manage!getNewMessageCount.action";
    //请求空气质量接口
    public static String getAirQuality=ip+"/st/app/kqzl.yt?";
    //请求违规操作接口
    public static String getIllegalOperation=ip+"/st/app/wgcz.yt?";
    //请求城市列表接口
    public static String getCityData=ip+"/st/app/xzqh.yt";
}
