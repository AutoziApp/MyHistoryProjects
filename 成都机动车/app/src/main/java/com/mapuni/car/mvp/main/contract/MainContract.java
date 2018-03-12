package com.mapuni.car.mvp.main.contract;

import android.app.Activity;
import android.content.Context;

import com.mapuni.car.mvp.main.model.ConfigBean;
import com.mapuni.core.base.CoreBaseModel;
import com.mapuni.core.base.CoreBasePresenter;
import com.mapuni.core.base.CoreBaseView;
import com.mapuni.core.net.response.MyHttpResponse;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by lybin on 2017/6/27.
 */

public interface MainContract {
    interface MainModel extends CoreBaseModel{
        String[] getTabs(Context context);
        Flowable<MyHttpResponse<ConfigBean>> getConfig(Map map);
    }
    interface MainView extends CoreBaseView{
        void showTabList(String[] mTabs);
        Activity getParentActivity();
    }
    abstract  class MainPresenter extends CoreBasePresenter<MainModel,MainView>{
        public abstract void getTabList();
    }
}
