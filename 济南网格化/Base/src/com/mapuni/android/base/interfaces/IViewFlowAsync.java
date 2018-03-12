package com.mapuni.android.base.interfaces;

import android.view.View;

/**
 * FileName: IViewFlowAsync.java
 * Description:滑动界面的接口
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:53:24
 */
public interface IViewFlowAsync {
	/**
	 * 在开启线程中调用用于耗时操作
	 */
	public Object callBackground(Object arg);
	
	/**
	 * Description:	在主线程调用，用来更新UI
	 * @param position
	 * @param result
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:52:03
	 */
	public View callOnUI(int position, Object result);
	
	
}
