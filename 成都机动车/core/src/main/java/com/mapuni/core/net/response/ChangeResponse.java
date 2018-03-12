package com.mapuni.core.net.response;

import android.text.TextUtils;

/**
 * Created by codeest on 16/10/10.
 */

public class ChangeResponse<T> {

//    private int flag;
//    private String message;
    private String err;
    private String isExist;
    private String carInfo;
    private String alertinfo;
    private T data;

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getAlertinfo() {
        return alertinfo;
    }

    public void setAlertinfo(String alertinfo) {
        this.alertinfo = alertinfo;
    }

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
        return data;
    }

    public void setResult(T data) {
        this.data = data;
    }


}
