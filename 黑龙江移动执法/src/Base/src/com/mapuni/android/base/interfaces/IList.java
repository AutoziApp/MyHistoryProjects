package com.mapuni.android.base.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

/**
 * FileName: IList.java
 * Description:列表业务类的接口
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:34:52
 */
public interface IList {
	/**
	 * Description:获取当前对象的主键值
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:34:41
	 */
	public String getListTitleText();
	
	/**
	 * Description:设置当前主键值
	 * @param currentIDValue
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:34:31
	 */
	public void setCurrentID(String currentIDValue);
	
	/**
	 * Description:获取样式列表信息
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:34:02
	 */
	public ArrayList<HashMap<String, Object>> getDataList();

	/**
	 * Description:			获取样式列表信息
	 * @param fliterHashMap	过滤条件
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:33:56
	 */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap);
	
	/**
	 * Description:获取样式列表信息
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:33:48
	 */
	public HashMap<String,Object> getStyleList(Context context) throws IOException;

	/**
	 * Description:获取查询条件
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:33:40
	 */
	public HashMap<String, Object> getFilter();
	
	/**
	 * Description:listivew滑动事件次数
	 * @param listScrollTimes
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:33:30
	 */
	public void setListScrolltimes(int listScrollTimes) ;
	
	/**
	 * Description:listivew滑动事件次数
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:33:15
	 */
	public int GetListScrolltimes() ;
	
}
