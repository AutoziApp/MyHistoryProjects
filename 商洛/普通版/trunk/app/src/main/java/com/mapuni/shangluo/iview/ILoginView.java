package com.mapuni.shangluo.iview;

import com.mapuni.shangluo.bean.LoginResultBean;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface ILoginView {


    String getUserName();

    String getPassWord();

    void showProgressDialog();

    void hideProgressDialog();

    void onError(String errMsg);

    void onSuccess(LoginResultBean bean);
}
