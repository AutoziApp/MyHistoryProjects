package com.jy.environment.model;

import java.io.Serializable;

public class UserRegisterModel implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5490760107739209939L;
    private boolean flag;
    private String msg;
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    

}
