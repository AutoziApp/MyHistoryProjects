package com.mapuni.car.mvp.ywrequest.model;

import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.car.mvp.ywrequest.contract.RequestContract;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by yawei on 2017/7/3.
 */

public class RequestModel implements RequestContract.RequestModel{
    @Override
    public Flowable<RequestHttpResponse<Map>> requestCarRequestDetail(Map map) {
        return  ApiManager.getInstence().getCarService().getCarRequestDetail(map);
    }
}
