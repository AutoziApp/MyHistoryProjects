package com.mapuni.car.mvp.login.model;

import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.login.contract.LoginContract;
import com.mapuni.core.net.response.MyHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by yawei on 2017/7/3.
 */

public class LoginModel implements LoginContract.LoginModel {
    @Override
    public Flowable<LoginBean> getUserBean(Map map) {
        return ApiManager.getInstence().getCarService().login(map);
    }


    @Override
    public Flowable<MyHttpResponse<Map>> getList() {
        return ApiManager.getInstence().getCarService().getCarModeList();
    }
}
