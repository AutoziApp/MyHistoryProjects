package com.mapuni.core.net.response;

import android.text.TextUtils;

/**
 * Created by codeest on 16/10/10.
 */

public class CarHttpResponse<T> {

//    private int flag;
//    private String message;
    private String err;
    private String isExist;
    private T carInfo;

    public void setErr(String err) {
        if(TextUtils.isEmpty(err)){
            err="网络异常";
        }
        this.err = err;
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

    public String getErr() {
        return err;
    }

    public void setError(String error) {
        this.err = error;
    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public T getResult() {
        return carInfo;
    }

    public void setResult(T data) {
        this.carInfo = data;
    }


}
