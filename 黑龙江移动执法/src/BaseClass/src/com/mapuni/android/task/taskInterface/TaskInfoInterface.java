package com.mapuni.android.task.taskInterface;

import java.util.HashMap;
import java.util.List;

/**
 * ������Ϣ�ӿ�
 * 
 * @author Sahadev
 * 
 */
public interface TaskInfoInterface {

	/**
	 * ��ȡ���������б�
	 * 
	 * @return ����
	 */
	public List<Object> getTaskNum();

	/**
	 * ��ȡ������ϸ��Ϣ�б�
	 * 
	 * @return ����
	 */
	public HashMap<String, Object> getTaskListInfo();

}
