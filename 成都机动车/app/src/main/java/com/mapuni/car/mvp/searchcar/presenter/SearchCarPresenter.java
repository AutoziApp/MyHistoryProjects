package com.mapuni.car.mvp.searchcar.presenter;

import android.util.Log;

import com.mapuni.car.mvp.searchcar.contract.SearchCarContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.car.mvp.searchcar.model.CarSelectBean;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;

import java.util.Map;


public class SearchCarPresenter extends SearchCarContract.SearchCarPresenter {
    @Override
    public void onStart() {

    }

    public void requestData(Map map,CarSelectBean carSelectBean){
        addSubscribe(mModel.getCarList(map)
                .compose(RxUtil.<CarBean>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<CarBean>(mView){

                    @Override
                    public void onNext(CarBean carBean) {
                        mView.startCarListActivity(carBean,carSelectBean);
                        mView.updateComplete(carBean.getErrInfo());

                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                }));
    }
    public void requestSelect(Map map){
        addSubscribe(mModel.getCarSelect()
                .compose(RxUtil.<CarSelectBean>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<CarSelectBean>(mView){

                    @Override
                    public void onNext(CarSelectBean carBean) {
//                        Map<String, String> xszInfo = carBean.getXszInfo();
//                        String agentname = xszInfo.get("agentname");
//                        Log.e("sss",agentname);
                        if(carBean!=null&&carBean.getZxInfo()!=null&&carBean.getZxInfo().size()>0) {
                            requestData(map, carBean);
                        }else {
                            mView.updateComplete("网络状态不佳");
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                }));
    }
}
