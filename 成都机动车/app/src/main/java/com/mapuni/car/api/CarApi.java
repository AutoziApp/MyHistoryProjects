package com.mapuni.car.api;

import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.login.model.LoginBean;
import com.mapuni.car.mvp.main.model.ConfigBean;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarTypeBean;
import com.mapuni.car.mvp.searchcar.model.CarSearchDetailBean;
import com.mapuni.car.mvp.searchcar.model.CarSelectBean;
import com.mapuni.core.net.response.CarHttpResponse;
import com.mapuni.core.net.response.ChangeResponse;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface CarApi {
    //用户登录
    @POST("/jdcapp/newandroid/login.action")
    Flowable<LoginBean> login(@QueryMap Map<String, Object> map);
    //下拉列表信息
    @GET("/jdcapp/newandroid/add-waitcar!getCodeCache.action")
    Flowable<MyHttpResponse<Map>> getCarModeList();
    @GET("/jdcapp/newandroid/add-waitcar!getZdxx.action")
    Flowable<CarSelectBean> getCarSelect();
    //车辆查询
    @POST("/jdcapp/newandroid/add-waitcar!getCarInfo.action")
    Flowable<CarBean> getCarList(@QueryMap Map<String, Object> map);
    //外观检测
    @POST("/jdcapp/newandroid/get-data!getDcylb.action")
    Flowable<CarCheckBean> getCarCheckList(@QueryMap Map<String, Object> map);

    //外观检测
    @POST("/jdcapp/newandroid/get-data!getDcylb.action")
    Flowable<MyHttpResponse<Map>> getCarChecksList(@QueryMap Map<String, Object> map);

    //配置查询
    @POST("/jc/wgcy/getConfig.yt")
    Flowable<MyHttpResponse<ConfigBean>> getConfig(@QueryMap Map<String, Object> map);

    //查验
    @POST("/jc/wgcy/getWaitCheckCar.yt")
    Flowable<MyHttpResponse<Map>> getCarData(@QueryMap Map<String, Object> map);

    //可修改列表
    @POST("/jdcapp/newandroid/get-data!getKxglb.action")
    Flowable<CarCheckBean> getAddCarData(@QueryMap Map<String, Object> map);

    //修改检测方法
    @POST("/jdcapp/newandroid/get-data!getXgfflb.action")
    Flowable<CarCheckBean> getReviseCarData(@QueryMap Map<String, Object> map);

    //查询修改车辆信息
    @POST("/jdcapp/newandroid/get-data!getXgclxxsqlb.action")
    Flowable<CarCheckBean> getQueryReviseCarData(@QueryMap Map<String, Object> map);

    //查询跨站监测解锁
    @POST("/jdcapp/newandroid/get-data!getClfckzjssqlb.action")
    Flowable<CarCheckBean> getQueryLockCarData(@QueryMap Map<String, Object> map);

    //查询修改检测方法
    @POST("/jdcapp/newandroid/get-data!getXgffsqlb.action")
    Flowable<CarCheckBean> getQueryCheckCarData(@QueryMap Map<String, Object> map);

    //保存检测方法
    @POST("/jdcapp/newandroid/apply-change-method.action")
    Flowable<RequestHttpResponse> commitMethod(@Body RequestBody Body);


    //修改检测方法
//    @POST("jdczx/newandroid/get-data!getXgfflb.action")
//    Flowable<CarCheckBean> getReviseCarData(@QueryMap Map<String,Object> map);
    //带查验列表详情
    @POST("/jdcapp/newandroid/get-data!getDcyclxq.action")
    Flowable<CarHttpResponse<Map>> getChecksDetail(@QueryMap Map<String, Object> map);

    //可修改列表详情
//    @POST("jdczx/newandroid/get-data!getYcyclxq.action")
//    Flowable<MyHttpResponse<Map>> getReciverDetail(@QueryMap Map<String,Object> map);
//    @POST("/jdczx/newandroid/car-aspect-check.action")
    @POST("/jdcapp/newandroid/get-data!getYcyclxq.action")
    Flowable<CarHttpResponse<Map>> getReciverDetail(@QueryMap Map<String, Object> map);

    @GET("/jdcapp/android/add-waitcar!getCodeCache.action")
    Flowable<CarTypeBean> getCarTypeList();

    //查询修改车辆信息详情
    @GET("/jdcapp/newandroid/get-data!getXgclxxsqxq.action")
    Flowable<CarSearchDetailBean> getChangeCarInfoDetail(@QueryMap Map<String, Object> map);

    //修改监测方法申请详情
    @GET("/jdcapp/newandroid/get-data!getXgffsqxq.action")
    Flowable<CarSearchDetailBean> getJcffsqDetail(@QueryMap Map<String, Object> map);

    //车辆复测跨站解锁详情
    @GET("/jdcapp/newandroid/get-data!getclfckzjssqxq.action")
    Flowable<CarSearchDetailBean> getKzjssqDetail(@QueryMap Map<String, Object> map);

    //业务申请跨站检测解锁详情
    @GET("/jdcapp/newandroid/apply-unlock-car!getCheckInfo.action")
    Flowable<RequestHttpResponse<Map>> getCarRequestDetail(@QueryMap Map<String, Object> map);

    //修改检测方法详情
    @POST("/jdcapp/newandroid/get-data!getXgffxq.action")
    Flowable<CarHttpResponse<Map>> getChangeMethodDetail(@QueryMap Map<String, Object> map);

    //业务申请跨站检测解锁修改
    @GET("/jdcapp/newandroid!ApplyChangeMethod.action")
    Flowable<CarSearchDetailBean> changeCarRequestDetail(@QueryMap Map<String, Object> map);

    //保存跨站解锁数据
    @POST("/jdcapp/newandroid/apply-unlock-car.action")
    Flowable<RequestHttpResponse> commitUnlockData(@Body RequestBody Body);


    //车辆信息保存
    @POST("/jdcapp/newandroid/add-waitcar!saveWaitCar.action")
    Flowable<MyHttpResponse> commitData(@Body RequestBody Body);

    //车辆信息修改保存
    @POST("/jdcapp/newandroid/add-waitcar!applyChangeCar.action")
    Flowable<MyHttpResponse> commitReciverData(@Body RequestBody Body);

    //待检车辆提交
    @POST("/jdcapp/newandroid/car-aspect-check!aspectInfoSave.action")
    Flowable<ChangeResponse> commitCheckDetail(@Body RequestBody Body);

    //可修改列表详情信息保存
    @POST("/jdcapp/newandroid/car-aspect-check!updateWgcyImgs.action")
    Flowable<ChangeResponse> commitChageData(@Body RequestBody Body);
}
