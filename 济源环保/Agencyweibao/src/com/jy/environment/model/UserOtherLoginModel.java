package com.jy.environment.model;

import java.io.Serializable;

public class UserOtherLoginModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 209275995806226119L;

    private boolean flag;
    private String icon_url;
    private String userid;
    private String username;
    private String nicheng;
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getIcon_url() {
        return icon_url;
    }
    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
	public String getNicheng() {
		return nicheng;
	}
	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}
    
}
