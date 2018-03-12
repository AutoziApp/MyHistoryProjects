package com.jy.environment.model;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

public class Weib {

	private List<Pinglun> pinglun = new ArrayList<Pinglun>();
	private String id;
	private String username;
	private String userId;
	private String nc;
	private String icon;
	private String time;
	private String status;
	private String address;
	private String lon;
	private String lat;

	private String content;
	private List<String> small_pics;
	private List<String> big_pics;
	private String dianz;
	private List<String> dianz_users;
	private String zhuanfa;
	private String showlevel;
	private String env_state;
	private List<String> dianz_usersId;
	private String city;
	private List<String> dianz_usersNc;
	private String isAkey;
	private String pollutionType;
	private String isanonymous;
	private int imagesCount;
	private int commentCount;
	private int postsCount;
	
	public int getPostsCount() {
		return postsCount;
	}

	public void setPostsCount(int postsCount) {
		this.postsCount = postsCount;
	}

	public String getStatus() {
		return status;
	}

	public String getLon() {
	    return lon;
	}

	public void setLon(String lon) {
	    this.lon = lon;
	}

	public String getLat() {
	    return lat;
	}

	public void setLat(String lat) {
	    this.lat = lat;
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	private boolean isZanGuo = false;

	private boolean isHestory = false;

	public boolean isHestory() {
		return isHestory;
	}

	public void setHestory(boolean isHestory) {
		this.isHestory = isHestory;
	}

	public boolean isZanGuo() {
		return isZanGuo;
	}

	public void setZanGuo(boolean isZanGuo) {
		this.isZanGuo = isZanGuo;
	}

	public List<String> getDianz_usersId() {
		return dianz_usersId;
	}

	public void setDianz_usersId(List<String> dianz_usersId) {
		this.dianz_usersId = dianz_usersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Pinglun> getPinglun() {
		return pinglun;
	}

	public void setPinglun(List<Pinglun> pinglun) {
		this.pinglun = pinglun;
	}

	public void setBig_pics(List<String> big_pics) {
		this.big_pics = big_pics;
	}

	public List<String> getBig_pics() {
		return big_pics;
	}

	public List<String> getDianz_users() {
		return dianz_users;
	}

	public int getImagesCount() {
		return imagesCount;
	}

	public void setImagesCount(int imagesCount) {
		this.imagesCount = imagesCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDianz_users(List<String> dianz_users) {
		this.dianz_users = dianz_users;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNc() {
		return nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDianz() {
		return dianz;
	}

	public void setDianz(String dianz) {
		this.dianz = dianz;
	}

	public String getZhuanfa() {
		return zhuanfa;
	}

	public void setZhuanfa(String zhuanfa) {
		this.zhuanfa = zhuanfa;
	}

	public String getShowlevel() {
		return showlevel;
	}

	public void setShowlevel(String showlevel) {
		this.showlevel = showlevel;
	}

	public String getEnv_state() {
		return env_state;
	}

	public void setEnv_state(String env_state) {
		this.env_state = env_state;
	}

	public List<String> getSmall_pics() {
		return small_pics;
	}

	public void setSmall_pics(List<String> small_pics) {
		this.small_pics = small_pics;
	}

	public List<String> getDianz_usersNc() {
	    return dianz_usersNc;
	}

	public void setDianz_usersNc(List<String> dianz_usersNc) {
	    this.dianz_usersNc = dianz_usersNc;
	}

	
	public String getIsAkey() {
		return isAkey;
	}

	public void setIsAkey(String isAkey) {
		this.isAkey = isAkey;
	}

	public String getPollutionType() {
		return pollutionType;
	}

	public void setPollutionType(String pollutionType) {
		this.pollutionType = pollutionType;
	}

	public String getIsanonymous() {
		return isanonymous;
	}

	public void setIsanonymous(String isanonymous) {
		this.isanonymous = isanonymous;
	}

	public Weib(List<Pinglun> pinglun, String id, String username, String nc,
			String icon, String time, String content,String status,
			List<String> small_pics, List<String> big_pics, String dianz,
			List<String> dianz_users, String zhuanfa, String showlevel,
			String env_state, String userId, List<String> dianz_usersId,String address
			,String lon,String lat,List<String> dianz_usersNc,String isAkey,String pollutionType,String isanonymous,int imagesCount, int commentCount,int postsCount) {
		super();
		this.pinglun = pinglun;
		this.id = id;
		this.username = username;
		this.nc = nc;
		this.status = status;
		this.icon = icon;
		this.time = time;
		this.content = content;
		this.status = status;
		this.small_pics = small_pics;
		this.big_pics = big_pics;
		this.dianz = dianz;
		this.dianz_users = dianz_users;
		this.zhuanfa = zhuanfa;
		this.showlevel = showlevel;
		this.env_state = env_state;
		this.userId = userId;
		this.dianz_usersId = dianz_usersId;
		this.address = address;
		this.lon = lon;
		this.lat = lat;
		this.dianz_usersNc = dianz_usersNc;
		this.isAkey = isAkey;
		this.pollutionType = pollutionType;
		this.isanonymous = isanonymous;
		this.postsCount = postsCount;
	}

	@Override
	public String toString() {
		return "Weib [pinglun=" + pinglun + ", id=" + id + ", username="
				+ username + ", userId=" + userId + ", nc=" + nc + ", icon="
				+ icon + ", time=" + time + ", status=" + status + ", address="
				+ address + ", lon=" + lon + ", lat=" + lat + ", content="
				+ content + ", small_pics=" + small_pics + ", big_pics="
				+ big_pics + ", dianz=" + dianz + ", dianz_users="
				+ dianz_users + ", zhuanfa=" + zhuanfa + ", showlevel="
				+ showlevel + ", env_state=" + env_state + ", dianz_usersId="
				+ dianz_usersId + ", city=" + city + ", dianz_usersNc="
				+ dianz_usersNc + ", isAkey=" + isAkey + ", pollutionType="
				+ pollutionType + ", isanonymous=" + isanonymous
				+ ", imagesCount=" + imagesCount + ", commentCount="
				+ commentCount + ", postsCount=" + postsCount + ", isZanGuo="
				+ isZanGuo + ", isHestory=" + isHestory + "]";
	}



}
