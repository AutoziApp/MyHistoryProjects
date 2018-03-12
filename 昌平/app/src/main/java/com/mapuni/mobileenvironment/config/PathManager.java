package com.mapuni.mobileenvironment.config;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.mapuni.mobileenvironment.utils.Net;
import com.mapuni.mobileenvironment.utils.SharepreferenceUtil;

public class PathManager {
    //http://192.168.15.66:8006/WebService/DataAppService.asmx?op=GetEnterList
//    武艳雪ip
//    public  static final String SERVICES_PATH = "http://192.168.15.66:8006";
//public  static final String SERVICES_PATH = "http://192.168.15.138:8006";
    public static  String DEFAULT_BASE_SERVICE = "http://218.246.81.181";
    public static  String BASE_SERVICE;
    //    公司ip
    public static  String SERVICES_PATH = BASE_SERVICE+":8115";//用于在线监测
    public static  String SERVICES_PATH1 = BASE_SERVICE+":8113";//用于油烟监测

    //    http://www.micromap.com.cn:8080/epservice/v1.2/api/weather/getAll/%E6%98%8C%E5%B9%B3/0/0?token=YFJYeRKQouE0bWylekXl
    public static  String WEATHER_PATH = "http://www.micromap.com.cn:8080/epservice/v1.2/api/weather/getAll";
    public static  String WEATHER_TOKEN = "?token=YFJYeRKQouE0bWylekXl";
    public static  String WEATHER_CHANGPING = WEATHER_PATH + "/昌平/0/0" + WEATHER_TOKEN;
    public static  String WEATHER_CHANGPING_AQI = BASE_SERVICE+":8120/BJCPAir/getjsdsdata";
    public static  String LOGIN_PATH = SERVICES_PATH + "";
    //获取企业列表
//    public  static final String GetEnterList =SERVICES_PATH+"/WebService/DataAppService.asmx?op=GetEnterList" ;
    public static  String GetEnterList = SERVICES_PATH + "/WebService/DataAppService.asmx/GetEnterList";
    //获取最新超标企业
    public static  String GetOverEnterList = SERVICES_PATH + "/WebService/DataAppService.asmx/NewWryInfo";
    //获取污染源数据
    public static  String DataApp = SERVICES_PATH + "/WebService/DataAppService.asmx/DataAppByAll";
    //默认污染源数据
    public static  String FirDataApp = SERVICES_PATH + "/WebService/DataAppService.asmx/DataAppByEntCode";
    //    地图中污染源数据
    public static  String MapOverEnterList = SERVICES_PATH + "/WebService/DataAppService.asmx/AllWryInfo";
    //    地图中油烟数据
    public static  String MapYanList = SERVICES_PATH1 + "/WebService/DataAppService.asmx/AllOutputInfo";
    //获取油烟监控列表
//    public  static final String GetEnterList =SERVICES_PATH+"/WebService/DataAppService.asmx?op=GetEnterList" ;
    public static  String GetEnterList1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/GetEnterList";
    //获取最新超标油烟
    public static  String GetOverEnterList1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/NewWryInfo";
    //获取油烟数据
    public static  String DataApp1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/DataAppByAll";
    //默认油烟数据
    public static  String FirDataApp1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/DataAppByEntCode";
    //网络热力图图片
    public static String NetHeatMap="http://218.246.81.181:8119/file/img/2017-03/2017-03-29T21_00_00.png";
    //获取空气环境监测点位及基本信息接口
    public static  String GetSites = "http://218.246.81.181:8120/BJCPAir/yutu/getsites";
    //获取点位小时或日数据接口（按时间检索）
    public static  String GetSiteDataByTime = "http://218.246.81.181:8120/BJCPAir/yutu/getSiteDataByTime";
    //获取单个监测点位一段时间的数据接口
    public static  String SingleHistory = "http://218.246.81.181:8120/BJCPAir/yutu/singleHistory";
    //获取街镇数据
    public static  String GetStreets = "http://218.246.81.181:8120/BJCPAir/yutu/getStreets";
    //根据设备id获得一段时间日数据接口
    public static  String GetDayDataById = "http://218.246.81.181:8120/BJCPAir/yutu/getDayDataById";
    //所有站点的基本信息+最新小时监测数据
    public static  String GetDataHour = "http://218.246.81.181:8120/BJCPAir/yutu/getDataHour";

    /**
     * 系统部署文件的跟目录mobileemergency文件夹路径
     * /storage/emulated/0/mobilegrid/data/MobileEnforcement.db
     */
            public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/mobilegrid/";
    /**
     * 系统部署文件MobileEnforcement文件夹路径
     */
    public static final String SDCARD_RASK_DATA_PATH = BASE_PATH + "data/";
    /**
     * 手机端下载数据的临时文件目录，即DataTemp
     */
    public static final String SDCARD_TEMP_PATH = BASE_PATH + "DataTemp/";
    /**
     * 手机端config文件路径
     */
    public static final String SDCARD_CONFIG_LOCAL_PATH = SDCARD_RASK_DATA_PATH
            + "data/config.xml";
    /**
     * 地图文件
     */
    public static final String SDCARD_MAP_PATH = BASE_PATH + "Map/";

    /**
     * 手机端链接WebService的路径
     */
    public static final String WEBSERVICE_URL = "/MobileWebservice/mobileWebservice.asmx";
    /**
     * 手机端数据库MobileEmergency.db文件存放路径
     */
    public static final String SDCARD_DB_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "/MobileEnforcement.db";

    /**
     * 手机端日志文件目录
     */
    public static final String SDCARD_LOG_LOCAL_PATH = SDCARD_RASK_DATA_PATH + "data/log";

    /**
     * 手机文档存放
     */
    public static final String SDCARD_LOG_DOC_FILE_PATH = BASE_PATH + "Doc";

    /**
     * 命名空间
     */
    public static final String NAMESPACE = "http://tempuri.org/";


    /**
     * 异常记录文件
     */
    public static String EXCEPTION_PATH = BASE_PATH + "Exception";

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
    /**
     * 后台存放apk更新的路径
     */
    public static final String APK_DOWN_URL = "/MobileWebservice/autoupdate/app/MobileEnforcement.apk";

    /**
     * 后台verson.xml文件路径
     */
    public static final String APK_CODE_URL = "/MobileWebservice/autoupdate/app/verson.xml";// 版本信息URL后缀

    /**
     * webservice基本url
     */
    public static String WebServiceBaseAPNUrl = "http://192.168.4.69:6111";

    public static String WebServiceBaseUrl = "http://192.168.0.99.4567.69:6111";
    /**
     * webservice方法所在位置url
     */
    public static String WebServiceMethodsUrl = WebServiceBaseUrl + "/WebService1.asmx";
    /**
     * webservice查询url
     */
    public static String WebServiceSelectUrl = WebServiceMethodsUrl + "/ExecuteSQLDataSet";

    /**
     * webservice附件上传的url
     */
    public final static String WebServicePicUpLoad = "/InsertBulletionDetailInfor";
    //获取附件地址

    public static void setApnIp(String ip) {
        WebServiceBaseAPNUrl = ip;
//        setIp(ip);
    }
    public static void initBaseIp(Context context){
        BASE_SERVICE = SharepreferenceUtil.getBaseIp(context);
        if(BASE_SERVICE==null||BASE_SERVICE.equals("")){
            BASE_SERVICE = DEFAULT_BASE_SERVICE;
        }
        SERVICES_PATH = BASE_SERVICE+":8115";
        SERVICES_PATH1 = BASE_SERVICE+":8113";
        WEATHER_CHANGPING_AQI = BASE_SERVICE+":8120/BJCPAir/getjsdsdata";
        GetEnterList = SERVICES_PATH+"/WebService/DataAppService.asmx/GetEnterList";
        GetOverEnterList = SERVICES_PATH + "/WebService/DataAppService.asmx/NewWryInfo";
        DataApp = SERVICES_PATH + "/WebService/DataAppService.asmx/DataAppByAll";
        FirDataApp = SERVICES_PATH + "/WebService/DataAppService.asmx/DataAppByEntCode";
        MapOverEnterList = SERVICES_PATH + "/WebService/DataAppService.asmx/AllWryInfo";
        GetEnterList1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/GetEnterList";
        GetOverEnterList1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/NewWryInfo";
        DataApp1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/DataAppByAll";
        FirDataApp1 = SERVICES_PATH1 + "/WebService/DataAppService.asmx/DataAppByEntCode";
        MapYanList = SERVICES_PATH1 + "/WebService/DataAppService.asmx/AllOutputInfo";
    }
    public static void setIp(final Context context,final String ip) {
        new AsyncTask<String,Object,Boolean>(){
            @Override
            protected Boolean doInBackground(String... params) {
                return Net.checkURL(params[0]);
            }
            @Override
            protected void onPostExecute(Boolean bool) {
                super.onPostExecute(bool);
                if(bool){
                    SharepreferenceUtil.setBaseIp(context,ip);
                    BASE_SERVICE = ip;
                    initBaseIp(context);
                    Toast.makeText(context,"地址已变更",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context,"地址不通,请输入正确地址",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(ip);
//        WebServiceMethodsUrl = WebServiceBaseUrl + "/WebService1.asmx";
//        WebServiceSelectUrl = WebServiceMethodsUrl + "/ExecuteSQLDataSet";
    }
}

