package com.mapuni.car.mvp.listcar.contract;

import android.content.Context;

import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.core.base.CoreBaseModel;
import com.mapuni.core.base.CoreBasePresenter;
import com.mapuni.core.base.CoreBaseView;
import com.mapuni.core.net.response.CarHttpResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by lybin on 2017/6/27.
 */

public interface CarListInfoContract {
    interface CarListInfoModel extends CoreBaseModel {
        Flowable<CarCheckBean> getCarInfoList(Map map);
        Flowable<CarCheckBean> getAddCarDetail(Map map);
        Flowable<CarCheckBean> getReviseCarDetail(Map map);
        Flowable<CarCheckBean> getQueryReviseCarDetail(Map map);
        Flowable<CarCheckBean> getQueryLockCarDetail(Map map);
        Flowable<CarCheckBean>  getQueryCheckCarDetail(Map map);
        Flowable<CarHttpResponse<Map>> getChangeMethodDetail(Map map);
//        Flowable<CarTypeBean> getList();
        List getCarListData();
        void setCarListData(List<CarCheckBean.DataBean> data);

        String getTitle(Context context, String s);
        String[] getValues(Context context,String s);
        String[] getKeys(Context context);
        String getColor(Context context,String s);
        String getInputType(Context context,String s);
        String getTestData(Context context, String s);
    }
    interface CarListInfoAvtivity extends CoreBaseView{
        void jumpActivity(Map data);
        void onLoadMore(List list);
        void OnLoadData(CarCheckBean carCheckbean);
        void updateComplete();
        LinkedHashMap<String,String> getmap();
    }
    interface OnItemClick{
        void Onclick(CarBean bean);
    }
    abstract  class CarListInfoPresenter extends CoreBasePresenter<CarListInfoContract.CarListInfoModel,CarListInfoAvtivity> {

    }
}
