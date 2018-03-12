package com.jy.environment.model;

public class Pinglun {

	private String commentId;
	private String user;
	private String content;
	private String commentPersonId;
	private String nc;

	public String getNc() {
	    return nc;
	}

	public void setNc(String nc) {
	    this.nc = nc;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentPersonId() {
		return commentPersonId;
	}

	public void setCommentPersonId(String commentPersonId) {
		this.commentPersonId = commentPersonId;
	}

	public Pinglun(String nc,String commentId, String user, String content,
			String commentPersonId) {
		super();
		this.nc = nc;
		this.commentId = commentId;
		this.user = user;
		this.content = content;
		this.commentPersonId = commentPersonId;
	}

	@Override
	public String toString() {
		return "Pinglun [commentId=" + commentId + ", user=" + user
				+ ", content=" + content + ", commentPersonId="
				+ commentPersonId + "]";
	}

	public Pinglun() {
		super();
		// TODO Auto-generated constructor stub
	}

}
