package com.mapuni.android.taskmanager;

import java.util.ArrayList;

public class FlowTrackingModel {
	/**
	 * 
	 * 执行流程实例Id
	 * */ 
	public String FlowInstanceId ;
	
	/**
	 * 执行区域编码
	 * */
	public String RegionCode ;
	
	/**
	 *执行单位名称
	 * */
	public String TransactoinUnit;
	
	/**
	 *显示顺序
	 * */
	
	public int DisplayOrder;
	
	
	
	/**
	 *流程任务明细列表
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
