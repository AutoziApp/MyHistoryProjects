package com.jy.environment.model;

import java.io.Serializable;

public class uploadRecordresult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String flag;
	private String msg;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public uploadRecordresult(String flag, String msg) {
		super();
		this.flag = flag;
		this.msg = msg;
	}
	
	

}
