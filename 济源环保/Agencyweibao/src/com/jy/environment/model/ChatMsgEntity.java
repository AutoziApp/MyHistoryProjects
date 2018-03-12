package com.jy.environment.model;

public class ChatMsgEntity {
//	"id": "29",
//    "account_id_num": "1",
//    "account_id": "csgzh",
//    "title": "ceshilujing",
//    "author": "q",
//    "face_pic_url": "http://192.168.15.58:8080/epservice/image/facepic/20140508101546Tulips.jpg",
//    "summary": "ssss",
//    "content": "<p>kankancheshi jieguo</p>
	private String xiaoxi_id;
	private String account_id_num;
	private String account_id;
	private String author;
	private String title;
	private String face_pic_url;
	private String summary;
	private String content;
	private String create_time;
	private String  message;
	private boolean flag;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getXiaoxi_id() {
		return xiaoxi_id;
	}
	public void setXiaoxi_id(String xiaoxi_id) {
		this.xiaoxi_id = xiaoxi_id;
	}
	public String getAccount_id_num() {
		return account_id_num;
	}
	public void setAccount_id_num(String account_id_num) {
		this.account_id_num = account_id_num;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFace_pic_url() {
		return face_pic_url;
	}
	public void setFace_pic_url(String face_pic_url) {
		this.face_pic_url = face_pic_url;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ChatMsgEntity [xiaoxi_id=" + xiaoxi_id + ", account_id_num="
				+ account_id_num + ", account_id=" + account_id + ", author="
				+ author + ", title=" + title + ", face_pic_url="
				+ face_pic_url + ", summary=" + summary + ", content="
				+ content + "]";
	}
	
}
