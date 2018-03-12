package com.mapuni.car.mvp.lookcar.presenter;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.presenter.DetailPresenter;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;

import java.util.HashMap;
import java.util.Map;


public class LookCarPresenter extends LookCarContract.MapPresenter{
    @Override
    public void onStart() {

    }
    //查验
    public void requestData(Map map){
        map.put("userId", ConsTants.UserId);
        map.put("regionCode",ConsTants.regionCode);
        addSubscribe( mModel.getCarDetail(map)
                .compose(RxUtil.<MyHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(Map map) {
                                map.put("type", DetailPresenter.CHECK_TYPE);
                                String[] keys = mView.getContext().getResources().getStringArray(R.array.carcheckout);
                                mView.jumpActivity(map,keys);
                            }}
                ));
    }
    //补录
    public void requestAddData(Map map){
//        map.put("userId",ConsTants.UserId);
//        map.put("regionCode",ConsTants.regionCode);
//        addSubscribe( mModel.getAddCarDetail(map)
//                .compose(RxUtil.<MyHttpResponse<Map>>rxSchedulerHelper())
//                .compose(RxUtil.<Map>handleResult())
//                .subscribeWith(
//                        new CommonSubscriber<Map>(mView) {
//                            @Override
//                            public void onError(Throwable e) {
//                                super.onError(e);
//                            }
//                            @Override
//                            public void onNext(Map map) {
                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
                                map.put("type", DetailPresenter.ADD_TYPE);
                                mView.jumpActivity(map,keys);
//                            }}
//                ));
    }



}
