package com.mapuni.car.mvp.detailcar.contract;

import android.content.Context;

import com.mapuni.car.mvp.searchcar.model.CarSearchDetailBean;
import com.mapuni.core.base.CoreBaseModel;
import com.mapuni.core.base.CoreBasePresenter;
import com.mapuni.core.base.CoreBaseView;
import com.mapuni.core.net.response.CarHttpResponse;
import com.mapuni.core.net.response.ChangeResponse;
import com.mapuni.core.net.response.MyHttpResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * Created by lybin on 2017/6/27.
 */

public interface DetailContract {
    interface DetailModel extends CoreBaseModel {
        Flowable<ChangeResponse> chageDetail(RequestBody body);

        Flowable<CarSearchDetailBean> getCarDetailData(Map map, int type);

        Flowable<MyHttpResponse> commitLookDetail(RequestBody body);

        Flowable<ChangeResponse> commitCheckDetail(RequestBody body);

        Flowable<CarHttpResponse<Map>> lookCheckDetail(Map map);

        Flowable<CarHttpResponse<Map>> lookReciverDetail(Map map);

        Flowable<MyHttpResponse<Map>> requestMethodDetail(Map map);

        String getTitle(Context context, String s);

        String[] getValues(Context context, String s);

        String[] getKeys(Context context);

        String getColor(Context context, String s);

        String getInputType(Context context, String s);

        String[] getReciverValues(Context context, String s);


        //测试数据
        String getTestData(Context context, String s);
    }

    interface CarDetailActivity extends CoreBaseView {
        void initRecycle(List list);

        void updateComplete(String msg,String isExist);

        void updataParts(int size);

    }

    abstract class DetailPresenter extends CoreBasePresenter<DetailModel, CarDetailActivity> {

    }
}
