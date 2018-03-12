package com.mapuni.administrator.presenter;

import com.mapuni.administrator.bean.LoginResultBean;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface ILoginPresenter {
    void login(String log, String lat);
    void onError(String errMsg);
    void onSuccess(LoginResultBean bean);

    
    
}
