package com.mapuni.car.mvp.searchcar.gotoview.contract;

import com.mapuni.car.mvp.searchcar.contract.SearchCarContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.core.base.CoreBasePresenter;
import com.mapuni.core.base.CoreBaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by lybin on 2017/6/27.
 */

public interface CarListContract {

    interface CarListAvtivity extends CoreBaseView{
        void jumpActivity(Map data, String[] keys);
        void onLoadMore(List list);
        void updateComplete(String errInfo);
    }
    interface OnItemClick{
        void Onclick(CarBean bean);

    }
    abstract  class CarListPresenter extends CoreBasePresenter<SearchCarContract.SearchCarModel,CarListAvtivity> {

    }
}
