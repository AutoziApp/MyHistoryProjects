package com.mapuni.android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author dell
 * 
 */
public class LawEnforcementTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String FlowTaskId;
	
	String FromChannel;
	String NextTransactorId;
	ArrayList<LeTaskEntLinkListxx> LeTaskEntLinkList = new ArrayList<LeTaskEntLinkListxx>();
	String PublishedTime;
	String Publisher;
	String PublisherId;
	String RegionCode;
	

	
	

	String Remark;
	String TaskCode;
	String TaskId;
	String TaskName;
	String TaskSource;
	String TaskType;
	String TransactedTime;
	String Urgency;
	
	public String getPublisher() {
		return Publisher;
	}
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	public String getFlowTaskId() {
		return FlowTaskId;
	}
	public void setFlowTaskId(String flowTaskId) {
		FlowTaskId = flowTaskId;
	}
	public String getFromChannel() {
		return FromChannel;
	}
	public void setFromChannel(String fromChannel) {
		FromChannel = fromChannel;
	}
	public String getNextTransactorId() {
		return NextTransactorId;
	}
	public void setNextTransactorId(String nextTransactorId) {
		NextTransactorId = nextTransactorId;
	}
	public ArrayList<LeTaskEntLinkListxx> getLeTaskEntLinkList() {
		return LeTaskEntLinkList;
	}
	public void setLeTaskEntLinkList(
			ArrayList<LeTaskEntLinkListxx> leTaskEntLinkList) {
		LeTaskEntLinkList = leTaskEntLinkList;
	}
	public String getPublishedTime() {
		return PublishedTime;
	}
	public void setPublishedTime(String publishedTime) {
		PublishedTime = publishedTime;
	}
	public String getPublisherId() {
		return PublisherId;
	}
	public void setPublisherId(String publisherId) {
		PublisherId = publisherId;
	}
	public String getRegionCode() {
		return RegionCode;
	}
	public void setRegionCode(String regionCode) {
		RegionCode = regionCode;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getTaskCode() {
		return TaskCode;
	}
	public void setTaskCode(String taskCode) {
		TaskCode = taskCode;
	}
	public String getTaskId() {
		return TaskId;
	}
	public void setTaskId(String taskId) {
		TaskId = taskId;
	}
	public String getTaskName() {
		return TaskName;
	}
	public void setTaskName(String taskName) {
		TaskName = taskName;
	}
	public String getTaskSource() {
		return TaskSource;
	}
	public void setTaskSource(String taskSource) {
		TaskSource = taskSource;
	}
	public String getTaskType() {
		return TaskType;
	}
	public void setTaskType(String taskType) {
		TaskType = taskType;
	}
	public String getTransactedTime() {
		return TransactedTime;
	}
	public void setTransactedTime(String transactedTime) {
		TransactedTime = transactedTime;
	}
	public String getUrgency() {
		return Urgency;
	}
	public void setUrgency(String urgency) {
		Urgency = urgency;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "LawEnforcementTask [FlowTaskId=" + FlowTaskId
				+ ", FromChannel=" + FromChannel + ", NextTransactorId="
				+ NextTransactorId + ", LeTaskEntLinkList=" + LeTaskEntLinkList
				+ ", PublishedTime=" + PublishedTime + ", Publisher="
				+ Publisher + ", PublisherId=" + PublisherId + ", RegionCode="
				+ RegionCode + ", Remark=" + Remark + ", TaskCode=" + TaskCode
				+ ", TaskId=" + TaskId + ", TaskName=" + TaskName
				+ ", TaskSource=" + TaskSource + ", TaskType=" + TaskType
				+ ", TransactedTime=" + TransactedTime + ", Urgency=" + Urgency
				+ "]";
	}
	
	
	
}
	
	
	
	
	
            
       
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	