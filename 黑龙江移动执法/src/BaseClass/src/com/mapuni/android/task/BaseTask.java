package com.mapuni.android.task;

import java.util.List;

import com.mapuni.android.attachment.Attachment;
import com.mapuni.android.enterprise.Enterprise;

/**
 * @author Sahadev
 * @category �������
 */
public class BaseTask {
	/**
	 * ������
	 */
	private String taskID;
	/**
	 * ��������
	 */
	private String taskName;
	/**
	 * ������ҵ
	 */
	private List<Enterprise> enterprises;
	/**
	 * �����û�
	 */
	private String createUser;
	/**
	 * ������Դ
	 */
	@SuppressWarnings("deprecation")
	private TaskComeFrom comeFrom;
	/**
	 * ����ע
	 */
	private String remarks;
	/**
	 * �����������
	 */
	private String regionCode;
	/**
	 * ���񸽼�
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
