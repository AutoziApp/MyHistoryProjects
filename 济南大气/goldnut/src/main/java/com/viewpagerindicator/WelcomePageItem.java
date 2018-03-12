package com.viewpagerindicator;

public class WelcomePageItem {

	private String imgurl;

	public String title;
	
	
	public WelcomePageItem(String imgurl) {
		this.imgurl = imgurl;
	}

	public WelcomePageItem(String imgurl,String t) {
		this.imgurl = imgurl;
		this.title = t;
	}
	
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	
	
	

	
}
