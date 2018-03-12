package com.mapuni.android.user;

import java.util.List;

import com.mapuni.android.task.TaskInfo;

/**
 * @author Sahadev Shang
 * @version 1.0.0
 * @category 用户基本信息类,所有用户实体类必须继承该类
 */
public abstract class BaseUser {
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户ID
	 */
	private String userID;
	/**
	 * 用户角色
	 */
	private String role;
	/**
	 * 用户部门ID
	 */
	private String depID;
	/**
	 * 用户所属区域代码
	 */
	private String regionCode;
	/**
	 * 数据同步区域代码(约等同于{@link #regionCode},尚不明确)
	 */
	private String syncDataRegionCode;
	/**
	 * 用户职务
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
	 * 当前用户的管辖区域
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
	 * 当前用户所属地区
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
	 * 获取当前用户的任务信息
	 */
	public abstract List<TaskInfo> getUserTaskInfo();

}
