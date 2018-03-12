package com.jy.environment.model;

import java.io.Serializable;

/**
 * 删除微博model
 * 
 * @author baiyuchuan
 * 
 */
public class DeleteBlogModel implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = -3877022392044654757L;
	private String blogId;// id
	private String userId;//用户名
	private boolean isDelete = false;// 是否已删除
	private String beizhu; // 备注信息
	public String getBlogId() {
	    return blogId;
	}
	public void setBlogId(String blogId) {
	    this.blogId = blogId;
	}
	public String getUserId() {
	    return userId;
	}
	public void setUserId(String userId) {
	    this.userId = userId;
	}
	public boolean isDelete() {
	    return isDelete;
	}
	public void setDelete(boolean isDelete) {
	    this.isDelete = isDelete;
	}
	public String getBeizhu() {
	    return beizhu;
	}
	public void setBeizhu(String beizhu) {
	    this.beizhu = beizhu;
	}
	public DeleteBlogModel(String blogId, String userId, boolean isDelete,
		String beizhu) {
	    super();
	    this.blogId = blogId;
	    this.userId = userId;
	    this.isDelete = isDelete;
	    this.beizhu = beizhu;
	}

	
}
