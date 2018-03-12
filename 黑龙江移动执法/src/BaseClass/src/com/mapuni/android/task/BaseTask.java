package com.mapuni.android.task;

import java.util.List;

import com.mapuni.android.attachment.Attachment;
import com.mapuni.android.enterprise.Enterprise;

/**
 * @author Sahadev
 * @category 任务基类
 */
public class BaseTask {
	/**
	 * 任务编号
	 */
	private String taskID;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 关联企业
	 */
	private List<Enterprise> enterprises;
	/**
	 * 创建用户
	 */
	private String createUser;
	/**
	 * 任务来源
	 */
	@SuppressWarnings("deprecation")
	private TaskComeFrom comeFrom;
	/**
	 * 任务备注
	 */
	private String remarks;
	/**
	 * 任务区域代码
	 */
	private String regionCode;
	/**
	 * 任务附件
	 */
	private List<Attachment> attachments;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public List<Enterprise> getEnterprises() {
		return enterprises;
	}

	public void setEnterprises(List<Enterprise> enterprises) {
		this.enterprises = enterprises;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@SuppressWarnings("deprecation")
	public TaskComeFrom getComeFrom() {
		return comeFrom;
	}

	@SuppressWarnings("deprecation")
	public void setComeFrom(TaskComeFrom comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}
