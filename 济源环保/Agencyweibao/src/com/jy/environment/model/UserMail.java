package com.jy.environment.model;

import java.io.Serializable;
/**
 * 邮件是否绑定成功
 * @author xuhuaiguang 
 *
 */

public class UserMail implements Serializable {
	private String flag;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	private String message;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "UserMail [flag=" + flag + ", message=" + message + "]";
	}

}
