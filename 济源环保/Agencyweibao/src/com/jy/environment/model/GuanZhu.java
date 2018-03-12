package com.jy.environment.model;

public class GuanZhu {
	private boolean flag;
	private String message;
	private String msg;
	private boolean status;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "GuanZhu [flag=" + flag + ", message=" + message + ", msg="
				+ msg + ", status=" + status + "]";
	}
	
}
