package com.mapuni.android.taskmanager;

public class TaskListDetail {
	/**
	 * ����ʱ��
	 * */		
	public String LeTaskCreatedTime;
	/**
	 *   �������
	 * */
	public String LeTaskTransactedTime;
	/**
	 *    ִ����������
	 * */
	public String LeTaskName;
	
	/**
	 *    ִ��������״̬
	 * */
	public String LeTaskProcessStatus;
	
	/**
	 *    ִ��������Դ
	 * */
	public String LeTaskSource;
	/**
	 *    �����̶�
	 * */
	
	public String LeTaskUrgency;
	
	/**
	 *     ������
	 * */
	public String LeTaskPublisher;
	/**
	 *    �����
	 * */
	public String LeTaskSubjectTerm;
	/**
	 *    ���̷�����Դ(1����λ�ڲ���2���ϼ���λ��3���¼���λ)
	 * */
	public String OriginatingSource;
	/**
	 * ��ǰ��������Id
	 * */
	public String CurrentFlowTaskId;
	/**
	 *�Ͻ���������Id
	 * */
	public String PrevFlowTaskId;
	/**
	 *�Ͻڰ�����
	 * */
	public String PrevTransactor;
	/**
	 *�Ͻڰ������
	 * */
	public String PrevTransactedComment;
	/**
	 *�½ڰ�����
	 * */
	public String NextTransactor;
	/**
	 *����code
	 * */
	public String LeTaskCode;
   /**
    * ����id
    * */
	
  public String LeTaskId;
  /**
   * ��������
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
