package com.mapuni.car.mvp.lookcar.model;

import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by yawei on 2017/7/3.
 */

public class LookCarModel implements LookCarContract.MapModel{
    @Override
    public Flowable<MyHttpResponse<Map>> getCarDetail(Map map) {
       return ApiManager.getInstence().getCarService().getCarChecksList(map);
    }

    @Override
    public Flowable<CarCheckBean> getAddCarDetail(Map map) {
        return ApiManager.getInstence().getCarService().getAddCarData(map);
    }

    @Override
    public Flowable<RequestHttpResponse<Map>> requestCarRequestDetail(Map map) {
        return  ApiManager.getInstence().getCarService().getCarRequestDetail(map);
    }
}
