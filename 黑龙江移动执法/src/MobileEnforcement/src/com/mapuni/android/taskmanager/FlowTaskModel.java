package com.mapuni.android.taskmanager;

public class FlowTaskModel {

	/**
	 * 
	 * �ڵ�����
	 */

	public String NodeName;

	/**
	 * 
	 * ��Ҫ����
	 */

	public int MainStepId;

	/**
	 * 
	 * �ڵ�״̬
	 */

	public int NodeStatus;

	/**
	 * 
	 * ����id
	 */

	public long FlowTaskId;

	/**
	 * 
	 * ������
	 */

	public String Transactor;

	/**
	 * 
	 * ������ְλ
	 */

	public String TransactorPosition;

	/**
	 * 
	 * Э����
	 */

	public String Assistant;

	/**
	 * 
	 * Э���˱�ǩ
	 */

	public String AssistantTag;

	/**
	 * 
	 * ������
	 */

	public String NodeResult;

	/**
	 * 
	 * ���̽ڵ�����ǩ
	 */

	public String NodeResultTag;

	/**
	 * 
	 * �������
	 */

	public String Comment;

	/**
	 * 
	 * ���������ǩ
	 */

	public String CommentTag;

	/**
	 * 
	 * ����ʱ��
	 */

	public String TransactedTime;

	/**
	 * 
	 * ����ʱ���ǩ
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
