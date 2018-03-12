package com.jy.environment.model;
//"id": "1",
//"user_id": "123",
//"name": "测试公众号",
//"fuction": "一般",
//"user_type": "普通",
//"public_photo": ""
//订阅号字段
public class SearchServiceItem {
	private String id;
	private String user_id;
	private String name;
	private String fuction;
	private String user_type;
	private String public_photo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFuction() {
		return fuction;
	}
	public void setFuction(String fuction) {
		this.fuction = fuction;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getPublic_photo() {
		return public_photo;
	}
	public void setPublic_photo(String public_photo) {
		this.public_photo = public_photo;
	}
	@Override
	public String toString() {
		return "SearchServiceItem [id=" + id + ", user_id=" + user_id + ", name="
				+ name + ", fuction=" + fuction + ", user_type=" + user_type
				+ ", public_photo=" + public_photo + "]";
	}

}
