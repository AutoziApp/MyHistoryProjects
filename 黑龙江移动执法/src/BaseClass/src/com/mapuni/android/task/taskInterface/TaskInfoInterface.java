package com.mapuni.android.task.taskInterface;

import java.util.HashMap;
import java.util.List;

/**
 * 任务信息接口
 * 
 * @author Sahadev
 * 
 */
public interface TaskInfoInterface {

	/**
	 * 获取任务数量列表
	 * 
	 * @return 待定
	 */
	public List<Object> getTaskNum();

	/**
	 * 获取任务详细信息列表
	 * 
	 * @return 待定
	 */
	public HashMap<String, Object> getTaskListInfo();

}
