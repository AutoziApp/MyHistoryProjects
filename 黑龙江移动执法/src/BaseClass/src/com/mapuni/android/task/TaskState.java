package com.mapuni.android.task;

/**
 * 任务状态
 * 
 * @author Administrator
 * 
 */
public enum TaskState {
	/**
	 * 审核
	 */
	AUDIT, /**
	 * 提交
	 */
	SUBMIT, /**
	 * 修改
	 */
	MODIFY, /**
	 * 执行
	 */
	EXECUTE, /**
	 * 正在执行
	 */
	EXECUTING, /**
	 * 协助
	 */
	ASSISTED, /**
	 * 归档
	 */
	ARCHIVE
}
