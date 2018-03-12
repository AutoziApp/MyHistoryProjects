package com.mapuni.core.rxjava;

import android.text.TextUtils;

import com.mapuni.core.base.CoreBaseView;
import com.mapuni.core.net.exception.ApiException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * Created by codeest on 2017/2/23.
 */

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private CoreBaseView mView;
    private String mErrorMsg;
    private boolean isShowErrorState = true;

    protected CommonSubscriber(CoreBaseView view){
        this.mView = view;
    }

    protected CommonSubscriber(CoreBaseView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    protected CommonSubscriber(CoreBaseView view, boolean isShowErrorState){
        this.mView = view;
        this.isShowErrorState = isShowErrorState;
    }

    protected CommonSubscriber(CoreBaseView view, String errorMsg, boolean isShowErrorState){
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showError(mErrorMsg);
        }else if (e instanceof ApiException) {
            if(!TextUtils.isEmpty(e.getMessage())) {
                mView.showError(new String(e.getMessage()));
            }
        } else if (e instanceof HttpException) {
            mView.showError("网络请求失败");
        } else {
            mView.showError("登录出错");
        }
//        if (isShowErrorState) {
//            mView.showError(new String(e.getMessage()));
//        }
    }
}
