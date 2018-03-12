package com.mapuni.android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author dell
 * 
 */
public class LeTaskEntLinkListxx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String EntCode;
	String EntName;
	String Id;
	
	String PageIndex;
	String PageSize;
	String Remark;
	String Status;
	String TaskId;
	String UpdatedTime;
	public String getEntCode() {
		return EntCode;
	}
	public void setEntCode(String entCode) {
		EntCode = entCode;
	}
	public String getEntName() {
		return EntName;
	}
	public void setEntName(String entName) {
		EntName = entName;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(String pageIndex) {
		PageIndex = pageIndex;
	}
	public String getPageSize() {
		return PageSize;
	}
	public void setPageSize(String pageSize) {
		PageSize = pageSize;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getTaskId() {
		return TaskId;
	}
	public void setTaskId(String taskId) {
		TaskId = taskId;
	}
	public String getUpdatedTime() {
		return UpdatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
