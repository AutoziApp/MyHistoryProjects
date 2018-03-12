package com.mapuni.android.taskmanager;

public class TaskListDetail {
	/**
	 * 创建时间
	 * */		
	public String LeTaskCreatedTime;
	/**
	 *   办结期限
	 * */
	public String LeTaskTransactedTime;
	/**
	 *    执法任务名称
	 * */
	public String LeTaskName;
	
	/**
	 *    执法任务处理状态
	 * */
	public String LeTaskProcessStatus;
	
	/**
	 *    执法任务来源
	 * */
	public String LeTaskSource;
	/**
	 *    紧急程度
	 * */
	
	public String LeTaskUrgency;
	
	/**
	 *     发布人
	 * */
	public String LeTaskPublisher;
	/**
	 *    主题词
	 * */
	public String LeTaskSubjectTerm;
	/**
	 *    流程发起来源(1：单位内部，2：上级单位，3：下级单位)
	 * */
	public String OriginatingSource;
	/**
	 * 当前流程任务Id
	 * */
	public String CurrentFlowTaskId;
	/**
	 *上节流程任务Id
	 * */
	public String PrevFlowTaskId;
	/**
	 *上节办理人
	 * */
	public String PrevTransactor;
	/**
	 *上节办理意见
	 * */
	public String PrevTransactedComment;
	/**
	 *下节办理人
	 * */
	public String NextTransactor;
	/**
	 *任务code
	 * */
	public String LeTaskCode;
   /**
    * 任务id
    * */
	
  public String LeTaskId;
  /**
   * 任务类型
   * */
  public String  LeTaskType;
@Override
public String toString() {
	return "TaskListDetail [LeTaskCreatedTime=" + LeTaskCreatedTime
			+ ", LeTaskTransactedTime=" + LeTaskTransactedTime
			+ ", LeTaskName=" + LeTaskName + ", LeTaskProcessStatus="
			+ LeTaskProcessStatus + ", LeTaskSource=" + LeTaskSource
			+ ", LeTaskUrgency=" + LeTaskUrgency + ", LeTaskPublisher="
			+ LeTaskPublisher + ", LeTaskSubjectTerm=" + LeTaskSubjectTerm
			+ ", OriginatingSource=" + OriginatingSource
			+ ", CurrentFlowTaskId=" + CurrentFlowTaskId + ", PrevFlowTaskId="
			+ PrevFlowTaskId + ", PrevTransactor=" + PrevTransactor
			+ ", PrevTransactedComment=" + PrevTransactedComment
			+ ", NextTransactor=" + NextTransactor + ", LeTaskCode="
			+ LeTaskCode + ", LeTaskId=" + LeTaskId + ", LeTaskType="
			+ LeTaskType + "]";
}

  
  
  
  
	
	

}
