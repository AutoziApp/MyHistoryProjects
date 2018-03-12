package com.jy.environment.model;

import java.io.Serializable;

public class DiscoverBlogUpLoadResult implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 9024446633939685836L;

    private String postid;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

	@Override
	public String toString() {
		return "DiscoverBlogUpLoadResult [postid=" + postid + "]";
	}
    
}
