package com.yutu.car.config;

import android.os.Environment;

/**
 * Created by yawei on 2017/3/28.
 */

public class PathManager {
    //  pdf路径
    public static String Dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
    public static String ip = "http://220.202.61.114:8855";
    public static String newIp = "http://192.168.120.223:8855";
    //    检测站信息
    public static String GetRegionStation = newIp + "/st/app/jcjg.yt?xzqy=000000";
    //维修站信息
    public static String GETWXStation = newIp + "/st/app/wx.yt?xzqy=000000";
    public static String GetPointFilter = ip + "/znzd/gis/point-filter.action";
    //检修站详情
    public static String GetCheckStationInfo = newIp + "/st/app/jcjgxx.yt";
    //维修站详情
    public static String GetCheckWXStationInfo = newIp + "/st/app/wxxx.yt";
    //车辆公开信息查询
    public static String GetCarInfo = newIp + "/st/app/clxx.yt";
    //车辆达标信息查询
    public static String GetCarDBInfo = newIp + "/st/app/dbcl.yt";
    //获取碳氧化物接口
    public static String getCOtotal = newIp + "/st/app/pwhs.yt?";
    //请求违规操作接口
    public static String getIllegalOperation = newIp + "/st/app/wgcz.yt?";
    //请求空气质量接口
    public static String getAirQuality = newIp + "/st/app/kqzl.yt?";
    //请求城市列表接口
    public static String getCityData = newIp + "/st/app/xzqh.yt";
    public static String GetAllFile = newIp + "/st/app/fgwj.yt";
    public static String GetTestUri = ip + "/znzd/manage/car-info.action";
    public static String GetSearchCarMessageUri = ip + "/znzd/manage/car-info!searchCar.action?";
    public static String GetCareMaageInforUri = ip + "/znzd/manage/car-info!carDetailInfo.action";
    public static String GetCheckStationName = ip + "/znzd/manage/car-info!getStations.action";
    public static String GetCheckCountInforUri = ip + "/znzd/manage/check-station!testStatistic.action";
    public static String GetManageMms = ip + "/znzd/message/message-manage!manageInfo.action";
    public static String GetManageMmsDetails = ip + "/znzd/message/message-manage!getInfoContent.action";
    public static String GetEnvironmentalCheckList = newIp + "/st/app/jcsh.yt";
    public static String GetEnvironmentalCheckListDetails = newIp + "/st/app/jcshxx.yt";
    public static String GetCheckoutStation = newIp + "/st/app/jcshjg.yt";
    public static String GetRowCheckNoQualified = ip + "/znzd/manage/road-check!handlerOption.action";
    public static String GetUsernameAndPassword = newIp + "/st/app/LoginOutInfo.yt";
    public static String getCheckStationLine = ip + "/znzd/manage/check-line!getCheckStationLine.action";
    public static String getCheckLine = ip + "/znzd/manage/check-line.action";


    public static void setPathUrl(String ip){

        newIp =ip;
        //    检测站信息
        GetRegionStation = newIp + "/st/app/jcjg.yt?xzqy=000000";
        //维修站信息
        GETWXStation = newIp + "/st/app/wx.yt?xzqy=000000";
        //检修站详情
        GetCheckStationInfo = newIp + "/st/app/jcjgxx.yt";
        //维修站详情
        GetCheckWXStationInfo = newIp + "/st/app/wxxx.yt";
        //车辆公开信息查询
        GetCarInfo = newIp + "/st/app/clxx.yt";
        //车辆达标信息查询
        GetCarDBInfo = newIp + "/st/app/dbcl.yt";
        //获取碳氧化物接口
        getCOtotal = newIp + "/st/app/pwhs.yt?";
        //请求违规操作接口
        getIllegalOperation = newIp + "/st/app/wgcz.yt?";
        //请求空气质量接口
        getAirQuality = newIp + "/st/app/kqzl.yt?";
        //请求城市列表接口
        getCityData = newIp + "/st/app/xzqh.yt";
        GetAllFile = newIp + "/st/app/fgwj.yt";
        GetEnvironmentalCheckList = newIp + "/st/app/jcsh.yt";
        GetEnvironmentalCheckListDetails = newIp + "/st/app/jcshxx.yt";
        GetCheckoutStation = newIp + "/st/app/jcshjg.yt";
        GetUsernameAndPassword = newIp + "/st/app/LoginOutInfo.yt";
    }
}
