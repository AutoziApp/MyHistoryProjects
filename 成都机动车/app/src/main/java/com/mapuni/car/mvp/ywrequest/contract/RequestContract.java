package com.mapuni.car.mvp.ywrequest.contract;

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

public interface RequestContract {
    interface RequestModel extends CoreBaseModel{
        Flowable<RequestHttpResponse<Map>> requestCarRequestDetail(Map map);
    }
    interface RequestFragment extends CoreBaseView{
         void jumpActivity(Map data);
    }


    abstract  class RequestPresenter extends CoreBasePresenter<RequestModel,RequestFragment> {

    }
}
