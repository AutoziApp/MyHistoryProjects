package com.mapuni.mobileenvironment.bean;
/**
 * 登录用户
 * Created by k on 2015/11/26.
 */
public class LogonUser {
    /** 用户名 */
    private String userName;
    /** 用户编码 */
    private String userCode;
    /** 区县名字 */
    private String countyName;
    /** 区划码 */
    private String countyCode;
    /** 手机号 */
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public LogonUser() {
    }

    public LogonUser(String userName, String userCode, String countyName, String countyCode) {
        this.userName = userName;
        this.userCode = userCode;
        this.countyName = countyName;
        this.countyCode = countyCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }
}
