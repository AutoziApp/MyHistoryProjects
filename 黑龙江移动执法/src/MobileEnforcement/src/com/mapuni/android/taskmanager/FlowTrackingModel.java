package com.mapuni.android.taskmanager;

import java.util.ArrayList;

public class FlowTrackingModel {
	/**
	 * 
	 * ִ������ʵ��Id
	 * */ 
	public String FlowInstanceId ;
	
	/**
	 * ִ���������
	 * */
	public String RegionCode ;
	
	/**
	 *ִ�е�λ����
	 * */
	public String TransactoinUnit;
	
	/**
	 *��ʾ˳��
	 * */
	
	public int DisplayOrder;
	
	
	
	/**
	 *����������ϸ�б�
	 * */
	
	public ArrayList<FlowTaskModel> FlowTaskList ;



	@Override
	public String toString() {
		return "FlowTrackingModel [FlowInstanceId=" + FlowInstanceId
				+ ", RegionCode=" + RegionCode + ", TransactoinUnit="
				+ TransactoinUnit + ", DisplayOrder=" + DisplayOrder
				+ ", FlowTaskList=" + FlowTaskList + "]";
	}
	
	
	
}
