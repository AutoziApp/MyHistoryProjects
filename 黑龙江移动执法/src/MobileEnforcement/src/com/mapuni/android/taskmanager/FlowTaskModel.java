package com.mapuni.android.taskmanager;

public class FlowTaskModel {

	/**
	 * 
	 * 节点名称
	 */

	public String NodeName;

	/**
	 * 
	 * 主要步骤
	 */

	public int MainStepId;

	/**
	 * 
	 * 节点状态
	 */

	public int NodeStatus;

	/**
	 * 
	 * 任务id
	 */

	public long FlowTaskId;

	/**
	 * 
	 * 办理人
	 */

	public String Transactor;

	/**
	 * 
	 * 办理人职位
	 */

	public String TransactorPosition;

	/**
	 * 
	 * 协办人
	 */

	public String Assistant;

	/**
	 * 
	 * 协办人标签
	 */

	public String AssistantTag;

	/**
	 * 
	 * 处理结果
	 */

	public String NodeResult;

	/**
	 * 
	 * 流程节点结果标签
	 */

	public String NodeResultTag;

	/**
	 * 
	 * 办理意见
	 */

	public String Comment;

	/**
	 * 
	 * 办理意见标签
	 */

	public String CommentTag;

	/**
	 * 
	 * 办理时间
	 */

	public String TransactedTime;

	/**
	 * 
	 * 办理时间标签
	 */

	public String TransactedTimeTag;
	
	/**
	 * 
	 * */
public String windowWidth;

	@Override
	public String toString() {
		return "FlowTaskModel [NodeName=" + NodeName + ", MainStepId="
				+ MainStepId + ", NodeStatus=" + NodeStatus + ", FlowTaskId="
				+ FlowTaskId + ", Transactor=" + Transactor
				+ ", TransactorPosition=" + TransactorPosition + ", Assistant="
				+ Assistant + ", AssistantTag=" + AssistantTag
				+ ", NodeResult=" + NodeResult + ", NodeResultTag="
				+ NodeResultTag + ", Comment=" + Comment + ", CommentTag="
				+ CommentTag + ", TransactedTime=" + TransactedTime
				+ ", TransactedTimeTag=" + TransactedTimeTag + ", windowWidth="
				+ windowWidth + "]";
	}




	
	
	
	

}
