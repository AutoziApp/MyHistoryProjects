package com.mapuni.car.mvp.ywrequest.presenter;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.presenter.DetailPresenter;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.car.mvp.ywrequest.contract.RequestContract;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;

import java.util.HashMap;
import java.util.Map;


public class RequestPresenter extends RequestContract.RequestPresenter{
    @Override
    public void onStart() {

    }

    public void getCarInfo(Map dataMap){
        HashMap map=new HashMap<>();
        map.put("carCardNumber",dataMap.get("cphm"));
        map.put("carCardColor",dataMap.get("hpzl"));
        map.put("VIN",dataMap.get("vin"));
        addSubscribe(mModel.requestCarRequestDetail(map)
                .compose(RxUtil.<RequestHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleRequestResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(Map  map) {
                                String color = (String) map.get("carCardColor");
                                String colorName = ConsTants.getCarColorMap().get(color);
                                map.put("carCardColor",colorName);
                                mView.jumpActivity(map);
//                                initRecycleBean(map);
                            }}
                ));
    }

}
