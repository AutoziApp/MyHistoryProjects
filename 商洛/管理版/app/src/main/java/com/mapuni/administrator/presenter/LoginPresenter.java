package com.mapuni.administrator.presenter;

import android.os.Handler;

import com.mapuni.administrator.bean.LoginResultBean;
import com.mapuni.administrator.iview.ILoginView;
import com.mapuni.administrator.model.LoginModel;


/**
 * Created by Administrator on 2017/8/14.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView mILoginView;
    private final LoginModel mLoginModel;

    public LoginPresenter(ILoginView iLoginView) {
        this.mILoginView = iLoginView;
        mLoginModel = new LoginModel(this);
    }

    @Override
    public void login(final String log, final String lat) {
//        mILoginView.showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoginModel.login(mILoginView.getUserName(), mILoginView.getPassWord(),log,lat);
            }
        },1500);
    }

    @Override
    public void onError(String errMsg) {
        mILoginView.hideProgressDialog();
        mILoginView.onError(errMsg);
    }

    @Override
    public void onSuccess(LoginResultBean bean) {
        mILoginView.hideProgressDialog();
        mILoginView.onSuccess(bean);
    }


}
