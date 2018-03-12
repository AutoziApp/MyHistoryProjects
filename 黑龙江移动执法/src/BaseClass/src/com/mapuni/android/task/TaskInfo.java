package com.mapuni.android.task;

import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.Drawable;

/**
 * ���������
 * 
 * @author Administrator
 * 
 */
public class TaskInfo {
	/**
	 * ����ͼ��
	 */
	private Drawable taskIcon;
	/**
	 * ����״̬
	 */
	private TaskState taskState;
	/**
	 * ��������
	 */
	private int taskNum;
	/**
	 * ��������
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
