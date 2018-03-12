package com.mapuni.android.model;

import java.io.Serializable;

/**
 * 
 * 
 * @author dell
 * 
 */
public class FlowStepListxx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String ExecutingState;
	String StepId;
	String StepName;
	String StepOrder;
	public String getExecutingState() {
		return ExecutingState;
	}
	public void setExecutingState(String executingState) {
		ExecutingState = executingState;
	}
	public String getStepId() {
		return StepId;
	}
	public void setStepId(String stepId) {
		StepId = stepId;
	}
	public String getStepName() {
		return StepName;
	}
	public void setStepName(String stepName) {
		StepName = stepName;
	}
	public String getStepOrder() {
		return StepOrder;
	}
	public void setStepOrder(String stepOrder) {
		StepOrder = stepOrder;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
