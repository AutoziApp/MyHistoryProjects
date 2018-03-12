package com.jy.environment.model;
/**
 * 删除微博评论model
 * 
 * @author baiyuchuan
 * 
 */
public class DeleteBlogCommentModel {

	private boolean isDelete = false;// 是否已删除成功
	private String commentPersonId; // 评论人ID
	private String commentId;// 评论ID
	private int beizhu; // 备注信息（默认显示在列表的位置）
	private int pos;//第几条评论
	public boolean isDelete() {
	    return isDelete;
	}
	public void setDelete(boolean isDelete) {
	    this.isDelete = isDelete;
	}
	public String getCommentPersonId() {
	    return commentPersonId;
	}
	public void setCommentPersonId(String commentPersonId) {
	    this.commentPersonId = commentPersonId;
	}
	public String getCommentId() {
	    return commentId;
	}
	public void setCommentId(String commentId) {
	    this.commentId = commentId;
	}
	public int getBeizhu() {
	    return beizhu;
	}
	public void setBeizhu(int beizhu) {
	    this.beizhu = beizhu;
	}
	public int getPos() {
	    return pos;
	}
	public void setPos(int pos) {
	    this.pos = pos;
	}
	public DeleteBlogCommentModel(boolean isDelete, String commentPersonId,
		String commentId, int beizhu, int pos) {
	    super();
	    this.isDelete = isDelete;
	    this.commentPersonId = commentPersonId;
	    this.commentId = commentId;
	    this.beizhu = beizhu;
	    this.pos = pos;
	}
	

}
