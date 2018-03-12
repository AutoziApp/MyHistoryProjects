package com.mapuni.android.user;

import java.util.List;

import com.mapuni.android.task.TaskInfo;
import com.mapuni.android.user.userInterface.UserAuditInterface;
import com.mapuni.android.user.userInterface.UserCreateInterface;
import com.mapuni.android.user.userInterface.UserExecuteInterface;

/**
 * 执行科室领导类
 * 
 * @author Sahadev
 * 
 */
public class ExecuteLeader extends BaseUser implements UserCreateInterface, UserAuditInterface, UserExecuteInterface {

	@Override
	public List<Object> getUserLeader() {
		return null;
	}

	@Override
	public List<TaskInfo> getUserTaskInfo() {
		return null;
	}
}
