package com.mapuni.car.mvp.listcar.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mapuni.car.R;
import com.mapuni.car.mvp.listcar.activity.CarListinfoActivity;
import com.mapuni.car.mvp.listcar.contract.CarListInfoContract;
import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.car.mvp.ywrequest.activity.CarRequestDetailActivity;
import com.mapuni.core.net.response.CarHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 28/25.
 */

public class CarListInfoPresenter extends CarListInfoContract.CarListInfoPresenter implements CarListInfoContract.OnItemClick {
    private  Map dataMap=new HashMap<>();
    private  Map map;
    private int pagerIndex = 2;
    private final int itemSize = 15;
    private boolean isEnd = false;
    private Context context;

    public void setParams(Map params){
        map = params;
    }
    @Override
    public void onStart() {
       // dataMap=(Map) ((Activity)mView.getContext()).getIntent().getSerializableExtra("map");
        context = mView.getContext();
    }

  //待检验列表
    public void requestData(){
        HashMap<String, String> map = mView.getmap();
        addSubscribe(mModel.getCarInfoList(map)
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckbean) {
//                                map.put("type", DetailPresenter.CHECK_TYPE);
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.carcheckout);
//                                mView.jumpActivity(map,keys);
//                                String carcardcolor = (String) map.get("CARCARDCOLOR");
                                List<CarCheckBean.DataBean> data = carCheckbean.getData();
                                mView.OnLoadData(carCheckbean);
                               mModel.setCarListData(data);
                               mView.updateComplete();
                            }}
                ));
    }

    //可修改列表
    public void requestAddData(){
        addSubscribe( mModel.getAddCarDetail(mView.getmap())
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckBean) {
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
//                                map.put("type", DetailPresenter.ADD_TYPE);
//                                mView.jumpActivity(map,keys);
                                List<CarCheckBean.DataBean> data = carCheckBean.getData();
                                mView.updateComplete();
                                mView.OnLoadData(carCheckBean);
                                mModel.setCarListData(data);
                            }}
                ));
    }
    //修改检测方法
    public void requestReviseData(){
        addSubscribe( mModel.getReviseCarDetail(mView.getmap())
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckBean) {
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
//                                map.put("type", DetailPresenter.ADD_TYPE);
//                                mView.jumpActivity(map,keys);
                                List<CarCheckBean.DataBean> data = carCheckBean.getData();
                                mView.updateComplete();
                                mView.OnLoadData(carCheckBean);
                                mModel.setCarListData(data);
                            }}
                ));
    }
    //跨站解锁4
    public void querylockData(){
        addSubscribe( mModel.getQueryLockCarDetail(mView.getmap())
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckBean) {
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
//                                map.put("type", DetailPresenter.ADD_TYPE);
//                                mView.jumpActivity(map,keys);
                                List<CarCheckBean.DataBean> data = carCheckBean.getData();
                                mView.updateComplete();
                                mView.OnLoadData(carCheckBean);
                                mModel.setCarListData(data);
                            }}
                ));
    }
    //查询修改检查方法4
    public void queryCheckData(){
        addSubscribe( mModel.getQueryCheckCarDetail(mView.getmap())
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckBean) {
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
//                                map.put("type", DetailPresenter.ADD_TYPE);
//                                mView.jumpActivity(map,keys);
                                List<CarCheckBean.DataBean> data = carCheckBean.getData();
                                mView.updateComplete();
                                mView.OnLoadData(carCheckBean);
                                mModel.setCarListData(data);
                            }}
                ));
    }

    //
    public void chageMethodDetail(HashMap map){
        addSubscribe( mModel.getChangeMethodDetail(map)
                .compose(RxUtil.<CarHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleCarResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(Map map) {
                                mView.jumpActivity(map);
                            }}
                ));
    }
    //
    public void queryReviseData(){
        addSubscribe( mModel.getQueryReviseCarDetail(mView.getmap())
                .compose(RxUtil.<CarCheckBean>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<CarCheckBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(CarCheckBean carCheckBean) {
//                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
//                                map.put("type", DetailPresenter.ADD_TYPE);
//                                mView.jumpActivity(map,keys);
                                List<CarCheckBean.DataBean> data = carCheckBean.getData();
                                mView.updateComplete();
                                mView.OnLoadData(carCheckBean);
                                mModel.setCarListData(data);
                            }}
                ));
    }
    public boolean isEnd(){
        return isEnd;
    }

    private void flipPager(int size){
        if(size>=itemSize){
            pagerIndex++;
        }else{
            isEnd = true;
        }
    }
//    private void initRecycleBean(){
//        Context context = mView.getContext();
//        String[] keys = mView.getContext().getResources().getStringArray(R.array.cardriverinfo);
//        List list = new ArrayList();
//        for(int i=0;i<keys.length;i++){
//            mModel.getList();
//            String name = mModel.getTitle(context,keys[i]);
//            String[] values = mModel.getValues(context,keys[i]);
//            String color = mModel.getColor(context,keys[i]);
//            String select = "";
//            if(values!=null&&values.length>1){
//                select = values[0];
//            }
////            if(dataMap.get(keys[i])!=null)
////                select =  dataMap.get(keys[i])+"";
//            if(values!=null&&values.length>1){
//                SelectValueBean bean = new SelectValueBean();
//                bean.setName(name);
//                bean.setSelects(values);
//                bean.setValue(select);
//                bean.setKey(keys[i]);
//                list.add(bean);
////            }else if(i==keys.length-2){
////                TimeValueBean bean = new TimeValueBean();
////                bean.setValue(select);
////                bean.setName(name);
////                bean.setKey(keys[i]);
////                list.add(bean);
////            }else if(i==keys.length-1){
////                PhotoBean bean = new PhotoBean();
////                String[] photoKeys = mView.getContext().getResources().getStringArray(R.array.photo_keys);
////                Map map = new HashMap();
////                for(int index=0;index<photoKeys.length;index++){
////                    String address = "";
//////                    if(dataMap.get(photoKeys[index])!=null){
//////                        address= (String) dataMap.get(photoKeys[index]);
//////                    }
////                    if(address!=null){
////                        map.put(photoKeys[index],address);
////                    }
////                }
////                bean.setPhotoAdress(map);
////                list.add(bean);
//            }else if(values!=null&&values.length==1&&values[0].equals("Edit")){
//                EditValueBean bean = new EditValueBean();
//                bean.setKey(keys[i]);
//                bean.setName(name);
//                bean.setValue(select);
//                bean.setMaxLength(mModel.getInputType(context,keys[i]));
//                list.add(bean);
//            }else{
//                TextValueBean bean = new TextValueBean();
//                bean.setValue(select);
//                bean.setName(name);
//                bean.setColor(color);
//                bean.setKey(keys[i]);
//                list.add(bean);
//            }
//        }
//       mView.onLoadMore(list);
//
//    }
    @Override
    public void Onclick(CarBean bean) {
//        HashMap map = new HashMap();
//        map.put("cphm",bean.getCphm());
//        map.put("hpzl",ConsTants.getCarTypeNum(bean.getHplb()));
//        requestAddData(map);
    }
}
