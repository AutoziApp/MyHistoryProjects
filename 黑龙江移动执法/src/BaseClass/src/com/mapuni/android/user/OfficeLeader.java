package com.mapuni.android.user;

import java.util.List;

import com.mapuni.android.task.TaskInfo;
import com.mapuni.android.user.userInterface.UserArchivedInterface;
import com.mapuni.android.user.userInterface.UserAuditInterface;
import com.mapuni.android.user.userInterface.UserCreateInterface;
import com.mapuni.android.user.userInterface.UserExecuteInterface;

/**
 * 办公室领导类
 * 
 * @author Sahadev
 * 
 */
public class OfficeLeader extends BaseUser implements UserCreateInterface,UserAuditInterface, UserExecuteInterface, UserArchivedInterface {

	@Override
	public List<TaskInfo> getUserTaskInfo() {
		return null;
	}

	@Override
	public List<Object> getUserLeader() {
		// TODO Auto-generated method stub
		return null;
	}

}
