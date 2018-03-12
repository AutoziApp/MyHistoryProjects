package com.mapuni.android.user.userInterface;

import java.util.List;

/**
 * @author Sahadev
 * @category 任务创建接口
 */
public interface UserCreateInterface {
	/**
	 * 获取该用户的审核领导
	 * 
	 * @return 返回领导类集合
	 */
	public List<Object> getUserLeader();
}
