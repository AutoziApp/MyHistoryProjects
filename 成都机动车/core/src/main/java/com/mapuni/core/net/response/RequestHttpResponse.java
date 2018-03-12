package com.mapuni.core.net.response;

import android.text.TextUtils;

/**
 * Created by suncm on 2017/12/15.
 */

public class RequestHttpResponse<T> {

    //    private int flag;
//    private String message;
    private String errInfo;
    private String isExist;
    private T checkInfo;

    public void setErrInfo(String errInfo) {
        if(TextUtils.isEmpty(errInfo)){
            errInfo="网络异常";
        }
        this.errInfo = errInfo;
    }

    public String getIsExist() {
        return isExist;
    }

    public void setIsExist(String isExist) {
        this.isExist = isExist;
    }

//    public int getFlag() {
//        return flag;
//    }
//
//    public void setFlag(int flag) {
//        this.flag = flag;
//    }

    public String getErrInfo() {
        return errInfo;
    }

//    public void setError(String error) {
//        this.err = error;
//    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public T getResult() {
        return checkInfo;
    }

    public void setResult(T data) {
        this.checkInfo = data;
    }


}

