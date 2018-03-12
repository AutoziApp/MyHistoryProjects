package com.mapuni.car.mvp.lookcar.contract;

import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.core.base.CoreBaseModel;
import com.mapuni.core.base.CoreBasePresenter;
import com.mapuni.core.base.CoreBaseView;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.net.response.RequestHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by lybin on 2017/6/27.
 */

public interface LookCarContract {
    interface MapModel extends CoreBaseModel {
        Flowable<MyHttpResponse<Map>> getCarDetail(Map map);

        Flowable<CarCheckBean> getAddCarDetail(Map map);

        Flowable<RequestHttpResponse<Map>> requestCarRequestDetail(Map map);
    }

    interface MapFragment extends CoreBaseView {
        void jumpActivity(Map data, String[] keys);
    }


    abstract class MapPresenter extends CoreBasePresenter<MapModel, MapFragment> {

    }
}
