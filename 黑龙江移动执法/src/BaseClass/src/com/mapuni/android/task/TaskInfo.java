package com.mapuni.android.task;

import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.Drawable;

/**
 * 任务管理类
 * 
 * @author Administrator
 * 
 */
public class TaskInfo {
	/**
	 * 任务图标
	 */
	private Drawable taskIcon;
	/**
	 * 任务状态
	 */
	private TaskState taskState;
	/**
	 * 任务数量
	 */
	private int taskNum;
	/**
	 * 关联任务
	 */
	private HashMap<TaskState, List<BaseTask>> relevanceTask;

	public Drawable getTaskIcon() {
		return taskIcon;
	}

	public void setTaskIcon(Drawable taskIcon) {
		this.taskIcon = taskIcon;
	}

	public TaskState getTaskState() {
		return taskState;
	}

	public void setTaskState(TaskState taskState) {
		this.taskState = taskState;
	}

	public int getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}

	public HashMap<TaskState, List<BaseTask>> getRelevanceTask() {
		return relevanceTask;
	}

	public void setRelevanceTask(HashMap<TaskState, List<BaseTask>> relevanceTask) {
		this.relevanceTask = relevanceTask;
	}
}
