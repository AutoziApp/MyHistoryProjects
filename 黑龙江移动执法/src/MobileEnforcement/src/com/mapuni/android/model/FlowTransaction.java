package com.mapuni.android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author dell
 * 
 */
public class FlowTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String CurrentFlowTaskId;
	String CurrentProcessStatus;
	String FromChannel;
	ArrayList<AvailableActionListxx> AvailableActionList = new ArrayList<AvailableActionListxx>();
	String NextTransactorId;
	String TransactedTime;
	String TransactionType;
	String TransactorId;
	public String getCurrentFlowTaskId() {
		return CurrentFlowTaskId;
	}
	public void setCurrentFlowTaskId(String currentFlowTaskId) {
		CurrentFlowTaskId = currentFlowTaskId;
	}
	public String getCurrentProcessStatus() {
		return CurrentProcessStatus;
	}
	public void setCurrentProcessStatus(String currentProcessStatus) {
		CurrentProcessStatus = currentProcessStatus;
	}
	public String getFromChannel() {
		return FromChannel;
	}
	public void setFromChannel(String fromChannel) {
		FromChannel = fromChannel;
	}
	public ArrayList<AvailableActionListxx> getAvailableActionList() {
		return AvailableActionList;
	}
	public void setAvailableActionList(
			ArrayList<AvailableActionListxx> availableActionList) {
		AvailableActionList = availableActionList;
	}
	public String getNextTransactorId() {
		return NextTransactorId;
	}
	public void setNextTransactorId(String nextTransactorId) {
		NextTransactorId = nextTransactorId;
	}
	public String getTransactedTime() {
		return TransactedTime;
	}
	public void setTransactedTime(String transactedTime) {
		TransactedTime = transactedTime;
	}
	public String getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	public String getTransactorId() {
		return TransactorId;
	}
	public void setTransactorId(String transactorId) {
		TransactorId = transactorId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	

}
