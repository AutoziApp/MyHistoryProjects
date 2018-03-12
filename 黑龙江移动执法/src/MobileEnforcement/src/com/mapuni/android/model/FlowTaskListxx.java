package com.mapuni.android.model;

import java.io.Serializable;

/**
 * 
 * 
 * @author dell
 * 
 */
public class FlowTaskListxx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String Assistant;
	String AssistantTag;
	String Comment;
	String CommentTag;
	
	String FlowTaskId;
	String MainStepId;
	String NodeName;
	String NodeStatus;
	String TransactedTime;
	String TransactedTimeTag;
	String Transactor;
	
	String TransactorPosition;
	String windowWidth;
	public String getAssistant() {
		return Assistant;
	}
	public void setAssistant(String assistant) {
		Assistant = assistant;
	}
	public String getAssistantTag() {
		return AssistantTag;
	}
	public void setAssistantTag(String assistantTag) {
		AssistantTag = assistantTag;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public String getCommentTag() {
		return CommentTag;
	}
	public void setCommentTag(String commentTag) {
		CommentTag = commentTag;
	}
	public String getFlowTaskId() {
		return FlowTaskId;
	}
	public void setFlowTaskId(String flowTaskId) {
		FlowTaskId = flowTaskId;
	}
	public String getMainStepId() {
		return MainStepId;
	}
	public void setMainStepId(String mainStepId) {
		MainStepId = mainStepId;
	}
	public String getNodeName() {
		return NodeName;
	}
	public void setNodeName(String nodeName) {
		NodeName = nodeName;
	}
	public String getNodeStatus() {
		return NodeStatus;
	}
	public void setNodeStatus(String nodeStatus) {
		NodeStatus = nodeStatus;
	}
	public String getTransactedTime() {
		return TransactedTime;
	}
	public void setTransactedTime(String transactedTime) {
		TransactedTime = transactedTime;
	}
	public String getTransactedTimeTag() {
		return TransactedTimeTag;
	}
	public void setTransactedTimeTag(String transactedTimeTag) {
		TransactedTimeTag = transactedTimeTag;
	}
	public String getTransactor() {
		return Transactor;
	}
	public void setTransactor(String transactor) {
		Transactor = transactor;
	}
	public String getTransactorPosition() {
		return TransactorPosition;
	}
	public void setTransactorPosition(String transactorPosition) {
		TransactorPosition = transactorPosition;
	}
	public String getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(String windowWidth) {
		this.windowWidth = windowWidth;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
