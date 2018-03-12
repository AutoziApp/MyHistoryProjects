package com.mapuni.android.user;

import java.util.List;

import com.mapuni.android.task.TaskInfo;

/**
 * @author Sahadev Shang
 * @version 1.0.0
 * @category �û�������Ϣ��,�����û�ʵ�������̳и���
 */
public abstract class BaseUser {
	/**
	 * �û�����
	 */
	private String userName;
	/**
	 * �û�ID
	 */
	private String userID;
	/**
	 * �û���ɫ
	 */
	private String role;
	/**
	 * �û�����ID
	 */
	private String depID;
	/**
	 * �û������������
	 */
	private String regionCode;
	/**
	 * ����ͬ���������(Լ��ͬ��{@link #regionCode},�в���ȷ)
	 */
	private String syncDataRegionCode;
	/**
	 * �û�ְ��
	 */
	private String jobTitle;

	protected BaseUser() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepID() {
		return depID;
	}

	public void setDepID(String depID) {
		this.depID = depID;
	}

	/**
	 * ��ǰ�û��Ĺ�Ͻ����
	 * 
	 * @return
	 */
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * ��ǰ�û���������
	 * 
	 * @return
	 */
	public String getSyncDataRegionCode() {
		return syncDataRegionCode;
	}

	public void setSyncDataRegionCode(String syncDataRegionCode) {
		this.syncDataRegionCode = syncDataRegionCode;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * ��ȡ��ǰ�û���������Ϣ
	 */
	public abstract List<TaskInfo> getUserTaskInfo();

}
