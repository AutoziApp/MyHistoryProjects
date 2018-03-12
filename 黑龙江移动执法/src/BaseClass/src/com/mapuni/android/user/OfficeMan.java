package com.mapuni.android.user;

import java.util.List;

import com.mapuni.android.task.TaskInfo;
import com.mapuni.android.user.userInterface.UserArchivedInterface;
import com.mapuni.android.user.userInterface.UserCreateInterface;
import com.mapuni.android.user.userInterface.UserExecuteInterface;

/**
 * 办公室人员类
 * 
 * @author Sahadev
 * 
 */
public class OfficeMan extends BaseUser implements UserCreateInterface,UserExecuteInterface, UserArchivedInterface {

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
