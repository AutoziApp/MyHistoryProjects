package com.jy.environment.model;

import java.io.Serializable;

public class News implements Serializable{
/**
     * 
     */
    private static final long serialVersionUID = 4697001833329095930L;
	//环境说消息数目
	private boolean flag;
	private int count;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "News [flag=" + flag + ", count=" + count + "]";
	}
	
}
