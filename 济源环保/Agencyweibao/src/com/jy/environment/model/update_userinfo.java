package com.jy.environment.model;

import java.io.Serializable;

public class update_userinfo implements Serializable{

	private static final long serialVersionUID = -5490760107739209939L;
	
	private boolean flag;
	private String content;
	private String userid;
	private String type;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
