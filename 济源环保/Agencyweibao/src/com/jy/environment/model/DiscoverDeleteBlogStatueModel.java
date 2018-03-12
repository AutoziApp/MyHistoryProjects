package com.jy.environment.model;

import java.io.Serializable;

/**
 * 删除微博model
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverDeleteBlogStatueModel implements Serializable{

    private static final long serialVersionUID = -3877022392044654757L;
	private boolean status = false;// 是否已删除
	public boolean isStatus() {
	    return status;
	}
	public void setStatus(boolean status) {
	    this.status = status;
	}

	
}
