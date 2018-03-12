package com.mapuni.android.task;

/**
 * 任务来源
 * 
 * @author Administrator
 * @deprecated 无法实时同步后台数据，暂时弃用
 */
public enum TaskComeFrom {
	/**
	 * 现场执法
	 */
	LAW_ENFORCEMENT, /**
	 * 一般任务流程
	 */
	TASK_FLOWS, /**
	 * 移交移送执法人员
	 */
	TRANSFER, /**
	 * 简易执法
	 */
	SIMPLE_TASK
}
