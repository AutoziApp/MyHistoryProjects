package com.jy.environment.model;

import java.io.Serializable;

public class UserGetUerInfoModel implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1384531611482669912L;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UserGetUerInfoModel [username="+username+",userpic=" + userpic + ", mail=" + mail
				+ ", gender=" + gender + ", area=" + area + ", nc=" + nc
				+ ", phone=" + phone + ", isEMailBind=" + isEmailBind
				+ ", isPhoneBind=" + isPhoneBind + "]";
	}

	private String username;
	private String userpic;
	private String mail;
	private String gender;
	private String area;
	private String nc;
	private String phone;
	private String isEmailBind;
	private String isPhoneBind;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIsEmailBind() {
		return isEmailBind;
	}

	public void setIsEmailBind(String isEMailBind) {
		this.isEmailBind = isEMailBind;
	}

	public String getIsPhoneBind() {
		return isPhoneBind;
	}

	public void setIsPhoneBind(String isPhoneBind) {
		this.isPhoneBind = isPhoneBind;
	}

	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNc() {
		return nc;
	}

	public void setNc(String nc) {
		this.nc = nc;
	}

}
