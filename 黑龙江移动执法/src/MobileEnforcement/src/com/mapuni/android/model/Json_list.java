package com.mapuni.android.model;

import java.util.ArrayList;

public class Json_list {
	private static final long serialVersionUID = 3583721472904066180L;
	public ArrayList<FlowStepListxx> getFlowStepList() {
		return FlowStepList;
	}
	public void setFlowStepList(ArrayList<FlowStepListxx> flowStepList) {
		FlowStepList = flowStepList;
	}
	public ArrayList<FlowTaskListxx> getFlowTaskList() {
		return FlowTaskList;
	}
	public void setFlowTaskList(ArrayList<FlowTaskListxx> flowTaskList) {
		FlowTaskList = flowTaskList;
	}
	public FlowTransaction getFlowTransaction() {
		return FlowTransaction;
	}
	public void setFlowTransaction(FlowTransaction flowTransaction) {
		FlowTransaction = flowTransaction;
	}
	public LawEnforcementTask getLawEnforcementTask() {
		return LawEnforcementTask;
	}
	public void setLawEnforcementTask(LawEnforcementTask lawEnforcementTask) {
		LawEnforcementTask = lawEnforcementTask;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private ArrayList<FlowStepListxx> FlowStepList = new ArrayList<FlowStepListxx>();
	private ArrayList<FlowTaskListxx> FlowTaskList = new ArrayList<FlowTaskListxx>();
	private FlowTransaction FlowTransaction;
	private LawEnforcementTask LawEnforcementTask;
	private String Message;
	private String Result;
	
	
}
