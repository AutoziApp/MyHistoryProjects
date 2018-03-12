package com.jy.environment.model;

/**
 * 登录结果
 * @author baiyuchuan
 *
 */
public class LoginModel{
	private Boolean flag;
	public LoginModel(Boolean flag, String userId, String userName,
			String niCheng, String userPic, String emailBind, String email) {
		super();
		this.flag = flag;
		this.userId = userId;
		this.userName = userName;
		this.niCheng = niCheng;
		this.userPic = userPic;
		this.emailBind = emailBind;
		this.email = email;
	}

	private String userId;
	private String userName;
	private String niCheng;
	private String userPic;
	private String emailBind;
	private String email;

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNiCheng() {
		return niCheng;
	}

	public void setNiCheng(String niCheng) {
		this.niCheng = niCheng;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getEmailBind() {
		return emailBind;
	}

	public void setEmailBind(String emailBind) {
		this.emailBind = emailBind;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "LoginModel [flag=" + flag + ", userId=" + userId
				+ ", userName=" + userName + ", niCheng=" + niCheng
				+ ", userPic=" + userPic + ", emailBind=" + emailBind
				+ ", email=" + email + "]";
	}

}
